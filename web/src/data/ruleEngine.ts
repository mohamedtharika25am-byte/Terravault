import { Parcel, ParcelIssue, ParcelStatus, IssueSeverity, RiskFactor } from '../types/parcel';

export interface RuleEvaluationResult {
  detectedIssues: ParcelIssue[];
  riskScore: number;
  verificationPercent: number;
  status: ParcelStatus;
  riskFactors: RiskFactor[];
  explanationSummary: string;
}

export function evaluateParcel(parcel: Parcel): RuleEvaluationResult {
  const issues: ParcelIssue[] = [];
  const riskFactors: RiskFactor[] = [];

  // 1. Boundary & Area Discrepancy Rule
  const areaDiff = Math.abs(parcel.areaHectares - parcel.gisCalculatedArea);
  if (areaDiff > 0.03) {
    const severity: IssueSeverity = areaDiff > 0.15 ? 'CRITICAL' : areaDiff > 0.08 ? 'HIGH' : 'MEDIUM';
    const weight = severity === 'CRITICAL' ? 100 : severity === 'HIGH' ? 70 : 40;
    
    issues.push({
      id: `ISS-${parcel.id.slice(-4)}-01`,
      parcelId: parcel.id,
      issueType: areaDiff > 0.10 ? 'BOUNDARY_MISMATCH' : 'AREA_DISCREPANCY',
      severity,
      department: 'SURVEY',
      title: `Cadastral Survey vs GIS Area Mismatch (${areaDiff.toFixed(2)} ha)`,
      description: `Revenue records state ${parcel.areaHectares} ha while GIS polygon calculation yields ${parcel.gisCalculatedArea} ha.`,
      detectedDate: '2026-08-20',
      status: 'OPEN',
      evidence: {
        'Survey Record Area': `${parcel.areaHectares} ha`,
        'GIS Satellite Area': `${parcel.gisCalculatedArea} ha`,
        'Calculated Variance': `${areaDiff.toFixed(2)} ha (${((areaDiff / parcel.areaHectares) * 100).toFixed(1)}%)`,
        'Survey Station': 'Coimbatore DGPS Grid #44'
      },
      recommendedAction: 'Initiate physical DGPS resurvey to reconcile cadastral boundary coordinates with Revenue Survey Office.'
    });

    riskFactors.push({
      title: 'Boundary Area Variance',
      severity,
      points: weight,
      reason: `Physical survey polygon differs from GIS boundary by ${areaDiff.toFixed(2)} ha.`
    });
  }

  // 2. Ownership & Registration Conflict Rule
  const regRecord = parcel.departmentSources.find(s => s.department === 'REGISTRATION');
  const revRecord = parcel.departmentSources.find(s => s.department === 'REVENUE');
  if (regRecord?.status === 'CONFLICT' || revRecord?.status === 'CONFLICT') {
    issues.push({
      id: `ISS-${parcel.id.slice(-4)}-02`,
      parcelId: parcel.id,
      issueType: 'OWNERSHIP_CONFLICT',
      severity: 'HIGH',
      department: 'REGISTRATION',
      title: 'Cross-Department Ownership Mismatch',
      description: `Revenue Patta lists ${parcel.ownerName} but Sub-Registrar Office deed has a pending transfer or dual claimant record.`,
      detectedDate: '2026-08-22',
      status: 'OPEN',
      evidence: {
        'Revenue Patta Owner': parcel.ownerName,
        'Registration Deed Party': parcel.previousOwner.split(' ')[0] ? `${parcel.previousOwner.split(' ')[0]} (Claimant)` : 'Conflicting Claimant',
        'Deed Reference': parcel.deedNumber,
        'Sub-Registrar Jurisdiction': 'SRO Coimbatore District'
      },
      recommendedAction: 'Cross-check latest registered encumbrance certificate and verify mutation index at Taluk Revenue Office.'
    });

    riskFactors.push({
      title: 'Ownership Record Conflict',
      severity: 'HIGH',
      points: 70,
      reason: 'Inconsistent claimant names detected between Revenue Patta Register and SRO Registered Sale Deed.'
    });
  }

  // 3. Land Use & Master Plan Violation Rule
  if (parcel.currentLandUse !== parcel.declaredLandUse || parcel.currentLandUse !== parcel.gisDetectedLandUse) {
    const isBufferViolation = parcel.gisDetectedLandUse === 'WATER_BODY_BUFFER' || parcel.declaredLandUse === 'WATER_BODY_BUFFER';
    const severity: IssueSeverity = isBufferViolation ? 'CRITICAL' : 'MEDIUM';
    const weight = isBufferViolation ? 100 : 40;

    issues.push({
      id: `ISS-${parcel.id.slice(-4)}-03`,
      parcelId: parcel.id,
      issueType: 'LAND_USE_CONFLICT',
      severity,
      department: 'PLANNING',
      title: isBufferViolation ? 'Critical Water-Body / Buffer Zone Encroachment' : 'Land Use Classification Conflict',
      description: `Declared as ${parcel.declaredLandUse}, but satellite GIS spectral analysis detects active ${parcel.gisDetectedLandUse} usage.`,
      detectedDate: '2026-08-24',
      status: 'OPEN',
      evidence: {
        'Declared Master Plan': parcel.declaredLandUse,
        'GIS Detected Spectral': parcel.gisDetectedLandUse,
        'Current Revenue Entry': parcel.currentLandUse,
        'DTCP Zone Clearance': isBufferViolation ? 'NO (Prohibited Buffer)' : 'Conditional Clearance'
      },
      recommendedAction: isBufferViolation 
        ? 'Issue immediate notice for wetland/water body preservation review under Section 4(1) of TN Land Encroachment Act.'
        : 'Submit for DTCP Land Conversion re-assessment and verify commercial property tax slab.'
    });

    riskFactors.push({
      title: 'Land Use Zonal Discrepancy',
      severity,
      points: weight,
      reason: 'Satellite GIS indices contradict declared DTCP master plan classification.'
    });
  }

  // 4. Missing Record / Court Case Rule
  if (parcel.courtCaseStatus.includes('Pending') || parcel.courtCaseStatus.includes('Injunction') || parcel.courtCaseStatus.includes('Suit')) {
    issues.push({
      id: `ISS-${parcel.id.slice(-4)}-04`,
      parcelId: parcel.id,
      issueType: 'ENCUMBRANCE_ALERT',
      severity: 'HIGH',
      department: 'LEGAL',
      title: 'Pending Judicial Litigation / Injunction Alert',
      description: `Active court litigation found: ${parcel.courtCaseStatus}. Property transfer prohibited under sub-judice status.`,
      detectedDate: '2026-08-15',
      status: 'OPEN',
      evidence: {
        'Case Tracking': parcel.courtCaseStatus,
        'Court Name': 'Principal District Court, Coimbatore',
        'Encumbrance Status': parcel.encumbranceStatus,
        'Injunction Status': 'Active Status Quo Order'
      },
      recommendedAction: 'Sync with e-Courts National Judicial Data Grid (NJDG) to obtain latest order copy before proceeding with mutation.'
    });

    riskFactors.push({
      title: 'Active Court Litigation',
      severity: 'HIGH',
      points: 70,
      reason: 'Court case registered on survey number; legal encumbrance restricts transfer.'
    });
  }

  // Calculate weighted Risk Score (0 - 100)
  const calculatedRisk = riskFactors.length === 0 
    ? 0 
    : Math.min(100, Math.max(...riskFactors.map(r => r.points)) + (riskFactors.length - 1) * 10);

  const calculatedVerification = calculatedRisk === 0 ? 98 : Math.max(15, 100 - calculatedRisk);

  let computedStatus: ParcelStatus = 'VERIFIED';
  if (calculatedRisk >= 75) {
    computedStatus = 'CRITICAL_ISSUE';
  } else if (calculatedRisk >= 35) {
    computedStatus = 'NEEDS_REVIEW';
  } else if (issues.some(i => i.status === 'UNDER_REVIEW')) {
    computedStatus = 'UNDER_VERIFICATION';
  }

  const explanation = calculatedRisk >= 75 
    ? 'Critical risk triggered due to severe high-penalty conflicts (e.g. wetland encroachment or legal injunctions) requiring immediate intervention.'
    : calculatedRisk >= 35
    ? 'Moderate risk detected due to boundary area variances or cross-department data mismatches that require officer review.'
    : 'High integrity parcel with all 6 department records fully synchronized and cadastral polygon matching GIS coordinates.';

  return {
    detectedIssues: issues.length === 0 ? parcel.issues : issues,
    riskScore: calculatedRisk,
    verificationPercent: calculatedVerification,
    status: computedStatus,
    riskFactors,
    explanationSummary: explanation
  };
}
