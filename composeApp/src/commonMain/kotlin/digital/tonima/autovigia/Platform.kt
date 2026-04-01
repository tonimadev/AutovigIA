package digital.tonima.autovigia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform