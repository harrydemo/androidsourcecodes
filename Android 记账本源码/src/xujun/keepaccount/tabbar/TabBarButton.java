package xujun.keepaccount.tabbar;


import xujun.keepaccount.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * @author 徐骏
 * @data   2010-11-1
 */
public class TabBarButton extends RadioButton
{
	private Context context;
	
	private Intent intent;
	private String label;
	
	public TabBarButton(Context context)
	{
		super(context);
		this.context = context;
	}
	public TabBarButton(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		this.context = context;
	}
	public void setState(int imageId,String label,Intent intent)
	{
		//On drawable
		ButtonStateDrawable offDrawable = new ButtonStateDrawable(context, imageId, label, false);
		//Off drawable
		ButtonStateDrawable onDrawable = new ButtonStateDrawable(context, imageId, label, true);
		setStateImageDrawables(onDrawable,offDrawable);
		this.intent = intent;
		this.label = label;
	}
	private void setStateImageDrawables(Drawable onDrawable, Drawable offDrawable)
	{
		StateListDrawable drawables = new StateListDrawable();
		
		int stateChecked = android.R.attr.state_checked;
		int stateFocused = android.R.attr.state_focused;
		int statePressed = android.R.attr.state_pressed;
		int stateWindowFocused = android.R.attr.state_window_focused;
		
		Resources resource = this.getResources();
		Drawable xDrawable = resource.getDrawable(R.drawable.bottom_bar_highlight);
		
		drawables.addState(new int[]{ stateChecked, -stateWindowFocused}, offDrawable);
		drawables.addState(new int[]{-stateChecked, -stateWindowFocused}, offDrawable);
		drawables.addState(new int[]{ stateChecked,  statePressed      }, onDrawable);
		drawables.addState(new int[]{-stateChecked,  statePressed      }, onDrawable);
		drawables.addState(new int[]{ stateChecked,  stateFocused      }, onDrawable);
		drawables.addState(new int[]{-stateChecked,  stateFocused      }, offDrawable);
		drawables.addState(new int[]{ stateChecked                     }, onDrawable);
		drawables.addState(new int[]{-stateChecked                     }, offDrawable);
		drawables.addState(new int[]{                  				   }, xDrawable);
		this.setButtonDrawable(drawables);
	}
	public Intent getIntent()
	{
		return intent;
	}
	public String getLabel()
	{
		return label;
	}
}
