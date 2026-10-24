package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_profiles")
data class GameProfile(
    @PrimaryKey
    val packageName: String,
    val title: String,
    val genre: String = "Action / RPG",
    val isCustomAdded: Boolean = false,
    val targetFps: Int = 90,
    val resolutionScale: String = "1080p FHD",
    val performanceMode: String = "FPS_BOOSTER",
    val antiAliasing: String = "2x MSAA",
    val doNotDisturb: Boolean = true,
    val touchSensitivity: Float = 1.8f,
    val autoBoostOnLaunch: Boolean = true,
    val inGameHudOverlay: Boolean = true,
    val launchCount: Int = 0,
    val lastLaunchedTimestamp: Long = 0L,
    val iconUri: String? = null
)
