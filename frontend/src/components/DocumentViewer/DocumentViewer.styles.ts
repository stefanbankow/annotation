import styled from 'styled-components';

export const ViewerContainer = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;
`;

export const ViewerHeader = styled.div`
  padding: ${({ theme }) => theme.spacing.lg};
  border-bottom: 1px solid ${({ theme }) => theme.colors.border};
  background-color: ${({ theme }) => theme.colors.surface};
`;

export const DocumentTitle = styled.h3`
  font-size: ${({ theme }) => theme.fontSizes.lg};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin: 0 0 ${({ theme }) => theme.spacing.sm} 0;
`;

export const DocumentMeta = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.sm};
  color: ${({ theme }) => theme.colors.textSecondary};
  display: flex;
  gap: ${({ theme }) => theme.spacing.md};
`;

export const TextContainer = styled.div`
  flex: 1;
  padding: ${({ theme }) => theme.spacing.lg};
  overflow: auto;
  background-color: ${({ theme }) => theme.colors.surface};
  position: relative;
`;

export const TextContent = styled.div`
  font-family: 'Georgia', 'Times New Roman', serif;
  font-size: ${({ theme }) => theme.fontSizes.md};
  line-height: 1.6;
  color: ${({ theme }) => theme.colors.text};
  white-space: pre-wrap;
  word-wrap: break-word;
  user-select: text;
  cursor: text;
  min-height: 400px;
  padding: ${({ theme }) => theme.spacing.lg};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.borderRadius.md};
  background-color: white;
  
  /* Custom text selection styling */
  &::selection {
    background-color: ${({ theme }) => `${theme.colors.primary}30`};
    color: ${({ theme }) => theme.colors.text};
  }
  
  /* Style for highlighted annotations */
  .annotation-highlight {
    position: relative;
    cursor: pointer;
    border-radius: 2px;
    transition: all 0.2s ease;
    
    &:hover {
      filter: brightness(0.9);
      box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }
    
    &.selected {
      box-shadow: 0 0 0 2px ${({ theme }) => theme.colors.primary};
      z-index: 10;
    }
  }
`;

export const SelectionToolbar = styled.div<{ $visible: boolean; $top: number; $left: number }>`
  position: absolute;
  top: ${({ $top }) => $top}px;
  left: ${({ $left }) => $left}px;
  transform: translateX(-50%);
  background-color: ${({ theme }) => theme.colors.text};
  color: white;
  padding: ${({ theme }) => theme.spacing.sm} ${({ theme }) => theme.spacing.md};
  border-radius: ${({ theme }) => theme.borderRadius.md};
  box-shadow: ${({ theme }) => theme.shadows.lg};
  z-index: 1000;
  display: ${({ $visible }) => $visible ? 'flex' : 'none'};
  gap: ${({ theme }) => theme.spacing.sm};
  align-items: center;
  font-size: ${({ theme }) => theme.fontSizes.sm};
  min-width: 280px;
  max-width: 450px;
  flex-wrap: wrap;
  
  /* Selected text preview styling */
  > span:first-child {
    flex: 1;
    min-width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    max-width: 150px;
  }
  
  /* Ensure it stays within viewport bounds */
  @media (max-width: 500px) {
    min-width: 250px;
    max-width: 350px;
    font-size: ${({ theme }) => theme.fontSizes.xs};
    padding: ${({ theme }) => theme.spacing.xs} ${({ theme }) => theme.spacing.sm};
    
    > span:first-child {
      max-width: 100px;
    }
  }
  
  &::after {
    content: '';
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    border: 6px solid transparent;
    border-top-color: ${({ theme }) => theme.colors.text};
  }
`;

export const SelectedTextPreview = styled.span`
  flex: 1;
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  max-width: 150px;
  font-style: italic;
  
  @media (max-width: 500px) {
    max-width: 100px;
  }
`;

export const LabelSelector = styled.select`
  background-color: white;
  color: ${({ theme }) => theme.colors.text};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.borderRadius.sm};
  padding: ${({ theme }) => theme.spacing.xs} ${({ theme }) => theme.spacing.sm};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  min-width: 120px;
`;

export const AnnotateButton = styled.button`
  background-color: ${({ theme }) => theme.colors.primary};
  color: white;
  border: none;
  border-radius: ${({ theme }) => theme.borderRadius.sm};
  padding: ${({ theme }) => theme.spacing.xs} ${({ theme }) => theme.spacing.sm};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  cursor: pointer;
  font-weight: 500;
  
  &:hover {
    background-color: ${({ theme }) => theme.colors.primaryHover};
  }
  
  &:disabled {
    background-color: ${({ theme }) => theme.colors.secondary};
    cursor: not-allowed;
  }
`;

export const CancelButton = styled.button`
  background-color: transparent;
  color: ${({ theme }) => theme.colors.textSecondary};
  border: 1px solid ${({ theme }) => theme.colors.border};
  border-radius: ${({ theme }) => theme.borderRadius.sm};
  padding: ${({ theme }) => theme.spacing.xs} ${({ theme }) => theme.spacing.sm};
  font-size: ${({ theme }) => theme.fontSizes.sm};
  cursor: pointer;
  
  &:hover {
    background-color: ${({ theme }) => theme.colors.border};
  }
`;

export const AnnotationsList = styled.div`
  margin-top: ${({ theme }) => theme.spacing.lg};
  padding-top: ${({ theme }) => theme.spacing.lg};
  border-top: 1px solid ${({ theme }) => theme.colors.border};
`;

export const AnnotationsTitle = styled.h4`
  font-size: ${({ theme }) => theme.fontSizes.md};
  font-weight: 600;
  color: ${({ theme }) => theme.colors.text};
  margin: 0 0 ${({ theme }) => theme.spacing.md} 0;
`;

export const AnnotationItem = styled.div<{ $selected?: boolean }>`
  padding: ${({ theme }) => theme.spacing.sm};
  margin-bottom: ${({ theme }) => theme.spacing.sm};
  border: 1px solid ${({ theme, $selected }) => 
    $selected ? theme.colors.primary : theme.colors.border};
  border-radius: ${({ theme }) => theme.borderRadius.md};
  cursor: pointer;
  transition: all 0.2s ease;
  background-color: ${({ theme, $selected }) => 
    $selected ? `${theme.colors.primary}10` : 'transparent'};
    
  &:hover {
    background-color: ${({ theme }) => `${theme.colors.primary}05`};
  }
`;

export const AnnotationLabel = styled.div<{ $color: string }>`
  display: inline-block;
  background-color: ${({ $color }) => $color};
  color: white;
  padding: ${({ theme }) => theme.spacing.xs} ${({ theme }) => theme.spacing.sm};
  border-radius: ${({ theme }) => theme.borderRadius.sm};
  font-size: ${({ theme }) => theme.fontSizes.xs};
  font-weight: 600;
  margin-bottom: ${({ theme }) => theme.spacing.xs};
`;

export const AnnotationText = styled.div`
  font-weight: 500;
  margin-bottom: ${({ theme }) => theme.spacing.xs};
  font-size: ${({ theme }) => theme.fontSizes.sm};
`;

export const AnnotationPosition = styled.div`
  font-size: ${({ theme }) => theme.fontSizes.xs};
  color: ${({ theme }) => theme.colors.textSecondary};
`;
