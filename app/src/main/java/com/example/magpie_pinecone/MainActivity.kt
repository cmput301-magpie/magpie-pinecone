package com.example.magpie_pinecone

import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.magpie_pinecone.ui.theme.MagpiepineconeTheme



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagpiepineconeTheme {
                // Get the Android ID of the device, used for getting account type.
                val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                PineconeApp(androidId)
            }
        }
    }
}