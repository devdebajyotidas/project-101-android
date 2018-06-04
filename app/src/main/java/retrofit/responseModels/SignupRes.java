package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudip on 5/10/2018.
 */

public class SignupRes {
    @SerializedName("success")
    public boolean success;
    @SerializedName("message")
    public String message;
    @SerializedName("request_id")
    public String request_id;
}
