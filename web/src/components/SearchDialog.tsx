import React, { useState, useEffect } from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { Search, X, MapPin, FileText, User, ArrowRight, ShieldAlert, CheckCircle2 } from 'lucide-react';
import { Parcel } from '../types/parcel';

export const SearchDialog: React.FC = () => {
  const { showSearchModal, setShowSearchModal, parcels, setActiveParcelId, setScreenState } = useTerravault();
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if ((e.ctrlKey || e.metaKey) && e.key.toLowerCase() === 'k') {
        e.preventDefault();
        setShowSearchModal(true);
      }
      if (e.key === 'Escape' && showSearchModal) {
        setShowSearchModal(false);
      }
    };
    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [showSearchModal, setShowSearchModal]);

  if (!showSearchModal) return null;

  const q = searchQuery.toLowerCase().trim();
  const results = q === '' 
    ? parcels.slice(0, 6)
    : parcels.filter(p => 
        p.id.toLowerCase().includes(q) ||
        p.surveyNumber.toLowerCase().includes(q) ||
        p.ownerName.toLowerCase().includes(q) ||
        p.village.toLowerCase().includes(q) ||
        p.taluk.toLowerCase().includes(q) ||
        p.deedNumber.toLowerCase().includes(q)
      );

  const handleSelectParcel = (p: Parcel) => {
    setActiveParcelId(p.id);
    setScreenState('PARCEL_DETAIL');
    setShowSearchModal(false);
  };

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      backgroundColor: 'rgba(15, 84, 62, 0.4)',
      backdropFilter: 'blur(4px)',
      display: 'flex',
      alignItems: 'flex-start',
      justifyContent: 'center',
      paddingTop: '100px',
      zIndex: 100
    }} onClick={() => setShowSearchModal(false)}>
      <div 
        style={{
          width: '640px',
          maxWidth: '92vw',
          backgroundColor: '#FFFFFF',
          borderRadius: '14px',
          boxShadow: '0 20px 40px rgba(0,0,0,0.2)',
          border: '1px solid #DEE8E3',
          overflow: 'hidden'
        }}
        onClick={(e) => e.stopPropagation()}
      >
        {/* Search Header */}
        <div style={{
          display: 'flex',
          alignItems: 'center',
          gap: '12px',
          padding: '16px 20px',
          borderBottom: '1px solid #DEE8E3'
        }}>
          <Search size={20} color="#167A5B" />
          <input
            type="text"
            placeholder="Search by survey # (e.g. 45/2A), ID, Owner name, or Deed #..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            autoFocus
            style={{
              flex: 1,
              border: 'none',
              outline: 'none',
              fontSize: '1rem',
              color: '#192A24',
              fontFamily: 'inherit'
            }}
          />
          <button 
            onClick={() => setShowSearchModal(false)}
            style={{ border: 'none', background: 'transparent', cursor: 'pointer', color: '#7E948C' }}
          >
            <X size={20} />
          </button>
        </div>

        {/* Search Results */}
        <div style={{ maxHeight: '380px', overflowY: 'auto', padding: '10px 12px' }}>
          <div style={{ fontSize: '0.72rem', fontWeight: 700, color: '#7E948C', textTransform: 'uppercase', padding: '6px 10px' }}>
            {searchQuery ? `Matching Records (${results.length})` : 'Spotlight Land Records'}
          </div>

          {results.length === 0 ? (
            <div style={{ padding: '30px', textAlign: 'center', color: '#52665F' }}>
              No land records matching "<strong>{searchQuery}</strong>"
            </div>
          ) : (
            results.map(parcel => (
              <div
                key={parcel.id}
                onClick={() => handleSelectParcel(parcel)}
                style={{
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'space-between',
                  padding: '10px 14px',
                  borderRadius: '8px',
                  cursor: 'pointer',
                  transition: 'background 0.15s ease',
                  borderBottom: '1px solid #F0F4F2'
                }}
                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = '#EEF3F0'}
                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = 'transparent'}
              >
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <span style={{ fontWeight: 700, fontSize: '0.92rem', color: '#192A24' }}>
                      S.No {parcel.surveyNumber}
                    </span>
                    <span style={{
                      fontSize: '0.7rem',
                      fontWeight: 700,
                      padding: '2px 8px',
                      borderRadius: '10px',
                      backgroundColor: parcel.status === 'VERIFIED' ? '#E6F6F0' : parcel.status === 'CRITICAL_ISSUE' ? '#FEEFEF' : '#FEF6E9',
                      color: parcel.status === 'VERIFIED' ? '#167A5B' : parcel.status === 'CRITICAL_ISSUE' ? '#D94848' : '#B47814'
                    }}>
                      {parcel.status.replace('_', ' ')}
                    </span>
                  </div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '10px', fontSize: '0.78rem', color: '#52665F', marginTop: '2px' }}>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                      <User size={12} /> {parcel.ownerName}
                    </span>
                    <span>•</span>
                    <span style={{ display: 'flex', alignItems: 'center', gap: '3px' }}>
                      <MapPin size={12} /> {parcel.village}, {parcel.taluk}
                    </span>
                  </div>
                </div>

                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                  <div style={{ textAlign: 'right', fontSize: '0.78rem' }}>
                    <div style={{ fontWeight: 600, color: '#192A24' }}>{parcel.areaHectares} ha</div>
                    <div style={{ color: parcel.riskScore > 50 ? '#D94848' : '#167A5B', fontSize: '0.72rem', fontWeight: 700 }}>
                      Risk {parcel.riskScore}/100
                    </div>
                  </div>
                  <ArrowRight size={16} color="#7E948C" />
                </div>
              </div>
            ))
          )}
        </div>

        {/* Footer info */}
        <div style={{
          padding: '10px 18px',
          backgroundColor: '#F8FAF7',
          borderTop: '1px solid #DEE8E3',
          display: 'flex',
          justifyContent: 'space-between',
          fontSize: '0.72rem',
          color: '#7E948C'
        }}>
          <span>Press <strong>ESC</strong> to close</span>
          <span>Tip: Search "Singanallur" or "45/2A"</span>
        </div>
      </div>
    </div>
  );
};
