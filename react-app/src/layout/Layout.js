import React, { useEffect } from 'react';
import Header from './header/Header';
import Sidebar from './sidebar/Sidebar';
import './Layout.css';

const Layout = (props) => {
    useEffect(() => {
        window.matchMedia('(min-width: 1025px)').addEventListener('change', (e) => {
            if(e.matches)
                window.setTimeout(() => window.dispatchEvent(new Event('resize')), 251);
        });

    });
    return (
        <>
            <div className="d-flex" id="wrapper">
                <Sidebar />
                <div className="t-content" id="page-content-wrapper">
                    <Header />
                    <div className="container-fluid">
                        {props.children}
                    </div>
                </div>
            </div>
        </>
    );
}

export default Layout;