package com.example.magpie_pinecone.backend.sharedDataClasses

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.magpie_pinecone.backend.deleteImageFromFirebase
import com.example.magpie_pinecone.backend.uploadImageToFirebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A ViewModel for managing image data. Makes it survive screen rotations
 *
 * Based off of https://developer.android.com/topic/libraries/architecture/viewmodel
 *
 * @param _images The mutable list of images
 * @param images The list of images
 */
class ImageViewModel : ViewModel() {
    private val _images = MutableStateFlow<List<ImageData>>(emptyList())
    val images: StateFlow<List<ImageData>> = _images.asStateFlow()

    /**
     * Adds an image to the list of images.
     *
     * @param image The image to add
     */
    private fun addImage(image: ImageData) {
        _images.update { it + image }
    }


    /**
     * Deletes an image from the list of images.
     *
     * @param image The image to delete
     */
    private fun deleteImage(image: ImageData) {
        _images.update { it - image }
    }

    /**
     * Uploads images to firebase and adds them to the list of images.
     */
    fun addImagesAndUploadToDB(images: List<ImageData>) {
        images.forEach { imageData ->
            viewModelScope.launch {
                val uri = imageData.localUri ?: return@launch

                uploadImageToFirebase(uri).onSuccess { downloadUrl ->
                    imageData.localUri = null
                    imageData.url = downloadUrl
                    addImage(imageData)
                }
            }
        }
    }

    fun setImages(images: List<ImageData>) {
        _images.value = images
    }

    /**
     * Deletes an image from firebase and also removes it from the list of images
     */
    fun deleteImageAndDeleteFromDB(image: ImageData) {
        viewModelScope.launch {
            val url = image.url ?: return@launch

            deleteImageFromFirebase(url).onSuccess {
                _images.update { list ->
                    list.filterNot { it == image }
                }
            }.onFailure { exception ->
                Log.e("Failed to delete image from database: ", exception.message ?: "")
            }
        }
    }
}