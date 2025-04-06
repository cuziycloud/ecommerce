import './App.css';
import HeroSection from './components/heroSection/HeroSection';
import Navigation from './components/navigation/Navigation';
import { BrowserRouter } from 'react-router-dom';
import NewArrivals from './components/sections/NewArrival' 
import content from './data/content.json';
import Category from './components/sections/categories/Category';
import Footer from './components/footer/Footer';


export default function App() {
  return (
    <BrowserRouter>
      <Navigation />
      <HeroSection />
      <NewArrivals />
      {content?.pages?.shop?.sections && content?.pages?.shop?.sections?.map((item, index) => <Category key={item?.title+index} {...item} />)}
      <Footer />
    </BrowserRouter>
    
  )
}
