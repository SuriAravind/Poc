package com.example.Oauthdemo;

/**
 * Created by Suriyanarayanan K
 * on 25/03/20 9:58 PM.
 */

public class Employee {
    String userId;
    String password;

    public Employee(String userId, String password) {
        super();
        this.userId = userId;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
