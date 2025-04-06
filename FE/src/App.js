import './App.css';
import HeroSection from './components/heroSection/HeroSection';
import Navigation from './components/Navigation/Navigation';
import { BrowserRouter } from 'react-router-dom';
import NewArrivals from './components/Sections_tmp/NewArrivals_tmp.jsx' 

export default function App() {
  return (
    <BrowserRouter>
      <Navigation />
      <HeroSection />
      <NewArrivals />
    </BrowserRouter>
    
  )
}
