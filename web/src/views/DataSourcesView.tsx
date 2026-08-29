import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { 
  Database, 
  RefreshCw, 
  CheckCircle2, 
  AlertTriangle, 
  Server, 
  Activity, 
  Lock, 
  Zap,
  Clock,
  ShieldCheck
} from 'lucide-react';

export const DataSourcesView: React.FC = () => {
  const { dataSources, isSyncing, triggerSystemSync } = useTerravault();

  return (
    <div style={{ display: 'flex', flexDirection: 'column', gap: '24px' }}>
      {/* Header */}
      <div className="card" style={{ padding: '24px' }}>
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', flexWrap: 'wrap', gap: '16px' }}>
          <div>
            <div style={{ display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '4px' }}>
              <Database size={22} color="#167A5B" />
              <h2 style={{ fontSize: '1.3rem', fontWeight: 800, color: '#192A24' }}>
                State DPI Gateways & Integration Connectors
              </h2>
            </div>
            <div style={{ fontSize: '0.82rem', color: '#52665F' }}>
              6 Autonomous State departmental APIs connected via Tamil Nadu State Data Centre (TNeGA Secure Gateway)
            </div>
          </div>

          <button
            onClick={triggerSystemSync}
            disabled={isSyncing}
            className="btn btn-primary"
            style={{ padding: '10px 18px' }}
          >
            <RefreshCw size={16} className={isSyncing ? 'animate-spin' : ''} />
            {isSyncing ? 'Synchronizing State Gateways...' : 'Trigger Full DPI Re-sync'}
          </button>
        </div>
      </div>

      {/* Gateway Cards */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(340px, 1fr))', gap: '16px' }}>
        {dataSources.map(source => {
          const isConflict = source.status === 'CONFLICT';
          const isMissing = source.status === 'MISSING';

          return (
            <div 
              key={source.department}
              className="card"
              style={{
                padding: '20px',
                borderTop: isConflict ? '4px solid #D94848' : isMissing ? '4px solid #D99B2B' : '4px solid #167A5B',
                display: 'flex',
                flexDirection: 'column',
                gap: '14px'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <div>
                  <h3 style={{ fontSize: '1.05rem', fontWeight: 800, color: '#192A24' }}>
                    {source.department}
                  </h3>
                  <div style={{ fontSize: '0.72rem', color: '#7E948C', fontFamily: 'var(--font-mono)' }}>
                    Gateway Endpoint: {source.recordNumber}
                  </div>
                </div>

                <span style={{
                  fontSize: '0.75rem',
                  fontWeight: 800,
                  padding: '3px 8px',
                  borderRadius: '6px',
                  backgroundColor: isConflict ? '#FEEFEF' : isMissing ? '#FEF6E9' : '#E6F6F0',
                  color: isConflict ? '#D94848' : isMissing ? '#B47814' : '#167A5B'
                }}>
                  {source.status}
                </span>
              </div>

              <div style={{ fontSize: '0.82rem', color: '#52665F', lineHeight: 1.4 }}>
                {source.details}
              </div>

              <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(3, 1fr)',
                gap: '8px',
                backgroundColor: '#F8FAF7',
                padding: '10px',
                borderRadius: '8px',
                fontSize: '0.75rem',
                textAlign: 'center'
              }}>
                <div>
                  <div style={{ color: '#7E948C', fontSize: '0.68rem', textTransform: 'uppercase', fontWeight: 700 }}>Latency</div>
                  <div style={{ fontWeight: 800, color: '#192A24' }}>{source.latencyMs} ms</div>
                </div>
                <div>
                  <div style={{ color: '#7E948C', fontSize: '0.68rem', textTransform: 'uppercase', fontWeight: 700 }}>Health</div>
                  <div style={{ fontWeight: 800, color: '#167A5B' }}>{source.healthPercent}%</div>
                </div>
                <div>
                  <div style={{ color: '#7E948C', fontSize: '0.68rem', textTransform: 'uppercase', fontWeight: 700 }}>Records</div>
                  <div style={{ fontWeight: 800, color: '#192A24' }}>{source.totalRecords}</div>
                </div>
              </div>

              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', fontSize: '0.72rem', color: '#7E948C', borderTop: '1px solid #F0F4F2', paddingTop: '10px' }}>
                <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                  <Lock size={12} color="#167A5B" /> TLS 1.3 mTLS Encrypted
                </span>
                <span>Last heartbeat: {source.lastUpdated}</span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};
