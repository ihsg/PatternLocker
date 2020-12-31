package com.github.ihsg.patternlocker

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.min


/**
 * Created by hsg on 20/09/2017.
 */

open class PatternLockerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    companion object {
        private const val TAG = "PatternLockerView"
    }

    /**
     * 绘制完后是否自动清除标志位，如果开启了该标志位，延时@freezeDuration毫秒后自动清除已绘制图案
     */
    var enableAutoClean: Boolean = false

    /**
     * 能否跳过中间点标志位，如果开启了该标志，则可以不用连续
     */
    var enableSkip: Boolean = false

    /**
     * 是否开启触碰反馈，如果开启了该标志，则每连接一个cell则会震动
     */
    var enableHapticFeedback: Boolean = false

    /**
     * 绘制完成后多久可以清除（单位ms），只有在@enableAutoClean = true 时有效
     */
    var freezeDuration: Int = 0

    /**
     * 绘制连接线
     */
    var linkedLineView: ILockerLinkedLineView? = null

    /**
     * 绘制未操作时的cell样式
     */
    var normalCellView: INormalCellView? = null

    /**
     * 绘制操作时的cell样式
     */
    var hitCellView: IHitCellView? = null

    /**
     * 是否是错误的图案
     */
    private var isError: Boolean = false

    /**
     * 终点x坐标
     */
    private var endX: Float = 0f

    /**
     * 终点y坐标
     */
    private var endY: Float = 0f

    /**
     * 记录绘制多少个cell，用于判断是否调用OnPatternChangeListener
     */
    private var hitSize: Int = 0

    /**
     * 真正的cell数组
     */
    private lateinit var cellBeanList: List<CellBean>

    /**
     * 记录已绘制cell的id
     */
    private val hitIndexList: MutableList<Int> by lazy {
        mutableListOf<Int>()
    }

    /**
     * 监听器
     */
    private var listener: OnPatternChangeListener? = null

    init {
        this.initAttrs(context, attrs, defStyleAttr)
        this.initData()
    }

    fun enableDebug() {
        Logger.enable = true
    }

    fun setOnPatternChangedListener(listener: OnPatternChangeListener?) {
        this.listener = listener
    }

    /**
     * 更改状态
     */
    fun updateStatus(isError: Boolean) {
        this.isError = isError
        invalidate()
    }

    /**
     * 清除已绘制图案
     */
    fun clearHitState() {
        this.clearHitData()
        this.isError = false
        this.listener?.onClear(this)
        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val a = min(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(a, a)
    }

    override fun onDraw(canvas: Canvas) {
        this.initCellBeanList()
        this.drawLinkedLine(canvas)
        this.drawCells(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return super.onTouchEvent(event)
        }

        var isHandle = false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                this.handleActionDown(event)
                isHandle = true
            }
            MotionEvent.ACTION_MOVE -> {
                this.handleActionMove(event)
                isHandle = true
            }
            MotionEvent.ACTION_UP -> {
                this.handleActionUp(event)
                isHandle = true
            }
            else -> {
            }
        }
        invalidate()
        return if (isHandle) true else super.onTouchEvent(event)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0)

        val normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, DefaultConfig.defaultNormalColor)
        val hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, DefaultConfig.defaultHitColor)
        val errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, DefaultConfig.defaultErrorColor)
        val fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, DefaultConfig.defaultFillColor)
        val lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, DefaultConfig.getDefaultLineWidth(resources))

        this.freezeDuration = ta.getInteger(R.styleable.PatternLockerView_plv_freezeDuration, DefaultConfig.defaultFreezeDuration)
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, DefaultConfig.defaultEnableAutoClean)
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_plv_enableHapticFeedback, DefaultConfig.defaultEnableHapticFeedback)
        this.enableSkip = ta.getBoolean(R.styleable.PatternLockerView_plv_enableSkip, DefaultConfig.defaultEnableSkip)

        ta.recycle()

        // style
        val styleDecorator = DefaultStyleDecorator(normalColor, fillColor, hitColor, errorColor, lineWidth)
        this.normalCellView = DefaultLockerNormalCellView(styleDecorator)
        this.hitCellView = DefaultLockerHitCellView(styleDecorator)
        this.linkedLineView = DefaultLockerLinkedLineView(styleDecorator)
    }

    private fun initData() {
        Logger.enable = DefaultConfig.defaultEnableLogger
        this.hitIndexList.clear()
    }

    private fun initCellBeanList() {
        if (!this::cellBeanList.isInitialized) {
            val w = this.width - this.paddingLeft - this.paddingRight
            val h = this.height - this.paddingTop - this.paddingBottom
            this.cellBeanList = CellFactory.buildCells(w, h)
        }
    }

    private fun drawLinkedLine(canvas: Canvas) {
        if (this.hitIndexList.isNotEmpty()) {
            this.linkedLineView?.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.endX,
                    this.endY,
                    this.isError)
        }
    }

    private fun drawCells(canvas: Canvas) {
        this.cellBeanList.forEach {
            if (it.isHit && this.hitCellView != null) {
                this.hitCellView?.draw(canvas, it, this.isError)
            } else {
                this.normalCellView?.draw(canvas, it)
            }
        }
    }

    private fun handleActionDown(event: MotionEvent) {
        //1. reset to default state
        this.clearHitData()

        //2. update hit state
        this.updateHitState(event)

        //3. notify listener
        this.listener?.onStart(this)
    }

    private fun handleActionMove(event: MotionEvent) {
        printLogger()

        //1. update hit state
        this.updateHitState(event)

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
        this.printLogger()

        //1. update hit state
        this.updateHitState(event)

        //2. update end point
        this.endX = 0f
        this.endY = 0f

        //3. notify listener
        this.listener?.onComplete(this, this.hitIndexList)


        //4. startTimer if needed
        if (this.enableAutoClean && this.hitIndexList.size > 0) {
            this.startTimer()
        }
    }

    private fun updateHitState(event: MotionEvent) {
        this.cellBeanList.forEach {
            if (!it.isHit && it.of(event.x, event.y)) {
                if (!enableSkip && this.hitIndexList.isNotEmpty()) {
                    val last = this.cellBeanList[this.hitIndexList.last()]
                    val mayId = (last.id + it.id) / 2
                    if (!this.hitIndexList.contains(mayId) && (abs(last.x - it.x) % 2 == 0) && (abs(last.y - it.y) % 2 == 0)) {
                        this.cellBeanList[mayId].isHit = true
                        this.hitIndexList.add(mayId)
                    }
                }
                it.isHit = true
                this.hitIndexList.add(it.id)
                this.hapticFeedback()
            }
        }
    }

    private fun clearHitData() {
        if (this.hitIndexList.isNotEmpty()) {
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

    private val action = Runnable {
        this.isEnabled = true
        this.clearHitState()
    }

    private fun startTimer() {
        this.isEnabled = false
        this.postDelayed(this.action, this.freezeDuration.toLong())
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
}