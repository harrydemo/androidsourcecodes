package com.macrocheng.cubeopengl;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;

/**
 * http://www.zeuscmd.com/tutorials/opengles/20-Transparency.php
 * @author bburns
 *
 */
public class GLTutorialCube extends GLTutorialBase  {
    float lightAmbient[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float lightDiffuse[] = new float[] { 0.6f, 0.6f, 0.6f, 1.0f };
	float[] lightPos = new float[] {0,0,3,1};

	float matAmbient[] = new float[] { 1f, 1f, 1f, 1.0f };
	float matDiffuse[] = new float[] { 1f, 1f, 1f, 1.0f };

	int tex;
	Bitmap[] bmp = new Bitmap[6];
	int[] tmp_tex = new int[6];

	float box[][] = new float[][] {
	        {
			-0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
	        },

	        {
			-0.5f, -0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
	        },

	        {
			-0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
	        },

	        {
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
	        },

	        {
			-0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			 -0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
	        },

	        {
			-0.5f, -0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f, -0.5f,
	        },
		};

	float texCoords[][] = new float[][] {
	        {
			 0.0f, 0.0f,
			 1.0f, 0.0f,
			 0.0f, 1.0f,
			 1.0f, 1.0f,
	        },

	        {
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
	        },

	        {
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
	        },

	        {
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
	        },

	        {
			 0.0f, 0.0f,
			 1.0f, 0.0f,
			 0.0f, 1.0f,
			 1.0f, 1.0f,
	        },

	        {
			// BOTTOM
			 1.0f, 0.0f,
			 1.0f, 1.0f,
			 0.0f, 0.0f,
			 0.0f, 1.0f,
	        }
		};

	FloatBuffer[] cubeBuff = new FloatBuffer[6];
	FloatBuffer[] texBuff = new FloatBuffer[6];

	public GLTutorialCube(Context c) {
		super(c, 20);

		for(int i = 0;i<box.length;i++)
		{
    		cubeBuff[i] = makeFloatBuffer(box[i]);
    		texBuff[i] = makeFloatBuffer(texCoords[i]);
		}

		bmp[0] = BitmapFactory.decodeResource(c.getResources(), R.drawable.aa);
		bmp[1] = BitmapFactory.decodeResource(c.getResources(), R.drawable.bb);
		bmp[2] = BitmapFactory.decodeResource(c.getResources(), R.drawable.cc);
		bmp[3] = BitmapFactory.decodeResource(c.getResources(), R.drawable.dd);
		bmp[4] = BitmapFactory.decodeResource(c.getResources(), R.drawable.ee);
		bmp[5] = BitmapFactory.decodeResource(c.getResources(), R.drawable.ff);

		setFocusable(true);
	}

	protected void init(GL10 gl) {
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, matAmbient, 0);
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, matDiffuse, 0);

		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient,	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse,	0);
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);

		gl.glEnable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_DEPTH_TEST);
//		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);

		gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepthf(1.0f);


		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);


		gl.glGenTextures(6, tmp_tex, 0);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        for(int i = 0; i < 6; i++)
        {
            gl.glBindTexture(GL10.GL_TEXTURE_2D, tmp_tex[i]);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp[i], 0);
        }
	}

	float xrot = 45.0f;
	float yrot = 45.0f;

	protected void drawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, 0, 0, 3, 0, 0, 0, 0, 1, 0);

		gl.glRotatef(xrot, 1, 0, 0);
		gl.glRotatef(yrot, 0, 1, 0);


		 for(int i = 0; i < 6; i++)
	        {
	            gl.glBindTexture(GL10.GL_TEXTURE_2D, tmp_tex[i]);
	            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, cubeBuff[i]);
	            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuff[i]);
	            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
	        }

		 if(flag == 1)
        {
            xrot += 2.0f;

        }else if(flag == 2)
        {
            xrot -= 2.0f;
        }else if(flag == 3)
        {
            yrot += 2.0f;
        }else if(flag == 4)
        {
            yrot -= 2.0f;
        }

        if(flag == 1 || flag == 2)
        if((int)(xrot%90)==0)
        {
            flag = 0;
        }
        if(flag == 3 || flag == 4)
        if((int)(yrot%90) ==0)
        {
            flag =0;
        }
	}

	float _x;
    float _y;
    int flag;
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
            _y = event.getY();
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE)
        {
            yrot += (_x - event.getX())/10;
            xrot += (_y - event.getY())/10;
        }
        return true;
    }

}