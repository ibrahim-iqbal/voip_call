package com.example.voip_call;

public class Createuser {
	String name;
	private String Email;
	private String Id;
	private String imgurl;
	private String num;

	Createuser(String email, String id, String imgurl, String name, String num) {
		Email = email;
		Id = id;
		this.imgurl = imgurl;
		this.name = name;
		this.num = num;
	}

	public String getId() {
		return Id;
	}

	public String getImgurl() {
		return imgurl;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return Email;
	}

	public String getNum() {
		return num;
	}
}
