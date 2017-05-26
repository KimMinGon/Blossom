package sendmail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class SendMail {

	private DataSource dataSource = null;

	public SendMail() {
		
	      try {
	          Context initCtx = new InitialContext();
	          Context envCtx = (Context) initCtx.lookup("java:comp/env");
	          this.dataSource = (DataSource) envCtx.lookup("jdbc/Blossom");
	       } catch (NamingException e) {
	          System.out.println("[에러] : " + e.getMessage());
	       }
		
	}
	
	public int sendMain(String data){
		
		final String username = "tragrammer.blossom@gmail.com";//발신 g메일
		final String password = "vmfhwprxm"; //비밀번호
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
				
			}
		  });

		int flag = 0;
		
		System.out.println(data);
		    Connection conn = null;
		    PreparedStatement pstmt = null;
		    ResultSet rs = null; 		 

		    try {
				conn = dataSource.getConnection();
				
				String sql = "select count(*) from useraccount where ID like ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1,data);
				
				rs = pstmt.executeQuery();
				rs.next();
				 
				
				if(Integer.parseInt(rs.getString(1)) == 0){
					flag = 0;
					
					return flag;
				 }else{		
				
				
				rs.close();
				
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress("tragrammer.blossom@gmail.com"));//발신메일
				message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(data));//수신메일주소
				
				
				
				message.setSubject(data + "님의 임시 비밀번호 입니다.");//제목
				
				
				
				conn.close();
				pstmt.close();
				String temporaryPassword;
				int size;
				
				StringBuffer buffer = new StringBuffer();
				Random random = new Random();
				size=8;

				String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9".split(",");

				for (int i = 0; i < size; i++) {
					
				buffer.append(chars[random.nextInt(chars.length)]);
				
				}
				
				
				message.setText(buffer.toString());//내용
				
				Transport.send(message);
				
				conn = dataSource.getConnection();
				
				String sql1 = "select usernumber from useraccount where ID like ? ";
				pstmt = conn.prepareStatement(sql1);
				
				pstmt.setString(1, data);
				
				rs = pstmt.executeQuery();
				rs.next();
				
				int usernumber = rs.getInt(1);
				

				
				conn = dataSource.getConnection();
				
				String sql2 = "update useraccount set password=? where usernumber=? ";
				pstmt = conn.prepareStatement(sql2);
				
				pstmt.setString(1,buffer.toString());
				pstmt.setInt(2,usernumber);
				
				flag = pstmt.executeUpdate();
				
				
				 }
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AddressException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				if (conn != null) try { conn.close(); } catch (SQLException e1) {e1.printStackTrace(); }
	            if (pstmt != null) try {pstmt.close(); } catch (SQLException e1) {e1.printStackTrace(); }
	            if (rs != null) try {rs.close(); } catch (SQLException e1) {e1.printStackTrace(); }
			}
		
			
		return flag;
	}
			
}