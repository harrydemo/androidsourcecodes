package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.example.demo.fragment.ContentFragment0;
import com.example.demo.fragment.ContentFragment1;
import com.example.demo.fragment.ContentFragment2;
import com.example.demo.fragment.ContentFragment3;
import com.example.demo.fragment.ContentFragment4;
import com.example.demo.fragment.ContentFragment5;
import com.example.demo.fragment.NavigationFragment;
import com.example.demo.fragment.NavigationFragment.onSelectorNavigationListener;
import com.example.demo.model.Navigation;

public class MainActivity extends FragmentActivity implements
		onSelectorNavigationListener {

	private List<Navigation> navs = buildNavigation();
	private NavigationFragment mNavigationFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		mNavigationFragment = (NavigationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_navigation);
		mNavigationFragment.setNavs(navs);
		mNavigationFragment.setOnSelectorNavigationListener(this);
		fillContent(navs.get(0));
	}

	@Override
	public void selector(Navigation navigation, int position) {
		fillContent(navigation);
	}

	private void fillContent(Navigation navigation) {
		Fragment fragment = null;
		switch (navigation.getType()) {
		case Navigation.TYPE_0:
			fragment = ContentFragment0.instance(navigation);
			break;
		case Navigation.TYPE_1:
			fragment = ContentFragment1.instance(navigation);
			break;
		case Navigation.TYPE_2:
			fragment = ContentFragment2.instance(navigation);
			break;
		case Navigation.TYPE_3:
			fragment = ContentFragment3.instance(navigation);
			break;
		case Navigation.TYPE_4:
			fragment = ContentFragment4.instance(navigation);
			break;
		case Navigation.TYPE_5:
			fragment = ContentFragment5.instance(navigation);
			break;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fl_content, fragment);
		transaction.commit();
	}

	private List<Navigation> buildNavigation() {
		List<Navigation> navigations = new ArrayList<Navigation>();
		navigations.add(new Navigation(Navigation.TYPE_0, "url", "首页"));
		navigations.add(new Navigation(Navigation.TYPE_1, "url", "好友"));
		navigations.add(new Navigation(Navigation.TYPE_2, "url", "广场"));
		navigations.add(new Navigation(Navigation.TYPE_3, "url", "用户设置"));
		navigations.add(new Navigation(Navigation.TYPE_4, "url", "朋友"));
		navigations.add(new Navigation(Navigation.TYPE_5, "url", "很长的标题哦哦哦"));
		navigations.add(new Navigation(Navigation.TYPE_0, "url", "我还是首页"));
		navigations.add(new Navigation(Navigation.TYPE_3, "url", "另一个用户设置"));
		return navigations;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
