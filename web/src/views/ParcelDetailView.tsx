import React, { useState } from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { 
  ShieldCheck, 
  AlertTriangle, 
  MapPin, 
  FileText, 
  CheckCircle2, 
  XCircle, 
  Scale, 
  Building2, 
  Clock, 
  UserCheck, 
  Compass, 
  Sparkles,
  ArrowRight,
  Printer,
  FileCheck,
  Check
} from 'lucide-react';

export const ParcelDetailView: React.FC = () => {
  const { 
    activeParcelId, 
    getParcelById, 
    setMapInitialParcelId, 
    setSelectedNav, 
    setScreenState,
    setReportInitialParcelId,
    resolveIssue,
    userProfile
  } = useTerravault();

  const [selectedIssueToResolve, setSelectedIssueToResolve] = useState<string | null>(null);
  const [resolutionReason, setResolutionReason] = useState<string>('Joint DGPS Field Resurvey Completed');
  const [officerNotes, setOfficerNotes] = useState<string>('Field boundary inspected with Taluk Surveyor. Coordinates reconciled with FMB.');

  const parcel = activeParcelId ? getParcelById(activeParcelId) : undefined;

  if (!parcel) {
    return (
      <div className="card" style={{ padding: '40px', textAlign: 'center' }}>
        <h2 style={{ fontSize: '1.2rem', fontWeight: 800, color: '#192A24' }}>No Parcel Selected</h2>
        <button 
          onClick={() => {
            setSelectedNav('PARCELS');
            setScreenState('MAIN_HUB');
          }}
          className="btn btn-primary"
          style={{ marginTop: '16px' }}
        >
          Return to Registry
        </button>
      </div>
    );
  }

  const areaDiff = Math.abs(parcel.areaHectares - parcel.gisCalculatedArea);
  const areaMismatch = areaDiff > 0.03;
  const isCritical = parcel.status === 'CRITICAL_ISSUE';

  const handleResolveSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedIssueToResolve) {
      resolveIssue(selectedIssueToResolve, officerNotes, resolutionReason);
      setSelectedIssueToResolve(null);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Top Banner Card */}
      <div className="card" style={{ padding: '24px', borderLeft: isCritical ? '6px solid #D94848' : parcel.status === 'VERIFIED' ? '6px solid #167A5B' : '6px solid #D99B2B' }}>
        <div style={{ display: 'flex', alignItems: 'flex-start', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '4px' }}>
              <h1 style={{ fontSize: '1.6rem', fontWeight: 900, color: '#192A24' }}>
                Survey No. {parcel.surveyNumber} (Sub-Division {parcel.subDivision})
              </h1>
              <span className={`badge ${parcel.status === 'VERIFIED' ? 'badge-verified' : isCritical ? 'badge-critical' : 'badge-review'}`}>
                {parcel.status.replace('_', ' ')}
              </span>
            </div>

            <div style={{ display: 'flex', alignItems: 'center', gap: '12px', fontSize: '0.85rem', color: '#52665F' }}>
              <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                <MapPin size={14} color="#167A5B" /> {parcel.village} Village, {parcel.taluk} Taluk, {parcel.district}
              </span>
              <span>•</span>
              <span>Cadastral ID: <strong>{parcel.id}</strong></span>
              <span>•</span>
              <span>Deed: <strong>{parcel.deedNumber}</strong></span>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <button
              onClick={() => {
                setMapInitialParcelId(parcel.id);
                setSelectedNav('GIS_MAP');
                setScreenState('MAIN_HUB');
              }}
              className="btn btn-secondary"
            >
              <Compass size={16} color="#167A5B" /> View on GIS Map
            </button>
            <button
              onClick={() => {
                setReportInitialParcelId(parcel.id);
                setSelectedNav('REPORTS');
                setScreenState('MAIN_HUB');
              }}
              className="btn btn-primary"
            >
              <FileCheck size={16} /> Official Certificate Report
            </button>
          </div>
        </div>

        {/* Intelligence Scorecards */}
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fit, minmax(180px, 1fr))',
          gap: '16px',
          marginTop: '20px',
          paddingTop: '20px',
          borderTop: '1px solid #DEE8E3'
        }}>
          <div>
            <div style={{ fontSize: '0.72rem', fontWeight: 800, textTransform: 'uppercase', color: '#7E948C' }}>Registered Owner</div>
            <div style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24' }}>{parcel.ownerName}</div>
            <div style={{ fontSize: '0.72rem', color: '#52665F' }}>Reg Date: {parcel.registrationDate}</div>
          </div>

          <div>
            <div style={{ fontSize: '0.72rem', fontWeight: 800, textTransform: 'uppercase', color: '#7E948C' }}>Revenue vs GIS Area</div>
            <div style={{ fontSize: '1rem', fontWeight: 800, color: areaMismatch ? '#D94848' : '#167A5B' }}>
              {parcel.areaHectares} ha / {parcel.gisCalculatedArea} ha
            </div>
            <div style={{ fontSize: '0.72rem', color: areaMismatch ? '#D94848' : '#167A5B', fontWeight: 600 }}>
              {areaMismatch ? `Δ ${areaDiff.toFixed(2)} ha (${((areaDiff/parcel.areaHectares)*100).toFixed(1)}% variance)` : '100% Cadastral Match'}
            </div>
          </div>

          <div>
            <div style={{ fontSize: '0.72rem', fontWeight: 800, textTransform: 'uppercase', color: '#7E948C' }}>Risk Score (0-100)</div>
            <div style={{ fontSize: '1.2rem', fontWeight: 900, color: parcel.riskScore > 50 ? '#D94848' : parcel.riskScore > 20 ? '#B47814' : '#167A5B' }}>
              {parcel.riskScore} / 100
            </div>
            <div style={{ fontSize: '0.72rem', color: '#52665F' }}>
              {parcel.verificationPercent}% Verification Confidence
            </div>
          </div>

          <div>
            <div style={{ fontSize: '0.72rem', fontWeight: 800, textTransform: 'uppercase', color: '#7E948C' }}>Judicial & Legal Status</div>
            <div style={{ fontSize: '0.85rem', fontWeight: 700, color: parcel.courtCaseStatus.includes('No') ? '#167A5B' : '#D94848' }}>
              {parcel.courtCaseStatus}
            </div>
            <div style={{ fontSize: '0.72rem', color: '#52665F' }}>{parcel.legalStatus}</div>
          </div>
        </div>
      </div>

      {/* 6 Department Verification Ledger Matrix */}
      <div className="card" style={{ padding: '24px' }}>
        <h2 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
          Unified 6-Department Verification Matrix
        </h2>
        <p style={{ fontSize: '0.8rem', color: '#52665F', marginBottom: '16px' }}>
          Real-time cross-matching of state revenue, registration, survey, municipal tax, urban planning, and court records.
        </p>

        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(280px, 1fr))', gap: '14px' }}>
          {parcel.departmentSources.map(source => {
            const isConflict = source.status === 'CONFLICT';
            const isMissing = source.status === 'MISSING';

            return (
              <div 
                key={source.department}
                style={{
                  padding: '16px',
                  borderRadius: '10px',
                  backgroundColor: isConflict ? '#FEF8F8' : isMissing ? '#FFFDF5' : '#F8FAF7',
                  border: isConflict ? '1px solid #FBDADA' : isMissing ? '1px solid #FFE8B8' : '1px solid #DEE8E3',
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '6px'
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                  <span style={{ fontWeight: 800, fontSize: '0.85rem', color: '#192A24' }}>
                    {source.department}
                  </span>
                  <span style={{
                    fontSize: '0.68rem',
                    fontWeight: 800,
                    padding: '2px 6px',
                    borderRadius: '4px',
                    backgroundColor: isConflict ? '#FEEFEF' : isMissing ? '#FEF6E9' : '#E6F6F0',
                    color: isConflict ? '#D94848' : isMissing ? '#B47814' : '#167A5B'
                  }}>
                    {source.status}
                  </span>
                </div>

                <div style={{ fontSize: '0.72rem', color: '#7E948C', fontFamily: 'var(--font-mono)' }}>
                  Ref: {source.recordNumber}
                </div>

                <div style={{ fontSize: '0.78rem', color: isConflict ? '#B93232' : '#192A24', fontWeight: isConflict ? 600 : 500 }}>
                  {source.details}
                </div>

                <div style={{ fontSize: '0.68rem', color: '#7E948C', marginTop: 'auto', paddingTop: '4px' }}>
                  Synced: {source.lastUpdated}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      {/* Discrepancies, Rule Engine Evidence & Official Resolution Section */}
      <div className="card" style={{ padding: '24px' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '16px' }}>
          <div>
            <h2 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24' }}>
              Detected Discrepancies & AI Rule Engine Analysis ({parcel.issues.length})
            </h2>
            <div style={{ fontSize: '0.8rem', color: '#52665F' }}>
              Automated conflict identification and recommended statutory actions
            </div>
          </div>
        </div>

        {parcel.issues.length === 0 ? (
          <div style={{
            padding: '24px',
            borderRadius: '10px',
            backgroundColor: '#E6F6F0',
            border: '1px solid rgba(22, 122, 91, 0.3)',
            display: 'flex',
            alignItems: 'center',
            gap: '12px'
          }}>
            <CheckCircle2 size={24} color="#167A5B" />
            <div>
              <div style={{ fontWeight: 800, fontSize: '0.95rem', color: '#167A5B' }}>
                Zero Discrepancies Found • High Integrity Land Parcel
              </div>
              <div style={{ fontSize: '0.8rem', color: '#52665F' }}>
                All 6 departmental datasets perfectly synchronize with cadastral DGPS spatial vector coordinates.
              </div>
            </div>
          </div>
        ) : (
          <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
            {parcel.issues.map((issue) => {
              const isResolved = issue.status === 'RESOLVED';

              return (
                <div 
                  key={issue.id}
                  style={{
                    padding: '20px',
                    borderRadius: '12px',
                    backgroundColor: isResolved ? '#F8FAF7' : issue.severity === 'CRITICAL' ? '#FEF6F6' : '#FEFCF8',
                    border: isResolved ? '1px solid #DEE8E3' : issue.severity === 'CRITICAL' ? '1px solid #FBDADA' : '1px solid #FFE8B8',
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '12px'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                      <span className={`badge ${isResolved ? 'badge-verified' : issue.severity === 'CRITICAL' ? 'badge-critical' : 'badge-review'}`}>
                        {issue.severity} • {issue.department}
                      </span>
                      <span style={{ fontWeight: 800, fontSize: '1rem', color: '#192A24' }}>
                        {issue.title}
                      </span>
                    </div>

                    <span style={{
                      fontSize: '0.75rem',
                      fontWeight: 800,
                      padding: '3px 8px',
                      borderRadius: '6px',
                      backgroundColor: isResolved ? '#E6F6F0' : '#FEF6E9',
                      color: isResolved ? '#167A5B' : '#B47814'
                    }}>
                      Status: {issue.status}
                    </span>
                  </div>

                  <p style={{ fontSize: '0.85rem', color: '#52665F', lineHeight: 1.5 }}>
                    {issue.description}
                  </p>

                  {/* Concrete Evidence Key-Values */}
                  <div style={{
                    backgroundColor: '#FFFFFF',
                    padding: '12px 16px',
                    borderRadius: '8px',
                    border: '1px solid #DEE8E3',
                    display: 'grid',
                    gridTemplateColumns: 'repeat(auto-fit, minmax(200px, 1fr))',
                    gap: '10px'
                  }}>
                    {Object.entries(issue.evidence).map(([k, v]) => (
                      <div key={k}>
                        <span style={{ fontSize: '0.7rem', color: '#7E948C', textTransform: 'uppercase', fontWeight: 700 }}>
                          {k}:
                        </span>{' '}
                        <span style={{ fontSize: '0.82rem', fontWeight: 700, color: '#192A24' }}>
                          {v}
                        </span>
                      </div>
                    ))}
                  </div>

                  <div style={{
                    backgroundColor: 'rgba(22, 122, 91, 0.08)',
                    padding: '10px 14px',
                    borderRadius: '8px',
                    fontSize: '0.82rem',
                    color: '#0F543E',
                    fontWeight: 600
                  }}>
                    <strong>Statutory Recommended Action:</strong> {issue.recommendedAction}
                  </div>

                  {isResolved ? (
                    <div style={{
                      backgroundColor: '#E6F6F0',
                      padding: '10px 14px',
                      borderRadius: '8px',
                      fontSize: '0.78rem',
                      color: '#167A5B',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'space-between'
                    }}>
                      <span><strong>Resolution Recorded:</strong> {issue.resolutionNotes}</span>
                      <span>By: {issue.resolvedBy} on {issue.resolvedAt}</span>
                    </div>
                  ) : (
                    <div style={{ display: 'flex', justifyContent: 'flex-end' }}>
                      <button
                        onClick={() => setSelectedIssueToResolve(issue.id)}
                        className="btn btn-primary"
                        style={{ fontSize: '0.8rem', padding: '6px 14px' }}
                      >
                        <UserCheck size={14} /> Execute Officer Resolution & Digital Sign-off
                      </button>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </div>

      {/* Officer Resolution Modal */}
      {selectedIssueToResolve && (
        <div style={{
          position: 'fixed',
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: 'rgba(15, 84, 62, 0.4)',
          backdropFilter: 'blur(4px)',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          zIndex: 120,
          padding: '20px'
        }}>
          <form 
            onSubmit={handleResolveSubmit}
            style={{
              width: '540px',
              maxWidth: '100%',
              backgroundColor: '#FFFFFF',
              borderRadius: '14px',
              boxShadow: 'var(--shadow-lg)',
              border: '1px solid #DEE8E3',
              overflow: 'hidden'
            }}
          >
            <div style={{
              padding: '18px 22px',
              borderBottom: '1px solid #DEE8E3',
              backgroundColor: '#0F543E',
              color: '#FFFFFF'
            }}>
              <h3 style={{ fontSize: '1.1rem', fontWeight: 800 }}>Official Officer Dispute Resolution</h3>
              <div style={{ fontSize: '0.75rem', color: '#A3C7B9' }}>
                Authority: {userProfile.name} ({userProfile.designation})
              </div>
            </div>

            <div style={{ padding: '20px 22px', display: 'flex', flexDirection: 'column', gap: '14px' }}>
              <div>
                <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#192A24', display: 'block', marginBottom: '6px' }}>
                  Resolution Category
                </label>
                <select
                  value={resolutionReason}
                  onChange={(e) => setResolutionReason(e.target.value)}
                  style={{
                    width: '100%',
                    padding: '8px 12px',
                    borderRadius: '8px',
                    border: '1px solid #DEE8E3',
                    fontSize: '0.85rem',
                    backgroundColor: '#F8FAF7'
                  }}
                >
                  <option value="Joint DGPS Field Resurvey Completed">Joint DGPS Field Resurvey Completed (Survey Reconciled)</option>
                  <option value="Mutation Registered & Title Deed Cleared">Mutation Registered & SRO Title Deed Cleared</option>
                  <option value="Commercial Regularization Penalty Paid">Commercial Regularization Penalty & DTCP Approval</option>
                  <option value="Judicial Certified Copy NJDG Ingestion">Judicial Certified Copy NJDG Ingestion (Stay Vacated)</option>
                  <option value="Encroachment Eviction & Waterbody Recovery">Encroachment Eviction & Waterbody Buffer Recovery</option>
                </select>
              </div>

              <div>
                <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#192A24', display: 'block', marginBottom: '6px' }}>
                  Officer Verification Notes & Statutory Order Number
                </label>
                <textarea
                  rows={3}
                  value={officerNotes}
                  onChange={(e) => setOfficerNotes(e.target.value)}
                  placeholder="Enter physical inspection findings, order numbers, and DRO sanction..."
                  required
                  style={{
                    width: '100%',
                    padding: '8px 12px',
                    borderRadius: '8px',
                    border: '1px solid #DEE8E3',
                    fontSize: '0.85rem',
                    fontFamily: 'inherit'
                  }}
                />
              </div>

              <div style={{
                backgroundColor: '#E8F4EE',
                padding: '10px 14px',
                borderRadius: '8px',
                fontSize: '0.75rem',
                color: '#0F543E'
              }}>
                ✓ Submitting will digitally stamp this record, recompute parcel risk score to <strong>5/100 (Verified)</strong>, and record entry into the immutable DPI audit ledger.
              </div>
            </div>

            <div style={{
              padding: '14px 22px',
              borderTop: '1px solid #DEE8E3',
              backgroundColor: '#F8FAF7',
              display: 'flex',
              justifyContent: 'flex-end',
              gap: '10px'
            }}>
              <button
                type="button"
                onClick={() => setSelectedIssueToResolve(null)}
                className="btn btn-secondary"
              >
                Cancel
              </button>
              <button
                type="submit"
                className="btn btn-primary"
              >
                <Check size={16} /> Confirm & Sign Clearance
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};
