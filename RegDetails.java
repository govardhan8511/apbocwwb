package Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import DBC.Validations_new;
import TrainingMaster.seedingbean;
import beens.MyUtil;
import beens.RegistrationBeen;

public class RegDetails extends ActionSupport implements ModelDriven<RegistrationBeen> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RegistrationBeen s = new RegistrationBeen();
	RegistrationBeen sb = new RegistrationBeen();
	List<RegistrationBeen> sblist = new ArrayList<RegistrationBeen>();

	public RegistrationBeen getS() {
		return s;
	}

	public void setS(RegistrationBeen s) {
		this.s = s;
	}

	public RegistrationBeen getSb() {
		return sb;
	}

	public void setSb(RegistrationBeen sb) {
		this.sb = sb;
	}

	public List<RegistrationBeen> getSblist() {
		return sblist;
	}

	public void setSblist(List<RegistrationBeen> sblist) {
		this.sblist = sblist;
	}

	public String GetValidRegDetails() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int j = 0;
		String aadhaarno = null;
		String regno = null;
		String dob = null;
		String regsql = "";
		String receipt_date = "";
		String valid_upto = null;
		String Msg = null;
		int age=0;
		String roleid=MyUtil.isBlank((String) session.getAttribute("user_role")).trim();
		String alo_code=MyUtil.isBlank((String) session.getAttribute("alocode")).trim();
		String filterreg_no = beens.MyUtil.filterBad(s.getReg_no()).trim();
		try {
			con = DBC.DBConnection.getConnection();
			if (filterreg_no.length() > 0) {
				Msg = Validations_new.Validate(filterreg_no, 25, 25, true);
				if (Msg != null) {
					request.setAttribute("i", "nodata");
					return SUCCESS;
				}
			}
			regsql = "select worker_aadhaar_no,worker_reg_no,a.alo_code,worker_name,relation_name,date_of_birth,coalesce(caste_name,'NA') caste_name,coalesce(mobile_no,'NA') mobile_no,reg_date receipt_date,'NA' subscription_years,rec_valid_upto valid_upto,trade_name,a.rec_status from wr_basic_details a left join caste_master  c on a.caste_code=c.numericcode left join trade_master d on d.numericcode=a.worker_trade_code where a.worker_reg_no=?";
			request.setAttribute("check_recdetails", "data");
			pstmt = con.prepareStatement(regsql);
			pstmt.setString(1, filterreg_no);
			rst = pstmt.executeQuery();
			if (rst.next()) {				
				sb = new RegistrationBeen();
				request.setAttribute("i", "ValidRegDetails");
				sb.setWorker_name(rst.getString("worker_name"));
				sb.setRelation_name(rst.getString("relation_name"));
				sb.setAlo_code(rst.getString("alo_code"));
				receipt_date = rst.getString("receipt_date");
				valid_upto = rst.getString("valid_upto");
				if (valid_upto == null) {
					valid_upto = "NA";
				} else {
					valid_upto = MyUtil.ChDate(valid_upto);
				}
				if (receipt_date == null) {
					receipt_date = "NA";
				} else {
					receipt_date = MyUtil.ChDate(receipt_date);
				}
				sb.setValid_upto(valid_upto);
				sb.setReceipt_date(receipt_date);
				dob = rst.getString("date_of_birth");
				if (dob != null) {
				 age=MyUtil.getAge(dob, MyUtil.gettodaysDate());
					dob = MyUtil.ChDate(dob);
				} else {
					dob = "NA";
				}
				sb.setDob(dob);
				sb.setReg_no(rst.getString("worker_reg_no"));
				aadhaarno = rst.getString("worker_aadhaar_no");
				if(aadhaarno==null){
					aadhaarno="NA";
				}else{
					if(aadhaarno.length()==12){
						aadhaarno="XXXXXXXX".concat(aadhaarno.substring(8, 12));
					}else{
						aadhaarno="Invalid";	
					}	
				}
				sb.setAadhar_no(aadhaarno);
				sb.setMobile_no(rst.getString("mobile_no"));
				sb.setCast_name(rst.getString("caste_name"));
				regno = rst.getString("worker_reg_no");
				sb.setTrade_name(rst.getString("trade_name"));
				sblist.add(sb);
				if(roleid.equalsIgnoreCase("5")){
					
				}else{
					if(!rst.getString("alo_code").equalsIgnoreCase(alo_code)){
						request.setAttribute("i", "not in this circle");
					}
					if(rst.getString("rec_status").equalsIgnoreCase("R")){
						request.setAttribute("i", "worker above 60");
					}else if(rst.getString("rec_status").equalsIgnoreCase("D")){
						request.setAttribute("i", "Worker Died");
					}else if(rst.getString("rec_status").equalsIgnoreCase("C")){
						request.setAttribute("i", "Pending with Admin");
					}else{
						if(age>60){
							request.setAttribute("i", "worker above 60 propose");	
						}
					}					
				}
			} else {
				request.setAttribute("i", "nodata");
			}
			return SUCCESS;
		} catch (Exception e) {
			request.setAttribute("i", "DB ERROR");
			return SUCCESS;
		} finally {
			if (rst != null) {
				try {
					rst.close();
				} catch (Exception e) {
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (Exception e) {

				}
			}
			if (con != null) {
				DbUtils.closeQuietly(con);
			}

		}
	}

	
	public RegistrationBeen getModel() {
		// TODO Auto-generated method stub
		return s;
	}

}
