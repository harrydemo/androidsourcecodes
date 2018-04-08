package irdc.ex03_10;

/* import���class */
import java.text.DecimalFormat;
import java.text.NumberFormat;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class EX03_10_1 extends Activity 
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    /* ����main.xml Layout */
    setContentView(R.layout.myalyout);
    
    /* ȡ��Intent�е�Bundle���� */
    Bundle bunde = this.getIntent().getExtras();
    
    /* ȡ��Bundle�����е����� */
    String sex = bunde.getString("sex");
    double height = bunde.getDouble("height");
    
    /*�ж��Ա� */
    String sexText="";
    if(sex.equals("M")){
      sexText="����";
    }else{
      sexText="Ů��";
    }
    
    /* ȡ�ñ�׼���� */
    String weight=this.getWeight(sex, height);
    
    /* �趨������� */
    TextView tv1=(TextView) findViewById(R.id.text1);
    tv1.setText("����һλ"+sexText+"\n��������"+height+
                "����\n��ı�׼������"+weight+"����");
  }
  
  /* ���������method */
  private String format(double num)
  {
    NumberFormat formatter = new DecimalFormat("0.00");
  String s=formatter.format(num);
  return s;
  }

  /* ��findViewById()ȡ��Button����onClickListener */  
  private String getWeight(String sex,double height)
  {
    String weight="";
  if(sex.equals("M"))
  {
    weight=format((height-80)*0.7);
    }else
  {
    weight=format((height-70)*0.6);
  } 
  return weight;
  }
}


