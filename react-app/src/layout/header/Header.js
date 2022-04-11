import React from 'react';
import './Header.css';

function Header() {
    const toggle = (e) => {
        e.preventDefault();
        document.getElementById("wrapper").classList.toggle("toggled");
        window.setTimeout(() => window.dispatchEvent(new Event('resize')), 251);
    }

    return (
        <>
          <nav className="t-navbar">
              <button className="btn btn-secondary t-btn-toggle" onClick={toggle} id="menu-toggle">메뉴 접기/펴기</button>
          </nav>
        </>
    );
}

export default Header;