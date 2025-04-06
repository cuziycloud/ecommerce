import React, { useState, useRef } from "react";
import AliceCarousel from "react-alice-carousel";
import HomeSectionCard from "../HomeSectionCard/HomeSectionCard";
import KeyboardDoubleArrowLeftIcon from "@mui/icons-material/KeyboardDoubleArrowLeft";
import { Button } from "@mui/material";

const HomeSectionCarousel = ({data, sectionName}) => {
  const carouselRef = useRef(null); // Thêm useRef
  const [activeIndex, setActiveIndex] = useState(0);

  const responsive = {
    0: { items: 1 },
    360: { items: 1.5 },
    480: { items: 2 },
    600: { items: 2.5 },
    768: { items: 3 },
    900: { items: 3.5 },
    1024: { items: 4 },
    1200: { items: 4.5 },
  };

  const items = data.slice(0, 10).map((item) => <HomeSectionCard product={item} />);
  const maxIndex = items.length - 1; // Giới hạn index tối đa

  const slidePrev = () => {
    if (activeIndex > 0) {
      const newIndex = activeIndex - 1;
      setActiveIndex(newIndex);
      carouselRef.current.slideTo(newIndex); // Cập nhật carousel
    }
  };

  const slideNext = () => {
    if (activeIndex < maxIndex) {
      const newIndex = activeIndex + 1;
      setActiveIndex(newIndex);
      carouselRef.current.slideTo(newIndex); // Cập nhật carousel
    }
  };

  const syncActiveIndex = ({ item }) => setActiveIndex(item);

  return (
    <div className="border border-blue-950">
        <h2 className="text-2xl font-extrabold text-gray-800 py-5">{sectionName}</h2>
      <div className="relative p-5">
        <AliceCarousel
          ref={carouselRef}
          items={items}
          disableButtonsControls
          disableDotsControls
          responsive={responsive}
          onSlideChanged={syncActiveIndex}
          activeIndex={activeIndex}
        />

        {/* Nút "Next" */}
        {activeIndex < maxIndex && (
          <Button
            variant="contained"
            className="z-50"
            onClick={slideNext}
            sx={{
              position: "absolute",
              top: "8rem",
              right: "0rem",
              transform: "translateX(50%) rotate(90deg)",
              bgcolor: "gray",
              "&:hover": { bgcolor: "black" },
            }}
            aria-label="next"
          >
            <KeyboardDoubleArrowLeftIcon sx={{ transform: "rotate(90deg)", color: "white" }} />
          </Button>
        )}

        {/* Nút "Previous" */}
        {activeIndex > 0 && (
          <Button
            variant="contained"
            className="z-50"
            onClick={slidePrev}
            sx={{
              position: "absolute",
              top: "8rem",
              left: "0rem",
              transform: "translateX(-50%) rotate(-90deg)",
              bgcolor: "gray",
              "&:hover": { bgcolor: "black" },
            }}
            aria-label="prev"
          >
            <KeyboardDoubleArrowLeftIcon sx={{ transform: "rotate(90deg)" }} />
          </Button>
        )}
      </div>
    </div>
  );
};

export default HomeSectionCarousel;
