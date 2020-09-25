package com.example.chalengesport.commons.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.example.chalengesport.R
import com.example.chalengesport.commons.utils.other.DialogStyle
import com.example.chalengesport.ui.widget.MaterialDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject


class Utils {
    companion object {

        // String
        fun Any?.toString(default: String = "") : String {
            return toString().string()
        }

        fun String?.isEmailPattern(): Boolean = Patterns.EMAIL_ADDRESS.matcher(string()).matches()

        fun String?.string(default: String =  "-"): String {
            val regex = Regex("^(null|NULL|Null|0)")
            return if (this?.contains(regex) == true) replace(regex, default)
            else this ?: default
        }

        fun String?.isNumeric() = string().toLongOrNull() != null

        fun String?.isAlphabet() = string().matches(Regex("[a-zA-Z]+"))

        fun String?.isAlphabetAndNumber() = string().matches(Regex("[a-zA-Z0-9]+")) && !isNumeric() && !isAlphabet()

        fun String.isPhoneNumber() = isNumeric() &&
                ((startsWith("08") && length == 12) ||
                        (startsWith("62") && length == 13) ||
                        (startsWith("+62") && length == 14))

        // Long
        fun Long?.long(default: Long = 0L) = this ?: default

        // Float
        fun Float?.float(default: Float = 0F) = this ?: default

        // Integer
        fun Int?.int(default: Int = 0) = this ?: default

        // Double
        fun Double?.double(default: Double = 0.0) = this ?: default

        // list
        fun <T> List<T>?.list(default: List<T> = listOf()): List<T> = this ?: default

        // Boolean
        fun Boolean?.boolean(default: Boolean = false) = this ?: default

        // Rating
        fun Float?.getRateString(): String {
            return if (float().toString().contains(".")) float().toString() else float().toInt().toString()
        }

        // Json Field
        fun JSONObject.getFieldOrNull(field: String): String =
            if (has(field)) getString(field) else ""

        // Error Body
        inline fun <reified T : Any?> ResponseBody.parse(): T? {
            val classType = object : TypeToken<T>() {}.type
            return Gson().fromJson(charStream(), classType)
        }


        fun getCoroutinesTimer(
            delayMillis: Long = 0,
            repeatMillis: Long = 0,
            action: () -> Unit
        ) = GlobalScope.launch {
            delay(delayMillis)
            if (repeatMillis > 0) {
                while (true) {
                    action()
                    delay(repeatMillis)
                }
            } else action()
        }

        // Context
        @SuppressLint("ResourceType")
        fun Activity.setStatusBarColor(@ColorRes colorRes: Int){
            window.statusBarColor = ContextCompat.getColor(applicationContext, colorRes)
        }

        fun Context.toast(
            message: CharSequence?,
            length: Int = Toast.LENGTH_SHORT
        ) {
            Toast.makeText(this, message, length).apply {
                with(view){
                    findViewById<TextView>(R.id.message).apply {
//                        typeface = font(R.font.open_sans_regular)
                    }
                }
            }.show()
        }

        @SuppressLint("PrivateResource")
        fun Context.dialog(
            title: String? = null,
            message: String? = null,
            icon: Drawable? = null,
            centered: Boolean = false,
            style: DialogStyle = DialogStyle.DEFAULT,
            items: Array<String> = emptyArray(),
            view: ((MaterialAlertDialogBuilder) -> View)? = null,
            onClickedAction: ((dialog: DialogInterface, position: Int) -> Unit)? = null,
            onMultiChoiceAction: ((dialog: DialogInterface, position: Int, checked: Boolean) -> Unit)? = null
        ) : MaterialAlertDialogBuilder {
            val styleRes = if (centered) R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered
            else R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog

            return MaterialAlertDialogBuilder(this, styleRes).apply {
                if (title != null) {
                    setTitle(title)
                }
                if (message != null) {
                    setMessage(message)
                }
                if (icon != null) {
                    setIcon(icon)
                }
                if (view != null) {
                    setView(view(this))
                }

                when {
                    style == DialogStyle.DEFAULT && items.isEmpty() -> return@apply

                    style == DialogStyle.SIMPLE && items.isNotEmpty() -> setItems(items) { dialog, which ->
                        if (onClickedAction != null) onClickedAction(dialog, which)
                    }

                    style == DialogStyle.SINGLE && items.isNotEmpty() -> setSingleChoiceItems(items, 0) { dialog, which ->
                        if (onClickedAction != null) onClickedAction(dialog, which)
                    }

                    style == DialogStyle.MULTI && items.isNotEmpty() -> setMultiChoiceItems(items, booleanArrayOf(false)) { dialog, which, isChecked ->
                        if (onMultiChoiceAction != null) onMultiChoiceAction(dialog, which, isChecked)
                    }
                }
            }
        }

        fun FragmentManager.dialogFragment(
            title: String? = null,
            message: String? = null,
            icon: Drawable? = null,
            centered: Boolean = false,
            style: DialogStyle = DialogStyle.DEFAULT,
            items: Array<String> = emptyArray(),
            view: ((MaterialAlertDialogBuilder) -> View)? = null,
            onClickedAction: ((dialog: DialogInterface, position: Int) -> Unit)? = null,
            onMultiChoiceAction: ((dialog: DialogInterface, position: Int, checked: Boolean) -> Unit)? = null
        ) {
            val dialog = MaterialDialogFragment(title, message, icon, centered, style, items, view, onClickedAction, onMultiChoiceAction)
            if (!dialog.isAdded) dialog.show(this, javaClass.simpleName)
        }

        fun FragmentManager.timePicker(
            selection: Long? = null,
            title: String? = null,
            onSaveClickAction: ((Long) -> Unit)?
        ) : MaterialDatePicker<Long> {
            return MaterialDatePicker.Builder.datePicker().apply {
                if (selection != null) {
                    setSelection(selection)
                }

                if (!title.isNullOrEmpty()) {
                    setTitleText(title)
                }
            }.build().apply {
                addOnPositiveButtonClickListener {
                    if (onSaveClickAction != null) {
                        onSaveClickAction(it)
                    } else dismiss()
                }
            }.also {
                it.show(this, javaClass.simpleName)
            }
        }

        fun FragmentManager.rangeTimePicker(
            selectedRange: Pair<Long,Long>? = null,
            title: String? = null,
            onSaveClickAction: ((Pair<Long,Long>) -> Unit)?
        ) : MaterialDatePicker<androidx.core.util.Pair<Long, Long>> {
            return MaterialDatePicker.Builder.dateRangePicker().apply {
                if (!title.isNullOrEmpty()) {
                    setTitleText(title)
                }

                if (selectedRange != null) {
                    setSelection(androidx.core.util.Pair(selectedRange.first, selectedRange.second))
                }

            }.build().apply {
                addOnPositiveButtonClickListener {
                    if (onSaveClickAction != null) {
                        onSaveClickAction(Pair(it.first.long(), it.second.long()))
                    } else dismiss()
                }
            }.also {
                it.show(this, javaClass.simpleName)
            }
        }

        fun Context.convertDpToPixel(dp: Float): Float {
            return dp * (resources
                .displayMetrics
                .densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun Context.convertPixelToDp(px: Float): Float {
            return px / (resources
                .displayMetrics
                .densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }

        fun Context.font(@FontRes fontRes: Int): Typeface? {
            return ResourcesCompat.getFont(this, fontRes)
        }

        fun Context.color(@ColorRes colorRes: Int): Int {
            return ContextCompat.getColor(this, colorRes)
        }

        fun Context.colorStateList(@ColorRes colorRes: Int): ColorStateList? {
            return ContextCompat.getColorStateList(this, colorRes)
        }

        fun Context.drawable(@DrawableRes drawableRes: Int): Drawable? {
            return ContextCompat.getDrawable(this, drawableRes)
        }

        fun Context.dimension(@DimenRes dimenRes: Int): Float {
            return resources.getDimension(dimenRes)
        }

        inline fun <reified T> Context.getResArray(@ArrayRes arrayRes: Int): T {
            return when(T::class.java.name){
                IntArray::class.java.name -> resources.getIntArray(arrayRes) as T
                else -> resources.getStringArray(arrayRes) as T
            }
        }

        // Dimension
        fun getDeviceHeight(context: Context): Int {
            return context
                .resources
                .displayMetrics
                .heightPixels
        }

        fun getDeviceWidth(context: Context): Int {
            return context
                .resources
                .displayMetrics
                .widthPixels
        }

        fun createCircleDrawable(
            context: Context,
            whSize: Pair<Int,Int>,
            @ColorRes backgroundColor: Int = R.color.colorGrey
        ): GradientDrawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                cornerRadii = floatArrayOf(0f,0f,0f,0f,0f,0f,0f,0f)
                color = context.colorStateList(backgroundColor)
                setSize(whSize.first, whSize.second)
            }
        }

    }
}