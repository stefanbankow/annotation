import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Navigate } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Document, Label, Annotation, CreateAnnotationRequest, UpdateAnnotationRequest } from '../../types';
import DocumentViewer from '../../components/DocumentViewer/DocumentViewer';
import AnnotationPanel from '../../components/AnnotationPanel/AnnotationPanel';
import {
  PageContainer,
  PageHeader,
  Title,
  BackButton,
  ContentContainer,
  ViewerContainer,
  PanelContainer,
  LoadingState,
  ErrorState,
} from './AnnotationPage.styles';

const AnnotationPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [document, setDocument] = useState<Document | null>(null);
  const [labels, setLabels] = useState<Label[]>([]);
  const [annotations, setAnnotations] = useState<Annotation[]>([]);
  const [selectedAnnotation, setSelectedAnnotation] = useState<Annotation | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const loadData = useCallback(async () => {
    if (!id) return;
    
    try {
      setLoading(true);
      const [docData, labelsData, annotationsData] = await Promise.all([
        apiService.getDocumentById(id),
        apiService.getLabels(),
        apiService.getAnnotationsByDocument(id),
      ]);
      
      setDocument(docData);
      setLabels(labelsData);
      setAnnotations(annotationsData);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load data');
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadData();
  }, [loadData]);

  const handleCreateAnnotation = async (annotationData: CreateAnnotationRequest) => {
    try {
      setError(null);
      const newAnnotation = await apiService.createAnnotation(annotationData);
      setAnnotations(prev => [...prev, newAnnotation]);
      setSelectedAnnotation(newAnnotation);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create annotation');
    }
  };

  const handleUpdateAnnotation = async (
    annotationId: string, 
    annotationData: UpdateAnnotationRequest
  ) => {
    try {
      setError(null);
      const updatedAnnotation = await apiService.updateAnnotation(annotationId, annotationData);
      setAnnotations(prev => prev.map(ann => 
        ann.id === annotationId ? updatedAnnotation : ann
      ));
      setSelectedAnnotation(updatedAnnotation);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update annotation');
    }
  };

  const handleDeleteAnnotation = async (annotationId: string) => {
    if (!window.confirm('Сигурни ли сте, че искате да изтриете тази анотация?')) {
      return;
    }

    try {
      setError(null);
      await apiService.deleteAnnotation(annotationId);
      setAnnotations(prev => prev.filter(ann => ann.id !== annotationId));
      if (selectedAnnotation?.id === annotationId) {
        setSelectedAnnotation(null);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete annotation');
    }
  };

  const handleAnnotationSelect = (annotation: Annotation) => {
    setSelectedAnnotation(annotation);
  };

  if (!id) {
    return <Navigate to="/documents" replace />;
  }

  if (loading) {
    return (
      <PageContainer>
        <LoadingState>Зареждане на документ...</LoadingState>
      </PageContainer>
    );
  }

  if (!document) {
    return (
      <PageContainer>
        <ErrorState>Документът не е намерен</ErrorState>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <div>
          <BackButton onClick={() => window.history.back()}>
            ← Назад
          </BackButton>
          <Title>{document.name}</Title>
        </div>
      </PageHeader>

      {error && <ErrorState>{error}</ErrorState>}

      <ContentContainer>
        <ViewerContainer>
          <DocumentViewer
            document={document}
            annotations={annotations}
            labels={labels}
            selectedAnnotation={selectedAnnotation}
            onAnnotationSelect={handleAnnotationSelect}
            onCreateAnnotation={handleCreateAnnotation}
          />
        </ViewerContainer>

        <PanelContainer>
          <AnnotationPanel
            document={document}
            labels={labels}
            annotations={annotations}
            selectedAnnotation={selectedAnnotation}
            onAnnotationSelect={handleAnnotationSelect}
            onCreateAnnotation={handleCreateAnnotation}
            onUpdateAnnotation={handleUpdateAnnotation}
            onDeleteAnnotation={handleDeleteAnnotation}
          />
        </PanelContainer>
      </ContentContainer>
    </PageContainer>
  );
};

export default AnnotationPage;
