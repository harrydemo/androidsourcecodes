package cn.err0r.android.sms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.err0r.android.sms.database.SMSINFODao;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;    
    private List<Map<String, Object>> mData;    
    public static Map<Integer, Boolean> isSelected;
    SMSINFODao smsinfodao;
    
    public ListAdapter(Context context) {    
        mInflater = LayoutInflater.from(context);    
        init(context);    
    }
    
  //初始化    
    private void init(Context context) {    
        mData=new ArrayList<Map<String, Object>>(); 
        smsinfodao = new SMSINFODao(context);
        Cursor cursor = smsinfodao.select();
        while (cursor.moveToNext()) {
        	Map<String, Object> map = new HashMap<String, Object>();    
            map.put("img", android.R.drawable.ic_dialog_email);  
            map.put("pn", cursor.getString(0));
            map.put("title", cursor.getString(1));
            map.put("body", cursor.getString(2));
            mData.add(map);
			
		}
        
        cursor.close();
        smsinfodao.close();
        //这儿定义isSelected这个map是记录每个listitem的状态，初始状态全部为false。    
        isSelected = new HashMap<Integer, Boolean>();    
        for (int i = 0; i < mData.size(); i++) {    
            isSelected.put(i, false);    
        }    
    }  
    
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size(); 
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;    
        //convertView为null的时候初始化convertView。    
        if (convertView == null) {    
            holder = new ViewHolder();    
            convertView = mInflater.inflate(R.layout.vlist, null);    
            holder.img = (ImageView) convertView.findViewById(R.id.img);    
            holder.title = (TextView) convertView.findViewById(R.id.title); 
            holder.pn = (TextView) convertView.findViewById(R.id.tvPn);
            holder.body = (TextView) convertView.findViewById(R.id.tvContext);
            holder.cBox = (CheckBox) convertView.findViewById(R.id.cb);    
            convertView.setTag(holder);    
        } else {    
            holder = (ViewHolder) convertView.getTag();    
        }    
        String txtBody = mData.get(position).get("body").toString();
        holder.img.setBackgroundResource((Integer) mData.get(position).get(    
                "img"));    
        holder.title.setText(mData.get(position).get("title").toString());    
        holder.pn.setText(mData.get(position).get("pn").toString());
        holder.body.setText(txtBody.length()>20?txtBody.substring(0, 20)+"...":txtBody);
        holder.cBox.setChecked(isSelected.get(position));    
        return convertView;
	}
	
	public final class ViewHolder {    
        public ImageView img;    
        public TextView title;
        public TextView pn;
        public TextView body;
        public CheckBox cBox;    
    }

}
