package payments;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import DBC.DBConnection;
import beens.MyUtil;
import javassist.bytecode.stackmap.BasicBlock.Catch;

public class PaymentMethods {
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpSession session = request.getSession();
	String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
	String ChecksumKey = MyUtil.GetFieldName("public.mst_parameter", "param_value","param_code='20' and status='A'");
    public PaymentBeanResp PaymentProsses(PaymentBean pb){
	/*String merchant_Id = "APBCWB";*/
	String merchant_Id="BDSKUATY";
	String Security_Id = "bdskuaty";
	String Currency_Type = "INR";
	String TypeField_1 = "R";
	String TypeField_2 = "F";	
	
	String msg = "";
	Connection con = null;
	PreparedStatement pstmt = null;
	String trans_id = "";
	ResultSet rs = null;
	String callbackUrl=null;
	int temp_id = pb.getReference_id(), tot_amount = pb.getTot_amount();
	String paymenttype=pb.getPayment_type();
	PaymentBeanResp pbr=new PaymentBeanResp();
	/*PaymentBeanResp pbr=null;*/
	
	/*String json=null;*/
	try {
		con = DBC.DBConnection.getConnection();			
		int trno = beens.MyUtil.logTrace(userid, "online_payment_details", "Insert", request);
		String payment_date = MyUtil.ChDate1(MyUtil.gettodaysDate());
		String emp_regstn = "INSERT INTO public.online_payment_details(reference_id,type_of_payment,  "
				+ "  total_amountpaid, paymentgateway,   date_of_payment,  record_freeze, "
				+ "rec_status, trno, rec_crt_timestamp,amount_interest,amount_penalty,merchant_id,security_id,tot_amount,currency_name,resp_return_url)VALUES (?, ?, ?, ?, ?,'F','A', ?,now(),?,?,?,?,?,?,?)"
				+ " RETURNING payment_transid;";
		pstmt = con.prepareStatement(emp_regstn);
		pstmt.setInt(1, temp_id);
		pstmt.setString(2,paymenttype);
		pstmt.setInt(3, tot_amount);
		pstmt.setString(4,pb.getPayment_gateway());
		pstmt.setString(5, payment_date);
		//trno pending
		pstmt.setInt(6, trno);
		pstmt.setInt(7, 0);
		pstmt.setInt(8, 0);
		pstmt.setString(9,merchant_Id);
		pstmt.setString(10, Security_Id);
		pstmt.setInt(11,tot_amount);
		pstmt.setString(12,Currency_Type);
		pstmt.setString(13,pb.getResponseURL());
		rs = pstmt.executeQuery();
		if (rs.next()) {
			trans_id = rs.getString("payment_transid");
		}
	String hash = "";
		msg = merchant_Id + "|"+trans_id+"|"+"NA"+"|"+2.00+"|"+"NA"+"|"+"NA"+"|"+"NA"
				+"|"+Currency_Type+"|"+"NA"+"|"+TypeField_1+"|"+Security_Id+"|"+"NA"+"|"+"NA"
				+"|"+TypeField_2+"|"+pb.getPayment_type()+"|"+temp_id+"|"+"NA"+"|"+"NA"+"|"+"NA"+"|"+"NA"
				+"|"+"NA"+"|"+"NA"+"";
		hash = CheckSum.HmacSHA256(msg,ChecksumKey);
		msg = msg+"|"+hash.toUpperCase();
	callbackUrl="https://apbocwwb.ap.nic.in/"+pb.getResponseURL()+"";
	 JSONObject obj = new JSONObject();
     obj.put("enableChildWindowPosting", true);
     obj.put("enablePaymentRetry", true);
     obj.put("retry_attempt_count", 2);
     /*obj.put("txtPayCategory","NETBANKING");*/
     JSONObject objnew = new JSONObject();
     objnew.put("msg", msg);
     objnew.put("options", obj);
     objnew.put("callbackUrl", callbackUrl);
	pbr.setMsg(msg);
System.out.println("msg isss->"+msg);
} catch (Exception e) {
	
		pbr.setMessage("exception");
	} finally {
		try{
		if (rs != null) {
			rs.close();
		}
		if (pstmt != null) {
			pstmt.close();
		}
		if (con != null) {
			DbUtils.closeQuietly(con);
		}
		}catch(Exception e){
			
		}
	}
	pbr.setMessage("success");
	return pbr;
}
    
    public PaymentBeanResp PaymentRegProsses(PaymentBean pb){
    	HttpServletRequest request = ServletActionContext.getRequest();
    	HttpServletResponse response = ServletActionContext.getResponse();
    	HttpSession session = request.getSession();
    	String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
    	String ChecksumKey = MyUtil.GetFieldName("public.mst_parameter", "param_value","param_code='20' and status='A'");
    	/*String merchant_Id = "APBCWB";*/
    	String merchant_Id="BDSKUATY";
    	String Security_Id = "bdskuaty";
    	String Currency_Type = "INR";
    	String TypeField_1 = "R";
    	String TypeField_2 = "F";	
    	
    	String msg = "";
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	String trans_id = "";
    	ResultSet rs = null;
    	String callbackUrl=null;
    	int temp_id = pb.getReference_id(), tot_amount = pb.getTot_amount();
    	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyssmmSS");
		SimpleDateFormat format = new SimpleDateFormat("DD-MM-YYYY");
		
		Date date = new Date();
		String	db = formatter.format(date);
    	int sub_years=pb.getSubscription_years();
    	String paymenttype=pb.getPayment_type();
    	PaymentBeanResp pbr=new PaymentBeanResp();
    	String dept_trans_id="APBCW"+temp_id+db;
    	/*PaymentBeanResp pbr=null;*/
    	
    	/*String json=null;*/
    	try {
    		
		
    		con = DBC.DBConnection.getConnection();			
    		int trno = beens.MyUtil.logTrace(userid, "online_payment_details", "Insert", request);
    		String payment_date = MyUtil.ChDate1(MyUtil.gettodaysDate());
    		String emp_regstn = "INSERT INTO public.online_payment_details(reference_id,type_of_payment,  "
    				+ "  total_amountpaid, paymentgateway, date_of_payment,  record_freeze, "
    				+ "rec_status, trno, rec_crt_timestamp,amount_interest,amount_penalty,merchant_id,security_id,tot_amount,currency_name,resp_return_url,subscription_years,dept_trans_id)VALUES (?, ?, ?, ?, ?,'F','A', ?,now(),?,?,?,?,?,?,?,?,?)"
    				+ " RETURNING payment_transid;";
    		pstmt = con.prepareStatement(emp_regstn);
    		pstmt.setInt(1, temp_id);
    		pstmt.setString(2,paymenttype);
    		pstmt.setInt(3, tot_amount);
    		pstmt.setString(4,pb.getPayment_gateway());
    		pstmt.setString(5, payment_date);
    		//trno pending
    		pstmt.setInt(6, trno);
    		pstmt.setInt(7, 0);
    		pstmt.setInt(8, 0);
    		pstmt.setString(9,merchant_Id);
    		pstmt.setString(10, Security_Id);
    		pstmt.setInt(11,tot_amount);
    		pstmt.setString(12,Currency_Type);
    		pstmt.setString(13,pb.getResponseURL());
    		pstmt.setInt(14, sub_years);
    		pstmt.setString(15, dept_trans_id);
    		rs = pstmt.executeQuery();
    		if (rs.next()) {
    			trans_id = rs.getString("payment_transid");
    		}
    		  trans_id="APBCW"+temp_id+db;
    		
    		
    	String hash = "";
    		msg = merchant_Id + "|"+dept_trans_id+"|"+"NA"+"|"+tot_amount+"|"+"NA"+"|"+"NA"+"|"+"NA"
    				+"|"+Currency_Type+"|"+"NA"+"|"+TypeField_1+"|"+Security_Id+"|"+"NA"+"|"+"NA"
    				+"|"+TypeField_2+"|"+pb.getPayment_type()+"|"+temp_id+"|"+"NA"+"|"+"NA"+"|"+"NA"+"|"+"NA"
    				+"|"+"NA"+"|"+"NA"+"";
    		hash = CheckSum.HmacSHA256(msg,ChecksumKey);
    		msg = msg+"|"+hash.toUpperCase();
    	callbackUrl="https://apbocwwb.ap.nic.in/"+pb.getResponseURL()+"";
    	 JSONObject obj = new JSONObject();
         obj.put("enableChildWindowPosting", true);
         obj.put("enablePaymentRetry", true);
         obj.put("retry_attempt_count", 2);
         /*obj.put("txtPayCategory","NETBANKING");*/
         JSONObject objnew = new JSONObject();
         objnew.put("msg", msg);
         objnew.put("options", obj);
         objnew.put("callbackUrl", callbackUrl);
    	pbr.setMsg(msg);

    } catch (Exception e) {
    	System.out.println("exception is--->"+e);
    	e.printStackTrace();
    		pbr.setMessage("exception");
    	} finally {
    		try{
    		if (rs != null) {
    			rs.close();
    		}
    		if (pstmt != null) {
    			pstmt.close();
    		}
    		if (con != null) {
    			DbUtils.closeQuietly(con);
    		}
    		}catch(Exception e){
    			
    		}
    	}
    	pbr.setMessage("success");
    	return pbr;
    }
    
    
    public PaymentBeanResp PaymentRenProsses(PaymentBean pb){
    	HttpServletRequest request = ServletActionContext.getRequest();
    	HttpServletResponse response = ServletActionContext.getResponse();
    	HttpSession session = request.getSession();
    	String userid = MyUtil.filterBad("" + session.getAttribute("user_id"));
    	String ChecksumKey = MyUtil.GetFieldName("public.mst_parameter", "param_value","param_code='20' and status='A'");
    	/*String merchant_Id = "APBCWB";*/
    	String merchant_Id="BDSKUATY";
    	String Security_Id = "bdskuaty";
    	String Currency_Type = "INR";
    	String TypeField_1 = "R";
    	String TypeField_2 = "F";	
    	
    	String msg = "";
    	Connection con = null;
    	PreparedStatement pstmt = null;
    	String trans_id = "";
    	ResultSet rs = null;
    	String callbackUrl=null;
    	int temp_id = pb.getReference_id(), tot_amount = pb.getTot_amount();
    	int sub_years=pb.getSubscription_years();
    	String paymenttype=pb.getPayment_type();
    	PaymentBeanResp pbr=new PaymentBeanResp();
    	SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyssmmSS");
		SimpleDateFormat format = new SimpleDateFormat("DD-MM-YYYY");
		
		Date date = new Date();
		String	db = formatter.format(date);
    	/*PaymentBeanResp pbr=null;*/
    	
    	/*String json=null;*/
    	try {
    		con = DBC.DBConnection.getConnection();			
    		int trno = beens.MyUtil.logTrace(userid, "online_payment_details", "Insert", request);
    		String payment_date = MyUtil.ChDate1(MyUtil.gettodaysDate());
    		 trans_id="APBCW"+temp_id+db;
    		String emp_regstn = "INSERT INTO public.online_payment_details(reference_id,type_of_payment,  "
    				+ "  total_amountpaid, paymentgateway,   date_of_payment,  record_freeze, "
    				+ "rec_status, trno, rec_crt_timestamp,amount_interest,amount_penalty,merchant_id,security_id,tot_amount,currency_name,resp_return_url,subscription_years,dept_trans_id)VALUES (?, ?, ?, ?, ?,'F','A', ?,now(),?,?,?,?,?,?,?,?,?)"
    				+ " RETURNING payment_transid;";
    		pstmt = con.prepareStatement(emp_regstn);
    		pstmt.setInt(1, temp_id);
    		pstmt.setString(2,paymenttype);
    		pstmt.setInt(3, tot_amount);
    		pstmt.setString(4,pb.getPayment_gateway());
    		pstmt.setString(5, payment_date);
    		//trno pending
    		pstmt.setInt(6, trno);
    		pstmt.setInt(7, 0);
    		pstmt.setInt(8, 0);
    		pstmt.setString(9,merchant_Id);
    		pstmt.setString(10, Security_Id);
    		pstmt.setInt(11,tot_amount);
    		pstmt.setString(12,Currency_Type);
    		pstmt.setString(13,pb.getResponseURL());
    		pstmt.setInt(14, sub_years);
    		pstmt.setString(15, trans_id);
    		rs = pstmt.executeQuery();
    		if (rs.next()) {
    			 trans_id="APBCW"+temp_id+db;
    		}
    	String hash = "";
    		msg = merchant_Id + "|"+trans_id+"|"+"NA"+"|"+tot_amount+"|"+"NA"+"|"+"NA"+"|"+"NA"
    				+"|"+Currency_Type+"|"+"NA"+"|"+TypeField_1+"|"+Security_Id+"|"+"NA"+"|"+"NA"
    				+"|"+TypeField_2+"|"+pb.getPayment_type()+"|"+temp_id+"|"+"NA"+"|"+"NA"+"|"+"NA"+"|"+"NA"
    				+"|"+"NA"+"|"+"NA"+"";
    		hash = CheckSum.HmacSHA256(msg,ChecksumKey);
    		msg = msg+"|"+hash.toUpperCase();
    	callbackUrl="https://apbocwwb.ap.nic.in/"+pb.getResponseURL()+"";
    	 JSONObject obj = new JSONObject();
         obj.put("enableChildWindowPosting", true);
         obj.put("enablePaymentRetry", true);
         obj.put("retry_attempt_count", 2);
         /*obj.put("txtPayCategory","NETBANKING");*/
         JSONObject objnew = new JSONObject();
         objnew.put("msg", msg);
         objnew.put("options", obj);
         objnew.put("callbackUrl", callbackUrl);
    	pbr.setMsg(msg);

    } catch (Exception e) {
    	
    		pbr.setMessage("exception");
    	} finally {
    		try{
    		if (rs != null) {
    			rs.close();
    		}
    		if (pstmt != null) {
    			pstmt.close();
    		}
    		if (con != null) {
    			DbUtils.closeQuietly(con);
    		}
    		}catch(Exception e){
    			
    		}
    	}
    	pbr.setMessage("success");
    	return pbr;
    }
    
 
    
public String PaymentUpdate(PaymentBean pb)  {
	Connection con = null;
	PreparedStatement pstmt = null;
	con = DBConnection.getConnection();	
	String temp_id=""+session.getAttribute("temp_id");
	if(userid==null){
		userid=temp_id;
	}
	try {
		String txn_date=MyUtil.ChDate1(pb.getTxn_date().substring(0, 10));
		/*String txn_date=pb.getTxn_date();*/
		int trno = beens.MyUtil.logTrace(userid, "online_payment_details", "update", request);
		String sql = "update online_payment_details set bank_txn_ref_no =?,txn_reference_no=?,txn_amount =?,txn_date =?,error_description =?,trno=?"
				+ ",bank_trans_datetime=?,error_status=?,txn_type=?,bank_id=?,bank_merchant_id=?,check_sum=?,security_type=?,settlement_type=? "
				+ ",security_pwd=?,internal_error_code=?,auth_status=? where dept_trans_id = ? ";
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, pb.getBank_txn_ref_no());
		pstmt.setString(2, pb.getTxn_reference_no());
		pstmt.setDouble(3, pb.getTxn_amount());
		pstmt.setDate(4, java.sql.Date.valueOf(txn_date));
		pstmt.setString(5, pb.getError_description());
		pstmt.setInt(6, trno);
		pstmt.setTimestamp(7,java.sql.Timestamp.valueOf(txn_date+" "+pb.getTxn_date().substring(11)));
		pstmt.setString(8,pb.getError_status());
		pstmt.setString(9,pb.getTxn_type());
		pstmt.setString(10,pb.getBank_id());
		pstmt.setString(11,pb.getBank_merchant_id());
		pstmt.setString(12,pb.getCheck_sum());
		pstmt.setString(13,pb.getSecurity_type());
		pstmt.setString(14, pb.getSettlement_type());
		pstmt.setString(15,pb.getSecurity_pwd());
		pstmt.setString(16,pb.getLoc_error_msg());
		pstmt.setString(17, pb.getAuth_status());
		pstmt.setString(18, pb.getCustomer_id());
		int i=pstmt.executeUpdate();
if(i>0){
	return pb.getLoc_error_msg();
}else{
	return "R2";
}
	} catch (Exception e) {
		System.out.println("exception in method--->"+e);
		e.printStackTrace();
		return "E1";
	}finally {			
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (con != null) {
			DbUtils.closeQuietly(con);
		}
	}
	
}
public PaymentBean GetRespValus(String msg){
	PaymentBean pb=new PaymentBean();	
	try{
		String hash = "";	
		String str = msg;
	     String[] temp;
	     temp = str.split("\\s*\\|\\s*");  
	     String Merchant_id = temp[0];
	     System.out.println("merchant_id is--->"+Merchant_id);
	     pb.setMerchant_id(Merchant_id);
	     String Customer_id = temp[1];
	     pb.setPayment_transid(Customer_id);
	     pb.setCustomer_id(Customer_id);
	     String Txn_Ref_no = temp[2];  
	     pb.setTxn_reference_no(Txn_Ref_no);
	     String Bank_Ref_no = temp[3];
	     pb.setBank_txn_ref_no(Bank_Ref_no);
	     String Txn_Amount = temp[4];   
	     pb.setTxn_amount(Double.valueOf(Txn_Amount));
	     String Bank_id = temp[5];
	     pb.setBank_id(Bank_id);
	     String Bank_Merchant_id= temp[6]; 
	     pb.setBank_merchant_id(Bank_Merchant_id);
	     String Txn_Type = temp[7];
	     pb.setTxn_type(Txn_Type);
	     String Currency_name = temp[8];   
	     pb.setCurrency_name(Currency_name);
	     String Item_code = temp[9];
	     pb.setItem_code(Item_code);
	     String Security_Type = temp[10];   
	     pb.setSecurity_type(Security_Type);
	     String Security_Id= temp[11];
	     pb.setSecurity_id(Security_Id);
	     String Security_pswd= temp[12];   
	     pb.setSecurity_pwd(Security_pswd);
	     String Txn_date= temp[13];
	     pb.setTxn_date(Txn_date);
	     String Auth_Status= temp[14];   
	     pb.setAuth_status(Auth_Status);
	     String Settlement_type= temp[15];
	     pb.setSettlement_type(Settlement_type);
	     String Add_info1 = temp[16];   
	     pb.setAddl_info1(Add_info1);
	     String Add_info2=temp[17];
	     pb.setAddl_info2(Add_info2);
	     String Add_info3 = temp[18];  
	     pb.setAddl_info3(Add_info3);
	     String Add_info4 = temp[19];
	     pb.setAddl_info4(Add_info4);
	     String Add_info5 = temp[20];   
	     String Add_info6 = temp[21];
	     String Add_info7 = temp[22];   
	     String Error_status = temp[23];
	     pb.setError_status(Error_status);
	     String Error_Description= temp[24];   
	     pb.setError_description(Error_Description);
	     String Check_Sum = temp[25]; 
	     pb.setCheck_sum(Check_Sum);
	     String actival_val= StringUtils.removeEnd(msg, "|"+pb.getCheck_sum());
			hash = CheckSum.HmacSHA256(actival_val,ChecksumKey);
			String checksumverify = hash.toUpperCase();
	     if(checksumverify.equals(Check_Sum)){
	    	/* if(Error_Description.equalsIgnoreCase("Success")){
	    		 pb.setLoc_error_msg("SU");
	    	 }else{
	    		 pb.setLoc_error_msg("PE");
	    	 }
	    	
	     }else{
	    	 pb.setLoc_error_msg("DT"); 
	     }*/
	    	 if(Auth_Status.equalsIgnoreCase("0300")){
	    		 pb.setLoc_error_msg("SU");
	    		 pb.setError_description("Success");
	    		 }
	    	 else if(Auth_Status.equalsIgnoreCase("0399")){
	    		 pb.setLoc_error_msg("PE");
	    		 pb.setError_description("Failure");
	    	 }
	    	 else{
		    	 pb.setLoc_error_msg("DT"); 
		     }
	    	 }
	
}catch(ArrayIndexOutOfBoundsException ar){
	pb.setLoc_error_msg("AI");
}catch (Exception e) {
	pb.setLoc_error_msg("EX");
}
	return pb;
}


}
