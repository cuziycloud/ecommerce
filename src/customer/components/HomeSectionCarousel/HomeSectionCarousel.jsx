import React from 'react'
import AliceCarousel from 'react-alice-carousel';
import HomeSectionCard from '../HomeSectionCard/HomeSectionCard';
import KeyboardDoubleArrowLeftIcon from '@mui/icons-material/KeyboardDoubleArrowLeft';
import { Button } from '@mui/material';

const HomeSectionCarousel = () => {

    const responsive = {
        0: { items: 1 },       // Điện thoại rất nhỏ (~320px)
        360: { items: 1.5 },   // Điện thoại nhỏ (~360px)
        480: { items: 2 },     // Điện thoại lớn (~480px)
        600: { items: 2.5 },   // Điện thoại cỡ trung (~600px)
        768: { items: 3 },     // Tablet (~768px)
        900: { items: 3.5 },   // Tablet lớn (~900px)
        1024: { items: 4 },    // Laptop nhỏ (~1024px)
        1200: { items: 4.5 },  // Laptop/Màn hình lớn (~1200px)
    };
    
        

    const items=[1,1,1,1,1].map((item)=><HomeSectionCard/>)
    return (
        <div className='relative px-4 lg:px-8'>
            <div className='relative p-5'>
            <AliceCarousel
                items={items}
                disableButtonsControls
                //autoPlay
                //autoPlayInterval={1000}
                infinite
                responsive={responsive}
            />
            <Button variant='contained' className='z-50' sx={{position: 'absolute', top:'8rem', right:'0rem', transform:'translateX(50%) rotate(90deg)'}} aria-label='next'>
                <KeyboardDoubleArrowLeftIcon sx={{transform:'rotate(90deg)'}}/>
            </Button>
            </div>
        </div>
    )
}

export default HomeSectionCarousel