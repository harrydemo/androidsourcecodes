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
	    
	    //加载activity2.xml
	    setContentView(R.layout.activity2);
	    
	    //得Intent中的Bundle对象
	    Bundle bunde = this.getIntent().getExtras();
	    
	    //取得Bundle对象中的数据 
	    String sex = bunde.getString("sex");
	    double height = bunde.getDouble("height");
	    
	    //判断性别 
	    String sexText="";
	    if(sex.equals("M"))
	    {
	      sexText="男性";
	    }
	    else
	    {
	      sexText="女性";
	    }
	    
	    //取得标准体重 
	    String weight=this.getWeight(sex, height);
	    
	    //设置输出文字
	    TextView tv1=(TextView) findViewById(R.id.text1);
	    tv1.setText("你是一位"+sexText+"\n你的身高是"
	                +height+"厘米\n你的标准体重是"+weight+"公斤");
	  }
	  
	  //四舍五入
	  private String format(double num)
	  {
	    NumberFormat formatter = new DecimalFormat("0.00");
	    String s=formatter.format(num);
	    return s;
	  }

	  //以findViewById()取得Button对象，并添加onClickListener 
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
