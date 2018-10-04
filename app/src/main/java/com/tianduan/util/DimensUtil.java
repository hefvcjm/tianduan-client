package com.tianduan.util;

import com.tianduan.MyApplication;

public class DimensUtil {

    /**
     * dp转px
     * @param dp
     * @return
     */
    public static int dip2px(int dp)
    {
        float density = MyApplication.newInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dp*density+0.5);
    }

    /** px转换dip */
    public static int px2dip(int px) {
        final float scale = MyApplication.newInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }
    /** px转换sp */
    public static int px2sp(int pxValue) {
        final float fontScale = MyApplication.newInstance().getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /** sp转换px */
    public static int sp2px(int spValue) {
        final float fontScale = MyApplication.newInstance().getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
