package com.github.ihsg.patternlocker

/**
 * Created by hsg on 20/09/2017.
 */

class CellBean(val id: Int, val x: Float, val y: Float, val radius: Float) {
    var isHit: Boolean = false

    /**
     * 是否触碰到该view
     *
     * @param x
     * @param y
     * @return
     */
    fun of(x: Float, y: Float): Boolean {
        val dx = this.x - x
        val dy = this.y - y
        return Math.sqrt((dx * dx + dy * dy).toDouble()) <= this.radius
    }
}