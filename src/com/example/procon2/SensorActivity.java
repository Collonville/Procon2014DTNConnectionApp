package com.example.procon2;

import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorActivity implements SensorEventListener {
    private SensorManager sensorManager;
    
  //THRESHOLD ����l�ȏ�����o���邽�߂�臒l
  	protected final static double THRESHOLD = 0.8;
  	
  	//low pass filter alpha ���[�p�X�t�B���^�̃A���t�@�l
  	protected final static double alpha = 0.8;
  	
  	//�[�������ۂɎ擾���������x�l�B�d�͉����x���܂܂��BThis values include gravity force.
  	private float[] currentOrientationValues = { 0.0f, 0.0f, 0.0f };
  	//���[�p�X�A�n�C�p�X�t�B���^��̉����x�l Values after low pass and high pass filter
  	private float[] currentAccelerationValues = { 0.0f, 0.0f, 0.0f };
   
  	//diff ����
  	private float dx = 0.0f;
  	private float dy = 0.0f;
  	private float dz = 0.0f;
  	
  	//previous data 1�O�̒l
  	private float old_x = 0.0f;
  	private float old_y = 0.0f;
  	private float old_z = 0.0f;
  	
  	//�x�N�g����
  	private double vectorSize = 0;
  	
  	//���ڂ̂����Ȃ��J�E���g�t���O�i���̒[���̗h���2��f�[�^�����Ă��܂��̂�h�����߁j
  	//count flag to prevent acquiring data twice with one movement of a device
  	private boolean counted = false;
  	
  	//�m�C�Y�΍�
  	private boolean noiseflg = true;
  	//�x�N�g����(�ő�l)
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
        // �Z���T�[��~���̃��X�i���� Stopping Listener
        if ( manager != null )
            manager.unregisterListener(this);
        manager = null;      
	}
 
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u
		
	}
 
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			// ���[�p�X�t�B���^�ŏd�͒l�𒊏o�@Isolate the force of gravity with the low-pass filter.
			currentOrientationValues[0] = event.values[0] * 0.1f + currentOrientationValues[0] * (1.0f - 0.1f);
			currentOrientationValues[1] = event.values[1] * 0.1f + currentOrientationValues[1] * (1.0f - 0.1f);
			currentOrientationValues[2] = event.values[2] * 0.1f + currentOrientationValues[2] * (1.0f - 0.1f);
			
			// �d�͂̒l���Ȃ�Remove the gravity contribution with the high-pass filter.
			currentAccelerationValues[0] = event.values[0] - currentOrientationValues[0];
			currentAccelerationValues[1] = event.values[1] - currentOrientationValues[1];
			currentAccelerationValues[2] = event.values[2] - currentOrientationValues[2];
 
			// �x�N�g���l�����߂邽�߂ɍ������v�Z�@diff for vector
			dx = currentAccelerationValues[0] - old_x;
			dy = currentAccelerationValues[1] - old_y;
			dz = currentAccelerationValues[2] - old_z;
 
			vectorSize = Math.sqrt((double) (dx * dx + dy * dy + dz * dz));
			
			// ���ڂ̓m�C�Y�ɂȂ邩��Ȃ�
			if (noiseflg == true) {
				noiseflg = false;
			} else {
				if (vectorSize > THRESHOLD) {
					if (counted == true) {
						counted = false;

						// �ő�l�Ȃ�i�[
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
 
			// ��ԍX�V
			old_x = currentAccelerationValues[0];
			old_y = currentAccelerationValues[1];
			old_z = currentAccelerationValues[2];
		}
	}
}
