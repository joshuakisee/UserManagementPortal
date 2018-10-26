package com.usermportal.usermanagementportal.util;

public class Validation {

    public static boolean emailValidation(String email)
    {
        if(email.equals("") || !email.contains("@")) return false;
        else return true;
    }

    public static boolean passwordValidation(String password)
    {
        if(password.equals("")) return false;
        else return true;
    }

}
