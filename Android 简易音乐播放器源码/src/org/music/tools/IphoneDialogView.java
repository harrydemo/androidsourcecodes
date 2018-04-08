package org.music.tools;

import org.app.music.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IphoneDialogView extends RelativeLayout {
	private TextView itemMessenge, title;
	private Button yes, no;

	public IphoneDialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
	
	}

	public IphoneDialogView(Context context) {
		super(context);
	
	}

	@Override
	protected void onFinishInflate() {
		title=(TextView) findViewById(R.id.dialog_title);
		itemMessenge=(TextView) findViewById(R.id.dialog_message);
		yes=(Button) findViewById(R.id.dialog_yes);
		no=(Button) findViewById(R.id.dialog_no);
		super.onFinishInflate();
	}

	public void setTitle(int TitleId){
		this.title.setText(TitleId);
	}
	public void setTitle(CharSequence title) {
		this.title.setText(title);
	}
	

	public void setItemmessenge(int MessageId) {
		this.itemMessenge.setText(MessageId);
	}

	
	public void HiddenButton(int ButtonId) {
		if (ButtonId==R.id.dialog_yes) {
			this.yes.setVisibility(GONE);
		}else if (ButtonId==R.id.dialog_no) {
			this.no.setVisibility(GONE);
		}
	}

	public void setMessage(CharSequence message) {
		this.itemMessenge.setText(message);
		
	}
}
