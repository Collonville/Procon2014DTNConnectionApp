package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.msgpack.MessagePack;

import android.os.Handler;

public class PersonalInfoReciver {
	private static Handler handler;

	private static DatagramSocket recSocket;

	private PersonalInfoDataSet personalInfoDataSet;
	private static boolean readConstactor = true;

	public PersonalInfoReciver() {
		if (readConstactor) {
			handler = new Handler();
			try {
				recSocket = new DatagramSocket(52000);
			} catch (SocketException e) {
				e.printStackTrace();
			}
			readConstactor = false;
		}
	}

	public void startRecive() {
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

						personalInfoDataSet = (PersonalInfoDataSet) msgpack
								.read(buf, PersonalInfoDataSet.class);
					} catch (Exception e) {
						e.printStackTrace();
					}

					handler.post(new Runnable() {
						@Override
						public void run() {
							if (DeviceInfo.isDtnConnection()) {
								if (DTNPerosonalInfoCollection.getHash()
										.indexOf(personalInfoDataSet.hash) == -1) {
									DTNPerosonalInfoCollection.addData(
											personalInfoDataSet.name,
											personalInfoDataSet.nameKana,
											personalInfoDataSet.age,
											personalInfoDataSet.sex,
											personalInfoDataSet.number,
											personalInfoDataSet.blood,
											personalInfoDataSet.injury,
											personalInfoDataSet.pic,
											personalInfoDataSet.hash,
											personalInfoDataSet.time,
											personalInfoDataSet.message);
									PersonalInformation
											.addData(personalInfoDataSet);
								}
							}
						}
					});
				}
			}
		}).start();
	}
}
