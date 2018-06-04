package com.example.newproject;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import extras.AllServicesActivity;
import okhttp3.ResponseBody;
import retrofit.responseModels.LoginRes;
import retrofit.responseModels.SuccessRes;
import retrofit.responseModels.SignupRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Colors;
import utils.Messages;
import utils.Methods;
import utils.ObjectHolder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener ,
        TextWatcher{

    private String REQUEST_ID = "";
    private boolean advLogin = false;

    @BindView(R.id.loginBtn)
    TextView loginBtn;
    @BindView(R.id.signUp)
    TextView signUp;
    @BindView(R.id.root)
    ConstraintLayout root;
    @BindView(R.id.loginLinear)
    LinearLayout loginLinear;
    @BindView(R.id.advLoginLinear)
    LinearLayout advLoginLinear;
    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.otp)
    EditText otp;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.resend)
    TextView resend;
    @BindView(R.id.tryAnotherMethod)
    TextView tryAnotherMethod;
    @BindView(R.id.mainRelative)
    RelativeLayout mainRelative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
       // new InitUI(this,R.layout.login_screen);
        setContentView(R.layout.new_login_screen);
        ObjectHolder.latestContext =this;
        ButterKnife.bind(this);
       // Sequent.origin(root).anim(this, SequentAnimation.FADE_IN_UP).start();
        loginBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        mobile.addTextChangedListener(this);
        resend.setOnClickListener(this);
        tryAnotherMethod.setOnClickListener(this);
        //loading.start();
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.loginBtn:
              // openNextScreen(TrakerHome.class);
               loginClick();
               break;
           case R.id.signUp:
               openNextScreen(SignupActivity.class);
               break;
           case R.id.resend:
               if (TextUtils.isEmpty(REQUEST_ID)){
                   Methods.showMessage(Messages.NOT_REQUESTOTP);                   
               }else if(!resend.getText().equals("Resend")){
                   Methods.showMessage(Messages.WAIT_FORSMS);
               }else {
                   resendOtp();
               }
               break;
           case R.id.tryAnotherMethod:
               tryAnotherMethod();
               break;
               default:
                   break;
       }
    }

    private void openNextScreen(Class<?> cls){
        startActivity(new Intent(LoginActivity.this,cls));
        overridePendingTransition(R.anim.enterright, R.anim.exitleft);
        finish();
    }

    private void loginClick() {
        if (advLogin){
            if (!Methods.isEmailValid(email.getText().toString().trim())){
                Methods.showMessage(Messages.EMAIL_VALIDATION);
            }else 
            if (password.getText().toString().trim().length() < 6){
                Methods.showMessage(Messages.PASSWORD_VALIDATION);
            }else {
              advancelogin();  
            }            
        }
        else {
            if (mobile.getText().toString().trim().length() != 10){
                Methods.showMessage(Messages.MOBILE_VALIDATION);
            }else if (otp.getText().toString().trim().length()<6){
                Methods.showMessage(Messages.OTP_VALIDATION);
            }else{
                loginViaOTP();
            }
        }
    }



    private void tryAnotherMethod() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (advLogin){
                    advLogin = false;
                    email.setText("");
                    password.setText("");
                    slidefromLefttoRight();
                }else {
                    advLogin = true;
                    cancleTimer();
                    mobile.setText("");
                    otp.setText("");
                    REQUEST_ID = "";
                    slidefromRightToLeft();
                }
            }
        });

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length()==10){
            Methods.hidesoftkey(root);
            requestforOTP(s.toString());
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        cancleTimer();
    }

    private CountDownTimer cdt;
    
    private void setinterval() {
        cancleTimer();
        resend.setTextColor(Colors.TextColor);
        cdt = new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend.setText("" + millisUntilFinished / 1000+"s");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                resend.setText("Resend");
                resend.setTextColor(Colors.PrimaryColor);
            }

        };
        cdt.start();
    }

    private void cancleTimer(){
        if (cdt != null){
            cdt.cancel();
            cdt = null;
            resend.setTextColor(Colors.PrimaryColor);
            resend.setText("Resend");
        }
    }


    private void requestforOTP(String mobileNo) {
        mobile.setEnabled(false);
        Methods.showProgress();
        Call<SignupRes> call = ObjectHolder.apiInterface.getOTP(mobileNo);
        call.enqueue(new Callback<SignupRes>() {
            @Override
            public void onResponse(Call<SignupRes> call, Response<SignupRes> response) {
                try{
                    Methods.dismissProgress();
                    mobile.setEnabled(true);
                    SignupRes signupRes = response.body();
                    if (signupRes != null){
                        if (signupRes.success){
                            REQUEST_ID = signupRes.request_id;
                            setinterval();
                            Methods.showMessage(signupRes.message);
                        }else {
                            Methods.showMessage(signupRes.message);
                        }

                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<SignupRes> call, Throwable t) {
                Methods.dismissProgress();
                mobile.setEnabled(true);
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

    private void resendOtp() {
        Methods.showProgress();
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
                        }
                        Methods.showMessage(successRes.message);
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


    private void advancelogin() {
        Methods.showProgress();
        String eid = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        String fcmToken = "";
        Call<ResponseBody> call = ObjectHolder.apiInterface.postAdvanceLogin(eid,pwd,fcmToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Methods.dismissProgress();
                Methods.showMessage(response.message());
                openNextScreen(AllServicesActivity.class);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }


    private void loginViaOTP() {
        Methods.showProgress();
        String request_id = REQUEST_ID;
        String otpStr = otp.getText().toString().trim();
        Call<LoginRes> call = ObjectHolder.apiInterface.postLogin(request_id,otpStr);
        call.enqueue(new Callback<LoginRes>() {
            @Override
            public void onResponse(Call<LoginRes> call, Response<LoginRes> response) {
                Methods.dismissProgress();
                try{
                    LoginRes loginRes = response.body();
                    if (loginRes!= null)
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


    public void slidefromRightToLeft() {
        try {
            TranslateAnimation animate;
            animate = new TranslateAnimation(0,-mainRelative.getWidth(), 0, 0);
            animate.setDuration(300);
            animate.setFillAfter(true);
            loginLinear.startAnimation(animate);
            if (advLoginLinear.getVisibility()==View.GONE){
                advLoginLinear.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);

            }
            TranslateAnimation animate1;
            animate1 = new TranslateAnimation(mainRelative.getWidth(),0, 0, 0);
            animate1.setDuration(350);
            animate1.setFillAfter(true);
            advLoginLinear.startAnimation(animate1);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    loginLinear.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }



    public void slidefromLefttoRight() {
        try {
            TranslateAnimation animate;
            animate = new TranslateAnimation(0,mainRelative.getWidth(), 0, 0);
            animate.setDuration(300);
            animate.setFillAfter(true);
            advLoginLinear.startAnimation(animate);
            if (loginLinear.getVisibility()==View.GONE)
                loginLinear.setVisibility(View.VISIBLE);
            TranslateAnimation animate1;
            animate1 = new TranslateAnimation(-mainRelative.getWidth(),0, 0, 0);
            animate1.setDuration(350);
            animate1.setFillAfter(true);
            loginLinear.startAnimation(animate1);
            animate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    advLoginLinear.setVisibility(View.GONE);
                    email.setVisibility(View.GONE);
                    password.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
