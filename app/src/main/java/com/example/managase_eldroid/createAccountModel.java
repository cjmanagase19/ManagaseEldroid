package com.example.managase_eldroid;

public class createAccountModel {
    private String firstName;
    private String lastName;
    private String middleName;
    private String age;
    private String address;
    private String profileUrl;

    public createAccountModel(String firstName, String lastName, String middleName, String age, String address, String profileUrl ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.age = age;
        this.address = address;
        this.profileUrl = profileUrl;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
