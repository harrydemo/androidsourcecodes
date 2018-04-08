/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sunniwell.spinnerdroplayout;

import java.util.ArrayList;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter showing the types of items that can be added to a {@link Workspace}.
 * <br>接口，用于显示项目的类型，可以添加到{@link Workspace}中的项目�? */
public class AddAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    
    private final ArrayList<ListItem> mItems = new ArrayList<ListItem>();
    
    public static final int ITEM_SHORTCUT = 0;
    public static final int ITEM_APPWIDGET = 1;
    public static final int ITEM_LIVE_FOLDER = 2;
    public static final int ITEM_WALLPAPER = 3;
    
    /**
     * Specific item in our list.
     * <br>我们的list要显示的项目
     */
    public class ListItem {
        public final CharSequence text;
        public final Drawable image;
        public final int actionTag;
        
        public ListItem(Resources res, int textResourceId, int imageResourceId, int actionTag) {
            text = res.getString(textResourceId);
            if (imageResourceId != -1) {
                image = res.getDrawable(imageResourceId);
            } else {
                image = null;
            }
            this.actionTag = actionTag;
        }
    }
    
    public AddAdapter(Main launcher) {
        super();

        /**
         * 以下两种方式均可以：
         */
//        mInflater = getLayoutInflater();   // 在Activity里面才行
//        LayoutInflater inflater = LayoutInflater.from(context);// 也行�?
        mInflater = (LayoutInflater) launcher.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        // Create default actions
        Resources res = launcher.getResources();
        
        mItems.add(new ListItem(res, R.string.group_shortcuts,
                R.drawable.ic_launcher_shortcut, ITEM_SHORTCUT));

        mItems.add(new ListItem(res, R.string.group_widgets,
                R.drawable.ic_launcher_appwidget, ITEM_APPWIDGET));
        
        mItems.add(new ListItem(res, R.string.group_live_folders,
                R.drawable.ic_launcher_folder_live, ITEM_LIVE_FOLDER));
        
        mItems.add(new ListItem(res, R.string.group_wallpapers,
                R.drawable.ic_launcher_gallery, ITEM_WALLPAPER));

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ListItem item = (ListItem) getItem(position);
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.add_list_item, parent, false);
        }
        
        TextView textView = (TextView) convertView;
        textView.setTag(item);
        //.........................
        textView.setText(item.text);
        textView.setCompoundDrawablesWithIntrinsicBounds(item.image, null, null, null);
        
        return convertView;// 齐霜给我讲清楚了,址引用?
    }

    public int getCount() {
        return mItems.size();
    }

    public Object getItem(int position) {
        return mItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		ListItem item = (ListItem) getItem(position);
        
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drop_list_item, parent, false);
        }
        
        TextView textView = (TextView) convertView;
        textView.setTag(item);
        
        textView.setText(item.text+"DRtest");
        textView.setCompoundDrawablesWithIntrinsicBounds(item.image, null, null, null);
        
        return textView;
	}
    
}
