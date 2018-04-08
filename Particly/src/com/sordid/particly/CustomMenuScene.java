package com.sordid.particly;

import java.util.ArrayList;
import java.util.HashMap;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.shape.Shape;
import org.anddev.andengine.input.touch.TouchEvent;

import android.view.MotionEvent;

/**
 * @author Methodin
 * @date 06.10.2011
 */
public class CustomMenuScene extends MenuScene {
	
	// ===========================================================
	// Fields
	// ===========================================================
	
	protected IOnMenuItemClickListener mOnMenuItemClickListener;

	protected CustomMenuAnimator mMenuAnimator = null;

	protected IMenuItem mSelectedMenuItem;
	
	protected MenuContainer currentContainer = new MenuContainer();
	
	protected HashMap<String, IShape> mItems = new HashMap<String, IShape>();
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public CustomMenuScene() {
		this(null, null);
	}

	public CustomMenuScene(final IOnMenuItemClickListener pOnMenuItemClickListener) {
		this(null, pOnMenuItemClickListener);
	}

	public CustomMenuScene(final Camera pCamera) {
		this(pCamera, null);
	}

	public CustomMenuScene(final Camera pCamera, final IOnMenuItemClickListener pOnMenuItemClickListener) {
		super(pCamera);
		this.mOnMenuItemClickListener = pOnMenuItemClickListener;
		this.setOnSceneTouchListener(this);
		this.setOnAreaTouchListener(this);
	}	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
	public IOnMenuItemClickListener getOnMenuItemClickListener() {
		return this.mOnMenuItemClickListener;
	}

	public void setOnMenuItemClickListener(final IOnMenuItemClickListener pOnMenuItemClickListener) {
		this.mOnMenuItemClickListener = pOnMenuItemClickListener;
	}

	@Override
	public MenuScene getChildScene() {
		return (MenuScene)super.getChildScene();
	}

	@Override
	public void setChildScene(final Scene pChildScene, final boolean pModalDraw, final boolean pModalUpdate, final boolean pModalTouch) throws IllegalArgumentException {
		if(pChildScene instanceof MenuScene) {
			super.setChildScene(pChildScene, pModalDraw, pModalUpdate, pModalTouch);
		} else {
			throw new IllegalArgumentException("MenuScene accepts only MenuScenes as a ChildScene.");
		}
	}

	@Override
	public void clearChildScene() {
		if(this.getChildScene() != null) {
			this.getChildScene().reset();
			super.clearChildScene();
		}
	}

	public void setMenuAnimator(final CustomMenuAnimator pMenuAnimator) {
		this.mMenuAnimator = pMenuAnimator;
	}	
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================	
	
	@Override
	public void buildAnimations() {
		this.prepareAnimations();
		this.mMenuAnimator.buildAnimations(this.currentContainer);
	}

	@Override
	public void prepareAnimations() {
		final float cameraHeight = this.mCamera.getHeight();
		final float cameraWidth = this.mCamera.getWidth();
		this.mMenuAnimator.prepareAnimations(this.currentContainer, cameraWidth, cameraHeight);
	}
	
	@Override
    public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final ITouchArea pTouchArea, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		final IMenuItem menuItem = ((IMenuItem)pTouchArea);
		switch(pSceneTouchEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				if(this.mSelectedMenuItem != null && this.mSelectedMenuItem != menuItem) {
					this.mSelectedMenuItem.onUnselected();
				}
				this.mSelectedMenuItem = menuItem;
				this.mSelectedMenuItem.onSelected();
				break;
			case MotionEvent.ACTION_UP:
				if(this.mOnMenuItemClickListener != null) {
					final boolean handled = this.mOnMenuItemClickListener.onMenuItemClicked(this, menuItem, pTouchAreaLocalX, pTouchAreaLocalY);
					menuItem.onUnselected();
					this.mSelectedMenuItem = null;
					return handled;
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				menuItem.onUnselected();
				this.mSelectedMenuItem = null;
				break;
		}
		return true;
	}	
	
	// ===========================================================
	// Methods
	// ===========================================================
	public CustomMenuScene add(Shape pEntity) {
		final String name = pEntity.getClass().getSimpleName();
		if(name.equals("SpriteMenuItem")
			|| name.equals("AnimatedSpriteMenuItem")
			|| name.equals("Text")
			|| name.equals("Sprite")
			|| name.equals("AnimatedSprite")
		) {
			this.getFirstChild().attachChild((IShape)pEntity);
			if(name.equals("SpriteMenuItem") || name.equals("AnimatedSpriteMenuItem")) {
				this.registerTouchArea((IMenuItem)pEntity);
			}
		}
		currentContainer.add(pEntity);
		return this;
	}
	public CustomMenuScene add(String identifier, Shape pEntity) {
		mItems.put(identifier, pEntity);
		return this.add(pEntity);
	}
	
	public CustomMenuScene br() {
		currentContainer.add(new Break());
		return this;
	}
	public CustomMenuScene br(float spacing) {
		currentContainer.add(new Break(spacing));
		return this;
	}
	
	public CustomMenuScene container() {
		final MenuContainer newContainer = new MenuContainer(currentContainer);
		currentContainer.add(newContainer);
		currentContainer = newContainer;
		return this;
	}
	public CustomMenuScene end() {
		currentContainer = currentContainer.getParent();
		return this;
	}	

	// ===========================================================
	// Classes
	// ==========================================================
	
	public class MenuContainer {
		MenuContainer mParent = null;
		ArrayList<Object> mEntities = new ArrayList<Object>();

		public MenuContainer() {}
		public MenuContainer(MenuContainer pParent) {
			mParent = pParent;
		}
		
		public MenuContainer getParent() {
			return mParent;
		}
	
		public void add(Object item) {
			mEntities.add(item);
		}
	}
	public class Break{
		private float mSpacing = 0;
		public Break(){}
		public Break(float pSpacing){
			mSpacing = pSpacing;
		}
		public float getSpacing() {
			return mSpacing;
		}
	}
}