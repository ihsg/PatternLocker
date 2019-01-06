package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultLockerLinkedLineView : ILockerLinkedLineView {
    @ColorInt
    private var normalColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0
    private var lineWidth: Float = 0f
    private val paint: Paint by lazy {
        Config.createPaint()
    }

    init {
        this.paint.style = Paint.Style.STROKE
    }

    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(normalColor: Int): DefaultLockerLinkedLineView {
        this.normalColor = normalColor
        return this
    }

    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(errorColor: Int): DefaultLockerLinkedLineView {
        this.errorColor = errorColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): DefaultLockerLinkedLineView {
        this.lineWidth = lineWidth
        return this
    }

    override fun draw(canvas: Canvas, hitIndexList: List<Int>, cellBeanList: List<CellBean>, endX: Float, endY: Float, isError: Boolean) {
        if (hitIndexList.isEmpty() || cellBeanList.isEmpty()) {
            return
        }

        val saveCount = canvas.save()
        val path = Path()
        var first = true

        hitIndexList.forEach {
            if (0 <= it && it < cellBeanList.size) {
                val c = cellBeanList[it]
                if (first) {
                    path.moveTo(c.x, c.y)
                    first = false
                } else {
                    path.lineTo(c.x, c.y)
                }
            }
        }

        if ((endX != 0f || endY != 0f) && hitIndexList.size < 9) {
            path.lineTo(endX, endY)
        }

        this.paint.color = this.getColor(isError)
        this.paint.strokeWidth = this.getLineWidth()
        canvas.drawPath(path, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.getErrorColor() else this.getNormalColor()
    }
}
