package com.example.ui.gis

data class GisLayerSettings(
    val showParcelBoundaries: Boolean = true,
    val showLandUseColors: Boolean = false,
    val showOwnershipLabels: Boolean = true,
    val showRoadNetwork: Boolean = true,
    val showWaterBodies: Boolean = true,
    val showTalukBoundaries: Boolean = true,
    val showIssueHeatmap: Boolean = false,
    val mapStyle: MapStyle = MapStyle.STREET_VECTOR
)

enum class MapStyle(val label: String) {
    STREET_VECTOR("OpenStreetMap Vector"),
    SATELLITE_HYBRID("Satellite Hybrid"),
    CADASTRAL_GRID("Cadastral DGPS Grid")
}
