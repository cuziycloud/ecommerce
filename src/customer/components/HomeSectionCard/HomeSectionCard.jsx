import React from 'react'

const HomeSectionCard = () => {
  return (
    <div className='cursor-pointer flex flex-col items-center bg-white rounded-lg shadow-lg overflow-hidden w-[15rem] mx-3'>
        <div className='h-[13rem] w-[10rem]'>
            <img className='object-cover object-top w-full h-full' src='https://images.unsplash.com/photo-1595370269025-1f070ca29b81?q=80&w=2670&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D' alt=''/>
        </div>

        <div className='p-4'>
            <h3 className='text-lg font-medium text-gray-900'>Ring</h3>
            <p className='mt-2 text-sm text-gray-500'>abcdefgh</p>
        </div>
    </div>
  )
}

export default HomeSectionCard