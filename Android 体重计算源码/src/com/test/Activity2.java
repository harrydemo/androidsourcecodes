package com.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Activity2 extends Activity {
	
	public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    
	    //����activity2.xml
	    setContentView(R.layout.activity2);
	    
	    //��Intent�е�Bundle����
	    Bundle bunde = this.getIntent().getExtras();
	    
	    //ȡ��Bundle�����е����� 
	    String sex = bunde.getString("sex");
	    double height = bunde.getDouble("height");
	    
	    //�ж��Ա� 
	    String sexText="";
	    if(sex.equals("M"))
	    {
	      sexText="����";
	    }
	    else
	    {
	      sexText="Ů��";
	    }
	    
	    //ȡ�ñ�׼���� 
	    String weight=this.getWeight(sex, height);
	    
	    //�����������
	    TextView tv1=(TextView) findViewById(R.id.text1);
	    tv1.setText("����һλ"+sexText+"\n��������"
	                +height+"����\n��ı�׼������"+weight+"����");
	  }
	  
	  //��������
	  private String format(double num)
	  {
	    NumberFormat formatter = new DecimalFormat("0.00");
	    String s=formatter.format(num);
	    return s;
	  }

	  //��findViewById()ȡ��Button���󣬲����onClickListener 
	  private String getWeight(String sex,double height)
	  {
	    String weight="";
	    if(sex.equals("M"))
	    {
	      weight=format((height-80)*0.7);
	    }
	    else
	    {
	      weight=format((height-70)*0.6);
	    }  
	    return weight;
	  }

}
