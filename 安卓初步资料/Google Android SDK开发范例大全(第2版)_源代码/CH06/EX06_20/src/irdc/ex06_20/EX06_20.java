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

    /* ȡ��ͨѶ¼������� */
    Cursor cursor = getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    /* ȡ�ñ��� */
    int c = cursor.getCount();
    if (c == 0)
    {
      Toast.makeText(EX06_20.this, "��ϵ��������\n�������ϵ������", Toast.LENGTH_LONG)
          .show();
    }

    /* ��Activity����Cursor */
    startManagingCursor(cursor);

    /* ����ʾ���ֶ����� */
    String[] columns =
    { ContactsContract.Contacts.DISPLAY_NAME };

    /* ����ʾ�ֶ����Ƶ�view */
    int[] entries =
    { android.R.id.text1 };

    mListAdapter = new SimpleCursorAdapter(this,
        android.R.layout.simple_list_item_1, cursor, columns, entries);
    /* ����Adapter */
    setListAdapter(mListAdapter);

  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id)
  {
    // TODO Auto-generated method stub

    /* ȡ�õ����Cursor */
    Cursor c = (Cursor) mListAdapter.getItem(position);

    /* ȡ��_id����ֶε�ֵ */
    int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));

    /* ��_idȥ��ѯ�绰��Cursor */
    Cursor phones = getContentResolver().query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
        null, null);

    StringBuffer sb = new StringBuffer();
    int type, typeLabelResource;
    String number;

    if (phones.getCount() > 0)
    {

      /* 2.0��������User�趨����绰���룬�����̳� */
      while (phones.moveToNext())
      {
        /* ȡ�õ绰��TYPE */
        type = phones.getInt(phones
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        /* ȡ�õ绰���� */
        number = phones.getString(phones
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        /* �ɵ绰��TYPE�ҳ�LabelResource */
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

