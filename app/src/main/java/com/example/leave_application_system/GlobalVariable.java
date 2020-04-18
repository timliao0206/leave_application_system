package com.example.leave_application_system;

import android.app.Application;

//this is global variable , you may use them in any activity.
//how: declare globalvariable = (GlobalVariable) getApplicationContext()

public class GlobalVariable extends Application {
    public String username="";
    public String identity="";
    public int id;

    public final String url = "jdbc:mysql://db4free.net:3306/";
    public final String dbuser = "timliao0206";
    public final String dbpassword = "hill1017";

    public void setUsername(String user){
        this.username=user;
    }

    public void setIdentity(String id){
        this.identity = id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){return this.username;}

    public String getIdentity(){return this.identity;}

    public int getId(){return this.id;}
}
