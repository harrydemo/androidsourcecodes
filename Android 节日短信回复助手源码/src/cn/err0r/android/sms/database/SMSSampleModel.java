package cn.err0r.android.sms.database;

import java.io.Serializable;

public class SMSSampleModel implements Serializable {
	private String _class;
	private String _body;
	private String _state;
	
	public String get_class() {
		return _class;
	}
	public void set_class(String _class) {
		this._class = _class;
	}
	public String get_body() {
		return _body;
	}
	public void set_body(String _body) {
		this._body = _body;
	}
	public String get_state() {
		return _state;
	}
	public void set_state(String _state) {
		this._state = _state;
	}

}
