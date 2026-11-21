package com.example.magpie_pinecone.backend.sharedDataClasses

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
    fun addImages(images: List<ImageData>) {
        _images.update { it + images }
    }

    /**
     * Deletes an image from the list of images.
     *
     * @param image The image to delete
     */
    fun deleteImage(image: ImageData) {
        _images.update { it - image }
    }
}