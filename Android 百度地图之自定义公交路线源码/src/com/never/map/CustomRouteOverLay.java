package com.never.map;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.ItemizedOverlay;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.Overlay;
import com.baidu.mapapi.OverlayItem;
import com.baidu.mapapi.Projection;
import com.baidu.mapapi.RouteOverlay;

/**
 * custom route overlay
 * @author neverever415
 *
 */
public class CustomRouteOverLay extends RouteOverlay {

	public Activity ac;
	private MapView mapView;

	static ArrayList<View> overlayviews = new ArrayList<View>();
	public CustomRouteOverLay(Activity arg0, MapView arg1) {
		super(arg0, arg1);
		ac = arg0;
		mapView = arg1;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean onTap(int arg0) {
		// TODO Auto-generated method stub
		// return super.onTap(arg0);
		return true;
	}

	@Override
	public void setData(MKRoute arg0) {
		// TODO Auto-generated method stub
		super.setData(arg0);
		addHint(arg0);
	}
	
	
	public void addHints(MKRoute routes) {
		for (int i = 0; i < routes.getNumSteps(); i++) {
			Drawable marker = ac.getResources().getDrawable(R.drawable.pop); // 得到需要标在地图上的资源
			marker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight()); // 为maker定义位置和边界
			OverItemT overitem = new OverItemT(marker,ac, routes.getStep(i).getContent(),routes.getStep(i).getPoint());
//			OverlayItem over=new OverlayItem(routes.GET, null, null);
			mapView.getOverlays().add(overitem); // 添加ItemizedOverlay实例到mMapView
		}
		mapView.invalidate();
	}
	/**
	 * 增加 指示路线
	 * @param routes
	 */
	public void addHint(MKRoute routes) {
		mapView.getOverlays().clear();// 先清空
//		mapView.removeAllViewsInLayout();
		View mPopView = ac.getLayoutInflater().inflate(R.layout.popview,
				null);
		for(int i=0;i<	overlayviews.size();i++){
			System.out.println("remove &"+i);
			mapView.removeViewInLayout(overlayviews.get(i));
			overlayviews.remove(i);
		}
	
		mapView.invalidate();
		// 添加ItemizedOverlay
		for (int i = 0; i < routes.getNumSteps(); i++) {

			Drawable marker = ac.getResources().getDrawable(R.drawable.pop); // 得到需要标在地图上的资源
			marker.setBounds(0, 0, marker.getIntrinsicWidth(),
					marker.getIntrinsicHeight()); // 为maker定义位置和边界
			GeoPoint pt = routes.getStep(i).getPoint();// =
														// routes.get(i).getPoint();
			if (i != 0 && i != routes.getNumSteps() - 1) {
				mPopView = ac.getLayoutInflater().inflate(R.layout.popview,
						null);
				mapView.addView(mPopView, new MapView.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
						MapView.LayoutParams.TOP_LEFT));
				mPopView.setVisibility(View.GONE);
				mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
						MapView.LayoutParams.BOTTOM_CENTER));
				mPopView.setVisibility(View.VISIBLE);
				Button button = (Button) mPopView.findViewById(R.id.overlay_pop);
				button.setText(routes.getStep(i).getContent());
				overlayviews.add(mPopView);
				overlayviews.add(button);
			} else {
				//修改起始点和终点样式-自定义
				mPopView = ac.getLayoutInflater().inflate(R.layout.popview,
						null);
				mapView.addView(mPopView, new MapView.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, null,
						MapView.LayoutParams.TOP_LEFT));
				mPopView.setVisibility(View.GONE);
				mapView.updateViewLayout(mPopView, new MapView.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, pt,
						MapView.LayoutParams.BOTTOM_CENTER));
				mPopView.setVisibility(View.VISIBLE);
				Button button = (Button) mPopView.findViewById(R.id.overlay_pop);
				button.offsetTopAndBottom(100);
				button.setTextColor(Color.BLUE);
				button.setBackgroundColor(Color.TRANSPARENT);
				button.setText(routes.getStep(i).getContent());
				overlayviews.add(mPopView);
				overlayviews.add(button);
			}
		}
	}

	class OverItemT extends ItemizedOverlay<OverlayItem> {

		private Drawable marker;
		private Context mContext;
		private GeoPoint p;
		private OverlayItem o;
		public OverItemT(Drawable marker, Context context, String title,GeoPoint p) {
			super(boundCenterBottom(marker));
			this.marker = marker;
			this.mContext = context;
			this.p = p;
			// 构造OverlayItem的三个参数依次为：item的位置，标题文本，文字片段
			o = new OverlayItem(p, title, title);
			populate(); // createItem(int)方法构造item。一旦有了数据，在调用其它方法前，首先调用这个方法
		}
		public void updateOverlay() {
			populate();
		}

		@Override
		public void draw(Canvas canvas, MapView mapView, boolean shadow) {

			// Projection接口用于屏幕像素坐标和经纬度坐标之间的变换
			Projection projection = mapView.getProjection();
			for (int index = size() - 1; index >= 0; index--) { // 遍历mGeoList
				OverlayItem overLayItem = getItem(index); // 得到给定索引的item
				String title = overLayItem.getTitle();
				// 把经纬度变换到相对于MapView左上角的屏幕像素坐标
				Point point = projection.toPixels(overLayItem.getPoint(), null);
				// 可在此处添加您的绘制代码
				Paint paintText = new Paint();
				paintText.setColor(Color.BLUE);
				paintText.setTextSize(15);
				canvas.drawText(title, point.x - 30, point.y, paintText); // 绘制文本
			}
			super.draw(canvas, mapView, shadow);
			// 调整一个drawable边界，使得（0，0）是这个drawable底部最后一行中心的一个像素
			boundCenterBottom(marker);
		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return o;
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		// 处理当点击事件
		protected boolean onTap(int i) {
			// 更新气泡位置,并使之显示
			return true;
		}

		@Override
		public boolean onTap(GeoPoint arg0, MapView arg1) {
			// TODO Auto-generated method stub
			// 消去弹出的气泡
			// ItemizedOverlayDemo.mPopView.setVisibility(View.GONE);
			return super.onTap(arg0, arg1);
		}
	}

}
