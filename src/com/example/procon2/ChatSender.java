package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.msgpack.MessagePack;

public class ChatSender{
	private static DatagramSocket sendSocket;
	private static DatagramPacket packet;
	private static InetAddress inetAddress;
	 
	public ChatSender() { 
		try {
			inetAddress = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public synchronized void sendByUDP(final MessageInfo messageInfo) {
		new Thread(new Runnable() {
	    @Override
	    public void run() {
	    	MessagePack msgpack = new MessagePack();
	    	try{
	    		byte[] data = msgpack.write(messageInfo);
	    		sendSocket = new DatagramSocket();
	            packet     = new DatagramPacket(data, data.length, inetAddress, 51000);
	        
	            sendSocket.send(packet);
	            sendSocket.close();
	        }catch(Exception e){
	        	
	        }
	    }
	  }).start();
	}
}



