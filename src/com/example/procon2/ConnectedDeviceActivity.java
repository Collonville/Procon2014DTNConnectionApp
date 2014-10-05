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
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class ConnectedDeviceActivity extends Fragment implements OnClickListener{
	private static Context context;
	private static View connectedDeviceListView;
	
	private static List<String> isConnectedDeviceList;
	private static List<String> connectedDeviceList;
	
	private static List<ConnectedDeviceListView> dataList;
	private static ChatAdapter adapter;
	
	private static Switch dtnSwitch;
	
	private static TextView dataSize;
	
	private static Timer timer;

	private static Handler handler;
	
	public ConnectedDeviceActivity(Context context) {
		ConnectedDeviceActivity.context = context;
		
		isConnectedDeviceList = new ArrayList<String>();
		connectedDeviceList   = new ArrayList<String>();
		
		dataList = new ArrayList<ConnectedDeviceListView>();
		adapter = new ChatAdapter();
		
		timer = new Timer(true);
		handler = new Handler();
	}
	
	
	public synchronized static void addConnectedDevice(String deviceIP) {	
		if(isConnectedDeviceList.indexOf(deviceIP) == -1) 
			isConnectedDeviceList.add(deviceIP);
	}
	
	public void startCheckConnectedIP(){
		timer.schedule(new TimerTask() {
	        @Override
	        public void run() {
	        	connectedDeviceList = new ArrayList<String>(isConnectedDeviceList);
	        	
	        	
        		handler.post(new Runnable() {
	                @Override
	                public void run() {
	                	dataList.clear();
	    	        	adapter.notifyDataSetChanged();
	                	
	                	for(int i = 0;i < connectedDeviceList.size(); i++) {
		                	dataList.add(0 ,new ConnectedDeviceListView(connectedDeviceList.get(i)));
				    	    adapter.notifyDataSetChanged();

	                	}
	                }
                });
        		
        		isConnectedDeviceList.clear();
        		
	        }
	      }, 0, 4000    //開始遅延(何ミリ秒後に開始するか)と、周期(何ミリ秒ごとに実行するか)
	    );
	}
	
	public static void updateRecivedSize() {
		dataSize.setText("これまで受信したデータ数:" + Integer.toString(DTNMessageCollection.getHash().size()));
	}
	
	@Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.connected_device_layout, container, false);
        
        dataSize = (TextView) v.findViewById(R.id.dataSize);
        updateRecivedSize();
        
        connectedDeviceListView = v.findViewById(R.id.connectedListView);
	    ((ListView)connectedDeviceListView).setAdapter(adapter);
	    
	    dtnSwitch = (Switch) v.findViewById(R.id.dtnSwitch);

	    
	    dtnSwitch.setChecked(DeviceInfo.isDtnRecive());
	    
	    dtnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
	        @Override
	        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
	            if(isChecked == true) {
	            	DeviceInfo.setDtnRecive(true);
	            	DeviceInfo.setDtnSend(true);
	            } else {
	            	DeviceInfo.setDtnRecive(false);
	            	DeviceInfo.setDtnSend(false);
	            }
	        }
	    });

   
        return v;
    }
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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

	      @SuppressLint("InflateParams") @Override
	      public View getView(int position,View convertView,ViewGroup parent) {
	    	  TextView deviceIP;
	
	    	  View v = convertView;

		      if(v == null){
		    	  LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	  v = inflater.inflate(R.layout.connected_device_listview_layout, null);
		      }
		      ConnectedDeviceListView deviceListView = (ConnectedDeviceListView)getItem(position);
		      if(deviceListView != null){
		    	  deviceIP        = (TextView) v.findViewById(R.id.deviceIP);

		        
		    	  deviceIP.setText(deviceListView.deviceIP);

		      }
		      return v;
	      }
	  }

}
