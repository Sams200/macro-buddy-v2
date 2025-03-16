package com.example.sams.controller;

import com.example.sams.entity.User;

public class SessionManager {
    private static User user=null;
    private static Boolean isLoggedIn=false;
    public static void setUser(User user){
        SessionManager.user = user;
        isLoggedIn=true;
    }

    public static User getUser(){
        return user;
    }

    public static boolean isLoggedIn(){
        return isLoggedIn;
    }

    public static void logout(){
        isLoggedIn=false;
        user=null;
    }
}
