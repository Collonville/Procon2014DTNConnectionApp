package com.example.procon2;

public class ChatListView {
	public String deviceName;
	public String message;
	public String time;

	public ChatListView(final String deviceName, final String message,
			final String time) {
		this.deviceName = deviceName;
		this.message = message;
		this.time = time;
	}
}
