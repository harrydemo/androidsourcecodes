package com.stickycoding.rokon;

import javax.microedition.khronos.opengles.GL10;

/**
 * TextSprite.text
 * A Sprite optimized for use with TextTexture
 *
 * @author Richard
 */
public class TextSprite extends Sprite {
	
	/**
	 * The text that this TextSprite contains
	 */
	public String text = "";

	protected float characterWidth;
	protected int textLength = 0;

	/**
	 * Creates a TextSprite from given coordinates and dimensions
	 * 
	 * @param x x-coordinate
	 * @param y y-coordinate
	 * @param characterWidth width for each character
	 * @param height height the height for each character
	 */
	public TextSprite(float x, float y, float characterWidth, float height) {
		super(x, y, characterWidth, height);
		this.characterWidth = width;
	}
	
	/**
	 * @return the current text
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Sets the text for this TextSprite
	 * @param text
	 */
	public void setText(String text) {
		this.text = text;
		textLength = this.text.length();
		width = characterWidth * textLength;
	}

	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.DrawableObject#setTexture(com.stickycoding.rokon.Texture)
	 */
	@Override
	public void setTexture(Texture texture) {
		if(!(texture instanceof TextTexture)) {
			Debug.error("Tried setting TextSprite to non-TextTexture");
			return;
		}
		super.setTexture(texture);
	}
	
	/* (non-Javadoc)
	 * @see com.stickycoding.rokon.DrawableObject#onDrawNormal(javax.microedition.khronos.opengles.GL10)
	 */
	@Override
	protected void onDrawNormal(GL10 gl) {
		if(text.length() < 1)
			return;		
		GLHelper.color4f(red, green, blue, alpha);		
		GLHelper.checkTextureValid(texture);
		if(blendFunction != null) {
			GLHelper.blendMode(blendFunction);
		} else {
			GLHelper.blendMode(Rokon.blendFunction);
		}	
		GLHelper.enableTextures();
		GLHelper.enableTexCoordArray();
		GLHelper.bindTexture(texture);		
		gl.glPushMatrix();
		GLHelper.enableVertexArray();
		GLHelper.bindBuffer(0, false);
		GLHelper.vertexPointer(Rokon.triangleStripBoxBuffer, GL10.GL_FLOAT);			
		gl.glTranslatef(getX(), getY(), 0);	
		for(int i = 0; i < textLength; i++) {
			gl.glPushMatrix();
			gl.glTranslatef(i * characterWidth, 0, 0);
			gl.glScalef(characterWidth, height, 0);
			GLHelper.texCoordPointer(texture.buffer[((FontTexture)texture).charPos(text.charAt(i))], GL10.GL_FLOAT);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			gl.glPopMatrix();
		}		
		gl.glPopMatrix();	
	}
}
