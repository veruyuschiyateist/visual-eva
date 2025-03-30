package com.vsial.eva.data_camera.processors

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ImageProxy

object BitmapTransformer {

    fun transform(image: ImageProxy, isFrontCamera: Boolean): Bitmap {
        val bitmap = image.toBitmap()
        val rotation = image.imageInfo.rotationDegrees
        return transform(bitmap, rotation, isFrontCamera)
    }

    private fun transform(
        bitmap: Bitmap,
        rotationDegrees: Int,
        isFrontCamera: Boolean = false
    ): Bitmap {
        val matrix = Matrix().apply {
            postRotate(rotationDegrees.toFloat())
            if (isFrontCamera) {
                postScale(-1f, 1f)
            }
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}