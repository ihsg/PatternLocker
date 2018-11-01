package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultIndicatorHitCellView : IHitCellView {
    @ColorInt
    private var normalColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0
    private val paint: Paint by lazy {
        Config.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    @ColorInt
    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(@ColorInt normalColor: Int): DefaultIndicatorHitCellView {
        this.normalColor = normalColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): DefaultIndicatorHitCellView {
        this.errorColor = errorColor
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.getErrorColor() else this.getNormalColor()
    }
}