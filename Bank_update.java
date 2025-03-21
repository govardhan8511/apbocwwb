package Registration;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import DBC.Validations_new;
import beens.MyUtil;
import beens.RegistrationBeen;

public class Bank_update extends ActionSupport implements ModelDriven<RegistrationBeen> {
	String ifsccode_new;
    public String getIfsccode_new() {
	return ifsccode_new;
}
public void setIfsccode_new(String ifsccode_new) {
	this.ifsccode_new = ifsccode_new;
}

	RegistrationBeen rb = new RegistrationBeen();
    PreparedStatement pstmt=null,pstmt11 = null;
    File challanuplaod = rb.getReceipt_upload();    
    File receipt_upload;
    String receipt_uploadFileName;

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
	public File getChallanuplaod() {
		return challanuplaod;
	}
	public void setChallanuplaod(File challanuplaod) {
		this.challanuplaod = challanuplaod;
	}
	
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession(false);

        String aadharno = "" + session.getAttribute("workeraadhaar");
        String alocode = "" + session.getAttribute("workeralocode");

//String aadharno = "450147806281";
//        String alocode = "B121";
        File receipt_upload1 = rb.getReceipt_upload();
        Connection con = null;
        ResultSet rst = null;
        int rs2=0;
	public String bank_update() throws Exception{	
		HttpServletRequest request = ServletActionContext.getRequest();
	    HttpSession session = request.getSession(false);
	    String aadharno =  MyUtil.filterBad("" + session.getAttribute("workeraadhaar"));
	    String alocode =  MyUtil.filterBad("" + session.getAttribute("workeralocode"));
	    String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
	    String temp_id=MyUtil.filterBad((""+session.getAttribute("temp_id")).trim());
	    Date d = new Date();
	    Timestamp ts = new Timestamp(d.getTime());
	    String stamp = ts.toString();
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rst = null;
	    String ifsccode=null;
	    File receipt_upload1 = rb.getReceipt_upload();
	    con = DBC.DBConnection.getConnection();
	    try{
	    	if(rb.getIfsccode1()!=null){
	    		ifsccode=rb.getIfsccode1();
	    	}else if(rb.getIfsccode_new()!=null){
	    		ifsccode=rb.getIfsccode_new();
	    	}
	    	if(ifsccode==null){
	    		ifsccode="NA";
	    	}
	    	ifsccode=ifsccode.trim();
	    	if(ifsccode.length()==11){
				int ifsccheck=MyUtil.DuplicateCheck("public.ifsc", "ifsccode='"+ifsccode+"'", "");	
				if(ifsccheck>0){
					int ifscrec=MyUtil.DuplicateCheck("worker_bank_acct_details", "temp_id='"+temp_id.trim()+"'", "");
					System.out.println("ifscrec is--->"+ifscrec);
					if(ifscrec==0){
						int tr_no_bank = beens.MyUtil.logTrace(userid, "worker_bank_acct_details", "Insert", request);
						String sql = "INSERT INTO worker_bank_acct_details(temp_id,  ifsc_code, account_no, rec_status,trno,alo_code,rec_ins_dt,ins_status)"
								+ "VALUES (?, ?, ?, ?, ?, ?,now(),'A');";
						pstmt = con.prepareStatement(sql);
						pstmt.setInt(1, Integer.parseInt(temp_id.trim()));
						pstmt.setString(2, ifsccode);
						pstmt.setString(3, rb.getAccount_num());
						pstmt.setString(4, "A");
						pstmt.setInt(5, tr_no_bank);
						pstmt.setString(6, alocode);
						int x = pstmt.executeUpdate();	
					}else{
						int tr_no_bank = beens.MyUtil.logTrace(userid, "public.worker_bank_acct_details", "Update", request);
					    String sql = "update worker_bank_acct_details set account_no=?, ifsc_code=?, alo_code=?,trno=?,rec_edited_dt=now() where temp_id=?";
					    pstmt = con.prepareStatement(sql);
					    pstmt.setString(1, rb.getAccount_num().trim());
					    //hear alo change as on 17-04-18 branch value as ifsc code
					    pstmt.setString(2, rb.getBranch_name());
					    pstmt.setString(3, alocode);
					    pstmt.setInt(4, tr_no_bank);
						pstmt.setInt(5, Integer.parseInt(temp_id.trim()));
						System.out.println("pstm is--->"+pstmt);
					    int x = pstmt.executeUpdate();
					}
	    
	    }
	    }
	   /* int sno = 01;
	    String fileds = "SNO$$$$$" + sno;
	    if(pstmt!=null) {
	    	pstmt.close();
	    }
	    int tr_noupdate = beens.MyUtil.logTrace(userid, "worker_attachments", "Insert", request);
	    File  file_photo=rb.getReceipt_upload();
	    if(file_photo!=null){
	    DBC.DBConnection.insertPhoto("worker_attachments", "update", "challan_receipt", rb.getReceipt_upload(), fileds, "integer", tr_noupdate, temp_id, alocode);
	    }
		int tr_npay = beens.MyUtil.logTrace(userid, "public.worker_payments", "Update", request);
		 String chndate = beens.MyUtil.ChDate1(rb.getReceipt_date());
		   String sqlpay="UPDATE worker_payments SET     receipt_date=?, receipt_no=?, total_workers_in_receipt=?, serial_no_worker_receipt=?,  trno=?,"
		   		+ "subscription_years=?, subscription_amount=?, registration_amount=?, total_amount=?  WHERE temp_id=? ";
		   pstmt=con.prepareStatement(sqlpay);
		   pstmt.setDate(1,java.sql.Date.valueOf(chndate));
		   pstmt.setString(2,rb.getReceipt_no().trim());
		   pstmt.setInt(3, rb.getTotal_workes());
			pstmt.setInt(4, rb.getSerial_worker_no());
			pstmt.setInt(5,tr_npay);
			 pstmt.setInt(6, rb.getSubscription_years());
			 pstmt.setInt(7, rb.getSubscription_fee_off());
				pstmt.setInt(8, rb.getReg_fee_off());
				pstmt.setInt(9, rb.getTotal_fee_off());
			pstmt.setInt(10,Integer.parseInt(temp_id));
		   pstmt.executeUpdate();
		 //receipt valid upto date update on 070518
			String reciptidate=rb.getReceipt_date();
		   String valid_upto=MyUtil.getValidUpto(reciptidate, rb.getSubscription_years(),"new");				    
		   int updcount=MyUtil.UpdateColumns("wr_basic_details", "rec_valid_upto='"+MyUtil.ChDate1(valid_upto)+"',reg_date='"+chndate+"'", "worker_aadhaar_no='"+aadharno.trim()+"'");
		   if(updcount==0){
				return "failure";	
			}*/
		    return SUCCESS;
	    } catch (Exception e) {
	    	System.out.println("updcount e------------->"+e);
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
        String Msg = Validations_new.Validate(rb.getBank_name(), 1, 50, false);

        
        // commented as on 28-08-2020
//         if (rb.getReceipt_no().length() >= 1) {
//    	         Msg = Validations_new.Validate(rb.getReceipt_no(),4,20, false);
//    	         if (Msg != null) {
//    	             addActionError("Challan Number " + Msg);
//    	         }
//    	     }
//    		 if (rb.getReceipt_date().length() >= 1) {
//    	         Msg = Validations_new.Validate(rb.getReceipt_date(), 8, 10, false);
//    	         if (Msg != null) {
//    	             addActionError("Challan Date " + Msg);
//    	         }else{
//    	        	 try{
//    	 			String dtval=MyUtil.checkCurrentDateValid(rb.getReceipt_date(), MyUtil.gettodaysDate())	;
//    				if(dtval.equalsIgnoreCase("N")){
//    					addActionError("Challan Date Must before today date");
//    				}
//    	        	 }catch(Exception e){
//    	        		 addActionError("Invalid Challan Date");
//    	        	 }
//    				}
//    	     }
//    		 if (String.valueOf(rb.getTotal_workes()).length() >= 1) {
//    	         Msg = Validations_new.Validate(String.valueOf(rb.getTotal_workes()), 2, 4, false);
//    	         if (Msg != null) {
//    	             addActionError(" Total Workers in Challan " + Msg);
//    	         }
//    	     }
//    		 if (String.valueOf(rb.getSerial_worker_no()).length() >= 1) {
//    	         Msg = Validations_new.Validate(String.valueOf(rb.getSerial_worker_no()), 2, 4, false);
//    	         if (Msg != null) {
//    	             addActionError(" Serial Number of Worker " + Msg);
//    	         }
//    	     }
//    		
//    	            String  filename=null;
//    	            filename = getReceipt_uploadFileName();
//    	                 if(filename==null){
//    	            
//    	                 }else {
//    	                	 
//        	                 if((!filename.endsWith(".gif")) && (!filename.endsWith(".jpg")) && (!filename.endsWith(".png")) && (!filename.endsWith(".jpeg"))) {
//    	                         addActionError("File Must be either one of this types .gif or .jpg or .png and .jpeg");
//    	                         }
//    	                     if(rb.getReceipt_upload().length() >= (1024*4000)) {
//    	                         double length = (rb.getReceipt_upload().length()/1024)/1024;
//    	                         addActionError("File Must below 40KB only ");
//    	                     }
//    	                 }
//    	                     	        
    }
	 public RegistrationBeen getModel() {
	        return rb;
	    }
}
	
