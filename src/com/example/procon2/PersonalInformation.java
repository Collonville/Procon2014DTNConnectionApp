package com.example.procon2;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

public class PersonalInformation extends Fragment implements OnClickListener {
	static final int REQUEST_CAPTURE_IMAGE = 100;

	private Context context;
	private MainActivity mainActivity;
	
	private static TextView nameTextView;
	private static TextView sexTextView;
	private static TextView insuranceCardNumberTextView;
	private static TextView injuryVTextiew;
	
	
	
	private static View picture;
	
	private static View takePicButton;
	private static View nameBtn;
	private static View sexBtn;
	private static View insuranceCardNumberBtn;
	private static View injuryBtn;
	
	

	
	
	public PersonalInformation(Context context) {
		this.context = context;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		// MyActivity 以外に所属する場合 Exception をスローし先に進ませない
        if (activity instanceof MainActivity == false) {
            throw new UnsupportedOperationException("MyActivity 以外からコールされている.");
        }
        mainActivity = (MainActivity) activity;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mainActivity = null;  // Activity のリーク対策
	}

	@Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.perosonal_information_layout, container, false);
     
        
        nameTextView   = (TextView) v.findViewById(R.id.nameView);
        sexTextView    = (TextView) v.findViewById(R.id.sexView);
        insuranceCardNumberTextView = (TextView) v.findViewById(R.id.insuranceCardNumberView);
        injuryVTextiew = (TextView) v.findViewById(R.id.injuryView);
        

        
        /** 初期画像の読み込み　**/
        picture = v.findViewById(R.id.pitureView);
        
        /** すでに読み込み済みの画像があればそれを使う　**/
        if(PersonInfo.getPicture() == null){
	        AssetManager as = getResources().getAssets();
	        try {
	        	InputStream is = as.open("images/noImage/noImage1.gif");
	        	Bitmap bm = BitmapFactory.decodeStream(is);
	        	((ImageView)picture).setImageBitmap(bm);
	        } catch(IOException e) {
	        	Log.d("Load Pic",e.toString());
	        }
        } else {
        	((ImageView)picture).setImageBitmap(PersonInfo.getPicture());
        }
        /** End **/
        
        takePicButton = v.findViewById(R.id.takePictureButton);
        ((Button)takePicButton).setOnClickListener(this);
        
        nameBtn = v.findViewById(R.id.changeNameBtn);
        ((Button)nameBtn).setOnClickListener(this);
        
        sexBtn = v.findViewById(R.id.changeSexBtn);
        ((Button)sexBtn).setOnClickListener(this);
        
        insuranceCardNumberBtn = v.findViewById(R.id.changeInsuranceCardNumberBtn);
        ((Button)insuranceCardNumberBtn).setOnClickListener(this);
        
        injuryBtn = v.findViewById(R.id.changeInjuryBtn);
        ((Button)injuryBtn).setOnClickListener(this);

        


        return v;
    }
	
	@SuppressLint("InflateParams") 
	@Override
	public void onClick(View v) {
		if(v == takePicButton){
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
		} else if(v == nameBtn) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.personal_info_daialog_name, null);
			
			final EditText nameInput = (EditText)view.findViewById(R.id.nameInput);
			final EditText kanaInput = (EditText)view.findViewById(R.id.kanaInput);
			
			new AlertDialog.Builder(context)
		        .setView(view)
		        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		                PersonInfo.setName(nameInput.getText().toString());
		                PersonInfo.setNameKana(kanaInput.getText().toString());
		                
		                nameTextView.setText(nameInput.getText().toString() + "(" + kanaInput.getText().toString() + ")");
		                
		                DeviceInfo.setDeviceName(nameInput.getText().toString());
		            }
		        }).show();
		} else if(v == sexBtn) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.personal_info_dialog_sexage, null);
			
			final EditText ageInput = (EditText)view.findViewById(R.id.ageInput);
			final String items[] = new String[] {"男性(Male)", "女性(Female)"};
			new AlertDialog.Builder(context)
	        .setView(view)
	        .setSingleChoiceItems(items, 0,new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	PersonInfo.setSex(items[which]);
	            	
	              
	            }
	        })
	        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {
	            	PersonInfo.setAge(ageInput.getText().toString());
	            	sexTextView.setText(PersonInfo.getAge() + "/" + PersonInfo.getSex());
	            }
	        }).show();
			
		} else if(v == insuranceCardNumberBtn) {
			
		} else if(v == injuryBtn) {
			
		} 
	}
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
	}

	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(REQUEST_CAPTURE_IMAGE == requestCode && resultCode == Activity.RESULT_OK ){
			Bitmap capturedImage = (Bitmap) data.getExtras().get("data");
			PersonInfo.setPicture(capturedImage);
			((ImageView)picture).setImageBitmap(capturedImage);
		}
	}

}
