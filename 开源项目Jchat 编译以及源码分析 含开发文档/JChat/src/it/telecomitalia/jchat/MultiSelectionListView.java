package it.telecomitalia.jchat;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Customized {@link ListView} that shows checkbox inside each item to allow
 * multiple selections of contacts. It is used in {@link ContactListActivity} to
 * show the contact list. Uses a customized xml-based layout
 * ���Ƶ�{@���ӵ�ListView��ʾ��ÿ����Ŀ�ĸ�ѡ��������ѡ������ϵ�ˡ���������}
 * {@����ContactListActivity��ʾ��ϵ���б�
 * ʹ�ö��ƵĻ���XML�Ĳ���
 */

public class MultiSelectionListView extends ListView
{
	/**
	 * Instantiates a new multiple selection list view.
	 * ʵ����һ���µĶ���ѡ���б���ͼ��
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
	 * ��ȡ�б��е����м��ĽӴ���
	 * @return the list of all checked contacts
	 */
	public List<String> getAllSelectedItems()
	{
		return ((ContactListAdapter) getAdapter()).getAllSelectedItemIds();
	}

	/**
	 * Uncheck all selected items.
	 * ȡ������ѡ��ѡ��
	 */
	public void uncheckAllSelectedItems()
	{
		((ContactListAdapter) getAdapter()).uncheckAll();
		invalidate();
	}

}
