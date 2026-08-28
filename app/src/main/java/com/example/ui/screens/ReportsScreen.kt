package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Parcel
import com.example.data.model.ParcelStatus
import com.example.data.repository.TerravaultRepository
import com.example.ui.gis.StatusPillBadge
import com.example.ui.theme.*

@Composable
fun ReportsScreen(
    initialParcelId: String? = null,
    onNavigateToParcel: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val parcels by TerravaultRepository.parcels.collectAsState()
    var selectedParcelId by remember {
        mutableStateOf(initialParcelId ?: "TN-COI-00123-0456")
    }

    val selectedParcel = remember(parcels, selectedParcelId) {
        parcels.find { it.id == selectedParcelId } ?: parcels.firstOrNull()
    }

    var showDownloadSuccess by remember { mutableStateOf(false) }

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
                        text = "Official Land Truth Certificates",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = CharcoalTextPrimary
                    )
                    Text(
                        text = "Verifiable unified cadastral dossiers for courts, banks & citizens",
                        fontSize = 12.sp,
                        color = MutedSlate
                    )
                }

                Button(
                    onClick = { showDownloadSuccess = true },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export PDF", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Parcel Selector dropdown bar
        item {
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
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
                    Text(
                        text = "Selected Parcel: ${selectedParcel?.id ?: "None"} (S.No ${selectedParcel?.surveyNumber})",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = CharcoalTextPrimary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { selectedParcelId = "TN-COI-00123-0456" },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedParcelId == "TN-COI-00123-0456") ForestGreenPrimary else SurfaceVariant,
                                contentColor = if (selectedParcelId == "TN-COI-00123-0456") Color.White else CharcoalTextPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Demo: 45/2A", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { selectedParcelId = "TN-COI-00344-0789" },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedParcelId == "TN-COI-00344-0789") ForestGreenPrimary else SurfaceVariant,
                                contentColor = if (selectedParcelId == "TN-COI-00344-0789") Color.White else CharcoalTextPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                            modifier = Modifier.height(28.dp)
                        ) {
                            Text("Verified: 12/4C", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Official Government Certificate Document Card
        if (selectedParcel != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhiteSurface),
                    border = androidx.compose.foundation.BorderStroke(2.dp, ForestGreenPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Certificate Header
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(ForestGreenPrimary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "GOVERNMENT OF TAMIL NADU",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = ForestGreenDark,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "DIGITAL PUBLIC INFRASTRUCTURE FOR LAND GOVERNANCE",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MutedSlate,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "TERRAVAULT UNIFIED CADASTRAL TRUTH CERTIFICATE",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = CharcoalTextPrimary,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Text(
                                text = "Certificate ID: TV-CERT-2026-COI-${selectedParcel.surveyNumber.replace("/", "")}",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                color = MutedSlate
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = ForestGreenPrimary, thickness = 1.5.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Identification Table
                        Text(
                            text = "1. CADASTRAL PARCEL IDENTIFICATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        CertificateFieldRow("Unique Parcel ID", selectedParcel.id)
                        CertificateFieldRow("Survey & Sub-Division", "${selectedParcel.surveyNumber} (Sub-Division ${selectedParcel.subDivision})")
                        CertificateFieldRow("Village / Taluk / District", "${selectedParcel.village} / ${selectedParcel.taluk} / ${selectedParcel.district}")
                        CertificateFieldRow("Primary Patta Owner", selectedParcel.ownerName)
                        CertificateFieldRow("Recorded Survey Area", "${selectedParcel.areaHectares} Hectares")
                        CertificateFieldRow("GIS DGPS Calculated Area", "${selectedParcel.gisCalculatedArea} Hectares")
                        CertificateFieldRow("Land Use Classification", selectedParcel.currentLandUse.label)

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "2. MULTI-DEPARTMENT VERIFICATION AUDIT",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        selectedParcel.departmentSources.forEach { source ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 2.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = source.department.label, fontSize = 11.sp, color = CharcoalTextSecondary)
                                Text(
                                    text = source.status.name,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (source.status == com.example.data.model.SourceStatus.CONFLICT) DangerRed else VerifiedGreen
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "3. CONFLICT & LITIGATION STATUS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        CertificateFieldRow("Court Litigation Status", selectedParcel.courtCaseStatus)
                        CertificateFieldRow("Encumbrance Status", selectedParcel.encumbranceStatus)
                        CertificateFieldRow("Municipal Tax Status", selectedParcel.taxStatus)
                        CertificateFieldRow("Overall Integrity Score", "${selectedParcel.verificationPercent}% (${selectedParcel.status.label})")

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = DividerColor)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Footer Digital Stamp & QR Verification
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "DIGITALLY ATTESTED BY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = MutedSlate)
                                Text(text = "TERRAVAULT STATE REVENUE GATEWAY", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                Text(text = "Cryptographic Hash: 8f9b...a41c", fontSize = 9.sp, fontFamily = FontFamily.Monospace, color = MutedSlate)
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceVariant)
                                    .border(1.dp, SubtleBorder, RoundedCornerShape(6.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.QrCode, contentDescription = "Verification QR", tint = CharcoalTextPrimary, modifier = Modifier.size(34.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDownloadSuccess) {
        AlertDialog(
            onDismissRequest = { showDownloadSuccess = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = VerifiedGreen)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Certificate Exported", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    text = "Official Truth Certificate TV-CERT-2026-COI-${selectedParcel?.surveyNumber?.replace("/", "")} generated successfully with tamper-evident digital seal.",
                    fontSize = 13.sp,
                    color = CharcoalTextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { showDownloadSuccess = false },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreenPrimary)
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun CertificateFieldRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 11.sp, color = MutedSlate)
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CharcoalTextPrimary)
    }
}
