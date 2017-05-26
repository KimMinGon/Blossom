<%@page import="sendmail.SendMail"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>

<%
String data = request.getParameter("data");
System.out.println(data);
SendMail sendmail = new SendMail();


int flag = sendmail.sendMain(data);

if(flag == 1){
	out.println("success");
}else{
	out.println("fail");
}

System.out.println(flag);

%>