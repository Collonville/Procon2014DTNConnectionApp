package com.example.procon2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class EmergencyMeasureActivity extends Fragment implements OnClickListener {
	
	private static ImageButton bleedingBtn;
	private static ImageButton cprBtn;
	private static ImageButton injuryBtn;
	
	private static Context context;

	public EmergencyMeasureActivity(Context contect) {
		this.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.emergency_measure_layout, container, false);
		
		bleedingBtn = (ImageButton) v.findViewById(R.id.imageButton1);
		cprBtn      = (ImageButton) v.findViewById(R.id.imageButton2);
		injuryBtn   = (ImageButton) v.findViewById(R.id.imageButton3);
		
		bleedingBtn.setOnClickListener(this);
		cprBtn.setOnClickListener(this);
		injuryBtn.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		if(v == bleedingBtn) {
			

			
		} else if(v == cprBtn) {
			
			
		} else if(v == injuryBtn) {
			
		}

	}
}
