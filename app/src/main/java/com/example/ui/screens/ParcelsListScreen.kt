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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LandUseType
import com.example.data.model.Parcel
import com.example.data.model.ParcelStatus
import com.example.data.repository.TerravaultRepository
import com.example.ui.gis.StatusPillBadge
import com.example.ui.theme.*

@Composable
fun ParcelsListScreen(
    onNavigateToParcelDetail: (String) -> Unit,
    onNavigateToMapWithParcel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val parcels by TerravaultRepository.parcels.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<ParcelStatus?>(null) }
    var selectedTaluk by remember { mutableStateOf<String?>(null) }
    var selectedLandUse by remember { mutableStateOf<LandUseType?>(null) }
    var sortByRisk by remember { mutableStateOf(true) }

    val taluks = remember { listOf("Coimbatore South", "Coimbatore North", "Sulur", "Pollachi", "Mettupalayam") }

    val filteredParcels = remember(parcels, searchQuery, selectedStatus, selectedTaluk, selectedLandUse, sortByRisk) {
        var list = parcels

        if (searchQuery.isNotBlank()) {
            val q = searchQuery.trim().lowercase()
            list = list.filter {
                it.id.lowercase().contains(q) ||
                it.surveyNumber.lowercase().contains(q) ||
                it.ownerName.lowercase().contains(q) ||
                it.village.lowercase().contains(q)
            }
        }

        if (selectedStatus != null) {
            list = list.filter { it.status == selectedStatus }
        }

        if (selectedTaluk != null) {
            list = list.filter { it.taluk.equals(selectedTaluk, ignoreCase = true) }
        }

        if (selectedLandUse != null) {
            list = list.filter { it.currentLandUse == selectedLandUse }
        }

        if (sortByRisk) {
            list.sortedByDescending { it.riskScore }
        } else {
            list.sortedBy { it.surveyNumber }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(IvoryBackground)
    ) {
        // Search & Filter header
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
                // Search field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Filter survey number, owner, village...", fontSize = 13.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = MutedSlate)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ForestGreenPrimary,
                        unfocusedBorderColor = SubtleBorder,
                        focusedContainerColor = ForestGreenLight.copy(alpha = 0.3f),
                        unfocusedContainerColor = SurfaceVariant
                    )
                )

                // Status Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterPill(
                            label = "All Status",
                            selected = selectedStatus == null,
                            onClick = { selectedStatus = null }
                        )
                    }
                    items(ParcelStatus.entries) { status ->
                        FilterPill(
                            label = status.label,
                            selected = selectedStatus == status,
                            onClick = { selectedStatus = if (selectedStatus == status) null else status }
                        )
                    }
                }

                // Taluk Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterPill(
                            label = "All Taluks",
                            selected = selectedTaluk == null,
                            onClick = { selectedTaluk = null }
                        )
                    }
                    items(taluks) { taluk ->
                        FilterPill(
                            label = taluk,
                            selected = selectedTaluk == taluk,
                            onClick = { selectedTaluk = if (selectedTaluk == taluk) null else taluk }
                        )
                    }
                }
            }
        }

        // List count and sort header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SHOWING ${filteredParcels.size} CADASTRE PARCELS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                letterSpacing = 0.5.sp
            )

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { sortByRisk = !sortByRisk }
                    .padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Sort,
                    contentDescription = null,
                    tint = CharcoalTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (sortByRisk) "Sort: Risk (High-Low)" else "Sort: Survey No",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalTextSecondary
                )
            }
        }

        // Parcels List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filteredParcels) { parcel ->
                ParcelCardItem(
                    parcel = parcel,
                    onClick = { onNavigateToParcelDetail(parcel.id) },
                    onMapClick = { onNavigateToMapWithParcel(parcel.id) }
                )
            }
        }
    }
}

@Composable
private fun FilterPill(
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

@Composable
private fun ParcelCardItem(
    parcel: Parcel,
    onClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (parcel.status == ParcelStatus.CRITICAL_ISSUE) DangerRed.copy(alpha = 0.4f)
            else if (parcel.status == ParcelStatus.NEEDS_REVIEW) WarningAmber.copy(alpha = 0.4f)
            else SubtleBorder
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = parcel.id,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalTextPrimary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        StatusPillBadge(status = parcel.status)
                    }
                    Text(
                        text = "Survey S.No: ${parcel.surveyNumber} • ${parcel.village}, ${parcel.taluk}",
                        fontSize = 12.sp,
                        color = MutedSlate,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${parcel.verificationPercent}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = if (parcel.verificationPercent > 80) VerifiedGreen else if (parcel.verificationPercent > 50) WarningAmber else DangerRed
                    )
                    Text(
                        text = "Risk: ${parcel.riskScore}/100",
                        fontSize = 10.sp,
                        color = MutedSlate
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(10.dp))

            // Details grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "PRIMARY OWNER", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MutedSlate)
                    Text(text = parcel.ownerName, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CharcoalTextPrimary)
                }
                Column {
                    Text(text = "SURVEY AREA", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MutedSlate)
                    Text(
                        text = "${parcel.areaHectares} ha ${if (parcel.areaHectares != parcel.gisCalculatedArea) "(!)" else ""}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (parcel.areaHectares != parcel.gisCalculatedArea) WarningAmber else CharcoalTextPrimary
                    )
                }
                Column {
                    Text(text = "LAND USE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MutedSlate)
                    Text(text = parcel.currentLandUse.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = CharcoalTextPrimary)
                }
            }

            if (parcel.issues.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                val issue = parcel.issues.first()
                Text(
                    text = "⚠️ ${issue.title}",
                    fontSize = 11.sp,
                    color = if (issue.severity == com.example.data.model.IssueSeverity.CRITICAL) DangerRed else WarningAmber,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onMapClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pin on Map", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text("View Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }
        }
    }
}
