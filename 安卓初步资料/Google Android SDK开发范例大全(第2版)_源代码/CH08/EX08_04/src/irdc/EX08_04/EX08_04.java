package irdc.EX08_04;

import android.app.Activity; 
import android.content.Intent; 
import android.net.Uri; 
import android.os.Bundle; 
import android.view.View; 
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView; 

public class EX08_04 extends Activity 
{
  /*声明一个ListView,TextView对象变量
   * 一个String array变量保存收藏夹
   * 与String变量来储存网址*/
  private ListView mListView1; 
  private TextView mTextView1; 
  private String[] myFavor;
  private String  myUrl;
   
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
     
    /*透过findViewById建构子建立ListView与TextView对象*/ 
    mListView1 =(ListView) findViewById(R.id.myListView1); 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mTextView1.setText(getResources().getString(R.string.hello));
    /*将收藏夹列表由string.xml中导入*/
    myFavor = new String[] { 
                               getResources().getString(R.string.str_list_url1), 
                               getResources().getString(R.string.str_list_url2), 
                               getResources().getString(R.string.str_list_url3), 
                               getResources().getString(R.string.str_list_url4) 
                             }; 
    /*自定义一ArrayAdapter准备传入ListView中,并将myFavor清单以参数传入*/ 
    ArrayAdapter<String> adapter = new  
    ArrayAdapter<String> 
    (EX08_04.this, android.R.layout.simple_list_item_1, myFavor); 
    
    /*将自定义完成的ArrayAdapter传入自定义的ListView中*/
    mListView1.setAdapter(adapter);
    /*将ListAdapter的可选(Focusable)选单选项打开*/
    mListView1.setItemsCanFocus(true);  
    /*设定ListView选单选项设为每次只能单一选项*/ 
    mListView1.setChoiceMode 
    (ListView.CHOICE_MODE_SINGLE); 
    /*设定ListView选项的nItemClickListener*/
    mListView1.setOnItemClickListener(new ListView.OnItemClickListener()
    { 

      @Override
      /*重写OnItemClick方法*/
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3)
      {
        // TODO Auto-generated method stub
        /*若所选菜单的文字与myFavor字符串数组第一个文字相同*/ 
        if(arg0.getAdapter().getItem(arg2).toString()==myFavor[0].toString())
        {
          /*取得网址并调用goToUrl()方法*/
          myUrl=getResources().getString(R.string.str_url1);
          goToUrl(myUrl);
        }
        /*若所选菜单的文字与myFavor字符串数组第二个文字相同*/ 
        else if (arg0.getAdapter().getItem(arg2).toString()==myFavor[1].toString())
        {
          /*取得网址并呼叫goToUrl()方法*/
          myUrl=getResources().getString(R.string.str_url2);
          goToUrl(myUrl);
        } 
        /*若所选菜单的文字与myFavor字符串数组第三个文字相同*/ 
        else if (arg0.getAdapter().getItem(arg2).toString()==myFavor[2].toString())
        {
          /*取得网址并调用goToUrl()方法*/
          myUrl=getResources().getString(R.string.str_url3);
          goToUrl(myUrl);
        } 
        /*若所选菜单的文字与myFavor字符串数组第四个文字相同*/ 
        else if (arg0.getAdapter().getItem(arg2).toString()==myFavor[3].toString())
        {
          /*取得网址并调用goToUrl()方法*/
          myUrl=getResources().getString(R.string.str_url4);
          goToUrl(myUrl);
        } 
        /*以上皆非*/
        else
        {
          /*显示错误信息*/
          mTextView1.setText("Ooops!!出错了");
        } 
      }
    }); 
  } 
    /*开启网页的方法*/
    private void goToUrl(String url)
    {
      Uri uri = Uri.parse(url); 
      Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
      startActivity(intent); 
    }
} 

