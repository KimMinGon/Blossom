<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name = "viewport" content = "width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=2.0" />
<title>Insert title here</title>
</head>
<body>

<form action ="upload_ok.jsp" method="post" enctype="multipart/form-data">


파일 : <input type="file" name="upload" /><br />
<input type="submit" value="업로드" />
<%
System.out.println("hsfhsfdgsdf");
%>
</form>

</body>
</html>