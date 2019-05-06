package com.github.ihsg.demo.ui.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue

import com.github.ihsg.demo.R
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.DefaultIndicatorNormalCellView
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import kotlinx.android.synthetic.main.activity_default_pattern_checking.*

class SimplePatternCheckingActivity : AppCompatActivity() {

    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_pattern_checking)

        val pivStyle = (this.patternIndicatorView.normalCellView as DefaultIndicatorNormalCellView).styleDecorator
        pivStyle.normalColor = ContextCompat.getColor(this, R.color.colorWhite)
        pivStyle.fillColor = ContextCompat.getColor(this, R.color.color_blue)
        pivStyle.hitColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        pivStyle.errorColor = ContextCompat.getColor(this, R.color.color_red)
        pivStyle.lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f,
                resources.displayMetrics)

        val plvStyle = (this.patternLockerView.normalCellView as DefaultLockerNormalCellView).styleDecorator
        plvStyle.normalColor = ContextCompat.getColor(this, R.color.colorWhite)
        plvStyle.fillColor = ContextCompat.getColor(this, R.color.color_blue)
        plvStyle.hitColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        plvStyle.errorColor = ContextCompat.getColor(this, R.color.color_red)
        plvStyle.lineWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f,
                resources.displayMetrics)

        this.patternLockerView!!.setOnPatternChangedListener(object : OnPatternChangeListener {
            override fun onStart(view: PatternLockerView) {}

            override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

            override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                val isError = !isPatternOk(hitIndexList)
                view.updateStatus(isError)
                patternIndicatorView!!.updateState(hitIndexList, isError)
                updateMsg()
            }

            override fun onClear(view: PatternLockerView) {
                finishIfNeeded()
            }
        })

        this.textMsg.text = "绘制解锁图案"
        this.patternHelper = PatternHelper()
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        this.patternHelper!!.validateForChecking(hitIndexList)
        return this.patternHelper!!.isOk
    }

    private fun updateMsg() {
        this.textMsg.text = this.patternHelper!!.message
        this.textMsg!!.setTextColor(if (this.patternHelper!!.isOk)
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        else
            ContextCompat.getColor(this, R.color.color_red))
    }

    private fun finishIfNeeded() {
        if (this.patternHelper!!.isFinish) {
            finish()
        }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, SimplePatternCheckingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
