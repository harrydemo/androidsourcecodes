package com.test;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MusicActivity extends Activity {

	/* 声明一个 ImageButton,TextView,MediaPlayer变量 */
	private ImageButton mButton01, mButton02, mButton03;
	private TextView mTextView01;
	private MediaPlayer mMediaPlayer01;
	/* 声明一个Flag作为确认音乐是否暂停的变量并默认为false */
	private boolean bIsPaused = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 通过findViewById构造器创建TextView与ImageView对象 */
		mButton01 = (ImageButton) findViewById(R.id.myButton1);
		mButton02 = (ImageButton) findViewById(R.id.myButton2);
		mButton03 = (ImageButton) findViewById(R.id.myButton3);
		mTextView01 = (TextView) findViewById(R.id.myTextView1);

		/* onCreate时创建MediaPlayer对象 */
		mMediaPlayer01 = new MediaPlayer();
		/* 将音乐以Import的方式存储在res/raw/always.mp3 */
		mMediaPlayer01 = MediaPlayer.create(MusicActivity.this, R.raw.big);

		/* 运行播放音乐的按钮 */
		mButton01.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			/* 覆盖OnClick事件 */
			public void onClick(View v) {
				try {

					if (mMediaPlayer01 != null) {
						mMediaPlayer01.stop();
					}
					/*
					 * 在MediaPlayer取得播放资源与stop()之后
					 * 要准备Playback的状态前一定要使用MediaPlayer.prepare()
					 */
					mMediaPlayer01.prepare();
					/* 开始或回复播放 */
					mMediaPlayer01.start();
					/* 改变TextView为开始播放状态 */
					mTextView01.setText(R.string.str_start);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					mTextView01.setText(e.toString());
					e.printStackTrace();
				}
			}
		});

		/* 停止播放 */
		mButton02.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					if (mMediaPlayer01 != null) {
						/* 停止播放 */
						mMediaPlayer01.stop();
						/* 改变TextView为停止播放状态 */
						mTextView01.setText(R.string.str_close);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					mTextView01.setText(e.toString());
					e.printStackTrace();
				}
			}
		});

		/* 暂停播放 */
		mButton03.setOnClickListener(new ImageButton.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					if (mMediaPlayer01 != null) {
						/* 是否为暂停状态=否 */
						if (bIsPaused == false) {
							/* 暂停播放 */
							mMediaPlayer01.pause();
							/* 设置Flag为treu表示 Player状态为暂停 */
							bIsPaused = true;
							/* 改变TextView为暂停播放 */
							mTextView01.setText(R.string.str_pause);
						}
						/* 是否为暂停状态=是 */
						else if (bIsPaused == true) {
							/* 回复播出状态 */
							mMediaPlayer01.start();
							/* 设置Flag为false表示 Player状态为非暂停状态 */
							bIsPaused = false;
							/* 改变TextView为开始播放 */
							mTextView01.setText(R.string.str_start);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					mTextView01.setText(e.toString());
					e.printStackTrace();
				}
			}
		});

		/* 当MediaPlayer.OnCompletionLister会运行的Listener */
		mMediaPlayer01
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
					// @Override
					/* 覆盖文件播出完毕事件 */
					public void onCompletion(MediaPlayer arg0) {
						try {
							/*
							 * 解除资源与MediaPlayer的赋值关系 让资源可以为其它程序利用
							 */
							mMediaPlayer01.release();
							/* 改变TextView为播放结束 */
							mTextView01
									.setText(R.string.str_OnCompletionListener);
						} catch (Exception e) {
							mTextView01.setText(e.toString());
							e.printStackTrace();
						}
					}
				});

		/* 当MediaPlayer.OnErrorListener会运行的Listener */
		mMediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			/* 覆盖错误处理事件 */
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				// TODO Auto-generated method stub
				try {
					/* 发生错误时也解除资源与MediaPlayer的赋值 */
					mMediaPlayer01.release();
					mTextView01.setText(R.string.str_OnErrorListener);
				} catch (Exception e) {
					mTextView01.setText(e.toString());
					e.printStackTrace();
				}
				return false;
			}
		});
	}

	@Override
	/* 覆盖主程序暂停状态事件 */
	protected void onPause() {
		// TODO Auto-generated method stub
		try {
			/* 再主程序暂停时解除资源与MediaPlayer的赋值关系 */
			mMediaPlayer01.release();
		} catch (Exception e) {
			mTextView01.setText(e.toString());
			e.printStackTrace();
		}
		super.onPause();
	}
}
