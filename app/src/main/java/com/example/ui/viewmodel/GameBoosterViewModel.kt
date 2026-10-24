package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GameRepository
import com.example.data.NetworkAnalyzerManager
import com.example.data.SystemMonitorManager
import com.example.model.GameProfile
import com.example.model.PerformanceMode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameBoosterViewModel(application: Application) : AndroidViewModel(application) {

    val systemMonitor = SystemMonitorManager(application)
    val networkAnalyzer = NetworkAnalyzerManager(application)
    val gameRepository = GameRepository(application)

    val systemStats = systemMonitor.systemStats
    val performanceMode = systemMonitor.currentMode
    val networkStats = networkAnalyzer.networkStats
    val gamesList: StateFlow<List<GameProfile>> = gameRepository.allGames.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _showBootAnimation = MutableStateFlow(true)
    val showBootAnimation: StateFlow<Boolean> = _showBootAnimation.asStateFlow()

    private val _selectedGameForEdit = MutableStateFlow<GameProfile?>(null)
    val selectedGameForEdit: StateFlow<GameProfile?> = _selectedGameForEdit.asStateFlow()

    private val _showInGameHud = MutableStateFlow(false)
    val showInGameHud: StateFlow<Boolean> = _showInGameHud.asStateFlow()

    private val _launchCountdownGame = MutableStateFlow<Pair<GameProfile, Int>?>(null)
    val launchCountdownGame: StateFlow<Pair<GameProfile, Int>?> = _launchCountdownGame.asStateFlow()

    private val _installedDeviceApps = MutableStateFlow<List<Pair<String, String>>>(emptyList())
    val installedDeviceApps: StateFlow<List<Pair<String, String>>> = _installedDeviceApps.asStateFlow()

    private var countdownJob: Job? = null

    init {
        systemMonitor.startMonitoring(viewModelScope)
        viewModelScope.launch {
            gameRepository.initializeDefaultGamesIfNeeded()
            loadDeviceApps()
        }
    }

    fun dismissBootAnimation() {
        _showBootAnimation.value = false
    }

    fun replayBootAnimation() {
        _showBootAnimation.value = true
    }

    fun loadDeviceApps() {
        viewModelScope.launch {
            _installedDeviceApps.value = gameRepository.getInstalledLaunchableApps()
        }
    }

    fun quickBoost(onComplete: () -> Unit = {}) {
        systemMonitor.performBoost(viewModelScope, isDeepBoost = false, onComplete = onComplete)
    }

    fun deepTurboBoost(onComplete: () -> Unit = {}) {
        systemMonitor.performBoost(viewModelScope, isDeepBoost = true, onComplete = onComplete)
    }

    fun activateCryoCooling(onComplete: () -> Unit = {}) {
        systemMonitor.setPerformanceMode(PerformanceMode.CRYO_COOLING)
        systemMonitor.performCryoCoolDown(viewModelScope, onComplete = onComplete)
    }

    fun activateFpsBooster() {
        systemMonitor.setPerformanceMode(PerformanceMode.FPS_BOOSTER)
    }

    fun setPerformanceMode(mode: PerformanceMode) {
        systemMonitor.setPerformanceMode(mode)
    }

    fun runNetworkDiagnostic() {
        networkAnalyzer.runLatencyDiagnostic(viewModelScope)
    }

    fun toggleDnsBooster() {
        networkAnalyzer.toggleDnsBooster()
    }

    fun selectGameForEdit(game: GameProfile?) {
        _selectedGameForEdit.value = game
    }

    fun saveGameProfile(game: GameProfile) {
        viewModelScope.launch {
            gameRepository.updateGame(game)
            _selectedGameForEdit.value = null
        }
    }

    fun addCustomGame(packageName: String, title: String) {
        viewModelScope.launch {
            val newGame = GameProfile(
                packageName = packageName,
                title = title,
                genre = "Custom Registered Game",
                isCustomAdded = true,
                targetFps = 90,
                resolutionScale = "1080p FHD",
                performanceMode = "FPS_BOOSTER"
            )
            gameRepository.addCustomGame(newGame)
        }
    }

    fun removeGame(game: GameProfile) {
        viewModelScope.launch {
            gameRepository.deleteGame(game)
            if (_selectedGameForEdit.value?.packageName == game.packageName) {
                _selectedGameForEdit.value = null
            }
        }
    }

    fun launchGameWithTurboSequence(game: GameProfile) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            // Quick memory cleanse before launch
            if (game.autoBoostOnLaunch) {
                systemMonitor.performBoost(viewModelScope, isDeepBoost = false)
            }

            for (sec in 3 downTo 1) {
                _launchCountdownGame.value = game to sec
                delay(700)
            }
            _launchCountdownGame.value = game to 0
            delay(400)

            gameRepository.recordGameLaunch(game.packageName)
            val success = gameRepository.launchGamePackage(game.packageName)
            _launchCountdownGame.value = null

            // If game has HUD overlay enabled, show the simulated low-overhead overlay
            if (game.inGameHudOverlay) {
                _showInGameHud.value = true
            }
        }
    }

    fun cancelLaunch() {
        countdownJob?.cancel()
        _launchCountdownGame.value = null
    }

    fun toggleInGameHud(show: Boolean) {
        _showInGameHud.value = show
    }

    override fun onCleared() {
        super.onCleared()
        systemMonitor.stopMonitoring()
    }
}
