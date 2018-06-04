package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sudip on 5/28/2018.
 */

public class ServiceModel {
    @SerializedName("success")
    public boolean success;
    @SerializedName("data")
    public List<Data> dataList;

    @SerializedName("message")
    public String message;

    public class Data{
        @SerializedName("service_id")
        public int id;
        @SerializedName("account_id")
        public int account_id;
        @SerializedName("name")
        public String name;
        @SerializedName("latitude")
        public double latitude;
        @SerializedName("longitude")
        public double longitude;
    }

}
