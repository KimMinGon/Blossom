<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="org.json.simple.JSONArray"%>
<%@ page import="model1.UserAccountDAO" %>

<% 
request.setCharacterEncoding("utf-8");
String data = request.getParameter("data");
System.out.println(data);

UserAccountDAO dao = new UserAccountDAO();
String alarm = dao.joinOk(data);
//System.out.println(alarm);
if(alarm.equals("dupli")){
	out.println("dupli");
}else if((alarm.split("/")[0]).equals("success")){
	out.println(alarm);
	System.out.println(alarm);
}else{
	out.println("fail");
}

%>

