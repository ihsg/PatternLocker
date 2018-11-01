package com.github.ihsg.demo.ui.def

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ihsg.demo.R
import kotlinx.android.synthetic.main.activity_default_style.*


class DefaultStyleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default_style)

        btnSetting.setOnClickListener { DefaultPatternSettingActivity.startAction(this@DefaultStyleActivity) }
        btnChecking.setOnClickListener { DefaultPatternCheckingActivity.startAction(this@DefaultStyleActivity) }
    }

    companion object {

        fun startAction(context: Context) {
            val intent = Intent(context, DefaultStyleActivity::class.java)
            context.startActivity(intent)
        }
    }
}
