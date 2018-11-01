package com.github.ihsg.demo.ui.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ihsg.demo.R
import kotlinx.android.synthetic.main.activity_simple_style.*

class SimpleStyleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_style)

        btnSetting.setOnClickListener { SimplePatternSettingActivity.startAction(this@SimpleStyleActivity) }

        btnChecking.setOnClickListener { SimplePatternCheckingActivity.startAction(this@SimpleStyleActivity) }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, SimpleStyleActivity::class.java)
            context.startActivity(intent)
        }
    }
}
