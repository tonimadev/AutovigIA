package digital.tonima.autovigia.di

import digital.tonima.autovigia.data.local.AppDatabase
import digital.tonima.autovigia.data.repository.VehicleRepositoryImpl
import digital.tonima.autovigia.domain.repository.VehicleRepository
import digital.tonima.autovigia.domain.usecase.VehicleStateMachine
import digital.tonima.autovigia.ui.features.onboarding.OnboardingViewModel
import digital.tonima.autovigia.ui.features.permissions.PermissionViewModel
import digital.tonima.autovigia.ui.features.home.DetectionViewModel
import digital.tonima.autovigia.domain.usecase.DetectionManager
import digital.tonima.autovigia.sensors.BackgroundController
import digital.tonima.autovigia.sensors.PlatformSensorEngine
import digital.tonima.autovigia.sensors.SensorEngine
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(commonModule(), platformModule())
    }

fun commonModule() = module {
    single<VehicleRepository> { VehicleRepositoryImpl(get()) }
    single { CoroutineScope(Dispatchers.Default + SupervisorJob()) }
    single { PlatformSensorEngine().getEngine() }
    single { VehicleStateMachine(get(), get()) }
    viewModel { OnboardingViewModel(get(), get()) }
    viewModel { PermissionViewModel(get()) }
    single { DetectionManager(get(), get(), get(), get()) }
    viewModel { DetectionViewModel(get()) }
    single { 
        val builder: RoomDatabase.Builder<AppDatabase> = get()
        builder
            .fallbackToDestructiveMigration(true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().vehicleDao() }
}

expect fun platformModule(): org.koin.core.module.Module
