import React from 'react';
import SvgFavourite from '../../components/common/SvgFavourite'; // Giả sử đúng đường dẫn
import { Link } from 'react-router-dom';

const ProductCard = ({ id, title, description, price, discount, rating, brand, thumbnail, slug }) => {
  // Kiểm tra hoặc đặt giá trị mặc định cho thumbnail
  const imageSrc = thumbnail || 'https://placehold.co/600x400?text=No+Image'; // Placeholder nếu không có ảnh

  return (
    // Container ngoài cùng của Card: w-full để chiếm hết cột grid, thêm border, rounded, shadow và overflow
    <div className='flex flex-col w-full border border-gray-200 rounded-lg overflow-hidden shadow-sm hover:shadow-md transition-shadow duration-200 ease-in-out relative group'>

      {/* Container chứa ảnh với Aspect Ratio */}
      {/* Chọn 1 tỷ lệ: aspect-square (vuông), aspect-[4/3] (ngang), aspect-[3/4] (đứng) */}
      <Link to={`/product/${slug || id}`} className="block aspect-square overflow-hidden"> {/* Ví dụ: ảnh vuông */}
        <img
          // Ảnh chiếm toàn bộ container, object-cover để lấp đầy và cắt nếu cần
          // Thêm hiệu ứng zoom nhẹ khi hover vào card (group-hover)
          className={`h-full w-full object-cover transition-transform duration-300 ease-in-out group-hover:scale-105`}
          src={imageSrc}
          alt={title || 'Product Image'}
          loading="lazy" // Thêm lazy loading cho hiệu suất
        />
      </Link>

      {/* Khu vực nội dung Text */}
      <div className='p-4 flex flex-col flex-grow'> {/* Thêm padding và flex-grow để đẩy nội dung xuống nếu card có chiều cao khác nhau */}
        <div className='flex justify-between items-start mb-2'> {/* items-start để title và giá không bị lệch dọc */}
          {/* Phần Title và Brand */}
          <div className='flex-1 mr-2'> {/* flex-1 để title chiếm nhiều không gian hơn, mr-2 tạo khoảng cách với giá */}
             <Link to={`/product/${slug || id}`} className="block">
                 {/* Tăng cỡ chữ title, thêm truncate nếu quá dài */}
                 <h3 className='text-base font-semibold text-gray-800 hover:text-blue-600 truncate' title={title}>
                     {title || 'Untitled Product'}
                 </h3>
             </Link>
             {/* Giữ lại brand nếu có */}
             {brand && <p className='text-sm text-gray-500'>{brand}</p>}
          </div>
          {/* Phần Giá */}
          {/* Đảm bảo giá luôn hiển thị rõ ràng */}
          <p className='text-base font-bold text-gray-900'>${price?.toFixed(2) || '0.00'}</p>
        </div>

         {/* (Tùy chọn) Có thể thêm mô tả ngắn hoặc rating ở đây nếu cần */}
         {/* <p className='text-xs text-gray-600 line-clamp-2 mb-auto'>{description}</p> */}
         {/* <div className='mt-auto'>Rating...</div> */}

      </div>

      {/* Nút Favourite */}
      <button
        onClick={() => console.log("Favourite clicked:", id)}
        // Điều chỉnh vị trí, thêm nền nhẹ và hiệu ứng hover cho dễ thấy và tương tác
        className='absolute top-2 right-2 p-1.5 bg-white/70 rounded-full text-gray-600 hover:text-red-500 hover:bg-white transition-colors duration-200 z-10'
        aria-label="Add to favourites" // Thêm aria-label cho accessibility
      >
        <SvgFavourite className="w-5 h-5" /> {/* Kiểm soát kích thước icon */}
      </button>

       {/* (Tùy chọn) Hiển thị Discount Badge */}
       {discount > 0 && (
         <div className='absolute top-2 left-2 bg-red-500 text-white text-xs font-bold px-2 py-0.5 rounded-md z-10'>
           -{discount}%
         </div>
       )}
    </div>
  );
}

export default ProductCard;