package model1;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserAccountDAO {
	private DataSource dataSource = null;

	public UserAccountDAO() {
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			this.dataSource = (DataSource) envCtx.lookup("jdbc/Blossom");
		} catch (NamingException e) {
			System.out.println("[에러] : " + e.getMessage());
		}
	}

	public String joinOk(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int flag1 = 0;
		int flag2 = 0;

		String id = "dupli";
		String alarm = "success";

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "insert into useraccount values(sequence1.nextval, ?, ?, sysdate, 0)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("id").toString());
			pstmt.setString(2, jsonObject.get("pw").toString());

			flag1 = pstmt.executeUpdate();

			pstmt.close();

			String sql1 = "insert into userprofile values(sequenceuserprofile.nextval, sequence1.currval,?,0,'')";
			pstmt = conn.prepareStatement(sql1);

			pstmt.setString(1, jsonObject.get("nickName").toString());

			flag2 = pstmt.executeUpdate();

			String sql2 = "select usernumber from useraccount where ID like ?";
			pstmt = conn.prepareStatement(sql2);
			pstmt.setString(1, jsonObject.get("id").toString());

			rs = pstmt.executeQuery();
			rs.next();

			System.out.println("-----------------" + (rs.getString(1)));
			alarm += "/" + rs.getString(1);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (SQLIntegrityConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
				}
		}

		// System.out.println(flag);
		return alarm;

	}

	public String loginOk(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		/*
		 * int flag1 = 0; int flag2 = 0;
		 */

		String id = "noexist";
		String pw = "diff";
		String alarm = "success";

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();
			String sql = "select count(*) from useraccount where ID like ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jsonObject.get("id").toString());

			rs = pstmt.executeQuery();
			rs.next();

			if (Integer.parseInt(rs.getString(1)) == 0) {
				return id;
			}

			pstmt.close();
			rs.close();

			/*
			 * if(jsonObject.get("google").toString().equals("true")){
			 * 
			 * }
			 */

			String sql1 = "select password,usernumber from useraccount where ID = ?";
			pstmt = conn.prepareStatement(sql1);

			pstmt.setString(1, jsonObject.get("id").toString());
			rs = pstmt.executeQuery();

			rs.next();

			String usernumber = rs.getString(2);

			if (jsonObject.get("google").toString().equals("true")) {

				String sql2 = "select nickname from userprofile where usernumber = ?";
				pstmt = conn.prepareStatement(sql2);

				pstmt.setString(1, usernumber);
				rs = pstmt.executeQuery();

				rs.next();

				return alarm + "/" + usernumber + "/" + rs.getString(1);
			}

			else if (jsonObject.get("google").toString().equals("false")) {

				if (!rs.getString(1).equals(jsonObject.get("pw").toString())) {
					return pw;
				}

				alarm += "/" + usernumber;

				String sql2 = "select nickname from userprofile where usernumber = ?";
				pstmt = conn.prepareStatement(sql2);

				pstmt.setString(1, usernumber);
				rs = pstmt.executeQuery();

				rs.next();

				alarm += "/" + rs.getString(1);

				return alarm;
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return alarm;
	}

	public JSONArray loginAccountOk(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		JSONObject jsonObject1 = new JSONObject();
		JSONArray jsonArray1 = new JSONArray();

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			conn = dataSource.getConnection();

			String sql = "select usernumber from useraccount where ID like ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("id").toString());

			rs = pstmt.executeQuery();

			rs.next();

			int usernumber = rs.getInt(1);

			pstmt.close();
			rs.close();

			String sql2 = "select w.weight, w.insertdate, h.height, h.insertdate, a.walknum, a.walkdate,d.tdrinking, d.drinkingdate, r.reward, r.currlevel, r.tpoint, r.updatedate, g.tdistance, g.stime, g.ttime, g.insertdate from userweight w, userheight h,walking a, drinking d, reward r, gps g where w.USERNUMBER=? and h.USERNUMBER=? and a.USERNUMBER=? and d.USERNUMBER=? and r.USERNUMBER=? and g.USERNUMBER=?";

			pstmt = conn.prepareStatement(sql2);

			pstmt.setInt(1, usernumber);
			pstmt.setInt(2, usernumber);
			pstmt.setInt(3, usernumber);
			pstmt.setInt(4, usernumber);
			pstmt.setInt(5, usernumber);
			pstmt.setInt(6, usernumber);

			rs = pstmt.executeQuery();

			while (rs.next()) {

				String weight = rs.getString("weight");
				jsonObject1.put("w.weight", weight);

				String insertdate = rs.getString("insertdate");
				jsonObject1.put("w.insertdate", insertdate);

				String height = rs.getString("height");
				jsonObject1.put("h.height", height);

				String hinsertdate = rs.getString("insertdate");
				jsonObject1.put("h.insertdate", hinsertdate);

				String walknum = rs.getString("walknum");
				jsonObject1.put("a.walknun", walknum);

				String walkdate = rs.getString("walkdate");
				jsonObject1.put("a.walkdate", walkdate);

				String tdrinking = rs.getString("tdrinking");
				jsonObject1.put("d.tdrinking", tdrinking);

				String drinkingdate = rs.getString("drinkingdate");
				jsonObject1.put("d.drinkingdate", drinkingdate);

				String reward = rs.getString("reward");
				jsonObject1.put("r.reward", reward);

				String currlevel = rs.getString("currlevel");
				jsonObject1.put("r.currlevel", currlevel);

				String tpoint = rs.getString("tpoint");
				jsonObject1.put("r.tpoint", tpoint);

				String updatedate = rs.getString("updatedate");
				jsonObject1.put("r.updatedate", updatedate);

				String tdistance = rs.getString("tdistance");
				jsonObject1.put("g.tdistance", tdistance);

				String ttime = rs.getString("ttime");
				jsonObject1.put("g.ttime", ttime);

				String stime = rs.getString("stime");
				jsonObject1.put("g.stime", stime);

				String gpsinsertdate = rs.getString("insertdate");
				jsonObject1.put("g.insertdate", gpsinsertdate);

				// System.out.println(jsonObject1.size());
				jsonArray1.add(jsonObject1);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return jsonArray1;
	}

	public Integer userAccount(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "select password from useraccount where usernumber=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, jsonObject.get("usernumber").toString());

			rs = pstmt.executeQuery();

			rs.next();

			String password = rs.getString(1);

			if (!password.equals(jsonObject.get("password"))) {
				return flag;
			} else {

				pstmt.close();
				String sql1 = "update useraccount set password=? where usernumber=? ";
				pstmt = conn.prepareStatement(sql1);

				pstmt.setString(1, jsonObject.get("changepw").toString());
				pstmt.setString(2, jsonObject.get("usernumber").toString());

				flag = pstmt.executeUpdate();

			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public Integer userProfile(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update userprofile set nickname=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("nickname").toString());
			pstmt.setString(2, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public Integer reward(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update reward set reward=? , currlevel=?, tpoint=? ,updatedate=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("reward").toString());
			pstmt.setString(2, jsonObject.get("currlevel").toString());
			pstmt.setString(3, jsonObject.get("tpoint").toString());
			pstmt.setString(4, "sysdate");
			pstmt.setString(5, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public Integer drinking(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update drinking set tdrinking=? ,drinkingdate=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("tdrinking").toString());
			pstmt.setString(2, "sysdate");
			pstmt.setString(3, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;
	}

	public Integer walking(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update walking set walknum=?, walkdate=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("walknum").toString());
			pstmt.setString(2, "sysdate");
			pstmt.setString(3, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public Integer gps(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update gps set tdistance=?, ttime=?, stime=?, insertdate=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("tdistance").toString());
			pstmt.setString(2, jsonObject.get("ttime").toString());
			pstmt.setString(3, jsonObject.get("stime").toString());
			pstmt.setString(4, "sysdate");
			pstmt.setString(5, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public Integer userweight(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update userweight set tdistance=?, ttime=?, stime=?, insertdate=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("tdistance").toString());
			pstmt.setString(2, jsonObject.get("ttime").toString());
			pstmt.setString(3, jsonObject.get("stime").toString());
			pstmt.setString(4, "sysdate");
			pstmt.setString(5, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public Integer userheight(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update userheight set height=?, insertdate=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("userheight").toString());
			pstmt.setString(2, "sysdate");
			pstmt.setString(3, jsonObject.get("usernumber").toString());

			flag = pstmt.executeUpdate();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;
	}

	public Integer character(String data) {

		Connection conn = null;
		PreparedStatement pstmt = null;

		int flag = 0;

		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);

			// System.out.println(jsonObject.get("id").toString());

			conn = dataSource.getConnection();

			String sql = "update character set clevel=?, totalpoint=? where usernumber=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("clevel").toString());
			pstmt.setString(2, jsonObject.get("totalpoint").toString());
			pstmt.setString(3, jsonObject.get("usernumber").toString());

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
		}

		return flag;

	}

	public String backupdata(String data, String table) {

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String success = "success";
		String fail = "fail";
		int usernumber = 0;

		JSONObject jsonObject1 = new JSONObject();
		JSONArray jsonArray1 = new JSONArray();

		try {
			JSONParser parser = new JSONParser();
		
			JSONArray jsonArray = (JSONArray) parser.parse(data);
			
			if( jsonArray.size() == 0)
				return "dataZero";
			
			JSONObject jsonObject = (JSONObject) jsonArray.get(0);
			
			conn = dataSource.getConnection();
			
			
			String sql = "";
			if (table.equals("weight")) {
				sql = "delete from userweight where usernumber like ?";
			} else if (table.equals("height")) {
				sql = "delete from userheight where usernumber like ?";	
			} else if (table.equals("walk")) {
				sql = "delete from walking where usernumber like ?";
			} else if (table.equals("water")) {
				sql = "delete from drinking where usernumber like ?";
			} else if (table.equals("reward")) {
				sql ="delete from reward where usernumber like ?";
			} else if (table.equals("gps")) {
				sql = "delete from gps where usernumber like ?";
			} else if (table.equals("ssak")) {
				sql ="delete from character where usernumber like ?";
			}
			
			System.out.println("딜리트구문 : " + jsonObject.get("usernumber").toString());
			
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, jsonObject.get("usernumber").toString());
			pstmt.executeUpdate();
			
			pstmt.close();
			
			
			if (table.equals("weight")) {
				for(int i = 0 ; i < jsonArray.size() ; i++) {
					jsonObject = (JSONObject) jsonArray.get(i);
					sql = "insert into userweight values(sequenceweight.nextval,?,?,?)";
	
					pstmt = conn.prepareStatement(sql);
	
					pstmt.setString(1, jsonObject.get("usernumber").toString());
					pstmt.setString(2, jsonObject.get("weight").toString());
					pstmt.setString(3, jsonObject.get("date").toString());
	
					pstmt.executeUpdate();
				}
			}

			else if (table.equals("height")) {
				sql = "insert into userheight values(sequenceheight.nextval,?,?,?)";

				pstmt = conn.prepareStatement(sql);
				
				for(int i = 0 ; i < jsonArray.size() ; i++) {
					jsonObject = (JSONObject) jsonArray.get(i);	
				
				pstmt.setString(1, jsonObject.get("usernumber").toString());
				pstmt.setString(2, jsonObject.get("height").toString());
				pstmt.setString(3, jsonObject.get("date").toString());

				pstmt.executeUpdate();
				}
				
			} else if (table.equals("walk")) {
				sql = "insert into walking values(sequencewalking.nextval,?,?,?)";

				pstmt = conn.prepareStatement(sql);

				for(int i = 0 ; i < jsonArray.size() ; i++) {
					jsonObject = (JSONObject) jsonArray.get(i);	
				pstmt.setString(1, jsonObject.get("usernumber").toString());
				pstmt.setString(2, jsonObject.get("walk").toString());
				pstmt.setString(3, jsonObject.get("date").toString());
				
				pstmt.executeUpdate();
				
				}
				
			} else if (table.equals("water")) {
				sql = "insert into drinking values(sequencedrinking.nextval,?,?,?)";

				pstmt = conn.prepareStatement(sql);

				for(int i = 0 ; i < jsonArray.size() ; i++) {
					jsonObject = (JSONObject) jsonArray.get(i);	
				pstmt.setString(1, jsonObject.get("usernumber").toString());
				pstmt.setString(2, jsonObject.get("water").toString());
				pstmt.setString(3, jsonObject.get("date").toString());

				pstmt.executeUpdate();
				}
				
			} else if (table.equals("reward")) {
				sql = "insert into reward values(sequencereward.nextval,?,?,?,?,?)";

				pstmt = conn.prepareStatement(sql);

				for(int i = 0 ; i < jsonArray.size() ; i++) {
					jsonObject = (JSONObject) jsonArray.get(i);	
				pstmt.setString(1, jsonObject.get("usernumber").toString());
				pstmt.setString(2, jsonObject.get("reward").toString());
				pstmt.setString(3, jsonObject.get("currlevel").toString());
				pstmt.setString(4, jsonObject.get("tpoint").toString());
				pstmt.setString(5, jsonObject.get("date").toString());

				pstmt.executeUpdate();
				}
				
			} else if (table.equals("gps")) {
				sql = "insert into gps values(sequencegps.nextval,?,?,?,?,?,?,?)";

				pstmt = conn.prepareStatement(sql);

				for(int i = 0 ; i < jsonArray.size() ; i++) {
					jsonObject = (JSONObject) jsonArray.get(i);	
				pstmt.setString(1, jsonObject.get("usernumber").toString());
				pstmt.setString(2, jsonObject.get("distance").toString());
				pstmt.setString(3, jsonObject.get("ttime").toString());
				pstmt.setString(4, jsonObject.get("stime").toString());
				pstmt.setString(5, jsonObject.get("date").toString());
				pstmt.setString(6, jsonObject.get("mapimg").toString());
				pstmt.setString(7, jsonObject.get("path").toString());

				pstmt.executeQuery();
				}
			
			} else if (table.equals("ssak")) {
				sql = "insert into character values(sequencecharacter.nextval,?,?,?,?,?,?)";

				pstmt = conn.prepareStatement(sql);

				for(int i = 0 ; i < jsonArray.size() ; i++) {
				
				jsonObject = (JSONObject) jsonArray.get(i);	
				pstmt.setString(1, jsonObject.get("usernumber").toString());
				pstmt.setString(2, jsonObject.get("level").toString());
				pstmt.setString(3, jsonObject.get("total_exp").toString());
				pstmt.setString(4, jsonObject.get("exp_today_walk").toString());
				pstmt.setString(5, jsonObject.get("exp_today_water").toString());
				pstmt.setString(6, jsonObject.get("date").toString());
				

				pstmt.executeUpdate();
				
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return fail;
		}  finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			// if ( != null) try {pstmt.close(); } catch (SQLException e1)
			// {e1.printStackTrace(); }

		}

		return success;

	}

	public String get_all_data_for_backup(String table, String usernumber){
	    Connection conn = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    JSONArray jsonArray = null;
	    JSONObject jsonObject = null;
	   
	    try {

	        conn = dataSource.getConnection();
	        
	        String tablename = "";
	        
	        switch(table){
	        	case "WEIGHT" :
	        		tablename = "userweight";
	        		break;
	        	case "HEIGHT" :
	        		tablename = "userheight";
	        		break;
	        	case "WATER" :
	        		tablename = "drinking";
	        		break;
	        	case "WALK" :
	        		tablename = "walking";
	        		break;
	        	case "REWARD" :
	        		tablename = "reward";
	        		break;
	        	case "GPS" :
	        		tablename = "gps";
	        		break;
	        	case "SSAK" :
	        		tablename = "character";	        		
	        		break;
	        }
	        
	        System.out.println(tablename);
	        String sql = "select * from "+ tablename +" where usernumber like ?";
	        System.out.println(sql + usernumber);
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, usernumber);

	        jsonArray = new JSONArray();
	        jsonObject = new JSONObject();

	        rs = pstmt.executeQuery();

	        switch (table) {
	            case "WEIGHT" :
	                while(rs.next())
	                {
	                	 jsonObject = new JSONObject();
	                	
	                	jsonObject.put("date", rs.getString("insertdate").split(" ")[0]);
	                    jsonObject.put("weight", rs.getString("weight"));
	                    jsonArray.add(jsonObject);
	                }
	                break;
	                
	            case "HEIGHT" :
	                while(rs.next())

	                {
	                	 jsonObject = new JSONObject();
	                    jsonObject.put("date", rs.getString("insertdate").split(" ")[0]);
	                    jsonObject.put("height", rs.getString("height"));
	                    jsonArray.add(jsonObject);
	                }
	                break;
	            case "WALK" :
	                while(rs.next())
	                {
	                	 jsonObject = new JSONObject();
	                    jsonObject.put("date", rs.getString("walkdate").split(" ")[0]);
	                    jsonObject.put("walk", rs.getString("walknum"));
	                    jsonArray.add(jsonObject);
	                }
	                break;
	            case "WATER" :
	                while(rs.next())
	                {
	                	 jsonObject = new JSONObject();
	                    jsonObject.put("date", rs.getString("drinkingdate").split(" ")[0]);
	                    jsonObject.put("water", rs.getString("tdrinking"));
	                    jsonArray.add(jsonObject);
	                }
	                break;
	            case "REWARD" :
	                //TABLE REWARD ....., date TEXT, reward INTEGER, currlevel INTEGER, tpoint INTEGER
	                while(rs.next())
	                {
	                	 jsonObject = new JSONObject();
	                    jsonObject.put("date", rs.getString("updatedate"));///////////////////
	                    jsonObject.put("reward", rs.getString("reward"));///////////////////
	                    jsonObject.put("currlevel", rs.getString("currlevel"));///////////////////
	                    jsonObject.put("tpoint", rs.getString("tpoint"));///////////////////
	                    jsonArray.add(jsonObject);
	                }
	                break;
	            case "GPS" :
	                //TABLE GPS ..... date TEXT, distance REAL, stime TEXT, ttime TEXT, mapimg TEXT, path TEXT
	                while(rs.next())
	                {
	                	 jsonObject = new JSONObject();
	                    jsonObject.put("date", rs.getString("insertdate"));///////////////////
	                    jsonObject.put("distance", rs.getString("tdistance"));///////////////////
	                    jsonObject.put("stime", rs.getString("stime"));///////////////////
	                    jsonObject.put("ttime", rs.getString("ttime"));///////////////////
	                    jsonObject.put("mapimg", rs.getString("imagename"));///////////////////
	                    jsonObject.put("path", rs.getString("path"));///////////////////
	                    jsonArray.add(jsonObject);
	                }
	                break;
	            case "SSAK" :
	                 //TABLE SSAK ....... date TEXT, exp_today_water INTEGER, exp_today_walk INTEGER, total_exp INTEGER, level INTEGER
	                while(rs.next())
	                {
	                	 jsonObject = new JSONObject();
	                    jsonObject.put("date", rs.getString("insertdata").split(" ")[0]);///////////////////
	                    jsonObject.put("exp_today_water", rs.getString("todaydrinking"));///////////////////
	                    jsonObject.put("exp_today_walk", rs.getString("todaywalk"));///////////////////
	                    jsonObject.put("total_exp", rs.getString("totalpoint"));///////////////////
	                    jsonObject.put("level", rs.getString("clevel"));///////////////////
	                    jsonArray.add(jsonObject);
	                } 
	                break;
	        }

	    } catch (NumberFormatException e) {
	        e.printStackTrace();    //System.out.println(e.getMessage()); 
	        return "fail";
	    }  catch (SQLException e) {
	        e.printStackTrace();   	//System.out.println(e.getMessage()); 
	        return "fail";
	    } finally {
	        if (conn != null) try { conn.close(); } catch (SQLException e1) { e1.printStackTrace(); }
	        if (pstmt != null) try { pstmt.close(); } catch (SQLException e1) { e1.printStackTrace(); }
	        if (rs != null) try { rs.close(); } catch (SQLException e1) { e1.printStackTrace(); }
	    }
	    System.out.println("test" + jsonArray.toJSONString());
	    return jsonArray.toJSONString();
	}


}