package five.itcast.cn.player.concrete;

import five.itcast.cn.Point;
import five.itcast.cn.player.base.BaseComputerAi;


//孙悟空
public class SunWuKongAi extends BaseComputerAi {

	//子类实现这个方法，并改变其顺序可以实现防守为主还是猛攻
	protected Point getBestPoint(){
		//即将单活4，且我没有半活4以上的，只能堵
		Point mostBest = getBestPoint(computerDouble3Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computer3Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(humanDouble3Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human3Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human4HalfAlives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;

		mostBest = getBestPoint(computerDouble2Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computer2Alives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(computer3HalfAlives, humanSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(humanDouble2Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human2Alives, computerSencodResults);
		if(mostBest!=null)
			return mostBest;
		
		mostBest = getBestPoint(human3HalfAlives, computerSencodResults);
		return mostBest;
	}
}
