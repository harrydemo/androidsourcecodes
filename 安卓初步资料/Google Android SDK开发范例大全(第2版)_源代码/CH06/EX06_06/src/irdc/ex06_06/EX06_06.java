package irdc.ex06_06;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class EX06_06 extends Activity
{
  private TextView myTextView1;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myTextView1 = (TextView) findViewById(R.id.myTextView1);

    /* ������PhoneStateListener */
    exPhoneCallListener myPhoneCallListener = new exPhoneCallListener();
    /* ȡ�õ绰���� */
    TelephonyManager tm = (TelephonyManager) this
        .getSystemService(Context.TELEPHONY_SERVICE);
    /* ע��Listener */
    tm.listen(myPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

  }

  /* �ڲ�class�̳�PhoneStateListener */
  public class exPhoneCallListener extends PhoneStateListener
  {
    /* ��дonCallStateChanged��״̬�ı�ʱ�ı�myTextView1�����ּ���ɫ */
    public void onCallStateChanged(int state, String incomingNumber)
    {
      switch (state)
      {
        /* ������״̬ʱ */
        case TelephonyManager.CALL_STATE_IDLE:
          myTextView1.setTextColor(getResources().getColor(R.drawable.red));
          myTextView1.setText("CALL_STATE_IDLE");
          break;
        /* ����绰ʱ */
        case TelephonyManager.CALL_STATE_OFFHOOK:
          myTextView1.setTextColor(getResources().getColor(R.drawable.green));
          myTextView1.setText("CALL_STATE_OFFHOOK");
          break;
        /* �绰����ʱ */
        case TelephonyManager.CALL_STATE_RINGING:
          getContactPeople(incomingNumber);
          break;
        default:
          break;
      }
      super.onCallStateChanged(state, incomingNumber);
    }
  }

  private void getContactPeople(String incomingNumber)
  {
    myTextView1.setTextColor(Color.BLUE);
    ContentResolver contentResolver = getContentResolver();
    Cursor cursor = null;

    /* cursor��Ҫ�ŵ��ֶ����� */
    String[] projection = new String[]
    { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER };

    /* ������绰������Ҹ���ϵ�� */
    cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
        ContactsContract.CommonDataKinds.Phone.NUMBER + "=?", new String[]
        { incomingNumber }, "");

    /* �Ҳ�����ϵ�� */
    if (cursor.getCount() == 0)
    {
      myTextView1.setText("unknown Number:" + incomingNumber);
    } else if (cursor.getCount() > 0)
    {
      cursor.moveToFirst();
      /* projection��������� */
      String name = cursor.getString(1);
      myTextView1.setText(name + ":" + incomingNumber);
    }

  }
}

