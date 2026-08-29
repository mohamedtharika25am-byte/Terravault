import React from 'react';
import { useTerravault, TerravaultProvider } from './context/TerravaultContext';
import { Sidebar } from './components/Sidebar';
import { Navbar } from './components/Navbar';
import { SearchDialog } from './components/SearchDialog';
import { NotificationDrawer } from './components/NotificationDrawer';
import { RoleSelectorModal } from './components/RoleSelectorModal';
import { LandingView } from './views/LandingView';
import { DashboardView } from './views/DashboardView';
import { GisMapView } from './views/GisMapView';
import { ParcelsListView } from './views/ParcelsListView';
import { ParcelDetailView } from './views/ParcelDetailView';
import { IssuesView } from './views/IssuesView';
import { AnalyticsView } from './views/AnalyticsView';
import { DataSourcesView } from './views/DataSourcesView';
import { ReportsView } from './views/ReportsView';

const MainLayout: React.FC = () => {
  const { screenState, selectedNav } = useTerravault();

  if (screenState === 'LANDING') {
    return (
      <>
        <LandingView />
        <RoleSelectorModal />
      </>
    );
  }

  const renderCurrentView = () => {
    if (screenState === 'PARCEL_DETAIL') {
      return <ParcelDetailView />;
    }

    switch (selectedNav) {
      case 'DASHBOARD':
        return <DashboardView />;
      case 'GIS_MAP':
        return <GisMapView />;
      case 'PARCELS':
        return <ParcelsListView />;
      case 'ISSUES':
        return <IssuesView />;
      case 'ANALYTICS':
        return <AnalyticsView />;
      case 'DATA_SOURCES':
        return <DataSourcesView />;
      case 'REPORTS':
        return <ReportsView />;
      default:
        return <DashboardView />;
    }
  };

  return (
    <div className="app-container">
      <Sidebar />
      <div className="main-content">
        <Navbar />
        <main className="content-body">
          {renderCurrentView()}
        </main>
      </div>

      {/* Global Modals & Drawers */}
      <SearchDialog />
      <NotificationDrawer />
      <RoleSelectorModal />
    </div>
  );
};

export const App: React.FC = () => {
  return (
    <TerravaultProvider>
      <MainLayout />
    </TerravaultProvider>
  );
};

export default App;
