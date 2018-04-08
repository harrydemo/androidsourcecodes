package com.example.demo.fragment;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demo.R;
import com.example.demo.model.Navigation;
import com.example.demo.widget.NavigationHorizontalScrollView;
import com.example.demo.widget.NavigationHorizontalScrollView.OnNavigationItemClickListener;

public class NavigationFragment extends Fragment implements OnNavigationItemClickListener {
	private List<Navigation> navs ;
	private NavigationHorizontalScrollView mHorizontalScrollView;

	public void setNavs(List<Navigation> navs) {
		this.navs = navs;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_navigation, null);
		mHorizontalScrollView = (NavigationHorizontalScrollView) view.findViewById(R.id.horizontal_scrollview);
		mHorizontalScrollView.setImageView((ImageView) view.findViewById(R.id.iv_pre),
				(ImageView) view.findViewById(R.id.iv_next));
		mHorizontalScrollView.setOnNavigationItemClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mHorizontalScrollView.setAdapter(new NavigationAdapter());
	}

	class NavigationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return navs.size();
		}

		@Override
		public Object getItem(int position) {
			return navs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.navigation_item, null);
			}
			((TextView) convertView).setText(navs.get(position).getTitle());
			return convertView;
		}

	}
	
	public interface onSelectorNavigationListener{
		void selector(Navigation navigation,int position);
	}
	private onSelectorNavigationListener onSelectorNavigationListener;

	public void setOnSelectorNavigationListener(
			onSelectorNavigationListener onSelectorNavigationListener) {
		this.onSelectorNavigationListener = onSelectorNavigationListener;
	}

	@Override
	public void click(int position) {
		if(onSelectorNavigationListener !=null){
			onSelectorNavigationListener.selector(navs.get(position), position);
		}
	}
	
}
