package irdc.ex05_18;

/* import���class */
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.os.Bundle; 
import android.telephony.TelephonyManager;

public class EX05_18 extends ListActivity 
{ 
  private TelephonyManager telMgr;
  private List<String> item=new ArrayList<String>();
  private List<String> value=new ArrayList<String>();
   
  @SuppressWarnings("static-access")
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /* ����main.xml Layout */
    setContentView(R.layout.main); 
    telMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
    
    /* ��ȡ�õ���Ϣд��List�� */
    /* ȡ��SIM��״̬ */
    item.add(getResources().getText(R.string.str_list0).toString());
    if(telMgr.getSimState()==telMgr.SIM_STATE_READY)
    {
      value.add("����");
    }
    else if(telMgr.getSimState()==telMgr.SIM_STATE_ABSENT)
    {
      value.add("��SIM��");
    }
    else
    {
      value.add("SIM����������δ֪��״̬");
    }
    
    /* ȡ��SIM������ */
    item.add(getResources().getText(R.string.str_list1).toString());
    if(telMgr.getSimSerialNumber()!=null)
    {
      value.add(telMgr.getSimSerialNumber());
    }
    else
    {
      value.add("�޷�ȡ��");
    }
    
    /* ȡ��SIM�������̴��� */
    item.add(getResources().getText(R.string.str_list2).toString());
    if(telMgr.getSimOperator().equals(""))
    {
      value.add("�޷�ȡ��");
    }
    else
    {
      value.add(telMgr.getSimOperator());
    }
    
    /* ȡ��SIM������������ */
    item.add(getResources().getText(R.string.str_list3).toString());
    if(telMgr.getSimOperatorName().equals(""))
    {
      value.add("�޷�ȡ��");
    }
    else
    {
      value.add(telMgr.getSimOperatorName());
    }
    
    /* ȡ��SIM������ */
    item.add(getResources().getText(R.string.str_list4).toString());
    if(telMgr.getSimCountryIso().equals(""))
    {
      value.add("�޷�ȡ��");
    }
    else
    {
      value.add(telMgr.getSimCountryIso());
    }
    
    /* ʹ���Զ����MyAdapter�������ݴ���ListActivity */
    setListAdapter(new MyAdapter(this,item,value));
  } 
}

