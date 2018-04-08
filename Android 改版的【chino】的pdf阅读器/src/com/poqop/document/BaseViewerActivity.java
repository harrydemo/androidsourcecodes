package com.poqop.document;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.poqop.R;
import com.poqop.document.events.CurrentPageListener;
import com.poqop.document.events.DecodingProgressListener;
import com.poqop.document.models.CurrentPageModel;
import com.poqop.document.models.DecodingProgressModel;
import com.poqop.document.models.ZoomModel;
import com.poqop.document.views.PageViewZoomControls;


public abstract class BaseViewerActivity extends Activity implements DecodingProgressListener, CurrentPageListener
{
    private static final int MENU_EXIT = 0;
    private static final int MENU_GOTO = 1;
    private static final int DIALOG_GOTO = 0;
    private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";
    private DecodeService decodeService;
    private DocumentView documentView;
    private ViewerPreferences viewerPreferences;
    private Toast pageNumberToast;
    private CurrentPageModel currentPageModel;

    private static final int MAX_VALUE = 3800;  //���÷Ŵ�������
	private static final float MULTIPLIER = 400.0f;
	public ZoomModel zoomModel;
	
    float lastX;
	float lastY;
	float magnify=1.0f;  //�Ŵ�
	float reduce=1.0f;   //��С
	LinearLayout zoom;   //�Զ���һ��linearlayout��������������Ű�ť
	
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initDecodeService();
        final ZoomModel zoomModel = new ZoomModel();
        final DecodingProgressModel progressModel = new DecodingProgressModel();
        progressModel.addEventListener(this);
        currentPageModel = new CurrentPageModel();
        currentPageModel.addEventListener(this);
        documentView = new DocumentView(this, zoomModel, progressModel, currentPageModel);
        zoomModel.addEventListener(documentView);
        documentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        decodeService.setContentResolver(getContentResolver());
        decodeService.setContainerView(documentView);
        documentView.setDecodeService(decodeService);
        decodeService.open(getIntent().getData());
        this.zoomModel = zoomModel;
        ImageView zoomIn,zoomOut;
        viewerPreferences = new ViewerPreferences(this);
        
        /*
         * �Ŵ��o�DƬ�M�зŴ�
         */
        zoom=new LinearLayout(this);
        zoom.setVisibility(View.GONE);
        zoom.setOrientation(LinearLayout.HORIZONTAL);
        
        zoom.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
        
        /*
         * �sС���o�DƬ�M�пsС
         */
        zoomOut=new ImageView(this);
		zoomOut.setImageResource(R.drawable.gallery_zoom_out_touch);
		zoomOut.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		zoomOut.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("��"+zoom.getWidth());
		        System.out.println("��:"+zoom.getHeight());
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
					setCurrentValue(getToureduceCurrentValues() - (event.getX() - lastX));					
					break;
				}
				return true;
			}
		});
        zoomIn=new ImageView(this);
		zoomIn.setImageResource(R.drawable.gallery_zoom_in_touch);
        zoomIn.setOnTouchListener(new View.OnTouchListener() {			
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction() & MotionEvent.ACTION_MASK) {
				case MotionEvent.ACTION_DOWN:
					lastX = event.getX();
					setCurrentValue(getToumagnifyCurrentValues() - (event.getX() - lastX));					
					break;
				}
				return true;
			}
		});
		zoom.addView(zoomOut); //��������ť��ӵ�һ��С������
		zoom.addView(zoomIn);
		
        final FrameLayout frameLayout = createMainContainer();
        frameLayout.addView(documentView);
        frameLayout.addView(zoom);  //��������ť��ӵ��ܲ�����ȥ 
        frameLayout.addView(createZoomControls(zoomModel));
        setFullScreen();
        setContentView(frameLayout);

        final SharedPreferences sharedPreferences = getSharedPreferences(DOCUMENT_VIEW_STATE_PREFERENCES, 0);
        documentView.goToPage(sharedPreferences.getInt(getIntent().getData().toString(), 0));
        documentView.showDocument();

        documentView.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				zoom.setVisibility(View.VISIBLE);
			}
		});
        
        viewerPreferences.addRecent(getIntent().getData());
    }
    
    
    
    // �Ŵ�
  	public float getToumagnifyCurrentValues() {
  		float mv = (zoomModel.getZoom() - 0.5f) * 400;
  		return mv;
  	}
  	
  	// ��С 
   	public float getToureduceCurrentValues() {
   		float mv = (zoomModel.getZoom() - 1.0f) * 200;
   		return mv;
   	}
  	
  	
  	public void setCurrentValue(float currentValue) {
 		if (currentValue < 0.0)
 			currentValue = 0.0f;
 		if (currentValue > MAX_VALUE)
 			currentValue = MAX_VALUE;
 		final float zoom = 1.0f + currentValue / MULTIPLIER;
 		zoomModel.setZoom(zoom);
 	}
  	

    public void decodingProgressChanged(final int currentlyDecoding)
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                getWindow().setFeatureInt(Window.FEATURE_INDETERMINATE_PROGRESS, currentlyDecoding == 0 ? 10000 : currentlyDecoding);
            }
        });
    }

    public void currentPageChanged(int pageIndex)
    {
        final String pageText = (pageIndex + 1) + "/" + decodeService.getPageCount();
        if (pageNumberToast != null)
        {
            pageNumberToast.setText(pageText);
        }
        else
        {
            pageNumberToast = Toast.makeText(this, pageText, 300);
        }
        pageNumberToast.setGravity(Gravity.TOP | Gravity.LEFT,0,0);
        pageNumberToast.show();
        saveCurrentPage();
    }

    private void setWindowTitle()
    {
        final String name = getIntent().getData().getLastPathSegment();
        getWindow().setTitle(name);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        setWindowTitle();
    }

    /*
     * ����ȫ��
     */
    private void setFullScreen()
    {
        if (viewerPreferences.isFullScreen())
        {
    //        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    //        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else
        {
            getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);  //�ڱ������ϼ�����½�����
        }
    }

    /*
     * ������������λ��
     */
    private PageViewZoomControls createZoomControls(ZoomModel zoomModel)
    {
        final PageViewZoomControls controls = new PageViewZoomControls(this, zoomModel);
        controls.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        zoomModel.addEventListener(controls);
        return controls;
    }

    private FrameLayout createMainContainer()
    {
        return new FrameLayout(this);
    }

    private void initDecodeService()
    {
        if (decodeService == null)
        {
            decodeService = createDecodeService();
        }
    }

    protected abstract DecodeService createDecodeService();

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        decodeService.recycle();
        decodeService = null;
        super.onDestroy();
    }

    private void saveCurrentPage()
    {
        final SharedPreferences sharedPreferences = getSharedPreferences(DOCUMENT_VIEW_STATE_PREFERENCES, 0);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(getIntent().getData().toString(), documentView.getCurrentPage());
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, MENU_EXIT, 0, "�˳�");
        menu.add(0, MENU_GOTO, 0, "��ת");
//        final MenuItem menuItem = menu.add(0, MENU_FULL_SCREEN, 0, "Full screen").setCheckable(true).setChecked(viewerPreferences.isFullScreen());
//        setFullScreenMenuItemText(menuItem);
        return true;
    }

    private void setFullScreenMenuItemText(MenuItem menuItem)
    {
        menuItem.setTitle("Full screen " + (menuItem.isChecked() ? "on" : "off"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case MENU_EXIT:
                System.exit(0);
                return true;
            case MENU_GOTO:
                showDialog(DIALOG_GOTO);
                return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
            case DIALOG_GOTO:
                return new GoToPageDialog(this, documentView, decodeService);
        }
        return null;
    }
}
