package com.example.newproject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.steelkiwi.cropiwa.image.CropIwaResultReceiver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import actionsDialogs.ChangeEmail;
import actionsDialogs.ChangeMobile;
import actionsDialogs.ChangePassword;
import actionsDialogs.CreateService;
import actionsDialogs.UpdateService;
import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.OnUpdateService;
import retrofit.responseModels.AllServiceRes;
import retrofit.responseModels.ProfileRes;
import retrofit.responseModels.SuccessRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Methods;
import utils.ObjectHolder;
import views.CircleImageView;
import views.NavigationTabStrip;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

/**
 * Created by XTREMSOFT on 02-Jun-2018.
 */

public class TrackerProfile extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener, OnUpdateService,CropIwaResultReceiver.Listener {
    @BindView(R.id.profileTabs)
    NavigationTabStrip profileTabs;
    @BindView(R.id.profileMenu)
    LinearLayout profileMenu;
    @BindView(R.id.profileImage)
    CircleImageView profileImage;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.anchor)
    View anchor;

    private ProfileRes profileRes;
    private final int REQUEST_PERMISSION = 101;
    private static final int REQUEST_PICK_IMAGE = 1001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracker_profile);
        ButterKnife.bind(this);
        ObjectHolder.latestContext = this;
        profileMenu.setOnClickListener(this);
        profileImage.setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        // generateDoubleSticky();
        setTabs();
        getProfileInfo();
    }

    private void getProfileInfo() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<ProfileRes> call = ObjectHolder.apiInterface.getProfile(accountId);
        call.enqueue(new Callback<ProfileRes>() {
            @Override
            public void onResponse(Call<ProfileRes> call, Response<ProfileRes> response) {
                Methods.dismissProgress();
                try{
                    profileRes = response.body();
                    if (profileRes!= null && pager.getCurrentItem() == 0){
                        setProfileData();
                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<ProfileRes> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

    private void setTabs() {
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = null;
                LayoutInflater inflater = LayoutInflater.from(TrackerProfile.this);
                switch (position){
                    case 1:
                        RecyclerView recyclerView = new RecyclerView(TrackerProfile.this);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(mLayoutManager);
                        profileserviceAdapter = new ProfileserviceAdapter();
                        recyclerView.setAdapter(profileserviceAdapter);
                        getAllServices();
                        view = recyclerView;
                        break;

                    default:
                        view = inflater.inflate(R.layout.own_profile,null);
                        initProfileFields(view);
                        setProfileData();
                        break;
                }
                if (view != null)
                    container.addView(view);
                return view;
            }
        });
        profileTabs.setViewPager(pager, 0);
    }

    private ProfileserviceAdapter profileserviceAdapter;

    private void getAllServices() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<AllServiceRes> call = ObjectHolder.apiInterface.getAllServices(accountId);
        call.enqueue(new Callback<AllServiceRes>() {
            @Override
            public void onResponse(Call<AllServiceRes> call, Response<AllServiceRes> response) {
                Methods.dismissProgress();
                try{
                    AllServiceRes allServiceRes = response.body();
                    if (allServiceRes!= null){
                        if (allServiceRes.success){
                            profileserviceAdapter.refreshAdapter(allServiceRes.dataList);
                        }else {
                            //  Methods.showMessage(allServiceRes.message);
                        }
                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<AllServiceRes> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

    private void initProfileFields(View view) {
        pName = view.findViewById(R.id.name);
        pEmail = view.findViewById(R.id.email);
        pDobEt = view.findViewById(R.id.dobEt);
        pAddress = view.findViewById(R.id.address);
        pCity = view.findViewById(R.id.city);
        pState = view.findViewById(R.id.state);
        pCountry = view.findViewById(R.id.country);
        pZip = view.findViewById(R.id.zip);
        view.findViewById(R.id.update).setOnClickListener(this);
    }

    private void setProfileData() {
        if (profileRes != null){
            if (profileRes.data.name != null){
                pName.setText(profileRes.data.name);
            }
            if (profileRes.data.email != null){
                pEmail.setText(profileRes.data.email);
            }
            if (profileRes.data.account.dob != null){
                pDobEt.setText(profileRes.data.account.dob);
            }
            if (profileRes.data.account.address!= null){
                pAddress.setText(profileRes.data.account.address);
            }
            if (profileRes.data.account.city != null){
                pCity.setText(profileRes.data.account.city);
            }
            if (profileRes.data.account.state != null){
                pState.setText(profileRes.data.account.state);
            }
            if (profileRes.data.account.country != null){
                pCountry.setText(profileRes.data.account.country);
            }
            if (profileRes.data.account.zip != null){
                pZip.setText(profileRes.data.account.zip);
            }
        }
    }


    private EditText pName,
            pEmail,
            pDobEt,
            pAddress,
            pCity,
            pState,
            pCountry,
            pZip;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profileMenu:
                createPopupMenu(anchor);
                break;
            case R.id.update:
                updateProfile();
                break;
            case R.id.back:
                super.onBackPressed();
                break;
            case R.id.profileImage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkPermission()){
                        pickImage();
                    }else {
                        requestPermission();
                    }
                }else{
                    pickImage();
                }

                break;
            default:
                break;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA,WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length >= 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickImage();
                    } else {
                        if (shouldShowRequestPermissionRationale(CAMERA) ||
                                shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
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
        new AlertDialog.Builder(TrackerProfile.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public void pickImage() {
        startActivityForResult(getPickImageChooserIntent(), REQUEST_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == REQUEST_PICK_IMAGE) {
                Uri imageUri = getPickImageResultUri(data);
                startActivity(CropActivity.callingIntent(this, imageUri));
            }
        }
        else {
            System.out.println("Image crop failed");
            setResult(Activity.RESULT_CANCELED);
        }
    }

    private void setImage(String imagePath) {
        //pImage.setImageBitmap(getImageFromStorage(imagePath));
/*
        try {
           imagePath =  Environment.getExternalStorageDirectory().getPath()+imagePath;

            File file = new File(imagePath);
            while (file != null) {
                Log.d("FILE", file + " exists=" + file.exists());
                file = file.getParentFile();
            }
            final InputStream imageStream = getContentResolver().openInputStream(Uri.parse(file.getPath()));
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG,100,baos);
            pImage.setImageBitmap(selectedImage);
            byte[] b = baos.toByteArray();
            base64 = Base64.encodeToString(b, Base64.DEFAULT);
            uploadImageToUrl();

        }catch (Exception  e){
            e.printStackTrace();
        }
*/
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    public void onCropSuccess(Uri croppedUri) {
        profileImage.setImageURI(croppedUri);
        //setImage(croppedUri.getPath());
        Log.e("",""+croppedUri);
    }

    @Override
    public void onCropFailed(Throwable e) {

    }



    private void updateProfile() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        String name = pName.getText().toString();
        String photo = "";
        String address =  pAddress.getText().toString();
        String city =  pCity.getText().toString();
        String zip =  pZip.getText().toString();
        String country =  pCountry.getText().toString();
        String lat = "";
        String lng = "";
        String dob =  pDobEt.getText().toString();
        Call<SuccessRes> call = ObjectHolder.apiInterface.updateProfile(accountId,name,photo,address,city,zip,country,lat,lng,dob);
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes!= null){
                        if (successRes.success){
                            //todo
                        }else {

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

    private void createPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(TrackerProfile.this, v);
        popupMenu.setOnMenuItemClickListener(TrackerProfile.this);
        popupMenu.getMenu().add(1,1, 1, "Create Service");
        popupMenu.getMenu().add(1,2,2,"Change Number");
        popupMenu.getMenu().add(1,3,3,"Change Email");
        popupMenu.getMenu().add(1,4,4,"Change Password");
        popupMenu.getMenu().add(1,5,5,"Verify Aadhaar");
        popupMenu.getMenu().add(1,6,6,"Delete Profile");
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                new CreateService().createServiceDialog(TrackerProfile.this);
                return true;
            case 2:
                new ChangeMobile().changeMobileDialog();
                return true;
            case 3:
                new ChangeEmail().changeEmailDialog();
                return true;
            case 4:
                new ChangePassword().changePasswordDialog();
                return true;
            case 5:
                showMenuDialog(R.layout.verify_aadhaar);
                return true;
            case 6:
                showDeleteRequestDialog("Are you sure to delete the Profile? ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProfileRequest();
                            }
                        });
                return true;
            default:
                return true;
        }
    }







    private void deleteProfileRequest() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<SuccessRes> call = ObjectHolder.apiInterface.deleteProfile(accountId);
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes!= null){
                        if (successRes.success){

                        }else {

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

    private void showDeleteRequestDialog(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(TrackerProfile.this)
                .setMessage(message)
                .setPositiveButton("Delete", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    public void showMenuDialog(int layout){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = LayoutInflater.from(this).inflate(layout,null);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.popup_show);
            view.setAnimation(anim);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){
            Methods.printException(e);
        }
    }

    @Override
    public void onUpdateService() {
        getAllServices();
    }


    public class ProfileserviceAdapter extends RecyclerView.Adapter<ProfileserviceAdapter.MyViewHolder> {

        private List<AllServiceRes.Data> mList;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.provider_profile_service_list, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final AllServiceRes.Data data = mList.get(position);
            holder.serviceName.setText(data.name);
            holder.area.setText(data.area);
            holder.rate.setText("Rs. "+data.rate);
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new UpdateService().updateServiceDialog(data,TrackerProfile.this);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeleteRequestDialog("Are you sure to delete the Service? ",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteService(data.id);
                                }
                            });

                }
            });
        }

        @Override
        public int getItemCount() {
            if (mList == null) return 0;
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.serviceName)
            TextView serviceName;
            @BindView(R.id.area)
            TextView area;
            @BindView(R.id.rate)
            TextView rate;
            @BindView(R.id.edit)
            LinearLayout edit;
            @BindView(R.id.delete)
            LinearLayout delete;
            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public  void refreshAdapter(List<AllServiceRes.Data> mList){
            this.mList = mList;
            notifyDataSetChanged();
        }
    }

    private void deleteService(int serviceId) {
        Methods.showProgress();
        Call<SuccessRes> call = ObjectHolder.apiInterface.deleteService(serviceId);
        call.enqueue(new Callback<SuccessRes>() {
            @Override
            public void onResponse(Call<SuccessRes> call, Response<SuccessRes> response) {
                Methods.dismissProgress();
                try{
                    SuccessRes successRes = response.body();
                    if (successRes!= null){
                        if (successRes.success && pager.getCurrentItem() ==1){
                            getAllServices();
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
