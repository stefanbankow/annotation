import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider } from 'styled-components';
import { GlobalStyle, theme } from './styles/theme';
import Navigation from './components/Navigation/Navigation';
import HomePage from './pages/HomePage/HomePage';
import DocumentsPage from './pages/DocumentsPage/DocumentsPage';
import LabelsPage from './pages/LabelsPage/LabelsPage';
import AnnotationPage from './pages/AnnotationPage/AnnotationPage';
import AnalyticsPage from './pages/AnalyticsPage/AnalyticsPage';
import { AppContainer } from './App.styles';

function App() {
  return (
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Router>
        <AppContainer>
          <Navigation />
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/documents" element={<DocumentsPage />} />
            <Route path="/documents/:id/annotate" element={<AnnotationPage />} />
            <Route path="/labels" element={<LabelsPage />} />
            <Route path="/analytics" element={<AnalyticsPage />} />
          </Routes>
        </AppContainer>
      </Router>
    </ThemeProvider>
  );
}

export default App;
