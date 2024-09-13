package com.github.ihsg.demo.ui.whole

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.ihsg.demo.R
import com.github.ihsg.demo.databinding.ActivityWholePatternCheckingBinding
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.DefaultLockerNormalCellView
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView

class WholePatternCheckingActivity : AppCompatActivity() {
    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWholePatternCheckingBinding.inflate(layoutInflater).apply {
            setContentView(root)

            val decorator = (patternLockerView.normalCellView as DefaultLockerNormalCellView).styleDecorator

            patternLockerView.hitCellView = RippleLockerHitCellView()
                .setHitColor(decorator.hitColor)
                .setErrorColor(decorator.errorColor)

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
            if (this.patternHelper?.isOk == true) {
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
            val intent = Intent(context, WholePatternCheckingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
