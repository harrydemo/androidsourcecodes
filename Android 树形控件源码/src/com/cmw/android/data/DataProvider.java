package com.cmw.android.data;

import java.util.List;

/**
 * ����Դ�ṩ�ӿ�
 * @author chengmingwei
 *
 */
public interface DataProvider {
	public List<?> getDataSource();
	public void foward(String caption) throws Exception;
}
