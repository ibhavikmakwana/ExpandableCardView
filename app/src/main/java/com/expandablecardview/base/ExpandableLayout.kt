package com.expandablecardview.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import com.expandablecardview.R
import com.expandablecardview.utils.Settings
import com.expandablecardview.utils.Utils

/**
 * Created by Bhavik Makwana on 1/8/2018.
 */
class ExpandableLayout : LinearLayout {
    private var mSettings: Settings? = null
    private var mExpandState: Int = 0
    private var mExpandAnimator: ValueAnimator? = null
    private var mParentAnimator: ValueAnimator? = null
    private var mExpandScrollAnimotorSet: AnimatorSet? = null
    private var mExpandedViewHeight: Int = 0
    private var mIsInit = true

    private var mScrolledParent: ScrolledParent? = null

    private var mOnExpandListener: OnExpandListener? = null

    private val parentScrollDistance: Int
        get() {
            var distance = 0

            if (mScrolledParent == null) {
                return distance
            }

            distance = (y + measuredHeight.toFloat() + mExpandedViewHeight.toFloat() - mScrolledParent!!.scrolledView!!.measuredHeight).toInt()
            for (index in 0 until mScrolledParent!!.childBetweenParentCount) {
                val parent = parent as ViewGroup
                distance += parent.y.toInt()
            }

            return distance
        }

    val isExpanded: Boolean
        get() = mExpandState == ExpandState.EXPANDED

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private fun init(attrs: AttributeSet?) {
        isClickable = true
        orientation = LinearLayout.VERTICAL
        this.clipChildren = false
        this.clipToPadding = false
        mExpandState = ExpandState.PRE_INIT
        mSettings = Settings()
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            mSettings!!.expandDuration = typedArray.getInt(R.styleable.ExpandableLayout_expDuration, Settings.EXPAND_DURATION)
            mSettings!!.expandWithParentScroll = typedArray.getBoolean(R.styleable.ExpandableLayout_expWithParentScroll, false)
            mSettings!!.expandScrollTogether = typedArray.getBoolean(R.styleable.ExpandableLayout_expExpandScrollTogether, true)
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val childCount = childCount
        if (childCount != 2) {
            throw IllegalStateException("ExpandableLayout must has two child view !")
        }
        if (mIsInit) {
            (getChildAt(0).layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = 0
            val marginLayoutParams = getChildAt(1).layoutParams as ViewGroup.MarginLayoutParams
            marginLayoutParams.bottomMargin = 0
            marginLayoutParams.topMargin = 0
            marginLayoutParams.height = 0
            mExpandedViewHeight = getChildAt(1).measuredHeight
            mIsInit = false
            mExpandState = ExpandState.CLOSED
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (mSettings!!.expandWithParentScroll) {
            mScrolledParent = Utils.getScrolledParent(this)
        }
    }


    private fun verticalAnimate(startHeight: Int, endHeight: Int) {
        val distance = parentScrollDistance

        val target = getChildAt(1)
        mExpandAnimator = ValueAnimator.ofInt(startHeight, endHeight)
        mExpandAnimator!!.addUpdateListener { animation ->
            target.layoutParams.height = animation.animatedValue as Int
            target.requestLayout()
        }

        mExpandAnimator!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (endHeight - startHeight < 0) {
                    mExpandState = ExpandState.CLOSED
                    if (mOnExpandListener != null) {
                        mOnExpandListener!!.onExpand(false)
                    }
                } else {
                    mExpandState = ExpandState.EXPANDED
                    if (mOnExpandListener != null) {
                        mOnExpandListener!!.onExpand(true)
                    }
                }
            }
        })

        mExpandState = if (mExpandState == ExpandState.EXPANDED) ExpandState.CLOSING else ExpandState.EXPANDING
        mExpandAnimator!!.duration = mSettings!!.expandDuration.toLong()
        if (mExpandState == ExpandState.EXPANDING && mSettings!!.expandWithParentScroll && distance > 0) {

            mParentAnimator = Utils.createParentAnimator(mScrolledParent!!.scrolledView!!, distance, mSettings!!.expandDuration)

            mExpandScrollAnimotorSet = AnimatorSet()

            if (mSettings!!.expandScrollTogether) {
                mExpandScrollAnimotorSet!!.playTogether(mExpandAnimator, mParentAnimator)
            } else {
                mExpandScrollAnimotorSet!!.playSequentially(mExpandAnimator, mParentAnimator)
            }
            mExpandScrollAnimotorSet!!.start()

        } else {
            mExpandAnimator!!.start()
        }
    }

    fun setExpand(expand: Boolean) {
        if (mExpandState == ExpandState.PRE_INIT) {
            return
        }
        getChildAt(1).layoutParams.height = if (expand) mExpandedViewHeight else 0
        requestLayout()
        mExpandState = if (expand) ExpandState.EXPANDED else ExpandState.CLOSED
    }

    fun toggle() {
        if (mExpandState == ExpandState.EXPANDED) {
            close()
        } else if (mExpandState == ExpandState.CLOSED) {
            expand()
        }
    }

    fun expand() {
        verticalAnimate(0, mExpandedViewHeight)
    }

    fun close() {
        verticalAnimate(mExpandedViewHeight, 0)
    }

    override fun performClick(): Boolean {
        toggle()
        return super.performClick()
    }

    interface OnExpandListener {
        fun onExpand(expanded: Boolean)
    }

    fun setOnExpandListener(onExpandListener: OnExpandListener) {
        this.mOnExpandListener = onExpandListener
    }


    fun setExpandScrollTogether(expandScrollTogether: Boolean) {
        this.mSettings!!.expandScrollTogether = expandScrollTogether
    }

    fun setExpandWithParentScroll(expandWithParentScroll: Boolean) {
        this.mSettings!!.expandWithParentScroll = expandWithParentScroll
    }

    fun setExpandDuration(expandDuration: Int) {
        this.mSettings!!.expandDuration = expandDuration
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (mExpandAnimator != null && mExpandAnimator!!.isRunning) {
            mExpandAnimator!!.cancel()
            mExpandAnimator!!.removeAllUpdateListeners()
        }
        if (mParentAnimator != null && mParentAnimator!!.isRunning) {
            mParentAnimator!!.cancel()
            mParentAnimator!!.removeAllUpdateListeners()
        }
        if (mExpandScrollAnimotorSet != null) {
            mExpandScrollAnimotorSet!!.cancel()
        }
    }

    companion object {
        private val TAG = ExpandableLayout::class.java.simpleName
    }
}