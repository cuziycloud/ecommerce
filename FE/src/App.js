import './App.css';
import HeroSection from './components/heroSection/HeroSection';
import Navigation from './components/navigation/Navigation';
import { BrowserRouter } from 'react-router-dom';
import NewArrivals from './components/sections/NewArrival' 


export default function App() {
  return (
    <BrowserRouter>
      <Navigation />
      <HeroSection />
      <NewArrivals />
    </BrowserRouter>
    
  )
}
