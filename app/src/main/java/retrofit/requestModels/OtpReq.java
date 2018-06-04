package retrofit.requestModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudip on 5/11/2018.
 */

public class OtpReq {
    @SerializedName("request_id")
    public String request_id;
    @SerializedName("otp")
    public String otp;
}
