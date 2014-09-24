package com.example.procon2;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class GpsActivity {
	private static final String TAG = "LocationSampleActivity";
    private LocationManager mLocationManager;
    private LocationProvider mProvider;
    private boolean mLocationEnabled;
    
    private static double latitude;
    private static double longitude;

    public GpsActivity(Context contect) {
        // LocationManagerのオブジェクト取得
        mLocationManager = (LocationManager) contect.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
                100, // 時間指定：10秒間隔
                0,  // 距離指定：10m間隔
                mListener);
        // GPS_PROVIDERの取得
        mProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        Log.e(TAG,mProvider.toString());
        
        mLocationEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!mLocationEnabled) {
        	Log.d("GPS","GPS is off");
        }
        
        latitude  = 0.0;
        longitude = 0.0;
    }
    
    public static String getLatitude(){
    	return Double.toString(latitude);
    }
    
    public static String getLongitude(){
    	return Double.toString(longitude);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mLocationEnabled) {
//            // 通知が不要になったタイミングで、必ずリスナーを解除する
//            mLocationManager.removeUpdates(mListener);
//        }
//    }

    /**
     * ロケーションマネージャにセットするリスナー
     */
    private final LocationListener mListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // 位置情報が変更された
            setLocationData(location);
        }

        public void onProviderDisabled(String provider) {
            // ユーザーによってProviderが無効になった
        }

        public void onProviderEnabled(String provider) {
            // ユーザーによってProviderが有効になった
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Providerの状態が変わった
        }

    };

    /**
     * センサーマネージャから通知される値をUI表示
     */
    private void setLocationData(Location location) {    
        //Log.d("provider_name",location.getProvider());
        Log.d("latitude",Double.toString(location.getLatitude()));
        Log.d("longitude",Double.toString(location.getLongitude()));
        
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        if (location.hasAltitude()) {
        	//Log.d("longitude",Double.toString(location.getAltitude()));
            //((TextView) findViewById(R.id.altitude)).setText("" + location.getAltitude());
        } else {
        	Log.d("longitude","disable");
            //((TextView) findViewById(R.id.altitude)).setText("disable");
        }

//        if (location.hasAccuracy()) {
//            ((TextView) findViewById(R.id.accuracy)).setText("" + location.getAccuracy());
//        } else {
//            ((TextView) findViewById(R.id.accuracy)).setText("disable");
//        }
//        ((TextView) findViewById(R.id.time)).setText("" + location.getTime());

//        if (location.hasBearing()) {
//            ((TextView) findViewById(R.id.bearing)).setText("" + location.getBearing());
//
//        } else {
//            ((TextView) findViewById(R.id.bearing)).setText("disable");
//        }
//        if (location.hasSpeed()) {
//            ((TextView) findViewById(R.id.speed)).setText("" + location.getSpeed());
//
//        } else {
//            ((TextView) findViewById(R.id.speed)).setText("disable");
//        }

    }

}
