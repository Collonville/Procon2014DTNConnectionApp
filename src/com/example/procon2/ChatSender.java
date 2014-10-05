package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.msgpack.MessagePack;

public class ChatSender {
	private static DatagramSocket sendSocket;
	private static DatagramPacket packet;
	private static InetAddress inetAddress;

	public ChatSender() {
		try {
			inetAddress = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public synchronized void sendByUDP(final MessageInfo messageInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MessagePack msgpack = new MessagePack();

				try {
					byte[] data = msgpack.write(messageInfo);
					sendSocket = new DatagramSocket();
					packet = new DatagramPacket(data, data.length, inetAddress, DeviceInfo.getUdpPort() + 1000);

					sendSocket.send(packet);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sendSocket.close();
				}
			}
		}).start();
	}
}
