package Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import beens.MyUtil;
import claims.ClaimsBeen;

public class Registration_Reports extends ActionSupport implements ModelDriven<ClaimsBeen> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ClaimsBeen cb=new ClaimsBeen();
	List<ClaimsBeen> clmdshblst=new ArrayList<ClaimsBeen>();
	
	
	
	public List<ClaimsBeen> getClmdshblst() {
		return clmdshblst;
	}

	public void setClmdshblst(List<ClaimsBeen> clmdshblst) {
		this.clmdshblst = clmdshblst;
	}

	public String MigratedStateReport() throws Exception {
		Connection clmdshb = null;
		PreparedStatement pstmtdshb = null;
		ResultSet rsdshb = null;
		String dshbqry ="";
		ClaimsBeen clmben=new ClaimsBeen();
		try {
			clmdshb = DBC.DBConnection.getConnection();	
			cb.setReportcategory(cb.getReportcategory());
			if(cb.getReportcategory().equals("0")) {
				cb.setMonth_name(MyUtil.getcurrentMonth().toUpperCase());
				cb.setClaim_todate(MyUtil.gettodaysDate());
				dshbqry = "select cnt,state_code,state_name,today_count,month_tot from(\r\n" + 
						"select count(*) cnt ,b.state_code_perm,sum(case when rec_ins_date=current_date then 1 else 0 end) today_count,\r\n" + 
						"sum(case when to_char(rec_ins_date,'YYYY-MM')=to_char(current_date,'YYYY-MM') then 1 else 0 end) month_tot  \r\n" + 
						"from wr_basic_details a  inner join wr_address_details_org b  on a.worker_reg_no=b.worker_reg_no  where migrated_worker='Y'  group by b.state_code_perm)aa\r\n" + 
						"right join state_master c on aa.state_code_perm=c.state_code";
				pstmtdshb = clmdshb.prepareStatement(dshbqry);
				rsdshb = pstmtdshb.executeQuery();
				while (rsdshb.next()) {
					 clmben = new ClaimsBeen();
					clmben.setCol5(rsdshb.getInt("state_code"));
					clmben.setCol1(rsdshb.getString("state_name"));
					clmben.setCol2(rsdshb.getInt("cnt"));
					clmben.setCol3(rsdshb.getInt("today_count"));
					clmben.setCol4(rsdshb.getInt("month_tot"));
					clmdshblst.add(clmben);
				}
			}else if(cb.getReportcategory().equals("1")){
//				System.out.println(""+cb.getClaim_fromdate());
				cb.setClaim_fromdate(cb.getClaim_fromdate());
				cb.setClaim_todate(cb.getClaim_todate());
				cb.setAction_type(cb.getAction_type());
		//------------------------between states-------------------------------------------------------		
				if(cb.getAction_type().equalsIgnoreCase("state")) {
					dshbqry="select cnt,state_code,state_name from(select count(*) cnt ,b.state_code_perm from wr_basic_details a  \r\n" + 
							"inner join wr_address_details_org b  on a.worker_reg_no=b.worker_reg_no  \r\n" + 
							"where rec_ins_date>=? and rec_ins_date<=? and  migrated_worker='Y'  group by b.state_code_perm)aa\r\n" + 
							"right join state_master c on aa.state_code_perm=c.state_code order by state_name";
					pstmtdshb = clmdshb.prepareStatement(dshbqry);
					pstmtdshb.setDate(1, java.sql.Date.valueOf(MyUtil.ChDate1(cb.getClaim_fromdate())));
					pstmtdshb.setDate(2, java.sql.Date.valueOf(MyUtil.ChDate1(cb.getClaim_todate())));
					rsdshb = pstmtdshb.executeQuery();
					while (rsdshb.next()) {
						 clmben = new ClaimsBeen();
						clmben.setCol1(rsdshb.getString("state_name"));
						clmben.setCol2(rsdshb.getInt("cnt"));
						clmben.setCol3(rsdshb.getInt("state_code"));
						clmdshblst.add(clmben);
					}
				}else if(cb.getAction_type().equalsIgnoreCase("dist_list")) {
					cb.setState_name(MyUtil.GetFieldName("state_master", "state_name", "state_code="+cb.getState_code()+""));
					cb.setState_code(cb.getState_code());
					dshbqry="select cnt,dist_code state_code,dist_name state_name from(select count(*) cnt ,b.dist_code_perm from wr_basic_details a  \r\n" + 
							"inner join wr_address_details_org b  on a.worker_reg_no=b.worker_reg_no  \r\n" + 
							"where rec_ins_date>=? and rec_ins_date<=? and  migrated_worker='Y' and state_code_perm=?  group by b.dist_code_perm)aa\r\n" + 
							"right join district_master c on  c.dist_code=aa.dist_code_perm where c.state_code=? order by state_name";
					pstmtdshb = clmdshb.prepareStatement(dshbqry);
					pstmtdshb.setDate(1, java.sql.Date.valueOf(MyUtil.ChDate1(cb.getClaim_fromdate())));
					pstmtdshb.setDate(2, java.sql.Date.valueOf(MyUtil.ChDate1(cb.getClaim_todate())));
					pstmtdshb.setInt(3, cb.getState_code());
					pstmtdshb.setInt(4, cb.getState_code());
					rsdshb = pstmtdshb.executeQuery();
					while (rsdshb.next()) {
						 clmben = new ClaimsBeen();
						clmben.setCol1(rsdshb.getString("state_name"));
						clmben.setCol2(rsdshb.getInt("cnt"));
						clmben.setDist_code(rsdshb.getString("state_code"));
						clmdshblst.add(clmben);
					}
				}else if(cb.getAction_type().equalsIgnoreCase("list_details")) {
					cb.setState_name(MyUtil.GetFieldName("state_master", "state_name", "state_code="+cb.getState_code()+""));
					cb.setAddrs_district_c(MyUtil.GetFieldName("district_master", "dist_name", "state_code="+cb.getState_code()+" and dist_code='"+cb.getDistrict_code()+"'"));
					dshbqry="select a.worker_reg_no,worker_name,relation_name,gendername,date_of_birth,trade_name  from wr_basic_details a  \r\n" + 
							" inner join wr_address_details_org b  on a.worker_reg_no=b.worker_reg_no  left join gender_master c on a.gender_code=c.numericcode\r\n" + 
							" left join trade_master d on a.worker_trade_code=d.numericcode " + 
							"where rec_ins_date>=? and rec_ins_date<=? and  migrated_worker='Y' and state_code_perm=? and dist_code_perm=?  ";
					pstmtdshb = clmdshb.prepareStatement(dshbqry);
					pstmtdshb.setDate(1, java.sql.Date.valueOf(MyUtil.ChDate1(cb.getClaim_fromdate())));
					pstmtdshb.setDate(2, java.sql.Date.valueOf(MyUtil.ChDate1(cb.getClaim_todate())));
					pstmtdshb.setInt(3, cb.getState_code());
					pstmtdshb.setString(4, cb.getDistrict_code());
					rsdshb = pstmtdshb.executeQuery();
					while (rsdshb.next()) {
						 clmben = new ClaimsBeen();
						clmben.setWorker_regno(rsdshb.getString("worker_reg_no"));
						clmben.setWorker_name(rsdshb.getString("worker_name"));
						clmben.setRelation_name(rsdshb.getString("relation_name"));
						clmben.setGenderen(rsdshb.getString("gendername"));
						String dob=rsdshb.getString("date_of_birth");
						if(dob==null) {
							dob="NA";
						}else {
							dob=MyUtil.ChDate(dob);
						}
						clmben.setWr_aadhhar_dob(dob);
						clmben.setTrade_name(rsdshb.getString("trade_name"));
						clmdshblst.add(clmben);
					}
				}
				
				
				
			}
		
			//---------------
			
			//-------------------
		} catch (Exception e) {
			System.out.println("Exception in service report-->"+e);
			e.printStackTrace();
		} finally {
			if (clmdshb != null) {
				DbUtils.closeQuietly(clmdshb);
				clmdshb = null;
			}
			if (pstmtdshb != null) {
				pstmtdshb.close();
				pstmtdshb = null;
			}
			if (rsdshb != null) {
				rsdshb.close();
				rsdshb = null;
			}
		}
		return SUCCESS;
	}

	
	public ClaimsBeen getModel() {
		// TODO Auto-generated method stub
		return cb;
	}
}
