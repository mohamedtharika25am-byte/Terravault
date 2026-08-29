import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { 
  CheckCircle2, 
  AlertTriangle, 
  ShieldAlert, 
  MapPin, 
  Layers, 
  FileText, 
  ArrowRight, 
  Activity, 
  TrendingUp, 
  RefreshCw,
  Clock,
  Database,
  ExternalLink
} from 'lucide-react';
import { DemoWorkflowBanner } from '../components/DemoWorkflowBanner';

export const DashboardView: React.FC = () => {
  const { 
    parcels, 
    issues, 
    dataSources, 
    auditLogs, 
    setActiveParcelId, 
    setScreenState, 
    setSelectedNav,
    setMapInitialParcelId,
    triggerSystemSync,
    isSyncing
  } = useTerravault();

  const totalParcels = parcels.length;
  const verifiedParcels = parcels.filter(p => p.status === 'VERIFIED').length;
  const needsReviewParcels = parcels.filter(p => p.status === 'NEEDS_REVIEW').length;
  const criticalParcels = parcels.filter(p => p.status === 'CRITICAL_ISSUE').length;
  const underVerifParcels = parcels.filter(p => p.status === 'UNDER_VERIFICATION').length;
  const totalAreaHectares = parcels.reduce((acc, p) => acc + p.areaHectares, 0).toFixed(2);
  const verificationRate = ((verifiedParcels / totalParcels) * 100).toFixed(1);

  // Priority parcels with open conflicts
  const urgentParcels = parcels.filter(p => p.status === 'CRITICAL_ISSUE' || p.status === 'NEEDS_REVIEW').slice(0, 4);

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* SIH Demo Tour Banner */}
      <DemoWorkflowBanner />

      {/* KPI Cards Grid */}
      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(auto-fit, minmax(220px, 1fr))',
        gap: '16px'
      }}>
        {/* Total Parcels */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: '0.8rem', fontWeight: 700, color: '#52665F', textTransform: 'uppercase' }}>
              Total Land Parcels
            </span>
            <div style={{ width: '32px', height: '32px', borderRadius: '8px', backgroundColor: '#EEF3F0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <Layers size={16} color="#167A5B" />
            </div>
          </div>
          <div style={{ marginTop: '12px' }}>
            <div style={{ fontSize: '2rem', fontWeight: 800, color: '#192A24' }}>
              {totalParcels}
            </div>
            <div style={{ fontSize: '0.78rem', color: '#52665F', display: 'flex', alignItems: 'center', gap: '4px' }}>
              <span>{totalAreaHectares} ha Cadastral Area</span>
            </div>
          </div>
        </div>

        {/* 100% Verified */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderLeft: '4px solid #167A5B' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: '0.8rem', fontWeight: 700, color: '#167A5B', textTransform: 'uppercase' }}>
              Verified (100% Match)
            </span>
            <div style={{ width: '32px', height: '32px', borderRadius: '8px', backgroundColor: '#E6F6F0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <CheckCircle2 size={16} color="#167A5B" />
            </div>
          </div>
          <div style={{ marginTop: '12px' }}>
            <div style={{ fontSize: '2rem', fontWeight: 800, color: '#167A5B' }}>
              {verifiedParcels}
            </div>
            <div style={{ fontSize: '0.78rem', color: '#167A5B', fontWeight: 600 }}>
              {verificationRate}% Integrity Rate
            </div>
          </div>
        </div>

        {/* Needs Review */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderLeft: '4px solid #D99B2B' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: '0.8rem', fontWeight: 700, color: '#B47814', textTransform: 'uppercase' }}>
              Needs Review
            </span>
            <div style={{ width: '32px', height: '32px', borderRadius: '8px', backgroundColor: '#FEF6E9', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <AlertTriangle size={16} color="#D99B2B" />
            </div>
          </div>
          <div style={{ marginTop: '12px' }}>
            <div style={{ fontSize: '2rem', fontWeight: 800, color: '#B47814' }}>
              {needsReviewParcels}
            </div>
            <div style={{ fontSize: '0.78rem', color: '#52665F' }}>
              Boundary & Record variances
            </div>
          </div>
        </div>

        {/* Critical Conflicts */}
        <div className="card" style={{ display: 'flex', flexDirection: 'column', justifyContent: 'space-between', borderLeft: '4px solid #D94848' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <span style={{ fontSize: '0.8rem', fontWeight: 700, color: '#D94848', textTransform: 'uppercase' }}>
              Critical Conflicts
            </span>
            <div style={{ width: '32px', height: '32px', borderRadius: '8px', backgroundColor: '#FEEFEF', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
              <ShieldAlert size={16} color="#D94848" />
            </div>
          </div>
          <div style={{ marginTop: '12px' }}>
            <div style={{ fontSize: '2rem', fontWeight: 800, color: '#D94848' }}>
              {criticalParcels}
            </div>
            <div style={{ fontSize: '0.78rem', color: '#D94848', fontWeight: 600 }}>
              Wetland/Injunction alerts
            </div>
          </div>
        </div>
      </div>

      {/* Main Grid: Urgent Conflicts & Real-time Gateways */}
      <div style={{ display: 'grid', gridTemplateColumns: '1.6fr 1fr', gap: '24px' }}>
        {/* Left: Urgent Action Stream */}
        <div className="card" style={{ padding: '24px' }}>
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '18px' }}>
            <div>
              <h2 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24' }}>
                Priority Conflict Stream
              </h2>
              <div style={{ fontSize: '0.8rem', color: '#52665F' }}>
                Parcels requiring officer intervention and DGPS resurvey
              </div>
            </div>
            <button 
              onClick={() => {
                setSelectedNav('ISSUES');
                setScreenState('MAIN_HUB');
              }}
              className="btn btn-secondary"
              style={{ fontSize: '0.8rem', padding: '6px 12px' }}
            >
              View All ({issues.length}) <ArrowRight size={14} />
            </button>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {urgentParcels.map(parcel => {
              const topIssue = parcel.issues[0];
              const isCrit = parcel.status === 'CRITICAL_ISSUE';

              return (
                <div 
                  key={parcel.id}
                  style={{
                    padding: '16px',
                    borderRadius: '12px',
                    backgroundColor: isCrit ? '#FEF6F6' : '#FEFCF8',
                    border: isCrit ? '1px solid rgba(217, 72, 72, 0.3)' : '1px solid rgba(217, 155, 43, 0.3)',
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '8px'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                      <span style={{ fontWeight: 800, fontSize: '0.95rem', color: '#192A24' }}>
                        Survey No. {parcel.surveyNumber}
                      </span>
                      <span style={{ fontSize: '0.75rem', color: '#52665F' }}>
                        ({parcel.village}, {parcel.taluk})
                      </span>
                    </div>

                    <span className={isCrit ? 'badge badge-critical pulse-critical' : 'badge badge-review'}>
                      {parcel.status.replace('_', ' ')}
                    </span>
                  </div>

                  <div style={{ fontSize: '0.82rem', color: '#192A24', fontWeight: 600 }}>
                    Owner: {parcel.ownerName} • {parcel.areaHectares} ha
                  </div>

                  {topIssue && (
                    <div style={{
                      backgroundColor: 'rgba(255, 255, 255, 0.8)',
                      padding: '8px 12px',
                      borderRadius: '6px',
                      fontSize: '0.78rem',
                      color: isCrit ? '#B93232' : '#B47814',
                      fontWeight: 600
                    }}>
                      ⚠️ {topIssue.title}
                    </div>
                  )}

                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: '8px', marginTop: '4px' }}>
                    <button
                      onClick={() => {
                        setMapInitialParcelId(parcel.id);
                        setSelectedNav('GIS_MAP');
                        setScreenState('MAIN_HUB');
                      }}
                      className="btn btn-secondary"
                      style={{ padding: '5px 10px', fontSize: '0.75rem' }}
                    >
                      <MapPin size={12} color="#167A5B" /> Inspect on GIS
                    </button>
                    <button
                      onClick={() => {
                        setActiveParcelId(parcel.id);
                        setScreenState('PARCEL_DETAIL');
                      }}
                      className="btn btn-primary"
                      style={{ padding: '5px 12px', fontSize: '0.75rem' }}
                    >
                      View Intelligence Dossier <ArrowRight size={12} />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Right Column: DPI Gateway Matrix & Live Audit Log */}
        <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
          {/* DPI Gateways Health */}
          <div className="card" style={{ padding: '20px' }}>
            <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '14px' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                <Database size={18} color="#167A5B" />
                <h3 style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24' }}>
                  State DPI Gateways
                </h3>
              </div>
              <button 
                onClick={triggerSystemSync}
                disabled={isSyncing}
                style={{
                  background: 'none',
                  border: 'none',
                  color: '#167A5B',
                  fontSize: '0.75rem',
                  fontWeight: 700,
                  cursor: 'pointer',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '4px'
                }}
              >
                <RefreshCw size={12} className={isSyncing ? 'animate-spin' : ''} />
                {isSyncing ? 'Syncing...' : 'Sync'}
              </button>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
              {dataSources.map(ds => (
                <div 
                  key={ds.department}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    padding: '8px 10px',
                    borderRadius: '8px',
                    backgroundColor: '#F8FAF7',
                    border: '1px solid #DEE8E3'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <span style={{
                      width: '8px',
                      height: '8px',
                      borderRadius: '50%',
                      backgroundColor: ds.status === 'CONFLICT' ? '#D94848' : '#167A5B'
                    }}></span>
                    <span style={{ fontSize: '0.8rem', fontWeight: 700, color: '#192A24' }}>
                      {ds.department}
                    </span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: '0.72rem', color: '#52665F' }}>
                    <span>{ds.latencyMs}ms</span>
                    <span style={{ fontWeight: 700, color: ds.status === 'CONFLICT' ? '#D94848' : '#167A5B' }}>
                      {ds.status}
                    </span>
                  </div>
                </div>
              ))}
            </div>
          </div>

          {/* Real-time Audit Stream */}
          <div className="card" style={{ padding: '20px' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '14px' }}>
              <Activity size={18} color="#167A5B" />
              <h3 style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24' }}>
                Live Governance Audit Log
              </h3>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '10px', maxHeight: '230px', overflowY: 'auto' }}>
              {auditLogs.slice(0, 4).map(log => (
                <div 
                  key={log.id}
                  style={{
                    fontSize: '0.75rem',
                    borderLeft: '2px solid #167A5B',
                    paddingLeft: '10px',
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '2px'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <span style={{ fontWeight: 700, color: '#192A24' }}>{log.action}</span>
                    <span style={{ color: '#7E948C', fontSize: '0.68rem' }}>{log.timestamp}</span>
                  </div>
                  <span style={{ color: '#52665F' }}>{log.details}</span>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
