import React from 'react';
import { Label } from '../../types';
import {
  TreeContainer,
  TreeTitle,
  LabelItem,
  LabelColor,
  LabelName,
  LabelActions,
  ActionButton,
  ChildrenContainer,
  EmptyState,
} from './LabelTree.styles';

interface LabelTreeProps {
  labels: Label[];
  selectedLabel?: Label | null;
  onLabelSelect: (label: Label) => void;
  onLabelEdit: (label: Label) => void;
  onLabelDelete: (id: string) => void;
}

const LabelTree: React.FC<LabelTreeProps> = ({
  labels,
  selectedLabel,
  onLabelSelect,
  onLabelEdit,
  onLabelDelete,
}) => {
  // Build hierarchical structure
  const buildTree = (parentId: string | null = null): Label[] => {
    return labels
      .filter(label => label.parentId === parentId)
      .sort((a, b) => a.name.localeCompare(b.name));
  };

  const renderLabel = (label: Label, level: number = 0): React.ReactNode => {
    const children = buildTree(label.id);
    const isSelected = selectedLabel?.id === label.id;

    return (
      <div key={label.id}>
        <LabelItem 
          $selected={isSelected} 
          $level={level}
          onClick={() => onLabelSelect(label)}
        >
          <div style={{ display: 'flex', alignItems: 'center', flex: 1 }}>
            <LabelColor $color={label.color} />
            <LabelName>
              {label.name}
              {label.annotationCount > 0 && (
                <span style={{ marginLeft: '8px', fontSize: '0.8em', opacity: 0.7 }}>
                  ({label.annotationCount})
                </span>
              )}
            </LabelName>
          </div>
          <LabelActions onClick={(e: React.MouseEvent) => e.stopPropagation()}>
            <ActionButton 
              onClick={() => onLabelEdit(label)}
              title="Редактиране"
            >
              ✏️
            </ActionButton>
            <ActionButton 
              onClick={() => onLabelDelete(label.id)}
              title="Изтриване"
              $variant="danger"
            >
              🗑️
            </ActionButton>
          </LabelActions>
        </LabelItem>
        
        {children.length > 0 && (
          <ChildrenContainer>
            {children.map(child => renderLabel(child, level + 1))}
          </ChildrenContainer>
        )}
      </div>
    );
  };

  const rootLabels = buildTree();

  return (
    <TreeContainer>
      <TreeTitle>Етикети ({labels.length})</TreeTitle>
      
      {rootLabels.length === 0 ? (
        <EmptyState>
          Няма създадени етикети. Създайте първия си етикет.
        </EmptyState>
      ) : (
        rootLabels.map(label => renderLabel(label))
      )}
    </TreeContainer>
  );
};

export default LabelTree;
