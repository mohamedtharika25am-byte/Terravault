package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.AuditLogEntry
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDrawer(
    auditLogs: List<AuditLogEntry>,
    onDismiss: () -> Unit,
    onNavigateToParcel: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "System Alerts & Audit Log",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "Real-time stream of rule engine triggers and parcel mutation audits.",
                fontSize = 13.sp,
                color = MutedSlate,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(auditLogs) { log ->
                    Surface(
                        onClick = {
                            if (log.parcelId != null) {
                                onNavigateToParcel(log.parcelId)
                                onDismiss()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = SurfaceVariant,
                        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (log.action.contains("Alert")) Icons.Default.Warning else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (log.action.contains("Alert")) DangerRed else ForestGreenPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = log.action,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CharcoalTextPrimary
                                    )
                                    Text(
                                        text = log.timestamp.takeLast(5),
                                        fontSize = 11.sp,
                                        color = MutedSlate
                                    )
                                }

                                Text(
                                    text = log.details,
                                    fontSize = 12.sp,
                                    color = CharcoalTextSecondary,
                                    modifier = Modifier.padding(top = 2.dp)
                                )

                                if (log.parcelId != null) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Target: ${log.parcelId} → View",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ForestGreenPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
