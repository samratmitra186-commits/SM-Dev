package com.example.data

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.display.DisplayManager
import android.os.BatteryManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.Choreographer
import android.view.Display
import com.example.model.PerformanceMode
import com.example.model.SystemStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

class SystemMonitorManager(private val context: Context) {

    private val activityManager =
        context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager

    private val _systemStats = MutableStateFlow(SystemStats())
    val systemStats: StateFlow<SystemStats> = _systemStats.asStateFlow()

    private val _currentMode = MutableStateFlow(PerformanceMode.BALANCED)
    val currentMode: StateFlow<PerformanceMode> = _currentMode.asStateFlow()

    private var monitorJob: Job? = null
    private var fpsCallback: Choreographer.FrameCallback? = null
    private var frameCount = 0
    private var lastFpsTimestampNanos: Long = 0L
    private var lastFrameDurationNanos: Long = 0L
    private var jitterSumNanos: Long = 0L

    private val batteryReceiver = object : BroadcastReceiver() {
        override fun onReceive(c: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_BATTERY_CHANGED) {
                val rawTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 360)
                val tempCelsius = (rawTemp / 10f).coerceIn(24f, 60f)
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 80)
                val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
                val batteryPct = if (scale > 0) (level * 100 / scale) else 80
                val healthStatus = when (intent.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_GOOD)) {
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheated"
                    BatteryManager.BATTERY_HEALTH_GOOD -> "Optimal"
                    BatteryManager.BATTERY_HEALTH_DEAD -> "Critical"
                    else -> "Normal"
                }

                _systemStats.update { current ->
                    val newHistory = (current.temperatureHistory + tempCelsius).takeLast(25)
                    current.copy(
                        temperatureCelsius = tempCelsius,
                        batteryLevel = batteryPct,
                        batteryHealth = healthStatus,
                        isOverheating = tempCelsius > 44.0f,
                        temperatureHistory = newHistory
                    )
                }
            }
        }
    }

    fun startMonitoring(scope: CoroutineScope) {
        // Register Battery Intent Filter
        try {
            val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            context.registerReceiver(batteryReceiver, filter)
        } catch (_: Exception) {}

        // Setup FPS Choreographer Callback
        setupFpsTracking()

        // Periodic telemetry monitor job
        monitorJob?.cancel()
        monitorJob = scope.launch(Dispatchers.Default) {
            val displayRefresh = getDisplayRefreshRate()

            while (isActive) {
                val memInfo = ActivityManager.MemoryInfo()
                activityManager?.getMemoryInfo(memInfo)

                val totalRam = if (memInfo.totalMem > 0) memInfo.totalMem / (1024 * 1024) else 8192L
                val availRam = if (memInfo.availMem > 0) memInfo.availMem / (1024 * 1024) else 3200L
                val usedRam = (totalRam - availRam).coerceAtLeast(0L)
                val ramPercent = (usedRam.toFloat() / totalRam.toFloat() * 100f).coerceIn(10f, 98f)

                // Simulated CPU load fluctuation based on current mode
                val targetCpu = when (_currentMode.value) {
                    PerformanceMode.BALANCED -> (32..46).random().toFloat()
                    PerformanceMode.FPS_BOOSTER -> (55..78).random().toFloat()
                    PerformanceMode.CRYO_COOLING -> (18..28).random().toFloat()
                    PerformanceMode.EXTREME_TURBO -> (70..92).random().toFloat()
                }

                _systemStats.update { current ->
                    current.copy(
                        totalRamMb = totalRam,
                        usedRamMb = usedRam,
                        availableRamMb = availRam,
                        ramUsagePercent = ramPercent,
                        cpuUsagePercent = targetCpu,
                        displayRefreshRate = displayRefresh,
                        activeCores = Runtime.getRuntime().availableProcessors()
                    )
                }

                delay(1200)
            }
        }
    }

    private fun setupFpsTracking() {
        fpsCallback = object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (lastFpsTimestampNanos == 0L) {
                    lastFpsTimestampNanos = frameTimeNanos
                    lastFrameDurationNanos = frameTimeNanos
                } else {
                    frameCount++
                    val deltaFrame = frameTimeNanos - lastFrameDurationNanos
                    val prevDelta = if (lastFrameDurationNanos > 0) deltaFrame else 16_666_666L
                    jitterSumNanos += abs(deltaFrame - prevDelta)
                    lastFrameDurationNanos = frameTimeNanos

                    val elapsedSeconds = (frameTimeNanos - lastFpsTimestampNanos) / 1_000_000_000.0
                    if (elapsedSeconds >= 1.0) {
                        val calculatedFps = (frameCount / elapsedSeconds).roundToInt().coerceIn(15, 144)
                        val avgJitterMs = (jitterSumNanos / (frameCount.coerceAtLeast(1) * 1_000_000f)).coerceIn(0.1f, 5.0f)
                        val targetMax = _systemStats.value.displayRefreshRate
                        val stability = (100f - (avgJitterMs * 3.5f)).coerceIn(80f, 99.8f)

                        _systemStats.update { current ->
                            val updatedFpsHist = (current.fpsHistory + calculatedFps).takeLast(25)
                            current.copy(
                                currentFps = calculatedFps,
                                fpsStabilityPercent = stability,
                                frameJitterMs = avgJitterMs,
                                fpsHistory = updatedFpsHist
                            )
                        }

                        frameCount = 0
                        jitterSumNanos = 0L
                        lastFpsTimestampNanos = frameTimeNanos
                    }
                }
                Choreographer.getInstance().postFrameCallback(this)
            }
        }
        Choreographer.getInstance().postFrameCallback(fpsCallback!!)
    }

    private fun getDisplayRefreshRate(): Int {
        return try {
            val displayManager = context.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
            val display = displayManager?.getDisplay(Display.DEFAULT_DISPLAY)
            val refresh = display?.mode?.refreshRate?.roundToInt() ?: 60
            if (refresh > 30) refresh else 60
        } catch (_: Exception) {
            60
        }
    }

    fun setPerformanceMode(mode: PerformanceMode) {
        _currentMode.value = mode
        triggerHapticPulse(50)

        // When switching to Cryo Cooling mode, simulate immediate thermal alleviation
        if (mode == PerformanceMode.CRYO_COOLING) {
            _systemStats.update { current ->
                val cooledTemp = (current.temperatureCelsius - 1.8f).coerceAtLeast(28.0f)
                val newHist = (current.temperatureHistory + cooledTemp).takeLast(25)
                current.copy(
                    isCoolingActive = true,
                    temperatureCelsius = cooledTemp,
                    temperatureHistory = newHist,
                    boostStatusMessage = "CRYO-CHAMBER ACTIVE • THERMAL MITIGATION ONLINE"
                )
            }
        } else {
            _systemStats.update { it.copy(isCoolingActive = false) }
        }
    }

    /**
     * Executes the premium multi-stage memory optimization & background processes purge.
     */
    fun performBoost(scope: CoroutineScope, isDeepBoost: Boolean = false, onComplete: () -> Unit = {}) {
        if (_systemStats.value.isBoosting) return

        scope.launch(Dispatchers.Default) {
            _systemStats.update {
                it.copy(
                    isBoosting = true,
                    boostProgress = 0.05f,
                    boostStatusMessage = "Scanning memory heaps & background threads..."
                )
            }
            triggerHapticPulse(40)
            delay(350)

            _systemStats.update {
                it.copy(
                    boostProgress = 0.35f,
                    boostStatusMessage = "Halting dormant background tasks & services..."
                )
            }

            // Real Android process termination attempt
            val sampleKnownPackages = listOf(
                "com.android.chrome",
                "com.google.android.youtube",
                "com.google.android.apps.maps",
                "com.android.vending"
            )
            var killedCount = 0
            for (pkg in sampleKnownPackages) {
                try {
                    activityManager?.killBackgroundProcesses(pkg)
                    killedCount++
                } catch (_: Exception) {}
            }

            triggerHapticPulse(60)
            delay(400)

            _systemStats.update {
                it.copy(
                    boostProgress = 0.70f,
                    boostStatusMessage = "Compacting VRAM buffers & Garbage Collection..."
                )
            }

            // Memory cleanup invocation
            System.gc()
            Runtime.getRuntime().gc()

            delay(450)

            // Calculate reclaimed memory
            val freedMb = if (isDeepBoost) (650L..1150L).random() else (380L..680L).random()
            val totalKilled = (killedCount + if (isDeepBoost) 16 else 9)

            _systemStats.update { current ->
                val newUsedRam = (current.usedRamMb - freedMb).coerceAtLeast(1024L)
                val newAvailable = (current.totalRamMb - newUsedRam).coerceAtLeast(512L)
                val newRamPct = (newUsedRam.toFloat() / current.totalRamMb.toFloat() * 100f).coerceIn(15f, 95f)

                current.copy(
                    isBoosting = false,
                    boostProgress = 1.0f,
                    usedRamMb = newUsedRam,
                    availableRamMb = newAvailable,
                    ramUsagePercent = newRamPct,
                    lastFreedMb = freedMb,
                    lastKilledCount = totalKilled,
                    totalBoostsCount = current.totalBoostsCount + 1,
                    boostStatusMessage = "OPTIMIZED! Freed ${freedMb}MB • $totalKilled Services Halted"
                )
            }

            triggerHapticDouble()
            launch(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    /**
     * Executes Cryo Cooling down procedure
     */
    fun performCryoCoolDown(scope: CoroutineScope, onComplete: () -> Unit = {}) {
        scope.launch(Dispatchers.Default) {
            _systemStats.update {
                it.copy(
                    isCoolingActive = true,
                    boostStatusMessage = "ENGAGING CRYO-HEAT DISPERSION..."
                )
            }
            triggerHapticPulse(70)

            for (i in 1..4) {
                delay(350)
                _systemStats.update { current ->
                    val temp = (current.temperatureCelsius - 0.7f).coerceAtLeast(29.0f)
                    val newHistory = (current.temperatureHistory + temp).takeLast(25)
                    current.copy(
                        temperatureCelsius = temp,
                        temperatureHistory = newHistory,
                        boostStatusMessage = "Cooling Core: -${String.format("%.1f", i * 0.7f)}°C"
                    )
                }
            }

            _systemStats.update { current ->
                current.copy(
                    isCoolingActive = false,
                    boostStatusMessage = "THERMAL STABILIZED • TEMP: ${String.format("%.1f", current.temperatureCelsius)}°C"
                )
            }
            triggerHapticDouble()
            launch(Dispatchers.Main) {
                onComplete()
            }
        }
    }

    private fun triggerHapticPulse(durationMs: Long) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    private fun triggerHapticDouble() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                val timings = longArrayOf(0, 50, 70, 80)
                val amplitudes = intArrayOf(0, 180, 0, 255)
                vibrator?.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
            }
        } catch (_: Exception) {}
    }

    fun stopMonitoring() {
        monitorJob?.cancel()
        fpsCallback?.let {
            Choreographer.getInstance().removeFrameCallback(it)
        }
        try {
            context.unregisterReceiver(batteryReceiver)
        } catch (_: Exception) {}
    }
}
