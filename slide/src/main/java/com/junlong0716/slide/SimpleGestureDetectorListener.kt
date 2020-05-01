package com.junlong0716.slide

import android.view.GestureDetector
import android.view.MotionEvent

/**
 * @ClassName:      SimpleGestureDetectorListener
 * @Description:
 * @Author:         LiJunlong
 * @CreateDate:     2020/5/1 11:23 AM
 */
open class SimpleGestureDetectorListener: GestureDetector.OnGestureListener {
    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return true
    }

    override fun onLongPress(e: MotionEvent?) {

    }

}