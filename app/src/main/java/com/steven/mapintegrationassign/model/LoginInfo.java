package com.steven.mapintegrationassign.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LoginInfo {

    @SerializedName("userName")
    @Expose
    private String userName;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("Version")
    @Expose
    private String version ;


    public LoginInfo(String uname, String pswd, String version){

        this.userName = uname ;
        this.password = pswd;
        this.version = version;
    }



}
