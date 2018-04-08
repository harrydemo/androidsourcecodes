package com.shinylife.smalltools.api;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.shinylife.smalltools.entity.AddressInfo;
import com.shinylife.smalltools.entity.AppUpdateInfo;
import com.shinylife.smalltools.entity.IDCardInfo;
import com.shinylife.smalltools.entity.PhoneInfo;
import com.shinylife.smalltools.helper.Constants;
import com.shinylife.smalltools.helper.HttpClientHelper;
import com.shinylife.smalltools.helper.HttpException;
import com.shinylife.smalltools.helper.HttpResponse;

public class ApiImpl {
	public AddressInfo getAddressInfo(String no) {
		try {
			String response = HttpRequest(String.format(Constants.API_URL,
					"zip", URLEncoder.encode(no)));
			if (response != null && response.length() > 0) {
				AddressInfo ai = new AddressInfo();
				JSONObject jb = new JSONObject(response);
				ai.setCity(jb.getString("city"));
				ai.setLocation(jb.getString("location"));
				ai.setPhone(jb.getString("phone"));
				ai.setZipcode(jb.getString("zipcode"));
				ai.setProvince(jb.getString("province"));
				return ai;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public IDCardInfo getIDCardInfo(String no) {
		try {
			String response = HttpRequest(String.format(Constants.API_URL,
					"id", no));
			if (response != null && response.length() > 0) {
				IDCardInfo ai = new IDCardInfo();
				JSONObject jb = new JSONObject(response);
				ai.setBirthday(jb.getString("birthday"));
				ai.setCode(jb.getString("code"));
				ai.setGender(jb.getString("gender").equals("m") ? "ÄÐ" : "Å®");
				ai.setLocation(jb.getString("location"));
				return ai;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public PhoneInfo getPhoneInfo(String no) {
		try {
			String response = HttpRequest(String.format(Constants.API_URL,
					"mobile", no));
			if (response != null && response.length() > 0) {
				PhoneInfo ai = new PhoneInfo();
				JSONObject jb = new JSONObject(response);
				ai.setPhonenum(jb.getString("phonenum"));
				ai.setLocation(jb.getString("location"));
				return ai;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	public AppUpdateInfo getNewVersion(String curVersionNo) {
		try {
			String response = HttpRequest(String.format(
					Constants.CHECK_VERSION_API_URL, curVersionNo));
			if (response != null && response.length() > 0) {
				AppUpdateInfo ai = new AppUpdateInfo();
				JSONObject jb = new JSONObject(response);
				ai.setHasNewVersion(jb.getBoolean("hasnewversion"));
				ai.setLastVersion(jb.getString("lastversion"));
				ai.setUrl(jb.getString("appurl"));
				ai.setAppSize(jb.getString("appsize"));
				return ai;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	private String HttpRequest(String url) throws HttpException {
		HttpClientHelper client = new HttpClientHelper();
		HttpResponse response = client.get(url, false);
		if (response != null) {
			return response.asString();
		}
		return null;
	}
}
