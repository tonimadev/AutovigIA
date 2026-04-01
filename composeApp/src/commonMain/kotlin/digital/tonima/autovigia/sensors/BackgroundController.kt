package digital.tonima.autovigia.sensors

interface BackgroundController {
    fun start()
    fun stop()
    fun isRunning(): Boolean
}

expect class PlatformBackgroundController() : BackgroundController
