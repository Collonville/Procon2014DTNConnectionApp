package com.example.procon2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ListView;

public class ChatActivity extends Fragment implements OnClickListener {
	private static View sendBt;
	private static View chatMessageInput;
	private static View chatMessageListView;

	private static ProgressDialog mPrgDlg;

	private static List<ChatListView> dataList = new ArrayList<ChatListView>();
	private static ChatAdapter adapter;

	private static Context context;
	private static ChatSender chatSender;

	public ChatActivity(Context context) {
		ChatActivity.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.chat_layout, container, false);

		chatSender = new ChatSender();

		sendBt = v.findViewById(R.id.sendBtn);
		((Button) sendBt).setOnClickListener(this);

		chatMessageInput = v.findViewById(R.id.messageInput);

		chatMessageListView = v.findViewById(R.id.chatListView);
		adapter = new ChatAdapter();
		((ListView) chatMessageListView).setAdapter(adapter);

		mPrgDlg = new ProgressDialog(context);
		mPrgDlg.setTitle("メッセージを送信中(Sennding Message)");
		mPrgDlg.setMessage("しばらくお待ちください(Please Wait)");
		mPrgDlg.setIndeterminate(false);
		mPrgDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mPrgDlg.setMax(5);
		mPrgDlg.incrementProgressBy(0);

		return v;
	}

	Runnable runnable = new Runnable() {
		public void run() {
			for (int i = 0; i < 5; i++) {
				try {
					Thread.sleep(140);
					mPrgDlg.setProgress(i + 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			Message msg = new Message();
			msg.arg1 = 0;
			handler.sendMessage(msg);
		}
	};

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			mPrgDlg.dismiss();
			sendBt.setClickable(true);
		};
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		if (v == sendBt) {
			List<String> id = new ArrayList<String>();
			List<String> deviceName = new ArrayList<String>();
			List<String> deviceIP = new ArrayList<String>();
			List<String> chatMessage = new ArrayList<String>();
			List<String> time = new ArrayList<String>();
			List<String> hash = new ArrayList<String>();
			List<String> latitude = new ArrayList<String>();
			List<String> longitude = new ArrayList<String>();
			List<String> isMoving = new ArrayList<String>();

			id.add("4");
			deviceName.add(DeviceInfo.getDeviceName());
			deviceIP.add(DeviceInfo.getDeviceIP());

			latitude.add(Double.toString(GpsActivity.getLatitude()));
			longitude.add(Double.toString(GpsActivity.getLongitude()));
			isMoving.add(SensorActivity.getIsMoving());

			if (((EditText) chatMessageInput).getText().toString().equals("")) {
				chatMessage.add("None Message");
			} else
				chatMessage.add(((EditText) chatMessageInput).getText()
						.toString());

			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			time.add(df.format(date));
			hash.add(getHash(deviceName.get(0) + deviceIP.get(0)
					+ chatMessage.get(0) + time.get(0)));

			MessageInfo messageInfo = new MessageInfo(id, deviceName, deviceIP,
					chatMessage, time, hash, latitude, longitude, isMoving);

			sendBt.setClickable(false);
			mPrgDlg.setCanceledOnTouchOutside(false);
			mPrgDlg.show();

			Thread thread = new Thread(runnable);
			thread.start();
			/** End **/

			chatSender.sendByUDP(messageInfo);

			((EditText) chatMessageInput).getEditableText().clear();
		}
	}

	public static void pushChatMessage(final String deviceName,
			final String chatMessage, final String time) {
		dataList.add(0, new ChatListView(deviceName, chatMessage, time));
		adapter.notifyDataSetChanged();
	}

	public static void deleteAllChatMessage() {
		dataList.clear();
		adapter.notifyDataSetChanged();
	}

	private class ChatAdapter extends BaseAdapter {
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
			TextView deviceName;
			TextView message;
			TextView time;

			View v = convertView;

			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.chat_listview_layout, null);
			}
			ChatListView chatListView = (ChatListView) getItem(position);
			if (chatListView != null) {
				deviceName = (TextView) v.findViewById(R.id.deviceName);
				message = (TextView) v.findViewById(R.id.message);
				time = (TextView) v.findViewById(R.id.time);

				deviceName.setText(chatListView.deviceName);
				message.setText(chatListView.message);
				time.setText(chatListView.time);
			}
			return v;
		}
	}

	/*
	 * SHA-256に基づいてハッシュ生成(ハッシュ値長さ=64)
	 */
	public static String getHash(String text) {
		MessageDigest md = null;
		StringBuffer buffer = new StringBuffer();

		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoneHashAlgoError", "No Algo");
		}

		md.update(text.getBytes());

		byte[] valueArray = md.digest();

		for (int i = 0; i < valueArray.length; i++) {
			String tmpStr = Integer.toHexString(valueArray[i] & 0xff);

			if (tmpStr.length() == 1) {
				buffer.append('0').append(tmpStr);
			} else {
				buffer.append(tmpStr);
			}
		}
		return buffer.toString();
	}
}
