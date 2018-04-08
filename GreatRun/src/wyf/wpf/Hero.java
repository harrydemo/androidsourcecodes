package wyf.wpf;			//声明包语句
public class Hero extends Sprite{
	public Hero(int col, int row){
		super(col, row);
	}
	//方法：判断是否到家,接收家的行列值
	public boolean foundHome(int col,int row){
		int hrow = this.row;
		int hcol = this.col;
		if(hrow == row && hcol == col){		//判断英雄的中心点所在的格子是否同家所在的格子一样	
			return true;
		}
		return false;
	}	
}