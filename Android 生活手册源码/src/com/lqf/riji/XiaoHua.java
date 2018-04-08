package com.lqf.riji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lqf.gerenriji.R;

public class XiaoHua extends Activity {
	private TextView textView;
	// 定义所需要的控件
	private ListView listView2;
	// 定义一个键值对数组
	private List<Map<String, Object>> xiaohua;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaohua);		
		// 调用XiaoHua方法获取内容
		XiaoHua();
		// 获取所需要的控件
		listView2 = (ListView) findViewById(R.id.listview2);
		// 创建ListView适配器
		SimpleAdapter adapter = new SimpleAdapter(this, xiaohua,
				R.layout.xiaohua, new String[] { "序号", "内容" }, new int[] {
						R.id.textxuhao, R.id.textxiaohua });
		listView2.setAdapter(adapter);
		listView2.setOnCreateContextMenuListener(menuListener);
	}

	// 定义一个数据源
	public void XiaoHua() {
		xiaohua = new ArrayList<Map<String, Object>>();
		Map<String, Object> itesm;

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 1);
		itesm.put("内容", " 美术课下课，同学们把作业交给老师。一名学生："
				+ "老师，把我的作业放在最上面吧！老师：为什么？同学：我画的" + "是鸡蛋，怕把它压碎了。");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 2);
		itesm.put("内容", " 有一天，有一只兔子不幸掉进了一个箱子，结果出来的时候"
				+ "它变成了一只鸭子。知道这是为什么吗？因为那个箱子里，放着的是变“压”器。");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 3);
		itesm.put("内容", " 一哥们儿巨思念前女友，多年未见却很想复合。拨通电话后，那边"
				+ "慌忙挂下电话，说一会儿再打。过了两个小时，那边电话回过来了，前女友说：不好"
				+ "意思，刚刚在生孩子哥们儿一口鲜血差点喷出来...");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 4);
		itesm.put("内容", " 和老婆炒架，她非常伤心、痛苦流涕，我束手无策啊但四岁的女儿突然"
				+ "说了一句话我们都笑疯了：妈妈，你别哭了，老公还不是你自己找的，怪谁...");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 5);
		itesm.put("内容", " 一男子新婚第二天就上班了，上班时闷闷不乐。朋友问其故，男子"
				+ "说：以前嫖惯了，昨晚和我媳妇完事后我随手扔给她100块。 友释然道：你给她钱这"
				+ "也没什么啊！男子懊恼道：问题是她顺手找回我20...");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 6);
		itesm.put("内容", " 从前，有根火柴不爱洗头、有一次，它在路上走呀走，感觉头很痒。它就"
				+ "挠呀挠，用力过猛，把头挠着火了。它被及时送到了医院，从此世界上就有了第一根棉签..");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 7);
		itesm.put("内容", " 小明：“你说一个月打三次胎会不会有事？”小强：“你女朋友？！”" + "小明：“不是。是我自行车。”");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 8);
		itesm.put("内容", " 小名听完生物老师讲课“三代内亲人之间不能结婚，直系血亲就更不能了”大"
				+ "受打击，拨通了举报电话说：“我们家有三对儿血亲结婚。”次日警察来到小名家：“你们家哪"
				+ "些人是近亲结婚啊？”小名指着爸爸妈妈、爷爷奶奶、外公外婆道：“就是他们。”警察目瞪：“竟"
				+ "这么猖狂？不过不太对啊！”小名：“这还有什么问题啊，爸爸妈妈、爷爷奶奶、外公外婆都是我"
				+ "的血亲阿，他们怎么能结婚呢？”");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 9);
		itesm.put("内容", " 一个记者问包拯最难忘的经历是什么？包拯痛苦地回答：有一次，我不小心掉到了煤"
				+ "矿井里。记者：发生了什么值得你难忘的事吗？包拯：尼玛，我掉井里才发现，我居然能和周围的煤合体！");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 10);
		itesm.put("内容", " 从前从前有一只鸟,他每天都会经过一片玉米田,但是很不幸的,有一天那片玉米田发生了火"
				+ "灾,所有的玉米都变成了爆米花!!!小鸟飞过去以后……以为下雪,就冷死了……");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 11);
		itesm.put("内容", " 一天，豆沙包在马路上走着，突然出了车祸，肚皮被撞破了，临死前，他看了看自己的肚"
				+ "子说：“哦，原来我是豆沙包。”");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 12);
		itesm.put("内容", " 一个糖,在北极走着走着,觉得他好冷~~于是就变成了冰糖");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 13);
		itesm.put("内容", " 一大学生被敌人抓了。敌人把他绑在了电线杆上，然后问他：说，你是哪里的？不说就电死你！大"
				+ "学生回了敌人一句话，结果被电死了，他说：我是电大的！");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 14);
		itesm.put("内容", " 传说中，有一个杀手，心是冷的，剑是冷的，手也是冷的！于是……他冻死了！！！");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("序号", 15);
		itesm.put("内容", " 有一个农夫带着一匹马和一只狗上山打猎,走了一天都打不到,但农夫还是继续走.突然"
				+ "那匹马说;“都走了一天了.还走想累死我吗?“农夫和猎狗吓撒腿就跑,他们跑到一棵树下,猎狗拍着"
				+ "胸脯说“吓死我了,马会说话“结果农夫被吓死了.");
		xiaohua.add(itesm);
	}

	// 该方法用于显示上下文菜单
	OnCreateContextMenuListener menuListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// 添加上下文菜单
			menu.add(Menu.NONE, 0, 3, "发送");
			menu.add(Menu.NONE, 1, 3, "取消");
			// 添加上下文菜单标题
			menu.setHeaderTitle("操作选项");
			// 添加上下文菜单标题图片
			menu.setHeaderIcon(android.R.drawable.ic_popup_sync);
		}
	};
	//点击上下文菜单所触发的事件
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Uri uri = Uri.parse("smsto:");
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);//这里是一个视图View,不是Sendto
			intent.putExtra("address", "13662261401");//电话号码，这行去掉的话，默认就没有电话
			textView = (TextView) findViewById(R.id.textxiaohua);//获取文本框
			String sss = textView.getText().toString();//获取文本框内容
			intent.putExtra("sms_body",sss);//传值信息
			intent.setType("vnd.android-dir/mms-sms");//设置intent类型
			startActivity(intent);
			break;
		case 1:
			break;
		}
		return super.onContextItemSelected(item);
	}

}
