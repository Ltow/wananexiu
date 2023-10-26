package com.bossed.waej.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.bossed.waej.util.States;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {
    private final Rect mTextBound = new Rect();
    private int[] colors = {0xFF414BFF, 0xFF0095FF};//默认颜色
    private int direction = 0;//默认方向

    public GradientTextView(Context context) {
        super(context);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置自定义渐变颜色和方向
     *
     * @param direction 方向
     * @param colors    颜色
     */
    public void setGradientAttribute(int direction, int[] colors) {
        this.colors = colors;
        this.direction = direction;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int orientation;
        if (direction == States.vertical) {
            orientation = getMeasuredHeight();//纵向渐变
        } else {
            orientation = getMeasuredWidth();//横向渐变
        }
        Paint mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        @SuppressLint("DrawAllocation")
        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, orientation,
                colors,
                null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }
}
