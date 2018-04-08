package cn.com.karl.utils;

import java.util.List;

import cn.com.karl.dida.R;
import cn.com.karl.domain.Sent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
    private Context context;
    
	private List<Sent> sents;

	public List<Sent> getSents() {
		return sents;
	}

	public void setSents(List<Sent> sents) {
		this.sents = sents;
	}

	public MyAdapter(Context context) {
		super();
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sents.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sents.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Sent sent=sents.get(position);
		if(convertView==null){
		convertView=LayoutInflater.from(context).inflate(R.layout.listitem, null);
		}
		TextView textOrig=(TextView) convertView.findViewById(R.id.text_orig);
		TextView textTrans=(TextView) convertView.findViewById(R.id.text_trans);
		textOrig.setText(sent.getOrig());
		textTrans.setText(sent.getTrans());
		
		return convertView;
	}

}
