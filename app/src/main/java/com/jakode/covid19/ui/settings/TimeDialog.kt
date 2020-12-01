package com.jakode.covid19.ui.settings

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakode.covid19.R
import com.jakode.covid19.databinding.DialogTimeBinding
import com.jakode.covid19.utils.getColor
import com.jakode.covid19.utils.isDarkTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class TimeDialog : BottomSheetDialogFragment() {
    private val args: TimeDialogArgs by navArgs()
    private var _binding: DialogTimeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BottomSheetDialog
    private lateinit var onTimeDialogListener: OnTimeDialogListener

    private val myHandlers by lazy { MyHandlers() }

    private var hour by Delegates.notNull<Int>()
    private var minute by Delegates.notNull<Int>()
    private var isHourFocus = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_time, container, false)
        initialize()
        return binding.root
    }

    private fun initialize() {
        onTimeDialogListener = args.onTimeDialogListener
        hour = args.hour
        minute = args.minute
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            handlers = myHandlers

            plus.setOnLongClickListener {
                MainScope().launch {
                    while (true) {
                        myHandlers.plus()
                        delay(200L)
                        if (!plus.isPressed) break
                    }
                }
                true
            }

            minus.setOnLongClickListener {
                MainScope().launch {
                    while (true) {
                        myHandlers.minus()
                        delay(200L)
                        if (!minus.isPressed) break
                    }
                }
                true
            }
        }

        updateMinute()
        updateHour()
    }

    private fun updateHour() {
        binding.hour.text = String.format("%02d", this@TimeDialog.hour)
    }

    private fun updateMinute() {
        binding.minute.text = String.format("%02d", this@TimeDialog.minute)
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    inner class MyHandlers {
        fun onClose() {
            dismiss()
        }

        fun plus() {
            if (isHourFocus) {
                hour++
                if (hour == 24) hour = 0
                updateHour()
            } else {
                minute++
                if (minute == 60) minute = 0
                updateMinute()
            }
        }

        fun minus() {
            if (isHourFocus) {
                hour--
                if (hour == -1) hour = 23
                updateHour()
            } else {
                minute--
                if (minute == -1) minute = 59
                updateMinute()
            }
        }

        @SuppressLint("ResourceAsColor")
        fun onClick(view: View) {
            binding.apply {
                when (view.id) {
                    R.id.hour -> {
                        isHourFocus = true

                        hour.setBackgroundResource(R.drawable.view_stroke_on)
                        hour.setTextColor(
                            if (isDarkTheme(requireActivity())) getColor("7D53C6")
                            else getColor("F5AD47")
                        )

                        minute.setBackgroundResource(R.drawable.view_stroke_off)
                        minute.setTextColor(
                            if (isDarkTheme(requireActivity())) getColor("FDFDFD")
                            else getColor("232323")
                        )
                    }
                    R.id.minute -> {
                        isHourFocus = false

                        minute.setBackgroundResource(R.drawable.view_stroke_on)
                        minute.setTextColor(
                            if (isDarkTheme(requireActivity())) getColor("7D53C6")
                            else getColor("F5AD47")
                        )

                        hour.setBackgroundResource(R.drawable.view_stroke_off)
                        hour.setTextColor(
                            if (isDarkTheme(requireActivity())) getColor("FDFDFD")
                            else getColor("232323")
                        )
                    }
                }
            }
        }

        fun confirmed() {
            if (hour == 0 && minute == 0) {
                minute = 1
            }
            onTimeDialogListener.onChanged(hour, minute, myHandlers)
        }
    }
}