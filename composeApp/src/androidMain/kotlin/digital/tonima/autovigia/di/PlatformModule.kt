package digital.tonima.autovigia.di

import digital.tonima.autovigia.data.local.getDatabaseBuilder
import digital.tonima.autovigia.sensors.PlatformSensorEngine
import digital.tonima.autovigia.sensors.PermissionManager
import digital.tonima.autovigia.sensors.AndroidPermissionManager
import org.koin.dsl.module

actual fun platformModule() = module {
    single { getDatabaseBuilder(get()) }
    single { PlatformSensorEngine().getEngine() }
    single<PermissionManager> { AndroidPermissionManager(get()) }
}
