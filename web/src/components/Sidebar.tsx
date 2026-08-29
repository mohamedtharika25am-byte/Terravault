import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { NavItem } from '../types/parcel';
import { 
  LayoutDashboard, 
  MapPin, 
  Layers, 
  AlertTriangle, 
  BarChart3, 
  Database, 
  FileText, 
  ShieldCheck, 
  ChevronRight
} from 'lucide-react';

export const Sidebar: React.FC = () => {
  const { selectedNav, setSelectedNav, setScreenState, parcels, issues, userProfile, setShowRoleModal } = useTerravault();

  const criticalIssuesCount = issues.filter(i => i.status === 'OPEN' && i.severity === 'CRITICAL').length;
  const totalParcelsCount = parcels.length;

  const navItems: { id: NavItem; label: string; icon: React.ComponentType<{ size?: number; className?: string; color?: string }>; badge?: string | number; badgeColor?: string }[] = [
    { id: 'DASHBOARD', label: 'Command Center', icon: LayoutDashboard },
    { id: 'GIS_MAP', label: 'GIS Cadastral Map', icon: MapPin },
    { id: 'PARCELS', label: 'Parcel Registry', icon: Layers, badge: totalParcelsCount },
    { 
      id: 'ISSUES', 
      label: 'Conflict & Disputes', 
      icon: AlertTriangle, 
      badge: criticalIssuesCount > 0 ? `${criticalIssuesCount} Crit` : undefined,
      badgeColor: 'bg-red-100 text-red-700'
    },
    { id: 'ANALYTICS', label: 'Land Intelligence', icon: BarChart3 },
    { id: 'DATA_SOURCES', label: 'DPI Gateways', icon: Database },
    { id: 'REPORTS', label: 'Official Certificates', icon: FileText }
  ];

  return (
    <aside style={{
      width: '280px',
      backgroundColor: '#0F543E',
      color: '#FFFFFF',
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      flexShrink: 0,
      borderRight: '1px solid rgba(255, 255, 255, 0.1)',
      userSelect: 'none'
    }}>
      {/* Brand Header */}
      <div style={{
        padding: '24px 20px 16px 20px',
        borderBottom: '1px solid rgba(255, 255, 255, 0.1)',
        cursor: 'pointer'
      }} onClick={() => setScreenState('LANDING')}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
          <div style={{
            width: '42px',
            height: '42px',
            borderRadius: '10px',
            backgroundColor: '#167A5B',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            border: '1px solid rgba(255, 255, 255, 0.2)'
          }}>
            <ShieldCheck size={26} color="#FFFFFF" />
          </div>
          <div>
            <div style={{ fontSize: '1.25rem', fontWeight: 800, letterSpacing: '1px', color: '#FFFFFF', display: 'flex', alignItems: 'center', gap: '6px' }}>
              TERRAVAULT
            </div>
            <div style={{ fontSize: '0.72rem', color: '#A3C7B9', fontWeight: 600 }}>
              DPI LAND GOVERNANCE • TN
            </div>
          </div>
        </div>
      </div>

      {/* Navigation List */}
      <nav style={{ flex: 1, padding: '16px 12px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '4px' }}>
        <div style={{ fontSize: '0.7rem', textTransform: 'uppercase', letterSpacing: '0.08em', color: '#7FA99B', padding: '8px 12px 4px 12px', fontWeight: 700 }}>
          Main Modules
        </div>

        {navItems.map(item => {
          const isActive = selectedNav === item.id;
          const Icon = item.icon;

          return (
            <button
              key={item.id}
              onClick={() => {
                setSelectedNav(item.id);
                setScreenState('MAIN_HUB');
              }}
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'space-between',
                width: '100%',
                padding: '10px 14px',
                borderRadius: '8px',
                border: 'none',
                backgroundColor: isActive ? '#167A5B' : 'transparent',
                color: isActive ? '#FFFFFF' : '#C2D9CF',
                cursor: 'pointer',
                fontWeight: isActive ? 700 : 500,
                fontSize: '0.88rem',
                transition: 'all 0.15s ease',
                textAlign: 'left'
              }}
              onMouseEnter={(e) => {
                if (!isActive) e.currentTarget.style.backgroundColor = 'rgba(255, 255, 255, 0.08)';
              }}
              onMouseLeave={(e) => {
                if (!isActive) e.currentTarget.style.backgroundColor = 'transparent';
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
                <Icon size={18} color={isActive ? '#FFFFFF' : '#A3C7B9'} />
                <span>{item.label}</span>
              </div>

              {item.badge && (
                <span style={{
                  padding: '2px 8px',
                  borderRadius: '12px',
                  fontSize: '0.72rem',
                  fontWeight: 700,
                  backgroundColor: item.id === 'ISSUES' ? '#D94848' : 'rgba(255, 255, 255, 0.18)',
                  color: '#FFFFFF'
                }}>
                  {item.badge}
                </span>
              )}
            </button>
          );
        })}
      </nav>

      {/* User Role & Quick Switcher Footer */}
      <div style={{
        padding: '14px 16px',
        borderTop: '1px solid rgba(255, 255, 255, 0.1)',
        backgroundColor: '#0A3B2B'
      }}>
        <div 
          onClick={() => setShowRoleModal(true)}
          style={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: '8px 10px',
            borderRadius: '8px',
            backgroundColor: 'rgba(255, 255, 255, 0.06)',
            cursor: 'pointer',
            border: '1px solid rgba(255, 255, 255, 0.1)'
          }}
          title="Click to Switch Role"
        >
          <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
            <div style={{
              width: '32px',
              height: '32px',
              borderRadius: '50%',
              backgroundColor: '#D99B2B',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              fontWeight: 800,
              fontSize: '0.85rem',
              color: '#0F543E'
            }}>
              {userProfile.name[0]}
            </div>
            <div>
              <div style={{ fontSize: '0.82rem', fontWeight: 700, color: '#FFFFFF', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis', maxWidth: '140px' }}>
                {userProfile.name}
              </div>
              <div style={{ fontSize: '0.7rem', color: '#7FA99B' }}>
                {userProfile.role.replace('_', ' ')}
              </div>
            </div>
          </div>
          <ChevronRight size={16} color="#7FA99B" />
        </div>
      </div>
    </aside>
  );
};
