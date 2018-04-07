package com.src.zhang.demo;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import android.widget.ExpandableListView;

/**
 * 私念
 * 
 * @author Administrator
 * 
 */
public class ListViewActivity extends Activity
{
	ExpandableListView expandableListView;

	ListViewAdapter treeViewAdapter;

	public String[] groups = { "列表1", "列表2", "列表3" };

	public String[][] child = { { "" }, { "" }, { "", "" } };

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		treeViewAdapter = new ListViewAdapter(this,
				ListViewAdapter.PaddingLeft >> 1);
		expandableListView = (ExpandableListView) this
				.findViewById(R.id.expandableListView);

		List<ListViewAdapter.TreeNode> treeNode = treeViewAdapter.GetTreeNode();
		for (int i = 0; i < groups.length; i++)
		{
			ListViewAdapter.TreeNode node = new ListViewAdapter.TreeNode();
			node.parent = groups[i];
			for (int ii = 0; ii < child[i].length; ii++)
			{
				node.childs.add(child[i][ii]);
			}
			treeNode.add(node);
		}

		treeViewAdapter.UpdateTreeNode(treeNode);
		expandableListView.setAdapter(treeViewAdapter);
	}
}