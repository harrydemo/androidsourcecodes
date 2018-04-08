package irdc.ex10_05;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class EX10_05 extends Activity implements SurfaceHolder.Callback
{
  /* 建立私有Camera对象 */
  private Camera mCamera01;
  private Button mButton01, mButton02, mButton03;
  
  /* 作为review照下来的相片之用 */
  private ImageView mImageView01;
  private String TAG = "HIPPO_DEBUG";
  private SurfaceView mSurfaceView01;
  private SurfaceHolder mSurfaceHolder01;
  
  /* 预设相机预览模式为false */
  private boolean bIfPreview = false;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    /* 使应用程序全屏幕执行，不使用title bar */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    
    setContentView(R.layout.main);
    DrawCaptureRect mDraw = new DrawCaptureRect
    (
      EX10_05.this,
      // PORTRAIT
      //110, 10, 100, 100,
      190, 10, 100, 100,
      //181, 1, 118, 118,
      getResources().getColor(R.drawable.lightred)
    );
    addContentView(mDraw, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    
    /* 取得屏幕解析像素 */
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    mImageView01 = (ImageView) findViewById(R.id.myImageView1);
    
    /* 以SurfaceView作为相机Preview之用 */
    mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
    
    /* 系结SurfaceView，取得SurfaceHolder对象 */
    mSurfaceHolder01 = mSurfaceView01.getHolder();
    
    /* Activity必须实作SurfaceHolder.Callback */
    mSurfaceHolder01.addCallback(EX10_05.this);
    
    /* 额外的设定预览大小设定，在此不使用 */
    //mSurfaceHolder01.setFixedSize(160, 120);
      
    /*
     * 以SURFACE_TYPE_PUSH_BUFFERS(3)
     * 作为SurfaceHolder显示型态 
     * */
    mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton03 = (Button)findViewById(R.id.myButton3);
    
    /* 开启相机及Preview */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* 自订初始化开启相机函数 */
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
        /* 自定义拍照函数 */
        takePicture();
      }
    });
  }
  
  /* 自定义初始相机函数 */
  private void initCamera()
  {
    if(!bIfPreview)
    {
      /* 若相机非在预览模式，则开启相机 */
      try
      {
        /* 
         * The Heap 是应用程序在手机里执行所配置的空间
         * 当超过预设的16 MB（每一个应用程序）时就会导致 "Out of memory"的错误
         * 目前看来是Cupcake(AVD 1.5)才会发生 
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
        /* 建立Camera.Parameters物件 */
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
        
          /* 在2.0模拟器中，设定不支持的PreviewSize将造成Exception */
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
            /* 在2.0模拟器中，设定不支持的PictureSize将造成Exception */
            parameters.setPictureSize(512, 384);
            //parameters.setPictureSize(213, 350);
            /* 将Camera.Parameters设定予Camera */
            mCamera01.setParameters(parameters);
            /* setPreviewDisplay唯一的参数为SurfaceHolder */
            mCamera01.setPreviewDisplay(mSurfaceHolder01);
            /* 立即执行Preview */
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
            EX10_05.this,
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
      /* 呼叫takePicture()方法拍照 */
      mCamera01.takePicture(shutterCallback, rawCallback, jpegCallback);
    }
  }
  
  /* 相机重置 */
  private void resetCamera()
  {
    if (mCamera01 != null && bIfPreview)
    {
      try
      {
        mCamera01.stopPreview();
        /* 扩展学习，释放Camera对象 */
        //mCamera01.release();
        bIfPreview = false;
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
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
      
      try
      {
        /* 扩展学习，解碼图文件 */
        /*
        import java.io.File;
        
        String strQRTestFile = "/sdcard/test_qrcode.jpg"; 
        File myImageFile = new File(strQRTestFile);
        
        if(myImageFile.exists())
        {
          Bitmap myBmp = BitmapFactory.decodeFile(strQRTestFile); 
          mImageView01.setImageBitmap(myBmp);
          String strQR2 = decodeQRImage(myBmp);
          if(strQR2!="")
          {
            if (URLUtil.isNetworkUrl(strQR2))
            {
              mMakeTextToast(strQR2, true);
              Uri mUri = Uri.parse(strQR2);
              Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
              startActivity(intent);
            }
            else
            {
              mMakeTextToast(strQR2, true);
            }
          }
        }
        */
        
        /* onPictureTaken传入的第一个参数即为相片的byte */
        Bitmap bm = null;
        bm = BitmapFactory.decodeByteArray(_data, 0, _data.length);
        
        int resizeWidth = 160;
        int resizeHeight = 120;
        float scaleWidth = ((float) resizeWidth) / bm.getWidth();
        float scaleHeight = ((float) resizeHeight) / bm.getHeight();
        
        Matrix matrix = new Matrix();
        /* 使用Matrix.postScale方法缩小 Bitmap Size*/
        matrix.postScale(scaleWidth, scaleHeight);
        
        /* 建立新的Bitmap对象 */
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        
        /* 撷取4:3的图档的置中红色框部分100x100像素 */
        Bitmap resizedBitmapSquare = Bitmap.createBitmap(resizedBitmap, 30, 10, 100, 100);
        //Bitmap resizedBitmapSquare = Bitmap.createBitmap(resizedBitmap, 21, 1, 118, 118);
        //Bitmap resizedBitmapSquare = Bitmap.createBitmap(resizedBitmap, 60, 20, 200, 200);
        
        /* 将拍照的图文件以ImageView显示出来 */
        mImageView01.setImageBitmap(resizedBitmapSquare);
        
        /* 将传入的图文件译码成字符串 */
        String strQR2 = decodeQRImage(resizedBitmapSquare);
        if(strQR2!="")
        {
          if (URLUtil.isNetworkUrl(strQR2))
          {
            /* OMIA规范，网址条形码，开启浏览器上网 */
            mMakeTextToast(strQR2, true);
            Uri mUri = Uri.parse(strQR2);
            Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
            startActivity(intent);
          }
          else if(eregi("wtai://",strQR2))
          {
            /* OMIA规范，手机拨打电话格式 */
            String[] aryTemp01 = strQR2.split("wtai://");
            Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+aryTemp01[1]));
            startActivity(myIntentDial); 
          }
          else if(eregi("TEL:",strQR2))
          {
            /* OMIA规范，手机拨打电话格式 */
            String[] aryTemp01 = strQR2.split("TEL:");
            Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+aryTemp01[1]));
            startActivity(myIntentDial);
          }
          else
          {
            /* 若仅是文字，则以Toast显示出来 */
            mMakeTextToast(strQR2, true);
          }
        }
        
        /* 显示完图文件，立即重置相机，并关闭预览 */
        resetCamera();
        Thread.sleep(1000);
        /* 再重新启动相机继续预览 */
        initCamera();
      }
      catch (Exception e)
      {
        Log.e(TAG, e.getMessage());
      }
    }
  };
  
  public void mMakeTextToast(String str, boolean isLong)
  {
    if(isLong==true)
    {
      Toast.makeText(EX10_05.this, str, Toast.LENGTH_LONG).show();
    }
    else
    {
      Toast.makeText(EX10_05.this, str, Toast.LENGTH_SHORT).show();
    }
  }
  
  /* 解碼传入的Bitmap图档 */
  public String decodeQRImage(Bitmap myBmp)
  {
    String strDecodedData = "";
    try
    {
      QRCodeDecoder decoder = new QRCodeDecoder();
      strDecodedData  = new String(decoder.decode(new AndroidQRCodeImage(myBmp)));
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    return strDecodedData; 
  }
  
  /* 自订实作QRCodeImage类别 */
  class AndroidQRCodeImage implements QRCodeImage
  {
    Bitmap image;
    
    public AndroidQRCodeImage(Bitmap image)
    {
      this.image = image;
    }
    
    public int getWidth()
    {
      return image.getWidth();
    }
    
    public int getHeight()
    {
      return image.getHeight();
    }
    
    public int getPixel(int x, int y)
    {
      return image.getPixel(x, y);
    }   
  }
  
  /* 绘制相机预览画面里的正方形方框 */
  class DrawCaptureRect extends View
  {
    private int colorFill;
    private int intLeft,intTop, intWidth,intHeight;
    
    public DrawCaptureRect(Context context, int intX, int intY, int intWidth, int intHeight, int colorFill)
    {
      super(context);
      this.colorFill = colorFill;
      this.intLeft = intX;
      this.intTop = intY;
      this.intWidth = intWidth;
      this.intHeight = intHeight;
    }
    
    @Override
    protected void onDraw(Canvas canvas)
    {
      Paint mPaint01 = new Paint();
      mPaint01.setStyle(Paint.Style.FILL);
      mPaint01.setColor(colorFill);
      mPaint01.setStrokeWidth(1.0F);
      /* 在画布上绘制红色的四条方框线 */
      canvas.drawLine(this.intLeft, this.intTop, this.intLeft+intWidth, this.intTop, mPaint01);
      canvas.drawLine(this.intLeft, this.intTop, this.intLeft, this.intTop+intHeight, mPaint01);
      canvas.drawLine(this.intLeft+intWidth, this.intTop, this.intLeft+intWidth, this.intTop+intHeight, mPaint01);
      canvas.drawLine(this.intLeft, this.intTop+intHeight, this.intLeft+intWidth, this.intTop+intHeight, mPaint01);
      super.onDraw(canvas);
    }
  }
  
  /* 自订比对字符串函数 */
  public static boolean eregi(String strPat, String strUnknow)
  {
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.find();
  }
  
  /* 自订字符串取代函数 */
  public String eregi_replace(String strFrom, String strTo, String strTarget)
  {
    String strPattern = "(?i)"+strFrom;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strTarget);
    if(m.find())
    {
      return strTarget.replaceAll(strFrom, strTo);
    }
    else
    {
      return strTarget;
    }
  }
  
  @Override
  public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed");
  }
  
  @Override
  public void surfaceCreated(SurfaceHolder surfaceholder)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Changed");
  }
  
  @Override
  public void surfaceDestroyed(SurfaceHolder surfaceholder)
  {
    // TODO Auto-generated method stub
    Log.i(TAG, "Surface Destroyed");
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    try
    {
      /* Activity失去焦点，释放相机资源 */
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

