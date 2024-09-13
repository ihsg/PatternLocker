package com.github.ihsg.demo.ui.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ihsg.demo.databinding.ActivitySimpleStyleBinding

class SimpleStyleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivitySimpleStyleBinding.inflate(layoutInflater).apply {
            setContentView(root)
            btnSetting.setOnClickListener {
                SimplePatternSettingActivity.startAction(this@SimpleStyleActivity)
            }
            btnChecking.setOnClickListener {
                SimplePatternCheckingActivity.startAction(this@SimpleStyleActivity)
            }
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, SimpleStyleActivity::class.java)
            context.startActivity(intent)
        }
    }
}
