package com.example.voip_call;

public class UserinfoList {
	String imageurl, name, massage, id, Email;
	private long tm;

	public UserinfoList() {
	}

	UserinfoList(String imageurl, String name, String id, String email) {
		this.imageurl = imageurl;
		this.name = name;
		this.id = id;
		Email = email;
	}

	UserinfoList(String imageurl, String name, String massage, String Id, long tm) {
		this.imageurl = imageurl;
		this.name = name;
		this.massage = massage;
		id = Id;

		this.tm = tm;
	}

	public UserinfoList() {
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	long getTm() {
		return tm;
	}

	public void setTm(long tm) {
		this.tm = tm;
	}

	String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	String getMassage() {
		return massage;
	}

	public void setMassage(String massage) {
		this.massage = massage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
