package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.data.repository.RuleEngine
import com.example.data.repository.TerravaultRepository
import com.example.ui.gis.StatusPillBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParcelDetailScreen(
    parcelId: String,
    onBack: () -> Unit,
    onNavigateToMap: (String) -> Unit,
    onNavigateToReport: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val parcels by TerravaultRepository.parcels.collectAsState()
    val parcel = parcels.find { it.id == parcelId }

    if (parcel == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Parcel Not Found", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = onBack) { Text("Go Back") }
            }
        }
        return
    }

    var selectedIssueToResolve by remember { mutableStateOf<ParcelIssue?>(null) }
    var resolutionNotes by remember { mutableStateOf("") }
    var resolutionReason by remember { mutableStateOf("Joint DGPS Resurvey Reconciled") }
    var showResurveySuccessDialog by remember { mutableStateOf(false) }

    val ruleEvaluation = remember(parcel) { RuleEngine.evaluateParcel(parcel) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = parcel.id,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = CharcoalTextPrimary
                        )
                        Text(
                            text = "Unified Single Source of Truth Profile",
                            fontSize = 11.sp,
                            color = MutedSlate
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = CharcoalTextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToMap(parcel.id) }) {
                        Icon(Icons.Default.Place, contentDescription = "View on GIS Map", tint = ForestGreenPrimary)
                    }
                    IconButton(onClick = { onNavigateToReport(parcel.id) }) {
                        Icon(Icons.Outlined.Description, contentDescription = "Generate Report", tint = ForestGreenPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureWhiteSurface)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(IvoryBackground)
                .padding(padding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // 1. Cadastral Header & Status Banner
            item {
                ParcelHeaderCard(
                    parcel = parcel,
                    ruleEvaluation = ruleEvaluation,
                    onOpenMap = { onNavigateToMap(parcel.id) }
                )
            }

            // 2. Mini Cadastral Polygon Map Canvas
            item {
                CadastralPolygonPreviewCard(
                    parcel = parcel,
                    onTriggerResurvey = {
                        showResurveySuccessDialog = true
                    }
                )
            }

            // 3. Transparent Rule-Based "Explainable Intelligence" Engine Breakdown
            item {
                ExplainableIntelligenceCard(ruleEvaluation = ruleEvaluation)
            }

            // 4. Multi-Department Data Reconciliation Matrix (6 Departments)
            item {
                Text(
                    text = "MULTI-DEPARTMENT DATA MATRIX (6 SOURCES)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                DepartmentSourcesMatrix(sources = parcel.departmentSources)
            }

            // 5. Detected Issues & Action Center
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DETECTED CONFLICTS & ACTIONS (${parcel.issues.size})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (parcel.issues.isNotEmpty()) DangerRed else ForestGreenPrimary,
                        letterSpacing = 1.sp
                    )
                }
            }

            if (parcel.issues.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = VerifiedGreenBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, VerifiedGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = VerifiedGreen, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("All Records Harmonized & Verified", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = CharcoalTextPrimary)
                                Text("Zero boundary variances or ownership disputes detected across all 6 departments.", fontSize = 11.sp, color = CharcoalTextSecondary)
                            }
                        }
                    }
                }
            } else {
                items(parcel.issues) { issue ->
                    ParcelIssueDetailCard(
                        issue = issue,
                        onResolve = { selectedIssueToResolve = issue }
                    )
                }
            }

            // 6. Primary Action Buttons
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = { onNavigateToReport(parcel.id) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary)
                    ) {
                        Icon(Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Truth Report", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (parcel.issues.isNotEmpty()) {
                                selectedIssueToResolve = parcel.issues.first()
                            } else {
                                showResurveySuccessDialog = true
                            }
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(48.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                    ) {
                        Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            if (parcel.issues.isNotEmpty()) "Resolve Conflict" else "Re-verify Parcel",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Modal Dialog: Resolve Discrepancy
    if (selectedIssueToResolve != null) {
        val issue = selectedIssueToResolve!!
        AlertDialog(
            onDismissRequest = { selectedIssueToResolve = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Gavel, contentDescription = null, tint = ForestGreenPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resolve Discrepancy", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Issue: ${issue.title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CharcoalTextPrimary
                    )

                    Text(
                        text = "Select reconciliation justification and enter official officer notes for the immutable audit log:",
                        fontSize = 11.sp,
                        color = CharcoalTextSecondary
                    )

                    val resolutionOptions = listOf(
                        "Joint Field DGPS Resurvey Reconciled",
                        "Certified Sub-Registrar Deed Verified",
                        "DTCP Zonal Regularization Fee Endorsed",
                        "High Court / District Court Order Complied"
                    )

                    resolutionOptions.forEach { opt ->
                        Surface(
                            onClick = { resolutionReason = opt },
                            shape = RoundedCornerShape(8.dp),
                            color = if (resolutionReason == opt) ForestGreenLight else SurfaceVariant,
                            border = if (resolutionReason == opt) androidx.compose.foundation.BorderStroke(1.dp, ForestGreenPrimary) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = resolutionReason == opt,
                                    onClick = { resolutionReason = opt },
                                    colors = RadioButtonDefaults.colors(selectedColor = ForestGreenPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = opt, fontSize = 11.sp, color = CharcoalTextPrimary)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = resolutionNotes,
                        onValueChange = { resolutionNotes = it },
                        label = { Text("Officer Resolution Notes & Reference ID") },
                        placeholder = { Text("e.g. Conducted joint DGPS resurvey with Revenue Inspector; coordinates matched within standard tolerance.") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ForestGreenPrimary,
                            unfocusedBorderColor = SubtleBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        TerravaultRepository.resolveIssue(
                            issueId = issue.id,
                            officerNotes = resolutionNotes.ifBlank { "Reconciled pursuant to cadastral land audit." },
                            resolutionReason = resolutionReason
                        )
                        selectedIssueToResolve = null
                        resolutionNotes = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("Apply & Harmonize")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedIssueToResolve = null }) {
                    Text("Cancel", color = MutedSlate)
                }
            }
        )
    }

    // Success Dialog: DGPS Resurvey
    if (showResurveySuccessDialog) {
        AlertDialog(
            onDismissRequest = { showResurveySuccessDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DGPS Resurvey Queued", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "A differential GPS field resurvey task has been issued to the Coimbatore Survey Division for Survey No. ${parcel.surveyNumber}. Coordinates will auto-sync upon inspector field upload.",
                    fontSize = 13.sp,
                    color = CharcoalTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { showResurveySuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun ParcelHeaderCard(
    parcel: Parcel,
    ruleEvaluation: RuleEngine.RuleEvaluationResult,
    onOpenMap: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SubtleBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = parcel.id,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = CharcoalTextPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusPillBadge(status = parcel.status)
                    }
                    Text(
                        text = "Survey S.No: ${parcel.surveyNumber} (Sub-Division ${parcel.subDivision})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestGreenPrimary,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "${parcel.village} Village, ${parcel.taluk} Taluk, ${parcel.district} District",
                        fontSize = 12.sp,
                        color = MutedSlate
                    )
                }

                // Integrity circular score
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                if (parcel.verificationPercent > 80) VerifiedGreenBg
                                else if (parcel.verificationPercent > 50) WarningAmberBg
                                else DangerRedBg
                            )
                            .border(
                                2.dp,
                                if (parcel.verificationPercent > 80) VerifiedGreen
                                else if (parcel.verificationPercent > 50) WarningAmber
                                else DangerRed,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${parcel.verificationPercent}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = if (parcel.verificationPercent > 80) VerifiedGreen else if (parcel.verificationPercent > 50) WarningAmber else DangerRed
                            )
                        }
                    }
                    Text(
                        text = "Truth Score",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MutedSlate,
                        modifier = Modifier.padding(top = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(12.dp))

            // 4 summary stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryStatBox(
                    label = "PRIMARY OWNER",
                    value = parcel.ownerName,
                    subtext = "Patta #PATTA-${parcel.surveyNumber}",
                    modifier = Modifier.weight(1f)
                )
                SummaryStatBox(
                    label = "SURVEY AREA",
                    value = "${parcel.areaHectares} ha",
                    subtext = "GIS: ${parcel.gisCalculatedArea} ha",
                    isWarning = parcel.areaHectares != parcel.gisCalculatedArea,
                    modifier = Modifier.weight(1f)
                )
                SummaryStatBox(
                    label = "LAND USE",
                    value = parcel.currentLandUse.label,
                    subtext = "Zoned: ${parcel.declaredLandUse.label}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryStatBox(
    label: String,
    value: String,
    subtext: String,
    isWarning: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = SurfaceVariant,
        border = if (isWarning) androidx.compose.foundation.BorderStroke(1.dp, WarningAmber.copy(alpha = 0.4f)) else null,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MutedSlate, letterSpacing = 0.5.sp)
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isWarning) WarningAmber else CharcoalTextPrimary, maxLines = 1)
            Text(text = subtext, fontSize = 9.sp, color = if (isWarning) WarningAmber else MutedSlate, maxLines = 1)
        }
    }
}

@Composable
private fun CadastralPolygonPreviewCard(
    parcel: Parcel,
    onTriggerResurvey: () -> Unit
) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Polyline, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Cadastral DGPS Polygon & Coordinates",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )
                }

                Button(
                    onClick = onTriggerResurvey,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenLight, contentColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.GpsFixed, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Trigger Resurvey", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Polygon mini canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF2F6F3))
                    .border(1.dp, SubtleBorder, RoundedCornerShape(8.dp))
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    val step = 25f
                    var gx = 0f
                    while (gx < w) {
                        drawLine(Color(0x1A167A5B), Offset(gx, 0f), Offset(gx, h), 0.8f)
                        gx += step
                    }
                    var gy = 0f
                    while (gy < h) {
                        drawLine(Color(0x1A167A5B), Offset(0f, gy), Offset(w, gy), 0.8f)
                        gy += step
                    }

                    // Polygon path
                    val path = Path()
                    val p1 = Offset(w * 0.25f, h * 0.20f)
                    val p2 = Offset(w * 0.78f, h * 0.18f)
                    val p3 = Offset(w * 0.85f, h * 0.80f)
                    val p4 = Offset(w * 0.18f, h * 0.75f)

                    path.moveTo(p1.x, p1.y)
                    path.lineTo(p2.x, p2.y)
                    path.lineTo(p3.x, p3.y)
                    path.lineTo(p4.x, p4.y)
                    path.close()

                    drawPath(path = path, color = Color(0x33167A5B), style = Fill)
                    drawPath(path = path, color = ForestGreenPrimary, style = Stroke(width = 2.5f))

                    // Node pins
                    listOf(p1, p2, p3, p4).forEachIndexed { idx, pt ->
                        drawCircle(color = WarningAmber, radius = 5f, center = pt)
                        drawCircle(color = ForestGreenDark, radius = 5f, center = pt, style = Stroke(1.5f))
                    }
                }

                // Corner coordinates chip
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = PureWhiteSurface.copy(alpha = 0.9f),
                    modifier = Modifier.align(Alignment.BottomEnd).padding(6.dp)
                ) {
                    Text(
                        text = "V1 (10.9995°N, 77.0110°E) • 4 Vertices",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = CharcoalTextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Variance banner if area differs
            if (parcel.areaHectares != parcel.gisCalculatedArea) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = WarningAmberBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, WarningAmber.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.WarningAmber, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Area Discrepancy of ${String.format("%.2f", kotlin.math.abs(parcel.areaHectares - parcel.gisCalculatedArea))} ha Detected",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalTextPrimary
                            )
                            Text(
                                text = "Revenue Record: ${parcel.areaHectares} ha • GIS Calculated Polygon: ${parcel.gisCalculatedArea} ha",
                                fontSize = 11.sp,
                                color = CharcoalTextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExplainableIntelligenceCard(ruleEvaluation: RuleEngine.RuleEvaluationResult) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SageGreenAccent.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(ForestGreenLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ForestGreenPrimary, modifier = Modifier.size(14.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Explainable Intelligence Engine",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestGreenPrimary
                    )
                }

                Text(
                    text = "Transparent Rules",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MutedSlate
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = ruleEvaluation.explanationSummary,
                fontSize = 12.sp,
                color = CharcoalTextSecondary,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = DividerColor)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "TRIGGERED LOGICAL RULES (${ruleEvaluation.riskFactors.size})",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MutedSlate,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            if (ruleEvaluation.riskFactors.isEmpty()) {
                Text(
                    text = "✓ All rules passed: zero area variances, matching registration deeds, compliant master plan land use.",
                    fontSize = 11.sp,
                    color = VerifiedGreen
                )
            } else {
                ruleEvaluation.riskFactors.forEach { factor ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (factor.severity == IssueSeverity.CRITICAL) DangerRed
                                    else if (factor.severity == IssueSeverity.HIGH) WarningAmber
                                    else ForestGreenPrimary
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = factor.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CharcoalTextPrimary
                            )
                            Text(
                                text = factor.reason,
                                fontSize = 10.5.sp,
                                color = CharcoalTextSecondary
                            )
                        }
                        Text(
                            text = "+${factor.points} pts",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (factor.severity == IssueSeverity.CRITICAL) DangerRed else WarningAmber
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DepartmentSourcesMatrix(sources: List<DepartmentSourceRecord>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        sources.forEach { source ->
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PureWhiteSurface,
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (source.status == SourceStatus.CONFLICT) DangerRed.copy(alpha = 0.3f) else SubtleBorder
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (source.status == SourceStatus.CONFLICT) DangerRedBg else ForestGreenLight),
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
                                tint = if (source.status == SourceStatus.CONFLICT) DangerRed else ForestGreenPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = source.department.label,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = CharcoalTextPrimary
                            )
                            Text(
                                text = source.details,
                                fontSize = 11.sp,
                                color = if (source.status == SourceStatus.CONFLICT) DangerRed else CharcoalTextSecondary,
                                maxLines = 1
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
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = source.status.name,
                            fontSize = 9.sp,
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
            }
        }
    }
}

@Composable
private fun ParcelIssueDetailCard(
    issue: ParcelIssue,
    onResolve: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (issue.severity == IssueSeverity.CRITICAL) DangerRed.copy(alpha = 0.4f) else WarningAmber.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WarningAmber,
                        contentDescription = null,
                        tint = if (issue.severity == IssueSeverity.CRITICAL) DangerRed else WarningAmber,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = issue.title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CharcoalTextPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (issue.severity == IssueSeverity.CRITICAL) DangerRedBg else WarningAmberBg)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = issue.severity.label.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (issue.severity == IssueSeverity.CRITICAL) DangerRed else WarningAmber
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = issue.description, fontSize = 12.sp, color = CharcoalTextSecondary)

            if (issue.evidence.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = SurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "EVIDENCE TRAIL:", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MutedSlate)
                        issue.evidence.forEach { (k, v) ->
                            Text(text = "• $k: $v", fontSize = 10.5.sp, color = CharcoalTextPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Recommended Action: ${issue.recommendedAction}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ForestGreenPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onResolve,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.Gavel, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Resolve Discrepancy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
