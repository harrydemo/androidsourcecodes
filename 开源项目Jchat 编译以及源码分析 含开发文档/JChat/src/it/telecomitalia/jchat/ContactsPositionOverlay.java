package it.telecomitalia.jchat;

import jade.util.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.FontMetrics;
import android.location.Location;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

/**
 * Defines the overlay used to draw information over the map view. It allows
 * drawing contacts position and provides functionalities for automatic zooming
 * and centering of the map.
 * 定义用于绘制地图视图的信息覆盖。
 * 它允许绘制的接触位置，并提供自动缩放和地图为中心的功能。
 */

//联系人的位置重叠
public class ContactsPositionOverlay extends Overlay
{

	/**
	 * Instance of the Jade logger for debugging.实例桥接记录进行调试。
	 */
	private final Logger myLogger = Logger.getMyLogger(this.getClass().getName());

	/**
	 * Upper threshold, used for automatic zooming adjustment. It is a squared
	 * distance defined as a percentage of screen width
	 * 阈值上限，用于自动缩放调整。这是一个距离平方定义为屏幕宽度的百分比
	 */
	private int UPPER_THRESHOLD = 0;

	/**
	 * Lower threshold, used for automatic zooming adjustment. It is a squared
	 * distance defined as a percentage of screen width
	 * 门槛较低，用于自动变焦调整。这是一个距离平方定义为屏幕宽度的百分比
	 */
	private int LOWER_THRESHOLD = 0;

	/**
	 * Controller used to perform map zooming and centering
	 * 控制器用于执行地图缩放和居中
	 */
	private MapController mapController;

	/**
	 * Paint object, used for drawing.渲染对象，用于绘图。
	 */
	private Paint myPaint;

	/**
	 * Bitmap object that shows the yellow paddle.Bitmap对象，显示了黄色的桨。
	 */
	private Bitmap ylwPaddle;

	/** The blue paddle. */
	private Bitmap bluePaddle;

	/**
	 * Bitmap object that shows the blue baloon.Bitmap对象，显示了蓝色的热气球。
	 */
	private Bitmap blueBaloon;

	/**
	 * Bitmap object that shows the highlight when selecting a contact in map
	 * mode		Bitmap对象显示的亮点时，选择一个在地图模式下的接触
	 */
	private Bitmap highlight;

	/**
	 * Resource object, stored for making quicker access to resource files
	 * 资源对象，更快地访问资源文件存储
	 */
	private Resources appRes;

	/**
	 * Map containing data describing the online contacts to be drawn and their
	 * status (checked/unchecked)
	 * 地图包含描述要绘制的在线联系人的数据和他们的状态（选中/取消）
	 */
	private Map<String, ContactLayoutData> contactPositionMap;

	/**
	 * Percentage defining the scroll area width with respect to screen width
	 * 定义滚动区域的宽度与屏幕宽度的百分比
	 */
	private static final float SCROLL_AREA_WIDTH_RATIO = 0.70f;

	/**
	 * Percentage defining the scroll area height with respect to screen height
	 * 定义滚动区域的高度与屏幕高度的百分比
	 */
	private static final float SCROLL_AREA_HEIGHT_RATIO = 0.70f;

	/**
	 * Percentage with respect to screen width for defining the upper threshold.
	 * 关于屏幕宽度的定义上限的百分比。
	 */
	private static final float UPPER_THRESHOLD_RATIO = 0.46f;
	/**
	 * Percentage with respect to screen width for defining the lower threshold
	 * 百分比方面，筛选确定门槛较低的宽度
	 */
	private static final float LOWER_THRESHOLD_RATIO = 0.35f;

	/**
	 * Final width of the scrolling area in Pixel最后滚动区域的像素宽度
	 */
	private int SCROLL_AREA_WIDTH = -1;

	/**
	 * Final height of the scrolling area in Pixel最后滚动区域的高度像素
	 */
	private int SCROLL_AREA_HEIGHT = -1;

	/**
	 * Map view object on which we draw the overlay地图视图对象上，我们绘制覆盖
	 */
	private MapView myMapView;

	/**
	 * Width of the map view on which we draw宽度上，我们绘制地图视图
	 */
	private int WIDTH = -1;

	/**
	 * Height of the map view on which we draw高度上，我们绘制地图视图
	 */
	private int HEIGHT = -1;

	/**
	 * Constant used to for choosing zoom level. It means that zoom to max level
	 * is required不断选择缩放级别。这意味着，需要放大到最大水平
	 */
	private static final int ZOOM_MAX = 0;

	/**
	 * Constant used to for choosing zoom level. It means that zoom should be
	 * dynamically recomputed不断选择缩放级别。这意味着应动态地重新计算，变焦
	 */
	private static final int RECOMPUTE_ZOOM = 1;

	/**
	 * Constant used to for choosing zoom level. It means zoom shall not be
	 * recomputed不断选择缩放级别。这意味着，不得重新计算变焦
	 */
	private static final int NO_ZOOM = 2;

	/**
	 * The scrolling area (when exiting this area, scrolling is recomputed)
	 * 滚动区域（退出这一领域时，滚动重新计算）
	 */
	private Rect scrollingArea;

	private Context ctn;

	/**
	 * Instantiates a new contacts position overlay.
	 * 实例化一个新的接触位置重叠。
	 * 
	 * @param cont
	 *            current application context
	 * @param myMapView
	 *            the map view on which the overlay is drawn
	 * @param ctn
	 *            the file for accessing resources
	 */
	public ContactsPositionOverlay(Context cont, MapView myMapView, Resources ctn)
	{
		this.ctn = cont;
		mapController = myMapView.getController();
		appRes = ctn;
		myPaint = new Paint();
		this.myMapView = myMapView;
		scrollingArea = new Rect();
		contactPositionMap = new HashMap<String, ContactLayoutData>();
		ylwPaddle = BitmapFactory.decodeResource(appRes, R.drawable.ylw_circle);
		highlight = BitmapFactory.decodeResource(appRes, R.drawable.checked);
		blueBaloon = BitmapFactory.decodeResource(appRes, R.drawable.bluemessage);
		bluePaddle = BitmapFactory.decodeResource(appRes, R.drawable.blu_circle);
	}

	/**
	 * Check if scrolling of the map view is needed or not. This basically means
	 * that the view should be centered.
	 * 检查需要或不滚动的地图视图。这基本上意味着，应集中视图。
	 * @return true centering is needed and false otherwise
	 */
	private boolean scrollingIsNeeded()
	{

		Collection<ContactLayoutData> pointList = contactPositionMap.values();

		for (Iterator<ContactLayoutData> iterator = pointList.iterator(); iterator.hasNext();)
		{
			ContactLayoutData contactLayoutData = iterator.next();

			if (contactLayoutData.isVisible && !scrollingArea.contains(contactLayoutData.positionOnScreen.x, contactLayoutData.positionOnScreen.y))
			{
				return true;
			}
		}

		return false;

	}

	/**
	 * Checks if zoom level should be recomputed according to current position
	 * of contacts on the map. It basically checks if the max squared distance
	 * between the midpoint and one of the contacts is greater than the given
	 * threshold. If only one contact is drawn, zoom level shall be set to max.
	 * 根据当前的位置在地图上的接触，如果缩放级别的支票应重新计算。
	 * 它的基本检查，如果中点和接触之间的最大距离平方大于给定的阈值。
	 * 如果只有一个接触绘制，缩放级别应设置为最大。
	 * @param params
	 *            object containing the parameters computed from the current map
	 *            view.
	 * @return value that indicates how zoom level should be changed (ZOOM_MAX,
	 *         NO_ZOOM, RECOMPUTE_ZOOM)
	 */
	private int zoomChangeIsNeeded(PointClusterParams params)
	{
		int retval = NO_ZOOM;

		int currentNumberOfPoints = getContactsOnline();

		// If we have just one point left, we need to zoom to max level
		//如果我们只是一个点离开，我们需要放大到最高等级
		if (currentNumberOfPoints == 1 && myMapView.getZoomLevel() < 21)
		{
			retval = ZOOM_MAX;
		} 
		else if (currentNumberOfPoints > 1)
		{

			// If we have many points compute the max squared distance from the
			// midpoint如果我们有很多点计算中点的最大距离平方
			int maxDistSquared = getMaxDistSquared(contactPositionMap.values(), params.midpointOnScreen);

			// if we are in the too far or too near range
			if (maxDistSquared < LOWER_THRESHOLD || maxDistSquared > UPPER_THRESHOLD)
			{
				retval = RECOMPUTE_ZOOM;
			}
		}
		return retval;
	}

	/**
	 * Performs scrolling by centering the map on the point cluster using the
	 * set of parameters
	 * 执行由点群围绕地图使用的参数集的滚动
	 * @param params
	 *            the set of parameters computed from point cluster
	 */
	private void doScrolling(PointClusterParams params)
	{

		mapController.setCenter(params.midpointOnMap);
	}

	/**
	 * Adjust the zoom level depending on cluster point parameters (coordinate
	 * max span in average)
	 * 调整缩放级别，根据群集点参数（最大跨度的平均坐标）
	 * @param params
	 *            the point cluster parameters
	 * @param howToZoom
	 *            integer value coming from zoomChangeIsNeeded()
	 */
	private void doZoom(PointClusterParams params, int howToZoom)
	{
		if (howToZoom == ZOOM_MAX)
			mapController.setZoom(16);
		if (howToZoom == RECOMPUTE_ZOOM)
			mapController.zoomToSpan(params.coordMaxSpan[0], params.coordMaxSpan[1]);
	}

	/**
	 * Draws all the online contacts on the map.
	 * 绘制在地图上的所有在线联系人
	 * @param c
	 *            the canvas we use to draw
	 * @param p
	 *            the paint object we use to draw
	 */
	private void drawOnlineContacts(Canvas c, Paint p)
	{

		// myLogger.log(Logger.INFO, "Start drawing all contacts!!");
		FontMetrics fm = p.getFontMetrics();

		int bluePaddleOffsetY = bluePaddle.getHeight();
		int bluePaddleOffsetX = bluePaddle.getWidth() / 2;
		int ylwPaddleOffsetY = ylwPaddle.getHeight();
		int ylwPaddleOffsetX = ylwPaddle.getWidth() / 2;
		int blueBaloonOffsetY = 25;
		int blueBaloonOffsetX = 4;
		myPaint.setTextSize(18);

		Set<String> allParticipants = MsnSessionManager.getInstance().getAllParticipantIds();
		int color = 0;
		int iconToTextOffsetY = 5;

		for (Iterator<ContactLayoutData> iterator = contactPositionMap.values().iterator(); iterator.hasNext();)
		{
			ContactLayoutData cData = (ContactLayoutData) iterator.next();
			int bitmapOriginX = 0;
			int bitmapOriginY = 0;
			Bitmap bitmapToBeDrawn = null;

			if (cData.isVisible)
			{
				if (cData.isMyContact)
				{
					color = Color.YELLOW;
					// myLogger.log(Logger.INFO,
					// "Drawing my contact: position on screen (" +
					// cData.positionOnScreen[0] + ";" +
					// cData.positionOnScreen[1] + ")");
					bitmapOriginX = cData.positionOnScreen.x - ylwPaddleOffsetX;
					bitmapOriginY = cData.positionOnScreen.y - ylwPaddleOffsetY;
					bitmapToBeDrawn = ylwPaddle;
				} 
				else
				{
					// Here blueBaloon for people you're chatting with
					if (allParticipants.contains(cData.idContact))
					{
						bitmapOriginX = cData.positionOnScreen.x - blueBaloonOffsetX;
						bitmapOriginY = cData.positionOnScreen.y - blueBaloonOffsetY;
						bitmapToBeDrawn = blueBaloon;
					} 
					else
					{
						bitmapOriginX = cData.positionOnScreen.x - bluePaddleOffsetX;
						bitmapOriginY = cData.positionOnScreen.y - bluePaddleOffsetY;
						bitmapToBeDrawn = bluePaddle;
					}
					color = Color.BLUE;
				}

				int textOriginX = cData.positionOnScreen.x;
				int textOriginY = bitmapOriginY - iconToTextOffsetY;

				RectF rect = new RectF(textOriginX - 2, textOriginY + (int) fm.top - 2, textOriginX + this.getStringLength(cData.name, myPaint) + 2, textOriginY + (int) fm.bottom + 2);

				// Draws the debugging rct for collision
				// Draw the right bitmap icon

				if (cData.isChecked)
				{
					c.drawBitmap(highlight, bitmapOriginX, bitmapOriginY, myPaint);
				} else
				{
					c.drawBitmap(bitmapToBeDrawn, bitmapOriginX, bitmapOriginY, myPaint);
				}

				// Change color for background rectangle
				myPaint.setColor(Color.argb(100, 0, 0, 0));
				c.drawRoundRect(rect, 4.0f, 4.0f, myPaint);// Rect(rect,
															// myPaint);
				// ChangeColor for text
				myPaint.setColor(color);
				c.drawText(cData.name, textOriginX, textOriginY, myPaint);

				myPaint.setARGB(255, 255, 0, 0);
			}
		}
	}

	/**
	 * Overrides Overlay.draw() to draw the scene
	 * 覆盖Overlay.draw（）绘制场景
	 */
	public void draw(Canvas canvas, MapView mapView, boolean shadow)
	{
		Projection p = mapView.getProjection();

		updateOnScreenPosition(p);
		// Compute params needed for further computations on the point cluster
		int onlineContacts = getContactsOnline();
		if (onlineContacts > 0)
		{
			PointClusterParams params = extractParams(p, onlineContacts);

			// Things we do just the first time
			if (WIDTH == -1)
			{
				initialize(p, mapView.getWidth(), mapView.getHeight());
				int howToZoom = zoomChangeIsNeeded(params);
				doScrolling(params);
				doZoom(params, howToZoom);
			}
			else
			{

				// if any pixel is out our scrolling area
				if (scrollingIsNeeded())
				{
					// change map center
					doScrolling(params);
				}

				int howToZoom = zoomChangeIsNeeded(params);

				if (howToZoom != NO_ZOOM)
				{
					doZoom(params, howToZoom);
				}
			}
			// Draw all the contacts
			drawOnlineContacts(canvas, myPaint);
		}
	}

	/**
	 * Initialize all the constants values needed for drawing contacts and
	 * performing zooming and scrolling.
	 * 初始化绘制联系人和执行缩放和滚动所需的所有常量的值。
	 * @param calculator
	 *            {@link PixelCalculator} needed for retrieving map view size
	 *            and projecting map points on screen
	 *            检索地图视图的大小和投影屏幕上的地图点所需的计算器{@的链接PixelCalculator}
	 */
	private void initialize(Projection p, int width, int height)
	{
		WIDTH = width;
		HEIGHT = height;

		SCROLL_AREA_HEIGHT = (int) (HEIGHT * SCROLL_AREA_HEIGHT_RATIO);
		SCROLL_AREA_WIDTH = (int) (WIDTH * SCROLL_AREA_WIDTH_RATIO);
		scrollingArea.top = (HEIGHT - SCROLL_AREA_HEIGHT) / 2;
		scrollingArea.bottom = scrollingArea.top + SCROLL_AREA_HEIGHT;
		scrollingArea.left = (WIDTH - SCROLL_AREA_WIDTH) / 2;
		scrollingArea.right = scrollingArea.left + SCROLL_AREA_WIDTH;
		int tmpThresh = (int) (WIDTH * UPPER_THRESHOLD_RATIO);
		UPPER_THRESHOLD = tmpThresh * tmpThresh;
		tmpThresh = (int) (WIDTH * LOWER_THRESHOLD_RATIO);
		LOWER_THRESHOLD = tmpThresh * tmpThresh;

	}

	/**
	 * Recomputes on screen positions of all the contacts after changing their
	 * location on the map
	 * 重新计算后，改变其位置在地图上的所有联系人的屏幕位置
	 * @param calc
	 *            the pixel calculator
	 */
	private void updateOnScreenPosition(Projection calc)
	{
		for (ContactLayoutData cData : contactPositionMap.values())
		{
			if (cData.isVisible)
			{
				calc.toPixels(new GeoPoint(cData.latitudeE6, cData.longitudeE6), cData.positionOnScreen);
			}
		}
	}

	/**
	 * Retrieves the number of contacts that currently are online
	 * 检索，目前在线的联系人数量
	 * @return number of online contacts
	 */
	private int getContactsOnline()
	{
		int online = 0;

		for (ContactLayoutData mapEntry : contactPositionMap.values())
		{
			if (mapEntry.isVisible)
			{
				online++;
			}
		}

		return online;
	}

	/**
	 * Computes a set of parameters from the the current contacts locations.
	 * <p>
	 * These are in particular:
	 * <ul>
	 * <li>cluster midpoint on the map (back projection of midpoint on screen)
	 * <li>cluster midpoint on screen (average of contact positions on screen)
	 * <li>max longitude span
	 * <li>max latitude span
	 * </ul>
	 * These parameters are useful for automatic zooming and centering and are
	 * computed during each draw cycle
	 * 计算一组参数，从目前接触的位置。
		特别是：
		在地图上的集群中点（回投影屏幕上的中点）
		集群中点（平均在屏幕上的接触位置在屏幕上）
		最大经度跨度
		最大的纬度跨度
		这些参数自动变焦和中心，并在每个抽奖周期计算
	 * @param calc
	 *            the {@link PixelCalculator} needed for computations
	 * 
	 * @return class carrying results of computation
	 */
	//扩展参数
	private PointClusterParams extractParams(Projection p, int contactsOnLine)
	{

		int maxLat = Integer.MIN_VALUE;
		int minLat = Integer.MAX_VALUE;
		;
		int maxLong = Integer.MIN_VALUE;
		;
		int minLong = Integer.MAX_VALUE;
		;

		PointClusterParams params = new PointClusterParams();

		params.midpointOnScreen = new int[2];
		params.midpointOnScreen[0] = 0;
		params.midpointOnScreen[1] = 0;
		params.coordMaxSpan = new int[2];

		for (Iterator<ContactLayoutData> iterator = contactPositionMap.values().iterator(); iterator.hasNext();)
		{
			ContactLayoutData clData = (ContactLayoutData) iterator.next();

			if (clData.isVisible)
			{

				params.midpointOnScreen[0] += clData.positionOnScreen.x;
				params.midpointOnScreen[1] += clData.positionOnScreen.y;

				maxLat = (clData.latitudeE6 > maxLat) ? clData.latitudeE6 : maxLat;
				maxLong = (clData.longitudeE6 > maxLong) ? clData.longitudeE6 : maxLong;
				minLong = (clData.longitudeE6 < minLong) ? clData.longitudeE6 : minLong;
				minLat = (clData.latitudeE6 < minLat) ? clData.latitudeE6 : minLat;

				// we need to zoom in another way if we have a single point

				if (maxLat == minLat)
				{
					params.coordMaxSpan[0] = -1;
					params.coordMaxSpan[1] = -1;
				} else
				{
					params.coordMaxSpan[0] = maxLat - minLat;
					params.coordMaxSpan[1] = maxLong - minLong;
				}
			}
		}

		params.midpointOnScreen[0] /= contactsOnLine;
		params.midpointOnScreen[1] /= contactsOnLine;

		params.midpointOnMap = p.fromPixels(params.midpointOnScreen[0], params.midpointOnScreen[1]);

		return params;

	}

	/**
	 * Computes the max squared distance between the position of each contact on
	 * the screen and the midpoint.
	 * 计算每个屏幕上的接触和中点位置之间的最大距离平方。
	 * @param points
	 *            locations of the online contacts in screen coordinates
	 * @param midpoint
	 *            the midpoint in screen coordinate
	 * 
	 * @return the max squared distance  最大距离平方
	 */
	private int getMaxDistSquared(Collection<ContactLayoutData> points, int[] midpoint)
	{

		int maxDist = 0;

		// For each point
		for (Iterator<ContactLayoutData> iterator = points.iterator(); iterator.hasNext();)
		{
			ContactLayoutData contactLayoutData = iterator.next();
			// Compute distance squared
			int distX = midpoint[0] - contactLayoutData.positionOnScreen.x;
			int distY = midpoint[1] - contactLayoutData.positionOnScreen.y;
			int distSq = distX * distX + distY * distY;

			if (distSq > maxDist)
				maxDist = distSq;
		}

		return maxDist;
	}

	/**
	 * Collects and stores a set of parameters useful for automatic adjustment
	 * of zoom level and automatic map centering.
	 * 收集和存储一组参数，自动调整缩放级别和地图自动定心有用。
	 */
	private class PointClusterParams
	{

		/**
		 * Max span of latitude and longitude as a coordinate pair in最大跨度的纬度和经度坐标对一个
		 * microdegrees
		 */
		public int[] coordMaxSpan;

		/**
		 * Midpoint on the map (computed as the back projection of the midpoint
		 * on screen)在地图上的中点（背投影屏幕上的中点计算）
		 */
		public GeoPoint midpointOnMap;

		/**
		 * The midpoint on screen obtained as the average of the contact's
		 * on-screen positions.
		 * 屏幕上的中点所得的平均接触的屏幕上的位置。
		 */
		public int[] midpointOnScreen;
	}

	/**
	 * Provides the status of the contacts as drawn on the screen. Mantains a
	 * collection of info useful for drawing contacts data on map
	 * 在屏幕上绘制提供了联系人的状态。Mantains收集有用的信息，在地图上绘制联系人数据
	 */
	private class ContactLayoutData
	{

		/**
		 * The position on screen in pixel.
		 */
		public Point positionOnScreen;

		/**
		 * The latitude in microdegrees.
		 */
		public int latitudeE6;

		/**
		 * The longitude in microdegrees.
		 */
		public int longitudeE6;

		/**
		 * The altitude in microdegrees.
		 */
		public int altitudeE6;

		/**
		 * The name of the contact that should be drawn as a label
		 */
		public String name;

		/**
		 * true if the contact is selected on map, false otherwise
		 */
		public boolean isChecked;

		/**
		 * true if this is my contact, false otherwise
		 */
		public boolean isMyContact;

		/**
		 * Flag that means that the contact should be shown on map (online)
		 */
		public boolean isVisible;

		/**
		 * The contact id.
		 */
		public String idContact;

		/**
		 * Instantiates a new contact layout data.
		 * 
		 * @param cname
		 *            the name of the contact
		 * @param idcontact
		 *            the contact id
		 * @param contactLoc
		 *            the contact location on the map
		 * @param visible
		 *            true if contact should be drawn, false otherwise
		 */
		public ContactLayoutData(String cname, String idcontact, Location contactLoc, boolean visible)
		{
			this.name = cname;
			this.idContact = idcontact;
			isMyContact = false;
			positionOnScreen = new Point();
			latitudeE6 = (int) (contactLoc.getLatitude() * 1E6);
			longitudeE6 = (int) (contactLoc.getLongitude() * 1E6);
			altitudeE6 = (int) (contactLoc.getAltitude() * 1E6);
			isVisible = visible;
		}

		/**
		 * Update the contact location on map with new data
		 * 
		 * @param latitude
		 *            the latitude in microdegrees
		 * @param longitude
		 *            the longitude in microdegrees
		 * @param altitude
		 *            the altitude in microdegrees
		 */
		public void updateLocation(int latitude, int longitude, int altitude)
		{

			latitudeE6 = latitude;
			longitudeE6 = longitude;
			altitudeE6 = altitude;

		}

	}

	/**
	 * Returns the length of a string on screen in pixel drawn with the given
	 * Paint object.
	 * 返回一个像素在屏幕上用给定的油漆对象绘制的字符串的长度。
	 * @param name
	 *            string to be drawn
	 * @param paint
	 *            the Paint object
	 * 
	 * @return the string length in pixel
	 */
	private int getStringLength(String name, Paint paint)
	{
		float[] widthtext = new float[name.length()];
		float sumvalues = 0;
		paint.getTextWidths(name, widthtext);
		for (int n = 0; n < widthtext.length; n++)
		{
			sumvalues += widthtext[n];
		}
		return (int) sumvalues;
	}

	/**
	 * Check clicked position for hitting any contact. Any contact hit is marked
	 * as checked.
	 * 检查点击击中任何接触的位置。任何接触命中标记检查。
	 * @param point
	 *            the clicked point in screen coordinate
	 */
	public void checkClickedPosition(Point point)
	{

		int width = bluePaddle.getWidth();
		int height = bluePaddle.getHeight();
		String myId = ContactManager.getInstance().getMyContact().getPhoneNumber();

		for (ContactLayoutData contact : contactPositionMap.values())
		{
			Rect r = new Rect(contact.positionOnScreen.x - width / 2, contact.positionOnScreen.y - height, contact.positionOnScreen.x + width / 2, contact.positionOnScreen.y);
			if (r.contains(point.x, point.y) && !contact.idContact.equals(myId))
			{
				contact.isChecked = !contact.isChecked;
			}
		}
	}

	/**
	 * Retrieves all the clicked contacts
	 * 检索所有点击的接触
	 * @return list of id (phone numbers) of all selected contacts
	 */
	public List<String> getSelectedItems()
	{

		List<String> ids = new ArrayList<String>();

		for (ContactLayoutData cdata : contactPositionMap.values())
		{
			if (cdata.isChecked)
			{
				ids.add(cdata.idContact);
			}
		}

		return ids;
	}

	/**
	 * Unchecks all contacts.
	 * 取消选中所有联系人。
	 */
	public void uncheckAllContacts()
	{
		for (ContactLayoutData data : contactPositionMap.values())
		{
			data.isChecked = false;
			myMapView.invalidate();
		}
	}

	/**
	 * Initialize the adapter by populating it with all available contacts and
	 * by creating the info for each item. Every other modification shall be
	 * incremental.
	 * 初始化适配器填充所有可用的接触，并通过创建每个项目的详细信息。
	 * 所有其他的修改应是渐进的。
	 */
	public final void initializePositions()
	{
		Map<String, Contact> localContactMap = ContactManager.getInstance().getAllContacts();
		Contact myContact = ContactManager.getInstance().getMyContact();
		ContactLocation myCloc = ContactManager.getInstance().getMyContactLocation();

		ContactLayoutData myCl = new ContactLayoutData(myContact.getName(), myContact.getPhoneNumber(), myCloc, true);
		myCl.isMyContact = true;
		myCl.isVisible = isValid(myCloc);
		contactPositionMap.put(myCl.idContact, myCl);

		for (Map.Entry<String, Contact> contactEntry : localContactMap.entrySet())
		{
			String phoneNum = contactEntry.getKey();
			Contact currentC = contactEntry.getValue();
			// empty location for invisible contact
			ContactLayoutData cdata = new ContactLayoutData(currentC.getName(), phoneNum, new ContactLocation(((JChatApplication) ((Activity) ctn).getApplication()).getProperty(JChatApplication.LOCATION_PROVIDER)), false);
			contactPositionMap.put(phoneNum, cdata);
		}

	}

	/**
	 * Update all contacts location based on the changes notified by the agent
	 * (contacts added and contacts removed)
	 * 根据代理通知更改，更新所有接触位置（联系人和联系删除）
	 * @param changes
	 *            list of changes
	 * @param locationMap
	 * @param contactMap
	 */
	public void update(ContactListChanges changes, Map<String, Contact> contactMap, Map<String, ContactLocation> locationMap)
	{
		//先删除 再添加 最后更新
		ContactLocation cMyLoc = ContactManager.getInstance().getMyContactLocation();

		// myLogger.log(Logger.INFO,
		// "It's time for updating the contactsPositionOverlay!!!!!");

		// Removed contacts
		for (String removedId : changes.contactsDeleted)
		{
			contactPositionMap.remove(removedId);
		}

		// Added contacts
		for (String addedId : changes.contactsAdded)
		{

			ContactLayoutData newData = new ContactLayoutData(contactMap.get(addedId).getName(), addedId, locationMap.get(addedId), true);
			newData.isVisible = isValid(locationMap.get(addedId));
			contactPositionMap.put(addedId, newData);
		}

		// update all others
		for (ContactLayoutData cData : contactPositionMap.values())
		{
			ContactLocation lastLocation = null;
			Contact curContact = null;

			if (cData.isMyContact)
			{

				// update contact visibility
				lastLocation = cMyLoc;
				cData.isVisible = isValid(lastLocation);
				// myLogger.log(Logger.INFO,
				// "Ok... Ready to update location of my contact!!!!!");
				cData.updateLocation((int) (lastLocation.getLatitude() * 1E6), (int) (lastLocation.getLongitude() * 1E6), (int) (lastLocation.getAltitude() * 1E6));

			} else
			{
				curContact = contactMap.get(cData.idContact);
				if (curContact != null && curContact.isOnline())
				{
					lastLocation = locationMap.get(cData.idContact);
					if (isValid(lastLocation))
					{
						cData.isVisible = true;
						cData.updateLocation((int) (lastLocation.getLatitude() * 1E6), (int) (lastLocation.getLongitude() * 1E6), (int) (lastLocation.getAltitude() * 1E6));
					}
				} else
				{
					cData.isVisible = false;
				}
			}

		}

	}

	private boolean isValid(Location loc)
	{
		//是否有效值
		return (loc.getLatitude() != Double.POSITIVE_INFINITY && loc.getLongitude() != Double.POSITIVE_INFINITY && loc.getAltitude() != Double.POSITIVE_INFINITY);
	}
}
