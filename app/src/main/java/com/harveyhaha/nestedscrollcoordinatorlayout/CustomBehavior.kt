package com.harveyhaha.nestedscrollcoordinatorlayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

class CustomBehavior : AppBarLayout.Behavior {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (mTouchSlop < 0) {
            mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        }
    }

    private var mTouchSlop: Int = -1
    private var mScrollingViewBehaviorView: View? = null
    private var mIsBeginDragged = false
    private var mNeedDispatcher = true
    private var mLastMotionX = 0
    private var mLastMotionY = 0
    private var mActivityPointId = -1
    private var mCurrentEvent: MotionEvent? = null

    //确认滑动去拦截，如果是在本控件内拖拽则拦截touche event
    override fun onInterceptTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        Log.e("CustomBehavior", "onInterceptTouchEvent: " + ev.action + " mTouchSlop:" + mTouchSlop)
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                mIsBeginDragged = false
                mNeedDispatcher = true
                val x = ev.x.toInt()
                val y = ev.y.toInt()
                if (parent.isPointInChildBounds(child, x.toInt(), y)) {
                    Log.e("onInterceptTouchEvent", "isPointInChildBounds")
                    mLastMotionY = y
                    mLastMotionX = x
                    mActivityPointId = ev.getPointerId(0)
                    mCurrentEvent?.recycle()
                    mCurrentEvent = MotionEvent.obtain(ev)
                }
            }

//            MotionEvent.ACTION_MOVE -> {
//                val activityId = mActivityPointId
//                Log.e("onInterceptTouchEvent", "activityId: $activityId")
//                if (activityId != 0 && ev.findPointerIndex(activityId) != -1) {
//                    val moveY = ev.y
//                    val diffY = Math.abs(moveY - mLastMotionY)
//                    Log.e(
//                        "onInterceptTouchEvent",
//                        "滑动距离: " + diffY + "mTouchSlop：" + mTouchSlop
//                    )
//                    if (diffY > mTouchSlop) { //说明是滑动动作
//                        mIsBeginDragged = true
//                    }
//                }
//            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                mIsBeginDragged = false
                mNeedDispatcher = true
                mActivityPointId = -1
            }
        }
        if (!mIsBeginDragged) {
            return super.onInterceptTouchEvent(parent, child, ev)
        }
        return true
    }

    //去处理将AppBarLayout.Behavior事件交给AppBarLayout.ScrollingViewBehavior
    override fun onTouchEvent(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        ev: MotionEvent
    ): Boolean {
        Log.e("CustomBehavior", "onTouchEvent: " + ev.action)
        var mIsTouchEvent = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mIsTouchEvent = false
            }

            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.x
                val moveY = ev.y
                val diffX = Math.abs(moveX - mLastMotionX)
                val diffY = Math.abs(moveY - mLastMotionY)
                if (diffY > mTouchSlop && diffY > diffX) { //说明是滑动动作，只考虑垂直滑动
                    mIsBeginDragged = true
                }
                if (mIsBeginDragged) {
                    Log.e("onTouchEvent", "滑动距离: " + diffY + "mTouchSlop：" + mTouchSlop)
                    //位移到下方View
                    val offset = child.height - child.bottom
                    if (mNeedDispatcher) {
                        mNeedDispatcher = false
                        Log.e("CustomBehavior", "offset: $offset")
                        mCurrentEvent?.offsetLocation(0F, offset.toFloat())
                        //传递下去 进行移交事件
                        mScrollingViewBehaviorView?.dispatchTouchEvent(mCurrentEvent)
                    } else {
                        ev.offsetLocation(0F, offset.toFloat())
                        mScrollingViewBehaviorView?.dispatchTouchEvent(ev)
                        mIsTouchEvent = true
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                if (mIsBeginDragged) {
                    ev.offsetLocation(0F, (child.height - child.bottom).toFloat())
                    mScrollingViewBehaviorView?.dispatchTouchEvent(ev)
                    mIsTouchEvent = true
                }
            }
        }
        if (!mIsTouchEvent) {
            return super.onTouchEvent(parent, child, ev)
        }
        return mIsTouchEvent
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout,
        abl: AppBarLayout,
        layoutDirection: Int
    ): Boolean {
        val onLayoutChild = super.onLayoutChild(parent, abl, layoutDirection)
        val childCount: Int = parent.childCount
        for (i in 0 until childCount) {
            val childView: View = parent.getChildAt(i)
            val behavior: CoordinatorLayout.Behavior<AppBarLayout> =
                (childView.layoutParams as CoordinatorLayout.LayoutParams).behavior as CoordinatorLayout.Behavior<AppBarLayout>
            if (behavior is AppBarLayout.ScrollingViewBehavior) {
                mScrollingViewBehaviorView = childView
            }
        }
        return onLayoutChild
    }
}
