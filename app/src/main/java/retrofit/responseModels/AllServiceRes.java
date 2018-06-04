package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sudip on 5/21/2018.
 */

public class AllServiceRes {

    @SerializedName("success")
    public boolean success;
    @SerializedName("data")
    public List<Data> dataList;

    @SerializedName("message")
    public String message;

    public class Data{
        @SerializedName("id")
        public int id;
        @SerializedName("account_id")
        public int account_id;
        @SerializedName("name")
        public String name;
        @SerializedName("rate")
        public int rate;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
        @SerializedName("area")
        public String area;
        @SerializedName("is_active")
        public int is_active;
        @SerializedName("image")
        public String image;
    }

}
