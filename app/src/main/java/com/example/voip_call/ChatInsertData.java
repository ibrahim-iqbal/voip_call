package com.example.voip_call;

public class ChatInsertData {
	private String chatid;
	private String massage;
	private String senderId;
	private String reciverId;
	private int unread;
	private long time;

	ChatInsertData(String chatid, String massage, String senderId, String reciverId, int unread, long time) {
		this.chatid = chatid;
		this.massage = massage;
		this.senderId = senderId;
		this.reciverId = reciverId;
		this.unread = unread;
		this.time = time;
	}

	public String getChatid() {
		return chatid;
	}

	public String getMassage() {
		return massage;
	}

	public String getSenderId() {
		return senderId;
	}

	public String getReciverId() {
		return reciverId;
	}

	public int getUnread() {
		return unread;
	}

	public long getTime() {
		return time;
	}
}
