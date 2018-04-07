package com.xcontacts.ui;

import com.xcontacts.ui.model.AbstractItem;
import com.xcontacts.utils.MyDialog;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

import android.content.Context;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.LinearLayout;

public class PhoneItem extends AbstractItem {

	public PhoneItem(Context context) {
		super(context);
	}

	@Override
	public void initViews(LinearLayout container, int arrayRes, int hintRes) {
		this.mContainer = container;
		this.etContent.setHint(hintRes);
		this.mTypes = getResources().getStringArray(arrayRes);

		// 下面这四行使得用户添加一行数据时，数据的type会按顺序循环显示出来
		final int countOfTypes = this.mTypes.length;
		int typeMinusCustom = countOfTypes - 2;// 减去1是为了排除Custom和Assistant
		int countOfChilds = container.getChildCount();
		int indexOfNextType = countOfChilds % typeMinusCustom;

		this.btnType.setText(mTypes[indexOfNextType]);
		getChanges().put(Phone.TYPE, getItemType(indexOfNextType));

		this.btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyLog.d("btnType-->onClick()");
				MyDialog.show(PhoneItem.this, btnType, mTypes, Phone.TYPE,
						Phone.LABEL, getChanges());
			}
		});
		// 监听EditText中内容变化
		this.etContent.addTextChangedListener(new MyTextWachter(Phone.NUMBER,
				getChanges()));
		// 点击，删除这一行
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
	 * 根据Phone的类型数组，判断用户选择的Phone类型
	 * 
	 * @param postion
	 *            用户选择的类型在类型数组中的位置
	 * @return
	 */
	public int getItemType(int position) {
		int phoneType = 0;
		switch (position) {
		case 0:
			phoneType = Phone.TYPE_HOME;
			break;
		case 1:
			phoneType = Phone.TYPE_MOBILE;
			break;
		case 2:
			phoneType = Phone.TYPE_WORK;
			break;
		case 3:
			phoneType = Phone.TYPE_FAX_WORK;
			break;
		case 4:
			phoneType = Phone.TYPE_FAX_HOME;
			break;
		case 5:
			phoneType = Phone.TYPE_PAGER;
			break;
		case 6:
			phoneType = Phone.TYPE_OTHER;
			break;
		case 7:
			phoneType = Phone.TYPE_MMS;
			break;
		case 8:
			phoneType = Phone.TYPE_CALLBACK;
			break;
		case 9:
			phoneType = Phone.TYPE_CAR;
			break;
		case 10:
			phoneType = Phone.TYPE_COMPANY_MAIN;
			break;
		case 11:
			phoneType = Phone.TYPE_ISDN;
			break;
		case 12:
			phoneType = Phone.TYPE_MAIN;
			break;
		case 13:
			phoneType = Phone.TYPE_OTHER_FAX;
			break;
		case 14:
			phoneType = Phone.TYPE_RADIO;
			break;
		case 15:
			phoneType = Phone.TYPE_TELEX;
			break;
		case 16:
			phoneType = Phone.TYPE_TTY_TDD;
			break;
		case 17:
			phoneType = Phone.TYPE_WORK_MOBILE;
			break;
		case 18:
			phoneType = Phone.TYPE_WORK_PAGER;
			break;
		case 19:// 用户自定义Lable(即data3字段)
			phoneType = Phone.TYPE_ASSISTANT;
			break;
		case 20:
			// 用户自定义Lable(即data3字段)
			phoneType = Phone.TYPE_CUSTOM;
			break;
		}
		return phoneType;
	}

	/**
	 * @param type
	 *            Phone的类型
	 * @return 根据用户选择的类型(如Phone.TYPE_HOME)返回该类型在类型数组中的position.
	 */
	public int getPositionBasedOnType(int type) {
		int position = 0;
		switch (type) {
		case Phone.TYPE_HOME:
			position = 0;
			break;
		case Phone.TYPE_MOBILE:
			position = 1;
			break;
		case Phone.TYPE_WORK:
			position = 2;
			break;
		case Phone.TYPE_FAX_WORK:
			position = 3;
			break;
		case Phone.TYPE_FAX_HOME:
			position = 4;
			break;
		case Phone.TYPE_PAGER:
			position = 5;
			break;
		case Phone.TYPE_OTHER:
			position = 6;
			break;
		case Phone.TYPE_MMS:
			position = 7;
			break;
		case Phone.TYPE_CALLBACK:
			position = 8;
			break;
		case Phone.TYPE_CAR:
			position = 9;
			break;
		case Phone.TYPE_COMPANY_MAIN:
			position = 10;
			break;
		case Phone.TYPE_ISDN:
			position = 11;
			break;
		case Phone.TYPE_MAIN:
			position = 12;
			break;
		case Phone.TYPE_OTHER_FAX:
			position = 13;
			break;
		case Phone.TYPE_RADIO:
			position = 14;
			break;
		case Phone.TYPE_TELEX:
			position = 15;
			break;
		case Phone.TYPE_TTY_TDD:
			position = 16;
			break;
		case Phone.TYPE_WORK_MOBILE:
			position = 17;
			break;
		case Phone.TYPE_WORK_PAGER:
			position = 18;
			break;
		case Phone.TYPE_ASSISTANT:
			position = 19;
			break;
		case Phone.TYPE_CUSTOM:
			position = 20;
			break;
		}
		return position;
	}
}