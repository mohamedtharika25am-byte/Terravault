import React, { useState } from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { 
  FileText, 
  Printer, 
  Download, 
  ShieldCheck, 
  CheckCircle2, 
  QrCode, 
  Building2, 
  Award,
  AlertTriangle,
  FileCheck
} from 'lucide-react';

export const ReportsView: React.FC = () => {
  const { parcels, reportInitialParcelId, setReportInitialParcelId, userProfile } = useTerravault();

  const [selectedParcelId, setSelectedParcelId] = useState<string>(reportInitialParcelId || 'TN-COI-00123-0456');

  const parcel = parcels.find(p => p.id === selectedParcelId) || parcels[0];
  const isVerified = parcel?.status === 'VERIFIED';
  const issueCount = parcel?.issues.length || 0;

  const handlePrint = () => {
    window.print();
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
      {/* Controls Bar (hidden during print) */}
      <div className="card no-print" style={{ padding: '16px 20px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '12px' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <span style={{ fontSize: '0.85rem', fontWeight: 700, color: '#192A24' }}>
            Select Land Parcel:
          </span>
          <select
            value={selectedParcelId}
            onChange={(e) => setSelectedParcelId(e.target.value)}
            style={{
              padding: '8px 12px',
              borderRadius: '8px',
              border: '1px solid #DEE8E3',
              backgroundColor: '#F8FAF7',
              fontSize: '0.85rem',
              fontWeight: 600,
              minWidth: '280px'
            }}
          >
            {parcels.map(p => (
              <option key={p.id} value={p.id}>
                S.No {p.surveyNumber} - {p.ownerName} ({p.village}) [{p.status}]
              </option>
            ))}
          </select>
        </div>

        <div style={{ display: 'flex', gap: '10px' }}>
          <button
            onClick={handlePrint}
            className="btn btn-primary"
            style={{ gap: '6px' }}
          >
            <Printer size={16} /> Print / Save PDF Certificate
          </button>
        </div>
      </div>

      {/* Official Government Certificate (Printable) */}
      <div 
        className="card report-page"
        style={{
          padding: '40px 48px',
          maxWidth: '900px',
          margin: '0 auto',
          width: '100%',
          backgroundColor: '#FFFFFF',
          border: '2px solid #DEE8E3',
          boxShadow: 'var(--shadow-md)',
          position: 'relative'
        }}
      >
        {/* Certificate Watermark / Header */}
        <div style={{ textAlign: 'center', borderBottom: '2px solid #0F543E', paddingBottom: '20px', marginBottom: '24px' }}>
          <div style={{ fontSize: '0.78rem', fontWeight: 800, letterSpacing: '2px', color: '#52665F', textTransform: 'uppercase' }}>
            GOVERNMENT OF TAMIL NADU • REVENUE & DISASTER MANAGEMENT DEPARTMENT
          </div>
          <h1 style={{ fontSize: '1.4rem', fontWeight: 900, color: '#0F543E', margin: '6px 0 2px 0' }}>
            UNIFIED CADASTRAL LAND INTELLIGENCE & VERIFICATION CERTIFICATE
          </h1>
          <div style={{ fontSize: '0.75rem', color: '#7E948C', fontWeight: 600 }}>
            Issued under Tamil Nadu Digital Public Infrastructure Framework (Terravault v2.0)
          </div>
        </div>

        {/* Certificate Reference & QR code header */}
        <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '24px', backgroundColor: '#F8FAF7', padding: '16px', borderRadius: '10px', border: '1px solid #DEE8E3' }}>
          <div>
            <div style={{ fontSize: '0.75rem', color: '#7E948C' }}>Certificate Reference No:</div>
            <div style={{ fontSize: '0.95rem', fontWeight: 800, color: '#192A24', fontFamily: 'var(--font-mono)' }}>
              TN-DPI-CBE-{parcel.id.slice(-8)}-2026
            </div>
            <div style={{ fontSize: '0.75rem', color: '#52665F', marginTop: '4px' }}>
              Generated Date: <strong>{new Date().toLocaleDateString('en-GB')} {new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</strong>
            </div>
          </div>

          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <div style={{
              width: '54px',
              height: '54px',
              backgroundColor: '#FFFFFF',
              border: '1px solid #DEE8E3',
              borderRadius: '6px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center'
            }}>
              <QrCode size={40} color="#0F543E" />
            </div>
            <div style={{ fontSize: '0.68rem', color: '#52665F', maxWidth: '100px' }}>
              Scan to verify cryptographic signature
            </div>
          </div>
        </div>

        {/* Section 1: Land Parcel Dossier */}
        <div style={{ marginBottom: '24px' }}>
          <h2 style={{ fontSize: '0.92rem', fontWeight: 800, color: '#0F543E', borderBottom: '1px solid #DEE8E3', paddingBottom: '6px', marginBottom: '12px' }}>
            1. CADASTRAL LAND PROFILE
          </h2>
          <table style={{ width: '100%', fontSize: '0.82rem', borderCollapse: 'collapse' }}>
            <tbody>
              <tr style={{ borderBottom: '1px solid #F0F4F2' }}>
                <td style={{ padding: '6px 0', color: '#52665F', width: '25%' }}>Survey Number:</td>
                <td style={{ padding: '6px 0', fontWeight: 800, width: '25%' }}>{parcel.surveyNumber} (Sub-div: {parcel.subDivision})</td>
                <td style={{ padding: '6px 0', color: '#52665F', width: '25%' }}>Cadastral ID:</td>
                <td style={{ padding: '6px 0', fontWeight: 700, width: '25%', fontFamily: 'var(--font-mono)' }}>{parcel.id}</td>
              </tr>
              <tr style={{ borderBottom: '1px solid #F0F4F2' }}>
                <td style={{ padding: '6px 0', color: '#52665F' }}>Registered Owner:</td>
                <td style={{ padding: '6px 0', fontWeight: 800 }}>{parcel.ownerName}</td>
                <td style={{ padding: '6px 0', color: '#52665F' }}>Prior Registered Owner:</td>
                <td style={{ padding: '6px 0' }}>{parcel.previousOwner}</td>
              </tr>
              <tr style={{ borderBottom: '1px solid #F0F4F2' }}>
                <td style={{ padding: '6px 0', color: '#52665F' }}>Jurisdiction:</td>
                <td style={{ padding: '6px 0' }}>{parcel.village}, {parcel.taluk}</td>
                <td style={{ padding: '6px 0', color: '#52665F' }}>District / State:</td>
                <td style={{ padding: '6px 0' }}>Coimbatore, Tamil Nadu</td>
              </tr>
              <tr style={{ borderBottom: '1px solid #F0F4F2' }}>
                <td style={{ padding: '6px 0', color: '#52665F' }}>Revenue Declared Area:</td>
                <td style={{ padding: '6px 0', fontWeight: 800 }}>{parcel.areaHectares} Hectares</td>
                <td style={{ padding: '6px 0', color: '#52665F' }}>GIS Computed Area:</td>
                <td style={{ padding: '6px 0', fontWeight: 800 }}>{parcel.gisCalculatedArea} Hectares</td>
              </tr>
              <tr>
                <td style={{ padding: '6px 0', color: '#52665F' }}>Approved Land Use:</td>
                <td style={{ padding: '6px 0' }}>{parcel.declaredLandUse}</td>
                <td style={{ padding: '6px 0', color: '#52665F' }}>Satellite Detected Use:</td>
                <td style={{ padding: '6px 0' }}>{parcel.gisDetectedLandUse}</td>
              </tr>
            </tbody>
          </table>
        </div>

        {/* Section 2: 6-Department Verification Ledger */}
        <div style={{ marginBottom: '24px' }}>
          <h2 style={{ fontSize: '0.92rem', fontWeight: 800, color: '#0F543E', borderBottom: '1px solid #DEE8E3', paddingBottom: '6px', marginBottom: '12px' }}>
            2. CROSS-DEPARTMENT DIGITAL VERIFICATION LEDGER
          </h2>
          <table style={{ width: '100%', fontSize: '0.8rem', borderCollapse: 'collapse', textAlign: 'left' }}>
            <thead>
              <tr style={{ backgroundColor: '#F8FAF7', borderBottom: '1px solid #DEE8E3', color: '#52665F', fontWeight: 800 }}>
                <th style={{ padding: '8px 10px' }}>Department</th>
                <th style={{ padding: '8px 10px' }}>Record Reference</th>
                <th style={{ padding: '8px 10px' }}>Synchronized Status</th>
                <th style={{ padding: '8px 10px' }}>Verification Result</th>
              </tr>
            </thead>
            <tbody>
              {parcel.departmentSources.map(s => (
                <tr key={s.department} style={{ borderBottom: '1px solid #F0F4F2' }}>
                  <td style={{ padding: '8px 10px', fontWeight: 700 }}>{s.department}</td>
                  <td style={{ padding: '8px 10px', fontFamily: 'var(--font-mono)', fontSize: '0.75rem' }}>{s.recordNumber}</td>
                  <td style={{ padding: '8px 10px' }}>
                    <span style={{
                      padding: '2px 6px',
                      borderRadius: '4px',
                      fontSize: '0.7rem',
                      fontWeight: 800,
                      backgroundColor: s.status === 'CONFLICT' ? '#FEEFEF' : '#E6F6F0',
                      color: s.status === 'CONFLICT' ? '#D94848' : '#167A5B'
                    }}>
                      {s.status}
                    </span>
                  </td>
                  <td style={{ padding: '8px 10px', fontSize: '0.75rem', color: '#52665F' }}>{s.details}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* Section 3: Discrepancy & Dispute Audit */}
        <div style={{ marginBottom: '28px' }}>
          <h2 style={{ fontSize: '0.92rem', fontWeight: 800, color: '#0F543E', borderBottom: '1px solid #DEE8E3', paddingBottom: '6px', marginBottom: '12px' }}>
            3. DISPUTE & LITIGATION AUDIT SUMMARY
          </h2>
          <div style={{ fontSize: '0.8rem', color: '#192A24', lineHeight: 1.5, backgroundColor: '#F8FAF7', padding: '12px 16px', borderRadius: '8px', border: '1px solid #DEE8E3' }}>
            <div>• Court Case Status: <strong>{parcel.courtCaseStatus}</strong></div>
            <div>• Encumbrance Certificate Status: <strong>{parcel.encumbranceStatus}</strong></div>
            <div>• Cadastral Boundary Status: <strong>{parcel.boundaryStatus}</strong></div>
            <div>• Property Tax Status: <strong>{parcel.taxStatus}</strong></div>
          </div>
        </div>

        {/* Signatures & Seal */}
        <div style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'flex-end',
          paddingTop: '24px',
          borderTop: '2px solid #DEE8E3'
        }}>
          <div>
            <div style={{
              width: '80px',
              height: '80px',
              borderRadius: '50%',
              border: '2px dashed #167A5B',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              color: '#167A5B',
              fontSize: '0.65rem',
              fontWeight: 800,
              textAlign: 'center',
              padding: '6px'
            }}>
              GOVT SEAL • COIMBATORE
            </div>
          </div>

          <div style={{ textAlign: 'right' }}>
            <div style={{ fontSize: '0.78rem', color: '#167A5B', fontWeight: 800, fontFamily: 'var(--font-mono)' }}>
              [DIGITALLY SIGNED VIA TNeGA e-SIGN]
            </div>
            <div style={{ fontWeight: 800, fontSize: '0.95rem', color: '#192A24', marginTop: '4px' }}>
              {userProfile.name}
            </div>
            <div style={{ fontSize: '0.75rem', color: '#52665F' }}>
              {userProfile.designation}
            </div>
            <div style={{ fontSize: '0.7rem', color: '#7E948C' }}>
              District Collectorate, Coimbatore
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
