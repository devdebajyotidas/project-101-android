package views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.newproject.R;


/**
 * Created by XTREMSOFT on 23-Nov-2017.
 */

public class FloatingSquareBtn extends AppCompatTextView {
    private String customAttr;

    public FloatingSquareBtn(Context context ) {
        this( context, null );
    }

    public FloatingSquareBtn(Context context, AttributeSet attrs ) {
        this( context, attrs, R.attr.imageButtonStyle );
    }

    public FloatingSquareBtn(Context context, AttributeSet attrs,
                             int defStyle ) {
        super( context, attrs, defStyle );
    }
}
