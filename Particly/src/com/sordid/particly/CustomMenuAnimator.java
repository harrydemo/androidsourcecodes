package com.sordid.particly;

import java.util.ArrayList;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.modifier.AlphaModifier;
import org.anddev.andengine.entity.scene.menu.animator.BaseMenuAnimator;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.IShape;

import com.sordid.particly.CustomMenuScene.Break;
import com.sordid.particly.CustomMenuScene.MenuContainer;


/**
 * @author Methodin
 * @date 06.10.2011
 */
public class CustomMenuAnimator extends BaseMenuAnimator {
        // ===========================================================
        // Constants
        // ===========================================================

        private static final float ALPHA_FROM = 0.0f;
        private static final float ALPHA_TO = 1.0f;
        private final float mSpacing;

        // ===========================================================
        // Fields
        // ===========================================================

        // ===========================================================
        // Constructors
        // ===========================================================

        public CustomMenuAnimator() {
        	mSpacing = 2.0f;
        }
        public CustomMenuAnimator(float pSpacing) {
        	mSpacing = pSpacing;
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================
        
        // ===========================================================
        // Methods
        // ===========================================================

		public void buildAnimations(final CustomMenuScene.MenuContainer container) {
			for(Object o : container.mEntities) {
				final String name = o.getClass().getSimpleName();
				if(name.equals("Sprite")
					|| name.equals("AnimatedSprite")
					|| name.equals("SpriteMenuItem")
					|| name.equals("AnimatedSpriteMenuItem")
					|| name.equals("Text")
				) {
					if(!((IEntity)o).isVisible()) continue;
					((IEntity)o).registerEntityModifier(new AlphaModifier(DURATION, ALPHA_FROM, ALPHA_TO));	
				} else if(name.equals("MenuContainer")) {
					buildAnimations((MenuContainer)o);
				}
			}
		}

		public void prepareAnimations(final CustomMenuScene.MenuContainer container, final float pCameraWidth, final float pCameraHeight) {
			ArrayList<BufferedMenuCollection> bufferedCollections = new ArrayList<BufferedMenuCollection>();
			float bufferedHeight = 0;
			float currentMaxHeight = 0;
			bufferedCollections.add(new BufferedMenuCollection());
			IShape entity;
			String name;
			for(final Object o : container.mEntities) {
				name = o.getClass().getSimpleName();
				if(name.equals("Sprite")
					|| name.equals("AnimatedSprite")
					|| name.equals("SpriteMenuItem")
					|| name.equals("AnimatedSpriteMenuItem")
					|| name.equals("Text")
				) {
					if(!((IEntity)o).isVisible()) continue;
					entity = (IShape)o;
					entity.setAlpha(ALPHA_FROM);
					bufferedCollections.get(bufferedCollections.size()-1).addObject(new BufferedMenuObject(entity, bufferedHeight));
					currentMaxHeight = Math.max(currentMaxHeight, entity.getHeight() + this.mSpacing);
				}
				else if(name.equals("Break")) {
					bufferedHeight += currentMaxHeight + ((Break)o).getSpacing();
					bufferedCollections.add(new BufferedMenuCollection());
					currentMaxHeight = 0;
				}
			}	
			
			float basey = pCameraHeight / 2 - bufferedHeight / 2;
			for(final BufferedMenuCollection collection : bufferedCollections) {
				float x = pCameraWidth / 2 - collection.totalWidth / 2;
				for(final BufferedMenuObject object : collection.objects) {
					object.entityReference.setPosition(x, basey + object.yOffset);
					x += object.getWidth();
				}
			}
		}

		@Override
		public void buildAnimations(ArrayList<IMenuItem> arg0, float arg1,
				float arg2) {}

		@Override
		public void prepareAnimations(ArrayList<IMenuItem> arg0, float arg1,
				float arg2) {}
		
		public class BufferedMenuCollection {
			private ArrayList<BufferedMenuObject> objects = new ArrayList<BufferedMenuObject>();
			private float totalWidth = 0;
			
			public void addObject(BufferedMenuObject pObject) {
				objects.add(pObject);
				totalWidth += pObject.getWidth();
				if(objects.size() > 1) {
					totalWidth += CustomMenuAnimator.this.mSpacing;
				}
			}
		}
		
		public class BufferedMenuObject{
			private final IShape entityReference;
			private final float yOffset;
			
			public BufferedMenuObject(final IShape pEntity, final float pYOffset) {
				entityReference = pEntity;
				yOffset = pYOffset;
			}
			
			public float getWidth() {
				return entityReference.getWidth() + CustomMenuAnimator.this.mSpacing;
			}
		}
        
        // ===========================================================
}