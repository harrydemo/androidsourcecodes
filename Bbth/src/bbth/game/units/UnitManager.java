package bbth.game.units;

import bbth.engine.fastgraph.Wall;
import bbth.engine.util.Bag;

public interface UnitManager {
	void notifyUnitDead(Unit unit);

	Bag<Unit> getUnitsInCircle(float x, float y, float r);

	Bag<Unit> getUnitsIntersectingLine(float x, float y, float x2, float y2);

	void removeWall(Wall wall);
}
