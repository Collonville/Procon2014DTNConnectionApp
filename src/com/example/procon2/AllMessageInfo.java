package com.example.procon2;

public class AllMessageInfo {
	private String deviceName  = null; //���M���̒[����
	private String deviceIP    = null; //���M����IP�A�h���X
	private String chatMessage = null; //�`���b�g���瑗���Ă������b�Z�[�W
	private String hash        = null; //���ׂẴf�[�^���܂񂾃n�b�V���l
	private String time        = null;
	
	//private String Latitude;  //�ܓx
	//private String Longitude; //�y�x
	
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
