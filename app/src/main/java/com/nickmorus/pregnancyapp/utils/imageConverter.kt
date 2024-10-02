package com.nickmorus.pregnancyapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.nickmorus.pregnancyapp.R


fun base64ToBitmap(base64String: String?): Bitmap? {
    if (base64String.isNullOrEmpty()) return null
    return try {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    } catch (e: IllegalArgumentException) {
        null
    }
}
fun bitmapToPainter(bitmap: Bitmap): Painter {
    return BitmapPainter(bitmap.asImageBitmap())
}
@Composable
fun Base64Image(base64String: String?, modifier: Modifier) {
    val bitmap = base64String?.let { base64ToBitmap(it) }
    val painter = bitmap?.let { bitmapToPainter(it) }

    Image(
        painter = painter ?: painterResource(R.drawable.placeholder), // Заглушка если картинка не загружена
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
