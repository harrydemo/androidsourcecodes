/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.alipay.android.appDemo4;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * demo展示商品列表填充器
 * 
 */
public class ProductListAdapter extends BaseAdapter {

	private ArrayList<Products.ProductDetail> m_productList = null;
	private Context context;

	private class ProductItemView {
		TextView subject;
		TextView body;
		TextView price;
	}

	public ProductListAdapter(Context c, ArrayList<Products.ProductDetail> list) {
		m_productList = list;
		context = c;
	}

	public int getCount() {
		return m_productList.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ProductItemView itemView;
		if (convertView == null) {
			itemView = new ProductItemView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.product_item, null);
			itemView.subject = (TextView) convertView
					.findViewById(R.id.subject);
			itemView.body = (TextView) convertView.findViewById(R.id.body);
			itemView.price = (TextView) convertView.findViewById(R.id.price);

			convertView.setTag(itemView);
		} else {
			itemView = (ProductItemView) convertView.getTag();
		}

		itemView.subject.setText(this.m_productList.get(position).subject);
		itemView.body.setText(this.m_productList.get(position).body);
		itemView.price.setText(this.m_productList.get(position).price);

		return convertView;
	}
}
