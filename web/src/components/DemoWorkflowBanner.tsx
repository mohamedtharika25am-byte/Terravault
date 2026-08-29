import React from 'react';
import { useTerravault } from '../context/TerravaultContext';
import { Sparkles, ArrowRight, Play, CheckCircle } from 'lucide-react';

export const DemoWorkflowBanner: React.FC = () => {
  const { demoStepIndex, handleDemoStep } = useTerravault();

  const steps = [
    { num: 1, title: "Overview", subtitle: "Command Center & KPIs" },
    { num: 2, title: "GIS Map", subtitle: "Singanallur S.No 45/2A" },
    { num: 3, title: "Critical Parcel", subtitle: "Singanallur Lake Encroachment" },
    { num: 4, title: "Intelligence Dossier", subtitle: "Cross-Department Matrix" },
    { num: 5, title: "Dispute Resolution", subtitle: "Officer Resurvey Order" },
    { num: 6, title: "DPI Certificate", subtitle: "Official QR Land Report" }
  ];

  return (
    <div style={{
      backgroundColor: '#0F543E',
      color: '#FFFFFF',
      borderRadius: '12px',
      padding: '12px 18px',
      marginBottom: '20px',
      boxShadow: '0 4px 12px rgba(15, 84, 62, 0.15)',
      display: 'flex',
      flexDirection: 'column',
      gap: '10px'
    }}>
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
          <div style={{
            backgroundColor: '#D99B2B',
            color: '#0F543E',
            padding: '3px 8px',
            borderRadius: '6px',
            fontSize: '0.72rem',
            fontWeight: 800,
            display: 'flex',
            alignItems: 'center',
            gap: '4px'
          }}>
            <Sparkles size={12} /> SIH 2026 EVALUATION WORKFLOW
          </div>
          <span style={{ fontSize: '0.85rem', fontWeight: 600, color: '#E8F4EE' }}>
            Interactive Demo Script • Step {demoStepIndex + 1} of {steps.length}
          </span>
        </div>

        <button
          onClick={() => handleDemoStep((demoStepIndex + 1) % steps.length)}
          style={{
            backgroundColor: '#D99B2B',
            color: '#0F543E',
            border: 'none',
            padding: '5px 12px',
            borderRadius: '6px',
            fontSize: '0.75rem',
            fontWeight: 700,
            cursor: 'pointer',
            display: 'flex',
            alignItems: 'center',
            gap: '4px'
          }}
        >
          Next Step <ArrowRight size={14} />
        </button>
      </div>

      <div style={{
        display: 'grid',
        gridTemplateColumns: 'repeat(6, 1fr)',
        gap: '8px'
      }}>
        {steps.map((step, idx) => {
          const isActive = demoStepIndex === idx;
          const isPassed = idx < demoStepIndex;

          return (
            <button
              key={step.num}
              onClick={() => handleDemoStep(idx)}
              style={{
                backgroundColor: isActive ? '#D99B2B' : isPassed ? '#167A5B' : 'rgba(255, 255, 255, 0.1)',
                color: isActive ? '#0F543E' : '#FFFFFF',
                border: isActive ? '1px solid #FFE3A8' : '1px solid rgba(255, 255, 255, 0.1)',
                borderRadius: '8px',
                padding: '6px 8px',
                textAlign: 'left',
                cursor: 'pointer',
                transition: 'all 0.15s ease'
              }}
            >
              <div style={{ display: 'flex', alignItems: 'center', gap: '6px', fontSize: '0.75rem', fontWeight: 800 }}>
                {isPassed ? <CheckCircle size={12} color="#FFFFFF" /> : <span>{step.num}.</span>}
                <span>{step.title}</span>
              </div>
              <div style={{
                fontSize: '0.65rem',
                color: isActive ? '#3F2C0B' : 'rgba(255, 255, 255, 0.7)',
                whiteSpace: 'nowrap',
                overflow: 'hidden',
                textOverflow: 'ellipsis',
                marginTop: '2px'
              }}>
                {step.subtitle}
              </div>
            </button>
          );
        })}
      </div>
    </div>
  );
};
