package irdc.EX06_01;

/*��������BroadcastReceiver��*/
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.os.Bundle; 
/*��������telephoney.gsm.SmsMessage����ȡ����*/
import android.telephony.SmsMessage; 
/*��������Toast���������û��յ�����*/
import android.widget.Toast; 

/* �Զ���̳���BroadcastReceiver��,����ϵͳ����㲥����Ϣ */
public class EX06_01_SMSreceiver extends BroadcastReceiver 
{ 
   /*������̬�ַ���,��ʹ��android.provider.Telephony.SMS_RECEIVED��ΪActionΪ���ŵ�����*/
  private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED"; 
  
  @Override 
  public void onReceive(Context context, Intent intent) 
  { 
    // TODO Auto-generated method stub 
    /* �жϴ���Intent�Ƿ�Ϊ����*/
    if (intent.getAction().equals(mACTION)) 
    { 
      /*����һ�ַ��������ϱ���sb*/
      StringBuilder sb = new StringBuilder(); 
      /*������Intent����������*/
      Bundle bundle = intent.getExtras(); 
      /*�ж�Intent��������*/
      if (bundle != null) 
      { 
        /* pdusΪ android�ڽ����Ų��� identifier
         * ͸��bundle.get("")����һ������pdus�Ķ���*/
        Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
        /*�������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С*/
        SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
        for (int i = 0; i<myOBJpdus.length; i++) 
        {  
          messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]);  
        } 
          
        /* �������Ķ��źϲ��Զ�����Ϣ��StringBuilder���� */  
        for (SmsMessage currentMessage : messages) 
        {  
          sb.append("���յ�����:\n");  
          /* ��Ѷ�ߵĵ绰���� */ 
          sb.append(currentMessage.getDisplayOriginatingAddress());  
          sb.append("\n------�����Ķ���------\n");  
          /* ȡ�ô���ѶϢ��BODY */  
          sb.append(currentMessage.getDisplayMessageBody());  
        }  
      }    
      /* ��Notification(Toase)��ʾ������Ϣ  */
      Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show(); 
       
      /* ������Activity */ 
      Intent i = new Intent(context, EX06_01.class); 
      /*�趨�ü�Activity��һ���µ�task��ִ��*/
      i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
      context.startActivity(i); 
    } 
  } 
} 


