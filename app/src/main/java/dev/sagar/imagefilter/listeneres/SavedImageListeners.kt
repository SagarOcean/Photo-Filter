package dev.sagar.imagefilter.listeneres

import java.io.File

interface SavedImageListeners {
    fun onImageClicked(file : File)
}