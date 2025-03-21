package payments;

public class PaymentBean {
private int reference_id;
private int tot_amount;
private String payment_transid;
private double txn_amount;
private String Customer_id,bank_txn_ref_no,txn_reference_no,txn_date,error_description;
private String payment_type;
private String payment_gateway;
private String responseURL,check_sum,security_id,loc_error_msg;
private String msg,auth_status,settlement_type,addl_info1,addl_info2,addl_info3,addl_info4,error_status;
private String merchant_id,bank_id,bank_merchant_id,txn_type,currency_name,item_code,security_type,security_pwd;
private boolean check_sum_valid;
private int subscription_years;
private String message;
private String worker_name,father_name,worker_reg_no,valid_upto;


public String getWorker_name() {
	return worker_name;
}

public void setWorker_name(String worker_name) {
	this.worker_name = worker_name;
}

public String getFather_name() {
	return father_name;
}

public void setFather_name(String father_name) {
	this.father_name = father_name;
}

public String getWorker_reg_no() {
	return worker_reg_no;
}

public void setWorker_reg_no(String worker_reg_no) {
	this.worker_reg_no = worker_reg_no;
}

public String getValid_upto() {
	return valid_upto;
}

public void setValid_upto(String valid_upto) {
	this.valid_upto = valid_upto;
}

public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public boolean isCheck_sum_valid() {
	return check_sum_valid;
}

public int getSubscription_years() {
	return subscription_years;
}

public void setSubscription_years(int subscription_years) {
	this.subscription_years = subscription_years;
}

public void setCheck_sum_valid(boolean check_sum_valid) {
	this.check_sum_valid = check_sum_valid;
}
public double getTxn_amount() {
	return txn_amount;
}
public void setTxn_amount(double txn_amount) {
	this.txn_amount = txn_amount;
}
public String getLoc_error_msg() {
	return loc_error_msg;
}
public void setLoc_error_msg(String loc_error_msg) {
	this.loc_error_msg = loc_error_msg;
}
public String getSecurity_id() {
	return security_id;
}
public void setSecurity_id(String security_id) {
	this.security_id = security_id;
}
public String getCheck_sum() {
	return check_sum;
}
public void setCheck_sum(String check_sum) {
	this.check_sum = check_sum;
}
public String getAuth_status() {
	return auth_status;
}
public void setAuth_status(String auth_status) {
	this.auth_status = auth_status;
}
public String getSettlement_type() {
	return settlement_type;
}
public void setSettlement_type(String settlement_type) {
	this.settlement_type = settlement_type;
}
public String getAddl_info1() {
	return addl_info1;
}
public void setAddl_info1(String addl_info1) {
	this.addl_info1 = addl_info1;
}
public String getAddl_info2() {
	return addl_info2;
}
public void setAddl_info2(String addl_info2) {
	this.addl_info2 = addl_info2;
}
public String getAddl_info3() {
	return addl_info3;
}
public void setAddl_info3(String addl_info3) {
	this.addl_info3 = addl_info3;
}
public String getAddl_info4() {
	return addl_info4;
}
public void setAddl_info4(String addl_info4) {
	this.addl_info4 = addl_info4;
}
public String getError_status() {
	return error_status;
}
public void setError_status(String error_status) {
	this.error_status = error_status;
}
public String getItem_code() {
	return item_code;
}
public void setItem_code(String item_code) {
	this.item_code = item_code;
}
public String getSecurity_type() {
	return security_type;
}
public void setSecurity_type(String security_type) {
	this.security_type = security_type;
}
public String getSecurity_pwd() {
	return security_pwd;
}
public void setSecurity_pwd(String security_pwd) {
	this.security_pwd = security_pwd;
}
public String getCurrency_name() {
	return currency_name;
}
public void setCurrency_name(String currency_name) {
	this.currency_name = currency_name;
}
public String getTxn_type() {
	return txn_type;
}
public void setTxn_type(String txn_type) {
	this.txn_type = txn_type;
}
public String getBank_merchant_id() {
	return bank_merchant_id;
}
public void setBank_merchant_id(String bank_merchant_id) {
	this.bank_merchant_id = bank_merchant_id;
}
public String getMerchant_id() {
	return merchant_id;
}
public void setMerchant_id(String merchant_id) {
	this.merchant_id = merchant_id;
}
public String getBank_id() {
	return bank_id;
}
public void setBank_id(String bank_id) {
	this.bank_id = bank_id;
}
public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public String getResponseURL() {
	return responseURL;
}
public void setResponseURL(String responseURL) {
	this.responseURL = responseURL;
}
public String getPayment_gateway() {
	return payment_gateway;
}
public void setPayment_gateway(String payment_gateway) {
	this.payment_gateway = payment_gateway;
}
public String getPayment_type() {
	return payment_type;
}
public void setPayment_type(String payment_type) {
	this.payment_type = payment_type;
}
public int getReference_id() {
	return reference_id;
}
public void setReference_id(int reference_id) {
	this.reference_id = reference_id;
}
public int getTot_amount() {
	return tot_amount;
}
public void setTot_amount(int tot_amount) {
	this.tot_amount = tot_amount;
}
public String getCustomer_id() {
	return Customer_id;
}
public void setCustomer_id(String customer_id) {
	Customer_id = customer_id;
}
public String getBank_txn_ref_no() {
	return bank_txn_ref_no;
}
public void setBank_txn_ref_no(String bank_txn_ref_no) {
	this.bank_txn_ref_no = bank_txn_ref_no;
}
public String getTxn_reference_no() {
	return txn_reference_no;
}
public void setTxn_reference_no(String txn_reference_no) {
	this.txn_reference_no = txn_reference_no;
}


public String getTxn_date() {
	return txn_date;
}
public void setTxn_date(String txn_date) {
	this.txn_date = txn_date;
}
public String getError_description() {
	return error_description;
}
public void setError_description(String error_description) {
	this.error_description = error_description;
}
public String getPayment_transid() {
	return payment_transid;
}
public void setPayment_transid(String payment_transid) {
	this.payment_transid = payment_transid;
}


}
