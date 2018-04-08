package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.stickycoding.rokon.vbo.ArrayVBO;

/**
 * GLHelper.java
 * Contains various functions that help minimize and optimize OpenGL calls and state changes.
 * The aim is to have all OpenGL calls were made from this class.
 * 
 * @author Richard
 */

public class GLHelper {
	
	private static GL10 gl;
	private static boolean glVertexArray, glTexCoordArray, glTexture2D, glColorArray;
	private static int textureIndex = -1, arrayBuffer = -1, elementBuffer = -1, srcBlendMode = -1, dstBlendMode = -1;
	private static float glColor4fRed = -1, glColor4fGreen = -1, glColor4fBlue = -1, glColor4fAlpha = -1;
    private static BufferObject lastVertexPointerBuffer;
    private static BufferObject lastTexCoordPointerBuffer;
    private static ColourBuffer lastColourBufferObject;
    private static float lineWidth;
    
    public static void enableColourArray() {
    	if(!glColorArray) {
    		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    		glColorArray = true;
    	}
    }
    
    public static void disableColourArray() {
    	if(glColorArray) {
    		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    		glColorArray = false;
    	}
    }
    
    public static void colourPointer(ColourBuffer colourBuffer) {
    	if(lastColourBufferObject != colourBuffer) {
    		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colourBuffer.get());
            lastColourBufferObject = colourBuffer;
    	}
    }
    
    /**
     * Resets what GLHelper thinks is going on, forces everything to be put through
     */
    public static void reset() {
    	glVertexArray = false;
    	glTexCoordArray = false;
    	glTexture2D = false;
    	textureIndex = -1;
    	arrayBuffer = -1;
    	elementBuffer = -1;
    	srcBlendMode = -1;
    	dstBlendMode = -1;
    	glColor4fRed = -1;
    	glColor4fGreen = -1;
    	glColor4fBlue = -1;
    	glColor4fAlpha = -1;
    	lastVertexPointerBuffer = null;
    	lastTexCoordPointerBuffer = null;
    	lastColourBufferObject = null;
    	glColorArray = false;
    	lineWidth = 1;
    }
    
    /**
     * Sets the line width
     * 
     * @param lineWidth float value, > 0
     */
    public static void lineWidth(float lineWidth) {
    	if(lineWidth == GLHelper.lineWidth) return;
    	GLHelper.lineWidth = lineWidth;
    	gl.glLineWidth(lineWidth);
    }
	
	protected static void setGL(GL10 gl) {
		GLHelper.gl = gl;
	}
	
	/**
	 * Enables the GL_VERTEX_ARRAY state
	 */
	public static void enableVertexArray() {
		if(!glVertexArray) {
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			glVertexArray = true;
		}
	}
	
	/**
	 * Disables the GL_VERTEX_ARRAY state
	 */
	public static void disableVertexArray() {
		if(glVertexArray) {
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			glVertexArray = false;
		}
	}
	
	/**
	 * Enables the GL_TEXTURE_COORD_ARRAY state
	 */
	public static void enableTexCoordArray() {
		if(!glTexCoordArray) {
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			glTexCoordArray = true;
		}
	}
	
	/**
	 * Disables the GL_TEXTURE_COORD_ARRAY state
	 */
	public static void disableTexCoordArray() {
		if(glTexCoordArray) {
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			glTexCoordArray = false;
		}
	}
	
	/**
	 * Enables the GL_TEXTURE_2D state
	 */
	public static void enableTextures() {
		if(!glTexture2D) {
			gl.glEnable(GL10.GL_TEXTURE_2D);
			glTexture2D = true;
		}
	}
	
	/**
	 * Disables the GL_TEXTURE_2D state
	 */
	public static void disableTextures() {
		if(glTexture2D) {
			gl.glDisable(GL10.GL_TEXTURE_2D);
			glTexture2D = false;
		}
	}
	
	/**
	 * Binds a GL_ARRAY_BUFFER
	 * 
	 * @param bufferIndex index of the buffer to bind
	 */
	public static void bindBuffer(int bufferIndex, boolean force) {
		if(bufferIndex != arrayBuffer || force) {
			((GL11)gl).glBindBuffer(GL11.GL_ARRAY_BUFFER, bufferIndex);
			arrayBuffer = bufferIndex;
		}
	}
	
	/**
	 * Binds a GL_ELEMENT_ARRAY_BUFFER
	 * 
	 * @param elementBufferIndex index of the buffer to bind
	 */
	public static void bindElementBuffer(int elementBufferIndex) {
		if(elementBufferIndex != elementBuffer) {
			((GL11)gl).glBindBuffer(GL11.GL_ELEMENT_ARRAY_BUFFER, elementBufferIndex);
			elementBuffer = elementBufferIndex;
		}
	}
	
	/**
	 * Binds a texture, given by texture index
	 * 
	 * @param textureIndex index of the texture to bind
	 */
	public static void bindTexture(int textureIndex) {
		if(GLHelper.textureIndex != textureIndex) {
			Debug.print("@@ bind " + textureIndex);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIndex);
			GLHelper.textureIndex = textureIndex;
		}
	}
	
	/**
	 * Binds a Texture
	 * 
	 * @param texture valid Texture object
	 */
	public static void bindTexture(Texture texture) {
		checkTextureValid(texture);
		bindTexture(texture.textureIndex);
	}
	
	/**
	 * Checks whether a Texture has been loaded onto the hardware
	 * If it hasn't, it loads
	 * 
	 * @param texture valid Texture object
	 */
	public static void checkTextureValid(Texture texture) {
		if(texture.textureIndex == -1 && texture.parentAtlas == null) {
			texture.onLoadTexture(gl);
			Debug.print("checkTextureValid - load self");
			Debug.print("we're at " + texture.textureIndex);
		} else {
			if(texture.textureIndex == -1) {
				texture.parentAtlas.onLoadTexture(gl);
				Debug.print("checkTextureValid - load parent");
				Debug.print("we're at " + texture.textureIndex);
			}
		}
	}
	
	/**
	 * Sets the glBlendFunc, by custom parameters
	 * 
	 * @param srcBlendMode
	 * @param dstBlendMode
	 */
	public static void blendMode(int srcBlendMode, int dstBlendMode) {
		if(GLHelper.srcBlendMode != srcBlendMode || GLHelper.dstBlendMode != dstBlendMode) {
			gl.glBlendFunc(srcBlendMode, dstBlendMode);
			GLHelper.srcBlendMode = srcBlendMode;
			GLHelper.dstBlendMode = dstBlendMode;
		}
	}
	
	/**
	 * Sets the glBlendFunc
	 * 
	 * @param blendFunction valid BlendFunction object
	 */
	public static void blendMode(BlendFunction blendFunction) {
		if(blendFunction.getSrc() != srcBlendMode || blendFunction.getDst() != dstBlendMode) {
			gl.glBlendFunc(blendFunction.getSrc(), blendFunction.getDst());
			srcBlendMode = blendFunction.getSrc();
			dstBlendMode = blendFunction.getDst();
		}
	}

    /**
     * Sets the colour and alpha values
     * 
     * @param red 0f to 1f
     * @param green 0f to 1f
     * @param blue 0f to 1f
     * @param alpha 0f to 1f
     */
    public static void color4f(float red, float green, float blue, float alpha) {
        if(alpha != glColor4fAlpha || red != glColor4fRed || green != glColor4fGreen || blue != glColor4fBlue) {
            gl.glColor4f(red, green, blue, alpha);
            glColor4fRed = red;
            glColor4fGreen = green;
            glColor4fBlue = blue;
            glColor4fAlpha = alpha;
        }
    }

    /**
     * Passes a buffer for drawing textures
     * 
     * @param buffer valid BufferObject
     * @param type coordinate type, eg GL_FLOAT
     */
    public static void texCoordPointer(BufferObject buffer, int type) {
        if(lastTexCoordPointerBuffer != buffer) {
        	gl.glTexCoordPointer(2, type, 0, buffer.get());
            lastTexCoordPointerBuffer = buffer;
        }
    }
    
    /**
     * Passes an empty buffer for vertices, for use with VBO
     * 
     * @param type coordinate type, eg GL_FLOAT
     */
    public static void vertexPointer(int type) {
    	lastVertexPointerBuffer = null;
    	((GL11)gl).glVertexPointer(2, type, 0, 0);
    }
    
    /**
     * Passes an empty buffer for textures, for use with VBO
     * 
     * @param type coordinate type, eg GL_FLOAT
     */
    public static void texCoordPointer(int type) {
    	lastTexCoordPointerBuffer = null;
    	((GL11)gl).glTexCoordPointer(2, type, 0, 0);
    }

    /**
     * Passes a buffer for vertices
     * 
     * @param buffer valid BufferObject
     * @param type coordinate type, eg GL_FLOAT
     */
    public static void vertexPointer(BufferObject buffer, int type) {
        if(lastVertexPointerBuffer != buffer) {
        	gl.glVertexPointer(2, type, 0, buffer.get());
            lastVertexPointerBuffer = buffer;
        }
    }
    
    /**
     * Draws a polygon, with optional border using standard vertex techniques
     * 
     * @param fill TRUE if drawing with a fill, FALSE otherwise
     * @param red red value for fill, 0f to 1f
     * @param green green value for fill, 0f to 1f
     * @param blue blue value for fill, 0f to 1f
     * @param alpha alpha value for fill, 0f to 1f
     * @param blendFunction valid BlendFunction object
     * @param vertexBuffer valid BufferObject
     * @param vertexMode method for drawing vertices, eg GL_TRIANGLE_STRIP 
     * @param x x-coordinate to draw from, top left
     * @param y y-coordinate to draw from, top left
     * @param width width of the shape to draw
     * @param height height of the shape to draw
     * @param rotation rotation, in degrees
     * @param rotateAboutPivot TRUE if rotating about a given pivot, FALSE if central
     * @param rotationPivotX x-coordinate of rotation pivot, if rotateAboutPivot TRUE
     * @param rotationPivotY y-coordinate of rotation pivot, if rotateAboutPivot FALSE
     * @param border TRUE if drawing a border, FALSE otherwise
     * @param borderBuffer valid BufferObject for the border shape, if border TRUE
     * @param borderRed red value for border, 0f to 1f, if border TRUE
     * @param borderGreen green value for border, 0f to 1f, if border TRUE
     * @param borderBlue blue value for border, 0f to 1f, if border TRUE
     * @param borderAlpha alpha value for border, 0f to 1f, if border TRUE
     * @param lineWidth -1 to use default
     * @param hasTexture TRUE if a Texture is being passed, FALSE otherwise
     * @param texture valid Texture object to be rendered
     * @param textureTile the index of the tile inside the Texture
     */
    public static void drawNormal(boolean fill, float red, float green, float blue, float alpha, BlendFunction blendFunction, BufferObject vertexBuffer, int vertexMode, float x, float y, float width, float height, float rotation, boolean rotateAboutPivot, float rotationPivotX, float rotationPivotY, boolean border, BufferObject borderBuffer, float borderRed, float borderGreen, float borderBlue, float borderAlpha, float lineWidth, boolean hasTexture, Texture texture, int textureTile, ColourBuffer colourBuffer) {
    	if(alpha == 0 && (borderAlpha == 0 || border == false)) return;
    	if(!fill && !border) return;
		if(blendFunction != null) {
			GLHelper.blendMode(blendFunction);
		} else {
			GLHelper.blendMode(Rokon.blendFunction);
		}
		gl.glPushMatrix();
		enableVertexArray();
		bindBuffer(0, false);
		if(x != 0 || y != 0) {
			gl.glTranslatef(x, y, 0);
		}
		if(rotation != 0) {
			if(!rotateAboutPivot) {
				gl.glTranslatef(width / 2, height / 2, 0);
				gl.glRotatef(rotation, 0, 0, 1);
				gl.glTranslatef(-width / 2, -height / 2, 0);
			} else {
				gl.glTranslatef(rotationPivotX, rotationPivotY, 0);
				gl.glRotatef(rotation, 0, 0, 1);
				gl.glTranslatef(-rotationPivotX, -rotationPivotY, 0);
			}
		}
		if(width != 1 || height != 1) {
			gl.glScalef(width, height, 0);
		}
		if(texture != null) {
			if(fill) {
				enableTextures();
				enableTexCoordArray();
				bindTexture(texture);
				color4f(red, green, blue, alpha);
				if(colourBuffer != null) {
					enableColourArray();
					colourPointer(colourBuffer);
				} else {
					disableColourArray();
				}
				texCoordPointer(texture.buffer[textureTile], GL10.GL_FLOAT);
				vertexPointer(vertexBuffer, GL10.GL_FLOAT);
				gl.glDrawArrays(vertexMode, 0, vertexBuffer.getSize() / 2);
			}
			if(border) {
				disableTexCoordArray();
				disableTextures();
				disableColourArray();
				color4f(borderRed, borderGreen, borderBlue, borderAlpha);
				vertexPointer(borderBuffer, GL10.GL_FLOAT);
				if(lineWidth != -1) {
					lineWidth(lineWidth);
				} else {
					lineWidth(RokonActivity.currentScene.defaultLineWidth);
				}
				gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, borderBuffer.getSize() / 2);
			}
		} else {
			disableTexCoordArray();
			disableTextures();
			if(fill) {
				color4f(red, green, blue, alpha);
				if(colourBuffer != null) {
					enableColourArray();
					colourPointer(colourBuffer);
				} else {
					disableColourArray();
				}
				vertexPointer(vertexBuffer, GL10.GL_FLOAT);
				gl.glDrawArrays(vertexMode, 0, vertexBuffer.getSize() / 2);
			}
			if(border) {
				disableColourArray();
				color4f(borderRed, borderGreen, borderBlue, borderAlpha);
				vertexPointer(borderBuffer, GL10.GL_FLOAT);
				if(lineWidth != -1) {
					lineWidth(lineWidth);
				} else {
					lineWidth(RokonActivity.currentScene.defaultLineWidth);
				}
				gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, borderBuffer.getSize() / 2);
			}
		}
		gl.glPopMatrix();	
    }

    /** Draws a polygon, using VBOs
    * @param fill TRUE if drawing with a fill, FALSE otherwise
    * @param red red value for fill, 0f to 1f
    * @param green green value for fill, 0f to 1f
    * @param blue blue value for fill, 0f to 1f
    * @param alpha alpha value for fill, 0f to 1f
    * @param blendFunction valid BlendFunction object
    * @param arrayVBO valid ArrayVBO for the shape
    * @param vertexMode method for drawing vertices, eg GL_TRIANGLE_STRIP 
    * @param x x-coordinate to draw from, top left
    * @param y y-coordinate to draw from, top left
    * @param width width of the shape to draw
    * @param height height of the shape to draw
    * @param rotation rotation, in degrees
    * @param rotateAboutPivot TRUE if rotating about a given pivot, FALSE if central
    * @param rotationPivotX x-coordinate of rotation pivot, if rotateAboutPivot TRUE
    * @param rotationPivotY y-coordinate of rotation pivot, if rotateAboutPivot FALSE
    * @param border TRUE if drawing a border, FALSE otherwise
    * @param borderVBO valid ArrayVBO for the border shape, if border TRUE
    * @param borderRed red value for border, 0f to 1f, if border TRUE
    * @param borderGreen green value for border, 0f to 1f, if border TRUE
    * @param borderBlue blue value for border, 0f to 1f, if border TRUE
    * @param borderAlpha alpha value for border, 0f to 1f, if border TRUE
    * @param lineWidth -1 to use default
    * @param hasTexture TRUE if a Texture is being passed, FALSE otherwise
    * @param texture valid Texture object to be rendered
    * @param textureTile the index of the tile inside the Texture
    */
    public static void drawVBO(boolean fill, float red, float green, float blue, float alpha, BlendFunction blendFunction, ArrayVBO arrayVBO, int vertexMode, float x, float y, float width, float height, float rotation, boolean rotateAboutPivot, float rotationPivotX, float rotationPivotY, boolean border, ArrayVBO borderVBO, float borderRed, float borderGreen, float borderBlue, float borderAlpha, float lineWidth, boolean hasTexture, Texture texture, int textureTile, ColourBuffer colourBuffer) {
    	if(alpha == 0 && (borderAlpha == 0 || border == false)) return;
    	if(!fill && !border) return;
		if(!arrayVBO.isLoaded()) {
			//Debug.print("Vertex VBO isn't loaded");
			arrayVBO.load(gl);
		}
		if(border && !borderVBO.isLoaded()) {
			//Debug.print("Border VBO isn't loaded");
			borderVBO.load(gl);
		}
		if(hasTexture) {
			checkTextureValid(texture);
			if(!texture.vbo[textureTile].isLoaded()) {
				//Debug.print("Texture VBO isn't loaded");
				texture.vbo[textureTile].load(gl);
			}
		}
		
		if(blendFunction != null) {
			GLHelper.blendMode(blendFunction);
		} else {
			GLHelper.blendMode(Rokon.blendFunction);
		}
		gl.glPushMatrix();
		enableVertexArray();
		if(x != 0 || y != 0) {
			gl.glTranslatef(x, y, 0);
		}
		if(rotation != 0) {
			if(!rotateAboutPivot) {
				gl.glTranslatef(width / 2, height / 2, 0);
				gl.glRotatef(rotation, 0, 0, 1);
				gl.glTranslatef(-width / 2, -height / 2, 0);
			} else {
				gl.glTranslatef(rotationPivotX, rotationPivotY, 0);
				gl.glRotatef(rotation, 0, 0, 1);
				gl.glTranslatef(-rotationPivotX, -rotationPivotY, 0);
			}
		}
		if(width != 1 || height != 1) {
			gl.glScalef(width, height, 0);
		}
		if(hasTexture) {
			enableTextures();
			enableTexCoordArray();
			bindTexture(texture);
			color4f(red, green, blue, alpha);
			texCoordPointer(texture.buffer[textureTile], GL10.GL_FLOAT);
			bindBuffer(arrayVBO.getBufferIndex(), false);
			if(colourBuffer != null) {
				enableColourArray();
				colourPointer(colourBuffer);
			} else {
				disableColourArray();
			}
			vertexPointer(GL10.GL_FLOAT);
			bindBuffer(texture.vbo[textureTile].getBufferIndex(), false);
			texCoordPointer(GL10.GL_FLOAT);
			gl.glDrawArrays(vertexMode, 0, arrayVBO.getBufferObject().getSize() / 2);
		} else {
			disableTexCoordArray();
			disableTextures();
			if(fill) {
				color4f(red, green, blue, alpha);
				bindBuffer(arrayVBO.getBufferIndex(), false);
				vertexPointer(GL10.GL_FLOAT);
				if(colourBuffer != null) {
					enableColourArray();
					colourPointer(colourBuffer);
				} else {
					disableColourArray();
				}
				gl.glDrawArrays(vertexMode, 0, arrayVBO.getBufferObject().getSize() / 2);
			}
			if(border) {
				disableColourArray();
				if(lineWidth != -1) {
					lineWidth(lineWidth);
				} else {
					lineWidth(RokonActivity.currentScene.defaultLineWidth);
				}
				color4f(borderRed, borderGreen, borderBlue, borderAlpha);
				bindBuffer(borderVBO.getBufferIndex(), false);
				vertexPointer(GL10.GL_FLOAT);
				gl.glDrawArrays(GL10.GL_LINE_LOOP, 0, borderVBO.getBufferObject().getSize() / 2);
			}
		}
		gl.glPopMatrix();	
    }

    /**
     * Removes a set of Textures from the hardware
     * 
     * @param texture an array of Texture objects
     */
    public static void removeTextures(Texture[] texture) {
    	for(int i = 0; i < texture.length; i++) {
    		if(texture[i] != null) {
    			int[] textureId = new int[] { texture[i].textureIndex };
    	    	gl.glDeleteTextures(1, textureId, 0);
    		}
    	}
    }

}
