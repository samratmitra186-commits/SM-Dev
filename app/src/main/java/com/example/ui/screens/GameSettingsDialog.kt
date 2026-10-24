package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GameProfile
import com.example.ui.components.CyberButton
import com.example.ui.components.CyberButtonStyle
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberBorderGlow
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.PerformanceOptimal
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThermalCritical

@Composable
fun GameSettingsDialog(
    game: GameProfile,
    onDismiss: () -> Unit,
    onSave: (GameProfile) -> Unit,
    onLaunch: (GameProfile) -> Unit,
    onDelete: (GameProfile) -> Unit
) {
    var targetFps by remember { mutableIntStateOf(game.targetFps) }
    var resolutionScale by remember { mutableStateOf(game.resolutionScale) }
    var performanceMode by remember { mutableStateOf(game.performanceMode) }
    var antiAliasing by remember { mutableStateOf(game.antiAliasing) }
    var doNotDisturb by remember { mutableStateOf(game.doNotDisturb) }
    var touchSensitivity by remember { mutableFloatStateOf(game.touchSensitivity) }
    var autoBoostOnLaunch by remember { mutableStateOf(game.autoBoostOnLaunch) }
    var inGameHudOverlay by remember { mutableStateOf(game.inGameHudOverlay) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, CyberBorderGlow, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = CyberSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = game.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.SansSerif,
                            color = TextPrimary
                        )
                        Text(
                            text = game.packageName,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = TextMuted
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Target FPS Selector
                Text(
                    text = "TARGET FPS LOCK",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val fpsOptions = listOf(60, 90, 120, 144)
                    fpsOptions.forEach { fps ->
                        val isSelected = targetFps == fps
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) ElectricBlue else CyberBackground)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else CyberBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { targetFps = fps }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$fps",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Performance Governor Mode
                Text(
                    text = "HARDWARE PROFILE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                val modes = listOf(
                    "FPS_BOOSTER" to "Ultra FPS Booster",
                    "CRYO_COOLING" to "Cryo Cooling",
                    "EXTREME_TURBO" to "Extreme Overdrive",
                    "BALANCED" to "Balanced Profile"
                )
                modes.forEach { (modeKey, modeLabel) ->
                    val isSelected = performanceMode == modeKey
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) CyberSurfaceVariant else CyberBackground)
                            .border(
                                1.dp,
                                if (isSelected) NeonCyan else CyberBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { performanceMode = modeKey }
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = modeLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) NeonCyan else TextSecondary
                        )
                        if (isSelected) {
                            Text(
                                text = "ACTIVE",
                                fontSize = 9.sp,
                                fontFamily = FontFamily.Monospace,
                                color = PerformanceOptimal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Resolution Scaling
                Text(
                    text = "RESOLUTION ENGINE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan
                )
                Spacer(modifier = Modifier.height(6.dp))
                val resolutions = listOf("720p Performance", "1080p FHD", "Native Quad HD")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    resolutions.forEach { res ->
                        val isSelected = resolutionScale == res
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyberSurfaceVariant else CyberBackground)
                                .border(
                                    1.dp,
                                    if (isSelected) NeonCyan else CyberBorder,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { resolutionScale = res }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = res.replace(" Performance", "").replace(" FHD", ""),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) NeonCyan else TextMuted
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Touch Sensitivity Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOUCH SAMPLING RATE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = NeonCyan
                    )
                    Text(
                        text = "${String.format("%.1f", touchSensitivity)}x (Fast Polling)",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary
                    )
                }
                Slider(
                    value = touchSensitivity,
                    onValueChange = { touchSensitivity = it },
                    valueRange = 1.0f..2.5f,
                    steps = 5,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonCyan,
                        activeTrackColor = ElectricBlue,
                        inactiveTrackColor = CyberBackground
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Toggles: DND, Auto Boost, In Game HUD
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(CyberBackground)
                        .border(1.dp, CyberBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    // Auto Boost
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Auto Boost on Launch",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Purges background RAM before game starts",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = autoBoostOnLaunch,
                            onCheckedChange = { autoBoostOnLaunch = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = ElectricBlue
                            )
                        )
                    }

                    // DND
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Gaming DND Mode",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Block heads-up alerts & notification popups",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = doNotDisturb,
                            onCheckedChange = { doNotDisturb = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = ElectricBlue
                            )
                        )
                    }

                    // Floating In-game HUD
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "In-Game Floating HUD",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = "Minimal overhead live FPS & temp widget",
                                fontSize = 10.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = inGameHudOverlay,
                            onCheckedChange = { inGameHudOverlay = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = NeonCyan,
                                checkedTrackColor = ElectricBlue
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CyberButton(
                        text = "SAVE CONFIG",
                        icon = Icons.Default.Save,
                        style = CyberButtonStyle.OUTLINE,
                        onClick = {
                            val updated = game.copy(
                                targetFps = targetFps,
                                resolutionScale = resolutionScale,
                                performanceMode = performanceMode,
                                antiAliasing = antiAliasing,
                                doNotDisturb = doNotDisturb,
                                touchSensitivity = touchSensitivity,
                                autoBoostOnLaunch = autoBoostOnLaunch,
                                inGameHudOverlay = inGameHudOverlay
                            )
                            onSave(updated)
                        },
                        modifier = Modifier.weight(1f),
                        testTag = "save_game_config_button"
                    )

                    CyberButton(
                        text = "TURBO LAUNCH",
                        icon = Icons.Default.PlayArrow,
                        style = CyberButtonStyle.PRIMARY,
                        onClick = {
                            val updated = game.copy(
                                targetFps = targetFps,
                                resolutionScale = resolutionScale,
                                performanceMode = performanceMode,
                                antiAliasing = antiAliasing,
                                doNotDisturb = doNotDisturb,
                                touchSensitivity = touchSensitivity,
                                autoBoostOnLaunch = autoBoostOnLaunch,
                                inGameHudOverlay = inGameHudOverlay
                            )
                            onSave(updated)
                            onLaunch(updated)
                        },
                        modifier = Modifier.weight(1.2f),
                        testTag = "turbo_launch_game_button"
                    )
                }

                if (game.isCustomAdded) {
                    Spacer(modifier = Modifier.height(10.dp))
                    CyberButton(
                        text = "REMOVE FROM DASHBOARD",
                        icon = Icons.Default.Delete,
                        style = CyberButtonStyle.TURBO,
                        onClick = { onDelete(game) },
                        modifier = Modifier.fillMaxWidth(),
                        testTag = "delete_game_button"
                    )
                }
            }
        }
    }
}
