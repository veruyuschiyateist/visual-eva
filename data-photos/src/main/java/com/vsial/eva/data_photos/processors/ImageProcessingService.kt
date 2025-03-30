package com.vsial.eva.data_photos.processors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import com.vsial.eva.domain_photos.entities.ImageFilterType
import androidx.core.net.toUri
import androidx.core.graphics.createBitmap
import com.vsial.eva.data_photos.mappers.toColorMatrix

object ImageProcessingService {

    fun generateFilteredBitmap(context: Context, uri: String, filter: ImageFilterType): Bitmap {
        val imageUri = uri.toUri()
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val sourceBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val filteredBitmap = createBitmap(sourceBitmap.width, sourceBitmap.height)

        val canvas = Canvas(filteredBitmap)
        val paint = Paint()

        filter.toColorMatrix()?.let {
            paint.colorFilter = ColorMatrixColorFilter(it)
        }

        canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

        return filteredBitmap
    }

}