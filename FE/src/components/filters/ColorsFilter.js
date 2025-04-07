import React, { useCallback, useState } from 'react';

export const colorSelector = {
  "Gold": "#FFD700",        // Màu vàng kim loại
  "Silver": "#C0C0C0",      // Màu bạc
  "Platinum": "#E5E4E2",    // Màu bạch kim
  "Black": "#252525",       // Màu đen
  "White": "#FFFFFF",       // Màu trắng
  "Gray": "#808080",        // Màu xám
  "Blue": "#0000FF",        // Màu xanh dương
  "Red": "#FF0000",         // Màu đỏ
  "Orange": "#FFA500",      // Màu cam
  "Navy": "#000080",        // Màu xanh biển
  "Yellow": "#FFFF00",      // Màu vàng
  "Pink": "#FFC0CB",        // Màu hồng
  "Green": "#008000"        // Màu xanh lá cây
}

const ColorsFilter = ({colors}) => {

  const [appliedColors,setAppliedColors] = useState([]);
  const onClickDiv = useCallback((item)=>{
    if(appliedColors.indexOf(item) > -1){
      
      setAppliedColors(appliedColors?.filter(color => color !== item));
    }
    else{
      setAppliedColors([...appliedColors,item]);
    }
  },[appliedColors,setAppliedColors]);

  return (
    <div className='flex flex-col mb-4'>
        <p className='text-[16px] text-black mt-5 mb-5'>Colors</p>
        <div className='flex flex-wrap px-2'>
            {colors?.map(item=> {
              return (
                <div className='flex flex-col mr-2'>
                  <div className='w-8 h-8 border rounded-xl mr-4 cursor-pointer hover:scale-110' onClick={()=>onClickDiv(item)} style={{background:`${colorSelector[item]}`}}></div>
                  <p className='text-sm text-gray-400 mb-2' style={{color:`${appliedColors?.includes(item) ? 'black':''}`}}>{item}</p>
                  </div>
              )
            })}
        </div>
    </div>
  )
}

export default ColorsFilter