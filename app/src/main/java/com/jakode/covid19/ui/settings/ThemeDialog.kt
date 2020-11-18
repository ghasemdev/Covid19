package com.jakode.covid19.ui.settings

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakode.covid19.R
import com.jakode.covid19.data.sharedpreferences.SharedPreferencesHelper
import com.jakode.covid19.databinding.DialogThemeBinding

class ThemeDialog : BottomSheetDialogFragment() {
    private val args: ThemeDialogArgs by navArgs()
    private var _binding: DialogThemeBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BottomSheetDialog
    private lateinit var onThemeDialogListener: OnThemeDialogListener

    private val myHandlers by lazy { MyHandlers() }
    private val sharedPreferences by lazy { SharedPreferencesHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_theme, container, false)
        onThemeDialogListener = args.onThemeDialogListener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            handlers = myHandlers

            theme = when (sharedPreferences.theme) {
                false -> {
                    lightTick.visibility = View.VISIBLE
                    lightTick
                }
                true -> {
                    darkTick.visibility = View.VISIBLE
                    darkTick
                }
                null -> {
                    systemTick.visibility = View.VISIBLE
                    systemTick
                }
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    inner class MyHandlers {
        fun onClose() {
            dismiss()
        }

        fun onClick(view: View, theme: ImageView? = null) {
            val id = when (theme?.id) {
                R.id.dark_tick -> R.id.dark_label
                R.id.light_tick -> R.id.light_label
                R.id.system_tick -> R.id.system_label
                else -> -1
            }
            if (view.id != id) {
                binding.apply {
                    theme?.visibility = View.INVISIBLE
                    when (view.id) {
                        R.id.dark_label -> {
                            darkTick.visibility = View.VISIBLE
                            binding.theme = darkTick
                            onThemeDialogListener.onChanged(theme = true, myHandlers)
                        }
                        R.id.light_label -> {
                            lightTick.visibility = View.VISIBLE
                            binding.theme = lightTick
                            onThemeDialogListener.onChanged(theme = false, myHandlers)
                        }
                        R.id.system_label -> {
                            systemTick.visibility = View.VISIBLE
                            binding.theme = systemTick
                            onThemeDialogListener.onChanged(theme = null, myHandlers)
                        }
                    }
                }
            }
        }
    }
}