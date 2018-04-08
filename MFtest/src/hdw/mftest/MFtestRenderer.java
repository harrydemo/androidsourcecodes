package hdw.mftest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;


public class MFtestRenderer implements GLSurfaceView.Renderer {
	
	public float eyex;
	public float eyey;
	public float eyez;////这三个坐标指定的位置观看
	
	public float face_color[]={
			1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f , 1.0f,1.0f,0.0f ,// r  x  黄
			0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f, 0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f , 0.0f,0.0f,1.0f ,// u  y  蓝
			1.0f,0.0f,0.0f,  1.0f,0.0f,0.0f , 1.0f,0.0f,0.0f , 1.0f,0.0f,0.0f, 1.0f,0.0f,0.0f,  1.0f,0.0f,0.0f , 1.0f,0.0f,0.0f, 1.0f,0.0f,0.0f , 1.0f,0.0f,0.0f ,// f  z  红
			
			1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f , 1.0f,1.0f,1.0f ,// l -x  白
			0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f , 0.0f,1.0f,0.0f ,// d -y  绿
			1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f , 1.0f,0.5f,0.0f ,// b -z  橘黄
	};
	public int width;
	public int height;//屏幕的尺寸
	
	public int action;//0无动作   1左   2右    3上    4下
	public int move;//F	70		B 66		U 85		D 68		R 82		L76(这个注释恐怕只有我能看懂 - -！)
	public float angle;
	@Override
	public void onDrawFrame(GL10 gl) {
		// TODO Auto-generated method stub
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, eyex, eyey, eyez, 0.0f, 0.0f, 0.0f, 0.0f,1.0f, 0.0f);
		
		//gl.glColor4f(0.63671875f, 0.76953125f, 0.22265625f, 0.0f);
		//wireCube(gl,1.0f,1.5f);//glTranslatef
		//gl.glTranslatef(-1.0f, 1.0f, 1.0f);	
		if(action==0){
			//
			 DrawCube0(gl);
			 DrawCube1(gl);
			 DrawCube2(gl);
			 DrawCube3(gl);
			 DrawCube4(gl);
			 DrawCube5(gl);
			 DrawCube6(gl);
			 DrawCube7(gl);
			 DrawCube8(gl);
			
			 DrawCube9(gl);
			 DrawCube10(gl);
			 DrawCube11(gl);
			 DrawCube12(gl);
			 /////////////中心
			 DrawCube13(gl);
			 DrawCube14(gl);
			 DrawCube15(gl);
			 DrawCube16(gl);

			 DrawCube17(gl);	
			 DrawCube18(gl);
			 DrawCube19(gl);
			 DrawCube20(gl);
			 DrawCube21(gl);
			 DrawCube22(gl);
			 DrawCube23(gl);
			 DrawCube24(gl);
			 DrawCube25(gl);
		 	 gl.glTranslatef(0,0,0);//画26个小正方形
		}
		else{
			DrawRotation(gl);
		}
	 	 
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		this.width=width;
		this.height=height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		if (width<=height)
		{
			gl.glOrthof(-3.0f, 3.0f,(float) (-3.0*((float)height/(float)width)),(float)(3.0*((float)height/(float)width)), 7.0f,-7.0f);//使用正交投影，设置视景体大小
			System.out.println("error");
		}
		else
		{
			//glOrtho(-3.0*(GLfloat)w/(GLfloat)h,3.0*(GLfloat)w/(GLfloat)h,-3.0,3.0,7.0,-7.0);
			gl.glOrthof((float) (-3.0*((float)width/(float)height)),(float)(3.0*((float)width/(float)height)),-3.0f,3.0f, 7.0f,-7.0f);
		}		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		
		eyex=(float)Math.sqrt(3.0);
		eyey=eyex;
		eyez=eyex;//初始化看位置
		action=0;
		gl.glShadeModel(GL10.GL_SMOOTH);// 启用阴影平滑
		gl.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        gl.glClearDepthf(1.0f); 		              // 设置深度缓存    
        gl.glEnable(GL10.GL_DEPTH_TEST);    // 启用深度测试    
        gl.glDepthFunc(GL10.GL_LEQUAL);     // 所作深度测试的类型
    
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST); // 告诉系统对透视进行修正		
	}
	public void wireCube(GL10 gl ,float lineW,	float cubeSize){
		//TODO 画一个线状立方体
		
		FloatBuffer triangleVB;
		float myfloat[]={
				cubeSize,cubeSize,cubeSize,//GL_LINE_LOOP
				cubeSize,cubeSize,-cubeSize,
				cubeSize,-cubeSize,-cubeSize,
				cubeSize,-cubeSize,cubeSize,				
				cubeSize,cubeSize,cubeSize,				
				-cubeSize,cubeSize,cubeSize,
				-cubeSize,cubeSize,-cubeSize,
				-cubeSize,-cubeSize,-cubeSize,
				-cubeSize,-cubeSize,cubeSize,				
				-cubeSize,cubeSize,cubeSize,
				cubeSize,cubeSize,cubeSize,				
				cubeSize,cubeSize,-cubeSize,
				-cubeSize,cubeSize,-cubeSize,				
				-cubeSize,-cubeSize,-cubeSize,
				cubeSize,-cubeSize,-cubeSize,				
				cubeSize,-cubeSize,cubeSize,
				-cubeSize,-cubeSize,cubeSize,
				-cubeSize,cubeSize,cubeSize,//GL_LINE_LOOP
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect( myfloat.length * 4); 
		vbb.order(ByteOrder.nativeOrder());
		triangleVB = vbb.asFloatBuffer();   // create a floating point buffer from the ByteBuffer
        triangleVB.put(myfloat);                // add the coordinates to the FloatBuffer
        triangleVB.position(0);                  // set the buffer to read the first coordinate

		
		gl.glPushMatrix();//当前变换矩阵入栈
		
		gl.glLineWidth(lineW);  
    	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);		
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, triangleVB);
        gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, 18);//画连接线
        
		gl.glPopMatrix();
	}
	private void DrawSquare(GL10 gl,float myfloat[]){
		FloatBuffer triangleVB;
		ByteBuffer vbb = ByteBuffer.allocateDirect( myfloat.length * 4); 
		vbb.order(ByteOrder.nativeOrder());
		triangleVB = vbb.asFloatBuffer();   // create a floating point buffer from the ByteBuffer
        triangleVB.put(myfloat);                // add the coordinates to the FloatBuffer
        triangleVB.position(0);                  // set the buffer to read the first coordinate
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);		
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0,triangleVB);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP , 0, 4);//画GL_TRIANGLE_STRIP
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	private void DrawCube0(GL10 gl){
	 // bottom left // bottom right // top left// top right
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, 1.0f, 1.0f);	
		
		gl.glColor4f(face_color[6], face_color[7], face_color[8], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[51], face_color[52], face_color[53], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[54], face_color[55], face_color[56], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube1(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, 1.0f, 0.0f);	
		
		gl.glColor4f(face_color[42], face_color[43], face_color[44], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[57], face_color[58], face_color[59], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube2(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, 1.0f, -1.0f);	
		
		gl.glColor4f(face_color[81], face_color[82], face_color[83], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[33], face_color[34], face_color[35], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[60], face_color[61], face_color[62], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube3(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, 0.0f, 1.0f);	
		
		gl.glColor4f(face_color[15], face_color[16], face_color[17], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[63], face_color[64], face_color[65], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube4(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, 0.0f, 0.0f);	
		
		gl.glColor4f(face_color[66], face_color[67], face_color[68], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube5(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, 0.0f, -1.0f);	
		
		gl.glColor4f(face_color[90], face_color[91], face_color[92], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[69], face_color[70], face_color[71], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube6(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, -1.0f, 1.0f);	
		
		gl.glColor4f(face_color[24], face_color[25], face_color[26], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[114], face_color[115], face_color[116], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(face_color[72], face_color[73], face_color[74], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube7(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, -1.0f, 0.0f);	
		
		gl.glColor4f(face_color[123], face_color[124], face_color[125], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(face_color[75], face_color[76], face_color[77], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube8(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(1.0f, -1.0f, -1.0f);	
		
		gl.glColor4f(face_color[99], face_color[100], face_color[101], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[132], face_color[133], face_color[134], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(face_color[78], face_color[79], face_color[80], 1.0f);
		DrawSquare(gl,new float[]{//x
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube9(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, 1.0f, 1.0f);	
		
		gl.glColor4f(face_color[3], face_color[4], face_color[5], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[48], face_color[49], face_color[50], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube10(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, 1.0f, 0.0f);	
		
		gl.glColor4f(face_color[39], face_color[40], face_color[41], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube11(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, 1.0f, -1.0f);	
		
		gl.glColor4f(face_color[84], face_color[85], face_color[86], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[30], face_color[31], face_color[32], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube12(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, 0.0f, 1.0f);	
		
		gl.glColor4f(face_color[12], face_color[13], face_color[14], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube13(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, 0.0f, -1.0f);	
		
		gl.glColor4f(face_color[93], face_color[94], face_color[95], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube14(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, -1.0f, 1.0f);	
		
		gl.glColor4f(face_color[21], face_color[22], face_color[23], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[111], face_color[112], face_color[113], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube15(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, -1.0f, 0.0f);	
	
		gl.glColor4f(face_color[120], face_color[121], face_color[122], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube16(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(0.0f, -1.0f, -1.0f);	
		
		gl.glColor4f(face_color[102], face_color[103], face_color[104], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[129], face_color[130], face_color[131], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube17(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, 1.0f, 1.0f);	
		
		gl.glColor4f(face_color[0], face_color[1], face_color[2], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[45], face_color[46], face_color[47], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[141], face_color[142], face_color[143], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube18(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, 1.0f, 0.0f);	
	
		gl.glColor4f(face_color[36], face_color[37], face_color[38], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[138], face_color[139], face_color[140], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube19(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, 1.0f, -1.0f);	
		
		gl.glColor4f(face_color[87], face_color[88], face_color[89], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[27], face_color[28], face_color[29], 1.0f);
		DrawSquare(gl,new float[]{//y
				-0.5f,0.5f,0.5f,
				-0.5f,0.5f,-0.5f,
				0.5f,0.5f,0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[135], face_color[136], face_color[137], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube20(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, 0.0f, 1.0f);	
		
		gl.glColor4f(face_color[9], face_color[10], face_color[11], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[150], face_color[151], face_color[152], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube21(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, 0.0f, 0.0f);	
	
		gl.glColor4f(face_color[147], face_color[148], face_color[149], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube22(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, 0.0f, -1.0f);	
		
		gl.glColor4f(face_color[96], face_color[97], face_color[98], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[144], face_color[145], face_color[146], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube23(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, -1.0f, 1.0f);	
		
		gl.glColor4f(face_color[18], face_color[19], face_color[20], 1.0f);
		DrawSquare(gl,new float[]{//z
				-0.5f,-0.5f,0.5f,			
				-0.5f,0.5f,0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,0.5f,0.5f
		});
		gl.glColor4f(face_color[108], face_color[109], face_color[110], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(face_color[159], face_color[160], face_color[161], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube24(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, -1.0f, 0.0f);	
	
		gl.glColor4f(face_color[117], face_color[118], face_color[119], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(face_color[156], face_color[157], face_color[158], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawCube25(GL10 gl){
		gl.glPushMatrix();//当前变换矩阵入栈
		gl.glTranslatef(-1.0f, -1.0f, -1.0f);	
		
		gl.glColor4f(face_color[105], face_color[106], face_color[107], 1.0f);
		DrawSquare(gl,new float[]{//-z
				-0.5f,-0.5f,-0.5f,			
				-0.5f,0.5f,-0.5f,
				0.5f,-0.5f,-0.5f,
				0.5f,0.5f,-0.5f
		});
		gl.glColor4f(face_color[126], face_color[127], face_color[128], 1.0f);
		DrawSquare(gl,new float[]{//-y
				-0.5f,-0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				0.5f,-0.5f,0.5f,
				0.5f,-0.5f,-0.5f
		});
		gl.glColor4f(face_color[153], face_color[154], face_color[155], 1.0f);
		DrawSquare(gl,new float[]{//-x
				-0.5f,-0.5f,0.5f,
				-0.5f,0.5f,0.5f,
				-0.5f,-0.5f,-0.5f,
				-0.5f,0.5f,-0.5f
		});
		gl.glColor4f(0,0,0, 1.0f);
		wireCube(gl,6.0f,0.5f);
		gl.glPopMatrix();
	}
	private void DrawRotation(GL10 gl){
	
			// 绘制小立方体
			switch (move)
			{
			case 70:
				DrawCube19(gl); DrawCube11(gl); DrawCube2(gl); DrawCube22(gl);
				DrawCube13(gl); DrawCube5(gl); DrawCube25(gl); DrawCube16(gl);
				DrawCube8(gl); DrawCube18(gl); DrawCube10(gl); DrawCube1(gl);
				DrawCube21(gl); DrawCube4(gl); DrawCube24(gl); DrawCube15(gl); DrawCube7(gl);
				gl.glPushMatrix();
				gl.glRotatef(-angle,0.0f,0.0f,1.0f);
				DrawCube0(gl); DrawCube9(gl); DrawCube17(gl); DrawCube3(gl);
				DrawCube12(gl); DrawCube20(gl); DrawCube6(gl); DrawCube14(gl); DrawCube23(gl);
				gl.glPopMatrix();
				break;
			case 66:
				DrawCube0(gl); DrawCube9(gl); DrawCube17(gl); DrawCube3(gl);
				DrawCube12(gl); DrawCube20(gl); DrawCube6(gl); DrawCube14(gl);
				DrawCube23(gl); DrawCube18(gl); DrawCube10(gl); DrawCube1(gl);
				DrawCube21(gl); DrawCube4(gl); DrawCube24(gl); DrawCube15(gl); DrawCube7(gl);
				gl.glPushMatrix();
				gl.glRotatef(angle,0.0f,0.0f,1.0f);
				DrawCube19(gl); DrawCube11(gl); DrawCube2(gl); DrawCube22(gl);
				DrawCube13(gl); DrawCube5(gl); DrawCube25(gl); DrawCube16(gl); DrawCube8(gl);
				gl.glPopMatrix();
				break;
			case 85:
				DrawCube3(gl); DrawCube23(gl);DrawCube24(gl); DrawCube25(gl);
				DrawCube4(gl); DrawCube5(gl); DrawCube6(gl); DrawCube7(gl);
				DrawCube8(gl); DrawCube12(gl); DrawCube13(gl); DrawCube14(gl);
				DrawCube15(gl);DrawCube16(gl);DrawCube20(gl); DrawCube21(gl); DrawCube22(gl); 
				gl.glPushMatrix();
				gl.glRotatef(-angle,0.0f,1.0f,0.0f);
				DrawCube0(gl); DrawCube1(gl); DrawCube2(gl);DrawCube9(gl);
				DrawCube10(gl); DrawCube11(gl); DrawCube17(gl); DrawCube18(gl); DrawCube19(gl);
				gl.glPopMatrix();
				break;
			case 68:
				DrawCube0(gl); DrawCube1(gl); DrawCube2(gl); DrawCube3(gl);
				DrawCube4(gl); DrawCube5(gl);  DrawCube9(gl); DrawCube10(gl); 
				DrawCube11(gl);DrawCube12(gl); DrawCube13(gl); DrawCube17(gl); 
				DrawCube18(gl); DrawCube19(gl);DrawCube20(gl); DrawCube21(gl); DrawCube22(gl);
				gl.glPushMatrix();
				gl.glRotatef(angle,0.0f,-1.0f,0.0f);
				DrawCube6(gl); DrawCube7(gl); DrawCube8(gl);DrawCube14(gl); 
				DrawCube15(gl);DrawCube16(gl);  DrawCube23(gl);DrawCube24(gl); DrawCube25(gl);
				gl.glPopMatrix();
				break;			
			case 82:
				 DrawCube9(gl); DrawCube10(gl); DrawCube11(gl); DrawCube25(gl);
				DrawCube12(gl); DrawCube13(gl); DrawCube14(gl); DrawCube15(gl);
				DrawCube16(gl); DrawCube17(gl); DrawCube18(gl); DrawCube19(gl);
				DrawCube20(gl); DrawCube21(gl); DrawCube22(gl); DrawCube23(gl);DrawCube24(gl); 
				gl.glPushMatrix();
				gl.glRotatef(-angle,1.0f,0.0f,0.0f);
				DrawCube0(gl); DrawCube1(gl); DrawCube2(gl); DrawCube3(gl);
				DrawCube4(gl); DrawCube5(gl); DrawCube6(gl); DrawCube7(gl); DrawCube8(gl);
				gl.glPopMatrix();
				break;			
			case 76:
				DrawCube0(gl); DrawCube1(gl); DrawCube2(gl); DrawCube3(gl);
				DrawCube4(gl); DrawCube5(gl); DrawCube6(gl); DrawCube7(gl);
				DrawCube8(gl); DrawCube9(gl); DrawCube10(gl); DrawCube11(gl);
				DrawCube12(gl); DrawCube13(gl); DrawCube14(gl); DrawCube15(gl); DrawCube16(gl); 
				gl.glPushMatrix();
				gl.glRotatef(-angle,-1.0f,0.0f,0.0f);
				DrawCube17(gl); DrawCube18(gl); DrawCube19(gl); DrawCube20(gl);
				DrawCube21(gl); DrawCube22(gl); DrawCube23(gl);DrawCube24(gl); DrawCube25(gl);
				gl.glPopMatrix();
				break;
			default:
				break;
			}
	}
	
}
