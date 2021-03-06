package com.app.foxy.custom

import android.content.Context
import android.graphics.Rect
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.app.foxy.R


class SwipeLayout : ViewGroup {

    /**
     * Main view is the view which is shown when the layout is closed.
     */
    private var mMainView: View? = null

    /**
     * Secondary view is the view which is shown when the layout is opened.
     */
    private var mSecondaryView: View? = null

    /**
     * The rectangle position of the main view when the layout is closed.
     */
    private val mRectMainClose = Rect()

    /**
     * The rectangle position of the main view when the layout is opened.
     */
    private val mRectMainOpen = Rect()

    /**
     * The rectangle position of the secondary view when the layout is closed.
     */
    private val mRectSecClose = Rect()

    /**
     * The rectangle position of the secondary view when the layout is opened.
     */
    private val mRectSecOpen = Rect()

    /**
     * The minimum distance (px) to the closest drag edge that the SwipeRevealLayout
     * will disallow the parent to intercept touch event.
     */
    private var mMinDistRequestDisallowParent = 0

    private var mIsOpenBeforeInit = false
    @Volatile
    private var mAborted = false
    @Volatile
    private var mIsScrolling = false
    /**
     * @return true if the drag/swipe motion is currently locked.
     */
    @Volatile
    var isDragLocked = false
        private set

    /**
     * Get the minimum fling velocity to cause the layout to open/close.

     * @return dp per second
     */
    /**
     * Set the minimum fling velocity to cause the layout to open/close.

     * @param velocity dp per second
     */
    var minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY
    private var mState = STATE_CLOSE
    private var mMode = MODE_NORMAL

    private var mLastMainLeft = 0
    private var mLastMainTop = 0
    var dragEdge = DRAG_EDGE_LEFT

    private var mDragHelper: ViewDragHelper? = null
    private var mGestureDetector: GestureDetectorCompat? = null

    private val mDragStateChangeListener: DragStateChangeListener? = null // only used for ViewBindHelper
    private var mSwipeListener: SwipeListener? = null

    private var mOnLayoutCount = 0

    internal interface DragStateChangeListener {
        fun onDragStateChanged(state: Int)
    }

    /**
     * Listener for monitoring events about swipe layout.
     */
    interface SwipeListener {
        /**
         * Called when the main view becomes completely closed.
         */
        fun onClosed(view: SwipeLayout)

        /**
         * Called when the main view becomes completely opened.
         */
        fun onOpened(view: SwipeLayout)

        /**
         * Called when the main view's position changes.

         * @param slideOffset The new offset of the main view within its range, from 0-1
         */
        fun onSlide(view: SwipeLayout, slideOffset: Float)
    }

    /**
     * No-op stub for [SwipeLayout.SwipeListener]. If you only want ot implement a subset
     * of the listener methods, you can extend this instead of implement the full interface.
     */
    class SimpleSwipeListener : SwipeLayout.SwipeListener {
        override fun onClosed(view: SwipeLayout) {}

        override fun onOpened(view: SwipeLayout) {}

        override fun onSlide(view: SwipeLayout, slideOffset: Float) {}
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector?.onTouchEvent(event)
        mDragHelper?.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        mDragHelper?.processTouchEvent(ev)
        mGestureDetector?.onTouchEvent(ev)

        val settling = mDragHelper?.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = mDragHelper?.viewDragState == ViewDragHelper.STATE_IDLE && mIsScrolling

        return settling || idleAfterScrolled
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // get views
        if (childCount >= 2) {
            mSecondaryView = getChildAt(0)
            mMainView = getChildAt(1)
        } else if (childCount == 1) {
            mMainView = getChildAt(0)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAborted = false

        for (index in 0 until childCount) {
            val child = getChildAt(index)

            var left: Int
            var right: Int
            var top: Int
            var bottom: Int
            bottom = 0
            top = bottom
            right = top
            left = right

            val minLeft = paddingLeft
            val maxRight = Math.max(r - paddingRight - l, 0)
            val minTop = paddingTop
            val maxBottom = Math.max(b - paddingBottom - t, 0)

            var measuredChildHeight = child.measuredHeight
            var measuredChildWidth = child.measuredWidth

            // need to take account if child size is match_parent
            val childParams = child.layoutParams
            var matchParentHeight = false
            var matchParentWidth = false

            if (childParams != null) {
                matchParentHeight = childParams.height == ViewGroup.LayoutParams.MATCH_PARENT || childParams.height == ViewGroup.LayoutParams.WRAP_CONTENT
                matchParentWidth = childParams.width == ViewGroup.LayoutParams.MATCH_PARENT || childParams.width == ViewGroup.LayoutParams.WRAP_CONTENT
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop
                childParams?.height = measuredChildHeight
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft
                childParams?.width = measuredChildWidth
            }

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> {
                    left = Math.max(r - measuredChildWidth - paddingRight - l, minLeft)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.max(r - paddingRight - l, minLeft)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_LEFT -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_TOP -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_BOTTOM -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.max(b - measuredChildHeight - paddingBottom - t, minTop)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.max(b - paddingBottom - t, minTop)
                }
            }

            child.layout(left, top, right, bottom)
        }

        // taking account offset when mode is SAME_LEVEL
        if (mMode == MODE_SAME_LEVEL) {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> mSecondaryView?.offsetLeftAndRight(-mSecondaryView!!.width)

                DRAG_EDGE_RIGHT -> mSecondaryView?.offsetLeftAndRight(mSecondaryView!!.width)

                DRAG_EDGE_TOP -> mSecondaryView?.offsetTopAndBottom(-mSecondaryView!!.height)

                DRAG_EDGE_BOTTOM -> mSecondaryView?.offsetTopAndBottom(mSecondaryView!!.height)
            }
        }

        initRects()

        if (mIsOpenBeforeInit) {
            open(false)
        } else {
            close(false)
        }

        mLastMainLeft = mMainView!!.left
        mLastMainTop = mMainView!!.top

        mOnLayoutCount++
    }

    /**
     * {@inheritDoc}
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount < 2) {
            throw RuntimeException("Layout must have two children")
        }

        val params = layoutParams

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        var desiredWidth = 0
        var desiredHeight = 0

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childParams = child.layoutParams

            if (childParams != null) {
                if (childParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumHeight = measuredHeight
                }

                if (childParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumWidth = measuredWidth
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            desiredWidth = Math.max(child.measuredWidth, desiredWidth)
            desiredHeight = Math.max(child.measuredHeight, desiredHeight)
        }

        // taking accounts of padding
        desiredWidth += paddingLeft + paddingRight
        desiredHeight += paddingTop + paddingBottom

        // adjust desired width
        if (widthMode == View.MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth
        } else {
            if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth
            }

            if (widthMode == View.MeasureSpec.AT_MOST) {
                desiredWidth = if (desiredWidth > measuredWidth) measuredWidth else desiredWidth
            }
        }

        // adjust desired height
        if (heightMode == View.MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight
        } else {
            if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight
            }

            if (heightMode == View.MeasureSpec.AT_MOST) {
                desiredHeight = if (desiredHeight > measuredHeight) measuredHeight else desiredHeight
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun computeScroll() {
        if (mDragHelper!!.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Open the panel to show the secondary view

     * @param animation true to animate the open motion. [SwipeLayout.SwipeListener] won't be
     * *                  called if is animation is false.
     */
    fun open(animation: Boolean) {
        mIsOpenBeforeInit = true
        mAborted = false

        if (animation) {
            mState = STATE_OPENING
            mDragHelper?.smoothSlideViewTo(mMainView!!, mRectMainOpen.left, mRectMainOpen.top)

            mDragStateChangeListener?.onDragStateChanged(mState)
        } else {
            mState = STATE_OPEN
            mDragHelper?.abort()

            mMainView?.layout(
                    mRectMainOpen.left,
                    mRectMainOpen.top,
                    mRectMainOpen.right,
                    mRectMainOpen.bottom
            )

            mSecondaryView!!.layout(
                    mRectSecOpen.left,
                    mRectSecOpen.top,
                    mRectSecOpen.right,
                    mRectSecOpen.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    /**
     * Close the panel to hide the secondary view

     * @param animation true to animate the close motion. [SwipeLayout.SwipeListener] won't be
     * *                  called if is animation is false.
     */
    fun close(animation: Boolean) {
        mIsOpenBeforeInit = false
        mAborted = false

        if (animation) {
            mState = STATE_CLOSING
            mDragHelper?.smoothSlideViewTo(mMainView!!, mRectMainClose.left, mRectMainClose.top)

            mDragStateChangeListener?.onDragStateChanged(mState)

        } else {
            mState = STATE_CLOSE
            mDragHelper?.abort()

            mMainView?.layout(
                    mRectMainClose.left,
                    mRectMainClose.top,
                    mRectMainClose.right,
                    mRectMainClose.bottom
            )

            mSecondaryView?.layout(
                    mRectSecClose.left,
                    mRectSecClose.top,
                    mRectSecClose.right,
                    mRectSecClose.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this)
    }

    fun setSwipeListener(listener: SwipeListener) {
        mSwipeListener = listener
    }

    /**
     * @param lock if set to true, the user cannot drag/swipe the layout.
     */
    fun setLockDrag(lock: Boolean) {
        isDragLocked = lock
    }

    /**
     * @return true if layout is fully opened, false otherwise.
     */
    val isOpened: Boolean
        get() = mState == STATE_OPEN

    /**
     * @return true if layout is fully closed, false otherwise.
     */
    val isClosed: Boolean
        get() = mState == STATE_CLOSE

    /**
     * In RecyclerView/ListView, onLayout should be called 2 times to display children views correctly.
     * This method check if it've already called onLayout two times.

     * @return true if you should call [.requestLayout].
     */
    private fun shouldRequestLayout(): Boolean {
        return mOnLayoutCount < 2
    }


    private val mainOpenLeft: Int
        get() {
            return when (dragEdge) {
                DRAG_EDGE_LEFT -> mRectMainClose.left + mSecondaryView!!.width

                DRAG_EDGE_RIGHT -> mRectMainClose.left - mSecondaryView!!.width

                DRAG_EDGE_TOP -> mRectMainClose.left

                DRAG_EDGE_BOTTOM -> mRectMainClose.left

                else -> 0
            }
        }

    private val mainOpenTop: Int
        get() {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> return mRectMainClose.top

                DRAG_EDGE_RIGHT -> return mRectMainClose.top

                DRAG_EDGE_TOP -> return mRectMainClose.top + mSecondaryView!!.height

                DRAG_EDGE_BOTTOM -> return mRectMainClose.top - mSecondaryView!!.height

                else -> return 0
            }
        }

    private val secOpenLeft: Int
        get() {
            if (mMode == MODE_NORMAL || dragEdge == DRAG_EDGE_BOTTOM || dragEdge == DRAG_EDGE_TOP) {
                return mRectSecClose.left
            }

            if (dragEdge == DRAG_EDGE_LEFT) {
                return mRectSecClose.left + mSecondaryView!!.width
            } else {
                return mRectSecClose.left - mSecondaryView!!.width
            }
        }

    private val secOpenTop: Int
        get() {
            if (mMode == MODE_NORMAL || dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                return mRectSecClose.top
            }

            if (dragEdge == DRAG_EDGE_TOP) {
                return mRectSecClose.top + mSecondaryView!!.height
            } else {
                return mRectSecClose.top - mSecondaryView!!.height
            }
        }

    private fun initRects() {
        // close position of main view
        mRectMainClose.set(
                mMainView!!.left,
                mMainView!!.top,
                mMainView!!.right,
                mMainView!!.bottom
        )

        // close position of secondary view
        mRectSecClose.set(
                mSecondaryView!!.left,
                mSecondaryView!!.top,
                mSecondaryView!!.right,
                mSecondaryView!!.bottom
        )

        // open position of the main view
        mRectMainOpen.set(
                mainOpenLeft,
                mainOpenTop,
                mainOpenLeft + mMainView!!.width,
                mainOpenTop + mMainView!!.height
        )

        // open position of the secondary view
        mRectSecOpen.set(
                secOpenLeft,
                secOpenTop,
                secOpenLeft + mSecondaryView!!.width,
                secOpenTop + mSecondaryView!!.height
        )
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        if (attrs != null && context != null) {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.SwipeLayout,
                    0, 0
            )

            dragEdge = a.getInteger(R.styleable.SwipeLayout_dragEdge, DRAG_EDGE_LEFT)
            minFlingVelocity = a.getInteger(R.styleable.SwipeLayout_flingVelocity, DEFAULT_MIN_FLING_VELOCITY)
            mMode = a.getInteger(R.styleable.SwipeLayout_mode, MODE_NORMAL)

            mMinDistRequestDisallowParent = a.getDimensionPixelSize(
                    R.styleable.SwipeLayout_minDistRequestDisallowParent,
                    dpToPx(DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT)
            )
        }

        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback)
        mDragHelper!!.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        mGestureDetector = GestureDetectorCompat(context, mGestureListener)
    }

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        internal var hasDisallowed = false

        override fun onDown(e: MotionEvent): Boolean {
            mIsScrolling = false
            hasDisallowed = false
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            mIsScrolling = true
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mIsScrolling = true

            if (parent != null) {
                val shouldDisallow: Boolean

                if (!hasDisallowed) {
                    shouldDisallow = distToClosestEdge >= mMinDistRequestDisallowParent
                    if (shouldDisallow) {
                        hasDisallowed = true
                    }
                } else {
                    shouldDisallow = true
                }

                // disallow parent to intercept touch event so that the layout will work
                // properly on RecyclerView or view that handles scroll gesture.
                parent.requestDisallowInterceptTouchEvent(shouldDisallow)
            }

            return false
        }
    }

    private val distToClosestEdge: Int
        get() {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> {
                    val pivotRight = mRectMainClose.left + mSecondaryView!!.width

                    return Math.min(
                            mMainView!!.left - mRectMainClose.left,
                            pivotRight - mMainView!!.left
                    )
                }

                DRAG_EDGE_RIGHT -> {
                    val pivotLeft = mRectMainClose.right - mSecondaryView!!.width

                    return Math.min(
                            mMainView!!.right - pivotLeft,
                            mRectMainClose.right - mMainView!!.right
                    )
                }

                DRAG_EDGE_TOP -> {
                    val pivotBottom = mRectMainClose.top + mSecondaryView!!.height

                    return Math.min(
                            mMainView!!.bottom - pivotBottom,
                            pivotBottom - mMainView!!.top
                    )
                }

                DRAG_EDGE_BOTTOM -> {
                    val pivotTop = mRectMainClose.bottom - mSecondaryView!!.height

                    return Math.min(
                            mRectMainClose.bottom - mMainView!!.bottom,
                            mMainView!!.bottom - pivotTop
                    )
                }
            }

            return 0
        }

    private val halfwayPivotHorizontal: Int
        get() {
            if (dragEdge == DRAG_EDGE_LEFT) {
                return mRectMainClose.left + mSecondaryView!!.width / 2
            } else {
                return mRectMainClose.right - mSecondaryView!!.width / 2
            }
        }

    private val halfwayPivotVertical: Int
        get() {
            if (dragEdge == DRAG_EDGE_TOP) {
                return mRectMainClose.top + mSecondaryView!!.height / 2
            } else {
                return mRectMainClose.bottom - mSecondaryView!!.height / 2
            }
        }

    private val mDragHelperCallback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            mAborted = false

            if (isDragLocked)
                return false

            mDragHelper!!.captureChildView(mMainView!!, pointerId)
            return false
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_TOP -> Math.max(
                        Math.min(top, mRectMainClose.top + mSecondaryView!!.height),
                        mRectMainClose.top
                )

                DRAG_EDGE_BOTTOM -> Math.max(
                        Math.min(top, mRectMainClose.top),
                        mRectMainClose.top - mSecondaryView!!.height
                )
                else -> child.top
            }
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_RIGHT -> Math.max(
                        Math.min(left, mRectMainClose.left),
                        mRectMainClose.left - mSecondaryView!!.width
                )

                DRAG_EDGE_LEFT -> Math.max(
                        Math.min(left, mRectMainClose.left + mSecondaryView!!.width),
                        mRectMainClose.left
                )

                else -> child.left
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val velRightExceeded = pxToDp(xvel.toInt()) >= minFlingVelocity
            val velLeftExceeded = pxToDp(xvel.toInt()) <= -minFlingVelocity
            val velUpExceeded = pxToDp(yvel.toInt()) <= -minFlingVelocity
            val velDownExceeded = pxToDp(yvel.toInt()) >= minFlingVelocity

            val pivotHorizontal = halfwayPivotHorizontal
            val pivotVertical = halfwayPivotVertical

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> if (velRightExceeded) {
                    close(true)
                } else if (velLeftExceeded) {
                    open(true)
                } else {
                    if (mMainView!!.right < pivotHorizontal) {
                        open(true)
                    } else {
                        close(true)
                    }
                }

                DRAG_EDGE_LEFT -> if (velRightExceeded) {
                    open(true)
                } else if (velLeftExceeded) {
                    close(true)
                } else {
                    if (mMainView!!.left < pivotHorizontal) {
                        close(true)
                    } else {
                        open(true)
                    }
                }

                DRAG_EDGE_TOP -> if (velUpExceeded) {
                    close(true)
                } else if (velDownExceeded) {
                    open(true)
                } else {
                    if (mMainView!!.top < pivotVertical) {
                        close(true)
                    } else {
                        open(true)
                    }
                }

                DRAG_EDGE_BOTTOM -> if (velUpExceeded) {
                    open(true)
                } else if (velDownExceeded) {
                    close(true)
                } else {
                    if (mMainView!!.bottom < pivotVertical) {
                        open(true)
                    } else {
                        close(true)
                    }
                }
            }
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)

            if (isDragLocked) {
                return
            }

            val edgeStartLeft = dragEdge == DRAG_EDGE_RIGHT && edgeFlags == ViewDragHelper.EDGE_LEFT

            val edgeStartRight = dragEdge == DRAG_EDGE_LEFT && edgeFlags == ViewDragHelper.EDGE_RIGHT

            val edgeStartTop = dragEdge == DRAG_EDGE_BOTTOM && edgeFlags == ViewDragHelper.EDGE_TOP

            val edgeStartBottom = dragEdge == DRAG_EDGE_TOP && edgeFlags == ViewDragHelper.EDGE_BOTTOM

            if (edgeStartLeft || edgeStartRight || edgeStartTop || edgeStartBottom) {
                mDragHelper!!.captureChildView(mMainView!!, pointerId)
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (mMode == MODE_SAME_LEVEL) {
                if (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                    mSecondaryView!!.offsetLeftAndRight(dx)
                } else {
                    mSecondaryView!!.offsetTopAndBottom(dy)
                }
            }

            val isMoved = mMainView!!.left != mLastMainLeft || mMainView!!.top != mLastMainTop
            if (mSwipeListener != null && isMoved) {
                if (mMainView!!.left == mRectMainClose.left && mMainView!!.top == mRectMainClose.top) {
                    mSwipeListener!!.onClosed(this@SwipeLayout)
                } else if (mMainView!!.left == mRectMainOpen.left && mMainView!!.top == mRectMainOpen.top) {
                    mSwipeListener!!.onOpened(this@SwipeLayout)
                } else {
                    mSwipeListener!!.onSlide(this@SwipeLayout, slideOffset)
                }
            }

            mLastMainLeft = mMainView!!.left
            mLastMainTop = mMainView!!.top
            ViewCompat.postInvalidateOnAnimation(this@SwipeLayout)
        }

        private val slideOffset: Float
            get() {
                return when (dragEdge) {
                    DRAG_EDGE_LEFT -> (mMainView!!.left - mRectMainClose.left).toFloat() / mSecondaryView!!.width

                    DRAG_EDGE_RIGHT -> (mRectMainClose.left - mMainView!!.left).toFloat() / mSecondaryView!!.width

                    DRAG_EDGE_TOP -> (mMainView!!.top - mRectMainClose.top).toFloat() / mSecondaryView!!.height

                    DRAG_EDGE_BOTTOM -> (mRectMainClose.top - mMainView!!.top).toFloat() / mSecondaryView!!.height

                    else -> 0f
                }
            }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val prevState = mState

            when (state) {
                ViewDragHelper.STATE_DRAGGING -> mState = STATE_DRAGGING

                ViewDragHelper.STATE_IDLE ->

                    // drag edge is left or right
                    mState = if (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                        if (mMainView!!.left == mRectMainClose.left) {
                            STATE_CLOSE
                        } else {
                            STATE_OPEN
                        }
                    } else {
                        if (mMainView!!.top == mRectMainClose.top) {
                            STATE_CLOSE
                        } else {
                            STATE_OPEN
                        }
                    }// drag edge is top or bottom
            }

            if (mDragStateChangeListener != null && !mAborted && prevState != mState) {
                mDragStateChangeListener.onDragStateChanged(mState)
            }
        }
    }

    private fun pxToDp(px: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    private fun dpToPx(dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    companion object {
        // These states are used only for ViewBindHelper
        protected val STATE_CLOSE = 0
        protected val STATE_CLOSING = 1
        protected val STATE_OPEN = 2
        protected val STATE_OPENING = 3
        protected val STATE_DRAGGING = 4

        private val DEFAULT_MIN_FLING_VELOCITY = 300 // dp per second
        private val DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1 // dp

        val DRAG_EDGE_LEFT = 0x1
        val DRAG_EDGE_RIGHT = 0x1 shl 1
        val DRAG_EDGE_TOP = 0x1 shl 2
        val DRAG_EDGE_BOTTOM = 0x1 shl 3

        /**
         * The secondary view will be under the main view.
         */
        val MODE_NORMAL = 0

        /**
         * The secondary view will stick the edge of the main view.
         */
        val MODE_SAME_LEVEL = 1

        fun getStateString(state: Int): String {
            return when (state) {
                STATE_CLOSE -> "state_close"

                STATE_CLOSING -> "state_closing"

                STATE_OPEN -> "state_open"

                STATE_OPENING -> "state_opening"

                STATE_DRAGGING -> "state_dragging"

                else -> "undefined"
            }
        }
    }
}
