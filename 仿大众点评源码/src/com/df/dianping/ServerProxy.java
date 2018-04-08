package com.df.dianping;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Parcelable.Creator;

public class ServerProxy
{
	private int i = 0;
	private List<Map<String, Object>> list;
	private Object obj;
	
	public void sendRequest(final ServerListener listener)
	{	
		final int num = i;
		if(i < 5)
		{
			Thread thread = new Thread() 
			{
				public void run() 
				{
					try
					{
						this.sleep(1000);
					}
					catch(Exception e)
					{
						
					}
					
					list = createData(num);
					if(i == 4)
					{
						listener.serverDataArrived(list, true);
					}
					else
					{
						listener.serverDataArrived(list, false);
					}
					
				}
			};
			thread.start();
			i++;
		}
		
		
	}
	
	private List<Map<String, Object>> createData(int i)
	{
		List<Map<String, Object>> list = new LinkedList<Map<String, Object>>();
		
		if(i == 0)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "Mo-Mo����");
			map.put("price", "�˾����0�6147");
			map.put("addr", "����· ��ʽ����");
			map.put("distance", "5.8km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "������");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "����");
			map.put("price", "�˾����0�6285");
			map.put("addr", "���� �ձ�����");
			map.put("distance", "890m");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "������");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "���߳�����ɽ��ȫ�㣨���궫·�꣩");
			map.put("price", "�˾����0�660");
			map.put("addr", "����㳡 ����");
			map.put("distance", "8.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", true);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "������");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "�̲����");
			map.put("price", "�˾����0�660");
			map.put("addr", "³Ѹ��԰ �����");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "բ����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "�������");
			map.put("price", "�˾����0�669");
			map.put("addr", "����· �����");
			map.put("distance", "5.0km");
			map.put("tuan", true);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "բ����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "�����ձ�����������");
			map.put("price", "�˾����0�6240");
			map.put("addr", "����㳡 �ձ�����");
			map.put("distance", "7.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�ֶ�����");
			list.add(map);
		}
		
		if(i == 1)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "羿�");
			map.put("price", "�˾����0�6136");
			map.put("addr", "��ʽ���");
			map.put("distance", "2.0km");
			map.put("tuan", true);
			map.put("promo", false);
			map.put("card", true);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "����");
			map.put("price", "�˾����0�6324");
			map.put("addr", "���ص��� �ձ�����");
			map.put("distance", "4.0km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "������");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "�Ķ�������");
			map.put("price", "�˾����0�676");
			map.put("addr", "��һ� ��ʽ���");
			map.put("distance", "4.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "�ɽ���");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "���ż�");
			map.put("price", "�˾����0�656");
			map.put("addr", "��վ �����");
			map.put("distance", "8.0km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "��ɽ��");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "���߳�����ɽ��ȫ�㣨�Ͼ���·�꣩");
			map.put("price", "�˾����0�663");
			map.put("addr", "�Ͼ���· ����");
			map.put("distance", "6.5km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", true);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "�ɽ���");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "�ٺϸ�������ʳ�ܻ㣨���¹�꣩");
			map.put("price", "�˾����0�6246");
			map.put("addr", "������ ������");
			map.put("distance", "6.6km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "��ɽ��");
			list.add(map);
		}
		
		if(i == 2)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "��Ǯ�����Ӱ���·�꣩");
			map.put("price", "�˾����0�6224");
			map.put("addr", "����԰/���Ż��� ������");
			map.put("distance", "2.9km");
			map.put("tuan", true);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "�����");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "���趫���������֮�ε꣩");
			map.put("price", "�˾����0�627");
			map.put("addr", "³Ѹ��԰ ��ʽ���");
			map.put("distance", "6.8km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�����");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "BLACK MAGIC CHOCOLATE");
			map.put("price", "�˾����0�648");
			map.put("addr", "������ ��ʽ���");
			map.put("distance", "6.8km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "������");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "70�󷹰�");
			map.put("price", "�˾����0�662");
			map.put("addr", "����· ���ｭ���");
			map.put("distance", "5.2km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "������");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "ϲ���ݹ��ʺ��ʻ�Ʒ");
			map.put("price", "�˾����0�6227");
			map.put("addr", "½���� ������");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "Ү�������½��ˣ���ͨ�꣩��");
			map.put("price", "�˾����0�669");
			map.put("addr", "��ǳ�/��ѧ�� �½�/����");
			map.put("distance", "6.6km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�����");
			list.add(map);
		}
		
		if(i == 3)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "��");
			map.put("price", "�˾����0�667");
			map.put("addr", "������ ������");
			map.put("distance", "5.4km");
			map.put("tuan", true);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "������");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "̩���󣨺����֮�ε꣩");
			map.put("price", "�˾����0�671");
			map.put("addr", "³Ѹ��԰ �����ǲ�");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "������");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "��������");
			map.put("price", "�˾����0�629");
			map.put("addr", "����㳡 ��ͼ��");
			map.put("distance", "8.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "������");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "Сɽ�ձ�����");
			map.put("price", "�˾����0�6183");
			map.put("addr", "����� �ձ�����");
			map.put("distance", "7.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�ֶ�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "Ī����ţ�ŷ�");
			map.put("price", "�˾����0�6602");
			map.put("addr", "½���� ţ��");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 05);
			map.put("area", "�ֶ�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "СС��԰");
			map.put("price", "�˾����0�669");
			map.put("addr", "��һ� ����");
			map.put("distance", "3.7km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "�ֶ�����");
			list.add(map);
		}
		
		if(i == 4)
		{
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("name", "����");
			map.put("price", "�˾����0�6103");
			map.put("addr", "��ɽ��԰ �ձ�����");
			map.put("distance", "2.4km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "�����");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "Ҭ������");
			map.put("price", "�˾����0�6179");
			map.put("addr", "������ ̩����");
			map.put("distance", "5.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�����");
			list.add(map);

			map = new HashMap<String, Object>();
			map.put("name", "ө���˼�");
			map.put("price", "�˾����0�6137");
			map.put("addr", "������ �����");
			map.put("distance", "7.1km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "������������·�꣩");
			map.put("price", "�˾����0�6457");
			map.put("addr", "����ѧԺ �ձ��տ�");
			map.put("distance", "5.1km");
			map.put("tuan", false);
			map.put("promo", false);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 50);
			map.put("area", "�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "ׯԴ");
			map.put("price", "�˾����0�6224");
			map.put("addr", "����� ��������");
			map.put("distance", "7.4km");
			map.put("tuan", true);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 40);
			map.put("area", "�����");
			list.add(map);
			
			map = new HashMap<String, Object>();
			map.put("name", "���ӣ�½����꣩");
			map.put("price", "�˾����0�6206");
			map.put("addr", "½���� �ձ�����");
			map.put("distance", "10km");
			map.put("tuan", false);
			map.put("promo", true);
			map.put("card", false);
			map.put("checkin", false);
			map.put("star", 45);
			map.put("area", "�����");
			list.add(map);
		}	
		return list;
	}
}