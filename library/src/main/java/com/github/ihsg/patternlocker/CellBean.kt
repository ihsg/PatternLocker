package com.github.ihsg.patternlocker

import kotlin.math.sqrt

/**
 * Created by hsg on 20/09/2017.
 *
 * @param id 表示该cell的编号，9个cell的编号如下：
 *
 * 0 1 2
 * 3 4 5
 * 6 7 8
 *
 * @param x 表示该cell的x坐标（点坐标）
 * @param y 表示该cell的y坐标（点坐标）
 * x,y 点坐标编号如下：
 * (0,0) (1,0) (2,0)
 * (0,1) (1,1) (2,1)
 * (0,2) (1,2) (2,2)
 *
 * @param centerX 表示该cell的圆心x坐标（相对坐标）
 * @param centerY 表示该cell的圆心y坐标（相对坐标）
 * centerX, centerY 圆心坐标如下：
 * (radius, radius)  (4radius, radius)  (7radius, radius)
 * (radius, 4radius) (4radius, 4radius) (7radius, 4radius)
 * (radius, 7radius) (4radius, 7radius) (7radius, 7radius)
 *
 * @param radius 表示该cell的半径
 * @param isHit 表示该cell是否被设置的标记
 */
data class CellBean(val id: Int,
                    val x: Int,
                    val y: Int,
                    val centerX: Float,
                    val centerY: Float,
                    val radius: Float,
                    var isHit: Boolean = false) {
    /**
     * 是否触碰到该view
     *
     * @param x
     * @param y
     * @return
     */
    fun of(x: Float, y: Float): Boolean {
        val dx = this.centerX - x
        val dy = this.centerY - y
        return sqrt((dx * dx + dy * dy)).toDouble() <= this.radius.toDouble()
    }
}