package com.xcontacts.utils;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.provider.ContactsContract.CommonDataKinds.BaseTypes;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xcontacts.activities.R;
import com.xcontacts.ui.model.MyLinearLayout;

/**
 * 显示信息类型的Dialog
 * 
 * @author Lefter
 */
public class MyDialog {
	public static void show(final MyLinearLayout item, final Button btnType,
			final String[] types, final String itemType, final String lable,
			final ContentValues values) {
		// 弹Dialog,供用户选择信息类型
		AlertDialog.Builder builder = new AlertDialog.Builder(item.getContext());
		builder.setTitle("Select Lable");
		builder.setItems(types, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int position) {
				// 获得用户选择的type
				int type = item.getItemType(position);
				MyLog.d("type:" + type);
				values.put(itemType, type);
				dialog.dismiss();
				// 判断是不是Custom
				if (type == BaseTypes.TYPE_CUSTOM
						|| type == Phone.TYPE_ASSISTANT
						|| type == Im.PROTOCOL_CUSTOM) {// 自定义
					// 再次弹出Dialog，供用户输入自定义的Type
					AlertDialog.Builder innerBuilder = new AlertDialog.Builder(
							item.getContext());
					innerBuilder.setTitle("Custom Lable Name");
					final EditText editText = new EditText(item.getContext());
					editText.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					innerBuilder.setView(editText);
					innerBuilder.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String strType = editText.getText()
											.toString();
									if (!TextUtils.isEmpty(strType)) {
										// 保存用户自定义的Type的Lable
										values.put(lable, strType);
										MyLog.i("	customLable:" + strType);
										// 设置按钮显示用户选择的Type
										btnType.setText(strType);
										dialog.dismiss();
									} else {
										Toast.makeText(item.getContext(),
												R.string.toast_Content_is_null,
												Toast.LENGTH_SHORT).show();
									}
								}
							});
					innerBuilder.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					innerBuilder.create().show();
				} else {// 不是自定义，保存类型
					// 设置按钮显示用户选择的Type
					btnType.setText(types[position]);
				}
			}
		});
		builder.create().show();
	}
}