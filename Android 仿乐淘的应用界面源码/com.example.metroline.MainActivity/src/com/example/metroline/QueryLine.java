package com.example.metroline;

import java.util.ArrayList;
import java.util.List;

/**
 * ��ѯ·��
 * 
 * @author Administrator
 * 
 */
public class QueryLine {
	public int count;
	public int size;
	public int index;
	public boolean zhaoDao = false;
	// ��һ��·
	public String[] str1 = null;
	// ��һ����·
	public String[] str2 = null;
	// ��·�����ı��
	String[] temp;
	// ��¼��������·
	List<String> recordsLine = new ArrayList<String>();
	// ��¼�����ĳ�վ
	List<String> recordsStation = new ArrayList<String>();
    
	// �����·���α�
	public int getCursor(String[] str, String comparisonStr) {
		for (int i = 0, j = str.length; i < j; i++) {
			// �ҵ������·���±�
			if (str[i].equals(comparisonStr)) {
				return i;
			}

		}
		return 0;
	}

	public boolean isFist = true;

	// ��ѯ�Ƿ����ظ��ĳ�վ��
	// ����1�� ��1����·��վ�� ����2����һ����· ����3 ����һ����· ����4 ��·����
	public boolean find(String str, String[] s1, String[] s2, String lineName1,
			String lineName2) {
		size = s1.length;
		boolean result = false;
		for (int i = 0; i < s1.length; i++) {
			if (str.equals(s1[i])) {
			

					if (s1.length > s2.length) {
						for (int j = 0; j < s2.length; j++) {
							if (str.equals(s2[j])) {
								if((s2.length==Data.lineStation[staendCursor].length)&&s2[0].equals(Data.lineStation[staendCursor][0]))
								{
									result = true;
									temp = s2;
									return  result;
								}
								else{
								// ��3����·���ҵ����ظ���
								recordsLine.add(lineName2);
								recordsStation.add(str);
							//	System.out.println(str);
							//	System.out.println("Linename" + lineName1);
								temp = s2;
								result = true;
								return  result;
								}
							}
						}
						
					} else {
						for (int j = 0; j < s1.length; j++) {
							if (str.equals(s2[j])) {
							     //����ж�˵�� ���һ����·Ҳ�Ѿ���ͨ
								if((s2.length==Data.lineStation[staendCursor].length)&&s2[0].equals(Data.lineStation[staendCursor][0]))
								{
									result = true;
									temp = s2;
									return  result;
								}
								else{
								// ��3����·���ҵ����ظ���
								recordsLine.add(lineName2);
								recordsStation.add(str);
								result = true;
								temp = s2;
								return  result;
								}
							}
						}
						
					}

				}
			
		}
		index++;
		// System.out.println("index:"+index +"   size"+size+" 's1"+s1.length);

		if (index < size) {

			find(s1[index], s1, s2, lineName1, lineName2);
		}
		return result;
	}

	String[] str3;
	int staingCursor;
	int staendCursor;
	public boolean queryLine(String staing, String staingSite, String strEnd,
			String endSite) {

		// �����·���α�

		 staingCursor = getCursor(Data.lineName, staing);

		// ������·���α�
		 staendCursor = getCursor(Data.lineName, strEnd);
		// �������е���·��
		for (int i = 0, j = Data.lineName.length; i < j; i++) {
			count = i + 1;
			if (i != staingCursor && count < j) {
				if (i == 0) {
					// �����ʼ��վ������վ��
					str1 = Data.lineStation[staingCursor];
					str2 = Data.lineStation[i];
					str3 = Data.lineStation[i + 1];
				} else if (i != 0) {
					// �����ʼ��վ������վ��
					if (str1 == null) {
						// �����ʼ��վ������վ��
						str1 = Data.lineStation[staingCursor];
						str2 = Data.lineStation[i];
						str3 = Data.lineStation[i + 1];
					} else {
						str1 = temp;
						str2 = Data.lineStation[i];
						str3 = Data.lineStation[i + 1];
					}
				}

				if (str1.length > str2.length) {
					// size = str1.length;
					temp = str2;
					if (find(str1[0], str2, str3, Data.lineName[i],
							Data.lineName[i + 1])) {
						System.out.println("i:"+i);
						// �ҵ��յ�վ��
						if (staendCursor == i) {
							zhaoDao = true;
							break;
						}
						continue;
					}
				} else {
					// size = str2.length;
					temp = str2;
					if (find(str2[0], str1, str3, Data.lineName[i],
							Data.lineName[i + 1])) {
						System.out.println("i:"+i);
						// �ҵ��յ�վ��
						if (staendCursor == i) {
							zhaoDao = true;
							break;
						}
						continue;
					}

				}
			}
		}
		return zhaoDao;
	}

}
