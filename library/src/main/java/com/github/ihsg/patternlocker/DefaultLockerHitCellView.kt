package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

open class DefaultLockerHitCellView(val styleDecorator: DefaultStyleDecorator) : IHitCellView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.FILL
    }

    override fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean) {
        val saveCount = canvas.save()

        // draw outer circle
        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, cellBean.radius, this.paint)

        // draw fill circle
        this.paint.color = this.styleDecorator.fillColor
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, cellBean.radius - this.styleDecorator.lineWidth, this.paint)

        // draw inner circle
        this.paint.color = this.getColor(isError)
        canvas.drawCircle(cellBean.centerX, cellBean.centerY, cellBean.radius / 5f, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
    }
}
