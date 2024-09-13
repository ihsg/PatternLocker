package com.github.ihsg.demo.ui.whole

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ihsg.demo.databinding.ActivityWholeStyleBinding

class WholeStyleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityWholeStyleBinding.inflate(layoutInflater).apply {
            setContentView(root)

            btnSetting.setOnClickListener {
                WholePatternSettingActivity.startAction(this@WholeStyleActivity)
            }

            btnChecking.setOnClickListener {
                WholePatternCheckingActivity.startAction(this@WholeStyleActivity)
            }
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, WholeStyleActivity::class.java)
            context.startActivity(intent)
        }
    }
}
