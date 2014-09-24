package com.example.procon2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * id:4
 * @author Collonville
 *
 */
public class ChatActivity extends Fragment implements OnClickListener {
	private static View sendBt;
	private static View chatMessageInput;
	private static View chatMessageListView;
	
	private static Calendar cal;
	
	private static ProgressDialog mPrgDlg;
	
	
	private static List<ChatListView> dataList = new ArrayList<ChatListView>();
	private static ChatAdapter adapter;
	
	private Context context;
	private static ChatSender chatSender;
	private static ChatReciver chatReciver;
	
	public ChatActivity(Context context){
		this.context = context;
	}

	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_layout, container, false);
        
        chatSender = new ChatSender();
        chatReciver = new ChatReciver();
        cal = Calendar.getInstance();
        
        sendBt = v.findViewById(R.id.sendBtn);
        ((Button)sendBt).setOnClickListener(this);
        
        chatMessageInput = v.findViewById(R.id.messageInput);
        
        chatMessageListView = v.findViewById(R.id.chatListView);
        adapter = new ChatAdapter();
	    ((ListView)chatMessageListView).setAdapter(adapter);
	    
	    mPrgDlg = new ProgressDialog(context);
        mPrgDlg.setTitle("メッセージを送信中(Sennding Message)");
        mPrgDlg.setMessage("しばらくお待ちください(Please Wait)");
        mPrgDlg.setIndeterminate(false);
       	mPrgDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       	mPrgDlg.setMax(5);	            // Max
       	mPrgDlg.incrementProgressBy(0);	// Min
	    
	    chatReciver.startChatRecive();

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
        @SuppressLint("HandlerLeak") @Override
        public void handleMessage(Message msg)
        {
            mPrgDlg.dismiss();
            sendBt.setClickable(true);
        };
    };

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == sendBt){
			List<String> deviceName  = new ArrayList<String>();
			List<String> deviceIP    = new ArrayList<String>();
			List<String> chatMessage = new ArrayList<String>();
			List<String> time        = new ArrayList<String>();
			List<String> hash        = new ArrayList<String>();
			List<String> latitude    = new ArrayList<String>();
			List<String> longitude   = new ArrayList<String>();
			
			deviceName.add(DeviceInfo.getDeviceName());
			deviceIP.add(DeviceInfo.getDeviceIP());
			
			latitude.add(GpsActivity.getLatitude());
			longitude.add(GpsActivity.getLongitude());
			
			if(((EditText)chatMessageInput).getText().toString().equals(""))
				chatMessage.add("None Message");
			else
				chatMessage.add(((EditText)chatMessageInput).getText().toString());
			
			time.add(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)) + ":" + Integer.toString(cal.get(Calendar.MINUTE)) + ":" + Integer.toString(cal.get(Calendar.SECOND)));
			hash.add(getHash(deviceName.get(0) + deviceIP.get(0) + chatMessage.get(0) + time.get(0)));

			cal = Calendar.getInstance();
			MessageInfo messageInfo = new MessageInfo(4,deviceName,deviceIP,chatMessage,time,hash,latitude,longitude,SensorActivity.getIsMoving());
			
			/**�A���[�g�_�C�A���O�̕\��**/
			sendBt.setClickable(false);
			mPrgDlg.setCanceledOnTouchOutside(false);
            mPrgDlg.show();
            
            Thread thread = new Thread(runnable);
            thread.start();
            /** End **/
			
			chatSender.sendByUDP(messageInfo);
			
			((EditText)chatMessageInput).getEditableText().clear();
		}
	}
	
	public static void pushChatMessage(String chatMessage,String deviceName,String ip,String time,String hash){
		dataList.add(0 ,new ChatListView(chatMessage, deviceName, ip, time ,hash));
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

	      @SuppressLint("InflateParams") @Override
	      public View getView(int position,View convertView,ViewGroup parent) {
	    	  TextView message;
	    	  TextView deviceName;
	    	  TextView deviceIP;
	    	  TextView hash;
	    	  TextView time;
	    	  
	    	  View v = convertView;

		      if(v == null){
		    	  LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	  v = inflater.inflate(R.layout.chat_listview_layout, null);
		      }
		      ChatListView chatListView = (ChatListView)getItem(position);
		      if(chatListView != null){
		    	  message        = (TextView) v.findViewById(R.id.message);
		    	  deviceName     = (TextView) v.findViewById(R.id.deviceName);
		    	  deviceIP       = (TextView) v.findViewById(R.id.deviceIP);
		    	  hash           = (TextView) v.findViewById(R.id.temp);
		    	  time           = (TextView) v.findViewById(R.id.time);
		        
		    	  message.setText(chatListView.message);
		    	  deviceName.setText(chatListView.deviceName);
		    	  deviceIP.setText(chatListView.deviceIP);
		    	  hash.setText(chatListView.hash);
		    	  time.setText(chatListView.time);
		      }
		      return v;
	      }
	  }
	
	public static String getHash(String text) {
	    MessageDigest md    = null;
	    StringBuffer buffer = new StringBuffer();
	 
	    try {
	        md = MessageDigest.getInstance("SHA-256");
	    } catch (NoSuchAlgorithmException e) {
	        Log.d("NoneHashAlgoError","No Algo");
	    }
	 
	    md.update(text.getBytes());
	 
	    byte[] valueArray = md.digest();
	 
	    for(int i = 0; i < valueArray.length; i++){
	        String tmpStr = Integer.toHexString(valueArray[i] & 0xff);
	 
	        if(tmpStr.length() == 1){
	            buffer.append('0').append(tmpStr);
	        } else {
	            buffer.append(tmpStr);
	        }
	    }
	    return buffer.toString();
	}
}

