package com.kang.database;
//Download by http://www.codefans.net
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kang.meituan.meituan;

public class DataIUDS
{
	private tuangouData data;

	public DataIUDS(Context context)
	{
		this.data = new tuangouData(context);

	}

	public void save(meituan mei) throws Exception
	{

		SQLiteDatabase db = data.getWritableDatabase(); // 得到用于数据库的实例

		db.execSQL(
				"insert into tuan ( url, website, deal_id, city_name, deal_title, deal_img, deal_desc, price, value, rebate, sales_num, start_time, end_time, shop_name, shop_addr, shop_area, shop_tel) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] {mei.getUrl(), mei.getWebsite(), mei.getDeal_id(),
						mei.getCity_name(), mei.getDeal_title(),
						mei.getDeal_img(), mei.getDeal_desc(), mei.getPrice(),
						mei.getValue(), mei.getRebate(), mei.getSales_num(),
						mei.getStart_time(), mei.getEnd_time(),
						mei.getShop_name(), mei.getShop_addr(),
						mei.getShop_area(), mei.getShop_tel() });

		db.close();
	}

	// 删除数据
	public void delete(int id)
	{
		SQLiteDatabase db = data.getWritableDatabase(); // 得到用于数据的数据库实例
		db.execSQL("delete from tuan where _id = ?",
				new Object[] { String.valueOf(id) });
		db.close();
	}
	
	public void deleteAllData(){
		SQLiteDatabase db = data.getWritableDatabase(); // 得到用于数据的数据库实例
		db.execSQL("delete from tuan");
		db.close();
	}

	// 查找单一数据
	public meituan find(int id) throws Exception
	{
		SQLiteDatabase db = data.getReadableDatabase(); // 得到用于数据的数据库实例
		Cursor cursor = db.rawQuery("select * from tuan where _id = ?",
				new String[] { String.valueOf(id) });
		if (cursor.moveToFirst())
		{
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String website = cursor.getString(cursor.getColumnIndex("website"));
			String deal_id = cursor.getString(cursor.getColumnIndex("deal_id"));
			String city_name = cursor.getString(cursor
					.getColumnIndex("city_name"));
			String deal_title = cursor.getString(cursor
					.getColumnIndex("deal_title"));
			String deal_img = cursor.getString(cursor
					.getColumnIndex("deal_img"));
			String deal_desc = cursor.getString(cursor
					.getColumnIndex("deal_desc"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String value = cursor.getString(cursor.getColumnIndex("value"));
			String rebate = cursor.getString(cursor.getColumnIndex("rebate"));
			String sales_num = cursor.getString(cursor
					.getColumnIndex("sales_num"));
			long start_time = cursor.getLong(cursor
					.getColumnIndex("start_time"));
			long end_time = cursor.getLong(cursor.getColumnIndex("end_time"));
			String shop_name = cursor.getString(cursor
					.getColumnIndex("shop_name"));
			String shop_addr = cursor.getString(cursor
					.getColumnIndex("shop_addr"));
			String shop_area = cursor.getString(cursor
					.getColumnIndex("shop_area"));
			String shop_tel = cursor.getString(cursor
					.getColumnIndex("shop_tel"));

			meituan mei = new meituan();
			mei.setId(_id);
			mei.setUrl(url);
			mei.setWebsite(website);
			mei.setDeal_id(deal_id);
			mei.setCity_name(city_name);
			mei.setDeal_title(deal_title);
			mei.setDeal_img(deal_img);
			mei.setDeal_desc(deal_desc);
			mei.setPrice(price);
			mei.setValue(value);
			mei.setRebate(rebate);
			mei.setSales_num(sales_num);
			mei.setStart_time(start_time);
			mei.setEnd_time(end_time);
			mei.setShop_name(shop_name);
			mei.setShop_addr(shop_addr);
			mei.setShop_area(shop_area);
			mei.setShop_tel(shop_tel);

			return mei;
		}
		
		cursor.close();

		db.close();

		return null;

	}

	// 根据ID来更新数据
	public void update(meituan mei, int id)
	{
		SQLiteDatabase db = data.getWritableDatabase();
		db.execSQL(
				"update tuan set website=?, deal_id=?, city_name=?, deal_title=?, deal_img=?, deal_desc=?, price=?, value=?, rebate =?, sales_num=?, start_time=?, end_time=?, shop_name=?, shop_addr=?, shop_area=?, shop_tel=? where _id=?",
				new Object[] { mei.getWebsite(), mei.getDeal_id(),
						mei.getCity_name(), mei.getDeal_title(),
						mei.getDeal_img(), mei.getDeal_desc(), mei.getPrice(),
						mei.getValue(), mei.getRebate(), mei.getSales_num(),
						mei.getStart_time(), mei.getEnd_time(),
						mei.getShop_name(), mei.getShop_addr(),
						mei.getShop_area(), mei.getShop_tel(),
						String.valueOf(id) });

		db.close();

	}

	public int getCount()
	{
		SQLiteDatabase db = data.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from tuan", null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		return count;
	}

	public List<meituan> getListData(int offset, int maxResult)
	{
		SQLiteDatabase db = data.getReadableDatabase();
		List<meituan> meituans = new ArrayList<meituan>();
		Cursor cursor = db.rawQuery(
				"select * from tuan order by _id desc limit ? , ?",
				new String[] { String.valueOf(offset),
						String.valueOf(maxResult) });

		while (cursor.moveToNext())
		{
			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String website = cursor.getString(cursor.getColumnIndex("website"));
			String deal_id = cursor.getString(cursor.getColumnIndex("deal_id"));
			String city_name = cursor.getString(cursor
					.getColumnIndex("city_name"));
			String deal_title = cursor.getString(cursor
					.getColumnIndex("deal_title"));
			String deal_img = cursor.getString(cursor
					.getColumnIndex("deal_img"));
			String deal_desc = cursor.getString(cursor
					.getColumnIndex("deal_desc"));
			String price = cursor.getString(cursor.getColumnIndex("price"));
			String value = cursor.getString(cursor.getColumnIndex("value"));
			String rebate = cursor.getString(cursor.getColumnIndex("rebate"));
			String sales_num = cursor.getString(cursor
					.getColumnIndex("sales_num"));
			long start_time = cursor.getLong(cursor
					.getColumnIndex("start_time"));
			long end_time = cursor.getLong(cursor.getColumnIndex("end_time"));
			String shop_name = cursor.getString(cursor
					.getColumnIndex("shop_name"));
			String shop_addr = cursor.getString(cursor
					.getColumnIndex("shop_addr"));
			String shop_area = cursor.getString(cursor
					.getColumnIndex("shop_area"));
			String shop_tel = cursor.getString(cursor
					.getColumnIndex("shop_tel"));

			meituan mei = new meituan();
			mei.setId(id);
			mei.setUrl(url);
			mei.setWebsite(website);
			mei.setDeal_id(deal_id);
			mei.setCity_name(city_name);
			mei.setDeal_title(deal_title);
			mei.setDeal_img(deal_img);
			mei.setDeal_desc(deal_desc);
			mei.setPrice(price);
			mei.setValue(value);
			mei.setRebate(rebate);
			mei.setSales_num(sales_num);
			mei.setStart_time(start_time);
			mei.setEnd_time(end_time);
			mei.setShop_name(shop_name);
			mei.setShop_addr(shop_addr);
			mei.setShop_area(shop_area);
			mei.setShop_tel(shop_tel);

			meituans.add(mei);

		}

		cursor.close();

		return meituans;
	}

}
