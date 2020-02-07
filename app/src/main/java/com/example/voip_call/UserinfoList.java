package com.example.voip_call;

public class UserinfoList
{
    String imageurl, name, Email;

    UserinfoList(String imageurl, String name, String email) {
        this.imageurl = imageurl;
        this.name = name;
        Email = email;
    }

    String getImageurl() {
        return imageurl;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return Email;
    }
}
