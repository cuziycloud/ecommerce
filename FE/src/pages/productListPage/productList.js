import React, { useEffect, useMemo, useState } from 'react'
import FilterIcon from '../../components/common/FilterIcon';
import content from '../../data/content.json';
import Categories from '../../components/filters/Categories';
import PriceFilter from '../../components/filters/PriceFilter';
import ColorsFilter from '../../components/filters/ColorsFilter';
import SizeFilter from '../../components/filters/SizeFilter';
import ProductCard from './ProductCard';
import { getAllProducts } from '../../api/fetchProducts';
import { useDispatch, useSelector } from 'react-redux';
import { setLoading } from '../../store/features/common'

const categories = content?.categories;  //lay trong content.json

const ProductList = ({categoryType}) => {

  const categoryData = useSelector((state)=> state?.categoryState?.categories);

  const dispatch = useDispatch();
  const [products, setProducts] = useState([]);
  console.log("categoryType: ", categoryType)

  const categoryContent = useMemo(()=>{
    return categories?.find((category)=> category.code === categoryType);
  },[categoryType]);

  console.log("hi", categoryContent)
  
  
  const productListItems = useMemo(()=>{
    return content?.products?.filter((product) =>
      (product?.category_id) === (categoryContent?.id)
    );
  },[categoryContent]);

  console.log(productListItems)



  const category = useMemo(()=>{
    return categoryData?.find(element => element?.code === categoryType);
  },[categoryData, categoryType]);

  useEffect(() => {
    if (!category?.id) {
      // console.log("break")
      return; // Nếu chưa có id thì không gọi API
    }
    dispatch(setLoading(true));
    getAllProducts(category.id)
      .then(res => {
        console.log("API response:", res); // Kiểm tra dữ liệu từ API
        setProducts(res);
      })
      .catch(err => console.error("getAllProducts error", err))
      .finally(() => dispatch(setLoading(false)));
  }, [category?.id, dispatch]);
  
  


  return (
    <div>
        <div className='flex'>
            <div className='w-[20%] p-[10px] border rounded-lg m-[20px]'>
                {/* Filters */}
                <div className='flex justify-between '>
                <p className='text-[16px] text-gray-600'>Filter</p>
                <FilterIcon />
                
                </div>
                <div>
                  {/* Product types */}
                <p className='text-[16px] text-black mt-5'>Categories</p>
                <Categories types={categoryContent?.types}/>
                <hr></hr>
                </div>
                  {/* Price */}
                  <PriceFilter />
                  <hr></hr>
                  {/* Colors */}
                  <ColorsFilter colors={categoryContent?.meta_data?.colors}/>
                  <hr></hr>
                   {/* Sizes */}
                   <SizeFilter sizes={categoryContent?.meta_data?.sizes}/>
            </div>

            <div className='p-[15px]'>
            <p className='text-black text-lg'>{categoryContent?.description}</p>
                {/* Products */}
                <div className='pt-4 grid grid-cols-1 lg:grid-cols-3 md:grid-cols-2 gap-8 px-2'>
                <ProductCard {... productListItems[1]}/>
                
                {productListItems?.map((item, index) => (
                  <ProductCard key={index} {...item}/>
                ))}
                </div>

            </div>

        </div>
    </div>
  )
}

export default ProductList