package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.GameProfile
import com.example.model.PerformanceMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Apex Booster", appName)
    }

    @Test
    fun `verify performance modes and targets`() {
        val fpsBooster = PerformanceMode.FPS_BOOSTER
        assertEquals(120, fpsBooster.targetFps)
        assertTrue(fpsBooster.cpuGovernor.contains("Performance"))

        val cryo = PerformanceMode.CRYO_COOLING
        assertEquals(60, cryo.targetFps)
        assertTrue(cryo.subtitle.contains("Thermal"))
    }

    @Test
    fun `verify game profile defaults`() {
        val game = GameProfile(
            packageName = "com.test.game",
            title = "Apex Arena",
            targetFps = 120,
            resolutionScale = "1080p FHD"
        )
        assertNotNull(game)
        assertEquals(120, game.targetFps)
        assertTrue(game.doNotDisturb)
    }

    @Test
    fun `verify efootball profile preset`() {
        val efootball = GameProfile(
            packageName = "jp.konami.pesam",
            title = "eFootball 2024",
            genre = "Competitive Football Simulation",
            targetFps = 60,
            resolutionScale = "1080p FHD",
            performanceMode = "FPS_BOOSTER",
            antiAliasing = "2x MSAA",
            touchSensitivity = 2.0f
        )
        assertEquals("eFootball 2024", efootball.title)
        assertEquals("jp.konami.pesam", efootball.packageName)
        assertEquals(60, efootball.targetFps)
    }
}
