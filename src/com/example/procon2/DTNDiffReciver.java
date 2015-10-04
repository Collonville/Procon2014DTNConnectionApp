package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.List;

import org.msgpack.MessagePack;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class DTNDiffReciver {
	private static Handler handler = new Handler();

	private static DatagramSocket recSocket;

	private MessageInfo messageInfo;
	private static Context context;

	public DTNDiffReciver(Context context) {
		try {
			recSocket = new DatagramSocket(53000);
		} catch (SocketException e) {
			e.printStackTrace();
		}

		DTNDiffReciver.context = context;
	}

	public void startDTNDiffRecive() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						byte[] buf = new byte[4096];
						MessagePack msgpack = new MessagePack();
						DatagramPacket packet = new DatagramPacket(buf,
								buf.length);

						recSocket.receive(packet);

						messageInfo = (MessageInfo) msgpack.read(buf,
								MessageInfo.class);

						final List<String> deviceName = Arrays
								.asList(messageInfo.deviceName);
						final List<String> deviceIP = Arrays
								.asList(messageInfo.deviceIP);
						final List<String> chatMessage = Arrays
								.asList(messageInfo.chatMessage);
						final List<String> time = Arrays
								.asList(messageInfo.time);
						final List<String> hash = Arrays
								.asList(messageInfo.hash);
						final List<String> latitude = Arrays
								.asList(messageInfo.latitude);
						final List<String> longitude = Arrays
								.asList(messageInfo.longitude);
						final List<String> isMoving = Arrays
								.asList(messageInfo.isMoving);

						handler.post(new Runnable() {
							@Override
							public void run() {
								int cnt = 0;

								for (int i = 1; i < hash.size(); i++) {
									if (DTNMessageCollection.getHash().indexOf(
											hash.get(i)) == -1) {
										cnt++;
										ChatActivity.pushChatMessage(
												deviceName.get(i),
												chatMessage.get(i), time.get(i));
										DTNMessageCollection.addData(
												deviceName.get(i),
												deviceIP.get(i),
												chatMessage.get(i),
												time.get(i), hash.get(i),
												latitude.get(0),
												longitude.get(0),
												isMoving.get(0));
									}
								}
								if (cnt != 0)
									Toast.makeText(context,
											"新しいチャットメッセージが" + cnt + "件受信されました",
											Toast.LENGTH_SHORT).show();

							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
