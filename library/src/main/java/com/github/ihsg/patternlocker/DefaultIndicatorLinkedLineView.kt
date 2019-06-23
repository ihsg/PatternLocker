package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultIndicatorLinkedLineView(val styleDecorator: DefaultStyleDecorator) : IIndicatorLinkedLineView {

    private val paint: Paint by lazy {
        DefaultConfig.createPaint()
    }

    init {
        this.paint.style = Paint.Style.STROKE
    }

    override fun draw(canvas: Canvas, hitIndexList: List<Int>, cellBeanList: List<CellBean>, isError: Boolean) {
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
