package linpeng.ztb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class ListData {
	public List<Map<String, String>> getGridviewdata() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		HashMap<String, String> hash = new HashMap<String, String>();
		hash.put("grid_title", "�б�");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "̸��");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "ѯ��");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "����");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "����");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "��ǰ��ʾ");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "����");
		list.add(hash);
		return list;
	}

	public void getListData(Document doc, int table_number, Context context,
			String url) {
		try {
			Element ele = doc.select("td[height=500]").first();
			Elements eles = ele.select("a");
			int newsclass = 0;
			String table_name = new IntToStrong().getname(table_number);
			if (ele.text().length() > 1) {
				DataBaseHelper dbh = new DataBaseHelper(context, table_name,
						null, 1);
				Log.i("shanchu", "ca");
				dbh.dellAll();
			}
			if (table_number != 3 && table_number != 5) {
				for (Element ele2 : eles) {
					String inittext = ele2.text();
					String isend = "��������";
					String changetext = inittext.replace("�����ڱ�����", "");// �滻���еġ����ڱ�����

					String newsurl = ele2.attr("abs:href");
					if (changetext.length() != inittext.length()) {
						isend = "���ڱ���";
					} else {
						changetext = changetext.replace("������������", "");// �滻���еġ�����������
					}
					if (ele2.text().replace("������Ϣ", "").length() >= 2) {
						DataBaseHelper dbh = new DataBaseHelper(context,
								table_name, null, 1);
						SQLiteDatabase sqh = dbh.getWritableDatabase();
						dbh.addnewslist(newsclass, changetext, ele2.parent()
								.parent().select("td").last().text(), isend,
								newsurl);
						newsclass++;
					}
				}
			} else {
				for (Element ele2 : eles) {
					String inittext = ele2.text();
					String newsurl = ele2.attr("abs:href");
					String name = new IntToStrong().getname(table_number);
					if (ele2.text().replace("������Ϣ", "").length() >= 2) {
						DataBaseHelper dbh = new DataBaseHelper(context, name,
								null, 1);
						SQLiteDatabase sqh = dbh.getWritableDatabase();
						dbh.addnewslist(newsclass, inittext, ele2.parent()
								.parent().select("td").last().text(), "",
								newsurl);
						newsclass++;
					}
				}
			}
		} catch (Exception e) {
			Log.i("s", "���粻ͨ");
			// Toast.makeText(context, "���粻ͨ�����Ժ�����",Toast.LENGTH_SHORT).show();
		}
	}
}
