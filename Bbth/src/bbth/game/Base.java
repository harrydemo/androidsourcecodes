package bbth.game;

import java.util.HashSet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import bbth.engine.core.GameActivity;
import bbth.engine.ui.UIView;
import bbth.game.units.Unit;
import bbth.game.units.UnitType;

public class Base extends UIView {
	public static final float BASE_HEIGHT = 20;
	private Paint paint;
	private Team team;
	private Player player;
	public boolean drawFill = true;
	private HashSet<Unit> cachedUnits = new HashSet<Unit>();

	public Base(Player player) {
		this.setSize(BBTHSimulation.GAME_WIDTH, BASE_HEIGHT);

		this.player = player;
		team = player.getTeam();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextAlign(Align.CENTER);
	}

	public void draw(Canvas canvas, boolean serverDraw) {
		if (drawFill) {
			paint.setColor(team.getBaseColor());
			canvas.drawRect(_rect, paint);
		}
		paint.setColor(Color.WHITE);

		canvas.save();
		canvas.translate((_rect.left + _rect.right) / 2, _rect.top + 13);
		if (serverDraw) {
			canvas.scale(1.f, -1.f);
			canvas.translate(0, paint.getTextSize() / 2);
		}

		if (health != cachedHealth)
			regenHealthText();
		canvas.drawText(healthText, 0, 0, paint);
		canvas.restore();

	}

	@Override
	public RectF getRect() {
		return _rect;
	}

	public void damageUnits(GridAcceleration accel) {
		accel.getUnitsInAABB(_rect.left, _rect.top, _rect.right, _rect.bottom, cachedUnits);
		for (Unit u : cachedUnits) {
			if (u.getTeam() == team.getOppositeTeam() && _rect.contains(u.getX(), u.getY())) {
				if (!BBTHGame.DEBUG) {
					if (u.getType() == UnitType.UBER) {
						player.adjustHealth((-10*u.getHealth())/u.getStartingHealth());
					} else {
						player.adjustHealth((-5 * u.getHealth())/u.getStartingHealth());
					}
				}

				u.takeDamage(u.getHealth(), null);
			}
		}
	}
	
	String healthText;
	int cachedHealth = -1;
	int health = -1;
	protected void regenHealthText() {
		cachedHealth = health;
		healthText = player.isLocal() ? GameActivity.instance.getString(R.string.yourhealth, cachedHealth) : GameActivity.instance.getString(R.string.enemyhealth, cachedHealth);
	}

	public void setHealth(int health) {
		this.health = health;
	}
}
