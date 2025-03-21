package Registration;

import beens.MyUtil;
import beens.RegistrationBeen;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import WorkerRegistration_NewAndEdit.Age;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.struts2.ServletActionContext;

public class PreviewRegistration extends ActionSupport implements ModelDriven<RegistrationBeen> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	RegistrationBeen rb = new RegistrationBeen();
	RegistrationBeen rb1=new RegistrationBeen();
	
	
	
    public RegistrationBeen getRb1() {
		return rb1;
	}

	public void setRb1(RegistrationBeen rb1) {
		this.rb1 = rb1;
	}

	Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
 List<RegistrationBeen> memList = new ArrayList<RegistrationBeen>();
  public RegistrationBeen getRb() {
        return rb;
    }

    public void setRb(RegistrationBeen rb) {
        this.rb = rb;
    }

    public List<RegistrationBeen> getMemList() {
        return memList;
    }

    public void setMemList(List<RegistrationBeen> memList) {
        this.memList = memList;
    }
    public String Preview() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        con = DBC.DBConnection.getConnection();
        rb1.setAction_type(rb.getAction_type());
       String aadharno = MyUtil.filterBad(""+session.getAttribute("workeraadhaar"));
        String alocode = MyUtil.filterBad("" + session.getAttribute("workeralocode"));
        String temp_id=MyUtil.filterBad((""+session.getAttribute("temp_id")).trim());
        try {
//            String sql = "SELECT worker_aadhaar_no,  alo_code,gender_code, alo_dist_code, worker_name,  date_of_birth, worker_age, caste_code, relation_name, worker_trade_code, no_work_days_year, mnregs_worker, migrated_worker, marital_status, mobile_no FROM wr_basic_details where worker_aadhaar_no='"+aadharno+"' limit 1";
            String sql = "SELECT a.alo_code,worker_aadhaar_no,  jurisdiction,gendername,  worker_name,  date_of_birth,  caste_name, relation_name, trade_name, no_work_days_year, mnregs_worker, migrated_worker, \n"
                    + "marital_status_name, mobile_no,worker_email,alo_app_num,aadhaar_verify,a.temp_id FROM wr_basic_details a inner join officer_master b on a.alo_code=b.officer_code \n"
                    + "inner join gender_master c on a.gender_code=c.numericcode inner join caste_master d on a.caste_code=d.numericcode inner join trade_master e on a.worker_trade_code=e.numericcode\n"
                    + "inner join marital_status_master f on a.marital_status=f.numericcode\n"
                    + " where temp_id=?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(temp_id));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                rb1.setAadhar_no(MyUtil.filterBad(rs.getString("worker_aadhaar_no")));
                rb1.setAlo_name(MyUtil.filterBad(rs.getString("jurisdiction")));
                rb1.setGenderen(MyUtil.filterBad(rs.getString("gendername")));
                rb1.setWorker_name(MyUtil.filterBad(rs.getString("worker_name")));
                rb1.setAlo_code(MyUtil.filterBad(rs.getString("alo_code")));
                rb1.setTemp_id(rs.getInt("temp_id"));
                try{
                rb1.setDob(MyUtil.ChDate(MyUtil.filterBad(rs.getString("date_of_birth"))));
                }catch(Exception e){
                	rb1.setDob("NA");
                }
                request.setAttribute("caste_code", MyUtil.filterBad(rs.getString("caste_name")));
                rb1.setCast_name(MyUtil.filterBad(rs.getString("caste_name")));
                rb1.setRelation_name(MyUtil.filterBad(rs.getString("relation_name")));
                rb1.setTrade_name(MyUtil.filterBad(rs.getString("trade_name")));
                request.setAttribute("no_work_days_year", MyUtil.filterBad(rs.getString("no_work_days_year")));
                rb1.setWork_days(rs.getInt("no_work_days_year"));
                rb1.setMnregs_worker(MyUtil.filterBad(rs.getString("mnregs_worker")));
                rb1.setMarital_stats_name(MyUtil.filterBad(rs.getString("marital_status_name")));
                rb1.setMobile_no(MyUtil.filterBad(rs.getString("mobile_no")));
                rb1.setEmail(MyUtil.filterBad(rs.getString("worker_email")));
                rb1.setAlo_app_num(rs.getInt("alo_app_num"));
                rb1.setAction_type(rs.getString("aadhaar_verify"));
            }
            String sql1 = "select slno,  state_name, dist_name, mandal_name  FROM wr_migrated_details a\n"
                    + "inner join state_master b on a.state_code=b.state_code inner join district_master c on c.state_code=a.state_code and a.dist_code=c.dist_code inner join mandal_master d on d.state_code=a.state_code and d.dist_code=a.dist_code and a.mandal_code=d.mandal_code and a.dist_code=d.dist_code where temp_id=?";
            pstmt = con.prepareStatement(sql1);
            pstmt.setInt(1, Integer.parseInt(temp_id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rb1.setMigrated_state_name(MyUtil.filterBad(rs.getString("state_name")));
                rb1.setMigrated_dist(MyUtil.filterBad(rs.getString("dist_name")));
                rb1.setMigrated_mandal(MyUtil.filterBad(rs.getString("mandal_name")));
            }else{
            	 rb1.setMigrated_state_name("NA");
                 rb1.setMigrated_dist("NA");
                 rb1.setMigrated_mandal("NA");
            }
            //======================
            String sqlv1="SELECT    house_no_temp, street_name_temp,house_no_perm, street_name_perm,"
            		+ " temp_state_name, perm_state_name, temp_dist_name, perm_dist_name, temp_mandal_name, perm_mandal_name,temp_village_name,"
            		+ " perm_village_name, temp_habname, perm_habname, temp_pincode, perm_pincode  FROM public.view_wr_address  where temp_id=?";
            pstmt = con.prepareStatement(sqlv1);
            pstmt.setInt(1, Integer.parseInt(temp_id));
          rs = pstmt.executeQuery();
          while (rs.next()) {
              rb1.setDoor_no_c(MyUtil.filterBad(rs.getString("house_no_temp")));
              rb1.setStreet_name_c(MyUtil.filterBad(rs.getString("street_name_temp")));
              rb1.setAddrs_state_c(MyUtil.filterBad(rs.getString("temp_state_name")));
              rb1.setAddrs_district_c(MyUtil.filterBad(rs.getString("temp_dist_name")));
              rb1.setAddrs_mandal_c(MyUtil.filterBad(rs.getString("temp_mandal_name")));
              rb1.setAddrs_village_c(MyUtil.filterBad(rs.getString("temp_village_name")));
              rb1.setAddrs_pincode_c(MyUtil.filterBad(rs.getString("temp_pincode")));
              rb1.setDoor_no_p(MyUtil.filterBad(rs.getString("house_no_perm")));
              rb1.setStreet_name_p(MyUtil.filterBad(rs.getString("street_name_perm")));
              rb1.setAddrs_state_p_txt(MyUtil.filterBad(rs.getString("perm_state_name")));
              rb1.setAddrs_district_p_txt(MyUtil.filterBad(rs.getString("perm_dist_name")));
              rb1.setAddrs_mandal_p_txt(MyUtil.filterBad(rs.getString("perm_mandal_name")));
              rb1.setAddrs_village_p_txt(MyUtil.filterBad(rs.getString("perm_village_name")));
              rb1.setAddrs_pincode_p(MyUtil.filterBad(rs.getString("perm_pincode")));
              
          }
            //=====================================
            String sql3 = "SELECT  employer_name, constn_name,work_place_location, dist_name, mandal_name, hab_name, pincode FROM worker_work_place_details a\n"
                    + " inner join district_master c on a.dist_code=c.dist_code inner join mandal_master d on a.mandal_code=d.mandal_code and a.dist_code=d.dist_code \n"
                    + "inner join habitation_master e on a.village_code=e.jb_code  where c.state_code=28 and d.state_code=28 and e.state_code=28 and temp_id=?";
            pstmt = con.prepareStatement(sql3);
            pstmt.setInt(1, Integer.parseInt(temp_id));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                rb1.setWork_employer_name(MyUtil.filterBad(rs.getString("employer_name")));
                rb1.setWork_place_orgname(MyUtil.filterBad(rs.getString("constn_name")));
                rb1.setWork_place_location(MyUtil.filterBad(rs.getString("work_place_location")));
                rb1.setWork_place_dist(MyUtil.filterBad(rs.getString("dist_name")));
                rb1.setWork_place_mandal(MyUtil.filterBad(rs.getString("mandal_name")));
                rb1.setWork_place_village(MyUtil.filterBad(rs.getString("hab_name")));
                rb1.setWork_place_pincode(MyUtil.filterBad(rs.getString("pincode")));
            }
            String sql4 = "SELECT   account_no,bankname,branchname, ifsc_code  FROM worker_bank_acct_details a\n"
                    + "inner join ifsc b on a.ifsc_code=b.ifsccode where temp_id=? ";
            pstmt = con.prepareStatement(sql4);
            pstmt.setInt(1, Integer.parseInt(temp_id));
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rb1.setAccount_num(MyUtil.filterBad(rs.getString("account_no")));
                rb1.setBank_name(MyUtil.filterBad(rs.getString("bankname")));
                rb1.setBranch_name(MyUtil.filterBad(rs.getString("branchname")));
                rb1.setIfsccode1(MyUtil.filterBad(rs.getString("ifsc_code")));
            }else{

                rb1.setAccount_num("NA");
                rb1.setBank_name("NA");
                rb1.setBranch_name("NA");
                rb1.setIfsccode1("NA");
            }
            String sql5 = "SELECT subscription_years,subscription_amount, registration_amount, total_amount,receipt_date, receipt_no, total_workers_in_receipt, serial_no_worker_receipt FROM worker_payments where temp_id=? ";
            pstmt = con.prepareStatement(sql5);
            pstmt.setInt(1, Integer.parseInt(temp_id));
            rs = pstmt.executeQuery();
            while (rs.next()) {
                rb1.setReceipt_no(MyUtil.filterBad(rs.getString("receipt_no")));
                rb1.setTotal_fee_off(rs.getInt("total_amount"));
                if(rs.getString("receipt_date")!=null){
                try{
                rb1.setReceipt_date(MyUtil.ChDate(rs.getString("receipt_date")));
                }catch(Exception e){
                	 rb1.setReceipt_date("Invalid");
                }
                }else{
                	rb1.setReceipt_date("NA");
                }
                rb1.setTot_wrks(MyUtil.filterBad(rs.getString("total_workers_in_receipt")));
                rb1.setSerial_worker_no(rs.getInt("serial_no_worker_receipt"));
                rb1.setSub_fee(MyUtil.filterBad(rs.getString("subscription_amount")));
                rb1.setReg_fee(MyUtil.filterBad(rs.getString("registration_amount")));
            }
            
            //family members details
            
            String memlistqry = "select * from worker_family_details where worker_aadhaar_no =?";
	            pstmt = con.prepareStatement(memlistqry);
	            pstmt.setString(1, aadharno);
	            rs = pstmt.executeQuery();
	            while (rs.next()) {
	                rb = new RegistrationBeen();
	                rb.setAadhar(rs.getString("worker_aadhaar_no"));
	                rb.setMemname(rs.getString("member_name"));
	                String memdob_dt = rs.getString("date_of_birth");
	                int mem_age=0;
                	if(memdob_dt == null){
                		mem_age=0;
                	}
                	else{
                		mem_age = MyUtil.getAge(memdob_dt, MyUtil.gettodaysDate());
                	}
                	rb.setMemage(String.valueOf(mem_age));
	                rb.setGenderen(beens.MyUtil.getOptionValue("gender_master", "numericcode", "gendername", rs.getString("gender_code")));
	                rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name", rs.getString("relation_code")));
	                rb.setA1(rs.getString("nominee").equals("Y")?"Yes":"No");
	                memList.add(rb);
	            }
	            String userid = ""+session.getAttribute("user_id");
	            request.setAttribute("previlist", rb1);
	            if(userid.equalsIgnoreCase("null")){
               	 return SUCCESS;
               }
               else{
               	return "LoginSuccess";
               }
        } catch (Exception e) {
        	System.out.println("Bank previce-->"+e);
        	e.printStackTrace();
            return "failure";
        }finally {
            if (pstmt != null) {

                try {
                    pstmt.close();
                } catch (Exception e) {

                }

            }if (rs != null) {

                try {
                    rs.close();
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
public String getrecept() throws Exception{
	 HttpServletRequest request = ServletActionContext.getRequest();
     HttpSession session = request.getSession();
     con = DBC.DBConnection.getConnection();
     String aadharno = "" + session.getAttribute("workeraadhaar");
     String temp_id=MyUtil.filterBad((""+session.getAttribute("temp_id")).trim());
	String appro="N";
	try{
		int tr_no_upd = beens.MyUtil.logTrace("public", "public.wr_basic_details", "Update", request);
	    String sqlupd1 = "update wr_basic_details set approval_status=?,edit_status=?,trno=?"
	            + "where worker_aadhaar_no=?";
	    pstmt = con.prepareStatement(sqlupd1);
	    pstmt.setString(1,appro);
	    pstmt.setString(2,appro);
	    pstmt.setInt(3, tr_no_upd);
	    pstmt.setString(4,aadharno);
	    pstmt.executeUpdate();

	     String userid = ""+session.getAttribute("user_id");
	     if(userid.equalsIgnoreCase("null")){
	    	 return SUCCESS;
	    }
	    else{
	    	return "LoginSuccess";
	    }
	}catch(Exception e){
	}finally {
		if(pstmt!=null){
			pstmt.close();
		}
		if(con!=null){
			DbUtils.closeQuietly(con);
		}
	}
	return "LoginSuccess";
}
  
       
    public RegistrationBeen getModel() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return rb;
    }
}
