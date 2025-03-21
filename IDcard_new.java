package cardgen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import DBC.DBConnection;
import WorkerRegistration_NewAndEdit.Age;
import beens.MyUtil;

public class IDcard_new extends ActionSupport implements ModelDriven<ALOApproveFormBean> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String sql = null;
	String sql1 = null;
	PreparedStatement ps1 = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	ResultSet rs3 = null;
	PreparedStatement ps2 = null;
	PreparedStatement ps3 = null;
	PreparedStatement ps4 = null;
	PreparedStatement ps5 = null;
	private List<ALOApproveFormBean> cardList = new ArrayList<ALOApproveFormBean>();
	 ALOApproveFormBean cardGenEntryBean = new ALOApproveFormBean();
	
	public List<ALOApproveFormBean> getCardList() {
		return cardList;
	}

	public void setCardList(List<ALOApproveFormBean> cardList) {
		this.cardList = cardList;
	}

	public ALOApproveFormBean getCardGenEntryBean() {
		return cardGenEntryBean;
	}

	public void setCardGenEntryBean(ALOApproveFormBean cardGenEntryBean) {
		this.cardGenEntryBean = cardGenEntryBean;
	}

	public String GenerateId(){
	//	System.out.println("Iam in action");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String user_id = (String) session.getAttribute("user_name");
		int trno = 0;
		//String paper = cardGenEntryBean.getPaper();
		session.setAttribute("paper", "2");
		String alocode = (String) session.getAttribute("alocode");
		String userid = "" + session.getAttribute("user_id");
		
		String[] cards = null;
		String idtype = "" + session.getAttribute("cardtype");
		if (idtype.equals("duplicate")) {
			String[] dupworkerid = new String[1];
			dupworkerid[0] = "" + session.getAttribute("dupworkerid");
			cards = dupworkerid;
		} else if (idtype.equals("renewal")) {
			String[] dupworkerid = new String[1];
			dupworkerid[0] = "" + session.getAttribute("worker_id");
			cards = dupworkerid;
		} else {
			cards = cardGenEntryBean.getSelectCards();
		}
		//String aadaharno = null;
		String basicsql = null;
		String approvstatus = null;
		String wrregno = null;
		try {
			con = DBConnection.getConnection();
			for (int i = 0; i < cards.length; i++) {
				if (!(alocode == null)) {
					basicsql = "SELECT concat('XXXXXXXX',substring(worker_aadhaar_no,9) ) worker_aadhaar_no, worker_reg_no, alo_code, worker_id,initcap(worker_name) worker_name,initcap(relation_name) relation_name, COALESCE (trade_name,'NA') trade_name, concat('XXXXXX',substring(mobile_no,7) ) mobile_no, date_of_birth, reg_date,"
							+ "    COALESCE(gendername,'NA')gendername, jurisdiction, approval_status, created_by  FROM public.view_idcard_details  where  alo_code=? and  worker_id=? ";
					ps = con.prepareStatement(basicsql);
					ps.setString(1,alocode);
					ps.setString(2,cards[i].trim());
				} else {
					basicsql = "SELECT concat('XXXXXXXX',substring(worker_aadhaar_no,9) ) worker_aadhaar_no, worker_reg_no, alo_code, worker_id, worker_name,relation_name, COALESCE (trade_name,'NA') trade_name, concat('XXXXXX',substring(mobile_no,7) ) mobile_no, date_of_birth, reg_date, COALESCE(gendername,'NA')gendername, jurisdiction, approval_status, created_by  FROM public.view_idcard_details   where  created_by=? and  worker_id=?  and approval_status in ('Y','A','T') ";
					ps = con.prepareStatement(basicsql);
					ps.setString(1,userid);
					ps.setString(2,cards[i].trim());
				}
				
				rs = ps.executeQuery();
				while (rs.next()) {
					cardGenEntryBean = new ALOApproveFormBean();
					cardGenEntryBean.setProof(rs.getString("worker_aadhaar_no"));
					String alouser = MyUtil.getOptionValue("mst_alocode", "alocode", "user_id",alocode);
					cardGenEntryBean.setAlocode(alouser);
					cardGenEntryBean.setWorker_name(rs.getString("worker_name"));
					cardGenEntryBean.setFather_name(rs.getString("relation_name"));
					String workId = rs.getString("worker_id");
					session.setAttribute("worker_id", workId);
					wrregno = rs.getString("worker_reg_no");
					cardGenEntryBean.setRegno(rs.getString("worker_reg_no"));				
					cardGenEntryBean.setWorker_id(workId);
					cardGenEntryBean.setWorker_name(rs.getString("worker_name"));
					cardGenEntryBean.setGender(rs.getString("gendername"));
					cardGenEntryBean.setPhoneno(rs.getString("mobile_no"));
					cardGenEntryBean.setNature_emp_name(rs.getString("trade_name"));
					String wrdob = rs.getString("date_of_birth");
					String regdt = rs.getString("reg_date");
					cardGenEntryBean.setPhoneno(rs.getString("mobile_no"));
					if(wrdob==null){
						wrdob="NA";
					}else{
						wrdob=MyUtil.ChDate(wrdob);
					}
					if(regdt==null){
						regdt="NA";
					}else{
						regdt=MyUtil.ChDate(regdt);
					}
					cardGenEntryBean.setReg_date(regdt);
					cardGenEntryBean.setDate_of_birth(wrdob);
					String jurisdiction = rs.getString("jurisdiction");
					cardGenEntryBean.setOfficer_code(jurisdiction);	
				// present address details
				String presaddrsquery = "SELECT worker_aadhaar_no, worker_reg_no,alo_code,initcap(house_no) house_no,initcap(street_name)street_name,initcap(state_txt) state_txt, initcap( district_txt) district_txt,initcap( mandal_txt) mandal_txt,initcap( village_txt)village_txt, aadhaar_pincode FROM public.wr_address_details where address_type='P'  and worker_reg_no=? ";
				ps1 = con.prepareStatement(presaddrsquery);
				ps1.setString(1,MyUtil.filterBad(wrregno));
				rs1 = ps1.executeQuery();
				if (rs1.next()) {
						cardGenEntryBean.setPermenant_addr1(rs1.getString("house_no"));
						cardGenEntryBean.setPermenant_addr2(rs1.getString("street_name"));
						cardGenEntryBean.setPermenant_addr_district(rs1.getString("district_txt"));
						cardGenEntryBean.setPermenant_addr_mandal(rs1.getString("mandal_txt"));
						cardGenEntryBean.setPermenant_habcode(rs1.getString("village_txt"));
						cardGenEntryBean.setPermenant_addr_pincode(rs1.getString("aadhaar_pincode"));
					
				}	
					cardList.add(cardGenEntryBean);
			}
			}
request.setAttribute("cardList", cardList);
		} catch (Exception e) {
		} finally {
			if (rs1 != null) {
				try {
					rs1.close();
				} catch (Exception e) {
					 
				}
			}

			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					 
				}
			}

			if (ps1 != null) {
				try {
					ps1.close();
				} catch (Exception e) {
					 
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					 
				}
			}
			if (con != null) {
				try {
					DbUtils.closeQuietly(con);
				} catch (Exception e) {
					 
				}
			}
		}

		return "success";
	}

	   
	public ALOApproveFormBean getModel() {
		// TODO Auto-generated method stub
		return cardGenEntryBean;
	}
}
