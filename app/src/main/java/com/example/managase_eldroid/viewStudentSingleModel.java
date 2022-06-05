package com.example.managase_eldroid;

import android.provider.Contacts;

public class viewStudentSingleModel {
    private String name;
    private String address;
    private String ID;
    private String url;
    private String UID;

    public viewStudentSingleModel(String name, String address, String ID, String url,String UID) {
        this.name = name;
        this.address = address;
        this.ID = ID;
        this.UID = UID;
        this.url = url;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
