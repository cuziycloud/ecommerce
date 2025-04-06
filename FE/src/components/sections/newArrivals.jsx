import React from 'react'
import SectionHeading from './secHeading/sectionHeading'
import Ring from '../../assets/img/j1.jpg'  
import Necklace from '../../assets/img/j2.jpg'
import Earrings from '../../assets/img/j3.jpg'
import Bracelet from '../../assets/img/j4.jpg'
import Carousel from 'react-multi-carousel';
import { responsive } from '../../utils/sectionConstants';
import './newArrivals.css';

const items = [{
    'title': 'Rings',
    imagePath: Ring
}, {
    'title': 'Necklaces',
    imagePath: Necklace
}, {
    'title': 'Earrings',
    imagePath: Earrings
}, {
    'title': 'Bracelets',
    imagePath: Bracelet
}, {
    'title': 'Watches',
    imagePath: require('../../assets/img/j5.jpg') 
}, {
    'title': 'Gold Sets',
    imagePath: require('../../assets/img/j5.jpg')
}];

const NewArrivals = () => {
  return (
    <>
      <SectionHeading title={'New Arrival'} />
      <Carousel
        responsive={responsive}
        autoPlay={false}
        swipeable={true}
        draggable={false}
        showDots={false}
        infinite={true}  
        partialVisible={true}  
        itemClass={'react-slider-custom-item'}
        className='px-8'
      >
        {items.map((item, index) => (
          <div key={index} className="carousel-item">
            <img src={item.imagePath} alt={item.title} className="carousel-image"/>
            <h3 className="carousel-title">{item.title}</h3>
          </div>
        ))}
      </Carousel>
    </>
  )
}

export default NewArrivals;
