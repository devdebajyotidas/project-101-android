package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.responseModels.SignupRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Messages;
import utils.Methods;
import utils.ObjectHolder;

/**
 * Created by Sudip on 5/10/2018.
 */

public class SignupActivity  extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.mobile)
    EditText mobile;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.confirmPassword)
    EditText confirmPassword;
    @BindView(R.id.signUpBtn)
    TextView signUpBtn;
    @BindView(R.id.backtoLogin)
    TextView backtoLogin;
    @BindView(R.id.providerChk)
    CheckBox providerChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.signup_screen);
        ObjectHolder.latestContext =this;
        ButterKnife.bind(this);
        // Sequent.origin(root).anim(this, SequentAnimation.FADE_IN_UP).start();
        signUpBtn.setOnClickListener(this);;
        backtoLogin.setOnClickListener(this);;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signUpBtn:
                if (isValidate()){
                   signupMethod();
                }
                break;
            case R.id.backtoLogin:
                openLogin();
                break;
            default:
                break;
        }
    }

    private void openLogin() {
        startActivity(new Intent(SignupActivity.this,LoginActivity.class));
        overridePendingTransition(R.anim.enterleft, R.anim.exitright);
        finish();
    }

    @Override
    public void onBackPressed() {
        openLogin();
    }

    private boolean isValidate(){
        if (mobile.getText().toString().trim().length() != 10){
            Methods.showMessage(Messages.MOBILE_VALIDATION);
            return false;
        }
        if (!Methods.isEmailValid(email.getText().toString().trim())){
            Methods.showMessage(Messages.EMAIL_VALIDATION);
            return false;
        }
        if (password.getText().toString().trim().length() < 6){
            Methods.showMessage(Messages.PASSWORD_VALIDATION);
            return false;
        }
        if (!password.getText().toString().trim()
                .equals(confirmPassword.getText().toString().trim())){
            Methods.showMessage(Messages.CONFIRM_PASSWORD_VALIDATION);
            return false;
        }
        return true;
    }




    private void gotoOTP(final SignupRes signupRes){
        try {
            Intent intent = new Intent(SignupActivity.this, OTPActivity.class);
            intent.putExtra(OTPActivity.REQUEST_ID,signupRes.request_id);
            startActivity(intent);
            overridePendingTransition(R.anim.enterright, R.anim.exitleft);
            finish();
            /*final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(ObjectHolder.latestContext);
            final View bottomSheetLayout = LayoutInflater.from(ObjectHolder.latestContext)
                    .inflate(R.layout.bottomsheet_signup, null);
            ((TextView)bottomSheetLayout.findViewById(R.id.msg)).setText(signupRes.message);

            (bottomSheetLayout.findViewById(R.id.backtoLogin)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    overridePendingTransition(R.anim.enterleft, R.anim.exitright);
                    finish();
                }
            });
            (bottomSheetLayout.findViewById(R.id.nextBtn)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();
                    Intent intent = new Intent(SignupActivity.this,OTPActivity.class);
                    intent.putExtra(OTPActivity.REQUEST_ID,signupRes.request_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enterright, R.anim.exitleft);
                    finish();
                }
            });
            mBottomSheetDialog.setContentView(bottomSheetLayout);
            mBottomSheetDialog.show();*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void signupMethod() {
        Methods.showProgress();
        String mob = mobile.getText().toString().trim();
        String eid = email.getText().toString().trim();
        String pwd = password.getText().toString().trim();
        int isProvider = providerChk.isChecked()? 1:0;
        String fcmToken = "";
        Call<SignupRes> call = ObjectHolder.apiInterface.postSignUp(mob,eid,pwd,isProvider,fcmToken);
        call.enqueue(new Callback<SignupRes>() {
            @Override
            public void onResponse(Call<SignupRes> call, Response<SignupRes> response) {
                Methods.dismissProgress();
                try{
                    SignupRes signupRes = response.body();
                    Methods.getStringfromModel(signupRes);
                    if (signupRes != null){
                        if (signupRes.success){
                            gotoOTP(signupRes);
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
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }


}
