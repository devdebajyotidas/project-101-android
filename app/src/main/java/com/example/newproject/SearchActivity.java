package com.example.newproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.responseModels.GoogleplaceRes;
import retrofit.responseModels.ServiceModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Colors;
import utils.Messages;
import utils.Methods;
import utils.ObjectHolder;

import static com.example.newproject.TrakerHome.LATITUDE;
import static com.example.newproject.TrakerHome.LONGITUTE;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    @BindView(R.id.applyBtn)
    TextView applyBtn;
    @BindView(R.id.serviceSpinner)
    Spinner serviceSpinner;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.locName)
    TextView locName;
    @BindView(R.id.locLinear)
    LinearLayout locLinear;

    private double latitude;
    private double longitude;
    private static final int AUTOCOMPLETE_RESULT = 106;


    private PlaceAutocompleteFragment autocompleteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ObjectHolder.latestContext = this;
        ButterKnife.bind(this);
        applyBtn.setOnClickListener(this);
        back.setOnClickListener(this);
        locLinear.setOnClickListener(this);
        serviceSpinner.setOnItemSelectedListener(this);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        Intent intent = getIntent();
        if (intent!=null){
            latitude = intent.getDoubleExtra(LATITUDE,0.00);
            longitude = intent.getDoubleExtra(LONGITUTE,0.00);
            setLocation();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_RESULT) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                locName.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    private void setLocation() {
        if (latitude>0){
            String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + ","+ longitude + "&sensor=true";
            Call<GoogleplaceRes> call = ObjectHolder.apiInterface.getPlaceName(url);
            call.enqueue(new Callback<GoogleplaceRes>() {
                @Override
                public void onResponse(Call<GoogleplaceRes> call, Response<GoogleplaceRes> response) {
                    Methods.dismissProgress();
                    try{
                        GoogleplaceRes gpr = response.body();
                        if (gpr!= null && gpr.status.equalsIgnoreCase("ok")){
                           try {
                               locName.setText(gpr.results.get(0).formatted_address);
                           }catch (Exception e){
                               Methods.printException(e);
                           }
                        }
                    }catch (Exception e){
                        Methods.printException(e);
                    }
                }
                @Override
                public void onFailure(Call<GoogleplaceRes> call, Throwable t) {
                    Methods.dismissProgress();
                    Methods.showMessage(t.getMessage());
                    call.cancel();
                }
            });
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.applyBtn:
                getAllServicesFromSearch();
                break;
            case R.id.back:
                finish();
               // setResustAndFinish(new ArrayList<ServiceModel.Data>());
                break;
            case R.id.locLinear:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this);
                    startActivityForResult(intent,AUTOCOMPLETE_RESULT);
                    overridePendingTransition(R.anim.enterright, R.anim.exitleft);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }                break;
                default:
                    break;
        }
    }

    private void getAllServicesFromSearch() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<ServiceModel> call = ObjectHolder.apiInterface.getAllServicesBySearch
                (accountId,"Electrician"/*,location.getLatitude(),location.getLongitude(),100*/);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                Methods.dismissProgress();
                try{
                    ServiceModel allServiceRes = response.body();
                    if (allServiceRes!= null){
                        if (allServiceRes.success){
                            if (allServiceRes.dataList.size()>0){
                               // addMarkers(allServiceRes.dataList);
                                setResustAndFinish(allServiceRes.dataList);
                            }else {
                                Methods.showMessage(Messages.SERVICE_NOT_FOUND);
                            }
                        }else {
                            Methods.showMessage(allServiceRes.message);
                        }
                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<ServiceModel> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

    private void setResustAndFinish(List<ServiceModel.Data> dataList) {
        Intent intent = new Intent();
        Gson gson = new Gson();
        String json = gson.toJson(dataList);
        intent.putExtra (TrakerHome.SEARCH_LIST,json);
        intent.putExtra(LATITUDE,latitude);
        intent.putExtra(LONGITUTE,longitude);
        setResult(TrakerHome.SEARCH_RESULT,intent);
        //overridePendingTransition(R.anim.enterright, R.anim.exitleft);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
       // setResustAndFinish(new ArrayList<ServiceModel.Data>());
    }

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

}
