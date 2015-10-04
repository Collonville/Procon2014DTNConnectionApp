package com.example.procon2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class PersonalInformation extends Fragment implements OnClickListener {
	static final int REQUEST_CAPTURE_IMAGE = 100;

	private static Context context;

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
	private static Button addDataBtn;
	private static EditText message;

	private static ListView personalInfoListView;
	private static List<PersonalInfoListView> dataList;
	private static ChatAdapter adapter;

	static int a;

	public PersonalInformation(Context context) {
		PersonalInformation.context = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.perosonal_information_layout,
				container, false);

		nameTextView = (TextView) v.findViewById(R.id.nameView);
		sexTextView = (TextView) v.findViewById(R.id.sexView);
		insuranceCardNumberTextView = (TextView) v
				.findViewById(R.id.insuranceCardNumberView);
		injuryVTextiew = (TextView) v.findViewById(R.id.injuryView);
		message = (EditText) v.findViewById(R.id.message);

		nameTextView.setText(PersonInfo.getName() + "("
				+ PersonInfo.getNameKana() + ")");
		sexTextView.setText(PersonInfo.getAge() + "/" + PersonInfo.getSex());
		insuranceCardNumberTextView.setText(PersonInfo.getInsuranceCardNumber()
				+ "/" + PersonInfo.getBloodType());

		dataList = new ArrayList<PersonalInfoListView>();
		adapter = new ChatAdapter();

		personalInfoListView = (ListView) v.findViewById(R.id.personalListView);
		personalInfoListView.setAdapter(adapter);

		personalInfoListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					// リスト項目クリック時の処理
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								context);

						// ダイアログの設定
						alertDialog.setTitle("メッセージ");
						alertDialog.setMessage(dataList.get(position).message);

						alertDialog.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
									}
								});

						// ダイアログの作成と表示
						alertDialog.create();
						alertDialog.show();

					}
				});

		switch (PersonInfo.getInjuryId()) {
		case 0:
		case 1:
			injuryVTextiew.setBackgroundColor(Color.GREEN);
			injuryVTextiew.setText(PersonInfo.getInjury());
			break;
		case 2:
			injuryVTextiew.setBackgroundColor(Color.YELLOW);
			injuryVTextiew.setText(PersonInfo.getInjury());
			break;
		}

		/** 初期画像の読み込み　 **/
		picture = v.findViewById(R.id.pitureView);

		/** すでに読み込み済みの画像があればそれを使う　 **/
		if (PersonInfo.getPicture() == null) {
			AssetManager as = getResources().getAssets();
			try {
				InputStream is = as.open("images/noImage/noImage1.gif");
				Bitmap bm = BitmapFactory.decodeStream(is);
				((ImageView) picture).setImageBitmap(bm);
			} catch (IOException e) {
				Log.d("Load Pic", e.toString());
			}
		} else {
			byte[] encodeByte = Base64.decode(PersonInfo.getPicture(),
					Base64.DEFAULT);
			Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
					encodeByte.length);
			((ImageView) picture).setImageBitmap(bitmap);
		}
		/** End **/

		takePicButton = v.findViewById(R.id.takePictureButton);
		((Button) takePicButton).setOnClickListener(this);

		nameBtn = v.findViewById(R.id.changeNameBtn);
		((Button) nameBtn).setOnClickListener(this);

		sexBtn = v.findViewById(R.id.changeSexBtn);
		((Button) sexBtn).setOnClickListener(this);

		insuranceCardNumberBtn = v
				.findViewById(R.id.changeInsuranceCardNumberBtn);
		((Button) insuranceCardNumberBtn).setOnClickListener(this);

		injuryBtn = v.findViewById(R.id.changeInjuryBtn);
		((Button) injuryBtn).setOnClickListener(this);

		addDataBtn = (Button) v.findViewById(R.id.add);
		addDataBtn.setOnClickListener(this);

		if (!DTNPerosonalInfoCollection.getHash().isEmpty()) {
			for (int i = 0; i < DTNPerosonalInfoCollection.getHash().size(); i++) {
				dataList.add(0, new PersonalInfoListView(
						DTNPerosonalInfoCollection.getName().get(i),
						DTNPerosonalInfoCollection.getNameKana().get(i),
						DTNPerosonalInfoCollection.getAge().get(i),
						DTNPerosonalInfoCollection.getSex().get(i),
						DTNPerosonalInfoCollection.getNumber().get(i),
						DTNPerosonalInfoCollection.getBlood().get(i),
						DTNPerosonalInfoCollection.getInjury().get(i),
						DTNPerosonalInfoCollection.getPic().get(i),
						DTNPerosonalInfoCollection.getTime().get(i),
						DTNPerosonalInfoCollection.getMessage().get(i)));
				adapter.notifyDataSetChanged();
			}
		}
		return v;
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		if (v == takePicButton) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
		} else if (v == nameBtn) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.personal_info_daialog_name,
					null);

			final EditText nameInput = (EditText) view
					.findViewById(R.id.nameInput);
			final EditText kanaInput = (EditText) view
					.findViewById(R.id.kanaInput);

			new AlertDialog.Builder(context)
					.setView(view)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									PersonInfo.setName(nameInput.getText()
											.toString());
									PersonInfo.setNameKana(kanaInput.getText()
											.toString());

									nameTextView.setText(nameInput.getText()
											.toString()
											+ "("
											+ kanaInput.getText().toString()
											+ ")");

									DeviceInfo.setDeviceName(nameInput
											.getText().toString());
								}
							}).show();
		} else if (v == sexBtn) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.personal_info_dialog_sexage,
					null);

			final EditText ageInput = (EditText) view
					.findViewById(R.id.ageInput);
			final String items[] = new String[] { "男性(Male)", "女性(Female)" };

			new AlertDialog.Builder(context)
					.setView(view)
					.setSingleChoiceItems(items, 0,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									PersonInfo.setSex(items[which]);
								}
							})
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									PersonInfo.setAge(ageInput.getText()
											.toString());
									sexTextView.setText(PersonInfo.getAge()
											+ "/" + PersonInfo.getSex());

								}
							}).show();
		} else if (v == insuranceCardNumberBtn) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(
					R.layout.personal_info_daialog_insurancecardnumber, null);

			final EditText insuranceCardNumberInput = (EditText) view
					.findViewById(R.id.insuranceCarNumberInput);

			new AlertDialog.Builder(context)
					.setView(view)
					.setSingleChoiceItems(PersonInfo.bloodTypeItems, 0,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									PersonInfo.setBloodTypeId(which);
								}
							})
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									PersonInfo
											.setInsuranceCardNumber(insuranceCardNumberInput
													.getText().toString());

									insuranceCardNumberTextView.setText(insuranceCardNumberInput
											.getText().toString()
											+ "/"
											+ PersonInfo.getBloodType());
								}
							}).show();
		} else if (v == injuryBtn) {
			new AlertDialog.Builder(context)
					.setSingleChoiceItems(PersonInfo.injuryItemns, 0,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									PersonInfo.setInjury(which);
								}
							})
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									injuryVTextiew.setText(PersonInfo
											.getInjury());

									switch (PersonInfo.getInjuryId()) {
									case 0:
									case 1:
										injuryVTextiew
												.setBackgroundColor(Color.GREEN);
										break;
									case 2:
										injuryVTextiew
												.setBackgroundColor(Color.YELLOW);
										break;
									}

								}
							}).show();
		} else if (v == addDataBtn) {
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			System.out.println(df.format(date));

			DTNPerosonalInfoCollection.addData(
					PersonInfo.getName(),
					PersonInfo.getNameKana(),
					PersonInfo.getAge(),
					PersonInfo.getSex(),
					PersonInfo.getInsuranceCardNumber(),
					PersonInfo.getBloodType(),
					PersonInfo.getInjury(),
					PersonInfo.getPicture(),
					getHash(PersonInfo.getNameKana() + PersonInfo.getAge()
							+ PersonInfo.getSex()
							+ PersonInfo.getInsuranceCardNumber()
							+ df.format(date) + DeviceInfo.getDeviceMAC()),
					df.format(date), message.getText().toString());

			dataList.add(
					0,
					new PersonalInfoListView(PersonInfo.getName(), PersonInfo
							.getNameKana(), PersonInfo.getAge(), PersonInfo
							.getSex(), PersonInfo.getInsuranceCardNumber(),
							PersonInfo.getBloodType(), PersonInfo.getInjury(),
							PersonInfo.getPicture(), df.format(date), message
									.getText().toString()));
			adapter.notifyDataSetChanged();

			message.getEditableText().clear();
		}
	}

	public static void addData(final PersonalInfoDataSet personalInfoDataSet) {
		dataList.add(0, new PersonalInfoListView(personalInfoDataSet.name,
				personalInfoDataSet.nameKana, personalInfoDataSet.age,
				personalInfoDataSet.sex, personalInfoDataSet.number,
				personalInfoDataSet.blood, personalInfoDataSet.injury,
				personalInfoDataSet.pic, personalInfoDataSet.time,
				personalInfoDataSet.message));
		adapter.notifyDataSetChanged();
		Toast.makeText(context, "新しい安否情報が受信されました", Toast.LENGTH_SHORT).show();
	}

	private String getHash(String text) {
		MessageDigest md = null;
		StringBuffer buffer = new StringBuffer();

		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			Log.d("NoneHashAlgoError", "No Algo");
		}

		md.update(text.getBytes());

		byte[] valueArray = md.digest();

		for (int i = 0; i < valueArray.length; i++) {
			String tmpStr = Integer.toHexString(valueArray[i] & 0xff);

			if (tmpStr.length() == 1) {
				buffer.append('0').append(tmpStr);
			} else {
				buffer.append(tmpStr);
			}
		}
		return buffer.toString();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private class ChatAdapter extends BaseAdapter {
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
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView name;
			TextView nameKana;
			TextView age;
			TextView sex;
			TextView number;
			TextView blood;
			TextView injury;
			ImageView pic;
			TextView time;

			View v = convertView;

			if (v == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.personal_info_listview_layout,
						null);
			}
			PersonalInfoListView personalInfoListView = (PersonalInfoListView) getItem(position);
			if (personalInfoListView != null) {
				name = (TextView) v.findViewById(R.id.name);
				nameKana = (TextView) v.findViewById(R.id.namekana);
				age = (TextView) v.findViewById(R.id.age);
				sex = (TextView) v.findViewById(R.id.sex);
				number = (TextView) v.findViewById(R.id.number);
				blood = (TextView) v.findViewById(R.id.blood);
				injury = (TextView) v.findViewById(R.id.injury);
				pic = (ImageView) v.findViewById(R.id.pic);
				time = (TextView) v.findViewById(R.id.time);

				if (personalInfoListView.name != null)
					name.setText(personalInfoListView.name);
				else
					name.setText("データなし");

				if (personalInfoListView.nameKana != null)
					nameKana.setText(personalInfoListView.nameKana);
				else
					nameKana.setText("データなし");

				if (personalInfoListView.age != null)
					age.setText(personalInfoListView.age);
				else
					age.setText("データなし");

				if (personalInfoListView.sex != null)
					sex.setText(personalInfoListView.sex);
				else
					sex.setText("データなし");

				if (personalInfoListView.number != null)
					number.setText(personalInfoListView.number);
				else
					number.setText("データなし");

				if (personalInfoListView.blood != null)
					blood.setText(personalInfoListView.blood);
				else
					blood.setText(personalInfoListView.blood);

				injury.setText(personalInfoListView.injury);
				time.setText(personalInfoListView.time);

				if (personalInfoListView.pic == null) {
					AssetManager as = context.getResources().getAssets();
					try {
						InputStream is = as.open("images/noImage/noImage1.gif");
						Bitmap bm = BitmapFactory.decodeStream(is);
						pic.setImageBitmap(bm);
					} catch (IOException e) {
						Log.d("Load Pic", e.toString());
					}
				} else {
					byte[] encodeByte = Base64.decode(personalInfoListView.pic,
							Base64.DEFAULT);
					Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte,
							0, encodeByte.length);
					pic.setImageBitmap(bitmap);
				}

			}
			return v;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (REQUEST_CAPTURE_IMAGE == requestCode
				&& resultCode == Activity.RESULT_OK) {
			Bitmap capturedImage = (Bitmap) data.getExtras().get("data");

			((ImageView) picture).setImageBitmap(capturedImage);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			capturedImage.compress(Bitmap.CompressFormat.JPEG, 20, baos);
			byte[] b = baos.toByteArray();
			String temp = Base64.encodeToString(b, Base64.DEFAULT);

			PersonInfo.setPicture(temp);
		}
	}

}
