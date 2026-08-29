import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { UserRole } from '../types/parcel';
import { 
  ShieldCheck, 
  Layers, 
  MapPin, 
  AlertTriangle, 
  FileCheck, 
  Compass, 
  ArrowRight, 
  Sparkles,
  CheckCircle2,
  Building2,
  Scale,
  Search,
  Database
} from 'lucide-react';

export const LandingView: React.FC = () => {
  const { setScreenState, setUserRole, setSelectedNav } = useTerravault();

  const handleRoleSelect = (role: UserRole) => {
    setUserRole(role);
    setScreenState('MAIN_HUB');
    setSelectedNav('DASHBOARD');
  };

  const departments = [
    { name: "Revenue Department", code: "REV", role: "Patta & Mutation Ownership", color: "#167A5B" },
    { name: "Registration (SRO)", code: "REG", role: "Sale Deeds & STAR 2.0", color: "#2E7BB4" },
    { name: "Survey & Land Records", code: "SUR", role: "FMB Sheets & DGPS Vector", color: "#D99B2B" },
    { name: "Municipal Property Tax", code: "TAX", role: "Assessment & Arrears", color: "#8E44AD" },
    { name: "Urban Planning (DTCP)", code: "PLN", role: "Zoning & Master Plan 2030", color: "#E67E22" },
    { name: "Judicial Courts (NJDG)", code: "LEG", role: "Injunctions & Stay Orders", color: "#D94848" }
  ];

  return (
    <div style={{
      minHeight: '100vh',
      backgroundColor: '#F8FAF7',
      display: 'flex',
      flexDirection: 'column'
    }}>
      {/* Top Banner */}
      <div style={{
        backgroundColor: '#0F543E',
        color: '#FFFFFF',
        padding: '10px 24px',
        fontSize: '0.8rem',
        fontWeight: 600,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <Sparkles size={14} color="#D99B2B" />
          <span>Smart India Hackathon 2026 • Digital Public Infrastructure Prototype</span>
        </div>
        <div style={{ color: '#A3C7B9' }}>
          State of Tamil Nadu • Land Governance & Cadastral Intelligence
        </div>
      </div>

      {/* Hero Section */}
      <section style={{
        padding: '60px 24px 40px 24px',
        maxWidth: '1200px',
        margin: '0 auto',
        textAlign: 'center',
        width: '100%'
      }}>
        <div style={{
          display: 'inline-flex',
          alignItems: 'center',
          gap: '8px',
          padding: '6px 14px',
          borderRadius: '9999px',
          backgroundColor: '#E8F4EE',
          border: '1px solid rgba(22, 122, 91, 0.3)',
          color: '#167A5B',
          fontSize: '0.82rem',
          fontWeight: 700,
          marginBottom: '20px'
        }}>
          <ShieldCheck size={16} /> Autonomous Cross-Department Land Verification
        </div>

        <h1 style={{
          fontSize: '3.2rem',
          fontWeight: 900,
          color: '#0F543E',
          lineHeight: 1.15,
          letterSpacing: '-0.02em',
          marginBottom: '16px'
        }}>
          TERRAVAULT
        </h1>

        <p style={{
          fontSize: '1.45rem',
          fontWeight: 700,
          color: '#192A24',
          marginBottom: '12px'
        }}>
          One Parcel. Complete Truth.
        </p>

        <p style={{
          fontSize: '1.05rem',
          color: '#52665F',
          maxWidth: '740px',
          margin: '0 auto 36px auto',
          lineHeight: 1.6
        }}>
          A next-generation Digital Public Infrastructure platform unifying <strong>Revenue Patta, Registration Deeds, Cadastral DGPS Survey, Municipal Tax, DTCP Zoning, and e-Courts Judicial Records</strong> into an autonomous, tamper-proof single source of truth.
        </p>

        {/* Enter App Actions */}
        <div style={{ display: 'flex', justifyContent: 'center', gap: '16px', flexWrap: 'wrap', marginBottom: '50px' }}>
          <button 
            onClick={() => handleRoleSelect('ADMIN')}
            className="btn btn-primary"
            style={{ padding: '14px 28px', fontSize: '1rem', borderRadius: '10px', boxShadow: 'var(--shadow-md)' }}
          >
            Launch Command Center <ArrowRight size={18} />
          </button>
          
          <button 
            onClick={() => {
              setUserRole('ADMIN');
              setScreenState('MAIN_HUB');
              setSelectedNav('GIS_MAP');
            }}
            className="btn btn-secondary"
            style={{ padding: '14px 24px', fontSize: '1rem', borderRadius: '10px' }}
          >
            <Compass size={18} color="#167A5B" /> Explore GIS Cadastral Map
          </button>
        </div>

        {/* 6 Unified Gateways Visual Bar */}
        <div style={{
          backgroundColor: '#FFFFFF',
          borderRadius: '16px',
          padding: '24px',
          border: '1px solid #DEE8E3',
          boxShadow: 'var(--shadow-sm)'
        }}>
          <div style={{ fontSize: '0.78rem', fontWeight: 800, textTransform: 'uppercase', letterSpacing: '0.06em', color: '#52665F', marginBottom: '16px' }}>
            Unified Real-Time State Data Layer
          </div>
          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(160px, 1fr))', gap: '12px' }}>
            {departments.map((dept) => (
              <div 
                key={dept.code}
                style={{
                  padding: '14px 12px',
                  borderRadius: '10px',
                  backgroundColor: '#F8FAF7',
                  border: '1px solid #DEE8E3',
                  textAlign: 'left'
                }}
              >
                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '6px' }}>
                  <span style={{ fontSize: '0.7rem', fontWeight: 800, color: dept.color, backgroundColor: `${dept.color}15`, padding: '2px 6px', borderRadius: '4px' }}>
                    {dept.code}
                  </span>
                  <span style={{ width: '8px', height: '8px', borderRadius: '50%', backgroundColor: '#167A5B' }}></span>
                </div>
                <div style={{ fontWeight: 800, fontSize: '0.85rem', color: '#192A24', marginBottom: '2px' }}>
                  {dept.name}
                </div>
                <div style={{ fontSize: '0.72rem', color: '#7E948C' }}>
                  {dept.role}
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Role Selection Showcase */}
      <section style={{
        backgroundColor: '#FFFFFF',
        padding: '50px 24px',
        borderTop: '1px solid #DEE8E3',
        borderBottom: '1px solid #DEE8E3'
      }}>
        <div style={{ maxWidth: '1200px', margin: '0 auto', textAlign: 'center' }}>
          <h2 style={{ fontSize: '1.8rem', fontWeight: 800, color: '#0F543E', marginBottom: '8px' }}>
            Select Governance Persona
          </h2>
          <p style={{ color: '#52665F', fontSize: '0.95rem', marginBottom: '32px' }}>
            Experience Terravault from different operational and public stakeholder roles.
          </p>

          <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(260px, 1fr))', gap: '20px' }}>
            <div 
              onClick={() => handleRoleSelect('ADMIN')}
              className="card card-interactive"
              style={{ textAlign: 'left', padding: '24px', borderTop: '4px solid #167A5B' }}
            >
              <div style={{ fontSize: '0.75rem', fontWeight: 800, color: '#167A5B', textTransform: 'uppercase', marginBottom: '6px' }}>
                State Authority
              </div>
              <h3 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
                District Collector / Admin
              </h3>
              <p style={{ fontSize: '0.82rem', color: '#52665F', marginBottom: '16px' }}>
                Dr. S. Karthikeyan, IAS • Full district analytics, conflict resolution overrides, and gateway controls.
              </p>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.82rem', fontWeight: 700, color: '#167A5B' }}>
                Enter as Collector <ArrowRight size={14} />
              </div>
            </div>

            <div 
              onClick={() => handleRoleSelect('GOVERNMENT_OFFICER')}
              className="card card-interactive"
              style={{ textAlign: 'left', padding: '24px', borderTop: '4px solid #2E7BB4' }}
            >
              <div style={{ fontSize: '0.75rem', fontWeight: 800, color: '#2E7BB4', textTransform: 'uppercase', marginBottom: '6px' }}>
                Revenue Administration
              </div>
              <h3 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
                Tahsildar / Revenue Officer
              </h3>
              <p style={{ fontSize: '0.82rem', color: '#52665F', marginBottom: '16px' }}>
                T. Anbarasan, DRO • Review mutation discrepancies, execute field enquiry, and sign verification orders.
              </p>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.82rem', fontWeight: 700, color: '#2E7BB4' }}>
                Enter as Tahsildar <ArrowRight size={14} />
              </div>
            </div>

            <div 
              onClick={() => handleRoleSelect('REVIEWER')}
              className="card card-interactive"
              style={{ textAlign: 'left', padding: '24px', borderTop: '4px solid #D99B2B' }}
            >
              <div style={{ fontSize: '0.75rem', fontWeight: 800, color: '#D99B2B', textTransform: 'uppercase', marginBottom: '6px' }}>
                Cadastral Survey
              </div>
              <h3 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
                Survey Inspector (GIS)
              </h3>
              <p style={{ fontSize: '0.82rem', color: '#52665F', marginBottom: '16px' }}>
                K. Priya, M.Tech • Inspect FMB area variances, DGPS satellite bounds, and water body buffer overlaps.
              </p>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.82rem', fontWeight: 700, color: '#D99B2B' }}>
                Enter as Inspector <ArrowRight size={14} />
              </div>
            </div>

            <div 
              onClick={() => handleRoleSelect('VIEWER')}
              className="card card-interactive"
              style={{ textAlign: 'left', padding: '24px', borderTop: '4px solid #8E44AD' }}
            >
              <div style={{ fontSize: '0.75rem', fontWeight: 800, color: '#8E44AD', textTransform: 'uppercase', marginBottom: '6px' }}>
                Citizen Portal
              </div>
              <h3 style={{ fontSize: '1.15rem', fontWeight: 800, color: '#192A24', marginBottom: '4px' }}>
                Public Citizen / Buyer
              </h3>
              <p style={{ fontSize: '0.82rem', color: '#52665F', marginBottom: '16px' }}>
                R. Soundararajan • Instant tamper-proof title check, court case shield, and QR verified land certificates.
              </p>
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.82rem', fontWeight: 700, color: '#8E44AD' }}>
                Enter as Citizen <ArrowRight size={14} />
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer style={{
        padding: '24px',
        textAlign: 'center',
        fontSize: '0.8rem',
        color: '#7E948C',
        marginTop: 'auto'
      }}>
        TERRAVAULT GIS Land Governance • Powered by Google Gemini AI & DGPS Spatial Rule Engine
      </footer>
    </div>
  );
};
