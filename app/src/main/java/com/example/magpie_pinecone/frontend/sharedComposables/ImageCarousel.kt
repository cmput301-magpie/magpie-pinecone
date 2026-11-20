package com.example.magpie_pinecone.frontend.sharedComposables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.example.magpie_pinecone.R
import coil3.compose.AsyncImage

@Composable
fun ImageCarousel(imgUris: List<Uri>, height: Dp = 200.dp, allowUploading: Boolean = false, onUpload: (uris: List<Uri>) -> Unit = {}) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10),
        onResult = { uris -> onUpload(uris) }
    )

    LazyRow(modifier = Modifier.height(height).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp,
        Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {

        items(
            imgUris,
            key = { uri -> uri.toString() }
        ) { uri ->
            ImageCard(uri, height)
        }

        if (allowUploading){
            // The add new images button
            item (key = "Upload Button") {
                /*
                Box(modifier = Modifier.width(height * 16f/9).height(height).border(
                    border = BorderStroke(2.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp)
                )) {

                }
                */
                OutlinedIconButton(modifier = Modifier.height(height).width(height * 16f/9),
                    border = BorderStroke(2.dp, Color.Gray),
                    shape = RoundedCornerShape(8.dp),
                    onClick = {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_add_24),
                        contentDescription = "Add new images"
                    )
                }
            }
        }
    }
}

// TODO: Accommodate different aspect ratios? Right now it forces images to 16:9
@Composable
fun ImageCard(url: Uri, height: Dp) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.height(height)) {
        AsyncImage(
            model = url,
            contentDescription = "Image card",
            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
            error = painterResource(android.R.drawable.ic_menu_report_image),
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(height)
        )
    }
}

@Composable
@Preview
fun ImageCarouselPreview() {
    val context = LocalContext.current
    val packageName = context.packageName

    ImageCarousel(listOf(
        "android.resource://$packageName/${R.drawable.placeholdercampsiteimage}".toUri(),
        "android.resource://$packageName/${R.drawable.placeholdercampsiteimage}".toUri(),
        "android.resource://$packageName/${R.drawable.placeholdercampsiteimage}".toUri(),
        "android.resource://$packageName/${R.drawable.placeholdercampsiteimage}".toUri(),
    ), allowUploading = true) { }
}