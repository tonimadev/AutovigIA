package digital.tonima.autovigia.domain.model

data class SensorData(
    val timestamp: Long,
    val audioFFT: FloatArray,
    val accelerometer: IMUData,
    val gyroscope: IMUData,
    val anomalyScore: Float = 0f
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SensorData) return false
        if (timestamp != other.timestamp) return false
        if (!audioFFT.contentEquals(other.audioFFT)) return false
        if (accelerometer != other.accelerometer) return false
        if (gyroscope != other.gyroscope) return false
        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + audioFFT.contentHashCode()
        result = 31 * result + accelerometer.hashCode()
        result = 31 * result + gyroscope.hashCode()
        return result
    }
}

data class IMUData(
    val x: Float,
    val y: Float,
    val z: Float
)
