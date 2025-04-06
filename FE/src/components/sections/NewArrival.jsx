import React from 'react';
import SectionHeading from './secHeading/SecHeading';
import Card from '../card/Card';
import Ring from '../../assets/img/j1.jpg';
import Necklace from '../../assets/img/j2.jpg';
import Earrings from '../../assets/img/j3.jpg';
import Bracelet from '../../assets/img/j4.jpg';
import Carousel from 'react-multi-carousel';
import { responsive } from '../../utils/sectionConstants';
import 'react-multi-carousel/lib/styles.css';
import './NewArrival.css';

const items = [
  {
    title: 'Rings',
    imagePath: Ring,
  },
  {
    title: 'Necklaces',
    imagePath: Necklace,
  },
  {
    title: 'Earrings',
    imagePath: Earrings,
  },
  {
    title: 'Bracelets',
    imagePath: Bracelet,
  },
  {
    title: 'Watches',
    imagePath: require('../../assets/img/j5.jpg'),
  },
  {
    title: 'Gold Sets',
    imagePath: require('../../assets/img/j6.png'),
  },
];

const NewArrivals = () => {
  return (
    <>
      <SectionHeading title={'New Arrivals'} />
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
        {items &&
          items.map((item, index) => (
            <Card
              key={item?.title + index}
              title={item.title}
              imagePath={item.imagePath}
            />
          ))}
      </Carousel>
    </>
  );
};

export default NewArrivals;
