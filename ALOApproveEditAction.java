package cardgen;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import DBC.Validations_new;
import beens.FileUploadsBean;
import beens.MyUtil;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringEscapeUtils;

public class ALOApproveEditAction extends ActionSupport implements ModelDriven<ALOApproveViewForm> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	File photo_edit, wrkr_exp_photo, wrkr_form21;
	String photo_editFileName, wrkr_exp_photoFileName, wrkr_form21FileName;
	private String type;

	public File getWrkr_exp_photo() {
		return wrkr_exp_photo;
	}

	public void setWrkr_exp_photo(File wrkr_exp_photo) {
		this.wrkr_exp_photo = wrkr_exp_photo;
	}

	public File getWrkr_form21() {
		return wrkr_form21;
	}

	public void setWrkr_form21(File wrkr_form21) {
		this.wrkr_form21 = wrkr_form21;
	}

	public String getWrkr_exp_photoFileName() {
		return wrkr_exp_photoFileName;
	}

	public void setWrkr_exp_photoFileName(String wrkr_exp_photoFileName) {
		this.wrkr_exp_photoFileName = wrkr_exp_photoFileName;
	}

	public String getWrkr_form21FileName() {
		return wrkr_form21FileName;
	}

	public void setWrkr_form21FileName(String wrkr_form21FileName) {
		this.wrkr_form21FileName = wrkr_form21FileName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public File getPhoto_edit() {
		return photo_edit;
	}

	public void setPhoto_edit(File photo_edit) {
		this.photo_edit = photo_edit;
	}

	public String getPhoto_editFileName() {
		return photo_editFileName;
	}

	public void setPhoto_editFileName(String photo_editFileName) {
		this.photo_editFileName = photo_editFileName;
	}

	public ALOApproveEditAction() {
	}

	ALOApproveViewForm aLOApproveViewForm = new ALOApproveViewForm();

	public ALOApproveViewForm getaLOApproveViewForm() {
		return aLOApproveViewForm;
	}

	public void setaLOApproveViewForm(ALOApproveViewForm aLOApproveViewForm) {
		this.aLOApproveViewForm = aLOApproveViewForm;
	}

	/**
	 *
	 * @return @throws Exception
	 */

	private List<ALOApproveViewForm> approveactionlist = new ArrayList<ALOApproveViewForm>();

	public List<ALOApproveViewForm> getApproveactionlist() {
		return approveactionlist;
	}

	public void setApproveactionlist(List<ALOApproveViewForm> approveactionlist) {
		this.approveactionlist = approveactionlist;
	}

	Connection con = null;
	Statement st = null;
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession session = request.getSession(false);
//	String alocode

	String alocode = MyUtil.filterBad("" + session.getAttribute("alocode"));

	public String ALOApproveEdit() throws Exception {
		if (!aLOApproveViewForm.getAction_type().equalsIgnoreCase("alo_edit_view")) {
			return "invalidaction";
		}
		
		Connection con = null;
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		PreparedStatement ps3 = null;
		PreparedStatement ps5 = null, psmigrtd = null;
		String query = null;
		int temp_id=0;
		ResultSet rs = null, rs2 = null, rs3 = null, rs5 = null, rsmigrtd = null;

//		String aadharno1 =;
		String type1 = MyUtil.filterBad(getType());
		if (type1.equals("edit")) {
			request.setAttribute("type", "edit");
		}
		if (MyUtil.checknull_number(aLOApproveViewForm.getAadharno())) {
			//returning value
		}
		try {
			con = DBC.DBConnection.getConnection();

			// Worker Personal Details
			//
			query = "SELECT worker_aadhaar_no,alo_code,worker_name,  date_of_birth,gender_code,caste_code, relation_name,"
					+ " worker_trade_code,no_work_days_year, mnregs_worker, migrated_worker, marital_status, mobile_no,worker_email,worker_union_code,a.temp_id ,rec_valid_upto,gendername   "
					+ " FROM wr_basic_details a left join gender_master b on a.gender_code=b.numericcode where temp_id=? ";
			ps = con.prepareStatement(query);
			ps.setInt(1,Integer.parseInt(aLOApproveViewForm.getAadharno()) );
			rs = ps.executeQuery();
			while (rs.next()) {
				temp_id=rs.getInt("temp_id");
				aLOApproveViewForm.setAadharcardno(rs.getString("worker_aadhaar_no"));
				aLOApproveViewForm.setAlocode(MyUtil.GetFieldName("officer_master", "jurisdiction",
						"officer_code='" + rs.getString("alo_code") + "'"));
				aLOApproveViewForm.setTemp_id(rs.getString("temp_id"));
				aLOApproveViewForm.setWorker_name(rs.getString("worker_name"));
				aLOApproveViewForm.setNature_emp(rs.getString("worker_trade_code"));
				aLOApproveViewForm.setGender(rs.getString("gendername"));
				aLOApproveViewForm.setPhoneno(rs.getString("mobile_no"));
				aLOApproveViewForm.setCast(rs.getString("caste_code"));
				aLOApproveViewForm.setFath_husb(rs.getString("relation_name"));
				aLOApproveViewForm.setMigrantworker(rs.getString("mnregs_worker"));
				aLOApproveViewForm.setMarital_status(rs.getString("marital_status"));
				aLOApproveViewForm.setTradeunion(rs.getString("worker_union_code"));
				String date1 = rs.getString("date_of_birth");
				String valid_dt = rs.getString("rec_valid_upto");
				if (valid_dt != null) {
					valid_dt = MyUtil.ChDate(valid_dt);
				} else {
					valid_dt = "NA";
				}
				aLOApproveViewForm.setValid_date(valid_dt);
				String date2 = null;
				if (date1 != null) {
					date2 = beens.MyUtil.ChDate(date1);
				} else {
					date2 = "";
				}
				aLOApproveViewForm.setDate_of_birth(date2);

				aLOApproveViewForm.setTrade_code(rs.getString("worker_trade_code"));
				String migrated = rs.getString("migrated_worker");
				// ::::::??"+rs.getString("migrated_worker")+":::::"+migrated);
				if (migrated.equals("Y")) {
					String migratedqry = "select slno,  state_name, dist_name, mandal_name  FROM wr_migrated_details a \n"
							+ "inner join state_master b on a.state_code=b.state_code inner join district_master c on a.dist_code=c.dist_code and c.state_code=a.state_code inner join mandal_master d on d.dist_code=a.dist_code and d.state_code=a.state_code and a.mandal_code=d.mandal_code "
							+ "where temp_id=?";
					psmigrtd = con.prepareStatement(migratedqry);
					psmigrtd.setInt(1, temp_id);
					rsmigrtd = psmigrtd.executeQuery();
					if (rsmigrtd.next()) {
						aLOApproveViewForm.setState(rsmigrtd.getString("state_name"));
						aLOApproveViewForm.setMigrantdistrict(rsmigrtd.getString("dist_name"));
						aLOApproveViewForm.setMigrantmandal(rsmigrtd.getString("mandal_name"));
					}
				}
			}

			// Worker Work Place Details
			String wrkdtquery = "SELECT  employer_name, work_place_location,pincode,dist_code, mandal_code, village_code FROM worker_work_place_details a   where temp_id=? ";
			ps2 = con.prepareStatement(wrkdtquery);
			ps2.setInt(1, temp_id);
			rs2 = ps2.executeQuery();
			while (rs2.next()) {
				aLOApproveViewForm.setPres_emp_name(rs2.getString("employer_name"));
				aLOApproveViewForm.setPres_emp_location(rs2.getString("work_place_location"));
				aLOApproveViewForm.setPres_emp_district(rs2.getString("dist_code"));
				aLOApproveViewForm.setPres_emp_mandal(rs2.getString("mandal_code"));
				aLOApproveViewForm.setPres_emp_village(rs2.getString("village_code"));
				aLOApproveViewForm.setPres_emp_pincode(rs2.getString("pincode"));
			}
			// ---------------new address start------------------
			String newadr = "SELECT  house_no_temp, street_name_temp,house_no_perm, street_name_perm, temp_state_name, perm_state_name,"
					+ "  temp_dist_name, perm_dist_name, temp_mandal_name, perm_mandal_name,  temp_village_name,coalesce(perm_village_name,perm_habname)  perm_village_name, temp_habname, perm_habname, "
					+ "   temp_pincode, perm_pincode, state_code_temp, dist_code_temp,  mandal_code_temp, hab_code_temp, village_code_temp, state_code_perm,"
					+ "  dist_code_perm, mandal_code_perm, hab_code_perm, village_code_perm  FROM public.view_wr_address where temp_id=? ";
			ps2 = con.prepareStatement(newadr);
			ps2.setInt(1, temp_id);
			rs2 = ps2.executeQuery();
			while (rs2.next()) {
				aLOApproveViewForm.setPermenant_addr1(rs2.getString("house_no_perm"));
				aLOApproveViewForm.setPermenant_addr2(rs2.getString("street_name_perm"));
				aLOApproveViewForm.setPermenant_addr_district(rs2.getString("perm_dist_name"));
				aLOApproveViewForm.setPermenant_addr_mandal(rs2.getString("perm_mandal_name"));
				aLOApproveViewForm.setPermenant_habcode(rs2.getString("perm_village_name"));
				aLOApproveViewForm.setPermenant_addr_pincode(rs2.getString("perm_pincode"));

				aLOApproveViewForm.setPres_addr_dno(rs2.getString("house_no_temp"));
				aLOApproveViewForm.setPres_addr_location(rs2.getString("street_name_temp"));
				aLOApproveViewForm.setPres_addr_district(rs2.getString("dist_code_temp"));
				aLOApproveViewForm.setPres_addr_mandal(rs2.getString("mandal_code_temp"));
				aLOApproveViewForm.setPres_addr_village(rs2.getString("village_code_temp"));
				aLOApproveViewForm.setPresent_habcode(rs2.getString("hab_code_temp"));
				aLOApproveViewForm.setPres_addr_pincode(rs2.getString("temp_pincode"));
			}
			// ------------------------new address end----------
			// Worker Bank Account Details
			String bankdtquery = "SELECT   COALESCE(account_no,'0')account_no, ifsc_code,bankname,branchname,ifsccode  FROM worker_bank_acct_details a inner join ifsc b on a.ifsc_code=b.ifsccode where temp_id=? ";
			ps3 = con.prepareStatement(bankdtquery);
			ps3.setInt(1,temp_id);
			rs3 = ps3.executeQuery();
			if (rs3.next()) {
				aLOApproveViewForm.setBank_accno(rs3.getString("account_no"));
				aLOApproveViewForm.setBank_name(rs3.getString("bankname"));
				aLOApproveViewForm.setIfsccode_new(rs3.getString("ifsccode"));
				aLOApproveViewForm.setBranch_name(rs3.getString("branchname"));
			} else {
				aLOApproveViewForm.setBank_accno("NA");
				aLOApproveViewForm.setBank_name("NA");
				aLOApproveViewForm.setIfsccode_new("NA");
				aLOApproveViewForm.setBranch_name("NA");
			}
			// Worker Fee Details

			String feequery = "SELECT payment_type,subscription_years,subscription_amount, registration_amount, total_amount,receipt_date, receipt_no, total_workers_in_receipt, serial_no_worker_receipt FROM worker_payments where temp_id=?  ";
			ps5 = con.prepareStatement(feequery);
			ps5.setInt(1,temp_id);
			rs5 = ps5.executeQuery();
			while (rs5.next()) {
				String feetype = rs5.getString("payment_type").trim();
				aLOApproveViewForm.setFee_type(feetype);
				if (feetype.equals("OFF")) {
					aLOApproveViewForm.setReceipt_no(rs5.getString("receipt_no"));
					aLOApproveViewForm.setReceipt_date(beens.MyUtil.ChDate(rs5.getString("receipt_date")));
					aLOApproveViewForm.setSubscription_years(rs5.getInt("subscription_years"));
					aLOApproveViewForm.setTotal_workes(rs5.getInt("total_workers_in_receipt"));
					aLOApproveViewForm.setSerial_worker_no(rs5.getInt("serial_no_worker_receipt"));
					aLOApproveViewForm.setTotal_fee_off(rs5.getInt("total_amount"));
					aLOApproveViewForm.setReg_fee_off(rs5.getInt("registration_amount"));
					aLOApproveViewForm.setSubscription_fee_off(rs5.getInt("subscription_amount"));
				}

			}

		} catch (Exception e) {
//			System.out.println("Exception in approve edit select action--->" + e);
//			e.printStackTrace();
			return "dberror";
		} finally {
			if (con != null) {
				try {
					DbUtils.closeQuietly(con);
				} catch (Exception e) {
				}
			}
		}
		request.setAttribute("reg_bean", aLOApproveViewForm);
		return SUCCESS;
	}

	public String aprrove_alo_update() throws Exception {
		if (!aLOApproveViewForm.getAction_type().equalsIgnoreCase("alo_app__upd")) {
			return "invalidaction";
		}
		PreparedStatement pstmt = null;

		File file_photo = null;
		file_photo = getPhoto_edit();
		int sno = 01;
		String fileds = "SNO$$$$$" + sno;
		String filename = null;
		try {
			filename = getPhoto_editFileName();
			if (filename == null) {
			} else {
				if ((!filename.endsWith(".JPEG")) && (!filename.endsWith(".JPG")) && (!filename.endsWith(".gif"))
						&& (!filename.endsWith(".jpg")) && (!filename.endsWith(".PNG")) && (!filename.endsWith(".png"))
						&& (!filename.endsWith(".jpeg"))) {
					addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
				}
				if (getPhoto_edit().length() >= (1024 * 150)) {
//					double length = (getPhoto_edit().length() / 1024) / 1024;
					addActionError("File Must below 15KB only ");
				}
			}
			int tradecode = 0;
			if (MyUtil.checknull_number(aLOApproveViewForm.getNature_emp())) {
				tradecode = Integer.parseInt(aLOApproveViewForm.getNature_emp());
			} else {
				return SUCCESS;
			}
			con = DBC.DBConnection.getConnection();

			int tr_no_img = beens.MyUtil.logTrace("public", "public.wr_basic_details", "Update", request);
			String sql = "update public.wr_basic_details set   mobile_no=?, worker_trade_code=?,caste_code=?"
					+ "where temp_id=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, MyUtil.filterBad(aLOApproveViewForm.getPhoneno()));
			pstmt.setInt(2, tradecode);
			pstmt.setInt(3, Integer.parseInt(aLOApproveViewForm.getCast().trim()));
			pstmt.setInt(4, Integer.parseInt(aLOApproveViewForm.getTemp_id()));
			pstmt.executeUpdate();
			String temp_id =aLOApproveViewForm.getTemp_id().trim();
			if (file_photo != null) {
				int photocount = MyUtil.DuplicateCheck("worker_photos",
						"alocode='" + alocode + "' and temp_id="+temp_id+"", "");
				if (photocount == 0) {
					DBC.DBConnection.insertPhoto("worker_photos", "insert", "photo", file_photo, fileds, "integer",
							tr_no_img,temp_id, alocode);
				} else {
					DBC.DBConnection.insertPhoto("worker_photos", "update", "photo", file_photo, fileds, "integer",
							tr_no_img, temp_id, alocode);
				}

			}

			

			File file_work = getWrkr_exp_photo();
			if (file_work != null) {
				DBC.DBConnection.insertPhoto("worker_attachments", "update", "work_certificate", file_work, fileds,
						"integer", tr_no_img, temp_id, alocode);
			}
			if (getWrkr_form21() != null) {
				// ----------form 27 upload on 240518--------
				String file_name=temp_id;
				String file_save_path = MyUtil.GetFieldName("public.mst_parameter", "param_value",
						"param_code='1' and status='A'");
				FileUploadsBean fu = MyUtil.uploadPDF(file_name, file_save_path, getWrkr_form21());
				if (fu.getMessage().equalsIgnoreCase("success")) {
					file_name = file_name + ".pdf";
					MyUtil.UpdateColumns("worker_attachments", "form27_path='" + file_name + "'",
							"temp_id='" + temp_id + "'");
				}
				// ------------------END---------------------
			}
			// address update
			pstmt.close();
			int tr_no_addrs = beens.MyUtil.logTrace("public", "public.wr_address_details_org", "Update", request);
			sql = "UPDATE public.wr_address_details_org  SET    house_no_temp=?, street_name_temp=?,  dist_code_temp=?, mandal_code_temp=?,"
					+ " hab_code_temp=?, pincode_temp=?,trno=?  where temp_id=? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, MyUtil.filterBad(aLOApproveViewForm.getPres_addr_dno()));
			pstmt.setString(2, aLOApproveViewForm.getPres_addr_location());
			pstmt.setString(3, MyUtil.filterBad(aLOApproveViewForm.getPres_addr_district()));
			pstmt.setString(4, MyUtil.filterBad(aLOApproveViewForm.getPres_addr_mandal()));
			pstmt.setString(5, MyUtil.filterBad(aLOApproveViewForm.getPres_addr_village()));
			pstmt.setString(6, MyUtil.filterBad(aLOApproveViewForm.getPres_addr_pincode()));
			pstmt.setInt(7, tr_no_addrs);
			pstmt.setInt(8, Integer.parseInt(temp_id));
			pstmt.executeUpdate();

			// work place details
			int tr_no_wrk = beens.MyUtil.logTrace("public", "public.worker_work_place_details", "Update", request);
			String sql_wrk = "update public.worker_work_place_details set  employer_name=?,  work_place_location=?,"
					+ " dist_code=?, mandal_code=?, village_code=?,pincode=?,trno=?  where temp_id=?";

			pstmt = con.prepareStatement(sql_wrk);

			pstmt.setString(1, MyUtil.filterBad(aLOApproveViewForm.getPres_emp_name()));
			pstmt.setString(2, MyUtil.filterBad(aLOApproveViewForm.getPres_emp_location()));
			pstmt.setString(3, MyUtil.filterBad(aLOApproveViewForm.getPres_emp_district()));
			pstmt.setString(4, MyUtil.filterBad(aLOApproveViewForm.getPres_emp_mandal()));
			pstmt.setString(5, MyUtil.filterBad(aLOApproveViewForm.getPres_emp_village()));
			pstmt.setString(6, MyUtil.filterBad(aLOApproveViewForm.getPres_emp_pincode()));
			pstmt.setInt(7, tr_no_wrk);
			pstmt.setInt(8,Integer.parseInt(temp_id));
			pstmt.executeUpdate();
			String ifsc2 = null;
			ifsc2 = aLOApproveViewForm.getIfsccode_new();
			if (ifsc2 == null) {
				ifsc2 = "noifsc";
			}
			if (!ifsc2.equalsIgnoreCase("noifsc")) {
				int tr_no_bank = beens.MyUtil.logTrace("public", "public.worker_bank_acct_details", "Update", request);
				String sqlbank = "update public.worker_bank_acct_details set account_no=?, ifsc_code=?, trno=?  where temp_id=?";
				pstmt = con.prepareStatement(sqlbank);
				pstmt.setString(1, aLOApproveViewForm.getBank_accno().trim());
				pstmt.setString(2, ifsc2);
				pstmt.setInt(3, tr_no_bank);
				pstmt.setInt(4,Integer.parseInt(temp_id));
				pstmt.executeUpdate();
			}
//================
			int regfee=50;
			int subscription_years=aLOApproveViewForm.getSubscription_years();
			int sub_fee=12*subscription_years;
			int total_fee=regfee+sub_fee;
			int tr_npay = beens.MyUtil.logTrace("public", "public.worker_payments", "Update", request);
			String chndate = beens.MyUtil.ChDate1(aLOApproveViewForm.getReceipt_date());
			String sqlpay = "UPDATE worker_payments SET     receipt_date=?, receipt_no=?, total_workers_in_receipt=?, serial_no_worker_receipt=?,  trno=?, "
					+ " subscription_years=?, subscription_amount=?, registration_amount=?, total_amount=?  WHERE temp_id=? ";
			pstmt = con.prepareStatement(sqlpay);
			pstmt.setDate(1, java.sql.Date.valueOf(chndate));
			pstmt.setString(2, aLOApproveViewForm.getReceipt_no().trim());
			pstmt.setInt(3, aLOApproveViewForm.getTotal_workes());
			pstmt.setInt(4, aLOApproveViewForm.getSerial_worker_no());
			pstmt.setInt(5, tr_npay);
			pstmt.setInt(6, aLOApproveViewForm.getSubscription_years());
			pstmt.setInt(7, aLOApproveViewForm.getSubscription_fee_off());
			pstmt.setInt(8, aLOApproveViewForm.getReg_fee_off());
			pstmt.setInt(9, total_fee);
			pstmt.setInt(10,Integer.parseInt(temp_id));
			pstmt.executeUpdate();
			// receipt valid upto date update on 070518
			String reciptidate = aLOApproveViewForm.getReceipt_date();
			String valid_upto = MyUtil.getValidUpto(reciptidate, aLOApproveViewForm.getSubscription_years(),"new");
			MyUtil.UpdateColumns("wr_basic_details",
					"rec_valid_upto='" + MyUtil.ChDate1(valid_upto) + "',reg_date='" + chndate + "'",
					"temp_id=" + aLOApproveViewForm.getTemp_id().trim() + "");

			// =============
		} catch (Exception e) {
//			System.out.println("Exception in approve edit action--->" + e);
//			e.printStackTrace();
			return "failure";
		} finally {
			if (pstmt != null) {
				{
					try {
						pstmt.close();
					} catch (Exception e) {

					}
				}
				if (con != null) {

					try {
					} catch (Exception e) {
					}
				}
			}
		}
		return SUCCESS;

	}

	public String alo_Reject() throws SQLException {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String alocode = StringEscapeUtils.escapeXml(beens.MyUtil.filterBad((String) session.getAttribute("alocode")));
		String user_id = StringEscapeUtils.escapeXml(MyUtil.filterBad("" + session.getAttribute("user_name")));
		String[] selectedApproval = aLOApproveViewForm.getAloapproveselect();

		if (!aLOApproveViewForm.getAction_type().equalsIgnoreCase("Reject")) {
			return "invalidaction";
		}
		int trno = 0;
		PreparedStatement ps1 = null;
		ResultSet rs3 = null;
		try {
			con = DBC.DBConnection.getConnection();
			if (selectedApproval.length == 0) {
				return SUCCESS;
			} else {

				String str = Arrays.toString(selectedApproval);

				str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
				str = str.replace(",", "','");

				for (int i = 0; i < selectedApproval.length; i++) {

					trno = beens.MyUtil.logTrace(user_id, "wr_basic_details", "update", request);

					try {
						String wrbasic = "update wr_basic_details set reject_status='Y',reject_reason= ?,reject_trno = ? where alo_code=? and  worker_aadhaar_no=? ";
						ps1 = con.prepareStatement(wrbasic);
						ps1.setString(1, aLOApproveViewForm.getReject_reason());
						ps1.setInt(2, trno);
						ps1.setString(3, alocode);
						ps1.setString(4, StringEscapeUtils.escapeXml(MyUtil.filterBad(selectedApproval[i].trim())));
						ps1.executeUpdate();
					} catch (Exception e) {
					}

				}
				if (ps1 != null)
					ps1.close();
				request.setAttribute("action_type", "reject");
				String aprvsucc = "select a.worker_name,a.relation_name,a.date_of_birth,a.gender_code,b.gendername,reject_reason "
						+ " from wr_basic_details a,gender_master b where a.gender_code=b.numericcode and worker_aadhaar_no in ('"
						+ str + "') and alo_code=? and reject_status='Y' ";
				ps1 = con.prepareStatement(aprvsucc);
				ps1.setString(1, alocode);
				rs3 = ps1.executeQuery();
				while (rs3.next()) {
					aLOApproveViewForm = new ALOApproveViewForm();
					aLOApproveViewForm.setWorker_name(rs3.getString("worker_name"));
					aLOApproveViewForm.setFather_name(rs3.getString("relation_name"));
					aLOApproveViewForm.setReject_reason(rs3.getString("reject_reason"));
					String wrdobdt = rs3.getString("date_of_birth");
					if (wrdobdt == null || wrdobdt == "") {
						wrdobdt = "N/A";
					} else {
						wrdobdt = beens.MyUtil.ChDate(wrdobdt);
					}
					aLOApproveViewForm.setDob(wrdobdt);
					aLOApproveViewForm.setGender(rs3.getString("gendername"));
					approveactionlist.add(aLOApproveViewForm);
				}
			}
		} catch (SQLException e) {

		} finally {

			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException exe) {

				}
			}

			if (con != null) {
				DbUtils.closeQuietly(con);
			}

		}
		return SUCCESS;
	}

	public String alo_Transfer() throws SQLException {
		System.out.println("in transfer");
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		String alocode = beens.MyUtil.filterBad((String) session.getAttribute("alocode"));
		String user_id = "" + session.getAttribute("user_name");
		String[] selectedApproval = aLOApproveViewForm.getAloapproveselect();
		System.out.println("selectedApproval------>"+selectedApproval);
		String action_t = aLOApproveViewForm.getAction_type();
		int trno = 0;
		int temp_id=0;
		PreparedStatement ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps5 = null, ps6 = null, ps7 = null,
				ps8 = null, ps9 = null;
		ResultSet rs1 = null, rs3 = null;
		try {
			con = DBC.DBConnection.getConnection();
			con.setAutoCommit(false);
			if (selectedApproval.length == 0) {
				return "invalidata";
			} else {
				String str = Arrays.toString(selectedApproval);

				str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
				str = str.replace(",", "','");

				for (int i = 0; i < selectedApproval.length; i++) {
					String trnsferdistcode = StringEscapeUtils
							.escapeXml(MyUtil.filterBad(aLOApproveViewForm.getTrnsfer_dist()));
					String trnsferalocode = StringEscapeUtils
							.escapeXml(MyUtil.filterBad(aLOApproveViewForm.getTrnsfer_alocode()));
					String aadhaarno = StringEscapeUtils.escapeXml(MyUtil.filterBad(selectedApproval[i].trim()));
					System.out.println("aadhaartrans--->"+aadhaarno);
					trno = beens.MyUtil.logTrace(user_id, "wr_basic_details", "update", request);
String temp_id_st=MyUtil.GetFieldName("wr_basic_details", "temp_id", "worker_aadhaar_no='"+aadhaarno+"'");
System.out.println("hvhvhvhvhv--->"+temp_id_st);
if(!temp_id_st.equalsIgnoreCase("NA")) {
	temp_id=Integer.parseInt(temp_id_st);
}
					try {
						String wrbasic = "update wr_basic_details set alo_dist_code=?,alo_code=?  ,transfer_trno = ?,transfer_alo_to=?,"
								+ "transfer_alo_from=? where  temp_id=? ";
						ps1 = con.prepareStatement(wrbasic);
						ps1.setString(1, trnsferdistcode);
						ps1.setString(2, trnsferalocode);
						ps1.setInt(3, trno);
						ps1.setString(4, alocode);
						ps1.setString(5, MyUtil.filterBad(aLOApproveViewForm.getTrnsfer_alocode()));
						ps1.setInt(6, temp_id);
						System.out.println("ps1 is=---->"+ps1);
						ps1.executeUpdate();
					} catch (Exception e) {
						System.out.println("Exception 222----->"+e);
						e.printStackTrace();
					}
					String wraddrs = "update wr_address_details_org set alo_code=?,trno=?  where  temp_id=? ";
					ps2 = con.prepareStatement(wraddrs);
					ps2.setString(1, trnsferalocode);
					ps2.setInt(2, trno);
					ps2.setInt(3, temp_id);
					ps2.executeUpdate();
					String wrkattch = "update worker_attachments set alocode=?,trno=? where alocode=? and  temp_id=? ";
					ps3 = con.prepareStatement(wrkattch);
					ps3.setString(1, trnsferalocode);
					ps3.setInt(2, trno);
					ps3.setString(3, alocode);
					ps3.setInt(4, temp_id);
					ps3.executeUpdate();

					String wrkins = "insert into worker_photos  select worker_id,reg_no,temp_id,application_no,?,photo,trno,win_regno from worker_photos where temp_id=?";
					ps4 = con.prepareStatement(wrkins);
					ps4.setString(1, trnsferalocode);
					ps4.setInt(2, temp_id);
					ps4.executeUpdate();
					if (ps4 != null) {
						ps4.close();
					}
					String wrkphts = "delete from worker_photos where temp_id=?  and alocode=? ";
					ps4 = con.prepareStatement(wrkphts);
					ps4.setInt(1, temp_id);
					ps4.setString(2, alocode);
					ps4.executeUpdate();

					String wrkbnkact = "update worker_bank_acct_details set alo_code=?,trno=? where alo_code=? and temp_id=? ";
					ps5 = con.prepareStatement(wrkbnkact);
					ps5.setString(1, trnsferalocode);
					ps5.setInt(2, trno);
					ps5.setString(3, alocode);
					ps5.setInt(4, temp_id);
					ps5.executeUpdate();

					String wrkfaml = "update worker_family_details set alo_code=?,trno=? where alo_code=? and temp_id=? ";
					ps6 = con.prepareStatement(wrkfaml);
					ps6.setString(1, trnsferalocode);
					ps6.setInt(2, trno);
					ps6.setString(3, alocode);
					ps6.setInt(4, temp_id);
					ps6.executeUpdate();

					String wrkpaym = "update worker_payments set alo_code=?,trno=? where alo_code=? and temp_id=? ";
					ps7 = con.prepareStatement(wrkpaym);
					ps7.setString(1, trnsferalocode);
					ps7.setInt(2, trno);
					ps7.setString(3, alocode);
					ps7.setInt(4, temp_id);
					ps7.executeUpdate();

					String wrkplcdt = "update worker_work_place_details set alo_code=?,trno=? where alo_code=? and temp_id=? ";
					ps8 = con.prepareStatement(wrkplcdt);
					ps8.setString(1, trnsferalocode);
					ps8.setInt(2, trno);
					ps8.setString(3, alocode);
					ps8.setInt(4, temp_id);
					ps8.executeUpdate();

				}
				request.setAttribute("action_type", "transfer");
				String aprvsucc = "select a.worker_name,a.relation_name,a.date_of_birth,a.gender_code,b.gendername "
						+ " from wr_basic_details a,gender_master b where a.gender_code=b.numericcode and worker_aadhaar_no in ('"
						+ str + "') and transfer_alo_to=?  ";

				ps9 = con.prepareStatement(aprvsucc);
				ps9.setString(1, alocode);
				System.out.println("ps9 is--->"+ps9);
				rs3 = ps9.executeQuery();
				while (rs3.next()) {
					aLOApproveViewForm = new ALOApproveViewForm();
					aLOApproveViewForm.setWorker_name(rs3.getString("worker_name"));
					aLOApproveViewForm.setFather_name(rs3.getString("relation_name"));
					String wrdobdt = rs3.getString("date_of_birth");
					if (wrdobdt == null || wrdobdt == "") {
						wrdobdt = "N/A";
					} else {
						wrdobdt = beens.MyUtil.ChDate(wrdobdt);
					}
					aLOApproveViewForm.setDob(wrdobdt);
					aLOApproveViewForm.setGender(rs3.getString("gendername"));
					approveactionlist.add(aLOApproveViewForm);
				}
			}
			con.commit();
		} catch (SQLException e) {
			System.out.println("Exception 2222AA----->"+e);
			e.printStackTrace();
			con.rollback();
			aLOApproveViewForm.setWorker_name("DB ERROR");
			approveactionlist.add(aLOApproveViewForm);
		} finally {

			if (rs1 != null) {
				try {
					rs1.close();
				} catch (SQLException exe) {

				}
			}

			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException exe) {

				}
			}
			if (ps2 != null) {
				try {
					ps2.close();
				} catch (SQLException exe) {

				}
			}
			if (ps3 != null) {
				try {
					ps3.close();
				} catch (SQLException exe) {

				}
			}
			if (ps4 != null) {
				try {
					ps4.close();
				} catch (SQLException exe) {

				}
			}
			if (ps5 != null) {
				try {
					ps5.close();
				} catch (SQLException exe) {

				}
			}
			if (con != null) {
				DbUtils.closeQuietly(con);

			}

		}
		if (action_t.equalsIgnoreCase("transfer_reg")) {
			request.setAttribute("back_button_action", "transfer_reg");
		}
		return SUCCESS;
	}

	public String aloRejectApprove() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		if (!aLOApproveViewForm.getAction_type().equalsIgnoreCase("aloRejectApprove")) {
			return "invalidaction";
		}
		String alocode = StringEscapeUtils.escapeXml(beens.MyUtil.filterBad((String) session.getAttribute("alocode")));
		String user_id = StringEscapeUtils.escapeXml(MyUtil.filterBad("" + session.getAttribute("user_name")));

		String[] selectedApproval = aLOApproveViewForm.getAloapproveselect();

		int trno = 0;
		PreparedStatement ps1 = null;
		ResultSet rs3 = null;
		try {
			con = DBC.DBConnection.getConnection();
			if (selectedApproval.length == 0) {
				return SUCCESS;
			} else {

				String str = Arrays.toString(selectedApproval);

				str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
				str = str.replace(",", "','");

				for (int i = 0; i < selectedApproval.length; i++) {

					trno = beens.MyUtil.logTrace(user_id, "wr_basic_details", "update", request);

					try {
						String wrbasic = "update wr_basic_details set reject_status='A',reject_approval_trno =? where alo_code=? and  worker_aadhaar_no=? ";
						ps1 = con.prepareStatement(wrbasic);
						ps1.setInt(1, trno);
						ps1.setString(2, alocode);
						ps1.setString(3, MyUtil.filterBad(selectedApproval[i].trim()));
						ps1.executeUpdate();
					} catch (Exception e) {
					}
					if (ps1 != null) {
						ps1.close();
					}
				}
				request.setAttribute("action_type", "aloRejectApprove");
				String aprvsucc = "select a.worker_name,a.relation_name,a.date_of_birth,a.gender_code,b.gendername,reject_reason "
						+ " from wr_basic_details a,gender_master b where a.gender_code=b.numericcode and worker_aadhaar_no in ('"
						+ MyUtil.filterBad(str) + "') and alo_code=? and reject_status='A' ";
				ps1 = con.prepareStatement(aprvsucc);
				ps1.setString(1, alocode);
				rs3 = ps1.executeQuery();
				while (rs3.next()) {
					aLOApproveViewForm = new ALOApproveViewForm();
					aLOApproveViewForm.setWorker_name(rs3.getString("worker_name"));
					aLOApproveViewForm.setFather_name(rs3.getString("relation_name"));
					aLOApproveViewForm.setReject_reason(rs3.getString("reject_reason"));
					String wrdobdt = rs3.getString("date_of_birth");
					if (wrdobdt == null || wrdobdt == "") {
						wrdobdt = "N/A";
					} else {
						wrdobdt = beens.MyUtil.ChDate(wrdobdt);
					}
					aLOApproveViewForm.setDob(wrdobdt);
					aLOApproveViewForm.setGender(rs3.getString("gendername"));
					approveactionlist.add(aLOApproveViewForm);
				}
			}
		} catch (SQLException e) {

		} finally {

			if (ps1 != null) {
				try {
					ps1.close();
				} catch (SQLException exe) {
				}
			}

			if (con != null) {
				DbUtils.closeQuietly(con);

			}

		}

		return SUCCESS;
	}

	public void validate() {
		String action_type = null;
		String filename = null;
		String Msg = "";
		action_type = aLOApproveViewForm.getAction_type();
		if (action_type == null) {
			addActionError("Invalid URL");
		} else {
			if (action_type.equalsIgnoreCase("alo_app__upd")) {
				// -------------
				if (aLOApproveViewForm.getReceipt_no().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getReceipt_no(), 4, 20, false);
					if (Msg != null) {
						addActionError("Challan Number " + Msg);
					}
				}
				if (aLOApproveViewForm.getReceipt_date().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getReceipt_date(), 8, 10, false);
					if (Msg != null) {
						addActionError("Challan Date " + Msg);
					} else {
						try {
							String dtval = MyUtil.checkCurrentDateValid(aLOApproveViewForm.getReceipt_date(),
									MyUtil.gettodaysDate());
							if (dtval.equalsIgnoreCase("N")) {
								addActionError("Challan Date Must before today date");
							}
						} catch (Exception e) {
							addActionError("Invalid Challan Date");
						}
					}
				}
				if (String.valueOf(aLOApproveViewForm.getTotal_workes()).length() >= 1) {
					Msg = Validations_new.Validate(String.valueOf(aLOApproveViewForm.getTotal_workes()), 2, 4, false);
					if (Msg != null) {
						addActionError(" Total Workers in Challan " + Msg);
					}
				}
				if (String.valueOf(aLOApproveViewForm.getSerial_worker_no()).length() >= 1) {
					Msg = Validations_new.Validate(String.valueOf(aLOApproveViewForm.getSerial_worker_no()), 2, 4,
							false);
					if (Msg != null) {
						addActionError(" Serial Number of Worker " + Msg);
					}
				}
				// ----------------
				if (aLOApproveViewForm.getPres_emp_district().length() > 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_emp_district().trim(), 2, 2, false);
					if (Msg != null) {
						addActionError("Work Place District" + Msg);
					}
				} else {
					addActionError("Please select  Place District");
				}
				if (aLOApproveViewForm.getPres_emp_mandal().length() > 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_emp_mandal(), 2, 3, false);
					if (Msg != null) {
						addActionError("Work Place Mandal" + Msg);
					}
				} else {
					addActionError("Please select  Place Mandal");
				}
				if (aLOApproveViewForm.getPres_emp_village().length() > 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_emp_village(), 2, 14, false);
					if (Msg != null) {
						addActionError("Work Place Village" + Msg);
					}
				} else {
					addActionError("Please select  Place Village");
				}
				if (aLOApproveViewForm.getPres_emp_name().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_emp_name(), 3, 100, false);
					if (Msg != null) {
						addActionError("Name of the Employer " + Msg);
					}
				} else {
					addActionError("Please Enter Name of the Employer");
				}
				if (aLOApproveViewForm.getPres_emp_location().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_emp_location(), 12, 100, false);
					if (Msg != null) {
						addActionError("Work Place Location" + Msg);
					}
				}

				// address validation

				if (aLOApproveViewForm.getPres_addr_district().length() > 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_addr_district().trim(), 2, 2, false);
					if (Msg != null) {
						addActionError(" District" + Msg);
					}
				}
				if (aLOApproveViewForm.getPres_addr_mandal().length() > 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_addr_mandal(), 2, 3, false);
					if (Msg != null) {
						addActionError("Mandal" + Msg);
					}
				}
				if (aLOApproveViewForm.getPres_addr_village().length() > 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_addr_village(), 2, 14, false);
					if (Msg != null) {
						addActionError("Village" + Msg);
					}
				}
				if (aLOApproveViewForm.getPres_addr_location().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPres_addr_location(), 12, 100, false);
					if (Msg != null) {
						addActionError("Name of the street " + Msg);
					}
				}

				// basic dertails
				if (aLOApproveViewForm.getPhoneno().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getPhoneno(), 2, 10, false);
					if (Msg != null) {
						addActionError("Mobile Number" + Msg);
					}
				} else {
					addActionError("Please Enter Mobile Number");
				}
				if (aLOApproveViewForm.getNature_emp().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getNature_emp(), 2, 2, false);
					if (Msg != null) {
						addActionError("Trade of the worker" + Msg);
					} else {
						if (Integer.parseInt(aLOApproveViewForm.getNature_emp()) == 0) {
							addActionError("Please select Trade of the worker");
						}
					}
				} else {
					addActionError("Please select Trade of the worker");
				}

				// bank details
				String ifsc2 = null;
				ifsc2 = aLOApproveViewForm.getIfsccode_new();
				if (ifsc2 == null) {
					ifsc2 = "noifsc";
				} else {
					Msg = Validations_new.Validate(aLOApproveViewForm.getBank_accno(), 9, 30, false);
					if (Msg != null) {
						addActionError("Account Number " + Msg);
					}
					if (String.valueOf(aLOApproveViewForm.getIfsccode_new()).length() >= 1) {
						Msg = Validations_new.Validate(String.valueOf(aLOApproveViewForm.getIfsccode_new()), 1, 12,
								false);
						if (Msg != null) {
							addActionError("Ifsc" + Msg);
						}
					}
				}
				filename = getPhoto_editFileName();
				if (filename == null) {
				} else {
					if ((!filename.endsWith(".JPEG")) && (!filename.endsWith(".JPG")) && (!filename.endsWith(".gif"))
							&& (!filename.endsWith(".jpg")) && (!filename.endsWith(".PNG"))
							&& (!filename.endsWith(".png")) && (!filename.endsWith(".jpeg"))) {
						addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
					}
					if (getPhoto_edit().length() >= (1024 * 150)) {
						double length = (getPhoto_edit().length() / 1024) / 1024;
						addActionError("File Must below 15KB only ");
					}
				}
				filename = getWrkr_exp_photoFileName();
				if (filename == null) {
				} else {
					if (getWrkr_exp_photo() != null) {
						filename = getWrkr_exp_photoFileName();
						if ((!filename.endsWith(".gif")) && (!filename.endsWith(".JPG")) && (!filename.endsWith(".jpg"))
								&& (!filename.endsWith(".png")) && (!filename.endsWith(".PNG"))
								&& (!filename.endsWith(".JPEG")) && (!filename.endsWith(".jpeg"))) {
							addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
						}
						if (getWrkr_exp_photo().length() >= (1024 * 4000)) {
							double length = (getWrkr_exp_photo().length() / 1024) / 1024;
							addActionError("File Must below 40KB only ");
						}
					}
				}
				filename = getWrkr_form21FileName();
				if (filename == null) {
				} else {
					if (getWrkr_form21() != null) {
						filename = getWrkr_form21FileName();
						if ((!filename.endsWith(".PDF")) && (!filename.endsWith(".pdf"))) {
							addActionError("Form XXVII Must be in pdf");
						}
						if (getWrkr_form21().length() >= (1024 * 80000)) {
							double length = (getWrkr_form21().length() / 1024) / 1024;
							addActionError("Form XXVII Must below 800KB only ");
						}
					}
				}
			} else if (action_type.equalsIgnoreCase("Reject")) {
				if (aLOApproveViewForm.getReject_reason().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getReject_reason(), 1, 200, false);
					if (Msg != null) {
						addActionError("Reason " + Msg);
					}
				} else {
					addActionError("Reason is required");
				}

				if (aLOApproveViewForm.getAloapproveselect() == null) {
					addActionError("select minimum one record!");
				} else {
					if (aLOApproveViewForm.getAloapproveselect().length == 0) {
						addActionError("select minimum one record!");
					}
				}

			} else if (action_type.equalsIgnoreCase("Transfer") || action_type.equalsIgnoreCase("transfer_reg")) {
				if (aLOApproveViewForm.getAloapproveselect() == null) {
					addActionError("select minimum one record!");
				} else {

					if (aLOApproveViewForm.getAloapproveselect().length == 0) {
						addActionError("select minimum one record!");
					}
				}
				if (aLOApproveViewForm.getTrnsfer_dist().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getTrnsfer_dist(), 2, 2, false);
					if (Msg != null) {
						addActionError("Transfer District " + Msg);
					}
				} else {
					addActionError("Transfer District is required");
				}
				if (aLOApproveViewForm.getTrnsfer_alocode().length() >= 1) {
					Msg = Validations_new.Validate(aLOApproveViewForm.getTrnsfer_alocode(), 1, 4, false);
					if (Msg != null) {
						addActionError("Transfer ALO Circlre " + Msg);
					}
				} else {
					addActionError("Transfer ALO Circlre is required");
				}
			} else if (action_type.equalsIgnoreCase("aloRejectApprove")) {
				if (aLOApproveViewForm.getAloapproveselect() == null) {
					addActionError("select minimum one record!");
				} else {
					if (aLOApproveViewForm.getAloapproveselect().length == 0) {
						addActionError("select minimum one record!");
					}
				}
			} else if (action_type.equalsIgnoreCase("alo_edit_view")) {
				if (aLOApproveViewForm.getAadharno() == null) {
					addActionError("Kindly select Record");
				} else {
					if (!MyUtil.checknull_number(aLOApproveViewForm.getAadharno())) {
						addActionError("Invalid Data Entered");
					} 
				}
			} else {
				addActionError("Wrong Action Submitted ");
			}
		}
	}

	public ALOApproveViewForm getModel() {
		return aLOApproveViewForm;
	}
}
