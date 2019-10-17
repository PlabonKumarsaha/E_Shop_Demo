package com.example.e_shop_demo.Model;

public class User {

    private String UserName,Password;
    private String PhoneNo;

    public User()
    {

    }

    public User(String userName, String password, String phoneNo) {
        UserName = userName;
        Password = password;
        PhoneNo = phoneNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }
}
