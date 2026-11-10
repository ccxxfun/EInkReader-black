package com.einkreader.ui.reader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 电池样式电量显示：外框 + 正极凸头，框内显示百分比数字
 * 颜色随日间/夜间主题切换（见 setThemeColor）
 */
public class BatteryView extends View {
    private final Paint framePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int level = -1; // -1 = 未知
    private int fgColor = Color.BLACK;

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        framePaint.setStyle(Paint.Style.STROKE);
    }

    /** 设置电量百分比（-1 表示未知，显示 --） */
    public void setLevel(int level) {
        this.level = level;
        invalidate();
    }

    /** 跟随主题切换前景色（日间黑 / 夜间白） */
    public void setThemeColor(int color) {
        this.fgColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float density = getResources().getDisplayMetrics().density;
        float w = getWidth(), h = getHeight();
        if (w <= 0 || h <= 0) return;

        // 电池主体尺寸（nub 凸头额外占 3dp）
        float bh = Math.min(h - 8 * density, 16 * density);
        float bw = Math.min(w - 12 * density, 32 * density);
        if (bw < 8 * density || bh < 8 * density) return;
        float left = (w - bw - 3 * density) / 2f;
        float top = (h - bh) / 2f;

        framePaint.setStrokeWidth(1.5f * density);
        framePaint.setColor(fgColor);
        textPaint.setColor(fgColor);

        // 主体外框（圆角矩形）
        canvas.drawRoundRect(new RectF(left, top, left + bw, top + bh),
                2 * density, 2 * density, framePaint);
        // 正极凸头（实心小矩形）
        canvas.drawRect(left + bw, top + bh * 0.3f,
                left + bw + 3 * density, top + bh * 0.7f, framePaint);

        // 框内百分比数字
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(bh * 0.58f);
        String label = level >= 0 ? level + "%" : "--";
        float textY = top + bh / 2f - (textPaint.ascent() + textPaint.descent()) / 2f;
        canvas.drawText(label, left + bw / 2f, textY, textPaint);
    }
}
