package com.github.ihsg.patternlocker

import android.util.Log

class Logger {
    companion object {
        var enable = false
        fun d(tag: String, msg: String) {
            if (enable) {
                Log.d(tag, msg)
            }
        }
    }
}