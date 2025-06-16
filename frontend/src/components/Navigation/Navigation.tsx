import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import {
  NavigationContainer,
  Nav,
  NavList,
  NavItem,
  NavLink,
  Logo,
  LogoText,
} from './Navigation.styles';

const Navigation: React.FC = () => {
  const location = useLocation();

  const navItems = [
    { path: '/', label: 'Начало', icon: '🏠' },
    { path: '/documents', label: 'Документи', icon: '📄' },
    { path: '/labels', label: 'Етикети', icon: '🏷️' },
    { path: '/analytics', label: 'Аналитика', icon: '📊' },
  ];

  return (
    <NavigationContainer>
      <Nav>
        <Logo>
          <Link to="/">
            <LogoText>📝 Annotation Tool</LogoText>
          </Link>
        </Logo>
        <NavList>
          {navItems.map((item) => (
            <NavItem key={item.path}>
              <NavLink
                as={Link}
                to={item.path}
                $active={location.pathname === item.path}
              >
                <span>{item.icon}</span>
                {item.label}
              </NavLink>
            </NavItem>
          ))}
        </NavList>
      </Nav>
    </NavigationContainer>
  );
};

export default Navigation;
