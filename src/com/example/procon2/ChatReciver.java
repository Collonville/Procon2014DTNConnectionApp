package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.msgpack.MessagePack;

import android.os.Handler;

public class ChatReciver {
	private static Handler handler;
	
	private static DatagramSocket recSocket;
	                     
	private String deviceName;  
	private String deviceIP;    
	private String chatMessage; 
	private String time;       
	private String hash;   
	private String latitude;
	private String longitude;
	
	private MessageInfo messageInfo;
	private static boolean readConstactor = true;

	public ChatReciver() {
		if(readConstactor) {
			handler = new Handler();
	        try {
				recSocket = new DatagramSocket(51000);
			} catch(SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        readConstactor = false;
		}
	}
	
	public void startChatRecive() {
		 new Thread(new Runnable() {
			@Override
            public void run() {
            	while(true) {
    	        	try {
    	            	byte []buf = new byte[1024];
    	            	MessagePack msgpack = new MessagePack();
    	        		DatagramPacket packet= new DatagramPacket(buf,buf.length);
    	        		
    	        		recSocket.receive(packet);
    	        		
    	        		messageInfo = (MessageInfo)msgpack.read(buf,MessageInfo.class);
    	        		
    	        		deviceName = messageInfo.deviceName[0];
    	        		deviceIP = messageInfo.deviceIP[0];
    	        		chatMessage = messageInfo.chatMessage[0];
    	        		time = messageInfo.time[0];
    	        		hash = messageInfo.hash[0];
    	        		latitude = messageInfo.latitude[0];
    	        		longitude = messageInfo.longitude[0];
    	        		
    	            }catch(Exception e){
    	            	
    	            }
            	
	                handler.post(new Runnable() {
		                @Override
		                public void run(){
		                	ChatActivity.pushChatMessage(chatMessage, deviceName, deviceIP, hash, time);
		                	DTNMessageCollection.addData(deviceName, deviceIP, chatMessage, time, hash, latitude, longitude);
		                }
	                });
            	}
            }
          }).start();	
	}
}
