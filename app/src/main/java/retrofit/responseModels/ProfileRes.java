package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudip on 5/19/2018.
 */

public class ProfileRes {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public Data data;


    public class Data{
        @SerializedName("id")
        public int id;
        @SerializedName("name")
        public String name;
        @SerializedName("email")
        public String email;
        @SerializedName("mobile")
        public String mobile;
        @SerializedName("mobile_verified")
        public int mobile_verified;
        @SerializedName("email_verified")
        public int email_verified;
        @SerializedName("account")
        public Account account;

    }

    public class Account {
        @SerializedName("id")
        public int id;
        @SerializedName("about")
        public String about;
        @SerializedName("dob")
        public String dob;
        @SerializedName("address")
        public String address;
        @SerializedName("city")
        public String city;
        @SerializedName("state")
        public String state;
        @SerializedName("country")
        public String country;
        @SerializedName("zip")
        public String zip;
        @SerializedName("aadhaar")
        public String aadhaar;
        @SerializedName("photo")
        public String photo;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("is_provider")
        public int is_provider;
        @SerializedName("aadhaar_verified")
        public int aadhaar_verified;
        @SerializedName("is_blocked")
        public int is_blocked;

    }
}
