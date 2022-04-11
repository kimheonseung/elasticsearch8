import React from 'react';
import { Redirect, Route } from 'react-router-dom';
import { checkLogin } from 'common/AuthUtil';

function PrivateRoute( {component: Component, ...rest} ) {
  return (
    <Route
      {...rest}
      render = { (props) => checkLogin() ?
        <Component {...props} /> :
        <Redirect to={ {pathname: '/login', state: {from: props.location}} } />
      }
    />
  );
}

export default PrivateRoute;