package dev.sagar.imagefilter.repositories

import android.graphics.Bitmap
import java.io.File

interface SavedImagesRepo {
    suspend fun loadSavedImages(): List<Pair<File,Bitmap>>?
}