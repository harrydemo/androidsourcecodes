package com.example.dairyclient;

import java.net.Socket;

import android.app.Application;

public class DairyApp extends Application {

	private Socket theconnSocket;
	private String theUserString;
	
	public Socket getSocket()
	{
		return theconnSocket;
	}
	public void setSocket(Socket thesocket)
	{
		theconnSocket=thesocket;
	}
	public String getUser()
	{
		return theUserString;
	}
	public void setUser(String theuid)
	{
		theUserString=theuid;
	}
	
}
