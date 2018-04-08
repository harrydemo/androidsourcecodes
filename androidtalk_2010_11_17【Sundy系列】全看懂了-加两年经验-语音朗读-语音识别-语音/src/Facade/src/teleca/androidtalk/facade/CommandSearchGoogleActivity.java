/**
 * @description : Activity of identify SearchGoogle command.
 * @version 1.0
 * @author Alex
 * @date 2010-11-10
 */
package teleca.androidtalk.facade;

import teleca.androidtalk.facade.util.Helper;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CommandSearchGoogleActivity extends Activity implements OnClickListener
{
	private Button btn_save,btn_cancel;
	private EditText edit1,edit2,edit3;
	private String cmd_name,cmd_category,cmd_relation;
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		setContentView(R.layout.addcommand);
		Drawable dw3 = this.getResources().getDrawable(R.drawable.btn_image);
		
		btn_save = (Button)findViewById(R.id.btn_add);
		btn_save.setBackgroundDrawable(dw3);
		btn_save.setOnClickListener(this);
		
		btn_cancel = (Button)findViewById(R.id.btn_cancel);
		btn_cancel.setBackgroundDrawable(dw3);
		btn_cancel.setOnClickListener(this);
		
		edit1 = (EditText)findViewById(R.id.edit1);
		edit2 = (EditText)findViewById(R.id.edit2);
		edit2.setText("search Google");
		//set edit2 can't be edited
		edit2.setFilters(new InputFilter[] { new InputFilter() 
		{
			@Override
			public CharSequence filter(CharSequence arg0, int arg1, int arg2,
					Spanned arg3, int arg4, int arg5) 
			{
				// TODO Auto-generated method stub
				return arg0.length() < 1 ? arg3.subSequence(arg4, arg5) : "";
			}
			} });
		edit3 = (EditText)findViewById(R.id.edit3);		
	}
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if(v == btn_save)
		{
			cmd_name = edit1.getText().toString();
			cmd_category = edit2.getText().toString();
			cmd_relation = edit3.getText().toString();
			boolean flag = Helper.isNameExist(cmd_name,CommandSearchGoogleActivity.this);
			//save data  to SQLite when the cmdName and cmdCategory isn't null and cmdName 
			//hasn't used yet.
			if (!(cmd_name.equals(""))&& !(cmd_relation.equals(""))) 
			{
				if (!flag) 
				{
					Helper.addCommand(CommandSearchGoogleActivity.this, cmd_name, cmd_category, cmd_relation);
					Intent i = new Intent();
					this.setResult(RESULT_OK, i);
				} 
				else if (flag)
				{
				Toast.makeText(	this,"CommandName has already existed,please reset a new one.",
					Toast.LENGTH_SHORT).show();
				return;
				}
		   }
		    else 
		    {
				Toast.makeText(this,"CommandName and Relation can't be empty.",
						Toast.LENGTH_SHORT).show();
				return;
			}
			finish();
		}
		if(v == btn_cancel)
		{
			finish();
		}
	}
}
