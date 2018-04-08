package src.youer.text;

import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * view����
 * 
 * @author Administrator
 * 
 * @param <V>
 */
public abstract class ZoomView<V extends View>
{

	protected V view;

	// -----------------------------------------------
	private static final int NONE = 0;// ��
	private static final int DRAG = 1;// ���µ�һ����
	private static final int ZOOM = 2;// ���µڶ�����

	/** ��Ļ�ϵ������ */
	private int mode = NONE;

	/** ��¼���µڶ�������һ����ľ��� */
	float oldDist;

	public ZoomView(V view)
	{
		this.view = view;
		setTouchListener();
	}

	private void setTouchListener()
	{
		view.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction() & MotionEvent.ACTION_MASK)
				{
				case MotionEvent.ACTION_DOWN:
					mode = DRAG;
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					mode = NONE;
					break;
				case MotionEvent.ACTION_POINTER_DOWN:
					oldDist = spacing(event);
					if (oldDist > 10f)
					{
						mode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_MOVE:
					if (mode == ZOOM)
					{
						// �����ƶ��ĵ���ʼ��ľ���
						float newDist = spacing(event);

						if (newDist > oldDist)
						{
							zoomOut();
						}
						if (newDist < oldDist)
						{
							zoomIn();
						}

					}
					break;
				}
				return true;
			}

			/**
			 * ���2�������� ����
			 * 
			 * @param event
			 * @return
			 */
			private float spacing(MotionEvent event)
			{
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}
		});
	}

	protected abstract void zoomIn();

	protected abstract void zoomOut();
}
