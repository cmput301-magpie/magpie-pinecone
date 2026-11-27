package com.example.magpie_pinecone.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.magpie_pinecone.R
import com.example.magpie_pinecone.backend.sharedDataClasses.ImageData
import com.example.magpie_pinecone.backend.sharedDataClasses.getImageAspectRatio
import com.example.magpie_pinecone.ui.theme.PillPurple

@Composable
fun ProfileImage(
    image: ImageData?,
    allowEditing: Boolean = false,
    onUserUpload: (ImageData) -> Unit = {},

) {
    val context = LocalContext.current

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            if (uri != null) {
                val imageData = ImageData(null, getImageAspectRatio(uri, context), uri)
                onUserUpload(imageData)
            }
        }
    )

    val modifier = Modifier
        .size(102.dp)
        .clip(CircleShape)
        .then(
            if (allowEditing) {
                Modifier.clickable {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            } else {
                Modifier
            }
        )

    if (image != null && (image.localUri != null || !image.url.isNullOrEmpty())) {
        AsyncImage(
            model = image.localUri ?: image.url,
            contentDescription = "Profile image",
            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
            error = painterResource(android.R.drawable.ic_menu_report_image),
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        if (allowEditing) {
            Icon(
                painter = painterResource(R.drawable.outline_add_24),
                contentDescription = "Add image",
                tint = PillPurple,
                modifier = modifier
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "Profile",
                tint = PillPurple,
                modifier = modifier
            )
        }
    }
}
