package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.ui.theme.CyberBackground
import com.example.ui.theme.CyberBorderGlow
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary

enum class CyberButtonStyle {
    PRIMARY,
    CRYO,
    TURBO,
    OUTLINE
}

@Composable
fun CyberButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: CyberButtonStyle = CyberButtonStyle.PRIMARY,
    enabled: Boolean = true,
    testTag: String = "cyber_button"
) {
    val brush = when (style) {
        CyberButtonStyle.PRIMARY -> Brush.horizontalGradient(
            colors = listOf(ElectricBlue, NeonCyan)
        )
        CyberButtonStyle.CRYO -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF0284C7), Color(0xFF38BDF8))
        )
        CyberButtonStyle.TURBO -> Brush.horizontalGradient(
            colors = listOf(Color(0xFFE11D48), Color(0xFFFB7185))
        )
        CyberButtonStyle.OUTLINE -> Brush.horizontalGradient(
            colors = listOf(Color(0xFF0F1B35), Color(0xFF132244))
        )
    }

    val contentColor = when (style) {
        CyberButtonStyle.PRIMARY -> CyberBackground
        CyberButtonStyle.CRYO -> CyberBackground
        CyberButtonStyle.TURBO -> TextPrimary
        CyberButtonStyle.OUTLINE -> NeonCyan
    }

    val borderColor = when (style) {
        CyberButtonStyle.OUTLINE -> CyberBorderGlow
        else -> Color.Transparent
    }

    Box(
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(brush)
            .then(
                if (style == CyberButtonStyle.OUTLINE) Modifier.border(1.dp, borderColor, RoundedCornerShape(12.dp))
                else Modifier
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text.uppercase(),
                color = contentColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )
        }
    }
}
