package com.xcontacts.ui.model;

import com.xcontacts.activities.R;
import com.xcontacts.utils.MyLog;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Email、Im、Phone继承自该类,因为他们有很大的共同点
 * 
 * @author Lefter
 */
public abstract class AbstractItem extends MyLinearLayout {
	/**
	 * 包裹相同类型信息的LinearLayout。如：包裹Phone信息的最外层的LinearLayout
	 */
	protected LinearLayout mContainer;
	/**
	 * 包裹一行的LinearLayout
	 */
	protected LinearLayout mItemContainer;

	protected Button btnType;
	protected EditText etContent;
	protected ImageButton ibDele;
	protected Context context;
	/**
	 * 存储信息的类型
	 */
	protected String[] mTypes;
	/**
	 * 编辑联系人时，我们需要每条信息在Data表中_id的值，用作更新条件
	 */
	private long dataId;
	/**
	 * 编辑联系人时，当某些信息被更新时，我们把改变后的信息存放在changes对象中
	 */
	private ContentValues changes = null;

	/**
	 * 创建对象后应立即调用initViews方法对变量进行初始化
	 * 
	 * @param context
	 *            上下文环境
	 */
	public AbstractItem(Context context) {
		super(context);
		this.context = context;
		mItemContainer = this;
		mItemContainer.setOrientation(HORIZONTAL);
		mItemContainer.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		btnType = new Button(context);
		btnType.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		etContent = new EditText(context);
		etContent.setLayoutParams(new LayoutParams(0,
				LayoutParams.WRAP_CONTENT, 1));

		ibDele = new ImageButton(context);
		ibDele.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		ibDele.setBackgroundResource(R.drawable.ic_btn_round_minus);

		mItemContainer.addView(btnType);
		mItemContainer.addView(etContent);
		mItemContainer.addView(ibDele);
	}

	/**
	 * 负责对Button、EditText、ImageButton的初始化。
	 * 具体事务为：Button的点击事件（设置类型）、EditText设置Hint、 ImageButton的点击事件（删除这一行）
	 * 
	 * @param container
	 *            存放每一行的容器
	 * @param arrayRes
	 *            选择Type时使用的资源
	 * @param hintRes
	 *            EditText中的提示信息
	 */
	public abstract void initViews(LinearLayout container, int arrayRes,
			int hintRes);

	/**
	 * @param content
	 *            设置用户输入的内容。如电话号码、Email地址等
	 */
	public void setContent(String content) {
		etContent.setText(content);
		MyLog.i("AbstractItem-->setContent()");
	}

	/**
	 * 设置选择类型按钮的文字
	 * 
	 * @param typeString
	 */
	public void setTypeString(String typeString) {
		this.btnType.setText(typeString);
	}

	/**
	 * @return the dataId
	 */
	public long getDataId() {
		return dataId;
	}

	/**
	 * @param dataId
	 *            the dataId to set
	 */
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	public ContentValues getChanges() {
		if (changes == null) {
			changes = new ContentValues();
		}
		return changes;
	}

	/**
	 * 根据用户选择的类型(如Phone.TYPE_HOME)返回该类型在类型数组中的position.
	 */
	public abstract int getPositionBasedOnType(int type);
}