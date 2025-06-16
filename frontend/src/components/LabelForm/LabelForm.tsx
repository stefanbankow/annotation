import React, { useState, useEffect } from 'react';
import { Label, CreateLabelRequest, UpdateLabelRequest } from '../../types';
import {
  FormContainer,
  FormTitle,
  FormGroup,
  Label as FormLabel,
  Input,
  Textarea,
  Select,
  ColorInput,
  ColorPreview,
  ButtonGroup,
  SubmitButton,
  CancelButton,
  ErrorMessage,
} from './LabelForm.styles';

interface LabelFormProps {
  existingLabel?: Label | null;
  labels: Label[];
  onSubmit: (data: CreateLabelRequest | UpdateLabelRequest) => void;
  onCancel: () => void;
}

const LabelForm: React.FC<LabelFormProps> = ({
  existingLabel,
  labels,
  onSubmit,
  onCancel,
}) => {
  const [name, setName] = useState('');
  const [color, setColor] = useState('#2563eb');
  const [description, setDescription] = useState('');
  const [parentId, setParentId] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    if (existingLabel) {
      setName(existingLabel.name);
      setColor(existingLabel.color);
      setDescription(existingLabel.description || '');
      setParentId(existingLabel.parentId || '');
    } else {
      setName('');
      setColor('#2563eb');
      setDescription('');
      setParentId('');
    }
  }, [existingLabel]);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setError('');

    if (!name.trim()) {
      setError('Името е задължително');
      return;
    }

    // Check for duplicate name (excluding current label if editing)
    const duplicate = labels.find(label => 
      label.name.toLowerCase() === name.trim().toLowerCase() &&
      label.id !== existingLabel?.id
    );
    
    if (duplicate) {
      setError('Етикет с това име вече съществува');
      return;
    }

    const data: CreateLabelRequest | UpdateLabelRequest = {
      name: name.trim(),
      color,
      description: description.trim() || undefined,
      parentId: parentId || undefined,
    };

    onSubmit(data);
  };

  // Filter available parent labels (exclude self and children)
  const availableParents = labels.filter(label => {
    if (!existingLabel) return true;
    
    // Exclude self
    if (label.id === existingLabel.id) return false;
    
    // Exclude children to prevent circular relationships
    const isChild = (childLabel: Label): boolean => {
      if (childLabel.parentId === existingLabel.id) return true;
      const parent = labels.find(l => l.id === childLabel.parentId);
      return parent ? isChild(parent) : false;
    };
    
    return !isChild(label);
  });

  return (
    <FormContainer>
      <FormTitle>
        {existingLabel ? 'Редактиране на етикет' : 'Създаване на нов етикет'}
      </FormTitle>
      
      <form onSubmit={handleSubmit}>
        <FormGroup>
          <FormLabel htmlFor="name">Име *</FormLabel>
          <Input
            id="name"
            type="text"
            value={name}
            onChange={(e: React.ChangeEvent<HTMLInputElement>) => setName(e.target.value)}
            placeholder="Въведете име на етикета"
            required
          />
        </FormGroup>

        <FormGroup>
          <FormLabel htmlFor="color">Цвят</FormLabel>
          <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
            <ColorInput
              id="color"
              type="color"
              value={color}
              onChange={(e: React.ChangeEvent<HTMLInputElement>) => setColor(e.target.value)}
            />
            <ColorPreview $color={color}>
              {color}
            </ColorPreview>
          </div>
        </FormGroup>

        <FormGroup>
          <FormLabel htmlFor="parent">Родителски етикет</FormLabel>
          <Select
            id="parent"
            value={parentId}
            onChange={(e: React.ChangeEvent<HTMLSelectElement>) => setParentId(e.target.value)}
          >
            <option value="">Няма родителски етикет</option>
            {availableParents.map((label) => (
              <option key={label.id} value={label.id}>
                {label.name}
              </option>
            ))}
          </Select>
        </FormGroup>

        <FormGroup>
          <FormLabel htmlFor="description">Описание</FormLabel>
          <Textarea
            id="description"
            value={description}
            onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setDescription(e.target.value)}
            placeholder="Опционално описание на етикета"
            rows={3}
          />
        </FormGroup>

        {error && <ErrorMessage>{error}</ErrorMessage>}

        <ButtonGroup>
          <SubmitButton type="submit">
            {existingLabel ? 'Запази промените' : 'Създай етикет'}
          </SubmitButton>
          <CancelButton type="button" onClick={onCancel}>
            Отказ
          </CancelButton>
        </ButtonGroup>
      </form>
    </FormContainer>
  );
};

export default LabelForm;
