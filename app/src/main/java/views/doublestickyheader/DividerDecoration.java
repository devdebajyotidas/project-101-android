package views.doublestickyheader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private int height;
    private int leftPadding;
    private int rightPadding;
    private Paint paint;

    private DividerDecoration(int height, int lPadding, int rPadding, int colour) {
        this.height = height;
        this.leftPadding = lPadding;
        this.rightPadding = rPadding;
        this.paint = new Paint();
        this.paint.setColor(colour);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = parent.getChildAt(i);
            final int top = child.getBottom();
            final int bottom = top + height;

            int left = child.getLeft() + leftPadding;
            int right = child.getRight() - rightPadding;

            c.save();
            c.drawRect(left, top, right, bottom, paint);
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, height);
    }

    public static class Builder {
        private Resources mResources;
        private int mHeight;
        private int mLPadding;
        private int mRPadding;
        private int mColour;

        public Builder(Context context) {
            mResources = context.getResources();
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1f, context.getResources().getDisplayMetrics());
            mLPadding = 0;
            mRPadding = 0;
            mColour = Color.BLACK;
        }

        public Builder setHeight(float pixels) {
            mHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixels, mResources.getDisplayMetrics());
            return this;
        }

        public Builder setHeight(@DimenRes int resource) {
            mHeight = mResources.getDimensionPixelSize(resource);
            return this;
        }

        public Builder setPadding(float pixels) {
            setLeftPadding(pixels);
            setRightPadding(pixels);
            return this;
        }

        public Builder setPadding(@DimenRes int resource) {
            setLeftPadding(resource);
            setRightPadding(resource);
            return this;
        }

        public Builder setLeftPadding(float pixelPadding) {
            mLPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixelPadding, mResources.getDisplayMetrics());
            return this;
        }

        public Builder setRightPadding(float pixelPadding) {
            mRPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, pixelPadding, mResources.getDisplayMetrics());
            return this;
        }

        public Builder setLeftPadding(@DimenRes int resource) {
            mLPadding = mResources.getDimensionPixelSize(resource);
            return this;
        }

        public Builder setRightPadding(@DimenRes int resource) {
            mRPadding = mResources.getDimensionPixelSize(resource);
            return this;
        }

        public Builder setColorResource(@ColorRes int resource) {
            setColor(mResources.getColor(resource));
            return this;
        }

        public Builder setColor(@ColorInt int color) {
            mColour = color;
            return this;
        }

        public DividerDecoration build() {
            return new DividerDecoration(mHeight, mLPadding, mRPadding, mColour);
        }
    }
}
