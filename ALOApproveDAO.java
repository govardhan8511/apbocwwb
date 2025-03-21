/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cardgen;

import java.sql.*;
import java.util.*;

import org.apache.commons.dbutils.DbUtils;

public class ALOApproveDAO {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    String sql = null;

    public List<ALOApproveFormBean> getList(String userid) throws SQLException {
        List<ALOApproveFormBean> list = new ArrayList<ALOApproveFormBean>();
        try {
            con = DBC.DBConnection.getConnection();
            sql = "select a.worker_id,a.worker_reg_no,a.worker_name,a.relation_name,a.date_of_birth,a.gender_code,b.gendername,a.approval_status,a.printdate "+
    		",DATE_PART('day', current_date - printdate::timestamp) days_count from wr_basic_details a,gender_master b where worker_aadhaar_no is not null and a.gender_code=b.numericcode and approval_status in ('Y','A','T') and (DATE_PART('day', current_date - printdate::timestamp) <7 or printdate is null) and created_by=? order by worker_id,alo_code,a.approval_status";
            ps = con.prepareStatement(sql);
            ps.setString(1, userid);
            System.out.println("ps is--->"+ps);
            rs = ps.executeQuery();
            int daysdiff = 0;
            while (rs.next()) {
            	daysdiff = rs.getInt("days_count");
                String printsts = rs.getString("approval_status");
            	if(printsts.equals("Y") || printsts.equals("A") || (daysdiff<=6 && printsts.equals("T"))){
                ALOApproveFormBean cardGenEntryBean = new ALOApproveFormBean();
                cardGenEntryBean.setWorker_id(rs.getString("worker_id"));
                cardGenEntryBean.setRegno(rs.getString("worker_reg_no"));
                cardGenEntryBean.setWorker_name(rs.getString("worker_name"));
                cardGenEntryBean.setFather_name(rs.getString("relation_name"));
                String dtb = rs.getString("date_of_birth");
                if(dtb==null){
                	dtb= "NA";
                }
                else{
                	dtb = beens.MyUtil.ChDate(rs.getString("date_of_birth"));
                }
                cardGenEntryBean.setAge(dtb);
                cardGenEntryBean.setGender(rs.getString("gendername"));
                if(printsts.equals("Y")){
                	cardGenEntryBean.setPrintstatus("1st Time Print");
                }
//                else if(printsts.equals("A")){
//                	cardGenEntryBean.setPrintstatus("2nd Time Print");
//                }
                else if(printsts.equals("T") || printsts.equals("A")){
                	if(daysdiff==6){
                    	cardGenEntryBean.setPrintstatus("Available 1 Day");
                	}
                	if(daysdiff==5){
                    	cardGenEntryBean.setPrintstatus("Available 2 Days");
                	}
                	if(daysdiff==4){
                    	cardGenEntryBean.setPrintstatus("Available 3 Days");
                	}
                	if(daysdiff==3){
                    	cardGenEntryBean.setPrintstatus("Available 4 Days");
                	}
                	if(daysdiff==2){
                    	cardGenEntryBean.setPrintstatus("Available 5 Days");
                	}
                	if(daysdiff==1){
                    	cardGenEntryBean.setPrintstatus("Available 6 Days");
                	}
                	if(daysdiff==0){
                    	cardGenEntryBean.setPrintstatus("Available 7 Days");
                	}
                }
                list.add(cardGenEntryBean);
            	}
            }
        } catch (Exception e) {
             
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                     
                }
            }
            if (ps != null) {
                try {
                    ps.close();
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

        return list;
    }
    /*get list from main table of card generation*/
    public List<ALOApproveFormBean> getList(String alocode, String userid) throws SQLException {
        List<ALOApproveFormBean> list = new ArrayList<ALOApproveFormBean>();
        try {
            con = DBC.DBConnection.getConnection();
            sql = "select a.worker_id,a.worker_reg_no,a.worker_name,a.relation_name,a.date_of_birth,a.gender_code,b.gendername "+
    		" from wr_basic_details a,gender_master b where worker_aadhaar_no is not null and a.gender_code=b.numericcode and approval_status in('Y','A')  and alo_code=? order by worker_id,alo_code";
            ps = con.prepareStatement(sql);
            ps.setString(1, alocode);
            System.out.println("ps iss->"+ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                ALOApproveFormBean cardGenEntryBean = new ALOApproveFormBean();
                cardGenEntryBean.setWorker_id(rs.getString("worker_id"));
                cardGenEntryBean.setRegno(rs.getString("worker_reg_no"));
                cardGenEntryBean.setWorker_name(rs.getString("worker_name"));
                cardGenEntryBean.setFather_name(rs.getString("relation_name"));
                String dtb = rs.getString("date_of_birth");
                if(dtb==null){
                	dtb= "N/A";
                }
                else{
                	dtb = beens.MyUtil.ChDate(rs.getString("date_of_birth"));
                }
                cardGenEntryBean.setAge(dtb);
                cardGenEntryBean.setGender(rs.getString("gendername"));
                list.add(cardGenEntryBean);
            }
        } catch (Exception e) {
             
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                     
                }
            }
            if (ps != null) {
                try {
                    ps.close();
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

        return list;
    }
}
