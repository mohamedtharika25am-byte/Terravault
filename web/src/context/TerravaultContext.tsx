import React, { createContext, useContext, useState } from 'react';
import { Parcel, ParcelIssue, DepartmentSourceRecord, AuditLogEntry, UserProfile, UserRole, NavItem, ScreenState, ParcelStatus } from '../types/parcel';
import { initialParcels, initialDataSources, initialAuditLogs } from '../data/mockSeedData';
import confetti from 'canvas-confetti';

interface TerravaultContextType {
  parcels: Parcel[];
  issues: ParcelIssue[];
  dataSources: DepartmentSourceRecord[];
  auditLogs: AuditLogEntry[];
  userProfile: UserProfile;
  screenState: ScreenState;
  selectedNav: NavItem;
  activeParcelId: string | null;
  mapInitialParcelId: string | null;
  reportInitialParcelId: string | null;
  isSyncing: boolean;
  demoStepIndex: number;
  showSearchModal: boolean;
  showNotifications: boolean;
  showRoleModal: boolean;
  
  // State setters & Actions
  setScreenState: (state: ScreenState) => void;
  setSelectedNav: (nav: NavItem) => void;
  setActiveParcelId: (id: string | null) => void;
  setMapInitialParcelId: (id: string | null) => void;
  setReportInitialParcelId: (id: string | null) => void;
  setUserRole: (role: UserRole) => void;
  setShowSearchModal: (show: boolean) => void;
  setShowNotifications: (show: boolean) => void;
  setShowRoleModal: (show: boolean) => void;
  handleDemoStep: (stepIndex: number) => void;
  triggerSystemSync: () => void;
  resolveIssue: (issueId: string, officerNotes: string, resolutionReason: string) => boolean;
  getParcelById: (id: string) => Parcel | undefined;
  getIssueById: (id: string) => ParcelIssue | undefined;
}

const TerravaultContext = createContext<TerravaultContextType | undefined>(undefined);

export const TerravaultProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [parcels, setParcels] = useState<Parcel[]>(initialParcels);
  const [issues, setIssues] = useState<ParcelIssue[]>(() => initialParcels.flatMap(p => p.issues));
  const [dataSources, setDataSources] = useState<DepartmentSourceRecord[]>(initialDataSources);
  const [auditLogs, setAuditLogs] = useState<AuditLogEntry[]>(initialAuditLogs);
  const [userProfile, setUserProfile] = useState<UserProfile>({
    name: 'Dr. S. Karthikeyan, IAS',
    email: 'collector.cbe@terravault.gov.in',
    role: 'ADMIN',
    designation: 'Special Officer & District Collector',
    jurisdiction: 'Coimbatore District, Tamil Nadu'
  });

  const [screenState, setScreenState] = useState<ScreenState>('LANDING');
  const [selectedNav, setSelectedNav] = useState<NavItem>('DASHBOARD');
  const [activeParcelId, setActiveParcelId] = useState<string | null>('TN-COI-00123-0456');
  const [mapInitialParcelId, setMapInitialParcelId] = useState<string | null>(null);
  const [reportInitialParcelId, setReportInitialParcelId] = useState<string | null>('TN-COI-00123-0456');
  const [isSyncing, setIsSyncing] = useState<boolean>(false);
  const [demoStepIndex, setDemoStepIndex] = useState<number>(0);
  const [showSearchModal, setShowSearchModal] = useState<boolean>(false);
  const [showNotifications, setShowNotifications] = useState<boolean>(false);
  const [showRoleModal, setShowRoleModal] = useState<boolean>(false);

  const setUserRole = (role: UserRole) => {
    let name = 'Dr. S. Karthikeyan, IAS';
    let designation = 'Special Officer & District Collector';
    let jurisdiction = 'Coimbatore District, Tamil Nadu';

    if (role === 'GOVERNMENT_OFFICER') {
      name = 'T. Anbarasan, DRO';
      designation = 'Tahsildar & Revenue Officer';
      jurisdiction = 'Coimbatore South Taluk';
    } else if (role === 'REVIEWER') {
      name = 'K. Priya, M.Tech (GIS)';
      designation = 'Cadastral DGPS Survey Inspector';
      jurisdiction = 'District Land Records Survey Directorate';
    } else if (role === 'VIEWER') {
      name = 'R. Soundararajan';
      designation = 'Citizen / Land Owner';
      jurisdiction = 'Singanallur Village';
    }

    setUserProfile({
      name,
      email: `${role.toLowerCase()}@terravault.gov.in`,
      role,
      designation,
      jurisdiction
    });
  };

  const getParcelById = (id: string) => {
    return parcels.find(p => p.id.toLowerCase() === id.toLowerCase());
  };

  const getIssueById = (id: string) => {
    return issues.find(i => i.id.toLowerCase() === id.toLowerCase());
  };

  const handleDemoStep = (stepIndex: number) => {
    setDemoStepIndex(stepIndex);
    switch (stepIndex) {
      case 0:
        setScreenState('MAIN_HUB');
        setSelectedNav('DASHBOARD');
        break;
      case 1:
        setScreenState('MAIN_HUB');
        setSelectedNav('GIS_MAP');
        setMapInitialParcelId('TN-COI-00123-0456');
        break;
      case 2:
        setScreenState('MAIN_HUB');
        setSelectedNav('GIS_MAP');
        setMapInitialParcelId('TN-COI-00892-1102');
        break;
      case 3:
        setActiveParcelId('TN-COI-00123-0456');
        setScreenState('PARCEL_DETAIL');
        break;
      case 4:
        setScreenState('MAIN_HUB');
        setSelectedNav('ISSUES');
        break;
      case 5:
        setReportInitialParcelId('TN-COI-00123-0456');
        setScreenState('MAIN_HUB');
        setSelectedNav('REPORTS');
        break;
      default:
        break;
    }
  };

  const triggerSystemSync = () => {
    setIsSyncing(true);
    setTimeout(() => {
      setDataSources(prev => prev.map(s => ({
        ...s,
        lastUpdated: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' }),
        healthPercent: Math.min(100, Math.floor(Math.random() * 5 + 95)),
        latencyMs: Math.floor(Math.random() * 80 + 60)
      })));

      setAuditLogs(prev => [
        {
          id: `LOG-${Math.floor(Math.random() * 9000 + 1000)}`,
          timestamp: new Date().toLocaleDateString('en-GB') + ' ' + new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
          action: 'State Gateway Full Sync',
          userRole: userProfile.role,
          parcelId: null,
          details: 'Synchronized 12,480 cadastral ledger vectors across 6 TN State departmental gateways.'
        },
        ...prev
      ]);
      setIsSyncing(false);
    }, 1200);
  };

  const resolveIssue = (issueId: string, officerNotes: string, resolutionReason: string): boolean => {
    const issueIndex = issues.findIndex(i => i.id === issueId);
    if (issueIndex === -1) return false;

    const oldIssue = issues[issueIndex];
    const timestamp = new Date().toISOString().slice(0, 16).replace('T', ' ');
    const updatedIssue: ParcelIssue = {
      ...oldIssue,
      status: 'RESOLVED',
      resolvedBy: userProfile.name,
      resolutionNotes: `${resolutionReason}: ${officerNotes}`,
      resolvedAt: timestamp
    };

    const newIssues = [...issues];
    newIssues[issueIndex] = updatedIssue;
    setIssues(newIssues);

    // Update the corresponding Parcel
    const parcelIndex = parcels.findIndex(p => p.id === updatedIssue.parcelId);
    if (parcelIndex !== -1) {
      const parcel = parcels[parcelIndex];
      const remainingOpenIssues = newIssues.filter(
        i => i.parcelId === parcel.id && i.status !== 'RESOLVED' && i.status !== 'REJECTED'
      );

      const newRisk = remainingOpenIssues.length === 0 ? 5 : Math.max(...remainingOpenIssues.map(i => {
        return i.severity === 'CRITICAL' ? 100 : i.severity === 'HIGH' ? 70 : i.severity === 'MEDIUM' ? 40 : 15;
      }));

      let newStatus: ParcelStatus = 'VERIFIED';
      if (remainingOpenIssues.length === 0) {
        newStatus = 'VERIFIED';
      } else if (newRisk >= 75) {
        newStatus = 'CRITICAL_ISSUE';
      } else if (newRisk >= 35) {
        newStatus = 'NEEDS_REVIEW';
      } else {
        newStatus = 'UNDER_VERIFICATION';
      }

      const updatedParcels = [...parcels];
      updatedParcels[parcelIndex] = {
        ...parcel,
        status: newStatus,
        riskScore: newRisk,
        verificationPercent: newStatus === 'VERIFIED' ? 98 : 100 - newRisk,
        issues: newIssues.filter(i => i.parcelId === parcel.id)
      };
      setParcels(updatedParcels);
    }

    // Add Audit Log
    setAuditLogs(prev => [
      {
        id: `LOG-${Math.floor(Math.random() * 9000 + 1000)}`,
        timestamp: new Date().toLocaleDateString('en-GB') + ' ' + new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        action: 'Official Conflict Resolution',
        userRole: `${userProfile.role} (${userProfile.name})`,
        parcelId: oldIssue.parcelId,
        details: `Issue ${issueId} resolved. ${resolutionReason}: ${officerNotes}`
      },
      ...prev
    ]);

    // Fire celebration confetti
    try {
      confetti({
        particleCount: 80,
        spread: 70,
        origin: { y: 0.6 }
      });
    } catch {
      // ignore
    }

    return true;
  };

  return (
    <TerravaultContext.Provider
      value={{
        parcels,
        issues,
        dataSources,
        auditLogs,
        userProfile,
        screenState,
        selectedNav,
        activeParcelId,
        mapInitialParcelId,
        reportInitialParcelId,
        isSyncing,
        demoStepIndex,
        showSearchModal,
        showNotifications,
        showRoleModal,
        setScreenState,
        setSelectedNav,
        setActiveParcelId,
        setMapInitialParcelId,
        setReportInitialParcelId,
        setUserRole,
        setShowSearchModal,
        setShowNotifications,
        setShowRoleModal,
        handleDemoStep,
        triggerSystemSync,
        resolveIssue,
        getParcelById,
        getIssueById
      }}
    >
      {children}
    </TerravaultContext.Provider>
  );
};

export const useTerravault = () => {
  const context = useContext(TerravaultContext);
  if (!context) {
    throw new Error('useTerravault must be used within a TerravaultProvider');
  }
  return context;
};
