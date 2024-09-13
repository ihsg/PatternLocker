package com.github.ihsg.demo.ui.def

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ihsg.demo.databinding.ActivityDefaultStyleBinding


class DefaultStyleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityDefaultStyleBinding.inflate(layoutInflater).apply {
            setContentView(root)

            btnSetting.setOnClickListener {
                DefaultPatternSettingActivity.startAction(this@DefaultStyleActivity)
            }
            btnChecking.setOnClickListener {
                DefaultPatternCheckingActivity.startAction(this@DefaultStyleActivity)
            }
        }
    }

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, DefaultStyleActivity::class.java)
            context.startActivity(intent)
        }
    }
}
