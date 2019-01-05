package com.github.ihsg.patternlocker

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.annotation.ColorInt

/**
 * Created by hsg on 22/02/2018.
 */

class DefaultIndicatorLinkedLineView : IIndicatorLinkedLineView {
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

    @ColorInt
    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(@ColorInt normalColor: Int): DefaultIndicatorLinkedLineView {
        this.normalColor = normalColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): DefaultIndicatorLinkedLineView {
        this.errorColor = errorColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): DefaultIndicatorLinkedLineView {
        this.lineWidth = lineWidth
        return this
    }

    override fun draw(canvas: Canvas, hitIndexList: List<Int>, cellBeanList: List<CellBean>, isError: Boolean) {
        if (hitIndexList.isEmpty() || cellBeanList.isEmpty()) {
            return
        }

        val saveCount = canvas.save()

        val first = cellBeanList[hitIndexList[0]]

        val path = Path()
        path.moveTo(first.x, first.y)

        hitIndexList.forEach {
            if(0 <= it && it < cellBeanList.size){
                val c = cellBeanList[it]
                path.lineTo(c.x, c.y)
            }
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
