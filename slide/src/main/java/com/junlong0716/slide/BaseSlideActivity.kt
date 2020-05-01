package com.junlong0716.slide

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Point
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs

abstract class BaseSlideActivity : AppCompatActivity() {
    // 手势滑动监听
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mDecorView: FrameLayout
    // 记录一下上次是多少位置
    private val mLastPoint = PointF()
    // 多少px是滑动了
    private var mTouchSlop = 0
    // window尺寸
    private var mWindowSize = Point()

    private var mIsFirst = false

    private var mCanSlide = false

    private var mSlideView: View? = null

    private var mSlideX = 0f

    private var mSlideFactor = 1f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeUtils.setActivityTranslucentTheme(this)
        setTheme(R.style.TransparentTheme)
        mDecorView = window.decorView as FrameLayout
        mGestureDetector = GestureDetector(this, mGestureDetectorListener)
        mTouchSlop = ViewConfiguration.get(this).scaledTouchSlop
        windowManager.defaultDisplay.getSize(mWindowSize)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        // 替换一下contentView
        findViewById<ViewGroup>(android.R.id.content).let {
            //viewGroup,将背景和content绑定在一起
            val viewGroup = FrameLayout(this).apply {
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
            //contentView
            it.getChildAt(0).apply {
                val params = layoutParams
                params.width = mWindowSize.x
                layoutParams = params
                fitsSystemWindows = true
                it.removeView(this)
                viewGroup.addView(this)
            }
            it.addView(viewGroup)
            mSlideView = viewGroup
        }
    }


    // 手势监听
    private val mGestureDetectorListener = object : SimpleGestureDetectorListener() {
        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            val y = e2!!.y - mLastPoint.y
            val x = e2.x - mLastPoint.x

            if (mIsFirst && abs(x) > mTouchSlop && abs(y) > abs(x) * 1.5) {
                mIsFirst = false
            }
            if (mIsFirst && abs(x) > mTouchSlop && abs(x) > abs(y) * 1.5) {
                mCanSlide = true
            }

            if (mCanSlide) {
                mSlideX += (x / mSlideFactor).toInt()
                mSlideView?.translationX = mSlideX
                mLastPoint.set(e2.x, e2.y)
                return true
            }

            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            mIsFirst = true
            mCanSlide = false
            mLastPoint.set(e!!.x, e.y)
            return true
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!!.action == MotionEvent.ACTION_UP) {
            if (mSlideX >= mWindowSize.x / 3){
                startAnim(true)
            }else{
                startAnim(false)
            }
            return true
        }
        return mGestureDetector.onTouchEvent(event)
    }

    private fun startAnim(back: Boolean) {
        ObjectAnimator().apply {
            duration = 300
            if (back){
                setFloatValues(mSlideX, mWindowSize.x.toFloat())
                addListener(object :SimpleAnimatorListener(){
                    override fun onAnimationEnd(animation: Animator?) {
                        finish()
                    }
                })
            }
            else {
                setFloatValues(mSlideX, 0f)
                addListener(object :SimpleAnimatorListener(){
                    override fun onAnimationEnd(animation: Animator?) {
                        mSlideX = 0f
                    }
                })
            }

            interpolator = DecelerateInterpolator()
            addUpdateListener {
                mSlideX = it.animatedValue as Float
                mSlideView?.translationX = mSlideX
            }
            start()
        }
    }
}
