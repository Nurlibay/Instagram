package uz.texnopos.instagram

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.texnopos.instagram.di.dataModule
import uz.texnopos.instagram.di.viewModelModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val modules = listOf(dataModule, viewModelModule)
        startKoin { // use AndroidLogger as Koin Logger - default Level.INFO
            androidLogger()

            // use the Android context given there
            androidContext(this@App)

            // load properties from assets/koin.properties file
            androidFileProperties()

            // module list
            koin.loadModules(modules)
        }
    }
}