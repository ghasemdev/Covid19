package com.jakode.covid19.ui.intro

import com.jakode.covid19.R
import com.jakode.covid19.model.Slider
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.ui.adapter.ViewType

data class IntroViewType(private val model: Slider) : ViewType<Slider> {
    override fun layoutId(): Int = R.layout.intro_item
    override fun data(): Slider = model
}