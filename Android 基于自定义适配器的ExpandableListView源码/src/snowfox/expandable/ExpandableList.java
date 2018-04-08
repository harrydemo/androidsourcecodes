package snowfox.expandable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandableList extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		ExpandableListView elv = (ExpandableListView)findViewById(R.id.expandableListView);

		//准备一级列表中显示的数据:2个一级列表,分别显示"group1"和"group2"
		List<Map<String, String>> groups = new ArrayList<Map<String, String>>();
		Map<String, String> group1 = new HashMap<String, String>();
		group1.put("group", "group1");
		Map<String, String> group2 = new HashMap<String, String>();
		group2.put("group", "group2");
		groups.add(group1);
		groups.add(group2);

		//准备第一个一级列表中的二级列表数据:两个二级列表,分别显示"childData1"和"childData2"
		List<Map<String, String>> child1 = new ArrayList<Map<String, String>>();
		Map<String, String> child1Data1 = new HashMap<String, String>();
		child1Data1.put("child", "child1Data1");
		Map<String, String> child1Data2 = new HashMap<String, String>();
		child1Data2.put("child", "child1Data2");
		child1.add(child1Data1);
		child1.add(child1Data2);
		
		//准备第二个一级列表中的二级列表数据:一个二级列表,显示"child2Data1"
		List<Map<String, String>> child2 = new ArrayList<Map<String, String>>();
		Map<String, String> child2Data1 = new HashMap<String, String>();
		child2Data1.put("child", "child2Data1");
		child2.add(child2Data1);
		
		//用一个list对象保存所有的二级列表数据
		List<List<Map<String, String>>> childs = new ArrayList<List<Map<String, String>>>();
		childs.add(child1);
		childs.add(child2);

		ExpandableAdapter viewAdapter = new ExpandableAdapter(this, groups, childs);
		elv.setAdapter(viewAdapter);
	}

	//自定义的ExpandListAdapter
	class ExpandableAdapter extends BaseExpandableListAdapter
	{
		private Context context;
		List<Map<String, String>> groups;
		List<List<Map<String, String>>> childs;

		/*
		 * 构造函数:
		 * 参数1:context对象
		 * 参数2:一级列表数据源
		 * 参数3:二级列表数据源
		 */
		public ExpandableAdapter(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs)
		{
			this.groups = groups;
			this.childs = childs;
			this.context = context;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition)
		{
			return childs.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		//获取二级列表的View对象
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent)
		{
			@SuppressWarnings("unchecked")
			String text = ((Map<String, String>) getChild(groupPosition, childPosition)).get("child");
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			//获取二级列表对应的布局文件, 并将其各元素设置相应的属性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.child, null);
			TextView tv = (TextView) linearLayout.findViewById(R.id.childTo);
			tv.setText(text);
			ImageView imageView = (ImageView)linearLayout.findViewById(R.id.imageView01);
			imageView.setImageResource(R.drawable.abc);
			
			return linearLayout;
		}

		@Override
		public int getChildrenCount(int groupPosition)
		{
			return childs.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition)
		{
			return groups.get(groupPosition);
		}

		@Override
		public int getGroupCount()
		{
			return groups.size();
		}

		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		//获取一级列表View对象
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			String text = groups.get(groupPosition).get("group");
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			//获取一级列表布局文件,设置相应元素属性
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.group, null);
			TextView textView = (TextView)linearLayout.findViewById(R.id.textView01);
			textView.setText(text);
			
			return linearLayout;
		}

		@Override
		public boolean hasStableIds()
		{
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return false;
		}

	}
}