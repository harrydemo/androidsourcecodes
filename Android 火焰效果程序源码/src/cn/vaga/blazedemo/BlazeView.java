package cn.vaga.blazedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class BlazeView extends SurfaceView implements SurfaceHolder.Callback {

	private int backWidth;

	private int backHeight;

	private Bitmap bgImage = null;

	private boolean firstLoad = false;

	BlazeThread mBlazeThread = new BlazeThread();

	SurfaceHolder mSurfaceHolder = null;

	private static RandNums randNums;

	public static int[] palette = new int[512];

	private int[] blazeBuf;
	
	private int[] blazePalItems;

	private Paint paint = new Paint();

	static {

		int index = 0;
		int cIndex = 0;
		double alpha = 0.0;
		randNums = new RandNums();
		// ���ɵ�ɫ��
		palette[0] = Color.argb(0, 0, 0, 0); // 0 ��ɫ��

		for (index = 1; index < 70; index++) {
			cIndex = index >> 1;
			palette[index] = Color.argb((int)(alpha++ / 512 * 255), cIndex + 25,
					randNums.nextInt() % 10, randNums.nextInt() % 10); // 1-34
			// �ӽ��ں�ɫ��
		}
		for (index = 70; index < 110; index++) {
			cIndex = index >> 1;
			palette[index] = Color.argb((int)(alpha++ / 512 * 255), cIndex + 25, cIndex - 25, randNums
					.nextInt() % 10); // 35-54 �ӽ��ں�ɫ�ģ��������Ҫ��һЩ
		}
		for (index = 110; index < 190; index++) {
			cIndex = index >> 1;
			palette[index] = Color.argb((int)(alpha++ / 512 * 255), cIndex + 75, cIndex, randNums
					.nextInt() % 5); // 55-94 �ӽ������ٺ�ɫ
		}
		for (index = 190; index < 400; index++) {
			cIndex = index >> 1;
			palette[index] = Color.argb((int)(alpha++ / 512 * 255), (cIndex + 70 > 255) ? 255 : (cIndex + 70), cIndex + 30, randNums
					.nextInt() % 10); // 95-199 �ٺ�ɫ��
		}
		for (index = 400; index < 512; index++) {
			cIndex = index >> 1;
			palette[index] = Color.argb((int)(alpha++ / 512 * 255), cIndex, cIndex - randNums.nextInt()
					% 25, randNums.nextInt() % 5); // 200-254
			// �ӽ��ڻ�ɫ
		}

	}

	public BlazeView(Context context) {
		super(context);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (!firstLoad) {
			bgImage = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.bg);
			bgImage = Bitmap.createScaledBitmap(bgImage, w, h, false);// ���Ŷ���
			backWidth = bgImage.getWidth();
			backHeight = bgImage.getHeight();

			blazeBuf = new int[backWidth * backHeight];
			blazePalItems = new int[backWidth * backHeight];
			for (int i = 0; i < backWidth * backHeight; ++i) {
				blazeBuf[i] = 0;
				blazePalItems[i] = 0;
			}
			firstLoad = true;
		}

	}

	class BlazeThread extends Thread {
		boolean running = true;

		public void setRunning(boolean running) {
			this.running = running;
		}

		@Override
		public void run() {
			Canvas c = null;
			while (running) {
				c = mSurfaceHolder.lockCanvas();
				doDraw(c);
				makFire();
				mSurfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	private void makFire() {
		int x = 0, y = 0;

		int fireoffset = 0;

		int firevalue = 0;

		int backWidthAdd1 = backWidth + 1;
		
		int backWidthSub1 = backWidth - 1;
		
		int heightSize = backHeight - 1;
		
		int widthSize = backWidth - 1;
		
		int tmpVal = 0;
		
		// �������һ�С�����
		int wmh = backWidth * (backHeight - 2) - 1;
		int ranV = randNums.nextInt();
		for (x = 0; x < backWidth; x += ranV % 3) {
			tmpVal = wmh + x;
			if (ranV % 2 != 0)
				blazePalItems[tmpVal] = 511;
			else
				blazePalItems[tmpVal] = 0;
			
			blazeBuf[tmpVal] = palette[blazePalItems[tmpVal]];
			ranV = randNums.nextInt();
		}

		
		// ���㻷������
		for (y = 1; y < heightSize; ++y) {
			for (x = 1; x < widthSize; ++x) {
				fireoffset = (y * backWidth) + x;
		//		blazeBuf[fireoffset] = palette[(int)(x * 1.0f / widthSize * 512)];
				firevalue = ((
						  blazePalItems[fireoffset - backWidth]
						+ blazePalItems[fireoffset + backWidth]
						+ blazePalItems[fireoffset + 1]
						+ blazePalItems[fireoffset - 1]
						+ blazePalItems[fireoffset - backWidthAdd1]
						+ blazePalItems[fireoffset - backWidthSub1]
						+ blazePalItems[fireoffset + backWidthSub1] 
						+ blazePalItems[fireoffset + backWidthAdd1]) 
						>> 3);

				if (firevalue > 0) // is it black?
				{
					--firevalue; // Nope. Beam me down Scotty.
					tmpVal = fireoffset - backWidth;
					blazePalItems[tmpVal] = firevalue;
					
					blazeBuf[tmpVal] = palette[blazePalItems[tmpVal]]; // Plot that
					// new color
					// on the above pixel
					// remember the typewriter analogy
				}
			}
		}

	}

	protected void doDraw(Canvas canvas) {
		canvas.drawBitmap(blazeBuf, 0, backWidth, 0, 0, backWidth, backHeight,
				true, paint);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mBlazeThread.setRunning(true);
		mBlazeThread.start();

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
		mBlazeThread.setRunning(false);
		// �Ǳ����ر��̣߳�ֱ���˴θ��߳����н���֮ǰ�����߳�ֹͣ���У��Է�ֹSurface�����¼���
		while (retry) {
			try {
				mBlazeThread.join(); // ����current
				// Thread(��ǰִ���߳�)ֱ���������߳�(thread)��ɡ�
				retry = false;
			} catch (InterruptedException e) {
			}
		}

	}

}
