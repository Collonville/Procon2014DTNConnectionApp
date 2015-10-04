package com.example.procon2;

import java.util.ArrayList;
import java.util.List;

public class DTNPerosonalInfoCollection {
	private static List<String> name = new ArrayList<String>();
	private static List<String> nameKana = new ArrayList<String>();
	private static List<String> age = new ArrayList<String>();
	private static List<String> sex = new ArrayList<String>();
	private static List<String> number = new ArrayList<String>();
	private static List<String> blood = new ArrayList<String>();
	private static List<String> injury = new ArrayList<String>();
	private static List<String> pic = new ArrayList<String>();
	private static List<String> hash = new ArrayList<String>();
	private static List<String> time = new ArrayList<String>();
	private static List<String> message = new ArrayList<String>();

	public static synchronized void addData(String name, String nameKana,
			String age, String sex, String number, String blood, String injury,
			String pic, String hash, String time, String message) {
		DTNPerosonalInfoCollection.name.add(name);
		DTNPerosonalInfoCollection.nameKana.add(nameKana);
		DTNPerosonalInfoCollection.age.add(age);
		DTNPerosonalInfoCollection.sex.add(sex);
		DTNPerosonalInfoCollection.number.add(number);
		DTNPerosonalInfoCollection.blood.add(blood);
		DTNPerosonalInfoCollection.injury.add(injury);
		DTNPerosonalInfoCollection.pic.add(pic);
		DTNPerosonalInfoCollection.hash.add(hash);
		DTNPerosonalInfoCollection.time.add(time);
		DTNPerosonalInfoCollection.message.add(message);
	}

	public static synchronized void deleteAllData() {
		DTNPerosonalInfoCollection.name.clear();
		DTNPerosonalInfoCollection.nameKana.clear();
		DTNPerosonalInfoCollection.age.clear();
		DTNPerosonalInfoCollection.sex.clear();
		DTNPerosonalInfoCollection.number.clear();
		DTNPerosonalInfoCollection.blood.clear();
		DTNPerosonalInfoCollection.injury.clear();
		DTNPerosonalInfoCollection.pic.clear();
		DTNPerosonalInfoCollection.hash.clear();
		DTNPerosonalInfoCollection.time.clear();
		DTNPerosonalInfoCollection.message.clear();
	}

	public static synchronized List<String> getMessage() {
		return message;
	}

	public static synchronized void setMessage(List<String> message) {
		DTNPerosonalInfoCollection.message = message;
	}

	public static synchronized List<String> getHash() {
		return hash;
	}

	public static synchronized void setHash(List<String> hash) {
		DTNPerosonalInfoCollection.hash = hash;
	}

	public static synchronized List<String> getName() {
		return name;
	}

	public static synchronized void setName(List<String> name) {
		DTNPerosonalInfoCollection.name = name;
	}

	public static synchronized List<String> getNameKana() {
		return nameKana;
	}

	public static synchronized void setNameKana(List<String> nameKana) {
		DTNPerosonalInfoCollection.nameKana = nameKana;
	}

	public static synchronized List<String> getAge() {
		return age;
	}

	public static synchronized void setAge(List<String> age) {
		DTNPerosonalInfoCollection.age = age;
	}

	public static synchronized List<String> getSex() {
		return sex;
	}

	public static synchronized void setSex(List<String> sex) {
		DTNPerosonalInfoCollection.sex = sex;
	}

	public static synchronized List<String> getNumber() {
		return number;
	}

	public static synchronized void setNumber(List<String> number) {
		DTNPerosonalInfoCollection.number = number;
	}

	public static synchronized List<String> getBlood() {
		return blood;
	}

	public static synchronized void setBlood(List<String> blood) {
		DTNPerosonalInfoCollection.blood = blood;
	}

	public static synchronized List<String> getInjury() {
		return injury;
	}

	public static synchronized void setInjury(List<String> injury) {
		DTNPerosonalInfoCollection.injury = injury;
	}

	public static synchronized List<String> getPic() {
		return pic;
	}

	public static synchronized void setPic(List<String> pic) {
		DTNPerosonalInfoCollection.pic = pic;
	}

	public static synchronized List<String> getTime() {
		return time;
	}

	public static synchronized void setTime(List<String> time) {
		DTNPerosonalInfoCollection.time = time;
	}

}
