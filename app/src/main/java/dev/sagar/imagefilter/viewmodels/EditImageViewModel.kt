package dev.sagar.imagefilter.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sagar.imagefilter.data.ImageFilter
import dev.sagar.imagefilter.repositories.EditImageRepo
import dev.sagar.imagefilter.utilities.Coroutines

class EditImageViewModel(private val editImageRepo: EditImageRepo) : ViewModel() {

    //region:: Prepare Image Preview

    private val imagePreviewDataState = MutableLiveData<ImagePreviewDataState>()
    val imagePreviewUIState : LiveData<ImagePreviewDataState> get() = imagePreviewDataState

    fun prepareImagePreview(imageUri: Uri){
        Coroutines.io {
            kotlin.runCatching {
                emitImagePreviewUIState(isLoading = true)
                editImageRepo.prepareImagePreview(imageUri)
            }.onSuccess { bitmap ->
                if (bitmap != null) {
                    emitImagePreviewUIState(bitmap = bitmap)
                }
                else{
                    emitImagePreviewUIState(error = "Unable to prepare Image preview")
                }
            }.onFailure {
                emitImagePreviewUIState(error = it.message.toString())
            }
        }
    }

    private fun emitImagePreviewUIState(
        isLoading: Boolean = false,
        bitmap: Bitmap?= null,
        error: String?= null
    ){
        val dataState = ImagePreviewDataState(isLoading,bitmap, error)
        imagePreviewDataState.postValue(dataState)
    }

    data class ImagePreviewDataState(
        val isLoading : Boolean,
        val bitmap : Bitmap?,
        val error: String?
    )

    //endregion

    //region:: Load Image Filters

    private val imageFiltersDataState = MutableLiveData<ImageFiltersDataState>()
    val imageFiltersUIState : LiveData<ImageFiltersDataState>  get() = imageFiltersDataState

    fun loadImageFilters(originalImage: Bitmap ){
        Coroutines.io {
            runCatching {
                editImageFiltersUIState(isLoading = true)
                editImageRepo.getImageFilters(getPreviewImage(originalImage))
            }.onSuccess {   imageFilters ->
                editImageFiltersUIState(imageFilters = imageFilters)
            }.onFailure {
                editImageFiltersUIState(error = it.message.toString())
            }
        }
    }

    private fun getPreviewImage(originalImage : Bitmap): Bitmap {
        return runCatching {
            val previewWidth = 150
            val previewHeight = originalImage.height * previewWidth / originalImage.width
            Bitmap.createScaledBitmap(originalImage,previewWidth,previewHeight,false)
        }.getOrDefault(originalImage)
    }

    private fun editImageFiltersUIState(
        isLoading : Boolean = false,
        imageFilters: List<ImageFilter>? = null,
        error : String? = null
    ){
        val dataState = ImageFiltersDataState(isLoading, imageFilters, error)
        imageFiltersDataState.postValue(dataState)
    }

    data class ImageFiltersDataState(
        val isLoading: Boolean,
        val imageFilters : List<ImageFilter>?,
        val error: String?
    )

    //endregion

    //region :: Save Filtered Image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDataState>()
    val saveFilteredImageUiState : LiveData<SaveFilteredImageDataState> get() = saveFilteredImageDataState

    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRepo.saveFilteredImage(filteredBitmap)
            }.onSuccess {
                emitSaveFilteredImageUiState(uri = it)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

    private fun emitSaveFilteredImageUiState(
        isLoading: Boolean = false,
        uri: Uri? = null,
        error: String? = null
    ){
        val dataState = SaveFilteredImageDataState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }

    data class SaveFilteredImageDataState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?
    )

    //endregion
}