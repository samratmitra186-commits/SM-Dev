package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.GameProfile
import com.example.model.PerformanceMode
import com.example.model.SystemStats
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberButtonStyle
import com.example.ui.components.LiveTelemetryChart
import com.example.ui.components.PerformanceGauge
import com.example.ui.theme.BoostPurple
import com.example.ui.theme.CryoIce
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberBorderGlow
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.HyperBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PerformanceOptimal
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThermalCritical

@Composable
fun HomeScreen(
    systemStats: SystemStats,
    performanceMode: PerformanceMode,
    games: List<GameProfile>,
    onQuickBoost: () -> Unit,
    onDeepBoost: () -> Unit,
    onActivateFpsBooster: () -> Unit,
    onActivateCryoCooling: () -> Unit,
    onSelectGameForEdit: (GameProfile) -> Unit,
    onLaunchGame: (GameProfile) -> Unit,
    onNavigateToGames: () -> Unit,
    onReplayBoot: () -> Unit,
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
            // Top App Bar / Futuristic Title Strip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CyberSurfaceElevated)
                            .border(1.dp, CyberBorderGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_game_logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "APEX BOOSTER",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(PerformanceOptimal)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "GAMING ENGINE ONLINE",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = NeonCyan
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onReplayBoot,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyberSurface)
                            .border(1.dp, CyberBorder, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Replay Boot Animation",
                            tint = NeonCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // Animated Performance HUD Gauge with Quick Boost Core
        item {
            PerformanceGauge(
                systemStats = systemStats,
                onQuickBoost = onQuickBoost
            )
        }

        // Hardware Optimization Fast-Action Modes
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "HARDWARE PROFILES",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // FPS Booster Mode Card
                    val isFpsActive = performanceMode == PerformanceMode.FPS_BOOSTER
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.5.dp,
                                if (isFpsActive) NeonCyan else CyberBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onActivateFpsBooster() }
                            .testTag("mode_fps_booster"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFpsActive) CyberSurfaceVariant else CyberSurface
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = "FPS Booster",
                                    tint = if (isFpsActive) NeonCyan else HyperBlue,
                                    modifier = Modifier.size(24.dp)
                                )
                                if (isFpsActive) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(NeonCyan.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "ACTIVE",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = NeonCyan
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "FPS BOOSTER",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = "Lock 120Hz & Purge RAM",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    // Cryo-Cooling Mode Card
                    val isCryoActive = performanceMode == PerformanceMode.CRYO_COOLING || systemStats.isCoolingActive
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.5.dp,
                                if (isCryoActive) CryoIce else CyberBorder,
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { onActivateCryoCooling() }
                            .testTag("mode_cryo_cooling"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCryoActive) CyberSurfaceVariant else CyberSurface
                        )
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AcUnit,
                                    contentDescription = "Cryo-Cooling",
                                    tint = if (isCryoActive) CryoIce else TextMuted,
                                    modifier = Modifier.size(24.dp)
                                )
                                if (isCryoActive) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(CryoIce.copy(alpha = 0.2f))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "CRYO ON",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            color = CryoIce
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "CRYO-COOLING",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = "Heat Downclock & Chill",
                                fontSize = 10.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // Live Real-Time Temperature Tracing Chart
        item {
            LiveTelemetryChart(
                title = "Hardware Thermal Tracing",
                history = systemStats.temperatureHistory,
                unit = "°C",
                currentValue = systemStats.temperatureCelsius,
                minVal = 26f,
                maxVal = 48f,
                lineColor = if (systemStats.temperatureCelsius > 42f) ThermalCritical else CryoIce
            )
        }

        // Installed Games Quick Launch Carousel
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "INSTALLED GAMES DASHBOARD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "VIEW ALL (${games.size})",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary,
                        modifier = Modifier.clickable { onNavigateToGames() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (games.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(CyberSurface)
                            .border(1.dp, CyberBorder, RoundedCornerShape(14.dp))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Scanning device for games...",
                            color = TextMuted,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(games.take(6), key = { it.packageName }) { game ->
                            GameQuickLaunchCard(
                                game = game,
                                onLaunch = { onLaunchGame(game) },
                                onConfigure = { onSelectGameForEdit(game) }
                            )
                        }
                    }
                }
            }
        }

        // Deep Turbo Optimizer Banner Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, CyberBorderGlow, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberSurfaceElevated)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "DEEP VRAM & HEAP PURGE",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = "Kills background wake-locks & compresses memory buffers",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(NeonCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CyberButton(
                            text = if (systemStats.isBoosting) "PURGING..." else "DEEP SYSTEM CLEANSE",
                            icon = Icons.Default.RocketLaunch,
                            style = CyberButtonStyle.PRIMARY,
                            enabled = !systemStats.isBoosting,
                            onClick = onDeepBoost,
                            modifier = Modifier.weight(1f),
                            testTag = "deep_turbo_boost_button"
                        )
                    }

                    if (systemStats.totalBoostsCount > 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "TOTAL SESSIONS OPTIMIZED: ${systemStats.totalBoostsCount} • LAST FREED: ${systemStats.lastFreedMb} MB",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = PerformanceOptimal
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun GameQuickLaunchCard(
    game: GameProfile,
    onLaunch: () -> Unit,
    onConfigure: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            // Header Row: Target FPS Pill & Settings Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ElectricBlue.copy(alpha = 0.25f))
                        .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${game.targetFps} FPS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan
                    )
                }

                IconButton(
                    onClick = onConfigure,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Configure Game",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = game.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                maxLines = 1
            )
            Text(
                text = game.genre,
                fontSize = 10.sp,
                color = TextMuted,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Fast Launch button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(ElectricBlue, NeonCyan)
                        )
                    )
                    .clickable { onLaunch() }
                    .padding(vertical = 8.dp)
                    .testTag("launch_game_${game.packageName}"),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Launch",
                        tint = CyberBackground,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "BOOST & PLAY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = CyberBackground
                    )
                }
            }
        }
    }
}
