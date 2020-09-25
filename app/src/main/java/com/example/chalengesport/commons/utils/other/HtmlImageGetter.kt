package com.example.chalengesport.commons.utils.other

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.text.Html
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.chalengesport.commons.utils.Utils

open class HtmlImageGetter(private var view: TextView) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable {
        val drawable = LevelListDrawable()

        view.run {
            Glide.with(context)
                .asBitmap()
                .load(source)
                .override(
                    Utils.getDeviceWidth(
                        context
                    ) - 120 , Target.SIZE_ORIGINAL)
                .transform(
                    GlideFitTransform(
                        context
                    )
                )
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        drawable.apply {
                            addLevel(1,1, BitmapDrawable(context.resources, resource))
                            setBounds(0,0, resource.width, resource.height)
                            level = 1
                        }
                        this@run.apply {
                            invalidate()
                            text = text
                        }
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                })

        }

        return drawable
    }
}