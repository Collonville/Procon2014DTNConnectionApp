package com.example.procon2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Handler hdl = new Handler();
		hdl.postDelayed(new splashHandler(), 1600);
	}

	class splashHandler implements Runnable {
		public void run() {
			// �X�v���b�V��������Ɏ��s����Activity���w�肵�܂��B
			Intent intent = new Intent(getApplication(), MainActivity.class);
			startActivity(intent);
			// SplashActivity���I�������܂��B
			SplashActivity.this.finish();
		}
	}

}
