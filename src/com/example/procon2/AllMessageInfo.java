package com.example.procon2;

public class AllMessageInfo {
	private String deviceName  = null; //送信元の端末名
	private String deviceIP    = null; //送信元のIPアドレス
	private String chatMessage = null; //チャットから送られてきたメッセージ
	private String hash        = null; //すべてのデータを含んだハッシュ値
	private String time        = null;
	
	//private String Latitude;  //緯度
	//private String Longitude; //軽度
	
	public AllMessageInfo(String deviceName,String deviceIP,String chatMessage,String hash,String time){
		this.deviceName  = deviceName;
		this.deviceIP    = deviceName;
		this.chatMessage = chatMessage;
		this.hash        = hash;
		this.setTime(time);
	}

	public String getDeviceName() {
		return deviceName;
	}

	public String getDeviceIP() {
		return deviceIP;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public String getHash() {
		return hash;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
