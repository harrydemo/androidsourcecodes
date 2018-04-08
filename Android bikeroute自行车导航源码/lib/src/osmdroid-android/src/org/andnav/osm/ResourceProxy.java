package org.andnav.osm;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public interface ResourceProxy {

	public static enum string {

		// renderers
		osmarender,
		mapnik,
		cyclemap,
		public_transport,
		base,
		topo,
		hills,
		cloudmade_small,
		cloudmade_standard,

		// overlays
		fiets_nl,
		base_nl,
		roads_nl,

		// other stuff
		unknown,

	}

	public static enum bitmap {

		/**
		 * For testing - the image doesn't exist.
		 */
		unknown,

		center,
		direction_arrow,
		marker_default,
		marker_default_focused_base,
		navto_small,
		next,
		previous,
		person,

	}

	String getString(string pResId);

	Bitmap getBitmap(bitmap pResId);

	/**
	 * Get a bitmap as a {@link Drawable}
	 * @param pResId
	 * @return
	 */
	Drawable getDrawable(bitmap pResId);
}
