package thuhe.thuhe.thuhe;

import android.graphics.Color;
import bbth.engine.ui.Anchor;
import bbth.engine.ui.UILabel;
import bbth.engine.ui.UISlider;
import bbth.engine.ui.UISwitch;
import bbth.engine.ui.UIView;
import bbth.game.BBTHGame;

public class UITestScreen extends UIView {
	public static final int AWESOME_YELLOW = Color.rgb(250, 177, 5);
	public static final int AWESOME_GREEN = Color.rgb(159, 228, 74);
	
	private UISlider sliderR, sliderG, sliderB;
	private UILabel label;
	private UISwitch uiSwitch;
	
	public UITestScreen() {
		this.setAnchor(Anchor.TOP_LEFT);
		this.setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
		this.setPosition(0, 0);
		
		sliderR = new UISlider(0.f, 255.f, 0.f);
		sliderR.setAnchor(Anchor.CENTER_CENTER);
		sliderR.setPosition(BBTHGame.WIDTH / 2, BBTHGame.HEIGHT / 2);
		sliderR.setSize(150, 20);
		this.addSubview(sliderR);
		
		sliderG = new UISlider(0.f, 255.f, 0.f);
		sliderG.setAnchor(Anchor.CENTER_CENTER);
		sliderG.setPosition(BBTHGame.WIDTH / 2, BBTHGame.HEIGHT / 2 + 80);
		sliderG.setSize(150, 20);
		this.addSubview(sliderG);

		sliderB = new UISlider(0.f, 255.f, 0.f);
		sliderB.setAnchor(Anchor.CENTER_CENTER);
		sliderB.setPosition(BBTHGame.WIDTH / 2, BBTHGame.HEIGHT / 2 + 160);
		sliderB.setSize(150, 20);
		this.addSubview(sliderB);

		label = new UILabel("", this);
		label.setAnchor(Anchor.CENTER_CENTER);
		label.setPosition(BBTHGame.WIDTH / 2, BBTHGame.HEIGHT / 2 - 80);
		label.setSize(100, 20);
		label.setTextSize(20.f);
		this.addSubview(label);
		
		uiSwitch = new UISwitch();
		uiSwitch.setAnchor(Anchor.CENTER_CENTER);
		uiSwitch.setPosition(BBTHGame.WIDTH / 2, BBTHGame.HEIGHT / 2 - 160);
		uiSwitch.setSize(100, 30);
		uiSwitch.setOnBackgroundColor(AWESOME_YELLOW);
		uiSwitch.setOnTextColor(Color.BLACK);
		this.addSubview(uiSwitch);
	}
	
	@Override
	public void onUpdate(float seconds) {
		super.onUpdate(seconds);

		int r = (int) sliderR.getValue();
		int g = (int) sliderG.getValue();
		int b = (int) sliderB.getValue();
		
		label.setText("Color(" + r + ", " + g + ", " + b + ")");
		this.uiSwitch.setOnBackgroundColor(Color.rgb(r, g, b));
	}
}
