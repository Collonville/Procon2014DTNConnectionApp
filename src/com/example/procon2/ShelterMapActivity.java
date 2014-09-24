package com.example.procon2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;


public class ShelterMapActivity extends Fragment implements OnClickListener{
	private Context context;
	
	public ShelterMapActivity(Context context){
		this.context = context;
	}
	
	@Override
    public View onCreateView( LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.shelter_layout, container, false);

   

        return v;
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
