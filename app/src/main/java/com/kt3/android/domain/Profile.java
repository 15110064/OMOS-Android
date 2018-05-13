package com.kt3.android.domain;

import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;


public class Profile implements Serializable {

    private int id;

    private String firstName;

    private String lastName;

    private long birthDay;

    private String emailAddress;

    public Profile(){
    }

    public Profile(String firstName, String lastName, long birthDay, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDay = birthDay;
        this.emailAddress = emailAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getBirthDay() {
        return birthDay;
    }

    public String getBirthDayString() {
        Date date = new Date(birthDay);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(birthDay);
    }

    public void setBirthDay(long birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
