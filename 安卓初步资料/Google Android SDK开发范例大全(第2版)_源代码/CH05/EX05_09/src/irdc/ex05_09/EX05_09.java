package irdc.ex05_09;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class EX05_09 extends Activity
{
  private AutoCompleteTextView myAutoCompleteTextView;
  private TextView myTextView1;
  private Cursor contactCursor;
  private ContactsAdapter myContactsAdapter;
  /* Ҫ��ȡͨѶ�������� */
  public static final String[] PEOPLE_PROJECTION = new String[]
  { ContactsContract.Contacts._ID,
      ContactsContract.CommonDataKinds.Phone.NUMBER,
      ContactsContract.Contacts.DISPLAY_NAME };

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.myAutoCompleteTextView);
    myTextView1 = (TextView) findViewById(R.id.myTextView1);

    /* ȡ��ContentResolver */
    ContentResolver content = getContentResolver();

    /* ȡ��ͨѶ����Cursor */
    contactCursor = content.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PEOPLE_PROJECTION,
        null, null, "");

    /* ��Cursor������ʵ�ֵ�ContactsAdapter */
    myContactsAdapter = new ContactsAdapter(this, contactCursor);

    myAutoCompleteTextView.setAdapter(myContactsAdapter);

    myAutoCompleteTextView
        .setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
            /* ȡ��Cursor */
            Cursor c = myContactsAdapter.getCursor();
            /* �Ƶ��������λ�� */
            c.moveToPosition(arg2);
            String number = c
                .getString(c
                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            /* ���Ҳ����绰ʱ��ʾ������绰 */
            number = number == null ? "������绰" : number;
            myTextView1.setText(c.getString(c
                .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                + "�ĵ绰��" + number);
          }

        });

  }
}

