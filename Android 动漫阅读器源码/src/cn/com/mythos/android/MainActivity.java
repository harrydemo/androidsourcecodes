package cn.com.mythos.android;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.com.mythos.android.Contents.Contents;
import cn.com.mythos.android.Contents.Utils;
import cn.com.mythos.touhoucartoonreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements OnTouchListener,
		OnGestureListener {
	private RelativeLayout layout1;
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private RelativeLayout layout4;
	private Bitmap bmap = null;
	private ImageButton openSDcard;
	private ImageButton page;
	private ImageButton bookmark;
	private ImageButton logout;
	private ImageButton setup;
	private ImageButton zoomSmall;
	private ImageButton zoomBig;
	private int disWidth;
	private int disHeight;
	private LinkedList<String> imageList;
	private Map<String, String> imagePosition;
	private String[] imageArray;
	private int imageIndex;
	private String index;
	private GestureDetector gestureScanner;
	private int picIndex;
	private Handler handler = new Handler();
	private Handler handler2 = new Handler();
	private TextView imageName;
	private TextView pagePosition;
	private ImageView imageView;
	private ImageButton lastPage;
	private ImageButton nextPage;
	private final float FLING_MIN_DISTANCE = 250;
	private final float FLING_MIN_VELOCITY = 400;
	private EditText BookmarkName;
	private String bookMarks;
	private static boolean isStop3 = false;
	private static boolean isStop5 = false;
	private static boolean isShow = false;
	private RelativeLayout relativeLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		
		layout1 = (RelativeLayout) findViewById(R.id.layout1);
		layout2 = (RelativeLayout) findViewById(R.id.layout2);
		layout3 = (RelativeLayout) findViewById(R.id.layout3);
		layout4 = (RelativeLayout) findViewById(R.id.layout4);

		openSDcard = (ImageButton) findViewById(R.id.openSDcard);
		openSDcard.setOnClickListener(openSDcardButton);

		page = (ImageButton) findViewById(R.id.page);
		page.setOnClickListener(pageButton);

		bookmark = (ImageButton) findViewById(R.id.bookmark);
		bookmark.setOnClickListener(bookmarkButton);

		setup = (ImageButton) findViewById(R.id.setup);
		setup.setOnClickListener(setupButton);

		logout = (ImageButton) findViewById(R.id.logout);
		logout.setOnClickListener(logoutButton);
		
		zoomSmall = (ImageButton) findViewById(R.id.zoomSmall);
		zoomSmall.setOnClickListener(zoomSmallButton);
		
		zoomBig = (ImageButton) findViewById(R.id.zoomBig);
		zoomBig.setOnClickListener(zoomBigButton);

		pagePosition = (TextView) findViewById(R.id.pagePosition);
		imageName = (TextView) findViewById(R.id.imageName);
		imageView = (ImageView) findViewById(R.id.imageView);
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_main);

		getDisplayMetrics();
		if (loadImages() != null && loadImages().size() > 0) {
			imageList = loadImages();
			imageArray = imageList.toArray(new String[imageList.size()]);
		} else {
			layout2.setVisibility(View.VISIBLE);
			layout4.setVisibility(View.VISIBLE);
			showMsg(R.string.noPic);
		}
		
		relativeLayout.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		
		doGestureEvent();
		initLoadImages();

		lastPage = (ImageButton) findViewById(R.id.lastPage);
		lastPage.setOnClickListener(lastPageButton);

		nextPage = (ImageButton) findViewById(R.id.nextPage);
		nextPage.setOnClickListener(nextPageButton);
	}
	
	//��дonPause�ķ���,���浱ǰ�Ķ���������Ϣ
	@Override
	protected void onPause() {
		super.onPause();
		try{
			String picPath = Utils.getImagePath(imagePosition, imageList);
			if(picPath != null) {
				Utils.saveFile(Contents.SHOWHISTORY, picPath, false);
			}
		}catch (Exception e) {
			
		}
	}
	
	
	
	//��С��ť�ļ���
	public View.OnClickListener zoomSmallButton = new View.OnClickListener() {
		public void onClick(View v) {
			if(imageList != null) {
				String picPath = Utils.getImagePath(imagePosition, imageList);
				Bitmap bitmap = Utils.imageZoomSmall(picPath, disWidth, disHeight);
				if(bitmap != null) {
					Utils.ScaleAngle = 0;
					setImageView(bitmap);
					bitmap = null;
				}
			}
		}
	};

	//�Ŵ�ť�ļ���
	public View.OnClickListener zoomBigButton = new View.OnClickListener() {	
		public void onClick(View v) {
			if(imageList != null) {
				String picPath = Utils.getImagePath(imagePosition, imageList);
				Bitmap bitmap = Utils.imageZoomBig(picPath, disWidth, disHeight);
				if(bitmap != null) {
					Utils.ScaleAngle = 0;
					setImageView(bitmap);
					bitmap = null;
				}
			}
		}
	};
	
	//setImageView����������,������ʾbitmap
	public void setImageView(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}
	
	//��д�˴�����Ӧ�¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	// ��ȡ�ֻ��ֱ��ʣ����ݴ�С���øߺͿ�
	public void getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		disWidth = dm.widthPixels;
		disHeight = dm.heightPixels;
	}

	// ����ָ���ļ����µ�����ͼƬ
	public LinkedList<String> loadImages() {
		LinkedList<String> list = new LinkedList<String>();
		String filePath = getPicPath();
		if (filePath != null && !"".equals(filePath)) {
			File picFile = new File(filePath);
			if (!picFile.exists()) {
				return list;
			} else {
				File[] files = Utils.getPicOrder(picFile.getParent());
				if (files != null && files.length > 0) {
					for (File file : files) {
						if (Utils.getFileExt(file.getPath())) {
							list.add(file.getAbsolutePath());
						}
					}
				}
			}
		}
		return list;
	}

	// ���������¼�
	public void doGestureEvent() {
		gestureScanner = new GestureDetector(this);
		gestureScanner
				.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
					public boolean onSingleTapConfirmed(MotionEvent e) {
						if (imageList != null && imageList.size() > 0) {
							// 5����Զ����أ�����ʵ��
							if(!isShow) {
								layout2.setVisibility(View.VISIBLE);
								layout3.setVisibility(View.VISIBLE);
								layout4.setVisibility(View.VISIBLE);
								isShow = true;
								handler = new Handler();
								handler.removeCallbacks(hideMenu1);
								handler.postDelayed(hideMenu1, 5000);
							}
						}
						return false;
					}

					// ���ز˵��������׶�ʵ��
					private Runnable hideMenu1 = new Runnable() {
						public void run() {
							layout2.setVisibility(View.GONE);
							layout3.setVisibility(View.GONE);
							layout4.setVisibility(View.GONE);
							isShow = false;
							handler.removeCallbacks(hideMenu1);
						}
					};
				
					public boolean onDoubleTapEvent(MotionEvent e) {
						// TODO Auto-generated method stub
						return false;
					}

					public boolean onDoubleTap(MotionEvent e) {
						if(imageList!=null&&imageList.size()>0){
							//��ȡ����ǰͼƬ��·��
							if(imagePosition!=null&&imagePosition.size()>0){
									getArrayAtBitmap("right"); 
							}
						}
						return false;
					}
				});
	}
	
	// ��ʼ��ͼƬ��Ϣ
	public void initLoadImages() {
		String picPath = getPicPath();
		if (picPath != null) {
			if (imageArray != null && imageArray.length > 0) {
				int i = getPicPosition(picPath, imageArray);
				setImageView(i);
			}
		}
	}

	// ����intent�������ͼƬ��·��
	public String getPicPath() {
		String picPath = "";
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null && bundle.size() > 0) {
			picPath = bundle.getString("picPath");
		} else {
			picPath = Utils.getFileRead(Contents.SHOWHISTORY);
		}
		return picPath;
	}

	// ���õ�ǰͼƬ��λ��
	public int getPicPosition(String picPath, String[] imagesArray) {
		int position = 0;
		for (int i = 0; i < imagesArray.length; i++) {
			if (picPath.equals(imagesArray[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	// ���õ�ǰͼƬ��С
	public void setImageView(int index) {
		imagePosition = new HashMap<String, String>();
		imagePosition.clear();
		imagePosition.put("positionId", String.valueOf(index));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(imageArray[index], options);
		imageView.setImageBitmap(bitmap);
		setPageInfo();
		Utils.resetBitmap();
	}

	// ����ͼƬ��Ϣ
	public void setPageInfo() {
		if (imageList != null && imageList.size() > 0) {
			if (imagePosition != null && imagePosition.size() > 0) {
				String picPath = Utils.getImagePath(imagePosition, imageList);
				onPause();
				if (picPath != null && picPath.length() > 0) {
					String picName = picPath.substring(
							picPath.lastIndexOf("/") + 1, picPath.length());
					imageName.setText(picName);
				}
				String page = Utils.getImagePagePosition(imagePosition,
						imageList);
				pagePosition.setText(page);
			}
		} else {
			showNoPicMsg();
		}
	}

	// ͼƬ��������Ϣ
	public void showNoPicMsg() {
		Toast.makeText(this, R.string.noPic, Toast.LENGTH_SHORT).show();
	}

	// ��Ϣ��ʾ
	public void showMsg(int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}
	
	// ��Ϣ��ʾ������
	public void showMsg(String str) {
		Toast.makeText(this, str,Toast.LENGTH_SHORT).show();
	}
	
	//��SDCard��ť�ļ���
	private View.OnClickListener openSDcardButton = new View.OnClickListener() {

		public void onClick(View v) {
			String picPath = Utils.getImagePath(imagePosition, imageList);
			Intent intent = new Intent(MainActivity.this, TabMainActivity.class);
			if(picPath != null) {
				Bundle bundle = new Bundle();
				bundle.putString("picPath", picPath);
				intent.putExtras(bundle);
			}
			isStop3 = true;
			isStop5 = true;
			setAutoPlayTime("0");
			startActivity(intent);
			finish();
		}
	};
	
	//��д�����ǵ�SHOWTIME�ļ�,��ֹ���л�Activityʱ���߳�����
	private void setAutoPlayTime(String time) {
		String content = "time=" + time;
		Utils.saveFile(Contents.AUTOSHOWTIME, content, false);
	}
	
	//ҳ����ת��ť�ļ�����ʵ�ֵ�һҳ����һҳ����һҳ�����һҳ����ת
	private View.OnClickListener pageButton = new View.OnClickListener() {

		public void onClick(View v) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle(R.string.pageTitle);
			String[] PAGEARRAY = new String[] { getString(R.string.firstPage),
					getString(R.string.beforePage),
					getString(R.string.nextPage), getString(R.string.lastPage) };
			dialog.setItems(PAGEARRAY, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					if(imagePosition != null && imagePosition.size() > 0) {
						int position = Integer.parseInt(imagePosition
								.get("positionId"));
						switch (which) {
						case 0:
							if (position > 0) {
								imageIndex = 0;
								setImageView(imageIndex);
								showMsg((imageIndex + 1) + "/" + imageArray.length);
							} else {
								showMsg(R.string.pageFirst);
							}
							break;
						case 1:
							if (position >= 1) {
								imageIndex = position - 1;
								setImageView(imageIndex);
								showMsg((imageIndex + 1) + "/" + imageArray.length);
							} else {
								showMsg(R.string.pageFirst);
							}
							break;
						case 2:
							if (position < imageArray.length - 1) {
								imageIndex = position + 1;
								setImageView(imageIndex);
								showMsg((imageIndex + 1) + "/" + imageArray.length);
							} else if (position == imageArray.length - 1) {
								showMsg(R.string.pageEnd);
							}
							break;
						case 3:
							if (position != imageArray.length - 1) {
								imageIndex = imageArray.length - 1;
								setImageView(imageIndex);
								showMsg((imageIndex + 1) + "/" + imageArray.length);
							} else {
								showMsg(R.string.pageEnd);
							}
							break;
						}
					}
				}
			});
			dialog.setNegativeButton(R.string.pageCancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			dialog.show();
		}
	};

	// lastPageButton�ļ���
	public View.OnClickListener lastPageButton = new View.OnClickListener() {

		public void onClick(View v) {
			getArrayAtBitmap("left");
		}
	};
	// nextPageButton�ļ���
	public View.OnClickListener nextPageButton = new View.OnClickListener() {

		public void onClick(View v) {
			getArrayAtBitmap("right");
		}
	};

	// ҳ����ת�ķ���
	public void getArrayAtBitmap(String str) {
		if (imageList != null && imageList.size() > 0) {
			int position = Integer.parseInt(imagePosition
					.get("positionId"));
			if ("left".equals(str)) {
				if (position >= 1) {
					imageIndex = position - 1;
					setImageView(imageIndex);
					showMsg((imageIndex + 1) + "/" + imageArray.length);
				} else {
					showMsg(R.string.pageFirst);
				}
			}
			if ("right".equals(str)) {
				if (position < imageArray.length - 1) {
					imageIndex = position + 1;
					setImageView(imageIndex);
					showMsg((imageIndex + 1) + "/" + imageArray.length);
				} else if (position == imageArray.length - 1) {
					showMsg(R.string.pageEnd);
				}
			}
		} else {
			showNoPicMsg();
		}
		setPageInfo();
	}

	// ������ǩ����
	private View.OnClickListener bookmarkButton = new View.OnClickListener() {

		public void onClick(View v) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout view = (RelativeLayout) inflater.inflate(
					R.layout.layout_bookmark_add, null);
			BookmarkName = (EditText) view.findViewById(R.id.bookMarkName);
			dialog.setIcon(R.raw.bookmark);
			dialog.setTitle(R.string.bookmarkTitle);
			dialog.setPositiveButton(R.string.bookmarkSubmit,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if(imagePosition != null && imagePosition.size() > 0) {
								String bookId = imagePosition.get("positionId");
								String picPath = Utils.getImagePath(imagePosition, imageList);
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								String now_time = df.format(new java.util.Date());
								bookMarks = BookmarkName.getText().toString() + "|" + now_time + "," + picPath + "#" + bookId + ";";
								Utils.saveFile(Contents.BOOKMARKS, bookMarks, true);
								showMsg(R.string.bookmarkSave);
							}
						}
					});
			dialog.setNegativeButton(R.string.bookmarkCancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.setView(view);
			dialog.show();
		}
	};
	
	//�˳���ť�ļ���
	private Button.OnClickListener logoutButton = new Button.OnClickListener() {

		public void onClick(View v) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle(R.string.logout_title);
			dialog.setMessage(R.string.logout_body);
			dialog.setPositiveButton(R.string.logout_submit,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							System.exit(0);
						}
					});

			dialog.setNegativeButton(R.string.logout_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.show();
		}
	};
	
	//���ð�ť�ļ���
	private View.OnClickListener setupButton = new View.OnClickListener() {

		public void onClick(View v) {
			new GetMoreSetup(MainActivity.this);
		}
	};

	public class GetMoreSetup extends Dialog{
		RelativeLayout view;
		RadioButton second0;
		RadioButton second3;
		RadioButton second5;
		RadioButton leftRotate;
		RadioButton rightRotate;

		public GetMoreSetup(Context context) {
			super(context);
			showDialog();
		}

		public void showDialog() {
			String[] SETUPARRAY = new String[] {
					getString(R.string.orderTimeBrowse),
					getString(R.string.bookmarketBrowse),
					getString(R.string.pictureRotate) };
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle(R.string.menu_more);
			dialog.setNegativeButton(R.string.picCancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.setItems(SETUPARRAY, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						getFixedTime();
						break;
					case 1:
						String picPath = Utils.getImagePath(imagePosition, imageList);
						Intent intent = new Intent(MainActivity.this, BookMarkActivity.class);
						if(picPath != null) {
							Bundle bundle = new Bundle();
							bundle.putString("picPath", picPath);
							intent.putExtras(bundle);
						}
						isStop3 = true;
						isStop5 = true;
						setAutoPlayTime("0");
						startActivity(intent);
						finish();
						break;
					case 2:
						getPicRotate();
						break;
					}
				}
			});
			dialog.show();
		}
		
		//��ʱ���
		private void getFixedTime() {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.layout_image_fixedtime, null);
			second0 = (RadioButton) view.findViewById(R.id.second0);
			second3 = (RadioButton) view.findViewById(R.id.second3);
			second5 = (RadioButton) view.findViewById(R.id.second5);
			String autoPlayTime = getAutoPlayTime();
			
			if(autoPlayTime == null || "".equals(autoPlayTime) || "0".equals(autoPlayTime)) {
				second0.setChecked(true);
			}else if("3".equals(autoPlayTime)) {
				second3.setChecked(true);
			}else if("5".equals(autoPlayTime)) {
				second5.setChecked(true);
			}
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle(R.string.fixedtimetobrowse);
			dialog.setView(view);
			dialog.setNegativeButton(R.string.picCancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {	
							dialog.dismiss();
						}
					})
					.setPositiveButton(R.string.picSetUp,
							new DialogInterface.OnClickListener() {
						
								public void onClick(DialogInterface dialog,
										int which) {
									if(second0.isChecked()) {
										isStop3 = true;
										isStop5 = true;
										setAutoPlayTime("0");							
									}else if(second3.isChecked()) {
										isStop3 = false;
										isStop5 = true;
										setAutoPlayTime(second3.getText().toString());
										handler2.postDelayed(AutoPlay3, 3000);
									}else if(second5.isChecked()) {
										isStop5 = false;
										isStop3 = true;
										setAutoPlayTime(second5.getText().toString());
										handler2.postDelayed(AutoPlay5, 5000);
									}
								}
							}).show();
		}

		//��ʱ����߳�3s
		public Runnable AutoPlay3 = new Runnable() {
			public void run() {
				String time = getAutoPlayTime();
				int playTime = 0;
				if(time != null && !"".equals(time)) {
					playTime = Integer.parseInt(time);
				}else {
					handler2.removeCallbacks(AutoPlay3);
				}
				if(imagePosition != null) {
					int position = Integer.parseInt(imagePosition.get("positionId").toString());
					if (position < imageArray.length - 1) {
						if(!isStop3) {
							imageIndex = position + 1;
							setImageView(imageIndex);		
							showMsg((imageIndex + 1) + "/" + imageArray.length);
							handler2.postDelayed(AutoPlay3, playTime * 1000);
						}else {
							handler2.removeCallbacks(AutoPlay3);
						}	
					} else if (position == imageArray.length - 1) {
						showMsg(R.string.pageEnd);
						setAutoPlayTime("0");
						handler2.removeCallbacks(AutoPlay3);
					}
				}
			}
		};
		
		//��ʱ����߳�5s
				public Runnable AutoPlay5 = new Runnable() {
					public void run() {
						String time = getAutoPlayTime();
						int playTime = 0;
						if(time != null && !"".equals(time)) {
							playTime = Integer.parseInt(time);
						}else {
							handler2.removeCallbacks(AutoPlay5);
						}
						if(imagePosition != null) {
							int position = Integer.parseInt(imagePosition.get("positionId").toString());
							if (position < imageArray.length - 1) {
								if(!isStop5) {
									imageIndex = position + 1;
									setImageView(imageIndex);		
									showMsg((imageIndex + 1) + "/" + imageArray.length);
									handler2.postDelayed(AutoPlay5, playTime * 1000);
								}else {
									handler2.removeCallbacks(AutoPlay5);
								}	
							} else if (position == imageArray.length - 1) {
								showMsg(R.string.pageEnd);
								setAutoPlayTime("0");
								handler2.removeCallbacks(AutoPlay5);
							}
						}
					}
				};
		
		//�����Զ����ʱ��ķ���
		private void setAutoPlayTime(String time) {
			String content = "time=" + time;
			Utils.saveFile(Contents.AUTOSHOWTIME, content, false);
		}
		
		
		//��ʱ����л�ȡ�Զ����ʱ��ķ���
		private String getAutoPlayTime() {
			String content = Utils.getFileRead(Contents.AUTOSHOWTIME);
			if(content != null && content.indexOf("=") != -1) {
				content = content.substring(content.indexOf("=") + 1, content.length());
				return content;
			}
			return null;
		}
		
		//��תͼƬ�ķ���
		private void getPicRotate() {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.layout_image_rotate, null);
			leftRotate = (RadioButton) view.findViewById(R.id.left);
			rightRotate = (RadioButton) view.findViewById(R.id.right);
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle(R.string.picRotate);
			dialog.setView(view);
			dialog.setNegativeButton(R.string.picCancel,
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.setPositiveButton(R.string.picSetUp,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									if(imagePosition != null && imagePosition.size() > 0) {
										String picPath = Utils.getImagePath(imagePosition, imageList);
										if(leftRotate.isChecked()) {
											Bitmap bitmap = Utils.imageRotate("left", 45, picPath);
											if(bitmap != null) {
												setImageView(bitmap);
											}
										}
										if(rightRotate.isChecked()) {
											Bitmap bitmap = Utils.imageRotate("right", 45, picPath);
											if(bitmap != null) {
												setImageView(bitmap);
											}
										}
									}
								}
							}).show();
		}
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	//������ҳ
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			Toast.makeText(this, "�������Ķ�������׽����", Toast.LENGTH_SHORT).show();
			getArrayAtBitmap("right");
		}else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			Toast.makeText(this, "���һ����Ķ�������׽����", Toast.LENGTH_SHORT).show();
			getArrayAtBitmap("left");
		}
		
		return false;
	}
	
	//�����¼�Ӱ��
	public boolean dispatchTouchEvent(MotionEvent event)
	{
	     if(gestureScanner.onTouchEvent(event))
	     {
	            event.setAction(MotionEvent.ACTION_CANCEL);
	     }
	     return super.dispatchTouchEvent(event);
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}


	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}
