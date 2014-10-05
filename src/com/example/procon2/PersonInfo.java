package com.example.procon2;

import android.graphics.Bitmap;

public class PersonInfo {
	final static String injuryItemns[]   = new String[] {"‰ö‰ä–³‚µ Fine", "ŒyÇ@Mild case(C‚è‚È‚Ç)", "’†“™Ç@Moderate Disease (f’f‚ª•K—v)"};
	final static String bloodTypeItems[] = new String[] {"A", "B", "O", "AB"};
	
	private static String name     = "No Data"; //–¼
	private static String nameKana = "No Data"; //‚©‚È•¶š
	private static String age      = "No Data"; //”N—î
	private static String sex      = "No Data"; //«•Ê
	private static String insuranceCardNumber = "No Data"; //•ÛŒ¯Ø”Ô†
	private static int    injury   = -1;
	private static int    bloodTypeId = -1;
	private static Bitmap picture;              //‰æ‘œ
	
	
	public static String getName() {
		return name;
	}
	public static void setName(String name) {
		PersonInfo.name = name;
	}
	public static String getAge() {
		return age;
	}
	public static void setAge(String age) {
		PersonInfo.age = age;
	}
	public static String getSex() {
		return sex;
	}
	public static void setSex(String sex) {
		PersonInfo.sex = sex;
	}
	public static String getInsuranceCardNumber() {
		return insuranceCardNumber;
	}
	public static void setInsuranceCardNumber(String insuranceCardNumber) {
		PersonInfo.insuranceCardNumber = insuranceCardNumber;
	}
	public static Bitmap getPicture() {
		return picture;
	}
	public static void setPicture(Bitmap picture) {
		PersonInfo.picture = picture;
	}
	public static String getNameKana() {
		return nameKana;
	}
	public static void setNameKana(String nameKana) {
		PersonInfo.nameKana = nameKana;
	}
	public static String getInjury() {
		return injuryItemns[injury];
	}
	public static int getInjuryId() {
		return injury;
	}
	public static void setInjury(int id) {
		PersonInfo.injury = id;
	}
	
	public static int getBloodTypeId() {
		return bloodTypeId;
	}
	public static String getBloodType() {
		return bloodTypeItems[bloodTypeId];
	}
	public static void setBloodTypeId(int bloodTypeId) {
		PersonInfo.bloodTypeId = bloodTypeId;
	}
}
