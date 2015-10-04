package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import org.msgpack.MessagePack;

import android.os.Handler;

public class PersonalInfoSender {
	private static DatagramSocket sendSocket;
	private static DatagramPacket packet;
	private static InetAddress inetAddress;

	private Timer timer;

	public PersonalInfoSender() {
		timer = new Timer(true);

		try {
			inetAddress = InetAddress.getByName("255.255.255.255");
		} catch (UnknownHostException e) {
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
									for (int i = 0; i < DTNPerosonalInfoCollection
											.getHash().size(); i++) {
										PersonalInfoDataSet personalInfoDataSet = new PersonalInfoDataSet(
												DTNPerosonalInfoCollection
														.getName().get(i),
												DTNPerosonalInfoCollection
														.getNameKana().get(i),
												DTNPerosonalInfoCollection
														.getAge().get(i),
												DTNPerosonalInfoCollection
														.getSex().get(i),
												DTNPerosonalInfoCollection
														.getNumber().get(i),
												DTNPerosonalInfoCollection
														.getBlood().get(i),
												DTNPerosonalInfoCollection
														.getInjury().get(i),
												DTNPerosonalInfoCollection
														.getPic().get(i),
												DTNPerosonalInfoCollection
														.getHash().get(i),
												DTNPerosonalInfoCollection
														.getTime().get(i),
												DTNPerosonalInfoCollection
														.getMessage().get(i));
										sendByUDP(personalInfoDataSet);
									}

								}
							}
						}).start();
					}
				});
			}
		}, 0, DeviceInfo.getDtnUpdateTime());
	}

	private void sendByUDP(final PersonalInfoDataSet personalInfoDataSet) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MessagePack msgpack = new MessagePack();
				try {
					byte[] data = msgpack.write(personalInfoDataSet);
					sendSocket = new DatagramSocket();
					packet = new DatagramPacket(data, data.length, inetAddress,
							52000);

					sendSocket.send(packet);
					sendSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
