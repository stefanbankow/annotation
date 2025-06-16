import React, { useState, useRef, useEffect } from 'react';
import { Document, Annotation, CreateAnnotationRequest, Label } from '../../types';
import {
  ViewerContainer,
  ViewerHeader,
  DocumentTitle,
  DocumentMeta,
  TextContainer,
  TextContent,
  SelectionToolbar,
  SelectedTextPreview,
  LabelSelector,
  AnnotateButton,
  CancelButton,
  AnnotationsList,
  AnnotationsTitle,
  AnnotationItem,
  AnnotationLabel,
  AnnotationText,
  AnnotationPosition,
} from './DocumentViewer.styles';

interface DocumentViewerProps {
  document: Document;
  annotations: Annotation[];
  labels: Label[];
  selectedAnnotation: Annotation | null;
  onAnnotationSelect: (annotation: Annotation) => void;
  onCreateAnnotation: (annotationData: CreateAnnotationRequest) => void;
}

interface TextSelection {
  text: string;
  startPosition: number;
  endPosition: number;
  rect: DOMRect;
}

const DocumentViewer: React.FC<DocumentViewerProps> = ({
  document,
  annotations,
  labels,
  selectedAnnotation,
  onAnnotationSelect,
  onCreateAnnotation,
}) => {
  const [selection, setSelection] = useState<TextSelection | null>(null);
  const [selectedLabelId, setSelectedLabelId] = useState('');
  const textContentRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleSelectionChange = () => {
      const windowSelection = window.getSelection();
      if (!windowSelection || windowSelection.rangeCount === 0) {
        setSelection(null);
        return;
      }

      const range = windowSelection.getRangeAt(0);
      const selectedText = range.toString().trim();
      
      if (!selectedText || !textContentRef.current) {
        setSelection(null);
        return;
      }

      // Check if selection is within our text content
      const textContainer = textContentRef.current;
      if (!textContainer.contains(range.commonAncestorContainer)) {
        setSelection(null);
        return;
      }

      // Calculate positions relative to the full text content
      const beforeRange = window.document.createRange();
      beforeRange.selectNodeContents(textContainer);
      beforeRange.setEnd(range.startContainer, range.startOffset);
      const startPosition = beforeRange.toString().length;
      const endPosition = startPosition + selectedText.length;

      // Get selection rectangle for toolbar positioning
      const rect = range.getBoundingClientRect();
      const containerRect = textContainer.getBoundingClientRect();
      const scrollTop = textContainer.scrollTop;
      const scrollLeft = textContainer.scrollLeft;
      
      // Calculate position relative to the text container's content area
      const relativeTop = rect.top - containerRect.top + scrollTop;
      const relativeLeft = rect.left - containerRect.left + scrollLeft;
      
      // Simple positioning: center above selection with bounds checking
      const desiredCenterLeft = relativeLeft + (rect.width / 2);
      
      // Popup dimensions (approximate, matches CSS)
      const popupWidth = 280; // min-width from styles
      const popupHalfWidth = popupWidth / 2;
      
      // Container boundaries (account for padding)
      const containerPadding = 32; // 2 * theme.spacing.lg (16px)
      const minLeft = popupHalfWidth + 10; // Half popup width + some padding
      const maxLeft = textContainer.clientWidth - containerPadding - popupHalfWidth - 10;
      
      // Constrain the position to stay within bounds
      const constrainedLeft = Math.max(minLeft, Math.min(desiredCenterLeft, maxLeft));
      const topPosition = relativeTop - 50; // Position above with some padding
      
      setSelection({
        text: selectedText,
        startPosition,
        endPosition,
        rect: new DOMRect(
          constrainedLeft,
          topPosition,
          rect.width,
          rect.height
        ),
      });
    };

    window.document.addEventListener('selectionchange', handleSelectionChange);
    return () => {
      window.document.removeEventListener('selectionchange', handleSelectionChange);
    };
  }, []);

  const handleCreateAnnotation = () => {
    if (!selection || !selectedLabelId) return;

    const annotationData: CreateAnnotationRequest = {
      documentId: document.id,
      labelId: selectedLabelId,
      startPosition: selection.startPosition,
      endPosition: selection.endPosition,
    };

    onCreateAnnotation(annotationData);
    setSelection(null);
    setSelectedLabelId('');
    
    // Clear text selection
    window.getSelection()?.removeAllRanges();
  };

  const handleCancelSelection = () => {
    setSelection(null);
    setSelectedLabelId('');
    window.getSelection()?.removeAllRanges();
  };

  const renderHighlightedText = () => {
    const textContent = document.content;
    if (!textContent || annotations.length === 0) {
      return textContent;
    }

    // Sort annotations by start position
    const sortedAnnotations = [...annotations].sort((a, b) => a.startPosition - b.startPosition);
    
    const parts: React.ReactNode[] = [];
    let lastIndex = 0;

    sortedAnnotations.forEach((annotation, index) => {
      // Add text before annotation
      if (annotation.startPosition > lastIndex) {
        parts.push(
          <span key={`text-${index}`}>
            {textContent.slice(lastIndex, annotation.startPosition)}
          </span>
        );
      }

      // Add highlighted annotation
      const isSelected = selectedAnnotation?.id === annotation.id;
      parts.push(
        <span
          key={`annotation-${annotation.id}`}
          className={`annotation-highlight ${isSelected ? 'selected' : ''}`}
          style={{
            backgroundColor: `${annotation.labelColor}30`,
            borderBottom: `2px solid ${annotation.labelColor}`,
          }}
          onClick={() => onAnnotationSelect(annotation)}
          title={`${annotation.labelName}: ${annotation.selectedText}`}
        >
          {textContent.slice(annotation.startPosition, annotation.endPosition)}
        </span>
      );

      lastIndex = annotation.endPosition;
    });

    // Add remaining text
    if (lastIndex < textContent.length) {
      parts.push(
        <span key="text-end">
          {textContent.slice(lastIndex)}
        </span>
      );
    }

    return parts;
  };

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('bg-BG', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  return (
    <ViewerContainer>
      <ViewerHeader>
        <DocumentTitle>{document.name}</DocumentTitle>
        <DocumentMeta>
          <span>Размер: {formatFileSize(document.fileSize)}</span>
          <span>Тип: {document.fileType.toUpperCase()}</span>
          {document.uploadDate && (
            <span>Качен: {formatDate(document.uploadDate)}</span>
          )}
          <span>Анотации: {annotations.length}</span>
        </DocumentMeta>
      </ViewerHeader>

      <TextContainer>
        <TextContent ref={textContentRef}>
          {renderHighlightedText()}
        </TextContent>

        {selection && (
          <SelectionToolbar
            $visible={true}
            $top={selection.rect.top}
            $left={selection.rect.left}
          >
            <SelectedTextPreview>
              "{selection.text.length > 20 ? selection.text.slice(0, 20) + '...' : selection.text}"
            </SelectedTextPreview>
            <LabelSelector
              value={selectedLabelId}
              onChange={(e) => setSelectedLabelId(e.target.value)}
            >
              <option value="">Изберете етикет</option>
              {labels.map((label) => (
                <option key={label.id} value={label.id}>
                  {label.name}
                </option>
              ))}
            </LabelSelector>
            <AnnotateButton
              onClick={handleCreateAnnotation}
              disabled={!selectedLabelId}
            >
              Анотирай
            </AnnotateButton>
            <CancelButton onClick={handleCancelSelection}>
              Отказ
            </CancelButton>
          </SelectionToolbar>
        )}

        {annotations.length > 0 && (
          <AnnotationsList>
            <AnnotationsTitle>Анотации ({annotations.length})</AnnotationsTitle>
            {annotations.map((annotation) => (
              <AnnotationItem
                key={annotation.id}
                $selected={selectedAnnotation?.id === annotation.id}
                onClick={() => onAnnotationSelect(annotation)}
              >
                <AnnotationLabel $color={annotation.labelColor}>
                  {annotation.labelName}
                </AnnotationLabel>
                <AnnotationText>"{annotation.selectedText}"</AnnotationText>
                <AnnotationPosition>
                  Позиция: {annotation.startPosition}-{annotation.endPosition}
                </AnnotationPosition>
                {annotation.comment && (
                  <div style={{ fontSize: '0.875rem', marginTop: '0.25rem', fontStyle: 'italic' }}>
                    {annotation.comment}
                  </div>
                )}
              </AnnotationItem>
            ))}
          </AnnotationsList>
        )}
      </TextContainer>
    </ViewerContainer>
  );
};

export default DocumentViewer;
