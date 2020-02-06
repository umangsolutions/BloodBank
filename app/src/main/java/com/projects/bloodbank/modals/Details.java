package com.projects.bloodbank.modals;

/**
 * Created by DELL on 20-02-2018.
 */

public class Details {
    private String id;
    private String name;
    private String email;
    private String number;
    private String password;
    private String blood;
    private String age;
    private String lastDate;
    private String setDate;

    public Details() {
    }

    public Details(String id, String name, String email, String number, String password1, String blood, String age, String lastDate, String setDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.number = number;
        this.password = password1;
        this.blood = blood;
        this.age = age;
        this.lastDate = lastDate;
        this.setDate = setDate;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getSetDate() {
        return setDate;
    }

    public void setSetDate(String setDate) {
        this.setDate = setDate;
    }
}

