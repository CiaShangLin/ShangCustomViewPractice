package com.shang.levelprogress

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.cos
import kotlin.math.sin

class LevelProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    companion object {
        data class Level(val text: String, val angle: Double)

        val data = listOf<Level>(
            Level("LV.0", 0.0),
            Level("LV.1", 30.0),
            Level("LV.3", 60.0),
            Level("LV.5", 90.0),
            Level("LV.7", 120.0),
            Level("LV.9", 150.0),
            Level("LV.★", 180.0),
        )
    }


    private var textBound = Rect()
    private var indicatorBorderRect = RectF()

    private val borderWidth = 16.dp(context).toFloat()
    private val textMarginWidth = 16.dp(context) //左右兼具
    private val textMarginTop = 11.dp(context)

    private val mStar by lazy { ContextCompat.getDrawable(context, R.drawable.icon_vip_dot) }
    private val mStarSize by lazy { 28.dp(context) }

    private val mBackgroundPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, R.color.black)
        }
    }

    private val mCoverPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = ContextCompat.getColor(context, R.color.black)
        }
    }

    private val mProgressBackgroundPaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            color = ContextCompat.getColor(context, R.color.gray_d8d8d8)
            isAntiAlias = true
        }
    }

    private val mProgressPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
            shader = LinearGradient(
                0f,
                0f,
                0f,
                0f,
                ContextCompat.getColor(context, R.color.yellow_d7c48a),
                ContextCompat.getColor(context, R.color.yellow_ae9151),
                Shader.TileMode.MIRROR
            )
        }
    }

    private val mDefaultTextPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = 12.dp(context).toFloat()
            color = ContextCompat.getColor(context, R.color.white)
        }
    }

    private val mSelectTextPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
            textSize = 12.dp(context).toFloat()
            color = ContextCompat.getColor(context, R.color.gray_cfbd86)
        }
    }

    private var mLevel = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mDefaultTextPaint.getTextBounds("LV.1", 0, 4, textBound)
        indicatorBorderRect.set(
            borderWidth / 2 + textBound.width() + textMarginWidth,
            borderWidth / 2 + textBound.height() + textMarginTop,
            width - borderWidth / 2 - textBound.width() - textMarginWidth,
            height * 2f
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawProgress(canvas)
        drawText(canvas)
        drawStar(canvas)
    }

    private fun drawProgress(canvas: Canvas?) {
        //背景
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mBackgroundPaint)

        //灰色半圓
        canvas?.drawArc(
            indicatorBorderRect,
            180f,
            180f,
            false,
            mProgressBackgroundPaint
        )

        //進度條
        canvas?.drawArc(
            indicatorBorderRect,
            180f,
            data[mLevel].angle.toFloat(),
            false,
            mProgressPaint
        )

        //黑底(左)
        canvas?.drawArc(
            indicatorBorderRect,
            180f,
            5f,
            false,
            mCoverPaint
        )

        //黑底(右)
        canvas?.drawArc(
            indicatorBorderRect,
            355f,
            175f,
            false,
            mCoverPaint
        )
    }

    private fun drawText(canvas: Canvas?) {
        data.forEachIndexed { index, d ->
            val r = width / 2f
            //LV0和LV★偏移
            val radians = when (index) {
                0 -> {
                    Math.toRadians(d.angle + 2)
                }
                data.size - 1 -> {
                    Math.toRadians(d.angle - 2)
                }
                else -> {
                    Math.toRadians(d.angle)
                }
            }

            val x =
                (r - (r - textBound.width() / 2f - textMarginWidth / 2f) * cos(radians)).toFloat()
            val y =
                (indicatorBorderRect.bottom / 2 - (indicatorBorderRect.bottom / 2 - textMarginTop - textBound.height()/2f) * sin(
                    radians
                )).toFloat()
            if (data[mLevel].angle >= d.angle) {
                canvas?.drawText(d.text, x, y, mSelectTextPaint)
            } else {
                canvas?.drawText(d.text, x, y, mDefaultTextPaint)
            }
        }
    }

    private fun drawStar(canvas: Canvas?) {
        mStar?.let {
            val level = data[mLevel]
            val r = width / 2f
            val radians = Math.toRadians(level.angle)
            val x =
                (r - (r - borderWidth / 2 - textBound.width() - textMarginWidth) * cos(radians)).toFloat()
            val y = (height - (height - mStarSize - textMarginTop- textBound.height()/2f) * sin(radians)).toFloat()

            var margin = 4.dp(context)
            if (level.angle < 90.0) {
                margin *= -1
            }
            it.setBounds(
                (x - mStarSize / 2 - margin).toInt(),
                (y - mStarSize).toInt(),
                (x + mStarSize / 2 - margin).toInt(),
                (y).toInt()
            )
            it.draw(canvas!!)
        }
    }

    fun setLevel(level: Int) {
        mLevel = level
        invalidate()
    }

    fun Int.dp(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
}