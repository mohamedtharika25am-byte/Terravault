package com.example.data.model

enum class ParcelStatus(val label: String, val colorHex: Long) {
    VERIFIED("Verified", 0xFF167A5B),
    NEEDS_REVIEW("Needs Review", 0xFFD99B2B),
    CRITICAL_ISSUE("Critical Issue", 0xFFD9534F),
    UNDER_VERIFICATION("Under Verification", 0xFF4B83B5)
}

enum class LandUseType(val label: String) {
    AGRICULTURAL("Agricultural"),
    RESIDENTIAL("Residential"),
    COMMERCIAL("Commercial"),
    INDUSTRIAL("Industrial"),
    WATER_BODY_BUFFER("Water Body / Buffer"),
    FOREST_RESERVE("Forest Reserve"),
    MIXED_USE("Mixed Use")
}

enum class DepartmentType(val label: String, val shortCode: String) {
    REVENUE("Revenue Department", "REV"),
    REGISTRATION("Registration (SRO)", "REG"),
    SURVEY("Survey & Land Records", "SUR"),
    TAX("Municipal / Property Tax", "TAX"),
    PLANNING("Urban Planning (DTCP/CMDA)", "PLN"),
    LEGAL("Courts & Judicial System", "LEG")
}

enum class IssueType(val label: String) {
    BOUNDARY_MISMATCH("Boundary Mismatch"),
    OWNERSHIP_CONFLICT("Ownership Conflict"),
    AREA_DISCREPANCY("Area Discrepancy"),
    LAND_USE_CONFLICT("Land Use Conflict"),
    MISSING_RECORD("Missing Record"),
    DUPLICATE_RECORD("Duplicate Record"),
    OUTDATED_RECORD("Outdated Record"),
    ENCUMBRANCE_ALERT("Encumbrance Alert")
}

enum class IssueSeverity(val label: String, val weight: Int) {
    CRITICAL("CRITICAL", 100),
    HIGH("HIGH", 70),
    MEDIUM("MEDIUM", 40),
    LOW("LOW", 15)
}

enum class IssueStatus(val label: String) {
    OPEN("Open"),
    UNDER_REVIEW("Under Review"),
    RESOLVED("Resolved"),
    REJECTED("Rejected")
}

enum class SourceStatus(val label: String) {
    VERIFIED("Verified"),
    CONFLICT("Conflict Detected"),
    MISSING("Missing Record"),
    SYNCED("Active Sync"),
    DEMO_MOCK("Demo / Mock")
}

data class CadastralPoint(
    val lat: Double,
    val lng: Double
)

data class DepartmentSourceRecord(
    val department: DepartmentType,
    val status: SourceStatus,
    val lastUpdated: String,
    val recordNumber: String,
    val details: String,
    val healthPercent: Int = 98,
    val latencyMs: Int = 120,
    val totalRecords: Int = 12480
)

data class ParcelIssue(
    val id: String,
    val parcelId: String,
    val issueType: IssueType,
    val severity: IssueSeverity,
    val department: DepartmentType,
    val title: String,
    val description: String,
    val detectedDate: String,
    var status: IssueStatus = IssueStatus.OPEN,
    val evidence: Map<String, String>,
    val recommendedAction: String,
    var resolvedBy: String? = null,
    var resolutionNotes: String? = null,
    var resolvedAt: String? = null
)

data class RiskFactor(
    val title: String,
    val severity: IssueSeverity,
    val points: Int,
    val reason: String
)

data class Parcel(
    val id: String, // e.g. "TN-COI-00123-0456"
    val surveyNumber: String, // e.g. "45/2A"
    val subDivision: String = "2A",
    val ownerName: String,
    val previousOwner: String = "S. Narayanaswamy (2014-2021)",
    val registrationDate: String = "14 Mar 2021",
    val deedNumber: String = "DOC-2021/SRO-CBE/4491",
    val areaHectares: Double, // Revenue declared
    val gisCalculatedArea: Double, // GIS satellite calculated
    val district: String = "Coimbatore",
    val taluk: String,
    val village: String,
    val currentLandUse: LandUseType,
    val declaredLandUse: LandUseType,
    val gisDetectedLandUse: LandUseType,
    var status: ParcelStatus,
    var riskScore: Int, // 0 to 100
    val verificationPercent: Int, // 0 to 100%
    val latitude: Double,
    val longitude: Double,
    val boundary: List<CadastralPoint>,
    val taxStatus: String = "Up to Date (₹4,850 paid)",
    val lastTaxPaymentDate: String = "12 Jan 2026",
    val outstandingTaxAmount: Double = 0.0,
    val courtCaseStatus: String = "No Pending Litigation",
    val encumbranceStatus: String = "Nil Encumbrance (2010-2026)",
    val legalStatus: String = "Clear Title",
    val surveyDate: String = "18 Nov 2023",
    val boundaryStatus: String = "DGPS Survey Verified",
    val departmentSources: List<DepartmentSourceRecord> = emptyList(),
    var issues: List<ParcelIssue> = emptyList(),
    val riskFactors: List<RiskFactor> = emptyList()
)

data class AuditLogEntry(
    val id: String,
    val timestamp: String,
    val action: String,
    val userRole: String,
    val parcelId: String?,
    val details: String
)

enum class UserRole(val title: String, val badge: String) {
    ADMIN("District Collector / Admin", "ADMIN"),
    GOVERNMENT_OFFICER("Tahsildar / Revenue Officer", "OFFICER"),
    REVIEWER("Cadastral Survey Inspector", "REVIEWER"),
    VIEWER("Public / Citizen Portal", "VIEWER")
}

data class UserProfile(
    val name: String = "Dr. S. Karthikeyan, IAS",
    val email: String = "admin@terravault.demo",
    val role: UserRole = UserRole.ADMIN,
    val designation: String = "Special Officer (Land Governance)",
    val jurisdiction: String = "Coimbatore District"
)
