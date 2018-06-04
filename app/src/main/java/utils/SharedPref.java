package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit.responseModels.LoginRes;

/**
 * Created by XtremsoftTechnologies on 22/02/16.
 */
public class SharedPref {

    public  SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    private final String className=getClass().getName();

    public SharedPref(Context context, String appName)
    {
        sharedPref = context.getSharedPreferences(appName, context.MODE_PRIVATE);
    }
    public  void storePref(String tagName, String tagValue) {
        try {
            editor = sharedPref.edit();
            editor.putString(tagName, tagValue);
            editor.commit();
        } catch (Exception e) {
            Methods.printException(e);
        }
    }
    public  String getPref(String tagName, String defaultValue) {
        try {
            String prefValue = sharedPref.getString(tagName, defaultValue);
            return prefValue;
        } catch (Exception e) {
            Methods.printException(e);
            return defaultValue;
        }
    }
    public  void storePref(String tagName, int value) {
        try {
            editor = sharedPref.edit();
            editor.putInt(tagName, value);
            editor.commit();
        } catch (Exception e) {
            Methods.printException(e);
        }
    }

    public  int getPref(String tagName, int defaultValue) {
        try {
            int prefValue = sharedPref.getInt(tagName, defaultValue);
            return prefValue;
        } catch (Exception e) {
            Methods.printException(e);
            return defaultValue;
        }
    }
    public void storePref(String tagName, boolean tagValue) {
        try {
            editor = sharedPref.edit();
            editor.putBoolean(tagName, tagValue);
            editor.commit();
        } catch (Exception e) {
            Methods.printException(e);
        }
    }

    public boolean getPref(String tagName, boolean defaultValue) {
        try {
            boolean prefValue = sharedPref.getBoolean(tagName, defaultValue);
            return prefValue;
        } catch (Exception e) {
            Methods.printException(e);
            return defaultValue;
        }
    }

    public LoginRes getLoginRes() {
        Gson gson = new Gson();
        String json = sharedPref.getString("loginDetails", null);
        Type type = new TypeToken<LoginRes>() {}.getType();
        LoginRes loginRes = gson.fromJson(json, type);
        return loginRes;
    }
    public void setLoginRes(LoginRes loginRes){
        editor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(loginRes);
        editor.putString("loginDetails", json);
        editor.commit();
    }


    public void clearPreference(){
        editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }
}
