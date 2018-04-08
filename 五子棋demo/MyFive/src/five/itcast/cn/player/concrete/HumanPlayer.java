package five.itcast.cn.player.concrete;

import java.util.List;

import five.itcast.cn.Point;
import five.itcast.cn.player.base.BasePlayer;
import five.itcast.cn.player.interfaces.IPlayer;


public class HumanPlayer extends BasePlayer implements IPlayer{

	@Override
	public void run(List<Point> enemyPoints,Point p) {
		getMyPoints().add(p);
		allFreePoints.remove(p);
	}
}
