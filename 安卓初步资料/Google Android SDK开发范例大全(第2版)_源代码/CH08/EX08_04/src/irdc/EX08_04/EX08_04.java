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
  /*����һ��ListView,TextView�������
   * һ��String array���������ղؼ�
   * ��String������������ַ*/
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
     
    /*͸��findViewById�����ӽ���ListView��TextView����*/ 
    mListView1 =(ListView) findViewById(R.id.myListView1); 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mTextView1.setText(getResources().getString(R.string.hello));
    /*���ղؼ��б���string.xml�е���*/
    myFavor = new String[] { 
                               getResources().getString(R.string.str_list_url1), 
                               getResources().getString(R.string.str_list_url2), 
                               getResources().getString(R.string.str_list_url3), 
                               getResources().getString(R.string.str_list_url4) 
                             }; 
    /*�Զ���һArrayAdapter׼������ListView��,����myFavor�嵥�Բ�������*/ 
    ArrayAdapter<String> adapter = new  
    ArrayAdapter<String> 
    (EX08_04.this, android.R.layout.simple_list_item_1, myFavor); 
    
    /*���Զ�����ɵ�ArrayAdapter�����Զ����ListView��*/
    mListView1.setAdapter(adapter);
    /*��ListAdapter�Ŀ�ѡ(Focusable)ѡ��ѡ���*/
    mListView1.setItemsCanFocus(true);  
    /*�趨ListViewѡ��ѡ����Ϊÿ��ֻ�ܵ�һѡ��*/ 
    mListView1.setChoiceMode 
    (ListView.CHOICE_MODE_SINGLE); 
    /*�趨ListViewѡ���nItemClickListener*/
    mListView1.setOnItemClickListener(new ListView.OnItemClickListener()
    { 

      @Override
      /*��дOnItemClick����*/
      public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3)
      {
        // TODO Auto-generated method stub
        /*����ѡ�˵���������myFavor�ַ��������һ��������ͬ*/ 
        if(arg0.getAdapter().getItem(arg2).toString()==myFavor[0].toString())
        {
          /*ȡ����ַ������goToUrl()����*/
          myUrl=getResources().getString(R.string.str_url1);
          goToUrl(myUrl);
        }
        /*����ѡ�˵���������myFavor�ַ�������ڶ���������ͬ*/ 
        else if (arg0.getAdapter().getItem(arg2).toString()==myFavor[1].toString())
        {
          /*ȡ����ַ������goToUrl()����*/
          myUrl=getResources().getString(R.string.str_url2);
          goToUrl(myUrl);
        } 
        /*����ѡ�˵���������myFavor�ַ������������������ͬ*/ 
        else if (arg0.getAdapter().getItem(arg2).toString()==myFavor[2].toString())
        {
          /*ȡ����ַ������goToUrl()����*/
          myUrl=getResources().getString(R.string.str_url3);
          goToUrl(myUrl);
        } 
        /*����ѡ�˵���������myFavor�ַ���������ĸ�������ͬ*/ 
        else if (arg0.getAdapter().getItem(arg2).toString()==myFavor[3].toString())
        {
          /*ȡ����ַ������goToUrl()����*/
          myUrl=getResources().getString(R.string.str_url4);
          goToUrl(myUrl);
        } 
        /*���ϽԷ�*/
        else
        {
          /*��ʾ������Ϣ*/
          mTextView1.setText("Ooops!!������");
        } 
      }
    }); 
  } 
    /*������ҳ�ķ���*/
    private void goToUrl(String url)
    {
      Uri uri = Uri.parse(url); 
      Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
      startActivity(intent); 
    }
} 

