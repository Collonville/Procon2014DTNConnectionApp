package com.example.procon2;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectedDeviceActivity extends Fragment implements
		OnClickListener {
	final private static int TIME_LOST = 6;
	private static Context context;
	private static View connectedDeviceListView;

	private static List<String> isConnectedDeviceList;
	private static List<Integer> timeLost;

	private static List<ConnectedDeviceListView> dataList;
	private static ChatAdapter adapter;

	private static TextView ip;
	private static TextView mac;
	private static TextView dtnPort;
	private static TextView gps;
	private static Switch dtnSwitch;
	private static Switch chatSwitch;

	private static TextView dataSize;

	private static Button deleteAllDataBtn;

	private static Timer timer;

	private static Handler handler;

	public ConnectedDeviceActivity(Context context) {
		ConnectedDeviceActivity.context = context;

		isConnectedDeviceList = new ArrayList<String>();
		timeLost = new ArrayList<Integer>();

		dataList = new ArrayList<ConnectedDeviceListView>();
		adapter = new ChatAdapter();

		timer = new Timer(true);
		handler = new Handler();
	}

	public synchronized static void addConnectedDevice(String deviceIP,
			String name) {
		if (isConnectedDeviceList.indexOf(deviceIP + ":" + name) == -1) {
			isConnectedDeviceList.add(deviceIP + ":" + name);
			timeLost.add(TIME_LOST);
		} else {
			timeLost.set(isConnectedDeviceList.indexOf(deviceIP + ":" + name),
					TIME_LOST);
		}
	}

	public void startCheckConnectedIP() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						// 寿命を１個ずつ縮める
						for (int i = 0; i < timeLost.size(); i++) {
							timeLost.set(i, timeLost.get(i) - 1);
						}

						// 寿命を迎えたデータは削除
						for (int i = 0; i < timeLost.size(); i++) {
							if (timeLost.get(i) <= -1) {
								timeLost.remove(i);
								isConnectedDeviceList.remove(i);
								dataList.remove(i);
								adapter.notifyDataSetChanged();
							}
						}

						// リストの更新
						dataList.clear();
						for (int i = 0; i < timeLost.size(); i++) {
							dataList.add(0, new ConnectedDeviceListView(
									isConnectedDeviceList.get(i)));
							adapter.notifyDataSetChanged();
						}

					}

				});
			}
		}, 0, 1000 // 開始遅延(何ミリ秒後に開始するか)と、周期(何ミリ秒ごとに実行するか)
		);
	}

	public static void updateRecivedSize() {
		dataSize.setText("データ総数:"
				+ Integer.toString(DTNMessageCollection.getHash().size()));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.connected_device_layout, container,
				false);

		dataSize = (TextView) v.findViewById(R.id.dataSize);
		updateRecivedSize();

		connectedDeviceListView = v.findViewById(R.id.connectedListView);
		((ListView) connectedDeviceListView).setAdapter(adapter);

		dtnSwitch = (Switch) v.findViewById(R.id.dtnSwitch);
		dtnSwitch.setChecked(DeviceInfo.isDtnConnection());
		dtnSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean isChecked) {
						if (isChecked == true) {
							DeviceInfo.setDtnConnection(true);
						} else {
							DeviceInfo.setDtnConnection(false);
						}
					}
				});

		chatSwitch = (Switch) v.findViewById(R.id.chatSwitch);
		chatSwitch.setChecked(DeviceInfo.isChatConnection());
		chatSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton compoundButton,
							boolean isChecked) {
						if (isChecked == true) {
							DeviceInfo.setChatConnection(true);
						} else {
							DeviceInfo.setChatConnection(false);
						}
					}
				});

		ip = (TextView) v.findViewById(R.id.ip);
		mac = (TextView) v.findViewById(R.id.mac);
		dtnPort = (TextView) v.findViewById(R.id.dtnPort);
		gps = (TextView) v.findViewById(R.id.gps);

		ip.setText("端末IPアドレス:" + DeviceInfo.getDeviceIP());
		mac.setText("端末MACアドレス:" + DeviceInfo.getDeviceMAC());
		dtnPort.setText("DTN通信(ポート):" + DeviceInfo.getUdpPort()
				+ "\nチャット通信(ポート):" + (DeviceInfo.getUdpPort() + 1000));
		gps.setText("GPS:" + (GpsActivity.getGpsOn() ? "On" : "Off"));

		deleteAllDataBtn = (Button) v.findViewById(R.id.deleteAllDataBtn);
		deleteAllDataBtn.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		if (v == deleteAllDataBtn) {
			DTNMessageCollection.deleteAllData();
			DTNPerosonalInfoCollection.deleteAllData();
			ChatActivity.deleteAllChatMessage();

			Toast.makeText(context, "端末内データを全て削除しました", Toast.LENGTH_SHORT)
					.show();

		}

	}

	private static class ChatAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public Object getItem(int position) {
			return dataList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView deviceIP;

			View v = convertView;

			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.connected_device_listview_layout,
						null);
			}
			ConnectedDeviceListView deviceListView = (ConnectedDeviceListView) getItem(position);
			if (deviceListView != null) {
				deviceIP = (TextView) v.findViewById(R.id.deviceIP);

				deviceIP.setText(deviceListView.deviceIP);

			}
			return v;
		}
	}

}
