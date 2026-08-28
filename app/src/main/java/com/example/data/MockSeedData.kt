package com.example.data

import com.example.data.model.*

object MockSeedData {

    fun generateParcels(): List<Parcel> {
        val parcels = mutableListOf<Parcel>()

        // 1. Featured SIH Presentation Demo Parcel: TN-COI-00123-0456
        parcels.add(
            Parcel(
                id = "TN-COI-00123-0456",
                surveyNumber = "45/2A",
                subDivision = "2A",
                ownerName = "Ravi Kumar",
                previousOwner = "Kumar Raj (Transfer Claim Dispute)",
                registrationDate = "14 Mar 2021",
                deedNumber = "DOC-2021/SRO-CBE-S/4491",
                areaHectares = 1.25,
                gisCalculatedArea = 1.10,
                district = "Coimbatore",
                taluk = "Coimbatore South",
                village = "Singanallur",
                currentLandUse = LandUseType.AGRICULTURAL,
                declaredLandUse = LandUseType.AGRICULTURAL,
                gisDetectedLandUse = LandUseType.MIXED_USE,
                status = ParcelStatus.NEEDS_REVIEW,
                riskScore = 65,
                verificationPercent = 68,
                latitude = 10.9984,
                longitude = 77.0125,
                boundary = listOf(
                    CadastralPoint(10.9995, 77.0110),
                    CadastralPoint(11.0002, 77.0138),
                    CadastralPoint(10.9978, 77.0145),
                    CadastralPoint(10.9969, 77.0118),
                    CadastralPoint(10.9995, 77.0110)
                ),
                taxStatus = "Pending ₹14,200",
                lastTaxPaymentDate = "10 Dec 2024",
                outstandingTaxAmount = 14200.0,
                courtCaseStatus = "Civil Suit Pending: OS 241/2023",
                encumbranceStatus = "Mortgage & Partition Dispute noted",
                legalStatus = "Sub-Judice (Status Quo Order)",
                surveyDate = "18 Nov 2022",
                boundaryStatus = "Variance Detected (>0.15 ha)",
                departmentSources = listOf(
                    DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.VERIFIED, "2026-08-26 09:30", "PATTA-2021-9982", "Owner: Ravi Kumar, Area: 1.25 ha"),
                    DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.CONFLICT, "2026-08-25 14:15", "DEED-4491/2021", "Mismatch: Secondary claimant Kumar Raj on deed"),
                    DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.CONFLICT, "2026-08-20 11:00", "FMB-45-2A-2022", "Survey FMB area (1.25 ha) vs GIS polygon (1.10 ha)"),
                    DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.VERIFIED, "2026-08-15 16:45", "TAX-CBE-S-4401", "Assessment: Agricultural S-2 (Arrears ₹14,200)"),
                    DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.VERIFIED, "2026-08-10 10:20", "DTCP-ZON-9902", "Designated Green Agro Belt"),
                    DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.CONFLICT, "2026-08-24 15:30", "NJDG-OS-241-2023", "Active Injunction Case pending at District Court")
                ),
                issues = listOf(
                    ParcelIssue(
                        id = "ISS-0456-01",
                        parcelId = "TN-COI-00123-0456",
                        issueType = IssueType.BOUNDARY_MISMATCH,
                        severity = IssueSeverity.HIGH,
                        department = DepartmentType.SURVEY,
                        title = "Cadastral Survey vs GIS Boundary Mismatch (0.15 ha)",
                        description = "Survey records indicate 1.25 ha but satellite DGPS GIS polygon measures 1.10 ha.",
                        detectedDate = "2026-08-20",
                        status = IssueStatus.OPEN,
                        evidence = mapOf(
                            "Survey FMB Area" to "1.25 ha",
                            "GIS Computed Area" to "1.10 ha",
                            "Area Variance" to "0.15 ha (-12.0%)",
                            "Survey Reference" to "FMB Map Sheet 45-Singanallur"
                        ),
                        recommendedAction = "Verify latest survey boundary and execute joint field DGPS resurvey with Revenue Inspector."
                    ),
                    ParcelIssue(
                        id = "ISS-0456-02",
                        parcelId = "TN-COI-00123-0456",
                        issueType = IssueType.OWNERSHIP_CONFLICT,
                        severity = IssueSeverity.HIGH,
                        department = DepartmentType.REGISTRATION,
                        title = "Ownership Conflict between Revenue Patta & SRO Deed",
                        description = "Revenue department registers Ravi Kumar, while Sub-Registrar records list Kumar Raj.",
                        detectedDate = "2026-08-22",
                        status = IssueStatus.OPEN,
                        evidence = mapOf(
                            "Revenue Patta" to "Ravi Kumar (100% Share)",
                            "Registration Record" to "Kumar Raj (Disputed Deed 4491/2021)",
                            "SRO Office" to "Coimbatore South Sub-Registrar"
                        ),
                        recommendedAction = "Cross-check latest registered deed and conduct enquiry before issuing mutation clearance."
                    ),
                    ParcelIssue(
                        id = "ISS-0456-03",
                        parcelId = "TN-COI-00123-0456",
                        issueType = IssueType.ENCUMBRANCE_ALERT,
                        severity = IssueSeverity.HIGH,
                        department = DepartmentType.LEGAL,
                        title = "Court Injunction: OS 241/2023 Pending",
                        description = "Principal District Court Coimbatore has issued an interim status quo order.",
                        detectedDate = "2026-08-24",
                        status = IssueStatus.OPEN,
                        evidence = mapOf(
                            "Case Number" to "OS 241/2023",
                            "Court" to "Principal District Munsif, Coimbatore",
                            "Interim Order" to "Status Quo on Alienation & Construction"
                        ),
                        recommendedAction = "Hold mutation processing pending certified court outcome copy."
                    )
                ),
                riskFactors = listOf(
                    RiskFactor("Boundary Discrepancy", IssueSeverity.HIGH, 70, "Survey vs GIS area differs by 0.15 ha"),
                    RiskFactor("Dual Ownership Claim", IssueSeverity.HIGH, 70, "Revenue Patta and SRO Sale Deed mismatch"),
                    RiskFactor("Active Litigation", IssueSeverity.HIGH, 70, "Court Injunction OS 241/2023")
                )
            )
        )

        // 2. Critical Parcel: Singanallur Lake Wetland Encroachment
        parcels.add(
            Parcel(
                id = "TN-COI-00892-1102",
                surveyNumber = "89/1B",
                subDivision = "1B",
                ownerName = "Apex Logistics & Infrastructure Ltd.",
                previousOwner = "K. Velusamy (2018)",
                registrationDate = "05 Feb 2023",
                deedNumber = "DOC-2023/SRO-CBE-S/1288",
                areaHectares = 3.80,
                gisCalculatedArea = 4.25,
                district = "Coimbatore",
                taluk = "Coimbatore South",
                village = "Singanallur",
                currentLandUse = LandUseType.COMMERCIAL,
                declaredLandUse = LandUseType.COMMERCIAL,
                gisDetectedLandUse = LandUseType.WATER_BODY_BUFFER,
                status = ParcelStatus.CRITICAL_ISSUE,
                riskScore = 95,
                verificationPercent = 25,
                latitude = 10.9920,
                longitude = 77.0220,
                boundary = listOf(
                    CadastralPoint(10.9935, 77.0200),
                    CadastralPoint(10.9942, 77.0245),
                    CadastralPoint(10.9908, 77.0252),
                    CadastralPoint(10.9898, 77.0210),
                    CadastralPoint(10.9935, 77.0200)
                ),
                taxStatus = "Arrears ₹85,000",
                lastTaxPaymentDate = "14 Mar 2023",
                outstandingTaxAmount = 85000.0,
                courtCaseStatus = "WP 8901/2024 (Madras High Court Green Bench)",
                encumbranceStatus = "Restricted Waterway Buffer Zone",
                legalStatus = "Show-Cause Notice Issued under TN Land Encroachment Act",
                surveyDate = "10 Jan 2024",
                boundaryStatus = "Encroachment on 50m Lake Eco-Sensitive Buffer",
                departmentSources = listOf(
                    DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.CONFLICT, "2026-08-25 10:00", "PATTA-89-1B", "Recorded as Commercial but encroaches wetland"),
                    DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.VERIFIED, "2026-08-24 16:20", "DEED-1288/2023", "Registered sale deed valid"),
                    DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.CONFLICT, "2026-08-22 14:00", "FMB-89-1B", "Boundary overlaps 0.45 ha into Public Waterbody Tank"),
                    DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.CONFLICT, "2026-08-18 11:30", "TAX-COMM-891", "Commercial tax unpaid ₹85,000"),
                    DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.CONFLICT, "2026-08-21 09:15", "DTCP-ZON-CRIT", "Violates 50m Singanallur Lake Eco Buffer"),
                    DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.CONFLICT, "2026-08-26 12:00", "HC-WP-8901-2024", "Madras High Court stay on commercial expansion")
                ),
                issues = listOf(
                    ParcelIssue(
                        id = "ISS-1102-01",
                        parcelId = "TN-COI-00892-1102",
                        issueType = IssueType.LAND_USE_CONFLICT,
                        severity = IssueSeverity.CRITICAL,
                        department = DepartmentType.PLANNING,
                        title = "Singanallur Wetland Buffer Encroachment (0.45 ha)",
                        description = "Satellite GIS confirms construction within prohibited 50m eco-sensitive lake boundary.",
                        detectedDate = "2026-08-21",
                        status = IssueStatus.OPEN,
                        evidence = mapOf(
                            "Eco Buffer Zone" to "Singanallur Lake Ramsar Candidate Wetland",
                            "Overlapping Area" to "0.45 ha (11.8% of site)",
                            "DTCP Clearance" to "REJECTED (Eco-sensitive Zone 4)"
                        ),
                        recommendedAction = "Issue demolition and recovery order under Tamil Nadu Protection of Tanks and Eviction of Encroachment Act."
                    ),
                    ParcelIssue(
                        id = "ISS-1102-02",
                        parcelId = "TN-COI-00892-1102",
                        issueType = IssueType.BOUNDARY_MISMATCH,
                        severity = IssueSeverity.CRITICAL,
                        department = DepartmentType.SURVEY,
                        title = "Cadastral Boundary Expansion into Waterbody Poramboke",
                        description = "Survey boundary expanded by 0.45 ha into State Water Resource Department channel.",
                        detectedDate = "2026-08-22",
                        status = IssueStatus.OPEN,
                        evidence = mapOf(
                            "WRD Water Channel ID" to "NOYYAL-FEEDER-CH-12",
                            "Calculated Intrusion" to "4,500 sq. meters"
                        ),
                        recommendedAction = "Summon WRD Executive Engineer and Revenue DRO for immediate spot sealing."
                    )
                ),
                riskFactors = listOf(
                    RiskFactor("Wetland Encroachment", IssueSeverity.CRITICAL, 100, "Commercial construction inside waterbody buffer"),
                    RiskFactor("Boundary Expansion", IssueSeverity.CRITICAL, 100, "Intrusion into state water channel"),
                    RiskFactor("High Court Stay", IssueSeverity.HIGH, 70, "Active High Court PIL against construction")
                )
            )
        )

        // 3. Fully Verified Parcel: Singanallur Agro Plot
        parcels.add(
            Parcel(
                id = "TN-COI-00344-0789",
                surveyNumber = "12/4C",
                subDivision = "4C",
                ownerName = "M. Shanmugasundaram",
                previousOwner = "K. Marimuthu (Inheritance 2012)",
                registrationDate = "19 Jun 2012",
                deedNumber = "DOC-2012/SRO-CBE-S/1104",
                areaHectares = 2.40,
                gisCalculatedArea = 2.39,
                district = "Coimbatore",
                taluk = "Coimbatore South",
                village = "Singanallur",
                currentLandUse = LandUseType.AGRICULTURAL,
                declaredLandUse = LandUseType.AGRICULTURAL,
                gisDetectedLandUse = LandUseType.AGRICULTURAL,
                status = ParcelStatus.VERIFIED,
                riskScore = 5,
                verificationPercent = 99,
                latitude = 11.0040,
                longitude = 77.0080,
                boundary = listOf(
                    CadastralPoint(11.0055, 77.0065),
                    CadastralPoint(11.0062, 77.0100),
                    CadastralPoint(11.0028, 77.0105),
                    CadastralPoint(11.0020, 77.0070),
                    CadastralPoint(11.0055, 77.0065)
                ),
                taxStatus = "Up to Date (₹2,400 paid)",
                lastTaxPaymentDate = "10 Jan 2026",
                outstandingTaxAmount = 0.0,
                courtCaseStatus = "No Litigation Found",
                encumbranceStatus = "Nil Encumbrance (1990-2026)",
                legalStatus = "Clean Title (Verified by Collectorate)",
                surveyDate = "14 Feb 2024",
                boundaryStatus = "Cadastral DGPS 100% Match",
                departmentSources = listOf(
                    DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.VERIFIED, "2026-08-27 08:00", "PATTA-12-4C", "Clean Title, Patta 12/4C"),
                    DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.VERIFIED, "2026-08-27 08:00", "DEED-1104/2012", "Ancestral Partition Deed in Order"),
                    DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.VERIFIED, "2026-08-27 08:00", "FMB-12-4C", "0.01 ha variance (Well within 0.05 tolerance)"),
                    DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.VERIFIED, "2026-08-27 08:00", "TAX-AGR-1209", "Zero tax arrears"),
                    DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.VERIFIED, "2026-08-27 08:00", "DTCP-ZON-AGRO", "Standard Agricultural Zone"),
                    DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.VERIFIED, "2026-08-27 08:00", "NJDG-NIL", "Zero active or historical cases")
                ),
                issues = emptyList(),
                riskFactors = emptyList()
            )
        )

        // 4. Peelamedu IT Corridor Parcel: Under Verification
        parcels.add(
            Parcel(
                id = "TN-COI-00411-0988",
                surveyNumber = "58/1A",
                subDivision = "1A",
                ownerName = "Titan Tech Innovations LLP",
                previousOwner = "G. Balasubramaniam",
                registrationDate = "22 Nov 2024",
                deedNumber = "DOC-2024/SRO-CBE-N/7740",
                areaHectares = 0.85,
                gisCalculatedArea = 0.84,
                district = "Coimbatore",
                taluk = "Coimbatore North",
                village = "Peelamedu",
                currentLandUse = LandUseType.COMMERCIAL,
                declaredLandUse = LandUseType.COMMERCIAL,
                gisDetectedLandUse = LandUseType.COMMERCIAL,
                status = ParcelStatus.UNDER_VERIFICATION,
                riskScore = 20,
                verificationPercent = 88,
                latitude = 11.0250,
                longitude = 77.0150,
                boundary = listOf(
                    CadastralPoint(11.0265, 77.0135),
                    CadastralPoint(11.0270, 77.0170),
                    CadastralPoint(11.0238, 77.0175),
                    CadastralPoint(11.0230, 77.0140),
                    CadastralPoint(11.0265, 77.0135)
                ),
                taxStatus = "Up to Date (₹48,200 paid)",
                lastTaxPaymentDate = "05 Feb 2026",
                outstandingTaxAmount = 0.0,
                courtCaseStatus = "No Litigation",
                encumbranceStatus = "HDFC Bank Project Loan Lien",
                legalStatus = "Clear Title with Registered Mortgage",
                surveyDate = "15 Jan 2026",
                boundaryStatus = "Re-survey Under Review by Tahsildar",
                departmentSources = listOf(
                    DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.SYNCED, "2026-08-27 10:00", "PATTA-58-1A", "Commercial Patta under inspection"),
                    DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.VERIFIED, "2026-08-27 10:00", "DEED-7740/2024", "Registered deed verified"),
                    DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.SYNCED, "2026-08-27 10:00", "FMB-58-1A", "Joint site survey scheduled"),
                    DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.VERIFIED, "2026-08-27 10:00", "TAX-COM-9921", "Commercial property tax up to date"),
                    DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.VERIFIED, "2026-08-27 10:00", "DTCP-IT-ZONE", "Approved IT Park Zone"),
                    DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.VERIFIED, "2026-08-27 10:00", "NJDG-NIL", "Zero litigation")
                ),
                issues = listOf(
                    ParcelIssue(
                        id = "ISS-0988-01",
                        parcelId = "TN-COI-00411-0988",
                        issueType = IssueType.OUTDATED_RECORD,
                        severity = IssueSeverity.LOW,
                        department = DepartmentType.SURVEY,
                        title = "Pending Routine Mutation Endorsement",
                        description = "Commercial conversion approval granted by DTCP; awaiting final Tahsildar digital signature on updated FMB.",
                        detectedDate = "2026-08-25",
                        status = IssueStatus.UNDER_REVIEW,
                        evidence = mapOf(
                            "Application Reference" to "MUT-2026-CBE-N-991",
                            "DTCP Conversion Order" to "DTCP/CBE/2025/1109",
                            "Pending Office" to "Tahsildar Coimbatore North"
                        ),
                        recommendedAction = "Expedite electronic digital signature on Form 7A mutation register."
                    )
                ),
                riskFactors = listOf(
                    RiskFactor("Pending Routine Signature", IssueSeverity.LOW, 15, "Awaiting Tahsildar e-sign on updated FMB")
                )
            )
        )

        // 5. Saravanampatti IT & Tech Park: Needs Review (Area Discrepancy)
        parcels.add(
            Parcel(
                id = "TN-COI-00512-0921",
                surveyNumber = "77/3A",
                subDivision = "3A",
                ownerName = "S. Soundararajan & Sons",
                previousOwner = "T. Rangaswamy",
                registrationDate = "11 Oct 2020",
                deedNumber = "DOC-2020/SRO-CBE-N/3310",
                areaHectares = 1.95,
                gisCalculatedArea = 1.83,
                district = "Coimbatore",
                taluk = "Coimbatore North",
                village = "Saravanampatti",
                currentLandUse = LandUseType.RESIDENTIAL,
                declaredLandUse = LandUseType.RESIDENTIAL,
                gisDetectedLandUse = LandUseType.COMMERCIAL,
                status = ParcelStatus.NEEDS_REVIEW,
                riskScore = 55,
                verificationPercent = 74,
                latitude = 11.0780,
                longitude = 76.9980,
                boundary = listOf(
                    CadastralPoint(11.0795, 76.9960),
                    CadastralPoint(11.0805, 77.0000),
                    CadastralPoint(11.0768, 77.0010),
                    CadastralPoint(11.0758, 76.9970),
                    CadastralPoint(11.0795, 76.9960)
                ),
                taxStatus = "Pending ₹22,400",
                lastTaxPaymentDate = "19 Aug 2025",
                outstandingTaxAmount = 22400.0,
                courtCaseStatus = "No Litigation",
                encumbranceStatus = "Nil Encumbrance",
                legalStatus = "Clear Title",
                surveyDate = "05 Mar 2023",
                boundaryStatus = "Area Discrepancy 0.12 ha",
                departmentSources = listOf(
                    DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.VERIFIED, "2026-08-25 11:00", "PATTA-77-3A", "Patta in name of S. Soundararajan"),
                    DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.VERIFIED, "2026-08-25 11:00", "DEED-3310/2020", "Valid Registered Deed"),
                    DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.CONFLICT, "2026-08-20 15:30", "FMB-77-3A", "Variance 0.12 ha between Revenue and GIS satellite"),
                    DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.CONFLICT, "2026-08-19 14:00", "TAX-RES-773", "Assessed as Residential but Commercial activity detected"),
                    DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.CONFLICT, "2026-08-22 10:00", "DTCP-ZON-RES", "Operating commercial warehouse in residential zone"),
                    DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.VERIFIED, "2026-08-26 09:00", "NJDG-NIL", "Zero cases")
                ),
                issues = listOf(
                    ParcelIssue(
                        id = "ISS-0921-01",
                        parcelId = "TN-COI-00512-0921",
                        issueType = IssueType.AREA_DISCREPANCY,
                        severity = IssueSeverity.MEDIUM,
                        department = DepartmentType.SURVEY,
                        title = "Cadastral Boundary Discrepancy (0.12 ha)",
                        description = "Survey record states 1.95 ha while GIS polygon calculation yields 1.83 ha.",
                        detectedDate = "2026-08-20",
                        status = IssueStatus.OPEN,
                        evidence = mapOf("Survey Area" to "1.95 ha", "GIS Area" to "1.83 ha", "Variance" to "0.12 ha (6.1%)"),
                        recommendedAction = "Conduct boundary field audit with Taluk Surveyor."
                    ),
                    ParcelIssue(
                        id = "ISS-0921-02",
                        parcelId = "TN-COI-00512-0921",
                        issueType = IssueType.LAND_USE_CONFLICT,
                        severity = IssueSeverity.MEDIUM,
                        department = DepartmentType.PLANNING,
                        title = "Commercial Use in Residential Zone",
                        description = "Property declared residential but GIS spectral imaging identifies commercial logistics warehouse.",
                        detectedDate = "2026-08-22",
                        status = IssueStatus.OPEN,
                        evidence = mapOf("Zoned As" to "Residential Medium Density", "Observed Use" to "Logistics Warehousing"),
                        recommendedAction = "Levy commercial change-of-land-use regularization penalty and re-assess municipal tax."
                    )
                ),
                riskFactors = listOf(
                    RiskFactor("Area Discrepancy", IssueSeverity.MEDIUM, 40, "0.12 ha variance on cadastral border"),
                    RiskFactor("Land Use Violation", IssueSeverity.MEDIUM, 40, "Commercial use without DTCP conversion")
                )
            )
        )

        // 6. Sulur Industrial Corridor: Missing Court Record
        parcels.add(
            Parcel(
                id = "TN-COI-00720-0314",
                surveyNumber = "104/3B",
                subDivision = "3B",
                ownerName = "K. Thangavel & Brothers",
                previousOwner = "M. Palanisamy",
                registrationDate = "08 Jan 2019",
                deedNumber = "DOC-2019/SRO-SUL/8812",
                areaHectares = 4.50,
                gisCalculatedArea = 4.48,
                district = "Coimbatore",
                taluk = "Sulur",
                village = "Neelambur",
                currentLandUse = LandUseType.INDUSTRIAL,
                declaredLandUse = LandUseType.INDUSTRIAL,
                gisDetectedLandUse = LandUseType.INDUSTRIAL,
                status = ParcelStatus.NEEDS_REVIEW,
                riskScore = 45,
                verificationPercent = 78,
                latitude = 11.0450,
                longitude = 77.0850,
                boundary = listOf(
                    CadastralPoint(11.0475, 77.0820),
                    CadastralPoint(11.0485, 77.0890),
                    CadastralPoint(11.0430, 77.0900),
                    CadastralPoint(11.0415, 77.0835),
                    CadastralPoint(11.0475, 77.0820)
                ),
                taxStatus = "Up to Date (₹64,000 paid)",
                lastTaxPaymentDate = "15 Jan 2026",
                outstandingTaxAmount = 0.0,
                courtCaseStatus = "Missing Judicial Integration Sync",
                encumbranceStatus = "Clear Certificate",
                legalStatus = "Legal API Offline / Unreachable",
                surveyDate = "12 Nov 2023",
                boundaryStatus = "Verified Cadastral Survey",
                departmentSources = listOf(
                    DepartmentSourceRecord(DepartmentType.REVENUE, SourceStatus.VERIFIED, "2026-08-26 11:00", "PATTA-104-3B", "Industrial Patta"),
                    DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.VERIFIED, "2026-08-26 11:00", "DEED-8812/2019", "Valid Deed"),
                    DepartmentSourceRecord(DepartmentType.SURVEY, SourceStatus.VERIFIED, "2026-08-26 11:00", "FMB-104-3B", "Matching FMB"),
                    DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.VERIFIED, "2026-08-26 11:00", "TAX-IND-104", "All taxes paid"),
                    DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.VERIFIED, "2026-08-26 11:00", "DTCP-IND-88", "Approved Industrial Estate"),
                    DepartmentSourceRecord(DepartmentType.LEGAL, SourceStatus.MISSING, "2026-08-18 10:00", "NJDG-TIMEOUT", "e-Courts NJDG gateway returned 404/Missing Record")
                ),
                issues = listOf(
                    ParcelIssue(
                        id = "ISS-0314-01",
                        parcelId = "TN-COI-00720-0314",
                        issueType = IssueType.MISSING_RECORD,
                        severity = IssueSeverity.HIGH,
                        department = DepartmentType.LEGAL,
                        title = "Missing Court Legal Status Confirmation",
                        description = "Judicial database returned missing record for Neelambur S.No 104/3B.",
                        detectedDate = "2026-08-18",
                        status = IssueStatus.OPEN,
                        evidence = mapOf("Court Gateway" to "NJDG e-Courts Tamil Nadu", "Error" to "404 Record Not Found for Survey Key"),
                        recommendedAction = "Request manual clearance certificate from District Judicial Liaison Officer."
                    )
                ),
                riskFactors = listOf(
                    RiskFactor("Missing Legal Status", IssueSeverity.HIGH, 70, "e-Courts NJDG judicial verification unavailable")
                )
            )
        )

        // Add 35+ more realistic, diverse parcels across the Coimbatore region!
        val additionalParcels = listOf(
            // Pollachi Agricultural & Coconut Plantations
            Triple("TN-COI-00810-0112", "112/1", 3.20 to 3.21) to Quad("Pollachi", "Kinathukadavu", "A. Subramanian", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 5, 10.7850, 77.0120),
            Triple("TN-COI-00811-0113", "112/2", 1.80 to 1.79) to Quad("Pollachi", "Kinathukadavu", "S. Krishnasamy", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 8, 10.7880, 77.0160),
            Triple("TN-COI-00812-0114", "114/3", 2.60 to 2.45) to Quad("Pollachi", "Pollachi South", "R. Natarajan", LandUseType.AGRICULTURAL, ParcelStatus.NEEDS_REVIEW, 42, 10.6650, 77.0080),
            Triple("TN-COI-00813-0115", "88/2C", 5.10 to 5.08) to Quad("Pollachi", "Pollachi North", "Green Agro Estates Ltd", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 2, 10.6850, 77.0320),
            
            // Coimbatore North (Gandhipuram, Ganapathy, Thudiyalur)
            Triple("TN-COI-00201-0551", "33/1A", 0.45 to 0.44) to Quad("Coimbatore North", "Gandhipuram", "Central Plaza Properties", LandUseType.COMMERCIAL, ParcelStatus.VERIFIED, 4, 11.0180, 76.9680),
            Triple("TN-COI-00202-0552", "33/1B", 0.32 to 0.28) to Quad("Coimbatore North", "Gandhipuram", "V. Balachandran", LandUseType.COMMERCIAL, ParcelStatus.NEEDS_REVIEW, 48, 11.0210, 76.9720),
            Triple("TN-COI-00203-0553", "91/4A", 1.15 to 1.14) to Quad("Coimbatore North", "Ganapathy", "P. Murugesan", LandUseType.RESIDENTIAL, ParcelStatus.VERIFIED, 6, 11.0380, 76.9850),
            Triple("TN-COI-00204-0554", "91/4B", 0.95 to 0.78) to Quad("Coimbatore North", "Ganapathy", "K. Jeyaraman", LandUseType.RESIDENTIAL, ParcelStatus.CRITICAL_ISSUE, 82, 11.0410, 76.9890),
            Triple("TN-COI-00205-0555", "102/2", 2.20 to 2.19) to Quad("Coimbatore North", "Thudiyalur", "Kongu Developers", LandUseType.RESIDENTIAL, ParcelStatus.VERIFIED, 3, 11.0720, 76.9420),
            Triple("TN-COI-00206-0556", "102/3", 1.40 to 1.38) to Quad("Coimbatore North", "Thudiyalur", "N. Senthil Kumar", LandUseType.AGRICULTURAL, ParcelStatus.UNDER_VERIFICATION, 25, 11.0760, 76.9480),

            // Coimbatore South (Ramanathapuram, Perur, Sundarapuram, Kuniamuthur)
            Triple("TN-COI-00301-0661", "18/2A", 0.65 to 0.65) to Quad("Coimbatore South", "Ramanathapuram", "Dr. A. Meenakshi", LandUseType.RESIDENTIAL, ParcelStatus.VERIFIED, 2, 10.9920, 76.9850),
            Triple("TN-COI-00302-0662", "18/2B", 0.80 to 0.68) to Quad("Coimbatore South", "Ramanathapuram", "R. Vijayaraghavan", LandUseType.RESIDENTIAL, ParcelStatus.NEEDS_REVIEW, 52, 10.9940, 76.9910),
            Triple("TN-COI-00303-0663", "42/1", 3.10 to 3.09) to Quad("Coimbatore South", "Perur", "Perur Temple Trust", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 1, 10.9750, 76.9150),
            Triple("TN-COI-00304-0664", "42/2", 1.50 to 1.22) to Quad("Coimbatore South", "Perur", "D. Selvaraj", LandUseType.AGRICULTURAL, ParcelStatus.CRITICAL_ISSUE, 88, 10.9780, 76.9210),
            Triple("TN-COI-00305-0665", "64/1A", 1.75 to 1.74) to Quad("Coimbatore South", "Sundarapuram", "Precision Engineering Works", LandUseType.INDUSTRIAL, ParcelStatus.VERIFIED, 5, 10.9520, 76.9740),
            Triple("TN-COI-00306-0666", "64/1B", 2.10 to 2.05) to Quad("Coimbatore South", "Sundarapuram", "Tex Fab Mills", LandUseType.INDUSTRIAL, ParcelStatus.NEEDS_REVIEW, 38, 10.9560, 76.9780),
            Triple("TN-COI-00307-0667", "79/3", 0.90 to 0.89) to Quad("Coimbatore South", "Kuniamuthur", "K. Sivakumar", LandUseType.RESIDENTIAL, ParcelStatus.VERIFIED, 4, 10.9620, 76.9520),

            // Sulur (Sulur Town, Arasur, Kangayampalayam)
            Triple("TN-COI-00401-0771", "15/1", 4.20 to 4.18) to Quad("Sulur", "Sulur Town", "Aero Precision Systems", LandUseType.INDUSTRIAL, ParcelStatus.VERIFIED, 3, 11.0280, 77.1250),
            Triple("TN-COI-00402-0772", "15/2", 2.80 to 2.50) to Quad("Sulur", "Sulur Town", "Sulur Agro Mills", LandUseType.AGRICULTURAL, ParcelStatus.NEEDS_REVIEW, 46, 11.0320, 77.1310),
            Triple("TN-COI-00403-0773", "82/4", 3.50 to 3.49) to Quad("Sulur", "Arasur", "Sri Balaji Spinning Mills", LandUseType.INDUSTRIAL, ParcelStatus.VERIFIED, 2, 11.0580, 77.1120),
            Triple("TN-COI-00404-0774", "82/5", 1.90 to 1.55) to Quad("Sulur", "Arasur", "G. Loganathan", LandUseType.INDUSTRIAL, ParcelStatus.CRITICAL_ISSUE, 86, 11.0620, 77.1180),
            Triple("TN-COI-00405-0775", "49/2", 2.30 to 2.29) to Quad("Sulur", "Kangayampalayam", "M. Devaraj", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 4, 11.0150, 77.0980),

            // Mettupalayam (Karamadai, Sirumugai, Bhavani River Basin)
            Triple("TN-COI-00501-0881", "12/1", 5.40 to 5.38) to Quad("Mettupalayam", "Karamadai", "Karamadai Agro Farms", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 3, 11.2420, 76.9620),
            Triple("TN-COI-00502-0882", "12/2", 2.90 to 2.55) to Quad("Mettupalayam", "Karamadai", "T. Jayachandran", LandUseType.AGRICULTURAL, ParcelStatus.NEEDS_REVIEW, 54, 11.2480, 76.9680),
            Triple("TN-COI-00503-0883", "71/3", 4.10 to 4.09) to Quad("Mettupalayam", "Sirumugai", "South India Paper Mills", LandUseType.INDUSTRIAL, ParcelStatus.VERIFIED, 5, 11.3120, 77.0080),
            Triple("TN-COI-00504-0884", "71/4", 3.00 to 2.40) to Quad("Mettupalayam", "Sirumugai", "Cauvery Agro Allied Ltd", LandUseType.INDUSTRIAL, ParcelStatus.CRITICAL_ISSUE, 90, 11.3180, 77.0150),
            Triple("TN-COI-00505-0885", "99/1", 6.20 to 6.18) to Quad("Mettupalayam", "Bhavani Basin", "Forest Department Protected", LandUseType.FOREST_RESERVE, ParcelStatus.VERIFIED, 1, 11.2850, 76.9150),

            // Additional Coimbatore City Parcels
            Triple("TN-COI-00601-0991", "24/1", 0.55 to 0.55) to Quad("Coimbatore North", "RS Puram", "Lakshmi Heritage Trust", LandUseType.COMMERCIAL, ParcelStatus.VERIFIED, 2, 11.0080, 76.9450),
            Triple("TN-COI-00602-0992", "24/2", 0.40 to 0.32) to Quad("Coimbatore North", "RS Puram", "K. Rajagopalan", LandUseType.RESIDENTIAL, ParcelStatus.NEEDS_REVIEW, 44, 11.0110, 76.9480),
            Triple("TN-COI-00603-0993", "55/3", 1.20 to 1.19) to Quad("Coimbatore South", "Vadavalli", "V. Annamalai", LandUseType.RESIDENTIAL, ParcelStatus.VERIFIED, 3, 11.0250, 76.9050),
            Triple("TN-COI-00604-0994", "55/4", 0.95 to 0.70) to Quad("Coimbatore South", "Vadavalli", "Marudhamalai Real Estate", LandUseType.RESIDENTIAL, ParcelStatus.CRITICAL_ISSUE, 78, 11.0280, 76.9110),
            Triple("TN-COI-00605-0995", "88/1", 3.80 to 3.79) to Quad("Coimbatore South", "Thondamuthur", "Kongu Organic Farms", LandUseType.AGRICULTURAL, ParcelStatus.VERIFIED, 2, 10.9950, 76.8750)
        )

        additionalParcels.forEach { (meta, details) ->
            val (id, surveyNo, areas) = meta
            val (revArea, gisArea) = areas
            val (taluk, village, owner, landUse, status, risk, lat, lng) = details

            val dLat = 0.0018
            val dLng = 0.0022
            val polygon = listOf(
                CadastralPoint(lat + dLat, lng - dLng),
                CadastralPoint(lat + dLat * 1.2, lng + dLng * 1.1),
                CadastralPoint(lat - dLat * 0.9, lng + dLng * 1.2),
                CadastralPoint(lat - dLat, lng - dLng * 0.8),
                CadastralPoint(lat + dLat, lng - dLng)
            )

            val isConflict = status == ParcelStatus.CRITICAL_ISSUE || status == ParcelStatus.NEEDS_REVIEW
            val issuesList = if (isConflict) {
                listOf(
                    ParcelIssue(
                        id = "ISS-${id.takeLast(4)}-01",
                        parcelId = id,
                        issueType = if (status == ParcelStatus.CRITICAL_ISSUE) IssueType.BOUNDARY_MISMATCH else IssueType.AREA_DISCREPANCY,
                        severity = if (status == ParcelStatus.CRITICAL_ISSUE) IssueSeverity.CRITICAL else IssueSeverity.MEDIUM,
                        department = DepartmentType.SURVEY,
                        title = "Area Difference of ${String.format("%.2f", revArea - gisArea)} ha Detected",
                        description = "Revenue ledger record is $revArea ha, but satellite GIS measurement gives $gisArea ha.",
                        detectedDate = "2026-08-22",
                        status = IssueStatus.OPEN,
                        evidence = mapOf("Revenue Area" to "$revArea ha", "GIS Area" to "$gisArea ha"),
                        recommendedAction = "Verify DGPS survey boundary and reconcile with Taluk land records."
                    )
                )
            } else emptyList()

            parcels.add(
                Parcel(
                    id = id,
                    surveyNumber = surveyNo,
                    subDivision = surveyNo.substringAfter("/", "1"),
                    ownerName = owner,
                    previousOwner = "Prior Record Verified (2015)",
                    registrationDate = "10 May 2018",
                    deedNumber = "DOC-2018/SRO-${taluk.take(3).uppercase()}/${id.takeLast(4)}",
                    areaHectares = revArea,
                    gisCalculatedArea = gisArea,
                    district = "Coimbatore",
                    taluk = taluk,
                    village = village,
                    currentLandUse = landUse,
                    declaredLandUse = landUse,
                    gisDetectedLandUse = if (status == ParcelStatus.CRITICAL_ISSUE) LandUseType.COMMERCIAL else landUse,
                    status = status,
                    riskScore = risk,
                    verificationPercent = if (status == ParcelStatus.VERIFIED) 96 else 100 - risk,
                    latitude = lat,
                    longitude = lng,
                    boundary = polygon,
                    taxStatus = if (status == ParcelStatus.CRITICAL_ISSUE) "Overdue ₹18,500" else "Paid Up to Date",
                    lastTaxPaymentDate = "10 Jan 2026",
                    outstandingTaxAmount = if (status == ParcelStatus.CRITICAL_ISSUE) 18500.0 else 0.0,
                    courtCaseStatus = if (status == ParcelStatus.CRITICAL_ISSUE) "Dispute Registered at Sub-Court" else "No Litigation Found",
                    encumbranceStatus = if (status == ParcelStatus.CRITICAL_ISSUE) "Disputed Encumbrance" else "Nil Encumbrance",
                    legalStatus = if (status == ParcelStatus.CRITICAL_ISSUE) "Sub-Judice" else "Clear Title",
                    surveyDate = "15 Oct 2023",
                    boundaryStatus = if (status == ParcelStatus.VERIFIED) "DGPS Survey Verified" else "Variance Pending Resolution",
                    departmentSources = listOf(
                        DepartmentSourceRecord(DepartmentType.REVENUE, if (status == ParcelStatus.CRITICAL_ISSUE) SourceStatus.CONFLICT else SourceStatus.VERIFIED, "2026-08-26 12:00", "PATTA-$surveyNo", "Revenue Patta Record"),
                        DepartmentSourceRecord(DepartmentType.REGISTRATION, SourceStatus.VERIFIED, "2026-08-26 12:00", "DEED-${id.takeLast(4)}", "SRO Registration"),
                        DepartmentSourceRecord(DepartmentType.SURVEY, if (isConflict) SourceStatus.CONFLICT else SourceStatus.VERIFIED, "2026-08-26 12:00", "FMB-$surveyNo", "Cadastral Survey FMB"),
                        DepartmentSourceRecord(DepartmentType.TAX, SourceStatus.VERIFIED, "2026-08-26 12:00", "TAX-$surveyNo", "Municipal Tax Assessment"),
                        DepartmentSourceRecord(DepartmentType.PLANNING, SourceStatus.VERIFIED, "2026-08-26 12:00", "DTCP-ZON-$surveyNo", "DTCP Master Plan 2030"),
                        DepartmentSourceRecord(DepartmentType.LEGAL, if (status == ParcelStatus.CRITICAL_ISSUE) SourceStatus.CONFLICT else SourceStatus.VERIFIED, "2026-08-26 12:00", "NJDG-REC", "e-Courts NJDG Legal Status")
                    ),
                    issues = issuesList,
                    riskFactors = if (isConflict) listOf(RiskFactor("Discrepancy Detected", if (status == ParcelStatus.CRITICAL_ISSUE) IssueSeverity.CRITICAL else IssueSeverity.MEDIUM, risk, "Mismatch between survey ledger and GIS boundary")) else emptyList()
                )
            )
        }

        return parcels
    }

    private data class Quad(
        val taluk: String,
        val village: String,
        val owner: String,
        val landUse: LandUseType,
        val status: ParcelStatus,
        val risk: Int,
        val lat: Double,
        val lng: Double
    )
}
