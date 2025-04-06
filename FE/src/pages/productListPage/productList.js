import React, { useEffect, useMemo, useState } from 'react'
import FilterIcon from '../../components/common/FilterIcon';
import content from '../../data/content.json';
import ProductCard from './ProductCard';
import { useDispatch, useSelector } from 'react-redux';
const categories = content?.categories;

const ProductList = ({categoryType}) => {

  const categoryData = useSelector((state)=> state?.categoryState?.categories);
  const dispatch = useDispatch();
  const [products,setProducts] = useState([]);

  const categoryContent = useMemo(()=>{
    return categories?.find((category)=> category.code === categoryType);
  },[categoryType]);

  const category = useMemo(()=>{
    return categoryData?.find(element => element?.code === categoryType);
  },[categoryData, categoryType]);


  return (
    <div>
        <div className='flex'>
            <div className='w-[20%] p-[10px] border rounded-lg m-[20px]'>
                {/* Filters */}
                <div className='flex justify-between '>
                <p className='text-[16px] text-gray-600'>Filter</p>
                <FilterIcon />
                
                </div>
            </div>

            <div className='p-[15px]'>
            <p className='text-black text-lg'>{category?.description}</p>
                {/* Products */}
                <div className='pt-4 grid grid-cols-1 lg:grid-cols-3 md:grid-cols-2 gap-8 px-2'>
                {products?.map((item,index)=>(
                  <ProductCard key={item?.id+"_"+index} {...item} title={item?.name}/>
                ))}
                </div>

            </div>

        </div>
    </div>
  )
}

export default ProductList