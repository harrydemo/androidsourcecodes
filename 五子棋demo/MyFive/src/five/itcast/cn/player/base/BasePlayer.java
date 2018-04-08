package five.itcast.cn.player.base;

import java.util.ArrayList;
import java.util.List;

import five.itcast.cn.IChessboard;
import five.itcast.cn.Point;
import five.itcast.cn.player.interfaces.IPlayer;

public abstract class BasePlayer implements IPlayer {
	//我已下的棋子
	protected List<Point> myPoints = new ArrayList<Point>(200);
	//棋盘
	protected IChessboard chessboard;
	//棋盘最大横坐标和纵标，
	protected int maxX;
	protected int maxY;
	
	//所有空白棋子
	protected List<Point> allFreePoints;

	@Override
	public final List<Point> getMyPoints() {
		return myPoints;
	}

	@Override
	public void setChessboard(IChessboard chessboard) {
		this.chessboard = chessboard;
		allFreePoints = chessboard.getFreePoints();
		maxX = chessboard.getMaxX();
		maxY = chessboard.getMaxY();
		myPoints.clear();
	}
	
	private final Point temp = new Point(0, 0);
	//我是否是否赢了
	public final boolean hasWin(){
		if(myPoints.size()<5){
			return false;
		}
		Point point = myPoints.get(myPoints.size()-1);
		int count = 1;
		int x=point.getX(),y=point.getY();
		//横向
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setX(temp.getX()-1)) && temp.getX()>=0 && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setX(temp.getX()+1)) && temp.getX()<maxX && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		//纵向
		count = 1;
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setY(temp.getY()-1)) && temp.getY()>=0) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setY(temp.getY()+1)) && temp.getY()<maxY && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		//正斜向 /
		count =1;
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setX(temp.getX()-1).setY(temp.getY()+1)) && temp.getX()>=0 && temp.getY()<maxY) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setX(temp.getX()+1).setY(temp.getY()-1)) && temp.getX()<maxX && temp.getY()>=0 && count<6) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		//反斜 \
		count = 1;
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setX(temp.getX()-1).setY(temp.getY()-1)) && temp.getX()>=0 && temp.getY()>=0) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		temp.setX(x).setY(y);
		while (myPoints.contains(temp.setX(temp.getX()+1).setY(temp.getY()+1)) && temp.getX()<maxX && temp.getY()<maxY && count<5) {
			count ++;
		}
		if(count>=5){
			return true;
		}
		return false;
	}
}
