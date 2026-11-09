package com.einkreader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * 全局界面缩放基类
 * 界面按原作者低分屏设备设计，在高分辨率墨水屏上会显得偏小。
 * 通过等比放大 density（densityDpi）实现整版缩放：sp 文字与 dp 容器
 * 一起放大，比例不变，避免只放大字体导致的文字溢出。
 *
 * 基准：480dp 宽（约 1.0 缩放），758px 密度1.0 → 约 1.58 倍
 */
public class UiScaleActivity extends Activity {

    /** 进程内缓存：缩放系数只从未缩放的 context 计算一次 */
    private static volatile float sCachedFactor = -1f;

    /** 根据屏幕宽度计算界面缩放系数，范围 1.0 ~ 2.5 */
    public static float uiScaleFactor(Context ctx) {
        if (sCachedFactor > 0) return sCachedFactor;
        DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        float widthDp = dm.widthPixels / dm.density;
        float factor = Math.max(1.0f, Math.min(2.5f, widthDp / 480f));
        sCachedFactor = factor;
        return factor;
    }

    /** 未缩放的真实密度（供正文渲染等需要原始 dp 值的场合使用） */
    public static float realDensity(Context ctx) {
        return ctx.getResources().getDisplayMetrics().density / uiScaleFactor(ctx);
    }

    @Override
    protected void attachBaseContext(Context base) {
        DisplayMetrics dm = base.getResources().getDisplayMetrics();
        Configuration c = new Configuration(base.getResources().getConfiguration());
        c.densityDpi = Math.round(dm.densityDpi * uiScaleFactor(base));
        super.attachBaseContext(base.createConfigurationContext(c));
    }
}
