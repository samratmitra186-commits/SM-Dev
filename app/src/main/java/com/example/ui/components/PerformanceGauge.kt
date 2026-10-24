package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SystemStats
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
import com.example.ui.theme.ThermalWarning
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PerformanceGauge(
    systemStats: SystemStats,
    onQuickBoost: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pulse animation for cyber core
    val infiniteTransition = rememberInfiniteTransition(label = "gauge_glow")
    val pulseRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val coreGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "core_glow"
    )

    // Animated values for gauge needles and arcs
    val animatedRamUsage = remember { Animatable(systemStats.ramUsagePercent) }
    LaunchedEffect(systemStats.ramUsagePercent) {
        animatedRamUsage.animateTo(
            targetValue = systemStats.ramUsagePercent,
            animationSpec = tween(600, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CyberSurface,
                        CyberBackground
                    )
                )
            )
            .border(1.dp, CyberBorder, RoundedCornerShape(24.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Header Tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (systemStats.isBoosting) NeonCyan else PerformanceOptimal)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (systemStats.isBoosting) "OPTIMIZING HEAP" else "SYSTEM TELEMETRY HUD",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "VRAM/RAM: ${(systemStats.totalRamMb / 1024f).toInt()} GB",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Interactive Gauge Center Piece
            Box(
                modifier = Modifier
                    .size(230.dp),
                contentAlignment = Alignment.Center
            ) {
                // Background & Tick drawing
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val radius = size.minDimension / 2f - 20f

                    // Outer faint cyber track (240 degree arc from 150 to 390)
                    drawArc(
                        color = Color(0xFF132240),
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 14f, cap = StrokeCap.Round)
                    )

                    // Secondary inner track
                    drawArc(
                        color = Color(0xFF0F1B33),
                        startAngle = 140f,
                        sweepAngle = 260f,
                        useCenter = false,
                        topLeft = Offset(center.x - (radius - 18f), center.y - (radius - 18f)),
                        size = Size((radius - 18f) * 2, (radius - 18f) * 2),
                        style = Stroke(width = 4f, cap = StrokeCap.Round)
                    )

                    // Dynamic Active Neon Sweep Arc (RAM Usage)
                    val activeSweep = (animatedRamUsage.value / 100f) * 260f
                    val arcBrush = Brush.sweepGradient(
                        colors = listOf(
                            ElectricBlue,
                            NeonCyan,
                            if (animatedRamUsage.value > 85f) ThermalCritical else NeonCyan
                        ),
                        center = center
                    )

                    drawArc(
                        brush = arcBrush,
                        startAngle = 140f,
                        sweepAngle = activeSweep.coerceIn(5f, 260f),
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 14f, cap = StrokeCap.Round)
                    )

                    // Draw futuristic radial ticks
                    val tickCount = 28
                    val stepAngle = 260f / tickCount
                    for (i in 0..tickCount) {
                        val angleDeg = 140f + (i * stepAngle)
                        val angleRad = Math.toRadians(angleDeg.toDouble())
                        val isMajor = i % 4 == 0
                        val innerR = radius - if (isMajor) 28f else 22f
                        val outerR = radius - 18f

                        val startX = center.x + (innerR * cos(angleRad)).toFloat()
                        val startY = center.y + (innerR * sin(angleRad)).toFloat()
                        val endX = center.x + (outerR * cos(angleRad)).toFloat()
                        val endY = center.y + (outerR * sin(angleRad)).toFloat()

                        val isPassed = (i * stepAngle) <= activeSweep
                        val tickColor = if (isPassed) NeonCyan else Color(0xFF1E3255)

                        drawLine(
                            color = tickColor,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = if (isMajor) 2.5f else 1.5f
                        )
                    }
                }

                // Rotating reticle ring in center
                Canvas(modifier = Modifier.size(115.dp)) {
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val rad = size.minDimension / 2f - 6f
                    drawCircle(
                        color = NeonCyan.copy(alpha = 0.12f),
                        radius = rad
                    )
                    drawCircle(
                        color = CyberBorderGlow.copy(alpha = 0.4f),
                        radius = rad,
                        style = Stroke(width = 2f)
                    )
                }

                // Center Core Button - Tap to Quick Boost!
                Box(
                    modifier = Modifier
                        .size(105.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    CyberSurfaceElevated,
                                    CyberSurface
                                )
                            )
                        )
                        .border(
                            2.dp,
                            if (systemStats.isBoosting) NeonCyan else CyberBorderGlow,
                            CircleShape
                        )
                        .shadow(
                            elevation = if (systemStats.isBoosting) 16.dp else 6.dp,
                            shape = CircleShape,
                            ambientColor = NeonCyan,
                            spotColor = NeonCyan
                        )
                        .clickable(enabled = !systemStats.isBoosting) {
                            onQuickBoost()
                        }
                        .testTag("boost_core_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Quick Boost",
                            tint = if (systemStats.isBoosting) NeonCyan else HyperBlue,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = if (systemStats.isBoosting) "OPTIMIZING" else "BOOST",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${systemStats.ramUsagePercent.toInt()}% RAM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (systemStats.ramUsagePercent > 80f) ThermalWarning else NeonCyan
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sub Telemetry Status Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(CyberSurfaceVariant.copy(alpha = 0.7f))
                    .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // FPS Ticker
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "FPS",
                        tint = NeonCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${systemStats.currentFps} FPS",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "Hz: ${systemStats.displayRefreshRate}",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }

                // RAM Free
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Memory,
                        contentDescription = "RAM",
                        tint = ElectricBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${systemStats.availableRamMb} MB",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "AVAILABLE",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }

                // Thermal
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val tempColor = when {
                        systemStats.temperatureCelsius > 44f -> ThermalCritical
                        systemStats.temperatureCelsius > 39f -> ThermalWarning
                        else -> PerformanceOptimal
                    }
                    Icon(
                        imageVector = Icons.Default.Thermostat,
                        contentDescription = "Temperature",
                        tint = tempColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "${String.format("%.1f", systemStats.temperatureCelsius)}°C",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = tempColor
                        )
                        Text(
                            text = if (systemStats.isCoolingActive) "COOLING" else "THERMAL",
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }
                }
            }

            // Boost status text message banner
            if (systemStats.boostStatusMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = systemStats.boostStatusMessage.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = if (systemStats.isBoosting) NeonCyan else TextSecondary,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}
