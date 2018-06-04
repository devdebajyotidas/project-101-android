package actionsDialogs;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.newproject.R;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;

import interfaces.OnUpdateService;
import retrofit.responseModels.SuccessRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Colors;
import utils.Messages;
import utils.Methods;
import utils.ObjectHolder;

/**
 * Created by Sudip on 5/23/2018.
 */

public class CreateService {

    private OnUpdateService ous;

    public void createServiceDialog(OnUpdateService onUpdateService){
        try {
            this.ous = onUpdateService;
            AlertDialog.Builder builder = new AlertDialog.Builder(ObjectHolder.latestContext);
            builder.setCancelable(false);
            final View view = LayoutInflater.from(ObjectHolder.latestContext).inflate(R.layout.create_service_screen,null);
            Animation anim = AnimationUtils.loadAnimation(ObjectHolder.latestContext, R.anim.popup_show);
            view.setAnimation(anim);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            final Spinner serviceSpinner = view.findViewById(R.id.serviceSpinner);
            final EditText rate = view.findViewById(R.id.rate);
            final PlacesAutocompleteTextView location = (PlacesAutocompleteTextView)view.findViewById(R.id.location);


            location.setOnPlaceSelectedListener(
                    new OnPlaceSelectedListener() {
                        @Override
                        public void onPlaceSelected(final Place place) {
                            // do something awesome with the selected place
                        }
                    }
            );



            serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0){
                        ((TextView) parent.getChildAt(0)).setTextColor(Colors.Gray500);
                        ((TextView) parent.getChildAt(0)).setTextSize(14);
                    }else {
                        ((TextView) parent.getChildAt(0)).setTextColor(Colors.TextColor);
                        ((TextView) parent.getChildAt(0)).setTextSize(16);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String rateStr = rate.getText().toString();
                    String locationStr = location.getText().toString();
                    if (serviceSpinner.getSelectedItemPosition() == 0){
                        Methods.showMessage(Messages.SELECT_SERVICE);
                    }else if (TextUtils.isEmpty(rateStr)){
                        Methods.showMessage(Messages.SERVICE_RATE);
                    }else {
                        createServiceMethod(serviceSpinner.getSelectedItem().toString(),rateStr,locationStr);
                        dialog.dismiss();
                    }
                }
            });
        }catch (Exception e){
            Methods.printException(e);
        }
    }


    private void createServiceMethod(String name, String rateStr, String locationStr) {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<SuccessRes> call = ObjectHolder.apiInterface.createService(accountId,name,rateStr,locationStr,"21.33","87.00");
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes!= null){
                        if (successRes.success ){
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
