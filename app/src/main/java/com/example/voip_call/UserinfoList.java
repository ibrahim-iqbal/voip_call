package com.example.voip_call;

public class UserinfoList
{
    String imageurl, name, massage, id, Email;

    public UserinfoList(String imageurl, String name, String id, String email) {
        this.imageurl = imageurl;
        this.name = name;
        this.id = id;
        Email = email;
    }

    public String getEmail() {
        return Email;
    }
    long tm;

    public UserinfoList(String imageurl, String name, String massage, String Id, long tm) {
        this.imageurl = imageurl;
        this.name = name;
        this.massage = massage;
        id = Id;
        this.tm = tm;
    }

    public long getTm() {
        return tm;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public String getMassage() {
        return massage;
    }

    public String getId() {
        return id;
    }
}
