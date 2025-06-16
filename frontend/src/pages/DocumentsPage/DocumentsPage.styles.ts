import styled from 'styled-components';

export const PageContainer = styled.div`
  flex: 1;
  padding: ${({ theme }) => theme.spacing.xl};
  max-width: 1280px;
  margin: 0 auto;
  width: 100%;
`;

export const PageHeader = styled.div`
  margin-bottom: ${({ theme }) => theme.spacing.xl};
`;

export const Title = styled.h1`
  font-size: ${({ theme }) => theme.fontSizes.xxxl};
  font-weight: bold;
  color: ${({ theme }) => theme.colors.text};
  margin: 0;
`;

export const UploadSection = styled.section`
  margin-bottom: ${({ theme }) => theme.spacing.xl};
`;

export const UploadArea = styled.div<{ $uploading?: boolean }>`
  position: relative;
  border: 2px dashed ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.borderRadius.lg};
  padding: ${({ theme }) => theme.spacing.xxl};
  text-align: center;
  background-color: ${({ theme }) => theme.colors.surface};
  transition: all 0.2s ease;
  cursor: pointer;
  opacity: ${({ $uploading }) => $uploading ? 0.6 : 1};
  pointer-events: ${({ $uploading }) => $uploading ? 'none' : 'auto'};

  &:hover {
    border-color: ${({ theme }) => theme.colors.primary};
    background-color: ${({ theme }) => `${theme.colors.primary}05`};
  }
`;

export const FileInput = styled.input`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  cursor: pointer;

  &:disabled {
    cursor: not-allowed;
  }
`;

export const UploadText = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.lg};
  color: ${({ theme }) => theme.colors.textSecondary};
  pointer-events: none;

  small {
    font-size: ${({ theme }) => theme.fontSizes.sm};
    color: ${({ theme }) => theme.colors.textSecondary};
    opacity: 0.8;
  }
`;

export const DocumentGrid = styled.div`
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: ${({ theme }) => theme.spacing.lg};
`;

export const DocumentCard = styled.div`
  background-color: ${({ theme }) => theme.colors.surface};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.borderRadius.lg};
  padding: ${({ theme }) => theme.spacing.lg};
  box-shadow: ${({ theme }) => theme.shadows.sm};
  transition: all 0.2s ease;

  &:hover {
    box-shadow: ${({ theme }) => theme.shadows.md};
    transform: translateY(-2px);
  }
`;

export const DocumentIcon = styled.div`
  font-size: 3rem;
  text-align: center;
  margin-bottom: ${({ theme }) => theme.spacing.md};
`;

export const DocumentInfo = styled.div`
  margin-bottom: ${({ theme }) => theme.spacing.lg};
`;

export const DocumentName = styled.h3`
  font-size: ${({ theme }) => theme.fontSizes.lg};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin: 0 0 ${({ theme }) => theme.spacing.sm} 0;
  word-break: break-word;
`;

export const DocumentMeta = styled.div`
  display: flex;
  flex-direction: column;
  gap: ${({ theme }) => theme.spacing.xs};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textSecondary};
`;

export const DocumentActions = styled.div`
  display: flex;
  gap: ${({ theme }) => theme.spacing.sm};
  flex-wrap: wrap;
`;

export const ActionButton = styled.div<{ $variant?: 'danger' }>`
  flex: 1;
  min-width: 100px;
  padding: ${({ theme }) => theme.spacing.sm} ${({ theme }) => theme.spacing.md};
  background-color: ${({ theme, $variant }) => 
    $variant === 'danger' ? theme.colors.danger : theme.colors.primary};
  color: white;
  border: none;
  border-radius: ${({ theme }) => theme.borderRadius.md};
  text-decoration: none;
  text-align: center;
  font-weight: 500;
  font-size: ${({ theme }) => theme.fontSizes.sm};
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    background-color: ${({ theme, $variant }) => 
      $variant === 'danger' ? '#dc2626' : theme.colors.primaryHover};
  }
`;

export const LoadingState = styled.div`
  text-align: center;
  padding: ${({ theme }) => theme.spacing.xxl};
  font-size: ${({ theme }) => theme.fontSizes.lg};
  color: ${({ theme }) => theme.colors.textSecondary};
`;

export const ErrorState = styled.div`
  background-color: ${({ theme }) => `${theme.colors.danger}10`};
  color: ${({ theme }) => theme.colors.danger};
  padding: ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.borderRadius.md};
  margin-bottom: ${({ theme }) => theme.spacing.lg};
  border: 1px solid ${({ theme }) => `${theme.colors.danger}30`};
`;
