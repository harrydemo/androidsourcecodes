package wealk.android.jewels.entity;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import wealk.android.jewels.constants.IConstants;

/**
 * 精灵接口的实现——边框精灵
 * @author qingfeng
 * @since 2010-11-03
 */
public class BorderSprite implements ISprite, IConstants {

	// ===========================================================
	// Fields
	// ===========================================================
	
	final Sprite mSprite;//边框精灵
	
	// ===========================================================
	// Constructors
	// ===========================================================
	
	public BorderSprite(int row, int col, TextureRegion mTextureRegion ){
		this.mSprite = new JewelCell(row, col, mTextureRegion);
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================	
	
	@Override
	public int getRow() {
		return (int)this.mSprite.getX()/CELL_WIDTH;
	}	
	
	@Override
	public int getCol() {
		return (int)this.mSprite.getY()/CELL_HEIGHT;
	}
	
	@Override
	public void setMapPosition(int row, int col){
		this.mSprite.setPosition(row * CELL_WIDTH, col * CELL_HEIGHT);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public Sprite getSprite(){
		return this.mSprite;
	}
	
	// ===========================================================
	// Methods
	// ===========================================================
}
