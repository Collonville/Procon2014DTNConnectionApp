package com.example.procon2;

import org.msgpack.annotation.MessagePackMessage;

@MessagePackMessage
public class PersonalInfoDataSet {
	public String name;
	public String nameKana;
	public String age;
	public String sex;
	public String number;
	public String blood;
	public String injury;
	public String pic;
	public String hash;
	public String time;
	public String message;

	public PersonalInfoDataSet() {

	}

	public PersonalInfoDataSet(String name, String nameKana, String age,
			String sex, String number, String blood, String injury, String pic,
			String hash, String time, String message) {
		this.name = name;
		this.nameKana = nameKana;
		this.age = age;
		this.sex = sex;
		this.number = number;
		this.blood = blood;
		this.injury = injury;
		this.pic = pic;
		this.hash = hash;
		this.time = time;
		this.message = message;
	}

}
