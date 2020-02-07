package com.example.voip_call;

class Chat_Data {
    private String massage;
    private String sender;


    Chat_Data(String massage, String sender) {
        this.massage = massage;
        this.sender = sender;

    }

    String getMassage() {
        return massage;
    }

    String getSender() {
        return sender;
    }

}

