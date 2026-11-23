package com.example.magpie_pinecone.frontend.ownerScreens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.backend.generateQrCodeBitmap
import com.example.magpie_pinecone.ui.components.PineconeTopBar

@Composable
fun CampsiteQRCodeScreen(campsiteID: String, campsiteName: String, onBack: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(30.dp)) {
        PineconeTopBar("QR Code", onBack)

        val size: Dp = 250.dp

        val density = LocalDensity.current
        val sizePx = with(density) { size.roundToPx() }

        var qrCodeBitmap by remember(campsiteID) { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(campsiteID) {
            qrCodeBitmap = generateQrCodeBitmap(campsiteID, sizePx)
        }

        Text("Scan this QR code to view the details of \"$campsiteName\"!")

        val bitmap = qrCodeBitmap
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(size)
            )
        } else {
            Box(
                modifier = Modifier.size(size).background(Color.White)
            )
        }
    }
}

@Preview
@Composable
fun CampsiteQRCodeScreenPreview() {
    CampsiteQRCodeScreen("AKLSDLASDJLASKDJLKASDJLADJALSDKJALKSDJA", "Preview campsite", {})
}