import EducationForm from "./EducationForm";
import "./Education.css";

const EducationModal = ({ data, onClose, onSave }) => {
  return (
    <div className="modal-backdrop">
      <div className="modal">
        <EducationForm
          initialData={data}
          onSubmit={onSave}
          onCancel={onClose}
        />
      </div>
    </div>
  );
};

export default EducationModal;
