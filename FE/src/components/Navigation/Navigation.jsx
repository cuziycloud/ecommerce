import React from 'react';
import { Wishlist } from '../common/Wishlist';
import { Link, NavLink, useNavigate } from 'react-router-dom';
import './Navigation.css';

const Navigation = ({ variant = "default" }) => {
  //const navigate = useNavigate();

  return (
    <nav className='flex flex-wrap md:flex-nowrap items-center py-6 px-6 md:px-16 justify-between gap-6 md:gap-10 custom-nav bg-white shadow-md'>
      
      {/* Logo */}
      <div className='flex items-center flex-shrink-0'>
        <a className='text-2xl md:text-3xl font-serif text-yellow-700 font-bold' href='/'>Jeweluxe</a>
      </div>

      {/* Nav Items */}
      {variant === "default" &&
        <div className='flex-grow'>
          <ul className='flex flex-wrap gap-6 md:gap-12 text-gray-700 hover:text-black font-medium justify-center'>
            <li><NavLink to='/rings' className={({ isActive }) => isActive ? 'active-link' : ''}>Rings</NavLink></li>
            <li><NavLink to='/necklaces' className={({ isActive }) => isActive ? 'active-link' : ''}>Necklaces</NavLink></li>
            <li><NavLink to='/bracelets' className={({ isActive }) => isActive ? 'active-link' : ''}>Bracelets</NavLink></li>
            <li><NavLink to='/earrings' className={({ isActive }) => isActive ? 'active-link' : ''}>Earrings</NavLink></li>
          </ul>
        </div>
      }

      {/* Search Bar */}
      {variant === "default" &&
        <div className='hidden lg:flex justify-center'>
          <div className='border rounded flex overflow-hidden border-gray-300'>
            <div className="flex items-center justify-center px-3">
              <svg className="h-4 w-4 text-gray-600" fill="currentColor" viewBox="0 0 24 24">
                <path d="M16.32 14.9l5.39 5.4a1 1 0 0 1-1.42 1.4l-5.38-5.38a8 8 0 1 1 1.41-1.41zM10 16a6 6 0 1 0 0-12 6 6 0 0 0 0 12z" />
              </svg>
            </div>
            <input type="text" className="px-2 py-2 outline-none w-40 md:w-64 lg:w-72" placeholder="Search for jewelry..." />
          </div>
        </div>
      }

      {/* Actions */}
      <div className='flex flex-wrap gap-4 justify-end items-center mt-4 md:mt-0'>
        {variant === "default" &&
          <ul className='flex gap-4 md:gap-6 items-center text-xs md:text-sm'>
            <li>
              <button><Wishlist /></button>
            </li>
            <li>
              <Link to='/cart-items' className='text-gray-700 hover:text-yellow-600 whitespace-nowrap'>Cart</Link>
            </li>
            <li className='text-black border border-black hover:bg-slate-100 font-medium rounded-lg px-3 py-1.5'>
              <NavLink to="/v1/register" className={({ isActive }) => isActive ? 'active-link' : ''}>Signup</NavLink>
            </li>
          </ul>
        }

        {variant === "auth" &&
          <ul className='flex flex-wrap gap-2 md:gap-4'>
            <li className='text-black border border-black hover:bg-slate-100 font-medium rounded-lg text-xs md:text-sm px-4 py-2'>
              <NavLink to="/v1/login" className={({ isActive }) => isActive ? 'active-link' : ''}>Login</NavLink>
            </li>
            <li className='text-black border border-black hover:bg-slate-100 font-medium rounded-lg text-xs md:text-sm px-4 py-2'>
              <NavLink to="/v1/register" className={({ isActive }) => isActive ? 'active-link' : ''}>Signup</NavLink>
            </li>
          </ul>
        }
      </div>
    </nav>
  );
};

export default Navigation;
