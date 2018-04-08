package irdc.ex10_06;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class EX10_06 extends Activity
{
  /* 独一无二的menu选项identifier，用以识别事件 */
  static final private int MENU_ADD = Menu.FIRST;
  static final private int MENU_EDIT = Menu.FIRST+1;
  static final private int MENU_DRAW = Menu.FIRST+2;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    // TODO Auto-generated method stub
    /* menu群组ID */
    int idGroup1 = 0;
    
    /* The order position of the item */
    int orderItem1 = Menu.NONE;
    int orderItem2 = Menu.NONE+1;
    int orderItem3 = Menu.NONE+2;
    
    /* 建立3个Menu选单 */
    menu.add(idGroup1, MENU_ADD, orderItem1, R.string.str_manu1).setIcon(android.R.drawable.ic_menu_add);
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
      case (MENU_ADD):
        /* 新建餐厅资料 */
        intent.setClass(EX10_06.this, EX10_06_02.class);
        startActivity(intent);
        finish();
        break;
      case (MENU_EDIT):
        /* 编辑数据 */
        intent.setClass(EX10_06.this, EX10_06_03.class);
        startActivity(intent);
        finish();
        break;
      case (MENU_DRAW):
        /* 前往系统随机数选择餐厅功能 */
        intent.setClass(EX10_06.this, EX10_06_04.class);
        startActivity(intent);
        finish();
        break;
    }
    return super.onOptionsItemSelected(item);
  }  
}

