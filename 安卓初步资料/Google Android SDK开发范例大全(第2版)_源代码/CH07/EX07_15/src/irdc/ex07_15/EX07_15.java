package irdc.ex07_15;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
/* 延含学习 */
//import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;

/* 引用Camera类 */
import android.hardware.Camera;

/* 引用PictureCallback为取得拍照后的事件 */
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

/* 使Activity实现SurfaceHolder.Callback */
public class EX07_15 extends Activity implements SurfaceHolder.Callback
{
  /* 建立私有Camera对象 */
  private Camera mCamera01;
  private Button mButton01, mButton02, mButton03;
  
  /* 当成review照下来的相片之用 */
  private ImageView mImageView01;
  private TextView mTextView01;
  private static String TAG = "HIPPO_DEBUG";
  private SurfaceView mSurfaceView01;
  private SurfaceHolder mSurfaceHolder01;
  //private int intScreenX, intScreenY;
  
  /* 默认相机预览模式为false */
  private boolean bIfPreview = false;
  
  /* 将照下来的图片保存在此 */
  private String strCaptureFilePath = "/sdcard/camera_snap.jpg";
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    /* 使应用程序全屏运行，不使用title bar */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.main);
    
    /* 判断存储卡是否存在 */
    if(!checkSDCard())
    {
      /* 提醒User未安装存储卡 */
      mMakeTextToast
      (
        getResources().getText(R.string.str_err_nosd).toString(),
        true
      );
    }
    
    /* 取得屏幕解析像素 */
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    //intScreenX = dm.widthPixels;
    //intScreenY = dm.heightPixels;
    //Log.i(TAG, Integer.toString(intScreenX));
    
    /* 延含学习 */
    //import android.content.pm.ActivityInfo;
    //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    
    mTextView01 = (TextView) findViewById(R.id.myTextView1);
    mImageView01 = (ImageView) findViewById(R.id.myImageView1);
    
    /* 以SurfaceView作为相机Preview之用 */
    mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
    
    /* 绑定SurfaceView，取得SurfaceHolder对象 */
    mSurfaceHolder01 = mSurfaceView01.getHolder();
    
    /* Activity必须实现SurfaceHolder.Callback */
    mSurfaceHolder01.addCallback(EX07_15.this);
    
    /* 额外的设置预览大小设定，在此不使用 */
    //mSurfaceHolder01.setFixedSize(320, 240);
    
    /*
     * 以SURFACE_TYPE_PUSH_BUFFERS(3)
     * 作为SurfaceHolder显示类型 
     * */
    mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton03 = (Button)findViewById(R.id.myButton3);
    
    /* 开启相机及Preview */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        
        /* 自定义初始化开启相机函数 */
        initCamera();
      }
    });
    
    /* 停止Preview及相机 */
    mButton02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* 自定义重置相机，并关闭相机预览函数 */
        resetCamera();
      }
    });
    
    /* 拍照 */
    mButton03.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* 当存储卡存在才允许拍照，保存临时图像文件 */
        if(checkSDCard())
        {
          /* 自定义拍照函数 */
          takePicture();
        }
        else 
        {
          /* 存储卡不存在的显示提示 */
          mTextView01.setText
          (
            getResources().getText(R.string.str_err_nosd).toString()
          );
        }
      }
    });
  }
  
  /* 告订初始相机函数 */
  private void initCamera()
  {
    if(!bIfPreview)
    {
      /* 若相机非?预览模式，则开启相机 */
      try
      {
        /* 
         * The Heap 是应用程序?手机里执行所配置的空间
         * 当超过预设的16 MB（每几个应用程序）时就会导致 "Out of memory"的错误
         * 目前看来是Cupcake(AVD 1.5)?会发生 
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
        /* 建立Camera.Parameters对象 */
        Camera.Parameters parameters = mCamera01.getParameters();
        /* 设定相片格式为JPEG */
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
        
          /* ?2.0模拟器中，设置不支持的PreviewSize将造成Exception */
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
            /* ?2.0模拟器中，设置不支持的PictureSize将造成Exception */
            parameters.setPictureSize(512, 384);
            //parameters.setPictureSize(213, 350);
            /* 将Camera.Parameters设定予Camera */
            mCamera01.setParameters(parameters);
            /* setPreviewDisplay唯几的参数为SurfaceHolder */
            mCamera01.setPreviewDisplay(mSurfaceHolder01);
            /* 立即运行Preview */
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
  
  /* 拍照撷取影像 */ 
  private void takePicture() 
  {
    if (mCamera01 != null && bIfPreview) 
    {
      /* 调用takePicture()方法拍照 */
      mCamera01.takePicture(shutterCallback, rawCallback, jpegCallback);
    }
  }
  
  /* 相机重置 */
  private void resetCamera()
  {
    if (mCamera01 != null && bIfPreview)
    {
      mCamera01.stopPreview();
      
      /* 延含学习，释放Camera对象 */
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
      
      /* onPictureTaken传入的第几个参数即为相片的byte */
      Bitmap bm = BitmapFactory.decodeByteArray(_data, 0, _data.length); 
      
      /* 创建文件 */
      File myCaptureFile = new File(strCaptureFilePath);
      try
      {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        /* 采用压缩转档方法 */
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        
        /* 调用flush()方法，更新BufferStream */
        bos.flush();
        
        /* 结束OutputStream */
        bos.close();
        
        /* 将拍照下来且保存完毕的图文件，显示出来 */ 
        mImageView01.setImageBitmap(bm);
        
        /* 显示完图片，立即重置相机，并关闭预览 */
        resetCamera();
        
        /* 再重新启动相机继续预览 */
        initCamera();
      }
      catch (Exception e)
      {
        Log.e(TAG, e.getMessage());
        Log.e(TAG, e.toString());
      }
    }
  };
  
  /* 自定义文件函数 */
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
    /* 判断存储卡是否存在 */
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
    /* 当Surface不存在，就需要删除图片 */
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

