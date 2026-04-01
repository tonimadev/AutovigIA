package digital.tonima.autovigia.domain.repository

import digital.tonima.autovigia.domain.model.Vehicle
import kotlinx.coroutines.flow.Flow

interface VehicleRepository {
    fun getVehicle(): Flow<Vehicle?>
    suspend fun saveVehicle(vehicle: Vehicle)
    suspend fun clearVehicle()
}
