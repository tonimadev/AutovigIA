package digital.tonima.autovigia.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import digital.tonima.autovigia.domain.model.Vehicle
import digital.tonima.autovigia.domain.model.VehicleStatus

@Entity(tableName = "vehicles")
data class VehicleEntity(
    @PrimaryKey val id: Int = 1, // Only one vehicle at a time for simplicity
    val brand: String,
    val model: String,
    val year: Int,
    val engine: String,
    val odometer: Long,
    val initialStatus: String // Store as String for simplicity or use Converters
)

fun VehicleEntity.toDomain() = Vehicle(
    brand = brand,
    model = model,
    year = year,
    engine = engine,
    odometer = odometer,
    initialStatus = VehicleStatus.valueOf(initialStatus)
)

fun Vehicle.toEntity() = VehicleEntity(
    brand = brand,
    model = model,
    year = year,
    engine = engine,
    odometer = odometer,
    initialStatus = initialStatus.name
)
