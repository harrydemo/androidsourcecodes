package com.xcontacts.ui;

import com.xcontacts.ui.model.AbstractItem;
import com.xcontacts.utils.MyDialog;
import com.xcontacts.utils.MyLog;
import com.xcontacts.utils.MyTextWachter;

import android.content.Context;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.widget.LinearLayout;

public class EmailItem extends AbstractItem {

	public EmailItem(Context context) {
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
		getChanges().put(Email.TYPE, getItemType(indexOfNextType));

		this.btnType.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MyLog.d("btnType-->onClick()");
				MyDialog.show(EmailItem.this, btnType, mTypes, Email.TYPE,
						Email.LABEL, getChanges());
			}
		});
		// 监听EditText中内容变化
		this.etContent.addTextChangedListener(new MyTextWachter(Email.DATA,
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
	 * 根据Email的类型数组，判断用户选择的Email类型
	 * 
	 * @param postion
	 *            用户选择的类型在类型数组中的位置
	 * @return
	 */
	public int getItemType(int position) {
		int emailType = 0;
		switch (position) {
		case 0:
			emailType = Email.TYPE_HOME;
			break;
		case 1:
			emailType = Email.TYPE_WORK;
			break;
		case 2:
			emailType = Email.TYPE_OTHER;
			break;
		case 3:
			emailType = Email.TYPE_MOBILE;
			break;
		case 4:// 特殊情况
			emailType = Email.TYPE_CUSTOM;
			break;
		// 如果是上述情况，则用户自定义Lable(即data3字段)
		}
		return emailType;
	}

	@Override
	public int getPositionBasedOnType(int type) {
		int position = 0;
		switch (type) {
		case Email.TYPE_HOME:
			position = 0;
			break;
		case Email.TYPE_WORK:
			position = 1;
			break;
		case Email.TYPE_OTHER:
			position = 2;
			break;
		case Email.TYPE_MOBILE:
			position = 3;
			break;
		case Email.TYPE_CUSTOM:
			position = 4;
			break;
		}
		return position;
	}
}