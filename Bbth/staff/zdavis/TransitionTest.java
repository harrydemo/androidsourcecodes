package zdavis;

import android.graphics.Canvas;
import android.graphics.Color;
import bbth.engine.ui.DefaultTransition;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UINavigationController;
import bbth.engine.ui.UIView;
import bbth.engine.util.Envelope;
import bbth.game.BBTHGame;

public class TransitionTest extends UINavigationController {
	UIView view1 = new UIView("view1") {
		{
			setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
		}
		@Override
		public void onDraw(Canvas canvas) {
			canvas.drawRGB(62, 107, 172);
			super.onDraw(canvas);
		}
	};
	UIView view2 = new UIView("view2") {
		{
			setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
		}
		@Override
		public void onDraw(Canvas canvas) {
			canvas.drawRGB(255, 255, 255);
			super.onDraw(canvas);
		}
	};
	
	DefaultTransition oneToTwo;
	DefaultTransition twoToOne;
	boolean oneInFront = true;
	
	public TransitionTest() {
		//super("transitiontest");
		
		UILabel label1 = new UILabel("View One", "l1");
		label1.setTextColor(Color.WHITE);
		view1.addSubview(label1);
		
		UILabel label2 = new UILabel("View Two", "l2");
		label2.setTextColor(Color.BLACK);
		view2.addSubview(label2);
		
		push(view1);
		
		oneToTwo = new DefaultTransition(2f);
		Envelope alpha = new Envelope(0f, Envelope.OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
		alpha.addFlatSegment(1f, 0.0);
		alpha.addLinearSegment(1f, 255.0);
		oneToTwo.setNewAlpha(alpha);
		Envelope scale = new Envelope(1f, Envelope.OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
		scale.addLinearSegment(1f, 0.0);
		oneToTwo.setOldScale(scale);
		
		twoToOne = new DefaultTransition(2f);
		Envelope x = new Envelope(BBTHGame.WIDTH, Envelope.OutOfBoundsHandler.RETURN_FIRST_OR_LAST);
		x.addLinearSegment(2f, 0.0);
		twoToOne.setNewX(x);
	}

	@Override
	public void onTouchDown(float x, float y) {
		if (oneInFront) {
System.err.println("About to push");
			push(view2, oneToTwo);
			oneInFront = false;
		} else {
System.err.println("About to pop");
			pop(twoToOne);
			oneInFront = true;
		}
	}

}
