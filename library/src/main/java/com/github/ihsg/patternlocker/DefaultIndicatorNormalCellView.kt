package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultIndicatorNormalCellView : INormalCellView {
    @ColorInt
    private var normalColor: Int = 0
    @ColorInt
    private var fillColor: Int = 0
    private var lineWidth: Float = 0f
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

    fun setNormalColor(@ColorInt normalColor: Int): DefaultIndicatorNormalCellView {
        this.normalColor = normalColor
        return this
    }

    @ColorInt
    fun getFillColor(): Int {
        return fillColor
    }

    fun setFillColor(@ColorInt fillColor: Int): DefaultIndicatorNormalCellView {
        this.fillColor = fillColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): DefaultIndicatorNormalCellView {
        this.lineWidth = lineWidth
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean) {
        val saveCount = canvas.save()

        //outer circle
        this.paint.color = this.getNormalColor()
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        //inner circle
        this.paint.color = this.getFillColor()
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint)

        canvas.restoreToCount(saveCount)
    }
}