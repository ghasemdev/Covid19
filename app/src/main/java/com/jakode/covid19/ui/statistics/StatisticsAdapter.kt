package com.jakode.covid19.ui.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakode.covid19.R
import com.jakode.covid19.databinding.StatisticItemBinding
import com.jakode.covid19.model.Statistics
import com.jakode.covid19.utils.getResources

class StatisticsAdapter(private var list: List<Statistics>) :
    RecyclerView.Adapter<StatisticsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            DataBindingUtil.inflate(inflater, R.layout.statistic_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Init
        holder.setData(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: StatisticItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setData(statistic: Statistics) {
            binding.statistic =
                statistic.apply { flag = getResources(binding.root.context, statistic.country) }
        }
    }
}