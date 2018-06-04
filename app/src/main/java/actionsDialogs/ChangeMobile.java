package actionsDialogs;

import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newproject.ProviderHome;
import com.example.newproject.R;

import retrofit.responseModels.LoginRes;
import retrofit.responseModels.SignupRes;
import retrofit.responseModels.SuccessRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Messages;
import utils.Methods;
import utils.ObjectHolder;

/**
 * Created by Sudip on 5/23/2018.
 */

public class ChangeMobile {

    private String MOBILE_NO = "";
    private AlertDialog dialog;

    public void changeMobileDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ObjectHolder.latestContext);
            final View view = LayoutInflater.from(ObjectHolder.latestContext).inflate(R.layout.change_mobile,null);
            Animation anim = AnimationUtils.loadAnimation(ObjectHolder.latestContext, R.anim.popup_show);
            view.setAnimation(anim);
            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            final EditText mobile = view.findViewById(R.id.mobile);
            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MOBILE_NO = mobile.getText().toString().trim();
                    if (TextUtils.isEmpty(MOBILE_NO) || MOBILE_NO.length()<10){
                        Methods.showMessage(Messages.MOBILE_VALIDATION);
                    }else {
                        reQuestForChangeMobile();
                    }
                }
            });

        }catch (Exception e){
            Methods.printException(e);
        }
    }





    private void reQuestForChangeMobile() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<SuccessRes> call = ObjectHolder.apiInterface.changeMobile(accountId,MOBILE_NO);
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                dialog.dismiss();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes!= null)
                        Methods.showMessage(successRes.message);
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<SuccessRes> call, Throwable t) {
                Methods.dismissProgress();
                dialog.dismiss();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }


}
