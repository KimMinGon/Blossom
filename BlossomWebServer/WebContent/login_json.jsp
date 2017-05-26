<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import= "java.util.ArrayList"%>
<%@ page import="model1.UserAccountDAO" %>

<% 

String data = request.getParameter("data");
System.out.println(data);
UserAccountDAO dao = new UserAccountDAO();
String alarm = dao.loginOk(data);

JSONArray jsonArray = new JSONArray();
JSONObject jsonObject = new JSONObject();

System.out.println(alarm);
	
if(alarm.equals("noexist")){
		out.println("noexist");
}else if(alarm.equals("diff")){
	out.println("diff");
}else if(alarm.split("/")[0].equals("success")){
	
	out.println(alarm);
	/* jsonArray = dao.loginAccountOk(data);
	jsonObject.put("success", "success");  (alarm.split("/")[0]).equals("success")){
	//
	jsonArray.add(1, jsonObject);
	System.out.println(jsonArray);
	out.println(jsonArray); */
	
}




%>

