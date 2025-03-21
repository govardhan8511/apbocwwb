package payments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import Reports.ReportBean;
import beens.MyUtil;
import java.util.*;

public class PaymentActions extends ActionSupport  implements ModelDriven<PaymentBean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PaymentBean pb = new PaymentBean();
	PaymentBean pbd = new PaymentBean();
	String msg;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	HttpServletRequest request = ServletActionContext.getRequest();
	HttpSession session = request.getSession(false);
	Connection cnctn = null;
	PreparedStatement prpstmt = null;
	ResultSet rst = null;
	String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
	String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("alocode")));
	
	public String OnlinePaymentProcess()  throws Exception {	
		PaymentMethods pm=new PaymentMethods();
		PaymentBeanResp resp=pm.PaymentProsses(pb);
		if(resp.getMessage().equalsIgnoreCase("exception")){
			return "exception";
		}else if(resp.getMessage().equalsIgnoreCase("success")){
			return "success";
		}else{
			return "undefined";
		}		
	}

	public String OnlinePaymentResp() {
		PaymentMethods pm=new PaymentMethods();
		String response_string =""+request.getParameter("msg");
		
		/*String response_string=getMsg();*/
		pbd=pm.GetRespValus(response_string);
		String resp=pm.PaymentUpdate(pbd);
	cnctn=DBC.DBConnection.getConnection();
		 if(resp.equalsIgnoreCase("SU")){			
			int trno = beens.MyUtil.logTrace(userid, "duplicatecard_challan", "Insert", request);
			try{
				String worker_reg_no =MyUtil.GetFieldName("wr_basic_details", "worker_reg_no", "temp_id='"+pbd.getAddl_info2()+"'");
			String chlnins = "INSERT INTO public.duplicatecard_challan(temp_id, challan_dt, challan_amnt,  trno,payment_type,payment_transid,worker_reg_no) "
					+ " VALUES (?, ?, ?, ?, ?,?,?)";
			prpstmt = cnctn.prepareStatement(chlnins);
			prpstmt.setInt(1,Integer.parseInt(pbd.getAddl_info2()));
			prpstmt.setString(2, beens.MyUtil.ChDate1(MyUtil.gettodaysDate()));
			prpstmt.setDouble(3, pbd.getTxn_amount());
			prpstmt.setInt(4, trno);
			prpstmt.setString(5,"ON");
			prpstmt.setString(6,pbd.getPayment_transid());
			prpstmt.setString(7,worker_reg_no);
			prpstmt.executeUpdate();
			if (prpstmt != null) {
				prpstmt.close();
			}
			String adharchk = "select * from wr_basic_details where  temp_id = ? ";
			prpstmt = cnctn.prepareStatement(adharchk);
			prpstmt.setInt(1,Integer.parseInt(pbd.getAddl_info2()));
			rst = prpstmt.executeQuery();
			while (rst.next()) {
				session.setAttribute("cardtype", "duplicate");
				session.setAttribute("dupworkerid",
						StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(rst.getString("worker_id"))));

			}
			}catch(Exception e){
			return "failure";
			}
			return "success";
		}else{
			String error_msg=MyUtil.GetFieldName("mst_online_pmt_internal_error", "", "internal_error_code='"+pbd.getLoc_error_msg()+"'");
			if(pbd.getLoc_error_msg().equalsIgnoreCase("PE")){
				pb.setLoc_error_msg("Error occured at payment gateway. "+pbd.getError_description());
			}else{
			pb.setLoc_error_msg("Error occured at "+error_msg);
			}
			return ERROR;
		}
	}

	
	
	public String onlineRegistrationStatusUpd() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Date dateobj = new Date();
		 System.out.println(df.format(dateobj));
		 DateFormat df1 = new SimpleDateFormat("dd-MM-YYYY");
		 Date dateobj1 = new Date();
		 System.out.println(df1.format(dateobj1));
		
		String response_string =""+request.getParameter("msg");
		System.out.println("response_string---->"+response_string);
		String temp_id=""+session.getAttribute("temp_id");
		String alocode = MyUtil.filterBad("" + session.getAttribute("workeralocode"));
		String userid =MyUtil.filterBad(""+session.getAttribute("user_id"));
		PaymentMethods pm=new PaymentMethods();
		/*String response_string=getMsg();*/
		pbd=pm.GetRespValus(response_string);
		
		String resp=pm.PaymentUpdate(pbd);
	cnctn=DBC.DBConnection.getConnection();
		 if(resp.equalsIgnoreCase("SU")){			
			int trno = beens.MyUtil.logTrace(temp_id, "worker_payments", "Insert", request);
			try{
				int checkpaydup=MyUtil.DuplicateCheck("worker_payments", "temp_id='"+temp_id.trim()+"'", "");
				String sql="";
				String sub_years=MyUtil.GetFieldName("online_payment_details", "subscription_years", "dept_trans_id='"+pbd.getPayment_transid()+"'");
				System.out.println("sub_years---->"+sub_years);
				if(checkpaydup==0){
					 sql = "INSERT INTO worker_payments(\n"
							+ "            temp_id, alo_code, subscription_years,  total_amount, payment_type,  \n"
							+ "             receipt_no, \n"
							+ "               rec_status,trno,receipt_date)VALUES ( ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?);";
					 prpstmt = cnctn.prepareStatement(sql);
					prpstmt.setInt(1, Integer.parseInt(temp_id));
					prpstmt.setString(2, alocode);
					prpstmt.setInt(3, Integer.parseInt(sub_years));
					prpstmt.setInt(4, pb.getTot_amount());
					prpstmt.setString(7, "ON");
					prpstmt.setString(8, pbd.getTxn_reference_no());
					
					prpstmt.setString(11, "A");
					prpstmt.setInt(12, trno);
					prpstmt.setDate(13, java.sql.Date.valueOf(df.format(dateobj)));
					int i = prpstmt.executeUpdate();
				}
				if (prpstmt != null) {
				prpstmt.close();
			}
			
			   String valid_upto=MyUtil.getValidUpto(df1.format(dateobj1), Integer.parseInt(sub_years),"new");				    
				
			   int updcount=MyUtil.UpdateColumns("wr_basic_details", "rec_valid_upto='"+MyUtil.ChDate1(valid_upto)+"',reg_date='"+df.format(dateobj)+"',approval_status='N',edit_status='N'", "temp_id='"+Integer.parseInt(temp_id)+"'");
				System.out.println("updcount--->"+updcount);
			   if(updcount==0){
					return "failure";	
				}
			   
			   
			}catch(Exception e){
				e.printStackTrace();
			return "failure";
			}
			request.setAttribute("Txn_Ref_no", pbd.getTxn_reference_no());
			request.setAttribute("Bank_Ref_no", pbd.getBank_txn_ref_no());
			request.setAttribute("paying_amount", pbd.getTxn_amount());
			request.setAttribute("cess_paid_date", pbd.getTxn_date());
			request.setAttribute("Error_Description", pbd.getError_description());
			request.setAttribute("payment_trans_id", pbd.getPayment_transid());
			
			return "success";
		}else{
			String error_msg=MyUtil.GetFieldName("mst_online_pmt_internal_error", "", "internal_error_code='"+pbd.getLoc_error_msg()+"'");
			if(pbd.getLoc_error_msg().equalsIgnoreCase("PE")){
				pb.setLoc_error_msg("Error occured at payment gateway. "+pbd.getError_description());
			}else{
			pb.setLoc_error_msg("Error occured at "+error_msg);
			}
			request.setAttribute("Txn_Ref_no", pbd.getTxn_reference_no());
			request.setAttribute("Bank_Ref_no", pbd.getBank_txn_ref_no());
			request.setAttribute("paying_amount", pbd.getTxn_amount());
			request.setAttribute("cess_paid_date", pbd.getTxn_date());
			request.setAttribute("Error_Description", pbd.getError_description());
			request.setAttribute("payment_trans_id", pbd.getPayment_transid());
			return ERROR;
		}
	}

	
	public String onlineRenewalStatusUpd() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		 Date dateobj = new Date();
		 DateFormat df1 = new SimpleDateFormat("dd-MM-YYYY");
		 Date dateobj1 = new Date();
		 String valid_upto=null;
		String response_string =""+request.getParameter("msg");
		System.out.println("response_string is---->"+response_string);
		String alocode = MyUtil.filterBad("" + session.getAttribute("workeralocode"));
		String userid =MyUtil.filterBad(""+session.getAttribute("user_id"));
	String	db_alo_code = null, worker_aadhaar = null,
				wr_dob = null, worker_name = null, relation_name = null;
		String temp_id="";
		String sql="";
		PaymentMethods pm=new PaymentMethods();
		/*String response_string=getMsg();*/
		pbd=pm.GetRespValus(response_string);
		String resp=pm.PaymentUpdate(pbd);
	cnctn=DBC.DBConnection.getConnection();
		try{
				 temp_id=MyUtil.GetFieldName("online_payment_details", "reference_id", "dept_trans_id='"+pbd.getPayment_transid()+"'");
				int trno = beens.MyUtil.logTrace(temp_id, "worker_payments", "Insert", request);
				int checkpaydup=MyUtil.DuplicateCheck("worker_payments", "temp_id='"+temp_id.trim()+"'", "");
				
				 sql = "select worker_reg_no,alo_code,rec_valid_upto,temp_id,worker_reg_no,date_of_birth,worker_name,relation_name from wr_basic_details where temp_id=?";
				prpstmt = cnctn.prepareStatement(sql);
				prpstmt.setInt(1, Integer.parseInt(temp_id));
				rst = prpstmt.executeQuery();
				if (rst.next()) {
					db_alo_code = rst.getString("alo_code");
					worker_aadhaar = rst.getString("worker_reg_no");
					wr_dob = rst.getString("date_of_birth");
					worker_name = rst.getString("worker_name");
					relation_name = rst.getString("relation_name");
					valid_upto=MyUtil.ChDate(rst.getString("rec_valid_upto"));
}
			
				String sub_years=MyUtil.GetFieldName("online_payment_details", "subscription_years", "dept_trans_id='"+pbd.getPayment_transid()+"'");
			if(checkpaydup==0){
					 sql = "INSERT INTO worker_payments(\n"
							+ "            temp_id, alo_code, subscription_years,  total_amount, payment_type,  \n"
							+ "             receipt_no, \n"
							+ "               rec_status,trno,receipt_date)VALUES ( ?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?);";
					 prpstmt = cnctn.prepareStatement(sql);
					prpstmt.setInt(1, Integer.parseInt(temp_id));
					prpstmt.setString(2, alocode);
					prpstmt.setInt(3, Integer.parseInt(sub_years));
					prpstmt.setInt(4, pb.getTot_amount());
					prpstmt.setString(7, "ON");
					prpstmt.setString(8, pbd.getTxn_reference_no());
					
					prpstmt.setString(11, "A");
					prpstmt.setInt(12, trno);
					prpstmt.setDate(13, java.sql.Date.valueOf(df.format(dateobj)));
					int i = prpstmt.executeUpdate();
				}
				if (prpstmt != null) {
				prpstmt.close();
			}
				if(pbd.getAuth_status().equals("0300")){
			    valid_upto=MyUtil.getValidUpto(valid_upto, Integer.parseInt(sub_years),"ren");	
			
			   int updcount=MyUtil.UpdateColumns("wr_basic_details", "rec_valid_upto='"+MyUtil.ChDate1(valid_upto)+"'", "temp_id='"+Integer.parseInt(temp_id)+"'");
				if(updcount==0){
					return "failure";	
				}}
			}catch(Exception e){
				System.out.println("exception is --->"+e);
				e.printStackTrace();
			return "error";
			}
			
			request.setAttribute("txn_reference", pbd.getPayment_transid());
			request.setAttribute("txn_amount", pbd.getTxn_amount());
			request.setAttribute("paymenttxn_id", pbd.getPayment_transid());
			request.setAttribute("Auth_status", pbd.getError_description());
			request.setAttribute("payment_type", "online");
			request.setAttribute("rec_valid_upto",valid_upto);
			request.setAttribute("worker_name", worker_name);
			request.setAttribute("worker_reg_no", worker_aadhaar);
			request.setAttribute("relation_name", relation_name);
			request.setAttribute("temp_id", temp_id);
			request.setAttribute("alo_code", db_alo_code);
			pb.setMessage("success_renewal");
			return "success";
		}
		
	
	
	
	public PaymentBean getModel() {
		// TODO Auto-generated method stub
		return pb;
	}

}
