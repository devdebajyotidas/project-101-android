package com.example.newproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import retrofit.ApiClient;
import retrofit.ApiInterface;
import utils.InitUI;
import utils.ObjectHolder;
import views.CircularReveal;

/**
 * Created by Sudip on 5/17/2018.
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.splashImage)
    ImageView splashImage;
    @BindView(R.id.splashText)
    TextView splashText;
    @BindView(R.id.splashTextone)
    TextView splashTextone;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new InitUI(this,R.layout.splash_screen);
        startSplashAnimation();
    }


    @Override
    protected void onResume() {
        super.onResume();
        ObjectHolder.latestContext = this;
    }


    private void startSplashAnimation() {
        Animation animZoomOut = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        splashImage.setAnimation(animZoomOut);
        animZoomOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation  animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.together);
                splashText.setVisibility(View.VISIBLE);
                splashText.setAnimation(animSlideDown);
                Animation  animSlideDown1 = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.together_one);
                splashTextone.setVisibility(View.VISIBLE);
                splashTextone.setAnimation(animSlideDown1);
                startThread();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    private void startThread() {
        new Thread() {
            public void run() {
                try {
                    sleep(1500);
                    startCirculerReveal();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void startCirculerReveal() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View reveal = findViewById(R.id.circularReveal);
                int centerY = reveal.getHeight() / 2;
                int centerX = reveal.getWidth() / 2;

                CircularReveal circularReveal = new CircularReveal(reveal, centerX, centerY);
                circularReveal.setExpandDur(700);
                circularReveal.setBackgroundColor(R.color.white);

                circularReveal.expand();
                circularReveal.setCircularRevealListener(new CircularReveal.CircularRevealListener() {
                    @Override
                    public void onAnimationEnd(int animState) {
                        ObjectHolder.apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        if (ObjectHolder.loginRes != null){
                            if (ObjectHolder.loginRes.data.is_provider == 0){
                                openNextScreen(TrakerHome.class);
                            }else {
                                openNextScreen(ProviderHome.class);
                            }
                          //  openNextScreen(TrakerHome.class);
                        }else {
                            openNextScreen(LoginActivity.class);
                        }
                    }
                });
            }
        });
    }

    private void openNextScreen(Class<?> cls){
        startActivity(new Intent(SplashActivity.this,cls));
        finish();
    }


}
