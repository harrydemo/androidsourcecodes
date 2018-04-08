package five.itcast.cn.player;

import java.util.HashMap;
import java.util.Map;

import five.itcast.cn.player.concrete.SunWuKongAi;
import five.itcast.cn.player.concrete.ZhuBaJieAi;
import five.itcast.cn.player.interfaces.IPlayer;


//电脑AI工厂类
public class AiFactory {
	private final static Map<Integer,IPlayer> ais = new HashMap<Integer, IPlayer>(2);
	//工厂方法，数字越大，难度越高
	public static IPlayer getInstance(int level){
		IPlayer ai = ais.get(level);
		if(ai==null){
			switch (level) {
			case 1:
				ais.put(level, new ZhuBaJieAi());
				break;
			case 2:
				ais.put(level, new SunWuKongAi());
				break;
			}
		}
		return ais.get(level);
	}
}
