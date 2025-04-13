import React from 'react';
import { colorSelector } from '../../components/filters/ColorsFilter';

const ProductColors = ({ colors }) => {
  // console.log("Flag 1", colors); 
  const validColors = colors?.filter(colorObj => colorSelector[colorObj.name]) || [];

  return (
    <div className='flex flex-wrap gap-3 pt-2'>
        {validColors.map((colorObj) => {
            const colorName = colorObj.name;
            const hexColor = colorSelector[colorName];
            const borderClass = (hexColor === '#FFFFFF' || hexColor === '#F5F5F5' || hexColor === '#FAF0E6' || hexColor === '#E1E1E1' || hexColor === '#E5E4E2') ? 'border border-gray-300' : '';

            return (
                <div
                    key={colorName} 
                    title={colorName} 
                    className={`rounded-full w-6 h-6 cursor-pointer shadow-sm ${borderClass}`} 
                    style={{ backgroundColor: hexColor }} 
                 >
                 </div>
            )
        })}
         {validColors.length === 0 && <p className="text-xs text-gray-500">Không có thông tin màu.</p>}
    </div>
  )
}

export default ProductColors;