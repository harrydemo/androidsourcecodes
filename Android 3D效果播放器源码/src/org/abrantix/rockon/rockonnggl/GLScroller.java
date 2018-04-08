package org.abrantix.rockon.rockonnggl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLScroller {
		
		private final String TAG = "GLScroller";

		public void setTextureId(int textureId)
		{
			mTextureId = textureId;
		}
		
	    public GLScroller(int textureId, float height) {

	    	/** 
	    	 * Save the texture ids
	    	 */
	    	mTextureId = textureId;
	    	
	    	
	    	float h = height/2.f;
	    	/**
	    	 * 1ST FACE SET
	    	 */
	    	// FACE 0
	        float[] coordsArray = {
	        		// X, Y, Z
	        		-h/4.f, h, 0.f,
	        		h/4.f, h, 0.f, 
	        		h/4.f, -h, 0.f,
	        		-h/4.f, -h, 0.f
//	        		-1.f, 1.f, 0.f,
//	        		1.f, 1.f, 0.f, 
//	        		1.f, -1.f, 0.f,
//	        		-1.f, -1.f, 0.f
	        };
//	        faceCoordsArray.add(coords0);
	        float[] normArray = {
	        	0.f, 0.f, -1.f	
	        };
//	        faceNormArray.add(norm0);
	        
	        /**
	         * Texture coordinates
	         */
	        float[] textCoords = {
		        		0.f, 1.f,
		        		1.f, 1.f,
		        		1.f, 0.f,
		        		0.f, 0.f
	        };
	        
	    	/**
	    	 * Generate our openGL buffers with the 
	    	 * vertice and texture coordinates 
	    	 * and drawing indexes
	    	 * VERTICAL 
	    	 */
	        // Buffers to be passed to gl*Pointer() functions
	        // must be direct, i.e., they must be placed on the
	        // native heap where the garbage collector cannot
	        // move them.
	        //
	        // Buffers with multi-byte datatypes (e.g., short, int, float)
	        // must have their byte order set to native order

	        ByteBuffer vbb = ByteBuffer.allocateDirect(VERTS * 3 * 4); // verts * ncoords * bytes per vert??
	        vbb.order(ByteOrder.nativeOrder());
	        mFVertexBuffer = vbb.asFloatBuffer();

	        ByteBuffer tbb = ByteBuffer.allocateDirect(VERTS * 2 * 4);
	        tbb.order(ByteOrder.nativeOrder());
	        mTexBuffer = tbb.asFloatBuffer();
	        
	        ByteBuffer nbb = ByteBuffer.allocateDirect(3 * 4);
	        nbb.order(ByteOrder.nativeOrder());
	        mNormalBuffer = nbb.asFloatBuffer();
	        
	        ByteBuffer ibb = ByteBuffer.allocateDirect(VERTS * 2);
	        ibb.order(ByteOrder.nativeOrder());
	        mIndexBuffer = ibb.asShortBuffer();

	        for (int i = 0; i < VERTS; i++) {
	            for(int j = 0; j < 3; j++) {
	            	mFVertexBuffer.put(coordsArray[i*3+j]);
	            }
	        }
	        
	        for(int i = 0; i < 3; i++)
	        	mNormalBuffer.put(normArray[i]);
	        
	        mTexBuffer.put(textCoords);

	        for(int i = 0; i < VERTS; i++) {
	            mIndexBuffer.put((short) i);
	        }

	        mFVertexBuffer.position(0);
	        mTexBuffer.position(0);
	        mNormalBuffer.position(0);
	        mIndexBuffer.position(0);
		}

	    /* optimization */
//	    int	x;
	    int y;
	    public void draw(GL10 gl) {
	    	
	    	/**
	    	 * Vertical scrolling, only draw covers
	    	 */
			gl.glActiveTexture(GL10.GL_TEXTURE0);
	        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
	        gl.glTexParameterx(
	        		GL10.GL_TEXTURE_2D, 
	        		GL10.GL_TEXTURE_WRAP_S,
	                GL10.GL_REPEAT);
	        gl.glTexParameterx(
	        		GL10.GL_TEXTURE_2D, 
	        		GL10.GL_TEXTURE_WRAP_T,
	                GL10.GL_REPEAT);
	    	
	        gl.glFrontFace(GL10.GL_CCW);
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
	        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
	        gl.glEnable(GL10.GL_TEXTURE_2D);
	        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
	        gl.glDrawElements(
//	        		GL10.GL_TRIANGLE_STRIP,
	        		GL10.GL_TRIANGLE_FAN,
//	        		GL10.GL_LINE_LOOP,
	        		VERTS,
	                GL10.GL_UNSIGNED_SHORT,
	                mIndexBuffer);
	    }

	    
	    private final static int 	VERTS = 4;
		private final int 			pointsPerFace = 4;
	    
		/* vertical scrolling buffers */
		private FloatBuffer mFVertexBuffer;
	    private FloatBuffer mTexBuffer;
	    private FloatBuffer mNormalBuffer;
	    private ShortBuffer mIndexBuffer;
	    
	    /** our textures id */
	    public	int mTextureId;    
	
}
