package com.feicong.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface IQQService{
	public List<Object> queryList(int startIndex, int endIndex);
	public long queryCount();
	public ArrayList<HashMap<String, String>> setAdapterListData(int startIndex, int endIndex);
}
