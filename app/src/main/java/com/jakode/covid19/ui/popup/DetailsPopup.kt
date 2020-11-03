package com.jakode.covid19.ui.popup

import kotlinx.android.synthetic.main.details_popup_layout.view.*

class DetailsPopup : PopupMenu() {
    override fun newInstance() = DetailsPopup()

    override fun initialize(popupTexts: List<String>) {
        popupView.popup_text.text = popupTexts[0]
    }

    override fun clickListener(listeners: List<PopupClickListener>) {
        popupView.row1.setOnClickListener {
            popupWindow.dismiss()
            listeners[0].setOnClickListener()
        }
    }
}