package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricMeter
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.GameProfile
import com.example.ui.components.InGameOverlaySheet
import com.example.ui.components.OpeningBootSequence
import com.example.ui.components.TurboLaunchDialog
import com.example.ui.screens.GameSettingsDialog
import com.example.ui.screens.GamesDashboardScreen
import com.example.ui.screens.HardwareOptimizerScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NetworkAnalyzerScreen
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberBorderGlow
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.HyperBlue
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.GameBoosterViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameBoosterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

enum class NavigationTab(val title: String, val icon: ImageVector) {
    HOME("DASHBOARD", Icons.Default.Speed),
    GAMES("GAMES", Icons.Default.Gamepad),
    HARDWARE("OPTIMIZER", Icons.Default.Memory),
    NETWORK("LATENCY", Icons.Default.NetworkCheck)
}

@Composable
fun MainAppContent(viewModel: GameBoosterViewModel) {
    val showBoot by viewModel.showBootAnimation.collectAsStateWithLifecycle()
    val systemStats by viewModel.systemStats.collectAsStateWithLifecycle()
    val performanceMode by viewModel.performanceMode.collectAsStateWithLifecycle()
    val networkStats by viewModel.networkStats.collectAsStateWithLifecycle()
    val gamesList by viewModel.gamesList.collectAsStateWithLifecycle()
    val installedApps by viewModel.installedDeviceApps.collectAsStateWithLifecycle()
    val selectedGameForEdit by viewModel.selectedGameForEdit.collectAsStateWithLifecycle()
    val launchCountdown by viewModel.launchCountdownGame.collectAsStateWithLifecycle()
    val showInGameHud by viewModel.showInGameHud.collectAsStateWithLifecycle()

    var currentTab by remember { mutableIntStateOf(NavigationTab.HOME.ordinal) }

    // Handle back button on sub-tabs
    BackHandler(enabled = currentTab != NavigationTab.HOME.ordinal) {
        currentTab = NavigationTab.HOME.ordinal
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CyberBackground)
    ) {
        Scaffold(
            containerColor = CyberBackground,
            bottomBar = {
                CyberBottomNavigation(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .statusBarsPadding()
            ) {
                when (NavigationTab.values()[currentTab]) {
                    NavigationTab.HOME -> {
                        HomeScreen(
                            systemStats = systemStats,
                            performanceMode = performanceMode,
                            games = gamesList,
                            onQuickBoost = { viewModel.quickBoost() },
                            onDeepBoost = { viewModel.deepTurboBoost() },
                            onActivateFpsBooster = { viewModel.activateFpsBooster() },
                            onActivateCryoCooling = { viewModel.activateCryoCooling() },
                            onSelectGameForEdit = { viewModel.selectGameForEdit(it) },
                            onLaunchGame = { viewModel.launchGameWithTurboSequence(it) },
                            onNavigateToGames = { currentTab = NavigationTab.GAMES.ordinal },
                            onReplayBoot = { viewModel.replayBootAnimation() }
                        )
                    }

                    NavigationTab.GAMES -> {
                        GamesDashboardScreen(
                            games = gamesList,
                            installedApps = installedApps,
                            onSelectGameForEdit = { viewModel.selectGameForEdit(it) },
                            onLaunchGame = { viewModel.launchGameWithTurboSequence(it) },
                            onAddCustomGame = { pkg, name -> viewModel.addCustomGame(pkg, name) },
                            onRefreshApps = { viewModel.loadDeviceApps() }
                        )
                    }

                    NavigationTab.HARDWARE -> {
                        HardwareOptimizerScreen(
                            systemStats = systemStats,
                            performanceMode = performanceMode,
                            onSetMode = { viewModel.setPerformanceMode(it) },
                            onDeepCleanse = { viewModel.deepTurboBoost() },
                            onCoolDown = { viewModel.activateCryoCooling() }
                        )
                    }

                    NavigationTab.NETWORK -> {
                        NetworkAnalyzerScreen(
                            networkStats = networkStats,
                            onRunDiagnostic = { viewModel.runNetworkDiagnostic() },
                            onToggleDnsBooster = { viewModel.toggleDnsBooster() }
                        )
                    }
                }
            }
        }

        // Floating In-Game HUD overlay
        AnimatedVisibility(
            visible = showInGameHud,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            InGameOverlaySheet(
                systemStats = systemStats,
                onQuickBoost = { viewModel.quickBoost() },
                onDismiss = { viewModel.toggleInGameHud(false) },
                modifier = Modifier.statusBarsPadding()
            )
        }

        // Game Settings Customization Dialog
        selectedGameForEdit?.let { game ->
            GameSettingsDialog(
                game = game,
                onDismiss = { viewModel.selectGameForEdit(null) },
                onSave = { updatedGame -> viewModel.saveGameProfile(updatedGame) },
                onLaunch = { updatedGame -> viewModel.launchGameWithTurboSequence(updatedGame) },
                onDelete = { gameToDelete -> viewModel.removeGame(gameToDelete) }
            )
        }

        // Turbo Launch Countdown Sequence
        launchCountdown?.let { (game, count) ->
            TurboLaunchDialog(
                game = game,
                countdownSeconds = count,
                onCancel = { viewModel.cancelLaunch() }
            )
        }

        // Opening Boot Animation Sequence
        AnimatedVisibility(
            visible = showBoot,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            OpeningBootSequence(
                onFinished = { viewModel.dismissBootAnimation() }
            )
        }
    }
}

@Composable
fun CyberBottomNavigation(
    currentTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CyberBackground)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CyberSurface)
                .border(1.dp, CyberBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 6.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationTab.values().forEachIndexed { index, tab ->
                val isSelected = currentTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) CyberSurfaceElevated else Color.Transparent)
                        .then(
                            if (isSelected) Modifier.border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                            else Modifier
                        )
                        .clickable { onTabSelected(index) }
                        .padding(vertical = 8.dp)
                        .testTag("nav_tab_${tab.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.title,
                            tint = if (isSelected) NeonCyan else TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tab.title,
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                            fontFamily = FontFamily.Monospace,
                            color = if (isSelected) NeonCyan else TextMuted,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}
