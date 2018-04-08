package com.ly.vo;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class UserVO implements HttpSessionBindingListener{
	private String u_username;
	private String u_pwd;
	private String u_email;
	private String u_name;
	private String u_sex;
	private String u_phone;
	private String u_headname;
	private String u_head;
	private String u_job;
	private String u_address;
	private String u_circle;
	private String u_gz;
	
	public String getU_username() {
		return u_username;
	}
	public void setU_username(String uUsername) {
		u_username = uUsername;
	}
	public String getU_pwd() {
		return u_pwd;
	}
	public void setU_pwd(String uPwd) {
		u_pwd = uPwd;
	}
	public String getU_email() {
		return u_email;
	}
	public void setU_email(String uEmail) {
		u_email = uEmail;
	}
	public String getU_name() {
		return u_name;
	}
	public void setU_name(String uName) {
		u_name = uName;
	}
	public String getU_sex() {
		return u_sex;
	}
	public void setU_sex(String uSex) {
		u_sex = uSex;
	}
	public String getU_phone() {
		return u_phone;
	}
	public void setU_phone(String uPhone) {
		u_phone = uPhone;
	}
	public String getU_head() {
		return u_head;
	}
	public void setU_head(String uHead) {
		u_head = uHead;
	}
	
	public String getU_headname() {
		return u_headname;
	}
	public void setU_headname(String uHeadname) {
		u_headname = uHeadname;
	}
	public String getU_job() {
		return u_job;
	}
	public void setU_job(String uJob) {
		u_job = uJob;
	}
	public String getU_address() {
		return u_address;
	}
	public void setU_address(String uAddress) {
		u_address = uAddress;
	}
	public String getU_circle() {
		return u_circle;
	}
	public void setU_circle(String uCircle) {
		u_circle = uCircle;
	}
	public String getU_gz() {
		return u_gz;
	}
	public void setU_gz(String uGz) {
		u_gz = uGz;
	}
	public void valueBound(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
