/**
 * @description : a demo of operation dialing .
 * @version 1.0
 * @author kevin qin
 * @date 2010-11-10
 */
package teleca.androidtalk.operation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.Contacts;
import android.telephony.PhoneNumberUtils;
import android.widget.Toast;

/**
 * this class call an phone number or contacts
 * 
 * @author kevin qin
 *
 */
public class OperationDialing extends OperationBase implements IOperation{
	private Context context;
	
	@Override
	public OpResult doOperation(Context context, String opData) {
		if (opData != null && context != null) {
			this.context = context;
			if (PhoneNumberUtils.isGlobalPhoneNumber(opData)) {
				callPhoneNumber(opData);
			}
			else {
				callContact(opData);
			}
		}
		return null;
	}
	
	/**
	 *  call a phone number
	 *  
	 * @param phoneNumber
	 * @return 
	 */
	private void callPhoneNumber(String phoneNumber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
		context.startActivity(intent);
	}
	
	/**
	 *  call a contact
	 *  
	 * @param name contact's name
	 * @return 
	 */
	private void callContact(String name) {
		String[] projection = {Contacts.People.DISPLAY_NAME, Contacts.People._ID};
		Cursor contacts = null;
		try {
			contacts = context.getContentResolver().query(Contacts.People.CONTENT_URI, projection, Contacts.People.DISPLAY_NAME + " like '" + name + "'", null, null);
		}
		catch(SQLiteException e) { }
		if (contacts != null && contacts.getCount() > 0) {
			if (contacts.getCount() == 1) {
				contacts.moveToFirst();
				String id = contacts.getString(contacts.getColumnIndex(Contacts.People._ID));
				String[] phoneProjection = {Contacts.Phones.PERSON_ID, Contacts.Phones.NUMBER};
				Cursor phones = context.getContentResolver().query(Contacts.Phones.CONTENT_URI, phoneProjection, Contacts.Phones.PERSON_ID + " = " + id, null, null);
				if (phones != null && phones.getCount() > 0) {
					if (phones.getCount() == 1) {
						phones.moveToFirst();
						callPhoneNumber(phones.getString(phones.getColumnIndex(Contacts.Phones.NUMBER)));
					}
					else {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Contacts.People.CONTENT_URI + "/" + id));
						context.startActivity(intent);
					}
				}
				else {
					// FIXME: we should return this message to invoker.
					Toast.makeText(context, "no phone number", Toast.LENGTH_SHORT).show();
				}
				if (phones != null) phones.close();
			}
			else {
				Intent intent = new Intent(Intent.ACTION_VIEW, Contacts.People.CONTENT_URI);
				context.startActivity(intent);
			}
		}
		else {
			// FIXME: we should return this message to invoker.
			Toast.makeText(context, "no contact", Toast.LENGTH_SHORT).show();
		}
		if (contacts != null) contacts.close();
	}
	
}