package com.example.newproject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapters.HomeSpinnerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.ListItemAddProg;
import retrofit.responseModels.ServiceDetailsRes;
import retrofit.responseModels.ServiceModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.LocationTracker;
import utils.Methods;
import utils.ObjectHolder;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by Sudip on 5/28/2018.
 */

public class TrakerHome extends AppCompatActivity implements OnMapReadyCallback,
        View.OnClickListener ,GoogleMap.OnCameraIdleListener,GoogleMap.OnMarkerClickListener,
        NavigationView.OnNavigationItemSelectedListener{
      private GoogleMap mMap;
      private LocationTracker locationTrackObj;
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    private final int REQUEST_PERMISSION = 101;
    public static final int SEARCH_RESULT = 102;
    public static final String SEARCH_LIST = "search_list";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUTE = "longitude";
    private Location location;
    private boolean notgetServices = true;

    @BindView(R.id.markarInfo)
    CardView markarInfo;
    @BindView(R.id.infoName)
    TextView infoName;
    /*@BindView(R.id.nav_view)
    NavigationView nav_view;*/
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(this, android.R.color.transparent));
        }
        setContentView(R.layout.traker_home);
        ObjectHolder.latestContext = this;
        ButterKnife.bind(this);
        initNavMenus();
        initialization();
    }

    private void initNavMenus() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
       /* View navHeader = nav_view.getHeaderView(0);
        navHeader.findViewById(R.id.navBack).setOnClickListener(this);
        navHeader.findViewById(R.id.home).setOnClickListener(this);
        navHeader.findViewById(R.id.profile).setOnClickListener(this);
        navHeader.findViewById(R.id.connects).setOnClickListener(this);
        navHeader.findViewById(R.id.providerServices).setOnClickListener(this);
        navHeader.findViewById(R.id.help).setOnClickListener(this);
        navHeader.findViewById(R.id.contactUs).setOnClickListener(this);*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.profile) {
            startActivity(new Intent(TrakerHome.this,TrackerProfile.class));
            overridePendingTransition(R.anim.enterright, R.anim.exitleft);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void initialization() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationTrackObj = new LocationTracker();
        if (!locationTrackObj.canGetLocation()) {
            locationTrackObj.showSettingsAlert(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        ObjectHolder.latestContext = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }




    private boolean checkLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},
                REQUEST_PERMISSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length >= 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        initializeMap(mMap);
                    } else {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel("You need to allow access the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermission();
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(TrakerHome.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()){
                initializeMap(mMap);
            }else {
                requestPermission();
            }
        }else{
            initializeMap(mMap);
        }
    }

    @SuppressLint("MissingPermission")
    private void initializeMap(GoogleMap mMap) {
        if (mMap != null) {
            mMap.getUiSettings().setScrollGesturesEnabled(true);
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.setMyLocationEnabled(true);
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setOnCameraIdleListener(this);
            mMap.setOnMarkerClickListener(this);
            Location myLocation = mMap.getMyLocation();
            if (myLocation == null)
                myLocation = locationTrackObj.getLocation();
            if (myLocation != null){
                this.location = myLocation;
                showLocation();
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (markarInfo.getVisibility() == View.VISIBLE)
                    markarInfo.setVisibility(View.GONE);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.navBack:
                drawerLayout.closeDrawers();
                break;
            case R.id.home:
                drawerLayout.closeDrawers();
                break;
            case R.id.profile:
                drawerLayout.closeDrawers();
                startActivity(new Intent(TrakerHome.this,TrackerProfile.class));
                overridePendingTransition(R.anim.enterright, R.anim.exitleft);
                break;
            case R.id.connects:
                drawerLayout.closeDrawers();
                break;
            case R.id.providerServices:
                drawerLayout.closeDrawers();
                break;
            case R.id.help:
                drawerLayout.closeDrawers();
                break;
            case R.id.contactUs:
                drawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tracker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(TrakerHome.this,SearchActivity.class);
            if (location!=null){
                intent.putExtra(LATITUDE,location.getLatitude());
                intent.putExtra(LONGITUTE,location.getLongitude());
            }
            startActivityForResult(intent,SEARCH_RESULT);
            overridePendingTransition(R.anim.enterright, R.anim.exitleft);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS){
           if (resultCode == -1){
               Methods.showProgress();
               starDetectLocationTimer();
           }else {
               Toast.makeText(this, "Cancel Clicked", Toast.LENGTH_SHORT).show();
           }
        }
        if (requestCode == SEARCH_RESULT) {
            if (data!=null){
                String searchRes = data.getStringExtra(SEARCH_LIST);
                Gson gson = new Gson();
                ServiceModel.Data[] favoriteItems = gson.fromJson(searchRes, ServiceModel.Data[].class);
                List<ServiceModel.Data> mList = Arrays.asList(favoriteItems);
                double lat = data.getDoubleExtra(LATITUDE,location.getLatitude());
                double lng = data.getDoubleExtra(LONGITUTE,location.getLongitude());
                mMap.setOnCameraIdleListener(null);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                        LatLng(lat,lng), 14));
                addMarkers(mList);
            }
        }
    }

    private CountDownTimer countDownTimer;

    private void starDetectLocationTimer() {
        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Location myLocation = mMap.getMyLocation();
                if (myLocation != null){
                    location = myLocation;
                    Methods.dismissProgress();
                    cancelTimer();
                    showLocation();
                }
            }

            @Override
            public void onFinish() {

            }
        };
        countDownTimer.start();
    }

    private void cancelTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private double lat = 22.6059440;
    private double lng = 88.3967540;

    private void showLocation(){
        if (location != null){
            try {
              /*  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                        LatLng(lat,lng), 14));*/
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                        LatLng(location.getLatitude(),
                        location.getLongitude()), 14));
            } catch (Exception e) {
                Methods.printException(e);
            }
        }
    }


    @Override
    public void onCameraIdle() {
        if(location != null && notgetServices){
            notgetServices = false;
            getAllServices();
        }
    }

    private void getAllServices() {
        Methods.showProgress();
       /* Call<ServiceModel> call = ObjectHolder.apiInterface.getAllServicesByLocation
                (lat,lng,100);*/
        Call<ServiceModel> call = ObjectHolder.apiInterface.getAllServicesByLocation
                (location.getLatitude(),location.getLongitude(),100);
        call.enqueue(new Callback<ServiceModel>() {
            @Override
            public void onResponse(Call<ServiceModel> call, Response<ServiceModel> response) {
                Methods.dismissProgress();
                try{
                    ServiceModel allServiceRes = response.body();
                    if (allServiceRes!= null){
                        if (allServiceRes.success){
                            if (allServiceRes.dataList.size()>0){
                                addMarkers(allServiceRes.dataList);
                            }else {
                                //Methods.showMessage(Messages.SERVICE_NOT_FOUND);
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



    private void addMarkers(List<ServiceModel.Data> dataList) {
        mMap.clear();
        for (ServiceModel.Data data: dataList){
            addServicesMarker(data);
        }
    }


    private void addServicesMarker(ServiceModel.Data data) {
        try {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createAvtarBitmap()));
            LatLng markerLatLng = new LatLng(data.latitude,data.longitude);
            markerOptions.position(markerLatLng);
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(data);
        }catch (Exception e){
            Methods.printException(e);
        }
    }


    private Bitmap createAvtarBitmap() {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(dp(40), dp(40), Bitmap.Config.ARGB_8888);
            result.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(result);
            Drawable drawable = getResources().getDrawable(R.mipmap.marker_new);
            drawable.setBounds(0, 0, dp(40), dp(40));
            drawable.draw(canvas);
            Paint roundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            RectF bitmapRect = new RectF();
            canvas.save();
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.man);
            if (bitmap != null) {
                BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                Matrix matrix = new Matrix();
                float scale = dp(38) / (float) bitmap.getWidth();
                matrix.postTranslate(dp(2), dp(2));
                matrix.postScale(scale, scale);
                roundPaint.setShader(shader);
                shader.setLocalMatrix(matrix);
                bitmapRect.set(dp(8), dp(3), dp(24 + 8), dp(26 + 3));
                canvas.drawRoundRect(bitmapRect, dp(26), dp(26), roundPaint);
            }
            canvas.restore();
            try {
                canvas.setBitmap(null);
            } catch (Exception e) {}
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getResources().getDisplayMetrics().density * value);
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        ServiceModel.Data data = (ServiceModel.Data) marker.getTag();
        if (data != null){
            if (markarInfo.getVisibility() == View.GONE)
                markarInfo.setVisibility(View.VISIBLE);
            infoName.setText(data.name);
            getServiceDetails(data);
        }
        return false;
    }

    private void getServiceDetails(ServiceModel.Data data) {
       // Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<ServiceDetailsRes> call = ObjectHolder.apiInterface.getServiceDetails(2,data.id);
        call.enqueue(new Callback<ServiceDetailsRes>() {
            @Override
            public void onResponse(Call<ServiceDetailsRes> call, Response<ServiceDetailsRes> response) {
              //  Methods.dismissProgress();
                try{
                    ServiceDetailsRes sdr = response.body();
                    if (sdr!= null){
                        if (sdr.success){
                        }else {
                            Methods.showMessage(sdr.message);
                        }
                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<ServiceDetailsRes> call, Throwable t) {
               // Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

}
