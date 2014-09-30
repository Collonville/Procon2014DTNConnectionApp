package com.example.procon2;

import android.graphics.Bitmap;

public class PersonInfo {
	private static String name;                //–¼
	private static String nameKana;
	private static String age;                 //”N—î
	private static String sex;                 //«•Ê
	private static String insuranceCardNumber; //•ÛŒ¯Ø”Ô†
	private static Bitmap   picture;             //‰æ‘œ
	
	
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
}
