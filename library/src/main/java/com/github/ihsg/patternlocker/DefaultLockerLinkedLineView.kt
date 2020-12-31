package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

open class DefaultLockerLinkedLineView(val styleDecorator: DefaultStyleDecorator) : ILockerLinkedLineView {
    companion object {
        private const val TAG = "DefaultLockerLinkedLineView"
    }

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas, hitIndexList: List<Int>, cellBeanList: List<CellBean>, endX: Float, endY: Float, isError: Boolean) {
        Logger.d(TAG, "hitIndexList = $hitIndexList, cellBeanList = $cellBeanList")
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
                    path.moveTo(c.centerX, c.centerY)
                    first = false
                } else {
                    path.lineTo(c.centerX, c.centerY)
                }
            }
        }

        if ((endX != 0f || endY != 0f) && hitIndexList.size < 9) {
            path.lineTo(endX, endY)
        }

        this.paint.color = this.getColor(isError)
        this.paint.strokeWidth = this.styleDecorator.lineWidth
        canvas.drawPath(path, this.paint)

        canvas.restoreToCount(saveCount)
    }

    @ColorInt
    private fun getColor(isError: Boolean): Int {
        return if (isError) this.styleDecorator.errorColor else this.styleDecorator.hitColor
    }
}
