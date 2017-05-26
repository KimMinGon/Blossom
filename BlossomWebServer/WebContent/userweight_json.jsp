<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>
<%@ page import="model1.UserAccountDAO" %>
<%

String data = request.getParameter("data");

UserAccountDAO dao = new UserAccountDAO();
int flag = dao.userweight(data);

if(flag == 1){
	
}
%>