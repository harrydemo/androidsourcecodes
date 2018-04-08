package com.renzh.earthtest;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
public class GLRender implements Renderer
{
	Sphere mEarthSphere;
	GLTexture mEarthTex;
	Context mContext;
	private float roatX=0;
	private float roatY=0;
	private void printMsg(String str){
		System.out.println("GLRender--------------->"+str);
	}
	public float getRoatX() {
		return roatX;
	}

	public void setRoatX(float roatX) {
		roatX%=360;
		this.roatX = roatX;
		mEarthSphere.setRoatX(this.roatX);
	}

	public float getRoatY() {
		return roatY;
		
	}

	public void setRoatY(float roatY) {
		roatY%=360;
		this.roatY = roatY;
		mEarthSphere.setRoatY(this.roatY);
	}
	//在原来的角度基础上动态旋转到指定的角度rox。degree是每次变化的角度。
    public void toRoatX(final float rox,final float degree){
    	int what=1;
    	float dx=rox-roatX;
    	//下列判断是为了以更有效的途径最快的旋转到指定角度。
        if(dx>0){
        	 if(dx>180)
        		 what=-1;
        }else{
        	what=-1;
        	if(dx<-180)
        		what=1;
        }
    	
    	final Handler h=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				setRoatX(roatX+degree*msg.what);//每次改变degree度
				 if(Math.abs((rox-roatX)%180)<=degree){
		    		setRoatX(rox) ;
		       	 }else
				sendEmptyMessageDelayed(msg.what, 25); 
			}
    	};
        h.sendEmptyMessage(what);	
	   	
    }
  //在原来的角度基础上动态旋转到指定的角度rox。degree是每次变化的角度。
    public void toRoatY(final float roy,final float degree){
    	int what=1;
    	float dy=roy-roatY;
    	//下列判断是为了以更有效的途径最快的旋转到指定角度。
        if(dy>0){
        	 if(dy>180)
        		 what=-1;
        }else{
        	what=-1;
        	if(dy<-180)
        		what=1;
        }
    	
    	final Handler h=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				setRoatY(roatY+degree*msg.what);//每次改变degree度
				 if(Math.abs((roy-roatY)%180)<=degree){
		    		setRoatY(roy) ;
		       	 }else
				sendEmptyMessageDelayed(msg.what, 25); 
			}
    	};
        h.sendEmptyMessage(what);	
	   	
    }
	
	
	
	public GLRender(Context context){
		 mContext = context;
		//第一个参数控制球大小，它最终会控制每个点到原点的长度，后面两个参数越大越圆，后面两个参数相当把球体分成多少行和列进行切割。
		 mEarthSphere = new Sphere(1.8f,30,30); 
	     mEarthTex = new GLTexture(context, R.raw.earth, false);
	}
	
	public void onDrawFrame(GL10 gl)
	{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();		
		gl.glTranslatef(0.0f, 0.0f, -10);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		

		mEarthSphere.draw((GL11)gl, mEarthTex, null, 0,0);//

	    gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	    gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	    gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		float ratio = (float) width / height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1, 1, 5, 20);
		gl.glMatrixMode(GL10.GL_MODELVIEW);	
		gl.glLoadIdentity();	
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config)
	{
		gl.glClearColor(0, 0, 0, 0);
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glClearDepthf(1.0f);
		gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_TEXTURE_2D);
	}
}

