package com.example.procon2;

public class PersonInfo {
	final static String injuryItemns[] = new String[] { "���䖳�� Fine",
			"�y��(�C�菝�Ȃ�)", "������(�f�f���K�v)" };
	final static String bloodTypeItems[] = new String[] { "A", "B", "O", "AB" };

	private static String name = "�f�[�^�Ȃ�"; // ����
	private static String nameKana = "�f�[�^�Ȃ�"; // ���ȕ���
	private static String age = "�f�[�^�Ȃ�"; // �N��
	private static String sex = "�f�[�^�Ȃ�"; // ����
	private static String insuranceCardNumber = "�f�[�^�Ȃ�"; // �ی��ؔԍ�
	private static int injury = 0;
	private static int bloodTypeId = 0;
	private static String picture = null; // �摜

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
