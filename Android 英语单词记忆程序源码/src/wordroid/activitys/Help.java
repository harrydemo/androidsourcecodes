package wordroid.activitys;

import wordroid.model.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class Help extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    this.setContentView(R.layout.showabout);
	    TextView tv = (TextView) this.findViewById(R.id.about_title);
	    tv.setText("°²×¿±³µ¥´Ê-Wordroid °ïÖú");
	    TextView words = (TextView) this.findViewById(R.id.about_words);
	    words.setText(R.string.words);
	    TextView abs = (TextView) this.findViewById(R.id.about_abstract);
	    abs.setText(R.string.abs);
	    TextView learn = (TextView) this.findViewById(R.id.about_learn);
	    learn.setText(R.string.learn);
	    TextView review = (TextView) this.findViewById(R.id.about_review);
	    review.setText(R.string.review);
	    TextView test = (TextView) this.findViewById(R.id.about_test);
	    test.setText(R.string.test);
	    TextView attetion = (TextView) this.findViewById(R.id.about_attention);
	    attetion.setText(R.string.attetion);  
	}

}
