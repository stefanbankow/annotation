import React, { useState, useEffect } from 'react';
import { apiService } from '../../services/api';
import { Label, CreateLabelRequest, UpdateLabelRequest } from '../../types';
import LabelForm from '../../components/LabelForm/LabelForm';
import LabelTree from '../../components/LabelTree/LabelTree';
import {
  PageContainer,
  PageHeader,
  Title,
  ContentContainer,
  LeftPanel,
  RightPanel,
  AddButton,
  LoadingState,
  ErrorState,
} from './LabelsPage.styles';

const LabelsPage: React.FC = () => {
  const [labels, setLabels] = useState<Label[]>([]);
  const [selectedLabel, setSelectedLabel] = useState<Label | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    loadLabels();
  }, []);

  const loadLabels = async () => {
    try {
      setLoading(true);
      const labelData = await apiService.getLabels();
      setLabels(labelData);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load labels');
    } finally {
      setLoading(false);
    }
  };

  const handleCreateLabel = async (labelData: CreateLabelRequest) => {
    try {
      setError(null);
      const newLabel = await apiService.createLabel(labelData);
      setLabels(prev => [...prev, newLabel]);
      setShowForm(false);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to create label');
    }
  };

  const handleUpdateLabel = async (id: string, labelData: UpdateLabelRequest) => {
    try {
      setError(null);
      const updatedLabel = await apiService.updateLabel(id, labelData);
      setLabels(prev => prev.map(label => 
        label.id === id ? updatedLabel : label
      ));
      setSelectedLabel(updatedLabel);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update label');
    }
  };

  const handleDeleteLabel = async (id: string) => {
    if (!window.confirm('Сигурни ли сте, че искате да изтриете този етикет?')) {
      return;
    }

    try {
      setError(null);
      await apiService.deleteLabel(id);
      setLabels(prev => prev.filter(label => label.id !== id));
      if (selectedLabel?.id === id) {
        setSelectedLabel(null);
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete label');
    }
  };

  const handleLabelSelect = (label: Label) => {
    setSelectedLabel(label);
    setShowForm(false);
  };

  const handleAddNew = () => {
    setSelectedLabel(null);
    setShowForm(true);
  };

  const handleEdit = (label: Label) => {
    setSelectedLabel(label);
    setShowForm(true);
  };

  const handleCancelForm = () => {
    setShowForm(false);
  };

  if (loading) {
    return (
      <PageContainer>
        <LoadingState>Зареждане на етикети...</LoadingState>
      </PageContainer>
    );
  }

  return (
    <PageContainer>
      <PageHeader>
        <Title>Управление на етикети</Title>
        <AddButton onClick={handleAddNew}>
          ➕ Добави етикет
        </AddButton>
      </PageHeader>

      {error && <ErrorState>{error}</ErrorState>}

      <ContentContainer>
        <LeftPanel>
          <LabelTree
            labels={labels}
            selectedLabel={selectedLabel}
            onLabelSelect={handleLabelSelect}
            onLabelEdit={handleEdit}
            onLabelDelete={handleDeleteLabel}
          />
        </LeftPanel>

        <RightPanel>
          {showForm ? (
            <LabelForm
              existingLabel={selectedLabel}
              labels={labels}
              onSubmit={selectedLabel ? 
                (data: UpdateLabelRequest) => handleUpdateLabel(selectedLabel.id, data) :
                handleCreateLabel
              }
              onCancel={handleCancelForm}
            />
          ) : selectedLabel ? (
            <div>
              <h3>Детайли за етикет</h3>
              <p><strong>Име:</strong> {selectedLabel.name}</p>
              <p><strong>Цвят:</strong> 
                <span 
                  style={{ 
                    backgroundColor: selectedLabel.color, 
                    padding: '2px 8px', 
                    marginLeft: '8px',
                    borderRadius: '4px',
                    color: 'white'
                  }}
                >
                  {selectedLabel.color}
                </span>
              </p>
              {selectedLabel.description && (
                <p><strong>Описание:</strong> {selectedLabel.description}</p>
              )}
              <p><strong>Брой анотации:</strong> {selectedLabel.annotationCount}</p>
              {selectedLabel.parentName && (
                <p><strong>Родителски етикет:</strong> {selectedLabel.parentName}</p>
              )}
              {selectedLabel.children && selectedLabel.children.length > 0 && (
                <p><strong>Дъщерни етикети:</strong> {selectedLabel.children.length}</p>
              )}
            </div>
          ) : (
            <div style={{ textAlign: 'center', padding: '2rem', color: '#6b7280' }}>
              Изберете етикет за преглед или създайте нов
            </div>
          )}
        </RightPanel>
      </ContentContainer>
    </PageContainer>
  );
};

export default LabelsPage;
