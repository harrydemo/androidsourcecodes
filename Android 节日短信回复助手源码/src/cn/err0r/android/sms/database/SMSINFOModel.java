package cn.err0r.android.sms.database;

import java.io.Serializable;

public class SMSINFOModel implements Serializable {
	
	private int _sid;
	private String _pn;
	private String _who;
	public String get_who() {
		return _who;
	}
	public void set_who(String _who) {
		this._who = _who;
	}
	private String _body;
	private String _getdate;
	
	public int get_sid() {
		return _sid;
	}
	public void set_sid(int _sid) {
		this._sid = _sid;
	}
	public String get_pn() {
		return _pn;
	}
	public void set_pn(String _pn) {
		this._pn = _pn;
	}
	public String get_body() {
		return _body;
	}
	public void set_body(String _body) {
		this._body = _body;
	}
	public String get_getdate() {
		return _getdate;
	}
	public void set_getdate(String _getdate) {
		this._getdate = _getdate;
	}

}
