package com.example.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.model.GameProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class GameRepository(private val context: Context) {

    private val db = GameDatabase.getInstance(context)
    private val dao = db.gameProfileDao()
    private val packageManager = context.packageManager

    val allGames: Flow<List<GameProfile>> = dao.getAllGames()

    suspend fun initializeDefaultGamesIfNeeded() = withContext(Dispatchers.IO) {
        val count = dao.getCount()
        if (count == 0) {
            // First scan installed games/apps on device
            val installedApps = getInstalledLaunchableApps()
            val listToInsert = mutableListOf<GameProfile>()

            // Find matching games or add real apps
            for (app in installedApps.take(6)) {
                listToInsert.add(
                    GameProfile(
                        packageName = app.first,
                        title = app.second,
                        genre = "Installed Mobile App",
                        isCustomAdded = false,
                        targetFps = 90,
                        resolutionScale = "1080p FHD",
                        performanceMode = "FPS_BOOSTER",
                        antiAliasing = "2x MSAA",
                        doNotDisturb = true,
                        touchSensitivity = 1.8f
                    )
                )
            }

            // Always bundle popular esports game presets so the user has immediate rich profiles to customize
            val popularEsportsGames = listOf(
                GameProfile(
                    packageName = "com.miHoYo.GenshinImpact",
                    title = "Genshin Impact",
                    genre = "Open-World Action RPG",
                    targetFps = 60,
                    resolutionScale = "1080p FHD",
                    performanceMode = "FPS_BOOSTER",
                    antiAliasing = "4x FXAA",
                    doNotDisturb = true,
                    touchSensitivity = 1.6f,
                    launchCount = 28,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 3600000L
                ),
                GameProfile(
                    packageName = "com.tencent.ig",
                    title = "PUBG Mobile / BGMI",
                    genre = "Battle Royale Shooter",
                    targetFps = 120,
                    resolutionScale = "720p Performance",
                    performanceMode = "EXTREME_TURBO",
                    antiAliasing = "Disabled",
                    doNotDisturb = true,
                    touchSensitivity = 2.2f,
                    launchCount = 42,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 7200000L
                ),
                GameProfile(
                    packageName = "com.activision.callofduty.shooter",
                    title = "Call of Duty: Warzone",
                    genre = "Tactical FPS Shooter",
                    targetFps = 120,
                    resolutionScale = "1080p FHD",
                    performanceMode = "FPS_BOOSTER",
                    antiAliasing = "2x MSAA",
                    doNotDisturb = true,
                    touchSensitivity = 2.4f,
                    launchCount = 19,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 86400000L
                ),
                GameProfile(
                    packageName = "com.dts.freefireth",
                    title = "Free Fire MAX",
                    genre = "Fast Battle Royale",
                    targetFps = 90,
                    resolutionScale = "1080p FHD",
                    performanceMode = "CRYO_COOLING",
                    antiAliasing = "2x MSAA",
                    doNotDisturb = true,
                    touchSensitivity = 2.0f,
                    launchCount = 14,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 172800000L
                ),
                GameProfile(
                    packageName = "com.riotgames.league.wildrift",
                    title = "League of Legends: Wild Rift",
                    genre = "Competitive MOBA 5v5",
                    targetFps = 120,
                    resolutionScale = "Native Quad HD",
                    performanceMode = "FPS_BOOSTER",
                    antiAliasing = "4x FXAA",
                    doNotDisturb = true,
                    touchSensitivity = 1.9f,
                    launchCount = 33,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 259200000L
                ),
                GameProfile(
                    packageName = "com.gameloft.android.ANMP.GloftA9HM",
                    title = "Asphalt 9: Legends",
                    genre = "Arcade Hyper Racing",
                    targetFps = 60,
                    resolutionScale = "1080p FHD",
                    performanceMode = "FPS_BOOSTER",
                    antiAliasing = "2x MSAA",
                    doNotDisturb = false,
                    touchSensitivity = 1.5f,
                    launchCount = 11,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 400000000L
                ),
                GameProfile(
                    packageName = "jp.konami.pesam",
                    title = "eFootball 2024",
                    genre = "Competitive Football Simulation",
                    targetFps = 60,
                    resolutionScale = "1080p FHD",
                    performanceMode = "FPS_BOOSTER",
                    antiAliasing = "2x MSAA",
                    doNotDisturb = true,
                    touchSensitivity = 2.0f,
                    launchCount = 26,
                    lastLaunchedTimestamp = System.currentTimeMillis() - 1800000L
                )
            )

            // Merge avoiding package duplication
            val existingPackages = listToInsert.map { it.packageName }.toSet()
            for (game in popularEsportsGames) {
                if (!existingPackages.contains(game.packageName)) {
                    listToInsert.add(game)
                }
            }

            dao.insertAll(listToInsert)
        } else {
            // Ensure eFootball is always available in pre-loaded titles
            if (dao.getGameByPackage("jp.konami.pesam") == null) {
                dao.insertOrUpdate(
                    GameProfile(
                        packageName = "jp.konami.pesam",
                        title = "eFootball 2024",
                        genre = "Competitive Football Simulation",
                        targetFps = 60,
                        resolutionScale = "1080p FHD",
                        performanceMode = "FPS_BOOSTER",
                        antiAliasing = "2x MSAA",
                        doNotDisturb = true,
                        touchSensitivity = 2.0f,
                        launchCount = 26,
                        lastLaunchedTimestamp = System.currentTimeMillis() - 1800000L
                    )
                )
            }
        }
    }

    suspend fun updateGame(game: GameProfile) = withContext(Dispatchers.IO) {
        dao.update(game)
    }

    suspend fun addCustomGame(game: GameProfile) = withContext(Dispatchers.IO) {
        dao.insertOrUpdate(game)
    }

    suspend fun deleteGame(game: GameProfile) = withContext(Dispatchers.IO) {
        dao.delete(game)
    }

    suspend fun recordGameLaunch(packageName: String) = withContext(Dispatchers.IO) {
        dao.incrementLaunch(packageName, System.currentTimeMillis())
    }

    fun getInstalledLaunchableApps(): List<Pair<String, String>> {
        val apps = mutableListOf<Pair<String, String>>()
        try {
            val mainIntent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = packageManager.queryIntentActivities(mainIntent, 0)
            for (info in resolveInfos) {
                val pkg = info.activityInfo.packageName
                // Skip self
                if (pkg != context.packageName) {
                    val label = info.loadLabel(packageManager).toString()
                    apps.add(pkg to label)
                }
            }
        } catch (_: Exception) {}
        return apps.sortedBy { it.second }
    }

    fun launchGamePackage(packageName: String): Boolean {
        return try {
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(launchIntent)
                true
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }
}
