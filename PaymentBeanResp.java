package payments;

import org.json.JSONObject;

public class PaymentBeanResp {
private String message;
private String msg;
private String options;
private String callbackUrl;

public String getCallbackUrl() {
	return callbackUrl;
}
public void setCallbackUrl(String callbackUrl) {
	this.callbackUrl = callbackUrl;
}
public String getMessage() {
	return message;
}

public void setMessage(String message) {
	this.message = message;
}

public String getMsg() {
	return msg;
}
public void setMsg(String msg) {
	this.msg = msg;
}
public PaymentBeanResp(String payment_encp_msg, String options, String enc_url) {
	this.msg = payment_encp_msg;
	System.out.println("option in bean is"+options);
	this.options=options;
	this.callbackUrl=enc_url;
}

public PaymentBeanResp(String payment_encp_msg, String enc_url) {
	this.msg = payment_encp_msg;
	this.callbackUrl=enc_url;
}
public String getOptions() {
	return options;
}
public void setOptions(String options) {
	this.options = options;
}

public PaymentBeanResp(){
	
}

}
