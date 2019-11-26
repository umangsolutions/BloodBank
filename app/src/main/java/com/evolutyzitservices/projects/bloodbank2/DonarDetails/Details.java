package com.evolutyzitservices.projects.bloodbank2.DonarDetails;

/**
 * Created by DELL on 20-02-2018.
 */

public class Details {
    String id;
    String name;
    String email;
    String number;
    String password1;
    String blood;
    String pincode;

    public Details(String id, String name, String email, String number, String password, String blood, String pincode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.password1 = password;
        this.blood = blood;
        this.pincode = pincode;
    }

    public Details() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
