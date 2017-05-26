<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page pageEncoding="UTF-8"%>

<%
	System.out.println("test");
	//String dir = application.getRealPath("/upload");
	String uploadPath = "C:/Users/kitcoop/Desktop/minsoo/project/workspace/Tragramer/WebContent/upload";


	int max = 2010241024;
	
	//최대크기, dir 디렉토리에 파일을 업로드하는 multipartRequest
	System.out.println(request);
	//System.out.println(dir);
	MultipartRequest mr = new MultipartRequest(request, uploadPath, max, "UTF-8");
	String orgFileName = mr.getOriginalFileName("uploaded_file");
	String saveFileName = mr.getFilesystemName("uploaded_file");
	
	
	//System.out.println(orgFileName+"이 저장되었습니다.");
	//System.out.println(saveFileName+"이 저장되었습니다.");
	
	
%>
