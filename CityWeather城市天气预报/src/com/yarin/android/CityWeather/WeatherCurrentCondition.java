package com.yarin.android.CityWeather;
//ʵʱ������Ϣ����(��ǰ������Ϣ)
/**
<current_conditions>
<condition data="����"/>
<temp_f data="88"/>
<temp_c data="31"/>
<humidity data="ʪ�ȣ� 58%"/>
<icon data="/ig/images/weather/cn_cloudy.gif"/>
<wind_condition data="���� �������٣�4 ��/��"/>
</current_conditions>
*/

public class WeatherCurrentCondition
{

	private String	condition;			// ����
	private String	temp_celcius;		// �����¶�
	private String	temp_fahrenheit;	// �����¶�
	private String	humidity;			// ʪ��:58%
	private String	wind_condition;		// ����...
	private String	icon;				// ͼ��


	public WeatherCurrentCondition()
	{

	}


	//�õ�Condition�����ƣ�
	public String getCondition()
	{
		return condition;
	}

	//����Condition�����ƣ�
	public void setCondition(String condition)
	{
		this.condition = condition;
	}

	//�õ������¶�
	public String getTemp_c()
	{
		return temp_celcius;
	}

	//�õ������¶�
	public String getTemp_f()
	{
		return temp_fahrenheit;
	}

	//���������¶�
	public void setTemp_celcius(String temp_celcius)
	{
		this.temp_celcius = temp_celcius;
	}

	//���û����¶�
	public void setTemp_fahrenheit(String temp_fahrenheit)
	{
		this.temp_fahrenheit = temp_fahrenheit;
	}

	//�õ���ʪ��:58%��
	public String getHumidity()
	{
		return humidity;
	}

	//���ã�ʪ��:58%��
	public void setHumidity(String humidity)
	{
		this.humidity = humidity;
	}

	//�õ�����ָʾ
	public String getWind_condition()
	{
		return wind_condition;
	}

	//���÷���ָʾ
	public void setWind_condition(String wind_condition)
	{
		this.wind_condition = wind_condition;
	}

	//�õ�ͼ���ַ
	public String getIcon()
	{
		return icon;
	}

	//����ͼ���ַ
	public void setIcon(String icon)
	{
		this.icon = icon;
	}

	//�õ�һ����װ������ַ�����������icno������ж���
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ʵʱ����: ").append(temp_celcius).append(" ��C");
		sb.append(" ").append(temp_fahrenheit).append(" F");
		sb.append(" ").append(condition);
		sb.append(" ").append(humidity);
		sb.append(" ").append(wind_condition);
		return sb.toString();
	}
}
