/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        gold: {
          primary: '#D4AF37',   // Metallic Luxury Gold
          secondary: '#E5C158', // Warm Gold Accent
          tertiary: '#F5E6C4',  // Champagne / Soft Gold
        },
        charcoal: {
          bg: '#121212',        // Deepest charcoal base
          surface: '#1E1E1E',   // Slate/charcoal panels
          card: '#262626',      // Premium card elements
          border: '#3D3D3D',    // Subtle border separations
        },
        text: {
          light: '#F5F5F5',     // Crisp readable body text
          muted: '#B0B0B0',     // Elegant descriptions
        }
      },
      letterSpacing: {
        luxury: '0.25em',
        haute: '0.4em',
      },
      fontFamily: {
        luxury: ['Playfair Display', 'Didot', 'serif'],
        sans: ['Inter', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
