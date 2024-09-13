package com.github.ihsg.demo.ui.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.ihsg.demo.R
import com.github.ihsg.demo.databinding.ActivitySimplePatternSettingBinding
import com.github.ihsg.demo.util.PatternHelper
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView

class SimplePatternSettingActivity : AppCompatActivity() {
    private var patternHelper: PatternHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivitySimplePatternSettingBinding.inflate(layoutInflater).apply {
            setContentView(root)
            patternLockerView.setOnPatternChangedListener(object : OnPatternChangeListener {
                override fun onStart(view: PatternLockerView) {}

                override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {}

                override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
                    val isOk = isPatternOk(hitIndexList)
                    view.updateStatus(!isOk)
                    patternIndicatorView.updateState(hitIndexList, !isOk)
                    updateMsg(textMsg)
                }

                override fun onClear(view: PatternLockerView) {
                    finishIfNeeded()
                }
            })
            textMsg.text = "设置解锁图案"
        }
        patternHelper = PatternHelper()
    }

    private fun isPatternOk(hitIndexList: List<Int>): Boolean {
        patternHelper?.validateForSetting(hitIndexList)
        return patternHelper?.isOk == true
    }

    private fun updateMsg(textMsg: TextView) {
        textMsg.text = this.patternHelper?.message
        textMsg.setTextColor(
            if (patternHelper?.isOk == true) {
                ContextCompat.getColor(this, R.color.colorAccent)
            } else {
                ContextCompat.getColor(this, R.color.color_red)
            }
        )
    }

    private fun finishIfNeeded() {
        if (this.patternHelper?.isFinish == true) {
            finish()
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, SimplePatternSettingActivity::class.java)
            context.startActivity(intent)
        }
    }
}
