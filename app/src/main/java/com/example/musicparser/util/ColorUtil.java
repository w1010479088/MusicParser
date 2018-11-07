package com.example.musicparser.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;

/**
 * 颜色
 * Created by songlintao on 2017/10/11.
 */

public class ColorUtil {

    public static int getDynamicColor(float percent, int startColor, int endColor) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        redCurrent = (int) (redStart + percent * redDifference);
        blueCurrent = (int) (blueStart + percent * blueDifference);
        greenCurrent = (int) (greenStart + percent * greenDifference);
        alphaCurrent = (int) (alphaStart + percent * alphaDifference);

        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    public static void setBackGroundColorFilter(View v, int filterColor) {
        Drawable background = v.getBackground();
        if (background != null) {
            v.setBackground(tintDrawable(background, filterColor));
        }
    }

    public static void setImageDrawableFilter(ImageView v, int filterColor) {
        Drawable background = v.getDrawable();
        if (background != null) {
            v.setBackground(tintDrawable(background, filterColor));
        }
    }


    public static Drawable tintDrawable(Drawable drawable, int color) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }


}
