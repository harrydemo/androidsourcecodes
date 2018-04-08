package cn.knight.barchart ;

import android.content.Context ;
import android.graphics.Canvas ;
import android.graphics.Color ;
import android.graphics.Paint ;
import android.view.View ;

public class ChartView extends View {

	private int[] data_screen ;
	private int[] data_power ;
	private int[] data_total ;
	private int flag ;
	private int margin ;

	private Chart chart ;
	private Paint paint ;

	public ChartView(Context context, int flag) {
		super(context) ;
		this.flag = flag ;
		margin = 0 ;
		chart = new Chart() ;
		data_screen = new int[] {90, 65, 80, 115 } ;
		data_power = new int[] {150, 125, 100, 130 } ;
		data_total = new int[4] ;
		for (int i = 0; i < 4; i++)
			data_total[i] = data_screen[i] + data_power[i] ;
		paint = new Paint() ;
		paint.setAntiAlias(true) ;
	}

	public void drawAxis(Canvas canvas) {
		paint.setColor(Color.YELLOW) ;
		paint.setStrokeWidth(2) ;
		canvas.drawLine(30, 300, 310, 300, paint) ;
		canvas.drawLine(30, 20, 30, 300, paint) ;

		int x = 90 ;
		int y = 250 ;

		for (int i = 0; i < 4; i++) {
			canvas.drawText(i + 1 + "", x, 320, paint) ;
			x += 60 ;
		}
		for (int i = 0; i < 5; i++) {
			canvas.drawText(50 * (i + 1) + "", 0, y, paint) ;
			y -= 50 ;
		}
	}

	public void drawChart(Canvas canvas) {
		if (flag == 1) {
			paint.setColor(Color.GREEN) ;
			int temp_screen = 30 ;
			for (int i = 0; i < 4; i++) {
				chart.setH(data_screen[i]) ;
				chart.setX(temp_screen + 20 * 2 + margin) ;
				chart.drawSelf(canvas, paint) ;
				margin = 20 ;
				temp_screen = chart.getX() ;
			}

			margin = 0 ;

			paint.setColor(Color.BLUE) ;
			int temp_power = 50 ;
			for (int i = 0; i < 4; i++) {
				chart.setH(data_power[i]) ;
				chart.setX(temp_power + 20 * 2 + margin) ;
				chart.drawSelf(canvas, paint) ;
				margin = 20 ;
				temp_power = chart.getX() ;
			}

			drawHighLines(canvas) ;
		} else if (flag == 2) {
			paint.setColor(Color.GREEN) ;
			int temp = 40 ;
			for (int i = 0; i < 4; i++) {
				chart.setH(data_total[i]) ;
				chart.setX(temp + 20 * 2 + margin) ;
				chart.drawSelf(canvas, paint) ;
				margin = 20 ;
				temp = chart.getX() ;
			}
		}
	}

	public void drawHighLines(Canvas canvas) {
		int[][] highPoints = new int[4][2] ;
		highPoints[0][0] = 90 ;
		highPoints[0][1] = data_total[0] ;
		highPoints[1][0] = 150 ;
		highPoints[1][1] = data_total[1] ;
		highPoints[2][0] = 210 ;
		highPoints[2][1] = data_total[2] ;
		highPoints[3][0] = 270 ;
		highPoints[3][1] = data_total[3] ;
		paint.setColor(Color.RED) ;
		for (int i = 0; i < 4; i++) {
			canvas.drawPoint(highPoints[i][0], 300 - highPoints[i][1], paint) ;
			canvas.drawText(data_total[i] + "", highPoints[i][0] - 10, 300 - highPoints[i][1] - 10,
					paint) ;
		}

		float[] pts = new float[16] ;
		for (int i = 0; i < 12; i++) {
			pts[0] = 32 ;
			pts[1] = 300 - highPoints[0][1] ;
			pts[2] = highPoints[0][0] ;
			pts[3] = 300 - highPoints[0][1] ;
			pts[4] = highPoints[0][0] ;
			pts[5] = 300 - highPoints[0][1] ;
			pts[6] = highPoints[1][0] ;
			pts[7] = 300 - highPoints[1][1] ;
			pts[8] = highPoints[1][0] ;
			pts[9] = 300 - highPoints[1][1] ;
			pts[10] = highPoints[2][0] ;
			pts[11] = 300 - highPoints[2][1] ;
			pts[12] = highPoints[2][0] ;
			pts[13] = 300 - highPoints[2][1] ;
			pts[14] = highPoints[3][0] ;
			pts[15] = 300 - highPoints[3][1] ;
		}
		canvas.drawLines(pts, paint) ;
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK) ;
		drawAxis(canvas) ;
		drawChart(canvas) ;
	}
}
