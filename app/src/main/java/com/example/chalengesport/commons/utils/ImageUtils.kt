package com.example.chalengesport.commons.utils

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.example.chalengesport.commons.utils.other.ConstantObject
import java.io.ByteArrayOutputStream

class ImageUtils {
    companion object{

        fun Bitmap?.isEmpty(): Boolean =
            this?.let { width == 0 || height == 0 } ?: false

        fun Bitmap?.getScaledBitmapAtLongestSide(
            targetSize: Int
        ): Bitmap? {
            if (this == null || width <= targetSize && height <= targetSize) return this

            val targetWidth: Int
            val targetHeight: Int

            when {
                height > width -> {
                    targetHeight = targetSize
                    val percentage = targetSize.toFloat() / height
                    targetWidth = (width * percentage).toInt()
                }
                else -> {
                    targetWidth = targetSize
                    val percentage = targetSize.toFloat() / width
                    targetHeight = (height * percentage).toInt()
                }
            }

            return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
        }

        fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)

            val byteArray = stream.toByteArray()
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }

        fun Bitmap?.toBytes(
            format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
        ) = this?.let {
            val outputStream = ByteArrayOutputStream()
            compress(format, 100, outputStream)
            outputStream.toByteArray()
        }

        fun Bitmap?.toDrawable(resources: Resources) =
            BitmapDrawable(resources, this)

        fun Bitmap?.addFrame(
            borderSize: Int,
            color: Int = Color.WHITE,
            recycle: Boolean = false
        ): Bitmap? {
            return this?.let {
                if (isEmpty()) return null

                val newWidth = width + borderSize
                val newHeight = height + borderSize

                val currentBitmap = Bitmap.createBitmap(newWidth, newHeight, config)
                val canvas = Canvas(currentBitmap)

                val rect = Rect(0, 0, newWidth, newHeight)
                val paint = Paint().apply {
                    this.color = color
                    this.style = Paint.Style.STROKE
                    this.strokeWidth = borderSize.toFloat()
                }

                canvas.drawRect(rect, paint)
                canvas.drawBitmap(this, borderSize.toFloat(), borderSize.toFloat(), null)
                if (recycle && !isRecycled) recycle()
                currentBitmap
            }
        }

        @JvmOverloads
        fun Bitmap?.scale(
            newWidth: Float,
            newHeight: Float,
            recycle: Boolean = false
        ): Bitmap? {
            return this?.let {
                val matrix = Matrix()
                matrix.setScale(newWidth,newHeight)
                val bitmap = Bitmap.createBitmap(this,  0, 0, width, height, matrix, true)
                if (recycle && !isRecycled) recycle()
                bitmap
            }
        }

        fun Bitmap?.addTextWatermark(
            text: String?,
            textSize: Float = 12f,
            textColor: Int = Color.WHITE,
            x: Float = 0f,
            y: Float = 0f,
            recycle: Boolean = false
        ): Bitmap? {
            return this?.let {
                if (isEmpty() || text.isNullOrEmpty()) return null

                val currentBitmap = copy(config, true)
                val canvas = Canvas(currentBitmap)
                val paint = Paint().apply {
                    color = textColor
                    setTextSize(textSize)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        getTextBounds(text, 0, text.length, Rect())
                }
                canvas.drawText(
                    text,
                    x,
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) y + textSize
                    else y,
                    paint
                )
                if (recycle && !isRecycled) recycle()
                currentBitmap
            }
        }

        fun ByteArray?.toBitmap() =
            this?.let {
                BitmapFactory.decodeByteArray(it, 0, size)
            }

        fun ByteArray?.isPNG() =
            this?.let {
                size >= 8 && (it[0] == 137.toByte() && it[1] == 80.toByte()
                        && it[2] == 78.toByte() && it[3] == 71.toByte()
                        && it[4] == 13.toByte() && it[5] == 10.toByte()
                        && it[6] == 26.toByte() && it[7] == 10.toByte())
            } ?: false

        fun ByteArray?.isJPG() =
            this?.let {
                (size >= 2 && it[0] == 0xFF.toByte() && it[1] == 0xD8.toByte())
            } ?: false

        fun ByteArray?.isGIF() =
            this?.let {
                (size >= 6
                        && it[0] == 'G'.toByte() && it[1] == 'I'.toByte()
                        && it[2] == 'F'.toByte() && it[3] == '8'.toByte()
                        && (it[4] == '7'.toByte() || it[4] == '9'.toByte()) && it[5] == 'a'.toByte())
            } ?: false

        fun ByteArray?.isBitmap() =
            this?.let {
                (size >= 2 && it[0].toInt() == 0x42 && it[1].toInt() == 0x4d)

            } ?: false

        fun ByteArray?.imageType(): String? =
            when {
                isJPG() -> ConstantObject.ImageExtension.JPG
                isPNG() -> ConstantObject.ImageExtension.PNG
                isGIF() -> ConstantObject.ImageExtension.GIF
                isBitmap() -> ConstantObject.ImageExtension.BITMAP
                else -> null
            }

        fun ByteArray?.toDrawable(resources: Resources) =
            toBitmap().toDrawable(resources)

        fun Drawable?.toBitmap(): Bitmap? =
            when (this) {
                is BitmapDrawable -> {
                    bitmap
                }
                is NinePatchDrawable -> {
                    val bitmap = Bitmap.createBitmap(
                        intrinsicWidth,
                        intrinsicHeight,
                        if (opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
                    )
                    val canvas = Canvas(bitmap)
                    setBounds(0,0, intrinsicWidth,intrinsicHeight)
                    draw(canvas)
                    bitmap
                }
                else -> {
                    null
                }
            }

        fun Drawable?.toBytes(
            format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
        ) = toBitmap().toBytes(format)

        fun View?.toBitmap(): Bitmap? {
            return this?.let {
                val bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                val backgroundDrawable = background
                if (backgroundDrawable != null) {
                    backgroundDrawable.draw(canvas)
                } else {
                    canvas.drawColor(Color.WHITE)
                }
                draw(canvas)
                bitmap
            }
        }

        @Suppress("Deprecation")
        fun Bitmap?.save(
            context: Context,
            fileName: String? = ConstantObject.File.Image.defaultFileName,
            quality: Int = 100,
            format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
        ): Uri? {
            return this?.let {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    compress(format, quality, ByteArrayOutputStream())
                    Uri.parse(
                        MediaStore.Images.Media.insertImage(
                            context.contentResolver,
                            it,
                            fileName + System.currentTimeMillis(),
                            null
                        )
                    )
                } else {
                    val contentValues = ContentValues().apply {
                        put(
                            MediaStore.MediaColumns.DISPLAY_NAME,
                            fileName + System.currentTimeMillis()
                        )
                        put(
                            MediaStore.MediaColumns.MIME_TYPE,
                            ConstantObject.File.MimeType.image
                        )
                        put(
                            MediaStore.MediaColumns.RELATIVE_PATH,
                            "${Environment.DIRECTORY_PICTURES}/${ConstantObject.File.Location.basePath}${ConstantObject.File.Location.storePath}"
                        )
                        put(
                            MediaStore.MediaColumns.IS_PENDING,
                            ConstantObject.File.Pending.isPending
                        )
                    }

                    val resolver = context.contentResolver
                    val uri = resolver.insert(
                        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                        contentValues
                    )

                    uri?.let { currentUri ->
                        resolver.openOutputStream(currentUri).use { output ->
                            compress(format, quality, output)
                        }
                    }

                    contentValues.apply {
                        clear()
                        put(
                            MediaStore.MediaColumns.IS_PENDING,
                            ConstantObject.File.Pending.notPending
                        )
                    }

                    uri?.apply {
                        resolver.update(
                            this,
                            contentValues,
                            null,
                            null
                        )
                    }
                }
            }
        }
    }
}