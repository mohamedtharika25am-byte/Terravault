import React, { useState } from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { Parcel, ParcelStatus, LandUseType } from '../types/parcel';
import { 
  Search, 
  Filter, 
  MapPin, 
  Layers, 
  ArrowRight, 
  Download, 
  LayoutGrid, 
  Table as TableIcon,
  ShieldCheck,
  AlertTriangle,
  FileText
} from 'lucide-react';

export const ParcelsListView: React.FC = () => {
  const { parcels, setActiveParcelId, setScreenState, setMapInitialParcelId, setSelectedNav, setReportInitialParcelId } = useTerravault();

  const [searchQuery, setSearchQuery] = useState('');
  const [selectedStatus, setSelectedStatus] = useState<string>('ALL');
  const [selectedTaluk, setSelectedTaluk] = useState<string>('ALL');
  const [selectedLandUse, setSelectedLandUse] = useState<string>('ALL');
  const [viewMode, setViewMode] = useState<'GRID' | 'TABLE'>('TABLE');
  const [sortBy, setSortBy] = useState<'SURVEY' | 'RISK_HIGH' | 'AREA_HIGH'>('RISK_HIGH');

  // Unique filter lists
  const taluks = Array.from(new Set(parcels.map(p => p.taluk)));
  const landUses = Array.from(new Set(parcels.map(p => p.currentLandUse)));

  const filteredParcels = parcels.filter(p => {
    const q = searchQuery.toLowerCase().trim();
    const matchQuery = q === '' ||
      p.id.toLowerCase().includes(q) ||
      p.surveyNumber.toLowerCase().includes(q) ||
      p.ownerName.toLowerCase().includes(q) ||
      p.village.toLowerCase().includes(q) ||
      p.taluk.toLowerCase().includes(q) ||
      p.deedNumber.toLowerCase().includes(q);

    const matchStatus = selectedStatus === 'ALL' || p.status === selectedStatus;
    const matchTaluk = selectedTaluk === 'ALL' || p.taluk === selectedTaluk;
    const matchLandUse = selectedLandUse === 'ALL' || p.currentLandUse === selectedLandUse;

    return matchQuery && matchStatus && matchTaluk && matchLandUse;
  }).sort((a, b) => {
    if (sortBy === 'RISK_HIGH') return b.riskScore - a.riskScore;
    if (sortBy === 'AREA_HIGH') return b.areaHectares - a.areaHectares;
    return a.surveyNumber.localeCompare(b.surveyNumber);
  });

  const exportCSV = () => {
    const headers = ['Parcel ID', 'Survey Number', 'Owner Name', 'Taluk', 'Village', 'Revenue Area (ha)', 'GIS Area (ha)', 'Status', 'Risk Score', 'Court Status'];
    const rows = filteredParcels.map(p => [
      p.id,
      p.surveyNumber,
      `"${p.ownerName}"`,
      p.taluk,
      p.village,
      p.areaHectares,
      p.gisCalculatedArea,
      p.status,
      p.riskScore,
      `"${p.courtCaseStatus}"`
    ]);

    const csvContent = 'data:text/csv;charset=utf-8,' + [headers.join(','), ...rows.map(r => r.join(','))].join('\n');
    const encodedUri = encodeURI(csvContent);
    const link = document.createElement('a');
    link.setAttribute('href', encodedUri);
    link.setAttribute('download', `Terravault_Cadastral_Registry_${new Date().toISOString().slice(0,10)}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Header & Controls Bar */}
      <div className="card" style={{ padding: '20px' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px', marginBottom: '16px' }}>
          <div>
            <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#192A24' }}>
              Cadastral Land Parcel Registry
            </h2>
            <div style={{ fontSize: '0.8rem', color: '#52665F' }}>
              Showing {filteredParcels.length} of {parcels.length} digital land records
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <button
              onClick={exportCSV}
              className="btn btn-secondary"
              style={{ fontSize: '0.8rem', padding: '6px 12px' }}
            >
              <Download size={14} color="#167A5B" /> Export Registry (CSV)
            </button>

            <div style={{ display: 'flex', backgroundColor: '#F8FAF7', border: '1px solid #DEE8E3', borderRadius: '8px', padding: '2px' }}>
              <button
                onClick={() => setViewMode('TABLE')}
                style={{
                  border: 'none',
                  background: viewMode === 'TABLE' ? '#FFFFFF' : 'transparent',
                  padding: '6px 10px',
                  borderRadius: '6px',
                  cursor: 'pointer',
                  boxShadow: viewMode === 'TABLE' ? 'var(--shadow-sm)' : 'none'
                }}
              >
                <TableIcon size={16} color={viewMode === 'TABLE' ? '#167A5B' : '#7E948C'} />
              </button>
              <button
                onClick={() => setViewMode('GRID')}
                style={{
                  border: 'none',
                  background: viewMode === 'GRID' ? '#FFFFFF' : 'transparent',
                  padding: '6px 10px',
                  borderRadius: '6px',
                  cursor: 'pointer',
                  boxShadow: viewMode === 'GRID' ? 'var(--shadow-sm)' : 'none'
                }}
              >
                <LayoutGrid size={16} color={viewMode === 'GRID' ? '#167A5B' : '#7E948C'} />
              </button>
            </div>
          </div>
        </div>

        {/* Filters Row */}
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px', flexWrap: 'wrap' }}>
          {/* Search */}
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
              placeholder="Search survey #, owner, village, deed..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              style={{
                border: 'none',
                background: 'transparent',
                outline: 'none',
                width: '100%',
                fontSize: '0.85rem',
                fontFamily: 'inherit'
              }}
            />
          </div>

          {/* Status Filter */}
          <select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
            style={{
              padding: '8px 12px',
              borderRadius: '8px',
              border: '1px solid #DEE8E3',
              backgroundColor: '#F8FAF7',
              fontSize: '0.82rem',
              fontWeight: 600,
              color: '#192A24',
              cursor: 'pointer'
            }}
          >
            <option value="ALL">All Statuses</option>
            <option value="VERIFIED">Verified (100% Match)</option>
            <option value="NEEDS_REVIEW">Needs Review</option>
            <option value="CRITICAL_ISSUE">Critical Issue</option>
            <option value="UNDER_VERIFICATION">Under Verification</option>
          </select>

          {/* Taluk Filter */}
          <select
            value={selectedTaluk}
            onChange={(e) => setSelectedTaluk(e.target.value)}
            style={{
              padding: '8px 12px',
              borderRadius: '8px',
              border: '1px solid #DEE8E3',
              backgroundColor: '#F8FAF7',
              fontSize: '0.82rem',
              fontWeight: 600,
              color: '#192A24',
              cursor: 'pointer'
            }}
          >
            <option value="ALL">All Taluks</option>
            {taluks.map(t => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>

          {/* Sort Filter */}
          <select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value as any)}
            style={{
              padding: '8px 12px',
              borderRadius: '8px',
              border: '1px solid #DEE8E3',
              backgroundColor: '#F8FAF7',
              fontSize: '0.82rem',
              fontWeight: 600,
              color: '#192A24',
              cursor: 'pointer'
            }}
          >
            <option value="RISK_HIGH">Highest Risk First</option>
            <option value="AREA_HIGH">Largest Area First</option>
            <option value="SURVEY">Survey Number (A-Z)</option>
          </select>
        </div>
      </div>

      {/* Main Content: Table or Grid */}
      {viewMode === 'TABLE' ? (
        <div className="card" style={{ padding: 0, overflow: 'hidden' }}>
          <div style={{ overflowX: 'auto' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse', textAlign: 'left', fontSize: '0.85rem' }}>
              <thead>
                <tr style={{ backgroundColor: '#F8FAF7', borderBottom: '1px solid #DEE8E3', color: '#52665F', fontSize: '0.75rem', textTransform: 'uppercase', fontWeight: 800 }}>
                  <th style={{ padding: '12px 16px' }}>Survey No & ID</th>
                  <th style={{ padding: '12px 16px' }}>Owner & Location</th>
                  <th style={{ padding: '12px 16px' }}>Area (Rev vs GIS)</th>
                  <th style={{ padding: '12px 16px' }}>Land Use</th>
                  <th style={{ padding: '12px 16px' }}>Status & Integrity</th>
                  <th style={{ padding: '12px 16px' }}>Litigation Status</th>
                  <th style={{ padding: '12px 16px', textAlign: 'right' }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredParcels.map(p => {
                  const areaDiff = Math.abs(p.areaHectares - p.gisCalculatedArea);
                  const isMismatch = areaDiff > 0.03;

                  return (
                    <tr 
                      key={p.id}
                      style={{ borderBottom: '1px solid #F0F4F2', transition: 'background 0.15s ease', cursor: 'pointer' }}
                      onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#F8FAF7'}
                      onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
                      onClick={() => {
                        setActiveParcelId(p.id);
                        setScreenState('PARCEL_DETAIL');
                      }}
                    >
                      <td style={{ padding: '14px 16px' }}>
                        <div style={{ fontWeight: 800, color: '#192A24', fontSize: '0.92rem' }}>
                          S.No {p.surveyNumber}
                        </div>
                        <div style={{ fontSize: '0.7rem', color: '#7E948C', fontFamily: 'var(--font-mono)' }}>
                          {p.id}
                        </div>
                      </td>

                      <td style={{ padding: '14px 16px' }}>
                        <div style={{ fontWeight: 700, color: '#192A24' }}>{p.ownerName}</div>
                        <div style={{ fontSize: '0.75rem', color: '#52665F' }}>{p.village}, {p.taluk}</div>
                      </td>

                      <td style={{ padding: '14px 16px' }}>
                        <div style={{ fontWeight: 700, color: isMismatch ? '#D94848' : '#192A24' }}>
                          {p.areaHectares} ha
                        </div>
                        <div style={{ fontSize: '0.72rem', color: isMismatch ? '#D94848' : '#167A5B' }}>
                          GIS: {p.gisCalculatedArea} ha {isMismatch && `(Δ ${areaDiff.toFixed(2)})`}
                        </div>
                      </td>

                      <td style={{ padding: '14px 16px' }}>
                        <span style={{ fontSize: '0.75rem', backgroundColor: '#EEF3F0', padding: '3px 8px', borderRadius: '6px', fontWeight: 600, color: '#192A24' }}>
                          {p.currentLandUse.replace('_', ' ')}
                        </span>
                      </td>

                      <td style={{ padding: '14px 16px' }}>
                        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                          <span className={`badge ${p.status === 'VERIFIED' ? 'badge-verified' : p.status === 'CRITICAL_ISSUE' ? 'badge-critical' : 'badge-review'}`}>
                            {p.status.replace('_', ' ')}
                          </span>
                          <span style={{ fontSize: '0.75rem', fontWeight: 700, color: p.riskScore > 50 ? '#D94848' : '#167A5B' }}>
                            {p.verificationPercent}%
                          </span>
                        </div>
                      </td>

                      <td style={{ padding: '14px 16px' }}>
                        <span style={{
                          fontSize: '0.75rem',
                          fontWeight: 600,
                          color: p.courtCaseStatus.includes('No') ? '#167A5B' : '#D94848'
                        }}>
                          {p.courtCaseStatus}
                        </span>
                      </td>

                      <td style={{ padding: '14px 16px', textAlign: 'right' }}>
                        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: '6px' }}>
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              setMapInitialParcelId(p.id);
                              setSelectedNav('GIS_MAP');
                            }}
                            className="btn btn-secondary"
                            style={{ padding: '4px 8px', fontSize: '0.72rem' }}
                            title="Inspect on GIS Map"
                          >
                            <MapPin size={13} color="#167A5B" />
                          </button>
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              setReportInitialParcelId(p.id);
                              setSelectedNav('REPORTS');
                            }}
                            className="btn btn-secondary"
                            style={{ padding: '4px 8px', fontSize: '0.72rem' }}
                            title="Generate Official Certificate"
                          >
                            <FileText size={13} color="#167A5B" />
                          </button>
                          <button
                            onClick={(e) => {
                              e.stopPropagation();
                              setActiveParcelId(p.id);
                              setScreenState('PARCEL_DETAIL');
                            }}
                            className="btn btn-primary"
                            style={{ padding: '4px 10px', fontSize: '0.72rem' }}
                          >
                            Dossier <ArrowRight size={12} />
                          </button>
                        </div>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        /* Grid Mode */
        <div style={{
          display: 'grid',
          gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))',
          gap: '16px'
        }}>
          {filteredParcels.map(p => (
            <div 
              key={p.id}
              className="card card-interactive"
              onClick={() => {
                setActiveParcelId(p.id);
                setScreenState('PARCEL_DETAIL');
              }}
              style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}
            >
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <span style={{ fontWeight: 800, fontSize: '1rem', color: '#192A24' }}>
                  S.No {p.surveyNumber}
                </span>
                <span className={`badge ${p.status === 'VERIFIED' ? 'badge-verified' : p.status === 'CRITICAL_ISSUE' ? 'badge-critical' : 'badge-review'}`}>
                  {p.status.replace('_', ' ')}
                </span>
              </div>

              <div>
                <div style={{ fontWeight: 700, color: '#192A24' }}>{p.ownerName}</div>
                <div style={{ fontSize: '0.78rem', color: '#52665F' }}>{p.village}, {p.taluk}</div>
              </div>

              <div style={{
                display: 'grid',
                gridTemplateColumns: '1fr 1fr',
                gap: '8px',
                backgroundColor: '#F8FAF7',
                padding: '8px 12px',
                borderRadius: '8px',
                fontSize: '0.75rem'
              }}>
                <div>
                  <span style={{ color: '#7E948C' }}>Area:</span> <strong>{p.areaHectares} ha</strong>
                </div>
                <div>
                  <span style={{ color: '#7E948C' }}>Risk:</span> <strong style={{ color: p.riskScore > 50 ? '#D94848' : '#167A5B' }}>{p.riskScore}/100</strong>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginTop: 'auto', paddingTop: '8px', borderTop: '1px solid #F0F4F2' }}>
                <span style={{ fontSize: '0.72rem', color: '#7E948C' }}>Deed: {p.deedNumber}</span>
                <span style={{ fontSize: '0.75rem', color: '#167A5B', fontWeight: 700, display: 'flex', alignItems: 'center', gap: '4px' }}>
                  Open Dossier <ArrowRight size={12} />
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
