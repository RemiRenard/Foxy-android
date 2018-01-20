package org.foxy.foxy.custom

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

import org.foxy.foxy.R

class CustomCircle

/**
 * Constructor circle used for the animation of the splash screen.
 * @param mContext the context
 * @param attrs   the attribute set
 */
(private val mContext: Context, attrs: AttributeSet?) : View(mContext, attrs) {

    private var mValueAnimator: ValueAnimator? = null
    private var mCenterX: Float = 0.toFloat()
    private var mCenterY: Float = 0.toFloat()
    private var mAnimateFloat: Float = 0.toFloat()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    fun startAnimation(centerX: Int, centerY: Int, radiusStart: Int, radiusEnd: Int) {
        mCenterX = centerX.toFloat()
        mCenterY = centerY.toFloat()
        //invalidate();
        mValueAnimator = ValueAnimator.ofFloat(radiusStart.toFloat(), radiusEnd.toFloat())
        mValueAnimator?.duration = ANIMATION_DURATION.toLong()
        mValueAnimator?.addUpdateListener { valueAnimator ->
            mAnimateFloat = valueAnimator.animatedValue as Float
            invalidate()
        }
        mValueAnimator!!.start()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paintEmpty = Paint()
        if (mCenterX + mCenterY != 0f) {
            paintEmpty.shader = RadialGradient(0f, 0f, mAnimateFloat, ContextCompat.getColor(context,
                    R.color.colorLightBlue), ContextCompat.getColor(context, R.color.colorDarkBlue), Shader.TileMode.MIRROR)
            paintEmpty.style = Paint.Style.STROKE
            paintEmpty.strokeWidth = 20f
            paintEmpty.isAntiAlias = true
            canvas.drawCircle(mCenterX, mCenterY, mAnimateFloat, paintEmpty)
            canvas.drawCircle(mCenterX, mCenterY, mAnimateFloat - 100, paintEmpty)
            canvas.drawCircle(mCenterX, mCenterY, mAnimateFloat - 200, paintEmpty)
            canvas.drawCircle(mCenterX, mCenterY, mAnimateFloat - 300, paintEmpty)
            canvas.drawCircle(mCenterX, mCenterY, mAnimateFloat - 400, paintEmpty)
        }
    }

    companion object {
        private val ANIMATION_DURATION = 2000
    }
}
