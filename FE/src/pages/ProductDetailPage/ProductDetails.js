import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { Link, useLoaderData } from 'react-router-dom';
import Breadcrumb from '../../components/Breadcrumb/Breadcrumb';
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

  const categories = useSelector((state) => state?.categoryState?.categories);
  const [similarProducts, setSimilarProducts] = useState([]);

  const productCategory = useMemo(() => {
    if (!product || !categories) return null;
    return categories.find((category) => category?.id === product?.category_id); // Match key from JSON
  }, [product, categories]);

  const breadCrumbLinks = useMemo(() => {
    const links = [{ title: 'Shop', path: '/' }];
    if (productCategory) {
      links.push({
        title: productCategory.name,
        path: productCategory.path || `/${productCategory.code?.toLowerCase()}`
      });
    }

     const productType = productCategory?.types?.find((item) => item?.id === product?.type_id);
     if (productType) {
       links.push({
         title: productType.name,
       });
     }
    if (product) {
      links.push({ title: product.name });
    }
    return links;
  }, [product, productCategory]);

  useEffect(() => {
    if (product?.category_id) {

      getAllProducts(product.category_id) 
        .then(res => {
          const excludedProduct = res?.filter((item) => item?.id !== product?.id) || [];
          setSimilarProducts(excludedProduct);
        })
        .catch(err => {
          console.error("Error fetching similar products:", err);
          setSimilarProducts([]);
        });
    }
  }, [product?.category_id, product?.id]);


  useEffect(() => {
    if (product?.thumbnail) {
      setCurrentImage(product.thumbnail);
    }
     setSelectedSize('');
     setError('');
  }, [product]);

  const handleAddToCart = useCallback(() => {
    if (!selectedSize) {
      setError('Vui lòng chọn kích cỡ.');
      return;
    }
    setError('');
    const selectedVariant = product?.variants?.find((variant) => variant?.size === selectedSize);

    if (!product.variants && product.size?.includes(selectedSize)) {
        dispatch(addItemToCartAction({
          productId: product.id,
          thumbnail: product.thumbnail,
          name: product.name,
          variant: { size: selectedSize, color: product.color?.[0] || 'Default' },
          quantity: 1,
          price: product.price,
          subTotal: product.price,
        }));
        console.log("Added basic product to cart:", product.name, selectedSize);
        return; 
    }

    if (!selectedVariant) {
        setError('Không tìm thấy biến thể sản phẩm cho kích cỡ này.');
        return;
    }

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
      console.log("Added variant to cart:", product.name, selectedVariant);
    } else {
      setError('Sản phẩm tạm hết hàng cho kích cỡ này.');
    }
  }, [dispatch, product, selectedSize]);

  useEffect(() => {
    if (selectedSize) {
      setError('');
    }
  }, [selectedSize]);

  const availableSizes = useMemo(() => {
    if (product?.variants) {
      return _.uniq(_.map(product.variants, 'size'));
    }
    return product?.size || []; 
  }, [product]);

   const availableColors = useMemo(() => {
     if (product?.variants) {
       return _.uniqBy(_.map(product.variants, v => ({ name: v.color, code: v.colorCode || null })), 'name');
     }
     return product?.color?.map(c => ({ name: c, code: null })) || [];
   }, [product]);

  if (!product) {
    return (
      <div className="flex justify-center items-center min-h-[60vh]">
        <p className="text-xl text-gray-500">Không tìm thấy sản phẩm!</p>
      </div>
    );
  }

  const productImages = product?.images || [];

  return (
    <div className="container mx-auto px-4 sm:px-6 lg:px-8 py-8 sm:py-12">
      <div className="mb-6 sm:mb-8">
        <Breadcrumb links={breadCrumbLinks} />
      </div>

      <div className='flex flex-col md:flex-row gap-8 lg:gap-16'>

        <div className='w-full md:w-1/2 lg:w-5/12'>
          <div className='flex flex-col-reverse md:flex-row gap-4'>
            <div className='flex md:flex-col gap-3 justify-center md:justify-start flex-wrap md:flex-nowrap md:w-auto'>
              <button
                  onClick={() => setCurrentImage(product.thumbnail)}
                  className={`rounded-lg overflow-hidden border-2 p-0.5 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 ${currentImage === product.thumbnail ? 'border-indigo-600' : 'border-transparent'} hover:border-gray-300`}
                  aria-label="Xem ảnh chính"
                >
                  <img
                    src={product.thumbnail}
                    className='h-16 w-16 lg:h-20 lg:w-20 object-cover cursor-pointer transition duration-150 ease-in-out'
                    alt={`Thumbnail chính`}
                  />
              </button>
              {productImages.map((imgUrl, index) => (
                <button
                  key={index}
                  onClick={() => setCurrentImage(imgUrl)}
                  className={`rounded-lg overflow-hidden border-2 p-0.5 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 ${currentImage === imgUrl ? 'border-indigo-600' : 'border-transparent'} hover:border-gray-300`}
                  aria-label={`Xem ảnh ${index + 1}`}
                >
                  <img
                    src={imgUrl}
                    className='h-16 w-16 lg:h-20 lg:w-20 object-cover cursor-pointer transition duration-150 ease-in-out'
                    alt={`Ảnh ${index + 1}`}
                  />
                </button>
              ))}
            </div>

            <div className='flex-1'>
              <img
                src={currentImage || product.thumbnail}
                className='w-full aspect-square rounded-xl border border-gray-200 object-cover shadow-sm'
                alt={product.name}
              />
            </div>
          </div>
        </div>

        <div className='w-full md:w-1/2 lg:w-7/12'>
          <h1 className='text-2xl sm:text-3xl font-bold tracking-tight text-gray-900'>{product.name}</h1>
          {product.rating && (
            <div className="mt-3">
                <Rating rating={product.rating} />
            </div>
          )}

          <div className='mt-4 flex items-baseline gap-x-2'>
             <p className='text-3xl font-bold tracking-tight text-gray-900'>${product.price?.toFixed(2)}</p>
               {product.discount > 0 && product.price && (
                 <p className='text-lg text-gray-500 line-through'>
                     ${(product.price / (1 - product.discount / 100)).toFixed(2)}
                 </p>
             )}
          </div>


          {availableSizes.length > 0 && (
            <div className='mt-8'>
              <div className='flex justify-between items-center mb-3'>
                <p className='text-base font-medium text-gray-900'>Chọn Kích Cỡ</p>
                <Link className='text-sm font-medium text-indigo-600 hover:text-indigo-500' to={'/size-guide'} target='_blank'>
                  Hướng dẫn chọn size
                </Link>
              </div>
              <SizeFilter
                sizes={availableSizes}
                selectedSize={selectedSize}
                onChange={(size) => setSelectedSize(size)}
                multi={false}
              />
              {error && error.includes('kích cỡ') && <p className='mt-2 text-sm text-red-600'>{error}</p>}
            </div>
          )}

          {availableColors.length > 0 && (
            <div className='mt-8'>
              <p className='text-base font-medium text-gray-900 mb-3'>Màu sắc có sẵn</p>
              <ProductColors colors={availableColors} />
            </div>
          )}

          <div className='mt-8'>
             {error && !error.includes('kích cỡ') && <p className='mb-3 text-sm text-red-600'>{error}</p>}
            <button
              onClick={handleAddToCart}
              disabled={availableSizes.length > 0 && !selectedSize} // Disable if sizes exist but none selected
              className='flex items-center justify-center w-full px-8 py-3 bg-gray-900 text-base font-medium text-white rounded-lg border border-transparent hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-gray-900 transition duration-150 ease-in-out disabled:opacity-50 disabled:cursor-not-allowed'
            >
              <svg width="20" height="20" className='mr-2 -ml-1' viewBox="0 0 17 16" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M1.5 1.33325H2.00526C2.85578 1.33325 3.56986 1.97367 3.6621 2.81917L4.3379 9.014C4.43014 9.8595 5.14422 10.4999 5.99474 10.4999H13.205C13.9669 10.4999 14.6317 9.98332 14.82 9.2451L15.9699 4.73584C16.2387 3.68204 15.4425 2.65733 14.355 2.65733H4.5M4.52063 13.5207H5.14563M4.52063 14.1457H5.14563M13.6873 13.5207H14.3123M13.6873 14.1457H14.3123M5.66667 13.8333C5.66667 14.2935 5.29357 14.6666 4.83333 14.6666C4.3731 14.6666 4 14.2935 4 13.8333C4 13.373 4.3731 12.9999 4.83333 12.9999C5.29357 12.9999 5.66667 13.373 5.66667 13.8333ZM14.8333 13.8333C14.8333 14.2935 14.4602 14.6666 14 14.6666C13.5398 14.6666 13.1667 14.2935 13.1667 13.8333C13.1667 13.373 13.5398 12.9999 14 12.9999C14.4602 12.9999 14.8333 13.373 14.8333 13.8333Z" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" />
              </svg>
              Thêm vào giỏ hàng
            </button>
          </div>

          <div className='grid grid-cols-1 sm:grid-cols-2 gap-x-6 gap-y-5 mt-10 pt-8 border-t border-gray-200'>
            {extraSections.map((section, index) => (
              <div key={index} className='flex items-center text-sm text-gray-600'>
                <span className="text-gray-400 mr-3 flex-shrink-0">{React.cloneElement(section.icon, { className: 'w-6 h-6' })}</span>
                <span className="flex-grow">{section.label}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      {product.description && (
        <div className="mt-12 lg:mt-16 pt-10 border-t border-gray-200">
          <SectionHeading title={'Mô Tả Sản Phẩm'} />
          <div className='mt-6 prose prose-gray max-w-none text-gray-600 prose-sm sm:prose-base'>
            <p>{product.description}</p>
          </div>
        </div>
      )}

      <div className="mt-12 lg:mt-16 pt-10 border-t border-gray-200">
        <SectionHeading title={'Sản Phẩm Tương Tự'} />
        <div className='mt-8 grid grid-cols-2 gap-x-4 gap-y-8 sm:grid-cols-3 lg:grid-cols-4 sm:gap-x-6 lg:gap-x-8 lg:gap-y-10'>
          {similarProducts.length > 0 ? (
             similarProducts.map((item) => (
                <ProductCard key={item.id} {...item} />
             ))
          ) : (
            <p className="col-span-full text-center text-gray-500 py-10">Không tìm thấy sản phẩm tương tự.</p>
          )}
        </div>
      </div>
    </div>
  );
}

export default ProductDetails;