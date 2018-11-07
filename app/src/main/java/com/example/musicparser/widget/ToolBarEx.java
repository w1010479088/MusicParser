package com.example.musicparser.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicparser.R;
import com.example.musicparser.util.ColorUtil;

/**
 * ToolBar扩展
 * Created by mac on 16/8/16.
 */
public class ToolBarEx extends Toolbar {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private TextView mTvMenuTitle;
    private ImageView mIvMenu;


    private String mTitle = null;
    private String mMenuTitle = null;
    private boolean isBack = false;
    private int mSystemColor;
    private int mBgColor = 0;
    private int mFilterColor = 0;
    private Drawable mMenuDrawable = null;
    private Drawable mBackDrawable = null;

    public ToolBarEx(Context context) {
        this(context, null);
    }

    public ToolBarEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ToolBarEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.primary_toolbar, this);
        mIvBack = findViewById(R.id.iv_back);
        mIvMenu = findViewById(R.id.iv_menu);
        mTvTitle = findViewById(R.id.tv_toolbar_title);
        mTvMenuTitle = findViewById(R.id.tv_menu_title);
        parseStyle(attrs, defStyleAttr);
        mTvTitle.setText(mTitle);
        mIvBack.setVisibility(isBack ? VISIBLE : GONE);
        mTvMenuTitle.setText(mMenuTitle);
        mIvMenu.setImageDrawable(mMenuDrawable);
        mIvBack.setBackground(mBackDrawable);
        setColorFilter(mFilterColor);
        findViewById(R.id.toolbar_background).setBackground(new ColorDrawable(mBgColor));
        mIvBack.setOnClickListener((View v) -> (
                (Activity) v.getContext()).onBackPressed()
        );
    }

    private void parseStyle(AttributeSet attrs, int defStyleAttr) {
        TypedArray type = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ToolBarEx, defStyleAttr, 0);
        mTitle = type.getString(R.styleable.ToolBarEx_toolbar_title);
        isBack = type.getBoolean(R.styleable.ToolBarEx_toolbar_back, false);
        mMenuTitle = type.getString(R.styleable.ToolBarEx_toolbar_menu_title);
        mSystemColor = type.getColor(R.styleable.ToolBarEx_toolbar_background_color, ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mBgColor = type.getBoolean(R.styleable.ToolBarEx_toolbar_background_enable, true) ? mSystemColor : Color.TRANSPARENT;
        mMenuDrawable = type.getDrawable(R.styleable.ToolBarEx_toolbar_menu_drawable);
        mFilterColor = type.getColor(R.styleable.ToolBarEx_toolbar_filter_color, -1);
        mBackDrawable = type.getDrawable(R.styleable.ToolBarEx_toolbar_back_click_drawable);
        type.recycle();
    }

    @SuppressWarnings("unused")
    public void setBackGroundEnable(boolean enableBackGround) {
        findViewById(R.id.toolbar_background).setBackgroundColor(enableBackGround ? mSystemColor : Color.TRANSPARENT);
    }

    @SuppressWarnings("aLL")
    public void setBackEnable(boolean enable) {
        mIvBack.setVisibility(enable ? VISIBLE : GONE);
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setBackListener(OnClickListener listener) {
        mIvBack.setVisibility(VISIBLE);
        mIvBack.setOnClickListener(listener);
    }

    public void setMenuClickListener(OnClickListener listener) {
        mTvMenuTitle.setVisibility(VISIBLE);
        mTvMenuTitle.setOnClickListener(listener);
        mIvMenu.setOnClickListener(listener);
    }

    public void enableMenuBtn(boolean enable) {
        mTvMenuTitle.setEnabled(enable);
    }

    public void setMenuDrawable(int drawable) {
        if (0 != drawable) {
            mIvMenu.setImageResource(drawable);
        }
    }

    public void setMenuDrawableVisible(boolean visiable) {
        mIvMenu.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setColorFilter(@ColorInt int filterColor) {
        if (filterColor == -1)
            return;
        mTvTitle.setTextColor(filterColor);
        mTvMenuTitle.setTextColor(filterColor);
        ColorUtil.setImageDrawableFilter(mIvBack, filterColor);
        ColorUtil.setImageDrawableFilter(mIvMenu, filterColor);
    }
}
