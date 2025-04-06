import React from 'react'
import j4 from '../../assets/img/j4.jpg'

const HeroSection = () => {
  return (
    <div 
      className='relative flex items-center bg-cover bg-center text-left h-svh w-full' 
      style={{ backgroundImage: `url(${j4})` }}
    >
      <div className='absolute top-0 right-0 bottom-0 left-0 bg-black opacity-30'></div>

      <main className='px-10 lg:px-24 z-10 text-white'>
        <div className='text-left'>
          <h2 className='text-2xl tracking-widest text-white uppercase'>
            Exclusive Collection
          </h2>
        </div>

        <p className='mt-3 sm:mt-5 sm:max-w-xl text-6xl font-serif font-bold leading-tight'>
          Timeless Elegance
        </p>

        <p className='mt-3 sm:mt-5 sm:max-w-xl text-2xl font-light italic'>
          Discover the beauty of handcrafted jewelry
        </p>

        <button className='border rounded mt-6 border-white hover:bg-white hover:text-black hover:border-white text-white bg-transparent w-44 h-12 font-medium transition-all duration-300'>
          Explore Now
        </button>
      </main>
    </div>
  )
}

export default HeroSection
