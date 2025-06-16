import { Label, ColorOption } from '../types';

// Color utilities
export const generateRandomColor = (): string => {
  const colors = [
    '#FF6B6B', '#4ECDC4', '#45B7D1', '#96CEB4', '#FECA57',
    '#FF9F43', '#6C5CE7', '#A29BFE', '#FD79A8', '#E17055',
    '#00B894', '#00CEC9', '#74B9FF', '#A29BFE', '#FD79A8'
  ];
  return colors[Math.floor(Math.random() * colors.length)];
};

export const getContrastColor = (hexColor: string): string => {
  // Remove # if present
  const hex = hexColor.replace('#', '');
  
  // Convert to RGB
  const r = parseInt(hex.substr(0, 2), 16);
  const g = parseInt(hex.substr(2, 2), 16);
  const b = parseInt(hex.substr(4, 2), 16);
  
  // Calculate luminance
  const luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;
  
  return luminance > 0.5 ? '#000000' : '#FFFFFF';
};

export const colorOptions: ColorOption[] = [
  { value: '#FF6B6B', label: 'Red', color: '#FF6B6B' },
  { value: '#4ECDC4', label: 'Teal', color: '#4ECDC4' },
  { value: '#45B7D1', label: 'Blue', color: '#45B7D1' },
  { value: '#96CEB4', label: 'Green', color: '#96CEB4' },
  { value: '#FECA57', label: 'Yellow', color: '#FECA57' },
  { value: '#FF9F43', label: 'Orange', color: '#FF9F43' },
  { value: '#6C5CE7', label: 'Purple', color: '#6C5CE7' },
  { value: '#A29BFE', label: 'Light Purple', color: '#A29BFE' },
  { value: '#FD79A8', label: 'Pink', color: '#FD79A8' },
  { value: '#E17055', label: 'Coral', color: '#E17055' },
];

// Text utilities
export const extractContext = (
  text: string,
  startPosition: number,
  endPosition: number,
  contextLength: number = 50
): { before: string; after: string; selected: string } => {
  const beforeStart = Math.max(0, startPosition - contextLength);
  const afterEnd = Math.min(text.length, endPosition + contextLength);
  
  return {
    before: text.substring(beforeStart, startPosition),
    selected: text.substring(startPosition, endPosition),
    after: text.substring(endPosition, afterEnd)
  };
};

export const highlightText = (
  text: string,
  annotations: Array<{ startPosition: number; endPosition: number; labelColor: string; id: string }>
): Array<{ text: string; isAnnotated: boolean; color?: string; annotationId?: string }> => {
  if (!annotations.length) {
    return [{ text, isAnnotated: false }];
  }

  // Sort annotations by start position
  const sortedAnnotations = [...annotations].sort((a, b) => a.startPosition - b.startPosition);
  
  const result: Array<{ text: string; isAnnotated: boolean; color?: string; annotationId?: string }> = [];
  let currentPosition = 0;

  sortedAnnotations.forEach((annotation) => {
    // Add text before annotation
    if (currentPosition < annotation.startPosition) {
      result.push({
        text: text.substring(currentPosition, annotation.startPosition),
        isAnnotated: false
      });
    }

    // Add annotated text
    result.push({
      text: text.substring(annotation.startPosition, annotation.endPosition),
      isAnnotated: true,
      color: annotation.labelColor,
      annotationId: annotation.id
    });

    currentPosition = Math.max(currentPosition, annotation.endPosition);
  });

  // Add remaining text
  if (currentPosition < text.length) {
    result.push({
      text: text.substring(currentPosition),
      isAnnotated: false
    });
  }

  return result;
};

// Label utilities
export const buildLabelHierarchy = (labels: Label[]): Label[] => {
  const labelMap = new Map<string, Label>();
  const rootLabels: Label[] = [];

  // Create map of all labels
  labels.forEach(label => {
    labelMap.set(label.id, { ...label, children: [] });
  });

  // Build hierarchy
  labels.forEach(label => {
    const labelWithChildren = labelMap.get(label.id)!;
    
    if (label.parentId) {
      const parent = labelMap.get(label.parentId);
      if (parent) {
        parent.children = parent.children || [];
        parent.children.push(labelWithChildren);
      }
    } else {
      rootLabels.push(labelWithChildren);
    }
  });

  return rootLabels;
};

export const flattenLabelHierarchy = (labels: Label[]): Label[] => {
  const result: Label[] = [];
  
  const flatten = (labelList: Label[]) => {
    labelList.forEach(label => {
      result.push(label);
      if (label.children && label.children.length > 0) {
        flatten(label.children);
      }
    });
  };
  
  flatten(labels);
  return result;
};

// File utilities
export const getFileIcon = (fileType: string): string => {
  switch (fileType.toLowerCase()) {
    case 'pdf':
      return '📄';
    case 'docx':
    case 'doc':
      return '📝';
    case 'txt':
      return '📋';
    default:
      return '📄';
  }
};

export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 Bytes';
  
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

// Date utilities
export const formatDate = (dateString?: string): string => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  return date.toLocaleString();
};

export const formatRelativeTime = (dateString?: string): string => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const now = new Date();
  const diffInSeconds = Math.floor((now.getTime() - date.getTime()) / 1000);
  
  if (diffInSeconds < 60) return 'Just now';
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)} minutes ago`;
  if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)} hours ago`;
  if (diffInSeconds < 604800) return `${Math.floor(diffInSeconds / 86400)} days ago`;
  
  return formatDate(dateString);
};

// Validation utilities
export const validateEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export const validateHexColor = (color: string): boolean => {
  const hexRegex = /^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$/;
  return hexRegex.test(color);
};

// Keyboard shortcuts
export const keyboardShortcuts = {
  SAVE: 'Ctrl+S',
  NEW_ANNOTATION: 'Ctrl+N',
  DELETE: 'Delete',
  ESCAPE: 'Escape',
  ENTER: 'Enter',
} as const;
