import React, { useState, useEffect, useRef } from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { Parcel, ParcelStatus } from '../types/parcel';
import { MapContainer, TileLayer, Polygon, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import { 
  Layers, 
  MapPin, 
  AlertTriangle, 
  ShieldAlert, 
  CheckCircle2, 
  FileText, 
  Eye, 
  EyeOff, 
  ZoomIn, 
  ZoomOut, 
  Sparkles, 
  Maximize2,
  Filter,
  X,
  Compass,
  ArrowRight
} from 'lucide-react';

// Fix Leaflet default marker icon issue in Vite React
const defaultIcon = L.icon({
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41]
});

// Component to handle dynamic map centering/zooming
const MapController: React.FC<{ center: [number, number]; zoom: number }> = ({ center, zoom }) => {
  const map = useMap();
  useEffect(() => {
    map.setView(center, zoom, { animate: true });
  }, [center, zoom, map]);
  return null;
};

export const GisMapView: React.FC = () => {
  const { 
    parcels, 
    mapInitialParcelId, 
    setMapInitialParcelId, 
    setActiveParcelId, 
    setScreenState,
    setReportInitialParcelId,
    setSelectedNav
  } = useTerravault();

  const [selectedParcel, setSelectedParcel] = useState<Parcel | null>(null);
  const [statusFilter, setStatusFilter] = useState<ParcelStatus | 'ALL'>('ALL');
  const [mapStyle, setMapStyle] = useState<'STANDARD' | 'SATELLITE'>('STANDARD');

  // Layer Visibility
  const [showPolygons, setShowPolygons] = useState(true);
  const [showWaterBuffers, setShowWaterBuffers] = useState(true);
  const [showRiskHeatmap, setShowRiskHeatmap] = useState(true);
  const [showFmbOverlays, setShowFmbOverlays] = useState(true);

  // Map viewport state
  const [mapCenter, setMapCenter] = useState<[number, number]>([11.0120, 76.9950]);
  const [mapZoom, setMapZoom] = useState<number>(14);

  // When initialized with a specific parcel
  useEffect(() => {
    if (mapInitialParcelId) {
      const found = parcels.find(p => p.id === mapInitialParcelId);
      if (found) {
        setSelectedParcel(found);
        setMapCenter([found.latitude, found.longitude]);
        setMapZoom(16);
      }
    }
  }, [mapInitialParcelId, parcels]);

  const handleSelectParcel = (p: Parcel) => {
    setSelectedParcel(p);
    setMapCenter([p.latitude, p.longitude]);
    setMapZoom(16);
  };

  const filteredParcels = parcels.filter(p => {
    if (statusFilter === 'ALL') return true;
    return p.status === statusFilter;
  });

  const getPolygonStyle = (p: Parcel, isSelected: boolean) => {
    let fillColor = '#167A5B';
    let strokeColor = '#167A5B';

    if (p.status === 'CRITICAL_ISSUE') {
      fillColor = '#D94848';
      strokeColor = '#D94848';
    } else if (p.status === 'NEEDS_REVIEW') {
      fillColor = '#D99B2B';
      strokeColor = '#D99B2B';
    } else if (p.status === 'UNDER_VERIFICATION') {
      fillColor = '#2E7BB4';
      strokeColor = '#2E7BB4';
    }

    return {
      fillColor,
      fillOpacity: isSelected ? 0.7 : 0.35,
      color: isSelected ? '#000000' : strokeColor,
      weight: isSelected ? 4 : 2,
      dashArray: p.status === 'NEEDS_REVIEW' ? '4, 4' : undefined
    };
  };

  return (
    <div style={{ height: 'calc(100vh - 120px)', position: 'relative', borderRadius: '14px', overflow: 'hidden', border: '1px solid #DEE8E3' }}>
      {/* Top Floating Control Bar */}
      <div style={{
        position: 'absolute',
        top: '16px',
        left: '16px',
        right: '16px',
        zIndex: 1000,
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        flexWrap: 'wrap',
        gap: '12px',
        pointerEvents: 'none'
      }}>
        {/* Filter Pills */}
        <div style={{
          backgroundColor: 'rgba(255, 255, 255, 0.92)',
          backdropFilter: 'blur(8px)',
          padding: '6px 12px',
          borderRadius: '10px',
          border: '1px solid #DEE8E3',
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          boxShadow: 'var(--shadow-md)',
          pointerEvents: 'auto'
        }}>
          <span style={{ fontSize: '0.75rem', fontWeight: 700, color: '#52665F', marginRight: '4px' }}>
            Filter Status:
          </span>
          {(['ALL', 'VERIFIED', 'NEEDS_REVIEW', 'CRITICAL_ISSUE', 'UNDER_VERIFICATION'] as const).map(st => (
            <button
              key={st}
              onClick={() => setStatusFilter(st)}
              style={{
                border: 'none',
                padding: '4px 8px',
                borderRadius: '6px',
                fontSize: '0.72rem',
                fontWeight: 700,
                cursor: 'pointer',
                backgroundColor: statusFilter === st ? '#167A5B' : 'transparent',
                color: statusFilter === st ? '#FFFFFF' : '#52665F'
              }}
            >
              {st.replace('_', ' ')}
            </button>
          ))}
        </div>

        {/* Demo Spotlight Jump Buttons */}
        <div style={{
          backgroundColor: 'rgba(255, 255, 255, 0.92)',
          backdropFilter: 'blur(8px)',
          padding: '6px 12px',
          borderRadius: '10px',
          border: '1px solid #DEE8E3',
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          boxShadow: 'var(--shadow-md)',
          pointerEvents: 'auto'
        }}>
          <span style={{ fontSize: '0.75rem', fontWeight: 700, color: '#167A5B', display: 'flex', alignItems: 'center', gap: '4px' }}>
            <Sparkles size={12} /> Spotlight:
          </span>
          <button
            onClick={() => {
              const p = parcels.find(item => item.id === 'TN-COI-00123-0456');
              if (p) handleSelectParcel(p);
            }}
            className="btn btn-secondary"
            style={{ padding: '4px 8px', fontSize: '0.72rem' }}
          >
            Singanallur S.No 45/2A (Mismatch)
          </button>
          <button
            onClick={() => {
              const p = parcels.find(item => item.id === 'TN-COI-00892-1102');
              if (p) handleSelectParcel(p);
            }}
            className="btn btn-secondary"
            style={{ padding: '4px 8px', fontSize: '0.72rem', color: '#D94848' }}
          >
            Lake Buffer Encroachment
          </button>
          <button
            onClick={() => {
              const p = parcels.find(item => item.id === 'TN-COI-00344-0789');
              if (p) handleSelectParcel(p);
            }}
            className="btn btn-secondary"
            style={{ padding: '4px 8px', fontSize: '0.72rem', color: '#167A5B' }}
          >
            100% Clean Title
          </button>
        </div>

        {/* Map Basemap / Layer Switcher */}
        <div style={{
          backgroundColor: 'rgba(255, 255, 255, 0.92)',
          backdropFilter: 'blur(8px)',
          padding: '6px 10px',
          borderRadius: '10px',
          border: '1px solid #DEE8E3',
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          boxShadow: 'var(--shadow-md)',
          pointerEvents: 'auto'
        }}>
          <button
            onClick={() => setMapStyle(mapStyle === 'STANDARD' ? 'SATELLITE' : 'STANDARD')}
            className="btn btn-secondary"
            style={{ padding: '4px 8px', fontSize: '0.72rem' }}
          >
            <Layers size={14} color="#167A5B" />
            {mapStyle === 'STANDARD' ? 'Satellite View' : 'Carto Vector View'}
          </button>
        </div>
      </div>

      {/* Leaflet Map */}
      <MapContainer
        center={mapCenter}
        zoom={mapZoom}
        style={{ width: '100%', height: '100%' }}
        zoomControl={false}
      >
        <MapController center={mapCenter} zoom={mapZoom} />

        {/* Dynamic Tile Layer */}
        {mapStyle === 'STANDARD' ? (
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors &copy; <a href="https://carto.com/">CARTO</a>'
            url="https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png"
          />
        ) : (
          <TileLayer
            attribution='Tiles &copy; Esri &mdash; Source: Esri, i-cubed, USDA, USGS, AEX, GeoEye, Getmapping, Aerogrid, IGN, IGP, UPR-EGP, and the GIS User Community'
            url="https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}"
          />
        )}

        {/* Cadastral Parcel Polygons */}
        {showPolygons && filteredParcels.map(p => {
          const isSelected = selectedParcel?.id === p.id;
          const positions: [number, number][] = p.boundary.map(pt => [pt.lat, pt.lng]);

          return (
            <Polygon
              key={p.id}
              positions={positions}
              pathOptions={getPolygonStyle(p, isSelected)}
              eventHandlers={{
                click: () => setSelectedParcel(p)
              }}
            >
              <Popup>
                <div style={{ padding: '4px', minWidth: '180px' }}>
                  <div style={{ fontWeight: 800, fontSize: '0.9rem', color: '#192A24' }}>
                    Survey No. {p.surveyNumber}
                  </div>
                  <div style={{ fontSize: '0.75rem', color: '#52665F', margin: '2px 0 6px 0' }}>
                    {p.village}, {p.taluk}
                  </div>
                  <div style={{ fontSize: '0.75rem', fontWeight: 600 }}>
                    Owner: {p.ownerName}
                  </div>
                  <div style={{ fontSize: '0.75rem', color: '#167A5B' }}>
                    Area: {p.areaHectares} ha (GIS: {p.gisCalculatedArea} ha)
                  </div>
                  <div style={{ marginTop: '8px' }}>
                    <button
                      onClick={() => handleSelectParcel(p)}
                      style={{
                        width: '100%',
                        padding: '4px 8px',
                        backgroundColor: '#167A5B',
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        fontSize: '0.72rem',
                        fontWeight: 700,
                        cursor: 'pointer'
                      }}
                    >
                      Inspect in Dossier
                    </button>
                  </div>
                </div>
              </Popup>
            </Polygon>
          );
        })}

        {/* Markers for Spotlighted parcel centers */}
        {filteredParcels.map(p => (
          <Marker
            key={`marker-${p.id}`}
            position={[p.latitude, p.longitude]}
            icon={defaultIcon}
            eventHandlers={{
              click: () => setSelectedParcel(p)
            }}
          />
        ))}
      </MapContainer>

      {/* Selected Parcel Inspector Bottom Sheet / Drawer */}
      {selectedParcel && (
        <div style={{
          position: 'absolute',
          bottom: '20px',
          left: '20px',
          right: '20px',
          maxWidth: '850px',
          margin: '0 auto',
          zIndex: 1000,
          backgroundColor: '#FFFFFF',
          borderRadius: '14px',
          boxShadow: '0 12px 32px rgba(0,0,0,0.2)',
          border: '1px solid #DEE8E3',
          padding: '18px 22px',
          display: 'flex',
          flexDirection: 'column',
          gap: '12px'
        }}>
          {/* Header */}
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '12px' }}>
              <div style={{
                width: '40px',
                height: '40px',
                borderRadius: '8px',
                backgroundColor: selectedParcel.status === 'VERIFIED' ? '#E6F6F0' : selectedParcel.status === 'CRITICAL_ISSUE' ? '#FEEFEF' : '#FEF6E9',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center'
              }}>
                <MapPin size={20} color={selectedParcel.status === 'VERIFIED' ? '#167A5B' : selectedParcel.status === 'CRITICAL_ISSUE' ? '#D94848' : '#D99B2B'} />
              </div>
              <div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                  <span style={{ fontSize: '1.1rem', fontWeight: 800, color: '#192A24' }}>
                    Survey No. {selectedParcel.surveyNumber}
                  </span>
                  <span className={`badge ${selectedParcel.status === 'VERIFIED' ? 'badge-verified' : selectedParcel.status === 'CRITICAL_ISSUE' ? 'badge-critical' : 'badge-review'}`}>
                    {selectedParcel.status.replace('_', ' ')}
                  </span>
                </div>
                <div style={{ fontSize: '0.78rem', color: '#52665F' }}>
                  {selectedParcel.village} Village • {selectedParcel.taluk} Taluk • Deed: {selectedParcel.deedNumber}
                </div>
              </div>
            </div>

            <button 
              onClick={() => setSelectedParcel(null)}
              style={{ border: 'none', background: 'transparent', cursor: 'pointer', color: '#7E948C' }}
            >
              <X size={20} />
            </button>
          </div>

          {/* Key Intelligence Stats Bar */}
          <div style={{
            display: 'grid',
            gridTemplateColumns: 'repeat(auto-fit, minmax(130px, 1fr))',
            gap: '10px',
            backgroundColor: '#F8FAF7',
            padding: '10px 14px',
            borderRadius: '8px',
            border: '1px solid #DEE8E3'
          }}>
            <div>
              <div style={{ fontSize: '0.68rem', color: '#7E948C', textTransform: 'uppercase', fontWeight: 700 }}>Owner</div>
              <div style={{ fontSize: '0.85rem', fontWeight: 700, color: '#192A24' }}>{selectedParcel.ownerName}</div>
            </div>
            <div>
              <div style={{ fontSize: '0.68rem', color: '#7E948C', textTransform: 'uppercase', fontWeight: 700 }}>Revenue vs GIS Area</div>
              <div style={{ fontSize: '0.85rem', fontWeight: 700, color: selectedParcel.areaHectares !== selectedParcel.gisCalculatedArea ? '#D94848' : '#167A5B' }}>
                {selectedParcel.areaHectares} ha / {selectedParcel.gisCalculatedArea} ha
              </div>
            </div>
            <div>
              <div style={{ fontSize: '0.68rem', color: '#7E948C', textTransform: 'uppercase', fontWeight: 700 }}>Risk Score</div>
              <div style={{ fontSize: '0.85rem', fontWeight: 800, color: selectedParcel.riskScore > 50 ? '#D94848' : '#167A5B' }}>
                {selectedParcel.riskScore}/100
              </div>
            </div>
            <div>
              <div style={{ fontSize: '0.68rem', color: '#7E948C', textTransform: 'uppercase', fontWeight: 700 }}>Court Status</div>
              <div style={{ fontSize: '0.85rem', fontWeight: 600, color: selectedParcel.courtCaseStatus.includes('No') ? '#167A5B' : '#D94848' }}>
                {selectedParcel.courtCaseStatus}
              </div>
            </div>
          </div>

          {/* Detected Issues */}
          {selectedParcel.issues.length > 0 && (
            <div style={{
              backgroundColor: '#FEF8F8',
              border: '1px solid #FBDADA',
              borderRadius: '8px',
              padding: '8px 12px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'space-between'
            }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '0.8rem', color: '#B93232', fontWeight: 600 }}>
                <AlertTriangle size={16} />
                <span>{selectedParcel.issues[0].title}</span>
              </div>
              <span style={{ fontSize: '0.72rem', fontWeight: 700, color: '#B93232' }}>
                {selectedParcel.issues.length} Discrepancies
              </span>
            </div>
          )}

          {/* Action Buttons */}
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'flex-end', gap: '10px' }}>
            <button
              onClick={() => {
                setReportInitialParcelId(selectedParcel.id);
                setSelectedNav('REPORTS');
                setScreenState('MAIN_HUB');
              }}
              className="btn btn-secondary"
              style={{ fontSize: '0.8rem', padding: '6px 14px' }}
            >
              <FileText size={14} color="#167A5B" /> Generate Official Certificate
            </button>
            <button
              onClick={() => {
                setActiveParcelId(selectedParcel.id);
                setScreenState('PARCEL_DETAIL');
              }}
              className="btn btn-primary"
              style={{ fontSize: '0.8rem', padding: '6px 16px' }}
            >
              Open Full Intelligence Dossier <ArrowRight size={14} />
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
