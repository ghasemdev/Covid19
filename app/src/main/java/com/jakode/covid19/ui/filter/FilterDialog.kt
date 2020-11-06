package com.jakode.covid19.ui.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakode.covid19.R
import com.jakode.covid19.databinding.FilterLayoutBinding
import com.jakode.covid19.utils.getColor
import com.jakode.covid19.utils.isDarkTheme

class FilterDialog : BottomSheetDialogFragment() {
    private var binding: FilterLayoutBinding? = null

    private lateinit var listener: OnFilterDialogListener

    companion object {
        private const val RESOURCE_KEY = "RESOURCE"
        private const val LISTENER_KEY = "LISTENER"

        @Volatile
        private var instance: FilterDialog? = null
        private val LOCK = Any()

        operator fun invoke(
            @LayoutRes resource: Int,
            listener: OnFilterDialogListener
        ): FilterDialog =
            instance ?: synchronized(LOCK) {
                instance ?: newInstance(resource, listener).also {
                    instance = it
                }
            }

        private fun newInstance(resource: Int, listener: OnFilterDialogListener): FilterDialog {
            val args = Bundle().apply {
                putInt(RESOURCE_KEY, resource)
                putParcelable(LISTENER_KEY, listener)
            }
            FilterDialog().apply { arguments = args }.also { return it }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fun getResource() = requireArguments().getInt(RESOURCE_KEY)

        binding = DataBindingUtil.inflate(inflater, getResource(), container, false)
        initialize()
        return binding!!.root
    }

    private fun initialize() {
        fun getListener() = requireArguments().getParcelable<OnFilterDialogListener>(LISTENER_KEY)

        binding!!.apply {
            filter = getListener()
            handlers = MyHandlers()

            continent = allLabel
            order = false
            sortBy = caseTick
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog
            val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(sheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onDestroyView() {
        binding = null
        instance = null
        super.onDestroyView()
    }

    inner class MyHandlers {
        fun onClose() {
            dismiss()
        }

        fun setOnContinent(view: View, continent: TextView) {
            continent.apply {
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

            binding!!.continent = view
        }

        fun onOrderChanged(isChecked: Boolean) {
            binding!!.order = isChecked
        }

        fun setOnOrder(view: View, sortBy: ImageView) {
            val id = when (sortBy.id) {
                R.id.country_tick -> R.id.country_label
                R.id.case_tick -> R.id.case_label
                R.id.death_tick -> R.id.death_label
                R.id.recovered_tick -> R.id.recovered_label
                else -> -1
            }
            if (view.id != id) {
                binding!!.apply {
                    when (view.id) {
                        R.id.country_label -> {
                            countryTick.visibility = View.VISIBLE
                            binding!!.sortBy = countryTick
                        }
                        R.id.case_label -> {
                            caseTick.visibility = View.VISIBLE
                            binding!!.sortBy = caseTick
                        }
                        R.id.death_label -> {
                            deathTick.visibility = View.VISIBLE
                            binding!!.sortBy = deathTick
                        }
                        R.id.recovered_label -> {
                            recoveredTick.visibility = View.VISIBLE
                            binding!!.sortBy = recoveredTick
                        }
                    }
                    sortBy.visibility = View.INVISIBLE
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
    }
}