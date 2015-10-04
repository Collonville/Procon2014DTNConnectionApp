package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.msgpack.MessagePack;

import android.os.Handler;

public class ChatSender {
	private static DatagramSocket sendSocket;
	private static DatagramPacket packet;
	private static InetAddress inetAddress;
	private Handler handler = new Handler();

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
				if (DeviceInfo.isChatConnection()) {
					MessagePack msgpack = new MessagePack();
					try {
						byte[] data = msgpack.write(messageInfo);
						sendSocket = new DatagramSocket();
						packet = new DatagramPacket(data, data.length,
								inetAddress, DeviceInfo.getUdpPort() + 1000);

						sendSocket.send(packet);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						sendSocket.close();
					}
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							if (DTNMessageCollection.getHash().indexOf(
									messageInfo.hash[0]) == -1)
								ChatActivity.pushChatMessage(
										messageInfo.deviceName[0],
										messageInfo.chatMessage[0],
										messageInfo.time[0]);
							DTNMessageCollection.addData(
									messageInfo.deviceName[0],
									messageInfo.deviceIP[0],
									messageInfo.chatMessage[0],
									messageInfo.time[0], messageInfo.hash[0],
									messageInfo.latitude[0],
									messageInfo.longitude[0],
									messageInfo.isMoving[0]);

						}
					});
				}
			}
		}).start();
	}
}
