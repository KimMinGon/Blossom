<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>

<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %> 
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@page
    import="com.oreilly.servlet.MultipartRequest,com.oreilly.servlet.multipart.DefaultFileRenamePolicy,java.util.*,java.io.*"%>
<%
	request.setCharacterEncoding("utf-8");
	System.out.println(request.getParameter("data"));

	
	//���ǻ���. �������� ��Ȯ�� ��ξƷ��� ������ ������� �־���Ѵ�.
    //������ �ѱ涧 enctype="multipart/form-data"

    //��Ȯ�Ѱ���� ������ Ȯ�ι���� ������������...
    String realFolder = ""; //���ϰ�θ� �˾ƺ������� �ӽú����� �ϳ� �����,
    String saveFolder = "upload"; //�������� �������� ������ �ڿ�...
    String encType = "euc-kr"; //���ڵ���ĵ� �Բ� ������ ��,
    int maxSize = 100 * 1024 * 1024; //���� �ִ�뷮���� ����������.(���� 100�ް�)
    ServletContext context = getServletContext();
    realFolder = context.getRealPath(saveFolder);
 //   System.out.println("the realpath is : " + realFolder); // file path

    File dir = new File(realFolder); // ���丮 ��ġ ����
  
    if (!dir.exists()) { // ���丮�� �������� ������
        dir.mkdirs(); // ���丮 ����.!
    }
    // print current time
    Date today = new Date();
  //  System.out.println(today);
	//System.out.println("dddd" + request + "/" + realFolder);
    try {
        //��Ƽ��Ʈ������ ���ÿ� ������ ������ �ǰ�...
        MultipartRequest multi = null;
        multi = new MultipartRequest(request, "C:/Users/kitcoop/Desktop/minsoo/project/workspace/Tragramer/WebContent/upload", maxSize,
                encType);
        //�� ���������� ������ �̹� ������ �Ǿ���.

        //������ �Ѿ�Դ����� �Ķ���͵��� ���������� �̷���.
        Enumeration params = multi.getParameterNames();

        //�׸��� ������ �Ķ���͸� ������ ���...
        while (params.hasMoreElements()) {
            String name = (String) params.nextElement();//�Ķ�����̸��� �����µ�
            String value = multi.getParameter(name);//�̸��� �̿���  ���������´�
            System.out.println(name + " = " + value);
            application.log(name + " = " + value); // logManager
        }

        //�̹��� ���ϰ� ���õ� �Ķ���͸� �����´�.
        Enumeration files = multi.getFileNames();
      //  ByteArrayInputStream by = "";
        //�̹��� ���ϰ��� �Ķ���͸� ��������...
        while (files.hasMoreElements()) {
            String name = (String) files.nextElement();//�Ķ�����̸��� �����µ�
            String filename = multi.getFilesystemName(name);//�̸��� �̿��� ����� �����̸��� �����´�.
            String original = multi.getOriginalFileName(name);//�̸����̿��� ���� �����̸��� �����´�.
            String type = multi.getContentType(name);//����Ÿ�Ե� �����ü��ִ�.
            File f = multi.getFile(name);//���� �뷮�� �˾ƺ������ؼ��� �̷���...
            System.out.println("Parameter Name: " + name);
            System.out.println("Real File Name: " + original);
            System.out.println("Saved File Name: " + filename);
            System.out.println("File Type: " + type);
           
            if (f != null) {
                System.out.println("File Size: " + f.length());
            }

            System.out.println("-------------------------------");

        }
    } catch (IOException ioe) {
        System.out.println(ioe);
    } catch (Exception ex) {
        System.out.println(ex);
    }
%>