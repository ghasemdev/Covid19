package com.jakode.covid19.ui.adapter

import androidx.annotation.LayoutRes

interface ViewType<out T> {
    @LayoutRes
    fun layoutId(): Int
    fun data(): T
}