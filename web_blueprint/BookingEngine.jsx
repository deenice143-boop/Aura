import React, { useState } from 'react';

const STYLISTS = [
  { id: "st_1", name: "Elena Rostova", role: "Master Stylist", rating: 4.9, initial: "ER" },
  { id: "st_2", name: "Alexander Mercer", role: "Artistic Director", rating: 5.0, initial: "AM" },
  { id: "st_3", name: "Marcus Vance", role: "Senior Creative Artist", rating: 4.8, initial: "MV" },
  { id: "st_4", name: "Sophia Loren", role: "Special Event Stylist", rating: 4.9, initial: "SL" }
];

const SERVICES = [
  { id: "srv_1", name: "Signature Couture Cut", category: "Cuts", price: 150, duration: 60, desc: "Tailored scissor or razor haircut, luxury scalp massage, and bespoke signature blowout finish." },
  { id: "srv_2", name: "Hollywood Glam Blowout", category: "Blowouts", price: 95, duration: 45, desc: "Luxurious high-volume shampoo, custom protein hair masking, and glamour wave style." },
  { id: "srv_3", name: "Golden Balayage & Melt", category: "Color", price: 320, duration: 150, desc: "Bespoke hand-painted luxury golden color highlights finished with a custom toning glaze." },
  { id: "srv_4", name: "Restorative Caviar Treatment", category: "Treatment", price: 180, duration: 75, desc: "Intense cellular restoration therapy infused with luxury caviar lipids and advanced steam hydration." }
];

const TIME_SLOTS = ["09:00 AM", "10:30 AM", "11:30 AM", "01:00 PM", "02:30 PM", "04:00 PM", "05:30 PM"];

export default function BookingEngine({ preselectedStyle, onAddReservation, activeReservations, onCancelReservation }) {
  // Pre-fill if navigated from style visualizer
  const defaultService = preselectedStyle 
    ? (SERVICES.find(s => s.name.toLowerCase().includes(preselectedStyle.name.toLowerCase()) || preselectedStyle.name.toLowerCase().includes(s.name.toLowerCase())) || null)
    : null;

  const [service, setService] = useState(defaultService);
  const [stylist, setStylist] = useState(null);
  const [date, setDate] = useState(""); 
  const [time, setTime] = useState("");
  const [toast, setToast] = useState(null);

  const showToast = (message, type = "success") => {
    setToast({ message, type });
    setTimeout(() => {
      setToast(null);
    }, 4000);
  };

  const handleBook = () => {
    if (!service) {
      showToast("Please select a bespoke couture service before booking.", "error");
      return;
    }
    if (!stylist) {
      showToast("Please choose an artisan stylist for your appointment.", "error");
      return;
    }
    if (!date) {
      showToast("Please choose an appointment date.", "error");
      return;
    }
    if (!time) {
      showToast("Please choose a preferred time slot.", "error");
      return;
    }

    const newRes = {
      id: `booking_${Date.now()}`,
      serviceName: service.name,
      stylistName: stylist.name,
      dateString: date,
      timeSlot: time,
      totalPrice: service.price
    };
    onAddReservation(newRes);
    showToast(`SUCCESS: Reservation with ${stylist.name} for ${service.name} is fully confirmed!`, "success");
    
    // Clear selections on success for elegant flow
    setService(null);
    setStylist(null);
    setDate("");
    setTime("");
  };

  return (
    <div className="w-full max-w-4xl mx-auto px-4 py-6 animate-fadeIn">
      {/* Toast Notification Container */}
      {toast && (
        <div className={`fixed bottom-6 right-6 z-50 flex items-center gap-3 p-4 rounded-xl border shadow-2xl max-w-sm transition-all duration-300 ${
          toast.type === "success" 
            ? "bg-charcoal-surface border-gold-primary text-gold-primary" 
            : "bg-charcoal-surface border-red-500/50 text-red-400"
        }`}>
          <div className="flex-1 text-xs font-bold font-sans tracking-wide">
            {toast.message}
          </div>
          <button onClick={() => setToast(null)} className="text-[10px] uppercase font-bold hover:underline opacity-80 pl-2">
            Dismiss
          </button>
        </div>
      )}

      <div className="mb-6">
        <h2 className="text-2xl font-bold text-text-light font-luxury tracking-wide">
          Atelier Reservations
        </h2>
        <p className="text-text-muted text-sm mt-1">
          Secure your styling slot with our elite artisans. Live session variables are cached locally for optimal performance.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {/* Left Column: Configurator selectors */}
        <div className="md:col-span-2 space-y-6">
          {/* Select Service */}
          <div className="bg-charcoal-surface rounded-xl p-5 border border-charcoal-border">
            <h3 className="text-xs font-bold text-gold-primary uppercase tracking-widest mb-3">
              1. Select Bespoke Service
            </h3>
            <div className="space-y-3">
              {SERVICES.map((s) => {
                const isSel = service && s.id === service.id;
                return (
                  <div
                    key={s.id}
                    onClick={() => setService(s)}
                    className={`p-4 rounded-xl cursor-pointer border transition-all ${
                      isSel
                        ? 'bg-gold-primary/5 border-gold-primary'
                        : 'bg-charcoal-bg border-charcoal-border/50 hover:border-gold-primary/25'
                    }`}
                  >
                    <div className="flex justify-between items-start">
                      <div>
                        <h4 className="text-text-light text-sm font-bold tracking-wide">
                          {s.name}
                        </h4>
                        <p className="text-text-muted text-xs mt-1 leading-relaxed">
                          {s.desc}
                        </p>
                      </div>
                      <div className="text-right ml-4">
                        <span className="text-gold-primary font-extrabold text-sm">${s.price}</span>
                        <p className="text-text-muted text-[10px] mt-1">{s.duration} mins</p>
                      </div>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>

          {/* Select Stylist */}
          <div className="bg-charcoal-surface rounded-xl p-5 border border-charcoal-border">
            <h3 className="text-xs font-bold text-gold-primary uppercase tracking-widest mb-3">
              2. Choose Artisan Stylist
            </h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {STYLISTS.map((st) => {
                const isSel = stylist && st.id === stylist.id;
                return (
                  <div
                    key={st.id}
                    onClick={() => setStylist(st)}
                    className={`flex items-center gap-3 p-3 rounded-lg cursor-pointer border transition-all ${
                      isSel
                        ? 'bg-gold-primary/5 border-gold-primary'
                        : 'bg-charcoal-bg border-charcoal-border/50 hover:border-gold-primary/20'
                    }`}
                  >
                    <div className="w-10 h-10 rounded-full bg-charcoal-card flex items-center justify-center font-bold text-xs text-gold-primary border border-gold-primary/20">
                      {st.initial}
                    </div>
                    <div>
                      <h4 className="text-text-light text-xs font-bold">{st.name}</h4>
                      <p className="text-text-muted text-[10px]">{st.role}</p>
                    </div>
                    {isSel && (
                      <div className="ml-auto w-2.5 h-2.5 bg-gold-primary rounded-full" />
                    )}
                  </div>
                );
              })}
            </div>
          </div>

          {/* Date & Time slots */}
          <div className="bg-charcoal-surface rounded-xl p-5 border border-charcoal-border">
            <h3 className="text-xs font-bold text-gold-primary uppercase tracking-widest mb-3">
              3. Select Appointment Slot
            </h3>
            
            {/* Simple date selectors */}
            <div className="flex gap-2 overflow-x-auto pb-3 mb-4 scrollbar-thin">
              {["Fri, Jun 26", "Sat, Jun 27", "Sun, Jun 28", "Mon, Jun 29"].map((d) => {
                const isSel = d === date;
                return (
                  <button
                    key={d}
                    onClick={() => setDate(d)}
                    className={`px-4 py-2 rounded-lg text-xs font-semibold border whitespace-nowrap transition-all ${
                      isSel
                        ? 'bg-gold-primary text-charcoal-bg border-gold-primary shadow-sm'
                        : 'bg-charcoal-bg text-text-light border-charcoal-border hover:border-gold-primary/20'
                    }`}
                  >
                    {d}
                  </button>
                );
              })}
            </div>

            {/* Time slots grid */}
            <div className="grid grid-cols-3 sm:grid-cols-4 gap-2">
              {TIME_SLOTS.map((t) => {
                const isSel = t === time;
                return (
                  <button
                    key={t}
                    onClick={() => setTime(t)}
                    className={`h-9 rounded-lg text-xs font-semibold border transition-all ${
                      isSel
                        ? 'bg-gold-primary/10 text-gold-primary border-gold-primary'
                        : 'bg-charcoal-bg text-text-muted border-charcoal-border/55 hover:border-gold-primary/20'
                    }`}
                  >
                    {t}
                  </button>
                );
              })}
            </div>
          </div>
        </div>

        {/* Right Column: Reservation receipt / active list */}
        <div className="space-y-6">
          {/* Summary Box */}
          <div className="bg-charcoal-surface rounded-xl p-5 border border-gold-primary/20 shadow-lg">
            <h3 className="text-xs font-bold text-gold-primary uppercase tracking-widest mb-4">
              RESERVATION SUMMARY
            </h3>
            
            <div className="space-y-3 font-sans">
              <div className="flex justify-between items-center text-xs">
                <span className="text-text-muted">Bespoke Service:</span>
                <span className="text-text-light font-bold text-right line-clamp-1">
                  {service ? service.name : <span className="text-red-500/80 italic">Select Service</span>}
                </span>
              </div>
              <div className="flex justify-between items-center text-xs">
                <span className="text-text-muted">Master Artisan:</span>
                <span className="text-text-light font-bold">
                  {stylist ? stylist.name : <span className="text-red-500/80 italic">Select Stylist</span>}
                </span>
              </div>
              <div className="flex justify-between items-center text-xs">
                <span className="text-text-muted">Selected Slot:</span>
                <span className="text-gold-secondary font-bold">
                  {date && time ? `${date} at ${time}` : <span className="text-red-500/80 italic">Select Date & Time</span>}
                </span>
              </div>
              
              <hr className="border-charcoal-border my-4" />

              <div className="flex justify-between items-center">
                <span className="text-text-light text-sm font-bold font-luxury">Total Price:</span>
                <span className="text-gold-primary text-xl font-extrabold">
                  {service ? `$${service.price}` : "—"}
                </span>
              </div>
            </div>

            <button
              onClick={handleBook}
              className="w-full h-12 bg-gold-primary hover:bg-gold-secondary text-charcoal-bg font-bold text-xs tracking-widest rounded-lg transition-all duration-300 mt-5"
            >
              CONFIRM RESERVATION
            </button>
          </div>

          {/* Active Reservations list */}
          <div className="bg-charcoal-surface rounded-xl p-5 border border-charcoal-border">
            <h3 className="text-xs font-bold text-text-light tracking-wide mb-3">
              YOUR APPOINTMENTS ({activeReservations.length})
            </h3>
            {activeReservations.length === 0 ? (
              <div className="py-6 text-center">
                <p className="text-text-muted text-xs italic">No active bookings recorded.</p>
              </div>
            ) : (
              <div className="space-y-3 max-h-[220px] overflow-y-auto pr-1">
                {activeReservations.map((res) => (
                  <div key={res.id} className="bg-charcoal-bg p-3 rounded-lg border border-charcoal-border/50 relative">
                    <button
                      onClick={() => onCancelReservation(res.id)}
                      className="absolute top-2 right-2 text-red-500 text-xs hover:underline"
                    >
                      Cancel
                    </button>
                    <h4 className="text-text-light text-xs font-bold line-clamp-1 pr-8">{res.serviceName}</h4>
                    <p className="text-text-muted text-[10px] mt-0.5">with {res.stylistName}</p>
                    <p className="text-gold-secondary text-[10px] mt-1 font-semibold">{res.dateString} at {res.timeSlot}</p>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
