package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.data.repository.TerravaultRepository
import com.example.ui.theme.*

@Composable
fun LandingScreen(
    onEnterApp: (UserRole) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 28.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Brand
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ForestGreenPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Terrain,
                        contentDescription = "TERRAVAULT",
                        tint = Color.White,
                        modifier = Modifier.size(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "TERRAVAULT",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = ForestGreenPrimary,
                    letterSpacing = 2.sp
                )

                Text(
                    text = "One Parcel. Complete Truth.",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CharcoalTextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(ForestGreenLight)
                        .border(1.dp, ForestGreenPrimary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Smart India Hackathon 2026 • Digital Public Infrastructure",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                }
            }
        }

        // Problem & Solution Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "THE PROBLEM: FRAGMENTED LAND RECORDS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Land data is scattered across Revenue (Patta), Registration (SRO Deeds), Survey (FMB maps), Municipal Tax, Urban Planning (DTCP), and Judiciary. Boundary overlaps and disputed ownership go undetected.",
                        fontSize = 12.sp,
                        color = CharcoalTextSecondary,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "THE SOLUTION: UNIFIED DIGITAL CADASTRE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "TERRAVAULT synthesizes all 6 department silos into a single interactive GIS profile, executes transparent Explainable Intelligence rules, and auto-flags discrepancies in real-time.",
                        fontSize = 12.sp,
                        color = CharcoalTextSecondary,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        // Role Selection Header
        item {
            Text(
                text = "SELECT ACCESS ROLE TO ENTER DEMO",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                letterSpacing = 1.sp
            )
        }

        // 4 Role Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                RoleSelectionCard(
                    role = UserRole.ADMIN,
                    title = "District Collector / Special Officer",
                    subtitle = "Full executive district overview, authority overrides, and policy analytics",
                    icon = Icons.Default.AdminPanelSettings,
                    onClick = {
                        TerravaultRepository.updateUserRole(UserRole.ADMIN)
                        onEnterApp(UserRole.ADMIN)
                    }
                )

                RoleSelectionCard(
                    role = UserRole.GOVERNMENT_OFFICER,
                    title = "Tahsildar / Zonal Revenue Officer",
                    subtitle = "Review and resolve cross-department parcel discrepancies & mutations",
                    icon = Icons.Default.AccountBalance,
                    onClick = {
                        TerravaultRepository.updateUserRole(UserRole.GOVERNMENT_OFFICER)
                        onEnterApp(UserRole.GOVERNMENT_OFFICER)
                    }
                )

                RoleSelectionCard(
                    role = UserRole.REVIEWER,
                    title = "Senior Cadastral Survey Inspector",
                    subtitle = "Execute DGPS field boundary audits and reconcile FMB polygons",
                    icon = Icons.Default.GpsFixed,
                    onClick = {
                        TerravaultRepository.updateUserRole(UserRole.REVIEWER)
                        onEnterApp(UserRole.REVIEWER)
                    }
                )

                RoleSelectionCard(
                    role = UserRole.VIEWER,
                    title = "Citizen / Public Land Records Portal",
                    subtitle = "Verify clear title, check encumbrance & download Truth Certificate",
                    icon = Icons.Default.Public,
                    onClick = {
                        TerravaultRepository.updateUserRole(UserRole.VIEWER)
                        onEnterApp(UserRole.VIEWER)
                    }
                )
            }
        }
    }
}

@Composable
private fun RoleSelectionCard(
    role: UserRole,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ForestGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(20.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = CharcoalTextSecondary,
                    lineHeight = 14.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                tint = ForestGreenPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
