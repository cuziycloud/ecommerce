import React from 'react'
import SectionHeading from './SecHeading/sectionHeading'
import Card from '../Card/Card';
import Jeans from '../../assets/img/j1.jpg'
import Shirts from '../../assets/img/j2.jpg'
import Tshirt from '../../assets/img/j3.jpg'
import dresses from '../../assets/img/j4.jpg'
import Carousel from 'react-multi-carousel';
import { responsive } from '../../utils/sectionConstants';
import './NewArrivals.css';

const items = [{
    'title':'Jeans',
    imagePath:Jeans
},{
    'title':'Shirts',
    imagePath:Shirts
},{
    'title':'T-Shirts',
    imagePath:Tshirt
},{
    'title':'Dresses',
    imagePath:dresses
},
{
    'title':'Joggers',
    imagePath:require('../../assets/img/j5.jpg')
},
{
    'title':'Kurtis',
    imagePath:require('../../assets/img/j6.png')
}];

const NewArrivals = () => {
  return (
    <>
    <SectionHeading title={'New Arrivals'}/>
    <Carousel
        responsive={responsive}
        autoPlay={false}
        swipeable={true}
        draggable={false}
        showDots={false}
        infinite={false}
        partialVisible={false}
        itemClass={'react-slider-custom-item'}
        className='px-8'
      >
        {items && items?.map((item,index)=> <Card key={item?.title +index} title={item.title} imagePath={item.imagePath}/>)}

      </Carousel>
    </>
  )
}

export default NewArrivals