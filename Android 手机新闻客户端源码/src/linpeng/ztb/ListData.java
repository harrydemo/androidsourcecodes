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
		hash.put("grid_title", "招标");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "谈判");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "询价");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "答疑");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "县区");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "标前公示");
		list.add(hash);
		hash = new HashMap<String, String>();
		hash.put("grid_title", "巢湖");
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
					String isend = "报名结束";
					String changetext = inittext.replace("【正在报名】", "");// 替换文中的【正在报名】

					String newsurl = ele2.attr("abs:href");
					if (changetext.length() != inittext.length()) {
						isend = "正在报名";
					} else {
						changetext = changetext.replace("【报名结束】", "");// 替换文中的【报名结束】
					}
					if (ele2.text().replace("更多信息", "").length() >= 2) {
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
					if (ele2.text().replace("更多信息", "").length() >= 2) {
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
			Log.i("s", "网络不通");
			// Toast.makeText(context, "网络不通，请稍候再试",Toast.LENGTH_SHORT).show();
		}
	}
}
