package com.github.ihsg.patternlocker

import android.graphics.Canvas

/**
 * Created by hsg on 22/02/2018.
 */

interface IHitCellView {
    /**
     * 绘制已设置的每个图案的样式
     *
     * @param canvas
     * @param cellBean
     * @param isError
     */
    fun draw(canvas: Canvas, cellBean: CellBean, isError: Boolean)
}
