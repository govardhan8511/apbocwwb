package cardgen;

import java.io.PrintWriter;
import Login.SaltCheck;
import org.json.JSONObject;
import java.util.Arrays;
import DBC.Validations_new;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import com.onbarcode.barcode.QRCode;
import java.sql.SQLException;
import org.apache.commons.dbutils.DbUtils;
import java.sql.Date;
import DBC.DBConnection;
import org.apache.commons.lang.StringEscapeUtils;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import beens.MyUtil;
import org.apache.struts2.ServletActionContext;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.ActionSupport;

public class ALOApproveAction extends ActionSupport implements ModelDriven<ALOApproveFormBean>
{
    private static final long serialVersionUID = 1L;
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    String sql;
    String sql1;
    PreparedStatement ps1;
    ResultSet rs1;
    ResultSet rs2;
    ResultSet rs3;
    PreparedStatement ps2;
    PreparedStatement ps3;
    PreparedStatement ps4;
    PreparedStatement ps5;
    private List<ALOApproveFormBean> list;
    private List<ALOApproveFormBean> flist;
    private List<ALOApproveFormBean> cardList;
    private List<ALOApproveFormBean> approvedList;
    private ALOApproveFormBean cardGenEntryBean;
    private ALOApproveFormBean famlyEntryBean;
    private ALOApproveDAO cardGenEntryDAO;
    String Msg;
    private String regenid;
    private String dupcard_pay_date;
    private String receipt_no;
    private String reg_no;
    
    public ALOApproveAction() {
        this.con = null;
        this.ps = null;
        this.rs = null;
        this.sql = null;
        this.sql1 = null;
        this.ps1 = null;
        this.rs1 = null;
        this.rs2 = null;
        this.rs3 = null;
        this.ps2 = null;
        this.ps3 = null;
        this.ps4 = null;
        this.ps5 = null;
        this.list = new ArrayList<ALOApproveFormBean>();
        this.flist = new ArrayList<ALOApproveFormBean>();
        this.cardList = new ArrayList<ALOApproveFormBean>();
        this.approvedList = new ArrayList<ALOApproveFormBean>();
        this.cardGenEntryBean = new ALOApproveFormBean();
        this.famlyEntryBean = new ALOApproveFormBean();
        this.cardGenEntryDAO = new ALOApproveDAO();
        this.Msg = null;
    }
    
    public String getRegenid() {
        return this.regenid;
    }
    
    public void setRegenid(final String regenid) {
        this.regenid = regenid;
    }
    
    public String getDupcard_pay_date() {
        return this.dupcard_pay_date;
    }
    
    public void setDupcard_pay_date(final String dupcard_pay_date) {
        this.dupcard_pay_date = dupcard_pay_date;
    }
    
    public String getReceipt_no() {
        return this.receipt_no;
    }
    
    public void setReceipt_no(final String receipt_no) {
        this.receipt_no = receipt_no;
    }
    
    public String getReg_no() {
        return this.reg_no;
    }
    
    public void setReg_no(final String reg_no) {
        this.reg_no = reg_no;
    }
    
    public ALOApproveFormBean getFamlyEntryBean() {
        return this.famlyEntryBean;
    }
    
    public void setFamlyEntryBean(final ALOApproveFormBean famlyEntryBean) {
        this.famlyEntryBean = famlyEntryBean;
    }
    
    public List<ALOApproveFormBean> getFlist() {
        return this.flist;
    }
    
    public void setFlist(final List<ALOApproveFormBean> flist) {
        this.flist = flist;
    }
    
    public List<ALOApproveFormBean> getApprovedList() {
        return this.approvedList;
    }
    
    public void setApprovedList(final List<ALOApproveFormBean> approvedList) {
        this.approvedList = approvedList;
    }
    
    public List<ALOApproveFormBean> getCardList() {
        return this.cardList;
    }
    
    public void setCardList(final List<ALOApproveFormBean> cardList) {
        this.cardList = cardList;
    }
    
    public List<ALOApproveFormBean> getList() {
        return this.list;
    }
    
    public void setList(final List<ALOApproveFormBean> list) {
        this.list = list;
    }
    
    public ALOApproveDAO getCardGenEntryDAO() {
        return this.cardGenEntryDAO;
    }
    
    public void setCardGenEntryDAO(final ALOApproveDAO cardGenEntryDAO) {
        this.cardGenEntryDAO = cardGenEntryDAO;
    }
    
    public ALOApproveFormBean getCardGenEntryBean() {
        return this.cardGenEntryBean;
    }
    
    public void setCardGenEntryBean(final ALOApproveFormBean cardGenEntryBean) {
        this.cardGenEntryBean = cardGenEntryBean;
    }
    
    public String vendorEntryList() throws Exception {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        final String userid = MyUtil.filterBad(new StringBuilder().append(session.getAttribute("user_id")).toString()).trim();
        if (userid.equalsIgnoreCase("") || userid.equalsIgnoreCase("null")) {
            return "invalidata";
        }
        this.list = (List<ALOApproveFormBean>)this.cardGenEntryDAO.getList(userid);
        return "success";
    }
    
    public String entryList() throws Exception {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        final String userid = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("user_name")).toString()));
        final String userdata = userid.substring(0, 3);
        final String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("alocode")).toString()));
        if (userid.equalsIgnoreCase("") || userid.equalsIgnoreCase("null")) {
            return "invalidata";
        }
        this.list = (List<ALOApproveFormBean>)this.cardGenEntryDAO.getList(alocode, userdata);
        return "success";
    }
    
    public String getIDData() throws SQLException {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        int trno = 0;
        session.setAttribute("paper", (Object)"2");
        String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad((String)session.getAttribute("alocode")));
        final String userid = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("user_id")).toString()));
        if (alocode.equalsIgnoreCase("")) {
            alocode = null;
        }
        if (userid.equalsIgnoreCase("") || userid.equalsIgnoreCase("null")) {
            return "invalidata";
        }
        String[] cards = null;
        final String idtype = StringEscapeUtils.escapeJavaScript(new StringBuilder().append(session.getAttribute("cardtype")).toString());
        if (idtype.equals("duplicate")) {
            final String[] dupworkerid = cards = new String[] { new StringBuilder().append(session.getAttribute("dupworkerid")).toString() };
        }
        else if (idtype.equals("renewal")) {
            final String[] dupworkerid = cards = new String[] { new StringBuilder().append(session.getAttribute("worker_id")).toString() };
        }
        else {
            cards = this.cardGenEntryBean.getSelectCards();
        }
        String basicsql = null;
        String approvstatus = null;
        try {
            this.con = DBConnection.getConnection();
            for (int i = 0; i < cards.length; ++i) {
                final String workerid = MyUtil.filterBad(cards[i].trim());
                
                	if (!(alocode == null)) {
    					basicsql = "SELECT concat('XXXXXXXX',substring(a.worker_aadhaar_no,9) ) worker_aadhaar_no, a.worker_reg_no, a.alo_code, worker_id,initcap(worker_name) worker_name,"
    							+ "initcap(relation_name) relation_name, COALESCE (trade_name,'NA') trade_name, concat('XXXXXX',substring(mobile_no,7) ) mobile_no, date_of_birth, reg_date,rec_valid_upto,  "
    							+ " COALESCE(gendername,'NA')gendername, jurisdiction, approval_status, created_by,house_no_temp house_no, street_name_temp street_name,  temp_state_name state_txt,temp_dist_name district_txt ,"
    							+ " temp_mandal_name mandal_txt , coalesce(perm_village_name,temp_village_name) village_txt,temp_pincode aadhaar_pincode  FROM public.view_idcard_details a left  join public.view_wr_address b "
    							+ " on a.worker_reg_no=b.worker_reg_no  where  a.alo_code=? and  worker_id=? ";
    					ps = con.prepareStatement(basicsql);
    					ps.setString(1, alocode);
    					ps.setString(2, workerid);
    				} else {
    					basicsql = "SELECT concat('XXXXXXXX',substring(a.worker_aadhaar_no,9) ) worker_aadhaar_no, a.worker_reg_no, a.alo_code, worker_id,initcap(worker_name) worker_name,"
    							+ "initcap(relation_name) relation_name, COALESCE (trade_name,'NA') trade_name, concat('XXXXXX',substring(mobile_no,7) ) mobile_no, date_of_birth, reg_date,  rec_valid_upto,"
    							+ "  COALESCE(gendername,'NA')gendername, jurisdiction, approval_status, created_by,house_no_temp house_no, street_name_temp street_name,  temp_state_name state_txt,temp_dist_name district_txt ,"
    							+ "  temp_mandal_name mandal_txt , coalesce(perm_village_name,temp_village_name) village_txt,temp_pincode aadhaar_pincode  FROM public.view_idcard_details a left  join public.view_wr_address b "
    							+ "  on a.worker_reg_no=b.worker_reg_no   where  created_by=? and  worker_id=?  and approval_status in ('Y','A','T') ";
    					ps = con.prepareStatement(basicsql);
    					ps.setString(1, userid);
    					ps.setString(2, workerid);
    					
    				}
                	System.out.println("ps iiss->"+ps);
                this.rs = this.ps.executeQuery();
                while (this.rs.next()) {
                    (this.cardGenEntryBean = new ALOApproveFormBean()).setProof(this.rs.getString("worker_aadhaar_no"));
                    this.cardGenEntryBean.setTemp_alocode(this.rs.getString("alo_code"));
                    final String empcode = MyUtil.GetAloEmpCode(this.rs.getString("alo_code"));
                    this.cardGenEntryBean.setAlocode(empcode);
                    this.cardGenEntryBean.setWorker_name(this.rs.getString("worker_name"));
                    this.cardGenEntryBean.setFather_name(this.rs.getString("relation_name"));
                    final String workId = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(this.rs.getString("worker_id")));
                    session.setAttribute("worker_id", (Object)workId);
                    approvstatus = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(this.rs.getString("approval_status")));
                    this.cardGenEntryBean.setRegno(this.rs.getString("worker_reg_no"));
                    this.cardGenEntryBean.setWorker_id(workId);
                    this.cardGenEntryBean.setWorker_name(this.rs.getString("worker_name"));
                    this.cardGenEntryBean.setGender(this.rs.getString("gendername"));
                    this.cardGenEntryBean.setPhoneno(this.rs.getString("mobile_no"));
                    this.cardGenEntryBean.setNature_emp_name(this.rs.getString("trade_name"));
                    String wrdob = this.rs.getString("date_of_birth");
                    String regdt = this.rs.getString("reg_date");
                    this.cardGenEntryBean.setPhoneno(this.rs.getString("mobile_no"));
                    if (wrdob == null) {
                        wrdob = "NA";
                    }
                    else {
                        wrdob = MyUtil.ChDate(wrdob);
                    }
                    if (regdt == null) {
                        regdt = "NA";
                    }
                    else {
                        regdt = MyUtil.ChDate(regdt);
                    }
                    String rec_valid=rs.getString("rec_valid_upto");
                    if(rec_valid==null) {
                    	rec_valid="NA";
                    }
                    else {
                    	rec_valid=MyUtil.ChDate(rec_valid);
                    }
                    this.cardGenEntryBean.setReg_date(regdt);
                    this.cardGenEntryBean.setDate_of_birth(wrdob);
                    this.cardGenEntryBean.setLegacy_data(rec_valid);
                    final String jurisdiction = this.rs.getString("jurisdiction");
                    this.cardGenEntryBean.setOfficer_code(jurisdiction);
                    this.cardGenEntryBean.setPermenant_addr1(this.rs.getString("house_no"));
                    this.cardGenEntryBean.setPermenant_addr2(this.rs.getString("street_name"));
                    this.cardGenEntryBean.setPermenant_addr_district(this.rs.getString("district_txt"));
                    this.cardGenEntryBean.setPermenant_addr_mandal(this.rs.getString("mandal_txt"));
                    this.cardGenEntryBean.setPermenant_habcode(this.rs.getString("village_txt"));
                    this.cardGenEntryBean.setPermenant_addr_pincode(this.rs.getString("aadhaar_pincode"));
                    this.cardList.add(this.cardGenEntryBean);
                    System.out.println("sixe------?" + this.cardList.size());
                }
                if (approvstatus == null) {
                    approvstatus = "T";
                }
                else if (approvstatus.equals("Y")) {
                    approvstatus = "A";
                }
                else if (approvstatus.equals("A")) {
                    approvstatus = "T";
                }
                if (alocode != null) {
                    trno = MyUtil.logTrace(userid, "wr_basic_details", "update", request);
                    this.sql = "update wr_basic_details set approval_status=?,printdate=?, trno=? where worker_id=? and alo_code=?";
                    (this.ps2 = this.con.prepareStatement(this.sql)).setString(1, approvstatus);
                    this.ps2.setDate(2, Date.valueOf(MyUtil.ChDate1(MyUtil.gettodaysDate())));
                    this.ps2.setInt(3, trno);
                    this.ps2.setString(4, workerid);
                    this.ps2.setString(5, alocode);
                    this.ps2.executeUpdate();
                }
                else {
                    trno = MyUtil.logTrace(userid, "wr_basic_details", "update", request);
                    this.sql = "update wr_basic_details set approval_status=?,printdate=?, trno=? where worker_id=? and created_by=?";
                    (this.ps2 = this.con.prepareStatement(this.sql)).setString(1, approvstatus);
                    this.ps2.setDate(2, Date.valueOf(MyUtil.ChDate1(MyUtil.gettodaysDate())));
                    this.ps2.setInt(3, trno);
                    this.ps2.setString(4, workerid);
                    this.ps2.setString(5, userid);
                    this.ps2.executeUpdate();
                }
            }
        }
        catch (Exception e) {
            System.out.println("exception is------->" + e);
            e.printStackTrace();
        }
        finally {
            if (this.rs1 != null) {
                try {
                    this.rs1.close();
                }
                catch (Exception ex) {}
            }
            if (this.rs != null) {
                try {
                    this.rs.close();
                }
                catch (Exception ex2) {}
            }
            if (this.ps1 != null) {
                try {
                    this.ps1.close();
                }
                catch (Exception ex3) {}
            }
            if (this.ps != null) {
                try {
                    this.ps.close();
                }
                catch (Exception ex4) {}
            }
            if (this.con != null) {
                try {
                    DbUtils.closeQuietly(this.con);
                }
                catch (Exception ex5) {}
            }
        }
        if (this.rs1 != null) {
            try {
                this.rs1.close();
            }
            catch (Exception ex6) {}
        }
        if (this.rs != null) {
            try {
                this.rs.close();
            }
            catch (Exception ex7) {}
        }
        if (this.ps1 != null) {
            try {
                this.ps1.close();
            }
            catch (Exception ex8) {}
        }
        if (this.ps != null) {
            try {
                this.ps.close();
            }
            catch (Exception ex9) {}
        }
        if (this.con != null) {
            try {
                DbUtils.closeQuietly(this.con);
            }
            catch (Exception ex10) {}
        }
        return "success";
    }
    
    public String QRCode() throws IOException, Exception {
        try {
            final HttpServletRequest request = ServletActionContext.getRequest();
            final HttpServletResponse response = ServletActionContext.getResponse();
            final String data = MyUtil.filterBad(request.getParameter("DATA"));
            final QRCode barcode = new QRCode();
            if (data != null) {
                barcode.setData(data);
            }
            final ServletOutputStream servletoutputstream = response.getOutputStream();
            response.setContentType("image/jpeg");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0L);
            barcode.drawBarcode((OutputStream)servletoutputstream);
        }
        catch (Exception ex) {}
        return "none";
    }
    
    public ALOApproveFormBean getModel() {
        return this.cardGenEntryBean;
    }
    
    public String approveALOPage() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        final String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("alocode")).toString()));
        if (alocode.length() < 0) {
            return "invalidata";
        }
        this.Msg = Validations_new.Validate(alocode, 4, 4, false);
        if (this.Msg != null) {
            return "invalidata";
        }
        try {
            this.con = DBConnection.getConnection();
            this.sql1 = "select a.worker_aadhaar_no,a.temp_id,a.worker_name,a.relation_name,b.gendername from wr_basic_details a,gender_master b where worker_aadhaar_no is not null and alo_code=?  and a.gender_code=b.numericcode and a.approval_status ='N' and  reject_status is null ";
            (this.ps1 = this.con.prepareStatement(this.sql1)).setString(1, alocode);
            this.rs1 = this.ps1.executeQuery();
            while (this.rs1.next()) {
                (this.cardGenEntryBean = new ALOApproveFormBean()).setAadharcardno(this.rs1.getString(1));
                this.cardGenEntryBean.setTemp_id(this.rs1.getString(2));
                this.cardGenEntryBean.setTemp_worker_name(this.rs1.getString(3));
                this.cardGenEntryBean.setTemp_father_name(new StringBuilder().append(this.rs1.getString(4)).toString().equals("null") ? "N/A" : this.rs1.getString(4));
                this.cardGenEntryBean.setTemp_gender(this.rs1.getString(5));
                this.list.add(this.cardGenEntryBean);
            }
            return "success";
        }
        catch (Exception e) {
            return "failure";
        }
        finally {
            if (this.rs1 != null) {
                try {
                    this.rs1.close();
                }
                catch (Exception ex) {}
            }
            if (this.ps1 != null) {
                try {
                    this.ps1.close();
                }
                catch (Exception ex2) {}
            }
            if (this.con != null) {
                try {
                    DbUtils.closeQuietly(this.con);
                }
                catch (Exception ex3) {}
            }
        }
    }
    
    public String aloApproveSuccess() throws SQLException {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        final String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("alocode")).toString()));
        if (alocode.length() < 0) {
            return "invalidata";
        }
        this.Msg = Validations_new.Validate(alocode, 4, 4, false);
        if (this.Msg != null) {
            return "invalidata";
        }
        final String query = null;
        boolean commit = true;
        String year = null;
        final String user_id = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("user_name")).toString()));
        final String[] selectedApproval = this.cardGenEntryBean.getAloapproveselect();
        int count = 0;
        String worker_id = null;
        String reg_no = null;
        int trno = 0;
        final PreparedStatement pstmt = null;
        PreparedStatement ps1 = null;
        final PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps4 = null;
        PreparedStatement ps5 = null;
        PreparedStatement ps6 = null;
        PreparedStatement ps7 = null;
        PreparedStatement ps8 = null;
        PreparedStatement ps9 = null;
        PreparedStatement ps10 = null;
        final ResultSet rs1 = null;
        final ResultSet rs2 = null;
        ResultSet rs3 = null;
        int temp_id = 0;
        try {
            (this.con = DBConnection.getConnection()).setAutoCommit(false);
            if (selectedApproval.length == 0) {
                return "invalidata";
            }
            String str = Arrays.toString(selectedApproval);
            System.out.println("str"+str);
            str = str.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "");
            str = str.replace(",", "','");
            for (int i = 0; i < selectedApproval.length; ++i) {
                final String aadhaarno = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(selectedApproval[i].trim()));
               System.out.println("aadhaarno---->"+aadhaarno);
                year = MyUtil.gettodaysDate().substring(6, 10);
                if (year == null) {
                    request.setAttribute("run_time_errors", (Object)"year error");
                    return "runtime_error";
                }
                count = MyUtil.alo_reg_sequence(alocode.toLowerCase());
                if (count == 0) {
                    this.cardGenEntryBean.setAadharcard("sequence error");
                    request.setAttribute("run_time_errors", (Object)"sequence error");
                    return "runtime_error";
                }
                worker_id = MyUtil.filterBad(String.valueOf(alocode) + year + String.format("%05d", count));
                final String alocodeShortName = MyUtil.getOptionValue("officer_master", "officer_code", "alo_sname", alocode);
                reg_no = MyUtil.filterBad(String.valueOf(alocodeShortName.trim()) + "/" + year + "/" + String.format("%05d", count));
                trno = MyUtil.logTrace(user_id, "wr_basic_details", "update", request);
                final String temp_st = MyUtil.GetFieldName("wr_basic_details", "temp_id", "worker_aadhaar_no='" + aadhaarno + "'");
                if (temp_st.equalsIgnoreCase("NA")) {
                    return "runtime_error";
                }
                temp_id = Integer.parseInt(temp_st);
                final String wrbasic = "update wr_basic_details set worker_reg_no= ? , worker_id = ? , approval_status = ?, alo_trno = ? where alo_code=? and temp_id=? ";
                ps1 = this.con.prepareStatement(wrbasic);
                ps1.setString(1, reg_no);
                ps1.setString(2, worker_id);
                ps1.setString(3, "Y");
                ps1.setInt(4, trno);
                ps1.setString(5, alocode);
                ps1.setInt(6, temp_id);
                ps1.executeUpdate();
                System.out.println("ps1---->"+ps1);
                final String wraddrsorg = "update wr_address_details_org set worker_reg_no= ? where alo_code=? and temp_id=?  ";
                ps3 = this.con.prepareStatement(wraddrsorg);
                ps3.setString(1, reg_no);
                ps3.setString(2, alocode);
                ps3.setInt(3, temp_id);
                System.out.println("ps3---->"+ps3);
                ps3.executeUpdate();
                final String wrkattch = "update worker_attachments set worker_id = ? where alocode=? and temp_id=? ";
                ps4 = this.con.prepareStatement(wrkattch);
                ps4.setString(1, worker_id);
                ps4.setString(2, alocode);
                ps4.setInt(3, temp_id);
                ps4.executeUpdate();
                final String wrkphts = "update worker_photos set worker_id = ? , reg_no= ? where alocode=? and  temp_id=? ";
                ps5 = this.con.prepareStatement(wrkphts);
                ps5.setString(1, worker_id);
                ps5.setString(2, reg_no);
                ps5.setString(3, alocode);
                ps5.setInt(4, temp_id);
                ps5.executeUpdate();
                final String wrkbnkact = "update worker_bank_acct_details set worker_reg_no=? where alo_code=? and temp_id=? ";
                ps6 = this.con.prepareStatement(wrkbnkact);
                ps6.setString(1, reg_no);
                ps6.setString(2, alocode);
                ps6.setInt(3, temp_id);
                ps6.executeUpdate();
                final String wrkfaml = "update worker_family_details set worker_reg_no= ? where alo_code=? and temp_id=? ";
                ps7 = this.con.prepareStatement(wrkfaml);
                ps7.setString(1, reg_no);
                ps7.setString(2, alocode);
                ps7.setInt(3, temp_id);
                ps7.executeUpdate();
                final String wrkpaym = "update worker_payments set worker_reg_no=? where alo_code=? and temp_id=? ";
                ps8 = this.con.prepareStatement(wrkpaym);
                ps8.setString(1, reg_no);
                ps8.setString(2, alocode);
                ps8.setInt(3, temp_id);
                ps8.executeUpdate();
                final String wrkplcdt = "update worker_work_place_details set worker_reg_no=? where alo_code=? and temp_id=? ";
                ps9 = this.con.prepareStatement(wrkplcdt);
                ps9.setString(1, reg_no);
                ps9.setString(2, alocode);
                ps9.setInt(3, temp_id);
                ps9.executeUpdate();
                final String wr_nominee = "update worker_nominee_details set worker_reg_no= ? where  worker_aadhaar_no=? ";
                ps9 = this.con.prepareStatement(wr_nominee);
                ps9.setString(1, reg_no);
                ps9.setString(2, aadhaarno);
                ps9.executeUpdate();
            }
            final String aprvsucc = "select a.worker_id,a.worker_reg_no,a.worker_name,a.relation_name,a.date_of_birth,a.gender_code,b.gendername  from wr_basic_details a,gender_master b where a.gender_code=b.numericcode and worker_aadhaar_no in ('" + str + "') and alo_code=? and approval_status = 'Y' ";
            ps10 = this.con.prepareStatement(aprvsucc);
            ps10.setString(1, alocode);
            rs3 = ps10.executeQuery();
            System.out.println("aprvsucc--->"+aprvsucc);
            while (rs3.next()) {
                (this.cardGenEntryBean = new ALOApproveFormBean()).setWorker_id(rs3.getString("worker_id"));
                this.cardGenEntryBean.setRegno(rs3.getString("worker_reg_no"));
                this.cardGenEntryBean.setWorker_name(rs3.getString("worker_name"));
                this.cardGenEntryBean.setFather_name(rs3.getString("relation_name"));
                String wrdobdt = rs3.getString("date_of_birth");
                if (wrdobdt == null || wrdobdt == "") {
                    wrdobdt = "N/A";
                }
                else {
                    wrdobdt = MyUtil.ChDate(wrdobdt);
                }
                this.cardGenEntryBean.setAge(wrdobdt);
                this.cardGenEntryBean.setGender(rs3.getString("gendername"));
                this.approvedList.add(this.cardGenEntryBean);
            }
        }
        catch (SQLException e) {
            commit = false;
            System.out.println("Approval of Reg Error-->:" + e);
            e.printStackTrace();
            return "success";
        }
        finally {
            if (commit) {
                this.con.commit();
            }
            else {
                this.con.rollback();
            }
            if (rs1 != null) {
                try {
                    rs1.close();
                }
                catch (SQLException ex) {}
            }
            if (this.rs != null) {
                try {
                    this.rs.close();
                }
                catch (SQLException ex2) {}
            }
            if (this.ps != null) {
                try {
                    this.ps.close();
                }
                catch (SQLException ex3) {}
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                }
                catch (SQLException ex4) {}
            }
            if (ps2 != null) {
                try {
                    ps2.close();
                }
                catch (SQLException ex5) {}
            }
            if (ps4 != null) {
                try {
                    ps4.close();
                }
                catch (SQLException ex6) {}
            }
            if (ps5 != null) {
                try {
                    ps5.close();
                }
                catch (SQLException ex7) {}
            }
            if (ps6 != null) {
                try {
                    ps6.close();
                }
                catch (SQLException ex8) {}
            }
            if (ps3 != null) {
                try {
                    ps3.close();
                }
                catch (SQLException ex9) {}
            }
            if (this.con != null) {
                DbUtils.closeQuietly(this.con);
            }
        }
        if (commit) {
//            this.con.commit();
        }
        else {
            this.con.rollback();
        }
        if (rs1 != null) {
            try {
                rs1.close();
            }
            catch (SQLException ex10) {}
        }
        if (this.rs != null) {
            try {
                this.rs.close();
            }
            catch (SQLException ex11) {}
        }
        if (this.ps != null) {
            try {
                this.ps.close();
            }
            catch (SQLException ex12) {}
        }
        if (ps1 != null) {
            try {
                ps1.close();
            }
            catch (SQLException ex13) {}
        }
        if (ps2 != null) {
            try {
                ps2.close();
            }
            catch (SQLException ex14) {}
        }
        if (ps4 != null) {
            try {
                ps4.close();
            }
            catch (SQLException ex15) {}
        }
        if (ps5 != null) {
            try {
                ps5.close();
            }
            catch (SQLException ex16) {}
        }
        if (ps6 != null) {
            try {
                ps6.close();
            }
            catch (SQLException ex17) {}
        }
        if (ps3 != null) {
            try {
                ps3.close();
            }
            catch (SQLException ex18) {}
        }
        if (this.con != null) {
            DbUtils.closeQuietly(this.con);
        }
        return "success";
    }
    
    public static List<ALOApproveFormBean> approveALOPageLost() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        String Msg = null;
        final String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("alocode")).toString()));
        ALOApproveFormBean apbean = new ALOApproveFormBean();
        final List<ALOApproveFormBean> aplist = new ArrayList<ALOApproveFormBean>();
        if (alocode.length() < 0) {
            return aplist;
        }
        Msg = Validations_new.Validate(alocode, 4, 4, false);
        if (Msg != null) {
            return aplist;
        }
        Connection con = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        String sql1 = "";
        try {
            con = DBConnection.getConnection();
            sql1 = "select a.worker_aadhaar_no,a.temp_id,a.worker_name,a.relation_name,b.gendername from wr_basic_details a,gender_master b where worker_aadhaar_no is not null and alo_code=?  and a.gender_code=b.numericcode and a.approval_status ='N' and ( reject_status is null or reject_status='A') ";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, alocode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                apbean = new ALOApproveFormBean();
                apbean.setAadharcardno(rs1.getString(1));
                apbean.setTemp_id(rs1.getString(2));
                apbean.setTemp_worker_name(rs1.getString(3));
                apbean.setTemp_father_name(new StringBuilder().append(rs1.getString(4)).toString().equals("null") ? "N/A" : rs1.getString(4));
                apbean.setTemp_gender(rs1.getString(5));
                aplist.add(apbean);
            }
            return aplist;
        }
        catch (Exception e) {
            return aplist;
        }
        finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                }
                catch (Exception ex) {}
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                }
                catch (Exception ex2) {}
            }
            if (con != null) {
                try {
                    DbUtils.closeQuietly(con);
                }
                catch (Exception ex3) {}
            }
        }
    }
    
    public static List<ALOApproveFormBean> AloRejectedList() {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        String Msg = null;
        final String alocode = MyUtil.filterBad((String)session.getAttribute("alocode"));
        ALOApproveFormBean apbean = new ALOApproveFormBean();
        final List<ALOApproveFormBean> aplist = new ArrayList<ALOApproveFormBean>();
        if (alocode.length() < 0) {
            return aplist;
        }
        Msg = Validations_new.Validate(alocode, 4, 4, false);
        if (Msg != null) {
            return aplist;
        }
        Connection con = null;
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        String sql1 = "";
        try {
            con = DBConnection.getConnection();
            sql1 = "select a.worker_aadhaar_no,a.temp_id,a.worker_name,a.relation_name,b.gendername,a.reject_reason from wr_basic_details a,gender_master b where worker_aadhaar_no is not null and alo_code=?  and a.gender_code=b.numericcode and  a.reject_status='Y' ";
            ps1 = con.prepareStatement(sql1);
            ps1.setString(1, alocode);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
                apbean = new ALOApproveFormBean();
                apbean.setAadharcardno(rs1.getString(1));
                apbean.setTemp_id(rs1.getString(2));
                apbean.setTemp_worker_name(rs1.getString(3));
                apbean.setTemp_father_name(new StringBuilder().append(rs1.getString(4)).toString().equals("null") ? "N/A" : rs1.getString(4));
                apbean.setTemp_gender(rs1.getString(5));
                apbean.setReject_reason(rs1.getString(6));
                aplist.add(apbean);
            }
            return aplist;
        }
        catch (Exception e) {
            return aplist;
        }
        finally {
            if (rs1 != null) {
                try {
                    rs1.close();
                }
                catch (Exception ex) {}
            }
            if (ps1 != null) {
                try {
                    ps1.close();
                }
                catch (Exception ex2) {}
            }
            if (con != null) {
                try {
                    DbUtils.closeQuietly(con);
                }
                catch (Exception ex3) {}
            }
        }
    }
    
    public String printPassword() throws Exception {
        final HttpServletRequest request = ServletActionContext.getRequest();
        final HttpServletResponse response = ServletActionContext.getResponse();
        final HttpSession session = request.getSession();
        final String pagename = request.getHeader("referer");
        if (pagename.matches("(.*)checkpasswrd(.*)")) {
            return "invalidaction";
        }
        final String prntpwd = MyUtil.filterBad(request.getParameter("printpasswd"));
        final PrintWriter out = response.getWriter();
        final JSONObject json = new JSONObject();
        String org_pwd = null;
        final String org_userid = null;
        try {
            this.con = DBConnection.getConnection();
            final String userid = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(new StringBuilder().append(session.getAttribute("user_id")).toString()));
            if (userid.equalsIgnoreCase("") || userid.equalsIgnoreCase("null")) {
                json.put("Error", (Object)"Invalid Data");
                out.print(json);
                return null;
            }
            this.sql = "select user_id,printpassword from user_master where user_id= ? ";
            (this.ps = this.con.prepareStatement(this.sql)).setString(1, userid);
            this.rs = this.ps.executeQuery();
            if (!this.rs.next()) {
                json.put("password", (Object)"incorrect");
                out.print(json);
                return null;
            }
            org_pwd = this.rs.getString("printpassword");
            if (SaltCheck.saltcheck(org_pwd, prntpwd).equalsIgnoreCase("OK")) {
                json.put("password", (Object)"correct");
                out.print(json);
                return null;
            }
            json.put("password", (Object)"incorrect");
            out.print(json);
            return null;
        }
        catch (Exception c) {
            json.put("Error", (Object)"SRDH Server Down");
            out.print(json);
            return null;
        }
    }
    
    public void validate() {
    }
}