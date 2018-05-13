package com.kt3.android.domain;

import java.io.Serializable;

/**
 * Created by 97lynk on 11/04/2018.
 */

public class Account implements Serializable {

    private int id;

    private String userName;

    private String password;

    private boolean enabled;

    private Profile profile;

    //contructors
    public Account() {
    }

    public Account(String userName, boolean enabled) {
        this.userName = userName;
        this.enabled = enabled;
    }

    // standard getters and setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
