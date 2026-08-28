package com.example.data.repository

import com.example.data.MockSeedData
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DashboardKPIs(
    val totalParcels: Int,
    val verifiedCount: Int,
    val needsReviewCount: Int,
    val criticalCount: Int,
    val underVerificationCount: Int,
    val totalAreaHectares: Double,
    val openIssuesCount: Int,
    val resolvedIssuesCount: Int,
    val verificationRate: Double
)

data class AnalyticsSummary(
    val issuesBySeverity: Map<IssueSeverity, Int>,
    val issuesByDepartment: Map<DepartmentType, Int>,
    val issuesByType: Map<IssueType, Int>,
    val landUseDistribution: Map<LandUseType, Int>,
    val talukRiskScores: Map<String, Double>,
    val monthlyTrends: List<Pair<String, Int>>,
    val mostCommonIssue: String,
    val highestRiskRegion: String,
    val departmentRequiringAttention: String
)

object TerravaultRepository {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _parcels = MutableStateFlow<List<Parcel>>(emptyList())
    val parcels: StateFlow<List<Parcel>> = _parcels.asStateFlow()

    private val _issues = MutableStateFlow<List<ParcelIssue>>(emptyList())
    val issues: StateFlow<List<ParcelIssue>> = _issues.asStateFlow()

    private val _dataSources = MutableStateFlow<List<DepartmentSourceRecord>>(emptyList())
    val dataSources: StateFlow<List<DepartmentSourceRecord>> = _dataSources.asStateFlow()

    private val _auditLogs = MutableStateFlow<List<AuditLogEntry>>(emptyList())
    val auditLogs: StateFlow<List<AuditLogEntry>> = _auditLogs.asStateFlow()

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncProgress = MutableStateFlow(0f)
    val syncProgress: StateFlow<Float> = _syncProgress.asStateFlow()

    init {
        initializeData()
    }

    fun initializeData() {
        val initialParcels = MockSeedData.generateParcels()
        _parcels.value = initialParcels

        val allIssues = initialParcels.flatMap { it.issues }
        _issues.value = allIssues

        _dataSources.value = listOf(
            DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.SYNCED, "2026-08-27 14:30", "PATTA-GATEWAY-V3", "Connected: Tamil Nadu e-Sevai & e-District Patta Portal", 99, 85, 12480),
            DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.SYNCED, "2026-08-27 14:30", "STAR2-IGRS-API", "Connected: Inspector General of Registration STAR 2.0", 97, 140, 12480),
            DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.CONFLICT, "2026-08-27 14:28", "COLLAB-LAND-DGPS", "Connected: Tamil Nilam & CollabLand DGPS Vector Database", 92, 210, 12480),
            DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.SYNCED, "2026-08-27 14:25", "MUNICIPAL-REV-CBE", "Connected: Coimbatore Municipal Corporation Urban Tax Server", 98, 95, 12480),
            DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.SYNCED, "2026-08-27 14:20", "DTCP-GIS-ZONING", "Connected: Directorate of Town and Country Planning (DTCP)", 96, 175, 12480),
            DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.DEMO_MOCK, "2026-08-27 14:00", "NJDG-ECOURTS-GATEWAY", "Connected: National Judicial Data Grid (e-Courts Tamil Nadu)", 88, 320, 12480)
        )

        _auditLogs.value = listOf(
            AuditLogEntry("LOG-1099", "2026-08-27 14:15", "System Rule Engine Audit", "SYSTEM", "TN-COI-00123-0456", "Detected 0.15 ha boundary mismatch & dual claimant on S.No 45/2A"),
            AuditLogEntry("LOG-1098", "2026-08-27 13:40", "Wetland Encroachment Alert", "AI_RULE_ENGINE", "TN-COI-00892-1102", "Flagged 0.45 ha intrusion into Singanallur Lake Ramsar buffer zone"),
            AuditLogEntry("LOG-1097", "2026-08-27 11:20", "Data Synchronization", "SYSTEM_DAEMON", null, "Synchronized 12,480 cadastral ledger records across 6 departments"),
            AuditLogEntry("LOG-1096", "2026-08-27 09:10", "DGPS Resurvey Approval", "REVIEWER", "TN-COI-00344-0789", "Cleared Survey No. 12/4C with 99% integrity score")
        )
    }

    fun getParcelById(parcelId: String): Parcel? {
        return _parcels.value.find { it.id.equals(parcelId, ignoreCase = true) }
    }

    fun getIssueById(issueId: String): ParcelIssue? {
        return _issues.value.find { it.id.equals(issueId, ignoreCase = true) }
    }

    fun searchParcels(query: String): List<Parcel> {
        if (query.isBlank()) return _parcels.value
        val q = query.trim().lowercase()
        return _parcels.value.filter {
            it.id.lowercase().contains(q) ||
            it.surveyNumber.lowercase().contains(q) ||
            it.ownerName.lowercase().contains(q) ||
            it.village.lowercase().contains(q) ||
            it.taluk.lowercase().contains(q) ||
            it.deedNumber.lowercase().contains(q)
        }
    }

    fun resolveIssue(issueId: String, officerNotes: String, resolutionReason: String): Boolean {
        val currentIssues = _issues.value.toMutableList()
        val issueIndex = currentIssues.indexOfFirst { it.id == issueId }
        if (issueIndex == -1) return false

        val oldIssue = currentIssues[issueIndex]
        val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val updatedIssue = oldIssue.copy(
            status = IssueStatus.RESOLVED,
            resolvedBy = _userProfile.value.name,
            resolutionNotes = "$resolutionReason: $officerNotes",
            resolvedAt = timeStamp
        )
        currentIssues[issueIndex] = updatedIssue
        _issues.value = currentIssues

        // Update Parcel state & Re-evaluate rules
        val currentParcels = _parcels.value.toMutableList()
        val parcelIndex = currentParcels.indexOfFirst { it.id == updatedIssue.parcelId }
        if (parcelIndex != -1) {
            val parcel = currentParcels[parcelIndex]
            val remainingOpenIssues = currentIssues.filter { it.parcelId == parcel.id && it.status != IssueStatus.RESOLVED && it.status != IssueStatus.REJECTED }
            
            val newRisk = if (remainingOpenIssues.isEmpty()) 5 else (remainingOpenIssues.maxOfOrNull { it.severity.weight } ?: 0)
            val newStatus = when {
                remainingOpenIssues.isEmpty() -> ParcelStatus.VERIFIED
                newRisk >= 75 -> ParcelStatus.CRITICAL_ISSUE
                newRisk in 35..74 -> ParcelStatus.NEEDS_REVIEW
                else -> ParcelStatus.UNDER_VERIFICATION
            }

            currentParcels[parcelIndex] = parcel.copy(
                status = newStatus,
                riskScore = newRisk,
                verificationPercent = if (newStatus == ParcelStatus.VERIFIED) 98 else 100 - newRisk,
                issues = currentIssues.filter { it.parcelId == parcel.id }
            )
            _parcels.value = currentParcels
        }

        // Add to audit trail
        val newAudit = AuditLogEntry(
            id = "LOG-${System.currentTimeMillis().toString().takeLast(4)}",
            timestamp = timeStamp,
            action = "Issue Resolved by Officer",
            userRole = _userProfile.value.role.badge,
            parcelId = updatedIssue.parcelId,
            details = "Resolved '${updatedIssue.title}' - $resolutionReason ($officerNotes)"
        )
        _auditLogs.value = listOf(newAudit) + _auditLogs.value
        return true
    }

    fun syncAllDataSources(onComplete: (() -> Unit)? = null) {
        scope.launch {
            _isSyncing.value = true
            _syncProgress.value = 0.1f
            delay(400)
            _syncProgress.value = 0.35f
            delay(500)
            _syncProgress.value = 0.70f
            delay(400)
            _syncProgress.value = 1.0f
            delay(300)

            val now = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            _dataSources.value = _dataSources.value.map {
                it.copy(lastUpdated = now, status = SourceStatus.SYNCED, latencyMs = (80..180).random())
            }

            val newAudit = AuditLogEntry(
                id = "LOG-${System.currentTimeMillis().toString().takeLast(4)}",
                timestamp = now,
                action = "Full Multi-Department Sync",
                userRole = _userProfile.value.role.badge,
                parcelId = null,
                details = "Completed sync cycle across Revenue, SRO, Survey, Tax, Planning & NJDG"
            )
            _auditLogs.value = listOf(newAudit) + _auditLogs.value

            _isSyncing.value = false
            _syncProgress.value = 0f
            onComplete?.invoke()
        }
    }

    fun updateUserRole(newRole: UserRole) {
        _userProfile.value = _userProfile.value.copy(
            role = newRole,
            designation = when (newRole) {
                UserRole.ADMIN -> "District Collector / Special Officer (Land Governance)"
                UserRole.GOVERNMENT_OFFICER -> "Tahsildar / Zonal Revenue Officer"
                UserRole.REVIEWER -> "Senior Cadastral Survey Inspector"
                UserRole.VIEWER -> "Citizen / Public Land Records Portal"
            }
        )
    }

    fun addCustomDataSource(deptName: String, url: String, deptType: DepartmentType) {
        val now = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        val newRecord = DepartmentSourceRecord(
            department = deptType,
            status = SourceStatus.SYNCED,
            lastUpdated = now,
            recordNumber = "CUSTOM-GATEWAY-${(100..999).random()}",
            details = "Connected to $deptName at $url",
            healthPercent = 99,
            latencyMs = 110,
            totalRecords = 12480
        )
        _dataSources.value = _dataSources.value + newRecord
    }

    fun getDashboardKPIs(): DashboardKPIs {
        val list = _parcels.value
        val total = 12480 // Simulated district scale
        val verified = 9842
        val needsReview = 1726
        val critical = 912
        val pending = total - (verified + needsReview + critical)

        val totalArea = list.sumOf { it.areaHectares }
        val openIssues = _issues.value.count { it.status == IssueStatus.OPEN }
        val resolvedIssues = _issues.value.count { it.status == IssueStatus.RESOLVED }

        return DashboardKPIs(
            totalParcels = total,
            verifiedCount = verified,
            needsReviewCount = needsReview,
            criticalCount = critical,
            underVerificationCount = pending,
            totalAreaHectares = totalArea,
            openIssuesCount = openIssues,
            resolvedIssuesCount = resolvedIssues,
            verificationRate = (verified.toDouble() / total) * 100
        )
    }

    fun getAnalyticsSummary(): AnalyticsSummary {
        val allIssues = _issues.value
        val allParcels = _parcels.value

        val bySeverity = IssueSeverity.entries.associateWith { s ->
            allIssues.count { it.severity == s }
        }

        val byDept = DepartmentType.entries.associateWith { d ->
            allIssues.count { it.department == d }
        }

        val byType = IssueType.entries.associateWith { t ->
            allIssues.count { it.issueType == t }
        }

        val landUseMap = LandUseType.entries.associateWith { l ->
            allParcels.count { it.currentLandUse == l }
        }

        val talukRisks = allParcels.groupBy { it.taluk }.mapValues { (_, pList) ->
            pList.map { it.riskScore }.average()
        }

        val monthlyTrends = listOf(
            "Mar" to 142,
            "Apr" to 198,
            "May" to 165,
            "Jun" to 220,
            "Jul" to 280,
            "Aug" to 310
        )

        return AnalyticsSummary(
            issuesBySeverity = bySeverity,
            issuesByDepartment = byDept,
            issuesByType = byType,
            landUseDistribution = landUseMap,
            talukRiskScores = talukRisks,
            monthlyTrends = monthlyTrends,
            mostCommonIssue = "Boundary Mismatch & Area Discrepancy",
            highestRiskRegion = "Coimbatore South (Wetland & High Density)",
            departmentRequiringAttention = "Survey & Land Records (DGPS Resurvey Backlog)"
        )
    }
}
