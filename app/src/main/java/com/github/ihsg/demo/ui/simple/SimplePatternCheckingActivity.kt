package com.github.ihsg.demo.ui.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.ihsg.demo.R
import com.github.ihsg.demo.databinding.ActivitySimplePatternCheckingBinding
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.DefaultIndicatorNormalCellView
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView

class SimplePatternCheckingActivity : AppCompatActivity() {
    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivitySimplePatternCheckingBinding.inflate(layoutInflater).apply {
            setContentView(root)
            val context = this@SimplePatternCheckingActivity

            val pivStyle = (patternIndicatorView.normalCellView as DefaultIndicatorNormalCellView).styleDecorator
            pivStyle.normalColor = ContextCompat.getColor(context, R.color.colorWhite)
            pivStyle.fillColor = ContextCompat.getColor(context, R.color.color_blue)
            pivStyle.hitColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            pivStyle.errorColor = ContextCompat.getColor(context, R.color.color_red)
            pivStyle.lineWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2f,
                resources.displayMetrics
            )

            val plvStyle = (patternLockerView.normalCellView as DefaultLockerNormalCellView).styleDecorator
            plvStyle.normalColor = ContextCompat.getColor(context, R.color.colorWhite)
            plvStyle.fillColor = ContextCompat.getColor(context, R.color.color_blue)
            plvStyle.hitColor = ContextCompat.getColor(context, R.color.colorPrimaryDark)
            plvStyle.errorColor = ContextCompat.getColor(context, R.color.color_red)
            plvStyle.lineWidth = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5f,
                resources.displayMetrics
            )

            patternLockerView.setOnPatternChangedListener(object : OnPatternChangeListener {
                override fun onStart(view: PatternLockerView) {}

                override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

                override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                    val isError = !isPatternOk(hitIndexList)
                    view.updateStatus(isError)
                    patternIndicatorView.updateState(hitIndexList, isError)
                    updateMsg(textMsg)
                }

                override fun onClear(view: PatternLockerView) {
                    finishIfNeeded()
                }
            })

            textMsg.text = "绘制解锁图案"
        }
        patternHelper = PatternHelper()
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        patternHelper?.validateForChecking(hitIndexList)
        return patternHelper?.isOk == true
    }

    private fun updateMsg(textMsg: TextView) {
        textMsg.text = patternHelper?.message
        textMsg.setTextColor(
            if (patternHelper?.isOk == true) {
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
            } else {
                ContextCompat.getColor(this, R.color.color_red)
            }
        )
    }

    private fun finishIfNeeded() {
        if (patternHelper?.isFinish == true) {
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
