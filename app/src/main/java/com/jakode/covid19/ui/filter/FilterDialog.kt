package com.jakode.covid19.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakode.covid19.R
import com.jakode.covid19.databinding.DialogFilterBinding
import com.jakode.covid19.utils.getColor
import com.jakode.covid19.utils.isDarkTheme

class FilterDialog : BottomSheetDialogFragment() {
    private var _binding: DialogFilterBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BottomSheetDialog

    companion object {
        private const val LISTENER_KEY = "LISTENER"
        private const val ORDER_KEY = "ORDER"
        private const val CONTINENT_KEY = "CONTINENT"
        private const val SORT_BY_KEY = "SORT_BY"

        operator fun invoke(filter: Filter, listener: OnFilterDialogListener): FilterDialog {
            return newInstance(filter, listener)
        }

        private fun newInstance(filter: Filter, listener: OnFilterDialogListener): FilterDialog {
            val args = Bundle().apply {
                putParcelable(LISTENER_KEY, listener)
                putString(ORDER_KEY, filter.order)
                putString(CONTINENT_KEY, filter.continent)
                putString(SORT_BY_KEY, filter.sortBy)
            }
            FilterDialog().apply { arguments = args }.also { return it }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_filter, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            handlers = MyHandlers()
            if (arguments != null) {
                filter = arguments?.getParcelable(LISTENER_KEY)
                updateFilter(
                    arguments?.getString(CONTINENT_KEY),
                    arguments?.getString(SORT_BY_KEY),
                    arguments?.getString(ORDER_KEY)
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(sheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    fun updateContent(data: Filter) {
        if (activity != null) {
            updateFilter(data.continent, data.sortBy, data.order)
            dialog.show()
        }
    }

    private fun updateFilter(mContinent: String?, mSortBy: String?, mOrder: String?) {
        fun initLabel(view: TextView): TextView {
            view.apply {
                setTextColor(
                    if (!isDarkTheme(requireActivity())) getColor("EF9414")
                    else getColor("7D53C6")
                )
                setBackgroundResource(R.drawable.view_stroke_on)
            }
            return view
        }

        fun initTick(view: ImageView): ImageView {
            view.visibility = View.VISIBLE
            return view
        }

        binding.apply {
            continent = when (mContinent) {
                "Asia" -> initLabel(asiaLabel)
                "Africa" -> initLabel(africaLabel)
                "Europe" -> initLabel(europeLabel)
                "Oceania" -> initLabel(oceaniaLabel)
                "North-America" -> initLabel(northAmericaLabel)
                "South-America" -> initLabel(southAmericaLabel)
                else -> initLabel(allLabel)
            }

            sortBy = when (mSortBy) {
                "Country" -> initTick(countryTick)
                "Deaths" -> initTick(deathTick)
                "Recovered" -> initTick(recoveredTick)
                else -> initTick(caseTick)
            }

            order = mOrder.toBoolean()
            sort.isChecked = mOrder.toBoolean()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    inner class MyHandlers {
        fun onClose() {
            dismiss()
        }

        fun setOnContinent(view: View, continent: TextView? = null) {
            continent?.apply {
                setTextColor(
                    if (!isDarkTheme(requireActivity())) getColor("232323")
                    else getColor("FDFDFD")
                )
                setBackgroundResource(R.drawable.view_stroke_off)
            }
            (view as TextView).apply {
                setTextColor(
                    if (!isDarkTheme(requireActivity())) getColor("EF9414")
                    else getColor("7D53C6")
                )
                setBackgroundResource(R.drawable.view_stroke_on)
            }

            binding.continent = view
        }

        fun onOrderChanged(isChecked: Boolean) {
            binding.order = isChecked
        }

        fun setOnOrder(view: View, sortBy: ImageView? = null) {
            val id = when (sortBy?.id) {
                R.id.country_tick -> R.id.country_label
                R.id.case_tick -> R.id.case_label
                R.id.death_tick -> R.id.death_label
                R.id.recovered_tick -> R.id.recovered_label
                else -> -1
            }
            if (view.id != id) {
                binding.apply {
                    when (view.id) {
                        R.id.country_label -> {
                            countryTick.visibility = View.VISIBLE
                            binding.sortBy = countryTick
                        }
                        R.id.case_label -> {
                            caseTick.visibility = View.VISIBLE
                            binding.sortBy = caseTick
                        }
                        R.id.death_label -> {
                            deathTick.visibility = View.VISIBLE
                            binding.sortBy = deathTick
                        }
                        R.id.recovered_label -> {
                            recoveredTick.visibility = View.VISIBLE
                            binding.sortBy = recoveredTick
                        }
                    }
                    sortBy?.visibility = View.INVISIBLE
                }
            }
        }

        fun getSortBy(sortBy: ImageView): String {
            return when (sortBy.id) {
                R.id.country_tick -> "Country"
                R.id.case_tick -> "Total Case"
                R.id.death_tick -> "Deaths"
                R.id.recovered_tick -> "Recovered"
                else -> "NEVER HAPPEN"
            }
        }

        fun getContinent(continent: TextView): String {
            return when (continent.id) {
                R.id.all_label -> "All"
                R.id.asia_label -> "Asia"
                R.id.north_america_label -> "North-America"
                R.id.europe_label -> "Europe"
                R.id.africa_label -> "Africa"
                R.id.south_america_label -> "South-America"
                R.id.oceania_label -> "Oceania"
                else -> "NEVER HAPPEN"
            }
        }
    }
}