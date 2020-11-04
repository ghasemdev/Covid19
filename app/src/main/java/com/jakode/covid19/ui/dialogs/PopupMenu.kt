package com.jakode.covid19.ui.dialogs

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.jakode.covid19.R
import kotlinx.android.synthetic.main.details_popup_layout.view.*

object PopupMenu {
    @SuppressLint("InflateParams")
    fun show(text: String, view: View, x: Int, y: Int) {
        // Create a View object yourself through inflater
        val inflater =
            view.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.details_popup_layout, null)
        popupView.popup_text.text = text

        // Specify the length and width through constants
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT

        // Make Inactive Items Outside Of PopupWindow
        val focusable = true

        // Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.elevation = 5F

        // Set the location of the window on the screen
        popupWindow.showAsDropDown(view, x, y)
    }
}