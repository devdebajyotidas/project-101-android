package views;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.example.newproject.R;

import utils.ObjectHolder;

/**
 * Created by Sudip on 5/8/2018.
 */

public class Dialogs {
    private static CustomProgressDialog progressDialog;

    public static void showProgress(final String msg) {
        try {
            AppCompatActivity aca = (AppCompatActivity) ObjectHolder.latestContext;
            aca.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dismissProgress();
                    progressDialog = null;
                    progressDialog = new CustomProgressDialog(ObjectHolder.latestContext,msg);
                    progressDialog.show();
                    //  progressDialog.setCancelable(true);
                }
            });
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
    }

    public static void dismissProgress() {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog=null;
        }
    }

    /*             ProgressDialog progressDialog = new ProgressDialog(ObjectHolder.latestContext , R.style.MyAlertDialogStyle);
                    progressDialog.setMessage("Please wait ...");
                    progressDialog.show();*/
}
