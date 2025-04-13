import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Link, useLoaderData } from 'react-router-dom'; 
import Breadcrumb from '../../components/Breadcrumb/Breadcrumb';
// import content from '../../data/content.json'; 
import Rating from '../../components/Rating/Rating';
import SizeFilter from '../../components/filters/SizeFilter'; 
import ProductColors from './ProductColors'; 
import SvgCreditCard from '../../components/common/SvgCreditCard';
import SvgCloth from '../../components/common/SvgCloth';
import SvgShipping from '../../components/common/SvgShipping';
import SvgReturn from '../../components/common/SvgReturn';
import SectionHeading from '../../components/sections/secHeading/SecHeading';
import ProductCard from '../productListPage/ProductCard'; 
import { useDispatch, useSelector } from 'react-redux';
import _ from 'lodash';
import { getAllProducts } from '../../api/fetchProducts'; 
import { addItemToCartAction } from '../../store/actions/cartAction';

const extraSections = [
  { icon: <SvgCreditCard />, label: 'Thanh toán an toàn' },
  { icon: <SvgCloth />, label: 'Kích cỡ & Vừa vặn' },
  { icon: <SvgShipping />, label: 'Miễn phí vận chuyển' },
  { icon: <SvgReturn />, label: 'Miễn phí đổi trả' }
];

const ProductDetails = () => {
  const product = useLoaderData();
  const dispatch = useDispatch();

  const [currentImage, setCurrentImage] = useState('');
  const [selectedSize, setSelectedSize] = useState('');
  const [error, setError] = useState('');
  const cartItems = useSelector((state) => state.cartState?.cart);
  const categories = useSelector((state) => state?.categoryState?.categories);
  const [similarProducts, setSimilarProducts] = useState([]);
  const productCategory = useMemo(() => {
    if (!product || !categories) return null;
    return categories.find((category) => category?.id === product?.categoryId);
  }, [product, categories]);

  //Breadcrumb links
  const breadCrumbLinks = useMemo(() => {
    if (!productCategory) return [{ title: 'Shop', path: '/' }]; // Default breadcrumb

    const links = [{ title: 'Shop', path: '/' }];
    if (productCategory) {
      links.push({
        title: productCategory.name,
        path: `/${productCategory.path || productCategory.code?.toLowerCase() || productCategory.name.toLowerCase()}`
      });
    }
    const productType = productCategory?.types?.find((item) => item?.id === product?.categoryTypeId); // Kiểm tra lại key 'types' và 'categoryTypeId'
    if (productType) {
       links.push({
         title: productType.name,
       });
    }
     links.push({ title: product.name }); 

    return links;
  }, [product, productCategory]);

  useEffect(() => {
    if (product?.categoryId) { // Chỉ gọi API nếu có categoryId
      getAllProducts(product.categoryId, product.categoryTypeId)
        .then(res => {
          const excludedProduct = res?.filter((item) => item?.id !== product?.id) || [];
          setSimilarProducts(excludedProduct);
        })
        .catch(err => {
          console.error("Error fetching similar products:", err);
          setSimilarProducts([]); // Đặt lại thành mảng rỗng nếu lỗi
        });
    }
  }, [product?.categoryId, product?.categoryTypeId, product?.id]);

  useEffect(() => {
    if (product?.thumbnail) {
      setCurrentImage(product.thumbnail);
    }
  }, [product?.thumbnail]);

  const handleAddToCart = useCallback(() => {
    if (!selectedSize) {
      setError('Vui lòng chọn kích cỡ.');
      return; // Dừng lại nếu chưa chọn size
    }
    setError(''); // Xóa lỗi nếu đã chọn size

    const selectedVariant = product?.variants?.find((variant) => variant?.size === selectedSize);

    if (!selectedVariant) {
        setError('Không tìm thấy biến thể sản phẩm cho kích cỡ này.');
        return;
    }

    // Kiểm tra số lượng tồn kho (stockQuantity) - Kiểm tra key từ API/Redux
    if (selectedVariant?.stockQuantity > 0) {
      dispatch(addItemToCartAction({
        productId: product.id,
        thumbnail: product.thumbnail,
        name: product.name,
        variant: selectedVariant,
        quantity: 1,
        price: selectedVariant.price || product.price,
        subTotal: selectedVariant.price || product.price, 
      }));
      console.log("Added to cart:", product.name, selectedVariant);
    } else {
      setError('Sản phẩm tạm hết hàng cho kích cỡ này.');
    }
  }, [dispatch, product, selectedSize]);

  useEffect(() => {
    if (selectedSize) {
      setError('');
    }
  }, [selectedSize]);

  const availableColors = useMemo(() => {
    if (!product?.variants) return [];
    return _.uniqBy(_.map(product.variants, variant => ({ name: variant.color, code: variant.colorCode })), 'name'); 
  }, [product?.variants]);

  const availableSizes = useMemo(() => {
    if (!product?.variants) return [];
    return _.uniq(_.map(product.variants, 'size'));
  }, [product?.variants]);


  // Xử lý trường hợp loader không trả về product
  if (!product) {
    return (
      <div className="flex justify-center items-center h-screen">
        <p className="text-xl text-gray-600">Không tìm thấy sản phẩm!</p>
      </div>
    );
  }

  const productImages = product?.productResources || [];


  return (
    <div className="container mx-auto px-4 py-8 md:px-6 lg:px-8">
      {/* Breadcrumb */}
      <Breadcrumb links={breadCrumbLinks} />

      {/* Layout chính: Ảnh và Thông tin */}
      <div className='flex flex-col md:flex-row mt-6 gap-8 lg:gap-12'>

        {/* Cột Ảnh */}
        <div className='w-full md:w-1/2 lg:w-5/12'> 
          <div className='flex flex-col-reverse md:flex-row gap-4'>
            {/* Thumbnails dọc */}
            <div className='flex md:flex-col gap-2 justify-center md:justify-start flex-wrap md:flex-nowrap md:w-1/6'>
              {/* Luôn hiển thị thumbnail gốc */}
              <button
                  onClick={() => setCurrentImage(product.thumbnail)}
                  className={`rounded-md overflow-hidden border-2 ${currentImage === product.thumbnail ? 'border-black' : 'border-transparent'} hover:border-gray-400`}
                  aria-label="Xem ảnh chính"
                >
                  <img
                    src={product.thumbnail}
                    className='h-16 w-16 object-cover cursor-pointer transition-opacity duration-200 hover:opacity-80'
                    alt={`Thumbnail chính`}
                  />
              </button>
              {/* Các ảnh phụ */}
              {productImages.map((item, index) => (
                <button
                  key={item.id || index} // Ưu tiên dùng ID nếu có
                  onClick={() => setCurrentImage(item.url)}
                  className={`rounded-md overflow-hidden border-2 ${currentImage === item.url ? 'border-black' : 'border-transparent'} hover:border-gray-400`}
                  aria-label={`Xem ảnh ${index + 1}`}
                >
                  <img
                    src={item.url}
                    className='h-16 w-16 object-cover cursor-pointer transition-opacity duration-200 hover:opacity-80'
                    alt={`Ảnh ${index + 1}`}
                  />
                </button>
              ))}
            </div>

            {/* Ảnh chính */}
            <div className='flex-1 flex justify-center items-start'>
              <img
                src={currentImage || product.thumbnail} // Dùng currentImage hoặc thumbnail gốc
                // Giữ tỷ lệ vuông hoặc tỷ lệ khác nếu muốn (aspect-[3/4])
                className='w-full aspect-square rounded-lg border border-gray-200 object-cover shadow-sm'
                alt={product.name}
              />
            </div>
          </div>
        </div>

        {/* Cột Thông tin */}
        <div className='w-full md:w-1/2 lg:w-7/12'> {/* Chiếm khoảng 7/12 trên màn lớn */}
          <h1 className='text-2xl lg:text-3xl font-bold text-gray-900'>{product.name}</h1>
          {/* Rating - thêm khoảng cách trên */}
          <div className="mt-2">
              <Rating rating={product.rating} />
          </div>

          {/* Giá - thêm khoảng cách trên */}
          <p className='text-2xl font-bold text-gray-800 mt-4'>${product.price?.toFixed(2)}</p>
          {/* Thêm giá gốc nếu có discount */}
          {product.originalPrice > product.price && (
              <p className='text-sm text-gray-500 line-through ml-2'>
                  ${product.originalPrice?.toFixed(2)}
              </p>
          )}


          {/* Bộ lọc Size - thêm khoảng cách */}
          <div className='mt-6'>
            <div className='flex justify-between items-center mb-2'>
              <p className='text-sm font-medium text-gray-900'>Chọn Kích Cỡ</p>
              <Link className='text-sm text-blue-600 hover:underline' to={'/size-guide'} target='_blank'>
                Hướng dẫn chọn size
              </Link>
            </div>
            {/* Component SizeFilter cần có prop để nhận size đã chọn và callback */}
            <SizeFilter
              sizes={availableSizes}
              selectedSize={selectedSize} // Truyền state vào
              onChange={(size) => setSelectedSize(size)} // Callback để cập nhật state
              multi={false} // Đảm bảo chỉ chọn 1
            />
             {/* Hiển thị lỗi size ngay dưới */}
             {error && error.includes('kích cỡ') && <p className='mt-1 text-sm text-red-600'>{error}</p>}
          </div>

          {/* Màu sắc - thêm khoảng cách */}
          {availableColors.length > 0 && (
            <div className='mt-6'>
              <p className='text-sm font-medium text-gray-900 mb-2'>Màu sắc có sẵn</p>
              {/* Component ProductColors cần được style */}
              <ProductColors colors={availableColors} />
            </div>
          )}

          {/* Nút Add to Cart và lỗi khác */}
          <div className='mt-8'>
             {/* Hiển thị lỗi khác (vd: hết hàng) */}
             {error && !error.includes('kích cỡ') && <p className='mb-2 text-sm text-red-600'>{error}</p>}
            <button
              onClick={handleAddToCart}
              // Dùng padding thay vì width cố định, làm nút to hơn, dễ nhấn hơn
              className='flex items-center justify-center w-full px-8 py-3 bg-black text-white rounded-lg hover:bg-gray-800 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-black transition duration-150 ease-in-out disabled:opacity-50 disabled:cursor-not-allowed'
              // Disable nút nếu đang có lỗi liên quan đến size hoặc không có size
              // disabled={!selectedSize || (error && error.includes('kích cỡ'))}
            >
              <svg width="20" height="20" className='mr-2' viewBox="0 0 17 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M1.5 1.33325H2.00526C2.85578 1.33325 3.56986 1.97367 3.6621 2.81917L4.3379 9.014C4.43014 9.8595 5.14422 10.4999 5.99474 10.4999H13.205C13.9669 10.4999 14.6317 9.98332 14.82 9.2451L15.9699 4.73584C16.2387 3.68204 15.4425 2.65733 14.355 2.65733H4.5M4.52063 13.5207H5.14563M4.52063 14.1457H5.14563M13.6873 13.5207H14.3123M13.6873 14.1457H14.3123M5.66667 13.8333C5.66667 14.2935 5.29357 14.6666 4.83333 14.6666C4.3731 14.6666 4 14.2935 4 13.8333C4 13.373 4.3731 12.9999 4.83333 12.9999C5.29357 12.9999 5.66667 13.373 5.66667 13.8333ZM14.8333 13.8333C14.8333 14.2935 14.4602 14.6666 14 14.6666C13.5398 14.6666 13.1667 14.2935 13.1667 13.8333C13.1667 13.373 13.5398 12.9999 14 12.9999C14.4602 12.9999 14.8333 13.373 14.8333 13.8333Z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
              </svg>
              Thêm vào giỏ hàng
            </button>
          </div>

          {/* Extra Sections - style lại */}
          <div className='grid grid-cols-2 gap-x-6 gap-y-4 mt-8 border-t border-gray-200 pt-6'>
            {extraSections.map((section, index) => (
              <div key={index} className='flex items-center text-sm text-gray-700'>
                <span className="text-gray-500 mr-2">{React.cloneElement(section.icon, { className: 'w-5 h-5' })}</span>
                {section.label}
              </div>
            ))}
          </div>
        </div>
      </div>

      {/* Phần Mô tả Sản phẩm - Tách biệt rõ ràng */}
      <div className="mt-12 lg:mt-16 border-t border-gray-200 pt-8">
        <SectionHeading title={'Mô Tả Sản Phẩm'} />
        <div className='prose prose-sm sm:prose lg:prose-lg max-w-none text-gray-700 mt-4 px-4 md:px-0'>
          {/* Sử dụng prose để tự động style text */}
          <p>{product.description}</p>
          {/* Thêm các đoạn mô tả khác nếu có */}
        </div>
      </div>

      {/* Phần Sản phẩm Tương tự */}
      <div className="mt-12 lg:mt-16">
        <SectionHeading title={'Sản Phẩm Tương Tự'} />
        <div className='mt-6 grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 md:gap-6'>
          {similarProducts.length > 0 ? (
             similarProducts.map((item) => (
                // Đảm bảo ProductCard nhận đủ props và có key duy nhất
                <ProductCard key={item.id} {...item} />
             ))
          ) : (
            // Style lại thông báo nếu không có sản phẩm
            <p className="col-span-full text-center text-gray-500 py-8">Không tìm thấy sản phẩm tương tự.</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default ProductDetails;