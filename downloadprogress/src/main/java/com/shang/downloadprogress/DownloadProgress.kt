package com.shang.downloadprogress

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * 可以定義在attrs的變數，但只是練習所以不寫
 * borderWidth
 * circle color
 * progress color
 * progress space color
 * progress second space color
 * space angle
 * text color
 * text size
 * download image
 * pause image
 * success image
 */
class DownloadProgress @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    sealed class State {
        object Success : State()
        object Pause : State()
        class Progress(val progress: Int) : State()
    }

    private val borderWidth = 4.dp(context).toFloat()
    private val angleSpace = 25f

    private val circlePaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private val progressBlackSpacePaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }

    private val progressPaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = borderWidth
        shader = LinearGradient(
            0f,
            0f,
            0f,
            0f,
            ContextCompat.getColor(context, R.color.gray_BEBEBE),
            ContextCompat.getColor(context, R.color.black_4F4F4F),
            Shader.TileMode.MIRROR
        )
    }

    private val progressGraySpacePaint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeWidth = borderWidth
        color = ContextCompat.getColor(context, R.color.black_4F4F4F)
    }

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL
        textSize = 30f
        color = Color.WHITE
        isAntiAlias = true
    }

    private val downloadImage by lazy {
        ContextCompat.getDrawable(context, R.drawable.icon_download)
    }

    private val pauseImage by lazy {
        ContextCompat.getDrawable(context, R.drawable.icon_pause)
    }

    private val successImage by lazy {
        ContextCompat.getDrawable(context, R.drawable.icon_success)
    }

    private var progressRectF = RectF()
    private var textBound = Rect()
    private var state: State = State.Pause

    fun setState(newState: State) {
        state = newState
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        progressRectF = RectF(
            0f + borderWidth / 2,
            0f + borderWidth / 2,
            width.toFloat() - borderWidth / 2,
            height.toFloat() - borderWidth / 2
        )
        initImagePosition()
    }

    private fun initImagePosition() {
        val top = (height * 0.172).toInt()
        val bottom = (height * 0.568).toInt()
        val left = (width * 0.31).toInt()
        val right = (width * 0.706).toInt()
        downloadImage?.setBounds(left, top, right, bottom)
        pauseImage?.setBounds(left, top, right, bottom)
        successImage?.setBounds(left, top, right, bottom)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackgroundCircle(canvas)
        when (state) {
            State.Pause -> {
                pauseImage?.draw(canvas)
                drawText(canvas, "暫停", textPaint)
            }
            is State.Progress -> {
                val progress = (state as State.Progress?)?.progress ?: 0
                drawGrayProgressSpace(canvas)
                drawProgress(canvas, progress)
                drawBlackProgressSpace(canvas)
                downloadImage?.draw(canvas)
                drawText(canvas, "${progress}%", textPaint)
            }
            State.Success -> {
                successImage?.draw(canvas)
                drawText(canvas, "已完成", textPaint)
            }
        }
    }

    private fun drawBackgroundCircle(canvas: Canvas?) {
        canvas?.drawCircle(width / 2f, height / 2f, width / 2f, circlePaint)
    }

    private fun drawGrayProgressSpace(canvas: Canvas?) {
        for (i in -90..270 step 30) {
            canvas?.drawArc(
                progressRectF,
                i.toFloat(),
                angleSpace,
                false,
                progressGraySpacePaint
            )
        }
    }

    private fun drawBlackProgressSpace(canvas: Canvas?) {
        for (i in -90..270 step 30) {
            canvas?.drawArc(
                progressRectF,
                i.toFloat() - 5f,
                5f,
                false,
                progressBlackSpacePaint
            )
        }
    }

    private fun drawProgress(canvas: Canvas?, progress: Int) {
        canvas?.drawArc(
            progressRectF,
            -90f,
            progress * 3.6f,
            false,
            progressPaint
        )
    }

    private fun drawText(canvas: Canvas?, text: String, paint: Paint) {
        paint.getTextBounds(text, 0, text.length, textBound)
        val x = width / 2f - textBound.exactCenterX()
        val y = height * 0.724f - textBound.exactCenterY()
        canvas?.drawText(text, x, y, paint)
    }

    fun Int.dp(context: Context): Int = (this * context.resources.displayMetrics.density).toInt()
}