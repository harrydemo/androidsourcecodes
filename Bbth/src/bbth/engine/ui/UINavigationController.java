package bbth.engine.ui;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import android.graphics.Canvas;

public class UINavigationController extends UIView {
	Transition instantTransition = new Transition() {
		@Override
		public boolean isDone() {
			return true;
		}

		@Override
		public void setTime(float transitionTime) {
		}

		@Override
		public void draw(Canvas canvas, UIView oldView, UIView newView) {
			newView.onDraw(canvas);
		}
	};

	private LinkedList<UIView> screens;
	private UIView currentView;

	private UIView newView;
	private Transition transition;
	private UISwipeTransition _transition;
	private boolean transitioning;
	private float transitionTime;

	private UINavigationEventListener navListener;

	public UINavigationController() {
		screens = new LinkedList<UIView>();
		navListener = null;
	}

	public void setNavListener(UINavigationEventListener newlistener) {
		navListener = newlistener;
	}

	@Override
	public void onUpdate(float seconds) {
		// if (currentView != null)
		// currentView.onUpdate(seconds);
		if (transitioning) {
			// if (newView != null)
			// newView.onUpdate(seconds);
			if (transitionTime < 0f)
				transitionTime = 0f;
			else
				transitionTime += seconds;
			if (transition != null)
				transition.setTime(transitionTime);
			if (_transition != null)
				_transition.onUpdate(seconds);
			if ((transition != null && transition.isDone())
					|| (_transition != null && _transition.isDone())) {
				transitioning = false;
				// Notify our navListener, if any.
				if (navListener != null) {
					navListener.onScreenHidden(currentView);
					navListener.onScreenShown(newView);
				}
				currentView = newView;
				transitionTime = -1f;

				// let gc do its work
				transition = null;
				newView = null;
			}
		} else {
			currentView.onUpdate(seconds);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (transitioning) {
			if (transition != null)
				transition.draw(canvas, currentView, newView);
			else if (_transition != null)
				_transition.onDraw(canvas, currentView, newView);
		} else if (currentView != null)
			currentView.onDraw(canvas);
	}

	@Override
	public boolean containsPoint(float x, float y) {
		return (currentView != null && !transitioning && currentView
				.containsPoint(x, y));
	}

	@Override
	public void onTouchDown(float x, float y) {
		if (currentView != null && !transitioning
				&& currentView.containsPoint(x, y))
			currentView.onTouchDown(x, y);
	}

	@Override
	public void onTouchUp(float x, float y) {
		if (currentView != null && !transitioning
				&& currentView.containsPoint(x, y))
			currentView.onTouchUp(x, y);
	}

	@Override
	public void onTouchMove(float x, float y) {
		if (currentView != null && !transitioning
				&& currentView.containsPoint(x, y))
			currentView.onTouchMove(x, y);

	}

	@Override
	public void setBounds(float left, float top, float right, float bottom) {
		for (UIView screen : screens)
			screen.setBounds(left, top, right, bottom);
	}

	private void startTransition(Transition transition, UIView newView) {
		this.transition = transition;
		this.newView = newView;
		transitioning = true;
	}

	private void startTransition(UISwipeTransition transition, UIView newView) {
		this._transition = transition;
		this._transition.reset();
		this.newView = newView;
		transitioning = true;
	}

	// returns true on success, false on failure
	public boolean pop() {
		if (screens.size() <= 1) {
			return false;
		}
		pop(instantTransition);
		return true;
	}

	public boolean pop(Transition transition) {
		if (screens.size() <= 1) {
			return false;
		}
		try {
			if (currentView != null)
				currentView.willHide(true);
			screens.removeFirst(); // remove current view
			UIView newView = screens.getFirst();
			newView.willAppear(true);
			startTransition(transition, newView);
		} catch (NoSuchElementException e) {
			currentView = null;
		}
		return true;
	}

	public boolean pop(UISwipeTransition transition) {
		if (screens.size() <= 1) {
			return false;
		}
		try {
			if (currentView != null)
				currentView.willHide(true);
			screens.removeFirst(); // remove current view
			UIView newView = screens.getFirst();
			newView.willAppear(true);
			startTransition(transition, newView);
		} catch (NoSuchElementException e) {
			currentView = null;
		}
		return true;
	}

	public void push(UIView screen) {
		push(screen, instantTransition);
	}

	public void push(UIView screen, Transition transition) {
		if (currentView != null)
			currentView.willHide(true);

		screens.addFirst(screen);
		screen.willAppear(true);
		startTransition(transition, screen);
	}

	public void push(UIView screen, UISwipeTransition transition) {
		if (currentView != null)
			currentView.willHide(true);
		screens.addFirst(screen);
		screen.willAppear(true);
		startTransition(transition, screen);
	}

	public void pushUnder(UIView screen) {
		screens.add(1, screen);
	}

	public void pushBack(UIView screen) {
		screens.addLast(screen);
	}

	public void clear() {
		if (currentView != null)
			currentView.willHide(true);

		screens.clear();
		currentView = null;
	}

	@Override
	public void onStop() {
		super.onStop();
		if (currentView != null)
			currentView.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode) {
		int idx = screens.size();
		while (idx-- > 0) {
			screens.get(idx).onActivityResult(requestCode, resultCode);
		}
	}
}
