package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.data.model.Parcel
import com.example.data.model.ParcelStatus
import com.example.data.repository.TerravaultRepository
import com.example.ui.gis.GisInspectorSheet
import com.example.ui.gis.GisLayerControlDialog
import com.example.ui.gis.GisLayerSettings
import com.example.ui.gis.GisMapView

@Composable
fun GisMapScreen(
    initialSelectedParcelId: String? = null,
    onNavigateToParcelDetail: (String) -> Unit,
    onNavigateToReport: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val parcels by TerravaultRepository.parcels.collectAsState()

    var selectedParcel by remember {
        mutableStateOf(
            if (initialSelectedParcelId != null) {
                parcels.find { it.id == initialSelectedParcelId }
            } else null
        )
    }

    LaunchedEffect(initialSelectedParcelId, parcels) {
        if (initialSelectedParcelId != null) {
            selectedParcel = parcels.find { it.id == initialSelectedParcelId }
        }
    }

    var layerSettings by remember { mutableStateOf(GisLayerSettings()) }
    var showLayersDialog by remember { mutableStateOf(false) }
    var statusFilter by remember { mutableStateOf<ParcelStatus?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        // Map Canvas
        GisMapView(
            parcels = parcels,
            selectedParcel = selectedParcel,
            onParcelSelected = { selectedParcel = it },
            layerSettings = layerSettings,
            onOpenLayers = { showLayersDialog = true },
            statusFilter = statusFilter,
            onStatusFilterChanged = { statusFilter = it }
        )

        // Inspector Bottom Sheet
        GisInspectorSheet(
            parcel = selectedParcel,
            onClose = { selectedParcel = null },
            onViewProfile = { parcelId -> onNavigateToParcelDetail(parcelId) },
            onGenerateReport = { parcelId -> onNavigateToReport(parcelId) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Layer Control Sheet
        if (showLayersDialog) {
            GisLayerControlDialog(
                settings = layerSettings,
                onSettingsChanged = { layerSettings = it },
                onDismiss = { showLayersDialog = false }
            )
        }
    }
}
