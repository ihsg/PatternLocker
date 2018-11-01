package com.github.ihsg.patternlocker

import android.content.Context
import android.graphics.Canvas
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by hsg on 20/09/2017.
 */

class PatternIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    @ColorInt
    private var normalColor: Int = 0
    @ColorInt
    private var fillColor: Int = 0
    @ColorInt
    private var hitColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0

    private var lineWidth: Float = 0f

    private var isError: Boolean = false
    private val hitIndexList: ArrayList<Int> by lazy {
        ArrayList<Int>()
    }
    private val cellBeanList: ArrayList<CellBean> by lazy {
        val w = width - paddingLeft - paddingRight
        val h = height - paddingTop - paddingBottom
        CellFactory(w, h).cellBeanList
    }

    private var linkedLineView: IIndicatorLinkedLineView? = null
    private var normalCellView: INormalCellView? = null
    private var hitCellView: IHitCellView? = null

    init {
        init(context, attrs, defStyleAttr)
    }

    @ColorInt
    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(@ColorInt normalColor: Int): PatternIndicatorView {
        this.normalColor = normalColor
        return this
    }

    @ColorInt
    fun getFillColor(): Int {
        return fillColor
    }

    fun setFillColor(@ColorInt fillColor: Int): PatternIndicatorView {
        this.fillColor = fillColor
        return this
    }

    @ColorInt
    fun getHitColor(): Int {
        return hitColor
    }

    fun setHitColor(@ColorInt hitColor: Int): PatternIndicatorView {
        this.hitColor = hitColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): PatternIndicatorView {
        this.errorColor = errorColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): PatternIndicatorView {
        this.lineWidth = lineWidth
        return this
    }

    fun getLinkedLineView(): IIndicatorLinkedLineView? {
        return linkedLineView
    }

    fun setLinkedLineView(linkedLineView: IIndicatorLinkedLineView): PatternIndicatorView {
        this.linkedLineView = linkedLineView
        return this
    }

    fun getNormalCellView(): INormalCellView? {
        return normalCellView
    }

    fun setNormalCellView(normalCellView: INormalCellView): PatternIndicatorView {
        this.normalCellView = normalCellView
        return this
    }

    fun getHitCellView(): IHitCellView? {
        return hitCellView
    }

    fun setHitCellView(hitCellView: IHitCellView): PatternIndicatorView {
        this.hitCellView = hitCellView
        return this
    }

    fun buildWithDefaultStyle() {
        this.setNormalCellView(DefaultIndicatorNormalCellView()
                .setNormalColor(this.getNormalColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setHitCellView(DefaultIndicatorHitCellView()
                .setErrorColor(this.getErrorColor())
                .setNormalColor(this.getHitColor())
        ).setLinkedLineView(DefaultIndicatorLinkedLineView()
                .setNormalColor(this.getHitColor())
                .setErrorColor(this.getErrorColor())
                .setLineWidth(this.getLineWidth())
        ).build()
    }

    fun build() {
        if (getNormalCellView() == null) {
            Log.e(TAG, "in build() function, normalCellView is null")
            return
        }

        if (getHitCellView() == null) {
            Log.e(TAG, "in build() function, hitCellView is null")
            return
        }

        if (getLinkedLineView() == null) {
            Log.w(TAG, "in build() function, linkedLineView is null")
        }
        postInvalidate()
    }

    fun updateState(hitIndexList: List<Int>?, isError: Boolean) {
        //1. reset to default state
        if (!this.hitIndexList.isEmpty()) {
            for (i in this.hitIndexList) {
                this.cellBeanList[i].isHit = false
            }
            this.hitIndexList.clear()
        }

        //2. update hit state
        if (hitIndexList != null) {
            this.hitIndexList.addAll(hitIndexList)
        }

        if (!this.hitIndexList.isEmpty()) {
            for (i in this.hitIndexList) {
                this.cellBeanList[i].isHit = true
            }
        }

        //3. update result
        this.isError = isError

        //4. update view
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val a = Math.min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(a, a)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawLinkedLine(canvas)
        drawCells(canvas)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.initAttrs(context, attrs, defStyleAttr)
        this.initData()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0)

        this.normalColor = ta.getColor(R.styleable.PatternIndicatorView_piv_color, Config.defaultNormalColor)
        this.fillColor = ta.getColor(R.styleable.PatternIndicatorView_piv_fillColor, Config.defaultFillColor)
        this.hitColor = ta.getColor(R.styleable.PatternIndicatorView_piv_hitColor, Config.defaultHitColor)
        this.errorColor = ta.getColor(R.styleable.PatternIndicatorView_piv_errorColor, Config.defaultErrorColor)
        this.lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_piv_lineWidth, Config.getDefaultLineWidth(resources))

        ta.recycle()

        this.setNormalColor(this.normalColor)
        this.setFillColor(this.fillColor)
        this.setHitColor(this.hitColor)
        this.setErrorColor(this.errorColor)
        this.setLineWidth(this.lineWidth)
    }

    private fun initData() {
        this.buildWithDefaultStyle()
    }

    private fun drawLinkedLine(canvas: Canvas) {
        if (!this.hitIndexList.isEmpty() && this.getLinkedLineView() != null) {
            this.getLinkedLineView()!!.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.isError)
        }
    }

    private fun drawCells(canvas: Canvas) {
        if (this.getHitCellView() == null) {
            Log.e(TAG, "drawCells(), hitCellView is null")
            return
        }

        if (this.getNormalCellView() == null) {
            Log.e(TAG, "drawCells(), normalCellView is null")
            return
        }

        for (item in this.cellBeanList) {
            if (item.isHit) {
                this.getHitCellView()!!.draw(canvas, item, this.isError)
            } else {
                this.getNormalCellView()!!.draw(canvas, item)
            }
        }
    }

    companion object {
        private const val TAG = "PatternIndicatorView"
    }
}