/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.alipay.android.appDemo4;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 模拟商户应用的商品列表，交易步骤。
 * 
 * 1. 将商户ID，收款帐号，外部订单号，商品名称，商品介绍，价格，通知地址封装成订单信息 2. 对订单信息进行签名 3.
 * 将订单信息，签名，签名方式封装成请求参数 4. 调用pay方法
 * 
 * @version v4_0413 2012-03-02
 */
public class AlixDemo extends Activity implements OnItemClickListener,
		OnItemLongClickListener {
	static String TAG = "AppDemo4";

	//
	// 模拟商户商品列表
	ListView mproductListView = null;
	ProductListAdapter m_listViewAdapter = null;
	ArrayList<Products.ProductDetail> mproductlist;

	private ProgressDialog mProgress = null;

	//
	// Called when the activity is first created.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");

		//
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否被安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		mspHelper.detectMobile_sp();

		//
		setContentView(R.layout.remote_service_binding);

		//
		// set title
		// 设置界面标题
		TextView mTitleName = (TextView) findViewById(R.id.AlipayTitleItemName);
		mTitleName.setText(getString(R.string.app_name));

		//
		// retrieve and show the product list.
		// 显示商品列表
		initProductList();
	}

	/**
	 * retrieve the product list. 
	 * 设置商品列表
	 */
	void initProductList() {
		Products products = new Products();
		this.mproductlist = products.retrieveProductInfo();

		mproductListView = (ListView) findViewById(R.id.ProductListView);
		m_listViewAdapter = new ProductListAdapter(this, this.mproductlist);
		mproductListView.setAdapter(m_listViewAdapter);
		mproductListView.setOnItemClickListener(this);
		mproductListView.setOnItemLongClickListener(this);
	}

	/**
	 * get the selected order info for pay. 获取商品订单信息
	 * 
	 * @param position
	 *            商品在列表中的位置
	 * @return
	 */
	String getOrderInfo(int position) {
		String strOrderInfo = "partner=" + "\"" + PartnerConfig.PARTNER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "seller=" + "\"" + PartnerConfig.SELLER + "\"";
		strOrderInfo += "&";
		strOrderInfo += "out_trade_no=" + "\"" + getOutTradeNo() + "\"";
		strOrderInfo += "&";
		strOrderInfo += "subject=" + "\"" + mproductlist.get(position).subject
				+ "\"";
		strOrderInfo += "&";
		strOrderInfo += "body=" + "\"" + mproductlist.get(position).body + "\"";
		strOrderInfo += "&";
		strOrderInfo += "total_fee=" + "\""
				+ mproductlist.get(position).price.replace("一口价:", "") + "\"";
		strOrderInfo += "&";
		strOrderInfo += "notify_url=" + "\""
				+ "http://notify.java.jpxx.org/index.jsp" + "\"";

		return strOrderInfo;
	}

	/**
	 * get the out_trade_no for an order.
	 * 获取外部订单号
	 * 
	 * @return
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}

	//
	//
	/**
	 *  sign the order info.
	 *  对订单信息进行签名
	 *  
	 * @param signType	签名方式 
	 * @param content		待签名订单信息
	 * @return
	 */
	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use.
	 * 获取签名方式
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}

	/**
	 * get the char set we use.
	 * 获取字符集
	 * 
	 * @return
	 */
	String getCharset() {
		String charset = "charset=" + "\"" + "utf-8" + "\"";
		return charset;
	}

	/**
	 * the onItemClick for the list view of the products.
	 * 商品列表商品被点击事件
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//
		// check to see if the MobileSecurePay is already installed.
		// 检测安全支付服务是否安装
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if (!isMobile_spExist)
			return;

		// check some info.
		// 检测配置信息
		if (!checkInfo()) {
			BaseHelper
					.showDialog(
							AlixDemo.this,
							"提示",
							"缺少partner或者seller，请在src/com/alipay/android/appDemo4/PartnerConfig.java中增加。",
							R.drawable.infoicon);
			return;
		}

		// start pay for this order.
		// 根据订单信息开始进行支付
		try {
			// prepare the order info.
			// 准备订单信息
			String orderInfo = getOrderInfo(position);
			// 这里根据签名方式对订单信息进行签名
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			// 对签名进行编码
			strsign = URLEncoder.encode(strsign);
			// 组装好参数
			String info = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&"
					+ getSignType();
			// start the pay.
			// 调用pay方法进行支付
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(info, mHandler, AlixId.RQF_PAY, this);

			if (bRet) {
				// show the progress bar to indicate that we have started
				// paying.
				// 显示“正在支付”进度条
				closeProgress();
				mProgress = BaseHelper.showProgress(this, null, "正在支付", false,
						true);
			} else
				;
		} catch (Exception ex) {
			Toast.makeText(AlixDemo.this, R.string.remote_call_failed,
					Toast.LENGTH_SHORT).show();
		}
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		return false;
	}

	/**
	 * check some info.the partner,seller etc.
	 * 检测配置信息
	 * partnerid商户id，seller收款帐号不能为空
	 * 
	 * @return
	 */
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}

	//
	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				switch (msg.what) {
				case AlixId.RQF_PAY: {
					//
					closeProgress();

					BaseHelper.log(TAG, strRet);

					// 从通知中获取参数
					try {
						// 获取交易状态，具体状态代码请参看文档
						String memo = "memo=";
						int imemoStart = strRet.indexOf("memo=");
						imemoStart += memo.length();
						int imemoEnd = strRet.indexOf(";result=");
						memo = strRet.substring(imemoStart, imemoEnd);
						// 对通知进行验签
						ResultChecker resultChecker = new ResultChecker(strRet);

						int retVal = resultChecker.checkSign();
						// 返回验签结果以及交易状态
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							BaseHelper.showDialog(
									AlixDemo.this,
									"提示",
									getResources().getString(
											R.string.check_sign_failed),
									android.R.drawable.ic_dialog_alert);
						} else {
							BaseHelper.showDialog(AlixDemo.this, "提示", memo,
									R.drawable.infoicon);
						}

					} catch (Exception e) {
						e.printStackTrace();

						BaseHelper.showDialog(AlixDemo.this, "提示", strRet,
								R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	//
	// 
	/**
	 * the OnCancelListener for lephone platform.
	 *	lephone系统使用到的取消dialog监听
	 */
	static class AlixOnCancelListener implements
			DialogInterface.OnCancelListener {
		Activity mcontext;

		AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

	//
	// close the progress bar
	// 关闭进度框
	void closeProgress() {
		try {
			if (mProgress != null) {
				mProgress.dismiss();
				mProgress = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回键监听事件
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BaseHelper.log(TAG, "onKeyDown back");

			this.finish();
			return true;
		}

		return false;
	}

	//
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.v(TAG, "onDestroy");

		try {
			mProgress.dismiss();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}