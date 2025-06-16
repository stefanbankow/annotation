// TypeScript types based on backend DTOs

export interface Label {
  id: string;
  name: string;
  color: string;
  description?: string;
  parentId?: string;
  parentName?: string;
  children?: Label[];
  outgoingRelationships?: LabelRelationship[];
  incomingRelationships?: LabelRelationship[];
  annotationCount: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateLabelRequest {
  name: string;
  color: string;
  description?: string;
  parentId?: string;
}

export interface UpdateLabelRequest {
  name: string;
  color: string;
  description?: string;
  parentId?: string;
}

export interface Document {
  id: string;
  name: string;
  originalFilename: string;
  fileType: string;
  content: string;
  fileSize: number;
  uploadDate?: string;
  annotations?: Annotation[];
  annotationCount: number;
}

export interface DocumentUploadRequest {
  file: File;
  title?: string;
  description?: string;
}

export interface Annotation {
  id: string;
  documentId: string;
  documentName: string;
  labelId: string;
  labelName: string;
  labelColor: string;
  startPosition: number;
  endPosition: number;
  selectedText: string;
  contextBefore: string;
  contextAfter: string;
  comment?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateAnnotationRequest {
  documentId: string;
  labelId: string;
  startPosition: number;
  endPosition: number;
  comment?: string;
}

export interface UpdateAnnotationRequest {
  documentId: string;
  labelId: string;
  startPosition: number;
  endPosition: number;
  comment?: string;
}

export interface LabelRelationship {
  id: string;
  sourceLabelId: string;
  sourceLabelName: string;
  sourceLabelColor: string;
  targetLabelId: string;
  targetLabelName: string;
  targetLabelColor: string;
  relationshipType?: string;
  description?: string;
  createdAt?: string;
}

export interface CreateLabelRelationshipRequest {
  sourceLabelId: string;
  targetLabelId: string;
  relationshipType?: string;
  description?: string;
}

export interface UpdateLabelRelationshipRequest {
  sourceLabelId: string;
  targetLabelId: string;
  relationshipType?: string;
  description?: string;
}

export interface TextDocument {
  id: string;
  title: string;
  content: string;
  description?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateTextDocumentRequest {
  title: string;
  content: string;
  description?: string;
}

export interface UpdateTextDocumentRequest {
  title: string;
  content: string;
  description?: string;
}

export interface Analytics {
  totalDocuments: number;
  totalAnnotations: number;
  totalLabels: number;
  totalLabelRelationships: number;
  mostUsedLabels: Array<{
    label: Label;
    usageCount: number;
  }>;
  labelDistribution: Array<{
    labelName: string;
    count: number;
    color: string;
  }>;
  annotationTrends: Array<{
    date: string;
    count: number;
  }>;
  recentActivity: Array<{
    type: string;
    entityId: string;
    entityName: string;
    timestamp: string;
  }>;
}

// UI-specific types
export interface AnnotationSelection {
  startPosition: number;
  endPosition: number;
  selectedText: string;
}

export interface ColorOption {
  value: string;
  label: string;
  color: string;
}

// API Response types
export interface ApiResponse<T> {
  data: T;
  message?: string;
  status: number;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}
