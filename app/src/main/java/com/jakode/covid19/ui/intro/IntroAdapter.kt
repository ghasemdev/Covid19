package com.jakode.covid19.ui.intro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakode.covid19.R
import com.jakode.covid19.databinding.IntroItemBinding
import com.jakode.covid19.model.Slider

class IntroAdapter(private var list: List<Slider>) :
    RecyclerView.Adapter<IntroAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            DataBindingUtil.inflate(inflater, R.layout.intro_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Init
        holder.setData(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: IntroItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(model: Slider) {
            binding.slider = model
        }
    }
}