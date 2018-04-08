package com.renren.api.connect.android.example;

import android.os.Bundle;

import com.renren.api.connect.android.Util;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.view.RenrenDialogListenerHelper;

/**
 * @author 李勇(yong.li@opi-corp.com) 2010-7-19
 */
public class PostFeedListener extends
		RenrenDialogListenerHelper.DefaultRenrenDialogListener {
	private Example example;

	PostFeedListener(Example example) {
		this.example = example;
	}

	@Override
	public void onComplete(Bundle values) {
		super.onComplete(values);
		Util.showAlert(example, "Feed Complete 提示", "发新鲜事完成");
	}

	@Override
	public void onCancel(Bundle values) {
		super.onCancel(values);
		Util.showAlert(example, "Feed Cancel 提示", "用户放弃发新鲜事");
	}

	@Override
	public void onRenrenError(RenrenError renrenError) {
		super.onRenrenError(renrenError);
		Util.showAlert(example, "Feed RenrenError 提示", "RenrenError:"
				+ renrenError);
	}

	@Override
	public void onHttpError(int errorCode, String description, String failingUrl) {
		super.onHttpError(errorCode, description, failingUrl);
		Util.showAlert(example, "Feed HttpError 提示", "HttpError errorCode: "
				+ errorCode + " errorMsg:" + description + " failurl:"
				+ failingUrl);
	}
}
