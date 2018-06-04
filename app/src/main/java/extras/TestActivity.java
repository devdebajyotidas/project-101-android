package extras;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.newproject.TrakerHome;
import com.example.newproject.R;

/**
 * Created by Sudip on 5/7/2018.
 */

public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.test_screen);
    }

    public void login(View v){
        showDialog();
       // startActivity(new Intent(TestActivity.this,LoginActivity.class));
       // finish();
    }
    public void home(View v){
        startActivity(new Intent(TestActivity.this,TrakerHome.class));
       // finish();
    }
    public void profile(View v){
        startActivity(new Intent(TestActivity.this,ProfileActivity.class));
      //  finish();
    }
    public void fullprofile(View v){
        startActivity(new Intent(TestActivity.this,FullProfileActivity.class));
      //  finish();
    }
    public void services(View v){
        startActivity(new Intent(TestActivity.this,ServicesActivity.class));
       // finish();
    }
    public void allservices(View v){
        startActivity(new Intent(TestActivity.this,AllServicesActivity.class));
       // finish();
    }
    public void signup(View v){

      //  startActivity(new Intent(TestActivity.this,SignupActivity.class));
       // finish();
    }

    public void showDialog(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final View view = LayoutInflater.from(this).inflate(R.layout.create_service_screen,null);
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.popup_show);
            view.setAnimation(anim);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
