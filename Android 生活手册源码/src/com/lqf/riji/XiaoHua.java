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
	// ��������Ҫ�Ŀؼ�
	private ListView listView2;
	// ����һ����ֵ������
	private List<Map<String, Object>> xiaohua;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xiaohua);		
		// ����XiaoHua������ȡ����
		XiaoHua();
		// ��ȡ����Ҫ�Ŀؼ�
		listView2 = (ListView) findViewById(R.id.listview2);
		// ����ListView������
		SimpleAdapter adapter = new SimpleAdapter(this, xiaohua,
				R.layout.xiaohua, new String[] { "���", "����" }, new int[] {
						R.id.textxuhao, R.id.textxiaohua });
		listView2.setAdapter(adapter);
		listView2.setOnCreateContextMenuListener(menuListener);
	}

	// ����һ������Դ
	public void XiaoHua() {
		xiaohua = new ArrayList<Map<String, Object>>();
		Map<String, Object> itesm;

		itesm = new HashMap<String, Object>();
		itesm.put("���", 1);
		itesm.put("����", " �������¿Σ�ͬѧ�ǰ���ҵ������ʦ��һ��ѧ����"
				+ "��ʦ�����ҵ���ҵ����������ɣ���ʦ��Ϊʲô��ͬѧ���һ���" + "�Ǽ������°���ѹ���ˡ�");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 2);
		itesm.put("����", " ��һ�죬��һֻ���Ӳ��ҵ�����һ�����ӣ����������ʱ��"
				+ "�������һֻѼ�ӡ�֪������Ϊʲô����Ϊ�Ǹ���������ŵ��Ǳ䡰ѹ������");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 3);
		itesm.put("����", " һ���Ƕ���˼��ǰŮ�ѣ�����δ��ȴ���븴�ϡ���ͨ�绰���Ǳ�"
				+ "��æ���µ绰��˵һ����ٴ򡣹�������Сʱ���Ǳߵ绰�ع����ˣ�ǰŮ��˵������"
				+ "��˼���ո��������Ӹ��Ƕ�һ����Ѫ��������...");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 4);
		itesm.put("����", " �����ų��ܣ����ǳ����ġ�ʹ�����飬�������޲߰��������Ů��ͻȻ"
				+ "˵��һ�仰���Ƕ�Ц���ˣ����裬�����ˣ��Ϲ����������Լ��ҵģ���˭...");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 5);
		itesm.put("����", " һ�����»�ڶ�����ϰ��ˣ��ϰ�ʱ���Ʋ��֡���������ʣ�����"
				+ "˵����ǰ�ι��ˣ��������ϱ�����º��������Ӹ���100�顣 ����Ȼ���������Ǯ��"
				+ "Ҳûʲô�������Ӱ��յ�����������˳���һ���20...");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 6);
		itesm.put("����", " ��ǰ���и���񲻰�ϴͷ����һ�Σ�����·����ѽ�ߣ��о�ͷ����������"
				+ "��ѽ�ӣ��������ͣ���ͷ���Ż��ˡ�������ʱ�͵���ҽԺ���Ӵ������Ͼ����˵�һ����ǩ..");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 7);
		itesm.put("����", " С��������˵һ���´�����̥�᲻�����£���Сǿ������Ů���ѣ�����" + "С���������ǡ��������г�����");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 8);
		itesm.put("����", " С������������ʦ���Ρ�����������֮�䲻�ܽ�飬ֱϵѪ�׾͸������ˡ���"
				+ "�ܴ������ͨ�˾ٱ��绰˵�������Ǽ������Զ�Ѫ�׽�顣�����վ�������С���ң������Ǽ���"
				+ "Щ���ǽ��׽�鰡����С��ָ�Űְ����衢үү���̡��⹫���ŵ������������ǡ�������Ŀ�ɣ�����"
				+ "��ô���񣿲�����̫�԰�����С�������⻹��ʲô���Ⱑ���ְ����衢үү���̡��⹫���Ŷ�����"
				+ "��Ѫ�װ���������ô�ܽ���أ���");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 9);
		itesm.put("����", " һ�������ʰ����������ľ�����ʲô������ʹ��ػش���һ�Σ��Ҳ�С�ĵ�����ú"
				+ "������ߣ�������ʲôֵ�������������𣿰��������꣬�ҵ�����ŷ��֣��Ҿ�Ȼ�ܺ���Χ��ú���壡");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 10);
		itesm.put("����", " ��ǰ��ǰ��һֻ��,��ÿ�춼�ᾭ��һƬ������,���Ǻܲ��ҵ�,��һ����Ƭ�����﷢���˻�"
				+ "��,���е����׶�����˱��׻�!!!С��ɹ�ȥ�Ժ󡭡���Ϊ��ѩ,�������ˡ���");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 11);
		itesm.put("����", " һ�죬��ɳ������·�����ţ�ͻȻ���˳�������Ƥ��ײ���ˣ�����ǰ�������˿��Լ��Ķ�"
				+ "��˵����Ŷ��ԭ�����Ƕ�ɳ������");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 12);
		itesm.put("����", " һ����,�ڱ�����������,����������~~���Ǿͱ���˱���");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 13);
		itesm.put("����", " һ��ѧ��������ץ�ˡ����˰��������˵��߸��ϣ�Ȼ��������˵����������ģ���˵�͵����㣡��"
				+ "ѧ�����˵���һ�仰������������ˣ���˵�����ǵ��ģ�");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 14);
		itesm.put("����", " ��˵�У���һ��ɱ�֣�������ģ�������ģ���Ҳ����ģ����ǡ����������ˣ�����");
		xiaohua.add(itesm);

		itesm = new HashMap<String, Object>();
		itesm.put("���", 15);
		itesm.put("����", " ��һ��ũ�����һƥ���һֻ����ɽ����,����һ�춼�򲻵�,��ũ���Ǽ�����.ͻȻ"
				+ "��ƥ��˵;��������һ����.��������������?��ũ����Թ������Ⱦ���,�����ܵ�һ������,�Թ�����"
				+ "�ظ�˵����������,���˵�������ũ��������.");
		xiaohua.add(itesm);
	}

	// �÷���������ʾ�����Ĳ˵�
	OnCreateContextMenuListener menuListener = new OnCreateContextMenuListener() {
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// ��������Ĳ˵�
			menu.add(Menu.NONE, 0, 3, "����");
			menu.add(Menu.NONE, 1, 3, "ȡ��");
			// ��������Ĳ˵�����
			menu.setHeaderTitle("����ѡ��");
			// ��������Ĳ˵�����ͼƬ
			menu.setHeaderIcon(android.R.drawable.ic_popup_sync);
		}
	};
	//��������Ĳ˵����������¼�
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Uri uri = Uri.parse("smsto:");
			Intent intent = new Intent(Intent.ACTION_VIEW,uri);//������һ����ͼView,����Sendto
			intent.putExtra("address", "13662261401");//�绰���룬����ȥ���Ļ���Ĭ�Ͼ�û�е绰
			textView = (TextView) findViewById(R.id.textxiaohua);//��ȡ�ı���
			String sss = textView.getText().toString();//��ȡ�ı�������
			intent.putExtra("sms_body",sss);//��ֵ��Ϣ
			intent.setType("vnd.android-dir/mms-sms");//����intent����
			startActivity(intent);
			break;
		case 1:
			break;
		}
		return super.onContextItemSelected(item);
	}

}
