package com.example.cbdc.dao;

public class Userinfo
{
    private String name;
    private String idcard;
    private String phonenum;
    private String password;
    private String paypassword;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }
    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getPhonenum() {
        return phonenum;
    }
    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPaypassword() {
        return paypassword;
    }
    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public String toString(){
        return name + "," + idcard + "," + phonenum + "," + password + "," + paypassword;
    }
}