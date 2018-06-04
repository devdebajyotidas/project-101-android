package com.example.newproject;

import android.app.Application;

import utils.ObjectHolder;
import utils.SharedPref;

public class ApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ObjectHolder.latestContext = getApplicationContext();
        ObjectHolder.sharedPref = new SharedPref(ObjectHolder.latestContext,getPackageName());
        ObjectHolder.loginRes = ObjectHolder.sharedPref.getLoginRes();
    }
}
