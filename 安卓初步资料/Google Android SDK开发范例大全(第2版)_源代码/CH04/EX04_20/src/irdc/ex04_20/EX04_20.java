package irdc.ex04_20; 
import android.app.ListActivity; 
import android.os.Bundle;
import android.view.Menu; 
import android.view.MenuItem; 
import android.view.View; 
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast; 
public class EX04_20 extends ListActivity 
{ 
  private int selectedItem = -1; 
  private String[] mString; 
  static final private int MENU_LIST1 = Menu.FIRST; 
  static final private int MENU_LIST2 = Menu.FIRST+1;
  private ArrayAdapter<String> mla; 
  @Override 
  protected void onCreate(Bundle savedInstanceState) 
  { 
    // TODO Auto-generated method stub 
    super.onCreate(savedInstanceState); 
    } 
  @Override 
  protected void onListItemClick(ListView l, View v, int position, long id) 
  { 
    // TODO Auto-generated method stub 
    selectedItem = position; 
    Toast.makeText(EX04_20.this, 
        mString[selectedItem],
        Toast.LENGTH_SHORT).show();
    super.onListItemClick(l, v, position, id); 
    }
  @Override 
  public boolean onCreateOptionsMenu(Menu menu) 
  { 
    // TODO Auto-generated method stub
    /* menuÈº×éID */
    int idGroup1 = 0;
    /* The order position of the item */
    int orderMenuItem1 = Menu.NONE;
    int orderMenuItem2 = Menu.NONE+1;
    menu.add(idGroup1, MENU_LIST1, orderMenuItem1, R.string.str_menu_list1);
    menu.add(idGroup1, MENU_LIST2, orderMenuItem2, R.string.str_menu_list2); 
    return super.onCreateOptionsMenu(menu); 
    } 
  @Override 
  public boolean onOptionsItemSelected(MenuItem item) 
  { 
    // TODO Auto-generated method stub 
    switch(item.getItemId()) 
    { 
    case (MENU_LIST1):
      mString = new String[] 
                           { getResources().getString(R.string.str_list1), 
                             getResources().getString(R.string.str_list2), 
                             getResources().getString(R.string.str_list3),
                             getResources().getString(R.string.str_list4) };
    mla = new ArrayAdapter<String>(EX04_20.this, R.layout.main, mString);
    EX04_20.this.setListAdapter(mla);
    break; 
    case (MENU_LIST2): 
      mString = new String[] 
                           { getResources().getString(R.string.str_list5), 
                             getResources().getString(R.string.str_list6), 
                             getResources().getString(R.string.str_list7), 
                             getResources().getString(R.string.str_list8) };
    mla = new ArrayAdapter<String>(EX04_20.this, R.layout.main, mString);
    EX04_20.this.setListAdapter(mla);
    break;
    }
    return super.onOptionsItemSelected(item);
    } 
  }
  
 
 
  
