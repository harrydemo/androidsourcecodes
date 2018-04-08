package com.cn.example.maxcard;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * ��demo�Ƿ�΢�ŵĶ�ά����Ƭ ����google�Ķ�ά����һ����Դ����Ŀ����Ҫ����һ����ά��ܼ� �����������ǽ�ͼƬ���ά���ϣ���ȻͼƬ����̫��
 * ��Ҫ��Ȼ��ά�����������
 */
public class MaxCardActivity extends Activity {
    // ͼƬ��ȵ�һ��
	private static final int IMAGE_HALFWIDTH = 20;
	// ��ʾ��ά��ͼƬ
	private ImageView imageview;
	// ���뵽��ά�������ͼƬ����
	private Bitmap mBitmap;
	// ��Ҫ��ͼͼƬ�Ĵ�С �����趨Ϊ40*40
	int[] pixels = new int[2*IMAGE_HALFWIDTH * 2*IMAGE_HALFWIDTH];

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
        // �������
		imageview = new ImageView(this);
        // ������Ҫ�����ͼƬ����
		mBitmap = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.ic_launcher)).getBitmap();
		// ����ͼƬ
		Matrix m = new Matrix();
		float sx = (float) 2*IMAGE_HALFWIDTH / mBitmap.getWidth();
		float sy = (float) 2*IMAGE_HALFWIDTH / mBitmap.getHeight();
		m.setScale(sx, sy);
		// ���¹���һ��40*40��ͼƬ
		mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(),
				mBitmap.getHeight(), m, false);

		try {
			String s = "��΢�Ŷ�ά����Ƭ";
			imageview.setImageBitmap(cretaeBitmap(new String(s.getBytes(),
					"ISO-8859-1")));
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setContentView(imageview);
	}

	/**
	 * ���ɶ�ά��
	 * 
	 * @param �ַ���
	 * @return Bitmap
	 * @throws WriterException
	 */
	public Bitmap cretaeBitmap(String str) throws WriterException {
		// ���ɶ�ά����,����ʱָ����С,��Ҫ������ͼƬ�Ժ��ٽ�������,������ģ������ʶ��ʧ��
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, 300, 300);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// ��ά����תΪһά��������,Ҳ����һֱ��������
		int halfW = width / 2;
		int halfH = height / 2;
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
						&& y < halfH + IMAGE_HALFWIDTH) {
					pixels[y * width + x] = mBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y
							- halfH + IMAGE_HALFWIDTH);
				} else {
					if (matrix.get(x, y)) {
						pixels[y * width + x] = 0xff000000;
					}
				}

			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// ͨ��������������bitmap
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}
}
