package com.jakode.covid19.ui.settings

import android.os.Bundle
import android.os.Parcel
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakode.covid19.R
import com.jakode.covid19.data.sharedpreferences.SharedPreferencesHelper
import com.jakode.covid19.databinding.FragmentSettingsBinding
import com.jakode.covid19.ui.activities.MainActivity
import com.jakode.covid19.utils.Language
import com.jakode.covid19.utils.OnBackPressedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings), OnBackPressedListener {
    private val viewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences by lazy { SharedPreferencesHelper(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        binding.languageValue.text = Language.getName(sharedPreferences.language)
        clickListener()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun clickListener() {
        binding.apply {
            toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }

            toolbarNotification.setOnClickListener {}

            toolbarNotification.setOnLongClickListener { true }

            language.setOnClickListener {
                val action =
                    SettingsFragmentDirections.actionSettingsFragmentToLanguageDialog(object :
                        OnLanguageDialogListener {

                        override fun onChanged(
                            language: Language,
                            handlers: LanguageDialog.MyHandlers
                        ) {
                            handlers.onClose()
                            if (activity != null) {
                                viewModel.saveLanguage(language)
                                (activity as MainActivity).recreate()
                            }
                        }

                        override fun describeContents() = 0
                        override fun writeToParcel(dest: Parcel?, flags: Int) {}
                    })
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = true
}