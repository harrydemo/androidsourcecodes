package wealk.android.jewels.entity;

import org.anddev.andengine.opengl.texture.region.TextureRegion;

/**
 * ICell接口的实现类——钻石单元格
 * @author Qingfeng
 * @since 2010-11-03
 */
public class JewelCell extends ICell {

	// ===========================================================
	// Constructors
	// ===========================================================

	public JewelCell(final int pCellX, final int pCellY, final TextureRegion pTextureRegion) {
		super(pCellX * CELL_WIDTH, pCellY * CELL_HEIGHT, CELL_WIDTH, CELL_HEIGHT, pTextureRegion);
	}	
}
