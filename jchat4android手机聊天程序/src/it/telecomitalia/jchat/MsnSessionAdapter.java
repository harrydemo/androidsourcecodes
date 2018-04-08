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


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter that describes the content of the list of messages shown in the {@link ChatActivity}
 * It is used to customized its appearance using a custom xml layout for its elements
 * 
 * @author Cristina Cucè
 * @author Marco Ughetti 
 * @author Stefano Semeria
 * @author Tiziana Trucco
 * @version 1.0 
 */
public class MsnSessionAdapter extends BaseAdapter {

	/** 
	 * List of View object providing a representation of each message
	 * received. 
	 */
	private LinkedList<View> messageViews;
	
	/** 
	 * The inflater used to transform custom layout xml files in View object  
	 */
	private LayoutInflater theInflater;
	
	/** 
	 * the conversation session this adapter refers to. 
	 */
	private MsnSession theSession;
	
	/** 
	 * Inner class used to generate color for contacts name. 
	 */
	private ContactColorGenerator colorGenerator;
	
	/**
	 * Instantiates a new session adapter.
	 * 
	 * @param vi the inflater that shall be used for inflating views
	 * @param res provides an access to xml layout resource files
	 */
	public MsnSessionAdapter(LayoutInflater vi, Resources res){
		theInflater = vi;
		messageViews = new LinkedList<View>();
		colorGenerator = new ContactColorGenerator(res);
	}
	
	/**
	 * Retrieves the number of items in this adapter
	 * @return number of items
	 */
	public int getCount() {
		return messageViews.size();
	}	
	
	/**
	 * Recreates the list of views in this adapter every time we change the current session (for example when we use the status bar notification 
	 * to move from one conversation to another)
	 * Only a single chat activity is always instantiated, that is redrawn each time we switch to another session 
	 * 
	 * @param session the session we switch to
	 */
	public void setNewSession(MsnSession session){
		theSession = session;
		messageViews.clear();
		
		List<MsnSessionMessage> messages = theSession.getMessageList();
		
		for (MsnSessionMessage msnSessionMessage : messages) {
			addMessageView(msnSessionMessage);
		}
	}
	
	
	/**
	 * Creates a new view by inflating the xml layout, populates it with message data and insert it into 
	 * the list of message view's head 
	 * 
	 * @param msg the message for which we need to create a new view
	 */
	public void addMessageView(MsnSessionMessage msg){
		View messageView = theInflater.inflate(R.layout.session_msg_layout, null);
	
		TextView senderNameTxtView = (TextView) messageView.findViewById(R.id.sender_name);
		senderNameTxtView.setText(msg.getSenderName());
		senderNameTxtView.setTextColor(colorGenerator.getColor(msg.getSenderNumTel()));
		TextView timeTextView = (TextView) messageView.findViewById(R.id.time_arrived);
		timeTextView.setText(msg.getTimeReceivedAsString());
		TextView contentTextView = (TextView) messageView.findViewById(R.id.message_txt);
		contentTextView.setText(msg.getMessageContent());

		messageViews.addFirst(messageView);
	}
	

	/**
	 * Retrieves the {@link MsnSessionMessage} at the given position.
	 * 
	 * @param index position in the list
	 * @return the {@link MsnSessionMessage} at given position
	 */
	public Object getItem(int index) {
		// TODO Auto-generated method stub
		List<MsnSessionMessage> messageList = theSession.getMessageList();
		MsnSessionMessage msg = messageList.get(index);
		return msg;
	}

	/**
	 * Dummy implementation for this Adapter method
	 */
	public long getItemId(int arg0) {
		return 0;
	}

	/**
	 * Builds a View object from the message at the given position
	 * 
	 * @param position index of the item 
	 * @param convertView view that could be used to avoid building a new view (not used)
	 * @param parent parent view (not used)
	 * 
	 * @return a View object corresponding to the message having the given index
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = messageViews.get(position);
		return v;
	}

	
	
	/**
	 * Inner class that generates colors to be shown in adapter's views (contact names have different colors)
	 */
	private class  ContactColorGenerator{
		
		/** 
		 * Map that stores colors for each contact name (only ten different contacts in a single conversation)
		 */
		private Map<String,Integer>  contactColorMap; 
		
		/** 
		 * The list of available colors. Colors are not randomly generated but stored in a palette of ten entries
		 * statically.  
		 */
		private int[] colorPalette;
		
		/** 
		 * Counter used to select a color from the static palette 
		 */
		private int counter;
		
		/**
		 * Instantiates a new contact color generator.
		 * 
		 * @param res object that makes all resources available
		 */
		public ContactColorGenerator(Resources res){
			contactColorMap = new HashMap<String, Integer>();
			colorPalette = new int[10];
			counter =0;
			loadPalette(res);
			
		}
		
		/**
		 * Load the static palette from color.xml file
		 * 
		 * @param res object that makes all resources available
		 */
		private void loadPalette(Resources res){
			colorPalette[0] = res.getColor(R.color.chat_dark_yellow);
			colorPalette[1] = res.getColor(R.color.chat_dark_orange);
			colorPalette[2] = res.getColor(R.color.chat_grass_green);
			colorPalette[3] = res.getColor(R.color.chat_pale_yellow);
			colorPalette[4] = res.getColor(R.color.chat_dark_pink);
			colorPalette[5] = res.getColor(R.color.chat_light_orange);
			colorPalette[6] = res.getColor(R.color.chat_dark_green);
			colorPalette[7] = res.getColor(R.color.chat_olive_green);
			colorPalette[8] = res.getColor(R.color.chat_earth_brown);
			colorPalette[9] = res.getColor(R.color.chat_strong_purple);
		}
		
		/**
		 * Generates a color for the given contact, taking it from the static palette and putting it into the map
		 * Please note that a new color shall be created only if a new contact appears.
		 * 
		 * @param contactName the contact name
		 * 
		 * @return the color created for that contact name
		 */
		public int getColor(String contactName){
			Integer color = contactColorMap.get(contactName);
			int colAsInt=0;
			
			
			//If color not available
			if (color == null){
				//Create a new random one
				colAsInt = colorPalette[counter];
				//Put it into the map
				contactColorMap.put(contactName, Integer.valueOf(colAsInt));
				counter = (counter + 1)% 10;
			} else {
				//retrieve the already created color
				colAsInt = color.intValue();
			}
			return colAsInt;
		}
		
	}
}
