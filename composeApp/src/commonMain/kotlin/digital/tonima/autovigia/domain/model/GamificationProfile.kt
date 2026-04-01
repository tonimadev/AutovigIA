package digital.tonima.autovigia.domain.model

data class GamificationProfile(
    val userId: String,
    val totalPoints: Long,
    val tier: Tier = Tier.NOVICE,
    val unlockedRewards: List<Reward> = emptyList()
)

enum class Tier(val label: String, val threshold: Long) {
    NOVICE("Novato", 0),
    CONTRIBUTOR("Contribuinte", 1000),
    MASTER("Contribuinte Master", 5000),
    ELITE("Elite AutoVigIA", 15000)
}

data class Reward(
    val id: String,
    val partner: String,
    val description: String,
    val pointsRequired: Long,
    val code: String? = null
)
