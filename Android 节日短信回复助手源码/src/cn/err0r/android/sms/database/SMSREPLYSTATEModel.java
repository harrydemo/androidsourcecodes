package cn.err0r.android.sms.database;

public class SMSREPLYSTATEModel {
	private int _rid;
	private int _sid;
	private int _state;
	private String _body;
	private String _date;
	
	public int get_rid() {
		return _rid;
	}
	public void set_rid(int _rid) {
		this._rid = _rid;
	}
	public int get_sid() {
		return _sid;
	}
	public void set_sid(int _sid) {
		this._sid = _sid;
	}
	public int get_state() {
		return _state;
	}
	public void set_state(int _state) {
		this._state = _state;
	}
	public String get_body() {
		return _body;
	}
	public void set_body(String _body) {
		this._body = _body;
	}
	public String get_date() {
		return _date;
	}
	public void set_date(String _date) {
		this._date = _date;
	}
	
	
}
