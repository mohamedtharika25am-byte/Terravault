package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun DemoWorkflowBanner(
    currentStepIndex: Int,
    onStepClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        "1. Overview",
        "2. GIS Map",
        "3. Critical Parcel",
        "4. Profile & Conflicts",
        "5. Resolve Discrepancy",
        "6. Official Report"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ForestGreenDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SIH 2026 EVALUATION WORKFLOW",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "Interactive Demo Script",
                    color = SageGreenAccent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Scrollable / spaced steps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                steps.forEachIndexed { index, title ->
                    val isCurrent = currentStepIndex == index
                    val isPast = index < currentStepIndex

                    Surface(
                        onClick = { onStepClicked(index) },
                        shape = RoundedCornerShape(6.dp),
                        color = if (isCurrent) WarningAmber else if (isPast) ForestGreenPrimary else Color(0x33FFFFFF),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 5.dp, horizontal = 2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                fontSize = 8.5.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCurrent) CharcoalTextPrimary else Color.White,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
