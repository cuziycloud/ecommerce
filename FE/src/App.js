import './App.css';
import HeroSection from './components/heroSection/HeroSection';
import Navigation from './components/navigation/Navigation.js';
import { BrowserRouter } from 'react-router-dom';
import NewArrivals from './components/sections/NewArrival.jsx' 

export default function App() {
  return (
    <BrowserRouter>
      <Navigation />
      <HeroSection />
      <NewArrivals />
    </BrowserRouter>
    
  )
}
