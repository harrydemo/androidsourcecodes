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
	
	//重写onPause的方法,保存当前阅读的漫画信息
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
	
	
	
	//缩小按钮的监听
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

	//放大按钮的监听
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
	
	//setImageView方法的重载,用于显示bitmap
	public void setImageView(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
	}
	
	//重写了触屏响应事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	// 获取手机分辨率，根据大小设置高和宽
	public void getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		disWidth = dm.widthPixels;
		disHeight = dm.heightPixels;
	}

	// 加载指定文件夹下的所有图片
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

	// 设置手势事件
	public void doGestureEvent() {
		gestureScanner = new GestureDetector(this);
		gestureScanner
				.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
					public boolean onSingleTapConfirmed(MotionEvent e) {
						if (imageList != null && imageList.size() > 0) {
							// 5秒后自动隐藏，后面实现
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

					// 隐藏菜单，第六阶段实现
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
							//获取到当前图片的路径
							if(imagePosition!=null&&imagePosition.size()>0){
									getArrayAtBitmap("right"); 
							}
						}
						return false;
					}
				});
	}
	
	// 初始化图片信息
	public void initLoadImages() {
		String picPath = getPicPath();
		if (picPath != null) {
			if (imageArray != null && imageArray.length > 0) {
				int i = getPicPosition(picPath, imageArray);
				setImageView(i);
			}
		}
	}

	// 根据intent获得漫画图片的路径
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

	// 设置当前图片的位置
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

	// 设置当前图片大小
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

	// 设置图片信息
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

	// 图片不存在信息
	public void showNoPicMsg() {
		Toast.makeText(this, R.string.noPic, Toast.LENGTH_SHORT).show();
	}

	// 信息提示
	public void showMsg(int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}
	
	// 信息提示的重载
	public void showMsg(String str) {
		Toast.makeText(this, str,Toast.LENGTH_SHORT).show();
	}
	
	//打开SDCard按钮的监听
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
	
	//重写并覆盖掉SHOWTIME文件,防止在切换Activity时的线程运作
	private void setAutoPlayTime(String time) {
		String content = "time=" + time;
		Utils.saveFile(Contents.AUTOSHOWTIME, content, false);
	}
	
	//页面跳转按钮的监听，实现第一页、上一页、下一页、最后一页的跳转
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

	// lastPageButton的监听
	public View.OnClickListener lastPageButton = new View.OnClickListener() {

		public void onClick(View v) {
			getArrayAtBitmap("left");
		}
	};
	// nextPageButton的监听
	public View.OnClickListener nextPageButton = new View.OnClickListener() {

		public void onClick(View v) {
			getArrayAtBitmap("right");
		}
	};

	// 页面跳转的方法
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

	// 保存书签方法
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
	
	//退出按钮的监听
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
	
	//设置按钮的监听
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
		
		//定时浏览
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

		//定时浏览线程3s
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
		
		//定时浏览线程5s
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
		
		//保存自动浏览时间的方法
		private void setAutoPlayTime(String time) {
			String content = "time=" + time;
			Utils.saveFile(Contents.AUTOSHOWTIME, content, false);
		}
		
		
		//定时浏览中获取自动浏览时间的方法
		private String getAutoPlayTime() {
			String content = Utils.getFileRead(Contents.AUTOSHOWTIME);
			if(content != null && content.indexOf("=") != -1) {
				content = content.substring(content.indexOf("=") + 1, content.length());
				return content;
			}
			return null;
		}
		
		//旋转图片的方法
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

	//滑动换页
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			Toast.makeText(this, "向左滑屏的动作被捕捉到了", Toast.LENGTH_SHORT).show();
			getArrayAtBitmap("right");
		}else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
//			Toast.makeText(this, "向右滑屏的动作被捕捉到了", Toast.LENGTH_SHORT).show();
			getArrayAtBitmap("left");
		}
		
		return false;
	}
	
	//消除事件影响
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
