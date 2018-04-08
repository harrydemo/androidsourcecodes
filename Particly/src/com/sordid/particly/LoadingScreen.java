package com.sordid.particly;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.audio.sound.SoundManager;
import org.anddev.andengine.opengl.buffer.BufferObjectManager;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LoadingScreen extends Activity  implements Serializable {
	private static final long serialVersionUID = 5754882104822620423L;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        load();
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	super.onNewIntent(intent);
    	load();
    }        
    
    private void load() {
    	loadTextures();
    	loadSounds();
        ImageButton next = (ImageButton) findViewById(R.id.loading);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChapterSelect.class);
                startActivity(intent);
            }
        });
    }
    
    private void loadTextures() {
    	ParticlyActivity.sTextures = new HashMap<String,TextureRegion>();
    	ParticlyActivity.sTiledTextures = new HashMap<String,TiledTextureRegion>();
    	ParticlyActivity.sTextureHolders = new ArrayList<Texture>();
    	ParticlyActivity.sHitVoiceSounds = new Sound[4];
        ParticlyActivity.sHitSounds = new Sound[4];
        ParticlyActivity.sWinSounds = new Sound[8];
        ParticlyActivity.sLoseSounds = new Sound[4];    	
    	Texture texture;
    	TextureRegionFactory.setAssetBasePath("gfx/");
    	BufferObjectManager.setActiveInstance(new BufferObjectManager());
    	
    	// Particles
    	texture = new Texture(32, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	ParticlyActivity.sTextures.put("particle", TextureRegionFactory.createFromAsset(texture, this, "particle_point.png", 0, 0));
    	ParticlyActivity.sTextures.put("particleWaypoint", TextureRegionFactory.createFromAsset(texture, this, "particle_waypoint.png", 0, 32));
    	ParticlyActivity.sTextures.put("particleLauncher", TextureRegionFactory.createFromAsset(texture, this, "particle_launcher.png", 0, 64));
    	ParticlyActivity.sTextureHolders.add(texture);
    	
        // Ball
        texture = new Texture(256, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ParticlyActivity.sTiledTextures.put("ball", TextureRegionFactory.createTiledFromAsset(texture, this, "dude.png", 0, 0, 3, 1));
        ParticlyActivity.sTextures.put("arrow", TextureRegionFactory.createFromAsset(texture, this, "arrow.png", 0, 32));
        ParticlyActivity.sTextureHolders.add(texture);
	
		// Menu
        texture = new Texture(256, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        ParticlyActivity.sTextures.put("menuReset", TextureRegionFactory.createFromAsset(texture, this, "menu_reset.png", 0, 0));
        ParticlyActivity.sTextures.put("menuNext", TextureRegionFactory.createFromAsset(texture, this, "menu_next.png", 0, 75));
        ParticlyActivity.sTextures.put("menuSelect", TextureRegionFactory.createFromAsset(texture, this, "menu_select.png", 0, 150));
        ParticlyActivity.sTiledTextures.put("menuWin", TextureRegionFactory.createTiledFromAsset(texture, this, "menu_win.png", 0, 225, 1, 2));
        ParticlyActivity.sTiledTextures.put("menuSound", TextureRegionFactory.createTiledFromAsset(texture, this, "menu_sound.png", 0, 375, 1, 2));
        ParticlyActivity.sTextureHolders.add(texture);    
        
    	// Misc
    	texture = new Texture(128, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
    	ParticlyActivity.sTextures.put("largeStarGlow", TextureRegionFactory.createFromAsset(texture, this, "large_star_glow.png", 0, 0));
    	ParticlyActivity.sTextureHolders.add(texture);        
    }
    
    private void loadSounds() {
        // Sound
    	SoundManager m = new SoundManager();
        SoundFactory.setAssetBasePath("sfx/");
        try {
        	int len = ParticlyActivity.sHitVoiceSounds.length;
        	for(int i=0;i<len;i++) {
        		ParticlyActivity.sHitVoiceSounds[i] = SoundFactory.createSoundFromAsset(m, this, "hitvoice"+(i+1)+".ogg");
        	}
        	len = ParticlyActivity.sHitSounds.length;
        	for(int i=0;i<len;i++) {
        		ParticlyActivity.sHitSounds[i] = SoundFactory.createSoundFromAsset(m, this, "hit"+(i+1)+".ogg");
        	}
        	len = ParticlyActivity.sWinSounds.length;
        	for(int i=0;i<len;i++) {
        		ParticlyActivity.sWinSounds[i] = SoundFactory.createSoundFromAsset(m, this, "win"+(i+1)+".ogg");
        		ParticlyActivity.sWinSounds[i].setVolume(0.18f);
        	}
        	len = ParticlyActivity.sLoseSounds.length;
        	for(int i=0;i<len;i++) {
        		ParticlyActivity.sLoseSounds[i] = SoundFactory.createSoundFromAsset(m, this, "lose"+(i+1)+".ogg");
        		ParticlyActivity.sLoseSounds[i].setVolume(0.18f);
        	}
        } catch (final IOException e) {
        	Slog.i("TMX File Error", e.toString());
        }
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      // ignore orientation/keyboard change
      super.onConfigurationChanged(newConfig);
    }
}