package com.cmw.android.data;

import java.util.List;

/**
 * 数据源提供接口
 * @author chengmingwei
 *
 */
public interface DataProvider {
	public List<?> getDataSource();
	public void foward(String caption) throws Exception;
}
