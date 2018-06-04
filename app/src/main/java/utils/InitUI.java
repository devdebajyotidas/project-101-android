package utils;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.example.newproject.R;

import butterknife.ButterKnife;

/**
 * Created by Sudip on 4/24/2018.
 */

public class InitUI {
    public InitUI(Activity act,int resource){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            act.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            act.getWindow().setStatusBarColor(
                    ContextCompat.getColor(act, android.R.color.transparent));
        }
        act.setContentView(resource);
        ObjectHolder.latestContext = act;
        ButterKnife.bind(act);
    }
}
