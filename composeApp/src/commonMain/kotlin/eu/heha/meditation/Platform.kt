package eu.heha.meditation

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform