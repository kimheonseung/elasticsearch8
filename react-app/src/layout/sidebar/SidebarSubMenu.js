import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'

const SidebarSubMenu = ({subMenuObject}) => {
  const subMenuTitle = subMenuObject.title;
  const subMenuGroup = subMenuObject.group;
  const subMenuHref  = subMenuObject.href;
  const subMenuIcon  = subMenuObject.icon;

  return (
      <>
        <a className="t-br-2 t-menu-a" href={subMenuHref}>
          <span data-menu={subMenuGroup} className="list-group-item t-sidebar-submenu t-br-2">
            <FontAwesomeIcon icon={subMenuIcon} /> {subMenuTitle}
          </span>
        </a>
      </>
  );
}

export default SidebarSubMenu;