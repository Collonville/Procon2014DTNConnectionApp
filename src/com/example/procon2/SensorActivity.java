package com.example.procon2;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorActivity implements SensorEventListener {
    private SensorManager sensorManager;
    
  //THRESHOLD ある値以上を検出するための閾値
  	protected final static double THRESHOLD = 0.8;
  	
  	//low pass filter alpha ローパスフィルタのアルファ値
  	protected final static double alpha = 0.8;
  	
  	//端末が実際に取得した加速度値。重力加速度も含まれる。This values include gravity force.
  	private float[] currentOrientationValues = { 0.0f, 0.0f, 0.0f };
  	//ローパス、ハイパスフィルタ後の加速度値 Values after low pass and high pass filter
  	private float[] currentAccelerationValues = { 0.0f, 0.0f, 0.0f };
   
  	//diff 差分
  	private float dx = 0.0f;
  	private float dy = 0.0f;
  	private float dz = 0.0f;
  	
  	//previous data 1つ前の値
  	private float old_x = 0.0f;
  	private float old_y = 0.0f;
  	private float old_z = 0.0f;
  	
  	//ベクトル量
  	private double vectorSize = 0;
  	
  	//一回目のゆれを省くカウントフラグ（一回の端末の揺れで2回データが取れてしまうのを防ぐため）
  	//count flag to prevent acquiring data twice with one movement of a device
  	private boolean counted = false;
  	
  	//ノイズ対策
  	private boolean noiseflg = true;
  	//ベクトル量(最大値)
  	private double vectorSize_max = 0;
  	
  	private static boolean isMoving = false;
  	
  	public static String getIsMoving(){
  		return Boolean.toString(isMoving);
  	}
    
    
    public SensorActivity(Context context) {
    	sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
		
		if(sensors.size()>0){
			Sensor s = sensors.get(0);
			sensorManager.registerListener(this, s,SensorManager.SENSOR_DELAY_UI);
		}
    }
    
	public void stopSensor(SensorManager manager) {	
        // センサー停止時のリスナ解除 Stopping Listener
        if ( manager != null )
            manager.unregisterListener(this);
        manager = null;      
	}
 
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
 
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// ローパスフィルタで重力値を抽出　Isolate the force of gravity with the low-pass filter.
			currentOrientationValues[0] = event.values[0] * 0.1f + currentOrientationValues[0] * (1.0f - 0.1f);
			currentOrientationValues[1] = event.values[1] * 0.1f + currentOrientationValues[1] * (1.0f - 0.1f);
			currentOrientationValues[2] = event.values[2] * 0.1f + currentOrientationValues[2] * (1.0f - 0.1f);
			
			// 重力の値を省くRemove the gravity contribution with the high-pass filter.
			currentAccelerationValues[0] = event.values[0] - currentOrientationValues[0];
			currentAccelerationValues[1] = event.values[1] - currentOrientationValues[1];
			currentAccelerationValues[2] = event.values[2] - currentOrientationValues[2];
 
			// ベクトル値を求めるために差分を計算　diff for vector
			dx = currentAccelerationValues[0] - old_x;
			dy = currentAccelerationValues[1] - old_y;
			dz = currentAccelerationValues[2] - old_z;
 
			vectorSize = Math.sqrt((double) (dx * dx + dy * dy + dz * dz));
			
			// 一回目はノイズになるから省く
			if (noiseflg == true) {
				noiseflg = false;
			} else {
				if (vectorSize > THRESHOLD) {
					if (counted == true) {
						counted = false;

						// 最大値なら格納
						if (vectorSize > vectorSize_max) {
							vectorSize_max = vectorSize;
						}
					} else if(counted == false) {
						counted = true;
						isMoving = true;
					}
				} else {
					isMoving = false;
				}
			}
 
			// 状態更新
			old_x = currentAccelerationValues[0];
			old_y = currentAccelerationValues[1];
			old_z = currentAccelerationValues[2];
		}
	}
}
