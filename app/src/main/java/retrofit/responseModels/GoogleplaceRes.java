package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by XTREMSOFT on 01-Jun-2018.
 */

public class GoogleplaceRes {
    @SerializedName("status")
    public String status;

    @SerializedName("results")
    public List<Result> results;

    public class Result{
        @SerializedName("formatted_address")
        public String formatted_address;
    }
}
