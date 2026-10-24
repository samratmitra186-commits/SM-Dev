package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorder
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceVariant
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ThermalCritical
import com.example.ui.theme.ThermalWarning

@Composable
fun LiveTelemetryChart(
    title: String,
    history: List<Float>,
    unit: String,
    currentValue: Float,
    minVal: Float = 25f,
    maxVal: Float = 50f,
    lineColor: Color = NeonCyan,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(CyberSurface)
            .border(1.dp, CyberBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Chart Title & Metrics
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(lineColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title.uppercase(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary,
                        letterSpacing = 0.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format("%.1f", currentValue),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = unit,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = lineColor,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Waveform
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberBackground)
            ) {
                val w = size.width
                val h = size.height

                // Draw cyber grid horizontal reference lines
                val gridLines = 3
                for (i in 1..gridLines) {
                    val y = h * (i.toFloat() / (gridLines + 1))
                    drawLine(
                        color = Color(0xFF13233D),
                        start = Offset(0f, y),
                        end = Offset(w, y),
                        strokeWidth = 1f
                    )
                }

                if (history.size < 2) return@Canvas

                val range = (maxVal - minVal).coerceAtLeast(1f)
                val stepX = w / (history.size - 1).coerceAtLeast(1)

                val points = history.mapIndexed { index, value ->
                    val normY = 1f - ((value - minVal) / range).coerceIn(0.05f, 0.95f)
                    Offset(index * stepX, normY * h)
                }

                val strokePath = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    for (i in 1 until points.size) {
                        val p0 = points[i - 1]
                        val p1 = points[i]
                        val cx = (p0.x + p1.x) / 2f
                        cubicTo(cx, p0.y, cx, p1.y, p1.x, p1.y)
                    }
                }

                val fillPath = Path().apply {
                    addPath(strokePath)
                    lineTo(points.last().x, h)
                    lineTo(points.first().x, h)
                    close()
                }

                // Gradient fill underneath
                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            lineColor.copy(alpha = 0.35f),
                            lineColor.copy(alpha = 0.0f)
                        ),
                        startY = 0f,
                        endY = h
                    )
                )

                // Neon Stroke line
                drawPath(
                    path = strokePath,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // Current head point dot
                val lastPt = points.last()
                drawCircle(
                    color = Color.White,
                    radius = 4.dp.toPx(),
                    center = lastPt
                )
                drawCircle(
                    color = lineColor,
                    radius = 2.dp.toPx(),
                    center = lastPt
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Sub info row: Min & Max
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val minHistory = history.minOrNull() ?: minVal
                val maxHistory = history.maxOrNull() ?: maxVal

                Text(
                    text = "MIN: ${String.format("%.1f", minHistory)}$unit",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = TextMuted
                )
                Text(
                    text = "REAL-TIME TRACING",
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    color = NeonCyan.copy(alpha = 0.7f)
                )
                Text(
                    text = "PEAK: ${String.format("%.1f", maxHistory)}$unit",
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = if (maxHistory > 42f) ThermalCritical else TextSecondary
                )
            }
        }
    }
}
