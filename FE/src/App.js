import './App.css';
import HeroSection from './components/heroSection/HeroSection';
import NewArrivals from './components/sections/NewArrival';
import content from './data/content.json';
import Category from './components/sections/categories/Category';

export default function App() {
  return (
    <>
      {/* <Navigation />  in pages/pdctP/pdctL.js*/}
      <HeroSection />
      <NewArrivals />
      {content?.pages?.shop?.sections && content?.pages?.shop?.sections?.map((item, index) => <Category key={item?.title+index} {...item} />)}
    </>
  );
}
