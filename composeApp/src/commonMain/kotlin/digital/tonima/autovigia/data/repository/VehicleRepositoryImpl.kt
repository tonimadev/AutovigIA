package digital.tonima.autovigia.data.repository

import digital.tonima.autovigia.data.local.dao.VehicleDao
import digital.tonima.autovigia.data.local.entity.toDomain
import digital.tonima.autovigia.data.local.entity.toEntity
import digital.tonima.autovigia.domain.model.Vehicle
import digital.tonima.autovigia.domain.repository.VehicleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VehicleRepositoryImpl(
    private val vehicleDao: VehicleDao
) : VehicleRepository {
    override fun getVehicle(): Flow<Vehicle?> {
        return vehicleDao.getVehicle().map { it?.toDomain() }
    }

    override suspend fun saveVehicle(vehicle: Vehicle) {
        vehicleDao.upsertVehicle(vehicle.toEntity())
    }

    override suspend fun clearVehicle() {
        vehicleDao.clear()
    }
}
