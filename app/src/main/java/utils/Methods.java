package utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newproject.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Methods {
    public static void printException(Exception e){
        if (e!=null)
        e.printStackTrace();
    }

    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void showMessage(final String msg){
        final AppCompatActivity aca = (AppCompatActivity) ObjectHolder.latestContext;
        aca.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDialog(msg);
                //Toast.makeText(aca, ""+msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getStringfromModel(Object obj){
        try {
            Gson gson = new Gson();
            String json = gson.toJson(obj);
            Log.e("JSON_OBJ",": "+json);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static ProgressDialog progressDialog;

    public static void showProgress(){
        final AppCompatActivity aca = (AppCompatActivity) ObjectHolder.latestContext;
        aca.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgress();
                progressDialog = new ProgressDialog(ObjectHolder.latestContext);
                progressDialog.setMessage("Please wait ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
    }

    public static void dismissProgress(){
        if (progressDialog != null){
            progressDialog.cancel();
            progressDialog = null;
        }
    }


    public static void showDialog(String msg){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ObjectHolder.latestContext);
            final View view = LayoutInflater.from(ObjectHolder.latestContext).inflate(R.layout.dialog_message,null);
            Animation anim = AnimationUtils.loadAnimation(ObjectHolder.latestContext, R.anim.popup_show);
            view.setAnimation(anim);
            builder.setView(view);

            final TextView msgTv = view.findViewById(R.id.msg);
            final TextView ok = view.findViewById(R.id.ok);
            msgTv.setText(msg);

            final AlertDialog dialog = builder.create();

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow().setLayout(600, 400);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void hidesoftkey(View view){
        try {
            InputMethodManager imm =
                    (InputMethodManager) ObjectHolder.latestContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void slidefromRightToLeft(View view,View main) {
        TranslateAnimation animate;
        if (view.getHeight() == 0) {
            main.getHeight(); // parent layout
            animate = new TranslateAnimation(main.getWidth(),
                    0, 0, 0);
        } else {
            animate = new TranslateAnimation(view.getWidth(),0, 0, 0); // View for animation
        }
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE); // Change visibility VISIBLE or GONE
    }

}
