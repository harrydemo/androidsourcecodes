package five.itcast.cn.player.interfaces;

import java.util.List;

import five.itcast.cn.IChessboard;
import five.itcast.cn.Point;

public interface IPlayer {
	//下一步棋子，传入对手已经下的棋子集合
	public void run(List<Point> enemyPoints, Point point);

	public boolean hasWin();
	
	public void setChessboard(IChessboard chessboard);
	
	public List<Point> getMyPoints();
}
