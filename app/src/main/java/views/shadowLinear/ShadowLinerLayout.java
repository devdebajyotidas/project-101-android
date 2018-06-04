package views.shadowLinear;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.example.newproject.R;

/**
 * Created by XTREMSOFT on 25-Apr-2018.
 */

public class ShadowLinerLayout extends LinearLayout {
    public ShadowLinerLayout(Context context) {
        super(context);
        initBackground();
    }

    public ShadowLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initBackground();
    }

    public ShadowLinerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBackground();
    }

    private void initBackground() {
        setBackground(ViewUtils.generateBackgroundWithShadow(this,android.R.color.white,
                R.dimen.radius_corner,R.color.shadow,R.dimen.elevation, Gravity.BOTTOM));
    }
}