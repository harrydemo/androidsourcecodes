package com.chenqi.service;

import java.util.ArrayList;
import java.util.List;

import com.chenqi.domain.User;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserService {
	private DBOpenHelper dbOpenHelper;
	
	public UserService(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}

	/*public void payment(){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.beginTransaction();//事启事务
		try{
			db.execSQL("update person set amount=amount-10 where personid=?", new Object[]{1});
			db.execSQL("update person set amount=amount+10 where personid=?", new Object[]{2});
			db.setTransactionSuccessful();//设置事务标志为成功，当结束事务时就会提交事务
		}finally{
			db.endTransaction();
		}
	}*/
	
	public void save(User user){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into userinfo (username,pwd,sex,age,telephone) values(?,?,?,?,?)",
				new Object[]{user.getUsername(),user.getPwd(),user.getSex(),user.getAge(),user.getTelephone()});
	}
	
	public String  findTedianByShengxiaoName(String name){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from shengxiao where shengxiaoname=?", new String[]{name.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public String  findTedianByxingzuoName(String name){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from xingzuo where xingzuoname=?", new String[]{name.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public String  findTedianByxuexingName(String name){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from xuexing where xuexingname=?", new String[]{name.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public String  findTedianBytwoxingzuo(String nanxingzuo,String nvxingzuo){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select tedian from peidui where nanxingzuo=? and nvxingzuo=?", new String[]{nanxingzuo.trim(),nvxingzuo.trim()});
		if(cursor.moveToFirst()){
			String tedian = cursor.getString(cursor.getColumnIndex("tedian"));
			return tedian;
		}
		return null;
	}
	public void insertPeidui(){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		/*db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","白羊座","躲在他的怀里,真的是天底下最温暖的一件事了.但守旧好静的他,的确跟不上你求新爱闹的脚步,留着他的电话号码吧!也许有一天你会再想起他.两情相悦指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","金牛座","这个专制的男人，对你而言简直像个暴君，在一次他坚持要你穿上最性感的衣服去赴宴时，你终于再也不愿忍受，这段恋情就此告终。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","双子座","在床上，他和你一样善于制造惊喜浪漫的气氛，但他的专制，和你不喜受拘束的个性，势必要起冲突。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","巨蟹座","他是个专制的暴君，喜爱热闹，你却性好恬静。本质既不相同，即使他显露出脆弱的一面，你也该义无反顾地离开他。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","狮子座","戏剧化而激情的恋爱过程，精彩程度绝不下于任何爱情文艺剧，你俩同样享受这种猛烈的爱，在床上亦然。但别轻易破坏和平，否则……"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","处女座","他让你陷入激情的热恋之中，但他那狂风暴雨的脾气令你震惊，更最受不了的还是他上厕所的习惯--关于马桶盖，你终于忍无可忍。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","天秤座","他是个外向活泼而有无穷精力的恋爱高手，你们的确有过澎湃的一段情，但他的专制，令你只想把他收藏在记忆中。两情相悦指数：5天长地久指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","天蝎座","你渴望细水长流的恋情，他却冲动躁进，你对他而言是难以承受之重，他对你而言，真的是难以承受之轻浮。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","射手座","他和你一样独立、爱自由，能纵情享乐而不担忧明日，只要不论及将来，你们可以一直相处愉快。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","魔羯座","虽然他很有野心，但他的实力的确赶不上你的帐单，想到后面还有许多医生、律师在排队，你实在没空多理他。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","水瓶座","精力充沛的他，对一切新奇的事物都能保持高度的兴趣，这点倒是与你不谋而合。但做完爱后，马上倒头呼呼大睡的作风，实在令人不敢恭维。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"白羊座","双鱼座","他的冲动、富攻击性，实在有悖于你慢工出细活的原则。在他第五次草草了事之后，你开始反省，这一切是否值得？当然答案是否定的。两情相悦指数：2天长地久指数：1"});*/
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","双鱼座","他总是记得帮你把冰箱填满食物，你真的是太感动了，你终于找到一个能完全照顾你的人。在床上，你们的步调亦相当一致。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","水瓶座","他是完全遵循惯性定律的动物，喜欢熟悉而固定的模式，你的做爱方式把他吓坏了。两情相悦指数：3天长地久指数：2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","魔羯座","他沉稳的个性的确抓得住你情绪的波动。在床上，你们亦非常契合，他不在意为你煮饭打扫，只要偶尔你也爬爬梯子换灯泡即可。两情相悦指数：5天长地久指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","射手座","他善妒、固执，而且坚持事情有一定的规律。他精力无穷，甚至可以天天做爱，但是，一定要在床上。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","天蝎座","他稳重而持续的付出，令你感动。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","天秤座","在性爱方面，你们的确有很好的互动。但你们毕竟是分属两个不同世界的人，他希望生活有一定的规律，你却一日数变，如果你们可以彼此互相体谅，或许还犹有可为。两情相悦指数：4天长地久指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","处女座","只要顺着他的毛摸，妳就能完全掌握住他，他重视干净的厨房和床铺，这点与你不谋而合。天生一对，还有什么理由可挑剔呢？"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","狮子座","他是个占有欲强的吝啬鬼，而你则花钱似流水。虽然在床上你俩可真的配合得完美无瑕，但除此之外，别无共通点。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","巨蟹座","他的慵懒和你的贤慧适成一对。谁在乎他饭后喜欢窝在电视前睡觉呢？反正，你也正想边看电视边享用饭后甜点。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","双子座","你喜欢随性的做爱，他却重视气氛的经营，于是在他拒绝你的时候，你发现了其它乐意配合的人。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","金牛座","你喜欢烹饪，他是个老饕，两人世界甜蜜无比。但小心物极必反，最好还是保持“有点粘又不太粘”的距离。两情相悦指数：5天长地久指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"金牛座","白羊座","躲在他的怀里,真的是天底下最温暖的一件事了.但守旧好静的他,的确跟不上你求新爱闹的脚步,留着他的电话号码吧!也许有一天你会再想起他。"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","双鱼座","两个双子在一起，一定有聊不完的话，但这并不一定表示你们能发展成恋人关系。不过，无论如何，你们还是彼此最佳的心理谘商导师。两情相悦指数：2天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","水瓶座","他唤起你内心的万般柔情，他的枕边细语令你砰然心动，但他需要很大的自我空间，远超过你的想象，释放他吧！两情相悦指数：3天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","魔羯座","他喜欢调情，而你也很能享受被追逐的乐趣，不论是心灵或肉体，你们都是颇投合的一对，只要你能让他对你专情，你们会相当甜蜜。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","射手座","虽然你们的个性南辕北辙，但对于你们之间的差异，两人都以幽默的精神看待，虽然当不成恋人，但还是"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","天蝎座","你们都是深谙口语沟通的人，甜言蜜语使你俩不论在心灵或性爱方面，都有更紧密的连系。两情相悦指数：5"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","天秤座","他和你一样畏于承诺，但只要还在交往中，他会乐意用尽心思讨好你。而和他上床更是令人回味无穷，尽情享乐吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","处女座","有丰富的想法，你则有细腻的感情，他能说善道、而你总是含情脉脉地望着他，虽然无法终身相守，却是一段值得回"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","狮子座","他喜欢调情，而你也很能享受被追逐的乐趣，不论是心灵或肉体，你们都是颇投合的一对，只要你能让他对你专情，你们会相当甜蜜。两情相悦指数：5天长地久指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","巨蟹座","他唤起你内心的万般柔情，他的枕边细语令你砰然心动，但他需要很大的自我空间，远超过你的想象，释放他吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","双子座","两个双子在一起，一定有聊不完的话，但这并不一定表示你们能发展成恋人关系。不过，无论如何，你们还是彼此最佳的心理谘商导师。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","白羊座","你辩才无碍,他才思敏捷,虽有充分的沟通,却少了一点“爱情激素”,情事难谐,却可望发展出一段纯友谊。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双子座","金牛座","这位长袖善舞的外交长才，似乎永远不安于室，从餐厅的女侍、你的姊妹、路上的女孩，他似乎跟每个人都太聊得来了。还是分道扬镳吧！"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","双鱼座",""});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","水瓶座","对你而言，他实在是太散漫了，总是说得多、做得少。好好地和他说再见吧！毕竟他还不算太坏。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","魔羯座","敏感的巨蟹承受不了你不经意的批评。事实上，他太容易受伤了，这段感情让你觉得好似走在钢索上，不过坚持就是胜利！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","射手座","喜欢照顾人的巨蟹座会竭尽心力地呵护你，但对你而言，他着实太粘人了，你们的恋情可以持续大约一个月，但接下来可就不那么乐观了。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","天蝎座","他和你同样心思细腻，两人共处的晚上是充满柔情绮旎的一夜,许多的拥抱、抚摸、及深情的吻,你们知道,此生非彼此莫属。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","天秤座","你们是本质上互不相属的一对，但他在床上及金钱上的慷慨施与，令你如沐春风。所以，有什么好担心的呢？两情相悦指数：3天长地久指数：2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","处女座","对他而言，你是无懈可击的完美恋人；对你而言，他则是平淡无奇的凡夫俗子。但是，若他的耐心感动了你，这段恋情就是长长久久。两情相悦指数：4天长地久指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","狮子座","他愿意无怨无悔的呵护你，真棒！然后粘着你，这可不行。初时甜蜜，久则生厌，但这段恋情让你学会“付出与爱”。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","巨蟹座","你们一样地恋家顾家，但小心你们的情绪化伤到了彼此。不过，还好你们都有足够的耐心互相为对方疗伤。两情相悦指数：4天长地久指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","双子座","他的敏感和对家的渴求，使你们的恋情日渐稳定，但你却慢慢地产生一种快窒息的感觉，还是分手吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","白羊座","安全感、稳定的生活、舒适的居家环境，是你俩所共同渴求的。虽然他的善妒多疑令你深感挫折，但只要你赢得了他的信任，你会发现他是一个最温柔的情人。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"巨蟹座","金牛座","他有时会送花献殷勤,有时却又不见人影.忽冷忽热,是你对他的感觉,别浪费时间揣测他的心思了,离开他吧! "});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","双鱼座","幽默、风趣、性感，你的确深深地为他所吸引，但别让他猜透你的心思，“欲擒故纵”是最好的对策。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","水瓶座","他的群众魅力令你心动，但深入交往之后，你会发现他毕竟和你是完全不一样的人，还是尽早另起炉灶吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","魔羯座","他那赤裸裸的热情吸引着你，而你的俏皮奔放亦令他着迷。可惜，他对你来说还是太激烈了点，最后你会发现，他只不过是虚张声势的猫，绝不是你心目中永远的狮子王。两情相悦指数：5天长地久指数：2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","射手座","你们各有自己想控管的领域，他浮夸虚荣，你矜持谦逊；他暴燥，你情绪化。不论能持续多久，你们的组合相当有趣。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","天蝎座","你们一样的热情，一样地冲劲十足，只要在家里摆上足够的镜子和衣橱，你们的生活保证热力四射。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","天秤座","他的狂野热情，对你来说是很浪漫的诱惑，一旦你开始发挥你的批评精神，他就会开始变得专制，一切也就完了。两情相悦指数：3天长地久指数：2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","处女座","他的占有欲激起你无穷的热情，你们的床上生活亦进展地如火如荼，令人欲罢不能。尽情享受和他相处的快乐时光吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","狮子座","在床上，你的表现令他感到不可思议，虽然那是很美妙的经验，但对自命英雄的他来说，的确有“最难消受美人恩”的感慨。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","巨蟹座","有野心、外向、性感热情的狮子，拥有你所向往的男性特质，但选定他前，记得先看看他发脾气的样子。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","双子座","狂野、慷慨的他，最懂得如何挑起你的情欲，但他迟迟不求婚，你可没耐心等他。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","白羊座","他能给你天后级的待遇，且他似乎能读出你所有的心事，不用水晶球，妳也知道他是妳的如意郎君。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"狮子座","金牛座","他喜欢主控一切，你会发现他的自律精神极差，连他自己事后都觉得尴尬。保持一贯的温柔，静观其变吧！"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","双鱼座","会教你如何更理性，而你将让他体会到痴恋的快乐。在现实生活上，他的精于分析，和你的热情，能结合出独特的乐趣。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","水瓶座","妳日夜不分的生活方式，真让他大叹吃不消。他冷静的批评和你的黑色幽默，能让你俩维持几周的热度，太久就不好玩了。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","魔羯座","你们一样渴求稳定，不论是财务上或情感上，你们是能同甘共苦的一对，只要心在一起，天下没有克服不了的难关。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","射手座","他对着你凌乱的床铺皱眉，而你则觉得他把时间浪费在打扫上实在太可怜。他一向言出必行，这对你而言，也委实太严肃了点。两情相悦指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","天蝎座","他善于分析，而你则宁可相信直觉，你俩的搭配，是最能互补的组合，只要你们能充分信赖对方，共同生活必定充满乐趣。两情相悦指数：4天长地久指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","天秤座","你陶醉于作战式的做爱方式，他却对绉成一团的床单皱眉。你喜欢在五星级饭店用餐，他却宁取山林野味。交往之前，仔细考虑一下吧！两情相悦指数：2"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","处女座","虽然，你们为了许多小事而争论不休，最后，你们会发现彼此是天造地设的一对，各方面皆然。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","狮子座","只要好好地哄他，他会为你付出一切。他愿意为你打扫房间，洗刷窗户，只要你在床上回报热情，他会觉得一切付出都是值得的。两情相悦指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","巨蟹座","他的批评一不小心就伤了你，但他的确是个成熟稳重且值得信赖的人，把自己的皮磨厚一点，不妨接受他吧！两情相悦指数：4天长地久指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","双子座","你们有着同样纤细的感觉及共同的话题，可惜你们的肢体沟通有些障碍，无法发展更亲密的关系，还是就当他的哥儿们吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","白羊座","在床上，你似乎比他还要活跃，但撇开性趣不谈，你们在各方面，都算是相当匹配的一对，就连存款，亦是旗鼓相当。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"处女座","金牛座","他的直言批评的确令人难以消受，但如果你能适应他务实的态度，你"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","双鱼座","你们彼此一见钟情，但他游戏人间的态度终将伤了你的心，谈场恋爱可以，至于规划未来的事就不必了。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","水瓶座","他的风流倜傥令你心动,但玩世不恭的行事风格却令你不敢领教,这段恋情注定要无疾而终。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","魔羯座","在床上，你们真是天造地设的一对，但为何恋情无法持久呢？也许因为两人的过去总是不时浮现，困扰着彼此吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","射手座","他是个很棒的情人，但他的四处留情令你泄气，在他离开你之前，跟他分手吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","天蝎座","他要求你要像个忠贞烈女，自己却自命风流，处处留情。他让你完全没有安全感，初识的浪漫，转"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","天秤座","他的不切实际对照你的务实，真的是很不搭调的组合。虽然拥有一夜情的浪漫，但翌晨不说再见就消失，令你觉得他未免也太离谱了。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","处女座","和另一个天秤座交往，仿佛和另一个自己沟通。他和你一样善于挑逗调情，也都是大众情人，你们的结合，让许多人跌破眼镜，但结局却是出乎意料的美满"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","狮子座","他友善的示意，令你觉得亲切，但用不了多久，你就会发现，他的友善是举世皆然。于是，你宁可另起炉灶。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","巨蟹座","在宴会上，你们都对彼此留下了极佳的第一印象，但上床之后，你会发现他的确有过人之处。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","双子座","他总是依然故我，虽然你为他做了那么大的改变，他似乎没感觉，也不领情。离开他，作你自己吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","白羊座","不只是说说而已，他还会陪着你参加街头运动，例如为环保而"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天秤座","金牛座","当他打电话说会晚点回来时，你已经有点坐立难安了，实际上，他根本没打算回来吃晚餐。这让你气愤不平，把他的食物拿去喂狗，之后请他吃自己吧！两情相悦指数：3天长地久指数：1"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","双鱼座","他的热情及奉献精神都不亚于你，但他的占有欲实在太强，你会被他管得疲惫不堪。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","水瓶座","他在床上的热情令你大开眼界，不过，参加你的姊妹淘大会时，他委实太沉默了点。在那么一长串的选择对象中，你只好把他除名了。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","魔羯座","他诚恳而忠贞，是值得你托付终身的对象，你们的床上生活更是乐趣无穷，留几个黄色笑话在床上讲吧！他会加倍回馈的。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","射手座","当他进入了你的生活，你会发现，朝思暮想，你还是无法将他从心里头拿掉，但他的善妒又逼得你喘不过气来。其实你想要的，无非是一些变化而已。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","天蝎座","你们一样对生活充满热情，对彼此亦然，爱得狂热、争执亦猛烈。如尽量避免冲突，其实你们是非常相似的一对。两情相悦指数：5"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","天秤座","他可以在床上点燃你全身的热情，也能透视你内心所有的需要。但当你想把他引见给你的朋友时，他却总是临阵脱逃。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","处女座","他在床上的魅力及细心，的确令人感动，但就现实的考量而言，他离你的标准还远的很呢！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","狮子座","你们是很容易一见钟情的组合，但可惜的是，男的善妒，女的霸道，不久即不欢而散。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","巨蟹座","他的热情化解了你的矜持，他的体贴治好了你的情绪化,你的坚贞,使他不再好猜疑，你们可称得上是绝配。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","双子座","他要的比你所能给的多，你的内心亦在挣扎，一个渴望和他终日缱绻，另一个你却期望拥有自己的空间。只有在床上，妳才是全心全意的。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","白羊座","他的床上功夫一向令妳欣喜若狂,把房间里可能砸碎的东西都收起来吧!尽情享受火辣辣的床上欢愉吧! 两情相悦指数：5"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"天蝎座","金牛座","把贴心话留着和你的手帕交说吧！天蝎座的他宁可多一点床上实质的互动。你俩一样情绪化、善妒"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","双鱼座","他虽是十二星座中的花花公子，但在床上，你们的确是相当投合的一对，给他一些自由的空间吧！他想回来的时候，自然会回来的。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","水瓶座","一夜情，可能是你和他之间最美好的回忆。他酷好自由，而你宁可要一份安定的感情，在他无故失踪前，甩掉他吧！两情相悦指数：3天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","魔羯座","喜欢冒险、爱好幻想，是你们共同的特性，同样自由主义的性格，使你们很难给彼此承诺。两情相悦指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","射手座","他玩世不恭的态度本来就不容易搏得你的信任，加上每次约会总是呼朋引伴的，你实在无法和这样的人更进一步。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","天蝎座","你愈想绑着他，他愈想逃离。最好的方式是自"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","天秤座","只要有空气和水，他就可以活得好好的，这点和你可是大相径庭的。你需要一个安稳的家、固定的床，所以，和他保持距离吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","处女座","你们有过许多共同且美妙的社交生活，但很可惜的是，当你想为他安定下来时，他却已准备要开始另一段漂泊的生活。两情相悦指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","狮子座","短暂的肉体欢愉是大有可为的，这位罗蜜欧是很好的调情圣手，想和他建立长久的关系吗？毫无希望。两情相悦指数：4天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","巨蟹座","你随时都可享受一段激情，妳和他，原本亦是无意的邂逅，但如果机缘凑巧，很可能成就一段良缘。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","双子座","他自由派的作风令你觉得轻松，但也为你带来了不安全感，还有，他常常不小心花到你的钱，在他尚未造成更严重的损害之前，离开他吧！两情相悦指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","白羊座","他巧妙的融入妳的生活之中，并且为你添加了许多额外的趣味，和他在一起，你真的享受到所有自由的乐趣。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"射手座","金牛座","和他相处，生活的确充满乐趣。但他是个不给承诺的人，别浪费太多时间在他"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","双鱼座","他充分享受为你服务的乐趣，在各方面皆然。尽情享受他的体贴，也好好把握他吧！两情相悦指数：5天长地久指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","水瓶座","他固守一成不变的生活，实在令你难以消受，他在床上的保守作风，完全无法挑起你的情欲，再观察看看吧！两情相悦指数：3天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","魔羯座","在生活上，你们是能完全分工的一对，你们常能彼此提醒对方忘记的事情，没有任何一对情侣能比你们有默契。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","射手座","追求稳定的他，希望你能同样地对这段情专注，但你对整个世界还有无穷的好奇心，让这只老山羊尽早知道你的底线吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","天蝎座","你的热情可以让他忘记一切烦恼，他丰厚的荷包让你觉得踏实，还有什么好说的呢？好好套牢他吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","天秤座","他是个稳重且财力雄厚的君子，但天秤女郎要的是个能玩会闹的舞林高手。对你而言，他太安静了，真是可惜，否则他可称得上是金龟婿呢！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","处女座","他既性感，又聪明有野心，你欣赏他温柔的做爱技巧及丰厚的股票存款，他也了解你对秩序的渴求。心有灵犀，夫复何求？"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","狮子座","你欣赏他的成熟睿智，以及事业有成所流露出的自然风采，但你渴望一段纯纯的爱情，他却把你现实地计算着谁的付出较多。两情相悦指数：3天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","巨蟹座","他总是喜欢炫耀自己的财富和野心。提醒他，你可是唯一愿意提供避风港的人。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","双子座","他的长才在于储蓄及管帐，当他看到你所记的帐簿，他真的有种濒临崩溃的感觉。在床上亦然，他喜欢照著书上来，你的随心所"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","白羊座","他是你心灵和肉欲两方面的最佳搭档，你们在各方面都相"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"摩羯座","金牛座","他会鼓励你做些务实的事，如股票投资之类。而你的淘气贪玩，为他的生活带来额外的乐趣。但相处日久，你再也无法忍受他的现实和严谨的生活。"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","双鱼座","他的知性神秘令妳好奇，但你发现，你永远无法了解他的内心世界，换个情人会更好。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","水瓶座","虽然口头上说着要保持连络，但他却不曾主动打电话给你，虽然你觉得与他交情不仅于此，他却还只当你是个朋友。最好和他有共识，只当他是个朋友。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","魔羯座","他愿意带着你乘着爱的翅膀一起飞翔，这对你而言，真是“何乐而不为”呢？就是他了，不用再找啦！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","射手座","他是个风趣幽默的人，和他相处可说充满欢笑，可惜他的床上表现太令人失望,你只好放弃他。两情相悦指数：4天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","天蝎座","他聪明睿智，你则明艳出色。你们走在一起，真称得上是一对金童玉女。收敛一下你的大小姐脾气吧！他值得你这么做。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","天秤座","虽然只睡两个小时，他还是精力充沛，这点令你慑服，尽情和他享受床上之乐吧！除此之外，好象没有其它可指望的了。两情相悦指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","处女座","他可以享受独处的时光,你也乐于给他自由的空间;但有朝一日,他会想和你定下来，你却觉得，时机尚未成熟。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","狮子座","你的干涉令他退怯，他渴求一些属于自我的空间，妳的多疑猜忌，会令他很快地"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","巨蟹座","这位好好先生的适应力极强，独立的生活空间和同穿一条裤子，他觉得并无不当，问题是：你要他吗？两情相悦指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","双子座","他的睿智机敏深深地吸引你，你真恨不得能立即拥有他。但是劝你别轻举妄"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","白羊座","他和你一样有着大胆的想象力，你们的性爱关系会是本世纪最完美的组合。在其它方面呢？一样地默契十足。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"水瓶座","金牛座","位理想主义者深深地吸引了你，俏皮的床上作风"});
		
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","双鱼座","你和他有许多的相同点，彼此温柔的对待，的确让对方感到窝心，但偶尔还是要想想现实面的问题吧！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","水瓶座","在性方面，他的柔情令你感到无比温馨，但他软弱的性格，实在令人失望。想和他有结局，真的需要很长、很长的一段爱情长跑。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","魔羯座","你太务实，他太多梦，如果你能试着不那么现实的话，你会发现他的确是很好的情人。两情相悦指数：5天长地久指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","射手座","你的态度让他觉得捉摸不定，他对你的忽冷忽热感到困惑，不小心处理的话，分手时，类似“致命的吸引力”的情节可能会上演喔！"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","天蝎座","你可以在床上给他很大的满足，但他脆弱的心灵经不起你一再地刺伤。这段恋情虽然甜蜜，却太短暂。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","天秤座","虽然和他做爱的感觉很棒，但敏感的他很快就会了解：你没办法随时提供他一个可哭诉的肩膀。因为，你实在太忙了。两情相悦指数：4天长地久指数：3"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","处女座","两情缱绻之时，你最能感受他的温柔与魅力，而你将成为他最好的事业顾问、谈心对象及老妈子，他是非你莫属了，而妳呢？只要觉得"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","狮子座","第一、他似乎太多愁善感了；第二、他好象总是一副阮囊羞涩的样子；第三、他关心自己远多于关心你。他该出局了吧！两情相悦指数：3天长地久指数：1"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","巨蟹座","一样的敏感和温柔，不论在生活上或床上，你们都相当的投契，可说是天作之合。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","双子座","他的柔情，使你停留了比自己所预期的时间还要长，但他的优柔寡断，最后还是气跑了你。"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","白羊座","他的敏感与诗人气质，令你想好好照顾这个多愁善感的男人，但你想当他的情人或是母亲呢？他是不会改变的，如果妳想和他在一起，就接受他吧！两情相悦指数：4"});
		db.execSQL("insert into peidui (nanxingzuo,nvxingzuo,tedian) values(?,?,?)",
				new Object[]{"双鱼座","金牛座","他是个逃避现实的人，虽然你有心帮他，最后你将徒劳无功，虽然他的床上表现相当温柔，但还是算了吧！两情相悦指数：2"});
		
		
	}
	public void insertXuexing(){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"O型血","O型人的处世态度有三种：好与人进行力量的对比、对于善恶敏感、重信用讲友情。O型人若认为对方是可以信赖的朋友时，其态度会出现一百八十度的变化：直爽开朗，亲切随和；热情主动的关照，虽带有强加于人的色彩，却充满人情的温暖。前面提到过的心灵爱抚也会充分表现出来；还喜欢全家交际。可以说，最值得结交的朋友莫过于O型人了。反之，若认定对方是敌人的话，O型人的反应也是够"});
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"A型血","A型人的优点是待人诚实，不轻口许诺，一旦应诺会全力以赴，决不失信。从这一点来说，A型人是可以信赖的相处共事的合作者。但另一方面，我觉得他们在与人交往中有过分区别对待的倾向，自己人与外面人，本公司与他公司的人，他们划得一清二楚。"});
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"B型血","不了解的人，看到B型人那简慢而又冷漠的态度会耽心其是否心怀歹意。可是接触后会觉得很好相处。他们不计较小事，虽欠浪漫情怀，但心地善良，富有人情味。还有的B型人常以行动表示自己对人的同情和理解，他们助人为乐到了近乎管闲事的程序。难弄的倒是他们那好顶牛的脾气：你这么说，他偏那么说；不管别人的意见正确与否，上来就放一通炮，说得又都是无关宏旨的东西……"});
		db.execSQL("insert into xuexing (xuexingname,tedian) values(?,?)",
				new Object[]{"AB型血","AB型人好燃烧绚丽的爱情之火。这种爱往往带有戏剧味儿。但有点过于戏剧化了甚至给人以“有意栽花”之感。那倒并非他们矫柔造作，而是本人向往追求这种爱。既有合理而又显得冷酷无情的理性主义思想方法，又有爱好空想，想入非非的特点，这是AB型人气质的重要的两重性。在爱情方面，他们往往要把自己比作恋爱剧中的主人公：悲剧式的，或者秘密相爱式的。在这种情况下，说得过分些，"});
		}
	public void insertXingzuo(){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"白羊座","白羊座的你喜欢无拘无束和自行其是，而不愿意步你人之后尘。你从来不掩饰自己的感情，要么热情洋溢，要么怒发冲冠。如果你的愿望受阻，你也决不悄然收兵。无论是在家里还是在外面，你都不怕争执，但事后总是弃之脑后，从不记恨在心。在困难和危险的关头，你能充分表现出自己的品格和勇气，得到人们的敬佩和赞扬。白羊座的你做事从不吝惜气力，宁可付出巨大的代价，也要力争前茅。","3月21日 - 4月20日"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"金牛座","这一座的人对逆境的适应较慢。挫折和失败常能使你意志消沉，甚至你会人为地把自己囚禁在无声的愤怒之中，拒绝与外界的一切接触。一旦境况有所好转，你又会重新振作起来，以空前的工作热情去实现自己的目标。金牛座的人思想趋于保守，但善于理财。当你拥有一定数量的财产，手头从不短缺时，你方能感到坦然自若。经济上，你的现实感非常强，十分善于安排自己的物质和家庭生活。事业","4月21日 - 5月21"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"双子座","双子座的人多半喜欢把自己的才智用于事业方面，而不愿意用以扩大自己的物质利益。你思想中的一闪念，常常会有助于你事业上的腾飞。这一座的人还有一个显著的特点；特别善于调动自己朋友的积极性。双子座的人适合从事文学、商业及需要语言表达能力的职业。在这些方面你能脱颖而出。另外，在新闻、摄影、旅行等需要机智、灵活和果敢的工作中，以及涉及人际关系方面的工作中，你会表现出非凡的才干。若是土星的影响力较大，你将成为一个出色的实业家。你特有的金融嗅觉和心理感应及善于把握时机的能力，将有助于你事业上的成功。","5月22日 - 6月21"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"巨蟹座","巨蟹座是夏天开始的第一个星座，夏天把深居简出、思想保守和敏感的性格带给了这一星座的人，这是个需要自我保护的人。出生在这一星座的人，有慈母(慈父)般的热情，也洋溢着孩子般的纯洁和天真，有很强的制力。你的人生哲学是：使自己和别人都幸福如愿。你对自己用双手创造出来的有限幸福心满意足，但总担心有人会夺去这一幸福，因此，你过分地抓住你在精神和物质上所拥有的一切。","6月22日 - 7月22"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"狮子座","狮子座的人不愿意置身于平庸的生活中，通常你很快就会摆脱这种境地，因为你颇晓人生和事业成功的秘诀。崇高的理想使你不愿意沉惬于狭窄的家庭圈子里，而希望到更大的范围中去施展自己的才智。有时你会表现得专横拔扈，但这与你肯为别人的幸福而努力奔波的善良心地并行不悖。非凡的才华使你很容易走上享有威望的职位。尊严和慷慨是你性格不可分割的一部分。你善于发现为自己工作的","7月23日 - 8月23"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"处女座","处女座的人对每一件事情都要周密计划，仔细安排，喜欢事情的来龙去脉一目了然，事后做记录以备查，每项开支都记入帐目。总之，你的生活是一丝不苟和井井有条的，不喜欢突如其来的事情扰乱你的生活。尤其是当太阳以外的某一个或几个行星出现在处女座时，这种性格特点就更为明显。处女座的人总是试图将自己的激情和冲动置于理智的控制之下。你深思熟虑、反复斟酌，有时甚至放过了有","8月24日 - 9月23"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"天秤座","天秤座是象征着秋天来临的星座，秋意表现在天秤座的人身上是对意气相投的特殊嗅觉。你寻求着共同点和互相谅解的土壤。和蔼可亲的秉性，使仇恨和敌意永远无法靠近你的身旁。天秤座的人温柔、娴雅，你需要欢欢乐乐地生活，需要忠贞不渝的友谊和爱情。随和与顺从是你性格上的主要特点。无论对天秤座的男性还是女性，都可以看到这些难能可贵的女性品质，品格正直，平易近人，处处闪耀","9月24日 - 10月2"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"天蝎座","天蝎座的人需要经常不断地处于忙碌之中。你喜欢亲自动手去做；喜欢改善自己的工作和生活环境；喜欢更新自己的想法，而不喜欢无所事事和庸庸碌碌的生活，那会使你丧失生机和活力。你从不接受任何失败，如果遭到了挫折，你将会产生强烈的心理变态反应。而后你会从零开始，凭着顽强的意志和坚韧不拔的精神，重新奔向成功。你喜欢戏剧性的场面，会不时地在你前进的道路上导演一幕。最","10月24日 - 11月"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"射手座","这一座的人性格开朗，思想活跃，注重文化修养，同时又不忘放眼世界。你人在现实生活，但思想常常飞向遥远的过去和美好的未来。敏捷的思想跳跃着，一会在这，一会儿又在那，使人觉得你近在眼前，又仿佛在天边；让觉得既与你志同道合，又仿佛与你格格不入。你是一个思想需要设法平静的人。射手座的人热情洋溢，对生活充满火热的激情。你从不计较个人的得失，喜欢同时投身到许许多多","11月23日 - 12月21"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"摩羯座","魔羯座的人，尤其当有几个行星同时处于你的星座时，你将是一个具有现实主义思想和有抱负的人；同时你又容易被热烈的感情征服，是一个有强烈的忘我精神的人。你表情平静而淡漠，不太容易接近，喜欢离群独处。你害怕别人毫无意义的谈话会占据你宝贵的时间，也不能接受别人对你粗暴无礼你严肃认真、思想深沉、始终如一、忠诚可靠、正直廉洁并富有献身精神。另外，你坚如磐石、毫不妥","12月22日 - 1月2"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"水瓶座","水瓶座的人是个个性极强的人，你向往人之间美好的情意，但绝不愿意受感情上的丝毫束缚。你喜欢丰富自已的思想境界，喜欢到旅行中去开阔自己的视野，并喜欢在与人交往之中了解你们的思想观点。水瓶座的人不能忍受任何约束，你也决不强迫自己去服从任何纪律。如果某件事引起了你的兴趣，你能为之付出巨大的努力，单调无味的生活会使你心烦意乱，甚至能产生一种使你周围的人都无法忍","1月21日 - 2月19"});
		db.execSQL("insert into xingzuo (xingzuoname,tedian,timerange) values(?,?,?)",
				new Object[]{"双鱼座","你的身上总保持着一种天真、忠厚的气质。一般地说，如果你的生辰天宫图中没有土星过多的不利因素影响，且又遇到木星或金星处在理想方位的情况时，你会经常受到幸运的照应：意想不到的事情、突如其来的机遇、喜幸的性格将会有利于你。你也有可能中彩或在人生道路上遇到一位非常富有的和慷慨的知已。于是，这位双鱼座的人就会进入另一种新的意境中，过着充满幻想或神秘色彩的美好生","2月20日 - 3月20"});
	}
	public void insertShengxiao(){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"鼠","鼠的本性使属鼠人对一切事情无孔不入，能够很好地保守自己的秘密，但你却是探听别人秘密的专家。属鼠人使用搜集到的致命消息置人于死地而后快，并且钻别人的空子而问心无愧。总之，属鼠人绝不会放过任何一个打听消息的机会。属鼠人很爱管闲事，但用意多是好的。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"牛","这个属相的本性是脚踏实地，从不感情用事。单凭感情很少能改变属牛人的想法。在一些事情上，如果别人征求属牛人的意见，属牛人总是支持有可靠、确切把握的方案。如果别人想在与属牛人打的官司中胜诉，就需要充分地利用耐心与智慧。属牛人的体质很好，不易生病。属牛人很自信，不妥协，你蔑视别人的软弱。如果属牛人能注意培养更多的幽默与热情，你的人生将会更幸福。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"虎","在东方，老虎象征着权力、热情和大胆。属虎人是一个爱造反、引人注目，并难以捉摸的人物。属虎人受到大家的敬畏，使人害怕你就像害怕真虎一样。属虎人能使家庭避开三大灾难：火、贼和鬼。假如别人对属虎人那生龙活虎的性格能习惯，那么，在你的周围会拥有幸运。属虎人的活力和对生活的乐观具有感染力，你会唤起人们心中的各种感情，唯独没有冷漠。总之，吸引人的属虎者会成为人们"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"兔","贪图安逸、厌恶冲突的品质会给属兔人带来弱者、机会主义和自我放纵的坏名声。如果让属兔人来选择生活道路，属兔人会选择安逸的生活方式。属兔人无论男女都总爱穿宽松舒适的衣服，但裁剪要一流的，料子要好的。属兔人还爱穿开士米背心、纯丝罩衫、耐摩的亚麻和花呢衣服，一块貂皮或灰鼠皮披在肩上，看上去很随便，但从你的穿着中一眼就可以看出你是属兔的。浮华的、几何形的或刺眼"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"龙","神话传说中那巨大、宏伟的龙使人们产生无限遐想。所以龙那神奇的品质，不管虚幻与否，肯定也包含在那些出生在龙年人们的心中。在中国，龙象征着皇帝或男性，它代表着权力。在龙年出生的人据说都带着命运之角。属龙的孩子喜欢拣重担挑，喜欢承担重要责任，即使这个孩子在家里最小也是如此。年龄较大的孩子常常能比他们的父母更能担负起抚养属龙人们的弟弟、妹妹的责任。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"蛇","蛇是十二属相中最顽强的属相，属蛇人是十二属相中最具有神秘感，最不可思议的人物。有些东方人认为蛇是阴险的、由于难以捉摸而令人产生恐惧感。其实，这是因为蛇的寿命太长，每脱一次皮就能够获得一次新生。这个特别的品质象征着属蛇人具有很强的再生的能力，在每一场战斗后你都能很快地恢复精力。中国人在传统意义上认为春夏两季出生的属蛇人最为厉害，而因为冬天是蛇冬眠的时间，所以冬天出生的属蛇人则相对来讲安静而顺从。另外，在好天气出生的属蛇人比在坏天气出生的属蛇人更为乐观，更容易得到满足。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"马","属马的人爱好智力锻炼及体育活动，人们可以从属马人们灵巧的举动，优美的身姿和急急的说话速度上看到这一点。属马人们反应迅速，能当机立断，属马人们动摇、少耐性的"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"羊","属羊人常因举止优雅，对人富有同情心而被人称道。属羊人喜欢儿童和小动物，是自然主义者。属羊人们很会理家。属羊人们能轻易谅解别人的过错，理解别人的难处。属羊人们在时间上慷慨，在金钱上大方，当别人落得无处安身、袋空如洗时，别人要相信属羊的朋友决不会见别人处困境而不顾的。无论走到哪里，属羊人都喜欢与人交往，对愿和属羊人合作的人以诚相待。一个人属羊意味着属羊人将来有美满婚姻，属羊人不仅会受到生活伴侣的爱，同样也会受到其属羊人亲属的爱戴。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"猴","属猴人多元性的品格之一是坚定性。尽管属猴人当中有些人看起来那么害羞、那么爱脸红，属猴人心中却正藏着不可动摇的想法。属猴人显示地是属猴人对自己的聪明、勇敢的自我欣赏，属猴人毫不掩饰自我欣赏后的欢乐和骄傲，也不对骄傲的言行加以任何粉饰，属猴人诚心诚意地认为别人比不上属猴人。如果别人真正了解了属猴的人，就不必费心去抱怨属猴人对“生活之乐”的自我陶醉，这就是属猴人之所以是属猴人的原因。然而，将所有出生猴年的人都说成自私的、嫉妒的也不准确。属猴人没有直接参加某些活动时，对从事这些活动的人并不大在意。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"鸡","属鸡人有不少优良品格弥补了自身的不足。属鸡人精明强干，组织能力强，严肃认真，待人直率，遇事果断。属鸡人对残暴的行为敢于正面指出，严厉批判。所有属鸡的都是对事物过分挑剔、追求尽善尽美的人。属鸡人对理论性较强的问题很敏感，在处理任何问题时都要按确立的章程去落实，对那些不按规章办事的人感到不理解。属鸡的人的优点还是很多的。属鸡人会在自己力所能及的情况下尽力去帮助别人。只是属鸡人的活力鼓动着属鸡人太想显示自己。属鸡人对自己的家庭非常关心，很维护自己的小天地。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"狗","人，具有约束自己的能力，使属狗人也会成为顾问、牧师、心理学家。"});
		db.execSQL("insert into shengxiao (shengxiaoname,tedian) values(?,?)",
				new Object[]{"猪","实际上，属猪人应称作物质主义者。只是属猪人不吝啬，喜欢同别人分享自己的所有。这样，在属猪人为别人付出时，属猪人也会从中受益。另一方面，属猪人的精神世界较粗浅，不敏感，甚至对别人给属猪人的侮辱只是不在乎的耸耸肩。属猪人眼光也较浅，只看眼前。也许正因为这个特点，倒使属猪人在本应极痛苦的时候解脱出来，属猪人从不把灾祸看得过重。引起属猪人陷入危机的境地也是由于属猪人过分慷慨造成的。当属猪人对别人提出的要求无法满足，或帮助别人时力不能及时，属猪人不是面对现实，而是极度的沮丧、失望。"});
		
	}
	
	
	public void update(User person){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("update userinfo set username=?,pwd=?,sex=?,age=?,telephone=? where userid=?", 
				new Object[]{person.getUsername(),person.getPwd(),person.getSex(),person.getAge(),person.getTelephone()});
	}
	
	public void delete(Integer id){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from userinfo where userid=?", new Object[]{id.toString()});
	}
	public void delete(String  username){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from userinfo where username=?", new Object[]{username.trim()});
	}
	public User find(Integer id){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo where userid=?", new String[]{id.toString()});
		if(cursor.moveToFirst()){
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			return new User(username,pwd,sex,age,telephone);
			
		}
		return null;
	}
	public User find(String username){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo where username=?", new String[]{username.trim()});
		if(cursor.moveToFirst()){
			//String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			return new User(username,pwd,sex,age,telephone);
			
		}
		return null;
	}
	public Boolean checkUser(String username,String pwd){
		//如果只对数据进行读取，建议使用此方法
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo where username=? and pwd=?", new String[]{username.trim(),pwd.trim()});
		if(cursor.moveToFirst()){
			return true;	
		}
		return false;
	}
	
	public List<User> getScrollData(Integer offset, Integer maxResult){
		List<User> persons = new ArrayList<User>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from userinfo limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
		while(cursor.moveToNext()){
			String username = cursor.getString(cursor.getColumnIndex("username"));
			String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
			String sex = cursor.getString(cursor.getColumnIndex("sex"));
			int age = cursor.getInt(cursor.getColumnIndex("age"));
			String telephone = cursor.getString(cursor.getColumnIndex("telephone"));
			persons.add(new User(username,pwd,sex,age,telephone));
		}
		cursor.close();
		return persons;
	}
	
	public Cursor getCursorScrollData(Integer offset, Integer maxResult){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		return db.rawQuery("select userid as _id, username, pwd,sex,age,telephone from userinfo limit ?,?",
				new String[]{offset.toString(), maxResult.toString()});
	}
	public Cursor getXingzuoCursor(){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		return db.rawQuery("select  _id, xingzuoname, tedian,timerange from xingzuo ",
				null);
	}
	
	public long getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from userinfo", null);
		cursor.moveToFirst();
		return cursor.getLong(0);
	}
}
