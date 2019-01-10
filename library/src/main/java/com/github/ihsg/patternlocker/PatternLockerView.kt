package com.github.ihsg.patternlocker

import android.content.Context
import android.graphics.Canvas
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View


/**
 * Created by hsg on 20/09/2017.
 */

class PatternLockerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    @ColorInt
    private var normalColor: Int = 0
    @ColorInt
    private var hitColor: Int = 0
    @ColorInt
    private var errorColor: Int = 0
    @ColorInt
    private var fillColor: Int = 0
    private var lineWidth: Float = 0f
    private var endX: Float = 0f
    private var endY: Float = 0f
    private var hitSize: Int = 0
    private var isError: Boolean = false
    private var enableAutoClean: Boolean = false
    private var canSkip: Boolean = false
    private var enableHapticFeedback: Boolean = false
    private val cellBeanList: List<CellBean> by lazy {
        this.paddingStart
        val w = this.width - this.paddingLeft - this.paddingRight
        val h = this.height - this.paddingTop - this.paddingBottom
        CellFactory(w, h).cellBeanList
    }
    private val hitIndexList: MutableList<Int> by lazy {
        mutableListOf<Int>()
    }
    private var listener: OnPatternChangeListener? = null
    private var linkedLineView: ILockerLinkedLineView? = null
    private var normalCellView: INormalCellView? = null
    private var hitCellView: IHitCellView? = null

    private val action = Runnable {
        isEnabled = true
        clearHitState()
    }

    init {
        this.init(context, attrs, defStyleAttr)
    }

    fun enableDebug() {
        Logger.enable = true
    }

    fun setOnPatternChangedListener(listener: OnPatternChangeListener?) {
        this.listener = listener
    }

    fun updateStatus(isError: Boolean) {
        this.isError = isError
        postInvalidate()
    }

    fun clearHitState() {
        clearHitData()
        this.isError = false
        if (this.listener != null) {
            this.listener!!.onClear(this)
        }

        postInvalidate()
    }

    @ColorInt
    fun getNormalColor(): Int {
        return normalColor
    }

    fun setNormalColor(@ColorInt normalColor: Int): PatternLockerView {
        this.normalColor = normalColor
        return this
    }

    @ColorInt
    fun getHitColor(): Int {
        return hitColor
    }

    fun setHitColor(@ColorInt hitColor: Int): PatternLockerView {
        this.hitColor = hitColor
        return this
    }

    @ColorInt
    fun getErrorColor(): Int {
        return errorColor
    }

    fun setErrorColor(@ColorInt errorColor: Int): PatternLockerView {
        this.errorColor = errorColor
        return this
    }

    @ColorInt
    fun getFillColor(): Int {
        return fillColor
    }

    fun setFillColor(@ColorInt fillColor: Int): PatternLockerView {
        this.fillColor = fillColor
        return this
    }

    fun getLineWidth(): Float {
        return lineWidth
    }

    fun setLineWidth(lineWidth: Float): PatternLockerView {
        this.lineWidth = lineWidth
        return this
    }

    fun getLinkedLineView(): ILockerLinkedLineView? {
        return linkedLineView
    }

    fun setLinkedLineView(linkedLineView: ILockerLinkedLineView): PatternLockerView {
        this.linkedLineView = linkedLineView
        return this
    }

    fun getNormalCellView(): INormalCellView? {
        return normalCellView
    }

    fun setNormalCellView(normalCellView: INormalCellView): PatternLockerView {
        this.normalCellView = normalCellView
        return this
    }

    fun getHitCellView(): IHitCellView? {
        return hitCellView
    }

    fun setHitCellView(hitCellView: IHitCellView): PatternLockerView {
        this.hitCellView = hitCellView
        return this
    }

    fun buildWithDefaultStyle() {
        this.setNormalCellView(DefaultLockerNormalCellView()
                .setNormalColor(this.getNormalColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setHitCellView(DefaultLockerHitCellView()
                .setHitColor(this.getHitColor())
                .setErrorColor(this.getErrorColor())
                .setFillColor(this.getFillColor())
                .setLineWidth(this.getLineWidth())
        ).setLinkedLineView(DefaultLockerLinkedLineView()
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val a = Math.min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(a, a)
    }

    override fun onDraw(canvas: Canvas) {
        drawLinkedLine(canvas)
        drawCells(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return super.onTouchEvent(event)
        }

        var isHandle = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleActionDown(event)
                isHandle = true
            }
            MotionEvent.ACTION_MOVE -> {
                handleActionMove(event)
                isHandle = true
            }
            MotionEvent.ACTION_UP -> {
                handleActionUp(event)
                isHandle = true
            }
            else -> {
            }
        }
        postInvalidate()
        return if (isHandle) true else super.onTouchEvent(event)
    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        this.initAttrs(context, attrs, defStyleAttr)
        this.initData()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0)

        this.normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, Config.defaultNormalColor)
        this.hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, Config.defaultHitColor)
        this.errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, Config.defaultErrorColor)
        this.fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, Config.defaultFillColor)
        this.lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, Config.getDefaultLineWidth(resources))
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, Config.defaultEnableAutoClean)
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_plv_enableHapticFeedback, Config.defaultEnableHapticFeedback)
        this.canSkip = ta.getBoolean(R.styleable.PatternLockerView_plv_canSkip, Config.defaultCanSkip)

        ta.recycle()

        this.setNormalColor(this.normalColor)
        this.setHitColor(this.hitColor)
        this.setErrorColor(this.errorColor)
        this.setFillColor(this.fillColor)
        this.setLineWidth(this.lineWidth)
    }

    private fun initData() {
        this.buildWithDefaultStyle()
        this.hitIndexList.clear()
        Logger.enable = Config.defaultEnableLogger
    }

    private fun drawLinkedLine(canvas: Canvas) {
        if (!this.hitIndexList.isEmpty() && getLinkedLineView() != null) {
            getLinkedLineView()!!.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.endX,
                    this.endY,
                    this.isError)
        }
    }

    private fun drawCells(canvas: Canvas) {
        if (getHitCellView() == null) {
            Log.e(TAG, "drawCells(), hitCellView is null")
            return
        }

        if (getNormalCellView() == null) {
            Log.e(TAG, "drawCells(), normalCellView is null")
            return
        }

        this.cellBeanList.forEach {
            if (it.isHit) {
                getHitCellView()!!.draw(canvas, it, this.isError)
            } else {
                getNormalCellView()!!.draw(canvas, it)
            }
        }
    }

    private fun handleActionDown(event: MotionEvent) {
        //1. reset to default state
        clearHitData()

        //2. update hit state
        updateHitState(event)

        //3. notify listener
        this.listener?.onStart(this)
    }

    private fun handleActionMove(event: MotionEvent) {
        printLogger()
        //1. update hit state
        updateHitState(event)

        //2. update end point
        this.endX = event.x
        this.endY = event.y

        //3. notify listener if needed
        val size = this.hitIndexList.size
        if (this.hitSize != size) {
            this.hitSize = size
            this.listener?.onChange(this, this.hitIndexList)
        }
    }

    private fun handleActionUp(event: MotionEvent) {
        printLogger()

        //1. update hit state
        updateHitState(event)

        //2. update end point
        this.endX = 0f
        this.endY = 0f

        //3. notify listener
        this.listener?.onComplete(this, this.hitIndexList)


        //4. startTimer if needed
        if (this.enableAutoClean && this.hitIndexList.size > 0) {
            startTimer()
        }
    }

    private fun updateHitState(event: MotionEvent) {
        this.cellBeanList.forEach {
            if (!it.isHit && it.of(event.x, event.y, this.canSkip)) {
                it.isHit = true
                this.hitIndexList.add(it.id)
                this.hapticFeedback()
            }
        }
    }

    private fun clearHitData() {
        if (!this.hitIndexList.isEmpty()) {
            this.hitIndexList.clear()
            this.hitSize = 0
            this.cellBeanList.forEach { it.isHit = false }
        }
    }

    override fun onDetachedFromWindow() {
        this.setOnPatternChangedListener(null)
        this.removeCallbacks(this.action)
        super.onDetachedFromWindow()
    }

    private fun startTimer() {
        isEnabled = false
        this.postDelayed(this.action, Config.defaultDelayTime.toLong())
    }

    private fun hapticFeedback() {
        if (this.enableHapticFeedback) {
            this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING
                            or HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
        }
    }

    private fun printLogger() {
        if (Logger.enable) {
            Logger.d(TAG, "cellBeanList = ${this.cellBeanList}, hitIndexList = ${this.hitIndexList}")
        }

    }

    companion object {
        private const val TAG = "PatternLockerView"
    }
}