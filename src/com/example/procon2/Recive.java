package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.msgpack.MessagePack;

import android.os.Handler;
import android.util.Log;

public class Recive {
	private Handler handler = new Handler();

	private static DatagramSocket recSocket;

	private String id;
	private String ip;

	private MessageInfo messageInfo;

	public Recive() {
		try {
			recSocket = new DatagramSocket(DeviceInfo.getUdpPort());
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public synchronized void startDTNRecive() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						byte[] buf = new byte[2048];
						MessagePack msgpack = new MessagePack();
						DatagramPacket packet = new DatagramPacket(buf,
								buf.length);

						recSocket.receive(packet);

						messageInfo = (MessageInfo) msgpack.read(buf,
								MessageInfo.class);

						// len = packet.getLength();
						ip = messageInfo.deviceIP[0];
						id = messageInfo.id[0];
					} catch (Exception e) {
						e.printStackTrace();
					}

					handler.post(new Runnable() {
						@Override
						public void run() {
							// マルチブロードキャストで送るため、自分自身に送信するのと他の端末から来るため2回入るのを防ぐ
							if (!DeviceInfo.getDeviceIP().equals(ip)
									&& DeviceInfo.isDtnConnection()) {
								switch (id) {
								case "0": {
									List<String> id = new ArrayList<String>();
									List<String> deviceName = new ArrayList<String>();
									List<String> deviceIP = new ArrayList<String>();
									List<String> chatMessage = new ArrayList<String>();
									List<String> time = new ArrayList<String>();
									List<String> hash = new ArrayList<String>();
									List<String> latitude = new ArrayList<String>();
									List<String> longitude = new ArrayList<String>();
									List<String> isMoving = new ArrayList<String>();

									// 接続してる隣接デバイス情報のリストに追加
									ConnectedDeviceActivity.addConnectedDevice(
											messageInfo.deviceIP[0],
											messageInfo.deviceName[0]);

									// 相手から受取ったハッシュ値をリストに格納
									hash = DTNMessageCollection
											.findNotSavedData(Arrays
													.asList(messageInfo.hash));

									// Log.d("Recived Data Size",Integer.toString(len)
									// + "Bytes");

									// ハッシュ値の違いがなければ送らない
									if (hash.size() != 0) {

										id.add("1");
										latitude.add("");
										longitude.add("");
										isMoving.add("");
										deviceName = DTNMessageCollection
												.getDTNConnectionInfoFromHash(
														hash,
														DTNMessageCollection
																.getDeviceName());
										deviceIP = DTNMessageCollection
												.getDTNConnectionInfoFromHash(
														hash,
														DTNMessageCollection
																.getDeviceIP());
										chatMessage = DTNMessageCollection
												.getDTNConnectionInfoFromHash(
														hash,
														DTNMessageCollection
																.getChatMessage());
										time = DTNMessageCollection
												.getDTNConnectionInfoFromHash(
														hash,
														DTNMessageCollection
																.getTime());

										/** 送るデータ数が15個を超えると送れないため分割して送る **/
										if (hash.size() > DeviceInfo.MAX_SEND_DATA) {
											int lastInfo = 0;
											List<String> _deviceName = new ArrayList<String>();
											List<String> _deviceIP = new ArrayList<String>();
											List<String> _chatMessage = new ArrayList<String>();
											List<String> _time = new ArrayList<String>();
											List<String> _hash = new ArrayList<String>();
											List<String> _latitude = new ArrayList<String>();
											List<String> _longitude = new ArrayList<String>();
											List<String> _isMoving = new ArrayList<String>();

											for (int i = 0; i < (hash.size() / DeviceInfo.MAX_SEND_DATA); i++) {

												/** 先頭から15個のデータを取り出す **/
												for (int j = i
														* DeviceInfo.MAX_SEND_DATA; j < DeviceInfo.MAX_SEND_DATA
														* (i + 1); j++) {
													_deviceName.add(deviceName
															.get(j));
													_deviceIP.add(deviceIP
															.get(j));
													_chatMessage
															.add(chatMessage
																	.get(j));
													_time.add(time.get(j));
													_hash.add(hash.get(j));
													_latitude.add(latitude
															.get(0));
													_longitude.add(longitude
															.get(0));
													_isMoving.add(isMoving
															.get(0));
												}

												/** 送信元情報の添付　 **/
												_deviceName.add(0, DeviceInfo
														.getDeviceName());
												_deviceIP.add(0, DeviceInfo
														.getDeviceIP());
												_chatMessage
														.add(0,
																"Debug:id=1 Message send to another devise");
												_time.add(0, "Debug:id=1 time");
												_hash.add(0, "Debug:id=1 hash ");
												_latitude.add(0, Double
														.toString(GpsActivity
																.getLatitude()));
												_longitude.add(
														0,
														Double.toString(GpsActivity
																.getLongitude()));
												_isMoving.add(0, SensorActivity
														.getIsMoving());
												/** End **/

												MessageInfo diffrenceMessageInfo = new MessageInfo(
														id, _deviceName,
														_deviceIP,
														_chatMessage, _time,
														_hash, _latitude,
														_longitude, _isMoving);
												// Send.sendByUDP(diffrenceMessageInfo);

												@SuppressWarnings("unused")
												DTNDiffSender diffSender = new DTNDiffSender(
														ip,
														diffrenceMessageInfo);

												_deviceName.clear();
												_deviceIP.clear();
												_chatMessage.clear();
												_time.clear();
												_hash.clear();
												_latitude.clear();
												_longitude.clear();
												_isMoving.clear();

												// try {
												// Thread.sleep(500);
												// } catch (InterruptedException
												// e) {
												// // TODO Auto-generated catch
												// // block
												// e.printStackTrace();
												// } // 5秒待つ

												lastInfo = DeviceInfo.MAX_SEND_DATA
														* (i + 1);
											}

											int dataDiffSize = hash.size()
													- lastInfo;

											// 残りのデータを転送する
											if (dataDiffSize != 0) {
												for (int k = 0; k < dataDiffSize; k++) {
													_deviceName.add(deviceName
															.get(k + lastInfo));
													_deviceIP.add(deviceIP
															.get(k + lastInfo));
													_chatMessage.add(chatMessage
															.get(k + lastInfo));
													_time.add(time.get(k
															+ lastInfo));
													_hash.add(hash.get(k
															+ lastInfo));
													_latitude.add(latitude
															.get(0));
													_longitude.add(longitude
															.get(0));
													_isMoving.add(isMoving
															.get(0));
												}

												/** 送信元情報の添付　 **/
												_deviceName.add(0, DeviceInfo
														.getDeviceName());
												_deviceIP.add(0, DeviceInfo
														.getDeviceIP());
												_chatMessage
														.add(0,
																"Debug:id=1 Message send to another devise");
												_time.add(0, "Debug:id=1 time");
												_hash.add(0, "Debug:id=1 hash ");
												_latitude.add(0, Double
														.toString(GpsActivity
																.getLatitude()));
												_longitude.add(
														0,
														Double.toString(GpsActivity
																.getLongitude()));
												_isMoving.add(0, SensorActivity
														.getIsMoving());
												/** End **/

												MessageInfo diffrenceMessageInfo_ = new MessageInfo(
														id, _deviceName,
														_deviceIP,
														_chatMessage, _time,
														_hash, _latitude,
														_longitude, _isMoving);

												@SuppressWarnings("unused")
												DTNDiffSender diffSender = new DTNDiffSender(
														ip,
														diffrenceMessageInfo_);
											}
										} else {
											/** 送信元情報の添付　 **/
											deviceName.add(0,
													DeviceInfo.getDeviceName());
											deviceIP.add(0, ip);
											chatMessage
													.add(0,
															"Debug:id=1 Message send to another devise");
											time.add(0, "Debug:id=1 time");
											hash.add(0, "Debug:id=1 hash ");
											latitude.add(0, Double
													.toString(GpsActivity
															.getLatitude()));
											longitude.add(0, Double
													.toString(GpsActivity
															.getLongitude()));
											isMoving.add(0, SensorActivity
													.getIsMoving());
											/** End **/

											Log.d("Will send",
													Integer.toString(hash
															.size())
															+ "to"
															+ ip);

											MessageInfo diffrenceMessageInfo = new MessageInfo(
													id, deviceName, deviceIP,
													chatMessage, time, hash,
													latitude, longitude,
													isMoving);
											// Send.sendByUDP(diffrenceMessageInfo);

											@SuppressWarnings("unused")
											DTNDiffSender diffSender = new DTNDiffSender(
													ip, diffrenceMessageInfo);
										}
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
