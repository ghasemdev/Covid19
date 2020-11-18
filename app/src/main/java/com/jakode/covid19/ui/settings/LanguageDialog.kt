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
import com.jakode.covid19.databinding.DialogLanguageBinding
import com.jakode.covid19.utils.Language

class LanguageDialog : BottomSheetDialogFragment() {
    private val args: LanguageDialogArgs by navArgs()
    private var _binding: DialogLanguageBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialog: BottomSheetDialog
    private lateinit var onLanguageDialogListener: OnLanguageDialogListener

    private val myHandlers by lazy { MyHandlers() }
    private val sharedPreferences by lazy { SharedPreferencesHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.dialog_language, container, false)
        onLanguageDialogListener = args.onLanguageDialogListener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            handlers = myHandlers

            language = when (sharedPreferences.language) {
                Language.Persian -> {
                    persianTick.visibility = View.VISIBLE
                    persianTick
                }
                Language.English -> {
                    englishTick.visibility = View.VISIBLE
                    englishTick
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

        fun onClick(view: View, language: ImageView? = null) {
            val id = when (language?.id) {
                R.id.english_tick -> R.id.english_label
                R.id.persian_tick -> R.id.persian_label
                else -> -1
            }
            if (view.id != id) {
                binding.apply {
                    language?.visibility = View.INVISIBLE
                    when (view.id) {
                        R.id.english_label -> {
                            englishTick.visibility = View.VISIBLE
                            binding.language = englishTick
                            onLanguageDialogListener.onChanged(Language.English, myHandlers)
                        }
                        R.id.persian_label -> {
                            persianTick.visibility = View.VISIBLE
                            binding.language = persianTick
                            onLanguageDialogListener.onChanged(Language.Persian, myHandlers)
                        }
                    }
                }
            }
        }
    }
}