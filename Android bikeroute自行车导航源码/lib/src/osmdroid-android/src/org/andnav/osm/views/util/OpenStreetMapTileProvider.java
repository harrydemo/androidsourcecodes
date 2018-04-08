// Created by plusminus on 21:46:22 - 25.09.2008
package org.andnav.osm.views.util;

import java.io.InputStream;

import org.andnav.osm.tileprovider.OpenStreetMapTile;
import org.andnav.osm.views.util.constants.OpenStreetMapViewConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;
import android.os.Handler;

/**
 *
 * @author Nicolas Gramlich
 *
 */
public abstract class OpenStreetMapTileProvider implements OpenStreetMapViewConstants {

	private static final Logger logger = LoggerFactory.getLogger(OpenStreetMapTileProvider.class);

	protected final OpenStreetMapTileCache mTileCache;
	protected final Handler mDownloadFinishedHandler;
	protected boolean mUseDataConnection = true;

	public OpenStreetMapTileProvider(final Handler pDownloadFinishedListener) {
		mTileCache = new OpenStreetMapTileCache();
		mDownloadFinishedHandler = pDownloadFinishedListener;
	}

	public void mapTileRequestCompleted(final OpenStreetMapTile pTile, final String pTilePath) {

		// add the tile to the cache
		// let the renderer convert the file to a drawable
		final Drawable drawable = pTile.getRenderer().getDrawable(pTilePath);
		if (drawable != null) {
			mTileCache.putTile(pTile, drawable);
		}

		// tell our caller we've finished and it should update its view
		mDownloadFinishedHandler.sendEmptyMessage(OpenStreetMapTile.MAPTILE_SUCCESS_ID);

		if (DEBUGMODE)
			logger.debug("MapTile request complete: " + pTile);
	}

	public void mapTileRequestCompleted(final OpenStreetMapTile pTile, final InputStream pTileInputStream) {

		// add the tile to the cache
		// let the renderer convert the file to a drawable
		try {
			final Drawable drawable = pTile.getRenderer().getDrawable(pTileInputStream);
			if (drawable != null) {
				mTileCache.putTile(pTile, drawable);
			}
		} finally {
			try {
				pTileInputStream.close();
			} catch(final Exception ignore) {}
		}

		// tell our caller we've finished and it should update its view
		mDownloadFinishedHandler.sendEmptyMessage(OpenStreetMapTile.MAPTILE_SUCCESS_ID);

		if (DEBUGMODE)
			logger.debug("MapTile request complete: " + pTile);
	}

	public void mapTileRequestCompleted(final OpenStreetMapTile pTile) {

		// tell our caller we've finished and it should update its view
		mDownloadFinishedHandler.sendEmptyMessage(OpenStreetMapTile.MAPTILE_SUCCESS_ID);

		if (DEBUGMODE)
			logger.debug("MapTile request complete: " + pTile);
	}

	public void ensureCapacity(final int aCapacity) {
		mTileCache.ensureCapacity(aCapacity);
	}

	/**
	 * Whether to use the network connection if it's available.
	 */
	public boolean useDataConnection() {
		return mUseDataConnection;
	}

	/**
	 * Set whether to use the network connection if it's available.
	 * @param aMode
	 * if true use the network connection if it's available.
	 * if false don't use the network connection even if it's available.
	 */
	public void setUseDataConnection(boolean aMode) {
		mUseDataConnection = aMode;
	}

	public abstract Drawable getMapTile(OpenStreetMapTile pTile);

	public abstract void detach();

}
