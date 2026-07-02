import React, { useState } from 'react';

const STYLE_DETAILS = [
  {
    id: "style_1",
    name: "Signature French Bob",
    category: "Modern Cut",
    imageSrc: "https://images.unsplash.com/photo-1595475243695-4d5e20363b34?auto=format&fit=crop&q=80&w=800",
    description: "A sophisticated chin-grazing French bob characterized by texturized ends and effortless movement.",
    longDescription: "Our premier modern cut. The French Bob is customized to frame your cheekbones perfectly. Includes our signature texturizing technique to ensure low daily maintenance while retaining an incredibly high-fashion look.",
    basePrice: 150,
    durationMin: 60,
    maintenanceLevel: "Low to Medium",
    hairTypeRecommended: "Straight, Wavy, or Fine Hair",
    colorsAvailable: ["Chocolat Noir", "Honey Blonde", "Muted Rose Gold", "Natural Onyx"],
    lengthOptions: ["Classic Jawline", "Modern Cheekbone", "Textured Lob"]
  },
  {
    id: "style_2",
    name: "Hollywood Glam Blowout",
    category: "Classic Blowout",
    imageSrc: "https://images.unsplash.com/photo-1492106087820-71f1a00d2b11?auto=format&fit=crop&q=80&w=800",
    description: "High-volume, cascading glamour waves with exceptional natural shine and professional hold.",
    longDescription: "The ultimate red-carpet blowout. Designed to maximize your hair's natural density, incorporating a custom silk-protein treatment before styling with iconic ceramic round brushes for premium bounce and shine.",
    basePrice: 95,
    durationMin: 45,
    maintenanceLevel: "Temporary (Event-ready)",
    hairTypeRecommended: "All Hair Types & Lengths",
    colorsAvailable: ["Golden Highlights", "Satin Pearl Gloss", "Warm Amber", "No Tint"],
    lengthOptions: ["Max Volume Bounce", "Sleek Cascading Waves", "Structured Curls"]
  },
  {
    id: "style_3",
    name: "Asymmetrical Chic Chop",
    category: "Modern Cut",
    imageSrc: "https://images.unsplash.com/photo-1605497746445-97d1b0a9eeae?auto=format&fit=crop&q=80&w=800",
    description: "A bold, avant-garde geometrical haircut featuring asymmetrical lines and razor-sharp texturizing.",
    longDescription: "A striking statement hairstyle crafted for trendsetters. Marcus Vance performs precision measurements to shape a sharp, custom-angled side silhouette that highlights your facial structure beautifully.",
    basePrice: 165,
    durationMin: 75,
    maintenanceLevel: "Medium (requires trim every 6 weeks)",
    hairTypeRecommended: "Thick, Medium, or Straight Hair",
    colorsAvailable: ["Platinum Frost", "Midnight Obsidian", "Copper Ember", "Amethyst Haze"],
    lengthOptions: ["Dramatic Angle", "Subtle Sleek Gradient", "Bold Razor Edged"]
  },
  {
    id: "style_4",
    name: "Gilded Braided Crown",
    category: "Updos",
    imageSrc: "https://images.unsplash.com/photo-1519699047748-de8e457a634e?auto=format&fit=crop&q=80&w=800",
    description: "A majestic romantic updo featuring loose bohemian braiding decorated with delicate floral or gold pin accents.",
    longDescription: "Perfect for weddings, exclusive events, or black-tie gala nights. Sophia custom weaves a gorgeous crown braid tailored to your hair's volume, incorporating luxury subtle floral or golden details.",
    basePrice: 195,
    durationMin: 90,
    maintenanceLevel: "Single Occasion",
    hairTypeRecommended: "Medium to Long Hair",
    colorsAvailable: ["Antique Brass Pins", "Gilded Gold Leaves", "Fresh Pearl Accents", "Classic Minimalist"],
    lengthOptions: ["Bohemian Fishtail Crown", "Double Dutch Braided Updo", "Classic Roman Halo"]
  }
];

export default function StyleVisualizer({ defaultStyleName, onReserveStyle }) {
  // Find style if a default is passed from portfolio inspect click, else use first
  const initialStyle = STYLE_DETAILS.find(s => s.name === defaultStyleName) || STYLE_DETAILS[0];
  
  const [selectedStyle, setSelectedStyle] = useState(initialStyle);
  const [color, setColor] = useState(initialStyle.colorsAvailable[0]);
  const [length, setLength] = useState(initialStyle.lengthOptions[0]);
  const [selectedCategory, setSelectedCategory] = useState("All");

  const categories = ["All", "Modern Cut", "Classic Blowout", "Updos"];

  // If prop changes, sync internal state
  React.useEffect(() => {
    const matched = STYLE_DETAILS.find(s => s.name === defaultStyleName);
    if (matched) {
      setSelectedStyle(matched);
      setColor(matched.colorsAvailable[0]);
      setLength(matched.lengthOptions[0]);
    }
  }, [defaultStyleName]);

  const handleStyleChange = (style) => {
    setSelectedStyle(style);
    setColor(style.colorsAvailable[0]);
    setLength(style.lengthOptions[0]);
  };

  // Auto-select the first style of the new category to ensure real-time UI updates
  React.useEffect(() => {
    if (selectedCategory !== "All") {
      const stylesInCat = STYLE_DETAILS.filter(s => s.category === selectedCategory);
      if (stylesInCat.length > 0 && !stylesInCat.some(s => s.id === selectedStyle.id)) {
        handleStyleChange(stylesInCat[0]);
      }
    }
  }, [selectedCategory]);

  const filteredStyles = selectedCategory === "All"
    ? STYLE_DETAILS
    : STYLE_DETAILS.filter(s => s.category === selectedCategory);

  return (
    <div className="w-full max-w-4xl mx-auto px-4 py-6 animate-fadeIn">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-text-light font-luxury tracking-wide">
          Interactive Styling Studio
        </h2>
        <p className="text-text-muted text-sm mt-1">
          Customize elite silhouettes, lock in luxury tints and texturing finishes, and book your signature look.
        </p>
      </div>

      {/* Horizontal Category Selectors */}
      <div className="flex gap-2 overflow-x-auto pb-4 mb-4 scrollbar-thin">
        {categories.map((cat) => {
          const isSelected = selectedCategory === cat;
          return (
            <button
              key={cat}
              onClick={() => setSelectedCategory(cat)}
              className={`px-5 py-2.5 rounded-full text-xs font-bold whitespace-nowrap border transition-all duration-300 ${
                isSelected
                  ? 'bg-gold-primary text-charcoal-bg border-gold-primary shadow-md shadow-gold-primary/10'
                  : 'bg-charcoal-surface text-text-light border-charcoal-border hover:border-gold-primary/30'
              }`}
            >
              {cat}
            </button>
          );
        })}
      </div>

      {/* Horizontal Style Selectors */}
      <div className="flex gap-2 overflow-x-auto pb-4 mb-6 scrollbar-thin">
        {filteredStyles.map((style) => {
          const isSelected = style.id === selectedStyle.id;
          return (
            <button
              key={style.id}
              onClick={() => handleStyleChange(style)}
              className={`px-5 py-2.5 rounded-lg text-sm font-semibold whitespace-nowrap border transition-all duration-300 ${
                isSelected
                  ? 'bg-gold-primary/15 text-gold-primary border-gold-primary shadow-lg shadow-gold-primary/5'
                  : 'bg-charcoal-surface text-text-light border-charcoal-border hover:border-gold-secondary/40'
              }`}
            >
              {style.name}
            </button>
          );
        })}
      </div>

      {/* Hero Preview Box with Overlaid Elements */}
      <div className="bg-charcoal-surface rounded-2xl overflow-hidden border border-gold-primary/25 shadow-xl">
        <div className="grid grid-cols-1 md:grid-cols-2">
          {/* Visual Image Banner with tags */}
          <div className="relative h-64 md:h-auto min-h-[320px] aspect-square">
            <img
              src={selectedStyle.imageSrc}
              alt={selectedStyle.name}
              className="w-full h-full object-cover"
            />
            <div className="absolute top-4 left-4 bg-charcoal-bg/90 backdrop-blur-sm px-3.5 py-1.5 rounded-lg text-xs font-bold text-gold-primary tracking-wider uppercase border border-charcoal-border/50">
              {selectedStyle.category}
            </div>
            <div className="absolute bottom-4 left-4 bg-gold-primary text-charcoal-bg px-4 py-1.5 rounded-lg text-sm font-extrabold shadow-lg">
              EST. ${selectedStyle.basePrice}
            </div>
          </div>

          {/* Style Configuration Details */}
          <div className="p-6 md:p-8 flex flex-col justify-between">
            <div>
              <h3 className="text-2xl font-bold text-text-light tracking-wide font-luxury">
                {selectedStyle.name}
              </h3>
              <p className="text-text-muted text-sm mt-3 leading-relaxed">
                {selectedStyle.longDescription}
              </p>

              <hr className="border-charcoal-border my-6" />

              {/* Technical Specifications specs */}
              <div className="grid grid-cols-3 gap-2">
                <div>
                  <h4 className="text-[10px] font-bold text-gold-secondary uppercase tracking-widest">
                    MAINTENANCE
                  </h4>
                  <p className="text-text-light text-xs mt-1 font-medium">
                    {selectedStyle.maintenanceLevel}
                  </p>
                </div>
                <div>
                  <h4 className="text-[10px] font-bold text-gold-secondary uppercase tracking-widest">
                    HAIR PROFILE
                  </h4>
                  <p className="text-text-light text-xs mt-1 font-medium">
                    {selectedStyle.hairTypeRecommended}
                  </p>
                </div>
                <div>
                  <h4 className="text-[10px] font-bold text-gold-secondary uppercase tracking-widest">
                    DURATION
                  </h4>
                  <p className="text-text-light text-xs mt-1 font-medium">
                    {selectedStyle.durationMin} mins
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Control Panel state modifiers */}
      <div className="mt-8">
        <h3 className="text-base font-bold text-gold-primary tracking-wider mb-3">
          TAILOR YOUR STYLE PROFILE
        </h3>
        <div className="bg-charcoal-surface rounded-xl p-5 border border-charcoal-border">
          {/* Colors */}
          <div className="mb-6">
            <span className="text-xs font-bold text-text-muted tracking-widest uppercase">
              Dye Tint & Highlights
            </span>
            <div className="grid grid-cols-2 sm:grid-cols-4 gap-3 mt-3">
              {selectedStyle.colorsAvailable.map((c) => {
                const isSel = c === color;
                return (
                  <button
                    key={c}
                    onClick={() => setColor(c)}
                    className={`h-11 rounded-lg text-xs font-semibold border transition-all ${
                      isSel
                        ? 'bg-gold-primary/10 text-gold-primary border-gold-primary shadow-sm'
                        : 'bg-charcoal-bg text-text-light border-charcoal-border hover:border-gold-primary/20'
                    }`}
                  >
                    {c}
                  </button>
                );
              })}
            </div>
          </div>

          {/* Lengths */}
          <div className="mb-4">
            <span className="text-xs font-bold text-text-muted tracking-widest uppercase">
              Texturing & Finish Profiles
            </span>
            <div className="grid grid-cols-2 sm:grid-cols-3 gap-3 mt-3">
              {selectedStyle.lengthOptions.map((l) => {
                const isSel = l === length;
                return (
                  <button
                    key={l}
                    onClick={() => setLength(l)}
                    className={`h-11 rounded-lg text-[11px] font-semibold border transition-all ${
                      isSel
                        ? 'bg-gold-primary/10 text-gold-primary border-gold-primary shadow-sm'
                        : 'bg-charcoal-bg text-text-light border-charcoal-border hover:border-gold-primary/20'
                    }`}
                  >
                    {l}
                  </button>
                );
              })}
            </div>
          </div>

          {/* Configuration string summary */}
          <div className="bg-charcoal-bg rounded-lg p-3.5 flex items-center gap-3 border border-charcoal-border/50">
            <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4 text-gold-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            <p className="text-xs text-text-light italic font-medium">
              Configured Selection: <span className="text-gold-secondary not-italic font-bold">{color}</span> with <span className="text-gold-secondary not-italic font-bold">{length}</span> finish.
            </p>
          </div>
        </div>
      </div>

      {/* Confirm & Deep Link Reservation Action */}
      <button
        onClick={() => onReserveStyle(selectedStyle, color, length)}
        className="w-full h-14 bg-gold-primary hover:bg-gold-secondary text-charcoal-bg font-extrabold text-sm tracking-widest rounded-xl transition-all duration-300 shadow-lg hover:shadow-gold-primary/10 mt-6 flex items-center justify-center gap-2"
      >
        RESERVE THIS CUSTOM LOOK
        <svg xmlns="http://www.w3.org/2000/svg" className="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M14 5l7 7m0 0l-7 7m7-7H3" />
        </svg>
      </button>
    </div>
  );
}
