package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.msgpack.MessagePack;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

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
	private String isMoving;

	private MessageInfo messageInfo;
	private static boolean readConstactor = true;
	private Context context;

	public ChatReciver(Context context) {
		if (readConstactor) {
			this.context = context;
			handler = new Handler();
			try {
				recSocket = new DatagramSocket(51000);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			readConstactor = false;
		}
	}

	public void startChatRecive() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						byte[] buf = new byte[1024];
						MessagePack msgpack = new MessagePack();
						DatagramPacket packet = new DatagramPacket(buf,
								buf.length);

						recSocket.receive(packet);

						messageInfo = (MessageInfo) msgpack.read(buf,
								MessageInfo.class);

						deviceName = messageInfo.deviceName[0];
						deviceIP = messageInfo.deviceIP[0];
						chatMessage = messageInfo.chatMessage[0];
						time = messageInfo.time[0];
						hash = messageInfo.hash[0];
						latitude = messageInfo.latitude[0];
						longitude = messageInfo.longitude[0];
						isMoving = messageInfo.isMoving[0];

					} catch (Exception e) {
						e.printStackTrace();
					}

					handler.post(new Runnable() {
						@Override
						public void run() {
							if (DeviceInfo.isChatConnection()) {
								if (DTNMessageCollection.getHash()
										.indexOf(hash) == -1) {
									ChatActivity.pushChatMessage(deviceName,
											chatMessage, time);
									DTNMessageCollection.addData(deviceName,
											deviceIP, chatMessage, time, hash,
											latitude, longitude, isMoving);

									if (!DeviceInfo.getDeviceIP().equals(
											deviceIP)) {
										Toast.makeText(context,
												"新しいチャットメッセージが受信されました",
												Toast.LENGTH_SHORT).show();
									}
								}
							}
						}
					});
				}
			}
		}).start();
	}
}
