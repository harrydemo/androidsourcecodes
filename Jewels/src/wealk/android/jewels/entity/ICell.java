package wealk.android.jewels.entity;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import wealk.android.jewels.constants.IConstants;

/**
 * 单元格的抽象类(相当于接口)
 * @author Qingfeng
 * @since 2010-11-03
 */
public abstract class ICell extends Sprite implements IConstants {

	public ICell(final int pCellX, final int pCellY, final int pWidth, final int pHeight, final TextureRegion pTextureRegion) {
		super(pCellX, pCellY, pWidth, pHeight, pTextureRegion);
	}
}
