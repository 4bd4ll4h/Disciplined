package com.example.disciplined;

import android.text.TextUtils;

public class Validation {


    public static boolean validateTask(String name){

        if (TextUtils.isEmpty(name) || name.length()>20 ) {

            return false;

        } else {

            return true;
        }
    }
    public static boolean validateDate(String Date){

        if (TextUtils.isEmpty(Date)) {

            return false;

        } else {

            return true;
        }
    }
    public static boolean validateTime(String Time){

        if (TextUtils.isEmpty(Time)) {

            return false;

        } else {

            return true;
        }
    }
}
