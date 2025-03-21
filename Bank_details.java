package Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

import DBC.Validations_new;
import beens.MyUtil;
import beens.RegistrationBeen;
import payments.PaymentBean;
import payments.PaymentBeanResp;
import payments.PaymentMethods;

import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import java.io.File;

public class Bank_details extends ActionSupport implements ModelDriven<RegistrationBeen> {

	/**
		 * 
		 
	private static final long serialVersionUID = 1L;
	File receipt_upload;
	String receipt_uploadFileName;
	String action_type;

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public File getReceipt_upload() {
		return receipt_upload;
	}

	public void setReceipt_upload(File receipt_upload) {
		this.receipt_upload = receipt_upload;
	}

	public String getReceipt_uploadFileName() {
		return receipt_uploadFileName;
	}

	public void setReceipt_uploadFileName(String receipt_uploadFileName) {
		this.receipt_uploadFileName = receipt_uploadFileName;
	}

	RegistrationBeen rb = new RegistrationBeen();
	PreparedStatement pstmt = null, pstmt11 = null;
	File receipt_upload1 = rb.getReceipt_upload();
	String receipt_upload1FileName;

	public File getReceipt_upload1() {
		return receipt_upload1;
	}

	public void setReceipt_upload1(File receipt_upload1) {
		this.receipt_upload1 = receipt_upload1;
	}

	public String getReceipt_upload1FileName() {
		return receipt_upload1FileName;
	}

	public void setReceipt_upload1FileName(String receipt_upload1FileName) {
		this.receipt_upload1FileName = receipt_upload1FileName;
	}

	public String bankdetails() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		String aadharno = MyUtil.filterBad("" + session.getAttribute("workeraadhaar"));
		String alocode = MyUtil.filterBad("" + session.getAttribute("workeralocode"));
		String temp_id=(""+session.getAttribute("temp_id")).trim();
		String userid =MyUtil.filterBad(""+session.getAttribute("user_id"));
		Connection con = null;
		int rs2 = 0;
		String sql="";
		String ifsccode = null;
		// changed as on 170418 branch value as ifsc code
		ifsccode = rb.getBranch_name();
		
		// //--------------------------------
		 if(rb.getIfsccode1()!=null){
		 ifsccode=rb.getIfsccode1();
		 }else if(rb.getIfsccode_new()!=null){
		 ifsccode=rb.getIfsccode_new();
		 }
		 if(ifsccode==null){
			 ifsccode="NA";
		 }
	    	ifsccode=ifsccode.trim();
		// -----------------------------------
		int tr_noI = beens.MyUtil.logTrace(userid, "worker_payments", "Insert", request);
		try {
			con = DBC.DBConnection.getConnection();
			int sno = 01;
			String fileds = "SNO$$$$$" + sno;
			
			// commented as on 28-08-2020 as the work flow changes regarding payment
			/*int checkpaydup=MyUtil.DuplicateCheck("worker_payments", "temp_id='"+temp_id.trim()+"'", "");
			String chndate = beens.MyUtil.ChDate1(rb.getReceipt_date());
			
			if(checkpaydup==0){
				 sql = "INSERT INTO worker_payments(\n"
						+ "            temp_id, alo_code, subscription_years, subscription_amount, registration_amount, total_amount, payment_type,  \n"
						+ "             receipt_no, total_workers_in_receipt, serial_no_worker_receipt, \n"
						+ "               rec_status,trno,receipt_date,renewal_count)VALUES ( ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?);";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, Integer.parseInt(temp_id));
				pstmt.setString(2, alocode);
				pstmt.setInt(3, rb.getSubscription_years());
				pstmt.setInt(4, rb.getSubscription_fee_off());
				pstmt.setInt(5, rb.getReg_fee_off());
				pstmt.setInt(6, rb.getTotal_fee_off());
				pstmt.setString(7, rb.getFee_type());
				pstmt.setString(8, rb.getReceipt_no());
				pstmt.setInt(9, rb.getTotal_workes());
				pstmt.setInt(10, rb.getSerial_worker_no());
				pstmt.setString(11, "A");
				pstmt.setInt(12, tr_noI);
				pstmt.setDate(13, java.sql.Date.valueOf(chndate));
				pstmt.setString(14,"RN");
				rs2 = pstmt.executeUpdate();
			}
			
			int tr_noupdate = beens.MyUtil.logTrace(userid, "worker_attachments", "Update", request);
			DBC.DBConnection.insertPhoto("worker_attachments", "update", "challan_receipt", rb.getReceipt_upload(),
					fileds, "integer", tr_noupdate, temp_id, alocode);*/
			// bank details
			if(ifsccode.length()==11){
				int ifsccheck=MyUtil.DuplicateCheck("public.ifsc", "ifsccode='"+ifsccode+"'", "");	
				if(ifsccheck>0){
					int tr_no_bank = beens.MyUtil.logTrace(userid, "worker_bank_acct_details", "Insert", request);
					sql = "INSERT INTO worker_bank_acct_details(temp_id,ifsc_code, account_no, rec_status,trno,alo_code)"
							+ "VALUES (?, ?, ?, ?, ?,?);";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(temp_id));
					pstmt.setString(2, ifsccode);
					pstmt.setString(3, rb.getAccount_num());
					pstmt.setString(4, "A");
					pstmt.setInt(5, tr_no_bank);
					pstmt.setString(6, alocode);
					int x = pstmt.executeUpdate();		
				}
			}
			
			//receipt valid upto date update on 070518
			/*String reciptidate=rb.getReceipt_date();
			   String valid_upto=MyUtil.getValidUpto(reciptidate, rb.getSubscription_years(),"new");				    
				int updcount=MyUtil.UpdateColumns("wr_basic_details", "rec_valid_upto='"+MyUtil.ChDate1(valid_upto)+"',reg_date='"+chndate+"'", "worker_aadhaar_no='"+aadharno.trim()+"'");
				if(updcount==0){
					return "failure";	
				}*/
				
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Bank Details Insert--->"+e);
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
	}

	
	
	
	
	
	public String online_banck_details() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession(false);
		System.out.println("in bank details"+rb.getFee_type());

		String aadharno = MyUtil.filterBad("" + session.getAttribute("workeraadhaar"));
		String alocode = MyUtil.filterBad("" + session.getAttribute("workeralocode"));
		String temp_id=(""+session.getAttribute("temp_id")).trim();
		String userid =MyUtil.filterBad(""+session.getAttribute("user_id"));
		Connection con = null;
		int rs2 = 0;
		String fee_typr=rb.getFee_type();
		
		String ifsccode = null;
		// changed as on 170418 branch value as ifsc code
		ifsccode = rb.getBranch_name();
		
		// //--------------------------------
		 if(rb.getIfsccode1()!=null){
		 ifsccode=rb.getIfsccode1();
		 }else if(rb.getIfsccode_new()!=null){
		 ifsccode=rb.getIfsccode_new();
		 }
		 if(ifsccode==null){
			 ifsccode="NA";
		 }
	    	ifsccode=ifsccode.trim();
		// -----------------------------------
		int tr_noI = beens.MyUtil.logTrace(userid, "worker_payments", "Insert", request);
		System.out.println(rb.getFee_type().equalsIgnoreCase("OFF"));
		try {
			con = DBC.DBConnection.getConnection();
			int sno = 01;
			String fileds = "SNO$$$$$" + sno;
			int checkpaydup=MyUtil.DuplicateCheck("worker_payments", "temp_id='"+temp_id.trim()+"'", "");
			String sql="";
			if(fee_typr.equalsIgnoreCase("ON")){
			/*if(checkpaydup==0){*/
				PaymentMethods pm=new PaymentMethods();
				PaymentBean pb=new PaymentBean();
				pb.setReference_id(Integer.parseInt(temp_id));
				pb.setPayment_type("REG");
				pb.setTot_amount(rb.getTotal());
				pb.setSubscription_years(rb.getSub_years());
				System.out.println("rb.getSub--->"+rb.getSubscription_years());
				pb.setPayment_gateway("BILDDESK");
				/*pb.setResponseURL(MyUtil.GetFieldName("public.mst_payment_type", "resp_url", "payment_type='DUPID'"));*/
			pb.setResponseURL("RegistrationPaymentGatewayResponse.jsp");
				PaymentBeanResp resp=	pm.PaymentRegProsses(pb);
				System.out.println("resp is---->"+resp.getMsg());
				System.out.println("response is->"+resp.getMsg());
				if(resp.getMessage().equalsIgnoreCase("success")){
					request.setAttribute("payment_msg", resp.getMsg());
					return "reg_pay_process";
				}
				
		}
			
			else if(fee_typr.equalsIgnoreCase("OFF")){
				String chndate = beens.MyUtil.ChDate1(rb.getReceipt_date());
				
				if(checkpaydup==0){
					 sql = "INSERT INTO worker_payments(\n"
							+ "            temp_id, alo_code, subscription_years, subscription_amount, registration_amount, total_amount, payment_type,  \n"
							+ "             receipt_no, total_workers_in_receipt, serial_no_worker_receipt, \n"
							+ "               rec_status,trno,receipt_date,renewal_count)VALUES ( ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?);";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1, Integer.parseInt(temp_id));
					pstmt.setString(2, alocode);
					pstmt.setInt(3, rb.getSubscription_years());
					pstmt.setInt(4, rb.getSubscription_fee_off());
					pstmt.setInt(5, rb.getReg_fee_off());
					pstmt.setInt(6, rb.getTotal_fee_off());
					pstmt.setString(7, rb.getFee_type());
					pstmt.setString(8, rb.getReceipt_no());
					pstmt.setInt(9, rb.getTotal_workes());
					pstmt.setInt(10, rb.getSerial_worker_no());
					pstmt.setString(11, "A");
					pstmt.setInt(12, tr_noI);
					pstmt.setDate(13, java.sql.Date.valueOf(chndate));
					pstmt.setString(14,"RN");
					System.out.println("fdkgndfngdngdl"+pstmt);
					rs2 = pstmt.executeUpdate();
				}
				
				int tr_noupdate = beens.MyUtil.logTrace(userid, "worker_attachments", "Update", request);
				DBC.DBConnection.insertPhoto("worker_attachments", "update", "challan_receipt", rb.getReceipt_upload(),
						fileds, "integer", tr_noupdate, temp_id, alocode);
				String reciptidate=rb.getReceipt_date();
				   String valid_upto=MyUtil.getValidUpto(reciptidate, rb.getSubscription_years(),"new");				    
					int updcount=MyUtil.UpdateColumns("wr_basic_details", "rec_valid_upto='"+MyUtil.ChDate1(valid_upto)+"',reg_date='"+chndate+"'", "worker_aadhaar_no='"+aadharno.trim()+"'");
					if(updcount==0){
						return "failure";	
					}
			
			}
			return "challan_details_process";
			}
			
			catch (Exception e) {
			e.printStackTrace();
			System.out.println("Bank Details Insert--->"+e);
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
	}

	
	
	
	public void validate() {
		String branch=rb.getBranch_name();
		String Msg="";
		
		String action_type=rb.getAction_type();
		System.out.println("action_type-------->"+action_type);
		
//		if (branch != null) {
//			 Msg = Validations_new.Validate(rb.getBranch_name(), 4, 11, false);
//			if(Msg.length()>1){
//				int ifsccheck=MyUtil.DuplicateCheck("public.ifsc", "ifsccode='"+rb.getBranch_name()+"'", "");	
//				if(ifsccheck==0){
//					addActionError("Bank Name " + Msg);		
//				}
//			}
		
//		}
//		if(rb.getAccount_num().length()>0){
//		Msg = Validations_new.Validate(rb.getAccount_num(), 9, 30, false);
//		if (Msg != null) {
//			addActionError("Account Number " + Msg);
//		}
//		}
//		if(rb.getBank_name().length()>0){
//			Msg = Validations_new.Validate(rb.getBank_name(), 9, 150, false);
//			if (Msg != null) {
//				addActionError("Bank Name " + Msg);
//			}
//			}
		// payment details validations
if(action_type.equalsIgnoreCase("payment_action")){
String fee_typr=rb.getFee_type();
if(fee_typr.equalsIgnoreCase("OFF")){
		if (rb.getReceipt_no().length() >= 1) {
			Msg = Validations_new.Validate(rb.getReceipt_no(), 4, 20, false);
			if (Msg != null) {
				addActionError("Challan Number " + Msg);
			}
		}
		if (rb.getReceipt_date().length() >= 1) {
			Msg = Validations_new.Validate(rb.getReceipt_date(), 8, 10, false);
			if (Msg != null) {
				addActionError("Challan Date " + Msg);
			}else{
			String dtval=MyUtil.checkCurrentDateValid(rb.getReceipt_date(), MyUtil.gettodaysDate())	;
			if(dtval.equalsIgnoreCase("N")){
				addActionError("Challan Date Must before today date");
			}
			}
		}
		if (String.valueOf(rb.getTotal_workes()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getTotal_workes()), 2, 4, false);
			if (Msg != null) {
				addActionError(" Total Workers in Challan " + Msg);
			}
		}
		if (String.valueOf(rb.getSerial_worker_no()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getSerial_worker_no()), 2, 4, false);
			if (Msg != null) {
				addActionError(" Serial Number of Worker " + Msg);
			}
		}
		if (String.valueOf(rb.getSubscription_years()).length() >= 1) {
			Msg = Validations_new.Validate(String.valueOf(rb.getSubscription_years()), 2, 1, false);
			if (Msg != null) {
				addActionError("Subscription Years " + Msg);
			}
			if (rb.getSubscription_years() == 0 || rb.getSubscription_years() > 5) {
				addActionError("Subscription Years Between 1-5 Years");
			}
		}
		String filename = "";
		filename = getReceipt_uploadFileName();
		if ((!filename.endsWith(".gif")) && (!filename.endsWith(".jpg")) && (!filename.endsWith(".png"))
				&& (!filename.endsWith(".jpeg"))) {
			addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
		}
		if (rb.getReceipt_upload().length() >= (1024 * 4000)) {
			double length = (rb.getReceipt_upload().length() / 1024) / 1024;
			addActionError("File Must below 40KB only ");
		}
}
}
	}

	public RegistrationBeen getModel() {
		return rb;
	}
}
