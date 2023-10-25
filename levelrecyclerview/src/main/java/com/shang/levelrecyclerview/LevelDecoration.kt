package com.shang.levelrecyclerview

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @param mDividerWidth 分割線的寬,如果太短可能會導致無法滾動到中間
 * @param mDividerHeight 分割線的高
 * @param mDividerDrawable 分割線Drawable可用Shape之類的替換
 */
class LevelDecoration(
    private val mDividerWidth: Int,
    private val mDividerHeight: Int,
    private val mDividerDrawable: Drawable
) : RecyclerView.ItemDecoration() {

    /**
     * 對於第一個和最後一個的左右撐開,這樣就可以置中顯示
     * 剩下的就是Divider的寬度
     */
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0
        when (position) {
            0 -> {
                outRect.left = parent.width / 2 - view.width / 2
                outRect.right = mDividerWidth
            }
            itemCount - 1 -> {
                outRect.left = mDividerWidth
                outRect.right = parent.width / 2 - view.width / 2
            }
            else -> {
                outRect.left = mDividerWidth
                outRect.right = mDividerWidth
            }
        }
    }

    /**
     * 繪製分割線,其實從頭直接畫到尾巴就好
     * 因為onDraw不會蓋到原先的view
     * 不用向原作者那樣計算bound
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)

        c.save()
        val top = parent.height / 2 - mDividerHeight / 2
        val bottom = parent.height / 2 + mDividerHeight / 2
        mDividerDrawable.setBounds(
            0,
            top,
            parent.width,
            bottom
        )
        mDividerDrawable.draw(c)
        c.restore()
    }

}

