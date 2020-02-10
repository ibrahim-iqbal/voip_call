package com.example.voip_call;

public class alluserinfo {
    String email, id, imgurl, name;

    public alluserinfo() {

    }

    public alluserinfo(String email, String id, String imgurl, String name) {
        this.email = email;
        this.id = id;
        this.imgurl = imgurl;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
