import React from 'react';
import SvgFavourite from '../../components/common/SvgFavourite'; 
import { Link } from 'react-router-dom';

const ProductCard = ({ id, title, description, price, discount, rating, brand, thumbnail, slug }) => {
  const imageSrc = thumbnail || 'https://placehold.co/600x400?text=No+Image'; 

  return (
    <div className='flex flex-col w-full border border-gray-200 rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200 ease-in-out relative group'>
      <Link to={`/product/${id}`} className="block aspect-square overflow-hidden"> 
        <img
          className={`h-full w-full object-cover transition-transform duration-300 ease-in-out group-hover:scale-105`}
          src={thumbnail}
          alt={title || 'Product Image'}
          loading="lazy" 
        />
      </Link>

      <div className='p-4 flex flex-col flex-grow'> 
        <div className='flex justify-between items-start mb-2'> 
          <div className='flex-1 mr-2'> 
             <Link to={`/product/${slug || id}`} className="block">
                 <h3 className='text-base font-semibold text-gray-800 hover:text-blue-600 truncate' title={title}>
                     {title || 'Untitled Product'}
                 </h3>
             </Link>
             {brand && <p className='text-sm text-gray-500'>{brand}</p>}
          </div>
          {/* Phần Giá */}
          <p className='text-base font-bold text-gray-900'>${price?.toFixed(2) || '0.00'}</p>
        </div>

      </div>

      {/* Nút Favourite */}
      <button
        onClick={() => console.log("Favourite clicked:", id)}
        className='absolute top-2 right-2 p-1.5 bg-white/70 rounded-full text-gray-600 hover:text-red-500 hover:bg-white transition-colors duration-200 z-10'
        aria-label="Add to favourites" 
      >
        <SvgFavourite className="w-5 h-5" /> 
      </button>

       {discount > 0 && (
         <div className='absolute top-2 left-2 bg-red-500 text-white text-xs font-bold px-2 py-0.5 rounded-md z-10'>
           -{discount}%
         </div>
       )}
    </div>
  );
}

export default ProductCard;