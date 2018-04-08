package com.example.myexpandablelistdemo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageExpandableListAdapter extends BaseExpandableListAdapter
{
    ArrayList<HashMap<String,View>> list;
    Activity context;
    /**
     * 
     * Creates a new instance of ImageExpandableListAdapter.
     *一个菜单为一个Map, group:一级菜单显示内容（view），child:二级菜单显示内容（view）
     * @param list
     */
    public ImageExpandableListAdapter(Activity contextActivity,ArrayList<HashMap<String,View>> list)
    {
        // TODO Auto-generated constructor stub
        context = contextActivity;
        this.list = list;
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return list.get(groupPosition).get("child");
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent)
    {
        
         return list.get(groupPosition).get("child");
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        // TODO Auto-generated method stub
        return list.get(groupPosition).get("group");
    }

    @Override
    public int getGroupCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        
        return list.get(groupPosition).get("group");
    }

    @Override
    public boolean hasStableIds()
    {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    

}
