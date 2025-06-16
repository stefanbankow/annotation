import React from 'react';
import { Link } from 'react-router-dom';
import {
  PageContainer,
  HeroSection,
  Title,
  Subtitle,
  FeatureGrid,
  FeatureCard,
  FeatureIcon,
  FeatureTitle,
  FeatureDescription,
  CTASection,
  CTAButton,
} from './HomePage.styles';

const HomePage: React.FC = () => {
  const features = [
    {
      icon: '📄',
      title: 'Анотиране на документи',
      description: 'Анотирайте документи във формат .txt, .docx и .pdf с различни етикети и цветове',
    },
    {
      icon: '🏷️',
      title: 'Управление на етикети',
      description: 'Създавайте, редактирайте и организирайте етикети в йерархична структура',
    },
    {
      icon: '🔗',
      title: 'Връзки между етикети',
      description: 'Създавайте еднопосочни връзки между етикети с описания',
    },
    {
      icon: '✏️',
      title: 'Текстов редактор',
      description: 'Въвеждайте и редактирайте текст директно в приложението',
    },
    {
      icon: '📊',
      title: 'Аналитика',
      description: 'Преглеждайте статистики за най-използвани етикети и тенденции',
    },
    {
      icon: '💾',
      title: 'Съхранение',
      description: 'Автоматично съхранение на оригинални и анотирани документи',
    },
  ];

  return (
    <PageContainer>
      <HeroSection>
        <Title>Уеб базиран инструмент за анотиране на документи</Title>
        <Subtitle>
          Професионален инструмент за анотиране на документи с развити възможности
          за етикетиране, организация и анализ на съдържанието
        </Subtitle>
        <CTASection>
          <CTAButton as={Link} to="/documents">
            Започнете анотиране
          </CTAButton>
          <CTAButton as={Link} to="/labels" $variant="secondary">
            Управление на етикети
          </CTAButton>
        </CTASection>
      </HeroSection>

      <FeatureGrid>
        {features.map((feature, index) => (
          <FeatureCard key={index}>
            <FeatureIcon>{feature.icon}</FeatureIcon>
            <FeatureTitle>{feature.title}</FeatureTitle>
            <FeatureDescription>{feature.description}</FeatureDescription>
          </FeatureCard>
        ))}
      </FeatureGrid>
    </PageContainer>
  );
};

export default HomePage;
