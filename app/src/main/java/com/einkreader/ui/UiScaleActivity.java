package com.einkreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 全局界面缩放基类
 * 界面文字原始 sp 值按原作者低分屏设备设计，在高分辨率墨水屏上会显得偏小。
 * 通过按屏幕宽度计算缩放系数放大系统 fontScale，所有 Activity 内的
 * sp 文字（含 AlertDialog）自动等比放大，无需逐个修改布局。
 *
 * 基准：480dp 宽（约 1.0 缩放），758px 密度1.0 → 约 1.58 倍
 */
public class UiScaleActivity extends Activity {

    /** 根据屏幕宽度计算界面缩放系数，范围 1.0 ~ 2.5 */
    public static float uiScaleFactor(Context ctx) {
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        float widthDp = dm.widthPixels / dm.density;
        return Math.max(1.0f, Math.min(2.5f, widthDp / 480f));
    }

    @Override
    protected void attachBaseContext(Context base) {
        Configuration c = new Configuration(base.getResources().getConfiguration());
        c.fontScale = c.fontScale * uiScaleFactor(base);
        super.attachBaseContext(base.createConfigurationContext(c));
    }
}
