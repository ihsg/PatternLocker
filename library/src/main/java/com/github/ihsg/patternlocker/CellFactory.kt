package com.github.ihsg.patternlocker

/**
 * Created by hsg on 20/09/2017.
 */

internal class CellFactory(private val width: Int, private val height: Int) {
    val TAG = "CellFactory"
    val cellBeanList: List<CellBean> by lazy {
        val result = ArrayList<CellBean>()
        result.clear()

        val pWidth = this.width / 8f
        val pHeight = this.height / 8f

        for (i: Int in 0..2) {
            Logger.d(TAG, "i = $i")
            for (j: Int in 0..2) {
                Logger.d(TAG, "j = $j")
                val id = (i * 3 + j) % 9
                val x = (j * 3 + 1) * pWidth
                val y = (i * 3 + 1) * pHeight
                result.add(CellBean(id, x, y, pWidth))
            }
        }
        Logger.d(TAG, "result = $result")
        return@lazy result
    }
}