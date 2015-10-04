package com.example.procon2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ShelterMapActivity extends Fragment implements OnClickListener {
	private static ArrayAdapter<String> prefectureAdapter;
	private static ArrayAdapter<String> citiesAdapter;

	private static ListView shelterAdapter;
	private static List<ShelterListView> dataList = new ArrayList<ShelterListView>();
	private static ShelterAdapter adapter;

	private static Spinner prefectureSpinner;
	private static Spinner citiesSpinner;

	private static Context context;

	private static List<String> shelterId = new ArrayList<String>();
	private static List<Double> leng = new ArrayList<Double>();

	private static View arrow;

	private static boolean updateCompass = false;

	private static List<Double> shelterLatiList = new ArrayList<Double>();
	private static List<Double> shelterLongList = new ArrayList<Double>();

	private static double shelterLati;
	private static double shelterLong;

	private static TextView latitudeTextView;
	private static TextView longitudeTextView;
	private static TextView toShelterLengTextView;

	// 県名の英語表記リスト
	private static String[] englichPrefecture;

	public ShelterMapActivity(Context context) {
		ShelterMapActivity.context = context;

		String[] prefecture = context.getResources().getStringArray(
				R.array.prefecture);

		prefectureAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, prefecture);
		prefectureAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		citiesAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item);
		citiesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		englichPrefecture = context.getResources().getStringArray(
				R.array.englishPrefecture);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.shelter_layout, container, false);

		latitudeTextView = (TextView) v.findViewById(R.id.latitude);
		longitudeTextView = (TextView) v.findViewById(R.id.longitude);
		toShelterLengTextView = (TextView) v.findViewById(R.id.toShelterLenth);

		/** 避難所のリスト関係　 **/
		shelterAdapter = (ListView) v.findViewById(R.id.shelterList);
		adapter = new ShelterAdapter();
		shelterAdapter.setAdapter(adapter);
		/** End **/

		/** 矢印画像のリソース読み取り　 **/
		arrow = v.findViewById(R.id.arraowImageView);

		/** すでに読み込み済みの画像があればそれを使う　 **/
		AssetManager as = getResources().getAssets();
		try {
			InputStream is = as.open("images/compas/171.png");
			Bitmap bm = BitmapFactory.decodeStream(is);
			((ImageView) arrow).setImageBitmap(bm);
		} catch (IOException e) {
			Log.d("Load Pic", e.toString());
		}
		/** End **/

		prefectureSpinner = (Spinner) v.findViewById(R.id.prefectureSpinner);
		prefectureSpinner.setAdapter(prefectureAdapter);
		prefectureSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						setCitiesCSV(englichPrefecture[position]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		citiesSpinner = (Spinner) v.findViewById(R.id.citiesSpinner);
		citiesSpinner.setAdapter(citiesAdapter);
		citiesSpinner
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						Spinner spinner = (Spinner) parent;
						// 選択されたアイテムを取得します
						String item = (String) spinner.getSelectedItem();
						setShelterInfo(shelterId.get(position));

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});

		/** コンパスActivcityが読み出されるまでコンパス情報の更新をしない **/
		updateCompass = true;

		return v;
	}

	public static boolean getUpdateCompass() {
		return updateCompass;
	}

	/*
	 * コンパスViewの描画を更新する
	 */
	public static void updateCompassView() {
		double lati = GpsActivity.getLatitude();
		double longi = GpsActivity.getLongitude();

		arrow.setRotation((float) getDegreeToShelter(
				-SensorActivity.getConpassDegree(), lati, longi));

		latitudeTextView.setText(Double.toString(lati));
		longitudeTextView.setText(Double.toString(longi));

		double lenToSehlter = getLenghToShelter(lati, longi, shelterLati,
				shelterLong);
		leng.add(lenToSehlter);
		toShelterLengTextView.setText(Double.toString(lenToSehlter) + "km");
	}

	public static double getShelterLen(double _lati, double _longi) {
		double lati = GpsActivity.getLatitude();
		double longi = GpsActivity.getLongitude();

		double lenToSehlter = getLenghToShelter(lati, longi, _lati, _longi);
		return lenToSehlter;
	}

	/*
	 * 2つのGPS情報から距離を取得する
	 */
	private static double getLenghToShelter(double latiA, double longiA,
			double latiB, double longiB) {
		final double earthR = 6378.137; // 地球の半径
		double latiDx = Math.PI / 180 * (latiB - latiA);
		double longDx = Math.PI / 180 * (longiB - longiA);

		double nsLen = earthR * latiDx; // 南北の距離
		double tkLen = Math.cos(Math.PI / 180 * latiA) * earthR * longDx; // 東西の距離

		return new BigDecimal(String.valueOf(Math.sqrt(Math.pow(nsLen, 2)
				+ Math.pow(tkLen, 2)))).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	/*
	 * 現在の場所から指定した避難所の方角を取得する
	 */
	private static double getDegreeToShelter(double deg, double lati,
			double longi) {
		double dx = lati - shelterLati;
		double dy = longi - shelterLong;

		return (deg - Math.toDegrees(Math.atan2(dy, dx)));
	}

	/*
	 * 県名から市町村のIDリストを取得し、市町村選択スピナーにセットする
	 */
	private void setCitiesCSV(String prefecture) {
		AssetManager as = getResources().getAssets();
		StringBuilder fName = new StringBuilder("csv/cities/" + prefecture
				+ ".csv");

		dataList.clear();
		shelterId.clear();

		try {
			InputStream is = as.open(new String(fName));
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = "";

			citiesAdapter.clear();
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {
					String data = st.nextToken();

					citiesAdapter.add(data);
					citiesAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

					String id = st.nextToken();
					shelterId.add(id);
				}
			}
			br.close();
			is.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		citiesSpinner.setAdapter(citiesAdapter);
	}

	/*
	 * 市町村のIDリストから避難所情報を受け取りListViewに反映させる
	 */
	private void setShelterInfo(String citiesId) {
		AssetManager as = getResources().getAssets();

		StringBuilder fName = new StringBuilder("csv/shelter/" + citiesId
				+ ".csv");
		Log.i("open", new String(fName));

		try {
			InputStream is = as.open(new String(fName));
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = "";

			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				while (st.hasMoreTokens()) {
					String name = st.nextToken();
					String adress = st.nextToken();

					String tel = st.nextToken();

					String perNum = st.nextToken();

					String object_ = st.nextToken();

					String lati = st.nextToken();
					shelterLatiList.add(Double.parseDouble(lati));

					String longi = st.nextToken();
					shelterLongList.add(Double.parseDouble(longi));

					leng.add(getShelterLen(Double.parseDouble(lati),
							Double.parseDouble(longi)));

					dataList.add(new ShelterListView(name, adress, tel, perNum,
							object_));
					adapter.notifyDataSetChanged();
				}
			}
			br.close();
			isr.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// for (int i = 0; i < dataList.size(); i++) {
		// for (int j = dataList.size() - 1; j > 1; j--) {
		// if (leng.get(j - 1) > leng.get(j)) {
		// List<ShelterListView> sdata = new ArrayList<ShelterListView>();
		// sdata.add(dataList.get(j - 1));
		// dataList.add(j - 1, dataList.get(j));
		// dataList.add(j, sdata.get(0));
		// adapter.notifyDataSetChanged();
		// }
		// }
		// }
	}

	private class ShelterAdapter extends BaseAdapter {
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

		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			TextView name;
			TextView address;
			TextView num;
			TextView tel;
			TextView ob;
			Button finalDestinationBtn;

			View v = convertView;

			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.shelter_list_layout, null);
			}
			ShelterListView shelterListView = (ShelterListView) getItem(position);
			if (shelterListView != null) {
				name = (TextView) v.findViewById(R.id.shelterName);
				address = (TextView) v.findViewById(R.id.textView1);
				num = (TextView) v.findViewById(R.id.TextView02);
				tel = (TextView) v.findViewById(R.id.textView4);
				ob = (TextView) v.findViewById(R.id.textView3);

				finalDestinationBtn = (Button) v
						.findViewById(R.id.finalDestinationBtn);

				ob.setText(shelterListView.shelterObject);
				tel.setText(shelterListView.shelterTel);
				num.setText(shelterListView.shelterPerNum);
				name.setText(shelterListView.shelterName);
				address.setText(shelterListView.shelterAddress);
				finalDestinationBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Log.d("aaa",Integer.toString(position));
						shelterLati = shelterLatiList.get(position);
						shelterLong = shelterLongList.get(position);
					}
				});

			}
			return v;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
