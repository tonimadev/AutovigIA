package digital.tonima.autovigia.data.local

import androidx.room.*
import digital.tonima.autovigia.data.local.dao.VehicleDao
import digital.tonima.autovigia.data.local.entity.VehicleEntity

@Database(entities = [VehicleEntity::class], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}
