/**
 * Shadow - Anonymous web browser for Android devices
 * Copyright (C) 2009 Connell Gauld
 * 
 * Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */

package uk.ac.cam.cl.dtg.android.tor.Shadow;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Item in the cookie whitelist preference page
 * @author Connell Gauld
 *
 */
public class WhitelistItemPreference extends Preference {
	
	private String mSite = null; 

	public WhitelistItemPreference(Context context) {
		super(context);
	}

	public WhitelistItemPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WhitelistItemPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setSite(String mSite) {
		this.mSite = mSite;
	}

	public String getSite() {
		return mSite;
	}

}
