/**
 * 
 */
package wordroid.activitys;

import java.util.ArrayList;

import wordroid.database.DataAccess;
import wordroid.model.R;
import wordroid.model.Word;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Administrator
 *
 */
public class EditWord extends Activity implements OnClickListener{
	private String action;
	private EditText spelling;
	private EditText meanning;
	private Button confirm;
	private Button cancel;
	private Word word;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("编辑生词本");
		this.setContentView(R.layout.editword);
		Bundle bundle = this.getIntent().getExtras();
		action = bundle.getString("action");
		initWidgets();
	}

	private void initWidgets() {
		// TODO Auto-generated method stub
		this.spelling = (EditText) this.findViewById(R.id.EditWord_spelling);
		this.cancel = (Button) this.findViewById(R.id.attention_cancel_button);
		this.confirm = (Button) this.findViewById(R.id.attention_confirm_button);
		this.meanning = (EditText) this.findViewById(R.id.EditWord_meanning);
		cancel.setOnClickListener(this);
		confirm.setOnClickListener(this);
		if (action.equals("edit")){
			Bundle bundle = this.getIntent().getExtras();
			DataAccess data = new DataAccess(this);
			Word word =data.QueryAttention("ID ='"+bundle.getString("id")+"'", null).get(0);
			spelling.setText(word.getSpelling());
			meanning.setText(word.getMeanning());
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v==cancel){
			finish();
		}
		if (v==confirm){
			if (meanning.getText().toString().equals("")||spelling.getText().toString().equals("")){
				Toast.makeText(EditWord.this, "信息不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
			if (action.equals("add")){
				boolean add =true;
				final DataAccess data = new DataAccess(this);
				ArrayList<Word> words = data.QueryAttention(null, null);
				
			for(int i=0;i<words.size();i++){
					if(this.spelling.getText().toString().equals(words.get(i).getSpelling())){
						add=false;
						Log.i("thesame", ""+i);
						word = words.get(i);
						}
			}
					if (!add){
						Dialog dialog = new AlertDialog.Builder(this)
			            .setIcon(R.drawable.dialog_icon)
			            .setTitle("该单词已存在")
			            .setMessage("生词本中有这个单词，要覆盖掉它吗？")
			            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                    word.setSpelling(spelling.getText().toString());
			                    word.setMeanning(meanning.getText().toString());
			                    data.UpdateAttention(word);
			                    finish();
			                }
			            })
			            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int whichButton) {
			                   }
			            }).create();
						dialog.show();
					}
						
				if(add){
				Word word1 = new Word();
				word1.setMeanning(this.meanning.getText().toString());
				word1.setSpelling(this.spelling.getText().toString());
				word1.setPhonetic_alphabet(" ");
				data.InsertIntoAttention(word1);
				finish();
				}
				
			}
			else if (action.equals("edit")){
				Bundle bundle = new Bundle();
				bundle = this.getIntent().getExtras();
				DataAccess data = new DataAccess(EditWord.this);
			    Word word = data.QueryAttention("ID = '"+bundle.getString("id")+"'", null).get(0);
			    word.setMeanning(this.meanning.getText().toString());
				word.setSpelling(this.spelling.getText().toString());
				data.UpdateAttention(word);
				finish();
				
			}
		}
	}

}
