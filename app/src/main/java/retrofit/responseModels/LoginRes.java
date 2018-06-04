package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudip on 5/18/2018.
 */

public class LoginRes {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public Data data;

    @SerializedName("message")
    public String message;

    public class Data{
        @SerializedName("account_id")
        public int account_id;
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("is_provider")
        public int is_provider;
        @SerializedName("is_blocked")
        public int is_blocked;
    }
}