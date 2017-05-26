<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page import="model1.UserAccountDAO"%>

<%
	request.setCharacterEncoding("utf-8");
	String data = "";
	String table = "";

	if (request.getParameter("data_weight") != null) {
		data = request.getParameter("data_weight");
		table ="weight";
	}
	else if (request.getParameter("data_height") != null) {
		data = request.getParameter("data_height");
		table ="height";
	}
	else if (request.getParameter("data_walk") != null) {
			data = request.getParameter("data_walk");
			table ="walk";
		}
	else if (request.getParameter("data_water") != null) {
		data = request.getParameter("data_water");
		table ="water";
	}
	else if (request.getParameter("data_reward") != null) {
		data = request.getParameter("data_reward"); 
		table ="reward";
	}
	else if (request.getParameter("data_gps") != null) {
		data = request.getParameter("data_gps");
		table ="gps";
	}
	else if (request.getParameter("data_ssak") != null) {
		data = request.getParameter("data_ssak");
		table ="ssak";
	} 
	else {
		out.println("error");
		System.out.println("에러");
	}
	

	 System.out.println(data);
	System.out.println(table);
 
	UserAccountDAO dao = new UserAccountDAO();
	String alarm = dao.backupdata(data, table);

	if (alarm.equals("success")) {
		out.println(alarm);
		System.out.println(alarm);
	} else if(alarm.equals("fail")) {
		out.println("fail");
		System.out.println(alarm);
	} else {
		out.println("datazero");
		System.out.println(alarm);
	}
	

	 
%>

