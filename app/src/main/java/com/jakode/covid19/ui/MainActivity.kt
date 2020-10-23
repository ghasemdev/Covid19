package com.jakode.covid19.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakode.covid19.R
import com.jakode.covid19.model.Global
import com.jakode.covid19.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.setStateEvent(MainStateEvent.GetBlogEvents)
        viewModel.dataState.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success<Global> -> {
                    Log.d("TAG", "onCreate: ${dataState.data}")
                }
                is DataState.Error -> {
                    Log.e("TAG", "onCreate: ${dataState.exception.message}")
                }
                is DataState.Loading -> {

                }
            }
        })
    }
}