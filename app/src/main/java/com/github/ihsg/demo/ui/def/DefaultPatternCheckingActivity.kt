package com.github.ihsg.demo.ui.def

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.ihsg.demo.R
import com.github.ihsg.demo.databinding.ActivityDefaultPatternCheckingBinding
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView


class DefaultPatternCheckingActivity : AppCompatActivity() {
    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityDefaultPatternCheckingBinding.inflate(layoutInflater).apply {
            setContentView(root)

            patternLockerView.linkedLineView = null
            patternLockerView.hitCellView = null

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
                ContextCompat.getColor(this, R.color.colorPrimary)
            } else {
                ContextCompat.getColor(this, R.color.colorAccent)
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
            val intent = Intent(context, DefaultPatternCheckingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
