package com.example.magpie_pinecone.frontend.sharedComposables

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.magpie_pinecone.backend.sharedDataClasses.ImageData
import com.example.magpie_pinecone.backend.sharedDataClasses.getImageAspectRatio


/**
 * Creates an image carousel. It does not wrap around when the user scrolls to the end.
 * 
 * @param imgDataList The list of image URIs to display.
 * @param height The height of the carousel (images will be scaled to this height).
 * @param allowModifying Whether the user can modify the images.
 * @param onUpload Called when images are uploaded.
 * @param onDelete Called when an image is deleted.
 */
@Composable
fun ImageCarousel(imgDataList: List<ImageData>, height: Dp = 200.dp, allowModifying: Boolean = false, onUpload: (uploadedImages: List<ImageData>) -> Unit = {}, onDelete: (deletedImage: ImageData) -> Unit = {}) {
    val context = LocalContext.current

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = 10),
        onResult = { uris ->
            val imageUriList: MutableList<ImageData> = mutableListOf()
            for (uri in uris) {
                imageUriList.add(ImageData(null, getImageAspectRatio(uri, context), uri))
            }
            onUpload(imageUriList.toList())
        }
    )

    LazyRow(modifier = Modifier.height(height).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp,
        Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {

        items(
            imgDataList,
            key = { uri -> uri.toString() }
        ) { uri ->
            ImageCard(uri, height, allowModifying, onDelete)
        }

        if (allowModifying){
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

/**
 * Creates an image card.
 *
 * @param imageData The image to display.
 * @param height The height of the card.
 * @param allowModifying Whether the user can modify the image.
 * @param onDelete Called when the image is deleted.
 */
@Composable
private fun ImageCard(imageData: ImageData, height: Dp, allowModifying: Boolean, onDelete: (deletedImage: ImageData) -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(height)
            .aspectRatio(imageData.aspectRatio)
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = imageData.localUri ?: imageData.url,
            contentDescription = "Image card",
            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
            error = painterResource(android.R.drawable.ic_menu_report_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (allowModifying) {
            // The delete button
            FilledIconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { onDelete(imageData) },
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = Color.Black
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_outline_24),
                    contentDescription = "Delete image",
                )
            }
        }
    }
}

/**
 * Preview of the image carousel. (Do we even need a kdocs comment for this?)
 */
@Composable
@Preview
fun ImageCarouselPreview() {
    val context = LocalContext.current
    val packageName = context.packageName

    ImageCarousel(listOf(
        ImageData("", 16f/9, "android.resource://$packageName/${R.drawable.placeholdercampsiteimage}".toUri()),
    ), allowModifying = true) { }
}