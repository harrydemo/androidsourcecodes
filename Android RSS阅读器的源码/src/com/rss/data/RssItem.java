package com.rss.data;

public class RssItem {
	public static final String TITLE = "title";
	public static final String PUBDATE = "pubDate";
	private String title;
	private String description;
	private String link;
	private String source;
	private String pubDate;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	
	public String toString() {
		if(title.length() > 20) {
			return title.substring(0, 42) + "...";
		}else {
			return title;
		}
	}
	
}
