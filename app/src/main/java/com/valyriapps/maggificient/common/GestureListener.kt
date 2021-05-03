package com.valyriapps.maggificient.common

import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs

class GestureListener(private val listener: SwipeGesture) : GestureDetector.OnGestureListener {
    private val swipeThreshold = 100
    private val swipeVelocityThreshold = 100

    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (abs(velocityY) < swipeVelocityThreshold || abs(e2.y - e1.y) < swipeThreshold) {
            return false
        }

        if (e2.y - e1.y > 0) {
            this.listener.onSwipeDown()
        } else {
            this.listener.onSwipeUp()
        }
        return true
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean {
        return if (e.action == MotionEvent.ACTION_UP) {
            this.listener.onTap()
            true
        } else {
            false
        }
    }

    override fun onLongPress(e: MotionEvent) {
        this.listener.onLongPressed()
    }

    override fun onShowPress(e: MotionEvent?) = Unit
    override fun onDown(e: MotionEvent?): Boolean = true
    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean = false
}