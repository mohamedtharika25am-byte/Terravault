import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { 
  BarChart3, 
  PieChart, 
  TrendingUp, 
  ShieldAlert, 
  MapPin, 
  Sparkles, 
  CheckCircle2,
  AlertTriangle,
  Building2
} from 'lucide-react';

export const AnalyticsView: React.FC = () => {
  const { parcels, issues } = useTerravault();

  // Aggregate issues by department
  const deptCounts: Record<string, number> = {};
  issues.forEach(i => {
    deptCounts[i.department] = (deptCounts[i.department] || 0) + 1;
  });

  // Aggregate by severity
  const severityCounts: Record<string, number> = {
    CRITICAL: issues.filter(i => i.severity === 'CRITICAL').length,
    HIGH: issues.filter(i => i.severity === 'HIGH').length,
    MEDIUM: issues.filter(i => i.severity === 'MEDIUM').length,
    LOW: issues.filter(i => i.severity === 'LOW').length
  };

  // Aggregate by Taluk
  const talukStats: Record<string, { count: number; highRiskCount: number; totalArea: number }> = {};
  parcels.forEach(p => {
    if (!talukStats[p.taluk]) {
      talukStats[p.taluk] = { count: 0, highRiskCount: 0, totalArea: 0 };
    }
    talukStats[p.taluk].count++;
    if (p.riskScore > 50) talukStats[p.taluk].highRiskCount++;
    talukStats[p.taluk].totalArea += p.areaHectares;
  });

  // Land use breakdown
  const landUseCounts: Record<string, number> = {};
  parcels.forEach(p => {
    landUseCounts[p.currentLandUse] = (landUseCounts[p.currentLandUse] || 0) + 1;
  });

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Top Banner */}
      <div className="card" style={{ padding: '24px', backgroundColor: '#0F543E', color: '#FFFFFF' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px', marginBottom: '8px' }}>
          <Sparkles size={20} color="#D99B2B" />
          <h2 style={{ fontSize: '1.3rem', fontWeight: 800 }}>
            Automated Land Intelligence & AI Risk Engine
          </h2>
        </div>
        <p style={{ fontSize: '0.88rem', color: '#E8F4EE', maxWidth: '800px', lineHeight: 1.5 }}>
          Continuous multi-spectral and cross-department anomaly scanning detects <strong>boundary shrinkage, double-patta issuance, eco-buffer encroachment, and unauthorized conversion</strong> across the Coimbatore district grid.
        </p>
      </div>

      {/* 4 Analytics Visual Cards Grid */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(320px, 1fr))', gap: '20px' }}>
        {/* Issues by Department */}
        <div className="card" style={{ padding: '20px' }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
            Discrepancies by Department
          </h3>
          <div style={{ fontSize: '0.78rem', color: '#52665F', marginBottom: '16px' }}>
            Cross-department root cause breakdown
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            {Object.entries(deptCounts).map(([dept, count]) => {
              const pct = ((count / issues.length) * 100).toFixed(0);
              return (
                <div key={dept}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8rem', fontWeight: 700, marginBottom: '4px' }}>
                    <span>{dept}</span>
                    <span style={{ color: '#167A5B' }}>{count} issues ({pct}%)</span>
                  </div>
                  <div style={{ height: '8px', backgroundColor: '#EEF3F0', borderRadius: '4px', overflow: 'hidden' }}>
                    <div style={{ width: `${pct}%`, height: '100%', backgroundColor: '#167A5B', borderRadius: '4px' }}></div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Severity Distribution */}
        <div className="card" style={{ padding: '20px' }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
            Issue Severity Distribution
          </h3>
          <div style={{ fontSize: '0.78rem', color: '#52665F', marginBottom: '16px' }}>
            Statutory risk weight allocation
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
            {Object.entries(severityCounts).map(([sev, count]) => {
              const color = sev === 'CRITICAL' ? '#D94848' : sev === 'HIGH' ? '#B47814' : sev === 'MEDIUM' ? '#D99B2B' : '#2E7BB4';
              const pct = issues.length > 0 ? ((count / issues.length) * 100).toFixed(0) : '0';

              return (
                <div key={sev}>
                  <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.8rem', fontWeight: 700, marginBottom: '4px' }}>
                    <span style={{ color }}>{sev}</span>
                    <span>{count} ({pct}%)</span>
                  </div>
                  <div style={{ height: '8px', backgroundColor: '#EEF3F0', borderRadius: '4px', overflow: 'hidden' }}>
                    <div style={{ width: `${pct}%`, height: '100%', backgroundColor: color, borderRadius: '4px' }}></div>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Taluk Level Risk Ranking */}
        <div className="card" style={{ padding: '20px' }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
            Taluk Vulnerability Ranking
          </h3>
          <div style={{ fontSize: '0.78rem', color: '#52665F', marginBottom: '16px' }}>
            Jurisdictional conflict hotspot assessment
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            {Object.entries(talukStats).map(([taluk, stat]) => (
              <div 
                key={taluk}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  padding: '10px 12px',
                  borderRadius: '8px',
                  backgroundColor: '#F8FAF7',
                  border: '1px solid #DEE8E3'
                }}
              >
                <div>
                  <div style={{ fontWeight: 700, fontSize: '0.85rem', color: '#192A24' }}>{taluk}</div>
                  <div style={{ fontSize: '0.72rem', color: '#52665F' }}>{stat.totalArea.toFixed(1)} ha cadastral coverage</div>
                </div>
                <div style={{ textAlign: 'right' }}>
                  <div style={{ fontSize: '0.82rem', fontWeight: 800, color: stat.highRiskCount > 0 ? '#D94848' : '#167A5B' }}>
                    {stat.highRiskCount} High Risk
                  </div>
                  <div style={{ fontSize: '0.72rem', color: '#7E948C' }}>{stat.count} Total Plots</div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Land Use Classification */}
        <div className="card" style={{ padding: '20px' }}>
          <h3 style={{ fontSize: '1rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
            Land Use Classification
          </h3>
          <div style={{ fontSize: '0.78rem', color: '#52665F', marginBottom: '16px' }}>
            Zoning distribution across mapped grid
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '8px' }}>
            {Object.entries(landUseCounts).map(([landUse, count]) => (
              <div 
                key={landUse}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  padding: '8px 12px',
                  borderRadius: '8px',
                  backgroundColor: '#F8FAF7',
                  border: '1px solid #DEE8E3',
                  fontSize: '0.8rem'
                }}
              >
                <span style={{ fontWeight: 600, color: '#192A24' }}>{landUse.replace('_', ' ')}</span>
                <span style={{ fontWeight: 800, color: '#167A5B' }}>{count} Parcels</span>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};
