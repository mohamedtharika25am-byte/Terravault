package com.example.ui.gis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CadastralPoint
import com.example.data.model.LandUseType
import com.example.data.model.Parcel
import com.example.data.model.ParcelStatus
import com.example.ui.theme.*
import kotlin.math.abs
import kotlin.math.hypot

@Composable
fun GisMapView(
    parcels: List<Parcel>,
    selectedParcel: Parcel?,
    onParcelSelected: (Parcel?) -> Unit,
    layerSettings: GisLayerSettings,
    onOpenLayers: () -> Unit,
    statusFilter: ParcelStatus?,
    onStatusFilterChanged: (ParcelStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    // GIS Map Projection state
    // Center around Coimbatore: 11.0168° N, 76.9558° E
    var centerLat by remember { mutableDoubleStateOf(11.0120) }
    var centerLng by remember { mutableDoubleStateOf(76.9950) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffsetX by remember { mutableFloatStateOf(0f) }
    var panOffsetY by remember { mutableFloatStateOf(0f) }

    val textMeasurer = rememberTextMeasurer()

    // Filter parcels by status
    val filteredParcels = remember(parcels, statusFilter) {
        if (statusFilter == null) parcels else parcels.filter { it.status == statusFilter }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(if (layerSettings.mapStyle == MapStyle.SATELLITE_HYBRID) Color(0xFF1E2825) else IvoryBackground)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        zoomScale = (zoomScale * zoom).coerceIn(0.5f, 4.5f)
                        panOffsetX += pan.x
                        panOffsetY += pan.y
                    }
                }
                .pointerInput(filteredParcels, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY) {
                    detectTapGestures { tapOffset ->
                        // Hit test parcels
                        val canvasWidth = size.width.toFloat()
                        val canvasHeight = size.height.toFloat()

                        var clickedParcel: Parcel? = null
                        var minDistance = Float.MAX_VALUE

                        for (p in filteredParcels) {
                            val screenX = projectLngToX(p.longitude, centerLng, canvasWidth, zoomScale, panOffsetX)
                            val screenY = projectLatToY(p.latitude, centerLat, canvasHeight, zoomScale, panOffsetY)
                            val dist = hypot(tapOffset.x - screenX, tapOffset.y - screenY)
                            if (dist < 50f * zoomScale && dist < minDistance) {
                                minDistance = dist
                                clickedParcel = p
                            }
                        }

                        onParcelSelected(clickedParcel)
                    }
                }
        ) {
            val width = size.width
            val height = size.height

            // 1. Draw Base Cartographic Layer
            drawGisBaseMap(
                width = width,
                height = height,
                centerLat = centerLat,
                centerLng = centerLng,
                zoomScale = zoomScale,
                panOffsetX = panOffsetX,
                panOffsetY = panOffsetY,
                settings = layerSettings,
                textMeasurer = textMeasurer
            )

            // 2. Draw Roads Network if enabled
            if (layerSettings.showRoadNetwork) {
                drawRoadNetwork(
                    width = width,
                    height = height,
                    centerLat = centerLat,
                    centerLng = centerLng,
                    zoomScale = zoomScale,
                    panOffsetX = panOffsetX,
                    panOffsetY = panOffsetY
                )
            }

            // 3. Draw Water Bodies if enabled
            if (layerSettings.showWaterBodies) {
                drawWaterBodies(
                    width = width,
                    height = height,
                    centerLat = centerLat,
                    centerLng = centerLng,
                    zoomScale = zoomScale,
                    panOffsetX = panOffsetX,
                    panOffsetY = panOffsetY
                )
            }

            // 4. Draw Taluk Boundaries if enabled
            if (layerSettings.showTalukBoundaries) {
                drawTalukBoundaries(
                    width = width,
                    height = height,
                    centerLat = centerLat,
                    centerLng = centerLng,
                    zoomScale = zoomScale,
                    panOffsetX = panOffsetX,
                    panOffsetY = panOffsetY,
                    textMeasurer = textMeasurer
                )
            }

            // 5. Draw Conflict Heatmap Hotspots if enabled
            if (layerSettings.showIssueHeatmap) {
                filteredParcels.filter { it.status == ParcelStatus.CRITICAL_ISSUE || it.status == ParcelStatus.NEEDS_REVIEW }.forEach { p ->
                    val x = projectLngToX(p.longitude, centerLng, width, zoomScale, panOffsetX)
                    val y = projectLatToY(p.latitude, centerLat, height, zoomScale, panOffsetY)
                    val radius = if (p.status == ParcelStatus.CRITICAL_ISSUE) 90f * zoomScale else 60f * zoomScale
                    val color = if (p.status == ParcelStatus.CRITICAL_ISSUE) DangerRed.copy(alpha = 0.25f) else WarningAmber.copy(alpha = 0.20f)
                    drawCircle(color = color, radius = radius, center = Offset(x, y))
                }
            }

            // 6. Draw Cadastral Parcel Polygons
            if (layerSettings.showParcelBoundaries) {
                filteredParcels.forEach { parcel ->
                    val isSelected = selectedParcel?.id == parcel.id
                    drawParcelPolygon(
                        parcel = parcel,
                        isSelected = isSelected,
                        width = width,
                        height = height,
                        centerLat = centerLat,
                        centerLng = centerLng,
                        zoomScale = zoomScale,
                        panOffsetX = panOffsetX,
                        panOffsetY = panOffsetY,
                        layerSettings = layerSettings,
                        textMeasurer = textMeasurer
                    )
                }
            }
        }

        // Top Map Filter Bar & Layer Trigger
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Filter Chips
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MapFilterChip(
                        label = "All (${parcels.size})",
                        selected = statusFilter == null,
                        onClick = { onStatusFilterChanged(null) }
                    )
                    MapFilterChip(
                        label = "Critical",
                        selected = statusFilter == ParcelStatus.CRITICAL_ISSUE,
                        color = DangerRed,
                        onClick = { onStatusFilterChanged(if (statusFilter == ParcelStatus.CRITICAL_ISSUE) null else ParcelStatus.CRITICAL_ISSUE) }
                    )
                    MapFilterChip(
                        label = "Review",
                        selected = statusFilter == ParcelStatus.NEEDS_REVIEW,
                        color = WarningAmber,
                        onClick = { onStatusFilterChanged(if (statusFilter == ParcelStatus.NEEDS_REVIEW) null else ParcelStatus.NEEDS_REVIEW) }
                    )
                    MapFilterChip(
                        label = "Verified",
                        selected = statusFilter == ParcelStatus.VERIFIED,
                        color = VerifiedGreen,
                        onClick = { onStatusFilterChanged(if (statusFilter == ParcelStatus.VERIFIED) null else ParcelStatus.VERIFIED) }
                    )
                }

                // Layer control button
                IconButton(
                    onClick = onOpenLayers,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(PureWhiteSurface)
                        .shadow(4.dp, CircleShape)
                        .border(1.dp, SubtleBorder, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Layers,
                        contentDescription = "GIS Layers",
                        tint = ForestGreenPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Floating GIS Navigation Controls (Zoom in, Zoom out, Center, Reset)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GisFloatingButton(icon = Icons.Default.Add, contentDesc = "Zoom In") {
                zoomScale = (zoomScale * 1.3f).coerceAtMost(4.5f)
            }
            GisFloatingButton(icon = Icons.Default.Remove, contentDesc = "Zoom Out") {
                zoomScale = (zoomScale / 1.3f).coerceAtLeast(0.5f)
            }
            GisFloatingButton(icon = Icons.Default.MyLocation, contentDesc = "Center Coimbatore") {
                centerLat = 11.0120
                centerLng = 76.9950
                panOffsetX = 0f
                panOffsetY = 0f
                zoomScale = 1.0f
            }
        }

        // Bottom Left Coordinates & Scale Bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = if (selectedParcel != null) 230.dp else 16.dp),
            shape = RoundedCornerShape(6.dp),
            color = PureWhiteSurface.copy(alpha = 0.9f),
            border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "EPSG:4326 • 11°00'N 77°00'E • ${(zoomScale * 100).toInt()}%",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = CharcoalTextSecondary
                )
            }
        }
    }
}

@Composable
private fun MapFilterChip(
    label: String,
    selected: Boolean,
    color: Color = ForestGreenPrimary,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) color else PureWhiteSurface.copy(alpha = 0.95f),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (selected) color else SubtleBorder),
        shadowElevation = 2.dp,
        modifier = Modifier.height(32.dp)
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
private fun GisFloatingButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = PureWhiteSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        shadowElevation = 4.dp,
        modifier = Modifier.size(38.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDesc,
                tint = CharcoalTextPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

// ---------------- GIS Projection & Drawing Utilities ----------------

private fun projectLngToX(lng: Double, centerLng: Double, width: Float, zoom: Float, panX: Float): Float {
    val baseScale = width * 1800f // Scaling factor for Coimbatore coordinate spread (~0.3 degrees)
    return (width / 2f) + ((lng - centerLng) * baseScale * zoom).toFloat() + panX
}

private fun projectLatToY(lat: Double, centerLat: Double, height: Float, zoom: Float, panY: Float): Float {
    val baseScale = height * 2200f
    // Latitude increases upwards, so screen Y decreases
    return (height / 2f) - ((lat - centerLat) * baseScale * zoom).toFloat() + panY
}

private fun DrawScope.drawGisBaseMap(
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    settings: GisLayerSettings,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val isSatellite = settings.mapStyle == MapStyle.SATELLITE_HYBRID
    val isDgpsGrid = settings.mapStyle == MapStyle.CADASTRAL_GRID

    // 1. Background Fill
    drawRect(color = if (isSatellite) Color(0xFF1B2824) else if (isDgpsGrid) Color(0xFFEEF5F1) else IvoryBackground)

    // 2. Cadastral Grid Lines (Lat/Lng graticule)
    val gridColor = if (isSatellite) Color(0x1AFFFFFF) else Color(0x2E167A5B)
    val step = 40f * zoomScale

    var x = panOffsetX % step
    while (x < width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = 0.8f
        )
        x += step
    }

    var y = panOffsetY % step
    while (y < height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = 0.8f
        )
        y += step
    }
}

private fun DrawScope.drawRoadNetwork(
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float
) {
    val roadColor = Color(0xFFD4CDC0)
    val highwayColor = Color(0xFFF1BE7A)

    // NH-544 Avinashi Road corridor
    val nh544 = listOf(
        11.0080 to 76.9550,
        11.0180 to 76.9950,
        11.0280 to 77.0350,
        11.0450 to 77.0850,
        11.0580 to 77.1250
    )
    drawPolyline(nh544, highwayColor, 4.5f * zoomScale, width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)

    // Trichy Road corridor
    val trichyRoad = listOf(
        10.9950 to 76.9600,
        10.9960 to 76.9950,
        10.9980 to 77.0450,
        11.0120 to 77.0950
    )
    drawPolyline(trichyRoad, roadColor, 3f * zoomScale, width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)

    // Pollachi Road
    val pollachiRoad = listOf(
        10.9950 to 76.9600,
        10.9520 to 76.9740,
        10.8850 to 76.9900,
        10.7850 to 77.0120,
        10.6650 to 77.0080
    )
    drawPolyline(pollachiRoad, roadColor, 3f * zoomScale, width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)

    // Mettupalayam Road
    val mtpRoad = listOf(
        11.0180 to 76.9550,
        11.0720 to 76.9420,
        11.1500 to 76.9500,
        11.2420 to 76.9620,
        11.3120 to 77.0080
    )
    drawPolyline(mtpRoad, roadColor, 3f * zoomScale, width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)
}

private fun DrawScope.drawWaterBodies(
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float
) {
    // Noyyal River Polyline
    val noyyalRiver = listOf(
        10.9700 to 76.8800,
        10.9750 to 76.9150,
        10.9850 to 76.9500,
        10.9900 to 76.9900,
        10.9950 to 77.0300,
        11.0020 to 77.0800,
        11.0150 to 77.1400
    )
    drawPolyline(noyyalRiver, Color(0xFF86BCD8), 5f * zoomScale, width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)

    // Singanallur Lake Polygon
    val singanallurLake = listOf(
        10.9940 to 77.0210,
        10.9960 to 77.0260,
        10.9910 to 77.0280,
        10.9880 to 77.0230,
        10.9940 to 77.0210
    )
    drawFilledPolygon(singanallurLake, Color(0x6676B6DB), Color(0xFF4B94BE), width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)

    // Valankulam Lake
    val valankulam = listOf(
        10.9920 to 76.9680,
        10.9960 to 76.9740,
        10.9880 to 76.9750,
        10.9920 to 76.9680
    )
    drawFilledPolygon(valankulam, Color(0x6676B6DB), Color(0xFF4B94BE), width, height, centerLat, centerLng, zoomScale, panOffsetX, panOffsetY)
}

private fun DrawScope.drawTalukBoundaries(
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    // Taluk centroid labels
    val taluks = listOf(
        "COIMBATORE SOUTH" to (10.9880 to 76.9700),
        "COIMBATORE NORTH" to (11.0450 to 76.9750),
        "SULUR" to (11.0350 to 77.1100),
        "POLLACHI" to (10.7200 to 77.0150),
        "METTUPALAYAM" to (11.2600 to 76.9700)
    )

    taluks.forEach { (name, coord) ->
        val x = projectLngToX(coord.second, centerLng, width, zoomScale, panOffsetX)
        val y = projectLatToY(coord.first, centerLat, height, zoomScale, panOffsetY)
        if (x in -100f..(width + 100f) && y in -100f..(height + 100f)) {
            val textLayout = textMeasurer.measure(
                text = name,
                style = TextStyle(fontSize = (11 * zoomScale.coerceIn(0.8f, 1.4f)).sp, fontWeight = FontWeight.Bold, color = Color(0x551F2D2D), letterSpacing = 2.sp)
            )
            drawText(textLayout, topLeft = Offset(x - textLayout.size.width / 2, y))
        }
    }
}

private fun DrawScope.drawParcelPolygon(
    parcel: Parcel,
    isSelected: Boolean,
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoomScale: Float,
    panOffsetX: Float,
    panOffsetY: Float,
    layerSettings: GisLayerSettings,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    if (parcel.boundary.isEmpty()) return

    val path = Path()
    var isFirst = true

    val screenPoints = parcel.boundary.map { pt ->
        val x = projectLngToX(pt.lng, centerLng, width, zoomScale, panOffsetX)
        val y = projectLatToY(pt.lat, centerLat, height, zoomScale, panOffsetY)
        Offset(x, y)
    }

    screenPoints.forEach { pt ->
        if (isFirst) {
            path.moveTo(pt.x, pt.y)
            isFirst = false
        } else {
            path.lineTo(pt.x, pt.y)
        }
    }
    path.close()

    // Determine colors
    val (fillColor, strokeColor) = if (layerSettings.showLandUseColors) {
        when (parcel.currentLandUse) {
            LandUseType.AGRICULTURAL -> Color(0x444CAF50) to Color(0xFF2E7D32)
            LandUseType.COMMERCIAL -> Color(0x442196F3) to Color(0xFF1565C0)
            LandUseType.RESIDENTIAL -> Color(0x44FF9800) to Color(0xFFE65100)
            LandUseType.INDUSTRIAL -> Color(0x449C27B0) to Color(0xFF6A1B9A)
            LandUseType.WATER_BODY_BUFFER -> Color(0x4400BCD4) to Color(0xFF00838F)
            LandUseType.FOREST_RESERVE -> Color(0x44009688) to Color(0xFF004D40)
            LandUseType.MIXED_USE -> Color(0x44795548) to Color(0xFF4E342E)
        }
    } else {
        when (parcel.status) {
            ParcelStatus.VERIFIED -> MapParcelFillVerified to MapParcelStrokeVerified
            ParcelStatus.NEEDS_REVIEW -> MapParcelFillReview to MapParcelStrokeReview
            ParcelStatus.CRITICAL_ISSUE -> MapParcelFillCritical to MapParcelStrokeCritical
            ParcelStatus.UNDER_VERIFICATION -> MapParcelFillPending to MapParcelStrokePending
        }
    }

    // Draw Fill
    drawPath(path = path, color = if (isSelected) fillColor.copy(alpha = 0.7f) else fillColor, style = Fill)

    // Draw Stroke
    val strokeWidth = if (isSelected) 3.5f * zoomScale else 1.8f * zoomScale
    drawPath(path = path, color = if (isSelected) ForestGreenDark else strokeColor, style = Stroke(width = strokeWidth))

    // Draw Boundary Node Pins at corners
    screenPoints.forEach { pt ->
        drawCircle(
            color = if (isSelected) WarningAmber else PureWhiteSurface,
            radius = if (isSelected) 4.5f * zoomScale else 3f * zoomScale,
            center = pt
        )
        drawCircle(
            color = if (isSelected) ForestGreenPrimary else strokeColor,
            radius = if (isSelected) 4.5f * zoomScale else 3f * zoomScale,
            center = pt,
            style = Stroke(width = 1.2f)
        )
    }

    // Centroid coordinate for Survey Number and Owner label
    val centroidX = screenPoints.map { it.x }.average().toFloat()
    val centroidY = screenPoints.map { it.y }.average().toFloat()

    // Draw Survey Number Badge
    if (zoomScale >= 0.7f) {
        val labelText = "S.No ${parcel.surveyNumber}"
        val textLayout = textMeasurer.measure(
            text = labelText,
            style = TextStyle(fontSize = (9 * zoomScale.coerceIn(0.7f, 1.2f)).sp, fontWeight = FontWeight.Bold, color = CharcoalTextPrimary)
        )

        val pillWidth = textLayout.size.width + 12f
        val pillHeight = textLayout.size.height + 6f
        val pillLeft = centroidX - pillWidth / 2
        val pillTop = centroidY - pillHeight / 2

        drawRoundRect(
            color = PureWhiteSurface.copy(alpha = 0.95f),
            topLeft = Offset(pillLeft, pillTop),
            size = androidx.compose.ui.geometry.Size(pillWidth, pillHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f)
        )
        drawRoundRect(
            color = if (isSelected) ForestGreenPrimary else strokeColor.copy(alpha = 0.6f),
            topLeft = Offset(pillLeft, pillTop),
            size = androidx.compose.ui.geometry.Size(pillWidth, pillHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f),
            style = Stroke(width = 1f)
        )
        drawText(textLayout, topLeft = Offset(centroidX - textLayout.size.width / 2, centroidY - textLayout.size.height / 2))
    }

    // If selected, draw animated pulse / bounding indicator
    if (isSelected) {
        drawCircle(
            color = ForestGreenPrimary.copy(alpha = 0.2f),
            radius = 28f * zoomScale,
            center = Offset(centroidX, centroidY)
        )
    }
}

private fun DrawScope.drawPolyline(
    coords: List<Pair<Double, Double>>,
    color: Color,
    strokeWidth: Float,
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoom: Float,
    panX: Float,
    panY: Float
) {
    if (coords.size < 2) return
    val path = Path()
    var first = true
    coords.forEach { (lat, lng) ->
        val x = projectLngToX(lng, centerLng, width, zoom, panX)
        val y = projectLatToY(lat, centerLat, height, zoom, panY)
        if (first) {
            path.moveTo(x, y)
            first = false
        } else {
            path.lineTo(x, y)
        }
    }
    drawPath(path = path, color = color, style = Stroke(width = strokeWidth, cap = StrokeCap.Round, join = StrokeJoin.Round))
}

private fun DrawScope.drawFilledPolygon(
    coords: List<Pair<Double, Double>>,
    fillColor: Color,
    strokeColor: Color,
    width: Float,
    height: Float,
    centerLat: Double,
    centerLng: Double,
    zoom: Float,
    panX: Float,
    panY: Float
) {
    if (coords.size < 3) return
    val path = Path()
    var first = true
    coords.forEach { (lat, lng) ->
        val x = projectLngToX(lng, centerLng, width, zoom, panX)
        val y = projectLatToY(lat, centerLat, height, zoom, panY)
        if (first) {
            path.moveTo(x, y)
            first = false
        } else {
            path.lineTo(x, y)
        }
    }
    path.close()
    drawPath(path = path, color = fillColor, style = Fill)
    drawPath(path = path, color = strokeColor, style = Stroke(width = 1.5f))
}
