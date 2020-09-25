package com.example.chalengesport.commons.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.text.underline
import androidx.core.view.doOnPreDraw
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.chalengesport.R
import com.example.chalengesport.commons.utils.Utils.Companion.color
import com.example.chalengesport.commons.utils.Utils.Companion.font
import com.example.chalengesport.commons.utils.Utils.Companion.int
import com.example.chalengesport.commons.utils.Utils.Companion.string
import com.example.chalengesport.commons.utils.other.ChipStyle
import com.example.chalengesport.commons.utils.other.HtmlImageGetter
import com.example.chalengesport.commons.utils.other.ImageCornerOptions
import com.example.chalengesport.commons.utils.other.TextStyleOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round

class ViewUtils {
    companion object {
        // Image Processing
        fun ImageView.loadImage(
            source: Any?,
            corner: ImageCornerOptions? = ImageCornerOptions.NORMAL,
            radius: Int? = null,
            overrideSize: Int? = null,
            placeHolder: Drawable? = ColorDrawable(Color.LTGRAY),
            @ColorRes background: Int? = null,
            scaleType: ImageView.ScaleType? = null
        ) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

            when (corner) {
                ImageCornerOptions.NORMAL -> {
                    requestOptions.transform(
                        CenterCrop()
                    )
                }

                ImageCornerOptions.CIRCLE -> {
                    requestOptions.transform(
                        CenterCrop(),
                        RoundedCorners(
                            resources.getDimensionPixelSize(
                                R.dimen.requestOptionCircleRadius
                            )
                        )
                    )
                }

                ImageCornerOptions.ROUNDED -> {
                    val cornerRadius = if (radius != null) {
                        round(
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                radius.toFloat(),
                                resources.displayMetrics
                            )
                        ).toInt()
                    } else {
                        resources.getDimensionPixelSize(R.dimen.requestOptionRoundRadius)
                    }

                    requestOptions.transform(
                        CenterCrop(),
                        RoundedCorners(cornerRadius)
                    )
                }
            }

            source?.let {
                if (scaleType == ImageView.ScaleType.FIT_CENTER) requestOptions.fitCenter()
                else if (scaleType == ImageView.ScaleType.CENTER_INSIDE) requestOptions.centerInside()

                if (overrideSize != null) {
                    requestOptions.override(overrideSize)
                }

                requestOptions.placeholder(placeHolder).error(placeHolder)

                Glide.with(context)
                    .load(it)
                    .apply(requestOptions)
                    .into(this)

                if (background != null) {
                    doOnPreDraw {
                        setBackground(Utils.createCircleDrawable(
                            context,
                            Pair(measuredWidth, measuredHeight),
                            background
                        )
                        )
                    }
                }
            }
        }

        // Text Processing
        fun TextView.textOrNull(charSequence: CharSequence?) {
            text = charSequence ?: "-"
        }

        @SuppressLint("SetTextI18n")
        fun TextView.textCurrency(
            price: Int?,
            prefix: String = "",
            suffix: String = ""
        ) {
            val currencyFormat: NumberFormat = DecimalFormat("#,###")
            text = "${prefix}Rp${currencyFormat.format(price.int()).string()} $suffix"
        }

        fun TextView.textDate(date: Date) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            text = dateFormat.format(date).string()
        }

        fun TextView.textWithStyle(
            charSequences: List<CharSequence>?,
            styleOptions: TextStyleOptions
        ) {
            text = when (styleOptions) {
                TextStyleOptions.BNL -> {
                    buildSpannedString {
                        bold { append(charSequences?.getOrNull(0)) }
                        append(
                            " ${charSequences?.getOrNull(1)}"
                        )
                    }
                }

                TextStyleOptions.BLN -> {
                    charSequences?.joinToString("")
                }

                TextStyleOptions.LBN -> {
                    charSequences?.joinToString("")
                }

                TextStyleOptions.LNB -> {
                    charSequences?.joinToString("")
                }

                TextStyleOptions.NBL -> {
                    charSequences?.joinToString("")
                }

                TextStyleOptions.NLB -> {
                    charSequences?.joinToString("")
                }
                TextStyleOptions.NORMAL -> {
                    charSequences?.joinToString("")
                }
            }
        }

        fun TextView.strike(){
            paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        fun RatingBar.rate(rate: Float?){
            rating = rate ?: 0f
        }

        @SuppressLint("SetTextI18n")
        fun TextView.rateText(rate: Float?){
            text = if(rate == 0.0f){
                "0/5"
            }else{
                "$rate/5"
            }
        }

        @SuppressLint("SetTextI18n")
        fun TextView.ellipsisText(
            suffix: CharSequence? = "",
            maxLines: Int,
            vararg charSequences: CharSequence?
        ) = doOnPreDraw {
            text = charSequences.joinToString("")

            when(charSequences.size){
                1 -> {
                    if (maxLines <= 0 ) {
                        val lastIndex = layout.getLineEnd(0)

                        text = buildSpannedString {
                            append(
                                "${text.subSequence(
                                    0, lastIndex - (suffix?.length.int()) - 10
                                )} . . . $suffix"
                            )
                        }
                    } else {
                        val lastIndex = layout.getLineEnd(maxLines - 1)
                        text = buildSpannedString {
                            append(
                                "${text.subSequence(
                                    0, lastIndex - (suffix?.length.int()) - 10
                                )} . . . $suffix"
                            )
                        }
                    }
                }

                2 -> {
                    if (maxLines <= 0) {
                        val lastIndex = layout.getLineEnd(0)

                        text = buildSpannedString {
                            bold { append("${charSequences[0]} ") }
                            append(
                                "${text.subSequence(
                                    charSequences[0]?.length.int(),
                                    lastIndex - (suffix?.length.int()) - 10
                                )} . . . $suffix"
                            )
                        }

                    } else {
                        val lastIndex = layout.getLineEnd(maxLines - 1)
                        text = buildSpannedString {
                            bold { append("${charSequences[0]} ") }
                            append(
                                "${text.subSequence(
                                    charSequences[0]?.length.int(),
                                    lastIndex - (suffix?.length.int()) - 10
                                )} . . . $suffix"
                            )
                        }
                    }
                }

                else -> {
                    //TODO("Nested Item Style ")
                }
            }
        }

        @Suppress("DEPRECATION")
        fun TextView.htmlTextOrNull(html: String) {

            text = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY,
                        HtmlImageGetter(
                            this
                        ), null) as Spannable
                }
                else -> {
                    Html.fromHtml(html,
                        HtmlImageGetter(
                            this
                        ), null) as Spannable
                }
            }

        }

        fun TextView.textWithClickSpan(
            charSequence: CharSequence? = null,
            spanOffset: Pair<Int,Int> = Pair(0, 0),
            spanColor: Int = R.color.colorPrimaryDark,
            clickAction: ((View) -> Unit?)? = null
        ) {
            clickAction?.let { action ->
                movementMethod = LinkMovementMethod.getInstance()

                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(widget: View) {
                        action(widget)
                    }
                }

                text = buildSpannedString {
                    append(charSequence?.subSequence(0..spanOffset.first.minus(1)))
                    color(context.color(spanColor)) {
                        underline { append(
                            charSequence?.subSequence(spanOffset.first, spanOffset.second),
                            clickableSpan,
                            Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                        )}
                    }
                }
            }
        }

        // Input Processing
        fun TextInputLayout.getText(): String? =
            editText?.text.toString()

        fun TextInputLayout.setText(charSequence: CharSequence? = "") {
            editText?.setText(charSequence)
        }

        fun TextInputLayout.textIsEmpty(): Boolean =
            editText?.text.toString().isEmpty()

        fun TextInputLayout.warn(boxName: CharSequence? = "") {
            error = "Mohon isi kolom $boxName"
        }

        fun EditText.addDelayOnType(
            delayMillis: Long = 0,
            action: (String) -> Unit
        ) {
            var job: Job? = null

            addTextChangedListener {
                job?.cancel()

                job = MainScope().launch {
                    delay(delayMillis)
                    action(it.toString())
                }
            }
        }

        // View Processing
        fun View.hide(
            visible: Boolean,
            delayMillis: Long = 0
        ) {
            MainScope().launch {
                delay(delayMillis)
                visibility = if (visible) View.GONE else View.VISIBLE
            }
        }

        fun View.snack(
            message: CharSequence,
            actionText: CharSequence? = null,
            length: Int = Snackbar.LENGTH_SHORT,
            isAnchored: Boolean = false,
            action: ((View) -> Unit?)? = null
        )

//        {
//            Snackbar.make(this, message, length).apply {
//                with(view){
//                    if (isAnchored) {
//                        anchorView = this
//                    }
//
//                    findViewById<TextView>(R.id.snackbar_text).apply {
//                        typeface = context.font(R.font.open_sans_regular)
//                        gravity = Gravity.CENTER
//                        maxLines = 2
//                        ellipsize = TextUtils.TruncateAt.END
//                    }
//
//                    if (!actionText.isNullOrEmpty()){
//                        findViewById<TextView>(R.id.snackbar_text).apply {
//                            typeface = context.font(R.font.open_sans_semibold)
//                        }
//
//                        setAction(actionText) {
//                            if (action != null) {
//                                action(it)
//                            }
//                        }
//                    }
//                }
//            }.show()
//        }
//
//        fun ChipGroup.addChip(
//            chipText: String?,
//            chipStyle: ChipStyle = ChipStyle.ACTION,
//            chipTypeFace: Int = R.font.open_sans_regular,
//            chipIcon: Drawable? = null,
//            chipAttributes: (Chip.() -> Unit)? = null,
//            clickAction: ((View) -> Unit)? = null
//        )
        {
//            val chipStyleId = when(chipStyle) {
//                ChipStyle.ACTION -> R.style.CustomChipStyleAction
//
//                ChipStyle.CHOICE -> R.style.CustomChipStyleChoice
//
//                ChipStyle.ENTRY -> R.style.CustomChipStyleEntry
//
//                ChipStyle.FILTER -> R.style.CustomChipStyleFilter
//            }

//            val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, chipStyleId)
//
//            val chip = Chip(context).apply {
//                setChipDrawable(chipDrawable)
//                text = chipText.string()
//                typeface = context.font(chipTypeFace)
//
//                if (chipIcon != null) {
//                    setChipIcon(chipIcon)
//                }
//
//                if (chipAttributes != null) {
//                    chipAttributes()
//                }

//                isClickable = true
//                isFocusable = true
//
//                setOnClickListener {
//                    if (clickAction != null) {
//                        clickAction(this)
//                    }
//                }
//            }
//
//            addView(chip)
        }
    }
}