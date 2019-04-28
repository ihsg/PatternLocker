package com.github.ihsg.patternlocker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by hsg on 20/09/2017.
 */

class PatternIndicatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "PatternIndicatorView"
    }

    var linkedLineView: IIndicatorLinkedLineView? = null
    var normalCellView: INormalCellView? = null
    var hitCellView: IHitCellView? = null

    private var isError: Boolean = false
    private val hitIndexList: MutableList<Int> by lazy {
        mutableListOf<Int>()
    }
    private val cellBeanList: List<CellBean> by lazy {
        val w = this.width - this.paddingLeft - this.paddingRight
        val h = this.height - this.paddingTop - this.paddingBottom
        CellFactory(w, h).cellBeanList
    }

    init {
        init(context, attrs, defStyleAttr)
    }

    fun updateState(hitIndexList: List<Int>?, isError: Boolean) {
        hitIndexList?.let {
            //1. clear pre state
            if (this.hitIndexList.isNotEmpty()) {
                this.hitIndexList.clear()
            }

            //2. record new state
            this.hitIndexList.addAll(it)

            //3. update result
            this.isError = isError

            //4. update view
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val a = Math.min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(a, a)
    }

    override fun onDraw(canvas: Canvas) {
        this.updateHitState()
        this.drawLinkedLine(canvas)
        this.drawCells(canvas)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.initAttrs(context, attrs, defStyleAttr)
        this.initData()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0)

        val normalColor = ta.getColor(R.styleable.PatternIndicatorView_piv_color, Config.defaultNormalColor)
        val fillColor = ta.getColor(R.styleable.PatternIndicatorView_piv_fillColor, Config.defaultFillColor)
        val hitColor = ta.getColor(R.styleable.PatternIndicatorView_piv_hitColor, Config.defaultHitColor)
        val errorColor = ta.getColor(R.styleable.PatternIndicatorView_piv_errorColor, Config.defaultErrorColor)
        val lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_piv_lineWidth, Config.getDefaultLineWidth(resources))

        ta.recycle()

        val decorator = DefaultStyleDecorator(normalColor, fillColor, hitColor, errorColor, lineWidth)
        this.normalCellView = DefaultIndicatorNormalCellView(decorator)
        this.hitCellView = DefaultIndicatorHitCellView(decorator)
        this.linkedLineView = DefaultIndicatorLinkedLineView(decorator)
    }

    private fun initData() {
        this.hitIndexList.clear()
    }

    private fun updateHitState() {
        //1. clear pre state
        this.cellBeanList.forEach {
            it.isHit = false
        }

        //2. update hit state
        this.hitIndexList.let { it ->
            if (it.isNotEmpty()) {
                it.forEach {
                    if (0 <= it && it < this.cellBeanList.size) {
                        this.cellBeanList[it].isHit = true
                    }
                }
            }
        }
    }

    private fun drawLinkedLine(canvas: Canvas) {
        if (this.hitIndexList.isNotEmpty()) {
            this.linkedLineView?.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.isError)
        }
    }

    private fun drawCells(canvas: Canvas) {
        if (this.hitCellView == null) {
            Log.e(TAG, "drawCells(), hitCellView is null")
            return
        }

        if (this.normalCellView == null) {
            Log.e(TAG, "drawCells(), normalCellView is null")
            return
        }

        this.cellBeanList.forEach {
            if (it.isHit) {
                this.hitCellView?.draw(canvas, it, this.isError)
            } else {
                this.normalCellView?.draw(canvas, it)
            }
        }
    }
}