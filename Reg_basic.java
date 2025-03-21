/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Registration;

import beens.AadhaarDetailsBean;
import beens.CBClaimBean;
import beens.CBserviceClaimMethod;
import beens.FileUploadsBean;
import beens.MyUtil;
import beens.MyutilPublicMethods;
import beens.RegistrationBeen;
import servicesMethods.JwtMethods;

import com.Demo.services.DemoAuthBean;
import com.Demo.services.DemoAuthImpl;
import com.Demo.services.RespBean;
import com.opensymphony.xwork2.ActionSupport;

import DBC.Validations_new;
import com.opensymphony.xwork2.ModelDriven;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.struts2.ServletActionContext;

public class Reg_basic extends ActionSupport implements ModelDriven<RegistrationBeen> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	File wrkr_photo, wrkr_exp_photo, wrkr_form21;
	String wrkr_photoFileName, wrkr_exp_photoFileName, wrkr_form21FileName;
	String regcaptcha;

	public String getRegcaptcha() {
		return regcaptcha;
	}

	public void setRegcaptcha(String regcaptcha) {
		this.regcaptcha = regcaptcha;
	}

	public File getWrkr_photo() {
		return wrkr_photo;
	}

	public void setWrkr_photo(File wrkr_photo) {
		this.wrkr_photo = wrkr_photo;
	}

	public File getWrkr_exp_photo() {
		return wrkr_exp_photo;
	}

	public void setWrkr_exp_photo(File wrkr_exp_photo) {
		this.wrkr_exp_photo = wrkr_exp_photo;
	}

	public String getWrkr_photoFileName() {
		return wrkr_photoFileName;
	}

	public void setWrkr_photoFileName(String wrkr_photoFileName) {
		this.wrkr_photoFileName = wrkr_photoFileName;
	}

	public String getWrkr_exp_photoFileName() {
		return wrkr_exp_photoFileName;
	}

	public void setWrkr_exp_photoFileName(String wrkr_exp_photoFileName) {
		this.wrkr_exp_photoFileName = wrkr_exp_photoFileName;
	}

	RegistrationBeen rb = new RegistrationBeen();

	public RegistrationBeen getRb() {
		return rb;
	}

	public void setRb(RegistrationBeen rb) {
		this.rb = rb;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public File getWrkr_form21() {
		return wrkr_form21;
	}

	public void setWrkr_form21(File wrkr_form21) {
		this.wrkr_form21 = wrkr_form21;
	}

	public String getWrkr_form21FileName() {
		return wrkr_form21FileName;
	}

	public void setWrkr_form21FileName(String wrkr_form21FileName) {
		this.wrkr_form21FileName = wrkr_form21FileName;
	}

	Timestamp timestamp = new Timestamp(System.currentTimeMillis());

	public String Regis_basic() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		String alo_code = rb.getAlo_code();
		String district_code = MyUtil.filterBad("" + session.getAttribute("district_code"));
		
		System.out.println("DISTRICT CODE IS--->"+session.getAttribute("district_code"));
		
		System.out.println("dISTRICT CODE IS--->"+rb.getWork_place_dist());
		
		String ip = request.getRemoteAddr();
		Connection con = null;
		Connection conman=null;		
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		String worker_name = null;
		String dob = null;
		int  i = 0;
		int basicount=0;
		int addrcount=0;
		int workplscount=0;
		int temp_id=0;
		conman=DBC.DBConnection.getConnection();
		conman.setAutoCommit(false);

		// sessions
		String userid = "" + session.getAttribute("user_id");
		String createdby = null;
        String dob_check="NA";
        String migrated_worker=null;
        String verifythrouth=null;
        //CB claim checking start--------------------------
        String check_bimaservice = MyUtil.GetFieldName("public.mst_parameter", "param_value","param_code='21' and status='A'");
		if(check_bimaservice.equalsIgnoreCase("Y")){
			CBserviceClaimMethod cb=new CBserviceClaimMethod();
			CBClaimBean bima_rst=cb.CheckCBClaim(rb.getAadhar_no().trim(), "");
			
			if(bima_rst.getMessage().equalsIgnoreCase("Yes")){
				request.setAttribute("uid_check_server", "Claimed CB");
				return "dublicate_uid";
			}else if(bima_rst.getMessage().equalsIgnoreCase("ServerDown")){
				request.setAttribute("uid_check_server", "Bima server down");
				return "dublicate_uid";
			}
		}
       
		//-------------CB claim checking end------------------
		try {
			String user_role=MyUtil.filterBad(MyUtil.filterBad("" + session.getAttribute("user_role")));
			if(user_role.equalsIgnoreCase("21")){
				JwtMethods jm=new JwtMethods();
				 boolean session_mints=jm.getminutes(Integer.parseInt(""+session.getAttribute("slno")));
			        if(session_mints){
			        	MyUtil.UpdateColumns("services_records.gp_token_history", "token_status='I'", "slno="+Integer.parseInt(""+session.getAttribute("slno"))+"");
			    	   return "service_session";
			        }
			}
			if(rb.getAction_type().equalsIgnoreCase("migrated")) {
				verifythrouth="D";
				migrated_worker="N";
				if(!rb.getAddrs_state_p_txt().trim().equalsIgnoreCase("28")) {
					migrated_worker="Y";
					
					String migrateddist=null;
					String migratedmandal=null;
					String migratedvillage=null;
					if(rb.getCheck_text_validate().equalsIgnoreCase("text_validation")) {
						MyutilPublicMethods mp=new MyutilPublicMethods();
						migrateddist=MyUtil.GetFieldName("district_master", "dist_code", "state_code='"+rb.getAddrs_state_p_txt()+"' and dist_name ilike '"+rb.getAddrs_district_p_txt()+"'");
						
						if(migrateddist.equalsIgnoreCase("NA")) {
							migrateddist=mp.insertMaster(conman, "district", rb.getAddrs_state_p_txt().trim(), null, rb.getAddrs_district_p_txt().trim(), null, null, null, null);
						rb.setAddrs_district_p(migrateddist);
						}else {
							rb.setAddrs_district_p(migrateddist);	
						}
						migratedmandal=MyUtil.GetFieldName("public.mandal_master", "mandal_code", "state_code='"+rb.getAddrs_state_p_txt()+"' and dist_code='"+rb.getAddrs_district_p()+"' and mandal_name ilike '"+rb.getAddrs_mandal_p_txt()+"'");
						if(migratedmandal.equalsIgnoreCase("NA")) {
							migratedmandal=mp.insertMaster(conman, "mandal", rb.getAddrs_state_p_txt().trim(), rb.getAddrs_district_p(), null, null, rb.getAddrs_mandal_p_txt(), null, null);
						rb.setAddrs_mandal_p(migratedmandal);
						}else {
							rb.setAddrs_mandal_p(migratedmandal);
						}
						
						migratedvillage=MyUtil.GetFieldName("public.village_master", "pss_village_code", "state_code='"+rb.getAddrs_state_p_txt()+"' and dist_code='"+rb.getAddrs_district_p()+"' and mandal_code='"+rb.getAddrs_mandal_p()+"' and pss_village_name ilike '"+rb.getAddrs_village_p_txt()+"'");
						if(migratedvillage.equalsIgnoreCase("NA")) {
							migratedvillage=mp.insertMaster(conman, "village", rb.getAddrs_state_p_txt().trim(), rb.getAddrs_district_p(), null, rb.getAddrs_mandal_p(),null, null, rb.getAddrs_village_p_txt());
						rb.setAddrs_village_p(migratedvillage);
						}else {
							rb.setAddrs_village_p(migratedvillage);
						}
					}
					
				}
				
				
			}else if(rb.getAction_type().equalsIgnoreCase("registred")) {
				verifythrouth="G";
//				verifythrouth="P";
				migrated_worker="N";
				AadhaarDetailsBean ab = MyUtil.GetAadhaarDetails(rb.getAadhar_no());
				if (ab.getMessage().equalsIgnoreCase("success")) {
					worker_name = ab.getAadhaar_name();
					dob = ab.getDob();
					dob = MyUtil.ChDate1(dob);
					int chekage = MyUtil.getAge(dob, MyUtil.gettodaysDate());
					if (chekage < 18 || chekage >= 60) {
						request.setAttribute("uid_check_server", "age not range");
						return "dublicate_uid";
					}
				} else {
					request.setAttribute("uid_check_server", "sever down");
					return "dublicate_uid";
				}
			}
			
			//common for both migrated and pss aadhaar verification on 04-02-19--
			 dob_check=rb.getDob();
	     		try{
	     			dob_check=MyUtil.ChDate1(dob_check);
	     		}catch(Exception e){
	     			request.setAttribute("uid_check_server", "age not range");
					return "dublicate_uid";       		
	     	}
			int chekage=MyUtil.getAge(dob_check, MyUtil.gettodaysDate());
     	if(chekage<18 || chekage>=60){
     		request.setAttribute("uid_check_server", "age not range");
				return "dublicate_uid";
     	}
     	if(alo_code.equalsIgnoreCase("null")) {
				alo_code="NA";
			}
			if(district_code.equalsIgnoreCase("null")) {
				district_code=MyUtil.filterBad("" + session.getAttribute("dist_code"));
			}
     	String dobyear = rb.getDob().substring(6);
//     	String checkingflag="Y";
//     	AadhaarDetailsBean ab=MyUtil.GetAadhaarDetails(aadhaar);
//			DemoAuthImpl daimpl = new DemoAuthImpl();
//			DemoAuthBean da = new DemoAuthBean();
//			da.setUser_id(userid);
//			da.setAlo_code(alo_code);
//			da.setDistcode(district_code);
//			da.setDob_year(dobyear);
//			da.setIp_addr(ip);
//			da.setWorker_name(rb.getWorker_name());
//			da.setSession_id("" +session);
//			da.setUid(rb.getAadhar_no().trim());
//			RespBean rb1 = daimpl.DemoAuthCheck(da);
//			String resprbcode = rb1.getRespMessage();
//			String respcode = rb1.getRespCode();
//
//			if (respcode.equalsIgnoreCase("202R") || respcode.equalsIgnoreCase("201R")) {
//				request.setAttribute("uid_check_server", "Internal error");
//				return "dublicate_uid";
//			}
//
//			if (!respcode.equalsIgnoreCase("00")) {
//				request.setAttribute("uid_check_server", "details not matched");
//				return "dublicate_uid";
//			} 
//     	
     	worker_name = rb.getWorker_name().trim();
     	dob=dob_check;
			//---------------------------------------
			con = DBC.DBConnection.getConnection();
			String adharchk = "select count(worker_aadhaar_no) as adharcount from  wr_basic_details where worker_aadhaar_no = ? ";
			pstmt = con.prepareStatement(adharchk);
			pstmt.setString(1, rb.getAadhar_no().trim());
			rst = pstmt.executeQuery();
			if (rst.next()) {
				i = rst.getInt("adharcount");
			}
			if (i > 0) {
				request.setAttribute("uid_check_server", "dup uid");
				return "dublicate_uid";
			} else {				
				int dupuid=MyUtil.DuplicateCheck("deleted_records.wr_details", "worker_aadhaar_no='"+rb.getAadhar_no().trim()+"'", "");
				if(dupuid>0){
					request.setAttribute("uid_check_server", "Cancle_UID");
					return "dublicate_uid";
				}
				if(pstmt!=null){
					pstmt.close();
				}
				try {
					if (userid.equalsIgnoreCase("null") || userid == null|| userid.equalsIgnoreCase("")) {
						createdby = "PUBLIC";
					} else {
						createdby = userid;
					}
					
					if(pstmt!=null){
						pstmt.close();
					}
					
					int tr_no = beens.MyUtil.logTrace(userid, "wr_basic_details", "Insert", request);
					String sql = "INSERT INTO wr_basic_details( worker_aadhaar_no, worker_name, gender_code,  "
							+ " caste_code, mobile_no, relation_code, relation_name,   worker_trade_code, no_work_days_year,"
							+ " mnregs_worker, migrated_worker, rec_status, marital_status,alo_dist_code,alo_code,trno,worker_email,rec_timestamp,edit_status,approval_status,created_by,worker_union_code,date_of_birth,alo_app_num,aadhaar_verify,rec_ins_date)\n"
							+ "    VALUES ( ?, ?, ?, ?, ?, ?, ?,   ?, ?, ?,  ?, ?, ?,?,?,?,?,now(),?,?,?,?,?,?,?,?) RETURNING temp_id";

					pstmt = conman.prepareStatement(sql);
					pstmt.setString(1, rb.getAadhar_no());
					pstmt.setString(2, WordUtils.capitalizeFully(worker_name));
					pstmt.setInt(3, rb.getGender());
					// pstmt.setString(4, getDob());
					/* pstmt.setInt(4,rb.getAge()); */
					pstmt.setInt(4, rb.getCast());
					pstmt.setString(5, rb.getMobile_no());
					pstmt.setInt(6, rb.getRelation_code());
					pstmt.setString(7, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getRelation_name())));
					pstmt.setInt(8, rb.getTrade());
					pstmt.setInt(9, rb.getWork_days());
					pstmt.setString(10, rb.getMnregs_worker());
					pstmt.setString(11, migrated_worker);
					// pstmt.setString(13,"F");
					pstmt.setString(12, "A");
					pstmt.setInt(13, rb.getMarital_status());
					pstmt.setString(14, rb.getWork_place_dist());
					pstmt.setString(15, rb.getAlo_code());
					pstmt.setInt(16, tr_no);
					pstmt.setString(17, rb.getEmail());
					pstmt.setString(18, "Y");
					pstmt.setString(19, "P");
					pstmt.setString(20, createdby);
					pstmt.setString(21, rb.getUnion_code());
					pstmt.setDate(22, java.sql.Date.valueOf(dob));
					pstmt.setInt(23,rb.getAlo_app_num());
					pstmt.setString(24, verifythrouth);
					pstmt.setDate(25,java.sql.Date.valueOf(MyUtil.ChDate1(MyUtil.gettodaysDate())) );
					rst=pstmt.executeQuery();
					if(rst.next()) {
						basicount=1;
						temp_id=rst.getInt("temp_id");
					}
					// address insert
					// pss addres code added on 20-06-2018
					if(rb.getAction_type().equals("registred")) {
						if (rb.getChecksameaddr() == null) {
							
							// new address table
							sql = "INSERT INTO public.wr_address_details_org(temp_id,alo_code, house_no_temp, street_name_temp,"
									+ "state_code_temp, dist_code_temp, mandal_code_temp, hab_code_temp,pincode_temp, house_no_perm, street_name_perm, state_code_perm,"
									+ "dist_code_perm, mandal_code_perm, hab_code_perm, aadhaar_pincode_perm,remarks,  rec_status, trno, village_code_temp,"
									+ "village_code_perm)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?);";

							pstmt = conman.prepareStatement(sql);
							pstmt.setInt(1, temp_id);	
							pstmt.setString(2, rb.getAlo_code());
							pstmt.setString(3, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_c())));
							pstmt.setString(4, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_c())));
							pstmt.setInt(5, Integer.parseInt(rb.getAddrs_state_c()));
							pstmt.setString(6, rb.getAddrs_district_c());
							pstmt.setString(7, rb.getAddrs_mandal_c());
							pstmt.setString(8, rb.getAddrs_village_c());
							pstmt.setString(9, (rb.getAddrs_pincode_c()));
							pstmt.setString(10, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
							pstmt.setString(11, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
							pstmt.setInt(12, Integer.parseInt(rb.getBoc_state_code()));
							pstmt.setString(13, rb.getBoc_dist_code());
							pstmt.setString(14, rb.getBoc_mandal_code());
							pstmt.setString(15,null);
							pstmt.setString(16, (rb.getAddrs_pincode_p()));
							pstmt.setString(17,"pss updated");
							pstmt.setString(18, "A");
							pstmt.setInt(19, tr_no);
							pstmt.setString(20, rb.getAddrs_pss_village_c());
							pstmt.setString(21, rb.getBoc_vallage_code());
							addrcount = pstmt.executeUpdate();
						} else {	
							
							// new address table
							sql = "INSERT INTO public.wr_address_details_org(temp_id,alo_code, house_no_temp, street_name_temp,"
									+ "state_code_temp, dist_code_temp, mandal_code_temp, hab_code_temp,pincode_temp, house_no_perm, street_name_perm, state_code_perm,"
									+ "dist_code_perm, mandal_code_perm, hab_code_perm, aadhaar_pincode_perm,remarks,  rec_status, trno, village_code_temp,"
									+ "village_code_perm)VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?);";

							pstmt = conman.prepareStatement(sql);
							pstmt.setInt(1, temp_id);	
							pstmt.setString(2, rb.getAlo_code());
							pstmt.setString(3, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
							pstmt.setString(4, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
							pstmt.setInt(5, Integer.parseInt(rb.getBoc_state_code()));
							pstmt.setString(6, rb.getBoc_dist_code());
							pstmt.setString(7, rb.getBoc_mandal_code());
							pstmt.setString(8, rb.getAddrs_village_c());
							pstmt.setString(9, (rb.getAddrs_pincode_p()));
							pstmt.setString(10, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
							pstmt.setString(11, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
							pstmt.setInt(12, Integer.parseInt(rb.getBoc_state_code()));
							pstmt.setString(13, rb.getBoc_dist_code());
							pstmt.setString(14, rb.getBoc_mandal_code());
							pstmt.setString(15, null);
							pstmt.setString(16, (rb.getAddrs_pincode_p()));
							pstmt.setString(17,"pss updated");
							pstmt.setString(18, "A");
							pstmt.setInt(19, tr_no);
							pstmt.setString(20, rb.getAddrs_village_p());
							pstmt.setString(21, rb.getAddrs_village_p());

							addrcount = pstmt.executeUpdate();
						}
					}else if(rb.getAction_type().equalsIgnoreCase("migrated")) {
						sql = "INSERT INTO public.wr_address_details_org(temp_id,alo_code, house_no_temp, street_name_temp,"
								+ "state_code_temp, dist_code_temp, mandal_code_temp, hab_code_temp,pincode_temp, house_no_perm, street_name_perm, state_code_perm,"
								+ "dist_code_perm, mandal_code_perm, hab_code_perm, aadhaar_pincode_perm,remarks,  rec_status, trno, village_code_temp,"
								+ "village_code_perm)VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?,?);";

						pstmt = conman.prepareStatement(sql);
						pstmt.setInt(1, temp_id);	
						pstmt.setString(2, rb.getAlo_code());
						pstmt.setString(3, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_c())));
						pstmt.setString(4, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_c())));
						pstmt.setInt(5, Integer.parseInt(rb.getAddrs_state_c()));
						pstmt.setString(6, rb.getAddrs_district_c());
						pstmt.setString(7, rb.getAddrs_mandal_c());
						pstmt.setString(8, rb.getAddrs_village_c());
						pstmt.setString(9, (rb.getAddrs_pincode_c()));
						pstmt.setString(10, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
						pstmt.setString(11, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
						pstmt.setInt(12, Integer.parseInt(rb.getAddrs_state_p_txt()));
						pstmt.setString(13, rb.getAddrs_district_p());
						pstmt.setString(14, rb.getAddrs_mandal_p());
						pstmt.setString(15,null);
						pstmt.setString(16, (rb.getAddrs_pincode_p()));
						pstmt.setString(17,"migrated worker");
						pstmt.setString(18, "A");
						pstmt.setInt(19, tr_no);
						pstmt.setString(20, rb.getAddrs_pss_village_c());
						pstmt.setString(21, rb.getAddrs_village_p());
						addrcount = pstmt.executeUpdate();
					}

					// permenet address

					// work place details
					String sql_wrk = "insert into  worker_work_place_details(temp_id,alo_code, employer_name, constn_name,  work_place_location,"
							+ " dist_code, mandal_code, village_code,pincode,rec_status,trno) "
							+ "values (?,?,?,?,?,?,?,?,?,'A',?)";

					pstmt = conman.prepareStatement(sql_wrk);
					pstmt.setInt(1,temp_id);
					pstmt.setString(2, rb.getAlo_code());
					pstmt.setString(3, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getWork_employer_name())));
					pstmt.setString(4, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getWork_place_orgname())));
					pstmt.setString(5, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getWork_place_location())));
					pstmt.setString(6, rb.getWork_place_dist());
					pstmt.setString(7, rb.getWork_place_mandal());
					pstmt.setString(8, rb.getWork_place_village());
					pstmt.setString(9, rb.getWork_place_pincode());
					pstmt.setInt(10, tr_no);	
					workplscount=pstmt.executeUpdate();

					// uploads
					File file_photo = getWrkr_photo();
					// File file_adhar = getWrkr_aadhar_photo();
					File file_work = getWrkr_exp_photo();
					File file_form = getWrkr_form21();
					int sno = 01;
					String fileds = "SNO$$$$$" + sno;

				
					if( basicount>0 && addrcount>0 && workplscount>0 ){
						conman.commit();
						DBC.DBConnection.insertPhoto("worker_photos", "insert", "photo", file_photo, fileds, "integer",
								tr_no, ""+temp_id, rb.getAlo_code());
							DBC.DBConnection.insertPhoto("worker_attachments", "insert", "work_certificate", file_work, fileds,
								"integer", tr_no, ""+temp_id, rb.getAlo_code());
						
						// select tempid
						// ----------form 27 upload on 240518--------
						String file_save_path = MyUtil.GetFieldName("public.mst_parameter", "param_value",
								"param_code='1' and status='A'");
						String aadhaar_no = rb.getAadhar_no();
						String file_name = MyUtil.GetFieldName(" wr_basic_details", "temp_id",
								"worker_aadhaar_no='" + aadhaar_no + "'");
						if(file_name.equalsIgnoreCase("NA")) {
							request.setAttribute("uid_check_server", "Form 27 not uploaded");
							return "dublicate_uid"; 
						}
						FileUploadsBean fu = MyUtil.uploadPDF(file_name, file_save_path, getWrkr_form21());
						if (fu.getMessage().equalsIgnoreCase("success")) {
							file_name = file_name + ".pdf";
							MyUtil.UpdateColumns("worker_attachments", "form27_path='" + file_name + "'",
									"temp_id=" + temp_id + "");
						}else {
							request.setAttribute("uid_check_server", "Form 27 not uploaded");
							return "dublicate_uid";  
						}
						}else{
						conman.rollback();	
						return "fail";
					}
					
					// ------------------END---------------------
					String temqry = "select temp_id,worker_aadhaar_no,alo_code,date_of_birth,worker_name from  wr_basic_details where worker_aadhaar_no = ? ";
					pstmt = con.prepareStatement(temqry);
					pstmt.setString(1, rb.getAadhar_no());
					rst = pstmt.executeQuery();
					if (rst.next()) {
						session.setAttribute("temp_id", rst.getString("temp_id"));
						session.setAttribute("workeraadhaar", rst.getString("worker_aadhaar_no"));
						session.setAttribute("workeralocode", rst.getString("alo_code"));
						session.setAttribute("workername", rst.getString("worker_name"));
						session.setAttribute("worker_date_of_birth", rst.getString("date_of_birth"));
						request.setAttribute("td", "2");
					}
					if (userid.equalsIgnoreCase("null")) {
						return SUCCESS;
					}else if(user_role.equals("21")){
						return "GPSuccess";
					} else if(rb.getAction_type().equals("registred")){
						return "LoginSuccess";
					}else if(rb.getAction_type().equals("migrated")){
						return "MigratedSuccess";
					}else {
						return "invalidata";
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("exception is---->"+e);
				   conman.rollback();
					return "fail";
				} finally {
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

		} catch (Exception e) {
			System.out.println("Exception--->"+e);
			e.printStackTrace();
			conman.rollback();
			return "fail";
		} finally {
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

		// -----

	}

	public String Reg_basic_update() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		String aadharno = MyUtil.filterBad("" + session.getAttribute("workeraadhaar"));
		String alocode = MyUtil.filterBad("" + session.getAttribute("workeralocode"));
		String temp_id=MyUtil.filterBad((""+session.getAttribute("temp_id")).trim());
		String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
		String user_role=MyUtil.filterBad(MyUtil.filterBad("" + session.getAttribute("user_role")));
		Date d = new Date();
		Timestamp ts = new Timestamp(d.getTime());
		String stamp = ts.toString();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rst = null;
		int rs2 = 0;
		con = DBC.DBConnection.getConnection();
		String migrated_worker="N";
		if(rb.getAction_type().equalsIgnoreCase("migrated")) {
			migrated_worker="N";
			if(!rb.getAddrs_state_p_txt().trim().equalsIgnoreCase("28")) {
				migrated_worker="Y";
				String migrateddist=null;
				String migratedmandal=null;
				String migratedvillage=null;
				if(rb.getCheck_text_validate().equalsIgnoreCase("text_validation")) {
					MyutilPublicMethods mp=new MyutilPublicMethods();
					migrateddist=MyUtil.GetFieldName("district_master", "dist_code", "state_code='"+rb.getAddrs_state_p_txt()+"' and dist_name ilike '"+rb.getAddrs_district_p_txt()+"'");
					if(migrateddist.equalsIgnoreCase("NA")) {
						migrateddist=mp.insertMaster(con, "district", rb.getAddrs_state_p_txt().trim(), null, rb.getAddrs_district_p_txt().trim(), null, null, null, null);
					rb.setAddrs_district_p(migrateddist);
					}else {
						rb.setAddrs_district_p(migrateddist);	
					}
					migratedmandal=MyUtil.GetFieldName("public.mandal_master", "mandal_code", "state_code='"+rb.getAddrs_state_p_txt()+"' and dist_code='"+rb.getAddrs_district_p()+"' and mandal_name ilike '"+rb.getAddrs_mandal_p_txt()+"'");
					if(migratedmandal.equalsIgnoreCase("NA")) {
						migratedmandal=mp.insertMaster(con, "mandal", rb.getAddrs_state_p_txt().trim(), rb.getAddrs_district_p(), null, null, rb.getAddrs_mandal_p_txt(), null, null);
					rb.setAddrs_mandal_p(migratedmandal);
					}else {
						rb.setAddrs_mandal_p(migratedmandal);
					}
					
					migratedvillage=MyUtil.GetFieldName("public.village_master", "pss_village_code", "state_code='"+rb.getAddrs_state_p_txt()+"' and dist_code='"+rb.getAddrs_district_p()+"' and mandal_code='"+rb.getAddrs_mandal_p()+"' and pss_village_name ilike '"+rb.getAddrs_village_p_txt()+"'");
					if(migratedvillage.equalsIgnoreCase("NA")) {
						migratedvillage=mp.insertMaster(con, "village", rb.getAddrs_state_p_txt().trim(), rb.getAddrs_district_p(), null, rb.getAddrs_mandal_p(),null, null, rb.getAddrs_village_p_txt());
					rb.setAddrs_village_p(migratedvillage);
					}else {
						rb.setAddrs_village_p(migratedvillage);
					}
				}
				
			}			
			 
		}
		
		try {
			

			int tr_no = beens.MyUtil.logTrace(userid, " wr_basic_details", "Update", request);
			System.out.println("tr_no isss->"+tr_no);
			String sql = "update  wr_basic_details set  caste_code=?, mobile_no=?, worker_trade_code=?, no_work_days_year=?,"
					+ " mnregs_worker=?, alo_app_num=?,  marital_status=?,alo_dist_code=?,alo_code=?,trno=?,worker_email=?,worker_union_code=?,relation_name=?,migrated_worker=? "
					+ "where worker_aadhaar_no=?";
			
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rb.getCast());
			pstmt.setString(2, rb.getMobile_no());
			pstmt.setInt(3, rb.getTrade());
			pstmt.setInt(4, rb.getWork_days());
			pstmt.setString(5, rb.getMnregs_worker());
			pstmt.setInt(6,rb.getAlo_app_num());
			pstmt.setInt(7, rb.getMarital_status());
			pstmt.setString(8, rb.getWork_place_dist());
			pstmt.setString(9, rb.getAlo_code());
			pstmt.setInt(10, tr_no);
			pstmt.setString(11, rb.getEmail());
			pstmt.setString(12, rb.getUnion_code());
			pstmt.setString(13, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getRelation_name().trim())));
			pstmt.setString(14, migrated_worker);
			pstmt.setString(15, aadharno);
			rs2 = pstmt.executeUpdate();
			if(pstmt!=null){
				pstmt.close();
			}
			// address update
			int tr_no_addrs = beens.MyUtil.logTrace(userid, " wr_address_details_org", "Update", request);
			if(rb.getAction_type().equals("registred")) {
			if(rb.getChecksameaddr()==null){
				//--------
				sql="UPDATE public.wr_address_details_org   SET  house_no_temp=? ,"
						+ "street_name_temp=?, state_code_temp=?, dist_code_temp=?, mandal_code_temp=?,"
						+ "hab_code_temp=?, pincode_temp=?, trno=?,alo_code=?, village_code_temp=?,house_no_perm=?, street_name_perm=?  WHERE temp_id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_c())));
				pstmt.setString(2, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_c())));
				pstmt.setInt(3, Integer.parseInt(rb.getAddrs_state_c()));
				pstmt.setString(4, MyUtil.isBlank(rb.getAddrs_district_c()));
				pstmt.setString(5, MyUtil.isBlank(rb.getAddrs_mandal_c()));
				pstmt.setString(6, MyUtil.isBlank(rb.getAddrs_village_c()));
				pstmt.setString(7, (rb.getAddrs_pincode_c()));
				pstmt.setInt(8, tr_no_addrs);
				pstmt.setString(9, rb.getAlo_code());
				pstmt.setString(10, rb.getAddrs_pss_village_c());
				pstmt.setString(11, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
				pstmt.setString(12, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
				pstmt.setInt(13, Integer.parseInt(temp_id));
				int z = pstmt.executeUpdate();
			}else{				
				sql="UPDATE public.wr_address_details_org   SET  house_no_temp=? ,"
						+ "street_name_temp=?, state_code_temp=?, dist_code_temp=?, mandal_code_temp=?,"
						+ "hab_code_temp=?, pincode_temp=?, trno=?,alo_code=?, village_code_temp=?,house_no_perm=?, street_name_perm=?  WHERE temp_id=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
				pstmt.setString(2, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
				pstmt.setInt(3, Integer.parseInt(rb.getBoc_state_code()));
				pstmt.setString(4, MyUtil.isBlank(rb.getBoc_dist_code()));
				pstmt.setString(5, MyUtil.isBlank(rb.getBoc_mandal_code()));
				pstmt.setString(6, MyUtil.isBlank(rb.getAddrs_village_c()));
				pstmt.setString(7, (rb.getAddrs_pincode_p()));
				pstmt.setInt(8, tr_no_addrs);
				pstmt.setString(9, rb.getAlo_code());
				pstmt.setString(10, rb.getBoc_vallage_code());
				pstmt.setString(11, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
				pstmt.setString(12, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
				pstmt.setInt(13, Integer.parseInt(temp_id));
				 pstmt.executeUpdate();
			}}else if(rb.getAction_type().equalsIgnoreCase("migrated")) {
				sql = "UPDATE public.wr_address_details_org  SET   house_no_temp=?,street_name_temp=?, state_code_temp=?, dist_code_temp=?, mandal_code_temp=?, " 
						+"       hab_code_temp=?, pincode_temp=?, house_no_perm=?, street_name_perm=?,state_code_perm=?, dist_code_perm=?, mandal_code_perm=?, hab_code_perm=?," 
						+"       aadhaar_pincode_perm=?,   trno=?, village_code_temp=?, village_code_perm=?  WHERE temp_id=? ";

				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_c())));
				pstmt.setString(2, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_c())));
				pstmt.setInt(3, Integer.parseInt(rb.getAddrs_state_c()));
				pstmt.setString(4, rb.getAddrs_district_c());
				pstmt.setString(5, rb.getAddrs_mandal_c());
				pstmt.setString(6, rb.getAddrs_village_c());
				pstmt.setString(7, (rb.getAddrs_pincode_c()));
				pstmt.setString(8, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getDoor_no_p())));
				pstmt.setString(9, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getStreet_name_p())));
				pstmt.setInt(10, Integer.parseInt(rb.getAddrs_state_p_txt()));
				pstmt.setString(11, rb.getAddrs_district_p());
				pstmt.setString(12, rb.getAddrs_mandal_p());
				pstmt.setString(13,null);
				pstmt.setString(14, (rb.getAddrs_pincode_p()));
				pstmt.setInt(15, tr_no);
				pstmt.setString(16, rb.getAddrs_pss_village_c());
				pstmt.setString(17, rb.getAddrs_village_p());
				pstmt.setInt(18, Integer.parseInt(temp_id));
				 pstmt.executeUpdate();
			}
			// work place details
			int tr_no_wrk = beens.MyUtil.logTrace(userid, " worker_work_place_details", "Update", request);
			String sql_wrk = "update  worker_work_place_details set alo_code=?, employer_name=?, constn_name=?,  work_place_location=?,"
					+ " dist_code=?, mandal_code=?, village_code=?,pincode=?,trno=?  where temp_id=?";

			pstmt = con.prepareStatement(sql_wrk);

			pstmt.setString(1, rb.getAlo_code());
			pstmt.setString(2, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getWork_employer_name())));
			pstmt.setString(3, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getWork_place_orgname())));
			pstmt.setString(4, WordUtils.capitalizeFully(MyUtil.filterBad(rb.getWork_place_location())));
			pstmt.setString(5, rb.getWork_place_dist());
			pstmt.setString(6, rb.getWork_place_mandal());
			pstmt.setString(7, rb.getWork_place_village());
			pstmt.setString(8, rb.getWork_place_pincode());
			pstmt.setInt(9, tr_no_wrk);
			pstmt.setInt(10, Integer.parseInt(temp_id));
			pstmt.executeUpdate();
			// file updates
			File file_photo = getWrkr_photo();
			// File file_adhar = getWrkr_aadhar_photo();
			File file_work = getWrkr_exp_photo();
			File file_form = getWrkr_form21();
			int sno = 01;
			String fileds = "SNO$$$$$" + sno;
			int tr_no_img = beens.MyUtil.logTrace(userid, " worker_photos", "Update", request);
			if (file_photo != null) {
				DBC.DBConnection.insertPhoto("worker_photos", "update", "photo", file_photo, fileds, "integer",
						tr_no_img, temp_id, alocode);
			}
			
			if (file_work != null) {
				DBC.DBConnection.insertPhoto("worker_attachments", "update", "work_certificate", file_work, fileds,
						"integer", tr_no_img, temp_id, alocode);
			}
			if (getWrkr_form21() != null) {
				// ----------form 27 upload on 240518--------
				String file_save_path = MyUtil.GetFieldName("public.mst_parameter", "param_value",
						"param_code='1' and status='A'");
				String aadhaar_no = rb.getAadhar_no();
				String file_name = MyUtil.GetFieldName(" wr_basic_details", "temp_id",
						"worker_aadhaar_no='" + aadhaar_no + "'");
				FileUploadsBean fu = MyUtil.uploadPDF(file_name, file_save_path, getWrkr_form21());
				if (fu.getMessage().equalsIgnoreCase("success")) {
					file_name = file_name + ".pdf";
					MyUtil.UpdateColumns("worker_attachments", "form27_path='" + file_name + "'",
							"temp_id="+ temp_id +"");
				}
				// ------------------END---------------------
			}

			// select tempid
			String temqry = "select temp_id,worker_aadhaar_no,alo_code,worker_name from  wr_basic_details where worker_aadhaar_no = '"
					+ rb.getAadhar_no() + "' ";
			pstmt = con.prepareStatement(temqry);
			rst = pstmt.executeQuery();
			if (rst.next()) {
				session.setAttribute("temp_id", rst.getString("temp_id"));
				session.setAttribute("workeraadhaar", rst.getString("worker_aadhaar_no"));
				session.setAttribute("workeralocode", rst.getString("alo_code"));
				session.setAttribute("workername", rst.getString("worker_name"));
				request.setAttribute("td", "2");
			}
		
			if (userid.equalsIgnoreCase("null")) {
				return SUCCESS;
			}else if(user_role.equals("21")){
				return "GPSuccess";
			}  else if(rb.getAction_type().equals("registred")){
				return "LoginSuccess";
			}else if(rb.getAction_type().equals("migrated")){
				return "MigratedSuccess";
			}else {
				return "invalidata";
			}
		} catch (Exception e) {
			System.out.println("Basic update error---->"+e);
			e.printStackTrace();
			return "fail";
		} finally {
			if (pstmt != null) {

				try {
					pstmt.close();
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

	}

	   
	public RegistrationBeen getModel() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		return rb;
	}

	public void validate() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(true);
		String Msg = "";
		// String wrkraadhar=null;
		String mode = "";
		mode = request.getParameter("mode");
		String wrkraadhar = "" + session.getAttribute("workeraadhaar");
		System.out.println("wrkraadhar"+wrkraadhar);
		String userid = "" + session.getAttribute("user_id");
		if (userid.equalsIgnoreCase("null")) {
			String temp_captcha = "" + session.getAttribute("captcha");
			if (!temp_captcha.equalsIgnoreCase(getRegcaptcha())) {
				addActionError("Captcha Is InValid");
			}
		}

		// added newly on 10/27/2017 union trade
		String union = null;
		union = rb.getUnion_code();
		System.out.println("union------>"+rb.getUnion_code());
		if (union == null) {
			union = "";
		}
		// added on 22/12/2017
		String reg_type = null;
		reg_type = rb.getCheck_registion_login();
		if (reg_type == null) {
		} else if (reg_type.equalsIgnoreCase("public")) {
			if (mode.equalsIgnoreCase("insert")) {
				if (rb.getCheck_worker_name().length() >= 1) {
					Msg = Validations_new.Validate(rb.getCheck_worker_name(), 3, 50, false);
					if (Msg != null) {
						addActionError("Worker Name " + Msg);
					}
				} else {
					addActionError("Worker Name Required");
				}
			}

		}

		if (union.length() > 1) {
			Msg = Validations_new.Validate(union, 2, 2, false);
			if (Msg != null) {
				addActionError("Union Selection" + Msg);
			}
		} else {
			union = "N";
			Msg = Validations_new.Validate(rb.getUnion_code(), 2, 2, false);
			if (Msg != null) {
				addActionError("Union Selection" + Msg);

			}
		}

		if (rb.getAadhar_no().length() >= 1) {
			// Msg = Validations_new.Validate(rb.getAadhar_no(), 2, 12, false);
			boolean verilog = MyUtil.validateVerhoeff(rb.getAadhar_no());
			if (verilog == false) {
				addActionError("Invalid Aadhaar No");
			}
		}
		if (rb.getWorker_name().length() >= 1) {
			Msg = Validations_new.Validate(rb.getWorker_name(), 3, 70, false);
			if (Msg != null) {
				addActionError("Worker Name " + Msg);
			}
		}
		if (String.valueOf(rb.getGender()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getGender()), 2, 1, false);
			if (Msg != null) {
				addActionError(" Gender " + Msg);
			}
		}
		if (rb.getDob().length() >= 1) {
			Msg = Validations_new.Validate(rb.getDob(), 8, 10, false);
			if (Msg != null) {
				addActionError("Date of Birth " + Msg);
			}
		}
		if (rb.getMobile_no().length() >= 1) {
			Msg = Validations_new.Validate(rb.getMobile_no(), 10, 10, false);
			if (Msg != null) {
				addActionError("Mobile Number" + Msg);
			}
		}
		if (rb.getStreet_name_p().length() > 1) {
			Msg = Validations_new.Validate(rb.getStreet_name_p(), 12, 40, true);
			if (Msg != null) {
				addActionError("Permanent Street " + Msg);
			}
		}
//----------------Migrated workers ---------------------
		if(rb.getAction_type().equals("migrated")) {
			if(rb.getAddrs_state_p_txt().length()>1) {
				Msg = Validations_new.Validate(rb.getAddrs_state_p_txt().trim(), 2, 2, false);
				if (Msg != null) {
					addActionError("Permanent State" + Msg);
				}	
			}
			if(rb.getAddrs_state_p_txt().equalsIgnoreCase("28")) {
				if (rb.getAddrs_district_p().length() > 1) {
					Msg = Validations_new.Validate(rb.getAddrs_district_p().trim(), 2, 2, true);
					if (Msg != null) {
						addActionError("Permanent District" + Msg);
					}
				}
				if (rb.getAddrs_mandal_p().length() > 1) {
					Msg = Validations_new.Validate(rb.getAddrs_mandal_p(), 2, 4, true);
					if (Msg != null) {
						addActionError("Permanent Mandal" + Msg);
					}
				}
				if (rb.getAddrs_village_p().length() > 1) {
					Msg = Validations_new.Validate(rb.getAddrs_village_p(), 2, 4, true);
					if (Msg != null) {
						addActionError("Permanent village" + Msg);
					}
				}
			}else {
				if(rb.getCheck_text_validate().equalsIgnoreCase("text_validation")) {
					if (rb.getAddrs_district_p_txt().length() > 1) {
						Msg = Validations_new.Validate(rb.getAddrs_district_p_txt().trim(), 9, 50, true);
						if (Msg != null) {
							addActionError("Permanent District" + Msg);
						}
					}
					if (rb.getAddrs_mandal_p_txt().length() > 1) {
						Msg = Validations_new.Validate(rb.getAddrs_mandal_p_txt(), 9, 50, true);
						if (Msg != null) {
							addActionError("Permanent Mandal" + Msg);
						}
					}
					if (rb.getAddrs_village_p_txt().length() > 1) {
						Msg = Validations_new.Validate(rb.getAddrs_village_p_txt(), 9, 50, true);
						if (Msg != null) {
							addActionError("Permanent village" + Msg);
						}
					}
				}else {
					if (rb.getAddrs_district_p().length() > 1) {
						Msg = Validations_new.Validate(rb.getAddrs_district_p().trim(), 2, 2, true);
						if (Msg != null) {
							addActionError("Permanent District" + Msg);
						}
					}
					if (rb.getAddrs_mandal_p().length() > 1) {
						Msg = Validations_new.Validate(rb.getAddrs_mandal_p(), 2, 4, true);
						if (Msg != null) {
							addActionError("Permanent Mandal" + Msg);
						}
					}
					if (rb.getAddrs_village_p().length() > 1) {
						Msg = Validations_new.Validate(rb.getAddrs_village_p(), 2, 4, true);
						if (Msg != null) {
							addActionError("Permanent village" + Msg);
						}
					}
				}
				
				
			}
			
		}
		//--------------End Migrated worker --------------------------
		if (String.valueOf(rb.getMarital_status()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getMarital_status()), 2, 1, false);
			if (Msg != null) {
				addActionError("Marital Status" + Msg);
			}
		}

		if (rb.getRelation_name().length() >= 1) {
			Msg = Validations_new.Validate(rb.getRelation_name(), 23, 70, false);
			if (Msg != null) {
				addActionError("Relation Name Has " + Msg + "Please Give Alphabets Only!");
			}
		}
		if (rb.getMnregs_worker().length() >= 1) {
			Msg = Validations_new.Validate(rb.getMnregs_worker(), 3, 1, false);
			if (Msg != null) {
				addActionError("MNREGS Worker " + Msg);
			}
		}
		if (String.valueOf(rb.getWork_days()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getWork_days()), 2, 3, false);
			if (Msg != null) {
				addActionError("No of Work days in Year" + Msg);
			}
			if (rb.getWork_days() > 365) {
				addActionError("No of Work days not more then 365");
			}
		}
		if (String.valueOf(rb.getCast()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getCast()), 2, 3, false);
			if (Msg != null) {
				addActionError("Community" + Msg);
			}
		}
		if (String.valueOf(rb.getTrade()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getTrade()), 2, 2, false);
			if (Msg != null) {
				addActionError("Trade of the worker" + Msg);
			}
		}
		if (rb.getAlo_code().length() >= 1) {
			Msg = Validations_new.Validate(rb.getAlo_code(), 4, 4, false);
			if (Msg != null) {
				addActionError("ALO Circle" + Msg);
			}
		}
		
		// work place details
		if (rb.getWork_place_dist().length() > 1) {
			Msg = Validations_new.Validate(rb.getWork_place_dist().trim(), 2, 2, false);
			if (Msg != null) {
				addActionError("Work Place District" + Msg);
			}
		}
		if (rb.getWork_place_mandal().length() > 1) {
			Msg = Validations_new.Validate(rb.getWork_place_mandal(), 2, 4, false);
			if (Msg != null) {
				addActionError("Work Place Mandal" + Msg);
			}
		}
		if (rb.getWork_place_village().length() > 1) {
			Msg = Validations_new.Validate(rb.getWork_place_village(), 2, 14, false);
			if (Msg != null) {
				addActionError("Work Place Village" + Msg);
			}
		}
		if (rb.getWork_employer_name().length() >= 1) {
			Msg = Validations_new.Validate(rb.getWork_employer_name(), 3, 100, false);
			if (Msg != null) {
				addActionError("Name of the Employer " + Msg);
			}
		}
		if (rb.getWork_place_location().length() >= 1) {
			Msg = Validations_new.Validate(rb.getWork_place_location(), 12, 100, false);
			if (Msg != null) {
				addActionError("Work Place Location" + Msg);
			}
		}
		// address valudations
		if (rb.getChecksameaddr() == null) {
			if (rb.getStreet_name_c().length() > 1) {
				Msg = Validations_new.Validate(rb.getStreet_name_c(), 12, 40, false);
				if (Msg != null) {
					addActionError("Present Street " + Msg);
				}
			}
			if (rb.getAddrs_district_c().length() > 1) {
				Msg = Validations_new.Validate(rb.getAddrs_district_c().trim(), 2, 2, false);
				if (Msg != null) {
					addActionError("Present District" + Msg);
				}
			}
			if (rb.getAddrs_mandal_c().length() > 1) {
				Msg = Validations_new.Validate(rb.getAddrs_mandal_c(), 2, 4, false);
				if (Msg != null) {
					addActionError("Present Mandal" + Msg);
				}
			}
			if (rb.getAddrs_pss_village_c().length() > 1) {
				Msg = Validations_new.Validate(rb.getAddrs_pss_village_c(), 2, 5, false);
				if (Msg != null) {
					addActionError("Present village" + Msg);
				}
			}

		}
//		if (rb.getAddrs_village_c().length() > 1) {
//			Msg = Validations_new.Validate(rb.getAddrs_village_c(), 2, 14, false);
//			if (Msg != null) {
//				addActionError("Present Habitation" + Msg);
//			}
//		}
//		if (rb.getAlo_app_num()> 0) {
//			Msg = Validations_new.Validate(String.valueOf(rb.getAlo_app_num()), 2, 6, false);
//			
//			if (Msg != null) {
//				addActionError("ALO Application Number" + Msg);
//			}
//		}

		// file validations
//*******old work exp photo validation* *****/
//		String filename = null;
//		if (getWrkr_exp_photo() != null) {
//			filename = getWrkr_exp_photoFileName();
//			if ((!filename.endsWith(".JPEG")) && (!filename.endsWith(".gif")) && (!filename.endsWith(".jpg"))
//					&& (!filename.endsWith(".png")) && (!filename.endsWith(".jpeg"))) {
//				addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
//			}
//			if (getWrkr_exp_photo().length() >= (1024 * 40)) {
//				double length = (getWrkr_exp_photo().length() / 1024) / 1024;
//				addActionError("File Must below 40KB only ");
//			}
//		}
		/*******old work exp photo validation end * *****/
		String filename = null;
		if (getWrkr_exp_photo() != null) {
			filename = getWrkr_exp_photoFileName();
			if((!filename.endsWith(".PDF")) && (!filename.endsWith(".pdf")))
				 {
				addActionError("Work Experience Certificate must be of pdf");
			}
			if (getWrkr_exp_photo().length() >= (1024 * 500)) {
				double length = (getWrkr_exp_photo().length() / 1024) / 1024;
				addActionError("Work Experience Certificate must be 500KB only ");
			}
		}
		

		
		
		System.out.println("file Name is--->"+getWrkr_form21());
//		if(wrkraadhar.length()<4) {
//			filename = getWrkr_form21FileName();
//			 if((!filename.endsWith(".PDF")) && (!filename.endsWith(".pdf")))
//			 {
//			 addActionError("Form XXVII Must be in pdf");
//			 }
//			 if(getWrkr_form21().length() >= (1024*500)) {
//			 double length = (getWrkr_form21().length()/1024)/1024;
//			 addActionError("Form XXVII Must below 500KB only ");
//			 }
//		}else {
//		if (getWrkr_form21() != null) {
//			filename = getWrkr_form21FileName();
//			System.out.println("filename is--->"+filename);
//			 if((!filename.endsWith(".PDF")) && (!filename.endsWith(".pdf")))
//			 {
//			 addActionError("Form XXVII Must be in pdf");
//			 }
//			 if(getWrkr_form21().length() >= (1024*500)) {
//			 double length = (getWrkr_form21().length()/1024)/1024;
//			 addActionError("Form XXVII Must below 500KB only ");
//			 }
//		}
//		}
		filename = getWrkr_photoFileName();
		if (filename == null) {
		} else {
			if ((!filename.endsWith(".JPEG")) && (!filename.endsWith(".JPG")) && (!filename.endsWith(".gif"))
					&& (!filename.endsWith(".jpg")) && (!filename.endsWith(".PNG")) && (!filename.endsWith(".png"))
					&& (!filename.endsWith(".jpeg"))) {
				addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
			}
			if (getWrkr_photo().length() >= (1024 * 15)) {
				double length = (getWrkr_photo().length() / 1024) / 1024;
				addActionError("File Must below 15KB only ");
			}
		}

		filename = getWrkr_exp_photoFileName();
		if (filename == null) {
		} else {
			if (getWrkr_exp_photo() != null) {
				filename = getWrkr_exp_photoFileName();
				if ((!filename.endsWith(".PDF")) && (!filename.endsWith(".pdf")) ) {
					addActionError("File Must be of type pdf");
				}
				if (getWrkr_exp_photo().length() >= (1024 * 500)) {
					double length = (getWrkr_exp_photo().length() / 1024) / 1024;
					addActionError("File Must below 500KB only ");
				}
			}
		}

	}

}
