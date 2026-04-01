package digital.tonima.autovigia.data.local.dao

import androidx.room.*
import digital.tonima.autovigia.data.local.entity.VehicleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {
    @Query("SELECT * FROM vehicles LIMIT 1")
    fun getVehicle(): Flow<VehicleEntity?>

    @Upsert
    suspend fun upsertVehicle(vehicle: VehicleEntity)

    @Query("DELETE FROM vehicles")
    suspend fun clear()
}
