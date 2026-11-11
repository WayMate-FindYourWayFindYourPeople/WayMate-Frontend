package com.waymate.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.R
import com.waymate.app.ui.theme.WayMateGreen
import com.waymate.app.ui.theme.WayMateTextPrimary

/**
 * Official WayMate Logo Composable.
 * STRICT COMPLIANCE:
 * - Uses ONLY the official R.drawable.logo_waymate asset.
 * - Preserves exact original geometry and proportions (ContentScale.Fit).
 * - Never stretches, distorts, or alters shape.
 * - Allows optional color tinting without altering geometry.
 */
@Composable
fun WayMateLogo(
    modifier: Modifier = Modifier,
    height: Dp = 48.dp,
    tint: Color? = null,
    contentDescription: String = "WayMate Official Logo"
) {
    Image(
        painter = painterResource(id = R.drawable.logo_waymate),
        contentDescription = contentDescription,
        modifier = modifier
            .height(height)
            .aspectRatio(1024f / 673f, matchHeightConstraintsFirst = true),
        contentScale = ContentScale.Fit,
        colorFilter = tint?.let { ColorFilter.tint(it) }
    )
}

/**
 * Branded Logo + App Name Header lockup.
 */
@Composable
fun WayMateBrandHeader(
    modifier: Modifier = Modifier,
    logoHeight: Dp = 44.dp,
    showTagline: Boolean = false,
    textColor: Color = WayMateTextPrimary,
    logoTint: Color? = null
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        WayMateLogo(
            height = logoHeight,
            tint = logoTint
        )
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Way",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = (logoHeight.value * 0.55).sp
                    )
                )
                Text(
                    text = "Mate",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = WayMateGreen,
                        fontSize = (logoHeight.value * 0.55).sp
                    )
                )
            }
            if (showTagline) {
                Text(
                    text = "Smart Airport & Shared Transfers",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = textColor.copy(alpha = 0.7f),
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
