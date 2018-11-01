package com.github.ihsg.demo.ui.whole

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ihsg.demo.R
import kotlinx.android.synthetic.main.activity_simple_style.*

class WholeStyleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_whole_style)

        btnSetting.setOnClickListener { WholePatternSettingActivity.startAction(this@WholeStyleActivity) }
        btnChecking.setOnClickListener { WholePatternCheckingActivity.startAction(this@WholeStyleActivity) }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, WholeStyleActivity::class.java)
            context.startActivity(intent)
        }
    }
}
