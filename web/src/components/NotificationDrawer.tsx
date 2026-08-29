import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { X, AlertTriangle, ShieldCheck, RefreshCw, ArrowRight } from 'lucide-react';

export const NotificationDrawer: React.FC = () => {
  const { showNotifications, setShowNotifications, auditLogs, issues, setActiveParcelId, setScreenState, setSelectedNav } = useTerravault();

  if (!showNotifications) return null;

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(15, 84, 62, 0.35)',
      backdropFilter: 'blur(3px)',
      display: 'flex',
      justifyContent: 'flex-end',
      zIndex: 100
    }} onClick={() => setShowNotifications(false)}>
      <div 
        style={{
          width: '420px',
          maxWidth: '85vw',
          backgroundColor: '#FFFFFF',
          height: '100%',
          boxShadow: '-4px 0 24px rgba(0,0,0,0.15)',
          display: 'flex',
          flexDirection: 'column',
          borderLeft: '1px solid #DEE8E3'
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Header */}
        <div style={{
          padding: '18px 20px',
          borderBottom: '1px solid #DEE8E3',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          backgroundColor: '#F8FAF7'
        }}>
          <div>
            <h2 style={{ fontSize: '1.05rem', fontWeight: 800, color: '#192A24' }}>System Notifications & Audit Stream</h2>
            <div style={{ fontSize: '0.75rem', color: '#52665F' }}>Real-time DPI Cross-Engine Events</div>
          </div>
          <button 
            onClick={() => setShowNotifications(false)}
            style={{ border: 'none', background: 'transparent', cursor: 'pointer', color: '#7E948C' }}
          >
            <X size={20} />
          </button>
        </div>

        {/* Content Tabs / Stream */}
        <div style={{ flex: 1, overflowY: 'auto', padding: '16px' }}>
          <div style={{ fontSize: '0.72rem', fontWeight: 700, color: '#7E948C', textTransform: 'uppercase', marginBottom: '10px' }}>
            Recent Activity Logs ({auditLogs.length})
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}>
            {auditLogs.map((log) => (
              <div 
                key={log.id}
                style={{
                  padding: '12px',
                  borderRadius: '10px',
                  backgroundColor: '#F8FAF7',
                  border: '1px solid #DEE8E3'
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '4px' }}>
                  <span style={{ fontSize: '0.7rem', fontWeight: 700, color: '#167A5B', backgroundColor: '#E6F6F0', padding: '2px 6px', borderRadius: '4px' }}>
                    {log.userRole}
                  </span>
                  <span style={{ fontSize: '0.68rem', color: '#7E948C' }}>{log.timestamp}</span>
                </div>

                <div style={{ fontSize: '0.82rem', fontWeight: 700, color: '#192A24', marginBottom: '2px' }}>
                  {log.action}
                </div>

                <div style={{ fontSize: '0.75rem', color: '#52665F', lineHeight: 1.4 }}>
                  {log.details}
                </div>

                {log.parcelId && (
                  <button
                    onClick={() => {
                      setActiveParcelId(log.parcelId!);
                      setScreenState('PARCEL_DETAIL');
                      setShowNotifications(false);
                    }}
                    style={{
                      marginTop: '8px',
                      display: 'flex',
                      alignItems: 'center',
                      gap: '4px',
                      fontSize: '0.72rem',
                      color: '#167A5B',
                      fontWeight: 700,
                      border: 'none',
                      background: 'transparent',
                      cursor: 'pointer',
                      padding: 0
                    }}
                  >
                    Inspect Parcel {log.parcelId} <ArrowRight size={12} />
                  </button>
                )}
              </div>
            ))}
          </div>
        </div>

        {/* Footer */}
        <div style={{ padding: '14px 18px', borderTop: '1px solid #DEE8E3', backgroundColor: '#F8FAF7' }}>
          <button 
            onClick={() => {
              setSelectedNav('ISSUES');
              setScreenState('MAIN_HUB');
              setShowNotifications(false);
            }}
            className="btn btn-primary"
            style={{ width: '100%', fontSize: '0.85rem' }}
          >
            View All Open Conflicts
          </button>
        </div>
      </div>
    </div>
  );
};
