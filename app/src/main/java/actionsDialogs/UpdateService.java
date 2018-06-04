package actionsDialogs;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.example.newproject.R;

import interfaces.OnUpdateService;
import retrofit.responseModels.AllServiceRes;
import retrofit.responseModels.SuccessRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Methods;
import utils.ObjectHolder;

/**
 * Created by Sudip on 5/23/2018.
 */

public class UpdateService {
    private OnUpdateService ous;

    public void updateServiceDialog(final AllServiceRes.Data data, OnUpdateService ous){
        try {
            this.ous = ous;
            AlertDialog.Builder builder = new AlertDialog.Builder(ObjectHolder.latestContext);
            builder.setCancelable(false);
            final View view = LayoutInflater.from(ObjectHolder.latestContext).inflate(R.layout.update_service,null);
            Animation anim = AnimationUtils.loadAnimation(ObjectHolder.latestContext, R.anim.popup_show);
            view.setAnimation(anim);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            final EditText rate = view.findViewById(R.id.rate);
            final EditText location = view.findViewById(R.id.location);
            String rateStr = ""+data.rate;
            rate.setText(rateStr);
            location.setText(data.area);
            rate.setSelection(rateStr.length());
            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rateStr = rate.getText().toString();
                    String locationStr = location.getText().toString(); if (TextUtils.isEmpty(rateStr)){
                        //todo
                    }else if (TextUtils.isEmpty(locationStr)){

                    }else {
                        updateServiceMethod(data.id,rateStr,locationStr);
                        dialog.dismiss();
                    }
                }
            });
        }catch (Exception e){
            Methods.printException(e);
        }
    }

    private void updateServiceMethod(int serviceId, String rateStr, String locationStr) {
        Methods.showProgress();
        Call<SuccessRes> call = ObjectHolder.apiInterface.updateService(serviceId,rateStr,locationStr);
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes!= null){
                        if (successRes.success){
                            ous.onUpdateService();
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


}
