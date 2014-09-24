package com.example.procon2;

public class ChatListView {
	public String message;
	public String deviceName;
	public String deviceIP;
	public String time;  
	public String hash;
	  
	public ChatListView(String message, String deviceName, String deviceIP,String time,String hash){
		this.message    = message;
		this.deviceName = deviceName;
		this.deviceIP   = deviceIP;
		this.time       = time;
		this.hash       = hash;
	}  
}
