import React from 'react';
import './Footer.css';
import FbIcon from '../common/FbIcon'
import IgIcon from '../common/IgIcon'

const Footer = () => {
  return (
    <footer className="footer">
      <div className="waves">
        <div className="wave" id="wave1"></div>
        <div className="wave" id="wave2"></div>
        <div className="wave" id="wave3"></div>
        <div className="wave" id="wave4"></div>
      </div>
      <ul className="social-icon">
        <li className="social-icon__item">
        <a href='/fb'><FbIcon /></a>
        </li>
        <li className="social-icon__item">
        <a href='/ig'><IgIcon /></a>
        </li>
      </ul>
      <ul className="menu">
        <li className="menu__item">
          <a className="menu__link" href="#">Home</a>
        </li>
        <li className="menu__item">
          <a className="menu__link" href="#">About</a>
        </li>
        <li className="menu__item">
          <a className="menu__link" href="#">Services</a>
        </li>
        <li className="menu__item">
          <a className="menu__link" href="#">Team</a>
        </li>
        <li className="menu__item">
          <a className="menu__link" href="#">Contact</a>
        </li>
      </ul>
      <p>&copy;2025 Jeweluxe | All Rights Reserved</p>
    </footer>
  );
}

export default Footer;
