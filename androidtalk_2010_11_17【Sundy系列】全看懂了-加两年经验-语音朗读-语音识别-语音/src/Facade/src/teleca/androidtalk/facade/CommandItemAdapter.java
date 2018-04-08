/**
 * @description : Identify a data model extends ArrayAdapter.
 * @version 1.0
 * @author Alex
 * @date 2010-11-10
 */
package teleca.androidtalk.facade;

import java.util.List;

import teleca.androidtalk.facade.util.Command;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommandItemAdapter extends ArrayAdapter<Command> 
{
	int resource;
	public CommandItemAdapter(Context _context, int _resource,List<Command> _items) 
	{
		super(_context, _resource, _items);
		resource = _resource;
	}
	/**
	 *  get the view of user_identify_command 
	 *  
	 * @param position,convertView,parent
	 * @return View
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LinearLayout todoView;
		Command item = getItem(position);
		String cmdName = item.getCmdName();
		String cmdCategory = item.getCmdCategory();
		String cmdRelation = item.getRelation();
		if (convertView == null) 
		{
			todoView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, todoView, true);
		}
		else 
		{
			todoView = (LinearLayout) convertView;
		}
		TextView nameView = (TextView) todoView.findViewById(R.id.TextView01);
		TextView categoryView = (TextView) todoView.findViewById(R.id.TextView02);
		TextView relationView = (TextView) todoView.findViewById(R.id.TextView03);
		nameView.setText(cmdName);
//		nameView.setText(item.getId());
		categoryView.setText(cmdCategory);
		relationView.setText(cmdRelation);
		return todoView;
	}
}
