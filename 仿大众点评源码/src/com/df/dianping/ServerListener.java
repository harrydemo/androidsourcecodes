package com.df.dianping;

import java.util.List;

public interface ServerListener 
{
	void serverDataArrived(List list, boolean isEnd);
}
