package com.example.ui.gis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.Parcel
import com.example.data.model.ParcelStatus
import com.example.ui.theme.*

@Composable
fun GisInspectorSheet(
    parcel: Parcel?,
    onClose: () -> Unit,
    onViewProfile: (String) -> Unit,
    onGenerateReport: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = parcel != null,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        modifier = modifier
    ) {
        if (parcel != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = parcel.id,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalTextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                StatusPillBadge(status = parcel.status)
                            }
                            Text(
                                text = "Survey S.No: ${parcel.surveyNumber} • ${parcel.village}, ${parcel.taluk}",
                                fontSize = 12.sp,
                                color = MutedSlate,
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(SurfaceVariant)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Inspector",
                                tint = CharcoalTextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    // 4 Grid KPI stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InspectorMetricCard(
                            title = "AREA",
                            value = "${parcel.areaHectares} ha",
                            subtitle = if (parcel.areaHectares != parcel.gisCalculatedArea) "GIS: ${parcel.gisCalculatedArea} ha" else "100% Match",
                            isWarning = parcel.areaHectares != parcel.gisCalculatedArea,
                            modifier = Modifier.weight(1f)
                        )
                        InspectorMetricCard(
                            title = "OWNER",
                            value = parcel.ownerName.split(" ").take(2).joinToString(" "),
                            subtitle = parcel.deedNumber.takeLast(10),
                            isWarning = false,
                            modifier = Modifier.weight(1f)
                        )
                        InspectorMetricCard(
                            title = "INTEGRITY",
                            value = "${parcel.verificationPercent}%",
                            subtitle = "Risk: ${parcel.riskScore}/100",
                            isWarning = parcel.riskScore > 35,
                            modifier = Modifier.weight(1f)
                        )
                        InspectorMetricCard(
                            title = "ISSUES",
                            value = "${parcel.issues.size}",
                            subtitle = if (parcel.issues.isNotEmpty()) "Pending Action" else "None",
                            isWarning = parcel.issues.isNotEmpty(),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    if (parcel.issues.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        val topIssue = parcel.issues.first()
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (topIssue.severity) {
                                com.example.data.model.IssueSeverity.CRITICAL -> DangerRedBg
                                com.example.data.model.IssueSeverity.HIGH,
                                com.example.data.model.IssueSeverity.MEDIUM -> WarningAmberBg
                                com.example.data.model.IssueSeverity.LOW -> InfoBlueBg
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                when (topIssue.severity) {
                                    com.example.data.model.IssueSeverity.CRITICAL -> DangerRed.copy(alpha = 0.3f)
                                    com.example.data.model.IssueSeverity.HIGH,
                                    com.example.data.model.IssueSeverity.MEDIUM -> WarningAmber.copy(alpha = 0.3f)
                                    com.example.data.model.IssueSeverity.LOW -> InfoBlue.copy(alpha = 0.3f)
                                }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WarningAmber,
                                    contentDescription = null,
                                    tint = when (topIssue.severity) {
                                        com.example.data.model.IssueSeverity.CRITICAL -> DangerRed
                                        else -> WarningAmber
                                    },
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = topIssue.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = CharcoalTextPrimary
                                    )
                                    Text(
                                        text = topIssue.recommendedAction,
                                        fontSize = 10.sp,
                                        color = CharcoalTextSecondary,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onGenerateReport(parcel.id) },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary)
                        ) {
                            Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Report", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = { onViewProfile(parcel.id) },
                            modifier = Modifier
                                .weight(1.3f)
                                .height(42.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                        ) {
                            Text("View Full Profile", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InspectorMetricCard(
    title: String,
    value: String,
    subtitle: String,
    isWarning: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SurfaceVariant,
        border = if (isWarning) androidx.compose.foundation.BorderStroke(1.dp, WarningAmber.copy(alpha = 0.4f)) else null,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSlate,
                letterSpacing = 0.5.sp
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = if (isWarning) WarningAmber else CharcoalTextPrimary,
                maxLines = 1
            )
            Text(
                text = subtitle,
                fontSize = 9.sp,
                color = if (isWarning) WarningAmber else MutedSlate,
                maxLines = 1
            )
        }
    }
}

@Composable
fun StatusPillBadge(status: ParcelStatus, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status) {
        ParcelStatus.VERIFIED -> VerifiedGreenBg to VerifiedGreen
        ParcelStatus.NEEDS_REVIEW -> WarningAmberBg to WarningAmber
        ParcelStatus.CRITICAL_ISSUE -> DangerRedBg to DangerRed
        ParcelStatus.UNDER_VERIFICATION -> InfoBlueBg to InfoBlue
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
