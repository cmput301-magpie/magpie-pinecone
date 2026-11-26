package com.example.magpie_pinecone.frontend.entrantScreens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.magpie_pinecone.ui.components.PineconeTopBar
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

@Composable
fun ScanQRCodeScreen(onBack: () -> Unit, onScanSuccessful: (campsiteID: String) -> Unit) {
    val context = LocalContext.current

    // 1. Configure the scanner (restrict to QR codes & enable auto-zoom)
    val scanner = remember(context) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .enableAutoZoom() // Automatically zooms in on distant QR codes
            .build()

        GmsBarcodeScanning.getClient(context, options)
    }



    Column(modifier = Modifier.fillMaxSize().padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        PineconeTopBar("Scan Code", onBack)
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // 2. Launch the Google-managed camera overlay
                scanner.startScan()
                    .addOnSuccessListener { code ->
                        // The raw string inside the QR code
                        if (code.rawValue != null) {
                            onScanSuccessful(code.rawValue!!)
                        } else {

                        }
                    }
            }
        ) {
            Text("Scan QR Code")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}