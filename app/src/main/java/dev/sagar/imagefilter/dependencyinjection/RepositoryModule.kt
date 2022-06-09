package dev.sagar.imagefilter.dependencyinjection

import dev.sagar.imagefilter.repositories.EditImageRepo
import dev.sagar.imagefilter.repositories.EditImageRepoClass
import dev.sagar.imagefilter.repositories.SavedImagesRepo
import dev.sagar.imagefilter.repositories.SavedImagesRepoClass
import org.koin.dsl.module
import org.koin.android.ext.koin.androidContext

val repositoryModule = module {
    factory<EditImageRepo> { EditImageRepoClass(androidContext())  }
    factory<SavedImagesRepo> { SavedImagesRepoClass(androidContext())  }
}