package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultLockerHitCellView : IHitCellView {
    @ColorInt
    private var hitColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0
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
    fun getHitColor(): Int {
        return hitColor
    }

    fun setHitColor(@ColorInt hitColor: Int): DefaultLockerHitCellView {
        this.hitColor = hitColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): DefaultLockerHitCellView {
        this.errorColor = errorColor
        return this
    }

    @ColorInt
    fun getFillColor(): Int {
        return fillColor
    }

    fun setFillColor(@ColorInt fillColor: Int): DefaultLockerHitCellView {
        this.fillColor = fillColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): DefaultLockerHitCellView {
        this.lineWidth = lineWidth
        return this
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

        // draw outer circle
        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius, this.paint)

        // draw fill circle
        this.paint.color = this.getFillColor()
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius - this.getLineWidth(), this.paint)

        // draw inner circle
        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.x, cellBean.y, cellBean.radius / 5f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.getErrorColor() else this.getHitColor()
    }
}
