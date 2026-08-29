export type ParcelStatus = 'VERIFIED' | 'NEEDS_REVIEW' | 'CRITICAL_ISSUE' | 'UNDER_VERIFICATION';

export type LandUseType = 
  | 'AGRICULTURAL'
  | 'RESIDENTIAL'
  | 'COMMERCIAL'
  | 'INDUSTRIAL'
  | 'WATER_BODY_BUFFER'
  | 'FOREST_RESERVE'
  | 'MIXED_USE';

export type DepartmentType = 
  | 'REVENUE'
  | 'REGISTRATION'
  | 'SURVEY'
  | 'TAX'
  | 'PLANNING'
  | 'LEGAL';

export type IssueType = 
  | 'BOUNDARY_MISMATCH'
  | 'OWNERSHIP_CONFLICT'
  | 'AREA_DISCREPANCY'
  | 'LAND_USE_CONFLICT'
  | 'MISSING_RECORD'
  | 'DUPLICATE_RECORD'
  | 'OUTDATED_RECORD'
  | 'ENCUMBRANCE_ALERT';

export type IssueSeverity = 'CRITICAL' | 'HIGH' | 'MEDIUM' | 'LOW';

export type IssueStatus = 'OPEN' | 'UNDER_REVIEW' | 'RESOLVED' | 'REJECTED';

export type SourceStatus = 'VERIFIED' | 'CONFLICT' | 'MISSING' | 'SYNCED' | 'DEMO_MOCK';

export interface CadastralPoint {
  lat: Double;
  lng: Double;
}

export type Double = number;

export interface DepartmentSourceRecord {
  department: DepartmentType;
  status: SourceStatus;
  lastUpdated: string;
  recordNumber: string;
  details: string;
  healthPercent?: number;
  latencyMs?: number;
  totalRecords?: number;
}

export interface ParcelIssue {
  id: string;
  parcelId: string;
  issueType: IssueType;
  severity: IssueSeverity;
  department: DepartmentType;
  title: string;
  description: string;
  detectedDate: string;
  status: IssueStatus;
  evidence: Record<string, string>;
  recommendedAction: string;
  resolvedBy?: string | null;
  resolutionNotes?: string | null;
  resolvedAt?: string | null;
}

export interface RiskFactor {
  title: string;
  severity: IssueSeverity;
  points: number;
  reason: string;
}

export interface Parcel {
  id: string;
  surveyNumber: string;
  subDivision: string;
  ownerName: string;
  previousOwner: string;
  registrationDate: string;
  deedNumber: string;
  areaHectares: number;
  gisCalculatedArea: number;
  district: string;
  taluk: string;
  village: string;
  currentLandUse: LandUseType;
  declaredLandUse: LandUseType;
  gisDetectedLandUse: LandUseType;
  status: ParcelStatus;
  riskScore: number;
  verificationPercent: number;
  latitude: number;
  longitude: number;
  boundary: CadastralPoint[];
  taxStatus: string;
  lastTaxPaymentDate: string;
  outstandingTaxAmount: number;
  courtCaseStatus: string;
  encumbranceStatus: string;
  legalStatus: string;
  surveyDate: string;
  boundaryStatus: string;
  departmentSources: DepartmentSourceRecord[];
  issues: ParcelIssue[];
  riskFactors: RiskFactor[];
}

export interface AuditLogEntry {
  id: string;
  timestamp: string;
  action: string;
  userRole: string;
  parcelId?: string | null;
  details: string;
}

export type UserRole = 'ADMIN' | 'GOVERNMENT_OFFICER' | 'REVIEWER' | 'VIEWER';

export interface UserProfile {
  name: string;
  email: string;
  role: UserRole;
  designation: string;
  jurisdiction: string;
}

export type NavItem = 
  | 'DASHBOARD'
  | 'GIS_MAP'
  | 'PARCELS'
  | 'ISSUES'
  | 'ANALYTICS'
  | 'DATA_SOURCES'
  | 'REPORTS';

export type ScreenState = 'LANDING' | 'MAIN_HUB' | 'PARCEL_DETAIL';
