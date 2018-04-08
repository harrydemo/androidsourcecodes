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
 * ���οؼ������ṩ��
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
		TreeElement fisrtelement1 = new TreeElement("01", "�û�����Ϳؼ�");  
        TreeElement fisrtelement2 = new TreeElement("02", "�˵��ͶԻ���");  
        TreeElement fisrtelement3 = new TreeElement("03", "2D����ʾ��");  
        nodes.add(fisrtelement1);  
        nodes.add(fisrtelement2);  
        nodes.add(fisrtelement3);
        
        TreeElement secondelement1 = new TreeElement("01_01", "android�еĳ����ؼ�"); 
        TreeElement secondelement2 = new TreeElement("01_02", "android�е������ؼ�");
        TreeElement secondelement3 = new TreeElement("01_03", "android ����");
        fisrtelement1.addChild(secondelement1);
        fisrtelement1.addChild(secondelement2);
        fisrtelement1.addChild(secondelement3);
        
        TreeElement thirdelement1 = new TreeElement("01_01_01", "�ı��ؼ�"); 
        TreeElement  thirdelement2 = new TreeElement("01_01_02", "��ť�ؼ�");
        TreeElement  thirdelement3 = new TreeElement("01_01_03", "�б�ؼ�");
        TreeElement  thirdelement4 = new TreeElement("01_01_04", "����ؼ�");
        TreeElement  thirdelement5 = new TreeElement("01_01_05", "���ں�ʱ��ؼ�");
        secondelement1.addChild(thirdelement1);
        secondelement1.addChild(thirdelement2);
        secondelement1.addChild(thirdelement3);
        secondelement1.addChild(thirdelement4);
        secondelement1.addChild(thirdelement5);
        
        TreeElement fourelement1 = new TreeElement("01_01_01_01", "TextView�ؼ�",getIntent(TextViewActivity.class));
        TreeElement fourelement2 = new TreeElement("01_01_01_02", "EditTextView�ؼ�",getIntent(EditTextViewActivity.class));
        thirdelement1.addChild(fourelement1);
        thirdelement1.addChild(fourelement2);
        
        TreeElement thirdelement0301 = new TreeElement("01_03_01", "LinearLayout����ʾ��",getIntent(LinearLayoutActivity.class));
        TreeElement thirdelement0302 = new TreeElement("01_03_02", "TableLayout����ʾ��",getIntent(TableLayoutActivity.class));
        TreeElement thirdelement0303 = new TreeElement("01_03_03", "LinearLayout���Ӳ���ʾ��",getIntent(LinearLayoutComplexActivity.class));
        TreeElement thirdelement0304 = new TreeElement("01_03_04", "Linear Table ��ϲ���",getIntent(Linear_Table_LayoutComplexActivity.class));
        TreeElement thirdelement0305 = new TreeElement("01_03_05", "RelitaveLayout ����",getIntent(RelativeLayoutActivity.class));
        TreeElement thirdelement0306 = new TreeElement("01_03_06", "RelitaveLayout����ʾ��",getIntent(RelativeLayoutComplexActivity.class));
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
