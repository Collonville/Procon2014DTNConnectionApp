package com.example.procon2;

public class PersonInfo {
	final static String injuryItemns[] = new String[] { "怪我無し Fine",
			"軽症(擦り傷など)", "中等症(診断が必要)" };
	final static String bloodTypeItems[] = new String[] { "A", "B", "O", "AB" };

	private static String name = "データなし"; // 氏名
	private static String nameKana = "データなし"; // かな文字
	private static String age = "データなし"; // 年齢
	private static String sex = "データなし"; // 性別
	private static String insuranceCardNumber = "データなし"; // 保険証番号
	private static int injury = 0;
	private static int bloodTypeId = 0;
	private static String picture = null; // 画像

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

	public static String getPicture() {
		return picture;
	}

	public static void setPicture(String picture) {
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
