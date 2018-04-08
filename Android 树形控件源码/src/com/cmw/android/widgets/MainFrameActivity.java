package com.cmw.android.widgets;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmw.android.data.TreeDataProvider;
import com.cmw.android.model.TreeElement;

public class MainFrameActivity  extends ListActivity{
	private List<TreeElement> nodes = new ArrayList<TreeElement>();
    private TreeViewAdapter treeViewAdapter = null;  
    private TreeDataProvider provider = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		provider = new TreeDataProvider(this);
		nodes = provider.getDataSource();
		 treeViewAdapter = new TreeViewAdapter(this, R.layout.mainframe,nodes);  
	     setListAdapter(treeViewAdapter);  
	     registerForContextMenu(getListView());  
	}
	
	
	@SuppressWarnings("unchecked")  
    private class TreeViewAdapter extends ArrayAdapter {  
        public TreeViewAdapter(Context context, int textViewResourceId,  
                List objects) {  
            super(context, textViewResourceId, objects);  
            mInflater = LayoutInflater.from(context);  
            mfilelist = objects;  
            mIconCollapse = BitmapFactory.decodeResource(  
                    context.getResources(), R.drawable.plus);  
            mIconExpand = BitmapFactory.decodeResource(context.getResources(),  
                    R.drawable.cut);  
        }  
  
        private LayoutInflater mInflater;  
        private List<TreeElement> mfilelist;  
        private Bitmap mIconCollapse;  
        private Bitmap mIconExpand;  
  
        public int getCount() {  
            return mfilelist.size();  
        }  
  
        public Object getItem(int position) {  
            return position;  
        }  
  
        public long getItemId(int position) {  
            return position;  
        }  
  
        public View getView(int position, View convertView, ViewGroup parent) {  
            ViewHolder holder;  
            convertView = mInflater.inflate(R.layout.mainframe, null);  
            holder = new ViewHolder();  
            holder.text = (TextView) convertView.findViewById(R.id.treetext);  
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);  
            convertView.setTag(holder);  
  
            final TreeElement obj = mfilelist.get(position);  
  
            holder.text.setOnClickListener(new View.OnClickListener() {  
  
                @Override  
                public void onClick(View v) {  
                    Log.i("TreeView", "obj.id:" + obj.getId());  
                    obj.forward(MainFrameActivity.this);
                }  
            });  
  
            int level = obj.getLevel();  
            holder.icon.setPadding(25 * (level + 1),  
                    holder.icon.getPaddingTop(), 0,  
                    holder.icon.getPaddingBottom());  
            holder.text.setText(obj.getTitle());  
            if (obj.isHasChild()&& (obj.isExpanded() == false)) {  
                holder.icon.setImageBitmap(mIconCollapse);  
            } else if (obj.isHasChild() && (obj.isExpanded() == true)) {  
                holder.icon.setImageBitmap(mIconExpand);  
            } else if (!obj.isHasChild()) {  
                holder.icon.setImageBitmap(mIconCollapse);  
                holder.icon.setVisibility(View.INVISIBLE);  
            }  
            return convertView;  
        }  
  
        class ViewHolder {  
            TextView text;  
            ImageView icon;  
  
        }  
    }  
  
    @Override  
    protected void onListItemClick(ListView l, View v, int position, long id) {  
        super.onListItemClick(l, v, position, id);  
        Log.i("TreeView", "position:" + position);  
        if (!nodes.get(position).isHasChild()) {  
            Toast.makeText(this, nodes.get(position).getTitle(), 2000);  
            return;  
        }  
  
        if (nodes.get(position).isExpanded()) {  
            nodes.get(position).setExpanded(false);  
            TreeElement element = nodes.get(position);  
            ArrayList<TreeElement> temp = new ArrayList<TreeElement>();  
  
            for (int i = position + 1; i < nodes.size(); i++) {  
                if (element.getLevel() >= nodes.get(i).getLevel()) {  
                    break;  
                }  
                temp.add(nodes.get(i));  
            }  
  
            nodes.removeAll(temp);  
  
            treeViewAdapter.notifyDataSetChanged();  
  
        } else {  
            TreeElement obj = nodes.get(position);  
            obj.setExpanded(true);  
            int level = obj.getLevel();  
            int nextLevel = level + 1;  
  
            for (TreeElement element : obj.getChilds()) {  
                element.setLevel(nextLevel);  
                element.setExpanded(false);  
                nodes.add(position + 1, element);  
  
            }  
            treeViewAdapter.notifyDataSetChanged();  
        }  
    }  
  
}
