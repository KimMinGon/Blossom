<%@page import="model1.UserAccountDAO"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="UTF-8"%>

<%     
request.setCharacterEncoding("utf-8");
String data = request.getParameter("data");

System.out.println(data);
UserAccountDAO dao = new UserAccountDAO();

int flag = dao.userAccount(data);

if(flag == 0){
	out.println("fail");
}else{
	out.println("success");
}
System.out.println(flag);
%>
