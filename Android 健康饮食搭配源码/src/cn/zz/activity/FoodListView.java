package cn.zz.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnFocusChangeListener;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class FoodListView extends ListActivity implements OnItemClickListener{//,OnFocusChangeListener
	ListView listView = null;
	// Button[] buttons={};
	private static final String[] food = { "猪肉", "猪肝", "猪血", "羊肉", "牛肉", "牛肝", "鹅肉", "兔肉", "狗肉",
			"鸭肉", "鸡肉", "驴肉", "鸡蛋", "鲤鱼", "黄鱼", "虾", "虾皮", "螃蟹", "蛤", "鳖肉",
			"田螺", "大蒜", "葱", "萝卜", "芹菜", "韭菜", "菠菜", "莴笋", "竹笋", "西红柿", "洋葱",
			"醋", "茶", "豆浆", "红糖", "蜂蜜", "牛奶", "白酒", "啤酒" };
	private static final String[] food1 = { "黄莲", "荞麦 雀肉 豆芽", "何首乌 地黄 黄豆 海带", "醋 红豆 半夏 南瓜",
			"橄榄 板粟 韭菜 ", "鲇鱼 鳗鱼 柿子", "狗肉 鲤鱼 柑橘", "鲤鱼 绿豆", "鳖", "鲤鱼", "金针菇",
			"豆浆 兔肉", "甘草 麦冬", "荞麦面 ", "富含维生素C的食物", "红枣 黄豆",
			"梨 柿子 茄子 花生仁 石榴 香瓜 芹菜 蜂蜜 西红柿", "芹菜 ", "鸭肉", "香瓜 木耳 牛肉 蚕豆 玉米",
			"地黄 何首乌 白术", "枣", "橘子 木耳", "黄瓜 蚬、蛤、蟹", "牛肉", "豆腐 鳝鱼 黄瓜", "蜂蜜",
			"糖浆", "白酒", "蜂蜜", "胡萝卜", "酒", "蜂蜜", "竹笋", "皮蛋", "豆腐 韭菜",
			"钙片果汁  药物 韭菜 柠檬", "胡萝卜 核桃 啤酒 红薯", "海鲜" };
	private static final int[] resId = { R.drawable.pork, R.drawable.pigliver, R.drawable.pigblood,
			R.drawable.lamb, R.drawable.beef, R.drawable.beefliver,
			R.drawable.goose, R.drawable.rabbit, R.drawable.dog,
			R.drawable.duck, R.drawable.chicken, R.drawable.donkey,
			R.drawable.egg, R.drawable.carp, R.drawable.yellowfish,
			R.drawable.shrimp, R.drawable.shrimp2, R.drawable.crab,
			R.drawable.clam, R.drawable.turtle, R.drawable.riversnail,
			R.drawable.garlic, R.drawable.onion, R.drawable.radish,
			R.drawable.celery, R.drawable.leek, R.drawable.spinach,
			R.drawable.lettuce, R.drawable.bamboo, R.drawable.tomato,
			R.drawable.foreignonion, R.drawable.vinegar, R.drawable.tea,
			R.drawable.beanmilk, R.drawable.brownsuger, R.drawable.honey,
			R.drawable.milk, R.drawable.whitespirit, R.drawable.beer };
	 private static final String [] foodjianjie={"猪肉是目前人们餐桌上最常见的肉类食物之一，猪瘦肉中富含铁，容易被人体吸收，能预防贫血。猪肉味甘咸，性平，有补虚、滋阴、养血、润燥的功效。"
     	,"猪肝味甘、苦，性温，归肝经。猪肝富含多种营养素，是预防缺血性贫血的首选食品，猪肝中的铁容易被人体吸收，是理想的补肝、明目、养血的食品。"
     	,"猪血味甘、苦，性温，有解毒清肠、补血美容的功效。猪血富含维生素B2、维生素C、蛋白质、铁、磷、钙、尼克酸等营养成分。",
     	"羊肉味甘、性温，具有补虚劳，祛寒冷，温补气血；益肾气，补形衰，开胃健力的功效。用于气血不足、腹部冷痛、体虚怕冷、腰膝酸痛、面黄肌瘦、血气两亏等一切虚状均有补益效果，最适宜冬季食用。",
     	"牛肉有补中益气，滋养脾胃，强健筋骨，化痰息风，止渴止涎之功效。牛肉富含蛋白质，而脂肪少，氨基酸组成比猪肉更接近人体需要，能提高机体抗病能力。",
     	"养血，补肝，明目。牛肝中铁质丰富，是补血食品中最常用的食物。"
     	,"鹅肉含有人体生长发育所必需的各种氨基酸，其组成接近人体所需氨基酸的比例，从生物学价值上来看，鹅肉是是理想的高蛋白、低脂肪、低胆固醇的营养健康食品，其亚麻酸含量超过其它肉类。鹅肉性平、味甘，具有益气补虚，和胃止渴的功效。",
     	"兔肉质地细嫩，味道鲜美，营养丰富，与猪牛、羊肉相比较，兔肉营养成分独特，具有高铁、高钙、高磷脂和低脂肪、低胆固醇等特点，并且具有很高的消化率(可达85%)，食后极易被消化吸收。",
     	"狗肉温补脾胃、补肾助阳、壮力气、补血脉。狗肉不仅蛋白质含量高，而且蛋白质质量极佳，尤以球蛋白比例大，对增强机体抗病力和细胞活力及器官功能有明显作用。",
     	"富含蛋白质、脂肪、铁、钾、糖等多种营养成分。其饱和脂肪酸的含量比猪肉、牛肉、羊肉少得多，脂肪酸熔点低，易于消化。所含胆固醇比鱼还要低一些。《本草纲目》记载：鸭肉主大补虚劳，最消毒热，利小便，除水肿，消胀满，利脏腑，退疮肿，定惊痫。",
     	"鸡肉含丰富蛋白质，种类多，其含量比猪、牛、羊肉都高，而脂肪含量比其它肉类低，且多为不饱和脂肪酸，营养价值高，消化率高，很容易被人体吸收利用，有增强体力、强壮身体的作用。",
     	"驴肉富含蛋白质，而且含有动物胶、骨胶原、钙、硫等成分，其中含有的高级不饱和脂肪酸，尤其是亚油酸、亚麻酸，对动脉硬化、冠心病、高血压有着良好的保健作用。中医认为：驴肉性味甘凉，有补气养血、滋阴壮阳、安神去烦功效。",
     	"鸡蛋被认为是营养丰富的食品，含有蛋白质、脂肪、卵黄素、卵磷脂、维生素和铁、钙、钾等人体所需要的矿物质。鸡蛋味甘、性平，具有养心安神、补血、滋阴润燥的功效。",
     	"鲤鱼的蛋白质不但含量高，而且质量也佳，人体消化吸收率可达96%，并能供给人体必需的氨基酸、矿物质、维生素A和维生素D。鲤鱼味甘、性平，具有健脾开胃、消水肿、利小便的作用。",
     	"黄鱼含有丰富的蛋白质、微量元素和维生素，对人体有很好的补益作用。中医认为，黄鱼有和胃止血、益肾补虚、健脾开胃、安神止痢、益气填精之功效，对贫血、失眠、头晕、食欲不振及妇女产后体虚有良好疗效。",
     	"虾营养极为丰富，含蛋白质是鱼、蛋、奶的几倍到几十倍；还含有丰富的钾、碘、镁、磷等矿物质及维生素A、氨茶碱等成分。",
     	"虾皮中含有丰富的蛋白质和矿物质，尤其是钙的含量极为丰富,是缺钙者补钙的较佳途径之一。",
     	"螃蟹含有丰富的蛋白质及钙、磷、铁等微量元素，对身体有很好的滋补作用。中医学认为螃蟹性寒、味咸，有清热、散血结、续断伤、理经脉和滋阴等功用；其壳可清热解毒、破淤清积止痛。",
     	"肉味鲜美、营养丰富，蛋白质含量高，氨基酸的种类组成及配比合理； 脂肪含量低，不饱和脂肪酸较高，易被人体消化吸收。蛤味咸、性寒，有滋阴、利水、化痰、软坚的功效。",
     	"鳖肉偏于滋阴补肾、补虚。是久病体弱，消瘦无力等人群的食疗佳品。鳖味咸、性平，具有滋阴凉血、平肝益气、散结软坚、消淤等功效。",
     	"含蛋白质、脂肪、碳水化合物、钙、磷、铁、硫胺素、核黄素、尼克酸、维生素。田螺味咸、性寒，有清热利水、除湿解读的功效。",
     	"大蒜含有丰富的营养成分，尤其是具有独特生物活性的蒜素等含硫化合物及硒蛋白等功能成分，这些物质具有显著的医用和食用价值。中医理论认为，大蒜生者辛热、熟者甘温，可除寒湿、怯阴邪、下气暖中、消谷化积、破恶血、攻冷积。",
     	"葱的主要营养成分是蛋白质、糖类、维生素A原(主要在绿色葱叶中含有)、食物纤维以及磷、铁、镁等矿物质等。中医记载：葱能发汗解表，散寒通阳，解毒散凝。主治风寒感冒轻症，痈肿疮毒，痢疾脉微，寒凝腹痛，小便不利等病症。",
     	"萝卜含有丰富的碳水化合物、纤维素、维生素C及钙、磷、钾、铁等矿物质，营养丰富；萝卜还有促进新陈代谢，增进食欲的作用，对于治疗消化不良、胃脘胀满、咳嗽多痰、胸闷气喘、伤风感冒有一定的疗效。",
     	"芹菜含有蛋白质、脂肪、碳水化合物、纤维素、维生素、矿物质等营养成分。其中，维生素B、P的含量较多。矿物质元素钙、磷、铁的含量更是高于一般绿色蔬菜。",
     	"韭菜性温，味辛，具有补肾起阳作用，且含有大量维生素和粗纤维，能增进胃肠蠕动，治疗便秘。",
     	"菠菜含水量高（90%～93%），而热量低，是镁、铁、钾和维生素A的优质来源；也是钙和维生素C的上等来源。所含的磷、锌、叶酸和维生素B6也较为丰富。菠菜中含有的维生素C能够提高铁的吸收率，并促进铁与造血的叶酸共同作用，有效地预防贫血症。",
     	"莴苣含蛋白质、脂粉、碳水化合物、钙、磷、铁、胡萝卜素、及维生素B、维生素C、尤其含烟酸较多。莴苣味道清新且略带苦味，可刺激消化酶分泌，增进食欲。其乳状浆液，可增强胃液、消化腺的分泌和胆汁的分泌，从而促进消化。",
     	"具有滋阴凉血、和中润肠、清热化痰、解渴除烦、清热益气、利隔爽胃、利尿通便、解毒透疹、养肝明目、消食的功效，还可开胃健脾，宽肠利膈，通肠排便，开膈豁痰，消油腻，解酒毒。",
     	"番茄富含维生素C，其含有的番茄红素有利尿和抑制细菌生长的作用，是优良的抗氧化剂，多吃番茄具有抗衰老作用，使皮肤保持白皙。西红柿味酸微甘、性平，具有生津止渴，健胃消食，凉血平肝，清热解毒，降低血压之功效。",
     	"洋葱所含的微量元素硒是一种很强的抗氧化剂，能清除体内的自由基，增强细胞的活力和代谢能力，具有抗衰老的功效。",
     	"醋可以开胃，促进唾液和胃液的分泌，帮助消化吸收，使食欲旺盛，消食化积。醋还有很好的抑菌和杀菌作用，能有效预防肠道疾病、流行性感冒和呼吸疾病。",
     	"茶叶内含化合物500种左右，有些是人体必需的营养成分。如维生素类、蛋白质、氨基酸、类脂类、糖类及矿物质元素等，对人体有较高营养价值。有些是对人体有保健和药用价值的成分，如茶多酚、咖啡碱、脂多糖等，常喝茶有利于人体健康。",
     	"豆浆含有丰富的植物蛋白，磷脂，维生素B1、B2，烟酸和铁、钙等矿物质，尤其是钙的含量，虽不及豆腐高，但比其他任何乳类都丰富。豆浆是防治高血脂、高血压、动脉硬化等疾病的理想食品。",
     	"红糖中所含有的葡萄糖、果糖等多种单糖和多糖类能量物质，为人体提供能量。",
     	"蜂蜜含有70%以上的转化糖，能够被人体肠壁细胞直接吸收利用，没有必要经人体消化，经常服用蜂蜜，能帮助消化。",
     	"牛奶的营养成分很高，牛奶中的矿物质种类也非常丰富，除了我们所熟知的钙以外，磷、铁、锌、铜、锰、钼的含量都很多。牛奶是人体钙的最佳来源，而且钙磷比例非常适当，利于钙的吸收。牛奶中含组成人体蛋白质的氨基酸有20种，其中有8种是人体本身不能合成的，为必需氨基酸，牛奶是人类最理想的天然食品。",
     	"白酒主要由水乙醇和少量微量元素组成。夜晚服用少量的白酒，可平缓的促进血液循环，起到催眠作用。饮少量白酒可刺激胃液分泌与唾液分泌，因而起到健胃和止疼痛、利小便及驱虫的作用。白酒味苦、甘、辛，性温，有毒，入心、肝、肺、胃经。",
     	"啤酒营养丰富，其中含有丰富的氨基酸，是原料大麦含有的蛋白质经过酶的作用分解而产生的。且啤酒中还含有多种维生素。适量饮啤酒有强心、利尿、健胃的功效。"};
	 private static final String []efood={"黄莲","荞麦","雀肉","豆芽","何首乌","地黄","黄豆","海带","醋"," 红豆 ","半夏","南瓜",
				"橄榄","板粟","韭菜 ", "鲇鱼","鳗鱼","柿子", "狗肉","鲤鱼","柑橘", "鲤鱼"," 绿豆", "鳖", "鲤鱼", "金针菇",
				"豆浆","兔肉", "甘草", "麦冬", "荞麦面 ", "富含维生素C的食物", "红枣","黄豆",
				"梨","柿子","茄子","花生仁 ","石榴 ","香瓜","芹菜","蜂蜜","西红柿","芹菜 ","鸭肉","香瓜","木耳","牛肉","蚕豆","玉米",
				"地黄 何首乌 白术", "枣", "橘子","木耳", "黄瓜","蚬,蛤,蟹", "牛肉", "豆腐","鳝鱼","黄瓜", "蜂蜜",
				"糖浆", "白酒", "蜂蜜", "胡萝卜", "酒", "蜂蜜", "竹笋", "皮蛋", "豆腐","韭菜",
				"钙片","果汁","药物","韭菜","柠檬", "胡萝卜","核桃","啤酒","红薯", "海鲜"};
	 private static final String [] efoodinfo={"猪肉多脂，酸寒滑腻。若中药配方中以黄莲为主时，应忌食猪肉，不然会降低药效，且容易引起腹泻。",
		        "《食疗本草》 记载：荞麦难消，动热风，不宜多食。由于荞麦面气味甘平而寒，猪肝多脂，两者都是不易消化之物，所以不宜同食。",
		        "同食会消化不良。","猪肝中的铜会加速豆芽中的维生素C氧化，失去其营养价值，因此猪肝不宜与豆芽、西红柿、山楂等富含维生素C的食物同食。",
		        "《本草纲目》记载：地黄、何首乌忌一切血、葱、蒜、萝卜，何首乌忌铁器。从药物化学理解之，何首乌所含有机酸中，亦有鞣酸存在。鞣酸遇铁则形成不易溶解之物质，且影响其他有效成分的吸收。而动物学中，皆含铁质，故何首乌亦忌诸血。",
		        "《本草纲目》记载：地黄、何首乌忌诸血、葱、蒜、萝卜，服地黄、何首乌诸血忌食之，云能损阳也。",
		        "同食容易消化不良。","同食会便秘。","《本草纲目》记载：羊肉同醋食伤人心。羊肉大热，醋性甘温，与酒性相近，两物同煮，易生火动血。",
		        "红豆不宜与羊肉同食。","会影响营养成分吸收。","《本草纲目》记载：南瓜不可与羊肉同食，令人气壅。南瓜补中益气，羊肉大热补虚，两补同进，会导致胸闷腹胀等症状。",
		        "同食会引起身体不适。","同食会引起身体不适。","易引起气血不顺，发热动火。","《本草图经》记载：鲇鱼肉不可合牛肝食，令人患风，噎涎。同食会引起身体不适。",
		        "鳗鱼所含的某些生物活性物质，对人体产生一定的不良作用。牛肝营养丰富，所含生物活性物质极为复杂，二者同食更易产生不利于人体的生化反应。",
		        "容易引起中毒。","两者同食，温热助火作用更强，不利健康。","两者同食，温热助火作用更强，不利健康。","柑橘味甘酸、性温，兔肉味酸性冷，两者同食会引起胃肠功能絮乱，导致腹泻。",
		        "二者的性味及营养功能不同，不宜共食。《金匮要略》记载：鲤鱼不可合犬肉食之。《饮膳正要》记载：鲤鱼不可与犬肉同食。","狗肉属于大热，有小毒，绿豆则属于大寒，能解毒，两种东西混在一起吃，容易引起身体不适。",
		        "《饮膳正要》记载：鸭肉不可与鳖肉同食。《本草纲目》记载：鳖肉甘平无毒，鳖甲咸平。鳖性冷，发水病，而鸭肉也属凉性，所以鸭肉不宜与鳖肉同食。","鸡肉甘温，鲤鱼甘平。鸡肉补中助阳，鲤鱼下气利水，性味不反但功能相乘。因此鸡肉不可与鲤鱼一起食用。",
		        "同食会引起心痛。","豆浆中含有胰蛋白酶，与鸡蛋清中的卵松蛋白相结合，会造成营养成分的损失，降低二者的营养价值。","《本草纲目》中说：鸡蛋同兔肉食成泄痢，因此两者同食容易引起腹泻。","同食会中毒。",
		        "麦冬养阴生津，鲤鱼利水消肿，两者功能不协调，同食损害健康。","食疗本草 记载：荞麦难消，动热风，不宜多食。由于荞麦面气味甘平而寒，黄鱼多脂，两者都是不易消化之物，所以不宜同食。","大量同食会生成有毒化学物，引起中毒。","同食会中毒。",
		        "同食会影响消化。","《名医别录》记载：梨性冷利，多食损人，因此梨性寒冷，蟹亦冷利，两者同食，伤肠胃。","《饮膳正要》记载：柿梨不可与蟹同食。柿中含鞣酸，蟹内富含蛋白，二者相遇，凝固为鞣酸蛋白，不易消化且妨碍消化功能。",
		        "蟹肉性寒，茄子甘寒滑利，这两者的食物药性同属寒性。一起吃用，肠胃会不舒服，严重的可能导致腹泻，特别是脾胃虚寒的人更应忌食。","花生仁性味甘平，与螃蟹同食食用易导致腹泻。","刺激肠胃，出现腹痛、恶心、呕吐等症状。",
		        "易导致腹泻。","同食会引起蛋白质的吸收受阻。","同食会中毒。","易引起腹泻。","同食易引起腹泻。","猪、兔、鸭之肉都属寒性，而鳖也属寒性，所以不宜配食。","田螺大寒，香瓜冷利，并有轻度导泻作用，二者皆属凉性，同食有损肠胃。所以食用田螺后不宜马上吃香瓜，更不宜同食。",
		        "寒性的田螺，遇上滑利的木耳，不利于消化，所以二者不宜同食。","不易消化，容易腹胀。","同食会肠绞痛。","同食容易中毒。","同食会影响营养成分的吸收。","同食会引起腹泻。","《本草纲目》、《饮膳正要》记载：有苍白术，忌食菘、葫荽、蒜，即苍术、白术不宜与白菜，大蒜等同食。",
		        "辛热助火。","萝卜会产生一种抗甲状腺的物质硫氰酸，如果同时食用大量的橘子、苹果、葡萄等水果，水果中的类黄酮物质在肠道经细菌分解后就会转化为抑制甲状腺作用的硫氰酸，进而诱发甲状腺肿大。","易诱发过敏性皮炎。","黄瓜中含有维生素C分解酶，两者同食，芹菜中的维生素C将会被分解破坏，降低营养价值。",
		        "蚬、蛤、蟹等体内皆含维生素B1分解酶，若与芹菜同食，可将其中的维生素B1破坏，降低营养价值。","《本草纲目》记载：牛肉合韭薤食，令人热病。牛肉甘温，补气助火；韭菜、薤、生姜等食物皆大辛大温之品。如将牛肉配以韭菜、薤、生姜等大辛大温的食物烹调食用，容易上火。",
		        "菠菜中的草酸与豆腐中的钙形成草酸钙，使钙质无法吸收。","同食易导致腹泻。","黄瓜中含有维生素C分解酶，与菠菜同食，会破坏菠菜中的维生素C。","同食易导致腹泻。","同食会引起中毒。","同食会感觉胸闷、气短。","同食伤眼睛，引起眼睛不适。",
		        "胡萝卜素在胡萝卜中含量较高，其中最具有维生素A生物活性的是β-胡萝卜素，但其在人类肠道中的吸收利用率很低，大约仅为维生素A的六分之一，其他胡萝卜素的吸收率更低， 而且遇酸会分解掉，因此胡萝卜不宜与醋一起食用。","喝酒后在酒精的作用下，人体处于酒精中毒的兴奋期，交感神经系统兴奋，心率加快，血压上升。而浓茶中的主要成分茶碱和咖啡因也可以兴奋人的交感神经系统。此时茶酒结合，可以使交感神经系统更加兴奋，对于有高血压、冠心病的人来说，就有可能使病情加重，甚至可能诱发中风或心肌梗塞。",
		        "豆浆中的蛋白质与蜂蜜容易产生变性沉淀，不能被人体吸收。","红糖甘温，竹笋甘寒，食物药性稍有抵触。竹笋蛋白中含有18种左右的氨基酸，其中的赖氨酸在与糖共同加热的过程中，易形成赖氨酸糖基，对人体不利。","《本草纲目》记载：皮蛋忌红糖，同食发呕。","豆腐能清热散血，下大肠浊气，蜂蜜甘凉滑利，两物同食，容易导致腹泻。",
		        "易导致腹泻。","牛奶中已经含有充足的钙，再与钙同服，不利于钙的吸收。","容易引起消化不良或腹泻。","牛奶中的钙、磷、铁容易和中药中的有机物质发生化学反应,生成难溶并稳定的化合物,使牛奶和药物的有效成分受到破坏。","牛奶与含草酸多的蔬菜混合食用，会影响钙的吸收。","牛奶和酸性水果不能一起吃。牛奶中的蛋白质８０％以上为酪蛋白，如在酸性情况下，酪蛋白易凝集，导致消化不良或腹泻。",
		        "胡萝卜含有丰富的胡萝卜素，与酒精一起进入人体，就会在肝脏中产生毒素，从而损害肝脏功能。","易导致血热，严重会鼻出血。","同食会使胃酸分泌减少，导致胃痉挛等症状，对心脑血管危害大。","同食易患结石。","食用海鲜时饮用大量啤酒会产生过多的尿酸从而引发痛风。尿酸过多便会沉积在关节和软组织中，进而引起关节和组织发。",""};       
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list);
		
		// listView=(ListView)findViewById(R.id.listview01);
		List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < resId.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ImageView01", resId[i]);
			map.put("TextView01", food[i]);
			map.put("TextView02", food1[i]);
            lists.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, lists,
				R.layout.list_view_row, new String[] { "ImageView01",
						"TextView01", "TextView02" }, new int[] {
						R.id.ImageView01, R.id.TextView01, R.id.TextView02 });
		setListAdapter(adapter);
        
		listView=this.getListView();
        listView.setOnItemClickListener(this);
		
//          listView=this.getListView();
//          listView.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(FoodListView.this, FoodInfo.class);
//				intent.putExtra("food1", food1[arg2]);
//				intent.putExtra("resId", resId[arg2]);
//				intent.putExtra("food", food[arg2]);
//				startActivity(intent);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
	}
//	new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view,
//				int position, long id) {
//			// TODO Auto-generated method stub
//			Intent intent=new Intent(FoodListView.this, FoodInfo.class);
////			List<Map<String, Object>> value=new ArrayList<Map<String, Object>>();
////			
////			Map<String, Object> map=new HashMap<String, Object>();
////			map.put("food1", food[position]);
////			map.put("resId", resId[position]);
////			map.put("food", food[position]);
////			value.add(map);
//			//intent.putCharSequenceArrayListExtra(name, value)
//			//intent.
//			//startActivity(intent);
//			setContentView(R.layout.foodinfo);
//		}
//	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> data=new ArrayList<Map<String,Object>>();
			for(int j=0;j<efood.length;j++){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put(efood[j], efoodinfo[j]);
			data.add(map);
			}
		Intent intent=new Intent();
		intent.setClass(this, FoodInfo.class);
		intent.putExtra("drawable", resId[position]);
		intent.putExtra("foodname", food[position]);
		intent.putExtra("efoodnema", food1[position]);
		intent.putExtra("foodinfo", foodjianjie[position]);
		//intent.putExtra("data", data);
//		intent.putCharSequenceArrayListExtra(name, value)("efood", data);
		
		startActivity(intent);
	}
}
