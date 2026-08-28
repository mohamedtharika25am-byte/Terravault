package com.example.data.repository

import com.example.data.model.*
import kotlin.math.abs
import kotlin.math.min

object RuleEngine {

    data class RuleEvaluationResult(
        val detectedIssues: List<ParcelIssue>,
        val riskScore: Int,
        val verificationPercent: Int,
        val status: ParcelStatus,
        val riskFactors: List<RiskFactor>,
        val explanationSummary: String
    )

    fun evaluateParcel(parcel: Parcel): RuleEvaluationResult {
        val issues = mutableListOf<ParcelIssue>()
        val riskFactors = mutableListOf<RiskFactor>()

        // 1. Boundary & Area Discrepancy Rule
        val areaDiff = abs(parcel.areaHectares - parcel.gisCalculatedArea)
        if (areaDiff > 0.03) {
            val severity = if (areaDiff > 0.15) IssueSeverity.CRITICAL else if (areaDiff > 0.08) IssueSeverity.HIGH else IssueSeverity.MEDIUM
            val issue = ParcelIssue(
                id = "ISS-${parcel.id.takeLast(4)}-01",
                parcelId = parcel.id,
                issueType = if (areaDiff > 0.10) IssueType.BOUNDARY_MISMATCH else IssueType.AREA_DISCREPANCY,
                severity = severity,
                department = DepartmentType.SURVEY,
                title = "Cadastral Survey vs GIS Area Mismatch (${String.format("%.2f", areaDiff)} ha)",
                description = "Revenue records state ${parcel.areaHectares} ha while GIS polygon calculation yields ${parcel.gisCalculatedArea} ha.",
                detectedDate = "2026-08-20",
                status = IssueStatus.OPEN,
                evidence = mapOf(
                    "Survey Record Area" to "${parcel.areaHectares} ha",
                    "GIS Satellite Area" to "${parcel.gisCalculatedArea} ha",
                    "Calculated Variance" to "${String.format("%.2f", areaDiff)} ha (${String.format("%.1f", (areaDiff / parcel.areaHectares) * 100)}%)",
                    "Survey Station" to "Coimbatore DGPS Grid #44"
                ),
                recommendedAction = "Initiate physical DGPS resurvey to reconcile cadastral boundary coordinates with Revenue Survey Office."
            )
            issues.add(issue)
            riskFactors.add(
                RiskFactor(
                    title = "Boundary Area Variance",
                    severity = severity,
                    points = severity.weight,
                    reason = "Physical survey polygon differs from GIS boundary by ${String.format("%.2f", areaDiff)} ha."
                )
            )
        }

        // 2. Ownership & Registration Conflict Rule
        val regRecord = parcel.departmentSources.find { it.department == DepartmentType.REGISTRATION }
        val revRecord = parcel.departmentSources.find { it.department == DepartmentType.REVENUE }
        if (regRecord?.status == SourceStatus.CONFLICT || revRecord?.status == SourceStatus.CONFLICT) {
            val issue = ParcelIssue(
                id = "ISS-${parcel.id.takeLast(4)}-02",
                parcelId = parcel.id,
                issueType = IssueType.OWNERSHIP_CONFLICT,
                severity = IssueSeverity.HIGH,
                department = DepartmentType.REGISTRATION,
                title = "Cross-Department Ownership Mismatch",
                description = "Revenue Patta lists ${parcel.ownerName} but Sub-Registrar Office deed has a pending transfer or dual claimant record.",
                detectedDate = "2026-08-22",
                status = IssueStatus.OPEN,
                evidence = mapOf(
                    "Revenue Patta Owner" to parcel.ownerName,
                    "Registration Deed Party" to (parcel.previousOwner.split(" ").firstOrNull()?.let { "$it (Claimant)" } ?: "Conflicting Claimant"),
                    "Deed Reference" to parcel.deedNumber,
                    "Sub-Registrar Jurisdiction" to "SRO Coimbatore District"
                ),
                recommendedAction = "Cross-check latest registered encumbrance certificate and verify mutation index at Taluk Revenue Office."
            )
            issues.add(issue)
            riskFactors.add(
                RiskFactor(
                    title = "Ownership Record Conflict",
                    severity = IssueSeverity.HIGH,
                    points = IssueSeverity.HIGH.weight,
                    reason = "Inconsistent claimant names detected between Revenue Patta Register and SRO Registered Sale Deed."
                )
            )
        }

        // 3. Land Use & Master Plan Violation Rule
        if (parcel.currentLandUse != parcel.declaredLandUse || parcel.currentLandUse != parcel.gisDetectedLandUse) {
            val isBufferViolation = parcel.gisDetectedLandUse == LandUseType.WATER_BODY_BUFFER || parcel.declaredLandUse == LandUseType.WATER_BODY_BUFFER
            val severity = if (isBufferViolation) IssueSeverity.CRITICAL else IssueSeverity.MEDIUM
            val issue = ParcelIssue(
                id = "ISS-${parcel.id.takeLast(4)}-03",
                parcelId = parcel.id,
                issueType = IssueType.LAND_USE_CONFLICT,
                severity = severity,
                department = DepartmentType.PLANNING,
                title = if (isBufferViolation) "Critical Water-Body / Buffer Zone Encroachment" else "Land Use Classification Conflict",
                description = "Declared as ${parcel.declaredLandUse.label}, but satellite GIS spectral analysis detects active ${parcel.gisDetectedLandUse.label} usage.",
                detectedDate = "2026-08-24",
                status = IssueStatus.OPEN,
                evidence = mapOf(
                    "Declared Master Plan" to parcel.declaredLandUse.label,
                    "GIS Detected Spectral" to parcel.gisDetectedLandUse.label,
                    "Current Revenue Entry" to parcel.currentLandUse.label,
                    "DTCP Zone Clearance" to if (isBufferViolation) "NO (Prohibited Buffer)" else "Conditional Clearance"
                ),
                recommendedAction = if (isBufferViolation) 
                    "Issue immediate notice for wetland/water body preservation review under Section 4(1) of TN Land Encroachment Act."
                else 
                    "Submit for DTCP Land Conversion re-assessment and verify commercial property tax slab."
            )
            issues.add(issue)
            riskFactors.add(
                RiskFactor(
                    title = "Land Use Zonal Discrepancy",
                    severity = severity,
                    points = severity.weight,
                    reason = "Satellite GIS indices contradict declared DTCP master plan classification."
                )
            )
        }

        // 4. Missing Record / Court Case Rule
        if (parcel.courtCaseStatus.contains("Pending") || parcel.courtCaseStatus.contains("Injunction") || parcel.courtCaseStatus.contains("Suit")) {
            val severity = IssueSeverity.HIGH
            val issue = ParcelIssue(
                id = "ISS-${parcel.id.takeLast(4)}-04",
                parcelId = parcel.id,
                issueType = IssueType.ENCUMBRANCE_ALERT,
                severity = severity,
                department = DepartmentType.LEGAL,
                title = "Pending Judicial Litigation / Injunction Alert",
                description = "Active court litigation found: ${parcel.courtCaseStatus}. Property transfer prohibited under sub-judice status.",
                detectedDate = "2026-08-15",
                status = IssueStatus.OPEN,
                evidence = mapOf(
                    "Case Tracking" to parcel.courtCaseStatus,
                    "Court Name" to "Principal District Court, Coimbatore",
                    "Encumbrance Status" to parcel.encumbranceStatus,
                    "Injunction Status" to "Active Status Quo Order"
                ),
                recommendedAction = "Sync with e-Courts National Judicial Data Grid (NJDG) to obtain latest order copy before proceeding with mutation."
            )
            issues.add(issue)
            riskFactors.add(
                RiskFactor(
                    title = "Active Court Litigation",
                    severity = severity,
                    points = severity.weight,
                    reason = "Court case registered on survey number; legal encumbrance restricts transfer."
                )
            )
        }

        val missingSource = parcel.departmentSources.find { it.status == SourceStatus.MISSING }
        if (missingSource != null) {
            val severity = IssueSeverity.MEDIUM
            val issue = ParcelIssue(
                id = "ISS-${parcel.id.takeLast(4)}-05",
                parcelId = parcel.id,
                issueType = IssueType.MISSING_RECORD,
                severity = severity,
                department = missingSource.department,
                title = "Missing Record from ${missingSource.department.label}",
                description = "No synchronized record found for Survey No. ${parcel.surveyNumber} in the ${missingSource.department.label} database.",
                detectedDate = "2026-08-18",
                status = IssueStatus.OPEN,
                evidence = mapOf(
                    "Missing System" to missingSource.department.label,
                    "Expected Identifier" to parcel.surveyNumber,
                    "Integration Protocol" to "State Digital Land Gateway REST API",
                    "Status Code" to "404 Record Not Found"
                ),
                recommendedAction = "Issue digital data ingestion request to the ${missingSource.department.label} nodal officer."
            )
            issues.add(issue)
            riskFactors.add(
                RiskFactor(
                    title = "Fragmented / Missing Record",
                    severity = severity,
                    points = severity.weight,
                    reason = "Crucial record absent in ${missingSource.department.label} database."
                )
            )
        }

        // Calculate weighted Risk Score (0 - 100)
        val calculatedRisk = if (riskFactors.isEmpty()) {
            0
        } else {
            val maxPoints = riskFactors.maxOf { it.points }
            val additionalPenalty = (riskFactors.size - 1) * 10
            min(100, maxPoints + additionalPenalty)
        }

        val calculatedVerification = if (calculatedRisk == 0) {
            98
        } else {
            maxOf(15, 100 - calculatedRisk)
        }

        val computedStatus = when {
            calculatedRisk >= 75 -> ParcelStatus.CRITICAL_ISSUE
            calculatedRisk in 35..74 -> ParcelStatus.NEEDS_REVIEW
            issues.any { it.status == IssueStatus.UNDER_REVIEW } -> ParcelStatus.UNDER_VERIFICATION
            else -> ParcelStatus.VERIFIED
        }

        val explanation = when {
            calculatedRisk >= 75 -> "Critical risk triggered due to severe high-penalty conflicts (e.g. wetland encroachment or legal injunctions) requiring immediate intervention."
            calculatedRisk in 35..74 -> "Moderate risk detected due to boundary area variances or cross-department data mismatches that require officer review."
            else -> "High integrity parcel with all 6 department records fully synchronized and cadastral polygon matching GIS coordinates."
        }

        return RuleEvaluationResult(
            detectedIssues = if (issues.isEmpty()) parcel.issues else issues,
            riskScore = calculatedRisk,
            verificationPercent = calculatedVerification,
            status = computedStatus,
            riskFactors = riskFactors,
            explanationSummary = explanation
        )
    }
}
