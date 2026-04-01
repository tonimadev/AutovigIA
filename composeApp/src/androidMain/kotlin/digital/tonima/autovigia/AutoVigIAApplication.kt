package digital.tonima.autovigia

import android.app.Application
import digital.tonima.autovigia.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class AutoVigIAApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        initKoin {
            androidLogger()
            androidContext(this@AutoVigIAApplication)
        }
    }
}