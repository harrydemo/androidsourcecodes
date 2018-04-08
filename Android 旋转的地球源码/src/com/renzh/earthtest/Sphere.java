package com.renzh.earthtest;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLU;

/**
 * A sphere object representation.
 * 
 */
public class Sphere {
	/* buffer holding vertex data */
	private FloatBuffer mVertexBuffer;
	/* buffer holding index data */
	private ShortBuffer mIndexBuffer;
	/* buffer holding texture coordinate data */
	private FloatBuffer mUVBuffer;
	/* VBO for vertex */
	private GLVBO mVertexVBO;
	/* VBO for index */
	private GLVBO mIdxVBO;
	/* VBO for texture coordinates*/
	private GLVBO mTexVBO;
	/* rows of the sphere grids */
	private int rows;
	/* columns of the sphere grids */
	private int cols;
	/* radius of the sphere */
	private float radius;
	/* total points */
	private int mCount;
   private float roatX=0,roatY=0;
   

	public float getRoatX() {
	return roatX;
}

public void setRoatX(float roatX) {
	this.roatX = roatX;
}

public float getRoatY() {
	return roatY;
}

public void setRoatY(float roatY) {
	this.roatY = roatY;
}

	public Sphere(float radiu, int row, int col) {
		this.radius = radiu;
		this.rows = row;
		this.cols = col;
		mCount = (row + 1) * (col + 1);
		initBuffer();
		build();
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);
		mUVBuffer.position(0);

		mVertexVBO = new GLVBO(GLVBO.VBO_VERTEX, mVertexBuffer);
		mIdxVBO = new GLVBO(GLVBO.VBO_INDEX, mIndexBuffer);
		mTexVBO = new GLVBO(GLVBO.VBO_TEXTURE, mUVBuffer);
	}

	/* initialize the buffers */
	private void initBuffer() {
		mVertexBuffer = BufferFactory.createFloatBuffer(mCount * 3);
		mIndexBuffer = BufferFactory.createShortBuffer(rows * cols * 6);
		mUVBuffer = BufferFactory.createFloatBuffer(mCount * 2);
	}

	/* build the sphere */
	private void build() {
		int r, c;
		Number3d n = new Number3d();
		Number3d pos = new Number3d();
		//类似二重积分，外面一层循环相当于在Z轴上由0 到PI上积分。n 这个点会绕z轴由0到PI渐变。
		for (r = 0; r <= rows; r++) {
			float v = (float) r / (float) rows; // [0,1]  //把纹理图片也按这个行列分割，v相当于这个纹理图片的第几行。当然总行数被认定为是1。
			float theta1 = v * (float) Math.PI; // [0,PI]
			n.setAll(0, 1, 0);
			n.rotateZ(theta1);
	    //内部循环相当于是在Y轴上由0到2*PI上渐变。这样在外重积分的基础上n这个点就会绕着Y轴由0到2*PI渐变。
			//通过这二重积分 那么n这个点就会隔相同的间距遍历球体上的所有点。 当这个间距越小，遍历点就越多，越近似一个圆。
			for (c = 0; c <= cols; c++) {//第一次循环就可以确定一个不能的点，所有的循环结束，这些不同的点就均匀的覆盖了球体。
				float u = (float) c / (float) cols; // [0,1]
				float theta2 = u * (float) (Math.PI * 2);// [0,2*PI];//u相当于纹理图片的列。
				pos.setAllFrom(n);
				pos.rotateY(theta2);
				pos.multiply(radius);
				mVertexBuffer.put(pos.x);
				mVertexBuffer.put(pos.y);
				mVertexBuffer.put(pos.z);
				mUVBuffer.put(u);
				mUVBuffer.put(v);
			}
		}

		//上面是rows+1行和cols+1列，这是因为球体是封闭的，多出的一行一列相当于闭合处，闭合片的点可以认为都重合了
		//下面遍历不是到rows+1行和cols+1列止，这是因为下面是确定正方形，当到了第rows行时，正方形的底面已经是第rows+1行了。cols+1也是相当于在
		//第rows列是正方形的右边已经到了cols+1列。 下面的构造有序正方形可以在图纸上模拟出来，确定很有序列。
		int colLength = cols + 1;
		for (r = 0; r < rows; r++) {
			int offset = r * colLength;
			for (c = 0; c < cols; c++) {//每次循环会在风格上确定一个有序的正方形面。
				int ul = offset + c;
				int ur = offset + c + 1;
				int br = offset + c + 1 + colLength;
				int bl = offset + c + colLength;
				//四个顶点可以确定一个正方形，不过在opengl中这个正方形要分两个三角形画出。
				mIndexBuffer.put((short) ul);
				mIndexBuffer.put((short) br);
				mIndexBuffer.put((short) ur);
				mIndexBuffer.put((short) ul);
				mIndexBuffer.put((short) bl);
				mIndexBuffer.put((short) br);
			}
		}
	}
	
	public void release(){
		mVertexVBO.release();
		mIdxVBO.release();
		mTexVBO.release();
	}

	public void draw(GL11 gl, GLTexture tex1, GLTexture tex2, float u, float v) {
		
		if (tex1 != null) {
			gl.glActiveTexture(GL10.GL_TEXTURE0);
			gl.glClientActiveTexture(GL10.GL_TEXTURE0);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			tex1.bind(gl);
			mTexVBO.bind(gl);
			gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_MODULATE);
		}

		if (tex2 != null) {
			gl.glActiveTexture(GL10.GL_TEXTURE1);
			gl.glClientActiveTexture(GL10.GL_TEXTURE1);
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			tex2.bind(gl);
			mTexVBO.bind(gl);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
					GL10.GL_DECAL);
			if (u != 0 || v != 0) {
				gl.glMatrixMode(GL10.GL_TEXTURE);
				gl.glLoadIdentity();
				gl.glTranslatef(u, v, 0);
				gl.glMatrixMode(GL10.GL_MODELVIEW);
			}
		}

		mVertexVBO.bind(gl);
		gl.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
		mIdxVBO.bind(gl);
		// initMaterialWhite(gl);
        //设置缓冲区，球面上的顶点法线向量与顶点坐标相同
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glRotatef(roatX, 1, 0, 0);
		gl.glRotatef(roatY, 0, 1, 0);
		gl.glDrawElements(GL11.GL_TRIANGLES, mIndexBuffer.capacity(),//
				GL10.GL_UNSIGNED_SHORT, 0);
		
		mIdxVBO.unBind();
		mVertexVBO.unBind();
		mTexVBO.unBind();
		
		if (tex2 != null) {
			tex2.unBind();
			gl.glActiveTexture(GL10.GL_TEXTURE1);
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glClientActiveTexture(GL10.GL_TEXTURE1);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}

		if (tex1 != null) {
			tex1.unBind();
			gl.glActiveTexture(GL10.GL_TEXTURE0);
			gl.glClientActiveTexture(GL10.GL_TEXTURE0);
			gl.glMatrixMode(GL10.GL_TEXTURE);
			gl.glLoadIdentity();
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
	}
	
	/* 
     *  
     * 设置材质对于环境光，散射光，镜面光的反射能力，白色材质能反射所有光，其他的对应于自己的颜色*/  
    private void initMaterialWhite(GL10 gl){  
        float [] ambientMaterial = {0.4f,0.4f,0.4f,1.0f};//环境光位白色材质  
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientMaterial, 0);  
        float [] diffuseMaterial = {0.8f,0.8f,0.8f,1.0f};//散射光为白色材质  
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseMaterial, 0);  
        float [] specularMaterial = {1.0f,1f,1f,1.0f};//高光材质为白色  
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, specularMaterial, 0);  
        float []shininessMaterial = {1.5f};//高光反射区域，数越大高亮区域越小，越暗  
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, shininessMaterial, 0);  
          
         float []emission = {0.0f,0.0f,1f,1.0f};//蓝色自发光，开启这个后，球体会自身发出蓝色光  
         gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, emission, 0);  
     
         float LightAmbient[]={0.5f,0.5f,0.5f,1.0f};  //  环境光的值。
        float LighDiffuse[]={1.0f,1.0f,1.0f,1.0f};    //散射光的值。
        float LightPosition[]={0.0f,0.0f,1.0f,1.0f};   //光照位置。
     
         gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_AMBIENT,LightAmbient,0);//   设置环境光源。
         gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_DIFFUSE,LighDiffuse,0);//    设置漫反射光源。
         gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_POSITION,LightPosition,0);//  设置光源位置。
         gl.glEnable(GL10.GL_LIGHTING);
        // gl.glEnable(GL10.GL_LIGHT0);
    }
	
}
