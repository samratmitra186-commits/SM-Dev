package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.model.GameServerPing
import com.example.model.NetworkStats
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberButtonStyle
import com.example.ui.components.LiveTelemetryChart
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

@Composable
fun NetworkAnalyzerScreen(
    networkStats: NetworkStats,
    onRunDiagnostic: () -> Unit,
    onToggleDnsBooster: () -> Unit,
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
                    text = "NETWORK LATENCY ANALYZER",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "GAMING PACKET ROUTER & MULTIPLAYER SERVER PING",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
            }
        }

        // Main Latency HUD Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.5.dp, CyberBorderGlow, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberSurfaceElevated)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Top strip: connection type
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = networkStats.connectionType,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(PerformanceOptimal.copy(alpha = 0.2f))
                                .border(1.dp, PerformanceOptimal, RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = networkStats.connectionGrade,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = PerformanceOptimal
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Big Ping Gauge Readout
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Ping
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "AVERAGE PING",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted
                            )
                            val pingColor = when {
                                networkStats.averagePingMs < 35 -> PerformanceOptimal
                                networkStats.averagePingMs < 75 -> NeonCyan
                                networkStats.averagePingMs < 110 -> ThermalWarning
                                else -> ThermalCritical
                            }
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${networkStats.averagePingMs}",
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = pingColor
                                )
                                Text(
                                    text = "ms",
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 6.dp, start = 2.dp)
                                )
                            }
                        }

                        // Jitter
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "JITTER",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${networkStats.currentJitterMs}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "ms",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                                )
                            }
                        }

                        // Packet Loss
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "PACKET LOSS",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = TextMuted
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${networkStats.packetLossPercent}",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Black,
                                    fontFamily = FontFamily.Monospace,
                                    color = if (networkStats.packetLossPercent == 0f) PerformanceOptimal else ThermalCritical
                                )
                                Text(
                                    text = "%",
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    CyberButton(
                        text = if (networkStats.isTesting) "TESTING GAMING SERVERS..." else "RUN NETWORK DIAGNOSTIC",
                        icon = Icons.Default.NetworkCheck,
                        style = CyberButtonStyle.PRIMARY,
                        enabled = !networkStats.isTesting,
                        onClick = onRunDiagnostic,
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "run_network_diagnostic_btn"
                    )
                }
            }
        }

        // DNS Anti-Lag Accelerator Toggle Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .border(1.dp, CyberBorder, RoundedCornerShape(18.dp)),
                colors = CardDefaults.cardColors(containerColor = CyberSurface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(ElectricBlue.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dns,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "DNS ANTI-LAG ACCELERATOR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                color = TextPrimary
                            )
                            Text(
                                text = networkStats.activeDnsRoute,
                                fontSize = 10.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }
                    }

                    Switch(
                        checked = networkStats.isDnsBoosterActive,
                        onCheckedChange = { onToggleDnsBooster() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = NeonCyan,
                            checkedTrackColor = ElectricBlue
                        )
                    )
                }
            }
        }

        // Live Ping History Line Chart
        item {
            val pingFloats = networkStats.pingHistory.map { it.toFloat() }
            LiveTelemetryChart(
                title = "Ping Latency Jitter History",
                history = if (pingFloats.isEmpty()) listOf(25f, 25f) else pingFloats,
                unit = "ms",
                currentValue = networkStats.averagePingMs.toFloat(),
                minVal = 10f,
                maxVal = 120f,
                lineColor = HyperBlue
            )
        }

        // Esports Gaming Server Hub Breakdown
        item {
            Text(
                text = "GLOBAL ESPORTS REGIONAL SERVERS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = NeonCyan,
                letterSpacing = 1.sp
            )
        }

        items(networkStats.servers, key = { it.host }) { server ->
            ServerPingRow(server = server)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ServerPingRow(server: GameServerPing) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, CyberBorder, RoundedCornerShape(14.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CyberBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = null,
                        tint = HyperBlue,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = server.serverName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "${server.region} • ${server.host}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    val pingColor = when {
                        server.pingMs < 35 -> PerformanceOptimal
                        server.pingMs < 70 -> NeonCyan
                        else -> ThermalWarning
                    }
                    Text(
                        text = "${server.pingMs} ms",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = pingColor
                    )
                    Text(
                        text = "Jitter: ${server.jitterMs}ms",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextMuted
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(
                            when (server.statusGrade) {
                                "S" -> PerformanceOptimal.copy(alpha = 0.2f)
                                "A" -> NeonCyan.copy(alpha = 0.2f)
                                else -> ThermalWarning.copy(alpha = 0.2f)
                            }
                        )
                        .border(
                            1.dp,
                            when (server.statusGrade) {
                                "S" -> PerformanceOptimal
                                "A" -> NeonCyan
                                else -> ThermalWarning
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = server.statusGrade,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = when (server.statusGrade) {
                            "S" -> PerformanceOptimal
                            "A" -> NeonCyan
                            else -> ThermalWarning
                        }
                    )
                }
            }
        }
    }
}
