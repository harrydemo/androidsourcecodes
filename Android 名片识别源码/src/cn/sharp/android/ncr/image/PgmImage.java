package cn.sharp.android.ncr.image;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.util.concurrent.atomic.AtomicBoolean;

import android.graphics.Bitmap;
import android.util.Log;

public class PgmImage {
	private final static String TAG = "PgmImage";

	public final static String MAGIC_NUMBER = "P5";
	public final static AtomicBoolean decodeFromBitmapCancelled = new AtomicBoolean(
			false);
	public final static AtomicBoolean isDecodingFromBitmap = new AtomicBoolean(
			false);
	private int width, height;
	private int maxVal;

	/**
	 * the pixel values used to represent the image
	 */
	private byte[] content;

	/**
	 * export the PGM object to OutputStream
	 * 
	 * @param os
	 *            the stream that the PGM image bytes will be written to
	 * @return true 
	 *            if the PGM image is exported successfully, otherwise false
	 */
	public boolean save(OutputStream os) {
		if (os == null) {
			throw new IllegalArgumentException(
					"output stream is not allowed null");
		}
		boolean noerror = true;
		try {
			os.write(MAGIC_NUMBER.getBytes()); // write magic number
			os.write(0x0A);// write LF   换行符
			os.write(intToBytes(width));
			os.write(0x20);// write SPACE 空格
			os.write(intToBytes(height));
			os.write(0x0A);// write LF  换行符
			os.write(intToBytes(maxVal));
			os.write(0x0A); // write LF  换行符
			os.write(content);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			noerror = false;
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				Log.e(TAG, "error when close outputsteram:" + e.getMessage());
				e.printStackTrace();
			}
		}
		return noerror;
	}

	/**
	 * 把一个图片转换成 pgm格式
	 * create PGM object from a bitmap
	 * 
	 * @param bitmap
	 *            Bitmap object that will be converted to PGM
	 * @return PGM object if conversion succeeds, else null
	 */
	public synchronized static PgmImage fromBitmap(Bitmap bitmap) {
		if (bitmap == null) {
			Log.e(TAG, "input bitmap is not allowed null");
			return null;
		}
		isDecodingFromBitmap.set(true);
		decodeFromBitmapCancelled.set(false);
		PgmImage pgm = new PgmImage();
		pgm.width = bitmap.getWidth();
		pgm.height = bitmap.getHeight();
		pgm.maxVal = 255;
		Log.e(TAG, "before allocate content bytes");
		pgm.content = new byte[pgm.width * pgm.height];
		if (decodeFromBitmapCancelled.get()) {
			isDecodingFromBitmap.set(false);
			Log.e(TAG, "decodeFromBitmap cancelled at step 1");
			return null;
		}
		Log.e(TAG, "allocate content bytes successfully");

		int lines = 300;// divide exactly by 1200
		int buffersize = pgm.width * lines;
		int[] pixels = new int[buffersize];
		int pi = 0;
		for (int i = 0; i < pgm.height; ++i) {
			if (decodeFromBitmapCancelled.get()) {
				isDecodingFromBitmap.set(false);
				Log.d(TAG, "decodeFromBitmap cancelled at step 2");
				return null;
			}
			if (i % lines == 0) {
				Log.e(TAG, "before allocate buffer " + i);
				Log.e(TAG, "allocate buffer " + i % lines);
				bitmap.getPixels(pixels, 0, pgm.width, 0, i, pgm.width, lines);
				if (decodeFromBitmapCancelled.get()) {
					isDecodingFromBitmap.set(false);
					Log.d(TAG, "decodeFromBitmap cancelled at step 3");
					return null;
				}
				pi = 0;
			}
			for (int j = 0; j < pgm.width; ++j) {
				int color = pixels[pi++];
				// int color = bitmap.getPixel(j, i);
				int r = (color & 0x00FF0000) >> 16;
				int g = (color & 0x0000FF00) >> 8;
				int b = color & 0x000000FF;
				pgm.content[i * pgm.width + j] = (byte) (0.299 * r + 0.587 * g + 0.114 * b);
			}
		}
		isDecodingFromBitmap.set(false);
		Log.v(TAG, "create PGM object successfully");
		return pgm;
	}

	public static void cancelDecodeFromBitmap() {
		decodeFromBitmapCancelled.set(true);
	}

	/**
	 * create PgmImage object from an existing PGM image file
	 * 
	 * @param file
	 *            the PGM image file object. PGM image with comment is not
	 *            supported
	 * @return PgmImage object if successful, otherwise null
	 */
	public static PgmImage fromFile(File file) {
		boolean error = false;
		if (file == null) {
			Log.e(TAG, "input file is not allowed null");
			return null;
		}
		if (file.exists() && file.isFile()) {
			if (file.length() < 1024) {
				Log.e(TAG, "invalid pgm file, file length too short");
				return null;
			}
		} else {
			Log.e(TAG, "file " + file.getAbsolutePath() + " not exists");
			return null;
		}
		InputStream fis = null;
		try {
			fis = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			error = true;
		}
		if (error) {
			Log.e(TAG, "failed to read file, return null");
			return null;
		} else {
			if (fis != null) {
				PgmImage pgm = new PgmImage();
				byte[] buffer = new byte[20];// store pgm head, 20 bytes are
				// sufficient;
				/**
				 * read PGM file head begin
				 */
				try {
					int bufferSize = fis.read(buffer);
					int start, end;
					// magic number OK
					if (buffer[0] == 'P' && buffer[1] == '5') {
						end = start = 3;
						// read width
						while (end < bufferSize && !isWhiteSpace(buffer[end]))
							++end;
						if (end >= bufferSize || start == end) {
							error = true;
							Log.e(TAG, "failed to read pgm image width");
						} else {
							int width = bytesToInt(buffer, start, end);
							if (width > 0) {
								Log.d(TAG, "get image width:" + width);
								pgm.width = width;
								end = start = end + 1;// skip whitespace
								// read height
								while (end < bufferSize
										&& !isWhiteSpace(buffer[end]))
									++end;
								if (end >= bufferSize || start == end) {
									error = true;
									Log.e(TAG,
											"failed to read pgm image height");
								} else {
									int height = bytesToInt(buffer, start, end);
									if (height > 0) {
										Log.d(TAG, "get image height:" + height);
										pgm.height = height;
										end = start = end + 1;
										// read max gray value,255
										while (end < bufferSize
												&& !isWhiteSpace(buffer[end]))
											++end;
										if (end >= bufferSize || start == end) {
											error = true;
											Log.e(TAG,
													"failed to read pgm image max gray value");
										} else {
											int maxVal = bytesToInt(buffer,
													start, end);
											if (maxVal > 0 && maxVal < 256) {
												Log.d(TAG, "get image maxVal:"
														+ maxVal);
												pgm.maxVal = maxVal;
												int contentLengthCalc = width
														* height;
												// move to the first byte of PGM
												// image content
												++end;
												int contentLengthActual = (int) file
														.length() - end;
												if (contentLengthCalc == contentLengthActual) {
													Log.d(TAG,
															"begin to read "
																	+ contentLengthActual
																	+ " bytes");
													byte[] content = new byte[contentLengthActual];
													// copy the bytes remained
													// in buffer
													for (int i = end; i < buffer.length; i++) {
														content[i - end] = buffer[i];
														Log.d(TAG,
																"remained bytes:"
																		+ (i - end)
																		+ ":"
																		+ (buffer[i] & 0x00FF));
													}
													Log.d(TAG,
															"remained byte number:"
																	+ (contentLengthActual
																			- buffer.length + end));
													fis.read(
															content,
															buffer.length - end,
															contentLengthActual
																	- buffer.length
																	+ end);
													pgm.content = content;
													Log.d(TAG,
															"finish reading pgm image file");
													/**
													 * read pgm image file end
													 */
												} else {
													error = true;
													Log.e(TAG,
															"image content length do not match, should be "
																	+ contentLengthCalc
																	+ " bytes, actually "
																	+ contentLengthActual
																	+ " bytes");
												}
											} else {
												error = true;
												Log.e(TAG,
														"invalid max gray value:"
																+ maxVal);
											}
										}
									} else {
										error = true;
										Log.e(TAG, "invalid pgm height:"
												+ height);
									}
								}
							} else {
								error = true;
								Log.e(TAG, "invalid pgm width:" + width);
							}
						}
					} else {
						error = true;
						Log.e(TAG, "invalid magic number");
					}
				} catch (IOException e) {
					Log.e(TAG, e.getMessage());
					error = true;
				} finally {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
						Log.e(TAG, e.getMessage());
					}
				}

				if (error) {
					Log.e(TAG, "error when retrieving pgm image, return null");
					return null;
				} else {
					Log.d(TAG, "get a pgm image");
					return pgm;
				}
			} else {
				Log.e(TAG, "failed to initialize FileInputStream, return null");
				return null;
			}
		}
	}

	private static boolean isWhiteSpace(byte i) {
		return i == 0x20 || i == 0x09 || i == 0x0A | i == 0x0D;
	}

	private static int bytesToInt(byte[] bytes, int start, int end) {
		if (bytes != null && start < end && start >= 0 && end <= bytes.length) {
			String ts = new String(bytes, start, end - start);
			Integer ti = Integer.valueOf(ts);
			return ti.intValue();
		} else {
			new IllegalArgumentException(
					"invalid parameter, byte array length:" + bytes.length
							+ ", start index(inc):" + start
							+ ", end index(exc):" + end);
		}
		return -1;
	}

	/**
	 * convert integer to byte arrays. Every bit of intt in decimal occupies one
	 * byte, from highest to lowest
	 * 
	 * @param intt
	 *            a positive integer
	 * @return the byte array, the first byte in the array corresponds to the
	 *         highest bit of intt in decimal
	 */
	private byte[] intToBytes(int intt) {
		if (intt <= 0) {
			throw new IllegalArgumentException(
					"only positive integer is allowed");
		}
		// byte[] temp = new byte[11];
		// int count = 0;
		// while (intt > 0) {
		// temp[count] = (byte) (intt % 10 + 48);
		// intt /= 10;
		// ++count;
		// }
		// byte[] result = new byte[count];
		// --count;
		// for (int i = 0; i <= count; ++i) {
		// result[count - i] = temp[i];
		// }
		// return result;
		return String.valueOf(intt).getBytes();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte[] getContent() {
		return content;
	}
}
