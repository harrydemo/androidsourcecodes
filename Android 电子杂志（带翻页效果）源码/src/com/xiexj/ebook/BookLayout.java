package com.xiexj.ebook;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Path;
import android.widget.FrameLayout;

/**
 * Ŀǰ��Ȼ����ַ����ɹ��������һ������ҳż������������������ּ���̫С���޷������Ŵ�
 * ��ҳ�ɹ��������һ�̣�����ҳ��ҳ����ʱ���������ᣬ���ּ���̫С���޷������Ŵ�
 * @author xiexj
 *
 */

public class BookLayout extends FrameLayout {

	//���е�ҳ��Ϣ
	private List<SinglePage> pageList;
	
	//���Ŀǰ��ʾ����ҳ,���û����-1,˵���Ȿ���Ǻ��ŵ�
	private int currentPage = -1;
	
	//ҳ���
	public static final int PAGE_WIDTH = 300;
	
	//ҳ���
	public static final int PAGE_HEIGHT = 400;
	
	//�ײ�
	private FrameLayout buttomLayout;
	
	//���㣨��ҳ��ʾ�㣩
	private FrameLayout topLayout;
	
	//�ڶ��㣨��ǰҳ��ʾ�㣩
	private FrameLayout secondLayout;
	
	//�����㣨��һҳ��ʾ�㣩
	private FrameLayout thirdLayout;
	
	//�Ƿ����Զ���ҳ
	private boolean isAutoFlip = false;
	
	//��ǰ������ҳ
	private SinglePage currentFlipPage = null;
	
	//�鱾����
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
	 * ������ҳ����ʾ������ҳ������
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
	 * ���õ�ǰ��ʾҳ
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
	 * �Ƿ����鱾�ķ���
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
	 * ��ȡ��ǰ����ҳ�ķ�ҳ
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
	 * �ɹ�������ǰҳ
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
