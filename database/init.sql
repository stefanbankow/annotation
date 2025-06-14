-- Create extension for UUID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Table for storing labels (етикети)
CREATE TABLE labels (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL UNIQUE,
    color VARCHAR(7) NOT NULL, -- HEX color code
    description TEXT,
    parent_id UUID REFERENCES labels(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing label relationships (връзки между етикети)
CREATE TABLE label_relationships (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    source_label_id UUID NOT NULL REFERENCES labels(id) ON DELETE CASCADE,
    target_label_id UUID NOT NULL REFERENCES labels(id) ON DELETE CASCADE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(source_label_id, target_label_id)
);

-- Table for storing documents
CREATE TABLE documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255) NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    file_type VARCHAR(10) NOT NULL, -- txt, docx, pdf
    file_path VARCHAR(500) NOT NULL,
    content TEXT, -- extracted text content
    upload_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    file_size BIGINT
);

-- Table for storing annotations
CREATE TABLE annotations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_id UUID NOT NULL REFERENCES documents(id) ON DELETE CASCADE,
    label_id UUID NOT NULL REFERENCES labels(id) ON DELETE CASCADE,
    start_position INTEGER NOT NULL,
    end_position INTEGER NOT NULL,
    selected_text TEXT NOT NULL,
    context_before TEXT,
    context_after TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Table for storing text documents created in the editor
CREATE TABLE text_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for better performance
CREATE INDEX idx_annotations_document_id ON annotations(document_id);
CREATE INDEX idx_annotations_label_id ON annotations(label_id);
CREATE INDEX idx_labels_parent_id ON labels(parent_id);
CREATE INDEX idx_label_relationships_source ON label_relationships(source_label_id);
CREATE INDEX idx_label_relationships_target ON label_relationships(target_label_id);

-- Insert some default labels
INSERT INTO labels (name, color, description) VALUES 
    ('Person', '#FF6B6B', 'Person names and references'),
    ('Location', '#4ECDC4', 'Places and geographical locations'),
    ('Organization', '#45B7D1', 'Companies, institutions, organizations'),
    ('Date', '#96CEB4', 'Dates and temporal expressions'),
    ('Important', '#FFEAA7', 'Important information'),
    ('Action', '#DDA0DD', 'Actions and activities'),
    ('Concept', '#F8D7DA', 'Key concepts and ideas');

-- Insert some example label relationships
INSERT INTO label_relationships (source_label_id, target_label_id, description) 
SELECT 
    l1.id, 
    l2.id, 
    'Person works at Organization'
FROM labels l1, labels l2 
WHERE l1.name = 'Person' AND l2.name = 'Organization';

INSERT INTO label_relationships (source_label_id, target_label_id, description) 
SELECT 
    l1.id, 
    l2.id, 
    'Organization located in Location'
FROM labels l1, labels l2 
WHERE l1.name = 'Organization' AND l2.name = 'Location';
