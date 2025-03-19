import React from "react";
import "./ProductCard.css";

const ProductCard = ({product}) => {
  return (
    <div className="productCard w-[15rem] m-3 transition-all cursor-pointer">
      <div className="h-[20rem]">
        <img
          className="h-full w-full object-contain object-center"
          src={product.imageUrl}
          alt=""
        />
      </div>

      <div className="textPart bg-white p-3 flex flex-col">
        <p className="font-bold opacity-60">{product.brand}</p>
        <p>{product.title}</p>

        <div className="flex items-center space-x-3 mt-2">
          <p className="font-semibold">{product.discountedPrice}$</p>
          <p className="line-through opacity-50">{product.price}$</p>
          <p className="text-green-600 font-semibold">{product.discountPersent}% off</p>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;
