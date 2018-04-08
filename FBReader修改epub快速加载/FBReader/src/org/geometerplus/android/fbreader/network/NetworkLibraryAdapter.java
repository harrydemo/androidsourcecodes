/*
 * Copyright (C) 2010-2011 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.network;

import org.geometerplus.android.fbreader.tree.ListAdapter;
import org.geometerplus.fbreader.network.NetworkTree;
import org.geometerplus.fbreader.network.tree.AddCustomCatalogItemTree;
import org.geometerplus.fbreader.network.tree.NetworkBookTree;
import org.geometerplus.fbreader.network.tree.SearchItemTree;
import org.geometerplus.zlibrary.core.image.ZLImage;
import org.geometerplus.zlibrary.core.image.ZLLoadableImage;
import org.geometerplus.zlibrary.ui.android.R;
import org.geometerplus.zlibrary.ui.android.image.ZLAndroidImageData;
import org.geometerplus.zlibrary.ui.android.image.ZLAndroidImageManager;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

class NetworkLibraryAdapter extends ListAdapter {
	NetworkLibraryAdapter(NetworkBaseActivity activity) {
		super(activity);
	}

	private int myCoverWidth = -1;
	private int myCoverHeight = -1;

	private final Runnable myInvalidateViewsRunnable = new Runnable() {
		public void run() {
			((NetworkBaseActivity)getActivity()).getListView().invalidateViews();
		}
	};

	public View getView(int position, View convertView, final ViewGroup parent) {
		final NetworkTree tree = (NetworkTree)getItem(position);
		final View view = (convertView != null) ? convertView :
			LayoutInflater.from(parent.getContext()).inflate(R.layout.network_tree_item, parent, false);

		((TextView)view.findViewById(R.id.network_tree_item_name)).setText(tree.getName());
		((TextView)view.findViewById(R.id.network_tree_item_childrenlist)).setText(tree.getSecondString());

		if (myCoverWidth == -1) {
			view.measure(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			myCoverHeight = view.getMeasuredHeight();
			myCoverWidth = myCoverHeight * 15 / 32;
			view.requestLayout();
		}

		final ImageView coverView = (ImageView)view.findViewById(R.id.network_tree_item_icon);
		coverView.getLayoutParams().width = myCoverWidth;
		coverView.getLayoutParams().height = myCoverHeight;
		coverView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		coverView.requestLayout();
		setupCover(coverView, tree, myCoverWidth, myCoverWidth);

		final ImageView statusView = (ImageView)view.findViewById(R.id.network_tree_item_status);
		final int status = (tree instanceof NetworkBookTree)
			? NetworkBookActions.getBookStatus(
				((NetworkBookTree)tree).Book,
				((NetworkBaseActivity)getActivity()).Connection
			  )
			: 0;
		if (status != 0) {
			statusView.setVisibility(View.VISIBLE);
			statusView.setImageResource(status);
		} else {
			statusView.setVisibility(View.GONE);
		}
		statusView.requestLayout();

		return view;
	}

	private void setupCover(final ImageView coverView, NetworkTree tree, int width, int height) {
		Bitmap coverBitmap = null;
		ZLImage cover = tree.getCover();
		if (cover != null) {
			ZLAndroidImageData data = null;
			final ZLAndroidImageManager mgr = (ZLAndroidImageManager)ZLAndroidImageManager.Instance();
			if (cover instanceof ZLLoadableImage) {
				final ZLLoadableImage img = (ZLLoadableImage)cover;
				if (img.isSynchronized()) {
					data = mgr.getImageData(img);
				} else {
					img.startSynchronization(myInvalidateViewsRunnable);
				}
			} else {
				data = mgr.getImageData(cover);
			}
			if (data != null) {
				coverBitmap = data.getBitmap(2 * width, 2 * height);
			}
		}
		if (coverBitmap != null) {
			coverView.setImageBitmap(coverBitmap);
		} else if (tree instanceof NetworkBookTree) {
			coverView.setImageResource(R.drawable.ic_list_library_book);
		} else if (tree instanceof AddCustomCatalogItemTree) {
			coverView.setImageResource(R.drawable.ic_list_plus);
		} else if (tree instanceof SearchItemTree) {
			coverView.setImageResource(R.drawable.ic_list_searchresult);
		} else {
			coverView.setImageResource(R.drawable.ic_list_library_books);
		}
	}
}
