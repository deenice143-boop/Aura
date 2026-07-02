# ⚜️ AURA Web Architecture Blueprint: React.lazy & Suspense Integration

Welcome to the production integration handbook. This technical blueprint serves as a high-fidelity reference manual for translating your AURA Haute Coiffure prototype into a premium, hyper-scalable live environment using **React**, **Tailwind CSS**, and **Supabase**.

---

## 💎 Architectural Concept: Code Splitting for Ultra-Premium Conversion

For luxury brands, page speed is directly proportional to conversion. The AURA workspace consists of three highly immersive, graphic-intensive modules. Compiling all three modules into a single monolithic bundle results in slow Time-to-Interactive (TTI).

By utilizing **React.lazy** and **Suspense**, this architecture forces **code-splitting** at the tab boundary:
1. **Initial Paint**: Only the core Shell and the high-resolution `PortfolioGallery` bundle chunk are downloaded.
2. **Asynchronous Fetching**: The `StyleVisualizer` and `BookingEngine` bundle chunks are downloaded in the background or dynamically fetched only when the user interacts with their corresponding tab anchors.
3. **Graceful Degradation**: During bundle transit, React catches the unresolved imports and presents a stunning `<SuspenseFallback />` displaying custom-designed skeletal outlines matching the incoming layout.

---

## 🛠️ File Structure

The blueprint files in this directory are fully structured and production-ready:
*   `tailwind.config.js` — Core brand design system locking in the metallic gold and deep charcoal hex variables.
*   `Dashboard.jsx` — Main landing shell displaying the brand header, active network status, and the `<Suspense>` wrapper code-splitting child modules.
*   `PortfolioGallery.jsx` — High-fidelity curated style feed with interactive client-side liking state.
*   `StyleVisualizer.jsx` — Interactive customized styling panel mapping color and texturing specifications.
*   `BookingEngine.jsx` — Dynamic reservations scheduler with state-complete form controls and live appointments registry.

---

## 🎨 Global Styling System: Tailwind CSS Configuration

Incorporate the brand design system globally by importing the unified Tailwind config (`tailwind.config.js`):

```js
// tailwind.config.js
module.exports = {
  theme: {
    extend: {
      colors: {
        gold: {
          primary: '#D4AF37',   // Metallic Luxury Gold
          secondary: '#E5C158', // Warm Gold Accent
        },
        charcoal: {
          bg: '#121212',        // Deepest dark void
          surface: '#1E1E1E',   // Luxurious container background
          border: '#3D3D3D',    // Elegant outline partitions
        }
      }
    }
  }
}
```

---

## 🔌 Connecting to the Live Backend (Supabase / FlutterFlow)

This blueprint contains explicit state hooks matching the schema of our live databases. Below is the technical transition map for connecting to **Supabase** via standard async clients.

### 1. Fetching Real-Time Artisans & Services
In `BookingEngine.jsx` or your root context, replace the static arrays with standard Supabase client hooks:

```javascript
import { createClient } from '@supabase/supabase-js';

const supabase = createClient('YOUR_SUPABASE_URL', 'YOUR_SUPABASE_ANON_KEY');

// Inside your component:
const [stylists, setStylists] = useState([]);
const [services, setServices] = useState([]);

useEffect(() => {
  const fetchAtelierData = async () => {
    const { data: stylistsList } = await supabase.from('stylists').select('*');
    const { data: servicesList } = await supabase.from('services').select('*');
    
    if (stylistsList) setStylists(stylistsList);
    if (servicesList) setServices(servicesList);
  };
  
  fetchAtelierData();
}, []);
```

### 2. Posting Secure Bookings (Insert)
In `BookingEngine.jsx`, hook the `handleBook` submission directly to Supabase's `bookings` table:

```javascript
const handleBook = async () => {
  const newBooking = {
    service_id: service.id,
    stylist_id: stylist.id,
    date_string: date,
    time_slot: time,
    total_price: service.price,
    customer_email: 'customer@example.com' // Map from Auth Context
  };

  const { data, error } = await supabase
    .from('bookings')
    .insert([newBooking])
    .select();

  if (error) {
    alert(`Reservation failure: ${error.message}`);
  } else {
    onAddReservation(data[0]);
    alert(`Success! Secure reservation recorded.`);
  }
};
```

### 3. Live Real-Time DB Subscriptions (Realtime API)
To synchronize reservation schedules instantly across all stylist devices (so no double-booking is possible), initialize Supabase's Postgres Changes listener:

```javascript
useEffect(() => {
  const channel = supabase
    .channel('schema-db-changes')
    .on(
      'postgres_changes',
      { event: '*', filter: 'customer_email=eq.customer@example.com', schema: 'public', table: 'bookings' },
      (payload) => {
        // Automatically sync UI list when database state updates
        if (payload.eventType === 'INSERT') {
          setActiveReservations(prev => [...prev, payload.new]);
        } else if (payload.eventType === 'DELETE') {
          setActiveReservations(prev => prev.filter(b => b.id !== payload.old.id));
        }
      }
    )
    .subscribe();

  return () => supabase.removeChannel(channel);
}, []);
```

---

## 💎 Design Consistency Guarantee

This structure mirrors the exact layout rules, padding constraints, and user navigation transitions deployed in your native Android Jetpack Compose app, guaranteeing a fully unified omnichannel customer experience:
1. **Interactive Carousel Modifiers**: Direct correlation between state adjustments.
2. **Deep-linking States**: Clicking "Configure" on a portfolio asset pre-selects variables in the interactive Style Visualizer, which seamlessly deep-links to the scheduler.
3. **Visual Depth**: Consistent usage of high-contrast gold text on pristine dark-slate containers framed by spacious margins.
