/**
 * TorProxyLib - Anonymous data communication for Android devices
 * 			   - Tools for application developers
 * Copyright (C) 2009 Connell Gauld
 * 
 *  Thanks to University of Cambridge,
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

package uk.ac.cam.cl.dtg.android.tor.TorProxyLib;

import android.os.Parcel;
import android.os.Parcelable;

public class HiddenServiceDescriptor implements Parcelable {
	
	private String mPublicKey;
	private String mPrivateKey;
	private String mUrl;

	public static final Parcelable.Creator<HiddenServiceDescriptor> CREATOR = new Parcelable.Creator<HiddenServiceDescriptor>() {
        public HiddenServiceDescriptor createFromParcel(Parcel in) {
            return new HiddenServiceDescriptor(in);
        }

        public HiddenServiceDescriptor[] newArray(int size) {
            return new HiddenServiceDescriptor[size];
        }
    };

    public HiddenServiceDescriptor(String publicKey, String privateKey, String url) {
		this.mPrivateKey = privateKey;
		this.mPublicKey = publicKey;
		this.mUrl = url;
	}

	public HiddenServiceDescriptor(Parcel in) {
		mPublicKey = in.readString();
		mPrivateKey = in.readString();
		mUrl = in.readString();
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(mPublicKey);
		p.writeString(mPrivateKey);
		p.writeString(mUrl);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public String getPublicKey() {
		return mPublicKey;
	}
	
	public String getPrivateKey() {
		return mPrivateKey;
	}
	
	public void setPublicKey(String key) {
		mPublicKey = key;
	}
	
	public void setPrivateKey(String key) {
		mPrivateKey = key;
	}

	public String getUrl() {
		return mUrl;
	}
	
	public void setUrl(String url) {
		mUrl = url;
	}

}
