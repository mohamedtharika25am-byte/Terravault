import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { UserRole } from '../types/parcel';
import { X, ShieldCheck, UserCheck, Check, Compass, Users } from 'lucide-react';

export const RoleSelectorModal: React.FC = () => {
  const { showRoleModal, setShowRoleModal, userProfile, setUserRole } = useTerravault();

  if (!showRoleModal) return null;

  const roles: { role: UserRole; title: string; subtitle: string; desc: string; icon: React.ComponentType<{ size?: number; color?: string }> }[] = [
    {
      role: 'ADMIN',
      title: 'District Collector & Magistrate',
      subtitle: 'Dr. S. Karthikeyan, IAS',
      desc: 'Full administrative authority across 6 department databases, system settings, and state-level audit approvals.',
      icon: ShieldCheck
    },
    {
      role: 'GOVERNMENT_OFFICER',
      title: 'Tahsildar & Revenue Officer',
      subtitle: 'T. Anbarasan, DRO',
      desc: 'Issue mutation clearances, regularize land use slabs, order physical field inspections, and resolve title disputes.',
      icon: UserCheck
    },
    {
      role: 'REVIEWER',
      title: 'Cadastral DGPS Survey Inspector',
      subtitle: 'K. Priya, M.Tech (GIS)',
      desc: 'Verify drone & DGPS boundary polygons, reconcile FMB sheet variances, and update digital village GIS sheets.',
      icon: Compass
    },
    {
      role: 'VIEWER',
      title: 'Citizen & Public Portal',
      subtitle: 'R. Soundararajan',
      desc: 'Transparent public land registry access, title verification before property purchase, and instant encumbrance checks.',
      icon: Users
    }
  ];

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(15, 84, 62, 0.45)',
      backdropFilter: 'blur(4px)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 110,
      padding: '20px'
    }} onClick={() => setShowRoleModal(false)}>
      <div 
        style={{
          width: '560px',
          maxWidth: '100%',
          backgroundColor: '#FFFFFF',
          borderRadius: '16px',
          boxShadow: '0 20px 50px rgba(0,0,0,0.25)',
          overflow: 'hidden',
          border: '1px solid #DEE8E3'
        }}
        onClick={(e) => e.stopPropagation()}
      >
        <div style={{
          padding: '20px 24px',
          borderBottom: '1px solid #DEE8E3',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'space-between',
          backgroundColor: '#0F543E',
          color: '#FFFFFF'
        }}>
          <div>
            <h2 style={{ fontSize: '1.2rem', fontWeight: 800 }}>Select Role & Governance Persona</h2>
            <div style={{ fontSize: '0.8rem', color: '#A3C7B9' }}>Switch perspective to simulate different state stakeholders</div>
          </div>
          <button 
            onClick={() => setShowRoleModal(false)}
            style={{ border: 'none', background: 'transparent', color: '#FFFFFF', cursor: 'pointer' }}
          >
            <X size={22} />
          </button>
        </div>

        <div style={{ padding: '20px 24px', display: 'flex', flexDirection: 'column', gap: '12px' }}>
          {roles.map((item) => {
            const isSelected = userProfile.role === item.role;
            const Icon = item.icon;

            return (
              <div
                key={item.role}
                onClick={() => {
                  setUserRole(item.role);
                  setShowRoleModal(false);
                }}
                style={{
                  display: 'flex',
                  alignItems: 'flex-start',
                  gap: '14px',
                  padding: '14px 16px',
                  borderRadius: '10px',
                  border: isSelected ? '2px solid #167A5B' : '1px solid #DEE8E3',
                  backgroundColor: isSelected ? '#E8F4EE' : '#FFFFFF',
                  cursor: 'pointer',
                  transition: 'all 0.15s ease'
                }}
                onMouseEnter={(e) => {
                  if (!isSelected) e.currentTarget.style.backgroundColor = '#F8FAF7';
                }}
                onMouseLeave={(e) => {
                  if (!isSelected) e.currentTarget.style.backgroundColor = '#FFFFFF';
                }}
              >
                <div style={{
                  width: '38px',
                  height: '38px',
                  borderRadius: '8px',
                  backgroundColor: isSelected ? '#167A5B' : '#EEF3F0',
                  color: isSelected ? '#FFFFFF' : '#167A5B',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  flexShrink: 0
                }}>
                  <Icon size={20} />
                </div>

                <div style={{ flex: 1 }}>
                  <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                    <div style={{ fontWeight: 800, fontSize: '0.95rem', color: '#192A24' }}>
                      {item.title}
                    </div>
                    {isSelected && (
                      <span style={{
                        display: 'flex',
                        alignItems: 'center',
                        gap: '4px',
                        fontSize: '0.75rem',
                        fontWeight: 700,
                        color: '#167A5B'
                      }}>
                        <Check size={14} /> Active Role
                      </span>
                    )}
                  </div>
                  <div style={{ fontSize: '0.78rem', fontWeight: 600, color: '#167A5B', marginBottom: '4px' }}>
                    {item.subtitle}
                  </div>
                  <div style={{ fontSize: '0.75rem', color: '#52665F', lineHeight: 1.4 }}>
                    {item.desc}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};
