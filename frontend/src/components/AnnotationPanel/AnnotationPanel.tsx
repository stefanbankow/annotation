import React, { useState } from 'react';
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
  const [showForm, setShowForm] = useState(false);
  const [selectedLabel, setSelectedLabel] = useState('');
  const [startPos, setStartPos] = useState(0);
  const [endPos, setEndPos] = useState(0);
  const [comment, setComment] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedLabel || startPos >= endPos) return;

    const annotationData: CreateAnnotationRequest = {
      documentId: document.id,
      labelId: selectedLabel,
      startPosition: startPos,
      endPosition: endPos,
      comment: comment || undefined,
    };

    onCreateAnnotation(annotationData);
    setShowForm(false);
    setSelectedLabel('');
    setStartPos(0);
    setEndPos(0);
    setComment('');
  };

  return (
    <div style={{ padding: '1rem', height: '100%', overflow: 'auto' }}>
      <h3>Анотации</h3>
      
      <button 
        onClick={() => setShowForm(!showForm)}
        style={{
          marginBottom: '1rem',
          padding: '0.5rem 1rem',
          backgroundColor: '#2563eb',
          color: 'white',
          border: 'none',
          borderRadius: '0.25rem',
          cursor: 'pointer'
        }}
      >
        ➕ Добави анотация
      </button>

      {showForm && (
        <form onSubmit={handleSubmit} style={{ marginBottom: '1rem', padding: '1rem', backgroundColor: '#f8f9fa', borderRadius: '0.5rem' }}>
          <div style={{ marginBottom: '1rem' }}>
            <label>Етикет:</label>
            <select 
              value={selectedLabel} 
              onChange={(e) => setSelectedLabel(e.target.value)}
              style={{ width: '100%', padding: '0.5rem', marginTop: '0.25rem' }}
              required
            >
              <option value="">Изберете етикет</option>
              {labels.map(label => (
                <option key={label.id} value={label.id}>{label.name}</option>
              ))}
            </select>
          </div>
          
          <div style={{ marginBottom: '1rem' }}>
            <label>Начална позиция:</label>
            <input 
              type="number" 
              value={startPos}
              onChange={(e) => setStartPos(Number(e.target.value))}
              style={{ width: '100%', padding: '0.5rem', marginTop: '0.25rem' }}
              required
            />
          </div>
          
          <div style={{ marginBottom: '1rem' }}>
            <label>Крайна позиция:</label>
            <input 
              type="number" 
              value={endPos}
              onChange={(e) => setEndPos(Number(e.target.value))}
              style={{ width: '100%', padding: '0.5rem', marginTop: '0.25rem' }}
              required
            />
          </div>
          
          <div style={{ marginBottom: '1rem' }}>
            <label>Коментар:</label>
            <textarea 
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              style={{ width: '100%', padding: '0.5rem', marginTop: '0.25rem' }}
              rows={3}
            />
          </div>
          
          <div>
            <button type="submit" style={{ marginRight: '0.5rem', padding: '0.5rem 1rem', backgroundColor: '#10b981', color: 'white', border: 'none', borderRadius: '0.25rem' }}>
              Създай
            </button>
            <button type="button" onClick={() => setShowForm(false)} style={{ padding: '0.5rem 1rem', backgroundColor: '#6b7280', color: 'white', border: 'none', borderRadius: '0.25rem' }}>
              Отказ
            </button>
          </div>
        </form>
      )}

      <div>
        {annotations.map(annotation => (
          <div 
            key={annotation.id}
            onClick={() => onAnnotationSelect(annotation)}
            style={{
              padding: '1rem',
              margin: '0.5rem 0',
              border: selectedAnnotation?.id === annotation.id ? '2px solid #2563eb' : '1px solid #e5e7eb',
              borderRadius: '0.5rem',
              cursor: 'pointer',
              backgroundColor: selectedAnnotation?.id === annotation.id ? '#eff6ff' : 'white'
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
              >
                🗑️
              </button>
            </div>
            <div style={{ fontWeight: 'bold', marginBottom: '0.25rem' }}>
              "{annotation.selectedText}"
            </div>
            <div style={{ fontSize: '0.875rem', color: '#6b7280' }}>
              Позиция: {annotation.startPosition}-{annotation.endPosition}
            </div>
            {annotation.comment && (
              <div style={{ fontSize: '0.875rem', marginTop: '0.5rem', fontStyle: 'italic' }}>
                {annotation.comment}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default AnnotationPanel;
