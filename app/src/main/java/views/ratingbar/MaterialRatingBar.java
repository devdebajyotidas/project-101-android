/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package views.ratingbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.TintTypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;

import com.example.newproject.R;


@SuppressLint("AppCompatCustomView")
public class MaterialRatingBar extends RatingBar {

    private static final String TAG = MaterialRatingBar.class.getSimpleName();

    private TintInfo mProgressTintInfo = new TintInfo();

    private MaterialRatingDrawable mDrawable;

    private OnRatingChangeListener mOnRatingChangeListener;
    private float mLastKnownRating;

    public MaterialRatingBar(Context context) {
        super(context);

        init(null, 0);
    }

    public MaterialRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs, 0);
    }

    public MaterialRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs, defStyleAttr);
    }

    @SuppressWarnings("RestrictedApi")
    private void init(AttributeSet attrs, int defStyleAttr) {
        Context context = getContext();
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.MaterialRatingBar, defStyleAttr, 0);
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_progressTint)) {
            mProgressTintInfo.mProgressTintList = a.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_progressTint);
            mProgressTintInfo.mHasProgressTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_progressTintMode)) {
            mProgressTintInfo.mProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialRatingBar_mrb_progressTintMode, -1), null);
            mProgressTintInfo.mHasProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_secondaryProgressTint)) {
            mProgressTintInfo.mSecondaryProgressTintList = a.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_secondaryProgressTint);
            mProgressTintInfo.mHasSecondaryProgressTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_secondaryProgressTintMode)) {
            mProgressTintInfo.mSecondaryProgressTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialRatingBar_mrb_secondaryProgressTintMode, -1), null);
            mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_progressBackgroundTint)) {
            mProgressTintInfo.mProgressBackgroundTintList = a.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_progressBackgroundTint);
            mProgressTintInfo.mHasProgressBackgroundTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_progressBackgroundTintMode)) {
            mProgressTintInfo.mProgressBackgroundTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialRatingBar_mrb_progressBackgroundTintMode, -1), null);
            mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_indeterminateTint)) {
            mProgressTintInfo.mIndeterminateTintList = a.getColorStateList(
                    R.styleable.MaterialRatingBar_mrb_indeterminateTint);
            mProgressTintInfo.mHasIndeterminateTintList = true;
        }
        if (a.hasValue(R.styleable.MaterialRatingBar_mrb_indeterminateTintMode)) {
            mProgressTintInfo.mIndeterminateTintMode = DrawableCompat.parseTintMode(a.getInt(
                    R.styleable.MaterialRatingBar_mrb_indeterminateTintMode, -1), null);
            mProgressTintInfo.mHasIndeterminateTintMode = true;
        }
        boolean fillBackgroundStars = a.getBoolean(
                R.styleable.MaterialRatingBar_mrb_fillBackgroundStars, isIndicator());
        a.recycle();

        mDrawable = new MaterialRatingDrawable(getContext(), fillBackgroundStars);
        mDrawable.setStarCount(getNumStars());
        setProgressDrawable(mDrawable);
    }

    @Override
    public void setNumStars(int numStars) {
        super.setNumStars(numStars);

        if (mDrawable != null) {
            mDrawable.setStarCount(numStars);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        int width = Math.round(height * mDrawable.getTileRatio() * getNumStars());
        setMeasuredDimension(View.resolveSizeAndState(width, widthMeasureSpec, 0), height);
    }

    @Override
    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);

        if (mProgressTintInfo != null) {
            applyProgressTints();
        }
    }

    @Override
    public void setIndeterminateDrawable(Drawable d) {
        super.setIndeterminateDrawable(d);

        if (mProgressTintInfo != null) {
            applyIndeterminateTint();
        }
    }

    @Nullable
    @Override
    public ColorStateList getProgressTintList() {
        logRatingBarTintWarning();
        return getSupportProgressTintList();
    }

    @Override
    public void setProgressTintList(@Nullable ColorStateList tint) {
        logRatingBarTintWarning();
        setSupportProgressTintList(tint);
    }

    @Nullable
    @Override
    public PorterDuff.Mode getProgressTintMode() {
        logRatingBarTintWarning();
        return getSupportProgressTintMode();
    }

    @Override
    public void setProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        logRatingBarTintWarning();
        setSupportProgressTintMode(tintMode);
    }

    @Nullable
    @Override
    public ColorStateList getSecondaryProgressTintList() {
        logRatingBarTintWarning();
        return getSupportSecondaryProgressTintList();
    }

    @Override
    public void setSecondaryProgressTintList(@Nullable ColorStateList tint) {
        logRatingBarTintWarning();
        setSupportSecondaryProgressTintList(tint);
    }

    @Nullable
    @Override
    public PorterDuff.Mode getSecondaryProgressTintMode() {
        logRatingBarTintWarning();
        return getSupportSecondaryProgressTintMode();
    }

    @Override
    public void setSecondaryProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        logRatingBarTintWarning();
        setSupportSecondaryProgressTintMode(tintMode);
    }

    @Nullable
    @Override
    public ColorStateList getProgressBackgroundTintList() {
        logRatingBarTintWarning();
        return getSupportProgressBackgroundTintList();
    }

    @Override
    public void setProgressBackgroundTintList(@Nullable ColorStateList tint) {
        logRatingBarTintWarning();
        setSupportProgressBackgroundTintList(tint);
    }

    @Nullable
    @Override
    public PorterDuff.Mode getProgressBackgroundTintMode() {
        logRatingBarTintWarning();
        return getSupportProgressBackgroundTintMode();
    }

    @Override
    public void setProgressBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        logRatingBarTintWarning();
        setSupportProgressBackgroundTintMode(tintMode);
    }

    @Nullable
    @Override
    public ColorStateList getIndeterminateTintList() {
        logRatingBarTintWarning();
        return getSupportIndeterminateTintList();
    }

    @Override
    public void setIndeterminateTintList(@Nullable ColorStateList tint) {
        logRatingBarTintWarning();
        setSupportIndeterminateTintList(tint);
    }

    @Nullable
    @Override
    public PorterDuff.Mode getIndeterminateTintMode() {
        logRatingBarTintWarning();
        return getSupportIndeterminateTintMode();
    }

    @Override
    public void setIndeterminateTintMode(@Nullable PorterDuff.Mode tintMode) {
        logRatingBarTintWarning();
        setSupportIndeterminateTintMode(tintMode);
    }

    private void logRatingBarTintWarning() {
        if (getContext().getApplicationInfo().minSdkVersion >= Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        Log.w(TAG, "Non-support version of tint method called, this is error-prone and will crash" +
                " below Lollipop if you are calling it as a method of RatingBar instead of" +
                " MaterialRatingBar");
    }

    @Nullable
    public ColorStateList getSupportProgressTintList() {
        return mProgressTintInfo.mProgressTintList;
    }

    public void setSupportProgressTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mProgressTintList = tint;
        mProgressTintInfo.mHasProgressTintList = true;

        applyPrimaryProgressTint();
    }

    @Nullable
    public PorterDuff.Mode getSupportProgressTintMode() {
        return mProgressTintInfo.mProgressTintMode;
    }

    public void setSupportProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mProgressTintMode = tintMode;
        mProgressTintInfo.mHasProgressTintMode = true;

        applyPrimaryProgressTint();
    }

    @Nullable
    public ColorStateList getSupportSecondaryProgressTintList() {
        return mProgressTintInfo.mSecondaryProgressTintList;
    }

    public void setSupportSecondaryProgressTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mSecondaryProgressTintList = tint;
        mProgressTintInfo.mHasSecondaryProgressTintList = true;

        applySecondaryProgressTint();
    }

    @Nullable
    public PorterDuff.Mode getSupportSecondaryProgressTintMode() {
        return mProgressTintInfo.mSecondaryProgressTintMode;
    }

    public void setSupportSecondaryProgressTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mSecondaryProgressTintMode = tintMode;
        mProgressTintInfo.mHasSecondaryProgressTintMode = true;

        applySecondaryProgressTint();
    }

    @Nullable
    public ColorStateList getSupportProgressBackgroundTintList() {
        return mProgressTintInfo.mProgressBackgroundTintList;
    }

    public void setSupportProgressBackgroundTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mProgressBackgroundTintList = tint;
        mProgressTintInfo.mHasProgressBackgroundTintList = true;

        applyProgressBackgroundTint();
    }

    @Nullable
    public PorterDuff.Mode getSupportProgressBackgroundTintMode() {
        return mProgressTintInfo.mProgressBackgroundTintMode;
    }

    public void setSupportProgressBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mProgressBackgroundTintMode = tintMode;
        mProgressTintInfo.mHasProgressBackgroundTintMode = true;

        applyProgressBackgroundTint();
    }

    @Nullable
    public ColorStateList getSupportIndeterminateTintList() {
        return mProgressTintInfo.mIndeterminateTintList;
    }

    public void setSupportIndeterminateTintList(@Nullable ColorStateList tint) {
        mProgressTintInfo.mIndeterminateTintList = tint;
        mProgressTintInfo.mHasIndeterminateTintList = true;

        applyIndeterminateTint();
    }

    @Nullable
    public PorterDuff.Mode getSupportIndeterminateTintMode() {
        return mProgressTintInfo.mIndeterminateTintMode;
    }

    public void setSupportIndeterminateTintMode(@Nullable PorterDuff.Mode tintMode) {
        mProgressTintInfo.mIndeterminateTintMode = tintMode;
        mProgressTintInfo.mHasIndeterminateTintMode = true;

        applyIndeterminateTint();
    }

    private void applyProgressTints() {
        if (getProgressDrawable() == null) {
            return;
        }
        applyPrimaryProgressTint();
        applyProgressBackgroundTint();
        applySecondaryProgressTint();
    }

    private void applyPrimaryProgressTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasProgressTintList || mProgressTintInfo.mHasProgressTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.progress, true);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mProgressTintList,
                        mProgressTintInfo.mHasProgressTintList, mProgressTintInfo.mProgressTintMode,
                        mProgressTintInfo.mHasProgressTintMode);
            }
        }
    }

    private void applySecondaryProgressTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasSecondaryProgressTintList
                || mProgressTintInfo.mHasSecondaryProgressTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.secondaryProgress,
                    false);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mSecondaryProgressTintList,
                        mProgressTintInfo.mHasSecondaryProgressTintList,
                        mProgressTintInfo.mSecondaryProgressTintMode,
                        mProgressTintInfo.mHasSecondaryProgressTintMode);
            }
        }
    }

    private void applyProgressBackgroundTint() {
        if (getProgressDrawable() == null) {
            return;
        }
        if (mProgressTintInfo.mHasProgressBackgroundTintList
                || mProgressTintInfo.mHasProgressBackgroundTintMode) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.background, false);
            if (target != null) {
                applyTintForDrawable(target, mProgressTintInfo.mProgressBackgroundTintList,
                        mProgressTintInfo.mHasProgressBackgroundTintList,
                        mProgressTintInfo.mProgressBackgroundTintMode,
                        mProgressTintInfo.mHasProgressBackgroundTintMode);
            }
        }
    }

    private Drawable getTintTargetFromProgressDrawable(int layerId, boolean shouldFallback) {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable == null) {
            return null;
        }
        progressDrawable.mutate();
        Drawable layerDrawable = null;
        if (progressDrawable instanceof LayerDrawable) {
            layerDrawable = ((LayerDrawable) progressDrawable).findDrawableByLayerId(layerId);
        }
        if (layerDrawable == null && shouldFallback) {
            layerDrawable = progressDrawable;
        }
        return layerDrawable;
    }

    private void applyIndeterminateTint() {
        Drawable indeterminateDrawable = getIndeterminateDrawable();
        if (indeterminateDrawable == null) {
            return;
        }
        if (mProgressTintInfo.mHasIndeterminateTintList
                || mProgressTintInfo.mHasIndeterminateTintMode) {
            indeterminateDrawable.mutate();
            applyTintForDrawable(indeterminateDrawable, mProgressTintInfo.mIndeterminateTintList,
                    mProgressTintInfo.mHasIndeterminateTintList,
                    mProgressTintInfo.mIndeterminateTintMode,
                    mProgressTintInfo.mHasIndeterminateTintMode);
        }
    }

    @SuppressLint("NewApi")
    private void applyTintForDrawable(Drawable drawable, ColorStateList tintList,
                                      boolean hasTintList, PorterDuff.Mode tintMode,
                                      boolean hasTintMode) {

        if (hasTintList || hasTintMode) {

            if (hasTintList) {
                if (drawable instanceof TintableDrawable) {
                    ((TintableDrawable) drawable).setTintList(tintList);
                } else {
                    logDrawableTintWarning();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintList(tintList);
                    }
                }
            }

            if (hasTintMode) {
                if (drawable instanceof TintableDrawable) {
                    ((TintableDrawable) drawable).setTintMode(tintMode);
                } else {
                    logDrawableTintWarning();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        drawable.setTintMode(tintMode);
                    }
                }
            }

            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }
    }

    private void logDrawableTintWarning() {
        Log.w(TAG, "Drawable did not implement TintableDrawable, it won't be tinted below" +
                " Lollipop");
    }

    public OnRatingChangeListener getOnRatingChangeListener() {
        return mOnRatingChangeListener;
    }

    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        mOnRatingChangeListener = listener;
    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress);

        float rating = getRating();
        if (mOnRatingChangeListener != null && rating != mLastKnownRating) {
            mOnRatingChangeListener.onRatingChanged(this, rating);
        }
        mLastKnownRating = rating;
    }

    public interface OnRatingChangeListener {

        void onRatingChanged(MaterialRatingBar ratingBar, float rating);
    }

    private static class TintInfo {

        public ColorStateList mProgressTintList;
        public PorterDuff.Mode mProgressTintMode;
        public boolean mHasProgressTintList;
        public boolean mHasProgressTintMode;

        public ColorStateList mSecondaryProgressTintList;
        public PorterDuff.Mode mSecondaryProgressTintMode;
        public boolean mHasSecondaryProgressTintList;
        public boolean mHasSecondaryProgressTintMode;

        public ColorStateList mProgressBackgroundTintList;
        public PorterDuff.Mode mProgressBackgroundTintMode;
        public boolean mHasProgressBackgroundTintList;
        public boolean mHasProgressBackgroundTintMode;

        public ColorStateList mIndeterminateTintList;
        public PorterDuff.Mode mIndeterminateTintMode;
        public boolean mHasIndeterminateTintList;
        public boolean mHasIndeterminateTintMode;
    }
}
