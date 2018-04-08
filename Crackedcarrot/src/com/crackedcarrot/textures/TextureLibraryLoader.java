package com.crackedcarrot.textures;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

public class TextureLibraryLoader {

	private static InputStream inStream;
	private static StringBuffer buf = null;

	public static TextureLibrary loadTextures(int resourceId, Context context) {

		TextureLibrary rLib = new TextureLibrary();
		inStream = context.getResources().openRawResource(resourceId);

		int c = 0;
		String trimmer = null;
		String[] resIdAndFrames = null;
		buf = new StringBuffer();

		try {
			while ((c = inStream.read()) != -1) {
				if (((char) c) != '\n')
					buf.append((char) c);
				else {
					trimmer = buf.toString();
					resIdAndFrames = trimmer.split(" ");
					if (resIdAndFrames.length != 2) {
						// Log.d("TEXTURE LIBRARY",
						// "Mallformed Texture Meta Data");

					} else {
						int resId = context.getResources().getIdentifier(
								resIdAndFrames[0].trim(), "drawable",
								context.getPackageName());
						if (resId == 0)
							Log.e("TEXTURE LIBRARY",
									"Invalid resourceId from context, Rid fetch failed!");

						rLib.setFrameData(resId,
								Integer.parseInt(resIdAndFrames[1].trim()));
						buf = new StringBuffer();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rLib;
	}
}
