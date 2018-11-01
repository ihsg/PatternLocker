package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultLockerNormalCellView : INormalCellView {
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

    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(normalColor: Int): DefaultLockerNormalCellView {
        this.normalColor = normalColor
        return this
    }

    fun getFillColor(): Int {
        return fillColor
    }

    fun setFillColor(fillColor: Int): DefaultLockerNormalCellView {
        this.fillColor = fillColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): DefaultLockerNormalCellView {
        this.lineWidth = lineWidth
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean) {
        val saveCount = canvas.save()

        // draw outer circle
        this.paint.color = this.getNormalColor()
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        // draw fill circle
        this.paint.color = this.getFillColor()
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint)

        canvas.restoreToCount(saveCount)
    }
}
