package com.github.ihsg.patternlocker

import android.graphics.Canvas

/**
 * Created by hsg on 22/02/2018.
 */

interface INormalCellView {
    /**
     * 绘制正常情况下（即未设置的）每个图案的样式
     *
     * @param canvas
     * @param cellBean the target cell view
     */
    fun draw(canvas: Canvas, cellBean: CellBean)
}