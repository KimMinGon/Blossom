<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="org.json.simple.JSONArray"%>
<%@ page import="model1.UserAccountDAO"%>

<%
	request.setCharacterEncoding("utf-8");

	String id = "";
	String table = "";

	if (request.getParameter("data_weight") != null) {
		id = request.getParameter("data_weight");
		table ="WEIGHT";
	}
	else if (request.getParameter("data_height") != null) {
		id = request.getParameter("data_height");
		table ="HEIGHT";
	}
	else if (request.getParameter("data_walk") != null) {
		id = request.getParameter("data_walk");
			table ="WALK";
		}
	else if (request.getParameter("data_water") != null) {
		id = request.getParameter("data_water");
		table ="WATER";
	}
	else if (request.getParameter("data_reward") != null) {
		id = request.getParameter("data_reward"); 
		table ="REWARD";
	}
	else if (request.getParameter("data_gps") != null) {
		id = request.getParameter("data_gps");
		table ="GPS";
	}
	else if (request.getParameter("data_ssak") != null) {
		id = request.getParameter("data_ssak");
		table ="SSAK";
	}
	else {
		out.println("error");
		System.out.println("에러");
	}
	
	System.out.println(table + " / " + id);

	UserAccountDAO dao = new UserAccountDAO();
	//String alarm = dao.get_all_data_for_backup(table, id);
	
	String alarm = "WEIGHT==" + dao.get_all_data_for_backup("WEIGHT", id)+"//";
	alarm += "HEIGHT==" + dao.get_all_data_for_backup("HEIGHT", id) + "//";
	alarm += "WALK==" + dao.get_all_data_for_backup("WALK", id) + "//";
	alarm += "WATER==" + dao.get_all_data_for_backup("WATER", id) + "//";
	alarm += "REWARD==" + dao.get_all_data_for_backup("REWARD", id) + "//";
	alarm += "GPS==" + dao.get_all_data_for_backup("GPS", id) + "//";
	alarm += "SSAK==" + dao.get_all_data_for_backup("SSAK", id);
	
	
	if (alarm.equals("fail")) {
		out.println("fail");
	} else {
		out.println(alarm);
	}
%>

