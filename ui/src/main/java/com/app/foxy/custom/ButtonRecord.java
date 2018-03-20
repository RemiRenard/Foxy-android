package com.app.foxy.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.app.foxy.R;

public class ButtonRecord extends View {

    private int mRadiusFillBlue, mRadiusStrokeCyan, mX, mY, mSpace;
    private CountDownTimer mTimer;
    private float mSweepAngle;
    private Paint mPaintFillPrimary, mPaintStrokeDark, mPaintStrokeRed;

    public ButtonRecord(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        managePaint();
        initVariables();
    }

    /**
     * Init variables used to measure radius and points.
     */
    private void initVariables() {
        mRadiusStrokeCyan = 100;
        mRadiusFillBlue = 0;
        mSweepAngle = 0;
        mSpace = mRadiusStrokeCyan + 20;
    }

    /**
     * Set up paints.
     */
    private void managePaint() {
        mPaintFillPrimary = new Paint();
        mPaintStrokeDark = new Paint();
        mPaintStrokeRed = new Paint();
        mPaintFillPrimary.setAntiAlias(true);
        mPaintStrokeDark.setAntiAlias(true);
        mPaintStrokeRed.setAntiAlias(true);
        mPaintStrokeDark.setStyle(Paint.Style.STROKE);
        mPaintStrokeRed.setStyle(Paint.Style.STROKE);
        mPaintStrokeDark.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        mPaintFillPrimary.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mPaintStrokeRed.setColor(Color.RED);
        mPaintStrokeDark.setStrokeWidth(20f);
        mPaintStrokeRed.setStrokeWidth(10f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        mX = getMeasuredWidth() / 2;
        mY = getMeasuredHeight() / 2;
    }

    @Override
    @SuppressLint("DrawAllocation")
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mY, mRadiusStrokeCyan, mPaintStrokeDark);
        canvas.drawCircle(mX, mY, mRadiusFillBlue, mPaintFillPrimary);
        RectF rectF = new RectF(mX - mSpace, mY - mSpace, mX + mSpace, mY + mSpace);
        canvas.drawArc(rectF, 270, mSweepAngle, false, mPaintStrokeRed);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTimer = new CountDownTimer(5000, 10) {
                    @Override
                    public void onTick(long l) {
                        if (l > 4800) {
                            mRadiusStrokeCyan += 1;
                        }
                        if (mRadiusFillBlue < mRadiusStrokeCyan - 30) {
                            mRadiusFillBlue += 5;
                        }
                        mSweepAngle += 1.35;
                        invalidate();
                    }

                    @Override
                    public void onFinish() {
                        mTimer.cancel();
                        initVariables();
                        invalidate();
                        Toast.makeText(getContext(), "Completed", Toast.LENGTH_SHORT).show();
                    }
                }.start();
                break;
            case MotionEvent.ACTION_UP:
                mTimer.onFinish();
                initVariables();
                invalidate();
                break;
        }
        return true;
    }
}
