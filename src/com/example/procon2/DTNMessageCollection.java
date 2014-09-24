package com.example.procon2;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class DTNMessageCollection {
	private static List<String> deviceName  = new ArrayList<String>();
	private static List<String> deviceIP    = new ArrayList<String>();
	private static List<String> chatMessage = new ArrayList<String>();
	private static List<String> time        = new ArrayList<String>();
	private static List<String> hash        = new ArrayList<String>();
	private static List<String> latitude    = new ArrayList<String>();
	private static List<String> longitude   = new ArrayList<String>();
	
	public synchronized static void addData(String deviceName, String deviceIP, String chatMessage, String time, String hash, String latitude, String longitude){
		DTNMessageCollection.deviceName.add(deviceName);
		DTNMessageCollection.deviceIP.add(deviceIP);
		DTNMessageCollection.chatMessage.add(chatMessage);
		DTNMessageCollection.time.add(time);
		DTNMessageCollection.hash.add(hash);
		DTNMessageCollection.latitude.add(latitude);
		DTNMessageCollection.longitude.add(longitude);
	}
	
	public synchronized static List<String> findNotSavedData(List<String> input){
		List<String> insideData = new ArrayList<String>(DTNMessageCollection.hash);
		
//		Log.d("Data sabun","自身の端末に入ってるハッシュ値。-------------------------------");
//		for(int i = 0;i < insideData.size(); i++)
//			Log.d("Data sabun",insideData.get(i));
//		Log.d("Data sabun","他の端末から受け取ったハッシュ値。-------------------------------");
//		for(int i = 0;i < input.size(); i++)
//			Log.d("Data sabun",input.get(i));

		for(int i = 0; i < insideData.size(); i++){
			int cnt = 0; 
			for(int j = 0; j < input.size(); j++){
				if(insideData.get(i).equals(input.get(j))){
					//Log.d("一致したハッシュ値",insideData.get(i));
					cnt++;
				}
			}
			if(cnt > 0){
				insideData.remove(i);
				//Log.d("remove","---------------------");
				i--;
			}
		}
		
		return insideData;
	}
	
	public synchronized static List<String> getDTNConnectionInfoFromHash(List<String> input,List<String> findFrom){
		List<String> returnlist = new ArrayList<String>();
		
		for(int i = 0; i < input.size(); i++){
			int index = DTNMessageCollection.hash.indexOf(input.get(i));	
			if(index != -1)
				returnlist.add(findFrom.get(index));
		}
		return returnlist;
	}
	
	public synchronized static List<String> getDeviceNameListfromHash(List<String> input){
		List<String> returnlist = new ArrayList<String>();
		
		for(int i = 0; i < input.size(); i++){
			int index = DTNMessageCollection.hash.indexOf(input.get(i));	
			if(index != -1)
				returnlist.add(DTNMessageCollection.deviceName.get(index));
		}
		return returnlist;
	}
	
	public synchronized static List<String> getchatMessageListfromHash(List<String> input){
		List<String> returnlist = new ArrayList<String>();
		
		for(int i = 0; i < input.size(); i++){
			int index = DTNMessageCollection.hash.indexOf(input.get(i));
			//Log.d("Index",Integer.toString(index));
			//Log.d("IndexChatMessage",DTNMessageCollection.chatMessage.get(index));
			if(index != -1)
				returnlist.add(DTNMessageCollection.chatMessage.get(index));
		}
		return returnlist;
	}
	
	public synchronized static List<String> getIPListfromHash(List<String> input){
		List<String> returnlist = new ArrayList<String>();
		
		for(int i = 0; i < input.size(); i++){
			int index = DTNMessageCollection.hash.indexOf(input.get(i));
			//Log.d("Index",Integer.toString(index));
			if(index != -1)
				returnlist.add(DTNMessageCollection.deviceIP.get(index));
		}
		return returnlist;
	}
	
	
	public static List<String> getDeviceName() {
		return deviceName;
	}
	public static void setDeviceName(List<String> deviceName) {
		DTNMessageCollection.deviceName = deviceName;
	}
	public static List<String> getDeviceIP() {
		return deviceIP;
	}
	public static void setDeviceIP(List<String> deviceIP) {
		DTNMessageCollection.deviceIP = deviceIP;
	}
	public static List<String> getChatMessage() {
		return chatMessage;
	}
	public static void setChatMessage(List<String> chatMessage) {
		DTNMessageCollection.chatMessage = chatMessage;
	}
	public static List<String> getTime() {
		return time;
	}
	public static void setTime(List<String> time) {
		DTNMessageCollection.time = time;
	}
	public static List<String> getHash() {
		return hash;
	}
	public static void setHash(List<String> hash) {
		DTNMessageCollection.hash = hash;
	}
	public static List<String> getLatitude() {
		return latitude;
	}
	public static void setLatitude(List<String> latitude) {
		DTNMessageCollection.latitude = latitude;
	}
	public static List<String> getLongitude() {
		return longitude;
	}
	public static void setLongitude(List<String> longitude) {
		DTNMessageCollection.longitude = longitude;
	}
}
