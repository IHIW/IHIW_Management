package org.ihiw.management.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.ihiw.management.domain.IhiwUser;
import org.ihiw.management.domain.Project;
import org.ihiw.management.domain.Upload;
import org.ihiw.management.domain.Validation;
import org.ihiw.management.domain.enumeration.FileType;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class UploadDTO {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileType type;

    private ZonedDateTime createdAt;
    private ZonedDateTime modifiedAt;
    private String fileName;
    private Boolean enabled;
    private String labCode;

    @ManyToOne
    @JsonIgnoreProperties("uploads")
    private IhiwUser createdBy;

    @Transient
    @JsonProperty
    private String rawDownload;

    @Transient
    @JsonProperty
    private Set<Validation> validations;

    @Transient
    @JsonProperty
    private Project project;

    @Transient
    @JsonProperty
    private Upload parentUpload;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileType getType() {
        return type;
    }

    public UploadDTO type(FileType type) {
        this.type = type;
        return this;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public UploadDTO createdAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getModifiedAt() {
        return modifiedAt;
    }

    public UploadDTO modifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
        return this;
    }

    public void setModifiedAt(ZonedDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public UploadDTO fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public UploadDTO enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public IhiwUser getCreatedBy() {
        return createdBy;
    }

    public UploadDTO createdBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
        return this;
    }

    public void setCreatedBy(IhiwUser ihiwUser) {
        this.createdBy = ihiwUser;
    }

    public String getRawDownload() {
        return rawDownload;
    }

    public void setRawDownload(String rawDownload) {
        this.rawDownload = rawDownload;
    }
    
    public String getLabCode() {
        return labCode;
    }

    public void setLabCode(String labCode) {
        this.labCode = labCode;
    }

    public Set<Validation> getValidations() {
        return validations;
    }

    public void setValidations(Set<Validation> validations) {
        this.validations = validations;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Upload getParentUpload() {
        return parentUpload;
    }

    public void setParentUpload(Upload parentUpload) {
        this.parentUpload = parentUpload;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Upload)) {
            return false;
        }
        return id != null && id.equals(((UploadDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Upload{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }

    public UploadDTO() {
        // Empty constructor needed for Jackson.
    }

    public UploadDTO(Upload upload) {
        this.id = upload.getId();
        this.type = upload.getType();
        this.createdAt = upload.getCreatedAt();
        this.modifiedAt = upload.getModifiedAt();
        this.fileName = upload.getFileName();
        this.enabled = upload.isEnabled();
        this.createdBy = upload.getCreatedBy();
        this.rawDownload = upload.getRawDownload();
        this.validations = upload.getValidations();
        this.project = upload.getProject();
        this.parentUpload = upload.getParentUpload();
        
    }


}
