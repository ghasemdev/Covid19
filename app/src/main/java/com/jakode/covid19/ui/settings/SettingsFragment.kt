package com.jakode.covid19.ui.settings

import android.os.Bundle
import android.os.Parcel
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jakode.covid19.R
import com.jakode.covid19.data.sharedpreferences.SharedPreferencesHelper
import com.jakode.covid19.databinding.FragmentSettingsBinding
import com.jakode.covid19.ui.activities.MainActivity
import com.jakode.covid19.utils.Intents
import com.jakode.covid19.utils.Language
import com.jakode.covid19.utils.OnBackPressedListener
import com.jakode.covid19.utils.changeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings), OnBackPressedListener {
    private val viewModel: SettingsViewModel by viewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferences by lazy { SharedPreferencesHelper(requireContext()) }

    private var duration = 0L
    private var hour by Delegates.notNull<Int>()
    private var minute by Delegates.notNull<Int>()

    private var easterEgg = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        observe()
        initialize()
        clickListener()

        // impalement onBackPressed
        (activity as MainActivity).setOnBackPressedListener(this)
    }

    private fun observe() {
        viewModel.duration.observe(viewLifecycleOwner, { duration -> // second
            hour = (duration / 3600).toInt()
            minute = ((duration % 3600) / 60).toInt()

            binding.updateDataValue.text = convertTime()
        })
    }

    private fun convertTime(): String {
        val hourAndOneMinute = getString(R.string.hour_and_one_minute)
        val everyHourOne = getString(R.string.every_hour_one_minute)
        val everyHourAnd = getString(R.string.every_hour_and)
        val everyMinute = getString(R.string.every_minute)
        val everyHour = getString(R.string.every_hour)
        val every = getString(R.string.every)
        val tMinute = getString(R.string.minute)
        val tHour = getString(R.string.hour)
        val and = getString(R.string.and)

        return if (hour == 0) {
            if (minute == 1) everyMinute else "$every $minute $tMinute"
        } else {
            if (minute == 0) {
                if (hour == 1) everyHour else "$every $hour $tHour"
            } else {
                if (hour == 1) {
                    if (minute == 1) everyHourOne else "$everyHourAnd $minute $tMinute"
                } else {
                    if (minute == 1) "$every $hour $hourAndOneMinute" else "$every $hour $tHour $and $minute $tMinute"
                }
            }
        }
    }

    private fun initialize() {
        binding.apply {
            languageValue.text = Language.getName(sharedPreferences.language)
            updateThemeValue(sharedPreferences.theme)
        }
    }

    private fun FragmentSettingsBinding.updateThemeValue(theme: Boolean?) {
        themeValue.text = when (theme) {
            false -> getString(R.string.light)
            true -> getString(R.string.dark)
            null -> getString(R.string.system_default)
        }
    }

    private fun clickListener() {
        binding.apply {
            toolbarBack.setOnClickListener {
                findNavController().navigateUp()
            }

            toolbarNotification.setOnClickListener {}

            toolbarNotification.setOnLongClickListener { true }

            language.setOnClickListener { languageDialog() }

            theme.setOnClickListener { themeDialog() }

            updateData.setOnClickListener { timeDialog() }

            removeHistory.setOnClickListener { viewModel.removeAllSearch() }

            suggestFriends.setOnClickListener { shareApp() }

            contactUs.setOnClickListener { sendComment() }

            version.setOnClickListener { easterEgg() }
        }
    }

    private fun shareApp() {
        Intents.sendText(requireContext())
    }

    private fun easterEgg() {
        easterEgg++
        if (easterEgg in 2..6) {
            Toast
                .makeText(requireContext(), "${7 - easterEgg} more times", Toast.LENGTH_SHORT)
                .show()
        }
        if (easterEgg == 7) {
            easterEgg = 0
            binding.apply {
                virus1.animate().alpha(1F).duration = 2000L
                virus2.animate().alpha(1F).duration = 4000L
                virus3.animate().alpha(1F).duration = 1000L
                virus4.animate().alpha(1F).duration = 2000L
                virus5.animate().alpha(1F).duration = 3000L
            }
        }
    }

    private fun sendComment() {
        Intents.sendComment(requireContext())
    }

    private fun languageDialog() {
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

    private fun FragmentSettingsBinding.themeDialog() {
        val action =
            SettingsFragmentDirections.actionSettingsFragmentToThemeDialog(object :
                OnThemeDialogListener {

                override fun onChanged(
                    theme: Boolean?,
                    handlers: ThemeDialog.MyHandlers
                ) {
                    handlers.onClose()
                    viewModel.saveTheme(theme)
                    changeTheme(theme)
                    updateThemeValue(theme)
                }

                override fun describeContents() = 0
                override fun writeToParcel(dest: Parcel?, flags: Int) {}
            })
        findNavController().navigate(action)
    }

    private fun timeDialog() {
        val action =
            SettingsFragmentDirections.actionSettingsFragmentToTimeDialog(object :
                OnTimeDialogListener {

                override fun onChanged(
                    hour: Int,
                    minute: Int,
                    handlers: TimeDialog.MyHandlers
                ) {
                    handlers.onClose()
                    activity?.let {
                        viewModel.saveDuration((hour.times(60) + minute).times(60).toLong())
                    }
                }

                override fun describeContents() = 0
                override fun writeToParcel(dest: Parcel?, flags: Int) {}
            }, hour, minute)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onBackPressed() = true
}