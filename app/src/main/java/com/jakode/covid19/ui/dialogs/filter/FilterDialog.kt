package com.jakode.covid19.ui.dialogs.filter

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jakode.covid19.R

class FilterDialog : BottomSheetDialogFragment() {

    companion object {
        @Volatile
        private var instance: FilterDialog? = null
        private val LOCK = Any()

        operator fun invoke(@LayoutRes resource: Int): FilterDialog =
            instance ?: synchronized(LOCK) {
                instance ?: newInstance(resource).also {
                    instance = it
                }
            }

        private fun newInstance(resource: Int): FilterDialog {
            val args = Bundle().apply { putInt("res", resource) }
            val filterDialog = FilterDialog()
            filterDialog.arguments = args
            return filterDialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(requireArguments().getInt("res"), container, false)
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
        instance = null
        super.onDestroyView()
    }
}