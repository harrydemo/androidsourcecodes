package grimbo.android.demo.slidingmenu;

import android.view.View;

public class SizeCallbackForMenu implements SizeCallback {
	 
	        int btnWidth;
	        View btnSlide;

	        public SizeCallbackForMenu(View btnSlide) {
	            super();
	            this.btnSlide = btnSlide;
	        }

	        @Override
	        public void onGlobalLayout() {
	            btnWidth = btnSlide.getMeasuredWidth();
	            System.out.println("btnWidth=" + btnWidth);
	        }

	        @Override
	        public void getViewSize(int idx, int w, int h, int[] dims) {
	            dims[0] = w;
	            dims[1] = h;
	            final int menuIdx = 0;
	            if (idx == menuIdx) {
//	                dims[0] = w - btnWidth;
	            	dims[0]=410;
	            }
	        }
	 

}
