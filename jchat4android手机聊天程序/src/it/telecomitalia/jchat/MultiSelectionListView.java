/*****************************************************************
 jChat is a  chat application for Android based on JADE
  Copyright (C) 2008 Telecomitalia S.p.A. 
 
 GNU Lesser General Public License

 This is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation, 
 version 2.1 of the License. 

 This software is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this software; if not, write to the
 Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 Boston, MA  02111-1307, USA.
 *****************************************************************/
 
package it.telecomitalia.jchat;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * Customized {@link ListView} that shows checkbox inside each item to allow multiple selections
 * of contacts. It is used in {@link ContactListActivity} to show the contact list.
 * Uses a customized xml-based layout
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0
 */
public class MultiSelectionListView extends ListView{

	/**
	 * Instantiates a new multiple selection list view.
	 * @param context current application context
	 * @param attrs set of attributes neeeded by parent
	 * 
	 */
	public MultiSelectionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * Gets the all contacts checked in the list.
	 * 
	 * @return the list of all checked contacts 
	 */
	public List<String> getAllSelectedItems () {				
		return ((ContactListAdapter) getAdapter()).getAllSelectedItemIds();
	}
	
	/**
	 * Uncheck all selected items.
	 */
	public void uncheckAllSelectedItems(){
		((ContactListAdapter) getAdapter()).uncheckAll();	
		invalidate();
	}
		
}
 
