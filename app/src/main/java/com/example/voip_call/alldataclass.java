package com.example.voip_call;

public class alldataclass {
    String massage, show_id;
    long time;

    public alldataclass() {
    }

    public alldataclass(String massage, String show_id, long time) {
        this.massage = massage;
        this.show_id = show_id;
        this.time = time;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getShow_id() {
        return show_id;
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
