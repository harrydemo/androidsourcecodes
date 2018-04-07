package tjuci.dl.anim;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class AnimActivity extends Activity {
	EditText edit;
	Button btn;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        edit = (EditText)findViewById(R.id.edit);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Animation anim = AnimationUtils.loadAnimation(AnimActivity.this, R.anim.myanim);
				edit.startAnimation(anim);
			}
		});
    }
}