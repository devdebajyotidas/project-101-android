package utils;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit.ApiInterface;
import retrofit.responseModels.LoginRes;

/**
 * Created by Sudip on 4/24/2018.
 */

public class ObjectHolder {
    public static Context latestContext;
    public static SharedPref sharedPref;
    public static final String BASE_URL = "http://serloc.atwebpages.com/api/v1/";
    public static ApiInterface apiInterface;
    public static LoginRes loginRes;
}
