package wyf.ytl;
/**
 * 该类为棋子的一个走法
 * 包含是什么棋子
 * 起始点的位置
 * 目标点的位置
 * 以及估值时所用到的score
 */
public class ChessMove {
	int ChessID;//表明是什么棋子
	int fromX;//起始的坐标
	int fromY;
	int toX;//目的地的坐标
	int toY;
	int score;//值,估值时会用到
	public ChessMove(int ChessID, int fromX,int fromY,int toX,int toY,int score){//构造器
		this.ChessID = ChessID;//棋子的类型
		this.fromX = fromX;//棋子的起始坐标
		this.fromY = fromY;
		this.toX = toX;//棋子的目标点x坐标
		this.toY = toY;//棋子的目标点y坐标
		this.score = score;
	}
}