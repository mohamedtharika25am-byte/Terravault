package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.IssueStatus
import com.example.data.model.ParcelIssue
import com.example.data.repository.TerravaultRepository
import com.example.ui.theme.*

@Composable
fun IssuesScreen(
    onNavigateToParcel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val issues by TerravaultRepository.issues.collectAsState()

    var selectedSeverity by remember { mutableStateOf<IssueSeverity?>(null) }
    var selectedDepartment by remember { mutableStateOf<DepartmentType?>(null) }
    var selectedStatus by remember { mutableStateOf<IssueStatus?>(IssueStatus.OPEN) }

    var issueToResolve by remember { mutableStateOf<ParcelIssue?>(null) }
    var officerNotes by remember { mutableStateOf("") }
    var resolutionReason by remember { mutableStateOf("Joint DGPS Resurvey Reconciled") }

    val filteredIssues = remember(issues, selectedSeverity, selectedDepartment, selectedStatus) {
        var list = issues
        if (selectedSeverity != null) {
            list = list.filter { it.severity == selectedSeverity }
        }
        if (selectedDepartment != null) {
            list = list.filter { it.department == selectedDepartment }
        }
        if (selectedStatus != null) {
            list = list.filter { it.status == selectedStatus }
        }
        list.sortedByDescending { it.severity.weight }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
    ) {
        // Top Filter Bar
        Surface(
            color = PureWhiteSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
            shadowElevation = 1.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Conflict Resolution Center",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalTextPrimary
                        )
                        Text(
                            text = "Review cross-department discrepancies & execute reconciliations",
                            fontSize = 12.sp,
                            color = MutedSlate
                        )
                    }

                    // Open vs Resolved count badge
                    val openCount = issues.count { it.status == IssueStatus.OPEN }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(DangerRedBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "$openCount Open",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = DangerRed
                        )
                    }
                }

                // Severity Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterPillItem(
                            label = "All Severity",
                            selected = selectedSeverity == null,
                            onClick = { selectedSeverity = null }
                        )
                    }
                    items(IssueSeverity.entries) { sev ->
                        FilterPillItem(
                            label = sev.label,
                            selected = selectedSeverity == sev,
                            onClick = { selectedSeverity = if (selectedSeverity == sev) null else sev }
                        )
                    }
                }

                // Department Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterPillItem(
                            label = "All Depts",
                            selected = selectedDepartment == null,
                            onClick = { selectedDepartment = null }
                        )
                    }
                    items(DepartmentType.entries) { dept ->
                        FilterPillItem(
                            label = dept.label.split(" ").first(),
                            selected = selectedDepartment == dept,
                            onClick = { selectedDepartment = if (selectedDepartment == dept) null else dept }
                        )
                    }
                }
            }
        }

        // List count header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SHOWING ${filteredIssues.size} ACTIONABLE ISSUES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                letterSpacing = 0.5.sp
            )

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Open (${issues.count { it.status == IssueStatus.OPEN }})",
                    fontSize = 11.sp,
                    fontWeight = if (selectedStatus == IssueStatus.OPEN) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedStatus == IssueStatus.OPEN) ForestGreenPrimary else MutedSlate,
                    modifier = Modifier.clickable { selectedStatus = IssueStatus.OPEN }
                )
                Text("•", color = MutedSlate, fontSize = 11.sp)
                Text(
                    text = "Resolved (${issues.count { it.status == IssueStatus.RESOLVED }})",
                    fontSize = 11.sp,
                    fontWeight = if (selectedStatus == IssueStatus.RESOLVED) FontWeight.Bold else FontWeight.Normal,
                    color = if (selectedStatus == IssueStatus.RESOLVED) ForestGreenPrimary else MutedSlate,
                    modifier = Modifier.clickable { selectedStatus = IssueStatus.RESOLVED }
                )
            }
        }

        // Issues List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredIssues) { issue ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (issue.status == IssueStatus.RESOLVED) VerifiedGreen.copy(alpha = 0.4f)
                        else if (issue.severity == IssueSeverity.CRITICAL) DangerRed.copy(alpha = 0.4f)
                        else WarningAmber.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = issue.id,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MutedSlate
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "• ${issue.department.label}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ForestGreenPrimary
                                    )
                                }
                                Text(
                                    text = issue.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalTextPrimary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (issue.status == IssueStatus.RESOLVED) VerifiedGreenBg
                                        else if (issue.severity == IssueSeverity.CRITICAL) DangerRedBg
                                        else WarningAmberBg
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (issue.status == IssueStatus.RESOLVED) "RESOLVED" else issue.severity.label.uppercase(),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (issue.status == IssueStatus.RESOLVED) VerifiedGreen else if (issue.severity == IssueSeverity.CRITICAL) DangerRed else WarningAmber
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = issue.description, fontSize = 12.sp, color = CharcoalTextSecondary)

                        if (issue.status == IssueStatus.RESOLVED) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = VerifiedGreenBg,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "✓ RESOLUTION RECORD: ${issue.resolutionNotes}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = VerifiedGreen
                                    )
                                    Text(
                                        text = "Resolved by ${issue.resolvedBy} on ${issue.resolvedAt}",
                                        fontSize = 9.5.sp,
                                        color = CharcoalTextSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = DividerColor)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Target: ${issue.parcelId}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary,
                                modifier = Modifier.clickable { onNavigateToParcel(issue.parcelId) }
                            )

                            if (issue.status != IssueStatus.RESOLVED) {
                                Button(
                                    onClick = { issueToResolve = issue },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Resolve", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Dialog: Resolve Issue
    if (issueToResolve != null) {
        val target = issueToResolve!!
        AlertDialog(
            onDismissRequest = { issueToResolve = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Gavel, contentDescription = null, tint = ForestGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resolve Issue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = target.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = "Target Parcel: ${target.parcelId}", fontSize = 11.sp, color = MutedSlate)

                    val options = listOf(
                        "Joint Field DGPS Resurvey Reconciled",
                        "Certified Sub-Registrar Deed Verified",
                        "DTCP Zonal Regularization Fee Endorsed",
                        "High Court / District Court Order Complied"
                    )

                    options.forEach { opt ->
                        Surface(
                            onClick = { resolutionReason = opt },
                            shape = RoundedCornerShape(6.dp),
                            color = if (resolutionReason == opt) ForestGreenLight else SurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = resolutionReason == opt, onClick = { resolutionReason = opt })
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(opt, fontSize = 11.sp)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = officerNotes,
                        onValueChange = { officerNotes = it },
                        label = { Text("Officer Audit Remarks") },
                        placeholder = { Text("Enter verification note...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        TerravaultRepository.resolveIssue(
                            issueId = target.id,
                            officerNotes = officerNotes.ifBlank { "Reconciled pursuant to cadastral land audit." },
                            resolutionReason = resolutionReason
                        )
                        issueToResolve = null
                        officerNotes = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("Harmonize & Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { issueToResolve = null }) { Text("Cancel") }
            }
        )
    }
}

@Composable
private fun FilterPillItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) ForestGreenPrimary else SurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) ForestGreenPrimary else SubtleBorder),
        modifier = Modifier.height(28.dp)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) Color.White else CharcoalTextPrimary
            )
        }
    }
}
