package com.example.voip_call;

public class ChatInsertData {
    String chatid;
    String massage;
    String senderId;
    String reciverId;
    int unread;

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

    public ChatInsertData(String chatid, String massage, String senderId, String reciverId, int unread) {
        this.chatid = chatid;
        this.massage = massage;
        this.senderId = senderId;
        this.reciverId = reciverId;
        this.unread = unread;
    }
}
