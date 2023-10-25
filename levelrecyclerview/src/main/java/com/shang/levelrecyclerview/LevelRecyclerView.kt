package com.shang.levelrecyclerview

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.max

class LevelRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val Float.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            Resources.getSystem().displayMetrics
        )
    val Int.dp: Int get() = toFloat().dp.toInt()

    private var mLevelDecoration: LevelDecoration


    private val mPagerSnapHelper by lazy {
        PagerSnapHelper()
    }
    private val mLinearLayoutManager by lazy {
        LinearLayoutManager(context, HORIZONTAL, false)
    }

    private var mMaxFactor = 0.45f
    private var mOnLevelChangeListener: OnLevelChangeListener? = null

    //當前位置
    private var mLastPosition = 0


    interface OnLevelChangeListener {
        fun onLevelChange(position: Int)
    }

    inner class LevelOnScrollListener : OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == SCROLL_STATE_IDLE) {
                changeSnapView()
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val first = mLinearLayoutManager.findFirstVisibleItemPosition()
            val last = mLinearLayoutManager.findLastVisibleItemPosition()
            val parentCenter = recyclerView.width / 2F
            for (i in first..last) {
                setItemTransform(i, parentCenter)
            }
            changeSnapView()
        }

        private fun setItemTransform(
            position: Int,
            parentCenter: Float
        ) {
            mLinearLayoutManager.findViewByPosition(position)?.run {
                val factor = calculationViewFactor(left.toFloat(), width.toFloat(), parentCenter)
                val scale = 1 + factor
                scaleX = scale
                scaleY = scale
                alpha = 1 - mMaxFactor + factor
            }
        }

        /**
         * 计算当前item的缩放与透明度系数
         * item的中心离recyclerView的中心越远，系数越小（负相关）
         */
        private fun calculationViewFactor(left: Float, width: Float, parentCenter: Float): Float {
            val viewCenter = left + width / 2
            val distance = abs(viewCenter - parentCenter) / width
            return max(0F, (1F - distance) * mMaxFactor)
        }

        /**
         * 修改当前居中的item，把当前等级回调给外界
         */
        private fun changeSnapView() {
            mPagerSnapHelper.findSnapView(mLinearLayoutManager)?.let {
                mLinearLayoutManager.getPosition(it).let { position ->
                    if (mLastPosition != position) {
                        mLastPosition = position
                        mOnLevelChangeListener?.onLevelChange(position)
                    }
                }
            }
        }
    }

    /**
     * 簡單來說就是計算view離中心的點距離是多少
     * boxStart + (boxEnd - boxStart) / 2 => RecyclerView的中心點
     * (viewStart + (viewEnd - viewStart) / 2) => View的中心點
     * 等價於view.left + view.width/2,不過沒有view的width所以用viewEnd-viewStart = view.width
     * 不能用scrollBy()因為PageSnapHelper會失效
     */
    class CustomSmoothScroller(context: Context) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int, viewEnd: Int, boxStart: Int,
            boxEnd: Int, snapPreference: Int
        ): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return super.calculateSpeedPerPixel(displayMetrics) * 3F
        }
    }


    init {
        context.obtainStyledAttributes(attrs, R.styleable.LevelRecyclerView).use {
            val dividerWidth =
                it.getDimension(R.styleable.LevelRecyclerView_dividerWidth, 20f.dp).toInt()
            val dividerHeight =
                it.getDimension(R.styleable.LevelRecyclerView_dividerHeight, 5f.dp).toInt()
            val dividerDrawable = it.getDrawable(R.styleable.LevelRecyclerView_divider)
                ?: ColorDrawable(Color.parseColor("#3A3A3C"))

            mMaxFactor = it.getFloat(R.styleable.LevelRecyclerView_maxFactor, 0.45f)
            mLevelDecoration = LevelDecoration(
                dividerWidth,
                dividerHeight,
                dividerDrawable
            )
        }

        layoutManager = mLinearLayoutManager
        mPagerSnapHelper.attachToRecyclerView(this)
        addOnScrollListener(LevelOnScrollListener())
        addItemDecoration(mLevelDecoration)
        post {
            //設置好之後要刷新一次Decoration
            invalidateItemDecorations()
        }
    }

    fun setOnLevelChangeListener(listener: OnLevelChangeListener) {
        mOnLevelChangeListener = listener
    }


    override fun smoothScrollToPosition(position: Int) {
        if (position == mLastPosition) return
        if (position < 0 || position >= (adapter?.itemCount ?: 0)) return

        mLinearLayoutManager.startSmoothScroll(
            CustomSmoothScroller(context).apply {
                targetPosition = position
            }
        )
    }


}