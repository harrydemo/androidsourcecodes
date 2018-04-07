import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/** 
 * @作者 song shi chao 
 * @QQ   421271944
 * @创建日期 2011-8-24 
 * @版本 V 1.0 
 */

public class data extends HttpServlet {
 
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}
 
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
 
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
 
		//response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
        //String format = request.getParameter("format");
        //String data = "";
//        JSONObject obj = null;
//        if (format != null || format.equalsIgnoreCase("xml")){
//            data = "songshichao";
//            response.setContentType("text/xml");    
//        } else if (format.equalsIgnoreCase("JSON")){
//        	obj=new JSONObject();
//        	obj.put("name", "song");
//        	data = obj.toString();
//        	System.out.println(data);
//            response.setContentType("application/json");
//        } 
        
//        response.setContentLength(data.length());
//        response.getWriter().print(data);
       
//        response.getWriter().print(obj.toString());
//        response.flushBuffer();
//        response.getWriter().close();
          
		 
		 JSONArray array = null;
		 array = new JSONArray();
		 for(int i=0;i<4;i++)
		 { 
			 JSONObject obj=new JSONObject();
			 obj.put("name", "song"); 
			 array.add(obj);
		 }
		
// 	     out.print("song");  这句和out.write("song shi chao");的效果是一样的
		 out.write(array.toString());
		 System.out.print(array.toString());
 		 
// 	 request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	 
	public void init() throws ServletException {
		 
	}

}
