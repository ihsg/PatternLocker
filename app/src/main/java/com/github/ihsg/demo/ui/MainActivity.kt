package com.github.ihsg.demo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.ihsg.demo.databinding.ActivityMainBinding
import com.github.ihsg.demo.ui.def.DefaultStyleActivity
import com.github.ihsg.demo.ui.simple.SimpleStyleActivity
import com.github.ihsg.demo.ui.whole.WholeStyleActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            btnDefault.setOnClickListener {
                DefaultStyleActivity.startAction(this@MainActivity)
            }

            btnSimple.setOnClickListener {
                SimpleStyleActivity.startAction(this@MainActivity)
            }

            btnWhole.setOnClickListener {
                WholeStyleActivity.startAction(this@MainActivity)
            }
        }
    }
}