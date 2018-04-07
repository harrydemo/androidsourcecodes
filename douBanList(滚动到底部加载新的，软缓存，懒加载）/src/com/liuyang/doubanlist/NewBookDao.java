package com.liuyang.doubanlist;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class NewBookDao {
	public static List<NewBook> getAllNewBooks() throws Exception{
		List<NewBook> list = new ArrayList<NewBook>();
		URL url = new URL("http://book.douban.com/latest");
		URLConnection conn = url.openConnection();
		Source source = new Source(conn);
		List<Element> liList = source.getAllElements("li");
		for(Element element:liList){
			 List<Element>  childrenList = element.getChildElements();
			 if(childrenList.size()==2&&"div".equals(childrenList.get(0).getName())&&"a".equals(childrenList.get(1).getName())){
				 NewBook newBook = new NewBook();
				 String name = childrenList.get(0).getAllElements().get(0).getTextExtractor().toString();
				 String message = childrenList.get(0).getAllElements().get(1).getTextExtractor().toString();
				 String synopsis = childrenList.get(0).getAllElements().get(2).getTextExtractor().toString();
				 String path = childrenList.get(1).getAllElements().get(1).getAttributeValue("src");
				 newBook.setBookName(name);
				 newBook.setBookMessage(message);
				 newBook.setBookSynopsis(synopsis);
				 newBook.setBookPicturePath(path);
				 list.add(newBook);
			 }
		}
		return list;
	}
}
