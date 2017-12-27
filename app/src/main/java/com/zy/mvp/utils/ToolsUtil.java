package com.zy.mvp.utils;

import android.content.Context;

public class ToolsUtil {
    public static int getHeightInPx(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWidthInPx(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getHeightInDp(Context context) {
        final float height = context.getResources().getDisplayMetrics().heightPixels;
        return px2dip(context, height);
    }

    public static int getWidthInDp(Context context) {
        final float width = context.getResources().getDisplayMetrics().widthPixels;
        return px2dip(context, width);
    }

    public static int dip2px(Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((float) 96 * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
