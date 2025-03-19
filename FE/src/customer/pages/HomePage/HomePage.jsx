import React from 'react'
import MainCarousel from '../../components/HomeCarousel/MainCarousel'
import HomeSectionCarousel from '../../components/HomeSectionCarousel/HomeSectionCarousel'
import { mens_kurta } from '../../../Data/ring'
import Footer from '../../components/Footer/Footer'

const HomePage = () => {
  return (
    <div>
        <MainCarousel/>
        <div className='space-y-10 py-20 flex flex-col justify-center px-5 lg:px-10'>
            <HomeSectionCarousel data={mens_kurta} sectionName={"Ring"}/>
            <HomeSectionCarousel data={mens_kurta} sectionName={"Necklace"}/>
            <HomeSectionCarousel data={mens_kurta} sectionName={"Earring"}/>
            <HomeSectionCarousel data={mens_kurta} sectionName={"Bracelet"}/>
            <HomeSectionCarousel data={mens_kurta} sectionName={"Accessory"}/>
        </div>
        <Footer/>
    </div>  
  )
}

export default HomePage