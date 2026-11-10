package com.einkreader.ui.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 点击区域设置（3x3 九宫格）
 * 点击任意格子 → 弹出动作选择（无操作/上一页/下一页/菜单）
 * 动作保存在 "tap_zone_0" ~ "tap_zone_8"（阅读界面实时按区域响应）
 */
public class TapZoneActivity extends com.einkreader.ui.UiScaleActivity {

    private static final String PREFS_NAME = "eink_reader_prefs";
    private static final String[] ACTION_NAMES = {"无操作", "上一页", "下一页", "菜单"};

    private final TextView[] zoneCells = new TextView[9];
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        // ===== 顶部标题栏 =====
        LinearLayout titleBar = new LinearLayout(this);
        titleBar.setOrientation(LinearLayout.HORIZONTAL);
        titleBar.setGravity(Gravity.CENTER_VERTICAL);
        titleBar.setBackgroundColor(0xFF333333);
        int pad = dp(12);
        titleBar.setPadding(pad, dp(8), pad, dp(8));

        TextView btnBack = new TextView(this);
        btnBack.setText("← 返回");
        btnBack.setTextColor(Color.WHITE);
        btnBack.setTextSize(14);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { finish(); }
        });
        titleBar.addView(btnBack, wrapLp());

        TextView tvTitle = new TextView(this);
        tvTitle.setText("点击区域设置");
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setTextSize(16);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        tvTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams titleLp = wrapLp();
        titleLp.weight = 1;
        titleBar.addView(tvTitle, titleLp);

        TextView btnReset = new TextView(this);
        btnReset.setText("恢复默认");
        btnReset.setTextColor(Color.WHITE);
        btnReset.setTextSize(14);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { resetToDefault(); }
        });
        titleBar.addView(btnReset, wrapLp());

        root.addView(titleBar, matchLp(dp(48)));

        // ===== 提示 =====
        TextView hint = new TextView(this);
        hint.setText("点击九宫格任意区域，选择该区域对应的动作\n阅读时点击对应区域即可触发");
        hint.setTextSize(13);
        hint.setTextColor(Color.BLACK);
        hint.setGravity(Gravity.CENTER);
        int hPad = dp(16);
        hint.setPadding(hPad, dp(10), hPad, dp(10));
        root.addView(hint, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        // ===== 3x3 九宫格（占满剩余空间）=====
        LinearLayout grid = new LinearLayout(this);
        grid.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams gridLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
        int g = dp(2); // 格间距
        grid.setPadding(g, 0, g, g);
        root.addView(grid, gridLp);

        for (int row = 0; row < 3; row++) {
            LinearLayout rowLl = new LinearLayout(this);
            rowLl.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f);
            grid.addView(rowLl, rowLp);

            for (int col = 0; col < 3; col++) {
                final int index = row * 3 + col;

                TextView cell = new TextView(this);
                cell.setTextSize(16);
                cell.setTextColor(Color.BLACK);
                cell.setGravity(Gravity.CENTER);
                // 墨水屏友好：白底黑框黑字，边框清晰
                GradientDrawable bg = new GradientDrawable();
                bg.setColor(Color.WHITE);
                bg.setStroke(dp(1), Color.BLACK);
                bg.setCornerRadius(dp(2));
                cell.setBackground(bg);
                LinearLayout.LayoutParams cellLp = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
                cellLp.setMargins(g, g, g, g);
                rowLl.addView(cell, cellLp);

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) { pickAction(index); }
                });

                zoneCells[index] = cell;
            }
        }

        setContentView(root);
        refreshLabels();
    }

    private void refreshLabels() {
        for (int i = 0; i < 9; i++) {
            int action = prefs.getInt("tap_zone_" + i, ReaderView.defaultTapZone(i));
            zoneCells[i].setText(ACTION_NAMES[Math.max(0, Math.min(3, action))]);
        }
    }

    private void pickAction(final int index) {
        new AlertDialog.Builder(this)
                .setTitle("选择操作（区域 " + (index + 1) + "）")
                .setItems(ACTION_NAMES, new android.content.DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        prefs.edit().putInt("tap_zone_" + index, which).apply();
                        refreshLabels();
                        Toast.makeText(TapZoneActivity.this,
                                "区域 " + (index + 1) + " → " + ACTION_NAMES[which],
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void resetToDefault() {
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < 9; i++) {
            editor.remove("tap_zone_" + i); // 移除后回落到 ReaderView.defaultTapZone
        }
        editor.apply();
        refreshLabels();
        Toast.makeText(this, "已恢复默认布局", Toast.LENGTH_SHORT).show();
    }

    // ===== 工具方法 =====

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private LinearLayout.LayoutParams wrapLp() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams matchLp(int heightPx) {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, heightPx);
    }
}
