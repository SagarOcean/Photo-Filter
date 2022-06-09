package dev.sagar.imagefilter.repositories

import android.graphics.Bitmap
import android.net.Uri
import dev.sagar.imagefilter.data.ImageFilter
import java.io.File
import java.io.FileOutputStream

interface EditImageRepo {
    suspend fun prepareImagePreview(imageUri: Uri) : Bitmap?
    suspend fun getImageFilters(image: Bitmap) : List<ImageFilter>
    suspend fun saveFilteredImage(filteredBitmap : Bitmap) : Uri?
}