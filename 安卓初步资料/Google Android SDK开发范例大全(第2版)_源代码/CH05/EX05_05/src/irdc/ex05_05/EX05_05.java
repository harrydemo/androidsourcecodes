package irdc.ex05_05; 
import android.app.Activity; 
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor; 
import android.os.Bundle;
import android.view.Menu; 
import android.view.MenuItem;
import android.view.View; 
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView; 
import android.widget.SimpleCursorAdapter;
public class EX05_05 extends Activity 
{ 
  private ToDoDB myToDoDB;
  private Cursor myCursor;
  private ListView myListView; 
  private EditText myEditText;
  private int _id;
  protected final static int MENU_ADD = Menu.FIRST; 
  protected final static int MENU_EDIT = Menu.FIRST + 1;
  protected final static int MENU_DELETE = Menu.FIRST + 2;
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
    super.onOptionsItemSelected(item);
    switch (item.getItemId()) 
    {
      case MENU_ADD: this.addTodo();
      break;
      case MENU_EDIT: this.editTodo();
      break; 
      case MENU_DELETE: this.deleteTodo();
      break;
    }
    return true; 
    } 
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  { 
    super.onCreateOptionsMenu(menu); 
    /* 新增三个MENU */ 
    menu.add(Menu.NONE, MENU_ADD, 0, R.string.strAddButton); 
    menu.add(Menu.NONE, MENU_EDIT, 0, R.string.strEditButton); 
    menu.add(Menu.NONE, MENU_DELETE, 0, R.string.strDeleteButton);
    return true; 
    } 
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main);
    myListView = (ListView) this.findViewById(R.id.myListView); 
    myEditText = (EditText) this.findViewById(R.id.myEditText); 
    myToDoDB = new ToDoDB(this); 
    /* 取得DataBase里的资料 */
    myCursor = myToDoDB.select();
    /* new SimpleCursorAdapter并将myCursor传入，显示数据的字段为todo_text */ 
    SimpleCursorAdapter adapter = 
      new SimpleCursorAdapter(this, R.layout.list, myCursor, new String[] { ToDoDB.FIELD_TEXT }, new int[] { R.id.listTextView1 });
    myListView.setAdapter(adapter); 
    /* 将myListView加入OnItemClickListener */ 
    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    { 
      @Override
      public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) 
      { 
        /* 将myCursor移到所点选的值 */
        myCursor.moveToPosition(arg2);
        /* 取得字段_id的值 */
        _id = myCursor.getInt(0); 
        /* 取得字段todo_text的值 */ 
        myEditText.setText(myCursor.getString(1));
        }
      }); 
    myListView .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
    {
      @Override 
      public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3)
      { 
        /* getSelectedItem所取得的是SQLiteCursor */
        SQLiteCursor sc = (SQLiteCursor) arg0.getSelectedItem();
        _id = sc.getInt(0); 
        myEditText.setText(sc.getString(1)); 
        }
      @Override 
      public void onNothingSelected(AdapterView arg0) { } 
      }); 
    } 
  private void addTodo() 
  {
    if (myEditText.getText().toString().equals("")) return;
    /* 新增数据到数据库 */
    myToDoDB.insert(myEditText.getText().toString()); 
    /* 重新查询 */
    myCursor.requery(); 
    /* 重新整理myListView */
    myListView.invalidateViews();
    myEditText.setText("");
    _id = 0; 
    } 
  private void editTodo()
  { 
    if (myEditText.getText().toString().equals(""))
      return;
    /* 修改数据 */ 
    myToDoDB.update(_id, myEditText.getText().toString()); 
    myCursor.requery(); 
    myListView.invalidateViews();
    myEditText.setText(""); _id = 0; 
    } 
  private void deleteTodo()
  {
    if(_id == 0)
    return;
    /* 删除数据 */ 
    myToDoDB.delete(_id); 
    myCursor.requery();
    myListView.invalidateViews(); 
    myEditText.setText("");
    _id = 0; 
    } 
  }