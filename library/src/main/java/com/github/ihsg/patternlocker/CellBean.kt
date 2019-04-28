package com.github.ihsg.patternlocker

/**
 * Created by hsg on 20/09/2017.
 *
 * @param id each cell id order like this:
 *
 * 0 1 2
 * 3 4 5
 * 6 7 8
 *
 * @param x
 * @param y
 * @param radius
 * @param isHit
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
    fun of(x: Float, y: Float, canSkip: Boolean): Boolean {
        val dx = this.x - x
        val dy = this.y - y
        val r = if (canSkip) this.radius else this.radius * 1.5f
        return Math.sqrt((dx * dx + dy * dy).toDouble()) <= r.toDouble()
    }

    override fun toString(): String {
        return "CellBean(id=$id, x=$x, y=$y, radius=$radius, isHit=$isHit)"
    }
}