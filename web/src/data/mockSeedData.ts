import { Parcel, ParcelIssue, DepartmentType, LandUseType, ParcelStatus, IssueSeverity, IssueStatus, SourceStatus, AuditLogEntry, DepartmentSourceRecord } from '../types/parcel';

export const initialParcels: Parcel[] = [
  // 1. Featured SIH Presentation Demo Parcel: TN-COI-00123-0456
  {
    id: "TN-COI-00123-0456",
    surveyNumber: "45/2A",
    subDivision: "2A",
    ownerName: "Ravi Kumar",
    previousOwner: "Kumar Raj (Transfer Claim Dispute)",
    registrationDate: "14 Mar 2021",
    deedNumber: "DOC-2021/SRO-CBE-S/4491",
    areaHectares: 1.25,
    gisCalculatedArea: 1.10,
    district: "Coimbatore",
    taluk: "Coimbatore South",
    village: "Singanallur",
    currentLandUse: "AGRICULTURAL",
    declaredLandUse: "AGRICULTURAL",
    gisDetectedLandUse: "MIXED_USE",
    status: "NEEDS_REVIEW",
    riskScore: 65,
    verificationPercent: 68,
    latitude: 10.9984,
    longitude: 77.0125,
    boundary: [
      { lat: 10.9995, lng: 77.0110 },
      { lat: 11.0002, lng: 77.0138 },
      { lat: 10.9978, lng: 77.0145 },
      { lat: 10.9969, lng: 77.0118 },
      { lat: 10.9995, lng: 77.0110 }
    ],
    taxStatus: "Pending ₹14,200",
    lastTaxPaymentDate: "10 Dec 2024",
    outstandingTaxAmount: 14200.0,
    courtCaseStatus: "Civil Suit Pending: OS 241/2023",
    encumbranceStatus: "Mortgage & Partition Dispute noted",
    legalStatus: "Sub-Judice (Status Quo Order)",
    surveyDate: "18 Nov 2022",
    boundaryStatus: "Variance Detected (>0.15 ha)",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-26 09:30", recordNumber: "PATTA-2021-9982", details: "Owner: Ravi Kumar, Area: 1.25 ha", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "CONFLICT", lastUpdated: "2026-08-25 14:15", recordNumber: "DEED-4491/2021", details: "Mismatch: Secondary claimant Kumar Raj on deed", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "CONFLICT", lastUpdated: "2026-08-20 11:00", recordNumber: "FMB-45-2A-2022", details: "Survey FMB area (1.25 ha) vs GIS polygon (1.10 ha)", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-15 16:45", recordNumber: "TAX-CBE-S-4401", details: "Assessment: Agricultural S-2 (Arrears ₹14,200)", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-10 10:20", recordNumber: "DTCP-ZON-9902", details: "Designated Green Agro Belt", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "CONFLICT", lastUpdated: "2026-08-24 15:30", recordNumber: "NJDG-OS-241-2023", details: "Active Injunction Case pending at District Court", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [
      {
        id: "ISS-0456-01",
        parcelId: "TN-COI-00123-0456",
        issueType: "BOUNDARY_MISMATCH",
        severity: "HIGH",
        department: "SURVEY",
        title: "Cadastral Survey vs GIS Boundary Mismatch (0.15 ha)",
        description: "Survey records indicate 1.25 ha but satellite DGPS GIS polygon measures 1.10 ha.",
        detectedDate: "2026-08-20",
        status: "OPEN",
        evidence: {
          "Survey FMB Area": "1.25 ha",
          "GIS Computed Area": "1.10 ha",
          "Area Variance": "0.15 ha (-12.0%)",
          "Survey Reference": "FMB Map Sheet 45-Singanallur"
        },
        recommendedAction: "Verify latest survey boundary and execute joint field DGPS resurvey with Revenue Inspector."
      },
      {
        id: "ISS-0456-02",
        parcelId: "TN-COI-00123-0456",
        issueType: "OWNERSHIP_CONFLICT",
        severity: "HIGH",
        department: "REGISTRATION",
        title: "Ownership Conflict between Revenue Patta & SRO Deed",
        description: "Revenue department registers Ravi Kumar, while Sub-Registrar records list Kumar Raj.",
        detectedDate: "2026-08-22",
        status: "OPEN",
        evidence: {
          "Revenue Patta": "Ravi Kumar (100% Share)",
          "Registration Record": "Kumar Raj (Disputed Deed 4491/2021)",
          "SRO Office": "Coimbatore South Sub-Registrar"
        },
        recommendedAction: "Cross-check latest registered deed and conduct enquiry before issuing mutation clearance."
      },
      {
        id: "ISS-0456-03",
        parcelId: "TN-COI-00123-0456",
        issueType: "ENCUMBRANCE_ALERT",
        severity: "HIGH",
        department: "LEGAL",
        title: "Court Injunction: OS 241/2023 Pending",
        description: "Principal District Court Coimbatore has issued an interim status quo order.",
        detectedDate: "2026-08-24",
        status: "OPEN",
        evidence: {
          "Case Number": "OS 241/2023",
          "Court": "Principal District Munsif, Coimbatore",
          "Interim Order": "Status Quo on Alienation & Construction"
        },
        recommendedAction: "Hold mutation processing pending certified court outcome copy."
      }
    ],
    riskFactors: [
      { title: "Boundary Discrepancy", severity: "HIGH", points: 70, reason: "Survey vs GIS area differs by 0.15 ha" },
      { title: "Dual Ownership Claim", severity: "HIGH", points: 70, reason: "Revenue Patta and SRO Sale Deed mismatch" },
      { title: "Active Litigation", severity: "HIGH", points: 70, reason: "Court Injunction OS 241/2023" }
    ]
  },

  // 2. Critical Parcel: Singanallur Lake Wetland Encroachment
  {
    id: "TN-COI-00892-1102",
    surveyNumber: "89/1B",
    subDivision: "1B",
    ownerName: "Apex Logistics & Infrastructure Ltd.",
    previousOwner: "K. Velusamy (2018)",
    registrationDate: "05 Feb 2023",
    deedNumber: "DOC-2023/SRO-CBE-S/1288",
    areaHectares: 3.80,
    gisCalculatedArea: 4.25,
    district: "Coimbatore",
    taluk: "Coimbatore South",
    village: "Singanallur",
    currentLandUse: "COMMERCIAL",
    declaredLandUse: "COMMERCIAL",
    gisDetectedLandUse: "WATER_BODY_BUFFER",
    status: "CRITICAL_ISSUE",
    riskScore: 95,
    verificationPercent: 25,
    latitude: 10.9920,
    longitude: 77.0220,
    boundary: [
      { lat: 10.9935, lng: 77.0200 },
      { lat: 10.9942, lng: 77.0245 },
      { lat: 10.9908, lng: 77.0252 },
      { lat: 10.9898, lng: 77.0210 },
      { lat: 10.9935, lng: 77.0200 }
    ],
    taxStatus: "Arrears ₹85,000",
    lastTaxPaymentDate: "14 Mar 2023",
    outstandingTaxAmount: 85000.0,
    courtCaseStatus: "WP 8901/2024 (Madras High Court Green Bench)",
    encumbranceStatus: "Restricted Waterway Buffer Zone",
    legalStatus: "Show-Cause Notice Issued under TN Land Encroachment Act",
    surveyDate: "10 Jan 2024",
    boundaryStatus: "Encroachment on 50m Lake Eco-Sensitive Buffer",
    departmentSources: [
      { department: "REVENUE", status: "CONFLICT", lastUpdated: "2026-08-25 10:00", recordNumber: "PATTA-89-1B", details: "Recorded as Commercial but encroaches wetland", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-24 16:20", recordNumber: "DEED-1288/2023", details: "Registered sale deed valid", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "CONFLICT", lastUpdated: "2026-08-22 14:00", recordNumber: "FMB-89-1B", details: "Boundary overlaps 0.45 ha into Public Waterbody Tank", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "CONFLICT", lastUpdated: "2026-08-18 11:30", recordNumber: "TAX-COMM-891", details: "Commercial tax unpaid ₹85,000", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "CONFLICT", lastUpdated: "2026-08-21 09:15", recordNumber: "DTCP-ZON-CRIT", details: "Violates 50m Singanallur Lake Eco Buffer", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "CONFLICT", lastUpdated: "2026-08-26 12:00", recordNumber: "HC-WP-8901-2024", details: "Madras High Court stay on commercial expansion", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [
      {
        id: "ISS-1102-01",
        parcelId: "TN-COI-00892-1102",
        issueType: "LAND_USE_CONFLICT",
        severity: "CRITICAL",
        department: "PLANNING",
        title: "Singanallur Wetland Buffer Encroachment (0.45 ha)",
        description: "Satellite GIS confirms construction within prohibited 50m eco-sensitive lake boundary.",
        detectedDate: "2026-08-21",
        status: "OPEN",
        evidence: {
          "Eco Buffer Zone": "Singanallur Lake Ramsar Candidate Wetland",
          "Overlapping Area": "0.45 ha (11.8% of site)",
          "DTCP Clearance": "REJECTED (Eco-sensitive Zone 4)"
        },
        recommendedAction: "Issue demolition and recovery order under Tamil Nadu Protection of Tanks and Eviction of Encroachment Act."
      },
      {
        id: "ISS-1102-02",
        parcelId: "TN-COI-00892-1102",
        issueType: "BOUNDARY_MISMATCH",
        severity: "CRITICAL",
        department: "SURVEY",
        title: "Cadastral Boundary Expansion into Waterbody Poramboke",
        description: "Survey boundary expanded by 0.45 ha into State Water Resource Department channel.",
        detectedDate: "2026-08-22",
        status: "OPEN",
        evidence: {
          "WRD Water Channel ID": "NOYYAL-FEEDER-CH-12",
          "Calculated Intrusion": "4,500 sq. meters"
        },
        recommendedAction: "Summon WRD Executive Engineer and Revenue DRO for immediate spot sealing."
      }
    ],
    riskFactors: [
      { title: "Wetland Encroachment", severity: "CRITICAL", points: 100, reason: "Commercial construction inside waterbody buffer" },
      { title: "Boundary Expansion", severity: "CRITICAL", points: 100, reason: "Intrusion into state water channel" },
      { title: "High Court Stay", severity: "HIGH", points: 70, reason: "Active High Court PIL against construction" }
    ]
  },

  // 3. Fully Verified Parcel: Singanallur Agro Plot
  {
    id: "TN-COI-00344-0789",
    surveyNumber: "12/4C",
    subDivision: "4C",
    ownerName: "M. Shanmugasundaram",
    previousOwner: "K. Marimuthu (Inheritance 2012)",
    registrationDate: "19 Jun 2012",
    deedNumber: "DOC-2012/SRO-CBE-S/1104",
    areaHectares: 2.40,
    gisCalculatedArea: 2.39,
    district: "Coimbatore",
    taluk: "Coimbatore South",
    village: "Singanallur",
    currentLandUse: "AGRICULTURAL",
    declaredLandUse: "AGRICULTURAL",
    gisDetectedLandUse: "AGRICULTURAL",
    status: "VERIFIED",
    riskScore: 5,
    verificationPercent: 99,
    latitude: 11.0040,
    longitude: 77.0080,
    boundary: [
      { lat: 11.0055, lng: 77.0065 },
      { lat: 11.0062, lng: 77.0100 },
      { lat: 11.0028, lng: 77.0105 },
      { lat: 11.0020, lng: 77.0070 },
      { lat: 11.0055, lng: 77.0065 }
    ],
    taxStatus: "Up to Date (₹2,400 paid)",
    lastTaxPaymentDate: "10 Jan 2026",
    outstandingTaxAmount: 0.0,
    courtCaseStatus: "No Litigation Found",
    encumbranceStatus: "Nil Encumbrance (1990-2026)",
    legalStatus: "Clean Title (Verified by Collectorate)",
    surveyDate: "14 Feb 2024",
    boundaryStatus: "Cadastral DGPS 100% Match",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-27 08:00", recordNumber: "PATTA-12-4C", details: "Clean Title, Patta 12/4C", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-27 08:00", recordNumber: "DEED-1104/2012", details: "Ancestral Partition Deed in Order", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "VERIFIED", lastUpdated: "2026-08-27 08:00", recordNumber: "FMB-12-4C", details: "0.01 ha variance (Well within 0.05 tolerance)", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-27 08:00", recordNumber: "TAX-AGR-1209", details: "Zero tax arrears", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-27 08:00", recordNumber: "DTCP-ZON-AGRO", details: "Standard Agricultural Zone", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "VERIFIED", lastUpdated: "2026-08-27 08:00", recordNumber: "NJDG-NIL", details: "Zero active or historical cases", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [],
    riskFactors: []
  },

  // 4. Peelamedu IT Corridor Parcel: Under Verification
  {
    id: "TN-COI-00411-0988",
    surveyNumber: "58/1A",
    subDivision: "1A",
    ownerName: "Titan Tech Innovations LLP",
    previousOwner: "G. Balasubramaniam",
    registrationDate: "22 Nov 2024",
    deedNumber: "DOC-2024/SRO-CBE-N/7740",
    areaHectares: 0.85,
    gisCalculatedArea: 0.84,
    district: "Coimbatore",
    taluk: "Coimbatore North",
    village: "Peelamedu",
    currentLandUse: "COMMERCIAL",
    declaredLandUse: "COMMERCIAL",
    gisDetectedLandUse: "COMMERCIAL",
    status: "UNDER_VERIFICATION",
    riskScore: 20,
    verificationPercent: 88,
    latitude: 11.0250,
    longitude: 77.0150,
    boundary: [
      { lat: 11.0265, lng: 77.0135 },
      { lat: 11.0270, lng: 77.0170 },
      { lat: 11.0238, lng: 77.0175 },
      { lat: 11.0230, lng: 77.0140 },
      { lat: 11.0265, lng: 77.0135 }
    ],
    taxStatus: "Up to Date (₹48,200 paid)",
    lastTaxPaymentDate: "05 Feb 2026",
    outstandingTaxAmount: 0.0,
    courtCaseStatus: "No Litigation",
    encumbranceStatus: "HDFC Bank Project Loan Lien",
    legalStatus: "Clear Title with Registered Mortgage",
    surveyDate: "15 Jan 2026",
    boundaryStatus: "Re-survey Under Review by Tahsildar",
    departmentSources: [
      { department: "REVENUE", status: "SYNCED", lastUpdated: "2026-08-27 10:00", recordNumber: "PATTA-58-1A", details: "Commercial Patta under inspection", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-27 10:00", recordNumber: "DEED-7740/2024", details: "Registered deed verified", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "SYNCED", lastUpdated: "2026-08-27 10:00", recordNumber: "FMB-58-1A", details: "Joint site survey scheduled", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-27 10:00", recordNumber: "TAX-COM-9921", details: "Commercial property tax up to date", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-27 10:00", recordNumber: "DTCP-IT-ZONE", details: "Approved IT Park Zone", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "VERIFIED", lastUpdated: "2026-08-27 10:00", recordNumber: "NJDG-NIL", details: "Zero litigation", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [
      {
        id: "ISS-0988-01",
        parcelId: "TN-COI-00411-0988",
        issueType: "OUTDATED_RECORD",
        severity: "LOW",
        department: "SURVEY",
        title: "Pending Routine Mutation Endorsement",
        description: "Commercial conversion approval granted by DTCP; awaiting final Tahsildar digital signature on updated FMB.",
        detectedDate: "2026-08-25",
        status: "UNDER_REVIEW",
        evidence: {
          "Application Reference": "MUT-2026-CBE-N-991",
          "DTCP Conversion Order": "DTCP/CBE/2025/1109",
          "Pending Office": "Tahsildar Coimbatore North"
        },
        recommendedAction: "Expedite electronic digital signature on Form 7A mutation register."
      }
    ],
    riskFactors: [
      { title: "Pending Routine Signature", severity: "LOW", points: 15, reason: "Awaiting Tahsildar e-sign on updated FMB" }
    ]
  },

  // 5. Saravanampatti IT & Tech Park: Needs Review
  {
    id: "TN-COI-00512-0921",
    surveyNumber: "77/3A",
    subDivision: "3A",
    ownerName: "S. Soundararajan & Sons",
    previousOwner: "T. Rangaswamy",
    registrationDate: "11 Oct 2020",
    deedNumber: "DOC-2020/SRO-CBE-N/3310",
    areaHectares: 1.95,
    gisCalculatedArea: 1.83,
    district: "Coimbatore",
    taluk: "Coimbatore North",
    village: "Saravanampatti",
    currentLandUse: "RESIDENTIAL",
    declaredLandUse: "RESIDENTIAL",
    gisDetectedLandUse: "COMMERCIAL",
    status: "NEEDS_REVIEW",
    riskScore: 55,
    verificationPercent: 74,
    latitude: 11.0780,
    longitude: 76.9980,
    boundary: [
      { lat: 11.0795, lng: 76.9960 },
      { lat: 11.0805, lng: 77.0000 },
      { lat: 11.0768, lng: 77.0010 },
      { lat: 11.0758, lng: 76.9970 },
      { lat: 11.0795, lng: 76.9960 }
    ],
    taxStatus: "Pending ₹22,400",
    lastTaxPaymentDate: "19 Aug 2025",
    outstandingTaxAmount: 22400.0,
    courtCaseStatus: "No Litigation",
    encumbranceStatus: "Nil Encumbrance",
    legalStatus: "Clear Title",
    surveyDate: "05 Mar 2023",
    boundaryStatus: "Area Discrepancy 0.12 ha",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-25 11:00", recordNumber: "PATTA-77-3A", details: "Patta in name of S. Soundararajan", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-25 11:00", recordNumber: "DEED-3310/2020", details: "Valid Registered Deed", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "CONFLICT", lastUpdated: "2026-08-20 15:30", recordNumber: "FMB-77-3A", details: "Variance 0.12 ha between Revenue and GIS satellite", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "CONFLICT", lastUpdated: "2026-08-19 14:00", recordNumber: "TAX-RES-773", details: "Assessed as Residential but Commercial activity detected", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "CONFLICT", lastUpdated: "2026-08-22 10:00", recordNumber: "DTCP-ZON-RES", details: "Operating commercial warehouse in residential zone", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "VERIFIED", lastUpdated: "2026-08-26 09:00", recordNumber: "NJDG-NIL", details: "Zero cases", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [
      {
        id: "ISS-0921-01",
        parcelId: "TN-COI-00512-0921",
        issueType: "AREA_DISCREPANCY",
        severity: "MEDIUM",
        department: "SURVEY",
        title: "Cadastral Boundary Discrepancy (0.12 ha)",
        description: "Survey record states 1.95 ha while GIS polygon calculation yields 1.83 ha.",
        detectedDate: "2026-08-20",
        status: "OPEN",
        evidence: { "Survey Area": "1.95 ha", "GIS Area": "1.83 ha", "Variance": "0.12 ha (6.1%)" },
        recommendedAction: "Conduct boundary field audit with Taluk Surveyor."
      },
      {
        id: "ISS-0921-02",
        parcelId: "TN-COI-00512-0921",
        issueType: "LAND_USE_CONFLICT",
        severity: "MEDIUM",
        department: "PLANNING",
        title: "Commercial Use in Residential Zone",
        description: "Property declared residential but GIS spectral imaging identifies commercial logistics warehouse.",
        detectedDate: "2026-08-22",
        status: "OPEN",
        evidence: { "Zoned As": "Residential Medium Density", "Observed Use": "Logistics Warehousing" },
        recommendedAction: "Levy commercial change-of-land-use regularization penalty and re-assess municipal tax."
      }
    ],
    riskFactors: [
      { title: "Area Discrepancy", severity: "MEDIUM", points: 40, reason: "0.12 ha variance on cadastral border" },
      { title: "Land Use Violation", severity: "MEDIUM", points: 40, reason: "Commercial use without DTCP conversion" }
    ]
  },

  // 6. Sulur Industrial Corridor: Missing Court Record
  {
    id: "TN-COI-00720-0314",
    surveyNumber: "104/3B",
    subDivision: "3B",
    ownerName: "K. Thangavel & Brothers",
    previousOwner: "M. Palanisamy",
    registrationDate: "08 Jan 2019",
    deedNumber: "DOC-2019/SRO-SUL/8812",
    areaHectares: 4.50,
    gisCalculatedArea: 4.48,
    district: "Coimbatore",
    taluk: "Sulur",
    village: "Neelambur",
    currentLandUse: "INDUSTRIAL",
    declaredLandUse: "INDUSTRIAL",
    gisDetectedLandUse: "INDUSTRIAL",
    status: "NEEDS_REVIEW",
    riskScore: 45,
    verificationPercent: 78,
    latitude: 11.0450,
    longitude: 77.0850,
    boundary: [
      { lat: 11.0475, lng: 77.0820 },
      { lat: 11.0485, lng: 77.0890 },
      { lat: 11.0430, lng: 77.0900 },
      { lat: 11.0415, lng: 77.0835 },
      { lat: 11.0475, lng: 77.0820 }
    ],
    taxStatus: "Up to Date (₹64,000 paid)",
    lastTaxPaymentDate: "15 Jan 2026",
    outstandingTaxAmount: 0.0,
    courtCaseStatus: "Missing Judicial Integration Sync",
    encumbranceStatus: "Clear Certificate",
    legalStatus: "Legal API Offline / Unreachable",
    surveyDate: "12 Nov 2023",
    boundaryStatus: "Verified Cadastral Survey",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-26 11:00", recordNumber: "PATTA-104-3B", details: "Industrial Patta", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-26 11:00", recordNumber: "DEED-8812/2019", details: "Valid Deed", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "VERIFIED", lastUpdated: "2026-08-26 11:00", recordNumber: "FMB-104-3B", details: "Matching FMB", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-26 11:00", recordNumber: "TAX-IND-104", details: "All taxes paid", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-26 11:00", recordNumber: "DTCP-IND-88", details: "Approved Industrial Estate", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "MISSING", lastUpdated: "2026-08-18 10:00", recordNumber: "NJDG-TIMEOUT", details: "e-Courts NJDG gateway returned 404/Missing Record", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [
      {
        id: "ISS-0314-01",
        parcelId: "TN-COI-00720-0314",
        issueType: "MISSING_RECORD",
        severity: "HIGH",
        department: "LEGAL",
        title: "Missing Court Legal Status Confirmation",
        description: "Judicial database returned missing record for Neelambur S.No 104/3B.",
        detectedDate: "2026-08-18",
        status: "OPEN",
        evidence: { "Court Gateway": "NJDG e-Courts Tamil Nadu", "Error": "404 Record Not Found for Survey Key" },
        recommendedAction: "Request manual clearance certificate from District Judicial Liaison Officer."
      }
    ],
    riskFactors: [
      { title: "Missing Legal Status", severity: "HIGH", points: 70, reason: "e-Courts NJDG judicial verification unavailable" }
    ]
  },

  // 7. Pollachi Agricultural & Coconut Plantations
  {
    id: "TN-COI-00810-0112",
    surveyNumber: "112/1",
    subDivision: "1",
    ownerName: "A. Subramanian",
    previousOwner: "Prior Family Partition",
    registrationDate: "10 May 2018",
    deedNumber: "DOC-2018/SRO-POL/0112",
    areaHectares: 3.20,
    gisCalculatedArea: 3.21,
    district: "Coimbatore",
    taluk: "Pollachi",
    village: "Kinathukadavu",
    currentLandUse: "AGRICULTURAL",
    declaredLandUse: "AGRICULTURAL",
    gisDetectedLandUse: "AGRICULTURAL",
    status: "VERIFIED",
    riskScore: 5,
    verificationPercent: 96,
    latitude: 10.7850,
    longitude: 77.0120,
    boundary: [
      { lat: 10.7868, lng: 77.0098 },
      { lat: 10.7872, lng: 77.0142 },
      { lat: 10.7834, lng: 77.0146 },
      { lat: 10.7832, lng: 77.0102 },
      { lat: 10.7868, lng: 77.0098 }
    ],
    taxStatus: "Paid Up to Date",
    lastTaxPaymentDate: "10 Jan 2026",
    outstandingTaxAmount: 0,
    courtCaseStatus: "No Litigation Found",
    encumbranceStatus: "Nil Encumbrance",
    legalStatus: "Clear Title",
    surveyDate: "15 Oct 2023",
    boundaryStatus: "DGPS Survey Verified",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "PATTA-112/1", details: "Revenue Patta Record", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DEED-0112", details: "SRO Registration", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "FMB-112/1", details: "Cadastral Survey FMB", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "TAX-112/1", details: "Municipal Tax Assessment", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DTCP-ZON-112/1", details: "DTCP Master Plan 2030", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "NJDG-REC", details: "e-Courts NJDG Legal Status", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [],
    riskFactors: []
  },

  // 8. Gandhipuram Commercial Center
  {
    id: "TN-COI-00201-0551",
    surveyNumber: "33/1A",
    subDivision: "1A",
    ownerName: "Central Plaza Properties",
    previousOwner: "C. Ramasamy",
    registrationDate: "15 Apr 2019",
    deedNumber: "DOC-2019/SRO-CBE-N/0551",
    areaHectares: 0.45,
    gisCalculatedArea: 0.44,
    district: "Coimbatore",
    taluk: "Coimbatore North",
    village: "Gandhipuram",
    currentLandUse: "COMMERCIAL",
    declaredLandUse: "COMMERCIAL",
    gisDetectedLandUse: "COMMERCIAL",
    status: "VERIFIED",
    riskScore: 4,
    verificationPercent: 98,
    latitude: 11.0180,
    longitude: 76.9680,
    boundary: [
      { lat: 11.0198, lng: 76.9658 },
      { lat: 11.0202, lng: 76.9702 },
      { lat: 11.0164, lng: 76.9706 },
      { lat: 11.0162, lng: 76.9662 },
      { lat: 11.0198, lng: 76.9658 }
    ],
    taxStatus: "Paid Up to Date (₹125,000 paid)",
    lastTaxPaymentDate: "02 Feb 2026",
    outstandingTaxAmount: 0,
    courtCaseStatus: "No Litigation Found",
    encumbranceStatus: "Nil Encumbrance",
    legalStatus: "Clear Title",
    surveyDate: "15 Oct 2023",
    boundaryStatus: "DGPS Survey Verified",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "PATTA-33/1A", details: "Revenue Patta Record", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DEED-0551", details: "SRO Registration", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "FMB-33/1A", details: "Cadastral Survey FMB", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "TAX-33/1A", details: "Municipal Tax Assessment", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DTCP-ZON-33/1A", details: "DTCP Commercial Zone", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "NJDG-REC", details: "e-Courts NJDG Legal Status", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [],
    riskFactors: []
  },

  // 9. Ganapathy Residential Dispute: Critical Issue
  {
    id: "TN-COI-00204-0554",
    surveyNumber: "91/4B",
    subDivision: "4B",
    ownerName: "K. Jeyaraman",
    previousOwner: "P. Murugesan",
    registrationDate: "12 Dec 2021",
    deedNumber: "DOC-2021/SRO-CBE-N/0554",
    areaHectares: 0.95,
    gisCalculatedArea: 0.78,
    district: "Coimbatore",
    taluk: "Coimbatore North",
    village: "Ganapathy",
    currentLandUse: "RESIDENTIAL",
    declaredLandUse: "RESIDENTIAL",
    gisDetectedLandUse: "COMMERCIAL",
    status: "CRITICAL_ISSUE",
    riskScore: 82,
    verificationPercent: 18,
    latitude: 11.0410,
    longitude: 76.9890,
    boundary: [
      { lat: 11.0428, lng: 76.9868 },
      { lat: 11.0432, lng: 76.9912 },
      { lat: 11.0394, lng: 76.9916 },
      { lat: 11.0392, lng: 76.9872 },
      { lat: 11.0428, lng: 76.9868 }
    ],
    taxStatus: "Overdue ₹18,500",
    lastTaxPaymentDate: "10 Jan 2026",
    outstandingTaxAmount: 18500.0,
    courtCaseStatus: "Dispute Registered at Sub-Court (OS 112/2024)",
    encumbranceStatus: "Disputed Encumbrance",
    legalStatus: "Sub-Judice",
    surveyDate: "15 Oct 2023",
    boundaryStatus: "Variance Pending Resolution (0.17 ha)",
    departmentSources: [
      { department: "REVENUE", status: "CONFLICT", lastUpdated: "2026-08-26 12:00", recordNumber: "PATTA-91/4B", details: "Revenue Patta Record", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DEED-0554", details: "SRO Registration", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "CONFLICT", lastUpdated: "2026-08-26 12:00", recordNumber: "FMB-91/4B", details: "Cadastral Survey FMB", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "TAX-91/4B", details: "Municipal Tax Assessment", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DTCP-ZON-91/4B", details: "DTCP Master Plan 2030", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "CONFLICT", lastUpdated: "2026-08-26 12:00", recordNumber: "NJDG-REC", details: "e-Courts NJDG Legal Status", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [
      {
        id: "ISS-0554-01",
        parcelId: "TN-COI-00204-0554",
        issueType: "BOUNDARY_MISMATCH",
        severity: "CRITICAL",
        department: "SURVEY",
        title: "Area Difference of 0.17 ha Detected",
        description: "Revenue ledger record is 0.95 ha, but satellite GIS measurement gives 0.78 ha.",
        detectedDate: "2026-08-22",
        status: "OPEN",
        evidence: { "Revenue Area": "0.95 ha", "GIS Area": "0.78 ha" },
        recommendedAction: "Verify DGPS survey boundary and reconcile with Taluk land records."
      }
    ],
    riskFactors: [
      { title: "Discrepancy Detected", severity: "CRITICAL", points: 82, reason: "Mismatch between survey ledger and GIS boundary" }
    ]
  },

  // 10. Bhavani River Protected Forest Parcel
  {
    id: "TN-COI-00505-0885",
    surveyNumber: "99/1",
    subDivision: "1",
    ownerName: "Forest Department Protected Reserve",
    previousOwner: "Government of Tamil Nadu",
    registrationDate: "01 Jan 1980",
    deedNumber: "DOC-1980/SRO-MET/0885",
    areaHectares: 6.20,
    gisCalculatedArea: 6.18,
    district: "Coimbatore",
    taluk: "Mettupalayam",
    village: "Bhavani Basin",
    currentLandUse: "FOREST_RESERVE",
    declaredLandUse: "FOREST_RESERVE",
    gisDetectedLandUse: "FOREST_RESERVE",
    status: "VERIFIED",
    riskScore: 1,
    verificationPercent: 99,
    latitude: 11.2850,
    longitude: 76.9150,
    boundary: [
      { lat: 11.2868, lng: 76.9128 },
      { lat: 11.2872, lng: 76.9172 },
      { lat: 11.2834, lng: 76.9176 },
      { lat: 11.2832, lng: 76.9132 },
      { lat: 11.2868, lng: 76.9128 }
    ],
    taxStatus: "Exempt (State Forest Land)",
    lastTaxPaymentDate: "N/A",
    outstandingTaxAmount: 0,
    courtCaseStatus: "No Litigation Found",
    encumbranceStatus: "Nil Encumbrance",
    legalStatus: "Protected State Forest",
    surveyDate: "15 Oct 2023",
    boundaryStatus: "DGPS Survey Verified",
    departmentSources: [
      { department: "REVENUE", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "PATTA-99/1", details: "State Forest Registry", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
      { department: "REGISTRATION", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DEED-0885", details: "Gazette Notification 1980", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
      { department: "SURVEY", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "FMB-99/1", details: "Cadastral Survey FMB", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
      { department: "TAX", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "TAX-EXEMPT", details: "Forest Exemption", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
      { department: "PLANNING", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "DTCP-FOREST", details: "Protected Ecocridor", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
      { department: "LEGAL", status: "VERIFIED", lastUpdated: "2026-08-26 12:00", recordNumber: "NJDG-NIL", details: "Zero Litigation", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
    ],
    issues: [],
    riskFactors: []
  }
];

export const initialDataSources: DepartmentSourceRecord[] = [
  { department: "REVENUE", status: "SYNCED", lastUpdated: "2026-08-27 14:30", recordNumber: "PATTA-GATEWAY-V3", details: "Connected: Tamil Nadu e-Sevai & e-District Patta Portal", healthPercent: 99, latencyMs: 85, totalRecords: 12480 },
  { department: "REGISTRATION", status: "SYNCED", lastUpdated: "2026-08-27 14:30", recordNumber: "STAR2-IGRS-API", details: "Connected: Inspector General of Registration STAR 2.0", healthPercent: 97, latencyMs: 140, totalRecords: 12480 },
  { department: "SURVEY", status: "CONFLICT", lastUpdated: "2026-08-27 14:28", recordNumber: "COLLAB-LAND-DGPS", details: "Connected: Tamil Nilam & CollabLand DGPS Vector Database", healthPercent: 92, latencyMs: 210, totalRecords: 12480 },
  { department: "TAX", status: "SYNCED", lastUpdated: "2026-08-27 14:25", recordNumber: "MUNICIPAL-REV-CBE", details: "Connected: Coimbatore Municipal Corporation Urban Tax Server", healthPercent: 98, latencyMs: 95, totalRecords: 12480 },
  { department: "PLANNING", status: "SYNCED", lastUpdated: "2026-08-27 14:20", recordNumber: "DTCP-GIS-ZONING", details: "Connected: Directorate of Town and Country Planning (DTCP)", healthPercent: 96, latencyMs: 175, totalRecords: 12480 },
  { department: "LEGAL", status: "DEMO_MOCK", lastUpdated: "2026-08-27 14:00", recordNumber: "NJDG-ECOURTS-GATEWAY", details: "Connected: National Judicial Data Grid (e-Courts Tamil Nadu)", healthPercent: 88, latencyMs: 320, totalRecords: 12480 }
];

export const initialAuditLogs: AuditLogEntry[] = [
  { id: "LOG-1099", timestamp: "2026-08-27 14:15", action: "System Rule Engine Audit", userRole: "SYSTEM", parcelId: "TN-COI-00123-0456", details: "Detected 0.15 ha boundary mismatch & dual claimant on S.No 45/2A" },
  { id: "LOG-1098", timestamp: "2026-08-27 13:40", action: "Wetland Encroachment Alert", userRole: "AI_RULE_ENGINE", parcelId: "TN-COI-00892-1102", details: "Flagged 0.45 ha intrusion into Singanallur Lake Ramsar buffer zone" },
  { id: "LOG-1097", timestamp: "2026-08-27 11:20", action: "Data Synchronization", userRole: "SYSTEM_DAEMON", parcelId: null, details: "Synchronized 12,480 cadastral ledger records across 6 departments" },
  { id: "LOG-1096", timestamp: "2026-08-27 09:10", action: "DGPS Resurvey Approval", userRole: "REVIEWER", parcelId: "TN-COI-00344-0789", details: "Cleared Survey No. 12/4C with 99% integrity score" }
];
