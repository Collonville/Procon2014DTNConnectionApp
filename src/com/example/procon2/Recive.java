package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.msgpack.MessagePack;

import android.os.Handler;

public class Recive {
	public static final int MAX_SEND_DATA = 15;
	
	private Handler  handler = new Handler();
	
	private static DatagramSocket recSocket;
	
	private String id;                         
	
	private MessageInfo messageInfo;
	
	//private int len;
	
	public Recive(){
		try {
			recSocket = new DatagramSocket(DeviceInfo.getUdpPort());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized void startDTNRecive(){
        new Thread(new Runnable() {
			@Override
            public void run() {
            	while(true) {
    	        	try {
    	            	byte []buf = new byte[2048];
    	            	MessagePack msgpack = new MessagePack();
    	        		DatagramPacket packet= new DatagramPacket(buf,buf.length);
    	        		
    	        		recSocket.receive(packet);
    	        		
    	        		messageInfo = (MessageInfo)msgpack.read(buf,MessageInfo.class);
    	        		
    	        		//len = packet.getLength();
    	        		
    	        		id = messageInfo.id[0];
    	            } catch(Exception e) {
    	            	e.printStackTrace();
    	            }
            	
	                handler.post(new Runnable() {
		                @Override
		                public void run() {
		                	//マルチブロードキャストで送るため、自分自身に送信するのと他の端末から来るため2回入るのを防ぐ
		                	if(!DeviceInfo.getDeviceIP().equals(Arrays.asList(messageInfo.deviceIP).get(0)) && DeviceInfo.isDtnRecive()) {
			                	switch(id) {
			                		case "0": {
			                			List<String> id          = new ArrayList<String>();
			                			List<String> deviceName  = new ArrayList<String>();
			                			List<String> deviceIP    = new ArrayList<String>();
			                			List<String> chatMessage = new ArrayList<String>();
			                			List<String> time        = new ArrayList<String>();
			                			List<String> hash        = new ArrayList<String>();
			                			List<String> latitude    = new ArrayList<String>();
			                			List<String> longitude   = new ArrayList<String>();
			                			List<String> isMoving    = new ArrayList<String>();
			                			
			                			//接続してる隣接デバイス情報のリストに追加
			                			ConnectedDeviceActivity.addConnectedDevice(messageInfo.deviceIP[0]);
			                			
			                			//相手から受取ったハッシュ値をリストに格納
			                			hash = DTNMessageCollection.findNotSavedData(Arrays.asList(messageInfo.hash));
			                			
			                			//Log.d("Is moving",Boolean.toString(SensorActivity.getIsMoving()));
			                			
			                			
			                			
			                			//Log.d("Recived Data Size",Integer.toString(len) + "Bytes");
			                			
//			                			Log.d("Data sabun","‘ŠŽè‚ªŽ‚Á‚Ä‚¢‚È‚¢ƒnƒbƒVƒ…’l‚ÌƒŠƒXƒg---------------------");
//			                			for(int i = 0;i < hash.size(); i++)
//			                				Log.d("Data sabun",hash.get(i));
			                			
			                			latitude.add("");
			                			longitude.add("");
			                			isMoving.add("");
			                			
//			                			

			                			//ハッシュ値の違いがなければ送らない
			                			if(hash.size() != 0) {
			                				id.add("1");
			                				deviceName  = DTNMessageCollection.getDTNConnectionInfoFromHash(hash, DTNMessageCollection.getDeviceName());
				                			deviceIP    = DTNMessageCollection.getDTNConnectionInfoFromHash(hash, DTNMessageCollection.getDeviceIP());
				                			chatMessage = DTNMessageCollection.getDTNConnectionInfoFromHash(hash, DTNMessageCollection.getChatMessage());
				                			time        = DTNMessageCollection.getDTNConnectionInfoFromHash(hash, DTNMessageCollection.getTime());
				                			
				                			/** 送るデータ数が15個を超えると送れないため分割して送る **/
				                			if(hash.size() > MAX_SEND_DATA){
				                				List<String> _deviceName  = new ArrayList<String>();
					                			List<String> _deviceIP    = new ArrayList<String>();
					                			List<String> _chatMessage = new ArrayList<String>();
					                			List<String> _time        = new ArrayList<String>();
					                			List<String> _hash        = new ArrayList<String>();
					                			List<String> _latitude    = new ArrayList<String>();
					                			List<String> _longitude   = new ArrayList<String>();
					                			List<String> _isMoving    = new ArrayList<String>();
					                			

				                				for(int i = 0; i < (hash.size() / MAX_SEND_DATA); i++){
					                				/** 先頭から15個のデータを取り出す **/
					                				for(int j = i * MAX_SEND_DATA ; j < MAX_SEND_DATA * (i + 1); j++){
					                					_deviceName.add(deviceName.get(j));
					                					_deviceIP.add(deviceIP.get(j));
					                					_chatMessage.add(chatMessage.get(j));
					                					_time.add(time.get(j));
					                					_hash.add(hash.get(j));
					                					_latitude.add(latitude.get(0));
					                					_longitude.add(longitude.get(0));
					                					_isMoving.add(isMoving.get(0));
					                				}
					                				
				                					/** 送信元情報の添付　**/
					                				_deviceName.add(0, DeviceInfo.getDeviceName());
					                				_deviceIP.add(0, DeviceInfo.getDeviceIP());
					                				_chatMessage.add(0, "Debug:id=1 Message send to another devise");
					                				_time.add(0, "Debug:id=1 time");
					                				_hash.add(0, "Debug:id=1 hash ");
					                				_latitude.add(0, Double.toString(GpsActivity.getLatitude()));
				                					_longitude.add(0, Double.toString(GpsActivity.getLongitude()));
				                					_isMoving.add(0, SensorActivity.getIsMoving());
					                				/** End **/

//					                				Log.d("Debug Max Message","‘ŠŽè‚É‘—‚éƒƒbƒZ[ƒW---------------------");
//						                			for(int k = 0;k < _hash.size(); k++)
//						                				Log.d("Debug Max Message",_hash.get(k));

					                				
					                				MessageInfo diffrenceMessageInfo = new MessageInfo(id, _deviceName, _deviceIP, _chatMessage, _time, _hash, _latitude, _longitude , _isMoving);
						                			Send.sendByUDP(diffrenceMessageInfo);
						                			
						                			_deviceName.clear();
						                			_deviceIP.clear();
						                			_chatMessage.clear();
						                			_time.clear();
						                			_hash.clear();
						                			_latitude.clear();
						                			_longitude.clear();
						                			_isMoving.clear();
				                				}
				                			} else {
				                				/** 送信元情報の添付　**/
				                				deviceName.add(0, DeviceInfo.getDeviceName());
				                				deviceIP.add(0, DeviceInfo.getDeviceIP());
				                				chatMessage.add(0, "Debug:id=1 Message send to another devise");
				                				time.add(0, "Debug:id=1 time");
				                				hash.add(0, "Debug:id=1 hash ");
			                					latitude.add(0, Double.toString(GpsActivity.getLatitude()));
			                					longitude.add(0, Double.toString(GpsActivity.getLongitude()));
			                					isMoving.add(0, SensorActivity.getIsMoving());
				                				/** End **/

				                				MessageInfo diffrenceMessageInfo = new MessageInfo(id, deviceName, deviceIP, chatMessage, time, hash, latitude, longitude, isMoving);
					                			Send.sendByUDP(diffrenceMessageInfo);
				                			}
			                			}
				                	}
			                		break;
			                		case "1": {
			                			/**　相手から送られてきた自身の端末にない情報の保存 **/
			                			boolean addData = true;
			                			List<String> deviceName  = new ArrayList<String>();
			                			List<String> deviceIP    = new ArrayList<String>();
			                			List<String> chatMessage = new ArrayList<String>();
			                			List<String> time        = new ArrayList<String>();
			                			List<String> hash        = new ArrayList<String>();
			                			List<String> latitude    = new ArrayList<String>();
			                			List<String> longitude   = new ArrayList<String>();
			                			List<String> isMoving    = new ArrayList<String>();
			                			
			                			/** 受取った配列情報をリストに変換 **/
			                			deviceName  = Arrays.asList(messageInfo.deviceName);
			                			deviceIP    = Arrays.asList(messageInfo.deviceIP);
			                			chatMessage = Arrays.asList(messageInfo.chatMessage);
			                			time        = Arrays.asList(messageInfo.time);
			                			hash        = Arrays.asList(messageInfo.hash);
			                			latitude    = Arrays.asList(messageInfo.latitude);
			                			longitude   = Arrays.asList(messageInfo.longitude);
			                			isMoving    = Arrays.asList(messageInfo.isMoving);
			                			/** End **/
			                			
			                			for(int i = 0; i < DTNMessageCollection.getHash().size(); i++){
			                				for(int j = 0; j < hash.size(); j ++){
			                					if(DTNMessageCollection.getHash().get(i).equals(hash.get(j))){
			                						addData = false;
			                						break;
			                					}
			                				}
			                			}
			                			if(addData){
				                			/** 受け取ったデータを自身のデータベースに追加 **/
			                				for(int i = 1; i < hash.size(); i ++) {
			                					ChatActivity.pushChatMessage(deviceName.get(i), chatMessage.get(i), time.get(i));
			                					DTNMessageCollection.addData(deviceName.get(i), deviceIP.get(i), chatMessage.get(i), time.get(i), hash.get(i), latitude.get(0), longitude.get(0), isMoving.get(0));
			                				}
			                				/** End **/
			                			}
			                		}

			                		break;	
			                	}
			                }
		                }
	                });
            	}
            }
        }).start();
	}
}

