package com.android.angle;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

/**
 * XML Object base class
 * @author Ivan Pajuelo
 *
 */
public abstract class XMLClass
{
	protected String mCommand;
	protected AngleActivity mActivity;

	public abstract void setCommandDefaults();

	public abstract void setCommandParam(String param, String value);

	public abstract void setCommandValue(String value);

	public abstract void setFieldDefaults(String field);

	public abstract void setFieldParam(String field, String param, String value);

	public abstract void setFieldValue(String field, String value);

	public XMLClass(AngleActivity activity)
	{
		mActivity = activity;
	}

	public void execute()
	{
		Log.d("XML", "class.execute");
		setCommandDefaults();
		mCommand = mActivity.xmlParser.getName().toLowerCase();
		getParams();
		try
		{
			if (mActivity.xmlParser.next() == XmlPullParser.TEXT)
			{
				setCommandValue(mActivity.xmlParser.getText());
				mActivity.xmlParser.next(); // Skip text end_tag
			}
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		getFields();
	}

	private void getParams()
	{
		for (int t = 0; t < mActivity.xmlParser.getAttributeCount(); t++)
			setCommandParam(mActivity.xmlParser.getAttributeName(t).toLowerCase(), mActivity.xmlParser.getAttributeValue(t).toLowerCase());
	}

	private void getFields()
	{
		try
		{
			do
			{
				while ((mActivity.xmlParser.getEventType() != XmlPullParser.START_TAG)&&(mActivity.xmlParser.getEventType()==XmlPullParser.END_DOCUMENT))
					mActivity.xmlParser.next();// skip comments
				if ((mActivity.xmlParser.getEventType()!=XmlPullParser.END_DOCUMENT)&&(mActivity.xmlParser.getEventType()!=XmlPullParser.END_TAG))
				{
					String fieldName = mActivity.xmlParser.getName().toLowerCase();
					setFieldDefaults(fieldName);
					for (int t = 0; t < mActivity.xmlParser.getAttributeCount(); t++)
						setFieldParam(fieldName, mActivity.xmlParser.getAttributeName(t).toLowerCase(), mActivity.xmlParser.getAttributeValue(t));
					mActivity.xmlParser.next(); // Go to text or end_tag
					if (mActivity.xmlParser.getEventType() == XmlPullParser.TEXT)
					{
						setFieldValue(fieldName, mActivity.xmlParser.getText());
						mActivity.xmlParser.next(); // Skip text end_tag
					}
					mActivity.xmlParser.next();// next field;
				}
			}
			while (mActivity.xmlParser.getEventType() != XmlPullParser.END_TAG);
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
