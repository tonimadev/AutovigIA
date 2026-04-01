package digital.tonima.autovigia.di

import digital.tonima.autovigia.data.local.getDatabaseBuilder
import digital.tonima.autovigia.sensors.PlatformSensorEngine
import digital.tonima.autovigia.sensors.BackgroundController
import digital.tonima.autovigia.sensors.PlatformBackgroundController
import digital.tonima.autovigia.sensors.PermissionManager
import digital.tonima.autovigia.sensors.IosPermissionManager
import org.koin.dsl.module

actual fun platformModule() = module {
    single { getDatabaseBuilder() }
    single { PlatformSensorEngine().getEngine() }
    single<BackgroundController> { PlatformBackgroundController() }
    single<PermissionManager> { IosPermissionManager() }
}
