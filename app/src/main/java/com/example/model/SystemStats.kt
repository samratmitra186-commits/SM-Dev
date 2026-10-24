package com.example.model

data class SystemStats(
    // RAM Metrics
    val totalRamMb: Long = 8192L,
    val usedRamMb: Long = 5240L,
    val availableRamMb: Long = 2952L,
    val ramUsagePercent: Float = 64f,

    // CPU Metrics
    val cpuUsagePercent: Float = 38f,
    val activeCores: Int = 8,
    val cpuClockGhz: Float = 2.84f,

    // Thermal / Battery
    val temperatureCelsius: Float = 36.5f,
    val batteryLevel: Int = 82,
    val batteryHealth: String = "Good",
    val isOverheating: Boolean = false,
    val isCoolingActive: Boolean = false,

    // Real-Time Frame Rate
    val currentFps: Int = 60,
    val displayRefreshRate: Int = 120,
    val fpsStabilityPercent: Float = 98.6f,
    val frameJitterMs: Float = 0.4f,

    // Boost Optimization State
    val isBoosting: Boolean = false,
    val boostProgress: Float = 0f,
    val boostStatusMessage: String = "SYSTEM OPTIMIZED",
    val lastFreedMb: Long = 0L,
    val lastKilledCount: Int = 0,
    val totalBoostsCount: Int = 0,

    // Historical Tracing Points (last 20 samples for live charts)
    val temperatureHistory: List<Float> = listOf(36.2f, 36.4f, 36.5f, 36.8f, 36.6f, 36.5f),
    val fpsHistory: List<Int> = listOf(60, 59, 60, 60, 58, 60)
)
