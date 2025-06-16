import React from 'react';
import { Document, Label, Annotation, CreateAnnotationRequest, UpdateAnnotationRequest } from '../../types';

interface AnnotationPanelProps {
  document: Document;
  labels: Label[];
  annotations: Annotation[];
  selectedAnnotation: Annotation | null;
  onAnnotationSelect: (annotation: Annotation) => void;
  onCreateAnnotation: (annotationData: CreateAnnotationRequest) => void;
  onUpdateAnnotation: (annotationId: string, annotationData: UpdateAnnotationRequest) => void;
  onDeleteAnnotation: (annotationId: string) => void;
}

const AnnotationPanel: React.FC<AnnotationPanelProps> = ({
  document,
  labels,
  annotations,
  selectedAnnotation,
  onAnnotationSelect,
  onCreateAnnotation,
  onUpdateAnnotation,
  onDeleteAnnotation,
}) => {
  return (
    <div style={{ padding: '1rem', height: '100%', overflow: 'auto' }}>
      <h3>Панел за анотации</h3>
      
      <div style={{ 
        padding: '1rem', 
        backgroundColor: '#f8f9fa', 
        borderRadius: '0.5rem',
        marginBottom: '1rem',
        border: '1px solid #e5e7eb'
      }}>
        <h4 style={{ margin: '0 0 0.5rem 0', fontSize: '1rem' }}>Как да анотирате:</h4>
        <ol style={{ margin: 0, paddingLeft: '1.2rem', fontSize: '0.875rem', color: '#6b7280' }}>
          <li>Изберете текст в документа</li>
          <li>Изберете етикет от падащото меню</li>
          <li>Натиснете "Анотирай"</li>
        </ol>
      </div>

      <div>
        <h4 style={{ marginBottom: '1rem' }}>Анотации ({annotations.length})</h4>
        {annotations.length === 0 ? (
          <div style={{ 
            textAlign: 'center', 
            padding: '2rem', 
            color: '#6b7280',
            fontStyle: 'italic'
          }}>
            Няма създадени анотации. Изберете текст в документа за създаване на анотация.
          </div>
        ) : (
          annotations.map(annotation => (
            <div 
              key={annotation.id}
              onClick={() => onAnnotationSelect(annotation)}
              style={{
                padding: '1rem',
                margin: '0.5rem 0',
                border: selectedAnnotation?.id === annotation.id ? '2px solid #2563eb' : '1px solid #e5e7eb',
                borderRadius: '0.5rem',
                cursor: 'pointer',
                backgroundColor: selectedAnnotation?.id === annotation.id ? '#eff6ff' : 'white',
                transition: 'all 0.2s ease'
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.5rem' }}>
                <span style={{ 
                  backgroundColor: annotation.labelColor, 
                  color: 'white', 
                  padding: '0.25rem 0.5rem', 
                  borderRadius: '0.25rem',
                  fontSize: '0.875rem',
                  fontWeight: 'bold'
                }}>
                  {annotation.labelName}
                </span>
                <div style={{ display: 'flex', gap: '0.25rem' }}>
                  <button 
                    onClick={(e) => {
                      e.stopPropagation();
                      // TODO: Implement edit functionality
                      console.log('Edit annotation:', annotation.id);
                    }}
                    style={{
                      backgroundColor: '#10b981',
                      color: 'white',
                      border: 'none',
                      borderRadius: '0.25rem',
                      padding: '0.25rem 0.5rem',
                      cursor: 'pointer',
                      fontSize: '0.75rem'
                    }}
                    title="Редактиране"
                  >
                    ✏️
                  </button>
                  <button 
                    onClick={(e) => {
                      e.stopPropagation();
                      onDeleteAnnotation(annotation.id);
                    }}
                    style={{
                      backgroundColor: '#ef4444',
                      color: 'white',
                      border: 'none',
                      borderRadius: '0.25rem',
                      padding: '0.25rem 0.5rem',
                      cursor: 'pointer',
                      fontSize: '0.75rem'
                    }}
                    title="Изтриване"
                  >
                    🗑️
                  </button>
                </div>
              </div>
              <div style={{ fontWeight: 'bold', marginBottom: '0.25rem' }}>
                "{annotation.selectedText}"
              </div>
              <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
                Позиция: {annotation.startPosition}-{annotation.endPosition}
              </div>
              {annotation.comment && (
                <div style={{ fontSize: '0.875rem', marginTop: '0.5rem', fontStyle: 'italic', color: '#6b7280' }}>
                  💬 {annotation.comment}
                </div>
              )}
              {annotation.createdAt && (
                <div style={{ fontSize: '0.75rem', color: '#9ca3af', marginTop: '0.5rem' }}>
                  Създадена: {new Date(annotation.createdAt).toLocaleDateString('bg-BG')}
                </div>
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default AnnotationPanel;
