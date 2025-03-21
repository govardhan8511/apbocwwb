
package Registration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

import DBC.Validations_new;
import TrainingMaster.seedingbean;
import WorkerRegistration_NewAndEdit.Age;
import beens.MyUtil;
import beens.RegistrationBeen;

public class FamilyMemberAdd extends ActionSupport implements ModelDriven<RegistrationBeen> {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private static String SUCCESS = "success";

	RegistrationBeen rb = new RegistrationBeen();
	List<RegistrationBeen> memList = new ArrayList<RegistrationBeen>();
	List<RegistrationBeen> nominelist = new ArrayList<RegistrationBeen>();

	public List<RegistrationBeen> getNominelist() {
		return nominelist;
	}

	public void setNominelist(List<RegistrationBeen> nominelist) {
		this.nominelist = nominelist;
	}

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
String Msg=null;
	public String addFamilyMember() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String aadhar = "" + session.getAttribute("workeraadhaar");
		String alocode = "" + session.getAttribute("workeralocode");
		String temp_id=(""+session.getAttribute("temp_id")).trim();
		String userid =MyUtil.filterBad(""+session.getAttribute("user_id"));
		int tr_no = beens.MyUtil.logTrace(userid, "worker_family_details", "Insert", request);
		String memdobdt = null;
		String actiontype = null;
		actiontype = rb.getAction_type();
		if (rb.getMemdob() != null) {
			memdobdt = beens.MyUtil.ChDate1(rb.getMemdob());
		}
		if (actiontype == null) {
			actiontype = "";
		}
		try {
			con = DBC.DBConnection.getConnection();

			
			
			if (rb.getMemname() != null) {

				if (actiontype.equalsIgnoreCase("ADD")) {
					if (rb.getA1().equalsIgnoreCase("Y")) {
						int checkworker = MyUtil.DuplicateCheck("public.wr_basic_details",
								"worker_aadhaar_no='" + rb.getMemaadhar() + "'", "");
//						if (checkworker > 0) {
//							request.setAttribute("checknomedup", "Worker as head of Family");
//							return SUCCESS;
//						} else {
							int checkfmy = MyUtil.DuplicateCheck("public.worker_family_details",
									"member_aadhaar_no='" + rb.getMemaadhar() + "'", "");
							if (checkfmy > 0) {
								request.setAttribute("checknomedup", "family member exists!");
								return SUCCESS;
							}
						}
//					}
					String addmem = "INSERT INTO worker_family_details(worker_aadhaar_no, member_aadhaar_no, relation_code, "
							+ "member_name, nominee,rec_status, trno,alo_code,member_occupation,gender_code,date_of_birth,temp_id)"
							+ " VALUES (?, ?, ?, ?,?, ?,?, ?,?,?,?,?);";
					pstm = con.prepareStatement(addmem);
					pstm.setString(1, aadhar);
					pstm.setString(2, rb.getMemaadhar());
					pstm.setInt(3, Integer.parseInt(rb.getMemrelation()));
					pstm.setString(4, WordUtils.capitalizeFully(rb.getMemname()));
					pstm.setString(5, rb.getA1());
					pstm.setString(6, "A");
					pstm.setInt(7, tr_no);
					pstm.setString(8, alocode);
					pstm.setString(9, rb.getMemoccupation());
					pstm.setInt(10, Integer.parseInt(rb.getMemgender()));
					pstm.setString(11, memdobdt);
					pstm.setInt(12, Integer.parseInt(temp_id));
					pstm.executeUpdate();
					if (pstm != null)
						pstm.close();
					if (rb.getA1().equalsIgnoreCase("Y")) {
						String nom = "INSERT INTO worker_nominee_details(worker_aadhaar_no,"
								+ "nominee_aadhaar_no, nominee_relaton,nominee_name,nominee_occupation, nominee_gender, "
								+ " trno, nominee_type,nominee_dob,temp_id)   VALUES (?, ?, ?, ?, ?,?,?, ?, ?,?);";
						pstm = con.prepareStatement(nom);
						pstm.setString(1, aadhar);
						pstm.setString(2, rb.getMemaadhar());
						pstm.setInt(3, Integer.parseInt(rb.getMemrelation()));
						pstm.setString(4, WordUtils.capitalizeFully(rb.getMemname()));
						pstm.setString(5, rb.getMemoccupation());
						pstm.setInt(6, Integer.parseInt(rb.getMemgender()));
						pstm.setInt(7, tr_no);
						pstm.setString(8, "F");
						pstm.setDate(9, java.sql.Date.valueOf(memdobdt));
						pstm.setInt(10, Integer.parseInt(temp_id));
						pstm.executeUpdate();
					}
				} else if (actiontype.equalsIgnoreCase("ADD Nominee")) {
					try {
						String nom = "INSERT INTO worker_nominee_details(worker_aadhaar_no,"
								+ "nominee_aadhaar_no, nominee_relaton,nominee_name,nominee_occupation, nominee_gender,"
								+ " trno, nominee_type,nominee_dob)   VALUES (?, ?, ?, ?, ?,?,?, ?, ?);";
						pstm = con.prepareStatement(nom);
						pstm.setString(1, aadhar);
						pstm.setString(2, rb.getMemaadhar());
						pstm.setInt(3, Integer.parseInt(rb.getMemrelation()));
						pstm.setString(4, WordUtils.capitalizeFully(rb.getMemname()));
						pstm.setString(5, rb.getMemoccupation());
						pstm.setInt(6, Integer.parseInt(rb.getMemgender()));
						pstm.setInt(7, tr_no);
						pstm.setString(8, "F");
						pstm.setDate(9, java.sql.Date.valueOf(memdobdt));
						pstm.executeUpdate();
					} catch (Exception e) {
						System.out.println("Exception in other nominee insert-->" + e);
					}

				}

			}

			String memlistqry = "select worker_aadhaar_no, worker_reg_no, alo_code, member_aadhaar_no,relation_code, member_name, gender_code, date_of_birth, relation_age, member_occupation,nominee,rec_status, trno, relation_agenew, sno  from worker_family_details where worker_aadhaar_no = ?";
			pstm = con.prepareStatement(memlistqry);
			pstm.setString(1, aadhar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				rb = new RegistrationBeen();
				rb.setSno(rs.getInt("sno"));
				rb.setAadhar(rs.getString("member_aadhaar_no"));
				rb.setMemname(rs.getString("member_name"));
				rb.setMemoccupation(rs.getString("member_occupation"));
				if (rs.getString("gender_code") == null) {
					rb.setMemgender("NA");
				} else {
					rb.setMemgender(rs.getString("gender_code").equals("1") ? "Male" : "Female");
				}
				rb.setGenderen(rs.getString("gender_code"));
				String memdob_dt = rs.getString("date_of_birth");
				int mem_age = 0;
				if (memdob_dt == null) {
					mem_age = 0;
				} else {
					// memdob_dt = beens.MyUtil.ChDate(memdob_dt);
					try {
						mem_age = MyUtil.getAge(memdob_dt, MyUtil.gettodaysDate());
					} catch (Exception e) {

					}

				}
				try {
					memdob_dt=MyUtil.ChDate(memdob_dt);
				}catch (Exception e) {
					// TODO: handle exception
					memdob_dt="";
				}
				rb.setMemdob(memdob_dt);
				rb.setMemage(String.valueOf(mem_age));
				rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
						rs.getString("relation_code")));
				if (rs.getString("nominee") == null) {
					rb.setA1("NA");
				} else {
					rb.setA1(rs.getString("nominee").equals("Y") ? "YES" : "NO");
				}

				rb.setMemrelation(rs.getString("relation_code"));
				rb.setMempincode(rs.getString("nominee"));
				memList.add(rb);
			}
			if (rs != null) {
				rs.close();
			}
			// nominee List details
			String nomineesql = "select * from worker_nominee_details where worker_aadhaar_no = ?";
			pstm = con.prepareStatement(nomineesql);
			pstm.setString(1, aadhar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				rb = new RegistrationBeen();
				rb.setSno(rs.getInt("slno"));
				rb.setAadhar(rs.getString("nominee_aadhaar_no"));
				rb.setMemname(rs.getString("nominee_name"));
				rb.setMemoccupation(rs.getString("nominee_occupation"));
				rb.setMemgender(rs.getString("nominee_gender").equals("1") ? "Male" : "Female");
				rb.setGenderen(rs.getString("nominee_gender"));
				String memdob_dt = rs.getString("nominee_dob");
				int mem_age = 0;
				if (memdob_dt == null) {
					mem_age = 0;
				} else {
					mem_age = MyUtil.getAge(memdob_dt, MyUtil.gettodaysDate());
				}
				rb.setMemdob(memdob_dt);
				rb.setMemage(String.valueOf(mem_age));
				rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
						rs.getString("nominee_relaton")));
				rb.setMemrelation(rs.getString("nominee_relaton"));
				nominelist.add(rb);
			}
			return SUCCESS;
		} catch (Exception e) {
			System.out.println("Exception in famly mem-->"+e);
			e.printStackTrace();
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	public String updateFamilyMember() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int tr_no = beens.MyUtil.logTrace("public", "worker_family_details", "Insert", request);
		String aadhar = "" + session.getAttribute("workeraadhaar");
		String alocode = "" + session.getAttribute("workeralocode");
		String memdobdt = null;
		if (rb.getMemdob() != null) {
			memdobdt = beens.MyUtil.ChDate1(rb.getMemdob());
		}
		try {
			con = DBC.DBConnection.getConnection();
			if (rb.getMemname() != null) {
				String updatemem = "UPDATE worker_family_details SET member_aadhaar_no=?, relation_code=?, member_name=?,"
						+ " gender_code=?, member_occupation=?, nominee=?, trno=?, date_of_birth=?  WHERE sno = ?";
				pstm = con.prepareStatement(updatemem);
				pstm.setString(1, rb.getMemaadhar());
				pstm.setInt(2, Integer.parseInt(rb.getMemrelation()));
				pstm.setString(3, WordUtils.capitalizeFully(rb.getMemname()));
				pstm.setInt(4, Integer.parseInt(rb.getMemgender()));
				pstm.setString(5, rb.getMemoccupation());
				pstm.setString(6, rb.getA1());
				pstm.setInt(7, tr_no);
				pstm.setString(8, memdobdt);
				pstm.setInt(9, rb.getSno());
				pstm.executeUpdate();
				if (pstm != null)
					pstm.close();
			}
			String memlistqry = "select * from worker_family_details where worker_aadhaar_no = ? ";
			pstm = con.prepareStatement(memlistqry);
			pstm.setString(1, aadhar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				rb = new RegistrationBeen();
				rb.setSno(rs.getInt("sno"));
				rb.setAadhar(rs.getString("member_aadhaar_no"));
				rb.setMemname(rs.getString("member_name"));
				rb.setMemoccupation(rs.getString("member_occupation"));
				rb.setMemgender(rs.getString("gender_code").equals("1") ? "Male" : "Female");
				rb.setGenderen(rs.getString("gender_code"));
				String memdob_dt = rs.getString("date_of_birth");
				int mem_age = 0;
				if (memdob_dt == null) {
					mem_age = 0;
				} else {
					mem_age = MyUtil.getAge(memdob_dt, MyUtil.gettodaysDate());
				}
				rb.setMemdob(memdob_dt);
				rb.setMemage(String.valueOf(mem_age));
				rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
						rs.getString("relation_code")));
				rb.setA1(rs.getString("nominee").equals("Y") ? "YES" : "NO");
				rb.setMemrelation(rs.getString("relation_code"));
				rb.setMempincode(rs.getString("nominee"));
				memList.add(rb);
			}
			// nominee List details
			String nomineesql = "select * from worker_nominee_details where worker_aadhaar_no = ? ";
			pstm = con.prepareStatement(nomineesql);
			pstm.setString(1, aadhar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				rb = new RegistrationBeen();
				rb.setSno(rs.getInt("slno"));
				rb.setAadhar(rs.getString("nominee_aadhaar_no"));
				rb.setMemname(rs.getString("nominee_name"));
				rb.setMemoccupation(rs.getString("nominee_occupation"));
				rb.setMemgender(rs.getString("nominee_gender").equals("1") ? "Male" : "Female");
				rb.setGenderen(rs.getString("nominee_gender"));
				String memdob_dt = rs.getString("nominee_dob");
				int mem_age = 0;
				if (memdob_dt == null) {
					mem_age = 0;
				} else {
					mem_age = MyUtil.getAge(memdob_dt, MyUtil.gettodaysDate());
				}
				rb.setMemdob(memdob_dt);
				rb.setMemage(String.valueOf(mem_age));
				rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
						rs.getString("nominee_relaton")));
				rb.setMemrelation(rs.getString("nominee_relaton"));
				nominelist.add(rb);
			}
			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	public String deleteFamilyMember() throws Exception {

		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int tr_no = beens.MyUtil.logTrace("public", "worker_family_details", "Insert", request);
		String aadhar = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("workeraadhaar")));
		String alocode = "" + session.getAttribute("workeralocode");
		try {
			con = DBC.DBConnection.getConnection();
			String delfamlmem = "delete from worker_family_details where sno = ?";
			pstm = con.prepareStatement(delfamlmem);
			pstm.setInt(1, rb.getSno());
			pstm.executeUpdate();
			pstm.close();
			String memlistqry = "select * from worker_family_details where worker_aadhaar_no = ?";
			pstm = con.prepareStatement(memlistqry);
			pstm.setString(1, aadhar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				rb = new RegistrationBeen();
				rb.setSno(rs.getInt("sno"));
				rb.setAadhar(rs.getString("member_aadhaar_no"));
				rb.setMemname(rs.getString("member_name"));
				rb.setMemoccupation(rs.getString("member_occupation"));
				rb.setMemgender(rs.getString("gender_code").equals("1") ? "Male" : "Female");
				rb.setGenderen(rs.getString("gender_code"));
				String memdob_dt = rs.getString("date_of_birth");
				int mem_age = 0;
				if (memdob_dt == null) {
					mem_age = 0;
				} else {
					memdob_dt = beens.MyUtil.ChDate(memdob_dt);
					if (memdob_dt.length() > 8) {
						String parts[] = memdob_dt.split("-");
						String day = parts[0];
						String month = parts[1];
						String year = parts[2];
						mem_age = Age.getAge(year, month, day);
					}
				}
				rb.setMemdob(memdob_dt);
				rb.setMemage(String.valueOf(mem_age));
				rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
						rs.getString("relation_code")));
				rb.setA1(rs.getString("nominee").equals("Y") ? "YES" : "NO");
				rb.setMemrelation(rs.getString("relation_code"));
				rb.setMempincode(rs.getString("nominee"));
				memList.add(rb);
			}
			rs.close();
			// nominee List details
			String nomineesql = "select * from worker_nominee_details where worker_aadhaar_no =? ";
			pstm = con.prepareStatement(nomineesql);
			pstm.setString(1, aadhar);
			rs = pstm.executeQuery();
			while (rs.next()) {
				rb = new RegistrationBeen();
				rb.setSno(rs.getInt("slno"));
				rb.setAadhar(rs.getString("nominee_aadhaar_no"));
				rb.setMemname(rs.getString("nominee_name"));
				rb.setMemoccupation(rs.getString("nominee_occupation"));
				rb.setMemgender(rs.getString("nominee_gender").equals("1") ? "Male" : "Female");
				rb.setGenderen(rs.getString("nominee_gender"));
				String memdob_dt = rs.getString("nominee_dob");
				int mem_age = 0;
				if (memdob_dt == null) {
					mem_age = 0;
				} else {
					memdob_dt = beens.MyUtil.ChDate(memdob_dt);
					if (memdob_dt.length() > 8) {
						String parts[] = memdob_dt.split("-");
						String day = parts[0];
						String month = parts[1];
						String year = parts[2];
						mem_age = Age.getAge(year, month, day);
					}
				}
				rb.setMemdob(memdob_dt);
				rb.setMemage(String.valueOf(mem_age));
				rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode", "relation_name",
						rs.getString("nominee_relaton")));
				rb.setMemrelation(rs.getString("nominee_relaton"));
				nominelist.add(rb);
			}
			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	public String getfmy_reg_details() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		// int tr_no = beens.MyUtil.logTrace("public", "worker_family_details",
		// "Insert", request);
		String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("alocode")));
		String userrole = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(""+ session.getAttribute("user_role")));
		String  memgender = null, memnom = null;
		String regno = null;
		String wr_age = null;
		String checkregno = null;
		String accessworker = null;
		int age=0;
		if(userrole.equalsIgnoreCase("")||userrole.equalsIgnoreCase("null")){
			return "permission";
		}
		regno = rb.getReg_no();
		if(regno.length()>0){
			Msg=Validations_new.Validate(regno, 25, 25, false);
			if(Msg!=null){
				request.setAttribute("rststatus", "invalid");
				return SUCCESS;
			}
		}else{
			request.setAttribute("rststatus", "invalid");
			return SUCCESS;
		}
		regno = beens.MyUtil.filterBad(regno);
		try {
			if (!regno.equalsIgnoreCase("invald")) {
				con = DBC.DBConnection.getConnection();
				String chekreg = "select worker_reg_no,worker_aadhaar_no,worker_name,date_of_birth,uid_seed_dt,EXTRACT(YEAR FROM age(cast(date_of_birth as date))) as age  from wr_basic_details where worker_reg_no=?";
				pstm = con.prepareStatement(chekreg);
				pstm.setString(1, regno);
				rs = pstm.executeQuery();
				while (rs.next()) {
					checkregno = MyUtil.filterBad(rs.getString("worker_reg_no"));
					wr_age = rs.getString("date_of_birth");
					age=rs.getInt("age");
					System.out.println("age is--->"+age);
				}
				if (wr_age != null) {
					wr_age = MyUtil.ChDate(wr_age);
				}
				pstm.close();
				rs.close();
				if (checkregno != null) {
					accessworker = MyUtil.CheckAloRegAccess(checkregno, alocode);
					if (!userrole.equalsIgnoreCase("5")) {
						if (accessworker == null) {
							request.setAttribute("rststatus", "not in this circle");
							return SUCCESS;
						}
					}

					String checkuidseed = MyUtil.RegUidCheck(checkregno);
					if (checkuidseed == null) {
						String regsql = "select worker_name,relation_name,date_of_birth,worker_reg_no  from wr_basic_details where worker_reg_no=?";
						pstm = con.prepareStatement(regsql);
						pstm.setString(1, checkregno);
						rs = pstm.executeQuery();
						while (rs.next()) {
							rb = new RegistrationBeen();
							rb.setWorker_name(rs.getString("worker_name"));
							rb.setRelation_name(rs.getString("relation_name"));
							rb.setDob(rs.getString("date_of_birth"));
							rb.setReg_no(rs.getString("worker_reg_no"));
							regno = rs.getString("worker_reg_no");
							memList.add(rb);
						}
						request.setAttribute("rststatus", "uid not seeded");
					} else {
						if(age>60){
							request.setAttribute("rststatus", "age above 60");
							return SUCCESS;
							
						}
						else{
						String memlistqry = "select * from worker_family_details where worker_reg_no=?";
						pstm = con.prepareStatement(memlistqry);
						pstm.setString(1, regno);
						rs = pstm.executeQuery();
						while (rs.next()) {
							rb = new RegistrationBeen();
							rb.setSno(rs.getInt("sno"));
							rb.setMemaadhar(rs.getString("member_aadhaar_no"));
							rb.setMemname(rs.getString("member_name"));
							rb.setMemoccupation(rs.getString("member_occupation"));
							memgender = rs.getString("gender_code");
							if (memgender == null) {
								memgender = "NA";
								rb.setMemgender(memgender);
							} else {
								rb.setMemgendercode(memgender);
								rb.setMemgender(rs.getString("gender_code").equals("1") ? "Male" : "Female");
							}
							rb.setGenderen(rs.getString("gender_code"));
							String memdob_dt = rs.getString("date_of_birth");
							int mem_age = 0;
							if (memdob_dt == null) {
								mem_age = 0;
							} else {
								memdob_dt = beens.MyUtil.ChDate(memdob_dt);
								if (memdob_dt.length() > 8) {
									String parts[] = memdob_dt.split("-");
									String day = parts[0];
									String month = parts[1];
									String year = parts[2];
									mem_age = Age.getAge(year, month, day);
								}
							}
							rb.setMemdob(memdob_dt);
							rb.setMemage(String.valueOf(mem_age));
							rb.setRelation_name(beens.MyUtil.getOptionValue("relation_master", "numericcode",
									"relation_name", rs.getString("relation_code")));
							memnom = rs.getString("nominee");
							if (memnom == null) {
								memnom = "NA";
								rb.setA1(memnom);
							} else {
								rb.setA1(rs.getString("nominee").equals("Y") ? "YES" : "NO");
							}

							rb.setMemrelation(rs.getString("relation_code"));
							rb.setMempincode(rs.getString("nominee"));
							memList.add(rb);
						}
						request.setAttribute("rststatus", "valid");
					}
				}} else {
					request.setAttribute("rststatus", "invalid");
				}
			} else {
				request.setAttribute("rststatus", "invalid");
			}
			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	public String getbank_reg_details() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		// int tr_no = beens.MyUtil.logTrace("public", "worker_family_details",
		// "Insert", request);
		String alocode = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad("" + session.getAttribute("alocode")));
		String userrole = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(""+ session.getAttribute("user_role")));
		String  memgender = null, memnom = null;
		String regno = null;
		String wr_age = null;
		String checkregno = null;
		String accessworker = null;
		if(userrole.equalsIgnoreCase("")||userrole.equalsIgnoreCase("null")){
			return "permission";
		}
		regno = rb.getReg_no();
		if(regno.length()>0){
			Msg=Validations_new.Validate(regno, 25, 25, false);
			if(Msg!=null){
				request.setAttribute("rststatus", "invalid");
				return SUCCESS;
			}
		}else{
			request.setAttribute("rststatus", "invalid");
			return SUCCESS;
		}
		regno = beens.MyUtil.filterBad(regno);
		try {
			if (!regno.equalsIgnoreCase("invald")) {
				con = DBC.DBConnection.getConnection();
				String chekreg = "select worker_reg_no,bank_name,branch_name,ifsc_code,account_no  from worker_bank_acct_details where worker_reg_no=?";
				pstm = con.prepareStatement(chekreg);
				pstm.setString(1, regno);
				rs=pstm.executeQuery();
				while(rs.next()){
					rb=new RegistrationBeen();
					rb.setAccount_num(rs.getString("account_no"));
					rb.setBank_name(rs.getString("bank_name"));
					rb.setIfsccode1(rs.getString("ifsc_code").trim());
					rb.setBranch_name(MyUtil.GetFieldName("public.ifsc", "branchname", "ifsccode='"+rs.getString("ifsc_code").trim()+"'"));
					memList.add(rb);
				}
				
			}
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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
		return SUCCESS;
	}

	public String fmy_add_alo() throws Exception {
		int ins = 0;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int tr_no = beens.MyUtil.logTrace("public", "worker_family_details", "Insert", request);
		String aadhar = "" + session.getAttribute("workeraadhaar");
		String alocode = "" + session.getAttribute("workeralocode");
		String memdobdt = null, memgender = null, memnom = null, falocde = null;
		String regno = null;
		String checkregno = null;
		String worker_aadhar = null;
		int fmycheck = 0;
		regno = rb.getReg_no();
		if (regno == null) {
			regno = "invald";
		} else {
			regno = MyUtil.filterBad(regno);
		}
		if (rb.getMemdob() != null) {
			memdobdt = beens.MyUtil.ChDate1(rb.getMemdob());
		}

		try {
			con = DBC.DBConnection.getConnection();
			String chekreg = "select worker_reg_no,worker_aadhaar_no,worker_name,alo_code  from wr_basic_details where worker_reg_no=?";
			pstm = con.prepareStatement(chekreg);
			pstm.setString(1, regno);
			rs = pstm.executeQuery();
			while (rs.next()) {
				checkregno = rs.getString("worker_reg_no");
				falocde = rs.getString("alo_code");
				worker_aadhar = StringEscapeUtils.escapeJavaScript(MyUtil.filterBad(rs.getString("worker_aadhaar_no")));
			}
			pstm.close();
			rs.close();
			if (checkregno != null) {
				String checkfmy = "select count(*) fmycount  from worker_family_details where worker_aadhaar_no=? and member_aadhaar_no=?";
				pstm = con.prepareStatement(checkfmy);
				pstm.setString(1, worker_aadhar);
				pstm.setString(2, rb.getMemaadhar());
				rs = pstm.executeQuery();
				while (rs.next()) {
					fmycheck = rs.getInt("fmycount");
				}
				if (fmycheck > 0) {
					request.setAttribute("i", "Already Exists");
				} else {
					String worker_aadhaar_no = MyUtil.GetFieldName("wr_basic_details", "worker_aadhaar_no",
							"worker_reg_no='" + checkregno + "'");
					String addmem = "INSERT INTO worker_family_details(worker_reg_no, member_aadhaar_no, relation_code, "
							+ "member_name,date_of_birth, nominee,rec_status, trno,alo_code,member_occupation,gender_code,worker_aadhaar_no)"
							+ " VALUES (?, ?, ?, ?,'" + memdobdt + "',?, ?,?, ?,?,?,?);";
					pstm = con.prepareStatement(addmem);
					pstm.setString(1, checkregno);
					pstm.setString(2, rb.getMemaadhar());
					pstm.setInt(3, Integer.parseInt(rb.getMemrelation()));
					pstm.setString(4, rb.getMemname());
					pstm.setString(5, "N");
					pstm.setString(6, "A");
					pstm.setInt(7, tr_no);
					pstm.setString(8, falocde);
					pstm.setString(9, rb.getMemoccupation());
					pstm.setInt(10, Integer.parseInt(rb.getMemgender()));
					pstm.setString(11, worker_aadhaar_no);
					ins = pstm.executeUpdate();
					if (ins > 0) {
						request.setAttribute("i", "ins success");
					} else {
						request.setAttribute("i", "ins failure");
					}
				}

				if (pstm != null)
					pstm.close();
			}
			request.setAttribute("checkservice", "fmyinsert");

			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	public String fmy_delete() throws Exception {
		int upd = 0;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int tr_no = beens.MyUtil.logTrace("public", "worker_family_details", "Insert", request);
		String aadhar = "" + session.getAttribute("workeraadhaar");
		String alocode = "" + session.getAttribute("workeralocode");
		String memdobdt = null, memgender = null, memnom = null, falocde = null;
		String regno = null;
		String checkregno = null;
		regno = rb.getReg_no();
		if (regno == null) {
			regno = "invald";
		} else {
			regno = MyUtil.filterBad(regno);
		}
		if (rb.getMemdob() != null) {
			memdobdt = beens.MyUtil.ChDate1(rb.getMemdob());
		}

		try {
			con = DBC.DBConnection.getConnection();
			String chekreg = "select worker_reg_no,worker_aadhaar_no,worker_name,alo_code  from wr_basic_details where worker_reg_no=?";
			pstm = con.prepareStatement(chekreg);
			pstm.setString(1, regno);
			rs = pstm.executeQuery();
			while (rs.next()) {
				checkregno = rs.getString("worker_reg_no");
				falocde = rs.getString("alo_code");
			}
			pstm.close();
			rs.close();
			if (checkregno != null) {
				String delfamlmem = "delete from worker_family_details where sno = ?";
				pstm = con.prepareStatement(delfamlmem);
				pstm.setInt(1, rb.getSno());
				upd = pstm.executeUpdate();
				if (upd == 0) {
					request.setAttribute("i", "del failure");
				} else {
					request.setAttribute("i", "del success");
				}
			}
			request.setAttribute("checkservice", "fmydel");
			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	public String fmy_upd() throws Exception {
		int upd = 0;
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession session = request.getSession();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int tr_no = beens.MyUtil.logTrace("public", "worker_family_details", "Update", request);
		String aadhar = "" + session.getAttribute("workeraadhaar");
		String alocode = "" + session.getAttribute("workeralocode");
		String memdobdt = null, memgender = null, memnom = null, falocde = null;
		String regno = null;
		String checkregno = null, worker_aadhar = null;
		int fmycheck = 0;
		regno = rb.getReg_no();
		if (regno == null) {
			regno = "invald";
		} else {
			regno = beens.MyUtil.filterBad(regno);
		}
		if (rb.getMemdob() != null) {
			memdobdt = beens.MyUtil.ChDate1(rb.getMemdob());
		}

		try {
			con = DBC.DBConnection.getConnection();
			String chekreg = "select worker_reg_no,worker_aadhaar_no,worker_name,alo_code  from wr_basic_details where worker_reg_no=?";
			pstm = con.prepareStatement(chekreg);
			pstm.setString(1, regno);
			rs = pstm.executeQuery();
			while (rs.next()) {
				checkregno = rs.getString("worker_reg_no");
				falocde = rs.getString("alo_code");
				worker_aadhar = rs.getString("worker_aadhaar_no");
			}
			pstm.close();
			rs.close();
			if (checkregno != null) {
				String checkfmy = "select count(*) fmycount  from worker_family_details where worker_aadhaar_no=? and member_aadhaar_no=?";
				pstm = con.prepareStatement(checkfmy);
				pstm.setString(1, worker_aadhar);
				pstm.setString(2, rb.getMemaadhar());
				rs = pstm.executeQuery();
				while (rs.next()) {
					fmycheck = rs.getInt("fmycount");
				}
				int oldsno = MyUtil.CheckValidFmyMdfy(rb.getMemaadhar());
				if (oldsno == 0) {
					String addmem = "UPDATE worker_family_details   SET   member_aadhaar_no=?, relation_code=?, member_name=?, "
							+ "gender_code=?,member_occupation=?, trno=?,date_of_birth=? where sno=?";
					pstm = con.prepareStatement(addmem);
					pstm.setString(1, rb.getMemaadhar());
					pstm.setInt(2, Integer.parseInt(rb.getMemrelation()));
					pstm.setString(3, rb.getMemname());
					pstm.setInt(4, Integer.parseInt(rb.getMemgender()));
					pstm.setString(5, rb.getMemoccupation());
					pstm.setInt(6, tr_no);
					pstm.setDate(7, java.sql.Date.valueOf(memdobdt));
					pstm.setInt(8, rb.getSno());
					upd = pstm.executeUpdate();
					if (upd == 0) {
						request.setAttribute("i", "upd failure");
					} else {
						request.setAttribute("i", "upd success");
					}
				} else {
					if (oldsno != rb.getSno()) {
						request.setAttribute("i", "Already Exists");
					} else {
						String addmem = "UPDATE worker_family_details   SET   trno=?, relation_code=?, member_name=?, gender_code=?, "
								+ "     member_occupation=?,date_of_birth='" + memdobdt + "' where sno=?";
						pstm = con.prepareStatement(addmem);
						pstm.setInt(1, tr_no);
						pstm.setInt(2, Integer.parseInt(rb.getMemrelation()));
						pstm.setString(3, rb.getMemname());
						pstm.setInt(4, Integer.parseInt(rb.getMemgender()));
						pstm.setString(5, rb.getMemoccupation());
						// pstm.setDate(6,java.sql.Date.valueOf(memdobdt));
						pstm.setInt(6, rb.getSno());
						upd = pstm.executeUpdate();
						if (upd == 0) {
							request.setAttribute("i", "upd failure");
						} else {
							request.setAttribute("i", "upd success");
						}
					}
				}

			}
			request.setAttribute("checkservice", "fmyupd");
			return SUCCESS;
		} catch (Exception e) {
			return "failure";
		} finally {
			if (pstm != null) {

				try {
					pstm.close();
				} catch (Exception e) {

				}

			}
			if (rs != null) {

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

	   
	public RegistrationBeen getModel() {
		// throw new UnsupportedOperationException("Not supported yet."); //To
		// change body of generated methods, choose Tools | Templates.
		return rb;
	}

	public void validate() {
		String Msg = null;
		if (rb.getMemaadhar() != null) {
			if (rb.getMemaadhar().length() >= 1) {
				Msg = Validations_new.Validate(rb.getMemaadhar(), 2, 12, false);
				if (Msg != null) {
					addActionError("Aadhar Number " + Msg);
				}
			}
		}

		if (rb.getMemname() != null) {
			if (rb.getMemname().length() >= 1) {
				Msg = Validations_new.Validate(rb.getMemname(), 3, 100, false);
				if (Msg != null) {
					addActionError("Member Name " + Msg);
				}
			}
		}
		if (rb.getMemrelation() != null) {
			if (rb.getMemrelation().length() >= 1) {
				Msg = Validations_new.Validate(rb.getMemrelation(), 2, 3, false);
				if (Msg != null) {
					addActionError("Relation " + Msg);
				}
			}
		}
		if (rb.getA1() != null) {
			if (rb.getA1().length() >= 1) {
				Msg = Validations_new.Validate(rb.getA1(), 6, 2, false);
				if (Msg != null) {
					addActionError("Nominee " + Msg);
				} else {
					if (rb.getA1().equalsIgnoreCase("Y")) {
						if (rb.getMemaadhar().length() >= 1) {
							Msg = Validations_new.Validate(rb.getMemaadhar(), 2, 12, false);
							if (Msg != null) {
								addActionError(" Nominee Aadhaar No " + Msg);
							}
						} else {
							addActionError("If Nominee Aadhaar No Required");
						}
					}
				}
			}
			// if(rb.getMemoccupation() != null){
			// if (rb.getMemoccupation().length() >= 1) {
			// Msg = Validations_new.Validate(rb.getMemoccupation(), 3, 20,
			// false);
			// if (Msg != null) {
			// addActionError("Member Occupation " + Msg);
			// }
			// }
			// }
			// nominee details

		}
		if (rb.getA1() != null) {
			if (rb.getMemdob().length() >= 1) {
				Msg = Validations_new.Validate(rb.getMemdob(), 8, 10, false);
				if (Msg != null) {
					addActionError("Memeber DOB " + Msg);
				} else {
					String dtval = MyUtil.checkCurrentDateValid(rb.getMemdob(), MyUtil.gettodaysDate());
					if (dtval.equalsIgnoreCase("N")) {
						addActionError("Memeber DOB Must before today date");
					}
				}
			}
		}
	}
}
