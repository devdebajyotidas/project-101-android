package retrofit;

import okhttp3.ResponseBody;
import retrofit.responseModels.AllServiceRes;
import retrofit.responseModels.AllTimelines;
import retrofit.responseModels.GoogleplaceRes;
import retrofit.responseModels.LoginRes;
import retrofit.responseModels.ServiceDetailsRes;
import retrofit.responseModels.ServiceModel;
import retrofit.responseModels.SuccessRes;
import retrofit.responseModels.ProfileRes;
import retrofit.responseModels.SignupRes;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by Sudip on 4/18/2018.
 */

public interface ApiInterface {
    @FormUrlEncoded
   // @Headers({"Accept: application/json"})
    @POST("accounts/register")
    Call<SignupRes> postSignUp(@Field("mobile") String mobile,
                              @Field("email") String email,
                              @Field("password") String password,
                              @Field("is_provider") int is_provider,
                              @Field("fcm_token") String fcm_token);
    @FormUrlEncoded
    @POST("accounts/login")
    Call<LoginRes> postLogin(@Field("request_id") String request_id,
                         @Field("otp") String otp);

    @GET("accounts/otp/resend/{request_id}")
    Call<SuccessRes> resendOTP(@Path("request_id") String requestId);

    @FormUrlEncoded
    @POST("accounts/otp/request")
    Call<SignupRes> getOTP( @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("accounts/login/advance")
    Call<ResponseBody> postAdvanceLogin(@Field("email") String email,//todo ResponceBody
                                        @Field("password") String password,
                                        @Field("fcm_token") String fcmToken);

    @GET("profile/{accountId}")
    Call<ProfileRes> getProfile( @Path("accountId") int accountId);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @PUT("profile/{accountId}")
    Call<SuccessRes> updateProfile(
           @Path("accountId") int accountId,
           @Field("name") String name,
           @Field("photo") String photo,
           @Field("address") String address,
           @Field("city") String city,
           @Field("zip") String zip,
           @Field("country") String country,
           @Field("latitude") String latitude,
           @Field("longitude") String longitude,
           @Field("dob") String dob

    );

    @DELETE("profile/{accountId}")
    Call<SuccessRes> deleteProfile(@Path("accountId") int accountId);//todo

    @GET("timeline/{accountId}")
    Call<AllTimelines> getAllTimelines(@Path("accountId") int accountId);

    @GET("service/{accountId}")
    Call<AllServiceRes> getAllServices( @Path("accountId") int accountId);

    @FormUrlEncoded
    @POST("service/{accountId}")
    Call<SuccessRes> createService(
            @Path("accountId") int accountId,
            @Field("name") String name,
            @Field("rate") String rate,
            @Field("area") String area,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude);

    @Headers({"Content-Type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @PUT("service/{service_id}")
    Call<SuccessRes> updateService(
            @Path("service_id") int serviceId,
            @Field("rate") String rate,
            @Field("area") String area
    );

    @DELETE("service/{service_id}")
    Call<SuccessRes> deleteService(@Path("service_id") int serviceId);

    @FormUrlEncoded
    @POST("profile/{accountId}/change/mobile")
    Call<SuccessRes> changeMobile(
            @Path("accountId") int accountId,
            @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("profile/{accountId}/change/email")
    Call<SuccessRes> changeEmail(
            @Path("accountId") int accountId,
            @Field("email") String email);

    @FormUrlEncoded
    @POST("profile/{accountId}/change/password")
    Call<SuccessRes> changePassword(
            @Path("accountId") int accountId,
            @Path("password") String password,
            @Field("password_confirmation") String password_confirmation);

    // For TRACKER

    @FormUrlEncoded
    @POST("service/taker/location")
    Call<ServiceModel> getAllServicesByLocation(
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("radius") int radius);



    @FormUrlEncoded
    @POST("service/taker/search")
    Call<ServiceModel> getAllServicesBySearch(
            @Field("user_id") int userId,
            @Field("name") String serviceType/*,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("radius") int radius*/);


    @GET("service/taker/show/{user_id}/{service_id}")
    Call<ServiceDetailsRes> getServiceDetails(@Path("user_id") int userId,
                                              @Path("service_id") int serviceId);

    @GET
    Call<GoogleplaceRes> getPlaceName(@Url String url);

}
