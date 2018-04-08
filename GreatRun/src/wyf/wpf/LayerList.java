package wyf.wpf;			//声明包语句

import java.util.ArrayList;		//引入相关类
import static wyf.wpf.ConstantUtil.*;	//引入相关类

/*
 * 该类负责管理游戏的所有平铺层，将其封装到成员变量中 
 * 同时提供方法来获得游戏的不可通过矩阵，供碰撞检测
 */
public class LayerList{
	public ArrayList<Layer> layerList = new ArrayList<Layer>();		//存放各种平铺层

	public static LayerList getLayerListByStage(int stage){//静态方法：根据不同的关卡返回不同的LayerLsit对象
		LayerList list= new LayerList();
		//从GameData类中读取图层信息，封装成Layer对象并添加到平铺层列表中
		for(int i=0;i<GameData.mapData[stage].length;i++){
			Layer layer = new Layer(GameData.mapData[stage][i],GameData.notInData[stage][i]);
			list.layerList.add(layer);
		}		
		return list;
	}
	//方法，返回综合了所有平铺层的不可通过矩阵
	public int[][] getTotalNotInMatrix(){
		int[][] result = new int[MAP_ROWS][MAP_COLS];		//创建数组用于返回
		for(int i=0;i<result.length;i++){			//循环每一行
			for(int j=0;j<result[i].length;j++){	//循环每一列
				for(Layer layer:layerList){			//循环每一个平铺层
					result[i][j] = result[i][j]|layer.notInMarix[i][j];	//求或运算
				}
			}
		}
		return result;
	}
}