import React from "react";

const ProductCard = () => {
  return (
    <div className="productCard w-[15rem] m-3 transition-all cursor-pointer">
      <div className="h-[20rem]">
        <img
          className="h-full w-full object-cover object-left-top"
          src=""
          alt=""
        />
      </div>

      <div className="textPart bg-white mt-4 flex flex-col">
        <p className="font-bold opacity-60">Name of product</p>
        <p>This is description about my product. (Coming soon)</p>

        <div className="flex items-center space-x-3 mt-2">
          <p className="font-semibold">30$</p>
          <p className="line-through opacity-50">100$</p>
          <p className="text-green-600 font-semibold">70% off</p>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;
