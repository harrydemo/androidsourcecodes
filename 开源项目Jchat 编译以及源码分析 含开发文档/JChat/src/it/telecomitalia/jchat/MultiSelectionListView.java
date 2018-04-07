package it.telecomitalia.jchat;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Customized {@link ListView} that shows checkbox inside each item to allow
 * multiple selections of contacts. It is used in {@link ContactListActivity} to
 * show the contact list. Uses a customized xml-based layout
 * 定制的{@链接的ListView显示在每个项目的复选框，以允许选择多个联系人。它是用来}
 * {@链接ContactListActivity显示联系人列表。
 * 使用定制的基于XML的布局
 */

public class MultiSelectionListView extends ListView
{
	/**
	 * Instantiates a new multiple selection list view.
	 * 实例化一个新的多重选择列表视图。
	 * @param context
	 *            current application context
	 * @param attrs
	 *            set of attributes neeeded by parent
	 * 
	 */
	public MultiSelectionListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * Gets the all contacts checked in the list.
	 * 获取列表中的所有检查的接触。
	 * @return the list of all checked contacts
	 */
	public List<String> getAllSelectedItems()
	{
		return ((ContactListAdapter) getAdapter()).getAllSelectedItemIds();
	}

	/**
	 * Uncheck all selected items.
	 * 取消所有选中选项
	 */
	public void uncheckAllSelectedItems()
	{
		((ContactListAdapter) getAdapter()).uncheckAll();
		invalidate();
	}

}
