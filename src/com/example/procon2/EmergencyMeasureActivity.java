package com.example.procon2;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

public class EmergencyMeasureActivity extends Fragment implements
		OnClickListener {

	private static ImageButton bleedingBtn;
	private static ImageButton cprBtn;
	private static ImageButton injuryBtn;
	private static ImageButton riceBtn;
	private static Button emergencyContactButton;

	private static Button emCpr_about;
	private static Button emCpr_procedure1;
	private static Button emCpr_procedure2;
	private static Button emCpr_procedure3;
	private static Button emCpr_procedure4;

	private static Button startBPMBtn;
	private static Button stopBPMBtn;

	private static Button emRice_about;
	private static Button emRice_procedure1;
	private static Button emRice_procedure2;
	private static Button emRice_procedure3;
	private static Button emRice_procedure4;

	private static Button toCpr_1;

	private static Context context;

	private static ViewFlipper viewFlipper;

	private static MediaPlayer mp;

	public EmergencyMeasureActivity(Context context) {
		EmergencyMeasureActivity.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.emergency_measure_layout, container,
				false);

		startBPMBtn = (Button) v.findViewById(R.id.startBPM);
		startBPMBtn.setOnClickListener(this);
		stopBPMBtn = (Button) v.findViewById(R.id.stopBPM);
		stopBPMBtn.setOnClickListener(this);

		toCpr_1 = (Button) v.findViewById(R.id.toCpr_1);
		toCpr_1.setOnClickListener(this);

		bleedingBtn = (ImageButton) v.findViewById(R.id.bleedingImageButton);
		cprBtn = (ImageButton) v.findViewById(R.id.imageButton2);
		injuryBtn = (ImageButton) v.findViewById(R.id.imageButton3);
		emergencyContactButton = (Button) v
				.findViewById(R.id.emergencyContactButton);
		riceBtn = (ImageButton) v.findViewById(R.id.riceBtn);

		bleedingBtn.setOnClickListener(this);
		cprBtn.setOnClickListener(this);
		injuryBtn.setOnClickListener(this);
		emergencyContactButton.setOnClickListener(this);
		riceBtn.setOnClickListener(this);

		viewFlipper = (ViewFlipper) v.findViewById(R.id.viewFlipper1);
		viewFlipper.setDisplayedChild(0);

		/** 心配蘇生法について　 **/
		emCpr_about = (Button) v.findViewById(R.id.about);
		emCpr_about.setOnClickListener(this);

		emCpr_procedure1 = (Button) v.findViewById(R.id.procedure1);
		emCpr_procedure1.setOnClickListener(this);

		emCpr_procedure2 = (Button) v.findViewById(R.id.procedure2);
		emCpr_procedure2.setOnClickListener(this);

		emCpr_procedure3 = (Button) v.findViewById(R.id.procedure3);
		emCpr_procedure3.setOnClickListener(this);

		emCpr_procedure4 = (Button) v.findViewById(R.id.procedure4);
		emCpr_procedure4.setOnClickListener(this);

		/** End **/

		/** RICEについて **/
		emRice_about = (Button) v.findViewById(R.id.rice1);
		emRice_about.setOnClickListener(this);
		emRice_procedure1 = (Button) v.findViewById(R.id.rice2);
		emRice_procedure1.setOnClickListener(this);
		emRice_procedure2 = (Button) v.findViewById(R.id.rice3);
		emRice_procedure2.setOnClickListener(this);
		emRice_procedure3 = (Button) v.findViewById(R.id.rice4);
		emRice_procedure3.setOnClickListener(this);
		emRice_procedure4 = (Button) v.findViewById(R.id.rice5);
		emRice_procedure4.setOnClickListener(this);
		/** End **/

		return v;
	}

	@Override
	public void onClick(View v) {
		/** 　メインの切り替え部分 **/
		if (v == bleedingBtn) {
			viewFlipper.setDisplayedChild(0);
		} else if (v == cprBtn) {
			viewFlipper.setDisplayedChild(1);
		} else if (v == riceBtn) {
			viewFlipper.setDisplayedChild(2);
		} else if (v == injuryBtn) {
			viewFlipper.setDisplayedChild(3);
		} else if (v == emergencyContactButton) {
			viewFlipper.setDisplayedChild(4);
		} else if (v == emCpr_about) {
			viewFlipper.setDisplayedChild(5);
		} else if (v == emCpr_procedure1) {
			viewFlipper.setDisplayedChild(6);
		} else if (v == emCpr_procedure2) {
			viewFlipper.setDisplayedChild(7);
		} else if (v == emCpr_procedure3) {
			viewFlipper.setDisplayedChild(8);
		} else if (v == emCpr_procedure4) {
			viewFlipper.setDisplayedChild(9);
		}
		/** End **/

		/** RICEについて **/
		if (v == emRice_about) {
			viewFlipper.setDisplayedChild(10);
		} else if (v == emRice_procedure1) {
			viewFlipper.setDisplayedChild(11);
		} else if (v == emRice_procedure2) {
			viewFlipper.setDisplayedChild(12);
		} else if (v == emRice_procedure3) {
			viewFlipper.setDisplayedChild(13);
		} else if (v == emRice_procedure4) {
			viewFlipper.setDisplayedChild(14);
		}

		if (v == startBPMBtn) {
			if (mp == null) {
				mp = MediaPlayer.create(context, R.raw.bpm100);
				mp.setLooping(true);
				mp.start();
			}
		} else if (v == stopBPMBtn) {
			if (mp != null) {
				if (mp.isPlaying()) {
					mp.stop();
					mp.release();
					mp = null;
				}
			}
		}

		if (v == toCpr_1) {
			viewFlipper.setDisplayedChild(6);
		}

	}
}
