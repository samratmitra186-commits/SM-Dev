package com.example.model

enum class PerformanceMode(
    val title: String,
    val subtitle: String,
    val description: String,
    val targetFps: Int,
    val cpuGovernor: String,
    val touchResponse: String
) {
    BALANCED(
        title = "Balanced Mode",
        subtitle = "Optimal Battery & Power",
        description = "Standard system clocks with dynamic thermal scaling and power efficiency.",
        targetFps = 60,
        cpuGovernor = "Schedutil",
        touchResponse = "1.0x (120Hz)"
    ),
    FPS_BOOSTER(
        title = "Ultra FPS Booster",
        subtitle = "Max Frame Rate Priority",
        description = "Forces maximum GPU rendering priority, aggressive RAM buffer cleanup, and locks touch polling.",
        targetFps = 120,
        cpuGovernor = "Performance (Max Clock)",
        touchResponse = "2.0x (240Hz Polling)"
    ),
    CRYO_COOLING(
        title = "Cryo-Cooling Mode",
        subtitle = "Active Thermal Mitigation",
        description = "Throttles background indexing and high-heat threads to drop phone temperature and prevent stutter.",
        targetFps = 60,
        cpuGovernor = "Cool-Down Throttle",
        touchResponse = "Standard (120Hz)"
    ),
    EXTREME_TURBO(
        title = "Cyber Extreme Turbo",
        subtitle = "Uncapped E-Sports Rig",
        description = "Overclocks CPU scheduler bias, disables Doze restrictions, and maximizes memory throughput.",
        targetFps = 144,
        cpuGovernor = "Extreme Overdrive",
        touchResponse = "2.5x (360Hz HyperTouch)"
    )
}
