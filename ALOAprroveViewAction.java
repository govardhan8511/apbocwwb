
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgen;

import beens.AadhaarDetailsBean;
import beens.MyUtil;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts2.ServletActionContext;

public class ALOAprroveViewAction extends ActionSupport implements ModelDriven<ALOApproveViewForm> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	   
    public ALOApproveViewForm getModel() {
        return aLOApproveViewForm;
    }
    
    public ALOAprroveViewAction() {
    }
    
    ALOApproveViewForm aLOApproveViewForm = new ALOApproveViewForm();

    public ALOApproveViewForm getaLOApproveViewForm() {
		return aLOApproveViewForm;
	}

	public void setaLOApproveViewForm(ALOApproveViewForm aLOApproveViewForm) {
		this.aLOApproveViewForm = aLOApproveViewForm;
	}

	/**
     *
     * @return @throws Exception
     */
    public String ALOApproveACC() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        Connection con = null;
        PreparedStatement ps = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        PreparedStatement ps5 = null,psmigrtd = null;
        String query = null;
        int temp_id=0;
        ResultSet rs = null , rs1 = null,rs2 = null, rs3 = null ,  rs5 = null, rsmigrtd=null;
		String user_role=MyUtil.filterBad(MyUtil.filterBad("" + session.getAttribute("user_role")));
if(!(user_role.equalsIgnoreCase("5") || user_role.equalsIgnoreCase("7")|| user_role.equalsIgnoreCase("10")|| user_role.equalsIgnoreCase("16"))){
			
			return "permission";
		}
        String aadharno=aLOApproveViewForm.getAadharno();
      if(!MyUtil.checknull_number(aadharno)){
    	 	 return "invalidata";
        }
        String type=StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(""+request.getParameter("type")));
        request.setAttribute("type", MyUtil.filterBad(type));
        session.setAttribute("aadharno", MyUtil.filterBad(aadharno));
if(type.equalsIgnoreCase("ekyc")){	
	AadhaarDetailsBean ab= MyUtil.GetAadhaarDetails(aadharno);
	aLOApproveViewForm.setAadharcardno(aadharno);
    aLOApproveViewForm.setWorker_name(ab.getAadhaar_name());
    aLOApproveViewForm.setGender(ab.getGender_name());
    aLOApproveViewForm.setPhoneno(ab.getMobile_no());
    aLOApproveViewForm.setFath_husb(ab.getRelation_name());
    aLOApproveViewForm.setDate_of_birth(ab.getDob());
    aLOApproveViewForm.setPhoto_aadhaar(ab.getPhoto());
    aLOApproveViewForm.setPermenant_addr1(ab.getDoor_no());
	aLOApproveViewForm.setPermenant_addr2(ab.getStreet_name());
	aLOApproveViewForm.setPermenant_addr_district(ab.getDist_name());
	aLOApproveViewForm.setPermenant_addr_mandal(ab.getMandal_name());
	aLOApproveViewForm.setPermenant_habcode(ab.getVillage_name());
	aLOApproveViewForm.setPermenant_addr_pincode(ab.getPincode());
	return SUCCESS;
}else if(type.equalsIgnoreCase("lot")){
        try {
            con = DBC.DBConnection.getConnection();
            
            // Worker Personal Details 
            query = "SELECT worker_aadhaar_no,  jurisdiction,gendername,a.temp_id, worker_name,  date_of_birth,  caste_name, relation_name, trade_name, no_work_days_year, mnregs_worker, migrated_worker, \n"
                    + "marital_status_name, mobile_no,worker_email,rec_valid_upto FROM wr_basic_details a inner join officer_master b on a.alo_code=b.officer_code \n"
                    + "inner join gender_master c on a.gender_code=c.numericcode inner join caste_master d on a.caste_code=d.numericcode inner join trade_master e on a.worker_trade_code=e.numericcode\n"
                    + "inner join marital_status_master f on a.marital_status=f.numericcode\n"
                    + " where a.temp_id=?";
            ps = con.prepareStatement(query);
            ps.setInt(1, Integer.parseInt(aadharno));
            rs = ps.executeQuery();
            while (rs.next()) {
            	temp_id=rs.getInt("temp_id");
            	aLOApproveViewForm.setAadharcardno(rs.getString("worker_aadhaar_no"));
                //String alocode = beens.MyUtil.getOptionValue("officer_master", "officer_code", "jurisdiction", rs.getString("alo_code"));
                aLOApproveViewForm.setAlocode(rs.getString("jurisdiction"));
                aLOApproveViewForm.setTemp_id(rs.getString("temp_id"));
                aLOApproveViewForm.setWorker_name(rs.getString("worker_name"));
                //String trade = beens.MyUtil.getOptionValue("trade_master", "numericcode", "trade_name", rs.getString("worker_trade_code"));
                aLOApproveViewForm.setNature_emp(rs.getString("trade_name"));
                //String gender = beens.MyUtil.getOptionValue("gender_master", "numericcode", "gendername", rs.getString("gender_code"));
                aLOApproveViewForm.setGender(rs.getString("gendername"));
                aLOApproveViewForm.setPhoneno(rs.getString("mobile_no"));
                //String caste = beens.MyUtil.getOptionValue("caste_master", "numericcode", "caste_name", rs.getString("caste_code"));
                aLOApproveViewForm.setCast(rs.getString("caste_name"));
                aLOApproveViewForm.setFath_husb(rs.getString("relation_name"));
                String valid_dt=rs.getString("rec_valid_upto");
                if(valid_dt!=null){
                	valid_dt=MyUtil.ChDate(valid_dt);
                }else{
                	valid_dt="NA";
                }
                aLOApproveViewForm.setValid_date(valid_dt);
                String date1 = rs.getString("date_of_birth");
                String date2 = null;
                if (date1 != null) {
                    date2 = beens.MyUtil.ChDate(date1);
                } else {
                    date2 = "";
                }
                aLOApproveViewForm.setDate_of_birth(date2);
                //String maritalst = beens.MyUtil.getOptionValue("marital_status_master", "numericcode", "marital_status_name", rs.getString("marital_status"));
                aLOApproveViewForm.setMarital_status(rs.getString("marital_status_name"));
                String migrated = rs.getString("migrated_worker");
                if(migrated.equals("Y")){
                	String migratedqry = "select slno,  state_name, dist_name, mandal_name  FROM wr_migrated_details a \n"
                			+ "inner join state_master b on a.state_code=b.state_code inner join district_master c on c.state_code=a.state_code and a.dist_code=c.dist_code inner join mandal_master d on d.state_code=a.state_code and d.dist_code=a.dist_code and a.mandal_code=d.mandal_code "
                			+ "where temp_id=?";
                	psmigrtd = con.prepareStatement(migratedqry);
                	psmigrtd.setInt(1, temp_id);
                	rsmigrtd = psmigrtd.executeQuery();
                	if(rsmigrtd.next()){
                		aLOApproveViewForm.setState(rsmigrtd.getString("state_name"));
                		aLOApproveViewForm.setMigrantdistrict(rsmigrtd.getString("dist_name"));
                		aLOApproveViewForm.setMigrantmandal(rsmigrtd.getString("mandal_name"));
                	}
                }
            }
            // Worker Permenent Address Details 
            String addrsquery = "SELECT    house_no_temp, street_name_temp,house_no_perm, street_name_perm, temp_state_name,"
            		+ " perm_state_name, temp_dist_name, perm_dist_name, temp_mandal_name, perm_mandal_name, "
      +" temp_village_name, perm_village_name, temp_habname, perm_habname,temp_pincode, perm_pincode    FROM public.view_wr_address where temp_id=? ";
            ps1 = con.prepareStatement(addrsquery);
            ps1.setInt(1, temp_id);
            rs1 = ps1.executeQuery();
            while (rs1.next()) {
            	//----------------------
             	aLOApproveViewForm.setPresent_addr1(rs1.getString("house_no_temp"));
            	aLOApproveViewForm.setPresent_addr2(rs1.getString("street_name_temp"));
            	aLOApproveViewForm.setPres_addr_district(rs1.getString("temp_dist_name"));
            	aLOApproveViewForm.setPres_addr_mandal(rs1.getString("temp_mandal_name"));
            	aLOApproveViewForm.setPres_addr_village(rs1.getString("temp_village_name"));
            	aLOApproveViewForm.setPres_addr_pincode(rs1.getString("temp_pincode"));
            	//----------------------
            	aLOApproveViewForm.setPermenant_addr1(rs1.getString("house_no_perm"));
            	aLOApproveViewForm.setPermenant_addr2(rs1.getString("street_name_perm"));
            	aLOApproveViewForm.setPermenant_addr_district(rs1.getString("perm_dist_name"));
            	aLOApproveViewForm.setPermenant_addr_mandal(rs1.getString("perm_mandal_name"));
            	aLOApproveViewForm.setPermenant_habcode(rs1.getString("perm_village_name"));
            	aLOApproveViewForm.setPermenant_addr_pincode(rs1.getString("perm_pincode"));
            }
       
            //Worker Work Place Details
            String wrkdtquery = "SELECT  employer_name, constn_name,work_place_location, dist_name, mandal_name, hab_name, pincode FROM worker_work_place_details a\n"
                    + " inner join district_master c on a.dist_code=c.dist_code inner join mandal_master d on a.mandal_code=d.mandal_code and c.dist_code = d.dist_code \n"
                    + "inner join habitation_master e on a.village_code=e.jb_code  where c.state_code=28 and d.state_code=28 and e.state_code=28  and temp_id=?";
            ps2 = con.prepareStatement(wrkdtquery);
            ps2.setInt(1, temp_id);
            rs2 = ps2.executeQuery();
            while (rs2.next()) {
            	 aLOApproveViewForm.setPres_emp_name(rs2.getString("employer_name"));
                 aLOApproveViewForm.setPres_emp_location(rs2.getString("work_place_location"));
                 //String predistrict = beens.MyUtil.getOptionValue("district_master", "dist_code", "dist_name", rs2.getString("dist_code"));
                 aLOApproveViewForm.setPres_emp_district(rs2.getString("dist_name"));
                 //String premandal = beens.MyUtil.getOptionValue("mandal_master", "mandal_code", "mandal_name", rs2.getString("mandal_code"), "dist_code", rs2.getString("dist_code"));
                 aLOApproveViewForm.setPres_emp_mandal(rs2.getString("mandal_name"));
                 //String prehabcode1 = beens.MyUtil.getOptionValue("habitation_master", "hab_code", "hab_name", rs2.getString("village_code"));
                 aLOApproveViewForm.setPres_emp_village(rs2.getString("hab_name"));
                 aLOApproveViewForm.setPres_emp_pincode(rs2.getString("pincode"));
                 
            }
            //Worker Bank Account Details
            String bankdtquery = "SELECT   account_no,bankname,branchname, ifsc_code  FROM worker_bank_acct_details a\n"
                    + "inner join ifsc b on a.ifsc_code=b.ifsccode where temp_id=? ";
            ps3 = con.prepareStatement(bankdtquery);
            ps3.setInt(1, temp_id);
            rs3 = ps3.executeQuery();
            if (rs3.next()) {
            	aLOApproveViewForm.setBank_accno(rs3.getString("account_no"));
                //String bankName = beens.MyUtil.getOptionValue("master_banks", "bank_id", "bank_name", rs2.getString("bank_name"));
                aLOApproveViewForm.setBank_name(rs3.getString("bankname"));
                //String branchName = beens.MyUtil.getOptionValue("master_bank_branches", "ifsc_code", "branch_location", rs2.getString("branch_name"), "bank_id", rs2.getString("bank_name"));
                aLOApproveViewForm.setBranch_name(rs3.getString("branchname"));
            }else{
            	aLOApproveViewForm.setBank_accno("NA");
                //String bankName = beens.MyUtil.getOptionValue("master_banks", "bank_id", "bank_name", rs2.getString("bank_name"));
                aLOApproveViewForm.setBank_name("NA");
                //String branchName = beens.MyUtil.getOptionValue("master_bank_branches", "ifsc_code", "branch_location", rs2.getString("branch_name"), "bank_id", rs2.getString("bank_name"));
                aLOApproveViewForm.setBranch_name("NA");
            }
            //Worker Fee  Details 
            String feequery = "SELECT * FROM worker_payments where temp_id=? ";
            ps5 = con.prepareStatement(feequery);
            ps5.setInt(1, temp_id);
            rs5 = ps5.executeQuery();
            while (rs5.next()) {
            	String feetype = rs5.getString("payment_type").trim();
            	aLOApproveViewForm.setFee_type(feetype);
            	if(feetype.equals("OFF")){
            		aLOApproveViewForm.setFee_rcpt(rs5.getString("receipt_no"));
            		aLOApproveViewForm.setFee_date(beens.MyUtil.ChDate(rs5.getString("receipt_date")));
            	}
            }
        } catch (Exception e) {
        	System.out.println("Exp---->"+e);
        	e.printStackTrace();
            return "dberror";
        } finally {
            if (con != null) {
                try {
                	DbUtils.closeQuietly(con);
                } catch (Exception e) {
                }
            }
        }
        return SUCCESS;      
}else{
	return "invalidaction";
}
    }
}

