package com.xiexj.ebook;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Path;
import android.widget.FrameLayout;

/**
 * 目前仍然会出现翻动成功后在最后一刻下面页偶尔会闪动的情况，出现几率太小，无法调试排错
 * 翻页成功后，在最后一刻，翻动页的页边有时会脱离书轴，出现几率太小，无法调试排错
 * @author xiexj
 *
 */

public class BookLayout extends FrameLayout {

	//所有的页信息
	private List<SinglePage> pageList;
	
	//左侧目前显示的书页,如果没有是-1,说明这本书是合着的
	private int currentPage = -1;
	
	//页面宽
	public static final int PAGE_WIDTH = 300;
	
	//页面高
	public static final int PAGE_HEIGHT = 400;
	
	//底层
	private FrameLayout buttomLayout;
	
	//顶层（反页显示层）
	private FrameLayout topLayout;
	
	//第二层（当前页显示层）
	private FrameLayout secondLayout;
	
	//第三层（下一页显示层）
	private FrameLayout thirdLayout;
	
	//是否处于自动翻页
	private boolean isAutoFlip = false;
	
	//当前翻动的页
	private SinglePage currentFlipPage = null;
	
	//书本内容
	private ArrayList<ArrayList<String>> contentList;
	
	public BookLayout(Context context) {
		super(context);
		this.currentPage = -1;
		buttomLayout = new FrameLayout(context);
		buttomLayout.setLayoutParams(LayoutBean.FF);
		this.addView(buttomLayout);
		thirdLayout = new FrameLayout(context);
		thirdLayout.setLayoutParams(LayoutBean.FF);
		this.addView(thirdLayout);
		secondLayout = new FrameLayout(context);
		secondLayout.setLayoutParams(LayoutBean.FF);
		this.addView(secondLayout);
		topLayout = new FrameLayout(context);
		topLayout.setLayoutParams(LayoutBean.FF);
		this.addView(topLayout);
		buttomLayout.setBackgroundResource(R.drawable.book_background);
	}
	
	public void setPageList(List<SinglePage> pageList,int currentPage){
		this.pageList = pageList;
		this.currentPage = currentPage;
		if(currentPage<-1||currentPage==pageList.size()){
			currentPage = -1;
		}
		refresh();
	}
	
	/**
	 * 翻动书页，显示其他两页的内容
	 * @param page
	 * @param maskPagePath
	 * @param resultPagePath
	 */
	void flipPage(SinglePage page,Path maskPagePath,float angle,float shadowAngle,float fx,float fy,float sdx,float sdy,int chooseCorner){
		SinglePage maskPage = getMaskPage(page);
		maskPage.onMaskPathDraw(maskPagePath,angle,shadowAngle,fx,fy,sdx,sdy,chooseCorner);
	}
	
	void refresh(){
		thirdLayout.removeAllViews();
		secondLayout.removeAllViews();
		topLayout.removeAllViews();
		List<SinglePage> showList = new ArrayList<SinglePage>();
		SinglePage leftShowPage = null;
		SinglePage leftMaskPage = null;
		SinglePage leftDownPage = null;
		if(currentPage!=-1){
			leftShowPage = pageList.get(currentPage);
			leftShowPage.reset();
			leftShowPage.getPageContent().create();
			leftShowPage.setLookPage(true);
			showList.add(leftShowPage);
			leftShowPage.setPosition(10, (480-PAGE_HEIGHT)/2);
			leftShowPage.setSize(PAGE_WIDTH,PAGE_HEIGHT);
			leftMaskPage = getMaskPage(leftShowPage);
			leftMaskPage.getPageContent().create();
			leftMaskPage.reset();
			
			showList.add(leftMaskPage);
			leftMaskPage.setPosition(10, (480-PAGE_HEIGHT)/2);
			leftMaskPage.setSize(PAGE_WIDTH, PAGE_HEIGHT);
			if(currentPage-2>=0){
				leftDownPage = pageList.get(currentPage-2);
				leftDownPage.reset();
				leftDownPage.getPageContent().create();
				leftDownPage.setMaskPage(false);
				showList.add(leftDownPage);
				leftDownPage.setPosition(10, (480-PAGE_HEIGHT)/2);
				leftDownPage.setSize(PAGE_WIDTH,PAGE_HEIGHT);
			}
		}
		SinglePage rightShowPage = null;
		SinglePage rightMaskPage = null;
		SinglePage rightDownPage = null;
		int rightPageIndex = currentPage+1;
		if(rightPageIndex<pageList.size()){
			rightShowPage = pageList.get(rightPageIndex);
			rightShowPage.reset();
			rightShowPage.getPageContent().create();
			rightShowPage.setLookPage(true);
			showList.add(rightShowPage);
			rightShowPage.setPosition(10+PAGE_WIDTH, (480-PAGE_HEIGHT)/2);
			rightShowPage.setSize(PAGE_WIDTH, PAGE_HEIGHT);
			rightMaskPage = getMaskPage(rightShowPage);
			rightMaskPage.reset();
			rightMaskPage.getPageContent().create();
			showList.add(rightMaskPage);
			rightMaskPage.setPosition(10+PAGE_WIDTH, (480-PAGE_HEIGHT)/2);
			rightMaskPage.setSize(PAGE_WIDTH, PAGE_HEIGHT);
			if(rightPageIndex+2<pageList.size()){
				rightDownPage = pageList.get(rightPageIndex+2);
				rightDownPage.reset();
				rightDownPage.getPageContent().create();
				rightDownPage.setMaskPage(false);
				showList.add(rightDownPage);
				rightDownPage.setPosition(10+PAGE_WIDTH, (480-PAGE_HEIGHT)/2);
				rightDownPage.setSize(PAGE_WIDTH,PAGE_HEIGHT);
			}
		}
		if(leftShowPage!=null){
			secondLayout.addView(leftShowPage);
		}
		if(rightShowPage!=null){
			secondLayout.addView(rightShowPage);
		}
		if(leftMaskPage!=null){
			topLayout.addView(leftMaskPage);
		}
		if(rightMaskPage!=null){
			topLayout.addView(rightMaskPage);
		}
		if(leftDownPage!=null){
			thirdLayout.addView(leftDownPage);
		}
		if(rightDownPage!=null){
			thirdLayout.addView(rightDownPage);
		}
		for(SinglePage p : pageList){
			if(showList.contains(p)) continue;
			p.getPageContent().destory();
		}
	}
	
	/**
	 * 设置当前显示页
	 * @param page
	 */
	public void setCurrentPage(int page){
		if(page%2==0){
			page -=1;
		}
		currentPage = page;
		refresh();
	}
	
	/**
	 * 是否是书本的封面
	 * @param p
	 * @return
	 */
	public boolean isFirstOrLastPage(SinglePage p){
		boolean flag = false;
		if(pageList!=null){
			SinglePage fp = pageList.get(0);
			if(fp==p){
				flag = true;
			}else{
				SinglePage lp = pageList.get(pageList.size()-1);
				if(lp==p) flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取当前翻动页的反页
	 * @param page
	 * @return
	 */
	SinglePage getMaskPage(SinglePage page){
		int c = pageList.indexOf(page);
		if(c%2!=0){
			return pageList.get(c-1);
		}else{
			return pageList.get(c+1);
		}
	}
	
	/**
	 * 成功翻动当前页
	 * @param page
	 */
	void successFlipPage(SinglePage page,boolean flag){
		if(flag){
			int c = pageList.indexOf(page);
			if(c%2!=0){
				currentPage-=2;
			}else{
				currentPage+=2;
			}
			refresh();
		}else{
			refresh();
		}
	}

	public boolean isAutoFlip() {
		return isAutoFlip;
	}

	public void setAutoFlip(boolean isAutoFlip) {
		this.isAutoFlip = isAutoFlip;
	}

	public SinglePage getCurrentFlipPage() {
		return currentFlipPage;
	}

	public void setCurrentFlipPage(SinglePage currentFlipPage) {
		this.currentFlipPage = currentFlipPage;
	}

	public ArrayList<ArrayList<String>> getContentList() {
		return contentList;
	}

	public void setContentList(ArrayList<ArrayList<String>> contentList) {
		this.contentList = contentList;
	}
}
