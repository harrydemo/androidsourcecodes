package hdw.mftest;

import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class MFtestActivity extends Activity {
    /** Called when the activity is first created. */
	private GLSurfaceView mGLView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLView = new MFtestSurfaceView(this);
        setContentView(mGLView);
    }
    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mGLView.onPause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mGLView.onResume();
	}
	class MFtestSurfaceView extends GLSurfaceView {
		private MFtestRenderer mRenderer;
		private float mPreviousX;
	    private float mPreviousY;//上一次的点
	    
	    private float mDownPreviousX;
	    private float mDownPreviousY;//上一次的Down点
	    
	   // private int oldDownX;
	   // private int 
	    private float theta;
	    private float fai;//球坐标
	    
        public MFtestSurfaceView(Context context){
            super(context);
            theta=(float)3.14159/4;
        	fai=(float)3.14159/4;
            // Set the Renderer for drawing on the GLSurfaceView
            mRenderer = new MFtestRenderer();
            setRenderer(mRenderer);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
        @Override 
        public boolean onTouchEvent(MotionEvent e) {
            // MotionEvent reports input details from the touch screen
            // and other input controls. In this case, you are only
            // interested in events where the touch position changed.

            float x = e.getX();
            float y = e.getY();
            switch (e.getAction()) {
                case MotionEvent.ACTION_MOVE://如果是 移动的话
        
                    float dx = x - mPreviousX;
                    float dy = y - mPreviousY;//x,y的变换值（不是0，就是1）

                    float te=theta;
                	theta=theta+dy/100;	//角度是弧度值
                	if (!(theta>0&&theta<3.14159))
                	{
                		theta=te;		//法线始终向上
                		
                	}
                	fai=fai+ dx/100;                                                                //x=rsinθcosφ
                	mRenderer.eyez=(float)(3*Math.sin(theta)*Math.cos(fai));    //y=rsinθsinφ
                	mRenderer.eyex=(float)(3*Math.sin(theta)*Math.sin(fai));     ////z=rcosθ     利用球坐标使观察点始终维持在半径为3的球上
                	mRenderer.eyey=(float)(3*Math.cos(theta));
                    requestRender();
                    //System.out.println(x+"      "+y);
                    break;
                case MotionEvent.ACTION_DOWN:
                	mDownPreviousX=x;
                	mDownPreviousY=y;
                	break;
                case MotionEvent.ACTION_UP:
                	if((((x-mDownPreviousX)>-5)&&((x-mDownPreviousX)<5))&&(((y-mDownPreviousY)>-5)&&((y-mDownPreviousY)<5)))//(((x-mDownPreviousX)>-3)||((x-mDownPreviousX)>-3))&&(((y-mDownPreviousY)>-3)||((y-mDownPreviousY)>-3))
                	{
                		 //System.out.println(x-mDownPreviousX);
                		if(      x     <  (mRenderer.width*(1-Math.sqrt(18)/6)/2)  )
                		{
                			//左
                			//System.out.println(x+"   左   "+y);
                			mRenderer.action = 1;
                			DrawAnimation();
                		}
                		else if(  x   >   mRenderer.width-(mRenderer.width*(1-Math.sqrt(18)/6)/2) )  
                		{
                			//右
                			//System.out.println(x+"   右   "+y);
                			mRenderer.action = 2;
                			DrawAnimation();
                		}
                		else if(y   <   mRenderer.height/2.0 - (1.5*mRenderer.width)/6){
                			//上
                			//System.out.println(x+"   上   "+y);
                			mRenderer.action = 3;
                			DrawAnimation();
                		}
                		else if(y   >   mRenderer.height/2.0 + (1.5*mRenderer.width)/6){
                			//下
                			//System.out.println(x+"   下   "+y);
                			mRenderer.action = 4;
                			DrawAnimation();
                		}
                		else{
                			mRenderer.action = 0;
                		}
                	}
                	break;
            }
           
            mPreviousX = x;
            mPreviousY = y;//保存这次点
            return true;
        }
        private void DrawAnimation(){
        	ChangeMoveByAction();//确定是哪个面旋转
        	
        	
        	mRenderer.angle=0;
        	while(mRenderer.angle<85){//绘制旋转效果
        		mRenderer.angle=mRenderer.angle+5;          		
        		try{
        			Thread.sleep(40);
        		}
        		catch(InterruptedException e){        			
        		}
        		requestRender();
        	}//绘制旋转效果
        	mRenderer.angle=0;
        	mRenderer.action = 0;
        	DrawColorChange();//改变颜色
        	requestRender();
        }
    	private void DrawColorChange(){
    		switch (mRenderer.move)
    		{
    		case 70:
    			LeftShift( 0*27+0*3, 0*27+6*3, 0*27+8*3, 0*27+2*3);
    			LeftShift( 0*27+1*3, 0*27+3*3, 0*27+7*3, 0*27+5*3);
    			
    			LeftShift( 1*27+6*3, 5*27+8*3, 4*27+2*3, 2*27+0*3);
    			LeftShift( 1*27+7*3, 5*27+5*3, 4*27+1*3, 2*27+3*3);
    			LeftShift( 1*27+8*3, 5*27+2*3, 4*27+0*3, 2*27+6*3);
    			break;
    		case 66:
    			LeftShift( 3*27+0*3, 3*27+6*3, 3*27+8*3, 3*27+2*3);
    			LeftShift( 3*27+1*3, 3*27+3*3, 3*27+7*3, 3*27+5*3);
    			
    			LeftShift( 1*27+0*3, 2*27+2*3, 4*27+8*3, 5*27+6*3);
    			LeftShift( 1*27+1*3, 2*27+5*3, 4*27+7*3, 5*27+3*3);
    			LeftShift( 1*27+2*3, 2*27+8*3, 4*27+6*3, 5*27+0*3);
    			break;
    		case 85:
    			LeftShift( 1*27+0*3, 1*27+6*3, 1*27+8*3, 1*27+2*3);//////////
    			LeftShift( 1*27+1*3, 1*27+3*3, 1*27+7*3, 1*27+5*3);

    			LeftShift( 0*27+0*3, 2*27+0*3, 3*27+0*3, 5*27+0*3);
    			LeftShift( 0*27+1*3, 2*27+1*3, 3*27+1*3, 5*27+1*3);
    			LeftShift( 0*27+2*3, 2*27+2*3, 3*27+2*3, 5*27+2*3);
    			break;
    		case 68:
    			LeftShift( 4*27+0*3, 4*27+2*3, 4*27+8*3, 4*27+6*3);
    			LeftShift( 4*27+1*3, 4*27+5*3, 4*27+7*3, 4*27+3*3);
    			
    			LeftShift( 0*27+6*3, 2*27+6*3, 3*27+6*3, 5*27+6*3);
    			LeftShift( 0*27+7*3, 2*27+7*3, 3*27+7*3, 5*27+7*3);
    			LeftShift( 0*27+8*3, 2*27+8*3, 3*27+8*3, 5*27+8*3);
    			break;			
    		case 82:
     			LeftShift( 2*27+0*3, 2*27+6*3, 2*27+8*3, 2*27+2*3);
     			LeftShift( 2*27+1*3, 2*27+3*3, 2*27+7*3, 2*27+5*3);
     			
     			LeftShift( 1*27+2*3, 0*27+2*3, 4*27+2*3, 3*27+6*3);
     			LeftShift( 1*27+5*3, 0*27+5*3, 4*27+5*3, 3*27+3*3);
     			LeftShift( 1*27+8*3, 0*27+8*3, 4*27+8*3, 3*27+0*3);
    			break;			
    		case 76:
    			LeftShift( 5*27+0*3, 5*27+6*3, 5*27+8*3, 5*27+2*3);
    			LeftShift( 5*27+1*3, 5*27+3*3, 5*27+7*3, 5*27+5*3);
    			
    			LeftShift( 1*27+0*3, 3*27+8*3, 4*27+0*3, 0*27+0*3);
    			LeftShift( 1*27+3*3, 3*27+5*3, 4*27+3*3, 0*27+3*3);
     			LeftShift( 1*27+6*3, 3*27+2*3, 4*27+6*3, 0*27+6*3);
    			break;
    		default:
    			break;
    		}
    	}
    	private void ChangeMoveByAction(){
    		//F	70		B 66		U 85		D 68		R 82		L76(这个注释恐怕只有我能看懂 - -！)
    		switch(mRenderer.action){
    		case 1:
    			if( (mRenderer.eyex > 0) && (mRenderer.eyez > 0) ){
    				mRenderer.move=76;
    			}
    			else if( (mRenderer.eyex > 0) && (mRenderer.eyez < 0) ){
    				mRenderer.move=70;
    			}
    			else if( (mRenderer.eyex < 0) && (mRenderer.eyez < 0) ){
    				mRenderer.move=82;
    			}
    			else if( (mRenderer.eyex < 0) && (mRenderer.eyez > 0) ){
    				mRenderer.move=66;
    			}
    			break;
    		case 2:
    			if( (mRenderer.eyex > 0) && (mRenderer.eyez > 0) ){
    				mRenderer.move=66;
    			}
    			else if( (mRenderer.eyex > 0) && (mRenderer.eyez < 0) ){
    				mRenderer.move=76;
    			}
    			else if( (mRenderer.eyex < 0) && (mRenderer.eyez < 0) ){
    				mRenderer.move=70;
    			}
    			else if( (mRenderer.eyex < 0) && (mRenderer.eyez > 0) ){
    				mRenderer.move=82;
    			}
    			break;
    		case 3:
    			mRenderer.move=85;
    			break;
    		case 4:
    			mRenderer.move=68;
    			break;
    		}
    	}
    	private void memcpy(int one,int two){
    		mRenderer.face_color[one]=mRenderer.face_color[two];
    		mRenderer.face_color[one+1]=mRenderer.face_color[two+1];
    		mRenderer.face_color[one+2]=mRenderer.face_color[two+2];
    	}
    	private void LeftShift(int a,int b,int c,int d){
    		// a<b<c<d 左 移
    		float temp1=mRenderer.face_color[a];
    		float temp2=mRenderer.face_color[a+1];
    		float temp3=mRenderer.face_color[a+2];
    		memcpy(a,b);
    		memcpy(b,c);
    		memcpy(c,d);
    	    mRenderer.face_color[d]= temp1;
    	    mRenderer.face_color[d+1]=temp2;
    	    mRenderer.face_color[d+2]=temp3;
    	}
    }
}