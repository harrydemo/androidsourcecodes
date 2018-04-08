package com.bus.shenyang.net;

import java.util.ArrayList;


import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKLine;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKRoute;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKStep;
import com.baidu.mapapi.MKTransitRoutePlan;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapView;
import com.bus.shenyang.R;
import com.bus.shenyang.adapter.BusAdapter;
import com.bus.shenyang.adapter.Plan;
import com.waps.AppConnect;
import com.waps.UpdatePointsNotifier;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NoSubway extends MapActivity implements UpdatePointsNotifier {
	
	private int jifeng;
	boolean update_text = false;
	private String ststop;
	private String enstop;
	private BMapManager mapManagerNoSubway;
	private MapView mMapView;
	private MKSearch mSearch;
	private ListView listView;
	private BusAdapter mAdapter;
	private ArrayList<Plan> planA;
	private ProgressDialog pd;
	String str = "";
	String[] Content;
	private String pointsText;
	private int flag;
	final Handler mHandler = new Handler();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.nosubway);
		AppConnect.getInstance("243988d2b20a7c04aa412689fe4c6a37", "WAPS", this);
		
		planA = new ArrayList<Plan>();
		System.out.println("ArrayList<Plan>()planA=" + planA);
		listView = (ListView) findViewById(R.id.listView);
		mAdapter = new BusAdapter(getLayoutInflater());
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new ListViewOnItemClickListener());	      
		Bundle bundle = this.getIntent().getExtras();
		ststop = bundle.getString("ststop1");
		enstop = bundle.getString("enstop1");
		System.out.println("NoSubwayststop=" + ststop);
		System.out.println("NoSubwayenstop=" + enstop);
		
		mapManagerNoSubway = new BMapManager(getApplication());
		mapManagerNoSubway.init("64174E2288F6C683C4CDA6094DE98AEDAFB577C8",
				null);
		super.initMapActivity(mapManagerNoSubway);

		mMapView = (MapView) findViewById(R.id.bmapView);
		mMapView.setTraffic(true);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setDrawOverlayWhenZooming(true);
		mSearch = new MKSearch();
		mSearch.init(mapManagerNoSubway, new MyNoSubwaySearchListener());
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = ststop;
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = enstop;
		mSearch.setTransitPolicy(MKSearch.EBUS_NO_SUBWAY);
		mSearch.transitSearch("沈阳", stNode, enNode);

	}

	@Override
	protected void onPause() {
		if (mapManagerNoSubway != null) {
			mapManagerNoSubway.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (mapManagerNoSubway != null) {
			mapManagerNoSubway.stop();
			System.out.print("AAAAAAAAAAA");
		}
		super.onResume();
		AppConnect.getInstance("243988d2b20a7c04aa412689fe4c6a37", "WAPS", this);
		pd = ProgressDialog
				.show(NoSubway.this, "请稍候", "正在连接服务器...", true, true);
		listView = (ListView) findViewById(R.id.listView);
		mAdapter = new BusAdapter(getLayoutInflater());
		listView.setAdapter(mAdapter);
		
		System.out.println("/////////");

		mapManagerNoSubway = new BMapManager(getApplication());
		mapManagerNoSubway.init("64174E2288F6C683C4CDA6094DE98AEDAFB577C8",
				null);
		mapManagerNoSubway.start();

		mSearch = new MKSearch();
		mSearch.init(mapManagerNoSubway, new MyNoSubwaySearchListener());
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = ststop;
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = enstop;
		mSearch.setTransitPolicy(MKSearch.EBUS_NO_SUBWAY);
		mSearch.transitSearch("沈阳", stNode, enNode);

	}

	private void refreshPage() {
		System.out.println("refreshPage" + planA);
		mAdapter.setData(planA);

	}
	public class ListViewOnItemClickListener implements OnItemClickListener,UpdatePointsNotifier{

		private int amount=1;
		private boolean state=false;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			AppConnect.getInstance(NoSubway.this).getPoints(NoSubway.this);
			int a=flag;
			System.out.println("flag1="+flag);
			if(a>=amount)
			{						
				AppConnect.getInstance(NoSubway.this).spendPoints(amount, NoSubway.this);
				System.out.println("jifeng="+jifeng);
			state=true;
			}else{
				System.out.println("1111111111111111");
				AppConnect.getInstance(NoSubway.this).showOffers(NoSubway.this);
				System.out.println("flag3="+flag);
				String str = "您的积分余额不足1分不能使用相关功能,余额为"+flag+"请获取积分";
				Toast.makeText(NoSubway.this
						, str, Toast.LENGTH_LONG).show();
				
			}
			if(state){
			Bundle bundle = new Bundle();
			bundle.putString("connet", Content[position]);
			Intent intent = new Intent(NoSubway.this, Detailed.class);
			intent.putExtras(bundle);
			startActivity(intent);
			}
		}
		final Runnable mUpdateResults = new Runnable() {
			

			public void run() {
			          jifeng=flag;
					update_text = false;
				
			}
		};
		@Override
		public void getUpdatePoints(String arg0, int pointTotal) {
			update_text = true;
			flag=pointTotal;
			mHandler.post(mUpdateResults);
			
		}

		@Override
		public void getUpdatePointsFailed(String error) {
			Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
			
		}

	}
	
	public class MyNoSubwaySearchListener implements MKSearchListener {

		private MKLine line;
		private MKRoute route;

		@Override
		public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {

			if (error != 0 || res == null) {
				Toast.makeText(NoSubway.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
				return;
			}
			int fangan = res.getNumPlan();
			if (planA.isEmpty()) {
				Content=new String[fangan];
				for (int i = 0; i < fangan; i++) {
					int strline = 0;
					String strcontent = "";
					int strroute = 0;
					int strdistance;
					String[] title = null;
					String[] OnStop = null;
					String[] OffStop = null;
					int[] NumViaStops = null;
					int[] Distance = null;
					String[] concent = null;
					str="";

					System.out
							.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
					res.getPlan(i);

					MKTransitRoutePlan routePlan = res.getPlan(i);
					title = new String[routePlan.getNumLines()];
					OnStop = new String[routePlan.getNumLines()];
					OffStop = new String[routePlan.getNumLines()];
					NumViaStops = new int[routePlan.getNumLines()];
					Distance = new int[routePlan.getNumRoute()];
					concent = new String[routePlan.getNumLines()];
					for (int index = 0; index < routePlan.getNumLines(); index++) {
						line = routePlan.getLine(index);

						line.getDistance();
						strline = strline + line.getDistance();// 得到距离

						line.getTitle();
						if (index == routePlan.getNumLines() - 1) {
							strcontent = strcontent + line.getTitle();
						} else {
							strcontent = strcontent + line.getTitle() + "→";
						}
						title[index] = line.getTitle();

						MKPoiInfo info = line.getGetOnStop();
						OnStop[index] = info.name;

						MKPoiInfo info1 = line.getGetOffStop();
						OffStop[index] = info1.name;

						line.getNumViaStops();
						NumViaStops[index] = line.getNumViaStops();

					}

					for (int index = 0; index < routePlan.getNumRoute(); index++) {
						route = routePlan.getRoute(index);
						route.getDistance();
						Distance[index] = route.getDistance();
					}
					strdistance = strline + strroute;
					Plan plan = new Plan();
					plan.id = i + 1;
					plan.content = strcontent;
					plan.distance = strdistance;
					planA.add(plan);

					for (int k = 0; k < routePlan.getNumRoute() - 1; k++) {
						if (k == 0) {
							if (Distance[0] == 0) {
								concent[0] = "从" + OnStop[k] + "站乘坐" + title[k]
										+ "经过" + NumViaStops[k] + "站在"
										+ OffStop[k] + "站下车";

							} else {
								concent[k] = "步行" + Distance[k] + "米" + "从"
										+ OnStop[k] + "站乘坐" + title[k] + "经过"
										+ NumViaStops[k] + "站在" + OffStop[k]
										+ "站下车";
							}
						} else {
							concent[k] = "步行" + Distance[k] + "米" + "从"
									+ OnStop[k] + "站乘坐" + title[k] + "经过"
									+ NumViaStops[k] + "站在" + OffStop[k]
									+ "站下车";
						}
						str = str + concent[k];
					}
					if (Distance[routePlan.getNumRoute() - 1] == 0) {
						Content[i]= str + "到站";
						System.out.println("str=====" + str + "到站");
					} else {
						
						
						Content[i]=str + "步行"+ Distance[routePlan.getNumRoute() - 1]
									+ "米到站";
						System.out
								.println("str=====" + str + "步行"
										+ Distance[routePlan.getNumRoute() - 1]
										+ "米到站");
					}
				}
			}

			refreshPage();
			pd.dismiss();
		}

		@Override
		public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	final Runnable mUpdateResults = new Runnable() {
		

		public void run() {
		          jifeng=flag;
				update_text = false;
			
		}
	};
	public void getUpdatePoints(String arg0, int pointTotal) {
		update_text = true;
		flag=pointTotal;
		mHandler.post(mUpdateResults);
	}

	public void getUpdatePointsFailed(String error) {
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
	}
}
