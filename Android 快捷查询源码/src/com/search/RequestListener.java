package com.search;

/**
 * �Զ���һ���������ӿ�
 * �Ժ����м������඼Ҫʵ�ָýӿ�
 * @author Administrator
 *
 */
public interface RequestListener {
	
	//�ɹ�����ʱ������
	public void onComplete(String result); 
	
	//�����쳣ʱ����
	public void onException(Exception e);

}
