import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown } from '@fortawesome/free-solid-svg-icons';
import './SidebarMenu.css';
import SidebarSubMenu from './SidebarSubMenu';

function SidebarMenu({menuObject}) {

  const menu           = menuObject.menu;
  const subMenuObject  = menuObject.subMenu; 

  const menuHasSubMenu = menu.hasSubMenu;
  const menuTitle      = menu.title;
  const menuGroup      = menu.group;
  const menuIcon       = menu.icon;
  const menuHref       = menu.href;
  const subMenuArray   = [];
  
  const subMenuToArray = () => {
    if(menuHasSubMenu) {
      /* 배열 초기화 */
      subMenuArray.splice(0, subMenuArray.length);
      const subMenuKeys = Object.keys(subMenuObject);
      subMenuKeys.forEach(subMenu => {
        subMenuArray.push(subMenuObject[subMenu]);
      })
    }
  }

  const foldToggle = (e) => {
    const dataMenu = e.currentTarget.dataset.menu;
    const subArr = document.getElementsByClassName('t-sidebar-submenu');
    
    for(let i = 0 ; i < subArr.length ; ++i) {
      const subDataMenu = subArr.item(i).dataset.menu;
      if(dataMenu === subDataMenu) {
        const classList = subArr.item(i).classList;
        if(classList.contains('t-submenu-hide')) {
          subArr.item(i).classList.add('t-submenu-show');
          subArr[i].classList.remove('t-submenu-hide');
        } else if(classList.contains('t-submenu-show')) {
          subArr.item(i).classList.add('t-submenu-hide');
          subArr[i].classList.remove('t-submenu-show');
        } else {
          subArr.item(i).classList.add('t-submenu-hide');
        }
      }
    }
  }

  subMenuToArray();

  return (
      <>
        {menuHasSubMenu ?
        <>
          <span data-menu={menuGroup} className="list-group-item t-sidebar-menu t-has-submenu t-br-2" onClick={foldToggle}>
            <span id="icon-wrap">
              <FontAwesomeIcon icon={menuIcon} /> {menuTitle}
            </span>
            <FontAwesomeIcon icon={faChevronDown} />
          </span>
          {
            subMenuArray.map((subMenu, index) => {
              return <SidebarSubMenu key={subMenu.key} subMenuObject={subMenu} />
            })
          }
        </>
        :
          <a className="t-br-2 t-menu-a" href={menuHref}>
            <span data-menu={menuGroup} className="list-group-item t-sidebar-menu t-br-2">
              <FontAwesomeIcon icon={menuIcon} /> {menuTitle}
            </span>
          </a>
        }
      </>
  );
}

export default SidebarMenu;