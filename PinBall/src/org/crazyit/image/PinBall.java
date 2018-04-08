package org.crazyit.image;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;

/**
 * Description:
 * <br/>site: <a href="http://www.crazyit.org">crazyit.org</a> 
 * <br/>Copyright (C), 2001-2012, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author  Yeeku.H.Lee kongyeeku@163.com
 * @version  1.0
 */
public class PinBall extends Activity
{
	// ����Ŀ��
	private int tableWidth;
	// ����ĸ߶�
	private int tableHeight;
	// ���ĵĴ�ֱλ��
	private int racketY;
	// ���涨�����ĵĸ߶ȺͿ��
	private final int RACKET_HEIGHT = 20;
	private final int RACKET_WIDTH = 70;
	// С��Ĵ�С
	private final int BALL_SIZE = 12;
	// С������������ٶ�
	private int ySpeed = 10;
	Random rand = new Random();
	// ����һ��-0.5~0.5�ı��ʣ����ڿ���С������з���
	private double xyRate = rand.nextDouble() - 0.5;
	// С�����������ٶ�
	private int xSpeed = (int) (ySpeed * xyRate * 2);
	// ballX��ballY����С�������
	private int ballX = rand.nextInt(200) + 20;
	private int ballY = rand.nextInt(10) + 20;
	// racketX�������ĵ�ˮƽλ��
	private int racketX = rand.nextInt(200);
	// ��Ϸ�Ƿ���������
	private boolean isLose = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// ȥ�����ڱ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
			WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ����PlaneView���
		final GameView gameView = new GameView(this);
		setContentView(gameView);
		// ��ȡ���ڹ�����
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		// �����Ļ��͸�
		tableWidth = display.getWidth();
		tableHeight = display.getHeight();
		racketY = tableHeight - 80;
		final Handler handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				if (msg.what == 0x123)
				{
					gameView.invalidate();
				}
			}
		};

		gameView.setOnKeyListener(new OnKeyListener()
		{
			@Override
			public boolean onKey(View source, int keyCode, KeyEvent event)
			{
				// ��ȡ���ĸ����������¼�
				switch (event.getKeyCode())
				{
					// ���Ƶ�������
					case KeyEvent.KEYCODE_DPAD_LEFT:
						if (racketX > 0)
							racketX -= 10;
						break;
					// ���Ƶ�������
					case KeyEvent.KEYCODE_DPAD_RIGHT:
						if (racketX < tableWidth - RACKET_WIDTH)
							racketX += 10;
						break;
				}
				// ֪ͨplaneView����ػ�
				gameView.invalidate();
				return true;
			}
		});
		final Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{

			@Override
			public void run()
			{
				// ���С��������߱߿�
				if (ballX <= 0 || ballX >= tableWidth - BALL_SIZE)
				{
					xSpeed = -xSpeed;
				}
				// ���С��߶ȳ���������λ�ã��Һ��������ķ�Χ֮�ڣ���Ϸ������
				if (ballY >= racketY - BALL_SIZE
					&& (ballX < racketX || ballX > racketX + RACKET_WIDTH))
				{
					timer.cancel();
					// ������Ϸ�Ƿ���������Ϊtrue��
					isLose = true;
				}
				// ���С��λ������֮�ڣ��ҵ�������λ�ã�С�򷴵�
				else if (ballY <= 0
					|| (ballY >= racketY - BALL_SIZE && ballX > racketX && ballX <= racketX
						+ RACKET_WIDTH))
				{
					ySpeed = -ySpeed;
				}
				// С����������
				ballY += ySpeed;
				ballX += xSpeed;
				// ������Ϣ��֪ͨϵͳ�ػ����
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 100);
	}

	class GameView extends View
	{
		public GameView(Context context)
		{
			super(context);
			setFocusable(true);
		}

		// ��дView��onDraw������ʵ�ֻ滭
		public void onDraw(Canvas canvas)
		{
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			// �����Ϸ�Ѿ�����
			if (isLose)
			{
				paint.setColor(Color.RED);
				paint.setTextSize(40);
				canvas.drawText("��Ϸ�ѽ���", 50, 200, paint);
			}
			// �����Ϸ��δ����
			else
			{
				// ������ɫ��������С��
				paint.setColor(Color.rgb(240, 240, 80));
				canvas.drawCircle(ballX, ballY, BALL_SIZE, paint);
				// ������ɫ������������
				paint.setColor(Color.rgb(80, 80, 200));
				canvas.drawRect(racketX, racketY, racketX + RACKET_WIDTH,
					racketY + RACKET_HEIGHT, paint);
			}
		}
	}
}