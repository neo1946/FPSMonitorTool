package com.neo1946.fpsmonitor;

import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2017/11/25.
 */

public abstract class CustomClickableSpan extends ClickableSpan {

    private boolean mShowUnderline;
    private int mTextColor;
    private int mDefaultBgColor = 0x00000000;
    private int mPressedBgColor = 0x00000000;
    private boolean mIsPressed;

    public CustomClickableSpan(int textColor, int pressedBgColor, boolean showUnderline) {
        mShowUnderline = showUnderline;
        mTextColor = textColor;
        mPressedBgColor = pressedBgColor;
    }

    public void setPressed(boolean pressed) {
        mIsPressed = pressed;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(mShowUnderline);
        ds.setColor(mTextColor);
        ds.bgColor = mIsPressed ? mPressedBgColor : mDefaultBgColor;
    }

}
