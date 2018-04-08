package com.cmw.android.model;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

/**
 * 树节点对象
 * @author chengmingwei
 *
 */
public class TreeElement {
	private String id;
	private String title;
	private boolean hasParent;
	private boolean hasChild;
	private TreeElement parent;
	private int Level;
	private List<TreeElement> childs = new ArrayList<TreeElement>();
	private boolean expanded;
	private Intent intent;
	
	public TreeElement(String id,String title) {
		super();
		this.id = id;
		this.title = title;
		this.hasParent = true;
		this.hasChild = false;
	}
	
	public TreeElement(String id,String title,Intent intent) {
		this(id,title);
		this.intent = intent;
	}
	public TreeElement(String id, String title, boolean hasParent,
			boolean hasChild, TreeElement parent, int level,
			 boolean expanded) {
		super();
		this.id = id;
		this.title = title;
		this.hasParent = hasParent;
		this.hasChild = hasChild;
		this.parent = parent;
		Level = level;
		if(null != parent){
			this.parent.getChilds().add(this);
		}
		this.expanded = expanded;
	}
	
	public void addChild(TreeElement node){
		this.hasChild = true;
		this.childs.add(node);
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isHasParent() {
		return hasParent;
	}
	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}
	public boolean isHasChild() {
		return hasChild;
	}
	public void setHasChild(boolean hasChild) {
		this.hasChild = hasChild;
	}
	public TreeElement getParent() {
		return parent;
	}
	public void setParent(TreeElement parent) {
		this.parent = parent;
	}
	public int getLevel() {
		return Level;
	}
	public void setLevel(int level) {
		Level = level;
	}
	public List<TreeElement> getChilds() {
		return childs;
	}
	public void setChilds(List<TreeElement> childs) {
		this.childs = childs;
	}
	public boolean isExpanded() {
		return expanded;
	}
	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public Intent getIntent() {
		return intent;
	}

	public void setIntent(Intent intent) {
		this.intent = intent;
	}
	
	public void forward(Context context){
		if(null == intent) return;
		context.startActivity(intent);
	}
}
