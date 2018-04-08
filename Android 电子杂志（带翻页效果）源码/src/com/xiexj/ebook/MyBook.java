package com.xiexj.ebook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Paint;
import android.os.Bundle;

public class MyBook extends Activity {
	
	private BookLayout blo;
	
	private int[] imgs = {
			R.drawable.book_cover,
			-1,
			R.drawable.book_page_1,
			R.drawable.book_page_2,
			R.drawable.book_page3,
			R.drawable.book_page_4,
			R.drawable.book_page_5,
			R.drawable.book_page_6,
			R.drawable.author,
			R.drawable.book_back_cover
	};
	
//	private String content = "    大家好，这是我第一次在这个论坛里发帖。学习android也快将近一年了，期间用android做过几个手机项目，以及在工作之余写过一个游戏。前段时间在论坛里有人寻求翻书效果的代码，当时也是抱着尝试一下的态度去做做试试，在工作之余经过3天半的奋战，终于一个漂亮的android翻页效果出炉了";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidUtils.setFullNoTitleScreen(this);
		blo = new BookLayout(this);
		List<SinglePage> singlePageList = new ArrayList<SinglePage>();
		boolean isLeft = false;
		int num = 0;
		for(int i=0;i<imgs.length;i++){
			PageContent content = new PageContent(this,imgs[i],blo);
			if(i==0||i==imgs.length-2||i==imgs.length-1){
				content.setCover(true);
			}else if(i!=1){
				content.setContentPage(true);
				content.setContentId(num++);
			}
			SinglePage page = new SinglePage(this,isLeft,i,blo,content);
			singlePageList.add(page);
			isLeft = !isLeft;
		}
		//Paint m_paint = new Paint();
//		blo.setContentList(AndroidUtils.getPageContentStringInfo(m_paint, content, PageContent.getPageContentLine(BookLayout.PAGE_HEIGHT), PageContent.getPageContentWidth(BookLayout.PAGE_WIDTH)));
		blo.setPageList(singlePageList, -1);
		setContentView(blo);
	}
}
