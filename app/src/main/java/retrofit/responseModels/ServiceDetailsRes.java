package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XTREMSOFT on 30-May-2018.
 */

public class ServiceDetailsRes {
    @SerializedName("success")
    public boolean success;
    @SerializedName("data")
    public Data data;

    @SerializedName("message")
    public String message;

    public class Data{
        @SerializedName("provider")
        public String provider;
        @SerializedName("rate")
        public int rate;
        @SerializedName("provider_id")
        public int provider_id;
        @SerializedName("name")
        public String name;
        @SerializedName("service_id")
        public int id;
        @SerializedName("address")
        public String address;
        @SerializedName("ratings")
        public float ratings;
        @SerializedName("distance")
        public String distance;
    }

}