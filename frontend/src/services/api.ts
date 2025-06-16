import axios, { AxiosInstance, AxiosResponse } from 'axios';
import {
  Label,
  CreateLabelRequest,
  UpdateLabelRequest,
  Document,
  Annotation,
  CreateAnnotationRequest,
  UpdateAnnotationRequest,
  LabelRelationship,
  CreateLabelRelationshipRequest,
  UpdateLabelRelationshipRequest,
  TextDocument,
  CreateTextDocumentRequest,
  UpdateTextDocumentRequest,
  Analytics,
  ApiError
} from '../types';

class ApiService {
  private api: AxiosInstance;

  constructor() {
    this.api = axios.create({
      baseURL: process.env.REACT_APP_API_URL || 'http://localhost:8080/api',
      timeout: 30000,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Response interceptor for error handling
    this.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.data) {
          const apiError = error.response.data as ApiError;
          return Promise.reject(new Error(apiError.message || 'API Error'));
        }
        return Promise.reject(new Error('Network error occurred'));
      }
    );
  }

  // Labels API
  async getLabels(): Promise<Label[]> {
    const response: AxiosResponse<Label[]> = await this.api.get('/labels');
    return response.data;
  }

  async getLabelById(id: string): Promise<Label> {
    const response: AxiosResponse<Label> = await this.api.get(`/labels/${id}`);
    return response.data;
  }

  async createLabel(labelData: CreateLabelRequest): Promise<Label> {
    const response: AxiosResponse<Label> = await this.api.post('/labels', labelData);
    return response.data;
  }

  async updateLabel(id: string, labelData: UpdateLabelRequest): Promise<Label> {
    const response: AxiosResponse<Label> = await this.api.put(`/labels/${id}`, labelData);
    return response.data;
  }

  async deleteLabel(id: string): Promise<void> {
    await this.api.delete(`/labels/${id}`);
  }

  // Documents API
  async getDocuments(): Promise<Document[]> {
    const response: AxiosResponse<Document[]> = await this.api.get('/documents');
    return response.data;
  }

  async getDocumentById(id: string): Promise<Document> {
    const response: AxiosResponse<Document> = await this.api.get(`/documents/${id}`);
    return response.data;
  }

  async uploadDocument(file: File, title?: string, description?: string): Promise<Document> {
    const formData = new FormData();
    formData.append('file', file);
    if (title) formData.append('title', title);
    if (description) formData.append('description', description);

    const response: AxiosResponse<Document> = await this.api.post('/documents/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    return response.data;
  }

  async deleteDocument(id: string): Promise<void> {
    await this.api.delete(`/documents/${id}`);
  }

  // Annotations API
  async getAnnotations(): Promise<Annotation[]> {
    const response: AxiosResponse<Annotation[]> = await this.api.get('/annotations');
    return response.data;
  }

  async getAnnotationById(id: string): Promise<Annotation> {
    const response: AxiosResponse<Annotation> = await this.api.get(`/annotations/${id}`);
    return response.data;
  }

  async getAnnotationsByDocument(documentId: string): Promise<Annotation[]> {
    const response: AxiosResponse<Annotation[]> = await this.api.get(`/annotations/document/${documentId}`);
    return response.data;
  }

  async createAnnotation(annotationData: CreateAnnotationRequest): Promise<Annotation> {
    const response: AxiosResponse<Annotation> = await this.api.post('/annotations', annotationData);
    return response.data;
  }

  async updateAnnotation(id: string, annotationData: UpdateAnnotationRequest): Promise<Annotation> {
    const response: AxiosResponse<Annotation> = await this.api.put(`/annotations/${id}`, annotationData);
    return response.data;
  }

  async deleteAnnotation(id: string): Promise<void> {
    await this.api.delete(`/annotations/${id}`);
  }

  // Label Relationships API
  async getLabelRelationships(): Promise<LabelRelationship[]> {
    const response: AxiosResponse<LabelRelationship[]> = await this.api.get('/label-relationships');
    return response.data;
  }

  async getLabelRelationshipById(id: string): Promise<LabelRelationship> {
    const response: AxiosResponse<LabelRelationship> = await this.api.get(`/label-relationships/${id}`);
    return response.data;
  }

  async createLabelRelationship(relationshipData: CreateLabelRelationshipRequest): Promise<LabelRelationship> {
    const response: AxiosResponse<LabelRelationship> = await this.api.post('/label-relationships', relationshipData);
    return response.data;
  }

  async updateLabelRelationship(id: string, relationshipData: UpdateLabelRelationshipRequest): Promise<LabelRelationship> {
    const response: AxiosResponse<LabelRelationship> = await this.api.put(`/label-relationships/${id}`, relationshipData);
    return response.data;
  }

  async deleteLabelRelationship(id: string): Promise<void> {
    await this.api.delete(`/label-relationships/${id}`);
  }

  // Text Documents API
  async getTextDocuments(): Promise<TextDocument[]> {
    const response: AxiosResponse<TextDocument[]> = await this.api.get('/text-documents');
    return response.data;
  }

  async getTextDocumentById(id: string): Promise<TextDocument> {
    const response: AxiosResponse<TextDocument> = await this.api.get(`/text-documents/${id}`);
    return response.data;
  }

  async createTextDocument(textDocumentData: CreateTextDocumentRequest): Promise<TextDocument> {
    const response: AxiosResponse<TextDocument> = await this.api.post('/text-documents', textDocumentData);
    return response.data;
  }

  async updateTextDocument(id: string, textDocumentData: UpdateTextDocumentRequest): Promise<TextDocument> {
    const response: AxiosResponse<TextDocument> = await this.api.put(`/text-documents/${id}`, textDocumentData);
    return response.data;
  }

  async deleteTextDocument(id: string): Promise<void> {
    await this.api.delete(`/text-documents/${id}`);
  }

  // Analytics API
  async getAnalytics(): Promise<Analytics> {
    const response: AxiosResponse<Analytics> = await this.api.get('/analytics');
    return response.data;
  }
}

export const apiService = new ApiService();
export default apiService;
