package com.example.demo.fragment;

import com.example.demo.model.Navigation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContentFragment3 extends Fragment {
	private static String KEY = "key";

	public static Fragment instance(Navigation navigation) {
		ContentFragment3 fragment0 = new ContentFragment3();
		Bundle bundle = new Bundle();
		bundle.putSerializable(KEY, navigation);
		fragment0.setArguments(bundle);
		return fragment0;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Navigation navigation = (Navigation) getArguments()
				.getSerializable(KEY);
		TextView textView = new TextView(getActivity());
		textView.setTextSize(25);
		textView.setText("type = " + navigation.getType() + " 的fragment \n"
				+ "导航信息：" + navigation.toString());
		return textView;
	}
}
