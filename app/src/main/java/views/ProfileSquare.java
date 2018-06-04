package views;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by XTREMSOFT on 07-May-2018.
 */

public class ProfileSquare extends RelativeLayout {
    public ProfileSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec-200);

    }
}