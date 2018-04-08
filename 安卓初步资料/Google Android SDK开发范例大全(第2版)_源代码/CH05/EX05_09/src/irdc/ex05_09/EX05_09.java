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
  /* 要获取通讯簿的名称 */
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

    /* 取得ContentResolver */
    ContentResolver content = getContentResolver();

    /* 取得通讯簿的Cursor */
    contactCursor = content.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PEOPLE_PROJECTION,
        null, null, "");

    /* 将Cursor传入自实现的ContactsAdapter */
    myContactsAdapter = new ContactsAdapter(this, contactCursor);

    myAutoCompleteTextView.setAdapter(myContactsAdapter);

    myAutoCompleteTextView
        .setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
              long arg3)
          {
            /* 取得Cursor */
            Cursor c = myContactsAdapter.getCursor();
            /* 移到所点击的位置 */
            c.moveToPosition(arg2);
            String number = c
                .getString(c
                    .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
            /* 当找不到电话时显示无输入电话 */
            number = number == null ? "无输入电话" : number;
            myTextView1.setText(c.getString(c
                .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                + "的电话是" + number);
          }

        });

  }
}

