/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgen;

import java.io.File;
import java.util.HashMap;

public class ALOApproveViewForm {
private String uid;
private String aadharno;
private String  pres_addr_pincode;
private String pres_addr_dno ; 
private String pres_addr_location;
//private String ifsccode1;
private String action_type;
private String reject_reason;
private String trnsfer_dist;
private String trnsfer_alocode;
private String[] aloapproveselect;
private String father_name;
private String dob;
private String trade_code;
private String photo_aadhaar;

private File myPhoto, rationcardcopy, aadharcardcopy, voteridcopy, arogyasricopy, bankcardcopy, employercertificationcopy, jobcardcopy, otherscopy, challan_receipt;
private String myPhotoContentType, myPhotoFileName, message, user_id, regfee50, regfee50_dt, regfee50_rcpt, regfee12_dt, regfee12_rcpt;
private String worker_id, ration_no, worker_name, gender,  date_of_birth, fath_husb, qualification, spouse_worker_id;
private int age;
//card details
private String rationcard, rationcardno, aadharcard, aadharcardno, voterid, voteridno, arogyasri, arogyasrino, bankcard, bankcardno, employercertification, employercertificationno, jobcard, jobcardno, others, othersno;
//nominee details
private String language, migrantdistrict, migrantmandal, migrantworkerstate, alocode, collectingalocode;
// private String nominee_name[], father_nominee[], occupation[], nominee_age[], percentage[];
//schemes
int jana, indira, rajiv, regfee12, tot_amt, fee_amount;
//present address deatils for english
private String present_addr1, present_addr2,pres_addr_district,pres_addr_mandal,pres_addr_village,present_habcode,present_addr_pincode, prestAndPerm, mode;
//present and permenant habitation code
private String permenant_habcode,  fee_type, fee_year, fee_date, fee_rcpt, valid_date, reg_year;
//permenant address details for telugu
private String permenant_addr1, permenant_addr2;
//present employer address details
private String pres_emp_name, pres_emp_dno, pres_emp_location, pres_emp_village, pres_emp_mandal, pres_emp_district, pres_emp_pincode;
private String nature_emp, bank_accno, bank_name, branch_name, state, migrantworker, migrantplace, request_locale;
private int pres_pkgs[], pkg_brk[];

String sl_no;
String fm_name;
String fm_relation;
String fm_relation1;
String fm_age;
String nominee_name, father_nominee, occupation, nominee_age, percentage;

private String phoneno;
private String cardtype1, cardtype2, cardtype3, cardtype4, cardtype5, cardtype6, cardtype7, cardtype8;
private String reg_no;

//added newly on 23-07-2018

private String marital_status,cast,ifsccode_new;

private String print_status;

//payment offline
private String receipt_no,receipt_date;
private int reg_fee_off,subscription_fee_off,total_fee_off,total_workes,serial_worker_no;
private int subscription_years;
//-------------------
private int count_total;
private int count_selectd;
private int count_approved;
private int count_pending;
private int count_reject;
private String alo_name;
private String type;
private String remaks_can_alo;
private String admin_remarks;


public String getAdmin_remarks() {
	return admin_remarks;
}

public void setAdmin_remarks(String admin_remarks) {
	this.admin_remarks = admin_remarks;
}

public String getRemaks_can_alo() {
	return remaks_can_alo;
}

public void setRemaks_can_alo(String remaks_can_alo) {
	this.remaks_can_alo = remaks_can_alo;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getAlo_name() {
	return alo_name;
}

public void setAlo_name(String alo_name) {
	this.alo_name = alo_name;
}

public int getCount_total() {
	return count_total;
}

public void setCount_total(int count_total) {
	this.count_total = count_total;
}

public int getCount_selectd() {
	return count_selectd;
}

public void setCount_selectd(int count_selectd) {
	this.count_selectd = count_selectd;
}

public int getCount_approved() {
	return count_approved;
}

public void setCount_approved(int count_approved) {
	this.count_approved = count_approved;
}

public int getCount_pending() {
	return count_pending;
}

public void setCount_pending(int count_pending) {
	this.count_pending = count_pending;
}

public int getCount_reject() {
	return count_reject;
}

public void setCount_reject(int count_reject) {
	this.count_reject = count_reject;
}

public int getSubscription_years() {
	return subscription_years;
}

public void setSubscription_years(int subscription_years) {
	this.subscription_years = subscription_years;
}

public String getReceipt_no() {
	return receipt_no;
}

public void setReceipt_no(String receipt_no) {
	this.receipt_no = receipt_no;
}

public String getReceipt_date() {
	return receipt_date;
}

public void setReceipt_date(String receipt_date) {
	this.receipt_date = receipt_date;
}

public int getReg_fee_off() {
	return reg_fee_off;
}

public void setReg_fee_off(int reg_fee_off) {
	this.reg_fee_off = reg_fee_off;
}

public int getSubscription_fee_off() {
	return subscription_fee_off;
}

public void setSubscription_fee_off(int subscription_fee_off) {
	this.subscription_fee_off = subscription_fee_off;
}

public int getTotal_fee_off() {
	return total_fee_off;
}

public void setTotal_fee_off(int total_fee_off) {
	this.total_fee_off = total_fee_off;
}

public int getTotal_workes() {
	return total_workes;
}

public void setTotal_workes(int total_workes) {
	this.total_workes = total_workes;
}

public int getSerial_worker_no() {
	return serial_worker_no;
}

public void setSerial_worker_no(int serial_worker_no) {
	this.serial_worker_no = serial_worker_no;
}

public String getPrint_status() {
	return print_status;
}

public void setPrint_status(String print_status) {
	this.print_status = print_status;
}

public String getIfsccode_new() {
	return ifsccode_new;
}

public void setIfsccode_new(String ifsccode_new) {
	this.ifsccode_new = ifsccode_new;
}

public String getCast() {
	return cast;
}

public void setCast(String cast) {
	this.cast = cast;
}

public String getMarital_status() {
	return marital_status;
}

public void setMarital_status(String marital_status) {
	this.marital_status = marital_status;
}

public String getPhoto_aadhaar() {
	return photo_aadhaar;
}

public void setPhoto_aadhaar(String photo_aadhaar) {
	this.photo_aadhaar = photo_aadhaar;
}

public String getTrade_code() {
	return trade_code;
}

public void setTrade_code(String trade_code) {
	this.trade_code = trade_code;
}

public String[] getAloapproveselect() {
	return aloapproveselect;
}

public void setAloapproveselect(String[] aloapproveselect) {
	this.aloapproveselect = aloapproveselect;
}

public String getFather_name() {
	return father_name;
}

public void setFather_name(String father_name) {
	this.father_name = father_name;
}

public String getDob() {
	return dob;
}

public void setDob(String dob) {
	this.dob = dob;
}

public String getReject_reason() {
	return reject_reason;
}

public void setReject_reason(String reject_reason) {
	this.reject_reason = reject_reason;
}

public String getTrnsfer_dist() {
	return trnsfer_dist;
}

public void setTrnsfer_dist(String trnsfer_dist) {
	this.trnsfer_dist = trnsfer_dist;
}

public String getTrnsfer_alocode() {
	return trnsfer_alocode;
}

public void setTrnsfer_alocode(String trnsfer_alocode) {
	this.trnsfer_alocode = trnsfer_alocode;
}

	public String getAction_type() {
	return action_type;
}

public void setAction_type(String action_type) {
	this.action_type = action_type;
}


	public String getPres_addr_pincode() {
	return pres_addr_pincode;
}

public void setPres_addr_pincode(String pres_addr_pincode) {
	this.pres_addr_pincode = pres_addr_pincode;
}

public String getPres_addr_location() {
	return pres_addr_location;
}

public void setPres_addr_location(String pres_addr_location) {
	this.pres_addr_location = pres_addr_location;
}

public String getPres_addr_district() {
	return pres_addr_district;
}

public void setPres_addr_district(String pres_addr_district) {
	this.pres_addr_district = pres_addr_district;
}

public String getPres_addr_mandal() {
	return pres_addr_mandal;
}

public void setPres_addr_mandal(String pres_addr_mandal) {
	this.pres_addr_mandal = pres_addr_mandal;
}

public String getPres_addr_village() {
	return pres_addr_village;
}

public void setPres_addr_village(String pres_addr_village) {
	this.pres_addr_village = pres_addr_village;
}

	public String getPres_addr_dno() {
	return pres_addr_dno;
}

public void setPres_addr_dno(String pres_addr_dno) {
	this.pres_addr_dno = pres_addr_dno;
}

	public String getAadharno() {
        return aadharno;
    }

    public void setAadharno(String aadharno) {
        this.aadharno = aadharno;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    private String reg_date;

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }

    public String getDup_worker_id() {
        return dup_worker_id;
    }

    public void setDup_worker_id(String dup_worker_id) {
        this.dup_worker_id = dup_worker_id;
    }

    public String getDup_worker_name() {
        return dup_worker_name;
    }

    public void setDup_worker_name(String dup_worker_name) {
        this.dup_worker_name = dup_worker_name;
    }

    public String getDup_father_name() {
        return dup_father_name;
    }

    public void setDup_father_name(String dup_father_name) {
        this.dup_father_name = dup_father_name;
    }

    public String getDup_age() {
        return dup_age;
    }

    public void setDup_age(String dup_age) {
        this.dup_age = dup_age;
    }

    public String getDup_gender() {
        return dup_gender;
    }

    public void setDup_gender(String dup_gender) {
        this.dup_gender = dup_gender;
    }
    private String dup_worker_id;
    private String dup_worker_name;
    private String dup_father_name;
    private String dup_age;
    private String dup_gender;
    private String temp_id;
    private String application_no;

    public String getApplication_no() {
        return application_no;
    }

    public void setApplication_no(String application_no) {
        this.application_no = application_no;
    }

    public String getTemp_id() {
        return temp_id;
    }

    public void setTemp_id(String temp_id) {
        this.temp_id = temp_id;
    }
    public String getPresent_addr_pincode() {
        return present_addr_pincode;
    }

    public void setPresent_addr_pincode(String present_addr_pincode) {
        this.present_addr_pincode = present_addr_pincode;
    }

    public String getPresent_addr_district() {
        return present_addr_district;
    }

    public void setPresent_addr_district(String present_addr_district) {
        this.present_addr_district = present_addr_district;
    }

    public String getPresent_addr_mandal() {
        return present_addr_mandal;
    }

    public void setPresent_addr_mandal(String present_addr_mandal) {
        this.present_addr_mandal = present_addr_mandal;
    }

    public String getPermenant_addr_pincode() {
        return permenant_addr_pincode;
    }

    public void setPermenant_addr_pincode(String permenant_addr_pincode) {
        this.permenant_addr_pincode = permenant_addr_pincode;
    }

    public String getPermenant_addr_district() {
        return permenant_addr_district;
    }

    public void setPermenant_addr_district(String permenant_addr_district) {
        this.permenant_addr_district = permenant_addr_district;
    }

    public String getPermenant_addr_mandal() {
        return permenant_addr_mandal;
    }

    public void setPermenant_addr_mandal(String permenant_addr_mandal) {
        this.permenant_addr_mandal = permenant_addr_mandal;
    }
    private String scheme_info;

    public String getScheme_info() {
        return scheme_info;
    }

    public void setScheme_info(String scheme_info) {
        this.scheme_info = scheme_info;
    }
    private String present_addr_district;
    private String present_addr_mandal;
    private String permenant_addr_pincode;
    private String permenant_addr_district;
    private String permenant_addr_mandal;

    public String getReg_no() {
        return reg_no;
    }

    public void setReg_no(String reg_no) {
        this.reg_no = reg_no;
    }

    public String getCardtype1() {
        return cardtype1;
    }

    public void setCardtype1(String cardtype1) {
        this.cardtype1 = cardtype1;
    }

    public String getCardtype2() {
        return cardtype2;
    }

    public void setCardtype2(String cardtype2) {
        this.cardtype2 = cardtype2;
    }

    public String getCardtype3() {
        return cardtype3;
    }

    public void setCardtype3(String cardtype3) {
        this.cardtype3 = cardtype3;
    }

    public String getCardtype4() {
        return cardtype4;
    }

    public void setCardtype4(String cardtype4) {
        this.cardtype4 = cardtype4;
    }

    public String getCardtype5() {
        return cardtype5;
    }

    public void setCardtype5(String cardtype5) {
        this.cardtype5 = cardtype5;
    }

    public String getCardtype6() {
        return cardtype6;
    }

    public void setCardtype6(String cardtype6) {
        this.cardtype6 = cardtype6;
    }

    public String getCardtype7() {
        return cardtype7;
    }

    public void setCardtype7(String cardtype7) {
        this.cardtype7 = cardtype7;
    }

    public String getCardtype8() {
        return cardtype8;
    }

    public void setCardtype8(String cardtype8) {
        this.cardtype8 = cardtype8;
    }
    

    public String getSl_no() {
        return sl_no;
    }
    private int sno;

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getFm_name() {
        return fm_name;
    }

    public void setFm_name(String fm_name) {
        this.fm_name = fm_name;
    }

    public String getFm_relation() {
        return fm_relation;
    }

    public void setFm_relation(String fm_relation) {
        this.fm_relation = fm_relation;
    }

    public String getFm_relation1() {
        return fm_relation1;
    }

    public void setFm_relation1(String fm_relation1) {
        this.fm_relation1 = fm_relation1;
    }

    public String getFm_age() {
        return fm_age;
    }

    public void setFm_age(String fm_age) {
        this.fm_age = fm_age;
    }
  

    public String getNominee_name() {
        return nominee_name;
    }

    public void setNominee_name(String nominee_name) {
        this.nominee_name = nominee_name;
    }

    public String getFather_nominee() {
        return father_nominee;
    }

    public void setFather_nominee(String father_nominee) {
        this.father_nominee = father_nominee;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNominee_age() {
        return nominee_age;
    }

    public void setNominee_age(String nominee_age) {
        this.nominee_age = nominee_age;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
    private String declartion, union_name, tradeunion, cardtype[], cardno[];
    //setting mandal_list,village_list,pres_employer_village when Editing and Updating
    private HashMap<String, String> mand_list, vill_list1, vill_list2, Pres_emp_vill_list, schemes_details, migrant_district, migrant_mandal, bank_branch;

    public File getMyPhoto() {
        return myPhoto;
    }

    public void setMyPhoto(File myPhoto) {
        this.myPhoto = myPhoto;
    }

    public File getRationcardcopy() {
        return rationcardcopy;
    }

    public void setRationcardcopy(File rationcardcopy) {
        this.rationcardcopy = rationcardcopy;
    }

    public File getAadharcardcopy() {
        return aadharcardcopy;
    }

    public void setAadharcardcopy(File aadharcardcopy) {
        this.aadharcardcopy = aadharcardcopy;
    }

    public File getVoteridcopy() {
        return voteridcopy;
    }

    public void setVoteridcopy(File voteridcopy) {
        this.voteridcopy = voteridcopy;
    }

    public File getArogyasricopy() {
        return arogyasricopy;
    }

    public void setArogyasricopy(File arogyasricopy) {
        this.arogyasricopy = arogyasricopy;
    }

    public File getBankcardcopy() {
        return bankcardcopy;
    }

    public void setBankcardcopy(File bankcardcopy) {
        this.bankcardcopy = bankcardcopy;
    }

    public File getEmployercertificationcopy() {
        return employercertificationcopy;
    }

    public void setEmployercertificationcopy(File employercertificationcopy) {
        this.employercertificationcopy = employercertificationcopy;
    }

    public File getJobcardcopy() {
        return jobcardcopy;
    }

    public void setJobcardcopy(File jobcardcopy) {
        this.jobcardcopy = jobcardcopy;
    }

    public File getOtherscopy() {
        return otherscopy;
    }

    public void setOtherscopy(File otherscopy) {
        this.otherscopy = otherscopy;
    }

    public File getChallan_receipt() {
        return challan_receipt;
    }

    public void setChallan_receipt(File challan_receipt) {
        this.challan_receipt = challan_receipt;
    }

    public String getMyPhotoContentType() {
        return myPhotoContentType;
    }

    public void setMyPhotoContentType(String myPhotoContentType) {
        this.myPhotoContentType = myPhotoContentType;
    }

    public String getMyPhotoFileName() {
        return myPhotoFileName;
    }

    public void setMyPhotoFileName(String myPhotoFileName) {
        this.myPhotoFileName = myPhotoFileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRegfee50() {
        return regfee50;
    }

    public void setRegfee50(String regfee50) {
        this.regfee50 = regfee50;
    }

    public String getRegfee50_dt() {
        return regfee50_dt;
    }

    public void setRegfee50_dt(String regfee50_dt) {
        this.regfee50_dt = regfee50_dt;
    }

    public String getRegfee50_rcpt() {
        return regfee50_rcpt;
    }

    public void setRegfee50_rcpt(String regfee50_rcpt) {
        this.regfee50_rcpt = regfee50_rcpt;
    }

    public String getRegfee12_dt() {
        return regfee12_dt;
    }

    public void setRegfee12_dt(String regfee12_dt) {
        this.regfee12_dt = regfee12_dt;
    }

    public String getRegfee12_rcpt() {
        return regfee12_rcpt;
    }

    public void setRegfee12_rcpt(String regfee12_rcpt) {
        this.regfee12_rcpt = regfee12_rcpt;
    }

    public String getWorker_id() {
        return worker_id;
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }

    public String getRation_no() {
        return ration_no;
    }

    public void setRation_no(String ration_no) {
        this.ration_no = ration_no;
    }

    public String getWorker_name() {
        return worker_name;
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

  

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getFath_husb() {
        return fath_husb;
    }

    public void setFath_husb(String fath_husb) {
        this.fath_husb = fath_husb;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpouse_worker_id() {
        return spouse_worker_id;
    }

    public void setSpouse_worker_id(String spouse_worker_id) {
        this.spouse_worker_id = spouse_worker_id;
    }

  

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRationcard() {
        return rationcard;
    }

    public void setRationcard(String rationcard) {
        this.rationcard = rationcard;
    }

    public String getRationcardno() {
        return rationcardno;
    }

    public void setRationcardno(String rationcardno) {
        this.rationcardno = rationcardno;
    }

    public String getAadharcard() {
        return aadharcard;
    }

    public void setAadharcard(String aadharcard) {
        this.aadharcard = aadharcard;
    }

    public String getAadharcardno() {
        return aadharcardno;
    }

    public void setAadharcardno(String aadharcardno) {
        this.aadharcardno = aadharcardno;
    }

    public String getVoterid() {
        return voterid;
    }

    public void setVoterid(String voterid) {
        this.voterid = voterid;
    }

    public String getVoteridno() {
        return voteridno;
    }

    public void setVoteridno(String voteridno) {
        this.voteridno = voteridno;
    }

    public String getArogyasri() {
        return arogyasri;
    }

    public void setArogyasri(String arogyasri) {
        this.arogyasri = arogyasri;
    }

    public String getArogyasrino() {
        return arogyasrino;
    }

    public void setArogyasrino(String arogyasrino) {
        this.arogyasrino = arogyasrino;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getBankcardno() {
        return bankcardno;
    }

    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }

    public String getEmployercertification() {
        return employercertification;
    }

    public void setEmployercertification(String employercertification) {
        this.employercertification = employercertification;
    }

    public String getEmployercertificationno() {
        return employercertificationno;
    }

    public void setEmployercertificationno(String employercertificationno) {
        this.employercertificationno = employercertificationno;
    }

    public String getJobcard() {
        return jobcard;
    }

    public void setJobcard(String jobcard) {
        this.jobcard = jobcard;
    }

    public String getJobcardno() {
        return jobcardno;
    }

    public void setJobcardno(String jobcardno) {
        this.jobcardno = jobcardno;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getOthersno() {
        return othersno;
    }

    public void setOthersno(String othersno) {
        this.othersno = othersno;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMigrantdistrict() {
        return migrantdistrict;
    }

    public void setMigrantdistrict(String migrantdistrict) {
        this.migrantdistrict = migrantdistrict;
    }

    public String getMigrantmandal() {
        return migrantmandal;
    }

    public void setMigrantmandal(String migrantmandal) {
        this.migrantmandal = migrantmandal;
    }

    public String getMigrantworkerstate() {
        return migrantworkerstate;
    }

    public void setMigrantworkerstate(String migrantworkerstate) {
        this.migrantworkerstate = migrantworkerstate;
    }

    public String getAlocode() {
        return alocode;
    }

    public void setAlocode(String alocode) {
        this.alocode = alocode;
    }

    public String getCollectingalocode() {
        return collectingalocode;
    }

    public void setCollectingalocode(String collectingalocode) {
        this.collectingalocode = collectingalocode;
    }

    public int getJana() {
        return jana;
    }

    public void setJana(int jana) {
        this.jana = jana;
    }

    public int getIndira() {
        return indira;
    }

    public void setIndira(int indira) {
        this.indira = indira;
    }

    public int getRajiv() {
        return rajiv;
    }

    public void setRajiv(int rajiv) {
        this.rajiv = rajiv;
    }

    public int getRegfee12() {
        return regfee12;
    }

    public void setRegfee12(int regfee12) {
        this.regfee12 = regfee12;
    }

    public int getTot_amt() {
        return tot_amt;
    }

    public void setTot_amt(int tot_amt) {
        this.tot_amt = tot_amt;
    }

    public int getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(int fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getPresent_addr1() {
        return present_addr1;
    }

    public void setPresent_addr1(String present_addr1) {
        this.present_addr1 = present_addr1;
    }

    public String getPresent_addr2() {
        return present_addr2;
    }

    public void setPresent_addr2(String present_addr2) {
        this.present_addr2 = present_addr2;
    }

    public String getPrestAndPerm() {
        return prestAndPerm;
    }

    public void setPrestAndPerm(String prestAndPerm) {
        this.prestAndPerm = prestAndPerm;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getPermenant_habcode() {
        return permenant_habcode;
    }

    public void setPermenant_habcode(String permenant_habcode) {
        this.permenant_habcode = permenant_habcode;
    }

    public String getPresent_habcode() {
        return present_habcode;
    }

    public void setPresent_habcode(String present_habcode) {
        this.present_habcode = present_habcode;
    }

    public String getFee_type() {
        return fee_type;
    }

    public void setFee_type(String fee_type) {
        this.fee_type = fee_type;
    }

    public String getFee_year() {
        return fee_year;
    }

    public void setFee_year(String fee_year) {
        this.fee_year = fee_year;
    }

    public String getFee_date() {
        return fee_date;
    }

    public void setFee_date(String fee_date) {
        this.fee_date = fee_date;
    }

    public String getFee_rcpt() {
        return fee_rcpt;
    }

    public void setFee_rcpt(String fee_rcpt) {
        this.fee_rcpt = fee_rcpt;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getReg_year() {
        return reg_year;
    }

    public void setReg_year(String reg_year) {
        this.reg_year = reg_year;
    }

    public String getPermenant_addr1() {
        return permenant_addr1;
    }

    public void setPermenant_addr1(String permenant_addr1) {
        this.permenant_addr1 = permenant_addr1;
    }

    public String getPermenant_addr2() {
        return permenant_addr2;
    }

    public void setPermenant_addr2(String permenant_addr2) {
        this.permenant_addr2 = permenant_addr2;
    }

    public String getPres_emp_name() {
        return pres_emp_name;
    }

    public void setPres_emp_name(String pres_emp_name) {
        this.pres_emp_name = pres_emp_name;
    }

    public String getPres_emp_dno() {
        return pres_emp_dno;
    }

    public void setPres_emp_dno(String pres_emp_dno) {
        this.pres_emp_dno = pres_emp_dno;
    }

    public String getPres_emp_location() {
        return pres_emp_location;
    }

    public void setPres_emp_location(String pres_emp_location) {
        this.pres_emp_location = pres_emp_location;
    }

    public String getPres_emp_village() {
        return pres_emp_village;
    }

    public void setPres_emp_village(String pres_emp_village) {
        this.pres_emp_village = pres_emp_village;
    }

    public String getPres_emp_mandal() {
        return pres_emp_mandal;
    }

    public void setPres_emp_mandal(String pres_emp_mandal) {
        this.pres_emp_mandal = pres_emp_mandal;
    }

    public String getPres_emp_district() {
        return pres_emp_district;
    }

    public void setPres_emp_district(String pres_emp_district) {
        this.pres_emp_district = pres_emp_district;
    }

    public String getPres_emp_pincode() {
        return pres_emp_pincode;
    }

    public void setPres_emp_pincode(String pres_emp_pincode) {
        this.pres_emp_pincode = pres_emp_pincode;
    }

    public String getNature_emp() {
        return nature_emp;
    }

    public void setNature_emp(String nature_emp) {
        this.nature_emp = nature_emp;
    }

    public String getBank_accno() {
        return bank_accno;
    }

    public void setBank_accno(String bank_accno) {
        this.bank_accno = bank_accno;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMigrantworker() {
        return migrantworker;
    }

    public void setMigrantworker(String migrantworker) {
        this.migrantworker = migrantworker;
    }

    public String getMigrantplace() {
        return migrantplace;
    }

    public void setMigrantplace(String migrantplace) {
        this.migrantplace = migrantplace;
    }

    public String getRequest_locale() {
        return request_locale;
    }

    public void setRequest_locale(String request_locale) {
        this.request_locale = request_locale;
    }

    public int[] getPres_pkgs() {
        return pres_pkgs;
    }

    public void setPres_pkgs(int[] pres_pkgs) {
        this.pres_pkgs = pres_pkgs;
    }

    public int[] getPkg_brk() {
        return pkg_brk;
    }

    public void setPkg_brk(int[] pkg_brk) {
        this.pkg_brk = pkg_brk;
    }

    public String getDeclartion() {
        return declartion;
    }

    public void setDeclartion(String declartion) {
        this.declartion = declartion;
    }

    public String getUnion_name() {
        return union_name;
    }

    public void setUnion_name(String union_name) {
        this.union_name = union_name;
    }

    public String getTradeunion() {
        return tradeunion;
    }

    public void setTradeunion(String tradeunion) {
        this.tradeunion = tradeunion;
    }

    public String[] getCardtype() {
        return cardtype;
    }

    public void setCardtype(String[] cardtype) {
        this.cardtype = cardtype;
    }

    public String[] getCardno() {
        return cardno;
    }

    public void setCardno(String[] cardno) {
        this.cardno = cardno;
    }

    public HashMap<String, String> getMand_list() {
        return mand_list;
    }

    public void setMand_list(HashMap<String, String> mand_list) {
        this.mand_list = mand_list;
    }

    public HashMap<String, String> getVill_list1() {
        return vill_list1;
    }

    public void setVill_list1(HashMap<String, String> vill_list1) {
        this.vill_list1 = vill_list1;
    }

    public HashMap<String, String> getVill_list2() {
        return vill_list2;
    }

    public void setVill_list2(HashMap<String, String> vill_list2) {
        this.vill_list2 = vill_list2;
    }

    public HashMap<String, String> getPres_emp_vill_list() {
        return Pres_emp_vill_list;
    }

    public void setPres_emp_vill_list(HashMap<String, String> Pres_emp_vill_list) {
        this.Pres_emp_vill_list = Pres_emp_vill_list;
    }

    public HashMap<String, String> getSchemes_details() {
        return schemes_details;
    }

    public void setSchemes_details(HashMap<String, String> schemes_details) {
        this.schemes_details = schemes_details;
    }

    public HashMap<String, String> getMigrant_district() {
        return migrant_district;
    }

    public void setMigrant_district(HashMap<String, String> migrant_district) {
        this.migrant_district = migrant_district;
    }

    public HashMap<String, String> getMigrant_mandal() {
        return migrant_mandal;
    }

    public void setMigrant_mandal(HashMap<String, String> migrant_mandal) {
        this.migrant_mandal = migrant_mandal;
    }

    public HashMap<String, String> getBank_branch() {
        return bank_branch;
    }

    public void setBank_branch(HashMap<String, String> bank_branch) {
        this.bank_branch = bank_branch;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }
}
