import React, { useState, useEffect } from 'react';
import { apiService } from '../../services/api';
import { Analytics } from '../../types';
import {
  PageContainer,
  PageHeader,
  Title,
  StatsGrid,
  StatCard,
  StatValue,
  StatLabel,
  ChartsContainer,
  ChartCard,
  ChartTitle,
  LoadingState,
  ErrorState,
} from './AnalyticsPage.styles';

const AnalyticsPage: React.FC = () => {
  const [analytics, setAnalytics] = useState<Analytics | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadAnalytics();
  }, []);

  const loadAnalytics = async () => {
    try {
      setLoading(true);
      const data = await apiService.getAnalytics();
      setAnalytics(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load analytics');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <PageContainer>
        <LoadingState>Зареждане на аналитика...</LoadingState>
      </PageContainer>
    );
  }

  if (error || !analytics) {
    return (
      <PageContainer>
        <ErrorState>{error || 'Няма данни за аналитика'}</ErrorState>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <Title>Аналитика</Title>
      </PageHeader>

      <StatsGrid>
        <StatCard>
          <StatValue>{analytics.totalDocuments}</StatValue>
          <StatLabel>Общо документи</StatLabel>
        </StatCard>
        <StatCard>
          <StatValue>{analytics.totalAnnotations}</StatValue>
          <StatLabel>Общо анотации</StatLabel>
        </StatCard>
        <StatCard>
          <StatValue>{analytics.totalLabels}</StatValue>
          <StatLabel>Общо етикети</StatLabel>
        </StatCard>
        <StatCard>
          <StatValue>{analytics.totalLabelRelationships}</StatValue>
          <StatLabel>Връзки между етикети</StatLabel>
        </StatCard>
      </StatsGrid>

      <ChartsContainer>
        <ChartCard>
          <ChartTitle>Най-използвани етикети</ChartTitle>
          <div style={{ padding: '1rem' }}>
            {analytics.mostUsedLabels.length === 0 ? (
              <div style={{ textAlign: 'center', color: '#6b7280', padding: '2rem' }}>
                Няма данни за етикети
              </div>
            ) : (
              analytics.mostUsedLabels.slice(0, 10).map((item: any, index: number) => (
                <div 
                  key={item.label.id}
                  style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    padding: '0.75rem',
                    marginBottom: '0.5rem',
                    backgroundColor: '#f8f9fa',
                    borderRadius: '0.5rem',
                    border: '1px solid #e5e7eb'
                  }}
                >
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                    <div 
                      style={{
                        width: '16px',
                        height: '16px',
                        backgroundColor: item.label.color,
                        borderRadius: '50%',
                        border: '1px solid rgba(0,0,0,0.1)'
                      }}
                    />
                    <span style={{ fontWeight: '500' }}>{item.label.name}</span>
                  </div>
                  <div style={{ 
                    backgroundColor: '#2563eb',
                    color: 'white',
                    padding: '0.25rem 0.5rem',
                    borderRadius: '0.25rem',
                    fontSize: '0.875rem',
                    fontWeight: 'bold'
                  }}>
                    {item.usageCount}
                  </div>
                </div>
              ))
            )}
          </div>
        </ChartCard>

        <ChartCard>
          <ChartTitle>Разпределение на етикетите</ChartTitle>
          <div style={{ padding: '1rem' }}>
            {analytics.labelDistribution.length === 0 ? (
              <div style={{ textAlign: 'center', color: '#6b7280', padding: '2rem' }}>
                Няма данни за разпределение
              </div>
            ) : (
              <div style={{ display: 'grid', gap: '0.5rem' }}>
                {analytics.labelDistribution.map((item: any, index: number) => (
                  <div 
                    key={index}
                    style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '0.5rem',
                      padding: '0.5rem'
                    }}
                  >
                    <div 
                      style={{
                        width: '12px',
                        height: '12px',
                        backgroundColor: item.color,
                        borderRadius: '50%'
                      }}
                    />
                    <span style={{ flex: 1 }}>{item.labelName}</span>
                    <span style={{ fontWeight: 'bold' }}>{item.count}</span>
                    <div 
                      style={{
                        width: `${Math.max(10, (item.count / Math.max(...analytics.labelDistribution.map((d: any) => d.count))) * 100)}px`,
                        height: '8px',
                        backgroundColor: item.color,
                        borderRadius: '4px',
                        opacity: 0.6
                      }}
                    />
                  </div>
                ))}
              </div>
            )}
          </div>
        </ChartCard>
      </ChartsContainer>
    </PageContainer>
  );
};

export default AnalyticsPage;
