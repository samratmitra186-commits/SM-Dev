package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PerformanceMode
import com.example.model.SystemStats
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberButtonStyle
import com.example.ui.components.LiveTelemetryChart
import com.example.ui.theme.BoostPurple
import com.example.ui.theme.CryoFrost
import com.example.ui.theme.CryoIce
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberBorderGlow
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.FlameOrange
import com.example.ui.theme.HyperBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PerformanceOptimal
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThermalCritical
import com.example.ui.theme.ThermalWarning

@Composable
fun HardwareOptimizerScreen(
    systemStats: SystemStats,
    performanceMode: PerformanceMode,
    onSetMode: (PerformanceMode) -> Unit,
    onDeepCleanse: () -> Unit,
    onCoolDown: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Text(
                    text = "HARDWARE OPTIMIZER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "KERNEL GOVERNOR • FPS BOOST & CRYO THERMAL ENGINE",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
            }
        }

        // Mode 1: Ultra FPS Booster Mode Feature Card
        item {
            val isFpsModeActive = performanceMode == PerformanceMode.FPS_BOOSTER
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        1.5.dp,
                        if (isFpsModeActive) NeonCyan else CyberBorder,
                        RoundedCornerShape(18.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isFpsModeActive) CyberSurfaceElevated else CyberSurface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ElectricBlue.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = NeonCyan,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "INCREASE FPS BOOSTER MODE",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "GPU rendering overdrive & 120Hz frame lock",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (isFpsModeActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonCyan.copy(alpha = 0.2f))
                                    .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "ENGAGED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = NeonCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Spec pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecPill(label = "TARGET FPS", value = "120 FPS MAX", color = NeonCyan)
                        SpecPill(label = "SCHEDULER", value = "PERFORMANCE", color = HyperBlue)
                        SpecPill(label = "TOUCH POLLING", value = "240 HZ", color = PerformanceOptimal)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    CyberButton(
                        text = if (isFpsModeActive) "FPS BOOSTER ACTIVE (MAX BIAS)" else "ACTIVATE FPS BOOSTER MODE",
                        icon = Icons.Default.Bolt,
                        style = CyberButtonStyle.PRIMARY,
                        onClick = { onSetMode(PerformanceMode.FPS_BOOSTER) },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "activate_fps_booster_btn"
                    )
                }
            }
        }

        // Mode 2: Temperature Cooling Down Mode Feature Card
        item {
            val isCryoActive = performanceMode == PerformanceMode.CRYO_COOLING || systemStats.isCoolingActive
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(
                        1.5.dp,
                        if (isCryoActive) CryoIce else CyberBorder,
                        RoundedCornerShape(18.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCryoActive) CyberSurfaceElevated else CyberSurface
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(CryoIce.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = null,
                                    tint = CryoIce,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "TEMPERATURE COOLING DOWN MODE",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Thermal throttling mitigation & heat alleviation",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        if (isCryoActive) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(CryoIce.copy(alpha = 0.2f))
                                    .border(1.dp, CryoIce, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "CRYO ACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = CryoIce
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SpecPill(label = "CORE TEMP", value = "${String.format("%.1f", systemStats.temperatureCelsius)}°C", color = CryoIce)
                        SpecPill(label = "THERMAL STATE", value = systemStats.batteryHealth.uppercase(), color = PerformanceOptimal)
                        SpecPill(label = "TARGET CHILL", value = "-3.5°C", color = CryoFrost)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    CyberButton(
                        text = if (systemStats.isCoolingActive) "CRYO DISPERSING HEAT..." else "INITIATE TEMPERATURE COOL DOWN",
                        icon = Icons.Default.AcUnit,
                        style = CyberButtonStyle.CRYO,
                        onClick = onCoolDown,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "activate_cryo_cooling_btn"
                    )
                }
            }
        }

        // Live Real-Time FPS Stability Waveform Chart
        item {
            val fpsFloats = systemStats.fpsHistory.map { it.toFloat() }
            LiveTelemetryChart(
                title = "Real-Time FPS Performance & Frame Pacing",
                history = if (fpsFloats.isEmpty()) listOf(60f, 60f) else fpsFloats,
                unit = " FPS",
                currentValue = systemStats.currentFps.toFloat(),
                minVal = 30f,
                maxVal = 144f,
                lineColor = NeonCyan
            )
        }

        // Live System Hardware Resource Meters
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, CyberBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "HARDWARE TELEMETRY GAUGES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // CPU Meter
                    MetricProgressBar(
                        title = "OCTA-CORE CPU LOAD",
                        detail = "${systemStats.cpuUsagePercent.toInt()}% @ ${systemStats.cpuClockGhz} GHz",
                        progress = systemStats.cpuUsagePercent / 100f,
                        color = if (systemStats.cpuUsagePercent > 80f) FlameOrange else NeonCyan
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // RAM Meter
                    MetricProgressBar(
                        title = "MEMORY ALLOCATION",
                        detail = "${systemStats.usedRamMb} MB / ${systemStats.totalRamMb} MB (${systemStats.ramUsagePercent.toInt()}%)",
                        progress = systemStats.ramUsagePercent / 100f,
                        color = if (systemStats.ramUsagePercent > 80f) ThermalCritical else ElectricBlue
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Frame Stability Meter
                    MetricProgressBar(
                        title = "FRAME PACING STABILITY",
                        detail = "${String.format("%.1f", systemStats.fpsStabilityPercent)}% (${String.format("%.1f", systemStats.frameJitterMs)}ms jitter)",
                        progress = systemStats.fpsStabilityPercent / 100f,
                        color = PerformanceOptimal
                    )
                }
            }
        }

        // Deep Cleanse Action Bar
        item {
            CyberButton(
                text = "EXECUTE DEEP VRAM PURGE & CACHE CLEAN",
                icon = Icons.Default.RocketLaunch,
                style = CyberButtonStyle.TURBO,
                onClick = onDeepCleanse,
                modifier = Modifier.fillMaxWidth(),
                testTag = "deep_cleanse_button"
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SpecPill(
    label: String,
    value: String,
    color: Color
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberBackground)
            .border(1.dp, CyberBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            fontFamily = FontFamily.Monospace,
            color = TextMuted
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = color
        )
    }
}

@Composable
private fun MetricProgressBar(
    title: String,
    detail: String,
    progress: Float,
    color: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = TextSecondary
            )
            Text(
                text = detail,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF132240)
        )
    }
}
