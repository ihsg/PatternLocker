package com.github.ihsg.patternlocker;

/**
 * Created by hsg on 20/09/2017.
 */

public class CellBean {

    public int id;
    public float x;
    public float y;
    public float radius;

    public boolean isHit;

    public CellBean(int id, float x, float y, float radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
     * 是否触碰到该view
     *
     * @param x
     * @param y
     * @return
     */
    public boolean of(float x, float y) {
        final float dx = this.x - x;
        final float dy = this.y - y;
        return Math.sqrt(dx * dx + dy * dy) <= this.radius;
    }
}