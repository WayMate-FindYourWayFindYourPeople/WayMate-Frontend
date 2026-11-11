package com.waymate.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waymate.app.ui.theme.*

@Composable
fun WayMateFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = WayMateGreen,
    activeTextColor: Color = Color.White,
    icon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) activeColor else WayMateWhite)
            .border(
                width = 1.dp,
                color = if (isSelected) activeColor else WayMateBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (icon != null) {
                icon()
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) activeTextColor else WayMateTextPrimary,
                    fontSize = 13.sp
                )
            )
        }
    }
}

@Composable
fun WomenOnlyBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(WayMateDaisyContainer)
            .border(1.dp, WayMateDaisy.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Female,
            contentDescription = "Women Only",
            tint = WayMateDaisyDark,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "Women Only",
            style = MaterialTheme.typography.labelSmall.copy(
                color = WayMateDaisyDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun MixedPoolBadge(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(WayMateSkyContainer)
            .border(1.dp, WayMateSky.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = "Mixed Pool",
            tint = WayMateSkyDark,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = "Mixed Pool",
            style = MaterialTheme.typography.labelSmall.copy(
                color = WayMateSkyDark,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun RouteTimeline(
    pickup: String,
    destination: String,
    modifier: Modifier = Modifier,
    pickupTime: String? = null,
    destinationTime: String? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            // Pickup node (Sky blue)
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(WayMateSky)
            )
            // Connector line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(28.dp)
                    .background(WayMateBorder)
            )
            // Destination node (WayMate Green)
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(WayMateGreen)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pickup,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = WayMateTextPrimary,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                if (pickupTime != null) {
                    Text(
                        text = pickupTime,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = destination,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = WayMateTextPrimary,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
                if (destinationTime != null) {
                    Text(
                        text = destinationTime,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = WayMateTextMuted,
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}
