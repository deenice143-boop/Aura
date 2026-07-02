import React, { useState } from 'react';

// Portfolio initial mock state
const INITIAL_PORTFOLIO = [
  {
    id: "p_1",
    title: "Signature French Bob",
    stylistName: "Elena Rostova",
    likesCount: 142,
    isLiked: false,
    imageSrc: "https://images.unsplash.com/photo-1595475243695-4d5e20363b34?auto=format&fit=crop&q=80&w=600", // High-end representative Unsplash image
    category: "Modern Cut"
  },
  {
    id: "p_2",
    title: "Hollywood Glam Blowout",
    stylistName: "Alexander Mercer",
    likesCount: 328,
    isLiked: false,
    imageSrc: "https://images.unsplash.com/photo-1492106087820-71f1a00d2b11?auto=format&fit=crop&q=80&w=600",
    category: "Classic Blowout"
  },
  {
    id: "p_3",
    title: "Asymmetrical Chic Chop",
    stylistName: "Marcus Vance",
    likesCount: 94,
    isLiked: false,
    imageSrc: "https://images.unsplash.com/photo-1605497746445-97d1b0a9eeae?auto=format&fit=crop&q=80&w=600",
    category: "Modern Cut"
  },
  {
    id: "p_4",
    title: "Gilded Braided Crown",
    stylistName: "Sophia Loren",
    likesCount: 215,
    isLiked: false,
    imageSrc: "https://images.unsplash.com/photo-1519699047748-de8e457a634e?auto=format&fit=crop&q=80&w=600",
    category: "Updos"
  }
];

export default function PortfolioGallery({ onSelectStyle }) {
  const [items, setItems] = useState(INITIAL_PORTFOLIO);
  const [selectedCategory, setSelectedCategory] = useState("All");

  const toggleLike = (id, e) => {
    e.stopPropagation(); // Avoid triggering look selection
    setItems(prev =>
      prev.map(item => {
        if (item.id === id) {
          const liked = !item.isLiked;
          return {
            ...item,
            isLiked: liked,
            likesCount: liked ? item.likesCount + 1 : item.likesCount - 1
          };
        }
        return item;
      })
    );
  };

  const categories = ["All", "Modern Cut", "Classic Blowout", "Updos"];

  const filteredItems = selectedCategory === "All"
    ? items
    : items.filter(item => item.category === selectedCategory);

  return (
    <div className="w-full max-w-6xl mx-auto px-4 py-6 animate-fadeIn">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-text-light font-luxury tracking-wide">
          Curated Haute Portfolio
        </h2>
        <p className="text-text-muted text-sm mt-1">
          Explore iconic, high-fashion creations styled by our resident master artisans. Click any design to customize and book.
        </p>
      </div>

      {/* Luxury Category Pills with smooth active highlight borders */}
      <div className="flex gap-2 overflow-x-auto pb-4 mb-6 scrollbar-none">
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

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 transition-all duration-500">
        {filteredItems.map((item) => (
          <div
            key={item.id}
            onClick={() => onSelectStyle(item.title)}
            className="group cursor-pointer bg-charcoal-surface rounded-xl overflow-hidden border border-charcoal-border hover:border-gold-primary/60 transition-all duration-300 flex flex-col justify-between transform hover:-translate-y-1 animate-scaleIn"
          >
            {/* Image Box */}
            <div className="relative aspect-[4/5] w-full overflow-hidden">
              <img
                src={item.imageSrc}
                alt={item.title}
                className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
                loading="lazy"
              />
              {/* Gold overlay card header */}
              <div className="absolute top-3 left-3 bg-charcoal-bg/90 px-3 py-1 rounded text-[10px] font-bold text-gold-primary tracking-widest uppercase">
                {item.category}
              </div>

              {/* Float Like button */}
              <button
                onClick={(e) => toggleLike(item.id, e)}
                className="absolute bottom-3 right-3 bg-charcoal-bg/80 hover:bg-charcoal-bg text-text-light rounded-full p-2.5 transition-colors shadow-lg border border-charcoal-border/50"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  viewBox="0 0 24 24"
                  fill={item.isLiked ? "#EF4444" : "currentColor"}
                  className={`w-4 h-4 ${item.isLiked ? 'text-red-500' : 'text-gold-primary'}`}
                >
                  <path d="M11.645 20.91l-.007-.003-.003-.001a3.752 3.752 0 01-1.896-3.23c0-2.316 2.1-4.323 4.145-5.918 2.122-1.655 4.318-2.103 4.318-3.762 0-.83-.67-1.5-1.5-1.5a1.5 1.5 0 00-1.5 1.5c0 .414-.336.75-.75.75h-1.5a.75.75 0 01-.75-.75 3 3 0 013-3c1.657 0 3 1.343 3 3 0 2.535-3.078 3.518-4.836 4.93-.82.658-1.503 1.325-1.87 2.028a4.992 4.992 0 00-.317 1.488H11.645z" />
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                </svg>
              </button>
            </div>

            {/* Description Area */}
            <div className="p-4 flex flex-col justify-between flex-grow">
              <div>
                <h3 className="text-text-light font-bold text-base tracking-wide group-hover:text-gold-primary transition-colors line-clamp-1">
                  {item.title}
                </h3>
                <p className="text-text-muted text-xs mt-0.5 font-sans">
                  crafted by {item.stylistName}
                </p>
              </div>

              <div className="mt-4 pt-3 border-t border-charcoal-border/40 flex items-center justify-between">
                <span className="text-[11px] text-text-muted flex items-center gap-1">
                  <span className="text-red-500">♥</span> {item.likesCount} appreciations
                </span>
                <span className="text-xs text-gold-primary font-bold flex items-center gap-1 hover:underline">
                  Configure 
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M9 5l7 7-7 7" />
                  </svg>
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
