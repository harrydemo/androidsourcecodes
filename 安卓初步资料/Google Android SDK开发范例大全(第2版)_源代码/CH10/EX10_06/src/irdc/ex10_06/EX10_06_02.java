package irdc.ex10_06;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/* �½���������Activity */
public class EX10_06_02 extends Activity
{
  private EditText mEditText01, mEditText02, mEditText03;
  private Button mButton01;
  static final private int MENU_EDIT = Menu.FIRST+1;
  static final private int MENU_DRAW = Menu.FIRST+2;
  private MySQLiteOpenHelper dbHelper=null;
  /* version������ڵ���1 */
  int version = 1;
  
  /* Table���ݱ� */
  String tables[] = { "t_restaurant" };
  
  /* �ֶ����� */
  String fieldNames[][] =
  {
    { "f_id", "f_name", "f_address", "f_cal" }
  };
  
  /* �ֶ����� */
  String fieldTypes[][] =
  {
    { "INTEGER PRIMARY KEY AUTOINCREMENT", "text" , "text", "text"}
  };
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.layout_add);
    
    mEditText01 = (EditText)findViewById(R.id.myEditText1);
    mEditText02 = (EditText)findViewById(R.id.myEditText2);
    mEditText03 = (EditText)findViewById(R.id.myEditText3);
    
    //mEditText01.setText("ˮ�����������");
    //mEditText02.setText("̨�����ں������·513��22Ū15��");
    //mEditText03.setText("540");
    
    /* CREATE TABLE t_restaurant (f_id INTEGER,f_name text,f_cal text) */
    dbHelper = new MySQLiteOpenHelper(this, "mydb", null, version, tables, fieldNames, fieldTypes);
    
    mButton01 = (Button)findViewById(R.id.myButton1);
    mButton01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View arg0)
      {
        // TODO Auto-generated method stub
        /* �����������ֶνԲ�Ϊ��ֵ */
        if(mEditText01.getText().toString().trim().length()!=0 && mEditText02.getText().toString().trim().length()!=0 && mEditText03.getText().toString().trim().length()!=0)
        {
          String f[] = { "f_id", "f_name" };
          String[] selectionArgs = { mEditText01.getText().toString() };
          /* ����select������Ѱ���ݱ� */
          Cursor c = dbHelper.select(tables[0], f, "f_name=?", selectionArgs, null, null, null);
          String strRes = "";
          while (c.moveToNext())
          {
            strRes += c.getString(0) + "\n";
          }
          
          if(strRes == "")
          {
            /* ���ݿ�δ�ҵ��������ƣ������� */
            String f2[] = { "f_name", "f_address", "f_cal"};
            String v[] = { mEditText01.getText().toString().trim(), mEditText02.getText().toString().trim(), mEditText03.getText().toString().trim() };
            long rowid = dbHelper.insert(tables[0], f2, v);
            strRes += rowid + "\n";
          }
          else
          {
            /* ���������Ѵ������ݿ� */
            
          }
          
          /* ǰ���༭���� */
          if(dbHelper!=null && dbHelper.getReadableDatabase().isOpen())
          {
            dbHelper.close();
          }
          Intent intent = new Intent();
          intent.setClass(EX10_06_02.this, EX10_06_03.class);
          startActivity(intent);
          finish();
        }
      }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // TODO Auto-generated method stub
    /* menuȺ��ID */
    int idGroup1 = 0;
    
    /* The order position of the item */
    int orderItem2 = Menu.NONE+1;
    int orderItem3 = Menu.NONE+2;
    
    menu.add(idGroup1, MENU_EDIT, orderItem2, R.string.str_manu2).setIcon(android.R.drawable.ic_dialog_info);
    menu.add(idGroup1, MENU_DRAW, orderItem3, R.string.str_manu3).setIcon(R.drawable.hipposmall);
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    // TODO Auto-generated method stub
    Intent intent = new Intent();
    switch(item.getItemId())
    {
      case (MENU_EDIT):
        /* ǰ���༭���� */
        if(dbHelper!=null && dbHelper.getReadableDatabase().isOpen())
        {
          dbHelper.close();
        }
        intent.setClass(EX10_06_02.this, EX10_06_03.class);
        startActivity(intent);
        finish();
        break;
      case (MENU_DRAW):
        /* ǰ��ϵͳ�����ѡ��������� */
        if(dbHelper!=null && dbHelper.getReadableDatabase().isOpen())
        {
          dbHelper.close();
        }
        intent.setClass(EX10_06_02.this, EX10_06_04.class);
        startActivity(intent);
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    super.onResume();
  }
  
  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    super.onPause();
  }
  
  @Override
  protected void onDestroy()
  {
    // TODO Auto-generated method stub
    if(dbHelper!=null && dbHelper.getReadableDatabase().isOpen())
    {
      dbHelper.close();
    }
    super.onDestroy();
  }
}

