package com.example.procon2;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.msgpack.MessagePack;

public class DTNDiffSender {
	private DatagramSocket sendSocket;
	private InetAddress inetAddress;
	private DatagramPacket packet;

	public DTNDiffSender(final String ip, final MessageInfo messageInfo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					inetAddress = InetAddress.getByName("255.255.255.255");
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}

				MessagePack msgpack = new MessagePack();
				try {
					byte[] data = msgpack.write(messageInfo);
					sendSocket = new DatagramSocket();
					packet = new DatagramPacket(data, data.length, inetAddress,
							53000);
					sendSocket.send(packet);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void closeSocket() {
		// sendSocket.close();
	}
}
