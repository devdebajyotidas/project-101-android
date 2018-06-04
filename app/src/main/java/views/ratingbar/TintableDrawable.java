package views.ratingbar;

import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface TintableDrawable {

    void setTint(@ColorInt int tintColor);

    void setTintList(@Nullable ColorStateList tint);

    void setTintMode(@NonNull PorterDuff.Mode tintMode);
}
