package com.javacc.android.piano;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;
import android.view.View;
import android.view.MotionEvent;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * @author: ZhangFL
 */
public class Piano extends Activity {

    private ImageButton imageButton_white1;
    private ImageButton imageButton_white2;
    private ImageButton imageButton_white3;
    private ImageButton imageButton_white4;
    private ImageButton imageButton_white5;
    private ImageButton imageButton_white6;
    private ImageButton imageButton_white7;
    private ImageButton imageButton_white8;

    private ImageButton imageButton_black1;
    private ImageButton imageButton_black2;
    private ImageButton imageButton_black3;
    private ImageButton imageButton_black4;
    private ImageButton imageButton_black5;
    private MediaPlayer mediaPlayer01;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.main);

        mediaPlayer01 = new MediaPlayer();
//        mediaPlayer01 = MediaPlayer.create(Piano.this, R.raw.white1);

        imageButton_white1 = (ImageButton) findViewById(R.id.white1);
        imageButton_white2 = (ImageButton) findViewById(R.id.white2);
        imageButton_white3 = (ImageButton) findViewById(R.id.white3);
        imageButton_white4 = (ImageButton) findViewById(R.id.white4);
        imageButton_white5 = (ImageButton) findViewById(R.id.white5);
        imageButton_white6 = (ImageButton) findViewById(R.id.white6);
        imageButton_white7 = (ImageButton) findViewById(R.id.white7);
        imageButton_white8 = (ImageButton) findViewById(R.id.white8);

        imageButton_black1 = (ImageButton) findViewById(R.id.black1);
        imageButton_black2 = (ImageButton) findViewById(R.id.black2);
        imageButton_black3 = (ImageButton) findViewById(R.id.black3);
        imageButton_black4 = (ImageButton) findViewById(R.id.black4);
        imageButton_black5 = (ImageButton) findViewById(R.id.black5);

        imageButton_white1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white1);
                    imageButton_white1.setImageResource(R.drawable.whiteback1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white1.setImageResource(R.drawable.white1);
                }
                return false;
            }
        });


        imageButton_white2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    play(R.raw.white2);
                    imageButton_white2.setImageResource(R.drawable.whiteback2);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white2.setImageResource(R.drawable.white2);
                }
                return false;
            }
        });
//
        imageButton_white3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white3);
                    imageButton_white3.setImageResource(R.drawable.whiteback3);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white3.setImageResource(R.drawable.white3);
                }
                return false;
            }
        });

        imageButton_white4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white4);
                    imageButton_white4.setImageResource(R.drawable.whiteback4);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white4.setImageResource(R.drawable.white4);
                }
                return false;
            }
        });

        imageButton_white5.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white5);
                    imageButton_white5.setImageResource(R.drawable.whiteback5);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white5.setImageResource(R.drawable.white5);
                }
                return false;
            }
        });

        imageButton_white6.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white6);
                    imageButton_white6.setImageResource(R.drawable.whiteback6);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white6.setImageResource(R.drawable.white6);
                }
                return false;
            }
        });

        imageButton_white7.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white7);
                    imageButton_white7.setImageResource(R.drawable.whiteback7);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white7.setImageResource(R.drawable.white7);
                }
                return false;
            }
        });

        imageButton_white8.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.white8);
                    imageButton_white8.setImageResource(R.drawable.whiteback8);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_white8.setImageResource(R.drawable.white8);
                }
                return false;
            }
        });

        imageButton_black1.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.black1);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black1.setImageResource(R.drawable.black1);
                }
                return false;
            }
        });

        imageButton_black2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.black2);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black2.setImageResource(R.drawable.black2);
                }
                return false;
            }
        });

        imageButton_black3.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.black3);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black3.setImageResource(R.drawable.black3);
                }
                return false;
            }
        });

        imageButton_black4.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.black4);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black4.setImageResource(R.drawable.black4);
                }
                return false;
            }
        });

        imageButton_black5.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    play(R.raw.black5);
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageButton_black5.setImageResource(R.drawable.black5);
                }
                return false;
            }
        });
//
//
        mediaPlayer01.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer arg0) {
                mediaPlayer01.release();
                mediaPlayer01 = null;
                Toast.makeText(Piano.this, "资源释放了!", Toast.LENGTH_SHORT).show();
            }
        });

        mediaPlayer01.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer arg0, int i, int i1) {
                try {
                    mediaPlayer01.release();
                    Toast.makeText(Piano.this, "发生错误了!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });


    }

    //-------------------------------------------------------------------------------------
    private void play(int resource) {
        try {

            mediaPlayer01.release();
            mediaPlayer01 = MediaPlayer.create(Piano.this, resource);
            mediaPlayer01.start();
        } catch (Exception e) {
            Toast.makeText(Piano.this, "发生错误了:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer01 != null) {
            mediaPlayer01.release();
            mediaPlayer01 = null;
        }

    }

}
