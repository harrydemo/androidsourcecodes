package cn.knight.barchart ;

import android.app.Activity ;
import android.graphics.Color ;
import android.os.Bundle ;
import android.view.KeyEvent ;
import android.view.View ;
import android.view.View.OnClickListener ;
import android.view.Window ;
import android.widget.Button ;
import android.widget.LinearLayout ;
import android.widget.TextView ;

public class Main extends Activity implements OnClickListener {

	private static final int SINGLECHART = 1 ;
	private static final int TOTALCHART = 2 ;

	private LinearLayout layout_chart ;
	private Button btn_single ;
	private Button btn_total ;
	private TextView txv_screen ;
	private TextView txv_power ;

	private ChartView chartView ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState) ;
		requestWindowFeature(Window.FEATURE_NO_TITLE) ;
		setContentView(R.layout.main) ;

		layout_chart = (LinearLayout) findViewById(R.id.chartArea) ;
		btn_single = (Button) findViewById(R.id.singleChart) ;
		btn_total = (Button) findViewById(R.id.totalChart) ;
		txv_screen = (TextView) findViewById(R.id.screenDesc) ;
		txv_power = (TextView) findViewById(R.id.powerDesc) ;

		btn_single.setOnClickListener(this) ;
		btn_total.setOnClickListener(this) ;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId() ;
		int which = 0 ;

		if (id == R.id.singleChart) {
			which = SINGLECHART ;
			txv_power.setVisibility(View.VISIBLE) ;
			txv_screen.setText("绿色: 显示器功耗") ;
			txv_power.setText("蓝色: 电源功耗") ;
		} else if (id == R.id.totalChart) {
			which = TOTALCHART ;
			txv_power.setVisibility(View.INVISIBLE) ;
			txv_screen.setText("绿色: 总功耗") ;
		}

		txv_screen.setVisibility(View.VISIBLE) ;
		txv_screen.setTextColor(Color.GREEN) ;
		txv_power.setTextColor(Color.BLUE) ;

		chartView = new ChartView(this, which) ;
		layout_chart.removeAllViews() ;
		layout_chart.addView(chartView) ;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK)
			System.exit(0) ;
		return true ;
	}

}
