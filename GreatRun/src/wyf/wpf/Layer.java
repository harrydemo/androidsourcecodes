package wyf.wpf;			//声明包语句
import static wyf.wpf.ConstantUtil.*;	//引入ConstantUtil类的静态常量
/*
 * 每一个Layer对象代表一个图层，该类中包含图层的地图矩阵
 * 和该图层的不可通过矩阵
 */
public class Layer{
	MyDrawable [][] mapMatrix;		//绘制矩阵
	int [][] notInMarix;	//通过性矩阵
	//构造器
	public Layer(int[][] mapMatrix,int[][] noThroughMatrix){
		this.mapMatrix = getMyDrawableMatrix(mapMatrix);
		this.notInMarix = noThroughMatrix;
	}
	//方法：根据地图数据创建一个MyDrawable矩阵
	public static MyDrawable [][] getMyDrawableMatrix(int [][] mapMatrix){
		MyDrawable [][] result = new MyDrawable[MAP_ROWS][MAP_COLS];
		for(int i=0;i<MAP_ROWS;i++){
			for(int j=0;j<MAP_COLS;j++){
				switch(mapMatrix[i][j]){		//判断该位置的图元编号
				case 1:		//路
					result[i][j] = new MyDrawable(0, TILE_SIZE);
					break;
				case 2:		//草地     
					result[i][j] = new MyDrawable(1, TILE_SIZE);
					break;
				case 3:		//木桩
					result[i][j] = new MyDrawable(2, TILE_SIZE);
					break;
				case 4:		//树
					result[i][j] = new MyDrawable(3, TILE_SIZE*2);
					break;
				case 5:		//花
					result[i][j] = new MyDrawable(4, TILE_SIZE);
					break;
				}
			}
		}		
		return result;
	}
}