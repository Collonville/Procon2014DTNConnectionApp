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
        // LocationManager�̃I�u�W�F�N�g�擾
        mLocationManager = (LocationManager) contect.getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
                100, // ���Ԏw��F10�b�Ԋu
                0,  // �����w��F10m�Ԋu
                mListener);
        // GPS_PROVIDER�̎擾
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
//            // �ʒm���s�v�ɂȂ����^�C�~���O�ŁA�K�����X�i�[����������
//            mLocationManager.removeUpdates(mListener);
//        }
//    }

    /**
     * ���P�[�V�����}�l�[�W���ɃZ�b�g���郊�X�i�[
     */
    private final LocationListener mListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // �ʒu��񂪕ύX���ꂽ
            setLocationData(location);
        }

        public void onProviderDisabled(String provider) {
            // ���[�U�[�ɂ����Provider�������ɂȂ���
        }

        public void onProviderEnabled(String provider) {
            // ���[�U�[�ɂ����Provider���L���ɂȂ���
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Provider�̏�Ԃ��ς����
        }

    };

    /**
     * �Z���T�[�}�l�[�W������ʒm�����l��UI�\��
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
