package com.example.procon2;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/********************************************************************/
/*’[––“à•”‚Ìó‘Ôî•ñ‚ð•Û‘¶‚µ‚Ä‚¨‚­‚½‚ß‚ÌƒNƒ‰ƒXB¡Œã•Ï”‚ª‘‚¦‚é—\’è—L‚è*/
/********************************************************************/
public class DeviceInfo {
	static private String deviceName = "Nexus7";
	static private String deviceIP;
	static private String deviceMAC;
	static private String deviceBattery;
	static private int dtnUpdateTime = 2000;

	final  static private int udpPort = 50000;
	
	private static WifiManager manager;
	private static WifiInfo info;
	
	private static int ipAdr;
	
	@SuppressLint("DefaultLocale") public DeviceInfo(Context context){
		/** Wi-Fi通信に関する情報 **/
		manager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		info = manager.getConnectionInfo();
		
		deviceMAC = String.format("%s", info.getMacAddress());
		/** End **/
		
	}
	
	public static String getDeviceName() {
		return deviceName;
	}

	public static void setDeviceName(String deviceName) {
		DeviceInfo.deviceName = deviceName;
	}

	@SuppressLint("DefaultLocale") public static String getDeviceIP() {
		info = manager.getConnectionInfo();
		ipAdr = info.getIpAddress();
		deviceIP = String.format("%02d.%02d.%02d.%02d", (ipAdr>>0)&0xff, (ipAdr>>8)&0xff, (ipAdr>>16)&0xff, (ipAdr>>24)&0xff);
		
		return deviceIP;
	}

	public static void setDeviceIP(String deviceIP) {
		DeviceInfo.deviceIP = deviceIP;
	}

	public static String getDeviceMAC() {
		return deviceMAC;
	}

	public static void setDeviceMAC(String deviceMAC) {
		DeviceInfo.deviceMAC = deviceMAC;
	}

	public static String getDeviceBattery() {
		return deviceBattery;
	}

	public static void setDeviceBattery(String deviceBattery) {
		DeviceInfo.deviceBattery = deviceBattery;
	}

	public static int getUdpPort() {
		return udpPort;
	}

	public static int getDtnUpdateTime() {
		return dtnUpdateTime;
	}

	public static void setDtnUpdateTime(int dtnUpdateTime) {
		DeviceInfo.dtnUpdateTime = dtnUpdateTime;
	}
}
