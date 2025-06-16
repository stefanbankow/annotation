import styled from 'styled-components';

export const TreeContainer = styled.div`
  width: 100%;
`;

export const TreeTitle = styled.h3`
  font-size: ${({ theme }) => theme.fontSizes.lg};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin: 0 0 ${({ theme }) => theme.spacing.lg} 0;
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  padding-bottom: ${({ theme }) => theme.spacing.sm};
`;

export const LabelItem = styled.div<{ $selected?: boolean; $level?: number }>`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: ${({ theme }) => theme.spacing.sm} ${({ theme }) => theme.spacing.md};
  margin-left: ${({ $level = 0 }) => $level * 20}px;
  background-color: ${({ theme, $selected }) => 
    $selected ? `${theme.colors.primary}10` : 'transparent'};
  border: 1px solid ${({ theme, $selected }) => 
    $selected ? theme.colors.primary : 'transparent'};
  border-radius: ${({ theme }) => theme.borderRadius.md};
  cursor: pointer;
  transition: all 0.2s ease;
  margin-bottom: ${({ theme }) => theme.spacing.xs};

  &:hover {
    background-color: ${({ theme }) => `${theme.colors.primary}05`};
  }
`;

export const LabelColor = styled.div<{ $color: string }>`
  width: 16px;
  height: 16px;
  background-color: ${({ $color }) => $color};
  border-radius: 50%;
  margin-right: ${({ theme }) => theme.spacing.sm};
  border: 1px solid rgba(0, 0, 0, 0.1);
`;

export const LabelName = styled.span`
  flex: 1;
  font-weight: 500;
  color: ${({ theme }) => theme.colors.text};
`;

export const LabelActions = styled.div`
  display: flex;
  gap: ${({ theme }) => theme.spacing.xs};
  opacity: 0;
  transition: opacity 0.2s ease;

  ${LabelItem}:hover & {
    opacity: 1;
  }
`;

export const ActionButton = styled.button<{ $variant?: 'danger' }>`
  background: none;
  border: none;
  cursor: pointer;
  padding: ${({ theme }) => theme.spacing.xs};
  border-radius: ${({ theme }) => theme.borderRadius.sm};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  transition: background-color 0.2s ease;

  &:hover {
    background-color: ${({ theme, $variant }) => 
      $variant === 'danger' ? `${theme.colors.danger}20` : `${theme.colors.primary}20`};
  }
`;

export const ChildrenContainer = styled.div`
  margin-left: ${({ theme }) => theme.spacing.md};
`;

export const EmptyState = styled.div`
  text-align: center;
  padding: ${({ theme }) => theme.spacing.xl};
  color: ${({ theme }) => theme.colors.textSecondary};
  font-style: italic;
`;
