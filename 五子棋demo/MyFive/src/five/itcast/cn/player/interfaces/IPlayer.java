package five.itcast.cn.player.interfaces;

import java.util.List;

import five.itcast.cn.IChessboard;
import five.itcast.cn.Point;

public interface IPlayer {
	//��һ�����ӣ���������Ѿ��µ����Ӽ���
	public void run(List<Point> enemyPoints, Point point);

	public boolean hasWin();
	
	public void setChessboard(IChessboard chessboard);
	
	public List<Point> getMyPoints();
}
