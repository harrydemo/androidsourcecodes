package com.example.android.livewallpapertouch;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class TouchLiveWallpaper extends WallpaperService {
	//����
    @Override
    public void onCreate() {
        super.onCreate();
    }
    //����
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //ʵ��������
    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine(getResources());
    }
    //WallpaperEngine����
    public class WallpaperEngine extends Engine {
        //private final Handler handler=new Handler();        
        private Bitmap image; //Image
        private Bitmap image01, image02, image03, image04; 
        private Bitmap image05, image06, image07, image08;
        private Bitmap image09, image10, image11, image12;
        private Bitmap image13, image14, image15, image16;
        private Bitmap image17, image18, image19, image20;
        private Bitmap[] imageIds;
        //public Integer[] photoIds; //Photo Array
        private int    px=0;  //Flag for switch
        private int    photoMax;  //Flag for switch
        private boolean visible; //��ʾ״̬
        private int     width;   //��
        private int     height;  //��
        //ʵ��WallpaperEngine()
        public WallpaperEngine(Resources r) {
        	Bitmap[] imageIds = {
        			image01, image02, image03, image04,
        			image05, image06, image07, image08,
        			image09, image10, image11, image12,
        			image13, image14, image15, image16,
        			image17, image18, image19, image20,
        	};	   	
        	Integer[] photoIds  = {
            		R.drawable.sakura01, R.drawable.sakura02,
            		R.drawable.sakura03, R.drawable.sakura04,
            		R.drawable.sakura05, R.drawable.sakura06,
            		R.drawable.sakura07, R.drawable.sakura08,
            		R.drawable.fire01, R.drawable.fire02,
            };
        	setImageIds(imageIds);
        	px=0;
        	photoMax = photoIds.length;
        	for (int i=0 ; i < photoMax ; i++) {
        		imageIds[i]=BitmapFactory.decodeResource(r,photoIds[i]);
        	}
        }
        //��������
        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            // Enable touch
            setTouchEventsEnabled(true);  
        }   
        //���Surface
        @Override
        public void onSurfaceChanged(SurfaceHolder holder,
            int format,int width,int height) {
            super.onSurfaceChanged(holder,format,width,height);
            this.width =width;
            this.height=height;
            drawFrame();
        }
        //����Surface
        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
        }
        //����Surface
        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            visible=false;
            //handler.removeCallbacks(drawThread);
        }
        //����ɼ�/���ɼ�״̬
        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible=visible;
            if (visible) {
                drawFrame();
            }
        }
        //�����ֽλ��
        @Override
        public void onOffsetsChanged(float xOffset,float yOffset,
            float xStep,float yStep,int xPixels,int yPixels) {
            drawFrame();
        }
        //ÿһ�δ��ػ���һ����Ƭ
        @Override
        public void onTouchEvent(MotionEvent event) {
          super.onTouchEvent(event);  
          if (event.getAction() == MotionEvent.ACTION_UP) {
            drawFrame();
          }
        }  
        //Frame���
        private void drawFrame() {
        	Bitmap[] imagexx = getImageIds();
        	//��������
            SurfaceHolder holder=getSurfaceHolder();
            Canvas c=holder.lockCanvas();
            //���
            c.drawColor(Color.BLUE);
            image = imagexx[px];
            px++;
            if (px >= photoMax ) {
            	px = 0;
            }
            c.drawBitmap(image, (width-image.getWidth())/2, (height-image.getHeight())/2, null);
            //��������
            holder.unlockCanvasAndPost(c);
        }
		public void setImageIds(Bitmap[] imageIds) {
			this.imageIds = imageIds;
		}
		public Bitmap[] getImageIds() {
			return imageIds;
		}
    }
}