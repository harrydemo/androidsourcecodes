package irdc.ex07_16; 

import java.io.IOException;
import android.app.Activity; 
import android.content.pm.ActivityInfo; 
import android.graphics.PixelFormat; 
import android.hardware.Camera; 
import android.hardware.Camera.PictureCallback; 
import android.hardware.Camera.ShutterCallback; 
import android.os.Bundle; 
import android.view.SurfaceHolder; 
import android.view.SurfaceView; 
import android.view.View; 
import android.view.Window; 
import android.view.WindowManager;
import android.widget.Button; 

public class EX07_16 extends Activity implements SurfaceHolder.Callback 
{
  private Camera mCamera; 
  private Button mButton; 
  private SurfaceView mSurfaceView; 
  private SurfaceHolder holder; 
  private AutoFocusCallback mAutoFocusCallback = 
                            new AutoFocusCallback(); 
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /* 隐藏状态栏 */
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    /* 隐藏标题栏 */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    /* 设定屏幕显示为横向 */
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    
    setContentView(R.layout.main);
    /* SurfaceHolder设置 */ 
    mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView); 
    holder = mSurfaceView.getHolder();
    holder.addCallback(EX07_16.this); 
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    /* 设置拍照Button的OnClick事件处理 */
    mButton = (Button)findViewById(R.id.myButton);  
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        /* 告动对焦后拍照 */
        mCamera.autoFocus(mAutoFocusCallback);
      }
    });
  }
  
  @Override 
  public void surfaceCreated(SurfaceHolder surfaceholder) 
  { 
    try
    {
      /* 打开相机， */
      mCamera = Camera.open();
      mCamera.setPreviewDisplay(holder);
    }
    catch (IOException exception)
    {
      mCamera.release();
      mCamera = null;
    }     
  } 
  
  @Override 
  public void surfaceChanged(SurfaceHolder surfaceholder,
                             int format,int w,int h) 
  {
    /* 相机初始化 */
    initCamera();
  } 

  @Override 
  public void surfaceDestroyed(SurfaceHolder surfaceholder) 
  { 
    stopCamera();
    mCamera.release();
    mCamera = null;
  }

  /* 拍照的method */
  private void takePicture()  
  { 
    if (mCamera != null)  
    { 
      mCamera.takePicture(shutterCallback, rawCallback,jpegCallback); 
    } 
  }

  private ShutterCallback shutterCallback = new ShutterCallback()  
  {  
    public void onShutter()  
    {  
      /* 按下快门瞬间会调用这里的程序 */
    }  
  };  
    
  private PictureCallback rawCallback = new PictureCallback()  
  {  
    public void onPictureTaken(byte[] _data, Camera _camera)  
    {  
      /* 要处理raw data?写?否 */
    }  
  };  

  private PictureCallback jpegCallback = new PictureCallback()  
  { 
    public void onPictureTaken(byte[] _data, Camera _camera) 
    { 
      /* 取得相片 */
      try 
      { 
        /* 让相片显示3秒后圳重设相机 */
        Thread.sleep(3000);
        /* 重新设定Camera */
        stopCamera(); 
        initCamera(); 
      } 
      catch (Exception e) 
      { 
        e.printStackTrace(); 
      } 
    } 
  };
  
  /* 告定义class AutoFocusCallback */
  public final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback 
  { 
    public void onAutoFocus(boolean focused, Camera camera) 
    { 
      /* 对到焦点拍照 */
      if (focused) 
      { 
        takePicture();
      } 
    } 
  };
  
  /* 相机初始化的method */
  private void initCamera() 
  { 
    if (mCamera != null) 
    {
      try 
      { 
        Camera.Parameters parameters = mCamera.getParameters(); 
        /* 设定相片大小为1024*768，
                             格式为JPG */
        parameters.setPictureFormat(PixelFormat.JPEG); 
        parameters.setPictureSize(1024,768);
        mCamera.setParameters(parameters); 
        /* 打开预览画面 */
        mCamera.startPreview(); 
      } 
      catch (Exception e) 
      { 
          e.printStackTrace(); 
      } 
    } 
  } 

  /* 停止相机的method */ 
  private void stopCamera() 
  { 
    if (mCamera != null) 
    { 
      try 
      { 
        /* 停止预览 */
        mCamera.stopPreview(); 
      } 
      catch(Exception e) 
      { 
        e.printStackTrace(); 
      } 
    } 
  }
}

