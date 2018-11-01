package com.github.ihsg.demo.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ihsg.demo.R
import com.github.ihsg.demo.ui.def.DefaultStyleActivity
import com.github.ihsg.demo.ui.simple.SimpleStyleActivity
import com.github.ihsg.demo.ui.whole.WholeStyleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnDefault.setOnClickListener { DefaultStyleActivity.startAction(this@MainActivity) }
        btnSimple.setOnClickListener { SimpleStyleActivity.startAction(this@MainActivity) }
        btnWhole.setOnClickListener { WholeStyleActivity.startAction(this@MainActivity) }
    }
}