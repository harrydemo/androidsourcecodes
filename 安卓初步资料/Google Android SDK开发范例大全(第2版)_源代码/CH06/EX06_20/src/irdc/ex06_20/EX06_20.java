package irdc.ex06_20;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class EX06_20 extends ListActivity
{
  private ListAdapter mListAdapter;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    /* 取得通讯录里的数据 */
    Cursor cursor = getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    /* 取得笔数 */
    int c = cursor.getCount();
    if (c == 0)
    {
      Toast.makeText(EX06_20.this, "联系人无资料\n请添加联系人资料", Toast.LENGTH_LONG)
          .show();
    }

    /* 用Activity管理Cursor */
    startManagingCursor(cursor);

    /* 欲显示的字段名称 */
    String[] columns =
    { ContactsContract.Contacts.DISPLAY_NAME };

    /* 欲显示字段名称的view */
    int[] entries =
    { android.R.id.text1 };

    mListAdapter = new SimpleCursorAdapter(this,
        android.R.layout.simple_list_item_1, cursor, columns, entries);
    /* 设置Adapter */
    setListAdapter(mListAdapter);

  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id)
  {
    // TODO Auto-generated method stub

    /* 取得点击的Cursor */
    Cursor c = (Cursor) mListAdapter.getItem(position);

    /* 取得_id这个字段得值 */
    int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));

    /* 用_id去查询电话的Cursor */
    Cursor phones = getContentResolver().query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
        null, null);

    StringBuffer sb = new StringBuffer();
    int type, typeLabelResource;
    String number;

    if (phones.getCount() > 0)
    {

      /* 2.0可以允许User设定多组电话号码，依序捞出 */
      while (phones.moveToNext())
      {
        /* 取得电话的TYPE */
        type = phones.getInt(phones
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        /* 取得电话号码 */
        number = phones.getString(phones
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        /* 由电话的TYPE找出LabelResource */
        typeLabelResource = ContactsContract.CommonDataKinds.Phone
            .getTypeLabelResource(type);

        sb.append(getString(typeLabelResource) + ": " + number + "\n");

      }
    } else
    {
      sb.append("no Phone number found");
    }

    Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();

    super.onListItemClick(l, v, position, id);
  }

}

