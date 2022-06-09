package dev.sagar.imagefilter.listeneres

import dev.sagar.imagefilter.data.ImageFilter

interface ImageFilterListener {
    fun onFilterSelected(imageFilter: ImageFilter)
}