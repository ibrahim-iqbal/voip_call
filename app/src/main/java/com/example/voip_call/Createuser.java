package com.example.voip_call;

public class Createuser {
    String Email;
    String Id;
    String imgurl;
    String name;

    public Createuser(String email, String id, String imgurl, String name) {
        Email = email;
        Id = id;
        this.imgurl = imgurl;
        this.name = name;
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
}
