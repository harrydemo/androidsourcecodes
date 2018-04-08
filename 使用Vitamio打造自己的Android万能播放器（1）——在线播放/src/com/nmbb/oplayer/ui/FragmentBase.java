package com.nmbb.oplayer.ui;

import com.nmbb.oplayer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentBase extends Fragment {

	protected ListView mListView;
	protected View mLoadingLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_file, container, false);
		mListView = (ListView) v.findViewById(android.R.id.list);
		mLoadingLayout = v.findViewById(R.id.loading);
		return v;
	}

	public boolean onBackPressed() {
		return false;
	}
}
