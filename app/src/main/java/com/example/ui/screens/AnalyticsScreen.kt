package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DepartmentType
import com.example.data.model.IssueSeverity
import com.example.data.model.LandUseType
import com.example.data.repository.TerravaultRepository
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(modifier: Modifier = Modifier) {
    val analytics = remember { TerravaultRepository.getAnalyticsSummary() }
    val kpis = remember { TerravaultRepository.getDashboardKPIs() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Headline Title
        item {
            Column {
                Text(
                    text = "District Land Governance Analytics",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = CharcoalTextPrimary
                )
                Text(
                    text = "Spatial intelligence, departmental friction metrics & conflict trends",
                    fontSize = 12.sp,
                    color = MutedSlate
                )
            }
        }

        // Executive Takeaway Highlights
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenDark),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Insights, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "EXECUTIVE INTELLIGENCE SUMMARY",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Most Common Conflict: ${analytics.mostCommonIssue}",
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "• Highest Friction Area: ${analytics.highestRiskRegion}",
                        fontSize = 12.sp,
                        color = Color.White,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "• Department Requiring Attention: ${analytics.departmentRequiringAttention}",
                        fontSize = 12.sp,
                        color = SageGreenAccent,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Discrepancies by Department
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "DISCREPANCIES BY DEPARTMENT",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    analytics.issuesByDepartment.forEach { (dept, count) ->
                        val percent = if (kpis.openIssuesCount > 0) (count.toFloat() / kpis.openIssuesCount.coerceAtLeast(1)) else 0.2f
                        AnalyticsBarRow(
                            label = dept.label,
                            count = count,
                            percentage = percent,
                            barColor = when (dept) {
                                DepartmentType.SURVEY -> WarningAmber
                                DepartmentType.PLANNING -> DangerRed
                                DepartmentType.LEGAL -> InfoBlue
                                DepartmentType.REGISTRATION -> ForestGreenPrimary
                                DepartmentType.TAX -> CharcoalTextSecondary
                                DepartmentType.REVENUE -> SageGreenAccent
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Taluk Risk Distribution
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "TALUK-WISE CONFLICT INDEX (AVG RISK)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    analytics.talukRiskScores.forEach { (taluk, risk) ->
                        AnalyticsBarRow(
                            label = taluk,
                            count = risk.toInt(),
                            percentage = (risk.toFloat() / 100f).coerceIn(0.05f, 1.0f),
                            barColor = if (risk > 50) DangerRed else if (risk > 30) WarningAmber else ForestGreenPrimary,
                            unit = "Risk Score"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Land Use Distribution
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "LAND USE CLASSIFICATION DISTRIBUTION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    analytics.landUseDistribution.forEach { (landUse, count) ->
                        val pct = count.toFloat() / 35f
                        AnalyticsBarRow(
                            label = landUse.label,
                            count = count,
                            percentage = pct.coerceIn(0.05f, 1.0f),
                            barColor = when (landUse) {
                                LandUseType.AGRICULTURAL -> VerifiedGreen
                                LandUseType.COMMERCIAL -> InfoBlue
                                LandUseType.RESIDENTIAL -> WarningAmber
                                LandUseType.INDUSTRIAL -> Color(0xFF9C27B0)
                                LandUseType.WATER_BODY_BUFFER -> DangerRed
                                LandUseType.FOREST_RESERVE -> ForestGreenPrimary
                                LandUseType.MIXED_USE -> CharcoalTextSecondary
                            },
                            unit = "Parcels"
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsBarRow(
    label: String,
    count: Int,
    percentage: Float,
    barColor: Color,
    unit: String = "Issues"
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CharcoalTextPrimary)
            Text(text = "$count $unit", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CharcoalTextSecondary)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { percentage.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = barColor,
            trackColor = SurfaceVariant
        )
    }
}
