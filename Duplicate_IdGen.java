package cardgen;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import DBC.Validations_new;
import Reports.ReportBean;
import WorkerRegistration_NewAndEdit.Age;
import beens.AadhaarDetailsBean;
import beens.MyUtil;
import payments.PaymentBean;
import payments.PaymentBeanResp;
import payments.PaymentMethods;

public class Duplicate_IdGen extends ActionSupport implements ModelDriven<ReportBean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ReportBean reportBean = new ReportBean();
	List<ReportBean> list = new ArrayList<ReportBean>();
	private List<ALOApproveFormBean> flist = new ArrayList<ALOApproveFormBean>();
	private File dup_upld_chln;
	private String dup_upld_chlnFileName;
	private String ifsccode_new, validdt;

	public String getDup_upld_chlnFileName() {
		return dup_upld_chlnFileName;
	}

	public void setDup_upld_chlnFileName(String dup_upld_chlnFileName) {
		this.dup_upld_chlnFileName = dup_upld_chlnFileName;
	}

	public String getValiddt() {
		return validdt;
	}

	public void setValiddt(String validdt) {
		this.validdt = validdt;
	}

	public String getIfsccode_new() {
		return ifsccode_new;
	}

	public void setIfsccode_new(String ifsccode_new) {
		this.ifsccode_new = ifsccode_new;
	}

	public File getDup_upld_chln() {
		return dup_upld_chln;
	}

	public void setDup_upld_chln(File dup_upld_chln) {
		this.dup_upld_chln = dup_upld_chln;
	}

	public List<ALOApproveFormBean> getFlist() {
		return flist;
	}

	public void setFlist(List<ALOApproveFormBean> flist) {
		this.flist = flist;
	}

	public ReportBean getReportBean() {
		return reportBean;
	}

	public void setReportBean(ReportBean reportBean) {
		this.reportBean = reportBean;
	}

	public List<ReportBean> getList() {
		return list;
	}

	public void setList(List<ReportBean> list) {
		this.list = list;
	}

	public String challanUpdate() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		Connection cnctn = null;
		PreparedStatement prpstmt = null;
		ResultSet rst = null;
		String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
		String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("alocode")));
		try {
			cnctn = DBC.DBConnection.getConnection();			
			int trno = beens.MyUtil.logTrace(userid, "wr_address_details_org", "Insert", request);
			int chekaddr = MyUtil.DuplicateCheck("wr_address_details_org",
					"worker_reg_no='" + reportBean.getWorker_regst_no().trim() + "'", "");
			if (chekaddr == 0) {
				AadhaarDetailsBean ab=new AadhaarDetailsBean();
			ab=MyUtil.GetAadhaarDetails(reportBean.getWorker_aadhaar_no().trim());
			if(!ab.getMessage().equalsIgnoreCase("success")) {
				if(ab.getMessage().equalsIgnoreCase("SRDH Server Down")) {
					reportBean.setAadhar_ack_reg_aadhaar_no("PSS Server Down Please Try again Later");
					return "aadhaar_error";
				}else if(ab.getMessage().equalsIgnoreCase("Invalid")){
					reportBean.setAadhar_ack_reg_aadhaar_no("This Aadhaar No. Not In PSS Server Please Enroll PSS Survey");
					return "aadhaar_error";
				}
				
			}else {
				String crnadrupd = "INSERT INTO public.wr_address_details_org(temp_id,worker_reg_no,alo_code, house_no_temp, street_name_temp,"
						+ "state_code_temp, dist_code_temp, mandal_code_temp, hab_code_temp,pincode_temp, "
						+ "house_no_perm, street_name_perm, state_code_perm,"
						+ "dist_code_perm, mandal_code_perm,  aadhaar_pincode_perm,  rec_status, trno, village_code_perm)"
						+ "VALUES (?, ?,?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?);";

				prpstmt = cnctn.prepareStatement(crnadrupd);
				prpstmt.setInt(1,reportBean.getTemp_id());
				prpstmt.setString(2, reportBean.getWorker_regst_no());
				prpstmt.setString(3, alocode);
				prpstmt.setString(4, WordUtils.capitalizeFully(reportBean.getPresdno()));
				prpstmt.setString(5, WordUtils.capitalizeFully(reportBean.getPresstreet()));
				prpstmt.setInt(6, 28);
				prpstmt.setString(7, reportBean.getPresdist());
				prpstmt.setString(8, reportBean.getPresmndl());
				prpstmt.setString(9, reportBean.getPresvilg());
				prpstmt.setString(10, reportBean.getPrespin());
				
				prpstmt.setString(11, WordUtils.capitalizeFully(reportBean.getPermdno()));
				prpstmt.setString(12,WordUtils.capitalizeFully(reportBean.getPermstreet()));
				prpstmt.setInt(13, Integer.parseInt(ab.getState_code()));
				prpstmt.setString(14,ab.getDist_code());
				prpstmt.setString(15,ab.getMandal_code());
				prpstmt.setString(16,ab.getPincode());
				prpstmt.setString(17, "A");
				prpstmt.setInt(18, trno);
				prpstmt.setString(19, ab.getVillage_code());
				 prpstmt.executeUpdate();
			}
			} else {
				String crnadrupd = "UPDATE public.wr_address_details_org  SET    house_no_temp=?, street_name_temp=?,  dist_code_temp=?, mandal_code_temp=?,"
						+ " hab_code_temp=?, pincode_temp=?,trno=?,house_no_perm=?,street_name_perm=?  where worker_reg_no=? ";
				prpstmt = cnctn.prepareStatement(crnadrupd);
				prpstmt.setString(1, MyUtil.filterBad(reportBean.getPresdno()));
				prpstmt.setString(2,  MyUtil.filterBad(reportBean.getPresstreet()));
				prpstmt.setString(3, reportBean.getPresdist());
				prpstmt.setString(4, reportBean.getPresmndl());
				prpstmt.setString(5, reportBean.getPresvilg());
				prpstmt.setString(6, reportBean.getPrespin());
				prpstmt.setInt(7, trno);
				prpstmt.setString(8, WordUtils.capitalizeFully( MyUtil.filterBad(reportBean.getPermdno())));
				prpstmt.setString(9,WordUtils.capitalizeFully( MyUtil.filterBad(reportBean.getPermstreet())));
				prpstmt.setString(10, reportBean.getWorker_regst_no());
				prpstmt.executeUpdate();
			}
			if(prpstmt!=null) {
				prpstmt.close();
			}				
			String basicupd = "update wr_basic_details set mobile_no=?,rec_valid_upto=?,trno=? where worker_reg_no = ?";
			prpstmt = cnctn.prepareStatement(basicupd);
			prpstmt.setString(1, reportBean.getPhoneno());
			prpstmt.setDate(2, java.sql.Date.valueOf(MyUtil.ChDate1(reportBean.getReg_valid_upto())));
			prpstmt.setInt(3, trno);
			prpstmt.setString(4, reportBean.getWorker_regst_no());
			prpstmt.executeUpdate();
			if (prpstmt != null) {
				prpstmt.close();
			}
			int checkbank = MyUtil.DuplicateCheck("worker_bank_acct_details",
					"worker_reg_no='" + reportBean.getWorker_regst_no().trim() + "'", "");
			if (checkbank == 0) {
				String bnkupd = "INSERT INTO public.worker_bank_acct_details(temp_id, worker_reg_no, alo_code, account_no,"
						+ "record_freeze, rec_status, trno, ifsc_code)VALUES (?, ?, ?, ?,'F','A', ?, ?)";
				prpstmt = cnctn.prepareStatement(bnkupd);
				prpstmt.setInt(1, reportBean.getTemp_id());
				prpstmt.setString(2, reportBean.getWorker_regst_no());
				prpstmt.setString(3, alocode);
				prpstmt.setString(4, reportBean.getBank_accno());
				prpstmt.setInt(5, trno);
				if (reportBean.getIfsccode1() == null) {
					reportBean.setIfsccode1(getIfsccode_new());
				}
				prpstmt.setString(6, reportBean.getIfsccode1());
				prpstmt.executeUpdate();
			} else {
				String bnkupd = "update worker_bank_acct_details set account_no=?, ifsc_code=?, trno=? where worker_reg_no =?";
				prpstmt = cnctn.prepareStatement(bnkupd);
				prpstmt.setString(1, reportBean.getBank_accno());
				if (reportBean.getIfsccode1() == null) {
					reportBean.setIfsccode1(getIfsccode_new());
				}
				prpstmt.setString(2, reportBean.getIfsccode1());
				prpstmt.setInt(3, trno);
				prpstmt.setString(4, reportBean.getWorker_regst_no());
				prpstmt.executeUpdate();
			}
			if(reportBean.getFee_type().equalsIgnoreCase("OFF")){
				trno = beens.MyUtil.logTrace(userid, "duplicatecard_challan", "Insert", request);
				String chlnins = "INSERT INTO public.duplicatecard_challan(challan_no, challan_dt, challan_amnt, worker_reg_no, trno,payment_type) "
						+ " VALUES (?, ?, ?, ?, ?,?)";
				prpstmt = cnctn.prepareStatement(chlnins);
				prpstmt.setString(1, reportBean.getDup_chaln_num());
				prpstmt.setString(2, beens.MyUtil.ChDate1(reportBean.getDup_chaln_date()));
				prpstmt.setInt(3, 20);
				prpstmt.setString(4, reportBean.getWorker_regst_no());
				prpstmt.setInt(5, trno);
				prpstmt.setString(6,"OFF");
				prpstmt.executeUpdate();
				if (prpstmt != null) {
					prpstmt.close();
				}
				
				int sno = 01;
				String fileds = "SNO$$$$$" + sno;
				DBC.DBConnection.insertPhoto("duplicatecard_challan", "updatedupchallan", "challan_file",
						getDup_upld_chln(), fileds, "integer", trno, "", "");

				String adharchk = "select * from wr_basic_details where  worker_reg_no = ? ";
				prpstmt = cnctn.prepareStatement(adharchk);
				prpstmt.setString(1, reportBean.getWorker_regst_no());
				rst = prpstmt.executeQuery();
				while (rst.next()) {
					reportBean = new ReportBean();
					reportBean.setWorker_aadhaar_no(getReportBean().getAadhar_regno());
					reportBean.setWorker_name(rst.getString("worker_name") == null ? "NA" : rst.getString("worker_name"));
					reportBean.setWorker_regst_no(
							rst.getString("worker_reg_no") == null ? "NA" : rst.getString("worker_reg_no"));
					reportBean.setWorker_legacy_reg_no(
							rst.getString("worker_legacy_reg_no") == null ? "NA" : rst.getString("worker_legacy_reg_no"));
					reportBean.setWorker_age(rst.getString("worker_age") == null ? "NA" : rst.getString("worker_age"));
					reportBean.setDate_of_birth(
							rst.getString("date_of_birth") == null ? "NA" : rst.getString("date_of_birth"));
					list.add(reportBean);
					session.setAttribute("cardtype", "duplicate");
					session.setAttribute("dupworkerid",
							StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(rst.getString("worker_id"))));

				}
			}else{
				//----------------
				PaymentMethods pm=new PaymentMethods();
				PaymentBean pb=new PaymentBean();
				pb.setReference_id(reportBean.getTemp_id());
				pb.setPayment_type("DUPID");
				pb.setTot_amount(20);
				pb.setPayment_gateway("BILDDESK");
				pb.setResponseURL(MyUtil.GetFieldName("public.mst_payment_type", "resp_url", "payment_type='DUPID'"));
			PaymentBeanResp resp=	pm.PaymentProsses(pb);
				if(resp.getMessage().equalsIgnoreCase("success")){
					request.setAttribute("payment_msg", resp.getMsg());
					return "dup_pay_process";
				}
			}
			
//        	String paymupd = "update worker_payments set receipt_date = ?,trno=? where worker_reg_no = ?";
//        	prpstmt= cnctn.prepareStatement(paymupd);
//        	prpstmt.setDate(1, java.sql.Date.valueOf(MyUtil.ChDate1(reportBean.getReg_valid_upto())));
//        	prpstmt.setInt(2, trno);
//        	prpstmt.setString(3, reportBean.getWorker_regst_no());
//        	prpstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("Duplicate Id Error--->" + e);
			e.printStackTrace();
			return "failure";
		} finally {
			if (rst != null) {

				try {
					rst.close();
				} catch (Exception e) {
				}
			}
			if (prpstmt != null) {

				try {
					prpstmt.close();
				} catch (Exception e) {

				}
			}
			if (cnctn != null) {

				try {
					DbUtils.closeQuietly(cnctn);
				} catch (Exception e) {
				}
			}

		}

		return SUCCESS;
	}

	public String dup_idgen() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection cnctn = null;
		String substring = "";
		String dbaadhaar = null;
		if (!reportBean.getAction_type().equalsIgnoreCase("dup_select")) {
			return "invalidaction";
		}
		PreparedStatement prpstmt = null, cadrpstm = null, bnkpstm = null, fmprst = null, phtpstmt = null;
		ResultSet rst = null, cadrs = null, famrs = null, bnkrs = null;
		String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("alocode")));
		String aadhaarno = MyUtil.filterBad(reportBean.getAadhar_regno());
		int temp_id=0;
		String pattern = "(.*)(\\.+)(.*)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(aadhaarno);
		if (aadhaarno == null) {
			return SUCCESS;
		}
		aadhaarno = aadhaarno.trim();
		if (MyUtil.checknull_number(aadhaarno)) {
			substring = "worker_aadhaar_no";
		} else {
			if (m.find()) {
				substring = "worker_legacy_reg_no";
			} else {
				substring = "worker_reg_no";
			}
		}
		try {
			cnctn = DBC.DBConnection.getConnection();
			String adharchk = "select worker_aadhaar_no,worker_reg_no,worker_id,worker_legacy_reg_no,alo_code,worker_name,relation_name,gender_code,date_of_birth,worker_trade_code,mobile_no,rec_valid_upto,temp_id  from wr_basic_details where "
					+ substring + "=? and alo_code = ? ";
			prpstmt = cnctn.prepareStatement(adharchk);
			prpstmt.setString(1, aadhaarno);
			prpstmt.setString(2, MyUtil.filterBad(alocode.trim()));
			rst = prpstmt.executeQuery();
			if (rst.next()) {
				dbaadhaar = rst.getString("worker_aadhaar_no");
				temp_id=rst.getInt("temp_id");
				if (dbaadhaar == null) {
					reportBean.setRelation_name("Not Linked");
					return "noaadhar";
				} else if (!MyUtil.validateVerhoeff(dbaadhaar)) {
					reportBean.setRelation_name("Invalid Aadhaar");
					return "noaadhar";
				}else if(dbaadhaar.length()!=12) {
					reportBean.setRelation_name("Invalid Aadhaar");
					return "noaadhar";
				}

				if (rst.getString("worker_reg_no") != null) {
					String alo_code = rst.getString("alo_code").trim();
					reportBean.setAlo_circle(rst.getString("alo_code") == null ? "NA"
							: beens.MyUtil.getOptionValue("officer_master", "officer_code", "jurisdiction",
									rst.getString("alo_code")));
					reportBean.setWorker_name(MyUtil
							.filterBad(rst.getString("worker_name") == null ? "NA" : rst.getString("worker_name")));
					reportBean.setRelation_name(MyUtil
							.filterBad(rst.getString("relation_name") == null ? "NA" : rst.getString("relation_name")));
					reportBean.setGender(rst.getString("gender_code") == null ? "NA"
							: beens.MyUtil.getOptionValue("gender_master", "numericcode", "gendername",
									rst.getString("gender_code")));
					reportBean.setTemp_id(temp_id);
					String wrdob = rst.getString("date_of_birth");
					if (wrdob == null) {
						wrdob = "NA";
					} else {
						try {
							wrdob = beens.MyUtil.ChDate(wrdob);
						} catch (Exception e) {
							wrdob = "Invalid";
						}

					}
					reportBean.setDate_of_birth(wrdob);
					reportBean.setTrade(rst.getString("worker_trade_code") == null ? "NA"
							: beens.MyUtil.getOptionValue("trade_master", "numericcode", "trade_name",
									rst.getString("worker_trade_code")));
					reportBean.setMobile(rst.getString("mobile_no") == null ? "NA" : rst.getString("mobile_no").trim());

					request.setAttribute("aadharno", dbaadhaar);
					String wrkregstno = MyUtil.filterBad(rst.getString("worker_reg_no").trim());
					reportBean.setWorker_regst_no(wrkregstno);
					reportBean.setWorker_id(rst.getString("worker_id"));
					String vdate = rst.getString("rec_valid_upto");
					if (vdate == null) {
						vdate = "";
					} else {
						try {
							vdate = MyUtil.ChDate(vdate);
						} catch (Exception e) {
							vdate = "";
						}
					}
					reportBean.setReg_valid_upto(vdate);

					// -------------Worker Family Details ------------------
					String fmlqry = "SELECT member_name,gender_code,relation_code,date_of_birth FROM worker_family_details where  worker_reg_no = ? ";
					fmprst = cnctn.prepareStatement(fmlqry);
					fmprst.setString(1, wrkregstno);
					famrs = fmprst.executeQuery();
					while (famrs.next()) {
						ALOApproveFormBean cgeb = new ALOApproveFormBean();
						cgeb.setMembername(famrs.getString("member_name"));
						cgeb.setDup_gender(beens.MyUtil.getOptionValue("gender_master", "numericcode", "gendername",
								famrs.getString("gender_code")));
						String relname = beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
								famrs.getString("relation_code"));
						cgeb.setRelation(relname);
						String dob = famrs.getString("date_of_birth");
						if (dob == null) {
							dob = "NA";
						} else {
							try {
								dob = beens.MyUtil.ChDate(dob);
							} catch (Exception e) {
								dob = "Invalid";
							}

						}

						cgeb.setAge(dob);
						flist.add(cgeb);
					}
					// -------------- Worker Present Address details ----------------
					String craddrqry = "SELECT   house_no_temp house_no, street_name_temp street_name,house_no_perm, street_name_perm,  temp_dist_name, perm_dist_name, temp_mandal_name, perm_mandal_name,"
							+ "  temp_village_name, perm_village_name, temp_habname, perm_habname,   temp_pincode pincode, perm_pincode, state_code_temp, dist_code_temp dist_code, "
							+ "  mandal_code_temp mandal_code, hab_code_temp village_code, village_code_temp, state_code_perm,  dist_code_perm, mandal_code_perm, hab_code_perm, village_code_perm  "
							+ "FROM public.view_wr_address where  worker_reg_no =?";
					cadrpstm = cnctn.prepareStatement(craddrqry);
					cadrpstm.setString(1, wrkregstno);
					cadrs = cadrpstm.executeQuery();
					if (cadrs.next()) {
						reportBean.setPresdno(cadrs.getString("house_no") == null ? "NA" : cadrs.getString("house_no"));
						reportBean.setPresstreet(
								cadrs.getString("street_name") == null ? "NA" : cadrs.getString("street_name"));
						reportBean.setPresdist(
								cadrs.getString("dist_code") == null ? "NA" : cadrs.getString("dist_code"));
						reportBean.setPresmndl(
								cadrs.getString("mandal_code") == null ? "NA" : cadrs.getString("mandal_code"));
						reportBean.setPresvilg(
								cadrs.getString("village_code") == null ? "NA" : cadrs.getString("village_code"));
						reportBean.setPrespin(cadrs.getString("pincode") == null ? "NA" : cadrs.getString("pincode"));
						reportBean.setPermdist(cadrs.getString("perm_dist_name"));
						reportBean.setPermmndl(cadrs.getString("perm_mandal_name"));
						reportBean.setPermvilg(cadrs.getString("perm_habname"));
						reportBean.setPermpin(cadrs.getString("perm_pincode"));
						reportBean.setPermdno(cadrs.getString("house_no_perm"));
						reportBean.setPermstreet(cadrs.getString("street_name_perm"));
					} else {
						reportBean.setPresdno("");
						reportBean.setPresstreet("");
						reportBean.setPrespin("");
					}

					// Worker Bank Account Details
					String bnkqry = "SELECT   account_no, ifsc_code,bankname,branchname,ifsccode  FROM worker_bank_acct_details a left join ifsc b on a.ifsc_code=b.ifsccode where worker_reg_no = ? ";
					bnkpstm = cnctn.prepareStatement(bnkqry);
					bnkpstm.setString(1, wrkregstno);
					bnkrs = bnkpstm.executeQuery();
					if (bnkrs.next()) {
						reportBean.setBank_accno(
								bnkrs.getString("account_no") == null ? "NA" : bnkrs.getString("account_no"));
						reportBean.setIfsccode1(
								bnkrs.getString("ifsc_code") == null ? "NA" : bnkrs.getString("ifsc_code"));
						reportBean
								.setBank_name(bnkrs.getString("bankname") == null ? "NA" : bnkrs.getString("bankname"));
						reportBean.setBranch_name(bnkrs.getString("branchname") == null ? "NA"
								: bnkrs.getString("branchname").replace("\n", "").trim());
					} else {
						reportBean.setBank_accno("");
						reportBean.setIfsccode1("");

					}
					// Worker photo Details
					int phtcount = 0;
					String phtqry = "SELECT photo,count(*) as phtcnt FROM worker_photos where reg_no=? and alocode=? group by photo ";
					bnkpstm = cnctn.prepareStatement(phtqry);
					bnkpstm.setString(1, wrkregstno);
					bnkpstm.setString(2, alo_code.trim());
					bnkrs = bnkpstm.executeQuery();
					if (bnkrs.next()) {
						phtcount = bnkrs.getInt("phtcnt");
					}
					if (phtcount == 0) {
						String user_id = StringEscapeUtils
								.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("user_id")));
						int trno = beens.MyUtil.logTrace(user_id, "worker_photos", "insert", request);
						;
						String inspht = "INSERT INTO worker_photos(worker_id, reg_no, alocode, trno,temp_id) VALUES (?, ?, ?, ?, ?)";
						phtpstmt = cnctn.prepareStatement(inspht);
						phtpstmt.setString(1, MyUtil.filterBad(rst.getString("worker_id")));
						phtpstmt.setString(2, wrkregstno);
						phtpstmt.setString(3, alo_code);
						phtpstmt.setInt(4, trno);
						phtpstmt.setInt(5, temp_id);
						phtpstmt.executeUpdate();
						request.setAttribute("phtupdate", "yes");
					} else {
						request.setAttribute("phtupdate", "no");
					}
				} else {

					return "nodata";
				}
			} else {
				return "nodata";
			}
			request.setAttribute("reportBean", reportBean);
			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (rst != null) {

				try {
					rst.close();
				} catch (Exception e) {
				}
			}
			if (prpstmt != null) {

				try {
					prpstmt.close();
				} catch (Exception e) {

				}
			}
			if (cnctn != null) {

				try {
					DbUtils.closeQuietly(cnctn);
				} catch (Exception e) {
				}
			}

		}
	}

	public void validate() {
		String Msg = "";
		String action_type = null;
		action_type = reportBean.getAction_type();
		if (action_type == null) {
			addActionError("Invalid URL");
		} else if (action_type.equalsIgnoreCase("dup_select")) {
			if ((reportBean.getAadhar_regno() != null) && reportBean.getAadhar_regno().length() >= 1) {
				Msg = Validations_new.Validate(reportBean.getAadhar_regno(), 1, 35, false);
				if (Msg != null) {
					addActionError("Aadhaar No / Old Reg.No / New Reg.No" + Msg);
				}
			}
		} else if (action_type.equalsIgnoreCase("challanupdate")) {
			if (reportBean.getDup_chaln_num().length() >= 1) {
				Msg = Validations_new.Validate(reportBean.getDup_chaln_num(), 4, 20, true);
				if (Msg != null) {
					addActionError("Challan Number " + Msg);
				}
			}
			if (reportBean.getPrespin().length() == 6) {
				Msg = Validations_new.Validate(reportBean.getPrespin(), 2, 6, true);
				if (Msg != null) {
					addActionError("Pincode " + Msg);
				}
			} else {
				addActionError("Invalid Pincode ");
			}
			if(reportBean.getFee_type().equalsIgnoreCase("OFF")){
			//-------------Temporly commented by perpose online payment testing 02-08-2019
			if (reportBean.getDup_chaln_date().length() >= 1) {
				String dtval = null;
				Msg = Validations_new.Validate(reportBean.getDup_chaln_date(), 8, 10, true);
				if (Msg != null) {
					addActionError("Challan Date " + Msg);
				} else {
					try {
						dtval = MyUtil.checkCurrentDateValid(reportBean.getDup_chaln_date(), MyUtil.gettodaysDate());
					} catch (Exception e) {
						dtval = "N";
					}
					if (dtval.equalsIgnoreCase("N")) {
						addActionError("Challan Date Must before today date");
					}
				}
			}
			String filename = "";
			filename = getDup_upld_chlnFileName().toLowerCase();
			if (!(filename.endsWith(".gif") || filename.endsWith(".jpg") || filename.endsWith(".png")
					|| filename.endsWith(".jpeg"))) {
				addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
			}
			if (getDup_upld_chln().length() >= (1024 * 4000)) {
				double length = (getDup_upld_chln().length() / 1024) / 1024;
				addActionError("file size is:-" + length);
				addActionError("File Must below 40KB only ");
			}
			}
			//---------------------End--------------------
			if (reportBean.getPhoneno().length() >= 1) {
				Msg = Validations_new.Validate(reportBean.getPhoneno(), 10, 10, false);
				if (Msg != null) {
					addActionError("Mobile No " + Msg);
				}
			}
			if (reportBean.getReg_valid_upto().length() >= 1) {
				Msg = Validations_new.Validate(reportBean.getReg_valid_upto(), 8, 10, false);
				if (Msg != null) {
					addActionError("Valid Upto Date " + Msg);
				} else {
					try {
						MyUtil.ChDate1(reportBean.getReg_valid_upto());

					} catch (Exception e) {
						addActionError("Invalid Valid Date");
					}
				}
			}
		
			//
			// address valudations
//			if (reportBean.getPresstreet() == null) {
				if (reportBean.getPresstreet().length() > 1) {
					Msg = Validations_new.Validate(reportBean.getPresstreet(), 9, 40, true);
					if (Msg != null) {
						addActionError("Present Street " + Msg);
					}
				}
				if (reportBean.getPresdist().length() > 1) {
					Msg = Validations_new.Validate(reportBean.getPresdist().trim(), 2, 2, false);
					if (Msg != null) {
						addActionError("Present District" + Msg);
					}
				}
				if (reportBean.getPresmndl().length() > 1) {
					Msg = Validations_new.Validate(reportBean.getPresmndl(), 2, 4, false);
					if (Msg != null) {
						addActionError("Present Mandal" + Msg);
					}
				}

//			}
			if (reportBean.getPresvilg().length() > 1) {
				Msg = Validations_new.Validate(reportBean.getPresvilg(), 2, 14, false);
				if (Msg != null) {
					addActionError("Present Habitation" + Msg);
				}
			}
			// bank
			if (reportBean.getBank_accno().length() > 1) {
				Msg = Validations_new.Validate(reportBean.getBank_accno(), 1, 25, false);
				if (Msg != null) {
					addActionError("Account No " + Msg);
				}
			}
			if (reportBean.getBranch_name().length() > 1) {
				Msg = Validations_new.Validate(reportBean.getBranch_name(), 1, 100, false);
				if (Msg != null) {
					addActionError("Branch  name " + Msg);
				}
			}
		}
	}

	   
	public ReportBean getModel() {
		// throw new UnsupportedOperationException("Not supported yet."); //To change
		// body of generated methods, choose Tools | Templates.
		return reportBean;
	}

}
