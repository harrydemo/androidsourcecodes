/*
 * [程序名称] Android 音乐播放器
 * [参考资料] http://code.google.com/p/rockon-android/ 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
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

package com.xmobileapp.rockplayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.xmobileapp.rockplayer.R;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.GradientDrawable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;


public class ImageCursorAdapter extends SimpleCursorAdapter{
	
	Cursor cursor;
	Context context;
	
    public ImageCursorAdapter(Context context, 
    							int layout, 
    							Cursor c,
    							String[] from,
    							int[] to){
        super(context, layout, c, from, to);
        this.cursor = c;
        this.context = context;        

    }

    
    ImageView im;
    TextView imTitle;
    int	IMAGE_VIEW_SIZE = 120;
    /* (non-Javadoc)
     * This is where you actually create the item view of the list
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
	    	/*
	    	 * Get the item list image component set its height right
	    	 */
    		im = (ImageView)
    			view.findViewById(R.id.image);
    		imTitle = (TextView)
    			view.findViewById(R.id.image_title);
    		
    		/*
    		 * set the image
    		 */
    		BitmapFactory.Options opts = new BitmapFactory.Options();
    		opts.inJustDecodeBounds = true;
    		BitmapFactory.decodeFile(
    				cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
    				opts);
    		opts.inSampleSize = (int) Math.floor(opts.outHeight / IMAGE_VIEW_SIZE);
    		opts.inJustDecodeBounds = false;
    		im.setImageBitmap(BitmapFactory.decodeFile(
    				cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)),
    				opts));
    		
    		/*
    		 * set the image title
    		 */
    		imTitle.setText(cursor.getString(
    				cursor.getColumnIndexOrThrow(
    						MediaStore.Images.Thumbnails.DATA)));

    }

}