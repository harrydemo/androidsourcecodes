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
	private String tempPath = "D:\\MyWork\\KDWB\\TEMP";//临时文件目录 
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
		if(action == null){//当action为空时
			return;
		}
		if(action.equals("upload")){//上传推荐动作
			String name=request.getParameter("photoName");
			String des = request.getParameter("photoDes");
			String xid = request.getParameter("album");
	    	try { 
	    		//实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
	    		DiskFileItemFactory factory = new DiskFileItemFactory(); 
	    		factory.setSizeThreshold(4096);//设置缓冲区大小，这里是4kb 
	    		factory.setRepository(tempPathFile);//设置缓冲区目录 
	           
	    		//用以上工厂实例化上传组件
	    		ServletFileUpload upload = new ServletFileUpload(factory); 

	    		upload.setSizeMax(3*1024*1024);//设置最大文件尺寸，这里是3MB 

	    		List<FileItem> items = upload.parseRequest(request);//得到所有的文件 
	    		Iterator<FileItem> i = items.iterator(); 
	    		while (i.hasNext()) {
	    			FileItem fi = (FileItem) i.next(); 
	    			String fileName = fi.getName(); 
	    			if (fileName != null){ 
	    				File fullFile = new File(fi.getName()); 
	    				DBUtil.insertImage(fullFile, name, des, xid);//存储到数据库
	   					//存储到硬盘中的文件
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
		else if(action.equals("uploadHead")){		//上传头像
			String hdes = request.getParameter("hdes");		//获取描述
			String uno = request.getParameter("uno");		//获取上传者
			try {
				String filePath = request.getParameter("filePath");	//获取文件路径
				DiskFileItemFactory factory = new DiskFileItemFactory(); 
				factory.setSizeThreshold(4096);//设置缓冲区大小，这里是4kb 
				factory.setRepository(tempPathFile);//设置缓冲区目录 
				
				//用以上工厂实例化上传组件
				ServletFileUpload upload = new ServletFileUpload(factory); 
				upload.setSizeMax(3*1024*1024);//设置最大文件尺寸，这里是3MB 
				
				List<FileItem> items = upload.parseRequest(request);//得到所有的文件 
				Iterator<FileItem> i = items.iterator();
				
	    		while (i.hasNext()) {
	    			FileItem fi = (FileItem) i.next(); 
	    			String fileName = fi.getName(); 
	    			if (fileName != null){ 
	    				File fullFile = new File(fi.getName()); 
	    				DBUtil.insertHeadFile(fullFile, hdes, uno);//存储到数据库
	   					//存储到硬盘中的文件
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
    public void init() throws ServletException {//初始化方法
//		初始化存放到硬盘上的文件
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
