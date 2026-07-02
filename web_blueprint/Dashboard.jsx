import React, { useState, Suspense, lazy } from 'react';
import { motion, AnimatePresence } from 'framer-motion';

// =========================================================================
// CODE SPLITTING VIA REACT.LAZY (DYNAMICAL BUNDLE CHUNKS FOR LUXURY SALON)
// =========================================================================
const BookingEngine = lazy(() => import('./BookingEngine'));
const PortfolioGallery = lazy(() => import('./PortfolioGallery'));
const StyleVisualizer = lazy(() => import('./StyleVisualizer'));

// Tab definition enumeration
const TABS = {
  PORTFOLIO: 'portfolio',
  VISUALIZER: 'visualizer',
  BOOKING: 'booking'
};

export default function Dashboard() {
  const [currentTab, setCurrentTab] = useState(TABS.PORTFOLIO);
  const [preselectedStyleName, setPreselectedStyleName] = useState(null);
  const [activeReservations, setActiveReservations] = useState([]);

  // Transition & Lazy states
  const handleSelectStyleFromPortfolio = (styleName) => {
    setPreselectedStyleName(styleName);
    setCurrentTab(TABS.VISUALIZER);
  };

  const handleReserveStyleFromVisualizer = (styleDetail, color, length) => {
    setPreselectedStyleName({
      name: styleDetail.name,
      configuredColor: color,
      configuredLength: length
    });
    setCurrentTab(TABS.BOOKING);
  };

  const handleAddReservation = (newRes) => {
    setActiveReservations(prev => [...prev, newRes]);
  };

  const handleCancelReservation = (id) => {
    setActiveReservations(prev => prev.filter(res => res.id !== id));
  };

  return (
    <div className="min-h-screen bg-charcoal-bg text-text-light flex flex-col justify-between font-sans selection:bg-gold-primary selection:text-charcoal-bg">
      {/* Brand Header */}
      <header className="sticky top-0 z-50 bg-charcoal-bg/95 backdrop-blur-md border-b border-charcoal-border/50 px-6 py-4 flex items-center justify-between">
        <div className="flex flex-col">
          <span className="text-sm font-extrabold text-gold-primary tracking-haute font-luxury">
            A U R A
          </span>
          <span className="text-[9px] font-bold text-text-muted tracking-luxury mt-0.5">
            HAUTE COIFFURE EST. 2026
          </span>
        </div>

        <div className="flex items-center gap-2 bg-charcoal-surface/65 px-3.5 py-1.5 rounded-full border border-charcoal-border/50 text-[11px] font-bold tracking-wider text-gold-secondary">
          <span className="w-2 h-2 bg-green-500 rounded-full animate-ping" />
          ATELIER CONNECTION ACTIVE
        </div>
      </header>

      {/* Main Content Area containing dynamically splitting React.lazy components */}
      <main className="flex-grow py-6 pb-24 overflow-hidden">
        {/* Dynamic Suspense boundary featuring high-fidelity skeleton fallback loaders */}
        <AnimatePresence mode="wait">
          <motion.div
            key={currentTab}
            initial={{ opacity: 0, y: 15 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -15 }}
            transition={{ duration: 0.35, ease: [0.16, 1, 0.3, 1] }}
            className="w-full h-full"
          >
            <Suspense fallback={<SuspenseFallback activeTab={currentTab} />}>
              {currentTab === TABS.PORTFOLIO && (
                <PortfolioGallery onSelectStyle={handleSelectStyleFromPortfolio} />
              )}
              {currentTab === TABS.VISUALIZER && (
                <StyleVisualizer
                  defaultStyleName={preselectedStyleName}
                  onReserveStyle={handleReserveStyleFromVisualizer}
                />
              )}
              {currentTab === TABS.BOOKING && (
                <BookingEngine
                  preselectedStyle={preselectedStyleName}
                  activeReservations={activeReservations}
                  onAddReservation={handleAddReservation}
                  onCancelReservation={handleCancelReservation}
                />
              )}
            </Suspense>
          </motion.div>
        </AnimatePresence>
      </main>

      {/* Persistent Luxury Navigation Dock */}
      <nav className="fixed bottom-0 left-0 right-0 z-50 bg-charcoal-surface/90 backdrop-blur-md border-t border-charcoal-border/60 py-3.5 px-6">
        <div className="max-w-md mx-auto flex items-center justify-around">
          {/* Portfolio button */}
          <button
            onClick={() => setCurrentTab(TABS.PORTFOLIO)}
            className={`flex flex-col items-center gap-1 transition-all ${
              currentTab === TABS.PORTFOLIO ? 'text-gold-primary scale-105' : 'text-text-muted hover:text-text-light'
            }`}
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5.5 w-5.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <span className="text-[10px] font-bold uppercase tracking-wider">Portfolio</span>
          </button>

          {/* Style Visualizer button */}
          <button
            onClick={() => setCurrentTab(TABS.VISUALIZER)}
            className={`flex flex-col items-center gap-1 transition-all ${
              currentTab === TABS.VISUALIZER ? 'text-gold-primary scale-105' : 'text-text-muted hover:text-text-light'
            }`}
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="h-5.5 w-5.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4" />
            </svg>
            <span className="text-[10px] font-bold uppercase tracking-wider">Visualizer</span>
          </button>

          {/* Booking Engine button */}
          <button
            onClick={() => setCurrentTab(TABS.BOOKING)}
            className={`flex flex-col items-center gap-1 transition-all ${
              currentTab === TABS.BOOKING ? 'text-gold-primary scale-105' : 'text-text-muted hover:text-text-light'
            }`}
          >
            <div className="relative">
              {activeReservations.length > 0 && (
                <span className="absolute -top-1 -right-1 bg-red-500 text-text-light text-[8px] font-extrabold rounded-full w-4 h-4 flex items-center justify-center animate-pulse">
                  {activeReservations.length}
                </span>
              )}
              <svg xmlns="http://www.w3.org/2000/svg" className="h-5.5 w-5.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
            </div>
            <span className="text-[10px] font-bold uppercase tracking-wider">Reservation</span>
          </button>
        </div>
      </nav>
    </div>
  );
}

// =========================================================================
// PREMIUM SUSPENSE FALLBACK WITH INDEPENDENT CHUNK SKELETON PLACEHOLDERS
// =========================================================================
function SuspenseFallback({ activeTab }) {
  return (
    <div className="w-full max-w-4xl mx-auto px-6 py-12 flex flex-col items-center animate-pulse">
      {/* Metallic Spinning Loader */}
      <div className="relative w-12 h-12 mb-6">
        <div className="absolute inset-0 border-2 border-charcoal-border rounded-full" />
        <div className="absolute inset-0 border-t-2 border-gold-primary rounded-full animate-spin" />
      </div>

      <span className="text-xs font-bold tracking-widest text-gold-primary uppercase">
        S U S P E N S E
      </span>
      <p className="text-text-muted text-xs font-mono mt-2 tracking-wide text-center">
        {activeTab === TABS.PORTFOLIO && 'Asynchronously fetching high-resolution photo assets...'}
        {activeTab === TABS.VISUALIZER && 'Downloading vector category modifiers & matrix bundle...'}
        {activeTab === TABS.BOOKING && 'Compiling secure SQLite connection state & form controllers...'}
      </p>

      {/* Corresponding module outline skeletons */}
      <div className="w-full mt-12 space-y-6">
        {activeTab === TABS.PORTFOLIO && (
          <div className="grid grid-cols-2 md:grid-cols-4 gap-6">
            {[1, 2, 3, 4].map(n => (
              <div key={n} className="bg-charcoal-surface rounded-xl aspect-[4/5] border border-charcoal-border/55" />
            ))}
          </div>
        )}

        {activeTab === TABS.VISUALIZER && (
          <div className="space-y-6">
            <div className="flex gap-2 mb-4">
              {[1, 2, 3].map(n => (
                <div key={n} className="bg-charcoal-surface rounded-lg w-24 h-10 border border-charcoal-border/55" />
              ))}
            </div>
            <div className="bg-charcoal-surface rounded-2xl h-80 border border-charcoal-border/55" />
          </div>
        )}

        {activeTab === TABS.BOOKING && (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
            <div className="md:col-span-2 space-y-4">
              <div className="bg-charcoal-surface rounded-xl h-48 border border-charcoal-border/55" />
              <div className="bg-charcoal-surface rounded-xl h-36 border border-charcoal-border/55" />
            </div>
            <div className="bg-charcoal-surface rounded-xl h-64 border border-charcoal-border/55" />
          </div>
        )}
      </div>
    </div>
  );
}
