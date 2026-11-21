package com.example.magpie_pinecone.backend.sharedDataClasses

import android.content.Context
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import android.net.Uri

fun getImageAspectRatio(uri: Uri, context: Context): Float {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    context.contentResolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(stream, null, options)
    }

    // Check EXIF orientation so portrait photos rotated 90°/270° don't get flipped ratios
    val rotation = context.contentResolver.openInputStream(uri)?.use { stream ->
        ExifInterface(stream).rotationDegrees
    } ?: 0

    val width = if (rotation == 90 || rotation == 270) options.outHeight else options.outWidth
    val height = if (rotation == 90 || rotation == 270) options.outWidth else options.outHeight

    return if (width > 0 && height > 0) width.toFloat() / height.toFloat() else 4f / 3f
}

data class ImageData(val uri: Uri, val aspectRatio: Float) {
    init {
        require(aspectRatio > 0) { "Aspect ratio cannot be negative" }
    }
}