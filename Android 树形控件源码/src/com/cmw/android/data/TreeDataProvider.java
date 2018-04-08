package com.cmw.android.data;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.cmw.android.model.TreeElement;
import com.cmw.android.widgets.examples.EditTextViewActivity;
import com.cmw.android.widgets.examples.LinearLayoutActivity;
import com.cmw.android.widgets.examples.LinearLayoutComplexActivity;
import com.cmw.android.widgets.examples.Linear_Table_LayoutComplexActivity;
import com.cmw.android.widgets.examples.RelativeLayoutActivity;
import com.cmw.android.widgets.examples.RelativeLayoutComplexActivity;
import com.cmw.android.widgets.examples.TableLayoutActivity;
import com.cmw.android.widgets.examples.TextViewActivity;
/**
 * 树形控件数据提供类
 * @author chengmingwei
 *
 */
public class TreeDataProvider implements DataProvider{
	private Context context=null;
	private  List<TreeElement> nodes = new ArrayList<TreeElement>();
	public TreeDataProvider(Context context) {
		super();
		this.context = context;
	}

	private void initDataSource(){
		TreeElement fisrtelement1 = new TreeElement("01", "用户界面和控件");  
        TreeElement fisrtelement2 = new TreeElement("02", "菜单和对话框");  
        TreeElement fisrtelement3 = new TreeElement("03", "2D动画示例");  
        nodes.add(fisrtelement1);  
        nodes.add(fisrtelement2);  
        nodes.add(fisrtelement3);
        
        TreeElement secondelement1 = new TreeElement("01_01", "android中的常见控件"); 
        TreeElement secondelement2 = new TreeElement("01_02", "android中的其它控件");
        TreeElement secondelement3 = new TreeElement("01_03", "android 布局");
        fisrtelement1.addChild(secondelement1);
        fisrtelement1.addChild(secondelement2);
        fisrtelement1.addChild(secondelement3);
        
        TreeElement thirdelement1 = new TreeElement("01_01_01", "文本控件"); 
        TreeElement  thirdelement2 = new TreeElement("01_01_02", "按钮控件");
        TreeElement  thirdelement3 = new TreeElement("01_01_03", "列表控件");
        TreeElement  thirdelement4 = new TreeElement("01_01_04", "网格控件");
        TreeElement  thirdelement5 = new TreeElement("01_01_05", "日期和时间控件");
        secondelement1.addChild(thirdelement1);
        secondelement1.addChild(thirdelement2);
        secondelement1.addChild(thirdelement3);
        secondelement1.addChild(thirdelement4);
        secondelement1.addChild(thirdelement5);
        
        TreeElement fourelement1 = new TreeElement("01_01_01_01", "TextView控件",getIntent(TextViewActivity.class));
        TreeElement fourelement2 = new TreeElement("01_01_01_02", "EditTextView控件",getIntent(EditTextViewActivity.class));
        thirdelement1.addChild(fourelement1);
        thirdelement1.addChild(fourelement2);
        
        TreeElement thirdelement0301 = new TreeElement("01_03_01", "LinearLayout布局示例",getIntent(LinearLayoutActivity.class));
        TreeElement thirdelement0302 = new TreeElement("01_03_02", "TableLayout布局示例",getIntent(TableLayoutActivity.class));
        TreeElement thirdelement0303 = new TreeElement("01_03_03", "LinearLayout复杂布局示例",getIntent(LinearLayoutComplexActivity.class));
        TreeElement thirdelement0304 = new TreeElement("01_03_04", "Linear Table 组合布局",getIntent(Linear_Table_LayoutComplexActivity.class));
        TreeElement thirdelement0305 = new TreeElement("01_03_05", "RelitaveLayout 布局",getIntent(RelativeLayoutActivity.class));
        TreeElement thirdelement0306 = new TreeElement("01_03_06", "RelitaveLayout复杂示例",getIntent(RelativeLayoutComplexActivity.class));
        secondelement3.addChild(thirdelement0301);
        secondelement3.addChild(thirdelement0302);
        secondelement3.addChild(thirdelement0303);
        secondelement3.addChild(thirdelement0304);
        secondelement3.addChild(thirdelement0305);
        secondelement3.addChild(thirdelement0306);
	}
	
	private Intent getIntent(Class<?> cls){
		Intent intent = new Intent();
		intent.setClass(context, cls);
		return intent;
	}
	
	
	@Override
	public void foward(String caption) throws Exception {
		
	}

	@Override
	public List<TreeElement> getDataSource() {
		if(null == nodes || nodes.size()==0)initDataSource();
		return nodes;
	}
	
}
