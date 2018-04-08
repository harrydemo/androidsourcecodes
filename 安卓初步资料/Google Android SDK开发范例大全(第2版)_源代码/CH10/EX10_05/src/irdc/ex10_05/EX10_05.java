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
  /* ����˽��Camera���� */
  private Camera mCamera01;
  private Button mButton01, mButton02, mButton03;
  
  /* ��Ϊreview����������Ƭ֮�� */
  private ImageView mImageView01;
  private String TAG = "HIPPO_DEBUG";
  private SurfaceView mSurfaceView01;
  private SurfaceHolder mSurfaceHolder01;
  
  /* Ԥ�����Ԥ��ģʽΪfalse */
  private boolean bIfPreview = false;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    
    /* ʹӦ�ó���ȫ��Ļִ�У���ʹ��title bar */
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
    
    /* ȡ����Ļ�������� */
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    
    mImageView01 = (ImageView) findViewById(R.id.myImageView1);
    
    /* ��SurfaceView��Ϊ���Preview֮�� */
    mSurfaceView01 = (SurfaceView) findViewById(R.id.mSurfaceView1);
    
    /* ϵ��SurfaceView��ȡ��SurfaceHolder���� */
    mSurfaceHolder01 = mSurfaceView01.getHolder();
    
    /* Activity����ʵ��SurfaceHolder.Callback */
    mSurfaceHolder01.addCallback(EX10_05.this);
    
    /* ������趨Ԥ����С�趨���ڴ˲�ʹ�� */
    //mSurfaceHolder01.setFixedSize(160, 120);
      
    /*
     * ��SURFACE_TYPE_PUSH_BUFFERS(3)
     * ��ΪSurfaceHolder��ʾ��̬ 
     * */
    mSurfaceHolder01.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton02 = (Button)findViewById(R.id.myButton2);
    mButton03 = (Button)findViewById(R.id.myButton3);
    
    /* ���������Preview */
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        
        /* �Զ���ʼ������������� */
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
        /* �Զ������պ��� */
        takePicture();
      }
    });
  }
  
  /* �Զ����ʼ������� */
  private void initCamera()
  {
    if(!bIfPreview)
    {
      /* ���������Ԥ��ģʽ��������� */
      try
      {
        /* 
         * The Heap ��Ӧ�ó������ֻ���ִ�������õĿռ�
         * ������Ԥ���16 MB��ÿһ��Ӧ�ó���ʱ�ͻᵼ�� "Out of memory"�Ĵ���
         * Ŀǰ������Cupcake(AVD 1.5)�Żᷢ�� 
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
        /* ����Camera.Parameters��� */
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
        
          /* ��2.0ģ�����У��趨��֧�ֵ�PreviewSize�����Exception */
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
            /* ��2.0ģ�����У��趨��֧�ֵ�PictureSize�����Exception */
            parameters.setPictureSize(512, 384);
            //parameters.setPictureSize(213, 350);
            /* ��Camera.Parameters�趨��Camera */
            mCamera01.setParameters(parameters);
            /* setPreviewDisplayΨһ�Ĳ���ΪSurfaceHolder */
            mCamera01.setPreviewDisplay(mSurfaceHolder01);
            /* ����ִ��Preview */
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
      try
      {
        mCamera01.stopPreview();
        /* ��չѧϰ���ͷ�Camera���� */
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
        /* ��չѧϰ����aͼ�ļ� */
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
        
        /* onPictureTaken����ĵ�һ��������Ϊ��Ƭ��byte */
        Bitmap bm = null;
        bm = BitmapFactory.decodeByteArray(_data, 0, _data.length);
        
        int resizeWidth = 160;
        int resizeHeight = 120;
        float scaleWidth = ((float) resizeWidth) / bm.getWidth();
        float scaleHeight = ((float) resizeHeight) / bm.getHeight();
        
        Matrix matrix = new Matrix();
        /* ʹ��Matrix.postScale������С Bitmap Size*/
        matrix.postScale(scaleWidth, scaleHeight);
        
        /* �����µ�Bitmap���� */
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        
        /* ߢȡ4:3��ͼ�������к�ɫ�򲿷�100x100���� */
        Bitmap resizedBitmapSquare = Bitmap.createBitmap(resizedBitmap, 30, 10, 100, 100);
        //Bitmap resizedBitmapSquare = Bitmap.createBitmap(resizedBitmap, 21, 1, 118, 118);
        //Bitmap resizedBitmapSquare = Bitmap.createBitmap(resizedBitmap, 60, 20, 200, 200);
        
        /* �����յ�ͼ�ļ���ImageView��ʾ���� */
        mImageView01.setImageBitmap(resizedBitmapSquare);
        
        /* �������ͼ�ļ�������ַ��� */
        String strQR2 = decodeQRImage(resizedBitmapSquare);
        if(strQR2!="")
        {
          if (URLUtil.isNetworkUrl(strQR2))
          {
            /* OMIA�淶����ַ�����룬������������� */
            mMakeTextToast(strQR2, true);
            Uri mUri = Uri.parse(strQR2);
            Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
            startActivity(intent);
          }
          else if(eregi("wtai://",strQR2))
          {
            /* OMIA�淶���ֻ�����绰��ʽ */
            String[] aryTemp01 = strQR2.split("wtai://");
            Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+aryTemp01[1]));
            startActivity(myIntentDial); 
          }
          else if(eregi("TEL:",strQR2))
          {
            /* OMIA�淶���ֻ�����绰��ʽ */
            String[] aryTemp01 = strQR2.split("TEL:");
            Intent myIntentDial = new Intent("android.intent.action.CALL",Uri.parse("tel:"+aryTemp01[1]));
            startActivity(myIntentDial);
          }
          else
          {
            /* ���������֣�����Toast��ʾ���� */
            mMakeTextToast(strQR2, true);
          }
        }
        
        /* ��ʾ��ͼ�ļ�������������������ر�Ԥ�� */
        resetCamera();
        Thread.sleep(1000);
        /* �����������������Ԥ�� */
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
  
  /* ��a�����Bitmapͼ�� */
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
  
  /* �Զ�ʵ��QRCodeImage��� */
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
  
  /* �������Ԥ��������������η��� */
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
      /* �ڻ����ϻ��ƺ�ɫ������������ */
      canvas.drawLine(this.intLeft, this.intTop, this.intLeft+intWidth, this.intTop, mPaint01);
      canvas.drawLine(this.intLeft, this.intTop, this.intLeft, this.intTop+intHeight, mPaint01);
      canvas.drawLine(this.intLeft+intWidth, this.intTop, this.intLeft+intWidth, this.intTop+intHeight, mPaint01);
      canvas.drawLine(this.intLeft, this.intTop+intHeight, this.intLeft+intWidth, this.intTop+intHeight, mPaint01);
      super.onDraw(canvas);
    }
  }
  
  /* �Զ��ȶ��ַ������� */
  public static boolean eregi(String strPat, String strUnknow)
  {
    String strPattern = "(?i)"+strPat;
    Pattern p = Pattern.compile(strPattern);
    Matcher m = p.matcher(strUnknow);
    return m.find();
  }
  
  /* �Զ��ַ���ȡ������ */
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
      /* Activityʧȥ���㣬�ͷ������Դ */
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

