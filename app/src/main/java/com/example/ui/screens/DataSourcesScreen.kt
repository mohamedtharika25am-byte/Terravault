package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.DepartmentSourceRecord
import com.example.data.model.DepartmentType
import com.example.data.model.SourceStatus
import com.example.data.repository.TerravaultRepository
import com.example.ui.theme.*

@Composable
fun DataSourcesScreen(modifier: Modifier = Modifier) {
    val dataSources by TerravaultRepository.dataSources.collectAsState()
    val isSyncing by TerravaultRepository.isSyncing.collectAsState()
    val syncProgress by TerravaultRepository.syncProgress.collectAsState()

    var showAddSourceDialog by remember { mutableStateOf(false) }
    var newDeptName by remember { mutableStateOf("") }
    var newApiUrl by remember { mutableStateOf("") }
    var newDeptType by remember { mutableStateOf(DepartmentType.PLANNING) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Data Sources & Interoperability",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalTextPrimary
                    )
                    Text(
                        text = "Digital Public Land Infrastructure (DPI) Gateways",
                        fontSize = 12.sp,
                        color = MutedSlate
                    )
                }

                Button(
                    onClick = { showAddSourceDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add API", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Live Sync Trigger Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
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
                                text = "Live Gateway Ingestion Daemon",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalTextPrimary
                            )
                            Text(
                                text = "6 of 6 State Land Gateways Active • Total 12,480 Cadastral Records Synced",
                                fontSize = 11.sp,
                                color = MutedSlate
                            )
                        }

                        Button(
                            onClick = { TerravaultRepository.syncAllDataSources() },
                            enabled = !isSyncing,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                        ) {
                            if (isSyncing) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Syncing...", fontSize = 11.sp)
                            } else {
                                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Sync All Gateways", fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                            trackColor = ForestGreenLight
                        )
                    }
                }
            }
        }

        // List of 6 Gateways
        items(dataSources) { source ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = when (source.department) {
                                        DepartmentType.REVENUE -> Icons.Default.Description
                                        DepartmentType.REGISTRATION -> Icons.Default.Assignment
                                        DepartmentType.SURVEY -> Icons.Default.CropSquare
                                        DepartmentType.TAX -> Icons.Default.ReceiptLong
                                        DepartmentType.PLANNING -> Icons.Default.Apartment
                                        DepartmentType.LEGAL -> Icons.Default.Gavel
                                    },
                                    contentDescription = null,
                                    tint = ForestGreenPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = source.department.label,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CharcoalTextPrimary
                                )
                                Text(
                                    text = source.recordNumber,
                                    fontSize = 11.sp,
                                    color = MutedSlate
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(
                                    when (source.status) {
                                        SourceStatus.VERIFIED, SourceStatus.SYNCED -> VerifiedGreenBg
                                        SourceStatus.CONFLICT -> DangerRedBg
                                        SourceStatus.MISSING -> WarningAmberBg
                                        SourceStatus.DEMO_MOCK -> InfoBlueBg
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = source.status.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (source.status) {
                                    SourceStatus.VERIFIED, SourceStatus.SYNCED -> VerifiedGreen
                                    SourceStatus.CONFLICT -> DangerRed
                                    SourceStatus.MISSING -> WarningAmber
                                    SourceStatus.DEMO_MOCK -> InfoBlue
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = source.details, fontSize = 12.sp, color = CharcoalTextSecondary)

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Uptime: ${source.healthPercent}%", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = ForestGreenPrimary)
                        Text(text = "Latency: ${source.latencyMs}ms", fontSize = 11.sp, color = CharcoalTextSecondary)
                        Text(text = "Updated: ${source.lastUpdated.takeLast(5)}", fontSize = 11.sp, color = MutedSlate)
                    }
                }
            }
        }
    }

    // Modal: Add Custom Data Source
    if (showAddSourceDialog) {
        AlertDialog(
            onDismissRequest = { showAddSourceDialog = false },
            title = { Text("Connect DPI Land Gateway", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newDeptName,
                        onValueChange = { newDeptName = it },
                        label = { Text("Gateway / Department Name") },
                        placeholder = { Text("e.g. State Forest & Environment GIS") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newApiUrl,
                        onValueChange = { newApiUrl = it },
                        label = { Text("REST / OGC WFS API Endpoint URL") },
                        placeholder = { Text("https://api.forest.tn.gov.in/v1/cadastre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newDeptName.isNotBlank()) {
                            TerravaultRepository.addCustomDataSource(
                                deptName = newDeptName,
                                url = newApiUrl.ifBlank { "https://api.gateway.gov.in/v1/sync" },
                                deptType = newDeptType
                            )
                            showAddSourceDialog = false
                            newDeptName = ""
                            newApiUrl = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("Connect Gateway")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSourceDialog = false }) { Text("Cancel") }
            }
        )
    }
}
