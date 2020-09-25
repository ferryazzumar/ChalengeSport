package com.example.chalengesport.ui.widget

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.chalengesport.commons.utils.Utils.Companion.dialog
import com.example.chalengesport.commons.utils.other.DialogStyle
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialDialogFragment constructor(
    private val centered: Boolean,
    private val view: ((MaterialAlertDialogBuilder) -> View)? = null
) : DialogFragment() {

    constructor(
        title: String? = null,
        message: String? = null,
        icon: Drawable? = null,
        centered: Boolean = false,
        style: DialogStyle = DialogStyle.DEFAULT,
        items: Array<String> = emptyArray(),
        view: ((MaterialAlertDialogBuilder) -> View)? = null,
        onClickedAction: ((dialog: DialogInterface, position: Int) -> Unit)? = null,
        onMultiChoiceAction: ((dialog: DialogInterface, position: Int, checked: Boolean) -> Unit)? = null
    ) : this(centered, view) {
        this.title = title
        this.message = message
        this.icon = icon
        this.style = style
        this.items = items
        this.onClickedAction = onClickedAction
        this.onMultiChoiceAction = onMultiChoiceAction
    }

    private var title: String? = null
    private var message: String? = null
    private var icon: Drawable? = null
    private var style = DialogStyle.DEFAULT
    private var items = emptyArray<String>()
    private var onClickedAction: ((dialog: DialogInterface, position: Int) -> Unit)? = null
    private var onMultiChoiceAction: ((dialog: DialogInterface, position: Int, checked: Boolean) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return requireContext().dialog(
            title,
            message,
            icon,
            centered,
            style,
            items,
            view,
            onClickedAction,
            onMultiChoiceAction
        ).create()
    }
}