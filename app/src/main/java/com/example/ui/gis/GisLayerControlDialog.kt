package com.example.ui.gis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GisLayerControlDialog(
    settings: GisLayerSettings,
    onSettingsChanged: (GisLayerSettings) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = PureWhiteSurface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = null,
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "GIS Layer Controls",
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
                text = "Toggle active spatial layers and cartographic visualizers.",
                fontSize = 13.sp,
                color = MutedSlate,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "BASE MAP THEME",
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
                MapStyle.entries.forEach { style ->
                    val isSelected = settings.mapStyle == style
                    Surface(
                        onClick = { onSettingsChanged(settings.copy(mapStyle = style)) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) ForestGreenPrimary else SurfaceVariant,
                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = when (style) {
                                    MapStyle.STREET_VECTOR -> Icons.Default.Map
                                    MapStyle.SATELLITE_HYBRID -> Icons.Default.Satellite
                                    MapStyle.CADASTRAL_GRID -> Icons.Default.GridOn
                                },
                                contentDescription = null,
                                tint = if (isSelected) Color.White else CharcoalTextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = when(style) {
                                    MapStyle.STREET_VECTOR -> "Vector"
                                    MapStyle.SATELLITE_HYBRID -> "Satellite"
                                    MapStyle.CADASTRAL_GRID -> "DGPS Grid"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) Color.White else CharcoalTextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SPATIAL DATA OVERLAYS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = ForestGreenPrimary,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            LayerSwitchItem(
                title = "Parcel Boundaries",
                subtitle = "Cadastral survey polygon borders and corner node pins",
                icon = Icons.Default.CropSquare,
                checked = settings.showParcelBoundaries,
                onCheckedChange = { onSettingsChanged(settings.copy(showParcelBoundaries = it)) }
            )

            LayerSwitchItem(
                title = "Land Use Zonal Overlay",
                subtitle = "Color-fill polygons by DTCP master plan category",
                icon = Icons.Default.Category,
                checked = settings.showLandUseColors,
                onCheckedChange = { onSettingsChanged(settings.copy(showLandUseColors = it)) }
            )

            LayerSwitchItem(
                title = "Owner & Survey Number Labels",
                subtitle = "Display survey markers and owner names on canvas",
                icon = Icons.Default.Label,
                checked = settings.showOwnershipLabels,
                onCheckedChange = { onSettingsChanged(settings.copy(showOwnershipLabels = it)) }
            )

            LayerSwitchItem(
                title = "Road & Transport Network",
                subtitle = "National highways, arterial roads and feeder streets",
                icon = Icons.Default.AltRoute,
                checked = settings.showRoadNetwork,
                onCheckedChange = { onSettingsChanged(settings.copy(showRoadNetwork = it)) }
            )

            LayerSwitchItem(
                title = "Water Bodies & Eco Buffers",
                subtitle = "Noyyal river, Singanallur lake and 50m preservation buffers",
                icon = Icons.Default.Water,
                checked = settings.showWaterBodies,
                onCheckedChange = { onSettingsChanged(settings.copy(showWaterBodies = it)) }
            )

            LayerSwitchItem(
                title = "Administrative Taluk Borders",
                subtitle = "Coimbatore North, South, Sulur, Pollachi, Mettupalayam",
                icon = Icons.Default.LocationCity,
                checked = settings.showTalukBoundaries,
                onCheckedChange = { onSettingsChanged(settings.copy(showTalukBoundaries = it)) }
            )

            LayerSwitchItem(
                title = "Conflict Hotspot Heatmap",
                subtitle = "Visual density of critical boundary and ownership disputes",
                icon = Icons.Default.Whatshot,
                checked = settings.showIssueHeatmap,
                onCheckedChange = { onSettingsChanged(settings.copy(showIssueHeatmap = it)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
            ) {
                Text("Apply Layers", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LayerSwitchItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (checked) ForestGreenLight else SurfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) ForestGreenPrimary else MutedSlate,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CharcoalTextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MutedSlate,
                lineHeight = 14.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = ForestGreenPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = SubtleBorder
            )
        )
    }
}
