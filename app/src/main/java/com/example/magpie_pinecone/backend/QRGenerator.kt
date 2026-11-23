package com.example.magpie_pinecone.backend

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun generateQrCodeBitmap(
    content: String,
    size: Int = 512
): Bitmap? = withContext(Dispatchers.Default) {
    if (content.isBlank()) return@withContext null

    try {
        val bitMat = QRCodeWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            size,
            size,
        )

        val width = bitMat.width
        val height = bitMat.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (bitMat[x, y]) Color.BLACK else Color.WHITE
            }
        }

        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}