import React from "react";
import "./ProductCard.css";

const ProductCard = () => {
  return (
    <div className="productCard w-[15rem] m-3 transition-all cursor-pointer">
      <div className="h-[20rem]">
        <img
          className="h-full w-full object-cover object-left-top"
          src="https://images.unsplash.com/photo-1603561591411-07134e71a2a9?q=80&w=2680&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
          alt=""
        />
      </div>

      <div className="textPart bg-white p-3 flex flex-col">
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
