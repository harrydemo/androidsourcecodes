package wpf;
import static wpf.ConstantUtil.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String tempPath = "D:\\MyWork\\KDWB\\TEMP";//��ʱ�ļ�Ŀ¼ 
    File tempPathFile; 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		this.doPost(req, resp);
	}
    @SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
           throws IOException, ServletException {
		request.setCharacterEncoding(CHAR_ENCODING);
		String action = request.getParameter("action");
		if(action == null){//��actionΪ��ʱ
			return;
		}
		if(action.equals("upload")){//�ϴ��Ƽ�����
			String name=request.getParameter("photoName");
			String des = request.getParameter("photoDes");
			String xid = request.getParameter("album");
	    	try { 
	    		//ʵ����һ��Ӳ���ļ�����,���������ϴ����ServletFileUpload
	    		DiskFileItemFactory factory = new DiskFileItemFactory(); 
	    		factory.setSizeThreshold(4096);//���û�������С��������4kb 
	    		factory.setRepository(tempPathFile);//���û�����Ŀ¼ 
	           
	    		//�����Ϲ���ʵ�����ϴ����
	    		ServletFileUpload upload = new ServletFileUpload(factory); 

	    		upload.setSizeMax(3*1024*1024);//��������ļ��ߴ磬������3MB 

	    		List<FileItem> items = upload.parseRequest(request);//�õ����е��ļ� 
	    		Iterator<FileItem> i = items.iterator(); 
	    		while (i.hasNext()) {
	    			FileItem fi = (FileItem) i.next(); 
	    			String fileName = fi.getName(); 
	    			if (fileName != null){ 
	    				File fullFile = new File(fi.getName()); 
	    				DBUtil.insertImage(fullFile, name, des, xid);//�洢�����ݿ�
	   					//�洢��Ӳ���е��ļ�
	    			} 
	    		}
	    		request.setAttribute("result", UPLOAD_SUCCESS);
	    		request.getRequestDispatcher("uploadImage.jsp").forward(request,response);
	    	} 
	    	
	    	catch(SizeLimitExceededException e){
	    		request.setAttribute("result", UPLOAD_SIZE_EXCEED);
	    		request.getRequestDispatcher("uploadImage.jsp").forward(request,response);
	    	}catch (Exception e) {
	    		request.setAttribute("result", UPLOAD_FAIL);
	    		request.getRequestDispatcher("uploadImage.jsp").forward(request,response);
	    	} 			
		}
		else if(action.equals("uploadHead")){		//�ϴ�ͷ��
			String hdes = request.getParameter("hdes");		//��ȡ����
			String uno = request.getParameter("uno");		//��ȡ�ϴ���
			try {
				String filePath = request.getParameter("filePath");	//��ȡ�ļ�·��
				DiskFileItemFactory factory = new DiskFileItemFactory(); 
				factory.setSizeThreshold(4096);//���û�������С��������4kb 
				factory.setRepository(tempPathFile);//���û�����Ŀ¼ 
				
				//�����Ϲ���ʵ�����ϴ����
				ServletFileUpload upload = new ServletFileUpload(factory); 
				upload.setSizeMax(3*1024*1024);//��������ļ��ߴ磬������3MB 
				
				List<FileItem> items = upload.parseRequest(request);//�õ����е��ļ� 
				Iterator<FileItem> i = items.iterator();
				
	    		while (i.hasNext()) {
	    			FileItem fi = (FileItem) i.next(); 
	    			String fileName = fi.getName(); 
	    			if (fileName != null){ 
	    				File fullFile = new File(fi.getName()); 
	    				DBUtil.insertHeadFile(fullFile, hdes, uno);//�洢�����ݿ�
	   					//�洢��Ӳ���е��ļ�
	    			} 
	    		}
	    		request.setAttribute("result", UPLOAD_SUCCESS);
	    		request.getRequestDispatcher("personalInfo.jsp").forward(request,response);
				
			} 
	    	catch(SizeLimitExceededException e){
	    		request.setAttribute("result", UPLOAD_SIZE_EXCEED);
	    		request.getRequestDispatcher("personalInfo.jsp").forward(request,response);
	    	}catch (Exception e) {
	    		request.setAttribute("result", UPLOAD_FAIL);
	    		request.getRequestDispatcher("personalInfo.jsp").forward(request,response);
	    	} 
		}
    } 
    public void init() throws ServletException {//��ʼ������
//		��ʼ����ŵ�Ӳ���ϵ��ļ�
//		File uploadFile = new File(uploadPath); 
//		if (!uploadFile.exists()) {  
//			uploadFile.mkdirs();
//		} 
    	File tempPathFile = new File(tempPath); 
    	if (!tempPathFile.exists()) { 
    		tempPathFile.mkdirs(); 
    	} 
    } 
}
