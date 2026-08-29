import React, { useState } from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { ParcelIssue, IssueSeverity, DepartmentType, IssueStatus } from '../types/parcel';
import { 
  AlertTriangle, 
  ShieldAlert, 
  CheckCircle2, 
  Filter, 
  Search, 
  ArrowRight, 
  UserCheck, 
  MapPin, 
  Sparkles,
  ExternalLink
} from 'lucide-react';

export const IssuesView: React.FC = () => {
  const { 
    issues, 
    parcels, 
    setActiveParcelId, 
    setScreenState, 
    resolveIssue, 
    userProfile 
  } = useTerravault();

  const [selectedSeverity, setSelectedSeverity] = useState<string>('ALL');
  const [selectedDept, setSelectedDept] = useState<string>('ALL');
  const [selectedStatus, setSelectedStatus] = useState<string>('ALL');
  const [searchQuery, setSearchQuery] = useState<string>('');

  const [resolvingIssueId, setResolvingIssueId] = useState<string | null>(null);
  const [notes, setNotes] = useState('Joint DGPS Survey and Field Verification cleared with Revenue Tahsildar.');
  const [reason, setReason] = useState('DGPS Resurvey Verification');

  const filteredIssues = issues.filter(issue => {
    const q = searchQuery.toLowerCase().trim();
    const matchQuery = q === '' ||
      issue.id.toLowerCase().includes(q) ||
      issue.parcelId.toLowerCase().includes(q) ||
      issue.title.toLowerCase().includes(q) ||
      issue.description.toLowerCase().includes(q);

    const matchSev = selectedSeverity === 'ALL' || issue.severity === selectedSeverity;
    const matchDept = selectedDept === 'ALL' || issue.department === selectedDept;
    const matchStatus = selectedStatus === 'ALL' || issue.status === selectedStatus;

    return matchQuery && matchSev && matchDept && matchStatus;
  });

  const criticalCount = issues.filter(i => i.severity === 'CRITICAL' && i.status === 'OPEN').length;
  const highCount = issues.filter(i => i.severity === 'HIGH' && i.status === 'OPEN').length;
  const resolvedCount = issues.filter(i => i.status === 'RESOLVED').length;

  const handleQuickResolve = (e: React.FormEvent) => {
    e.preventDefault();
    if (resolvingIssueId) {
      resolveIssue(resolvingIssueId, notes, reason);
      setResolvingIssueId(null);
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Header & Metric Banner */}
      <div className="card" style={{ padding: '24px' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px', marginBottom: '20px' }}>
          <div>
            <h2 style={{ fontSize: '1.3rem', fontWeight: 800, color: '#192A24' }}>
              Conflict Detection & Dispute Resolution Engine
            </h2>
            <div style={{ fontSize: '0.82rem', color: '#52665F' }}>
              Autonomous rule-based identification of boundary discrepancies, illegal conversions, and court litigations
            </div>
          </div>

          <div style={{ display: 'flex', gap: '12px' }}>
            <div style={{ padding: '8px 14px', borderRadius: '8px', backgroundColor: '#FEEFEF', border: '1px solid #FBDADA', textAlign: 'center' }}>
              <div style={{ fontSize: '1.2rem', fontWeight: 900, color: '#D94848' }}>{criticalCount}</div>
              <div style={{ fontSize: '0.68rem', fontWeight: 700, color: '#D94848', textTransform: 'uppercase' }}>Critical Alerts</div>
            </div>
            <div style={{ padding: '8px 14px', borderRadius: '8px', backgroundColor: '#FEF6E9', border: '1px solid #FFE8B8', textAlign: 'center' }}>
              <div style={{ fontSize: '1.2rem', fontWeight: 900, color: '#B47814' }}>{highCount}</div>
              <div style={{ fontSize: '0.68rem', fontWeight: 700, color: '#B47814', textTransform: 'uppercase' }}>High Priority</div>
            </div>
            <div style={{ padding: '8px 14px', borderRadius: '8px', backgroundColor: '#E6F6F0', border: '1px solid rgba(22, 122, 91, 0.3)', textAlign: 'center' }}>
              <div style={{ fontSize: '1.2rem', fontWeight: 900, color: '#167A5B' }}>{resolvedCount}</div>
              <div style={{ fontSize: '0.68rem', fontWeight: 700, color: '#167A5B', textTransform: 'uppercase' }}>Resolved</div>
            </div>
          </div>
        </div>

        {/* Filter Controls */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px', flexWrap: 'wrap' }}>
          <div style={{
            flex: '1 1 240px',
            display: 'flex',
            alignItems: 'center',
            gap: '8px',
            backgroundColor: '#F8FAF7',
            border: '1px solid #DEE8E3',
            borderRadius: '8px',
            padding: '8px 12px'
          }}>
            <Search size={16} color="#7E948C" />
            <input
              type="text"
              placeholder="Search by issue title, parcel ID, evidence..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{ border: 'none', background: 'transparent', outline: 'none', width: '100%', fontSize: '0.85rem' }}
            />
          </div>

          <select
            value={selectedSeverity}
            onChange={(e) => setSelectedSeverity(e.target.value)}
            style={{ padding: '8px 12px', borderRadius: '8px', border: '1px solid #DEE8E3', backgroundColor: '#F8FAF7', fontSize: '0.82rem', fontWeight: 600 }}
          >
            <option value="ALL">All Severities</option>
            <option value="CRITICAL">Critical Severity</option>
            <option value="HIGH">High Severity</option>
            <option value="MEDIUM">Medium Severity</option>
            <option value="LOW">Low Severity</option>
          </select>

          <select
            value={selectedDept}
            onChange={(e) => setSelectedDept(e.target.value)}
            style={{ padding: '8px 12px', borderRadius: '8px', border: '1px solid #DEE8E3', backgroundColor: '#F8FAF7', fontSize: '0.82rem', fontWeight: 600 }}
          >
            <option value="ALL">All Departments</option>
            <option value="SURVEY">Survey & Land Records</option>
            <option value="REGISTRATION">Registration (SRO)</option>
            <option value="PLANNING">Urban Planning (DTCP)</option>
            <option value="LEGAL">Courts & Judiciary</option>
            <option value="TAX">Municipal Tax</option>
            <option value="REVENUE">Revenue Department</option>
          </select>

          <select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
            style={{ padding: '8px 12px', borderRadius: '8px', border: '1px solid #DEE8E3', backgroundColor: '#F8FAF7', fontSize: '0.82rem', fontWeight: 600 }}
          >
            <option value="ALL">All Statuses</option>
            <option value="OPEN">Open Conflicts</option>
            <option value="RESOLVED">Resolved</option>
            <option value="UNDER_REVIEW">Under Review</option>
          </select>
        </div>
      </div>

      {/* Issues List */}
      <div style={{ display: 'flex', flexDirection: 'column', gap: '14px' }}>
        {filteredIssues.length === 0 ? (
          <div className="card" style={{ padding: '30px', textAlign: 'center', color: '#52665F' }}>
            No conflicts found matching current filters.
          </div>
        ) : (
          filteredIssues.map(issue => {
            const isCrit = issue.severity === 'CRITICAL';
            const isResolved = issue.status === 'RESOLVED';
            const parcel = parcels.find(p => p.id === issue.parcelId);

            return (
              <div 
                key={issue.id}
                className="card"
                style={{
                  padding: '20px',
                  borderLeft: isResolved ? '4px solid #167A5B' : isCrit ? '4px solid #D94848' : '4px solid #D99B2B',
                  display: 'flex',
                  flexDirection: 'column',
                  gap: '12px'
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '10px' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <span className={`badge ${isResolved ? 'badge-verified' : isCrit ? 'badge-critical' : 'badge-review'}`}>
                      {issue.severity}
                    </span>
                    <span style={{ fontSize: '0.75rem', fontWeight: 700, color: '#52665F', backgroundColor: '#EEF3F0', padding: '3px 8px', borderRadius: '4px' }}>
                      {issue.department}
                    </span>
                    <span style={{ fontSize: '0.75rem', color: '#7E948C', fontFamily: 'var(--font-mono)' }}>
                      {issue.id}
                    </span>
                  </div>

                  <span style={{ fontSize: '0.75rem', color: '#7E948C' }}>
                    Detected: {issue.detectedDate}
                  </span>
                </div>

                <div>
                  <h3 style={{ fontSize: '1.05rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
                    {issue.title}
                  </h3>
                  <p style={{ fontSize: '0.85rem', color: '#52665F', lineHeight: 1.5 }}>
                    {issue.description}
                  </p>
                </div>

                {/* Evidence snippet */}
                <div style={{
                  backgroundColor: '#F8FAF7',
                  padding: '10px 14px',
                  borderRadius: '8px',
                  border: '1px solid #DEE8E3',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '16px',
                  flexWrap: 'wrap',
                  fontSize: '0.78rem'
                }}>
                  {Object.entries(issue.evidence).slice(0, 3).map(([k, v]) => (
                    <div key={k}>
                      <span style={{ color: '#7E948C', fontWeight: 600 }}>{k}:</span>{' '}
                      <strong style={{ color: '#192A24' }}>{v}</strong>
                    </div>
                  ))}
                </div>

                {isResolved && (
                  <div style={{
                    backgroundColor: '#E6F6F0',
                    padding: '8px 12px',
                    borderRadius: '6px',
                    fontSize: '0.78rem',
                    color: '#167A5B'
                  }}>
                    ✓ Resolved: {issue.resolutionNotes} (by {issue.resolvedBy})
                  </div>
                )}

                {/* Action Bar */}
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', borderTop: '1px solid #F0F4F2', paddingTop: '12px' }}>
                  <div style={{ fontSize: '0.78rem', color: '#52665F' }}>
                    Linked Parcel: <strong>{parcel?.surveyNumber || issue.parcelId}</strong> ({parcel?.village}, {parcel?.taluk})
                  </div>

                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    {!isResolved && (
                      <button
                        onClick={() => setResolvingIssueId(issue.id)}
                        className="btn btn-primary"
                        style={{ padding: '5px 12px', fontSize: '0.78rem' }}
                      >
                        <UserCheck size={14} /> Resolve Conflict
                      </button>
                    )}
                    <button
                      onClick={() => {
                        setActiveParcelId(issue.parcelId);
                        setScreenState('PARCEL_DETAIL');
                      }}
                      className="btn btn-secondary"
                      style={{ padding: '5px 12px', fontSize: '0.78rem' }}
                    >
                      Inspect Parcel <ArrowRight size={14} />
                    </button>
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>

      {/* Quick Resolve Modal */}
      {resolvingIssueId && (
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
            onSubmit={handleQuickResolve}
            style={{
              width: '500px',
              maxWidth: '100%',
              backgroundColor: '#FFFFFF',
              borderRadius: '14px',
              border: '1px solid #DEE8E3',
              boxShadow: 'var(--shadow-lg)',
              overflow: 'hidden'
            }}
          >
            <div style={{ padding: '16px 20px', backgroundColor: '#0F543E', color: 'white' }}>
              <h3 style={{ fontSize: '1.05rem', fontWeight: 800 }}>Execute Officer Dispute Resolution</h3>
              <div style={{ fontSize: '0.75rem', color: '#A3C7B9' }}>Issue ID: {resolvingIssueId}</div>
            </div>

            <div style={{ padding: '20px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
              <div>
                <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#192A24', display: 'block', marginBottom: '4px' }}>
                  Resolution Action Category
                </label>
                <select
                  value={reason}
                  onChange={(e) => setReason(e.target.value)}
                  style={{ width: '100%', padding: '8px', borderRadius: '8px', border: '1px solid #DEE8E3', fontSize: '0.85rem' }}
                >
                  <option value="DGPS Resurvey Verification">DGPS Field Joint Resurvey Verified</option>
                  <option value="Title Clearance & Patta Synchronized">Title Clearance & SRO Deed Mutation Cleared</option>
                  <option value="Commercial Regularization Penalty Paid">Commercial Regularization Penalty Paid & DTCP Clear</option>
                  <option value="Judicial Certified Copy Ingestion">Judicial Certified Copy NJDG Ingestion</option>
                </select>
              </div>

              <div>
                <label style={{ fontSize: '0.78rem', fontWeight: 700, color: '#192A24', display: 'block', marginBottom: '4px' }}>
                  Verification Order & Field Officer Notes
                </label>
                <textarea
                  rows={3}
                  value={notes}
                  onChange={(e) => setNotes(e.target.value)}
                  required
                  style={{ width: '100%', padding: '8px', borderRadius: '8px', border: '1px solid #DEE8E3', fontSize: '0.85rem', fontFamily: 'inherit' }}
                />
              </div>
            </div>

            <div style={{ padding: '12px 20px', backgroundColor: '#F8FAF7', borderTop: '1px solid #DEE8E3', display: 'flex', justifyContent: 'flex-end', gap: '10px' }}>
              <button type="button" onClick={() => setResolvingIssueId(null)} className="btn btn-secondary">
                Cancel
              </button>
              <button type="submit" className="btn btn-primary">
                Confirm Clearance
              </button>
            </div>
          </form>
        </div>
      )}
    </div>
  );
};
