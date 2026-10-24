package com.example.model

data class GameServerPing(
    val serverName: String,
    val region: String,
    val host: String,
    val pingMs: Int,
    val jitterMs: Int,
    val packetLossPercent: Float,
    val statusGrade: String // "S", "A", "B", "C"
)

data class NetworkStats(
    val isTesting: Boolean = false,
    val averagePingMs: Int = 24,
    val currentJitterMs: Int = 2,
    val packetLossPercent: Float = 0.0f,
    val connectionType: String = "Wi-Fi 6 (5 GHz)",
    val signalStrengthPercent: Int = 94,
    val connectionGrade: String = "S-TIER",
    val isDnsBoosterActive: Boolean = true,
    val activeDnsRoute: String = "Cloudflare Gaming Warp (1.1.1.1 / 1.0.0.1)",
    val servers: List<GameServerPing> = listOf(
        GameServerPing("Asia Pacific Central", "Tokyo, JP", "1.1.1.1", 19, 1, 0.0f, "S"),
        GameServerPing("North America East", "Virginia, US", "8.8.8.8", 68, 3, 0.0f, "A"),
        GameServerPing("Europe West", "Frankfurt, DE", "9.9.9.9", 84, 4, 0.0f, "B"),
        GameServerPing("Southeast Asia", "Singapore, SG", "208.67.222.222", 32, 2, 0.0f, "S"),
        GameServerPing("Global BattleNet Gateway", "US-West", "4.2.2.2", 72, 3, 0.0f, "A")
    ),
    val pingHistory: List<Int> = listOf(22, 24, 25, 23, 24, 28, 24, 23, 24)
)
