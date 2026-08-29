import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { 
  Search, 
  Bell, 
  RefreshCw, 
  MapPin, 
  UserCheck, 
  CheckCircle2, 
  Sparkles 
} from 'lucide-react';

export const Navbar: React.FC = () => {
  const { 
    selectedNav, 
    userProfile, 
    isSyncing, 
    triggerSystemSync, 
    setShowSearchModal, 
    setShowNotifications,
    issues,
    screenState,
    setScreenState
  } = useTerravault();

  const openIssuesCount = issues.filter(i => i.status === 'OPEN').length;

  const getNavTitle = () => {
    if (screenState === 'PARCEL_DETAIL') return 'Parcel Intelligence & Dossier';
    switch (selectedNav) {
      case 'DASHBOARD': return 'Command Center & Verification Dashboard';
      case 'GIS_MAP': return 'Cadastral GIS Map & Spatial Overlays';
      case 'PARCELS': return 'Unified Land Parcel Registry';
      case 'ISSUES': return 'Conflict Detection & Dispute Resolution Engine';
      case 'ANALYTICS': return 'Land Intelligence & Anomaly Analytics';
      case 'DATA_SOURCES': return 'State Departmental DPI Gateways (6 Sources)';
      case 'REPORTS': return 'Official Land Intelligence Certificates';
      default: return 'Terravault';
    }
  };

  return (
    <header style={{
      height: '68px',
      backgroundColor: '#FFFFFF',
      borderBottom: '1px solid #DEE8E3',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'space-between',
      padding: '0 32px',
      position: 'sticky',
      top: 0,
      zIndex: 30,
      boxShadow: '0 1px 3px rgba(16, 40, 30, 0.04)'
    }}>
      {/* Left: Module Title & Breadcrumb */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '16px' }}>
        {screenState === 'PARCEL_DETAIL' && (
          <button 
            onClick={() => setScreenState('MAIN_HUB')}
            className="btn btn-secondary"
            style={{ padding: '6px 12px', fontSize: '0.8rem' }}
          >
            ← Back to Hub
          </button>
        )}
        <div>
          <h1 style={{ fontSize: '1.2rem', fontWeight: 800, color: '#192A24' }}>
            {getNavTitle()}
          </h1>
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.75rem', color: '#52665F' }}>
            <span style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
              <MapPin size={12} color="#167A5B" /> {userProfile.jurisdiction}
            </span>
            <span>•</span>
            <span style={{ display: 'flex', alignItems: 'center', gap: '4px', color: '#167A5B', fontWeight: 600 }}>
              <CheckCircle2 size={12} color="#167A5B" /> 6 DPI Gateways Online
            </span>
          </div>
        </div>
      </div>

      {/* Right: Global Actions */}
      <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
        {/* Search Button */}
        <button
          onClick={() => setShowSearchModal(true)}
          style={{
            display: 'flex',
            alignItems: 'center',
            gap: '10px',
            padding: '8px 14px',
            backgroundColor: '#F8FAF7',
            border: '1px solid #DEE8E3',
            borderRadius: '8px',
            color: '#52665F',
            fontSize: '0.85rem',
            cursor: 'pointer',
            minWidth: '220px',
            justifyContent: 'space-between'
          }}
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
            <Search size={16} color="#7E948C" />
            <span>Search survey #, owner, deed...</span>
          </div>
          <kbd style={{
            backgroundColor: '#EEF3F0',
            border: '1px solid #BED0C8',
            borderRadius: '4px',
            padding: '1px 6px',
            fontSize: '0.7rem',
            fontWeight: 700,
            color: '#192A24'
          }}>
            Ctrl+K
          </kbd>
        </button>

        {/* Sync Button */}
        <button
          onClick={triggerSystemSync}
          disabled={isSyncing}
          className="btn btn-secondary"
          style={{ padding: '8px 14px', gap: '6px' }}
          title="Synchronize all 6 State DPI Department Records"
        >
          <RefreshCw size={16} className={isSyncing ? 'animate-spin' : ''} style={{
            animation: isSyncing ? 'spin 1s linear infinite' : 'none'
          }} />
          <span style={{ fontSize: '0.85rem' }}>{isSyncing ? 'Syncing...' : 'Sync DPI'}</span>
        </button>

        {/* Notification Bell */}
        <button
          onClick={() => setShowNotifications(true)}
          style={{
            position: 'relative',
            width: '38px',
            height: '38px',
            borderRadius: '8px',
            backgroundColor: '#F8FAF7',
            border: '1px solid #DEE8E3',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            cursor: 'pointer'
          }}
          title="Conflict & Audit Notifications"
        >
          <Bell size={18} color="#192A24" />
          {openIssuesCount > 0 && (
            <span style={{
              position: 'absolute',
              top: '-4px',
              right: '-4px',
              backgroundColor: '#D94848',
              color: 'white',
              fontSize: '0.68rem',
              fontWeight: 800,
              width: '18px',
              height: '18px',
              borderRadius: '50%',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              border: '2px solid white'
            }}>
              {openIssuesCount}
            </span>
          )}
        </button>
      </div>

      <style>{`
        @keyframes spin {
          from { transform: rotate(0deg); }
          to { transform: rotate(360deg); }
        }
      `}</style>
    </header>
  );
};
