package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.responseModels.LoginRes;
import retrofit.responseModels.SuccessRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Messages;
import utils.Methods;
import utils.ObjectHolder;
import views.OtpView;

/**
 * Created by Sudip on 5/10/2018.
 */

public class OTPActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String REQUEST_ID = "request_id";
    @BindView(R.id.otp_view)
    OtpView otp_view;
    @BindView(R.id.verifyBtn)
    TextView verifyBtn;/*
    @BindView(R.id.backtoLogin)
    TextView backtoLogin;*/
    @BindView(R.id.resend)
    TextView resend;
    @BindView(R.id.waitInterval)
    TextView waitInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_screen);
        ObjectHolder.latestContext =this;
        ButterKnife.bind(this);
        verifyBtn.setOnClickListener(this);
        resend.setOnClickListener(this);
        resend.setEnabled(false);
        setinterval();
    }

    private CountDownTimer cdt;
    private void setinterval() {
        cancleTimer();
        resend.setVisibility(View.GONE);
        cdt = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                waitInterval.setText("" + millisUntilFinished / 1000+"s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                waitInterval.setText("");
                resend.setVisibility(View.VISIBLE);
            }

        };
        cdt.start();
    }
    
    private void cancleTimer(){
        if (cdt != null){
            cdt.cancel();
            cdt = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancleTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.verifyBtn:
                if (isValidate()){
                    loginMethod();
                }
                break;
            case R.id.resend:
               resendOtp();
                break;
            default:
                break;
        }
    }

    private void resendOtp() {
        Methods.showProgress();
        String REQUEST_ID = "";
        if (getIntent() != null){
            REQUEST_ID = getIntent().getStringExtra(REQUEST_ID);
        }
        Call<SuccessRes> call = ObjectHolder.apiInterface.resendOTP(REQUEST_ID);
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes != null){
                        if (successRes.success){
                           setinterval();
                        }else {
                            Methods.showMessage(successRes.message);
                        }
                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<SuccessRes> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }


    private boolean isValidate(){
        if (otp_view.getOtp().length()<6){
            Methods.showMessage(Messages.OTP_VALIDATION);
            return false;
        }
        return true;
    }


    private void loginMethod() {
        Methods.showProgress();
        String request_id = "";
        if (getIntent() != null){
            request_id = getIntent().getStringExtra(REQUEST_ID);
        }
        String otpStr = otp_view.getOtp();
        Call<LoginRes> call = ObjectHolder.apiInterface.postLogin(request_id,otpStr);
        call.enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                Methods.dismissProgress();
                try{
                    LoginRes loginRes = response.body();
                    if (loginRes!= null )
                        if (loginRes.success){
                            ObjectHolder.sharedPref.setLoginRes(loginRes);
                            ObjectHolder.loginRes = loginRes;
                            if (ObjectHolder.loginRes.data.is_provider == 0){
                                openNextScreen(TrakerHome.class);
                            }else {
                                openNextScreen(ProviderHome.class);
                            }
                        }else {
                            Methods.showMessage(loginRes.message);
                        }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<LoginRes> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

    private void openNextScreen(Class<?> cls){
        startActivity(new Intent(OTPActivity.this,cls));
        overridePendingTransition(R.anim.enterright, R.anim.exitleft);
        finish();
    }
}
