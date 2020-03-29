package com.github.ihsg.patternlocker

import androidx.annotation.ColorInt


/**
 * Created by hsg on 28/04/2019.
 */

data class DefaultStyleDecorator(
    @ColorInt var normalColor: Int,
    @ColorInt var fillColor: Int,
    @ColorInt var hitColor: Int,
    @ColorInt var errorColor: Int,
    var lineWidth: Float
)