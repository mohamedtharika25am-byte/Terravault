package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.DashboardKPIs
import com.example.data.repository.TerravaultRepository
import com.example.ui.components.DemoWorkflowBanner
import com.example.ui.gis.StatusPillBadge
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToParcels: () -> Unit,
    onNavigateToIssues: () -> Unit,
    onNavigateToSources: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToParcelDetail: (String) -> Unit,
    onStepClicked: (Int) -> Unit
) {
    val parcels by TerravaultRepository.parcels.collectAsState()
    val isSyncing by TerravaultRepository.isSyncing.collectAsState()
    val syncProgress by TerravaultRepository.syncProgress.collectAsState()
    val dataSources by TerravaultRepository.dataSources.collectAsState()
    val kpis = remember(parcels) { TerravaultRepository.getDashboardKPIs() }

    val recentCriticalParcels = remember(parcels) {
        parcels.filter { it.status == ParcelStatus.CRITICAL_ISSUE || it.status == ParcelStatus.NEEDS_REVIEW }.take(5)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // SIH Demo workflow banner
        item {
            DemoWorkflowBanner(
                currentStepIndex = 0,
                onStepClicked = onStepClicked
            )
        }

        // Welcome & Multi-Department Sync Bar
        item {
            DistrictSyncHeader(
                isSyncing = isSyncing,
                syncProgress = syncProgress,
                onTriggerSync = { TerravaultRepository.syncAllDataSources() }
            )
        }

        // 4 KPI Summary Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "TOTAL PARCELS",
                        value = "12,480",
                        subtitle = "Coimbatore District",
                        icon = Icons.Default.Terrain,
                        color = ForestGreenPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToParcels
                    )
                    KpiCard(
                        title = "VERIFIED TRUTH",
                        value = "${kpis.verifiedCount}",
                        subtitle = "${String.format("%.1f", kpis.verificationRate)}% Unified Rate",
                        icon = Icons.Default.CheckCircle,
                        color = VerifiedGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToParcels
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    KpiCard(
                        title = "NEEDS REVIEW",
                        value = "${kpis.needsReviewCount}",
                        subtitle = "Boundary & Name Mismatch",
                        icon = Icons.Default.WarningAmber,
                        color = WarningAmber,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToIssues
                    )
                    KpiCard(
                        title = "CRITICAL CONFLICTS",
                        value = "${kpis.criticalCount}",
                        subtitle = "Wetlands & Injunctions",
                        icon = Icons.Default.Dangerous,
                        color = DangerRed,
                        modifier = Modifier.weight(1f),
                        onClick = onNavigateToIssues
                    )
                }
            }
        }

        // Quick Actions Row
        item {
            Text(
                text = "GIS COMMAND SHORTCUTS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.Map,
                    title = "GIS Map View",
                    subtitle = "Spatial Canvas",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToMap
                )
                QuickActionButton(
                    icon = Icons.Default.Warning,
                    title = "Resolve Issues",
                    subtitle = "18 Action Items",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToIssues
                )
                QuickActionButton(
                    icon = Icons.Default.Description,
                    title = "Audit Reports",
                    subtitle = "Dossier Export",
                    modifier = Modifier.weight(1f),
                    onClick = onNavigateToReports
                )
            }
        }

        // Featured Spotlight Parcel Banner (TN-COI-00123-0456)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToParcelDetail("TN-COI-00123-0456") },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, WarningAmber.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(WarningAmber)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FEATURED PRESENTATION DEMO PARCEL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarningAmber,
                                letterSpacing = 0.5.sp
                            )
                        }
                        StatusPillBadge(status = ParcelStatus.NEEDS_REVIEW)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "TN-COI-00123-0456 (S.No 45/2A)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )

                    Text(
                        text = "Singanallur, Coimbatore South • Owner: Ravi Kumar vs Kumar Raj",
                        fontSize = 12.sp,
                        color = CharcoalTextSecondary,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = WarningAmberBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, WarningAmber.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Discrepancy: Revenue 1.25 ha vs GIS 1.10 ha • Active Court Injunction",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalTextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Inspect & Reconcile Truth →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    }
                }
            }
        }

        // Live Department Source Status Cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DEPARTMENT GATEWAYS (6 CONNECTED)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "View All →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary,
                    modifier = Modifier.clickable { onNavigateToSources() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(dataSources) { source ->
                    DepartmentGatewayMiniCard(source = source)
                }
            }
        }

        // Recent Critical Parcels list
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "CRITICAL DISCREPANCIES REQUIRING ACTION",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "All Issues (${kpis.openIssuesCount}) →",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary,
                    modifier = Modifier.clickable { onNavigateToIssues() }
                )
            }
        }

        items(recentCriticalParcels) { parcel ->
            ParcelConflictListItem(
                parcel = parcel,
                onClick = { onNavigateToParcelDetail(parcel.id) }
            )
        }
    }
}

@Composable
private fun DistrictSyncHeader(
    isSyncing: Boolean,
    syncProgress: Float,
    onTriggerSync: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Coimbatore District Cadastre",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "6 Depts Synchronized • Auto-reconciling in real-time",
                        color = SageGreenAccent,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = onTriggerSync,
                    enabled = !isSyncing,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PureWhiteSurface,
                        contentColor = ForestGreenPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = ForestGreenPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Syncing...", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sync All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (isSyncing) {
                Spacer(modifier = Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { syncProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = WarningAmber,
                    trackColor = ForestGreenDark
                )
            }
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        shadowElevation = 1.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedSlate,
                    letterSpacing = 0.5.sp
                )
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = CharcoalTextPrimary
            )

            Text(
                text = subtitle,
                fontSize = 10.5.sp,
                color = CharcoalTextSecondary,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(ForestGreenLight),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CharcoalTextPrimary, maxLines = 1)
            Text(text = subtitle, fontSize = 9.sp, color = MutedSlate, maxLines = 1)
        }
    }
}

@Composable
private fun DepartmentGatewayMiniCard(source: DepartmentSourceRecord) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        modifier = Modifier.width(140.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = source.department.label.split(" ").first(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = CharcoalTextPrimary,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (source.status == SourceStatus.CONFLICT) DangerRed else VerifiedGreen)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${source.healthPercent}% Uptime",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (source.healthPercent < 90) WarningAmber else ForestGreenPrimary
            )
            Text(
                text = "${source.latencyMs}ms latency",
                fontSize = 9.sp,
                color = MutedSlate
            )
        }
    }
}

@Composable
private fun ParcelConflictListItem(
    parcel: Parcel,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = parcel.id,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusPillBadge(status = parcel.status)
                }

                Text(
                    text = "S.No: ${parcel.surveyNumber} • ${parcel.ownerName} • ${parcel.village}",
                    fontSize = 12.sp,
                    color = CharcoalTextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                if (parcel.issues.isNotEmpty()) {
                    Text(
                        text = "Issue: ${parcel.issues.first().title}",
                        fontSize = 11.sp,
                        color = if (parcel.status == ParcelStatus.CRITICAL_ISSUE) DangerRed else WarningAmber,
                        maxLines = 1,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Risk: ${parcel.riskScore}/100",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (parcel.riskScore > 70) DangerRed else if (parcel.riskScore > 35) WarningAmber else ForestGreenPrimary
                )
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MutedSlate,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
