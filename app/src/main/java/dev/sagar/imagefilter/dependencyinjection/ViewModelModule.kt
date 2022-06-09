package dev.sagar.imagefilter.dependencyinjection

import dev.sagar.imagefilter.viewmodels.EditImageViewModel
import dev.sagar.imagefilter.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ EditImageViewModel(editImageRepo = get())}
    viewModel { SavedImagesViewModel(savedImagesRepo = get()) }
}