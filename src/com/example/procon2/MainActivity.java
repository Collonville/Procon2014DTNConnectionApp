package com.example.procon2;

import java.net.InetAddress;

import android.content.*;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import java.util.Random;

public class MainActivity extends FragmentActivity implements
		android.widget.TabHost.OnTabChangeListener {

	private IntentFilter mFilter;
	private String mLastTabId;
	private BroadcastReceiver mReceiver;
	private TabHost mTabHost;
	private WifiManager mWifiManager;
	private WifiManagerNew mWifiManagerNew;

	@SuppressWarnings("unused")
	private DeviceInfo deviceInfo;

	private Send send;
	private Recive recive;
	private ConnectedDeviceActivity connectedDeviceActivity;
	@SuppressWarnings("unused")
	private GpsActivity gpsActivity;
	@SuppressWarnings("unused")
	private SensorActivity sendorActivity;

	private DTNDiffReciver dtnDiffReciver;

	private ChatReciver chatReciver;

	private PersonalInfoSender personalInfoSender;
	private PersonalInfoReciver personalInfoReciver;

	private InputMethodManager mgr;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_main);

		mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

		/* Wrap WifiManager to access new methods */
		mWifiManagerNew = new WifiManagerNew(mWifiManager);

		/*
		 * Register broadcast receiver to get notified when Wifi has been
		 * enabled
		 */
		mFilter = new IntentFilter();
		mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {
					int state = intent.getIntExtra(
							WifiManager.EXTRA_WIFI_STATE,
							WifiManager.WIFI_STATE_UNKNOWN);

					if (state == WifiManager.WIFI_STATE_ENABLED) {
						log("Wifi state enabled");

						/*
						 * This function only returns valid results in enabled
						 * state and is not part of the standard API (yet?)
						 */
						if (mWifiManagerNew.isIbssSupported()) {
							log("Ad-hoc mode is supported");
							configureAdhocNetwork();
							log("Successfully configured Adhoc network!");
						} else {
							log("Sorry, Ad-hoc mode is not supported by your system or device!");
						}
					}
				}
			}
		};

		/* Tabs */
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		android.widget.TabHost.TabSpec chatTabspec = mTabHost
				.newTabSpec("chatTab");
		chatTabspec.setIndicator("チャット");
		chatTabspec.setContent(new DummyTabFactory(this));
		mTabHost.addTab(chatTabspec);
		mTabHost.setOnTabChangedListener(this);

		android.widget.TabHost.TabSpec personalＩnformationTabsec = mTabHost
				.newTabSpec("personalＩnformationTab");
		personalＩnformationTabsec.setIndicator("安否情報");
		personalＩnformationTabsec.setContent(new DummyTabFactory(this));
		mTabHost.addTab(personalＩnformationTabsec);
		mTabHost.setOnTabChangedListener(this);

		android.widget.TabHost.TabSpec mapTabsec = mTabHost
				.newTabSpec("mapTab");
		mapTabsec.setIndicator("避難所案内");
		mapTabsec.setContent(new DummyTabFactory(this));
		mTabHost.addTab(mapTabsec);
		mTabHost.setOnTabChangedListener(this);

		android.widget.TabHost.TabSpec emergencyTabsec = mTabHost
				.newTabSpec("emergencyTab");
		emergencyTabsec.setIndicator("応急処置");
		emergencyTabsec.setContent(new DummyTabFactory(this));
		mTabHost.addTab(emergencyTabsec);
		mTabHost.setOnTabChangedListener(this);

		android.widget.TabHost.TabSpec connetedDeviceTabsec = mTabHost
				.newTabSpec("decviceConnectionTab");
		connetedDeviceTabsec.setIndicator("通信情報");
		connetedDeviceTabsec.setContent(new DummyTabFactory(this));
		mTabHost.addTab(connetedDeviceTabsec);

		android.widget.TabHost.TabSpec mapQuestTabsec = mTabHost
				.newTabSpec("mapQuestTab");
		mapQuestTabsec.setIndicator("マップ");
		mapQuestTabsec.setContent(new DummyTabFactory(this));
		mTabHost.addTab(mapQuestTabsec);

		mTabHost.setOnTabChangedListener(this);
		onTabChanged("personalＩnformationTab");
		onTabChanged("mapQuestTab");
		onTabChanged("chatTab"); // 初期タブ設定
		/* End */

		deviceInfo = new DeviceInfo(this);

		/* Connected device Information */
		connectedDeviceActivity = new ConnectedDeviceActivity(this);
		connectedDeviceActivity.startCheckConnectedIP();
		/* End */

		/* Sensors */
		sendorActivity = new SensorActivity(this);
		/* End */

		/* GPS */
		gpsActivity = new GpsActivity(this);
		/* End */

		/* Send Thread */
		send = new Send();
		send.startDTNConnection();
		/* End */

		/* Receive Thread */
		recive = new Recive();
		recive.startDTNRecive();
		/* End */

		chatReciver = new ChatReciver(this);
		chatReciver.startChatRecive();

		personalInfoSender = new PersonalInfoSender();
		personalInfoSender.startDTNConnection();

		personalInfoReciver = new PersonalInfoReciver();
		personalInfoReciver.startRecive();

		dtnDiffReciver = new DTNDiffReciver(this);
		dtnDiffReciver.startDTNDiffRecive();

	}

	private void configureAdhocNetwork() {
		try {
			/*
			 * We use WifiConfigurationNew which provides a way to access the
			 * Ad-hoc mode and static IP configuration options which are not
			 * part of the standard API yet
			 */
			WifiConfigurationNew wifiConfig = new WifiConfigurationNew();

			/* Set the SSID and security as normal */
			wifiConfig.SSID = "\"ProconAdhoc\"";
			wifiConfig.allowedKeyManagement.set(KeyMgmt.NONE);

			/* Use reflection until API is official */
			wifiConfig.setIsIBSS(true);
			wifiConfig.setFrequency(2442);

			/* Use reflection to configure static IP addresses */
			wifiConfig.setIpAssignment("STATIC");
			wifiConfig
					.setIpAddress(InetAddress.getByName(createIPAddress()), 8);
			wifiConfig.setGateway(InetAddress.getByName("192.1.1.1"));
			wifiConfig.setDNS(InetAddress.getByName("8.8.8.8"));

			/* Add, enable and save network as normal */
			int id = mWifiManager.addNetwork(wifiConfig);
			if (id < 0) {
				log("Failed to add Ad-hoc network");
			} else {
				mWifiManager.enableNetwork(id, true);
				mWifiManager.saveConfiguration();
			}
		} catch (Exception e) {
			log("Wifi configuration failed!");
			e.printStackTrace();
		}
	}

	private void log(String s) {
		Log.d("AdhocDemo", s);
	}

	private String createIPAddress() {
		Random random = new Random();
		String u = Integer.toString(192);
		String t = Integer.toString(random.nextInt(254) + 1);
		String s = Integer.toString(random.nextInt(254) + 1);
		String v = Integer.toString(random.nextInt(254) + 1);

		return (u + "." + t + "." + s + "." + v);
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mFilter);
		mWifiManager.setWifiEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// menu/activity_main.xmlからメニューを作成
		getMenuInflater().inflate(R.menu.main, menu);
		return true;

	}

	public void onTabChanged(String s) {
		Log.d("TAB_FRAGMENT_LOG", (new StringBuilder("tabId:")).append(s)
				.toString());
		if (mLastTabId != s) {
			// キーボードを非表示
			FragmentTransaction fragmenttransaction = getSupportFragmentManager()
					.beginTransaction();

			mgr = (InputMethodManager) this.getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			mgr.hideSoftInputFromWindow(getWindow().getDecorView()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

			if ("chatTab" == s) {
				fragmenttransaction.replace(R.id.realtabcontent,
						new ChatActivity(this));
			} else if ("decviceConnectionTab" == s) {
				fragmenttransaction.replace(R.id.realtabcontent,
						new ConnectedDeviceActivity(this));
			} else if ("mapTab" == s) {
				fragmenttransaction.replace(R.id.realtabcontent,
						new ShelterMapActivity(this));
			} else if ("personalＩnformationTab" == s) {
				fragmenttransaction.replace(R.id.realtabcontent,
						new PersonalInformation(this));
			} else if ("emergencyTab" == s) {
				fragmenttransaction.replace(R.id.realtabcontent,
						new EmergencyMeasureActivity(this));
			} else if ("mapQuestTab" == s) {
				fragmenttransaction.replace(R.id.realtabcontent,
						new MapActivity(this));
			}

			mLastTabId = s;
			fragmenttransaction.commit();

		}
	}

	/*
	 * android:id/tabcontent のダミーコンテンツ
	 */
	private static class DummyTabFactory implements TabContentFactory {
		private final Context mContext;

		DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			return v;
		}
	}
}
