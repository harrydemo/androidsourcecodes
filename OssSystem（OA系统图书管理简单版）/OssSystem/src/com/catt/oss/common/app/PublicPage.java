package com.catt.oss.common.app;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.catt.oss.R;
import com.catt.oss.adapter.ListViewPageAdapter;
import com.catt.oss.service.MyService;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
public class PublicPage extends BaseActivity implements OnGestureListener{
	ViewFlipper flipper;
    GestureDetector detector;
    Button firstBtn;
    Button beforeBtn;
    Button nextBtn;
    Button afterBtn;
    TextView currentPage;   
    TextView totalPages;
    TextView everypagenum;
    TextView totalpagenums;
    EditText gotoPage;
    public  ListView listview;
    public static final int LIMIT=12;
    int pageCount=1;
    public static final int everyPage=LIMIT;
    public  List flist=new ArrayList();
    public ListViewPageAdapter fa;
    int curPage=1;
    int totalPage;
    int pagenumber;
    int totalpagenumbers;
    String code="";
    Map map;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.directorymain);
            listview = (ListView) findViewById(R.id.listview);
            listview.setOnTouchListener(new View.OnTouchListener() {
				
				public boolean onTouch(View v, MotionEvent event) {
					return onTouchEvent(event);
				}
			});
    }
    public  String getUrl(String url){
    	return MyService.getUrl(this, url);
    }

    /**
     * 
     * @Title: init
     * @Description: 初始化
     */
    public void init(ListViewPageAdapter pgadapter){
    	    fa=pgadapter;
    	    detector=new GestureDetector(this);
    	    flipper=(ViewFlipper) findViewById(R.id.viewflipper);
            firstBtn =(Button)findViewById(R.id.firstBtn) ;
            beforeBtn =(Button)findViewById(R.id.beforeBtn) ;
            nextBtn =(Button)findViewById(R.id.nextBtn) ;
            afterBtn=(Button)findViewById(R.id.afterBtn) ;
            currentPage=(TextView) findViewById(R.id.currentpage);
            totalPages=(TextView) findViewById(R.id.totalpage);
            everypagenum=(TextView) findViewById(R.id.everypagenum);
            totalpagenums=(TextView) findViewById(R.id.totalnum);
            pagenumber=everyPage;
            curPage=1;
            if (this.totalpagenumbers % pagenumber == 0) {
            	totalPage= this.totalpagenumbers / pagenumber;
           } else {
        	    totalPage= this.totalpagenumbers / pagenumber;
        	    totalPage+=1;
             }
            if(pagenumber>=totalpagenumbers){
            	pagenumber=totalpagenumbers;
            }
            Log.e("curPage=====", ""+curPage);
            Log.e("totalPage=====", ""+totalPage);
            Log.e("pagenumber=====", ""+pagenumber);
            Log.e("totalpagenumbers=====", ""+totalpagenumbers);
            currentPage.setTextColor(Color.RED);
            currentPage.setText(""+curPage);
            totalPages.setText("/"+totalPage);
            everypagenum.setTextColor(Color.RED);
            everypagenum.setText(""+pagenumber);
            totalpagenums.setText("/"+LIMIT);

    }
    /**
     * 
     * @Title: setListiner
     * @Description: 为按钮设置监听
     */
   public void setListiner() {
            firstBtn.setOnClickListener(firstListener);
            beforeBtn.setOnClickListener(beforeListener);
            nextBtn.setOnClickListener(nextListener);
            afterBtn.setOnClickListener(afterListener); 
    }
   public void checkButton(){
    	 if(pageCount<=1){
    		 pageCount=1;
    		 if(pageCount==totalPage){
    			 beforeBtn.setEnabled(false);
            	 nextBtn.setEnabled(false);
    			 Toast.makeText(this, "只有一页", Toast.LENGTH_SHORT).show();
            	 currentPage.setText(""+pageCount);
    		 }else{
    			 beforeBtn.setEnabled(false);
            	 nextBtn.setEnabled(true);
            	 Toast.makeText(this, "已是首页", Toast.LENGTH_SHORT).show();
            	 currentPage.setText(""+pageCount);
    		 }
        	
        }
    	 
    	 else if(pageCount>=totalPage){
        	 pageCount=totalPage;
        	 if(1==totalPage){
    			 beforeBtn.setEnabled(false);
            	 nextBtn.setEnabled(false);
    			 Toast.makeText(this, "只有一页", Toast.LENGTH_SHORT).show();
            	 currentPage.setText(""+pageCount);
    		 }else{
    			 nextBtn.setEnabled(false);
            	 beforeBtn.setEnabled(true);
            	 currentPage.setText(""+pageCount);
            	 Toast.makeText(this, "已是末页", Toast.LENGTH_SHORT).show();
    		 }
        	
         }  
    	 else{
    		 beforeBtn.setEnabled(true);
    		 nextBtn.setEnabled(true);
    		 currentPage.setText(""+pageCount);
    	 }

}
   public void getinitData(int pageCount,String scode){
   	    map=new HashMap();
	    String page;
 	    page=String.valueOf(pageCount);
	    map.put("pageNo",page);
	    map.put("limit",LIMIT);
	    if(!scode.equals("")&&scode!=null){
	    	map.put("action","selectPendingWorkItem");
	    	map.put("sAppSystemCode", scode);
	    }
	    else{
	    	map.put("action","selectPhoneWork");
	    }
        checkButton();
   }
    /**
     * 监听首页按钮
     */
   public OnClickListener firstListener = new OnClickListener(){
            @Override
            public void onClick(View view) {
            	    pageCount=1;
            	    getinitData(pageCount,code);
            }
            
    };

    /**
     * 监听上一页按钮
     */
    public OnClickListener beforeListener = new OnClickListener(){
            @Override
            public void onClick(View view) {
            	        pageCount--;
            	        if(pageCount>=1){
            	        	getinitData(pageCount,code);
            	        }else{
            	        	checkButton();
            	        }
            	        
            }
            
    };
    /**
     * 监听下页按钮
     */
    public OnClickListener nextListener = new OnClickListener(){
            @Override
            public void onClick(View view) {
            	        pageCount++;
            	        if(pageCount<=totalPage){
            	        	getinitData(pageCount,code);
            	        }else{
            	        	checkButton();
            	        }
            	        
            }
    };
    /**
     * 监听尾页按钮
     */
    public OnClickListener afterListener = new OnClickListener(){
            @Override
            public void onClick(View view) {
            	    pageCount=totalPage;
            	    getinitData(pageCount,code);
            }
            
    };
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() < 120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
		    pageCount--;
	        if(pageCount>=1){
	        	getinitData(pageCount,code);
	        }else{
	        	checkButton();
	        }			
	        this.flipper.showPrevious();
			return true;
		} else if (e1.getX() - e2.getX() > -120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			 pageCount++;
 	        if(pageCount<=totalPage){
 	        	getinitData(pageCount,code);
 	        }else{
 	        	checkButton();
 	        }
			this.flipper.showNext();
			return true;
		}
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//flipper.addView(addTextView("step 2"));
		//flipper.addView(addTextView("step 3"));
		boolean flag = false;
		try {
			flag = this.detector.onTouchEvent(event);
		} catch(Exception e){}
		return flag;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
