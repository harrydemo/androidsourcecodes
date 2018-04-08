package com.renren.api.connect.android.example;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.RequestListenerHelper.DefaultRequestListener;
import com.renren.api.connect.android.example.xml.FriendHandler;
import com.renren.api.connect.android.example.xml.RestSaxParser;
import com.renren.api.connect.android.exception.RenrenError;

/**
 * @author 李勇(yong.li@opi-corp.com) 2010-9-3
 */
public class FriendRequestListener extends DefaultRequestListener {

    private Example example;

    private ProgressDialog progress;

    private String dataFormate;

    FriendRequestListener(Example example, String dataFormate) {
        this.example = example;
        this.dataFormate = dataFormate;
        progress = ProgressDialog.show(example, "Get friends", "Loading...");
        progress.show();
    }

    @Override
    public void onFault(final Throwable fault) {
        showError("Fault", fault.toString());
    }

    @Override
    public void onRenrenError(final RenrenError e) {
        showError("RenrenError", e.toString());
    }

    private void showError(final String title, final String text) {
        example.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (progress != null) progress.dismiss();
                Util.showAlert(example, title, text);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onComplete(String response) {
        List<Map<String, String>> datas = null;
        if ("xml".equalsIgnoreCase(this.dataFormate)) {
            datas = (List<Map<String, String>>) RestSaxParser.parse(response, new FriendHandler());
        } else {
            datas = this.parseFriendJson(response);
        }

        ListAdapter adapter = new ImageAdapter(this.example, datas);
        ListView lv = new ListView(this.example);
        lv.setAdapter(adapter);

        this.progress.dismiss();
        this.showResult(lv);
    }

    private void showResult(final ListView lv) {
        this.example.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Builder alertBuilder = new Builder(example);
                alertBuilder.setTitle("My Friends");
                alertBuilder.setView(lv);
                alertBuilder.setNeutralButton("确定", null);
                alertBuilder.create().show();
            }
        });
    }

    private List<Map<String, String>> parseFriendJson(String json) {
        List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
        try {
            JSONArray friends = new JSONArray(json);
            for (int i = 0; i < friends.length(); i++) {
                Map<String, String> d = new HashMap<String, String>();
                Object obj = friends.get(i);
                JSONObject jobj = new JSONObject(obj.toString());
                d.put("headurl", jobj.getString("tinyurl"));
                d.put("name", jobj.getString("name"));
                datas.add(d);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return datas;
    }
}

class ImageAdapter extends BaseAdapter {

    private List<Map<String, String>> datas;

    private Context context;

    ImageAdapter(Context context, List<Map<String, String>> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.friend, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.head);
            viewHolder.text = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, String> friend = this.datas.get(position);
        viewHolder.text.setText(friend.get("name"));
        this.setViewImage(viewHolder.image, friend.get("headurl"));
        return convertView;
    }

    private Map<String, Bitmap> bmps = new HashMap<String, Bitmap>();

    private void setViewImage(ImageView v, String headurl) {
        Bitmap bmp = bmps.get(headurl);
        if (bmp != null) {
            v.setImageBitmap(bmp);
            return;
        }
        try {
            URL url = new URL(headurl);
            bmp = BitmapFactory.decodeStream(url.openStream());
            v.setImageBitmap(bmp);
            bmps.put(headurl, bmp);
        } catch (IOException e) {
            e.printStackTrace();
            setViewImage(v, headurl);
        }
    }

    static class ViewHolder {

        ImageView image;

        TextView text;
    }
}
