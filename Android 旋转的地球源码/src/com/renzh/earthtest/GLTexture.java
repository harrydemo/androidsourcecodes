package com.renzh.earthtest;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

/**
 * texture representation of gl
 * 
 */
public class GLTexture extends GLCache {

	// context for loading texture images
	protected Context mContext;
	// resource id for image
	protected int mResId;
	// texture name
	protected int mTexId;
	// use mipmap?
	protected boolean mUseMipmap;

	public GLTexture(Context context, int res, boolean mipmap) {
		this.mContext = context;
		this.mResId = res;
		this.mUseMipmap = mipmap;
	}
	
	@Override
	protected boolean onGenerate(GL11 gl11) {
		// Get the texture from the Android resource directory
 	//InputStream is = this.mContext.getResources().openRawResource( this.mResId);
		Bitmap resBMP = null;
         resBMP=BmpOper.decorateBmp(BmpOper.getBmpFromRaw(mContext, mResId));
//		try {
//			// BitmapFactory is an Android graphics utility for images
//			resBMP = BitmapFactory.decodeStream(is);
//		} finally {
//			// Always clear and close
//			try {
//				is.close();
//				is = null;
//			} catch (IOException exception) {
//			}
//		}

		int[] texs = new int[1];
		gl11.glGenTextures(1, texs, 0);
		mTexId = texs[0];

		if (this.mUseMipmap) {
			// Create mipmapped textures and bind it to texture
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, mTexId);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_NEAREST);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_CLAMP_TO_EDGE);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_CLAMP_TO_EDGE);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP,
					GL11.GL_TRUE);
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, resBMP, 0);
			// buildMipmap(gl11, resBMP);
		} else {
			// Create Linear Filtered Texture and bind it to texture
			gl11.glBindTexture(GL11.GL_TEXTURE_2D, mTexId);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL11.GL_TEXTURE_2D,
					GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
					GL10.GL_REPEAT);
			gl11.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
					GL10.GL_REPEAT);
			GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, resBMP, 0);

		}

		if (resBMP != null) {
			resBMP.recycle();
			resBMP = null;
		}
		return true;
	}

	@Override
	protected boolean onBind() {
		mGL.glBindTexture(GL11.GL_TEXTURE_2D, mTexId);
		return true;
	}

	@Override
	protected boolean onUnBind() {
		if (mGL != null) {
			mGL.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		}
		return true;
	}

	@Override
	public boolean release() {
		if (mGL != null) {
			int[] texture = new int[1];
			texture[0] = this.mTexId;
			mGL.glDeleteTextures(1, texture, 0);
			mGL = null;
		}
		return true;
	}

	/**
	 * Our own MipMap generation implementation. Scale the original bitmap down,
	 * always by factor two, and set it as new mipmap level.
	 * 
	 * @param gl
	 *            - The GL Context
	 * @param bitmap
	 *            - The bitmap to mipmap
	 */
	private void buildMipmap(GL11 gl, Bitmap bitmap) {
		int level = 0;
		int height = bitmap.getHeight();
		int width = bitmap.getWidth();

		while (height >= 1 || width >= 1) {
			// First of all, generate the texture from our bitmap and set it to
			// the according level
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, level, bitmap, 0);

			if (height == 1 || width == 1) {
				break;
			}

			// Increase the mipmap level
			level++;

			height /= 2;
			width /= 2;
			Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap, width, height,
					true);

			// Clean up
			bitmap.recycle();
			bitmap = bitmap2;
		}
	}
}
