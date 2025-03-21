/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgen;

import java.util.List;

/**
 *
 * @author theru hareesh
 */
public class ALOApproveFormBean {

    private String temp_registration_no;
    private String temp_reg_date;
    private String legacy_data;
    private String created_by;
    private String printstatus;

	public String getPrintstatus() {
		return printstatus;
	}

	public void setPrintstatus(String printstatus) {
		this.printstatus = printstatus;
	}

	public String getCreated_by() {
        return beens.MyUtil.filterBad(created_by);
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLegacy_data() {
        return beens.MyUtil.filterBad(legacy_data);
    }

    public void setLegacy_data(String legacy_data) {
        this.legacy_data = legacy_data;
    }

    public String getTemp_registration_no() {
        return beens.MyUtil.filterBad(temp_registration_no);
    }

    public void setTemp_registration_no(String temp_registration_no) {
        this.temp_registration_no = temp_registration_no;
    }

    public String getTemp_reg_date() {
        return beens.MyUtil.filterBad(temp_reg_date);
    }

    public void setTemp_reg_date(String temp_reg_date) {
        this.temp_reg_date = temp_reg_date;
    }
    private String temp_id;
    private String temp_worker_name;
    private String temp_father_name;
    private String temp_gender;
    private String worker_name_t;
    private String father_name_t;
    private String proof;
    private String district_telugu;
    private String mndname_telugu;
    private String habname_telugu;
    private String prdistrict_telugu;
    private String pmndname_telugu;

    public String getPrdistrict_telugu() {
        return beens.MyUtil.filterBad(prdistrict_telugu);
    }

    public void setPrdistrict_telugu(String prdistrict_telugu) {
        this.prdistrict_telugu = prdistrict_telugu;
    }

    public String getPmndname_telugu() {
        return beens.MyUtil.filterBad(pmndname_telugu);
    }

    public void setPmndname_telugu(String pmndname_telugu) {
        this.pmndname_telugu = pmndname_telugu;
    }

    public String getPrhabname_telugu() {
        return beens.MyUtil.filterBad(prhabname_telugu);
    }

    public void setPrhabname_telugu(String prhabname_telugu) {
        this.prhabname_telugu = prhabname_telugu;
    }
    private String prhabname_telugu;

    public String getDistrict_telugu() {
        return beens.MyUtil.filterBad(district_telugu);
    }

    public void setDistrict_telugu(String district_telugu) {
        this.district_telugu = district_telugu;
    }

    public String getMndname_telugu() {
        return beens.MyUtil.filterBad(mndname_telugu);
    }

    public void setMndname_telugu(String mndname_telugu) {
        this.mndname_telugu = mndname_telugu;
    }

    public String getHabname_telugu() {
        return beens.MyUtil.filterBad(habname_telugu);
    }

    public void setHabname_telugu(String habname_telugu) {
        this.habname_telugu = habname_telugu;
    }

    public String getProof() {
        return beens.MyUtil.filterBad(proof);
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getWorker_name_t() {
        return beens.MyUtil.filterBad(worker_name_t);
    }

    public void setWorker_name_t(String worker_name_t) {
        this.worker_name_t = worker_name_t;
    }

    public String getFather_name_t() {
        return beens.MyUtil.filterBad(father_name_t);
    }

    public void setFather_name_t(String father_name_t) {
        this.father_name_t = father_name_t;
    }

    public String[] getAloapproveselect() {
        return aloapproveselect;
    }

    public void setAloapproveselect(String[] aloapproveselect) {
        this.aloapproveselect = aloapproveselect;
    }
    private String reg_year;

    public String getReg_year() {
        return beens.MyUtil.filterBad(reg_year);
    }

    public void setReg_year(String reg_year) {
        this.reg_year = reg_year;
    }
    private String temp_age;
    private String dup_worker_id;
    private String dup_worker_name;
    private String dup_father_name;
    private String dup_age;
    private String dup_gender;

    public String getDup_worker_id() {
        return beens.MyUtil.filterBad(dup_worker_id);
    }

    public void setDup_worker_id(String dup_worker_id) {
        this.dup_worker_id = dup_worker_id;
    }

    public String getDup_worker_name() {
        return beens.MyUtil.filterBad(dup_worker_name);
    }

    public void setDup_worker_name(String dup_worker_name) {
        this.dup_worker_name = dup_worker_name;
    }

    public String getDup_father_name() {
        return beens.MyUtil.filterBad(dup_father_name);
    }

    public void setDup_father_name(String dup_father_name) {
        this.dup_father_name = dup_father_name;
    }

    public String getDup_age() {
        return beens.MyUtil.filterBad(dup_age);
    }

    public void setDup_age(String dup_age) {
        this.dup_age = dup_age;
    }

    public String getDup_gender() {
        return beens.MyUtil.filterBad(dup_gender);
    }

    public void setDup_gender(String dup_gender) {
        this.dup_gender = dup_gender;
    }
    private String[] aloapproveselect;

    public String getTemp_id() {
        return beens.MyUtil.filterBad(temp_id);
    }

    public void setTemp_id(String temp_id) {
        this.temp_id = temp_id;
    }

    public String getTemp_worker_name() {
        return beens.MyUtil.filterBad(temp_worker_name);
    }

    public void setTemp_worker_name(String temp_worker_name) {
        this.temp_worker_name = temp_worker_name;
    }

    public String getTemp_father_name() {
        return beens.MyUtil.filterBad(temp_father_name);
    }

    public void setTemp_father_name(String temp_father_name) {
        this.temp_father_name = temp_father_name;
    }

    public String getTemp_age() {
        return beens.MyUtil.filterBad(temp_age);
    }

    public void setTemp_age(String temp_age) {
        this.temp_age = temp_age;
    }

    public String getTemp_gender() {
        return beens.MyUtil.filterBad(temp_gender);
    }

    public void setTemp_gender(String temp_gender) {
        this.temp_gender = temp_gender;
    }

    public String getTemp_alocode() {
        return beens.MyUtil.filterBad(temp_alocode);
    }

    public void setTemp_alocode(String temp_alocode) {
        this.temp_alocode = temp_alocode;
    }
    private String temp_alocode;
    private String presdistrict;
    private String presmandal;
    private String present_addr_pincode;
    private String present_addr_district;

    public String getPresent_addr_pincode() {
        return beens.MyUtil.filterBad(present_addr_pincode);
    }

    public void setPresent_addr_pincode(String present_addr_pincode) {
        this.present_addr_pincode = present_addr_pincode;
    }

    public String getPresent_addr_district() {
        return beens.MyUtil.filterBad(present_addr_district);
    }

    public void setPresent_addr_district(String present_addr_district) {
        this.present_addr_district = present_addr_district;
    }

    public String getPresent_addr_mandal() {
        return beens.MyUtil.filterBad(present_addr_mandal);
    }

    public void setPresent_addr_mandal(String present_addr_mandal) {
        this.present_addr_mandal = present_addr_mandal;
    }

    public String getPermenant_addr_pincode() {
        return beens.MyUtil.filterBad(permenant_addr_pincode);
    }

    public void setPermenant_addr_pincode(String permenant_addr_pincode) {
        this.permenant_addr_pincode = permenant_addr_pincode;
    }

    public String getPermenant_addr_district() {
        return beens.MyUtil.filterBad(permenant_addr_district);
    }

    public void setPermenant_addr_district(String permenant_addr_district) {
        this.permenant_addr_district = permenant_addr_district;
    }

    public String getPermenant_addr_mandal() {
        return beens.MyUtil.filterBad(permenant_addr_mandal);
    }

    public void setPermenant_addr_mandal(String permenant_addr_mandal) {
        this.permenant_addr_mandal = permenant_addr_mandal;
    }
    private String present_addr_mandal;
    private String permenant_addr_pincode;
    private String permenant_addr_district;
    private String permenant_addr_mandal;

    public String getPresdistrict() {
        return beens.MyUtil.filterBad(presdistrict);
    }

    public void setPresdistrict(String presdistrict) {
        this.presdistrict = presdistrict;
    }

    public String getPresmandal() {
        return beens.MyUtil.filterBad(presmandal);
    }

    public void setPresmandal(String presmandal) {
        this.presmandal = presmandal;
    }

    public String getPermdistrict() {
        return beens.MyUtil.filterBad(permdistrict);
    }

    public void setPermdistrict(String permdistrict) {
        this.permdistrict = permdistrict;
    }

    public String getPermmandal() {
        return beens.MyUtil.filterBad(permmandal);
    }

    public void setPermmandal(String permmandal) {
        this.permmandal = permmandal;
    }
    private String officer_code;
    private String alocode;

    public String getAlocode() {
        return beens.MyUtil.filterBad(alocode);
    }

    public void setAlocode(String alocode) {
        this.alocode = alocode;
    }

    public String getOfficer_code() {
        return beens.MyUtil.filterBad(officer_code);
    }

    public void setOfficer_code(String officer_code) {
        this.officer_code = officer_code;
    }
    private String permdistrict;
    private String permmandal;
    private String from_no;
    private String[] selectApprove;

    public String[] getSelectApprove() {
        return selectApprove;
    }

    public void setSelectApprove(String[] selectApprove) {
        this.selectApprove = selectApprove;
    }

    public String getFrom_no() {
        return beens.MyUtil.filterBad(from_no);
    }

    public void setFrom_no(String from_no) {
        this.from_no = from_no;
    }
    private String dispaly_worker_id;

    public String getDispaly_worker_id() {
        return beens.MyUtil.filterBad(dispaly_worker_id);
    }

    public void setDispaly_worker_id(String dispaly_worker_id) {
        this.dispaly_worker_id = dispaly_worker_id;
    }

    public String getTo_no() {
        return beens.MyUtil.filterBad(to_no);
    }

    public void setTo_no(String to_no) {
        this.to_no = to_no;
    }
    private int test;

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
    
    private String[] selectCards;

    

	public String[] getSelectCards() {
        return selectCards;
    }

    public void setSelectCards(String[] selectCards) {
        this.selectCards = selectCards;
    }
    private String to_no;
    private String worker_id;

    public String getWorker_id() {
        return beens.MyUtil.filterBad(worker_id);
    }

    public void setWorker_id(String worker_id) {
        this.worker_id = worker_id;
    }
    private String appNo;

    public String getAppNo() {
        return beens.MyUtil.filterBad(appNo);
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getNature_emp_name() {
        return beens.MyUtil.filterBad(nature_emp_name);
    }

    public void setNature_emp_name(String nature_emp_name) {
        this.nature_emp_name = nature_emp_name;
    }

    public int getSlno() {
        return slno;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public String getMembername() {
        return beens.MyUtil.filterBad(membername);
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getRelation() {
        return beens.MyUtil.filterBad(relation);
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getF_age() {
        return beens.MyUtil.filterBad(f_age);
    }

    public void setF_age(String f_age) {
        this.f_age = f_age;
    }

    public String getWorker_name() {
        return beens.MyUtil.filterBad(worker_name);
    }

    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    public String getFather_name() {
        return beens.MyUtil.filterBad(father_name);
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getAge() {
        return beens.MyUtil.filterBad(age);
    }

    public void setAge(String age) {
        this.age = age;
    }
    private String rationcard, rationcardno, aadharcard, aadharcardno, voterid, voteridno, arogyasri, arogyasrino, bankcard, bankcardno, employercertification, employercertificationno, jobcard, jobcardno, others, othersno;
    String[] sl_no = null;
    String[] membernamef = null;

    public String[] getSl_no() {
        return sl_no;
    }

    public void setSl_no(String[] sl_no) {
        this.sl_no = sl_no;
    }

    public String[] getMembernamef() {
        return membernamef;
    }

    public void setMembernamef(String[] membernamef) {
        this.membernamef = membernamef;
    }

    public String[] getFage() {
        return fage;
    }

    public void setFage(String[] fage) {
        this.fage = fage;
    }

    public String[] getRelation1() {
        return relation1;
    }

    public void setRelation1(String[] relation1) {
        this.relation1 = relation1;
    }

    public String[] getCard() {
        return card;
    }

    public void setCard(String[] card) {
        this.card = card;
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

    public String[] getRelation_name() {
        return relation_name;
    }

    public void setRelation_name(String[] relation_name) {
        this.relation_name = relation_name;
    }

    public String[] getNominee_name() {
        return nominee_name;
    }

    public void setNominee_name(String[] nominee_name) {
        this.nominee_name = nominee_name;
    }

    public String[] getFather_nominee() {
        return father_nominee;
    }

    public void setFather_nominee(String[] father_nominee) {
        this.father_nominee = father_nominee;
    }

    public String[] getOccupation() {
        return occupation;
    }

    public void setOccupation(String[] occupation) {
        this.occupation = occupation;
    }

    public String[] getNominee_age() {
        return nominee_age;
    }

    public void setNominee_age(String[] nominee_age) {
        this.nominee_age = nominee_age;
    }

    public String[] getPercentage() {
        return percentage;
    }

    public void setPercentage(String[] percentage) {
        this.percentage = percentage;
    }
    String[] fage = null;
    String[] relation1 = null;
    String[] card = null;
    String[] cardtype = null;
    String[] cardno = null;
    String[] relation_name = null;
    String[] nominee_name = null;
    String[] father_nominee = null;
    String[] occupation = null;
    String[] nominee_age = null;
    String[] percentage = null;

    public String getRationcard() {
        return beens.MyUtil.filterBad(rationcard);
    }

    public void setRationcard(String rationcard) {
        this.rationcard = rationcard;
    }

    public String getRationcardno() {
        return beens.MyUtil.filterBad(rationcardno);
    }

    public void setRationcardno(String rationcardno) {
        this.rationcardno = rationcardno;
    }

    public String getAadharcard() {
        return beens.MyUtil.filterBad(aadharcard);
    }

    public void setAadharcard(String aadharcard) {
        this.aadharcard = aadharcard;
    }

    public String getAadharcardno() {
        return beens.MyUtil.filterBad(aadharcardno);
    }

    public void setAadharcardno(String aadharcardno) {
        this.aadharcardno = aadharcardno;
    }

    public String getVoterid() {
        return beens.MyUtil.filterBad(voterid);
    }

    public void setVoterid(String voterid) {
        this.voterid = voterid;
    }

    public String getVoteridno() {
        return beens.MyUtil.filterBad(voteridno);
    }

    public void setVoteridno(String voteridno) {
        this.voteridno = voteridno;
    }

    public String getArogyasri() {
        return beens.MyUtil.filterBad(arogyasri);
    }

    public void setArogyasri(String arogyasri) {
        this.arogyasri = arogyasri;
    }

    public String getArogyasrino() {
        return beens.MyUtil.filterBad(arogyasrino);
    }

    public void setArogyasrino(String arogyasrino) {
        this.arogyasrino = arogyasrino;
    }

    public String getBankcard() {
        return beens.MyUtil.filterBad(bankcard);
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getBankcardno() {
        return beens.MyUtil.filterBad(bankcardno);
    }

    public void setBankcardno(String bankcardno) {
        this.bankcardno = bankcardno;
    }

    public String getEmployercertification() {
        return beens.MyUtil.filterBad(employercertification);
    }

    public void setEmployercertification(String employercertification) {
        this.employercertification = employercertification;
    }

    public String getEmployercertificationno() {
        return beens.MyUtil.filterBad(employercertificationno);
    }

    public void setEmployercertificationno(String employercertificationno) {
        this.employercertificationno = employercertificationno;
    }

    public String getJobcard() {
        return beens.MyUtil.filterBad(jobcard);
    }

    public void setJobcard(String jobcard) {
        this.jobcard = jobcard;
    }

    public String getJobcardno() {
        return beens.MyUtil.filterBad(jobcardno);
    }

    public void setJobcardno(String jobcardno) {
        this.jobcardno = jobcardno;
    }

    public String getOthers() {
        return beens.MyUtil.filterBad(others);
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getOthersno() {
        return beens.MyUtil.filterBad(othersno);
    }

    public void setOthersno(String othersno) {
        this.othersno = othersno;
    }
    private String worker_name;
    private String father_name;
    private String age;
    private String regno;
    private String gender;
    private String permenant_habcode;
    private String present_habcode;
    private String present_addr1;
    private String present_addr2;
    private String present_addr3;
    private String present_addr4;
    private String permenant_addr1;
    private String permenant_addr2;
    private String permenant_addr3;
    private String permenant_addr4;
    private String nature_emp;
    private String bank_accno;
    private String bank_name;
    private String branch_name;
    private String state;
    private String phoneno;
    private String nature_emp_name;
    private int slno;
    private String membername;
    private String relation;
    private String f_age;

    public String getPhoneno() {
        return beens.MyUtil.filterBad(phoneno);
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getRegno() {
        return beens.MyUtil.filterBad(regno);
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getGender() {
        return beens.MyUtil.filterBad(gender);
    }

    public void setGender(String gender) {
        this.gender = beens.MyUtil.filterBad(gender);
    }

    public String getPermenant_habcode() {
        return beens.MyUtil.filterBad(permenant_habcode);
    }

    public void setPermenant_habcode(String permenant_habcode) {
        this.permenant_habcode = permenant_habcode;
    }

    public String getPresent_habcode() {
        return beens.MyUtil.filterBad(present_habcode);
    }

    public void setPresent_habcode(String present_habcode) {
        this.present_habcode = present_habcode;
    }

    public String getPresent_addr1() {
        return beens.MyUtil.filterBad(present_addr1);
    }

    public void setPresent_addr1(String present_addr1) {
        this.present_addr1 = present_addr1;
    }

    public String getPresent_addr2() {
        return beens.MyUtil.filterBad(present_addr2);
    }

    public void setPresent_addr2(String present_addr2) {
        this.present_addr2 = present_addr2;
    }

    public String getPresent_addr3() {
        return beens.MyUtil.filterBad(present_addr3);
    }

    public void setPresent_addr3(String present_addr3) {
        this.present_addr3 = present_addr3;
    }

    public String getPresent_addr4() {
        return beens.MyUtil.filterBad(present_addr4);
    }

    public void setPresent_addr4(String present_addr4) {
        this.present_addr4 = present_addr4;
    }

    public String getPermenant_addr1() {
        return beens.MyUtil.filterBad(permenant_addr1);
    }

    public void setPermenant_addr1(String permenant_addr1) {
        this.permenant_addr1 = permenant_addr1;
    }

    public String getPermenant_addr2() {
        return beens.MyUtil.filterBad(permenant_addr2);
    }

    public void setPermenant_addr2(String permenant_addr2) {
        this.permenant_addr2 = permenant_addr2;
    }

    public String getPermenant_addr3() {
        return beens.MyUtil.filterBad(permenant_addr3);
    }

    public void setPermenant_addr3(String permenant_addr3) {
        this.permenant_addr3 = permenant_addr3;
    }

    public String getPermenant_addr4() {
        return beens.MyUtil.filterBad(permenant_addr4);
    }

    public void setPermenant_addr4(String permenant_addr4) {
        this.permenant_addr4 = permenant_addr4;
    }

    public String getNature_emp() {
        return beens.MyUtil.filterBad(nature_emp);
    }

    public void setNature_emp(String nature_emp) {
        this.nature_emp = nature_emp;
    }

    public String getBank_accno() {
        return beens.MyUtil.filterBad(bank_accno);
    }

    public void setBank_accno(String bank_accno) {
        this.bank_accno = bank_accno;
    }

    public String getBank_name() {
        return beens.MyUtil.filterBad(bank_name);
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBranch_name() {
        return beens.MyUtil.filterBad(branch_name);
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getState() {
        return beens.MyUtil.filterBad(state);
    }

    public void setState(String state) {
        this.state = state;
    }
    private String mode;

    public String getMode() {
        return beens.MyUtil.filterBad(mode);
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode1() {
        return mode1;
    }

    public void setMode1(String mode1) {
        this.mode1 = mode1;
    }
    private String mode1;
    private String caste;
    private String date_of_birth;
    private String present_addr5, present_addr6;
    private String permenant_addr5, permenant_addr6;
    private String union_name;
    private String pres_emp_name;
    private String pres_emp_district;
    private String pres_emp_mandal;
    private String pres_emp_habcode;
    private String pres_emp_village;
    private String pres_emp_pincode;
    private String migrantworkerstate;
    private String valid_date;
    private String qualification;
    private String spouse_appno;
//added reject list perpose on 02-01-17
    private String reject_reason;
    
    public String getReject_reason() {
		return reject_reason;
	}

	public void setReject_reason(String reject_reason) {
		this.reject_reason = reject_reason;
	}

	public String getQualification() {
        return beens.MyUtil.filterBad(qualification);
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpouse_appno() {
        return beens.MyUtil.filterBad(spouse_appno);
    }

    public void setSpouse_appno(String spouse_appno) {
        this.spouse_appno = spouse_appno;
    }

    public String getValid_date() {
        return beens.MyUtil.filterBad(valid_date);
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public String getMigrant_district() {
        return beens.MyUtil.filterBad(migrant_district);
    }

    public void setMigrant_district(String migrant_district) {
        this.migrant_district = migrant_district;
    }

    public String getMigrant_mandal() {
        return beens.MyUtil.filterBad(migrant_mandal);
    }

    public void setMigrant_mandal(String migrant_mandal) {
        this.migrant_mandal = migrant_mandal;
    }
    private String migrant_district;
    private String migrant_mandal;

    public String getMigrantworkerstate() {
        return beens.MyUtil.filterBad(migrantworkerstate);
    }

    public void setMigrantworkerstate(String migrantworkerstate) {
        this.migrantworkerstate = migrantworkerstate;
    }

    public String getPres_emp_pincode() {
        return beens.MyUtil.filterBad(pres_emp_pincode);
    }

    public void setPres_emp_pincode(String pres_emp_pincode) {
        this.pres_emp_pincode = pres_emp_pincode;
    }

    public String getPres_emp_village() {
        return beens.MyUtil.filterBad(pres_emp_village);
    }

    public void setPres_emp_village(String pres_emp_village) {
        this.pres_emp_village = pres_emp_village;
    }

    public String getPres_emp_habcode() {
        return beens.MyUtil.filterBad(pres_emp_habcode);
    }

    public void setPres_emp_habcode(String pres_emp_habcode) {
        this.pres_emp_habcode = pres_emp_habcode;
    }

    public String getPres_emp_mandal() {
        return beens.MyUtil.filterBad(pres_emp_mandal);
    }

    public void setPres_emp_mandal(String pres_emp_mandal) {
        this.pres_emp_mandal = pres_emp_mandal;
    }

    public String getPres_emp_district() {
        return beens.MyUtil.filterBad(pres_emp_district);
    }

    public void setPres_emp_district(String pres_emp_district) {
        this.pres_emp_district = pres_emp_district;
    }

    public String getPres_emp_name() {
        return beens.MyUtil.filterBad(pres_emp_name);
    }

    public void setPres_emp_name(String pres_emp_name) {
        this.pres_emp_name = pres_emp_name;
    }

    public String getPres_emp_dno() {
        return beens.MyUtil.filterBad(pres_emp_dno);
    }

    public void setPres_emp_dno(String pres_emp_dno) {
        this.pres_emp_dno = pres_emp_dno;
    }

    public String getPres_emp_location() {
        return beens.MyUtil.filterBad(pres_emp_location);
    }

    public void setPres_emp_location(String pres_emp_location) {
        this.pres_emp_location = pres_emp_location;
    }
    private String pres_emp_dno;
    private String pres_emp_location;

    public String getUnion_name() {
        return beens.MyUtil.filterBad(union_name);
    }

    public void setUnion_name(String union_name) {
        this.union_name = union_name;
    }

    public String getPermenant_addr5() {
        return beens.MyUtil.filterBad(permenant_addr5);
    }

    public void setPermenant_addr5(String permenant_addr5) {
        this.permenant_addr5 = permenant_addr5;
    }

    public String getPermenant_addr6() {
        return beens.MyUtil.filterBad(permenant_addr6);
    }

    public void setPermenant_addr6(String permenant_addr6) {
        this.permenant_addr6 = permenant_addr6;
    }

    public String getPresent_addr5() {
        return beens.MyUtil.filterBad(present_addr5);
    }

    public void setPresent_addr5(String present_addr5) {
        this.present_addr5 = present_addr5;
    }

    public String getPresent_addr6() {
        return beens.MyUtil.filterBad(present_addr6);
    }

    public void setPresent_addr6(String present_addr6) {
        this.present_addr6 = present_addr6;
    }

    public String getDate_of_birth() {
        return beens.MyUtil.filterBad(date_of_birth);
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getCaste() {
        return beens.MyUtil.filterBad(caste);
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }
    private String reg_date;

    public String getReg_date() {
        return beens.MyUtil.filterBad(reg_date);
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
    private String paper;

    public String getPaper() {
        return beens.MyUtil.filterBad(paper);
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }
}
