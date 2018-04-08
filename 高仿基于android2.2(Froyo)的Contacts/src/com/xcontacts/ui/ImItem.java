package com.xcontacts.ui;

import com.xcontacts.ui.model.AbstractItem;
import com.xcontacts.utils.MyDialog;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

import android.content.Context;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;
import android.view.View;
import android.widget.LinearLayout;

public class ImItem extends AbstractItem {

	public ImItem(Context context) {
		super(context);
	}

	@Override
	public void initViews(LinearLayout container, int arrayRes, int hintRes) {
		this.mContainer = container;
		this.etContent.setHint(hintRes);
		this.mTypes = getResources().getStringArray(arrayRes);

		// 下面这四行使得用户添加一行数据时，数据的type会按顺序循环显示出来
		final int countOfTypes = this.mTypes.length;
		int typeMinusCustom = countOfTypes - 1;// 减去1是为了排除Custom
		int countOfChilds = container.getChildCount();
		int indexOfNextType = countOfChilds % typeMinusCustom;

		this.btnType.setText(mTypes[indexOfNextType]);
		getChanges().put(Im.PROTOCOL, getItemType(indexOfNextType));

		this.btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 对Im自定义时，实际上是对protocol进行自定义（CUSTOM_PROTOCOL保存自定义Protocol的名称）
				MyDialog.show(ImItem.this, btnType, mTypes, Im.PROTOCOL,
						Im.CUSTOM_PROTOCOL, getChanges());
			}
		});

		// 监听EditText中内容变化
		this.etContent.addTextChangedListener(new MyTextWachter(Im.DATA,
				getChanges()));

		// 点击，删除这一行.
		this.ibDele.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mContainer.removeView(mItemContainer);
				// 如果是编辑联系人时删除原来的信息，则应当在数据库中删除相关记录
				long dataId = getDataId();
				if (dataId > 0) {
					getContext().getContentResolver().delete(Data.CONTENT_URI,
							Data._ID + "=?",
							new String[] { String.valueOf(dataId) });
					MyLog.w("删除联系人的原有信息！删除条件是：Data._id = " + dataId);
				}
			}
		});
	}

	/**
	 * 根据Im的类型数组，判断用户选择的Im类型
	 * 
	 * @param postion
	 *            用户选择的类型在类型数组中的位置
	 * @return
	 */
	public int getItemType(int position) {
		int imType = 0;
		switch (position) {
		case 0:
			imType = Im.PROTOCOL_AIM;
			break;
		case 1:// windows live
			imType = Im.PROTOCOL_MSN;
			break;
		case 2:
			imType = Im.PROTOCOL_YAHOO;
			break;
		case 3:
			imType = Im.PROTOCOL_SKYPE;
			break;
		case 4:
			imType = Im.PROTOCOL_QQ;
			break;
		case 5:
			imType = Im.PROTOCOL_GOOGLE_TALK;
			break;
		case 6:
			imType = Im.PROTOCOL_ICQ;
			break;
		case 7:
			imType = Im.PROTOCOL_JABBER;
			break;
		// Im.PROTOCOL_NETMEETING与Im.PROTOCOL_CUSTOM
		case 8:// 特殊情况.用户自定义Custom_Protocol(即data6字段)
			imType = Im.PROTOCOL_CUSTOM;
			break;
		}
		return imType;
	}

	@Override
	public int getPositionBasedOnType(int type) {
		int position = 0;
		switch (type) {
		case Im.PROTOCOL_AIM:
			position = 0;
			break;
		case Im.PROTOCOL_MSN:
			position = 1;
			break;
		case Im.PROTOCOL_YAHOO:
			position = 2;
			break;
		case Im.PROTOCOL_SKYPE:
			position = 3;
			break;
		case Im.PROTOCOL_QQ:
			position = 4;
			break;
		case Im.PROTOCOL_GOOGLE_TALK:
			position = 5;
			break;
		case Im.PROTOCOL_ICQ:
			position = 6;
			break;
		case Im.PROTOCOL_JABBER:
			position = 7;
			break;
		case Im.PROTOCOL_CUSTOM:
			position = 8;
			break;
		}
		return position;
	}
}