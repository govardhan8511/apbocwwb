
package Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.struts2.ServletActionContext;

import com.Demo.services.DemoAuthBean;
import com.Demo.services.DemoAuthImpl;
import com.Demo.services.RespBean;
import com.opensymphony.xwork2.ActionSupport;

import DBC.Validations_new;
import beens.AadhaarDetailsBean;
import beens.CBClaimBean;
import beens.CBserviceClaimMethod;
import beens.MyUtil;
import servicesMethods.JwtMethods;

public class AadharCheck extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String aadharno;
	private String action_type;
	private String memname;
	private String checkingtype;
	private String dob;
	private String worker_name;
private String reg_no;
	public String getReg_no() {
	return reg_no;
}

public void setReg_no(String reg_no) {
	this.reg_no = reg_no;
}

	public String getWorker_name() {
		return worker_name;
	}

	public void setWorker_name(String worker_name) {
		this.worker_name = worker_name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getCheckingtype() {
		return checkingtype;
	}

	public void setCheckingtype(String checkingtype) {
		this.checkingtype = checkingtype;
	}

	public String getMemname() {
		return memname;
	}

	public void setMemname(String memname) {
		this.memname = memname;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getAadharno() {
		return aadharno;
	}

	public void setAadharno(String aadharno) {
		this.aadharno = aadharno;
	}

	String Msg = null;

	public String famaadharCheck() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String aadhar = MyUtil.filterBad("" + session.getAttribute("workeraadhaar"));
		if (aadhar.length() > 0) {
			Msg = Validations_new.Validate(aadhar, 2, 12, false);
			if (Msg != null) {
				aadhar = "error";
			}
		} else {
			aadhar = "error";
		}
		if (aadhar.equalsIgnoreCase("error")) {
			request.setAttribute("action_type", "invalid_data");
			request.setAttribute("i", "0");
			return SUCCESS;
		}
		String actiontype = null;
		actiontype = getCheckingtype();
		if (actiontype == null) {
			actiontype = "error";
			return "invalidaction";
		}

		if (actiontype.equalsIgnoreCase("family") || actiontype.equalsIgnoreCase("check_fmly_name")) {
			// family name checking
			if (actiontype.equalsIgnoreCase("check_fmly_name")) {
				String fmyname = MyUtil.filterBad(getMemname());
				if (fmyname == null) {
					request.setAttribute("action_type", "invalid_data");
					request.setAttribute("i", "0");
					return SUCCESS;
				}
				if (fmyname.length() > 0) {
					Msg = Validations_new.Validate(fmyname, 3, 50, false);
					if (Msg != null) {
						fmyname = "error";
					}
				} else {
					fmyname = "error";
				}
				if (fmyname.equalsIgnoreCase("error")) {
					request.setAttribute("action_type", "invalid_data");
					request.setAttribute("i", "0");
					return SUCCESS;
				}
				int checkname = MyUtil.DuplicateCheck("public.worker_family_details",
						"worker_aadhaar_no='" + aadhar + "' and member_name ilike '" + fmyname + "'", "");
				if (checkname > 0) {
					request.setAttribute("i", "1");
					request.setAttribute("action_type", "fmly_name_check");
					return SUCCESS;
				} else {
					request.setAttribute("i", "3");
					request.setAttribute("action_type", "fmly_name_check");
					return SUCCESS;
				}

			} else {
				String member_aadhaar = getAadharno();
				if (member_aadhaar != null) {
					member_aadhaar = member_aadhaar.trim();
				}
//				int checkworker = MyUtil.DuplicateCheck("public.wr_basic_details",
//						"worker_aadhaar_no='" + member_aadhaar + "'", "");
//				if (checkworker > 0) {
//					request.setAttribute("i", "1");
//					request.setAttribute("action_type", "fmly_aadhaar_check");
//					return SUCCESS;
//				} else {
					int checkfmy = MyUtil.DuplicateCheck("public.worker_family_details",
							"member_aadhaar_no='" + member_aadhaar + "'", "");

					if (checkfmy > 0) {
						request.setAttribute("i", "2");
						request.setAttribute("action_type", "fmly_aadhaar_check");
						return SUCCESS;
					} else {
						request.setAttribute("i", "3");
						request.setAttribute("action_type", "fmly_aadhaar_check");
						return SUCCESS;
					//}
				}
			}
		} else if (actiontype.equalsIgnoreCase("nominee")) {
			String wr_adr = MyUtil.filterBad("" + session.getAttribute("workeraadhaar"));
			String nomaadr = MyUtil.filterBad(getAadharno());

			if (aadhar.length() > 0) {
				Msg = Validations_new.Validate(aadhar, 2, 12, false);
				if (Msg != null) {
					aadhar = "error";
				}
			} else {
				aadhar = "error";
			}
			if (nomaadr.length() > 0) {
				Msg = Validations_new.Validate(nomaadr, 2, 12, false);
				if (Msg != null) {
					nomaadr = "error";
				}
			} else {
				nomaadr = "error";
			}
			if (aadhar.equalsIgnoreCase("error") || nomaadr.equalsIgnoreCase("error")) {
				request.setAttribute("i", "99");
				request.setAttribute("action_type", "fmly_nominee_check");
				return SUCCESS;
			}
			if (wr_adr.equalsIgnoreCase(getAadharno())) {
				request.setAttribute("i", "1");
				request.setAttribute("action_type", "fmly_nominee_check");
				return SUCCESS;
			}
			int countnom = MyUtil.DuplicateCheck("worker_nominee_details",
					"worker_aadhaar_no ='" + wr_adr + "'  and nominee_aadhaar_no='" + nomaadr + "'", "");
			if (countnom > 0) {
				request.setAttribute("i", "2");
				request.setAttribute("action_type", "fmly_nominee_check");
				return SUCCESS;
			} else {
				request.setAttribute("i", "8");
				request.setAttribute("action_type", "fmly_nominee_check");
				return SUCCESS;
			}

		} else {
			return SUCCESS;
		}

	}

	public String aadharCheck() throws NumberFormatException, Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		Connection adccon = null;
		PreparedStatement adcpstmt = null, adcepstmt = null;
		ResultSet adcrst = null, adcers = null;
		String action_type = null;
		action_type = getAction_type();
		String dob = "NA";
		int adharcount = 0;
		String user_role=MyUtil.filterBad("" + session.getAttribute("user_role"));
		if(user_role.equalsIgnoreCase("21")){
			JwtMethods jm=new JwtMethods();
			 boolean session_mints=jm.getminutes(Integer.parseInt(""+session.getAttribute("slno")));
		        if(session_mints){
		        	MyUtil.UpdateColumns("services_records.gp_token_history", "token_status='I'", "slno="+Integer.parseInt(""+session.getAttribute("slno"))+"");
		    	   return "service_session";
		        }
		}
		
		String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
		String alo_code = MyUtil.filterBad("" + session.getAttribute("alocode"));
		AadhaarDetailsBean ab = new AadhaarDetailsBean();
		try {
			String aadhaar = getAadharno();
			boolean verilog = MyUtil.validateVerhoeff(aadhaar);
			if (verilog == false) {
				request.setAttribute("i", "9");
				request.setAttribute("action_type", "worker_aadhaar_check");
				return SUCCESS;
			}
			if (action_type == null) {
				ab = MyUtil.GetAadhaarDetails(aadhaar);
				System.out.println("message is ---->"+ab.getMessage());
				if (ab.getMessage().equalsIgnoreCase("SRDH Server Down")) {
					request.setAttribute("i", "5");
					request.setAttribute("action_type", "worker_aadhaar_check");
					return SUCCESS;
				}
				if (ab.getMessage().equalsIgnoreCase("Invalid")) {
					request.setAttribute("i", "8");
					request.setAttribute("action_type", "worker_aadhaar_check");
					return SUCCESS;
				}
				dob = ab.getDob();				
				try {
					dob = MyUtil.ChDate1(dob);
				} catch (Exception e) {
					request.setAttribute("i", "6");
					request.setAttribute("action_type", "worker_aadhaar_check");
					return SUCCESS;
				}
				int chekage = MyUtil.getAge(dob, MyUtil.gettodaysDate());
				if (chekage < 18 || chekage >= 60) {
					request.setAttribute("i", "7");
					request.setAttribute("action_type", "worker_aadhaar_check");
					return SUCCESS;
				}
			} else if (action_type.equalsIgnoreCase("migrated")) {

			} else {
				return "invalidaction";
			}
			// CB service claim check
			String check_bimaservice = MyUtil.GetFieldName("public.mst_parameter", "param_value",
					"param_code='21' and status='A'");
			if(check_bimaservice.equalsIgnoreCase("Y")){
				CBserviceClaimMethod cb=new CBserviceClaimMethod();
				CBClaimBean bima_rst=cb.CheckCBClaim(aadhaar, "");
				if(bima_rst.getMessage().equalsIgnoreCase("Yes")){
					request.setAttribute("i", "12");
					request.setAttribute("adhwrregno", bima_rst.getClaim_message());
					request.setAttribute("action_type", "worker_aadhaar_check");
					return SUCCESS;
				}else if(bima_rst.getMessage().equalsIgnoreCase("ServerDown")){
					request.setAttribute("i", "14");
					request.setAttribute("action_type", "worker_aadhaar_check");
					return SUCCESS;
				}
			}
			
			// ---------------
			adccon = DBC.DBConnection.getConnection();
			String adhrchek = "SELECT COALESCE( (select count(worker_aadhaar_no) from wr_basic_details  where worker_aadhaar_no = ?), '0') as adharcnt ";
			adcpstmt = adccon.prepareStatement(adhrchek);
			adcpstmt.setString(1, getAadharno());
			adcrst = adcpstmt.executeQuery();
			if (adcrst.next()) {
				adharcount = adcrst.getInt("adharcnt");
			}
			if (adharcount == 0) {
				// ---------------------

				String district_code = MyUtil.filterBad("" + session.getAttribute("district_code"));
				if (district_code.equalsIgnoreCase("null")) {
					district_code = MyUtil.filterBad("" + session.getAttribute("dist_code")).trim();
				}
				String ip = request.getRemoteAddr();
				String dobyear = ab.getDob().substring(6);
				DemoAuthImpl daimpl = new DemoAuthImpl();
				DemoAuthBean da = new DemoAuthBean();
				da.setUser_id(userid);
				da.setAlo_code(alo_code);
				da.setDistcode(district_code.trim());
				da.setDob_year(dobyear);
				da.setIp_addr(ip);
				da.setWorker_name(ab.getAadhaar_name());
				da.setSession_id("" + session);
				da.setUid(getAadharno());
				
//				RespBean rb = daimpl.DemoAuthCheck(da);
//				String resprbcode = rb.getRespMessage();
//				String respcode = rb.getRespCode();
//				
//				 System.out.println("respcode is--------------->"+respcode+"-----"+resprbcode);
//				if (respcode.equalsIgnoreCase("202R") || respcode.equalsIgnoreCase("201R")) {
//					request.setAttribute("i", "5");
//					request.setAttribute("action_type", "worker_migrated_check");
//					return SUCCESS;
//				}
//				if (!respcode.equalsIgnoreCase("00")||respcode.equalsIgnoreCase("100")) {
//					request.setAttribute("i", "8");
//					request.setAttribute("action_type", "worker_migrated_check");
//					return SUCCESS;
//				}
//				

				request.setAttribute("i", "1");
				request.setAttribute("action_type", "worker_aadhaar_check");
				return SUCCESS;
			} else {
				String editstschk = "SELECT COALESCE( (select count(edit_status) from wr_basic_details  where worker_aadhaar_no = ?), '0') as editcnt , "
						+ "COALESCE( (select count(worker_reg_no) from wr_basic_details  where worker_aadhaar_no = ?), '0') as regcnt,worker_reg_no,edit_status,worker_aadhaar_no,alo_code,created_by   "
						+ "from public.wr_basic_details  where worker_aadhaar_no = ?;";
				adcepstmt = adccon.prepareStatement(editstschk);
				adcepstmt.setString(1, getAadharno());
				adcepstmt.setString(2, getAadharno());
				adcepstmt.setString(3, getAadharno());
				adcers = adcepstmt.executeQuery();
				if (adcers.next()) {
					int regcnt = adcers.getInt("regcnt");
					int edstscnt = adcers.getInt("editcnt");
					if (regcnt > 0) {
						request.setAttribute("i", "2");
						request.setAttribute("adhwrregno", MyUtil.filterBad(adcers.getString("worker_reg_no")));
						request.setAttribute("action_type", "worker_aadhaar_check");
						return SUCCESS;
					} else if (regcnt == 0 && edstscnt == 1) {
						if (("" + session.getAttribute("user_role")).trim().equalsIgnoreCase("16")) {
							if (!(MyUtil.filterBad(adcers.getString("created_by")).trim()
									.equalsIgnoreCase(userid.trim()))) {
								request.setAttribute("i", "13");
								request.setAttribute("crted_by", MyUtil.filterBad(adcers.getString("created_by")));
								request.setAttribute("action_type", "worker_aadhaar_check");
								return SUCCESS;
							}
						} else {
							if (!(MyUtil.filterBad(adcers.getString("alo_code")).trim()
									.equalsIgnoreCase(alo_code.trim()))) {
								request.setAttribute("i", "13");
								request.setAttribute("crted_by", MyUtil.GetFieldName("officer_master", "jurisdiction",
										"officer_code ='" + adcers.getString("alo_code") + "'"));
								request.setAttribute("action_type", "worker_aadhaar_check");
								return SUCCESS;
							}
						}
						String editsts = adcers.getString("edit_status");
						if (editsts.equals("N")) {
							request.setAttribute("i", "3");

							request.setAttribute("action_type", "worker_aadhaar_check");
							return SUCCESS;
						}
						if (editsts.equals("Y")) {
							session.setAttribute("workeraadhaar", adcers.getString("worker_aadhaar_no"));
							session.setAttribute("workeralocode", adcers.getString("alo_code"));
							request.setAttribute("i", "4");

							request.setAttribute("action_type", "worker_aadhaar_check");
							return SUCCESS;
						}
					}

				}

			}

			int dupuid = MyUtil.DuplicateCheck("deleted_records.wr_details",
					"worker_aadhaar_no='" + aadhaar.trim() + "'", "");
			if (dupuid > 0) {
				request.setAttribute("i", "10");
				request.setAttribute("action_type", "worker_aadhaar_check");
				return SUCCESS;
			}

			// -------------------
			request.setAttribute("action_type", "worker_aadhaar_check");
			return SUCCESS;
		} catch (Exception e) {

			return "failure";
		} finally {
			if (adcers != null) {

				try {
					adcers.close();
				} catch (Exception e) {
				}
			}
			if (adcepstmt != null) {
				try {
					adcepstmt.close();
				} catch (Exception e) {

				}
			}
			if (adccon != null) {

				try {
					DbUtils.closeQuietly(adccon);
				} catch (Exception e) {
				}
			}

		}
	}

	public String migrated_check() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
		String alo_code = MyUtil.filterBad("" + session.getAttribute("alocode"));
        String user_role=MyUtil.filterBad("" + session.getAttribute("user_role"));
		String district_code = MyUtil.filterBad("" + session.getAttribute("district_code"));
		String ip = request.getRemoteAddr();
		Connection adccon = null;
		PreparedStatement adcpstmt = null, adcepstmt = null;
		ResultSet adcrst = null, adcers = null;
		String dob_check = "NA";
		int adharcount = 0;
		
		try {
			String aadhaar = getAadharno();
			boolean verilog = MyUtil.validateVerhoeff(aadhaar);
			if (verilog == false) {
				request.setAttribute("i", "9");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}
			dob_check = getDob();
			try {
				dob_check = MyUtil.ChDate1(dob_check);
			} catch (Exception e) {
				request.setAttribute("i", "6");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}
			int chekage = MyUtil.getAge(dob_check, MyUtil.gettodaysDate());
			if (chekage < 18 || chekage >= 60) {
				request.setAttribute("i", "7");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}
			if (alo_code.equalsIgnoreCase("null")) {
				alo_code = "NA";
			}
			if (district_code.equalsIgnoreCase("null")) {
				district_code = MyUtil.filterBad("" + session.getAttribute("dist_code"));
			}
			if(user_role.equals("5")){
				String reg_no=getReg_no();
				
				district_code=MyUtil.GetFieldName("wr_basic_details", "alo_dist_code", "worker_reg_no='"+reg_no+"'");
			}
			String dobyear = getDob().substring(6);
			// String checkingflag="Y";
			// AadhaarDetailsBean ab=MyUtil.GetAadhaarDetails(aadhaar);
			DemoAuthImpl daimpl = new DemoAuthImpl();
			DemoAuthBean da = new DemoAuthBean();
			da.setUser_id(userid);
			da.setAlo_code(alo_code);
			da.setDistcode(district_code);
			da.setDob_year(dobyear);
			da.setIp_addr(ip);
			da.setWorker_name(getWorker_name());
			da.setSession_id("" + session);
			da.setUid(getAadharno());
			RespBean rb = daimpl.DemoAuthCheck(da);
			String resprbcode = rb.getRespMessage();
			String respcode = rb.getRespCode();
			System.out.println(respcode);
			if (respcode.equalsIgnoreCase("202R") || respcode.equalsIgnoreCase("201R")) {
				request.setAttribute("i", "5");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}

			if (!respcode.equalsIgnoreCase("00")) {
				request.setAttribute("i", "8");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			} else {
				request.setAttribute("i", "1");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}

		} catch (Exception e) {
			System.out.println("migrated check---->" + e);
			e.printStackTrace();
			request.setAttribute("i", "5");
			request.setAttribute("action_type", "worker_migrated_check");
			return SUCCESS;
		} finally {
			if (adcers != null) {

				try {
					adcers.close();
				} catch (Exception e) {
				}
			}
			if (adcepstmt != null) {
				try {
					adcepstmt.close();
				} catch (Exception e) {

				}
			}
			if (adccon != null) {

				try {
					DbUtils.closeQuietly(adccon);
				} catch (Exception e) {
				}
			}

		}
	}

	public String migrated_fmy_check() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
		String alo_code = MyUtil.filterBad("" + session.getAttribute("alocode"));
		String district_code = MyUtil.filterBad("" + session.getAttribute("district_code"));

		String ip = request.getRemoteAddr();
		Connection adccon = null;
		PreparedStatement adcpstmt = null, adcepstmt = null;
		ResultSet adcrst = null, adcers = null;
		String dob_check = "NA";
		int adharcount = 0;
		try {
			String aadhaar = getAadharno();
			boolean verilog = MyUtil.validateVerhoeff(aadhaar);
			if (verilog == false) {
				request.setAttribute("i", "9");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}
			dob_check = getDob();
			try {
				dob_check = MyUtil.ChDate1(dob_check);
			} catch (Exception e) {
				request.setAttribute("i", "6");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}

			String dobyear = getDob().substring(6);
			// String checkingflag="Y";
			// AadhaarDetailsBean ab=MyUtil.GetAadhaarDetails(aadhaar);
			DemoAuthImpl daimpl = new DemoAuthImpl();
			DemoAuthBean da = new DemoAuthBean();
			da.setUser_id(userid);
			da.setAlo_code(alo_code);
			da.setDistcode(district_code);
			da.setDob_year(dobyear);
			da.setIp_addr(ip);
			da.setWorker_name(getWorker_name());
			da.setSession_id("" + session);
			da.setUid(getAadharno());
			RespBean rb = daimpl.DemoAuthCheck(da);
			String resprbcode = rb.getRespMessage();
			String respcode = rb.getRespCode();

			if (respcode.equalsIgnoreCase("202R") || respcode.equalsIgnoreCase("201R")) {
				request.setAttribute("i", "5");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}

			if (!respcode.equalsIgnoreCase("00")) {
				request.setAttribute("i", "8");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			} else {
				request.setAttribute("i", "1");
				request.setAttribute("action_type", "worker_migrated_check");
				return SUCCESS;
			}

		} catch (Exception e) {
			return "fail";
		} finally {
			if (adcers != null) {

				try {
					adcers.close();
				} catch (Exception e) {
				}
			}
			if (adcepstmt != null) {
				try {
					adcepstmt.close();
				} catch (Exception e) {

				}
			}
			if (adccon != null) {

				try {
					DbUtils.closeQuietly(adccon);
				} catch (Exception e) {
				}
			}

		}
	}
}
