package retrofit.responseModels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sudip on 5/11/2018.
 */

public class SuccessRes {
    @SerializedName("success")
    public boolean success;
    @SerializedName("message")
    public String message;
}
