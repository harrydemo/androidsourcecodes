package cn.com.karl.domain;

import java.util.List;

public class Dict {

	private String key;   //ԭ��
	private String ps;    //��¼
	private String pron;  //������ַ
	private String pos;    //��������
	private String acceptation;  //����
	private List<Sent> sents;   //����
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public String getPron() {
		return pron;
	}
	public void setPron(String pron) {
		this.pron = pron;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getAcceptation() {
		return acceptation;
	}
	public void setAcceptation(String acceptation) {
		this.acceptation = acceptation;
	}
	public List<Sent> getSents() {
		return sents;
	}
	public void setSents(List<Sent> sents) {
		this.sents = sents;
	}
	
	
	
}
