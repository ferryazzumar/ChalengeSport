package com.example.chalengesport.commons.utils.other

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.example.chalengesport.commons.utils.ImageUtils.Companion.getScaledBitmapAtLongestSide
import com.example.chalengesport.commons.utils.Utils
import java.security.MessageDigest

class GlideFitTransform(
    private val context: Context
) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update("fit transformation".toByteArray())
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return toTransform.getScaledBitmapAtLongestSide(Utils.getDeviceWidth(context) - 120)
    }

}

