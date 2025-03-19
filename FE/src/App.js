import './App.css';
import Navigation from './customer/components/Navigation/Navigation';
import Product from './customer/components/Product/Product';
import ProductCard from './customer/components/Product/ProductCard';
import HomePage from './customer/pages/HomePage/HomePage';

function App() {
  return (
    <div className="">
      <Navigation/>
      <div>
      {/*<HomePage/>*/}
      <Product/>
      </div>
    </div>
  );
}

export default App;
