package com.php;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class JsonUtils {
	
	public LinkedList<User> parseUserFromJson(String jsonData){
		
		Type listType = new TypeToken<LinkedList<User>>(){}.getType();
		Gson gson = new Gson();
		LinkedList<User> users = gson.fromJson(jsonData, listType);
		for (Iterator iterator = users.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			System.out.println("id--->" + user.getId());
			System.out.println("title---->" + user.getTitle());
			System.out.println("date---->" + user.getDate());
			System.out.println("address---->" + user.getAddress());
		}
		
	   return users;
	}
}
