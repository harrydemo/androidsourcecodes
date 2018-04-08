package irdc.ex07_15;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
/* �Ӻ�ѧϰ */
//import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;

/* ����Camera�� */
import android.hardware.Camera;

/* ����PictureCallbackΪȡ�����պ���¼� */
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/* ʹActivityʵ��SurfaceHolder.Callback */
public class EX07_15 extends Activity implements SurfaceHolder.Callback
{
  /* ����˽��Camera���� */
  private Camera mCamera01;
  private Button mButton01, mButton02, mButton03;
  
  /* ����review����������Ƭ֮�� */
  private ImageView mImageView01;
  private TextView mTextView01;
  private static String TAG = "HIPPO_DEBUG";
  private SurfaceView mSurfaceView01;
  private SurfaceHolder mSurfaceHolder01;
  //private int intScreenX, intScreenY;
  
  /* Ĭ�����Ԥ��ģʽΪfalse */
  private boolean bIfPreview = false;
  
  /* ����������ͼƬ�����ڴ� */
  private String strCaptureFilePath = "/sdcard/camera_snap.jpg";
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    /* ʹӦ�ó���ȫ�����У���ʹ��title bar */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);
    
    /* �жϴ洢���Ƿ���� */
    if(!checkSDCard())
    {
      /* ����Userδ��װ�洢�� */
      mMakeTextToast
      (
        getResources().getText(R.string.str_err_nosd).toString(),
        true
      );
    }
    
    /* ȡ����Ļ�������� */
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    //intScreenX = dm.widthPixels;
    //intScreenY = dm.heightPixels;
    //Log.i(TAG, Integer.toString(intScreenX));
    
    /* �Ӻ�ѧϰ */
    //import android.content.pm.ActivityInfo;
    //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    
    mTextView01 = (TextView) findViewById(R.id.myTextView1);
    mImageView01 = (ImageView) findViewById(R.id.myImageView1);
    
    /* ��SurfaceView��Ϊ���Preview֮�� */
    mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
    
    /* ��SurfaceView��ȡ��SurfaceHolder���� */
    mSurfaceHolder01 = mSurfaceView01.getHolder();
    
    /* Activity����ʵ��SurfaceHolder.Callback */
    mSurfaceHolder01.addCallback(EX07_15.this);
    
    /* ���������Ԥ����С�趨���ڴ˲�ʹ�� */
    //mSurfaceHolder01.setFixedSize(320, 240);
    
    /*
     * ��SURFACE_TYPE_PUSH_BUFFERS(3)
     * ��ΪSurfaceHolder��ʾ���� 
     * */
    mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton03 = (Button)findViewById(R.id.myButton3);
    
    /* ���������Preview */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* �Զ����ʼ������������� */
        initCamera();
      }
    });
    
    /* ֹͣPreview����� */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* �Զ���������������ر����Ԥ������ */
        resetCamera();
      }
    });
    
    /* ���� */
    mButton03.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* ���洢�����ڲ��������գ�������ʱͼ���ļ� */
        if(checkSDCard())
        {
          /* �Զ������պ��� */
          takePicture();
        }
        else 
        {
          /* �洢�������ڵ���ʾ��ʾ */
          mTextView01.setText
          (
            getResources().getText(R.string.str_err_nosd).toString()
          );
        }
      }
    });
  }
  
  /* �涩��ʼ������� */
  private void initCamera()
  {
    if(!bIfPreview)
    {
      /* �������?Ԥ��ģʽ��������� */
      try
      {
        /* 
         * The Heap ��Ӧ�ó���?�ֻ���ִ�������õĿռ�
         * ������Ԥ���16 MB��ÿ����Ӧ�ó���ʱ�ͻᵼ�� "Out of memory"�Ĵ���
         * Ŀǰ������Cupcake(AVD 1.5)?�ᷢ�� 
         * Connect E from ICameraClient 0x....
         * new client (0x...) sttempting to connect - rejected
         * */
        mCamera01 = Camera.open();
      }
      catch(Exception e)
      {
        Log.e(TAG, e.getMessage());
      }
    }
    
    if (mCamera01 != null && !bIfPreview)
    {
      try
      {
        Log.i(TAG, "inside the camera");
        mCamera01.setPreviewDisplay(mSurfaceHolder01);
        /* ����Camera.Parameters���� */
        Camera.Parameters parameters = mCamera01.getParameters();
        /* �趨��Ƭ��ʽΪJPEG */
        parameters.setPictureFormat(PixelFormat.JPEG);
        //parameters.setPreviewSize(w, h);
        List<Camera.Size> s=parameters.getSupportedPreviewSizes();
        
        try
        {
          if(s!=null)
          {
            for(int i=0;i<s.size();i++)
            {
              Log.i(TAG,""+(((Camera.Size)s.get(i)).height)+"/"+(((Camera.Size)s.get(i)).width));
            }
          }
          parameters.setPreviewSize(320, 240);
          //parameters.setPreviewSize(176, 144);
        
          /* ?2.0ģ�����У����ò�֧�ֵ�PreviewSize�����Exception */
          s=parameters.getSupportedPictureSizes();
          try
          {
            if(s!=null)
            {
              for(int i=0;i<s.size();i++)
              {
                Log.i(TAG,""+(((Camera.Size)s.get(i)).height)+"/"+(((Camera.Size)s.get(i)).width));
              }
            }
            /* ?2.0ģ�����У����ò�֧�ֵ�PictureSize�����Exception */
            parameters.setPictureSize(512, 384);
            //parameters.setPictureSize(213, 350);
            /* ��Camera.Parameters�趨��Camera */
            mCamera01.setParameters(parameters);
            /* setPreviewDisplayΨ���Ĳ���ΪSurfaceHolder */
            mCamera01.setPreviewDisplay(mSurfaceHolder01);
            /* ��������Preview */
            mCamera01.startPreview();
            
            bIfPreview = true;
            Log.i(TAG, "startPreview");
          }
          catch (Exception e)
          {
            Log.i(TAG, e.toString());
            e.printStackTrace();
          }   
        }
        catch (Exception e)
        {
          Toast.makeText
          (
            EX07_15.this,
            "initCamera error.",
            Toast.LENGTH_LONG
          ).show();
          e.printStackTrace();
        }
      }
      catch (IOException e)
      {
        // TODO Auto-generated catch block
        mCamera01.release();
        mCamera01 = null;
        Log.i(TAG, e.toString());
        e.printStackTrace();
      }
    }
  }
  
  /* ����ߢȡӰ�� */ 
  private void takePicture() 
  {
    if (mCamera01 != null && bIfPreview) 
    {
      /* ����takePicture()�������� */
      mCamera01.takePicture(shutterCallback, rawCallback, jpegCallback);
    }
  }
  
  /* ������� */
  private void resetCamera()
  {
    if (mCamera01 != null && bIfPreview)
    {
      mCamera01.stopPreview();
      
      /* �Ӻ�ѧϰ���ͷ�Camera���� */
      //mCamera01.release();
      //mCamera01 = null;
      
      Log.i(TAG, "stopPreview");
      bIfPreview = false;
    }
  }
   
  private ShutterCallback shutterCallback = new ShutterCallback() 
  { 
    public void onShutter() 
    { 
      // Shutter has closed 
    } 
  }; 
   
  private PictureCallback rawCallback = new PictureCallback() 
  { 
    public void onPictureTaken(byte[] _data, Camera _camera) 
    { 
      // TODO Handle RAW image data 
    } 
  }; 

  private PictureCallback jpegCallback = new PictureCallback() 
  {
    public void onPictureTaken(byte[] _data, Camera _camera)
    {
      // TODO Handle JPEG image data
      
      /* onPictureTaken����ĵڼ���������Ϊ��Ƭ��byte */
      Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length); 
      
      /* �����ļ� */
      File myCaptureFile = new File(strCaptureFilePath);
      try
      {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        /* ����ѹ��ת������ */
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        
        /* ����flush()����������BufferStream */
        bos.flush();
        
        /* ����OutputStream */
        bos.close();
        
        /* �����������ұ�����ϵ�ͼ�ļ�����ʾ���� */ 
        mImageView01.setImageBitmap(bm);
        
        /* ��ʾ��ͼƬ������������������ر�Ԥ�� */
        resetCamera();
        
        /* �����������������Ԥ�� */
        initCamera();
      }
      catch (Exception e)
      {
        Log.e(TAG, e.getMessage());
        Log.e(TAG, e.toString());
      }
    }
  };
  
  /* �Զ����ļ����� */
  private void delFile(String strFileName)
  {
    try
    {
      File myFile = new File(strFileName);
      if(myFile.exists())
      {
        myFile.delete();
      }
    }
    catch (Exception e)
    {
      Log.e(TAG, e.toString());
      e.printStackTrace();
    }
  }
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX07_15.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX07_15.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  private boolean checkSDCard()
  {
    /* �жϴ洢���Ƿ���� */
    if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  @Override
  public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed1");
  }
  
  @Override
  public void surfaceCreated(SurfaceHolder surfaceholder)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed2");
  }
  
  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceholder)
  {
    // TODO Auto-generated method stub
    /* ��Surface�����ڣ�����Ҫɾ��ͼƬ */
    try
    {
      delFile(strCaptureFilePath);
      mCamera01.stopPreview();
      mCamera01.release();
      mCamera01 = null;
      Log.i(TAG, "Surface Destroyed");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    try
    {
      resetCamera();
      mCamera01.release();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    super.onPause();
  }
  
}

