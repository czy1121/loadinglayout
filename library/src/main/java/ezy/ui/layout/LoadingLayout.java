/*
 * Copyright 2016 czy1121
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ezy.ui.layout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ezy.library.loadinglayout.R;


public class LoadingLayout extends FrameLayout {
    public interface OnInflateListener {
        void onInflate(View inflated);
    }

    public static LoadingLayout wrap(Activity activity) {
        return wrap(((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0));
    }
    public static LoadingLayout wrap(Fragment fragment) {
        return wrap(fragment.getView());
    }
    public static LoadingLayout wrap(View view) {
        if (view == null) {
            throw new RuntimeException("content view can not be null");
        }
        ViewGroup parent = (ViewGroup)view.getParent();
        if (view == null) {
            throw new RuntimeException("parent view can not be null");
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        int index = parent.indexOfChild(view);
        parent.removeView(view);

        LoadingLayout layout = new LoadingLayout(view.getContext());
        parent.addView(layout, index, lp);
        layout.addView(view);
        layout.setContentView(view);
        return layout;
    }

    int mEmptyImage;
    CharSequence mEmptyText;

    int mErrorImage;
    CharSequence mErrorText, mRetryText;
    View.OnClickListener mRetryButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mRetryListener != null) {
                mRetryListener.onClick(v);
            }
        }
    };
    View.OnClickListener mRetryListener;

    OnInflateListener mOnEmptyInflateListener;
    OnInflateListener mOnErrorInflateListener;

    int mTextColor, mTextSize;
    int mButtonTextColor, mButtonTextSize;
    Drawable mButtonBackground;
    int mEmptyResId = NO_ID, mLoadingResId = NO_ID, mErrorResId = NO_ID;
    int mContentId = NO_ID;

    Map<Integer, View> mLayouts = new HashMap<>();


    public LoadingLayout(Context context) {
        this(context, null, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.styleLoadingLayout);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout, defStyleAttr, R.style.LoadingLayout_Style);
        mEmptyImage = a.getResourceId(R.styleable.LoadingLayout_llEmptyImage, NO_ID);
        mEmptyText = a.getString(R.styleable.LoadingLayout_llEmptyText);

        mErrorImage = a.getResourceId(R.styleable.LoadingLayout_llErrorImage, NO_ID);
        mErrorText = a.getString(R.styleable.LoadingLayout_llErrorText);
        mRetryText = a.getString(R.styleable.LoadingLayout_llRetryText);

        mTextColor = a.getColor(R.styleable.LoadingLayout_llTextColor, 0xff999999);
        mTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llTextSize, dp2px(16));

        mButtonTextColor = a.getColor(R.styleable.LoadingLayout_llButtonTextColor, 0xff999999);
        mButtonTextSize = a.getDimensionPixelSize(R.styleable.LoadingLayout_llButtonTextSize, dp2px(16));
        mButtonBackground = a.getDrawable(R.styleable.LoadingLayout_llButtonBackground);

        mEmptyResId = a.getResourceId(R.styleable.LoadingLayout_llEmptyResId, R.layout._loading_layout_empty);
        mLoadingResId = a.getResourceId(R.styleable.LoadingLayout_llLoadingResId, R.layout._loading_layout_loading);
        mErrorResId = a.getResourceId(R.styleable.LoadingLayout_llErrorResId, R.layout._loading_layout_error);
        a.recycle();
    }

    int dp2px(float dp) {
        return (int) (getResources().getDisplayMetrics().density * dp);
    }


    LayoutInflater mInflater;
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
        showLoading();
    }

    private void setContentView(View view) {
        mContentId = view.getId();
        mLayouts.put(mContentId, view);
    }

    public LoadingLayout setLoading(@LayoutRes int id) {
        if (mLoadingResId != id) {
            remove(mLoadingResId);
            mLoadingResId = id;
        }
        return this;
    }
    public LoadingLayout setEmpty(@LayoutRes int id) {
        if (mEmptyResId != id) {
            remove(mEmptyResId);
            mEmptyResId = id;
        }
        return this;
    }
    public LoadingLayout setOnEmptyInflateListener(OnInflateListener listener) {
        mOnEmptyInflateListener = listener;
        if (mOnEmptyInflateListener != null && mLayouts.containsKey(mEmptyResId)) {
            listener.onInflate(mLayouts.get(mEmptyResId));
        }
        return this;
    }
    public LoadingLayout setOnErrorInflateListener(OnInflateListener listener) {
        mOnErrorInflateListener = listener;
        if (mOnErrorInflateListener != null && mLayouts.containsKey(mErrorResId)) {
            listener.onInflate(mLayouts.get(mErrorResId));
        }
        return this;
    }

    public LoadingLayout setEmptyImage(@DrawableRes int resId) {
        mEmptyImage = resId;
        image(mEmptyResId, R.id.empty_image, mEmptyImage);
        return this;
    }
    public LoadingLayout setEmptyText(String value) {
        mEmptyText = value;
        text(mEmptyResId, R.id.empty_text, mEmptyText);
        return this;
    }
    public LoadingLayout setErrorImage(@DrawableRes int resId) {
        mErrorImage = resId;
        image(mErrorResId, R.id.error_image, mErrorImage);
        return this;
    }
    public LoadingLayout setErrorText(String value) {
        mErrorText = value;
        text(mErrorResId, R.id.error_text, mErrorText);
        return this;
    }

    public LoadingLayout setRetryText(String text) {
        mRetryText = text;
        text(mErrorResId, R.id.retry_button, mRetryText);
        return this;
    }

    public LoadingLayout setRetryListener(OnClickListener listener) {
        mRetryListener = listener;
        return this;
    }


//    public LoadingLayout setTextColor(@ColorInt int color) {
//        mTextColor = color;
//        return this;
//    }
//    public LoadingLayout setTextSize(@ColorInt int dp) {
//        mTextColor = dp2px(dp);
//        return this;
//    }
//    public LoadingLayout setButtonTextColor(@ColorInt int color) {
//        mButtonTextColor = color;
//        return this;
//    }
//    public LoadingLayout setButtonTextSize(@ColorInt int dp) {
//        mButtonTextColor = dp2px(dp);
//        return this;
//    }
//    public LoadingLayout setButtonBackground(Drawable drawable) {
//        mButtonBackground = drawable;
//        return this;
//    }

    public void showLoading() {
        show(mLoadingResId);
    }

    public void showEmpty() {
        show(mEmptyResId);
    }

    public void showError() {
        show(mErrorResId);
    }

    public void showContent() {
        show(mContentId);
    }

    private void show(int layoutId) {
        for (View view : mLayouts.values()) {
            view.setVisibility(GONE);
        }
        layout(layoutId).setVisibility(VISIBLE);
    }

    private void remove(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            View vg = mLayouts.remove(layoutId);
            removeView(vg);
        }
    }

    private View layout(int layoutId) {
        if (mLayouts.containsKey(layoutId)) {
            return mLayouts.get(layoutId);
        }
        View layout = mInflater.inflate(layoutId, this, false);
        layout.setVisibility(GONE);
        addView(layout);
        mLayouts.put(layoutId, layout);

        if (layoutId == mEmptyResId) {
            ImageView img = (ImageView) layout.findViewById(R.id.empty_image);
            if (img != null) {
                img.setImageResource(mEmptyImage);
            }
            TextView view = (TextView) layout.findViewById(R.id.empty_text);
            if (view != null) {
                view.setText(mEmptyText);
                view.setTextColor(mTextColor);
                view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            if (mOnEmptyInflateListener != null) {
                mOnEmptyInflateListener.onInflate(layout);
            }
        } else if (layoutId == mErrorResId) {
            ImageView img = (ImageView) layout.findViewById(R.id.error_image);
            if (img != null) {
                img.setImageResource(mErrorImage);
            }
            TextView txt = (TextView) layout.findViewById(R.id.error_text);
            if (txt != null) {
                txt.setText(mErrorText);
                txt.setTextColor(mTextColor);
                txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            }
            TextView btn = (TextView) layout.findViewById(R.id.retry_button);
            if (btn != null) {
                btn.setText(mRetryText);
                btn.setTextColor(mButtonTextColor);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mButtonTextSize);
                btn.setBackground(mButtonBackground);
                btn.setOnClickListener(mRetryButtonClickListener);
            }
            if (mOnErrorInflateListener != null) {
                mOnErrorInflateListener.onInflate(layout);
            }
        }
        return layout;
    }

    private void text(int layoutId, int ctrlId, CharSequence value) {
        if (mLayouts.containsKey(layoutId)) {
            TextView view = (TextView) mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setText(value);
            }
        }
    }

    private void image(int layoutId, int ctrlId, int resId) {
        if (mLayouts.containsKey(layoutId)) {
            ImageView view = (ImageView) mLayouts.get(layoutId).findViewById(ctrlId);
            if (view != null) {
                view.setImageResource(resId);
            }
        }
    }
}
