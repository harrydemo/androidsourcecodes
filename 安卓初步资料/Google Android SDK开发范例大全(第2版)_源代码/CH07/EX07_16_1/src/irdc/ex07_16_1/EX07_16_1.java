package irdc.ex07_16_1; 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity; 
import android.content.pm.ActivityInfo; 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat; 
import android.hardware.Camera; 
import android.hardware.Camera.PictureCallback; 
import android.hardware.Camera.ShutterCallback; 
import android.os.Bundle; 
import android.os.Environment;
import android.view.SurfaceHolder; 
import android.view.SurfaceView; 
import android.view.View; 
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button; 
import android.widget.Toast;

public class EX07_16_1 extends Activity implements SurfaceHolder.Callback 
{
  private Camera mCamera; 
  private Button mButton,mButton1,mButton2;
  private SurfaceView mSurfaceView; 
  private SurfaceHolder holder; 
  private AutoFocusCallback mAutoFocusCallback = 
                            new AutoFocusCallback(); 
  private String path="MyPhoto";
  private Bitmap bmp;
  private int cnt=1;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /* ����״̬�� */
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    /* ���ر����� */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    /* �趨��Ļ��ʾΪ���� */
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    
    setContentView(R.layout.main);
    /* SurfaceHolder�趨 */ 
    mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView); 
    holder = mSurfaceView.getHolder();
    holder.addCallback(EX07_16_1.this); 
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    /* Button��ʼ�� */
    mButton = (Button)findViewById(R.id.myButton);
    mButton1 = (Button)findViewById(R.id.myButton1);  
    mButton2 = (Button)findViewById(R.id.myButton2);  
    /* ����Button���¼����� */
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        /* �涯�Խ������� */
        mCamera.autoFocus(mAutoFocusCallback);
      }
    });
    /* ?�nButton���¼����� */
    mButton1.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        /* �����ļ� */
        if(bmp!=null)
        {
          /* ���SDCard�Ƿ���� */ 
          if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
          { 
            /* SD�������ڣ���ʾToast��Ϣ */
            Toast.makeText(EX07_16_1.this,"SD��������!�޷�������Ƭ",
                           Toast.LENGTH_LONG).show();
          }
          else
          {
            try
            {
              /* �ļ������ھʹ��� */
              File f=new File
              (
                Environment.getExternalStorageDirectory(),path
              );
              
              if(!f.exists())
              {
                f.mkdir();
              }
              /* ������Ƭ�ļ� */
              File n=new File(f,cnt+".jpg");
              FileOutputStream bos = 
                new FileOutputStream(n.getAbsolutePath());
              /* �ļ�ת�� */
              bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
              /* ����flush()����������BufferStream */
              bos.flush();
              /* ����OutputStream */
              bos.close();
              Toast.makeText(EX07_16_1.this,cnt+".jpg����ɹ�!",
                  Toast.LENGTH_LONG).show();
              cnt++;
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
        }
        
        mButton.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
        /* �����趨Camera */
        stopCamera(); 
        initCamera();
      } 
    }); 
    /* ����Button���¼����� */
    mButton2.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        mButton.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
        /* �����趨Camera */
        stopCamera(); 
        initCamera();
      } 
    }); 
  }
  
  @Override 
  public void surfaceCreated(SurfaceHolder surfaceholder) 
  { 
    try
    {
      /* ������� */
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
    /* �����ʼ�� */
    initCamera();
  } 

  @Override 
  public void surfaceDestroyed(SurfaceHolder surfaceholder) 
  { 
    stopCamera();
    mCamera.release();
    mCamera = null;
  }

  /* ���յ�method */
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
      /* ��أ����˲����?����ĳ��� */
    }  
  };  
    
  private PictureCallback rawCallback = new PictureCallback()  
  {  
    public void onPictureTaken(byte[] _data, Camera _camera)  
    {  
      /* Ҫ����raw data?д?�� */
    }  
  };  

  private PictureCallback jpegCallback = new PictureCallback()  
  { 
    public void onPictureTaken(byte[] _data, Camera _camera) 
    { 
      /* ȡ������ */
      try 
      { 
        /* �趨Button?���� */
        mButton.setVisibility(View.GONE);
        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        /* ȡ������Bitmap���� */
        bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length); 
      } 
      catch (Exception e) 
      { 
        e.printStackTrace(); 
      } 
    } 
  };
  
  /* �涨��class AutoFocusCallback */
  public final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback 
  { 
    public void onAutoFocus(boolean focused, Camera camera) 
    { 
      /* �Ե��������� */
      if (focused) 
      { 
        takePicture();
      } 
    } 
  };
  
  /* �����ʼ����method */
  private void initCamera() 
  { 
    if (mCamera != null) 
    {
      try 
      { 
        Camera.Parameters parameters = mCamera.getParameters(); 
        /* �趨��Ƭ��СΪ1024*768��
                             ��ʽΪJPG */
        parameters.setPictureFormat(PixelFormat.JPEG); 
        parameters.setPictureSize(1024,768);
        mCamera.setParameters(parameters); 
        /* ����Ԥ������ */
        mCamera.startPreview(); 
      } 
      catch (Exception e) 
      { 
          e.printStackTrace(); 
      } 
    } 
  } 

  /* ֹͣ�����method */ 
  private void stopCamera() 
  { 
    if (mCamera != null) 
    { 
      try 
      { 
        /* ֹͣԤ�� */
        mCamera.stopPreview(); 
      } 
      catch(Exception e) 
      { 
        e.printStackTrace(); 
      } 
    } 
  }
}

