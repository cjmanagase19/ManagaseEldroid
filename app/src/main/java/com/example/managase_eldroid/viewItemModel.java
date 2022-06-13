package com.example.managase_eldroid;

import android.provider.Contacts;

public class viewItemModel {
   private String price;
   private String title;
   private String URL;
   private String date;
   private String author;
   private String UID;

    public viewItemModel(String price, String title, String URL, String date, String author,String UID) {
        this.price = price;
        this.title = title;
        this.URL = URL;
        this.date = date;
        this.author = author;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
