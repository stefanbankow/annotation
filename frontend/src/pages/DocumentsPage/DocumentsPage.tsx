import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { apiService } from '../../services/api';
import { Document } from '../../types';
import {
  PageContainer,
  PageHeader,
  Title,
  UploadSection,
  DocumentGrid,
  DocumentCard,
  DocumentIcon,
  DocumentInfo,
  DocumentName,
  DocumentMeta,
  DocumentActions,
  ActionButton,
  UploadArea,
  UploadText,
  FileInput,
  LoadingState,
  ErrorState,
} from './DocumentsPage.styles';

const DocumentsPage: React.FC = () => {
  const [documents, setDocuments] = useState<Document[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [uploading, setUploading] = useState(false);

  useEffect(() => {
    loadDocuments();
  }, []);

  const loadDocuments = async () => {
    try {
      setLoading(true);
      const docs = await apiService.getDocuments();
      setDocuments(docs);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load documents');
    } finally {
      setLoading(false);
    }
  };

  const handleFileUpload = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    // Validate file type
    const allowedTypes = ['.txt', '.docx', '.pdf'];
    const fileExtension = '.' + file.name.split('.').pop()?.toLowerCase();
    if (!allowedTypes.includes(fileExtension)) {
      setError('Моля изберете файл от тип .txt, .docx или .pdf');
      return;
    }

    try {
      setUploading(true);
      setError(null);
      const uploadedDoc = await apiService.uploadDocument(file);
      setDocuments(prev => [uploadedDoc, ...prev]);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to upload document');
    } finally {
      setUploading(false);
      // Reset file input
      event.target.value = '';
    }
  };

  const handleDeleteDocument = async (id: string) => {
    if (!window.confirm('Сигурни ли сте, че искате да изтриете този документ?')) {
      return;
    }

    try {
      await apiService.deleteDocument(id);
      setDocuments(prev => prev.filter(doc => doc.id !== id));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete document');
    }
  };

  const getDocumentIcon = (fileType: string) => {
    switch (fileType.toLowerCase()) {
      case 'txt':
        return '📄';
      case 'docx':
        return '📝';
      case 'pdf':
        return '📕';
      default:
        return '📄';
    }
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

  if (loading) {
    return (
      <PageContainer>
        <LoadingState>Зареждане на документи...</LoadingState>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <Title>Документи</Title>
      </PageHeader>

      <UploadSection>
        <UploadArea $uploading={uploading}>
          <FileInput
            type="file"
            accept=".txt,.docx,.pdf"
            onChange={handleFileUpload}
            disabled={uploading}
          />
          <UploadText>
            {uploading ? 'Качване...' : 'Кликнете или плъзнете файл за качване'}
            <br />
            <small>Поддържани формати: .txt, .docx, .pdf</small>
          </UploadText>
        </UploadArea>
      </UploadSection>

      {error && <ErrorState>{error}</ErrorState>}

      <DocumentGrid>
        {documents.map((document) => (
          <DocumentCard key={document.id}>
            <DocumentIcon>{getDocumentIcon(document.fileType)}</DocumentIcon>
            <DocumentInfo>
              <DocumentName>{document.name}</DocumentName>
              <DocumentMeta>
                <div>Размер: {formatFileSize(document.fileSize)}</div>
                <div>Качен: {document.uploadDate ? formatDate(document.uploadDate) : 'N/A'}</div>
                <div>Анотации: {document.annotationCount}</div>
              </DocumentMeta>
            </DocumentInfo>
            <DocumentActions>
              <ActionButton as={Link} to={`/documents/${document.id}/annotate`}>
                Анотиране
              </ActionButton>
              <ActionButton 
                $variant="danger" 
                onClick={() => handleDeleteDocument(document.id)}
              >
                Изтриване
              </ActionButton>
            </DocumentActions>
          </DocumentCard>
        ))}
      </DocumentGrid>

      {documents.length === 0 && !loading && (
        <div style={{ textAlign: 'center', padding: '2rem', color: '#6b7280' }}>
          Няма качени документи. Качете първия си документ за анотиране.
        </div>
      )}
    </PageContainer>
  );
};

export default DocumentsPage;
