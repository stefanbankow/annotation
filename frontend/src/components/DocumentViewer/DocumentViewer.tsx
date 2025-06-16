import React from 'react';
import { Document, Annotation, CreateAnnotationRequest } from '../../types';

interface DocumentViewerProps {
  document: Document;
  annotations: Annotation[];
  selectedAnnotation: Annotation | null;
  onAnnotationSelect: (annotation: Annotation) => void;
  onCreateAnnotation: (annotationData: CreateAnnotationRequest) => void;
}

const DocumentViewer: React.FC<DocumentViewerProps> = ({
  document,
  annotations,
  selectedAnnotation,
  onAnnotationSelect,
  onCreateAnnotation,
}) => {
  return (
    <div style={{ padding: '1rem', height: '100%', overflow: 'auto' }}>
      <h3>Документ: {document.name}</h3>
      <div style={{ 
        whiteSpace: 'pre-wrap', 
        fontFamily: 'monospace',
        backgroundColor: '#f8f9fa',
        padding: '1rem',
        borderRadius: '0.5rem',
        border: '1px solid #e5e7eb',
        minHeight: '400px'
      }}>
        {document.content}
      </div>
      <div style={{ marginTop: '1rem' }}>
        <strong>Анотации: {annotations.length}</strong>
        {annotations.map(annotation => (
          <div 
            key={annotation.id}
            onClick={() => onAnnotationSelect(annotation)}
            style={{
              padding: '0.5rem',
              margin: '0.5rem 0',
              border: selectedAnnotation?.id === annotation.id ? '2px solid #2563eb' : '1px solid #e5e7eb',
              borderRadius: '0.25rem',
              cursor: 'pointer',
              backgroundColor: selectedAnnotation?.id === annotation.id ? '#eff6ff' : 'white'
            }}
          >
            <div style={{ color: annotation.labelColor, fontWeight: 'bold' }}>
              {annotation.labelName}
            </div>
            <div>"{annotation.selectedText}"</div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default DocumentViewer;
