package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.Parcel
import com.example.data.repository.TerravaultRepository
import com.example.ui.gis.StatusPillBadge
import com.example.ui.theme.*

@Composable
fun SearchDialog(
    onDismiss: () -> Unit,
    onSelectParcel: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember(searchQuery) {
        TerravaultRepository.searchParcels(searchQuery)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.82f),
            shape = RoundedCornerShape(16.dp),
            color = PureWhiteSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Search Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = ForestGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Global Parcel Search",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalTextPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search Parcel ID, Survey No, Owner, Village (e.g. TN-COI, 45/2A, Ravi)", fontSize = 13.sp) },
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

                // Quick suggestions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuggestionChip(
                        onClick = { searchQuery = "TN-COI-00123-0456" },
                        label = { Text("Demo: 45/2A", fontSize = 11.sp) }
                    )
                    SuggestionChip(
                        onClick = { searchQuery = "Singanallur" },
                        label = { Text("Singanallur", fontSize = 11.sp) }
                    )
                    SuggestionChip(
                        onClick = { searchQuery = "Ravi Kumar" },
                        label = { Text("Ravi Kumar", fontSize = 11.sp) }
                    )
                }

                HorizontalDivider(color = DividerColor)
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "MATCHING PARCELS (${searchResults.size})",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedSlate,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (searchResults.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.SearchOff, contentDescription = null, tint = MutedSlate, modifier = Modifier.size(40.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No matching parcel records found", fontSize = 13.sp, color = MutedSlate)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(searchResults) { parcel ->
                            SearchParcelResultItem(
                                parcel = parcel,
                                onClick = {
                                    onSelectParcel(parcel.id)
                                    onDismiss()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchParcelResultItem(
    parcel: Parcel,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = SurfaceVariant,
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
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusPillBadge(status = parcel.status)
                }

                Text(
                    text = "S.No: ${parcel.surveyNumber} • Owner: ${parcel.ownerName} • ${parcel.areaHectares} ha",
                    fontSize = 12.sp,
                    color = CharcoalTextSecondary,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Text(
                    text = "${parcel.village}, ${parcel.taluk} • ${parcel.currentLandUse.label}",
                    fontSize = 11.sp,
                    color = MutedSlate
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = ForestGreenPrimary
            )
        }
    }
}
