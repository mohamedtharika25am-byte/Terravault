package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerravaultTopBar(
    userProfile: UserProfile,
    onOpenSearch: () -> Unit,
    onOpenNotifications: () -> Unit,
    onOpenRoleSelector: () -> Unit,
    onStartDemoTour: () -> Unit,
    unreadNotificationCount: Int = 3,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // SIH Top strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForestGreenDark)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(WarningAmber)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Smart India Hackathon 2026 • Digital Public Land Infrastructure",
                        color = ForestGreenLight,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(WarningAmber.copy(alpha = 0.25f))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                        .clickable { onStartDemoTour() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.size(11.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "DEMO WORKFLOW",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Main Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo & Tagline
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ForestGreenPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Terrain,
                            contentDescription = "TERRAVAULT",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "TERRAVAULT",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = ForestGreenPrimary,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ForestGreenLight)
                                    .border(1.dp, ForestGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 5.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "DEMO",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                )
                            }
                        }
                        Text(
                            text = "One Parcel. Complete Truth.",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = CharcoalTextSecondary
                        )
                    }
                }

                // Actions: Search, Notifications, User Role Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Search trigger
                    IconButton(
                        onClick = onOpenSearch,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Global Search",
                            tint = CharcoalTextPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Notification Bell with Badge
                    Box {
                        IconButton(
                            onClick = onOpenNotifications,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = CharcoalTextPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        if (unreadNotificationCount > 0) {
                            Box(
                                modifier = Modifier
                                    .size(14.dp)
                                    .align(Alignment.TopEnd)
                                    .clip(CircleShape)
                                    .background(DangerRed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = unreadNotificationCount.toString(),
                                    color = Color.White,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // User Profile / Role Switcher Pill
                    Surface(
                        onClick = onOpenRoleSelector,
                        shape = RoundedCornerShape(20.dp),
                        color = ForestGreenLight,
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.role.badge.take(1),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = userProfile.role.badge,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}
