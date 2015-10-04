package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.msgpack.MessagePack;
import android.os.Handler;

public class Send {
	private static DatagramSocket sendSocket;
	private static DatagramPacket packet;
	private static InetAddress inetAddress;

	private Timer timer;

	public Send() {
		timer = new Timer(true);

		try {
			inetAddress = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startDTNConnection() {
		final Handler dtnHandler = new Handler();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				dtnHandler.post(new Runnable() {
					public void run() {
						new Thread(new Runnable() {
							public void run() {
								if (DeviceInfo.isDtnConnection()) {
									List<String> id = new ArrayList<String>();
									List<String> deviceName = new ArrayList<String>();
									List<String> deviceIP = new ArrayList<String>();
									List<String> dummy = new ArrayList<String>();
									List<String> latitude = new ArrayList<String>();
									List<String> longitude = new ArrayList<String>();
									List<String> isMoving = new ArrayList<String>();
									List<String> hash = new ArrayList<String>();

									id.add("0");
									deviceName.add(DeviceInfo.getDeviceName());
									deviceIP.add(DeviceInfo.getDeviceIP());

									latitude.add(Double.toString(GpsActivity
											.getLatitude()));
									longitude.add(Double.toString(GpsActivity
											.getLongitude()));
									isMoving.add(SensorActivity.getIsMoving());

									dummy.add("");
									if (!DTNMessageCollection.getHash()
											.isEmpty()) {
										if (DTNMessageCollection.getHash()
												.size() > DeviceInfo.MAX_SEND_DATA) {
											// Log.d("Send",
											// "Data size is longer then " +
											// DeviceInfo.MAX_SEND_DATA);
											int lastInfo = 0;
											// 先頭から１回に送る最大メッセージ数を回して送る
											for (int i = 0; i < (int) (DTNMessageCollection
													.getHash().size() / DeviceInfo.MAX_SEND_DATA); i++) {
												for (int j = i
														* DeviceInfo.MAX_SEND_DATA; j < DeviceInfo.MAX_SEND_DATA
														* (i + 1); j++) {
													hash.add(DTNMessageCollection
															.getHash().get(j));
												}

												MessageInfo messageInfo = new MessageInfo(
														id, deviceName,
														deviceIP, dummy, dummy,
														hash, latitude,
														longitude, isMoving);

												sendByUDP(messageInfo);

												hash.clear();
												lastInfo = (i + 1)
														* DeviceInfo.MAX_SEND_DATA;
												// Log.d("Send", "**");
											}
											//
											int dataDiff = DTNMessageCollection
													.getHash().size()
													- lastInfo;
											// Log.d("dataDiff　over 15",
											// Integer.toString(dataDiff));
											// 残りのデータを転送する
											// 残りデータが0でなければ送信する
											if (dataDiff != 0) {
												for (int k = 0; k < dataDiff; k++) {
													hash.add(DTNMessageCollection
															.getHash()
															.get(k + lastInfo));
													// Log.d("Send","****");
												}
												MessageInfo messageInfo = new MessageInfo(
														id, deviceName,
														deviceIP, dummy, dummy,
														hash, latitude,
														longitude, isMoving);

												sendByUDP(messageInfo);
												// } else {
												// //
												// Log.d("Send","No 15 over diff");
											}

										} else {
											MessageInfo messageInfo = new MessageInfo(
													id, deviceName, deviceIP,
													dummy, dummy,
													DTNMessageCollection
															.getHash(),
													latitude, longitude,
													isMoving);
											sendByUDP(messageInfo);
											// Log.d("Send",
											// "Send Datasize < 15");
										}
									} else {
										// 自身の端末のハッシュ値が空の場合IPアドレス情報だけを送信
										MessageInfo messageInfo = new MessageInfo(
												id, deviceName, deviceIP,
												dummy, dummy, dummy, latitude,
												longitude, isMoving);
										sendByUDP(messageInfo);
									}
								}
							}
						}).start();
					}
				});
			}
		}, 0, DeviceInfo.getDtnUpdateTime());
	}

	public synchronized static void sendByUDP(final MessageInfo messageInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MessagePack msgpack = new MessagePack();
				try {
					byte[] data = msgpack.write(messageInfo);
					sendSocket = new DatagramSocket();
					packet = new DatagramPacket(data, data.length, inetAddress,
							DeviceInfo.getUdpPort());

					// Log.d("Sending Data Size",Integer.toString(data.length));

					sendSocket.send(packet);
					sendSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
