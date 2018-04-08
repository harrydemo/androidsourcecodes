package kvgishen;

import bbth.engine.ui.Anchor;
import bbth.engine.ui.UIButton;
import bbth.engine.ui.UIButtonDelegate;
import bbth.engine.ui.UIProgressBar;
import bbth.engine.ui.UIRadioButton;
import bbth.engine.ui.UIScrollView;
import bbth.game.BBTHGame;

public class TitleScreen extends UIScrollView implements UIButtonDelegate {
	private UIButton greeting, scrollUp;
	private UIProgressBar progressBar;
	
	private float elapsedTime;
	
	public TitleScreen(Object tag)
	{
		super(tag);
		setSize(BBTHGame.WIDTH, BBTHGame.HEIGHT);
		setPosition(0, 0);
		
		greeting = new UIButton("Scroll To Bottom", null);
		greeting.setAnchor(Anchor.CENTER_CENTER);
		greeting.setSize(160, 20);
		greeting.setPosition(160.f, 90.f);
		greeting.delegate = this;
		addSubview(greeting);
		
		// testing progress bar
//		progressBar = new UIProgressBar(tag);
//		progressBar.setAnchor(Anchor.CENTER_CENTER);
//		progressBar.setSize(160, 20);
//		progressBar.setPosition(160, 1020);
//		progressBar.setBorderRadius(5);
//		progressBar.setProgress(1.f);
//		progressBar.setMode(Mode.INFINTE);
//		addSubview(progressBar);
		
		scrollUp = new UIButton("Scroll To Top", null);
		scrollUp.setAnchor(Anchor.CENTER_CENTER);
		scrollUp.setSize(160, 20);
		scrollUp.setPosition(160.f, 220.f);
		scrollUp.delegate = this;
		addSubview(scrollUp);
		
		UIRadioButton testButton = new UIRadioButton("Hello", null);
		testButton.setAnchor(Anchor.CENTER_CENTER);
		testButton.setPosition(160, 120);
		addSubview(testButton);
	}
	
	@Override
	public void onUpdate(float seconds) {
		super.onUpdate(seconds);

		this.elapsedTime += seconds;
//		progressBar.setProgress(elapsedTime / 10);
//		
//		if (this.progressBar.getProgress() == 1.f) {
//			this.progressBar.setMode(Mode.INFINTE);
//		}
	}

	@Override
	public void onClick(UIButton button) {
		if(button == greeting)
			scrollTo(0, 1020);
		else if(button == scrollUp)
			scrollTo(0,0);
		
	}
}
