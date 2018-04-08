package com.example.loan2;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class MainActivity extends Activity {
	String str="";
	View vi;
	EditText et;
	private RadioButton interest, principal;
	private EditText moneyText, yearText, rateText;
	private Button cal;
	DecimalFormat df = new DecimalFormat(".##");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        principal = (RadioButton) findViewById(R.id.rb_SameCapital);
        interest = (RadioButton) findViewById(R.id.rb_SameInterest); 
        moneyText = (EditText) findViewById(R.id.et_LoanAmount);

        yearText = (EditText) findViewById(R.id.et_Years);

        rateText = (EditText) findViewById(R.id.et_LoanRate);

        cal = (Button) findViewById(R.id.bt_Cal);
    
    et=(EditText) findViewById(R.id.et_LoanAmount);
    
    et.setText(str);
    
    cal.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			/*str="1111wpwpqy";
			et.setText(str);
			vi=v;*/
			
		    if(interest.isChecked())

                showInterestResult();

        else

                showPrincipalResult();


		}
			
	});
    }
    void showInterestResult() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(moneyText.getText().toString().equals("")){

                builder.setTitle("提示");

                builder.setMessage("请输入贷款总额！");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int id) {

                 dialog.cancel();

         }

     });

                AlertDialog alert = builder.create();

                alert.show();

                

        }else if(!(moneyText.getText().toString().equals(""))&&(yearText.getText().toString().equals(""))){

                builder.setTitle("提示");

                builder.setMessage("请输入贷款年限！");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int id) {

                 dialog.cancel();

         }

     });

                AlertDialog alert = builder.create();

                alert.show();

        }else if(!(moneyText.getText().toString().equals(""))&&!(yearText.getText().toString().equals(""))&&rateText.getText().toString().equals("")){

                builder.setTitle("提示");

                builder.setMessage("请输入年利率！");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int id) {

                 dialog.cancel();

         }

     });

                AlertDialog alert = builder.create();

                alert.show();

        }else{

                

        double money = Double.parseDouble(moneyText.getText().toString());

        double year = Double.parseDouble(yearText.getText().toString());

        double rate = Double.parseDouble(rateText.getText().toString()) / 100;

        double monthRate = rate / 12;

        double temp = 1;

        for (int i = 0; i < year * 12; i++) {

                temp *= (1 + monthRate);

        }

        double monthPayment = (money * monthRate * temp) / (temp - 1);

        double toatalInterest = monthPayment * year * 12 - money;

        double totalMoney = monthPayment * year * 12;



        

        String monthpayment = df.format(monthPayment);

        String toatalinterest = df.format(toatalInterest);

        String toatalmoney = df.format(totalMoney);



        final CharSequence[] items = { "首月还款 : " + monthpayment,

                        "利息总额 : " + toatalinterest, "累计本息 : " + toatalmoney };

        builder.setTitle("计算结果");

        builder.setItems(items, new DialogInterface.OnClickListener() {

                @Override

                public void onClick(DialogInterface dialog, int which) {

                }

        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                }

        });

        AlertDialog alert = builder.create();

        alert.show();

}

                

}



void showPrincipalResult() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(moneyText.getText().toString().equals("")){

                builder.setTitle("提示");

                builder.setMessage("请输入贷款总额！");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int id) {

                 dialog.cancel();

         }

     });

                AlertDialog alert = builder.create();

                alert.show();

                

        }else if(!(moneyText.getText().toString().equals(""))&&(yearText.getText().toString().equals(""))){

                builder.setTitle("提示");

                builder.setMessage("请输入贷款年限！");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int id) {

                 dialog.cancel();

         }

     });

                AlertDialog alert = builder.create();

                alert.show();

        }else if(!(moneyText.getText().toString().equals(""))&&!(yearText.getText().toString().equals(""))&&rateText.getText().toString().equals("")){

                builder.setTitle("提示");

                builder.setMessage("请输入年利率！");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

         public void onClick(DialogInterface dialog, int id) {

                 dialog.cancel();

         }

     });

                AlertDialog alert = builder.create();

                alert.show();

        }else{

                double money = Double.parseDouble(moneyText.getText().toString());

                double year = Double.parseDouble(yearText.getText().toString());

                double rate = Double.parseDouble(rateText.getText().toString()) / 100;

                double monthRate = rate / 12;



                double monthPayment = (money / (year * 12)) + (money - 0) * monthRate;

                double firstMonthInterest = money * monthRate;

                double temp = (monthPayment - firstMonthInterest) * monthRate;

                double totalInterest = firstMonthInterest + year * 12 * (year * 12 - 1)

                                * temp / 2;

                double totalMoney = totalInterest + money;



                String monthpayment = df.format(monthPayment);

                String toatalinterest = df.format(totalInterest);

                String toatalmoney = df.format(totalMoney);



                final CharSequence[] items = { "首月还款 : " + monthpayment,

                                "利息总额 : " + toatalinterest, "累计本息 : " + toatalmoney };

                builder.setTitle("计算结果");

                builder.setItems(items, new DialogInterface.OnClickListener() {

                        @Override

                        public void onClick(DialogInterface dialog, int which) {

                        }

                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                        }

                });

                AlertDialog alert = builder.create();

                alert.show();

        }

}
/************/
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
