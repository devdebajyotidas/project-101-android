package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Sudip on 5/21/2018.
 */

public class AllTimelines {

    @SerializedName("success")
    public boolean success;
    @SerializedName("data")
    public List<Data> dataList;

    @SerializedName("message")
    public String message;

    public class Data{
        @SerializedName("taken_id")
        public int takenId;
        @SerializedName("service_id")
        public int serviceId;
        @SerializedName("service_name")
        public String serviceName;
        @SerializedName("taken_at")
        public String takenAt;
        @SerializedName("amount")
        public int amount;
        @SerializedName("duration")
        public int duration;
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("user_id")
        public int userId;
        @SerializedName("progress")
        public String progress;
    }
}
