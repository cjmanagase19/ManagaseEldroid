package com.example.managase_eldroid;

public class uploadImage {
    private String imageName;
    private String imageUrl;


    public uploadImage(String imageName, String imageUrl) {
        if (imageName.trim().equals("")) {
            imageName = "nameless";
        }
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
