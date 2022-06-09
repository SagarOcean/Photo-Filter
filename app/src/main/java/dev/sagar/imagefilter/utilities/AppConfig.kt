package dev.sagar.imagefilter.utilities

import android.app.Application
import dev.sagar.imagefilter.dependencyinjection.repositoryModule
import dev.sagar.imagefilter.dependencyinjection.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class AppConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@AppConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
    }
}